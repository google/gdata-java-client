// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2.atom;

import com.google.api.data.client.v2.ClassInfo;
import com.google.api.data.client.v2.DateTime;
import com.google.api.data.client.v2.FieldInfo;
import com.google.api.data.client.v2.GDataClient;
import com.google.api.data.client.v2.GDataResponse;
import com.google.api.data.client.v2.GDataSerializer;
import com.google.api.data.client.v2.HttpTransport;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class AtomClient {
  private static final String CONTENT_TYPE = "application/atom+xml";

  private static final String PATCH_CONTENT_TYPE = "application/xml";
  static final String ATOM_NAMESPACE = "http://www.w3.org/2005/Atom";
  static final String GD_NAMESPACE = "http://schemas.google.com/g/2005";

  public static final class Builder {

    private List<String> namespaceAliases = null;
    private List<String> namespaceUris = null;

    public HttpTransport httpTransport;

    public GDataXmlParserFactory parserFactory;

    public String applicationName;

    public String authToken;

    public String version;

    public void addNamespaces(Map<String, String> namespaceAliastoUriMap) {
      List<String> namespaceAliases = this.namespaceAliases;
      List<String> namespaceUris = this.namespaceUris;
      if (namespaceAliases == null) {
        this.namespaceAliases = namespaceAliases = new ArrayList<String>();
        this.namespaceUris = namespaceUris = new ArrayList<String>();
      }
      for (Map.Entry<String, String> entry : namespaceAliastoUriMap.entrySet()) {
        namespaceAliases.add(entry.getKey());
        namespaceUris.add(entry.getValue());
      }
    }

    public AtomClient build() throws XmlPullParserException {
      return new AtomClient(this.httpTransport, this.applicationName,
          this.authToken, this.version, this.parserFactory,
          this.namespaceAliases, this.namespaceUris);
    }
  }

  private final GDataClient gdataClient;

  private final GDataXmlParserFactory parserFactory;

  private final ConcurrentHashMap<String, String> namespaceAliasToUriMap =
      new ConcurrentHashMap<String, String>();

  AtomClient(HttpTransport httpTransport, String applicationName,
      String authToken, String version, GDataXmlParserFactory parserFactory,
      List<String> namespaceAliases, List<String> namespaceUris)
      throws XmlPullParserException {
    this.parserFactory =
        parserFactory == null ? new DefaultGDataXmlParserFactory()
            : parserFactory;
    ConcurrentHashMap<String, String> namespaceAliasToUriMap =
        this.namespaceAliasToUriMap;
    int numNamespaces = namespaceAliases == null ? 0 : namespaceAliases.size();
    for (int i = 0; i < numNamespaces; i++) {
      String alias = namespaceAliases.get(i).intern();
      String uri = namespaceUris.get(i).intern();
      String previousUri = namespaceAliasToUriMap.put(alias, uri);
      if (previousUri != null) {
        throw new IllegalArgumentException(alias + " was already set to "
            + previousUri);
      }
    }
    this.gdataClient =
        new GDataClient(httpTransport, CONTENT_TYPE, (applicationName
                + "(atom+xml)"),
        authToken, version);
  }

  public AtomFeedResponse executeGetFeed(String uri) throws IOException,
      XmlPullParserException, AtomException {
    return parseFeedResponse(this.gdataClient.executeGet(uri));
  }

  public AtomFeedResponse executeGetFeedIfModified(String uri, String etag)
      throws IOException, XmlPullParserException, AtomException {
    return parseFeedResponse(this.gdataClient.executeGetIfModified(uri, etag));
  }

  public AtomEntryResponse executeGetEntry(String uri) throws IOException,
      XmlPullParserException, AtomException {
    return parseEntryResponse(this.gdataClient.executeGet(uri));
  }

  public AtomEntryResponse executeGetEntryIfModified(String uri, String etag)
      throws IOException, XmlPullParserException, AtomException {
    return parseEntryResponse(this.gdataClient.executeGetIfModified(uri, etag));
  }

  public InputStream executeGetMedia(String uri) throws IOException,
      AtomException, XmlPullParserException {
    return processResponse(this.gdataClient.executeGet(uri));
  }

  public AtomEntryResponse executePostEntry(String uri, Object entry)
      throws IOException, XmlPullParserException, AtomException {
    return parseEntryResponse(this.gdataClient.executePost(uri,
        new AtomSerializer(this, entry)));
  }

  public AtomEntryResponse executePostMedia(String uri, String fileName,
      String mediaType, InputStream mediaContent, long mediaContentLength)
      throws IOException, XmlPullParserException, AtomException {
    return parseEntryResponse(this.gdataClient.executePostMedia(uri, fileName,
        mediaType, mediaContent, mediaContentLength));
  }

  public AtomEntryResponse executePostMediaWithMetadata(String uri,
      GDataSerializer metadata, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException, XmlPullParserException,
      AtomException {
    return parseEntryResponse(this.gdataClient.executePostMediaWithMetadata(
        uri, metadata, mediaType, mediaContent, mediaContentLength));
  }

  public AtomEntryResponse executePatchEntryIfNotModified(String uri,
      String etag, Object patchedEntry) throws IOException, AtomException,
      XmlPullParserException {
    return parseEntryResponse(this.gdataClient.executePatchIfNotModified(uri,
        etag, new AtomSerializer(this, patchedEntry), PATCH_CONTENT_TYPE));
  }

  public AtomEntryResponse executePatchEntryIfNotModified(String uri,
      String etag, GDataSerializer content) throws IOException,
      XmlPullParserException, AtomException {
    return parseEntryResponse(this.gdataClient.executePatchIfNotModified(uri,
        etag, content, PATCH_CONTENT_TYPE));
  }

  public AtomEntryResponse executePatchEntryRelativeToOriginalIfNotModified(
      String uri, String etag, Object patchedEntry, Object originalEntry)
      throws IOException, AtomException, XmlPullParserException {
    return parseEntryResponse(this.gdataClient.executePatchIfNotModified(uri,
        etag, new AtomPatchRelativeToOriginalSerializer(this, patchedEntry,
            originalEntry), PATCH_CONTENT_TYPE));
  }

  public AtomEntryResponse executePutEntryIfNotModified(String uri,
      String etag, Object entry) throws IOException, XmlPullParserException,
      AtomException {
    if (!(entry instanceof AtomEntity)) {
      throw new IllegalArgumentException("can only post an item that extends "
          + AtomEntity.class.getName());
      // TODO: check subclasses extend GDataJsoncObject
    }
    return parseEntryResponse(this.gdataClient.executePutIfNotModified(uri,
        etag, new AtomSerializer(this, entry)));
  }

  public AtomEntryResponse executePutEntryIfNotModified(String uri,
      String etag, GDataSerializer content) throws IOException,
      XmlPullParserException, AtomException {
    return parseEntryResponse(this.gdataClient.executePutIfNotModified(uri,
        etag, content));
  }

  public AtomEntryResponse executePutMediaIfNotModified(String uri,
      String etag, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException, XmlPullParserException,
      AtomException {
    return parseEntryResponse(this.gdataClient.executePutMediaIfNotModified(
        uri, etag, mediaType, mediaContent, mediaContentLength));
  }

  public AtomEntryResponse executePutMediaWithMetadata(String uri,
      GDataSerializer metadata, String mediaType, InputStream mediaContent,
      long mediaContentLength) throws IOException, XmlPullParserException,
      AtomException {
    return parseEntryResponse(this.gdataClient.executePutMediaWithMetadata(uri,
        metadata, mediaType, mediaContent, mediaContentLength));
  }

  public void executeDeleteEntry(String uri) throws IOException, AtomException,
      XmlPullParserException {
    ignoreSuccessResponse(this.gdataClient.executeDelete(uri));
  }

  public void executeDeleteEntryIfNotModified(String uri, String etag)
      throws IOException, AtomException, XmlPullParserException {
    ignoreSuccessResponse(this.gdataClient
        .executeDeleteIfNotModified(uri, etag));
  }

  public <T, I> AtomFeedParser<T, I> useFeedParser(XmlPullParser parser,
      InputStream inputStream, Class<T> feedType, Class<I> entryType) {
    return new AtomFeedParser<T, I>(this, parser, inputStream, feedType,
        entryType);
  }

  public <T, I> AtomMultiKindFeedParser<T> useMultiKindFeedParser(
      XmlPullParser parser, InputStream inputStream, Class<T> feedType,
      Class<?>... entryTypes) {
    return new AtomMultiKindFeedParser<T>(this, parser, inputStream, feedType,
        entryTypes);
  }

  public <T> T parseEntry(XmlPullParser parser, InputStream inputStream,
      Class<T> entryType) throws IOException, XmlPullParserException {
    T newInstance = ClassInfo.newInstance(entryType);
    parseElementAndClose(parser, inputStream, newInstance);
    return newInstance;
  }

  private void ignoreSuccessResponse(GDataResponse response)
      throws AtomException, IOException, XmlPullParserException {
    processResponse(response).close();
  }

  private AtomEntryResponse parseEntryResponse(GDataResponse response)
      throws XmlPullParserException, IOException, AtomException {
    InputStream content = processResponse(response);
    try {
      XmlPullParser parser = createParser(response.getContentType(), content);
      AtomEntryResponse result = new AtomEntryResponse(this, parser, content);
      content = null;
      return result;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }

  private AtomFeedResponse parseFeedResponse(GDataResponse response)
      throws XmlPullParserException, IOException, AtomException {
    InputStream content = processResponse(response);
    try {
      XmlPullParser parser = createParser(response.getContentType(), content);
      AtomFeedResponse result = new AtomFeedResponse(this, parser, content);
      content = null;
      return result;
    } finally {
      if (content != null) {
        content.close();
      }
    }
  }

  private InputStream processResponse(GDataResponse response)
      throws IOException, AtomException, XmlPullParserException {
    if (response.isSuccessStatusCode()) {
      return response.getContent();
    }
    AtomException exception = new AtomException(this, response);
    InputStream inputStream = response.getContent();
    if (inputStream != null) {
      try {
        if (response.getContentType().startsWith(CONTENT_TYPE)) {
          XmlPullParser parser = createParser(CONTENT_TYPE, inputStream);
          exception.parser = parser;
          exception.inputStream = inputStream;
        } else {
          exception.parseContent(inputStream, response.getContentLength());
        }
        inputStream = null;
      } finally {
        if (inputStream != null) {
          inputStream.close();
        }
      }
    }
    throw exception;
  }

  private XmlPullParser createParser(String contentType, InputStream content)
      throws XmlPullParserException {
    if (!contentType.startsWith(CONTENT_TYPE)) {
      throw new IllegalArgumentException("Wrong content type: expected <"
          + CONTENT_TYPE + "> but got <" + contentType + ">");
    }
    XmlPullParser result = parserFactory.createParser();
    if (!result.getFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES)) {
      throw new IllegalStateException("XmlGDataParser needs to have "
          + "namespace aware feature");
    }
    // TODO: make the XML pull parser secure!
    result.setInput(content, null);
    return result;
  }

  XmlSerializer createSerializer() throws XmlPullParserException {
    return parserFactory.createSerializer();
  }

  void setNamespacePrefixes(XmlSerializer serializer) throws IOException {
    // TODO: too many namespaces: should only use ones actually in use
    for (Map.Entry<String, String> entry : namespaceAliasToUriMap.entrySet()) {
      serializer.setPrefix(entry.getKey(), entry.getValue());
    }
  }

  String getNamespaceUriForAlias(String alias) {
    String result = namespaceAliasToUriMap.get(alias.intern());
    if (result == null) {
      throw new IllegalArgumentException("unrecognized alias: " + alias);
    }
    return result;
  }

  public String toString(String elementName, Object element) {
    try {
      StringWriter writer = new StringWriter();
      XmlSerializer serializer = createSerializer();
      serializer.setOutput(writer);
      serializer.startDocument(null, null);
      setNamespacePrefixes(serializer);
      serializeElement(serializer, elementName, element);
      serializer.endDocument();
      return writer.toString();
    } catch (IOException e) {
      throw new IllegalArgumentException(e);
    } catch (XmlPullParserException e) {
      throw new IllegalArgumentException(e);
    }
  }

  class ElementSerializer {
    Object textValue = null;
    final List<String> attributeNames = new ArrayList<String>();
    final List<Object> attributeValues = new ArrayList<Object>();
    final List<String> subElementNames = new ArrayList<String>();
    final List<Object> subElementValues = new ArrayList<Object>();

    ElementSerializer(Object elementValue) {
      Class<?> valueClass = elementValue.getClass();
      if (ClassInfo.isImmutable(valueClass)) {
        this.textValue = elementValue;
      } else {
        if (!(elementValue instanceof Map<?, ?>)) {
          ClassInfo classInfo = ClassInfo.of(elementValue.getClass());
          for (String name : classInfo.getNames()) {
            FieldInfo fieldInfo = classInfo.getFieldInfo(name);
            Object fieldValue = fieldInfo.getValue(elementValue);
            if (fieldValue != null) {
              set(name, fieldValue);
            }
          }
          if (elementValue instanceof AtomEntity) {
            elementValue = ((AtomEntity) elementValue).getUnknownKeyMap();
          }
        }
        if (elementValue instanceof Map<?, ?>) {
          @SuppressWarnings("unchecked")
          Map<String, Object> mapValue = (Map<String, Object>) elementValue;
          for (Map.Entry<String, Object> entry : mapValue.entrySet()) {
            set(entry.getKey().intern(), entry.getValue());
          }
        }
      }
    }

    void serialize(XmlSerializer serializer, String elementName)
        throws IOException {
      int colon = elementName.indexOf(':');
      String elementLocalName = elementName.substring(colon + 1);
      String elementNamespaceUri =
          getNamespaceUriForAlias(colon == -1 ? "" : elementName.substring(0,
              colon));
      serialize(serializer, elementNamespaceUri, elementLocalName);
    }

    void serialize(XmlSerializer serializer, String elementNamespaceUri,
        String elementLocalName) throws IOException {
      serializer.startTag(elementNamespaceUri, elementLocalName);
      // attributes
      List<String> attributeNames = this.attributeNames;
      List<Object> attributeValues = this.attributeValues;
      int num = attributeNames.size();
      for (int i = 0; i < num; i++) {
        String attributeName = attributeNames.get(i);
        int colon = attributeName.indexOf(':');
        String attributeLocalName = attributeName.substring(colon + 1);
        String attributeNamespaceUri =
            colon == -1 ? null : getNamespaceUriForAlias(attributeName
                .substring(0, colon));
        serializer.attribute(attributeNamespaceUri, attributeLocalName,
            toSerializedValue(attributeValues.get(i)));
      }
      // text
      Object textValue = this.textValue;
      if (textValue != null) {
        serializer.text(toSerializedValue(textValue));
      }
      // elements
      List<String> subElementNames = this.subElementNames;
      List<Object> subElementValues = this.subElementValues;
      num = subElementNames.size();
      for (int i = 0; i < num; i++) {
        Object subElementValue = subElementValues.get(i);
        String subElementName = subElementNames.get(i);
        if (subElementValue instanceof List<?>) {
          for (Object subElement : (List<?>) subElementValue) {
            serializeElement(serializer, subElementName, subElement);
          }
        } else {
          serializeElement(serializer, subElementName, subElementValue);
        }
      }
      serializer.endTag(elementNamespaceUri, elementLocalName);
    }

    private void set(String fieldName, Object fieldValue) {
      if (fieldName == "text()") {
        this.textValue = fieldValue;
      } else if (fieldName.charAt(0) == '@') {
        this.attributeNames.add(fieldName.substring(1));
        this.attributeValues.add(fieldValue);
      } else {
        this.subElementNames.add(fieldName);
        this.subElementValues.add(fieldValue);
      }
    }
  }

  static String toSerializedValue(Object value) {
    if (value instanceof Float) {
      Float f = (Float) value;
      if (f.floatValue() == Float.POSITIVE_INFINITY) {
        return "INF";
      }
      if (f.floatValue() == Float.NEGATIVE_INFINITY) {
        return "-INF";
      }
    }
    if (value instanceof Double) {
      Double d = (Double) value;
      if (d.doubleValue() == Double.POSITIVE_INFINITY) {
        return "INF";
      }
      if (d.doubleValue() == Double.NEGATIVE_INFINITY) {
        return "-INF";
      }
    }
    if (value instanceof String || value instanceof Number
        || value instanceof Boolean) {
      return value.toString();
    }
    if (value instanceof DateTime) {
      return ((DateTime) value).toStringRfc3339();
    }
    throw new IllegalArgumentException("unrecognized value type: "
        + value.getClass());
  }

  void serializeElement(XmlSerializer serializer, String elementName,
      Object elementValue) throws IOException {
    new ElementSerializer(elementValue).serialize(serializer, elementName);
  }

  void serializeElement(XmlSerializer serializer, String elementNamespaceUri,
      String elementLocalName, Object elementValue) throws IOException {
    new ElementSerializer(elementValue).serialize(serializer,
        elementNamespaceUri, elementLocalName);
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

  // TODO: make this public?
  /**
   * Parses an XML elment using the given XML pull parser into the given
   * destination object, and then closes the given input stream.
   * <p>
   * Requires the the current event be {@link XmlPullParser#START_DOCUMENT},
   * followed by the {@link XmlPullParser#START_TAG} of the element being
   * parsed.
   */
  void parseElementAndClose(XmlPullParser parser, InputStream inputStream,
      Object destination) throws IOException, XmlPullParserException {
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
  <T> T parseElement(XmlPullParser parser, Class<T> destinationClass,
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
  void parseElement(XmlPullParser parser, Object destination,
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
              && AtomClient.ATOM_NAMESPACE.equals(parser.getNamespace())) {
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

  private String getFieldName(boolean isAttribute, String alias,
      String namespace, String name) {
    alias = alias == null ? "" : alias.intern();
    namespace = namespace.intern();
    if (!isAttribute || alias != "") {
      String oldNamespace = this.namespaceAliasToUriMap.put(alias, namespace);
      if (oldNamespace != null && oldNamespace != namespace) {
        throw new IllegalArgumentException("alias <" + alias
            + "> was already assigned to <" + oldNamespace
            + "> but is now being assigned to <" + namespace + ">");
      }
    }
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
