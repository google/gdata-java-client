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


package com.google.gdata.data.docs;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * A map of possible export formats.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = DocsNamespace.DOCS_ALIAS,
    nsUri = DocsNamespace.DOCS,
    localName = ExportFormat.XML_NAME)
public class ExportFormat extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "exportFormat";

  /** XML "source" attribute name */
  private static final String SOURCE = "source";

  /** XML "target" attribute name */
  private static final String TARGET = "target";

  private static final AttributeHelper.EnumToAttributeValue<Source>
      SOURCE_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.LowerCaseEnumToAttributeValue<Source>();

  /** Export source document type */
  private Source source = null;

  /** Export target mime type */
  private String target = null;

  /** Export source document type. */
  public enum Source {

    /** Document export format. */
    DOCUMENT,

    /** Presentation export format. */
    PRESENTATION,

    /** Spreadsheet export format. */
    SPREADSHEET

  }

  /**
   * Default mutable constructor.
   */
  public ExportFormat() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param source export source document type.
   * @param target export target mime type.
   */
  public ExportFormat(Source source, String target) {
    super();
    setSource(source);
    setTarget(target);
    setImmutable(true);
  }

  /**
   * Returns the export source document type.
   *
   * @return export source document type
   */
  public Source getSource() {
    return source;
  }

  /**
   * Sets the export source document type.
   *
   * @param source export source document type or <code>null</code> to reset
   */
  public void setSource(Source source) {
    throwExceptionIfImmutable();
    this.source = source;
  }

  /**
   * Returns whether it has the export source document type.
   *
   * @return whether it has the export source document type
   */
  public boolean hasSource() {
    return getSource() != null;
  }

  /**
   * Returns the export target mime type.
   *
   * @return export target mime type
   */
  public String getTarget() {
    return target;
  }

  /**
   * Sets the export target mime type.
   *
   * @param target export target mime type or <code>null</code> to reset
   */
  public void setTarget(String target) {
    throwExceptionIfImmutable();
    this.target = target;
  }

  /**
   * Returns whether it has the export target mime type.
   *
   * @return whether it has the export target mime type
   */
  public boolean hasTarget() {
    return getTarget() != null;
  }

  @Override
  protected void validate() {
    if (source == null) {
      throwExceptionForMissingAttribute(SOURCE);
    }
    if (target == null) {
      throwExceptionForMissingAttribute(TARGET);
    }
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
        ExtensionDescription.getDefaultDescription(ExportFormat.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(SOURCE, source, SOURCE_ENUM_TO_ATTRIBUTE_VALUE);
    generator.put(TARGET, target);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    source = helper.consumeEnum(SOURCE, true, Source.class, null,
        SOURCE_ENUM_TO_ATTRIBUTE_VALUE);
    target = helper.consume(TARGET, true);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    ExportFormat other = (ExportFormat) obj;
    return eq(source, other.source)
        && eq(target, other.target);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (source != null) {
      result = 37 * result + source.hashCode();
    }
    if (target != null) {
      result = 37 * result + target.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{ExportFormat source=" + source + " target=" + target + "}";
  }

}

