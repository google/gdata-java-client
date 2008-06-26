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


package com.google.gdata.data.spreadsheet;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Extension that globs together all "gsx:" tags into one.
 * 
 * Designed for row-based access to spreadsheets.
 * 
 * extra tags.  Another option is overriding ListEntry's getExtensionHandler
 * method.  Finally, this might be embedded as a private class inside of
 * ListEntry.
 *
 * 
 */
public class CustomElementCollection implements Extension {

  /**
   * Internal representation of a custom element.  This allows
   * for the addition of attributes.
   */
  private class CustomElement {

    /** The value of the custom element. */
    private String value;

    /**
     * Creates a custom element with only the value set.
     *
     * @param value the value of this custom element
     */
    public CustomElement(String value) {
      this.value = value;
    }

    /**
     * Creates a custom element with the value and the comment set.
     *
     * @param value the value of the custom element
     */
    public CustomElement(String value, String comment) {
      this.value = value;
    }

    /**
     * Returns the value of the custom element.
     *
     * @return value of the custom element
     */
    public String getValue() {
      return value;
    }

  }
  
  /**
   * All values, indexed by the header column.
   * 
   * For instance, the pair "name", "bill" would mean that the cell in
   * the column labelled "name" has the text "bill".
   */
  private Map<String, CustomElement> values =
      new LinkedHashMap<String, CustomElement>();
  
  /**
   * Gets the text at the cell, whose column is named columnHeader.
   * 
   * For instance, if the spreadsheet has a column "Name", then calling
   * getValue("name") will return the selected row's cell in that name
   * column.
   * 
   * @param columnHeader the lowercase, stripped version of the column header
   * @return the contents of the cell
   */
  public String getValue(String columnHeader) {
    CustomElement element = values.get(columnHeader.toLowerCase());
    
    if (element == null) {
      return null;
    }
    
    return element.getValue();
  }

  /**
   * Locally sets the value at the particular cell, specified by the
   * column name.
   * 
   * For instance, setValueLocal("name", "Ensulato") will change the
   * name column of this row to "Ensulato".  To commit the change,
   * you must .
   * 
   * The particular column you specify must already exist
   * in the spreadsheet; new columns are not automatically added.
   * 
   * @param columnHeader the header column (must already exist)
   * @param newContents the new contents; may not start with an '=' sign
   * @throws IllegalArgumentException if the contents begins with an equals
   *         sign
   * 
   * if a non-existent columnHeader is used.  But, when adding a new
   * entry, there needs to be a way to locally know the schema.
   */
  public void setValueLocal(String columnHeader, String newContents) {
    if (newContents.startsWith("=")) {
      throw new IllegalArgumentException("Formulas are not supported.");
    } else {
      values.put(columnHeader.toLowerCase(), 
          new CustomElement(newContents));
    }
  }
  
  /**
   * Locally clears the particular value.
   * 
   * Note that if the entire row is cleared, the entry will be rejected.
   * You must actually delete the row.
   * 
   * @param columnHeader the column header to clear.
   */
  public void clearValueLocal(String columnHeader) {
    values.remove(columnHeader.toLowerCase());
  }
  
  /**
   * Locally clears all existing values and copies the contents of the other
   * element collection over.
   *
   * @param other the custom element collection
   */
  public void replaceWithLocal(CustomElementCollection other) {
    values.clear();
    values.putAll(other.values);
  }
  
  /**
   * Gets a list of all tags that are set for this entry.
   * The initial order of the tags is preserved.
   * 
   * For instance, this might return an iterable with "name", "address",
   * "manager", "employeeid".
   * 
   * @return an Iterable with all the different tags for getValue
   */
  public Set<String> getTags() {
    return values.keySet();
  }
  
  /**
   * Returns the suggested extension description.
   */
  public static ExtensionDescription getDefaultDescription() {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(CustomElementCollection.class);
    desc.setNamespace(Namespaces.gSpreadCustomNs);
    desc.setLocalName("*");
    desc.setAggregate(true);
    return desc;
  }
  
  /**
   * Writes this cell as XML, omitting any unspecified fields.
   */
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    
    for (Map.Entry<String, CustomElement> entry : values.entrySet()) {

      ArrayList<XmlWriter.Attribute> attrs =
        new ArrayList<XmlWriter.Attribute>();

      // Attributes - if there were any
      
      w.simpleElement(Namespaces.gSpreadCustomNs, entry.getKey(),
          attrs, entry.getValue().getValue());
    }
  }
  
  /**
   * Yields an XML handler for parsing a Cell element.
   */
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs)
      throws ParseException, IOException {
    return new CustomElementHandler(localName);
  }
  
  /**
   * Parses custom gx: tags.
   *
   * 
   */
  private class CustomElementHandler extends ElementHandler {
    
    /**
     * The name of the custom tag.
     */
    private String tagName;
    
    /**
     * Constructs the handler, given the name of the element in use.
     */
    public CustomElementHandler(String tagName) {
      this.tagName = tagName;
    }
    
    /**
     * Processes an attribute.
     * 
     * Currently, no attributes are used (only the main text).
     */
    public void processAttribute(String namespace,
                                 String localName,
                                 String value)
        throws ParseException {
      
        // If there were any
    }
    
    public void processEndElement() throws ParseException {
      if (value == null) {
        values.put(tagName, new CustomElement(null));
      } else {
        values.put(tagName, new CustomElement(value));
      }
    }
  }
}
