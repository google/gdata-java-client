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


package com.google.gdata.data;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.Attribute;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Person type used for feed and entry authors and contributors. It may also
 * be used by services' custom elements.
 *
 * 
 */
public class Person extends ExtensionPoint implements IPerson {

  /**
   * Class constructor.
   */
  public Person() {}

  /**
   * Constructs a new Person instance with the specified name.
   */
  public Person(String name) {
    if (name == null)
      throw new NullPointerException("Name must have a value");
    this.name = name;
  }

  /**
   * Constructs a new Person instance with the specified name, URI,
   * and email address.
   */
  public Person(String name, String uri, String email) {
    this(name);
    this.uri = uri;
    this.email = email;
  }

  /** Human-readable name. */
  protected String name;
  public String getName() { return name; }
  public void setName(String v) { name = v; }

  /** Language of name. Derived from the current state of {@code xml:lang}. */
  protected String nameLang;
  public String getNameLang() { return nameLang; }
  public void setNameLang(String v) { nameLang = v; }

  /** URI associated with the person. */
  protected String uri;
  public String getUri() { return uri; }
  public void setUri(String v) { uri = v; }

  /** Email address. */
  protected String email;
  public String getEmail() { return email; }
  public void setEmail(String v) { email = v; }

  /**
   * Generates XML.
   * <p>
   * Designed to be used by types that reuse the standard Atom person type.
   *
   * @param   extProfile
   *            extension profile
   *
   * @param   w
   *            output writer
   *
   * @param   elementNamespace
   *            namespace for XML element
   *
   * @param   elementName
   *            name of XML element
   *
   * @param   attributes
   *            additional attributes
   *
   * @throws  IOException
   */
  public void generate(ExtensionProfile extProfile,
                       XmlWriter w,
                       XmlNamespace elementNamespace,
                       String elementName,
                       Collection<XmlWriter.Attribute> attributes)
      throws IOException {

    generateStartElement(w, elementNamespace, elementName, attributes, null);

    if (name != null && name.trim().length() > 0) {

      ArrayList<XmlWriter.Attribute> attrs;

      if (nameLang != null) {
        attrs = new ArrayList<XmlWriter.Attribute>(1);
        attrs.add(new XmlWriter.Attribute("xml:lang", nameLang));
      } else {
        attrs = null;
      }

      w.simpleElement(Namespaces.atomNs, "name", attrs, name);
    }

    if (uri != null && uri.trim().length() > 0) {
      w.simpleElement(Namespaces.atomNs, "uri", null, uri);
    }

    if (email != null && email.trim().length() > 0) {
      w.simpleElement(Namespaces.atomNs, "email", null, email);
    }

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(elementNamespace, elementName);
  }


  /**
   * Generates XML.
   * <p>
   * Designed to be used by {@link ExtensionPoint} types that reuse Person.
   */
  @Override
  protected void generate(XmlWriter w, ExtensionProfile p,
      XmlNamespace namespace, String localName, List<Attribute> attrs,
      AttributeGenerator generator) throws IOException {
    generate(p, w, namespace, localName, attrs);
  }

  /**
   * Generates XML in the Atom format.
   *
   * @param   extProfile
   *            extension profile
   *
   * @param   w
   *            output writer
   *
   * @param   elementName
   *            Atom element name
   *
   * @throws  IOException
   */
  public void generateAtom(ExtensionProfile extProfile,
                           XmlWriter w,
                           String elementName) throws IOException {

    generate(extProfile, w, Namespaces.atomNs, elementName, null);
  }

  /**
   * Generates XML in the RSS format.
   *
   * @param   w
   *            output writer
   *
   * @param   elementName
   *            RSS element name
   *
   * @throws  IOException
   */
  public void generateRss(XmlWriter w,
                          String elementName) throws IOException {

    String text = new String();
    if (email != null) { text += email; }

    if (name != null) {
      if (email != null) { text += " ("; }
      text += name;
      if (email != null) { text += ")"; }
    }

    w.simpleElement(Namespaces.rssNs, elementName, null, text);
  }

  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs) {
    return new AtomHandler(extProfile);
  }

  /** Parses XML in the Atom format. */
  public class AtomHandler extends ExtensionPoint.ExtensionHandler {


    public AtomHandler(ExtensionProfile extProfile) {
      super(extProfile, Person.this.getClass());
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {


      if (namespace.equals(Namespaces.atom)) {
        if (localName.equals("name")) {
          return new NameHandler();
        } else if (localName.equals("uri")) {
          return new UriHandler();
        } else if (localName.equals("email")) {
          return new EmailHandler();
        }
      } else {
        return super.getChildHandler(namespace, localName, attrs);
      }
      return null;
    }

    class NameHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() throws ParseException {

        if (name != null) {
          throw new ParseException(
              CoreErrorDomain.ERR.duplicateName);
        }

        if (value == null) {
          throw new ParseException(
              CoreErrorDomain.ERR.nameValueRequired);
        }

        name = value;
        nameLang = xmlLang;
      }
    }

    class UriHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() throws ParseException {

        if (uri != null) {
          throw new ParseException(
              CoreErrorDomain.ERR.duplicateUri);
        }

        if (value == null) {
          throw new ParseException(
              CoreErrorDomain.ERR.uriValueRequired);
        }

        uri = value;
      }
    }

    class EmailHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() throws ParseException {

        if (email != null) {
          throw new ParseException(
              CoreErrorDomain.ERR.duplicateEmail);
        }

        if (value == null) {
          throw new ParseException(
              CoreErrorDomain.ERR.emailValueRequired);
        }

        email = value;
      }
    }
  }
}
