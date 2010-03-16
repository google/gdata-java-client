/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.util.common.xml.parsing;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import java.io.StringReader;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.validation.Schema;

/**
 * Secures JDK-inbuilt Xerces parsers using the public APIs.
 *
 * 
 * 
 */
public class SecureGenericXMLFactory {

  private static final SecureEntityResolver NOOP_RESOLVER
      = new SecureEntityResolver();

  /* Supports customization via subclassing */
  protected SecureGenericXMLFactory() {}

  public static SAXParserFactory getSAXParserFactory(SAXParserFactory factory)
      throws ParserConfigurationException, SAXException {
    return new SecureSAXParserFactory(factory);
  }

  public static DocumentBuilderFactory getDocumentBuilderFactory(
      DocumentBuilderFactory factory) {
    return new SecureDocumentBuilderFactory(factory);
  }

  /**
   * Wraps an existing SAXParserFactory and ensures that any returned
   * SAXParser instances are secure.
   */
  static protected class SecureSAXParserFactory extends SAXParserFactory {

    private SAXParserFactory factory;

    /**
     * Constructs a new SecureSAXParserFactory instance that delegates
     * most functionality to an existing instance, but overrides where
     * needed to protect against XXE attacks.
     *
     * @param factory the existing SAXParserFactory that should be secured.
     * @throws ParserConfigurationException on configuration errors.
     * @throws SAXException on configuration failures.
     */
    protected SecureSAXParserFactory(SAXParserFactory factory)
        throws ParserConfigurationException, SAXException {

      this.factory = factory;

      /* Since we disable DTDs, we can't be validating. */
      factory.setValidating(false);

      /* This should be the default, but let's be safe and try and disable it.
       * We also have to cater for older XML parsers that do not support this.
       */
      try {
        factory.setXIncludeAware(false);
      } catch (UnsupportedOperationException e) {
        /* This is OK; older versions of the parser do not support XInclude at
         * all.
        */
      } catch (NoSuchMethodError e) {
        /* This is OK; older versions of the parser do not support XInclude at
         * all.  This is here for jdk 1.4 and earlier Xerces versions.
        */
      }

      /* Setting the attribute
       * http://apache.org/xml/features/disallow-doctype-decl to true causes an
       * immediate exception when a DTD is encountered. Unfortunately, an XML
       * document will sometimes include a harmless DTD so we cannot ban DTDs
       * outright.
       */
      try {
        factory.setFeature(
          "http://xml.org/sax/features/external-general-entities",
          false);
      } catch (IllegalArgumentException e) {
        /* OK.  Not all parsers will support this attribute */
      } catch (SAXNotRecognizedException e) {
        /* OK.  Not all parsers will support this attribute */
      }

      try {
        factory.setFeature(
          "http://xml.org/sax/features/external-parameter-entities",false);
      } catch (IllegalArgumentException e) {
        /* OK.  Not all parsers will support this attribute */
      } catch (SAXNotRecognizedException e) {
        /* OK.  Not all parsers will support this attribute */
      }
      try {
        factory.setFeature(
          "http://apache.org/xml/features/nonvalidating/load-external-dtd",
          false);
      } catch (IllegalArgumentException e) {
        /* OK.  Not all parsers will support this attribute */
      } catch (SAXNotRecognizedException e) {
        /* OK.  Not all parsers will support this attribute */
      }

      /* Again, older XML parsers do not support this. */
      try {
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
      } catch (IllegalArgumentException e) {
        /* OK.  Not all parsers will support this attribute */
      } catch (SAXNotRecognizedException e) {
        /* OK.  Not all parsers will support this attribute */
      }
    }

    @Override
    public SAXParser newSAXParser()
        throws ParserConfigurationException, SAXException {

      SAXParser parser = factory.newSAXParser();
      XMLReader xmlReader = parser.getXMLReader();
      xmlReader.setEntityResolver(NOOP_RESOLVER);

      return parser;
    }

    @Override
    public void setNamespaceAware(boolean awareness) {
      factory.setNamespaceAware(awareness);
    }

    @Override
    public void setValidating(boolean validating) {
      factory.setValidating(validating);
    }

    @Override
    public boolean isNamespaceAware() {
      return factory.isNamespaceAware();
    }

    @Override
    public boolean isValidating() {
      return factory.isValidating();
    }

    @Override
    public void setFeature(String name, boolean value)
        throws ParserConfigurationException, SAXNotRecognizedException,
               SAXNotSupportedException {
      factory.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name)
        throws ParserConfigurationException, SAXNotRecognizedException,
        SAXNotSupportedException {
      return factory.getFeature(name);
    }

    @Override
    public Schema getSchema() throws UnsupportedOperationException {
      return factory.getSchema();
    }

    @Override
    public void setSchema(Schema schema) throws UnsupportedOperationException {
      factory.setSchema(schema);
    }

    @Override
    public void setXIncludeAware(boolean state)
        throws UnsupportedOperationException {
      factory.setXIncludeAware(state);
    }

    @Override
    public boolean isXIncludeAware() throws UnsupportedOperationException {
      return factory.isXIncludeAware();
    }
  }

  /**
   * Wraps an existing DocumentBuilderFactory and ensures that any returned
   * DocumentBuilder instances are secure.
   */
  static protected class SecureDocumentBuilderFactory
      extends DocumentBuilderFactory {

    private DocumentBuilderFactory factory;

    /**
     * Constructs a new SecureDocumentBuilderFactory instance that delegates
     * most functionality to an existing instance, but overrides where
     * needed to protect against XXE attacks.
     *
     * @param factory the existing DocumentBuilderFactory that should be
     *        secured.
     */
    protected SecureDocumentBuilderFactory(DocumentBuilderFactory factory) {
      this.factory = factory;

      /* Since we disable DTDs, we can't be validating. */
      factory.setValidating(false);

      /* This should be the default, but let's be safe and try and disable it.
       * We also have to cater for older XML parsers that do not support this.
       */
      try {
        factory.setXIncludeAware(false);
      } catch (UnsupportedOperationException e) {
        /* This is OK; older versions of the parser do not support XInclude at
         * all.
         */
      } catch (NoSuchMethodError e) {
        /* This is OK; older versions of the parser do not support XInclude at
         * all.  This is here for jdk 1.4 and earlier Xerces versions.
         */
      }

      /* Setting the attribute
       * http://apache.org/xml/features/disallow-doctype-decl to true causes an
       * immediate exception when a DTD is encountered. Unfortunately, an XML
       * document will sometimes include a harmless DTD so we cannot ban DTDs
       * outright.
       */
      try {
        factory.setAttribute(
          "http://xml.org/sax/features/external-general-entities", false);
      } catch (IllegalArgumentException e) {
        /* OK.  Not all parsers will support this attribute */
      }
      try {
        factory.setAttribute(
          "http://xml.org/sax/features/external-parameter-entities", false);
      } catch (IllegalArgumentException e) {
        /* OK.  Not all parsers will support this attribute */
      }
      try {
        factory.setAttribute(
          "http://apache.org/xml/features/nonvalidating/load-external-dtd",
          false);
      } catch (IllegalArgumentException e) {
        /* OK.  Not all parsers will support this attribute */
      }

      /* Again, older XML parsers do not support this. */
      try {
        factory.setAttribute(XMLConstants.FEATURE_SECURE_PROCESSING,
                             Boolean.TRUE);
      } catch (IllegalArgumentException e) {
        /* OK.  Not all parsers will support this attribute */
      }
    }

    @Override
    public DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
      DocumentBuilder docBuilder = factory.newDocumentBuilder();
      docBuilder.setEntityResolver(NOOP_RESOLVER);
      return docBuilder;
    }

    @Override
    public void setNamespaceAware(boolean awareness) {
      factory.setNamespaceAware(awareness);
    }

    @Override
    public void setValidating(boolean validating) {
      factory.setValidating(validating);
    }

    @Override
    public void setIgnoringElementContentWhitespace(boolean whitespace) {
      factory.setIgnoringElementContentWhitespace(whitespace);
    }

    @Override
    public void setExpandEntityReferences(boolean expandEntityRef) {
      factory.setExpandEntityReferences(expandEntityRef);
    }

    @Override
    public void setIgnoringComments(boolean ignoreComments) {
      factory.setIgnoringComments(ignoreComments);
    }

    @Override
    public void setCoalescing(boolean coalescing) {
      factory.setCoalescing(coalescing);
    }

    @Override
    public boolean isNamespaceAware() {
      return factory.isNamespaceAware();
    }

    @Override
    public boolean isValidating() {
      return factory.isValidating();
    }

    @Override
    public boolean isIgnoringElementContentWhitespace() {
      return factory.isIgnoringElementContentWhitespace();
    }

    @Override
    public boolean isExpandEntityReferences() {
      return factory.isExpandEntityReferences();
    }

    @Override
    public boolean isIgnoringComments() {
      return factory.isIgnoringComments();
    }

    @Override
    public boolean isCoalescing() {
      return factory.isCoalescing();
    }

    @Override
    public void setAttribute(String name, Object value) throws IllegalArgumentException {
      factory.setAttribute(name, value);
    }

    @Override
    public Object getAttribute(String name) throws IllegalArgumentException {
      return factory.getAttribute(name);
    }

    @Override
    public void setFeature(String name, boolean value) throws ParserConfigurationException {
      factory.setFeature(name, value);
    }

    @Override
    public boolean getFeature(String name) throws ParserConfigurationException {
      return factory.getFeature(name);
    }

    @Override
    public Schema getSchema() throws UnsupportedOperationException {
      return factory.getSchema();
    }

    @Override
    public void setSchema(Schema schema) throws UnsupportedOperationException {
      factory.setSchema(schema);
    }

    @Override
    public void setXIncludeAware(boolean state) throws UnsupportedOperationException {
      factory.setXIncludeAware(state);
    }

    public boolean isIncludeAware() throws UnsupportedOperationException {
      return factory.isXIncludeAware();
    }
  }


  /**
   * A secure EntityResolver that returns an empty string in response to
   * any attempt to resolve an external entitity. The class is used by our
   * secure version of the internal saxon SAX parser.
   */
  private static final class SecureEntityResolver implements EntityResolver {

    public InputSource resolveEntity(String publicId, String systemId) {
      return new InputSource(new StringReader(""));
    }
  }
}
