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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * Type of crawl issue.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.WT_ALIAS,
    nsUri = Namespaces.WT,
    localName = CrawlIssueIssueType.XML_NAME)
public class CrawlIssueIssueType extends AbstractExtension {

  /** XML element name */
  static final String XML_NAME = "issue-type";

  private static final AttributeHelper.EnumToAttributeValue<IssueType>
      ISSUETYPE_ENUM_TO_ATTRIBUTE_VALUE = new
      AttributeHelper.EnumToAttributeValue<IssueType>() {
    public String getAttributeValue(IssueType enumValue) {
      return enumValue.toValue();
    }
  };

  /** Value */
  private IssueType issueType = null;

  /** Value. */
  public enum IssueType {

    /** The type of issue is HTTP error. */
    HTTP_ERROR("http-error"),

    /** The type of issue is URL In Sitemap. */
    IN_SITEMAP("in-sitemap"),

    /** The type of issue is News Error. */
    NEWS_ERROR("news-error"),

    /** The type of issue is Not Followed. */
    NOT_FOLLOWED("not-followed"),

    /** The type of issue is Not Found. */
    NOT_FOUND("not-found"),

    /** The type of issue is Restricted by robots.txt. */
    RESTRICTED_ROBOTS_TXT("restricted-robots-txt"),

    /** The type of issue is Timed Out. */
    TIMED_OUT("timed-out"),

    /** The type of issue is Unreachable. */
    UNREACHABLE("unreachable");

    private final String value;

    private IssueType(String value) {
      this.value = value;
    }

    /**
     * Returns the value used in the XML.
     *
     * @return value used in the XML
     */
    public String toValue() {
      return value;
    }

  }

  /**
   * Default mutable constructor.
   */
  public CrawlIssueIssueType() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param issueType value.
   */
  public CrawlIssueIssueType(IssueType issueType) {
    super();
    setIssueType(issueType);
    setImmutable(true);
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public IssueType getIssueType() {
    return issueType;
  }

  /**
   * Sets the value.
   *
   * @param issueType value or <code>null</code> to reset
   */
  public void setIssueType(IssueType issueType) {
    throwExceptionIfImmutable();
    this.issueType = issueType;
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasIssueType() {
    return getIssueType() != null;
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
        ExtensionDescription.getDefaultDescription(CrawlIssueIssueType.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.setContent(ISSUETYPE_ENUM_TO_ATTRIBUTE_VALUE.getAttributeValue(
        issueType));
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    issueType = helper.consumeEnum(null, false, IssueType.class, null,
        ISSUETYPE_ENUM_TO_ATTRIBUTE_VALUE);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    CrawlIssueIssueType other = (CrawlIssueIssueType) obj;
    return eq(issueType, other.issueType);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (issueType != null) {
      result = 37 * result + issueType.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{CrawlIssueIssueType issueType=" + issueType + "}";
  }

}

