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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

import java.util.List;

/**
 * Describes a crawl issue entry.
 *
 * 
 */
@Kind.Term(CrawlIssueEntry.KIND)
public class CrawlIssueEntry extends BaseEntry<CrawlIssueEntry> {

  /**
   * Crawl Issue Entry kind term value.
   */
  public static final String KIND = Namespaces.WT_PREFIX + "crawl_issue_entry";

  /**
   * Crawl Issue Entry kind category.
   */
  public static final Category CATEGORY = new
      Category(com.google.gdata.util.Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public CrawlIssueEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public CrawlIssueEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(CrawlIssueEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(CrawlIssueEntry.class,
        CrawlIssueCrawlType.getDefaultDescription(true, false));
    extProfile.declare(CrawlIssueEntry.class, CrawlIssueDateDetected.class);
    extProfile.declare(CrawlIssueEntry.class, CrawlIssueDetail.class);
    extProfile.declare(CrawlIssueEntry.class,
        CrawlIssueIssueType.getDefaultDescription(true, false));
    extProfile.declare(CrawlIssueEntry.class,
        CrawlIssueLinkedFromUrl.getDefaultDescription(false, true));
    extProfile.declare(CrawlIssueEntry.class,
        CrawlIssueUrl.getDefaultDescription(true, false));
  }

  /**
   * Returns the crawl issue crawl type.
   *
   * @return crawl issue crawl type
   */
  public CrawlIssueCrawlType getCrawlType() {
    return getExtension(CrawlIssueCrawlType.class);
  }

  /**
   * Sets the crawl issue crawl type.
   *
   * @param crawlType crawl issue crawl type or <code>null</code> to reset
   */
  public void setCrawlType(CrawlIssueCrawlType crawlType) {
    if (crawlType == null) {
      removeExtension(CrawlIssueCrawlType.class);
    } else {
      setExtension(crawlType);
    }
  }

  /**
   * Returns whether it has the crawl issue crawl type.
   *
   * @return whether it has the crawl issue crawl type
   */
  public boolean hasCrawlType() {
    return hasExtension(CrawlIssueCrawlType.class);
  }

  /**
   * Returns the crawl issue date detected.
   *
   * @return crawl issue date detected
   */
  public CrawlIssueDateDetected getDateDetected() {
    return getExtension(CrawlIssueDateDetected.class);
  }

  /**
   * Sets the crawl issue date detected.
   *
   * @param dateDetected crawl issue date detected or <code>null</code> to reset
   */
  public void setDateDetected(CrawlIssueDateDetected dateDetected) {
    if (dateDetected == null) {
      removeExtension(CrawlIssueDateDetected.class);
    } else {
      setExtension(dateDetected);
    }
  }

  /**
   * Returns whether it has the crawl issue date detected.
   *
   * @return whether it has the crawl issue date detected
   */
  public boolean hasDateDetected() {
    return hasExtension(CrawlIssueDateDetected.class);
  }

  /**
   * Returns the crawl issue detail.
   *
   * @return crawl issue detail
   */
  public CrawlIssueDetail getDetail() {
    return getExtension(CrawlIssueDetail.class);
  }

  /**
   * Sets the crawl issue detail.
   *
   * @param detail crawl issue detail or <code>null</code> to reset
   */
  public void setDetail(CrawlIssueDetail detail) {
    if (detail == null) {
      removeExtension(CrawlIssueDetail.class);
    } else {
      setExtension(detail);
    }
  }

  /**
   * Returns whether it has the crawl issue detail.
   *
   * @return whether it has the crawl issue detail
   */
  public boolean hasDetail() {
    return hasExtension(CrawlIssueDetail.class);
  }

  /**
   * Returns the crawl issue issue type.
   *
   * @return crawl issue issue type
   */
  public CrawlIssueIssueType getIssueType() {
    return getExtension(CrawlIssueIssueType.class);
  }

  /**
   * Sets the crawl issue issue type.
   *
   * @param issueType crawl issue issue type or <code>null</code> to reset
   */
  public void setIssueType(CrawlIssueIssueType issueType) {
    if (issueType == null) {
      removeExtension(CrawlIssueIssueType.class);
    } else {
      setExtension(issueType);
    }
  }

  /**
   * Returns whether it has the crawl issue issue type.
   *
   * @return whether it has the crawl issue issue type
   */
  public boolean hasIssueType() {
    return hasExtension(CrawlIssueIssueType.class);
  }

  /**
   * Returns the crawl issue linked from urls.
   *
   * @return crawl issue linked from urls
   */
  public List<CrawlIssueLinkedFromUrl> getLinkedFroms() {
    return getRepeatingExtension(CrawlIssueLinkedFromUrl.class);
  }

  /**
   * Adds a new crawl issue linked from url.
   *
   * @param linkedFrom crawl issue linked from url
   */
  public void addLinkedFrom(CrawlIssueLinkedFromUrl linkedFrom) {
    getLinkedFroms().add(linkedFrom);
  }

  /**
   * Returns whether it has the crawl issue linked from urls.
   *
   * @return whether it has the crawl issue linked from urls
   */
  public boolean hasLinkedFroms() {
    return hasRepeatingExtension(CrawlIssueLinkedFromUrl.class);
  }

  /**
   * Returns the crawl issue url.
   *
   * @return crawl issue url
   */
  public CrawlIssueUrl getUrl() {
    return getExtension(CrawlIssueUrl.class);
  }

  /**
   * Sets the crawl issue url.
   *
   * @param url crawl issue url or <code>null</code> to reset
   */
  public void setUrl(CrawlIssueUrl url) {
    if (url == null) {
      removeExtension(CrawlIssueUrl.class);
    } else {
      setExtension(url);
    }
  }

  /**
   * Returns whether it has the crawl issue url.
   *
   * @return whether it has the crawl issue url
   */
  public boolean hasUrl() {
    return hasExtension(CrawlIssueUrl.class);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{CrawlIssueEntry " + super.toString() + "}";
  }

}

