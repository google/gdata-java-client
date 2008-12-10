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


package com.google.gdata.data.introspection;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.Attribute;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Categories class implements the data model for the AtomPub categories
 * element, as described in Sec 7.2.1 of the AtomPub specification.
 * 
 * 
 */
public class Categories extends ExtensionPoint {
  
  // Locally cache the version-appropriate AtomPub namespace information.
  private XmlNamespace atomPubNs = Namespaces.getAtomPubNs();

  /**
   * Constructs a new empty Categories instance for the purposes of parsing
   * an AtomPub categories element or document.
   * 
   * @see #parseAtom(ExtensionProfile, XmlParser)
   */
  public Categories() {}
  
  /**
   * Constructs a Categories instance with in-line category descriptions.
   * 
   * @param fixed {@code true} if the list of categories is a fixed set,
   *        {@code false} if an open set.
   * @param defaultScheme the default scheme uri value that will be used for all
   *        nested category elements that do not have a scheme.
   * @param categories list of categories.
   */
  public Categories(boolean fixed, String defaultScheme, 
      Category ... categories) {
    this.fixed = fixed;
    this.defaultScheme = defaultScheme;
    if (categories.length != 0) {
      this.categories = Arrays.asList(categories);
    }
  }
  
  /**
   * Constructs a Categories instance that references an out-of-line list of
   * categories stored in a category document.
   * @param href
   */
  public Categories(String href) {
    this.href = href;
  }

  private Boolean fixed;
  private String defaultScheme;
  private String href;
  private List<Category> categories;
  
  /**
   * Returns {@code true} if the Categories instance contains in-line category
   * data that is a fixed set.
   * 
   * @return {@code true} there is a fixed set of categories.
   */
  public boolean isFixed() { return fixed != null && fixed.booleanValue(); }
  
  /**
   * Returns the default scheme used for nested categories if not specified
   * directly on the instance.  May be {@code null} if there is no default
   * scheme.
   * @return default scheme uri or {@code null}.
   */
  public String getDefaultScheme() { return defaultScheme; }
  
  /**
   * Returns the location of an external AtomPub categories document that
   * describes the list of categories.
   * @return uri of external Categories document or {@code null} if none.
   */
  public String getHref() { return href; }

  /**
   * Returns the list of in-line categories or {@code null} if there is no
   * associated list.
   * 
   * @return category list or {@code null}.
   */
  public List<Category> getCategoryList() { return categories; }
  
  /**
   * Adds a new category to the category list.
   * 
   * @param category new category to add.
   */
  public void addCategory(Category category) {
    if (categories == null) {
      categories = new ArrayList<Category>();
    }
    categories.add(category);
  }
  
  @Override
  public XmlParser.ElementHandler getHandler(ExtensionProfile p,
      String namespace, String localName, Attributes attrs) {
    return new Handler(p, attrs);
  }
 
  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    href = helper.consume("href", false);
    defaultScheme = helper.consume("scheme", false);
    String fixedValue = helper.consume("fixed", false);
    if (fixedValue != null) {
      if ("yes".equals(fixedValue)) {
        fixed = true;
      } else if ("no".equals(fixedValue)) {
        fixed = false;
      } else {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.invalidFixedAttribute);
        pe.setInternalReason("Invalid value for fixed attribute:" +
            fixedValue);
        throw pe;
      }
    }
  }
  
  @Override
  public void validate() throws IllegalStateException {
    if (href != null && 
        (fixed != null || defaultScheme != null || categories != null)) {
      throw new IllegalStateException("The href attribute cannot be used with " +
              "other attributes or nested category elements");
    }
  }

  /**
   * Parses a Categories element using data read from the specified parser
   * instance.
   * 
   * @param extProfile Extension profile.
   * @param parser XML input parse.
   */
  public void parseAtom(ExtensionProfile extProfile,
                        XmlParser parser) throws IOException,
                                              ParseException {

    Handler handler = new Handler(extProfile, null);
    parser.parse(handler, atomPubNs.getUri(), "categories");
  }
  
  /**
   * Generates XML.
   *
   * @param   w
   *            output writer
   *
   * @throws  IOException
   */
  @Override
  public void generate(XmlWriter w, ExtensionProfile extProfile) 
      throws IOException {

    List<Attribute> attrs = new ArrayList<Attribute>();
    if (fixed != null) {
      attrs.add(new Attribute("fixed", fixed ? "yes" : "no"));
    }
    if (defaultScheme != null) {
      attrs.add(new Attribute("scheme", defaultScheme));
    }
    if (href != null) {
      attrs.add(new Attribute("href", href));
    }
    w.startElement(atomPubNs, "categories", attrs, null);

    if (categories != null) {
      w.startRepeatingElement();
      for (Category category : categories) {
        category.generateAtom(w);
      }
      w.endRepeatingElement();
    }

    generateExtensions(w, extProfile);

    w.endElement(atomPubNs, "categories");
  }

  /**
   * The Handler class implements the {@link XmlParser.ElementHandler} for 
   * parsing an {@code app:categories} element.
   */
  public class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile extProfile, Attributes attrs) {
      super(extProfile, Categories.class, attrs);
    }
    
    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.atom)) {

        if (localName.equals("category")) {

          Category category = new Category();
          addCategory(category);
          return category.new AtomHandler();
        }
      }
      return super.getChildHandler(namespace, localName, attrs);
    }
 
  }
}
