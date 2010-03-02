// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.jsonc.jackson;

import com.google.api.data.client.v2.DateTime;
import com.google.api.data.client.v2.ClassInfo;
import com.google.api.data.client.v2.jsonc.JsoncObject;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Jackson {

  static final Logger LOGGER = Logger.getLogger(Jackson.class.getName());

  // TODO: investigate an alternative JSON parser, or slimmer Jackson?
  // or abstract out the JSON parser?

  // TODO: remove the feature to allow unquoted control chars when tab
  // escaping is fixed?

  static final JsonFactory JSON_FACTORY =
      new JsonFactory().configure(
          JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).configure(
          JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

  // TODO: turn off INTERN_FIELD_NAMES???

  public static <T> T parsePartialItem(JsonParser parser, Class<T> itemClass)
      throws IOException {
    skipToKey(parser, "entry");
    parser.nextToken();
    return parseItem(parser, itemClass);
  }

  public static <T, I> JsoncFeedParser<T, I> usePartialFeedParser(
      JsonParser parser, Class<T> feedClass, Class<I> itemClass)
      throws IOException {
    skipToKey(parser, "feed");
    return useFeedParser(parser, feedClass, itemClass);
  }

  public static <T, I> JsoncMultiKindFeedParser<T> usePartialMultiKindFeedParser(
      JsonParser parser, Class<T> feedClass, Class<?>... itemClasses)
      throws IOException {
    skipToKey(parser, "feed");
    return useMultiKindFeedParser(parser, feedClass, itemClasses);
  }

  public static <T> T parseItem(JsonParser parser, Class<T> itemClass)
      throws IOException {
    T newInstance = ClassInfo.newInstance(itemClass);
    parseAndClose(parser, newInstance);
    return newInstance;
  }

  public static <T, I> JsoncFeedParser<T, I> useFeedParser(JsonParser parser,
      Class<T> feedClass, Class<I> itemClass) {
    return new JsoncFeedParser<T, I>(parser, feedClass, itemClass);
  }

  public static <T, I> JsoncMultiKindFeedParser<T> useMultiKindFeedParser(
      JsonParser parser, Class<T> feedClass, Class<?>... itemClasses) {
    return new JsoncMultiKindFeedParser<T>(parser, feedClass, itemClasses);
  }

  @SuppressWarnings("unchecked")
  public static <T> T clone(T item) {
    if (item == null) {
      return null;
    }
    Class<? extends T> itemClass = (Class<? extends T>) item.getClass();
    // TODO: support enum for JSON-C string value?
    // TODO: support Java arrays?
    // don't need to clone immutable types
    if (ClassInfo.isImmutable(itemClass)) {
      return item;
    }
    // TODO: handle array?
    if (List.class.isAssignableFrom(itemClass)) {
      List<Object> itemList = (List<Object>) item;
      int size = itemList.size();
      List<Object> result =
          (List<Object>) ClassInfo.newListInstance(itemClass, size);
      for (int i = 0; i < size; i++) {
        Object value = itemList.get(i);
        result.add(clone(value));
      }
      return (T) result;
    }
    if (Map.class.isAssignableFrom(itemClass)) {
      Map<String, Object> itemMap = (Map<String, Object>) item;
      Map<String, Object> result =
          (Map<String, Object>) ClassInfo.newMapInstance(itemClass);
      for (Map.Entry<String, Object> entry : itemMap.entrySet()) {
        itemMap.put(entry.getKey(), clone(entry.getValue()));
      }
      return (T) result;
    }
    // clone basic JSON-C object
    T result = ClassInfo.newInstance(itemClass);
    Field[] fields = itemClass.getFields();
    int numFields = fields.length;
    for (int i = 0; i < numFields; i++) {
      // deep clone of each field
      Field field = fields[i];
      Class<?> fieldClass = field.getType();
      Object thisValue = ClassInfo.getValue(field, item);
      if (thisValue != null && !Modifier.isFinal(field.getModifiers())) {
        ClassInfo.setValue(field, result, clone(thisValue));
      }
    }
    return result;
  }

  static void skipToKey(JsonParser parser, String keyToFind) throws IOException {
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.getCurrentName();
      parser.nextToken();
      if (key == keyToFind) {
        break;
      }
      parser.skipChildren();
    }
  }

  // TODO: make this public?
  static void parseAndClose(JsonParser parser, Object destination)
      throws IOException {
    try {
      parse(parser, destination, null);
    } finally {
      parser.close();
    }
  }

  static <T> T parse(JsonParser parser, Class<T> destinationClass,
      String stopAtKey) throws IOException {
    T newInstance = ClassInfo.newInstance(destinationClass);
    parse(parser, newInstance, stopAtKey);
    return newInstance;
  }

  static void parse(JsonParser parser, Object destination, String stopAtKey)
      throws IOException {
    Class<?> destinationClass = destination.getClass();
    ClassInfo typeInfo = ClassInfo.of(destinationClass);
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.getCurrentName();
      JsonToken curToken = parser.nextToken();
      // stop at items for feeds
      if (key == stopAtKey) {
        return;
      }
      // get the field from the type information
      Field field = typeInfo.getField(key);
      if (field == null) {
        // TODO: handle Map
        if (JsoncObject.class.isAssignableFrom(destinationClass)) {
          JsoncObject object = (JsoncObject) destination;
          object.set(key, parseValue(parser, curToken, null, null));
        } else {
          // skip unrecognized field and its value
          parser.skipChildren();
        }
        continue;
      }
      // skip final fields
      Class<?> fieldClass = field.getType();
      if (Modifier.isFinal(field.getModifiers())) {
        // TODO: check for other kinds of field types?
        if (fieldClass == String.class) {
          Object fieldValue = ClassInfo.getValue(field, destination);
          String actualValue = parser.getText();
          if (fieldClass == String.class && !actualValue.equals(fieldValue)) {
            throw new IllegalArgumentException("expected " + key + " value <"
                + fieldValue + "> but was <" + actualValue + ">");
          }
        }
        parser.skipChildren();
        continue;
      }
      // TODO: "special" values like Double.POSITIVE_INFINITY?
      Object fieldValue = parseValue(parser, curToken, field, fieldClass);
      // TODO: support any subclasses of List and Map?
      ClassInfo.setValue(field, destination, fieldValue);
    }
  }

  private static Object parseValue(JsonParser parser, JsonToken token,
      Field field, Class<?> fieldClass) throws IOException {
    switch (token) {
      case START_ARRAY:
        if (fieldClass == null || List.class.isAssignableFrom(fieldClass)) {
          // TODO: handle array of array
          Class<?> subFieldClass = ClassInfo.getListParameter(field);
          @SuppressWarnings("unchecked")
          List<Object> listValue =
              (List<Object>) ClassInfo.newListInstance(fieldClass, 8);
          JsonToken listToken;
          while ((listToken = parser.nextToken()) != JsonToken.END_ARRAY) {
            listValue.add(parseValue(parser, listToken, null, subFieldClass));
          }
          if (listValue.getClass() == ArrayList.class) {
            ((ArrayList<?>) listValue).trimToSize();
            return listValue;
          }
          @SuppressWarnings("unchecked")
          List<Object> cloneListValue =
              (List<Object>) ClassInfo.newListInstance(fieldClass, listValue
                  .size());
          cloneListValue.addAll(listValue);
          return cloneListValue;
        }
        throw new IllegalArgumentException(parser.getCurrentName()
            + ": expected type that implements List but got " + fieldClass);
      case START_OBJECT:
        if (fieldClass == null || Map.class.isAssignableFrom(fieldClass)) {
          // TODO: handle sub-field type
          @SuppressWarnings("unchecked")
          Map<String, Object> mapValue =
              (Map<String, Object>) ClassInfo.newMapInstance(fieldClass);
          while (parser.nextToken() != JsonToken.END_OBJECT) {
            String mapKey = parser.getCurrentName();
            JsonToken mapToken = parser.nextToken();
            mapValue.put(mapKey, parseValue(parser, mapToken, null, null));
          }
          return mapValue;
        }
        return parse(parser, fieldClass, null);
      case VALUE_TRUE:
      case VALUE_FALSE:
        if (fieldClass != null && fieldClass != Boolean.class
            && fieldClass != boolean.class) {
          throw new IllegalArgumentException(parser.getCurrentName()
              + ": expected type Boolean or boolean but got " + fieldClass);
        }
        return token == JsonToken.VALUE_TRUE ? Boolean.TRUE : Boolean.FALSE;
      case VALUE_NUMBER_FLOAT:
        if (fieldClass != null && fieldClass != Float.class
            && fieldClass != float.class) {
          throw new IllegalArgumentException(parser.getCurrentName()
              + ": expected type Float or float but got " + fieldClass);
        }
        return parser.getFloatValue();
      case VALUE_NUMBER_INT:
        if (fieldClass == null || fieldClass == Integer.class
            || fieldClass == int.class) {
          return parser.getIntValue();
        }
        if (fieldClass == Short.class || fieldClass == short.class) {
          return parser.getShortValue();
        }
        if (fieldClass == Byte.class || fieldClass == byte.class) {
          return parser.getByteValue();
        }
        throw new IllegalArgumentException(parser.getCurrentName()
            + ": expected type Integer/int/Short/short/Byte/byte but got "
            + fieldClass);
      case VALUE_STRING:
        String stringValue = parser.getText();
        if (fieldClass == null || fieldClass == String.class) {
          return stringValue;
        }
        if (fieldClass == Long.class || fieldClass == long.class) {
          return Long.parseLong(stringValue);
        }
        if (fieldClass == Double.class || fieldClass == double.class) {
          return Double.parseDouble(stringValue);
        }
        if (fieldClass == Character.class || fieldClass == char.class) {
          if (stringValue.length() != 1) {
            throw new IllegalArgumentException(parser.getCurrentName()
                + ": expected type Character/char but got " + fieldClass);
          }
          return stringValue.charAt(0);
        }
        if (fieldClass == BigInteger.class) {
          return new BigInteger(stringValue);
        }
        if (fieldClass == BigDecimal.class) {
          return new BigDecimal(stringValue);
        }
        if (fieldClass == DateTime.class) {
          return DateTime.parseRfc3339(stringValue);
        }
        throw new IllegalArgumentException(
            parser.getCurrentName()
                + ": expected type String/Long/long/Double/double/Character/char/BigInteger/BigDecimal/DateTime byte but got "
                + fieldClass);
      case VALUE_NULL:
        return null;
      default:
        throw new IllegalArgumentException(parser.getCurrentName()
            + ": unexpected JSON node type");
    }
  }

}
