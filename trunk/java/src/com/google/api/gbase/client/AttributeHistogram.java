/* Copyright (c) 2006 Google Inc.
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

package com.google.api.gbase.client;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Histogram information for one attribute, described in
 * an entry using attributes in the gm: namespace.
 *
 * Make sure the current feed is an histogram feed and then get the
 * AttributeHistogram for the entry using:
 * {@link GoogleBaseEntry#getGoogleBaseMetadata()}.getAttributeHistogram().
 */
@ExtensionDescription.Default(
    nsAlias = GoogleBaseNamespaces.GM_ALIAS,
    nsUri = GoogleBaseNamespaces.GM_URI,
    localName = "attribute")
public class AttributeHistogram implements Extension {

  private GoogleBaseAttributeId attributeId;
  private int totalValueCount;
  private final List<UniqueValue> values = new ArrayList<UniqueValue>();

  /**
   * Creates an unitialized AttributeHistogram.
   */
  public AttributeHistogram() {

  }

  /**
   * Creates and initializes an AttributeHistogram.
   *
   * @param attributeName attribute name
   * @param attributeType attribute type
   */
  public AttributeHistogram(String attributeName,
      GoogleBaseAttributeType attributeType) {
    attributeId = new GoogleBaseAttributeId(attributeName, attributeType);
  }

  /**
   * Creates and initializes an AttributeHistogram.
   *
   * @param attributeId attribute Id
   */
  public AttributeHistogram(GoogleBaseAttributeId attributeId) {
    this.attributeId = attributeId;
  }

  /**
   * Gets the name of the attribute this histogram describes.
   *
   * @return attribute name
   */
  public String getAttributeName() {
    return attributeId.getName();
  }

  /**
   * Gets the type of the attribute this histogram describes.
   *
   * @return attribute type
   */
  public GoogleBaseAttributeType getAttributeType() {
    return attributeId.getType();
  }

  /**
   * Gets the name and type of the attribute this histogram describes.
   *
   * @return attribute id
   */
  public GoogleBaseAttributeId getAttributeId() {
    return attributeId;
  }


  /**
   * Gets the total number of values found for this attribute in
   * the result set for the query.
   *
   * This is not the total number of unique values, just the number
   * of times this attribute was set.
   *
   * @return total number of values found for this attribute, always
   *   {@code >= sum(getValues().getCount())}
   */
  public int getTotalValueCount() {
    return totalValueCount;
  }

  /**
   * Gets a list of unique values for the attribute and
   * the count for these values.
   *
   * Not all unique values might be available. For some types,
   * no values are ever available.
   *
   * @return a list of values and the number of time they
   *   were found in the result set, never null
   */
  public List<? extends UniqueValue> getValues() {
    return values;
  }

  /**
   * Gets a list of unique values for the attribute and the count
   * of these values, for values repeated at least a certain number
   * of times.
   *
   * @param minimumCount minimum number of times the value should
   *   have been encountered in the result set to matter
   * @return a list of values and the number of time they
   *   were found in the result set, never null
   */
  public List<? extends UniqueValue> getValues(int minimumCount) {
    if (minimumCount <= 0) {
      return values;
    }
    List<UniqueValue> retval = new ArrayList<UniqueValue>(values.size());
    for (UniqueValue value : values) {
      if (value.getCount() >= minimumCount) {
        retval.add(value);
      }
    }
    return retval;
  }

  /**
   * Adds a new value into the histogram.
   * @param count number of time the value was found
   * @param stringRepresentation
   * @exception IllegalArgumentException unless count is greater than 0
   * @exception NullPointerException if stringRepresentation is null
   */
  public void addValue(int count, String stringRepresentation) {
    values.add(new UniqueValue(count, stringRepresentation));
  }

  /**
   * Sets the total value count.
   */
  public void setTotalValueCount(int count) {
    this.totalValueCount = count;
  }


  /**
   * Sets attribute name and type.
   */
  public void setAttributeId(String name, GoogleBaseAttributeType type) {
    setAttributeId(new GoogleBaseAttributeId(name, type));
  }

  /**
   * Sets attribute name and type.
   */
  public void setAttributeId(GoogleBaseAttributeId attributeId) {
    this.attributeId = attributeId;
  }


  /**
   * Generates the XML representation for this tag.
   *
   * @param w XML writer
   * @param extProfile extension profile
   * @throws IOException thrown if there was an error writing to the XmlWriter
   */
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    if (attributeId == null) {
      return;
    }

    List<XmlWriter.Attribute> attrs = new ArrayList<XmlWriter.Attribute>();

    attrs.add(new XmlWriter.Attribute("name", attributeId.getName()));
    
    if (attributeId.getType() != null) {
      attrs.add(
          new XmlWriter.Attribute("type", attributeId.getType().toString()));
    }
    if (totalValueCount > 0) {
      attrs.add(
          new XmlWriter.Attribute("count", Integer.toString(totalValueCount)));
    }
    w.startElement(GoogleBaseNamespaces.GM, "attribute", attrs, null);

    if (values != null) {
      w.startRepeatingElement();
      for (AttributeHistogram.UniqueValue value : values) {
        value.generate(w);
      }
      w.endRepeatingElement();
    }

    w.endElement();
  }

  /**
   * Creates a handler for this gdata extension tag.
   *
   * @param extProfile {@inheritDoc}
   * @param namespace {@inheritDoc}
   * @param localName {@inheritDoc}
   * @param attrs {@inheritDoc}
   * @return {@inheritDoc}
   * @throws ParseException {@inheritDoc}
   * @throws IOException {@inheritDoc}
   */
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs)
      throws ParseException, IOException {
    AttributeHelper helper = new AttributeHelper(attrs);

    GoogleBaseAttributeType type =
        GoogleBaseAttributeType.getInstance(helper.consume("type", true));
    setAttributeId(
        new GoogleBaseAttributeId(helper.consume("name", true), type));

    setTotalValueCount(helper.consumeInteger("count", false, 0));

    helper.assertAllConsumed();
    
    return new XmlParser.ElementHandler() {
      @Override
      public XmlParser.ElementHandler getChildHandler(String namespace,
          String localName,
          Attributes attrs)
          throws ParseException, IOException {
        if (namespace.equals(GoogleBaseNamespaces.GM_URI) 
            && localName.equals("value")) {
          return new AddValueHandler(attrs);
        } else {
          return super.getChildHandler(namespace, localName, attrs);
        }
      }
    };
  }

  /**
   * A value, as a string, and the number of times the value appears
   * in the result set for the current query.
   */
  public static class UniqueValue {
    private final int count;
    private final String value;

    /**
     * Creates a new UniqueValue object.
     *
     * @param count number of time the value was found
     * @param value the value, as a string
     */
    private UniqueValue(int count, String value) {
      this.count = count;
      this.value = value;
    }

    /**
     * Gets the number of time this specific value was found.
     */
    public int getCount() {
      return count;
    }

    /**
     * Gets the value itself, as a string.
     *
     * @return string representation for the value
     */
    public String getValueAsString() {
      return value;
    }

    /**
     * Gets the value itself, as a string.
     */
    public String toString() {
      return value;
    }

    /**
     * Generates the XML representation for this tag.
     *
     * @param w XML writer
     */
    void generate(XmlWriter w) throws IOException {
      List<XmlWriter.Attribute> attrs = null;
      if (count > 0) {
        attrs = Collections.singletonList(
            new XmlWriter.Attribute("count", Integer.toString(count)));
      }
      w.simpleElement(GoogleBaseNamespaces.GM, "value", attrs, value);
    }
  }


  /**
   * Handles one gm:value tag and use it to add a value into a specific
   * {@link com.google.api.gbase.client.AttributeHistogram}.
   */
  private class AddValueHandler extends XmlParser.ElementHandler {
    private final int count;

    /**
     * Creates an new handler for a gm:value tag.
     *
     * @param attrs XML attributes for the gm:value tag
     */
    private AddValueHandler(Attributes attrs)
        throws IOException, ParseException {
      AttributeHelper helper = new AttributeHelper(attrs);
      this.count = helper.consumeInteger("count", false, 0);
      helper.assertAllConsumed();
    }


    @Override
    public void processEndElement() throws ParseException {
      String value = this.value;
      if ("".equals(value)) {
        value = null;
      }
      addValue(count, value);
    }
  }
}
