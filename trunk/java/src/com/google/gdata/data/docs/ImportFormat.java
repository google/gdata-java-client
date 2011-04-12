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
 * A map of possible import formats.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = DocsNamespace.DOCS_ALIAS,
    nsUri = DocsNamespace.DOCS,
    localName = ImportFormat.XML_NAME)
public class ImportFormat extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "importFormat";

  /** XML "source" attribute name */
  private static final String SOURCE = "source";

  /** XML "target" attribute name */
  private static final String TARGET = "target";

  private static final AttributeHelper.EnumToAttributeValue<Target>
      TARGET_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.LowerCaseEnumToAttributeValue<Target>();

  /** Import source mime type */
  private String source = null;

  /** Import target document type */
  private Target target = null;

  /** Import target document type. */
  public enum Target {

    /** Document import format. */
    DOCUMENT,

    /** Drawing import format. */
    DRAWING,

    /** Presentation import format. */
    PRESENTATION,

    /** Spreadsheet import format. */
    SPREADSHEET

  }

  /**
   * Default mutable constructor.
   */
  public ImportFormat() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param source import source mime type.
   * @param target import target document type.
   */
  public ImportFormat(String source, Target target) {
    super();
    setSource(source);
    setTarget(target);
    setImmutable(true);
  }

  /**
   * Returns the import source mime type.
   *
   * @return import source mime type
   */
  public String getSource() {
    return source;
  }

  /**
   * Sets the import source mime type.
   *
   * @param source import source mime type or <code>null</code> to reset
   */
  public void setSource(String source) {
    throwExceptionIfImmutable();
    this.source = source;
  }

  /**
   * Returns whether it has the import source mime type.
   *
   * @return whether it has the import source mime type
   */
  public boolean hasSource() {
    return getSource() != null;
  }

  /**
   * Returns the import target document type.
   *
   * @return import target document type
   */
  public Target getTarget() {
    return target;
  }

  /**
   * Sets the import target document type.
   *
   * @param target import target document type or <code>null</code> to reset
   */
  public void setTarget(Target target) {
    throwExceptionIfImmutable();
    this.target = target;
  }

  /**
   * Returns whether it has the import target document type.
   *
   * @return whether it has the import target document type
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
        ExtensionDescription.getDefaultDescription(ImportFormat.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(SOURCE, source);
    generator.put(TARGET, target, TARGET_ENUM_TO_ATTRIBUTE_VALUE);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    source = helper.consume(SOURCE, true);
    target = helper.consumeEnum(TARGET, true, Target.class, null,
        TARGET_ENUM_TO_ATTRIBUTE_VALUE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    ImportFormat other = (ImportFormat) obj;
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
    return "{ImportFormat source=" + source + " target=" + target + "}";
  }

}

