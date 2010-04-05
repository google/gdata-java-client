package com.google.api.client.xml;

import com.google.api.client.ClassInfo;
import com.google.api.client.DateTime;
import com.google.api.client.FieldInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/** XML utilities. */
public class Xml {

  /** XML Parser factory. */
  public static volatile XmlParserFactory parserFactory;

  /** Returns the parser factory. */
  private static XmlParserFactory getParserFactory()
      throws XmlPullParserException {
    XmlParserFactory parserFactory = Xml.parserFactory;
    if (parserFactory == null) {
      parserFactory = Xml.parserFactory = DefaultXmlParserFactory.getInstance();
    }
    return parserFactory;
  }

  /**
   * Returns a new XML serializer.
   * 
   * @throws IllegalArgumentException if encountered an
   *         {@link XmlPullParserException}
   */
  public static XmlSerializer createSerializer() {
    try {
      return getParserFactory().createSerializer();
    } catch (XmlPullParserException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /** Returns a new XML pullparser. */
  public static XmlPullParser createParser() throws XmlPullParserException {
    XmlPullParser result = getParserFactory().createParser();
    if (!result.getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES)) {
      throw new IllegalStateException("XML pull parser must have "
          + "namespace-aware feature");
    }
    // TODO: make the XML pull parser secure
    return result;
  }

  /**
   * Shows a debug string representation of an item entity.
   * <p>
   * It will make up something for the element name and XML namespaces. If those
   * are known, it is better to use
   * {@link XmlNamespaceDictionary#toStringOf(String, Object)}.
   * 
   * @param element element entity ({@link XmlEntity}, {@link Map}, or any
   *        object with public fields)
   */
  public static String toStringOf(Object element) {
    return new XmlNamespaceDictionary().toStringOf(null, element);
  }

  @SuppressWarnings("unchecked")
  public static <T> T clone(T item) {
    // TODO: revisit this for the new Entity implementation that extends Map
    if (item == null) {
      return null;
    }
    Class<? extends T> itemClass = (Class<? extends T>) item.getClass();
    // TODO: support enum for Atom string value?
    // TODO: support Java arrays?
    // don't need to clone immutable types
    if (ClassInfo.isPrimitive(itemClass)) {
      return item;
    }
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
    // TODO: clone AtomEntity
    return result;
  }

  /**
   * Parses an XML elment using the given XML pull parser into the given
   * destination object, and then closes the given input stream.
   * <p>
   * Requires the the current event be {@link XmlPullParser#START_DOCUMENT},
   * followed by the {@link XmlPullParser#START_TAG} of the element being
   * parsed.
   */
  public static <T> T parseElementAndClose(XmlPullParser parser,
      InputStream inputStream, Class<T> destinationClass) throws IOException,
      XmlPullParserException {
    T newInstance = ClassInfo.newInstance(destinationClass);
    parseElementAndClose(parser, inputStream, newInstance);
    return newInstance;
  }

  private static void parseValue(String stringValue, Field field,
      Object destination, boolean isAtomObject,
      Map<String, Object> destinationMap, String name) {
    if (field == null) {
      if (isAtomObject) {
        ((XmlEntity) destination).set(name, parseValue(stringValue, null));
      } else if (destinationMap != null) {
        destinationMap.put(name, parseValue(stringValue, null));
      }
    } else {
      Class<?> fieldClass = field.getType();
      if (Modifier.isFinal(field.getModifiers())
          && !ClassInfo.isPrimitive(fieldClass)) {
        throw new IllegalArgumentException(
            "final sub-element fields are not supported");
      }
      Object fieldValue = parseValue(stringValue, fieldClass);
      FieldInfo.setFieldValue(field, destination, fieldValue);
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
  public static void parseElementAndClose(XmlPullParser parser,
      InputStream inputStream, Object destination) throws IOException,
      XmlPullParserException {
    try {
      int eventType = parser.getEventType();
      if (eventType != XmlPullParser.START_DOCUMENT) {
        throw new IllegalArgumentException("wrong event type: " + eventType);
      }
      parseElement(parser, destination, null);
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
  public static <T> T parseElement(XmlPullParser parser,
      Class<T> destinationClass, CustomizeParser customizeParser)
      throws IOException, XmlPullParserException {
    T newInstance =
        destinationClass == null ? null : ClassInfo
            .newInstance(destinationClass);
    parseElement(parser, newInstance, customizeParser);
    return newInstance;
  }

  /**
   * Customizes the behavior of XML parsing. Subclasses may override any methods
   * they need to customize behavior.
   */
  public static class CustomizeParser {

    /**
     * Returns whether to stop parsing when reaching the start tag of an XML
     * element. By default, returns {@code false}, but subclasses may override.
     * 
     * @param namespace XML element's namespace URI
     * @param localName XML element's local name
     */
    public boolean stopAtStartTag(String namespace, String localName) {
      return false;
    }
  }

  /**
   * Parses an XML elment using the given XML pull parser into the given
   * destination object.
   * <p>
   * Requires the the current event be {@link XmlPullParser#START_TAG} (skipping
   * any initial {@link XmlPullParser#START_DOCUMENT}) of the element being
   * parsed. At normal parsing completion, the current event will either be
   * {@link XmlPullParser#END_TAG} of the element being parsed, or the
   * {@link XmlPullParser#START_TAG} of the requested {@code atom:entry}.
   */
  private static void parseElement(XmlPullParser parser, Object destination,
      CustomizeParser customizeParser) throws IOException,
      XmlPullParserException {
    Class<?> destinationClass =
        destination == null ? null : destination.getClass();
    boolean isXmlEntity =
        destination != null
            && XmlEntity.class.isAssignableFrom(destinationClass);
    boolean isMap =
        !isXmlEntity && destination != null
            && Map.class.isAssignableFrom(destinationClass);
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
            isXmlEntity, destinationMap, fieldName);
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
          // parse text content
          if (destination != null) {
            String textFieldName = "text()";
            field = isMap ? null : classInfo.getField(textFieldName);
            parseValue(parser.getText(), field, destination, isXmlEntity,
                destinationMap, textFieldName);
          }
          break;
        case XmlPullParser.START_TAG:
          if (customizeParser != null
              && customizeParser.stopAtStartTag(parser.getNamespace(), parser
                  .getName())) {
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
          if (field == null && !isMap && !isXmlEntity || field != null
              && ClassInfo.isPrimitive(fieldClass)) {
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
                        isXmlEntity, destinationMap, fieldName);
                  }
                  break;
              }
            }
          } else if (field == null || Map.class.isAssignableFrom(fieldClass)) {
            // TODO: handle sub-field type
            Map<String, Object> mapValue = ClassInfo.newMapInstance(fieldClass);
            parseElement(parser, mapValue, customizeParser);
            if (isMap) {
              @SuppressWarnings("unchecked")
              List<Object> list = (List<Object>) destinationMap.get(fieldName);
              if (list == null) {
                list = new ArrayList<Object>(1);
                destinationMap.put(fieldName, list);
              }
              list.add(mapValue);
            } else if (field != null) {
              FieldInfo.setFieldValue(field, destination, mapValue);
            } else {
              XmlEntity atom = (XmlEntity) destination;
              @SuppressWarnings("unchecked")
              List<Object> list = (List<Object>) atom.get(fieldName);
              if (list == null) {
                list = new ArrayList<Object>(1);
                atom.set(fieldName, list);
              }
              list.add(mapValue);
            }
          } else if (Collection.class.isAssignableFrom(fieldClass)) {
            @SuppressWarnings("unchecked")
            Collection<Object> collectionValue =
                (Collection<Object>) FieldInfo
                    .getFieldValue(field, destination);
            if (collectionValue == null) {
              collectionValue = ClassInfo.newCollectionInstance(fieldClass);
              FieldInfo.setFieldValue(field, destination, collectionValue);
            }
            Object elementValue = null;
            Class<?> subFieldClass = ClassInfo.getCollectionParameter(field);
            if (ClassInfo.isPrimitive(subFieldClass)) {
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
              elementValue =
                  parseElement(parser, subFieldClass, customizeParser);
            }
            collectionValue.add(elementValue);
          } else {
            FieldInfo.setFieldValue(field, destination, parseElement(parser,
                fieldClass, customizeParser));
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

  private Xml() {
  }
}
