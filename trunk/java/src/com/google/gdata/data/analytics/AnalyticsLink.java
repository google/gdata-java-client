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


package com.google.gdata.data.analytics;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

/**
 * Extends the base Link class with Analytics extensions.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = "atom",
    nsUri = Namespaces.atom,
    localName = AnalyticsLink.XML_NAME)
public class AnalyticsLink extends Link {

  /** XML element name */
  static final String XML_NAME = "link";

  /** XML "gd:targetKind" attribute name */
  private static final String TARGETKIND = "gd:targetKind";

  /** Kind of feed/entry the href is pointing to */
  private String targetKind = null;

  /** Link relation type. */
  public static final class Rel {

    /** Represents a link to a CHILD feed. */
    public static final String CHILD = AnalyticsNamespace.GA_PREFIX + "child";

    /** Represents a link to a PARENT entry. */
    public static final String PARENT = AnalyticsNamespace.GA_PREFIX + "parent";

  }

  /**
   * Default mutable constructor.
   */
  public AnalyticsLink() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param targetKind kind of feed/entry the href is pointing to.
   */
  public AnalyticsLink(String targetKind) {
    super();
    setTargetKind(targetKind);
    setImmutable(true);
  }

  /**
   * Returns the kind of feed/entry the href is pointing to.
   *
   * @return kind of feed/entry the href is pointing to
   */
  public String getTargetKind() {
    return targetKind;
  }

  /**
   * Sets the kind of feed/entry the href is pointing to.
   *
   * @param targetKind kind of feed/entry the href is pointing to or
   *     <code>null</code> to reset
   */
  public void setTargetKind(String targetKind) {
    throwExceptionIfImmutable();
    this.targetKind = targetKind;
  }

  /**
   * Returns whether it has the kind of feed/entry the href is pointing to.
   *
   * @return whether it has the kind of feed/entry the href is pointing to
   */
  public boolean hasTargetKind() {
    return getTargetKind() != null;
  }

  @Override
  protected void validate() {
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(AnalyticsLink.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(TARGETKIND, targetKind);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    targetKind = helper.consume("targetKind", false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    AnalyticsLink other = (AnalyticsLink) obj;
    return eq(targetKind, other.targetKind);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (targetKind != null) {
      result = 37 * result + targetKind.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{AnalyticsLink targetKind=" + targetKind + " " + super.toString() +
        "}";
  }


  @Override
  public ElementHandler getHandler(ExtensionProfile p, String namespace,
      String localName,
      Attributes attrs) {
    return new AnalyticsLinkHandler(p);
  }

  /**
   * An XML Parser for 'atom:link' extension for Google Analytics.
   */
  class AnalyticsLinkHandler extends AtomHandler {

    public AnalyticsLinkHandler(ExtensionProfile extProfile) {
      super(extProfile, AnalyticsLink.class);
    }

    /**
     * Adds parsing for 'gd:targetKind' attribute.
     */
    @Override
    public void processAttribute(String namespace, String localName,
        String value)
        throws ParseException {
      super.processAttribute(namespace, localName, value);

      if (namespace.equals(Namespaces.g) && localName.equals("targetKind")) {
        targetKind = value;
      }
    }
  }
}
