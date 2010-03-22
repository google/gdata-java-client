package com.google.api.data.client.v2.request;

import com.google.api.data.client.entity.Entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities to convert to/from untyped entities. Implementation
 * is currently lame but serviceable.
 *
 * Untyped = Entity, List<Untyped Object>, or primitive.
 *
 * Notes on API naming:
 * - fromFoo creates untyped object from a typed object
 * - toFoo creates for converting from an Entity,
 * toFoo is for converting to an Entity.
 *
 * @deprecated use {@link Entity}
 * @author uidude@google.com (Evan Gilbert)
 * @author vbarathan@google.com (Parakash Barathan)
 */
@Deprecated
class Convert {

  /**
   * Create an entity from a JSON string.
   *
   * @param s The string
   * @return The entity
   */
  static ApiEntity fromString(String s) {
    try {
      JSONObject jo = new JSONObject(s);
      return fromJson(jo);
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Create an entity from an Object.
   *
   * Currently only supports public fields, should
   * support more advanced reflection in the future.
   *
   * Also, if you pass in a String it will treat it as
   * as a JSON blob and call fromString();
   *
   * @param o The object
   * @return The entity
   */
  static ApiEntity fromObject(Object o) {
    // If a string, parse as JSON
    if (o instanceof String) {
      return fromString((String)o);
    }

    try {
      ApiEntity e = new ApiEntity();
      // Copy all public fields
      for (Field f : o.getClass().getFields()) {
        if (Modifier.isPublic(f.getModifiers())) {
          Object value = f.get(o);
          if (value != null) {
            e.put(f.getName(), fromAnyObject(value));
          }
        }
      }
      return e;
    } catch (IllegalAccessException iae) {
      throw new RuntimeException("shouldn't happen");
    }
  }

  /**
   * Create an untyped list from typed list.
   *
   * @param list The input list.
   * @return A list of untyped equivalents.
   */
  static List<?> fromTypedList(List<?> list) {
    List<Object> newList = new ArrayList<Object>();
    for (Object item : list) {
      newList.add(fromAnyObject(item));
    }
    return newList;
  }

  /**
   * Create an entity from a JSON object.
   *
   * @param jo The JSON object
   * @return The entity
   */
  static ApiEntity fromJson(JSONObject jo) {
    String[] names = JSONObject.getNames(jo);

    ApiEntity e = new ApiEntity();
    // No properties -> returns null
    if (names ==  null) {
      return e;
    }
    for (String s : names) {
      e.put(s, fromAnyObject(jo.opt(s)));
    }
    return e;
  }

  /**
   * Create an untyped list from a JSONArray.
   *
   * @param ja The JSON array
   * @return The untyped list
   */
  static List<?> fromJsonArray(JSONArray ja) {
    List<Object> list = new ArrayList<Object>();
    for (int i = 0; i < ja.length(); i++) {
      list.add(fromAnyObject(ja.opt(i)));
    }
    return list;
  }

  /**
   * Create an untyped object equivalent from any typed object.
   *
   * @param o The object
   * @return The untyped equivalent
   */
  static Object fromAnyObject(Object o) {
    Class<?> cls = o.getClass();
    if (ApiEntity.isPrimitive(cls)) {
      return o;
    } else if (JSONObject.NULL.equals(o)) {
      return null;
    } else if (o == null) {
      return null;
    } else if (o instanceof JSONObject) {
      return fromJson((JSONObject)o);
    } else if (o instanceof JSONArray) {
      return fromJsonArray((JSONArray)o);
    } else if (o instanceof List<?>) {
      return fromTypedList((List<?>)o);
    } else {
      return fromObject(o);
    }
    // TODO: Map
  }

  /**
   * Turn an entity into a JSON string.
   *
   * Currently always uses pretty printing. We can save bytes later.
   * @param e The entity
   * @return The JSON string
   */
  static String toString(ApiEntity e) {
    try {
      return toJson(e).toString(2);
    } catch (JSONException je) {
      // Have no idea why toString() would throw an exception
      throw new RuntimeException(je);
    }
  }

  /**
   * Turn an untyped object into a strongly typed object.
   *
   * Three types:
   * Object: Uses reflection on fields to set values on an object.
   * Primitive: Sets the primitive directly
   * List: Creates an ArrayList, converts the objects
   * (Map coming)
   *
   * @param o The untyped object
   * @param cls The target class
   * @param type The collection type (NONE|LIST|MAP)
   *
   * @return The typed object
   */
  static Object toObject(Object o, Class<?> cls, CollectionType type) {
    if (o == null) {
      return null;
    }  else if (ApiEntity.isPrimitive(o.getClass())) {
      if (!cls.equals(o.getClass())) {
        if (o.getClass().equals(String.class)) {
          if (cls.equals(Long.class)) {
            return Long.parseLong(o.toString());
          }
        }
        throw new RuntimeException(
            "Can't convert " + o.getClass() + " to " + cls);
      }
      return o;
    } else if (type.equals(CollectionType.LIST)) {
      Object possibleList = o;
      if (o instanceof ApiEntity) {
        possibleList = ((ApiEntity)o).get("list");
      }
      if (possibleList instanceof List<?>) {
        return toTypedList((List<?>)possibleList, cls);
      }
    } else if (o instanceof ApiEntity) {
      return toObject((ApiEntity)o, cls);
    }
    throw new RuntimeException(
        "No conversion for " + o.getClass() + " to " + cls);
  }

  /**
   * Create a typed object from an entity.
   *
   * @param e The entity
   * @param cls The class to convert to
   * @return The object
   */
  static Object toObject(ApiEntity e, Class<?> cls) {
    if (cls.equals(ApiEntity.class)) {
      return e;
    }
    Object o;
    try {
      o = cls.newInstance();
      for (Field f : cls.getFields()) {
        if (Modifier.isPublic(f.getModifiers())) {
          String key = f.getName();
          CollectionType ct = CollectionType.NONE;
          Type t = f.getGenericType();
          Class<?> objectClass = f.getType();
          if (t instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType)t;
            if (pt.getRawType().equals(List.class)) {
              ct = CollectionType.LIST;
              objectClass = (Class<?>)pt.getActualTypeArguments()[0];
            }
          }
          if (e.containsKey(key)) {
            f.set(o, toObject(e.get(key), objectClass, ct));
          }
        }
      }
      return o;
    } catch (InstantiationException iea) {
      throw new RuntimeException("shouldn't happen");
    } catch (IllegalAccessException iae) {
      throw new RuntimeException("shouldn't happen");
    }
  }

  /**
   * Convert an untyped list to a typed list. Creates a new
   * list and then converts all of the members.
   *
   * Doesn't support List<GenericType>
   *
   * @param list The untyped list
   * @param cls Class of items in the list
   * @return The typed list
   */
  static Object toTypedList(List<?> list, Class<?> cls) {
    List<Object> out = new ArrayList<Object>();
    for (Object item : list) {
      out.add(toObject(item, cls, CollectionType.NONE));
    }
    return out;
  }

  /**
   * Convert an entity to a JSON object.
   *
   * @param e The entity
   * @return The JSON object.
   */
  static JSONObject toJson(ApiEntity e) {
    try {
      JSONObject jo = new JSONObject();
      for (String key : e.keySet()) {
          jo.put(key, toJsonType(e.get(key)));
      }
      return jo;
    } catch (JSONException je) {
      // Not sure why this would ever be thrown
      throw new RuntimeException(je);
    }
  }

  /**
   * Convert an untyped list to a JSON array.
   *
   * @param list The untyped list
   * @return The JSON array
   */
  static JSONArray toJsonArray(List<?> list) {
    JSONArray ja = new JSONArray();
    for (Object o : list) {
      ja.put(toJsonType(o));
    }
    return ja;
  }

  /**
   * Convert any untyped object to a JSON object
   * @param o The object
   * @return The JSON object (JSONObject, JSONArray, or primitive)
   */
  static Object toJsonType(Object o) {
    if (o instanceof ApiEntity) {
      return toJson((ApiEntity)o);
    } else if (o instanceof List<?>) {
      return toJsonArray((List<?>)o);
    } else if (o == null || ApiEntity.isPrimitive(o.getClass())) {
      return o;
    } else {
      throw new RuntimeException(
          "Can't convert " + o.getClass() + " to JSON type");
    }
  }

  /**
   * Optional collection type for casting.
   */
  enum CollectionType {
    NONE,
    MAP,
    LIST
  }
  
}

