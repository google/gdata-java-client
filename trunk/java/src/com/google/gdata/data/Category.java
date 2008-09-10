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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Category type.
 * <p>
 * For the purposes of comparison, two Category instances are considered
 * to be identical if they have matching schemes and terms.  The label
 * attributes <em>are not</em> used for the purpose of testing equality.
 *
 * 
 */
public class Category implements ICategory {

  /**
   * The character used to prefix any (optional) scheme in the compound
   * scheme+term Category format.
   */
  public static final char SCHEME_PREFIX = '{';

  /**
   * The character used to suffix any (optional) scheme in the compound
   * scheme+term Category format.
   */
  public static final char SCHEME_SUFFIX = '}';


  public Category() {}

  // A simple pattern matcher for the "{scheme}term" syntax
  private static final Pattern categoryPattern =
    Pattern.compile("(\\{([^\\}]+)\\})?(.+)");


  /**
   * Constructs a new category from a Category string. The format of
   * the String is the same as the one used to represent a category in
   * a GData query: an optional scheme surrounded by braces, followed
   * by a term.
   *
   * @param category the category string
   */
  public Category(String category) {
    Matcher m = categoryPattern.matcher(category);
    if (!m.matches()) {
      throw new IllegalArgumentException("Invalid category: " + category);
    }

    if (m.group(2) != null) {
      scheme = m.group(2);
    }

    term = m.group(3);
  }


  /**
   * Constructs a new category.
   */
  public Category(String scheme, String term) { this(scheme, term, null); }

  /**
   * Constructs a new category.
   */
  public Category(String scheme, String term, String label) {
    this.scheme = scheme;
    if (term == null) {
      throw new NullPointerException("Invalid term. Cannot be null");
    }
    this.term = term;
    this.label = label;
  }

  /** Scheme (domain). */
  protected String scheme;
  public String getScheme() { return scheme; }
  public void setScheme(String v) { scheme = v; }

  /** Term. */
  protected String term;
  public String getTerm() { return term; }
  public void setTerm(String v) { term = v; }

  /** Human-readable label. */
  protected String label;
  public String getLabel() { return label; }
  public void setLabel(String v) { label = v; }

  /** Language. */
  protected String labelLang;
  public String getLabelLang() { return labelLang; }
  public void setLabelLang(String v) { labelLang = v; }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder();
    if (scheme != null) {
      sb.append(SCHEME_PREFIX);
      sb.append(scheme);
      sb.append(SCHEME_SUFFIX);
    }
    // Label syntax is not in the query model, so no need to define
    // public constants for the delimiters.
    sb.append(term);
    if (label != null) {
      sb.append("(");
      sb.append(label);
      sb.append(")");
    }
    return sb.toString();
  }

  // identical scheme/term values for all user-defined labels.  The label
  // attribute is being used for the user label.  This seems somewhat counter
  // to Atom semantics, where the scheme is a namespace and the term
  // identifies membership in the categories in that scheme.   Needs review,
  // but until this is done the label must be taken into account for
  // equals()/hashCode()
  @Override
  public boolean equals(Object obj) {
    if (! (obj instanceof Category)) {
      return false;
    }

    return toString().equals(obj.toString());
  }

  @Override
  public int hashCode() {
    int result = 17;
    result = 37 * result + ((scheme != null) ? scheme.hashCode() : 0);
    result = 37 * result + term.hashCode();
    result = 37 * result + ((label != null) ? label.hashCode() : 0);
    return result;
  }

  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            Output writer.
   *
   * @throws  IOException
   */
  public void generateAtom(XmlWriter w) throws IOException {

    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>(3);

    if (scheme != null) {
      attrs.add(new XmlWriter.Attribute("scheme", scheme));
    }

    if (term != null) {
      attrs.add(new XmlWriter.Attribute("term", term));
    }

    if (label != null) {
      attrs.add(new XmlWriter.Attribute("label", label));
    }

    if (labelLang != null) {
      attrs.add(new XmlWriter.Attribute("xml:lang", labelLang));
    }

    w.simpleElement(Namespaces.atomNs, "category", attrs, null);
  }

  /**
   * Generates XML in the RSS format.
   *
   * @param   w
   *            Output writer.
   *
   * @throws  IOException
   */
  public void generateRss(XmlWriter w) throws IOException {

    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>(3);

    if (scheme != null) {
      attrs.add(new XmlWriter.Attribute("domain", scheme));
    }

    if (labelLang != null) {
      attrs.add(new XmlWriter.Attribute("xml:lang", labelLang));
    }

    String value = term;
    if (term == null) {
      value = label;
    }

    w.simpleElement(Namespaces.rssNs, "category", attrs, value);
  }

  /** {@code <atom:category>} parser. */
  public class AtomHandler extends XmlParser.ElementHandler {

    Set<Category> categorySet;
    ExtensionProfile extProfile;
    Kind.Adaptable adaptable;

    public AtomHandler() {}

    /** Constructor used when parsing a Category for a source or entry  */
    public AtomHandler(ExtensionProfile extProfile,
                       Set<Category> categorySet,
                       Kind.Adaptable adaptable) {
      this.extProfile = extProfile;
      this.categorySet = categorySet;
      this.adaptable = adaptable;
    }

    @Override
    public void processAttribute(String namespace,
                                 String localName,
                                 String value) {

      if (namespace.equals("") && localName.equals("scheme")) {
        scheme = value;
      } else if (namespace.equals("") && localName.equals("term")) {
        term = value;
      } else if (namespace.equals("") && localName.equals("label")) {
        label = value;
      }
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs) {
      // Allow undefined extensions.
      return null;
    }

    @Override
    public void processEndElement() throws ParseException {

      if (term == null) {
        throw new ParseException(
           CoreErrorDomain.ERR.missingTermAttribute);
      }

      labelLang = xmlLang;

      // Allow undefined content.

      // Because categories are stored in a hashed collection, they can't
      // be stored until fully initialized.
      if (categorySet != null) {
        categorySet.add(Category.this);
      }

      // If parsing an Adaptable type and the parsing extension profile
      // supports autoextension and a Kind category is being parsed, then do
      // kind-based autoextension.
      if (adaptable != null &&
          extProfile.isAutoExtending() &&
          Kind.isKindCategory(Category.this)) {

        try {
          Kind.Adaptor adaptor = Kind.getAdaptor(term, adaptable);
          if (adaptor != null) {
              extProfile.addDeclarations(adaptor);
          }
        } catch (Kind.AdaptorException ae) {
          throw new ParseException(
              CoreErrorDomain.ERR.cantLoadKindAdaptor, ae);
        }
      }
    }
  }
}
