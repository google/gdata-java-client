// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.http.HttpResponse;
import com.google.api.data.client.v2.ClassInfo;
import com.google.api.data.client.v2.DateTime;
import com.google.api.data.client.v2.FieldInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public final class Atom {
  public static final String CONTENT_TYPE = "application/atom+xml";

  public static GDataXmlParserFactory parserFactory;

  static GDataXmlParserFactory getParserFactory()
      throws XmlPullParserException {
    GDataXmlParserFactory parserFactory = Atom.parserFactory;
    if (parserFactory == null) {
      parserFactory =
          Atom.parserFactory = DefaultGDataXmlParserFactory.getInstance();
    }
    return parserFactory;
  }

  public static String getEntryFields(Class<?> itemType) {
    StringBuilder buf = new StringBuilder();
    appendEntryFields(buf, itemType);
    return buf.toString();
  }

  public static String getFeedFields(Class<?> feedType, Class<?> itemType) {
    StringBuilder buf = new StringBuilder();
    appendFeedFields(buf, feedType, itemType);
    return buf.toString();
  }

  public static void appendEntryFields(StringBuilder buf, Class<?> itemType) {
    ClassInfo classInfo = ClassInfo.of(itemType);
    boolean first = true;
    for (String name : classInfo.getNames()) {
      Field field = classInfo.getField(name);
      if (Modifier.isFinal(field.getModifiers())) {
        continue;
      }
      if (first) {
        first = false;
      } else {
        buf.append(',');
      }
      buf.append(name);
      Class<?> fieldClass = field.getType();
      if (List.class.isAssignableFrom(fieldClass)) {
        // TODO: handle array of array?
        fieldClass = ClassInfo.getListParameter(field);
      }
      if (fieldClass != null && !ClassInfo.isImmutable(fieldClass)
          && !List.class.isAssignableFrom(fieldClass)
          && !Map.class.isAssignableFrom(fieldClass)) {
        buf.append('(');
        appendEntryFields(buf, fieldClass);
        buf.append(')');
      }
    }
  }

  public static void appendFeedFields(StringBuilder buf, Class<?> feedType,
      Class<?> itemType) {
    int length = buf.length();
    appendEntryFields(buf, feedType);
    if (buf.length() > length) {
      buf.append(",");
    }
    buf.append("entry(");
    appendEntryFields(buf, itemType);
    buf.append(')');
  }

  @SuppressWarnings("unchecked")
  public static <T> T clone(T item) {
    if (item == null) {
      return null;
    }
    Class<? extends T> itemClass = (Class<? extends T>) item.getClass();
    // TODO: support enum for Atom string value?
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
    // clone basic Atom object
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
    // TODO: clone AtomObject
    return result;
  }

  private Atom() {
  }

  public static <T> T parse(HttpResponse response,
      Class<T> classToInstantiateAndParse) throws IOException,
      XmlPullParserException, AtomException {
    InputStream content = processAsInputStream(response);
    try {
      XmlPullParser parser = processAsXmlPullParser(response, content);
      T result = parse(parser, content, classToInstantiateAndParse);
      content = null;
      return result;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }

  public static <T, I> AtomFeedParser<T, I> useFeedParser(
      HttpResponse response, Class<T> feedType, Class<I> entryType)
      throws XmlPullParserException, AtomException, IOException {
    InputStream content = processAsInputStream(response);
    try {
      XmlPullParser parser = processAsXmlPullParser(response, content);
      AtomFeedParser<T, I> result =
          new AtomFeedParser<T, I>(parser, content, feedType, entryType);
      content = null;
      return result;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }

  public static <T, I> AtomMultiKindFeedParser<T> useMultiKindFeedParser(
      HttpResponse response, Class<T> feedType, Class<?>... entryTypes)
      throws XmlPullParserException, AtomException, IOException {
    InputStream content = processAsInputStream(response);
    try {
      XmlPullParser parser = processAsXmlPullParser(response, content);
      AtomMultiKindFeedParser<T> result =
          new AtomMultiKindFeedParser<T>(parser, content, feedType, entryTypes);
      content = null;
      return result;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }

  public static <T> T parse(XmlPullParser parser, InputStream inputStream,
      Class<T> entryType) throws IOException, XmlPullParserException {
    T newInstance = ClassInfo.newInstance(entryType);
    parseElementAndClose(parser, inputStream, newInstance);
    return newInstance;
  }

  public static InputStream processAsInputStream(HttpResponse response)
      throws IOException, AtomException, XmlPullParserException {
    if (response.isSuccessStatusCode()) {
      return response.getContent();
    }
    AtomException exception = new AtomException(response);
    InputStream content = response.getContent();
    if (content != null) {
      try {
        if (response.getContentType().startsWith(CONTENT_TYPE)) {
          XmlPullParser parser = processAsXmlPullParser(response, content);
          exception.parser = parser;
          exception.inputStream = content;
        } else {
          exception.content = response.parseContentAsString();
        }
        content = null;
      } finally {
        if (content != null) {
          content.close();
        }
      }
    }
    throw exception;
  }

  public static XmlPullParser processAsXmlPullParser(HttpResponse response,
      InputStream content) throws XmlPullParserException {
    String contentType = response.getContentType();
    if (!contentType.startsWith(CONTENT_TYPE)) {
      throw new IllegalArgumentException("Wrong content type: expected <"
          + CONTENT_TYPE + "> but got <" + contentType + ">");
    }
    XmlPullParser result = Atom.getParserFactory().createParser();
    if (!result.getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES)) {
      throw new IllegalStateException("XmlGDataParser needs to have "
          + "namespace aware feature");
    }
    // TODO: make the XML pull parser secure!
    result.setInput(content, null);
    return result;
  }

  private static void parseValue(String stringValue, Field field,
      Object destination, boolean isAtomObject,
      Map<String, Object> destinationMap, String name) {
    if (field == null) {
      if (isAtomObject) {
        ((AtomEntity) destination).set(name, parseValue(stringValue, null));
      } else if (destinationMap != null) {
        destinationMap.put(name, parseValue(stringValue, null));
      }
    } else {
      Class<?> fieldClass = field.getType();
      if (Modifier.isFinal(field.getModifiers())) {
        // TODO: check for other kinds of field types?
        if (fieldClass == String.class) {
          Object fieldValue = FieldInfo.getFieldValue(field, destination);
          if (fieldClass == String.class && !stringValue.equals(fieldValue)) {
            throw new IllegalArgumentException("expected " + name + " value <"
                + fieldValue + "> but was <" + stringValue + ">");
          }
        }
      } else {
        Object fieldValue = parseValue(stringValue, fieldClass);
        FieldInfo.setFieldValue(field, destination, fieldValue);
      }
    }
  }

  /**
   * Parses an XML elment using the given XML pull parser into the given
   * destination object, and then closes the given input stream.
   * <p>
   * Requires the the current event be {@link XmlPullParser#START_DOCUMENT},
   * followed by the {@link XmlPullParser#START_TAG} of the element being
   * parsed.
   */
  static void parseElementAndClose(XmlPullParser parser,
      InputStream inputStream, Object destination) throws IOException,
      XmlPullParserException {
    try {
      int eventType = parser.getEventType();
      if (eventType != XmlPullParser.START_DOCUMENT) {
        throw new IllegalArgumentException("wrong event type: " + eventType);
      }
      parseElement(parser, destination, false);
    } finally {
      inputStream.close();
    }
  }

  /**
   * Parses an XML elment using the given XML pull parser into a new instance of
   * the given destination class. Optionally stops when reaching a top-level
   * {@code atom:entry}.
   * <p>
   * Requires the the current event be {@link XmlPullParser#START_TAG} (skipping
   * any initial {@link XmlPullParser#START_DOCUMENT}) of the element being
   * parsed. At normal parsing completion, the current event will either be
   * {@link XmlPullParser#END_TAG} of the element being parsed, or the
   * {@link XmlPullParser#START_TAG} of the requested {@code atom:entry}.
   */
  static <T> T parseElement(XmlPullParser parser, Class<T> destinationClass,
      boolean stopAtAtomEntry) throws IOException, XmlPullParserException {
    T newInstance =
        destinationClass == null ? null : ClassInfo
            .newInstance(destinationClass);
    parseElement(parser, newInstance, stopAtAtomEntry);
    return newInstance;
  }

  /**
   * Parses an XML elment using the given XML pull parser into the given
   * destination object. Optionally stops when reaching a top-level {@code
   * atom:entry}.
   * <p>
   * Requires the the current event be {@link XmlPullParser#START_TAG} (skipping
   * any initial {@link XmlPullParser#START_DOCUMENT}) of the element being
   * parsed. At normal parsing completion, the current event will either be
   * {@link XmlPullParser#END_TAG} of the element being parsed, or the
   * {@link XmlPullParser#START_TAG} of the requested {@code atom:entry}.
   */
  static void parseElement(XmlPullParser parser, Object destination,
      boolean stopAtAtomEntry) throws IOException, XmlPullParserException {
    Class<?> destinationClass =
        destination == null ? null : destination.getClass();
    boolean isAtomObject =
        destination != null
            && AtomEntity.class.isAssignableFrom(destinationClass);
    boolean isMap =
        destination != null && Map.class.isAssignableFrom(destinationClass);
    @SuppressWarnings("unchecked")
    Map<String, Object> destinationMap =
        isMap ? (Map<String, Object>) destination : null;
    ClassInfo classInfo =
        isMap || destination == null ? null : ClassInfo.of(destinationClass);
    int eventType = parser.getEventType();
    if (parser.getEventType() == XmlPullParser.START_DOCUMENT) {
      eventType = parser.next();
    }
    if (eventType != XmlPullParser.START_TAG) {
      throw new IllegalArgumentException("wrong event type: " + eventType);
    }
    // attributes
    if (destination != null) {
      int attributeCount = parser.getAttributeCount();
      for (int i = 0; i < attributeCount; i++) {
        String fieldName =
            getFieldName(true, parser.getAttributePrefix(i), parser
                .getAttributeNamespace(i), parser.getAttributeName(i));
        Field field = isMap ? null : classInfo.getField(fieldName);
        parseValue(parser.getAttributeValue(i), field, destination,
            isAtomObject, destinationMap, fieldName);
      }
    }
    Field field;
    while (true) {
      int event = parser.next();
      switch (event) {
        case XmlPullParser.END_DOCUMENT:
        case XmlPullParser.END_TAG:
          return;
        case XmlPullParser.TEXT:
          if (destination != null) {
            String textFieldName = "text()";
            field = isMap ? null : classInfo.getField(textFieldName);
            parseValue(parser.getText(), field, destination, isAtomObject,
                destinationMap, textFieldName);
          }
          break;
        case XmlPullParser.START_TAG:
          // stop at entry for feed
          if (stopAtAtomEntry && "entry".equals(parser.getName())
              && NamespaceDictionary.ATOM_NAMESPACE.equals(parser.getNamespace())) {
            return;
          }
          if (destination == null) {
            int level = 1;
            while (level != 0) {
              switch (parser.next()) {
                case XmlPullParser.END_DOCUMENT:
                  return;
                case XmlPullParser.START_TAG:
                  level++;
                  break;
                case XmlPullParser.END_TAG:
                  level--;
                  break;
              }
            }
            continue;
          }
          // element
          String fieldName =
              getFieldName(false, parser.getPrefix(), parser.getNamespace(),
                  parser.getName());
          field = isMap ? null : classInfo.getField(fieldName);
          Class<?> fieldClass = field == null ? null : field.getType();
          if (field == null && !isMap && !isAtomObject || field != null
              && ClassInfo.isImmutable(fieldClass)) {
            int level = 1;
            while (level != 0) {
              switch (parser.next()) {
                case XmlPullParser.END_DOCUMENT:
                  return;
                case XmlPullParser.START_TAG:
                  level++;
                  break;
                case XmlPullParser.END_TAG:
                  level--;
                  break;
                case XmlPullParser.TEXT:
                  if (level == 1) {
                    parseValue(parser.getText(), field, destination,
                        isAtomObject, destinationMap, fieldName);
                  }
                  break;
              }
            }
          } else if (field == null || Map.class.isAssignableFrom(fieldClass)) {
            // TODO: handle sub-field type
            @SuppressWarnings("unchecked")
            Map<String, Object> mapValue =
                (Map<String, Object>) ClassInfo.newMapInstance(fieldClass);
            parseElement(parser, mapValue, false);
            if (isMap) {
              @SuppressWarnings("unchecked")
              List<Object> list = (List<Object>) destinationMap.get(fieldName);
              if (list == null) {
                list = ClassInfo.newListInstance(null, 1);
                destinationMap.put(fieldName, list);
              }
              list.add(mapValue);
            } else if (field != null) {
              FieldInfo.setFieldValue(field, destination, mapValue);
            } else {
              AtomEntity atom = (AtomEntity) destination;
              @SuppressWarnings("unchecked")
              List<Object> list = (List<Object>) atom.get(fieldName);
              if (list == null) {
                list = ClassInfo.newListInstance(null, 1);
                atom.set(fieldName, list);
              }
              list.add(mapValue);
            }
          } else if (List.class.isAssignableFrom(fieldClass)) {
            @SuppressWarnings("unchecked")
            List<Object> listValue =
                (List<Object>) FieldInfo.getFieldValue(field, destination);
            Class<?> subFieldClass = ClassInfo.getListParameter(field);
            if (listValue == null) {
              @SuppressWarnings("unchecked")
              List<Object> listValueTemp =
                  (List<Object>) ClassInfo.newListInstance(fieldClass, 1);
              listValue = listValueTemp;
              FieldInfo.setFieldValue(field, destination, listValue);
            }
            Object elementValue = null;
            if (ClassInfo.isImmutable(subFieldClass)) {
              int level = 1;
              while (level != 0) {
                switch (parser.next()) {
                  case XmlPullParser.END_DOCUMENT:
                    return;
                  case XmlPullParser.START_TAG:
                    level++;
                    break;
                  case XmlPullParser.END_TAG:
                    level--;
                    break;
                  case XmlPullParser.TEXT:
                    if (level == 1) {
                      elementValue =
                          parseValue(parser.getText(), subFieldClass);
                    }
                    break;
                }
              }
            } else {
              elementValue = parseElement(parser, subFieldClass, false);
            }
            listValue.add(elementValue);
          } else {
            FieldInfo.setFieldValue(field, destination, parseElement(parser,
                fieldClass, false));
          }
          break;
      }
    }
  }

  private static String getFieldName(boolean isAttribute, String alias,
      String namespace, String name) {
    alias = alias == null ? "" : alias.intern();
    namespace = namespace.intern();
    StringBuilder buf = new StringBuilder(2 + alias.length() + name.length());
    if (isAttribute) {
      buf.append('@');
    }
    if (alias != "") {
      buf.append(alias).append(':');
    }
    return buf.append(name).toString().intern();
  }

  private static Object parseValue(String stringValue, Class<?> fieldType) {
    if (fieldType == null || fieldType == String.class) {
      return stringValue;
    }
    if (fieldType == Integer.class || fieldType == int.class) {
      return new Integer(stringValue);
    }
    if (fieldType == Short.class || fieldType == short.class) {
      return new Short(stringValue);
    }
    if (fieldType == Byte.class || fieldType == byte.class) {
      return new Byte(stringValue);
    }
    if (fieldType == Long.class || fieldType == long.class) {
      return new Long(stringValue);
    }
    if (fieldType == Double.class || fieldType == double.class) {
      if (stringValue.equals("INF")) {
        return new Double(Double.POSITIVE_INFINITY);
      }
      if (stringValue.equals("-INF")) {
        return new Double(Double.NEGATIVE_INFINITY);
      }
      return new Double(stringValue);
    }
    if (fieldType == Character.class || fieldType == char.class) {
      if (stringValue.length() != 1) {
        throw new IllegalArgumentException(
            "expected type Character/char but got " + fieldType);
      }
      return stringValue.charAt(0);
    }
    if (fieldType == BigInteger.class) {
      return new BigInteger(stringValue);
    }
    if (fieldType == BigDecimal.class) {
      return new BigDecimal(stringValue);
    }
    if (fieldType == DateTime.class) {
      return DateTime.parseRfc3339(stringValue);
    }
    if (fieldType == Boolean.class || fieldType == boolean.class) {
      return "true".equals(stringValue) ? Boolean.TRUE : Boolean.FALSE;
    }
    if (fieldType == Float.class || fieldType == float.class) {
      if (stringValue.equals("INF")) {
        return Float.POSITIVE_INFINITY;
      }
      if (stringValue.equals("-INF")) {
        return Float.NEGATIVE_INFINITY;
      }
      return Float.valueOf(stringValue);
    }
    throw new IllegalArgumentException("unexpected type: " + fieldType);
  }
}
