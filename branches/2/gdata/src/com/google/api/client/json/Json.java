/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.json;

import com.google.api.client.ClassInfo;
import com.google.api.client.DateTime;
import com.google.api.client.Entities;
import com.google.api.client.FieldInfo;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Json {

  // TODO: investigate an alternative JSON parser, or slimmer Jackson?
  // or abstract out the JSON parser?

  // TODO: remove the feature to allow unquoted control chars when tab
  // escaping is fixed?

  // TODO: turn off INTERN_FIELD_NAMES???

  public static final JsonFactory JSON_FACTORY =
      new JsonFactory().configure(
          JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true).configure(
          JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);

  public static final String CONTENT_TYPE = "application/json";

  @SuppressWarnings("unchecked")
  public static <T> T clone(T item) {
    if (item == null) {
      return null;
    }
    Class<? extends T> itemClass = (Class<? extends T>) item.getClass();
    // TODO: support enum for JSON-C string value?
    // TODO: support Java arrays?
    // don't need to clone immutable types
    if (ClassInfo.isPrimitive(itemClass)) {
      return item;
    }
    // TODO: fix me!
    // TODO: handle array?
    if (Collection.class.isAssignableFrom(itemClass)) {
      Collection<Object> itemCollection = (Collection<Object>) item;
      Collection<Object> result =
          (Collection<Object>) ClassInfo.newInstance(itemClass);
      for (Object value : itemCollection) {
        result.add(clone(value));
      }
      return (T) result;
    }
    if (Map.class.isAssignableFrom(itemClass)) {
      Map<String, Object> itemMap = (Map<String, Object>) item;
      Map<String, Object> result = ClassInfo.newMapInstance(itemClass);
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
      Class<?> fieldType = field.getType();
      Object thisValue = FieldInfo.getFieldValue(field, item);
      if (thisValue != null && !Modifier.isFinal(field.getModifiers())) {
        FieldInfo.setFieldValue(field, result, clone(thisValue));
      }
    }
    // TODO: clone JsonObject
    return result;
  }

  public static String toString(Object item) {
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    try {
      JsonGenerator generator =
          Json.JSON_FACTORY.createJsonGenerator(byteStream, JsonEncoding.UTF8);
      try {
        serialize(generator, item);
      } finally {
        generator.close();
      }
    } catch (IOException e) {
      e.printStackTrace(new PrintStream(byteStream));
    }
    return byteStream.toString();
  }

  public static void serialize(JsonGenerator generator, Object value)
      throws IOException {
    if (value == null) {
      generator.writeNull();
    }
    if (value instanceof String || value instanceof Long
        || value instanceof Double || value instanceof BigInteger
        || value instanceof BigDecimal) {
      // TODO: double: what about +- infinity?
      generator.writeString(value.toString());
    } else if (value instanceof Boolean) {
      generator.writeBoolean((Boolean) value);
    } else if (value instanceof Integer || value instanceof Short
        || value instanceof Byte) {
      generator.writeNumber(((Number) value).intValue());
    } else if (value instanceof Float) {
      // TODO: what about +- infinity?
      generator.writeNumber((Float) value);
    } else if (value instanceof DateTime) {
      generator.writeString(((DateTime) value).toStringRfc3339());
    } else if (value instanceof List<?>) {
      generator.writeStartArray();
      @SuppressWarnings("unchecked")
      List<Object> listValue = (List<Object>) value;
      int size = listValue.size();
      for (int i = 0; i < size; i++) {
        serialize(generator, listValue.get(i));
      }
      generator.writeEndArray();
    } else {
      generator.writeStartObject();
      for (Map.Entry<String, Object> entry : Entities.mapOf(value).entrySet()) {
        Object fieldValue = entry.getValue();
        if (fieldValue != null) {
          String fieldName = entry.getKey();
          generator.writeFieldName(fieldName);
          serialize(generator, fieldValue);
        }
      }
      generator.writeEndObject();
    }
  }

  /**
   * Parse a JSON Object from the given JSON parser (which is closed after
   * parsing completes) into the given destination class, optionally using the
   * given parser customizer.
   * 
   * @param <T> destination class type
   * @param parser JSON parser
   * @param destinationClass destination class that has a public default
   *        constructor to use to create a new instance
   * @param customizeParser optional parser customizer or {@code null} for none
   * @return new instance of the parsed destination class
   * @throws IOException I/O exception
   */
  public static <T> T parseAndClose(JsonParser parser,
      Class<T> destinationClass, CustomizeParser customizeParser)
      throws IOException {
    T newInstance = ClassInfo.newInstance(destinationClass);
    parseAndClose(parser, newInstance, customizeParser);
    return newInstance;
  }

  public static void skipToKey(JsonParser parser, String keyToFind)
      throws IOException {
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.getCurrentName();
      parser.nextToken();
      if (key == keyToFind) {
        break;
      }
      parser.skipChildren();
    }
  }

  /**
   * Parse a JSON Object from the given JSON parser (which is closed after
   * parsing completes) into the given destination object, optionally using the
   * given parser customizer.
   * 
   * @param parser JSON parser
   * @param destination destination object
   * @param customizeParser optional parser customizer or {@code null} for none
   * @throws IOException I/O exception
   */
  public static void parseAndClose(JsonParser parser, Object destination,
      CustomizeParser customizeParser) throws IOException {
    try {
      parse(parser, destination, customizeParser);
    } finally {
      parser.close();
    }
  }

  public static class CustomizeParser {

    public boolean stopAt(Object context, String key) {
      return false;
    }

    public void handleUnrecognizedKey(Object context, String key) {
    }

    public Collection<Object> newInstanceForArray(Object context, Field field) {
      return null;
    }

    public Object newInstanceForObject(Object context, Class<?> fieldClass) {
      return null;
    }
  }

  /**
   * Parse a JSON Object from the given JSON parser into the given destination
   * class, optionally using the given parser customizer.
   * 
   * @param <T> destination class type
   * @param parser JSON parser
   * @param destinationClass destination class that has a public default
   *        constructor to use to create a new instance
   * @param customizeParser optional parser customizer or {@code null} for none
   * @return new instance of the parsed destination class
   * @throws IOException I/O exception
   */
  public static <T> T parse(JsonParser parser, Class<T> destinationClass,
      CustomizeParser customizeParser) throws IOException {
    T newInstance = ClassInfo.newInstance(destinationClass);
    parse(parser, newInstance, customizeParser);
    return newInstance;
  }

  /**
   * Parse a JSON Object from the given JSON parser into the given destination
   * object, optionally using the given parser customizer.
   * 
   * @param parser JSON parser
   * @param destination destination object
   * @param customizeParser optional parser customizer or {@code null} for none
   * @throws IOException I/O exception
   */
  public static void parse(JsonParser parser, Object destination,
      CustomizeParser customizeParser) throws IOException {
    Class<?> destinationClass = destination.getClass();
    ClassInfo classInfo = ClassInfo.of(destinationClass);
    boolean isEntity = JsonEntity.class.isAssignableFrom(destinationClass);
    if (!isEntity && Map.class.isAssignableFrom(destinationClass)) {
      @SuppressWarnings("unchecked")
      Map<String, Object> destinationMap = (Map<String, Object>) destination;
      parseMap(parser, destinationMap, null, customizeParser);
      return;
    }
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.getCurrentName();
      JsonToken curToken = parser.nextToken();
      // stop at items for feeds
      if (customizeParser != null && customizeParser.stopAt(destination, key)) {
        return;
      }
      // get the field from the type information
      Field field = classInfo.getField(key);
      if (field != null) {
        // skip final fields
        Class<?> fieldClass = field.getType();
        if (Modifier.isFinal(field.getModifiers())
            && !ClassInfo.isPrimitive(fieldClass)) {
          throw new IllegalArgumentException(
              "final array/object fields are not supported");
        }
        Object fieldValue =
            parseValue(parser, curToken, field, fieldClass, destination,
                customizeParser);
        FieldInfo.setFieldValue(field, destination, fieldValue);
      } else if (isEntity) {
        // store unknown field in entity
        JsonEntity object = (JsonEntity) destination;
        object.set(key, parseValue(parser, curToken, null, null, destination,
            customizeParser));
      } else {
        // unrecognized field, skip value
        if (customizeParser != null) {
          customizeParser.handleUnrecognizedKey(destination, key);
        }
        parser.skipChildren();
      }
    }
  }

  private static void parseMap(JsonParser parser,
      Map<String, Object> destinationMap, Class<?> valueClass,
      CustomizeParser customizeParser) throws IOException {
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.getCurrentName();
      JsonToken curToken = parser.nextToken();
      // stop at items for feeds
      if (customizeParser != null
          && customizeParser.stopAt(destinationMap, key)) {
        return;
      }
      Object value =
          parseValue(parser, curToken, null, valueClass, destinationMap,
              customizeParser);
      destinationMap.put(key, value);
    }
  }

  private static Object parseValue(JsonParser parser, JsonToken token,
      Field field, Class<?> fieldClass, Object destination,
      CustomizeParser customizeParser) throws IOException {
    switch (token) {
      case START_ARRAY:
        if (fieldClass == null || Collection.class.isAssignableFrom(fieldClass)) {
          // TODO: handle JSON array of JSON array
          Collection<Object> collectionValue = null;
          if (customizeParser != null && field != null) {
            collectionValue =
                customizeParser.newInstanceForArray(destination, field);
          }
          if (collectionValue == null) {
            collectionValue = ClassInfo.newCollectionInstance(fieldClass);
          }
          Class<?> subFieldClass = ClassInfo.getCollectionParameter(field);
          JsonToken listToken;
          while ((listToken = parser.nextToken()) != JsonToken.END_ARRAY) {
            collectionValue.add(parseValue(parser, listToken, null,
                subFieldClass, destination, customizeParser));
          }
          return collectionValue;
        }
        throw new IllegalArgumentException(
            "expected field type that implements Collection but got "
                + fieldClass);
      case START_OBJECT:
        Object newInstance = null;
        boolean isMap =
            fieldClass == null || Map.class.isAssignableFrom(fieldClass);
        if (fieldClass != null && customizeParser != null) {
          newInstance =
              customizeParser.newInstanceForObject(destination, fieldClass);
        }
        if (newInstance == null) {
          if (isMap) {
            newInstance = ClassInfo.newMapInstance(fieldClass);
          } else {
            newInstance = ClassInfo.newInstance(fieldClass);
          }
        }
        if (isMap && fieldClass != null) {
          Class<?> valueClass = ClassInfo.getMapValueParameter(field);
          if (valueClass != null) {
            @SuppressWarnings("unchecked")
            Map<String, Object> destinationMap =
                (Map<String, Object>) newInstance;
            parseMap(parser, destinationMap, valueClass, customizeParser);
            return newInstance;
          }
        }
        parse(parser, newInstance, customizeParser);
        return newInstance;
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
          // TODO: "special" values like Double.POSITIVE_INFINITY?
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
                + ": expected type String/Long/long/Double/double/Character/char/BigInteger/BigDecimal/DateTime but got "
                + fieldClass);
      case VALUE_NULL:
        return null;
      default:
        throw new IllegalArgumentException(parser.getCurrentName()
            + ": unexpected JSON node type");
    }
  }
}
