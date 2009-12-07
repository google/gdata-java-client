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


package sample.gtt;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Contains constants for the various feed uris.
 *
 * 
 */
public class FeedUris {

  private static String baseUrl = "http://translate.google.com/toolkit/feeds";

  /**
   * Prevent instantiation of utility function
   */
  private FeedUris() {
  }

  /**
   * @param baseUrlArg the base url for the feeds
   */
  public static void setBaseUrl(String baseUrlArg) {
    baseUrl = baseUrlArg;
  }

  /**
   * Returns the documents feed url.
   */
  public static URL getDocumentsFeedUrl() throws MalformedURLException {
    return new URL(baseUrl + "/documents");
  }

  /**
   * Returns the feed url for given document.
   *
   * @param docId id of the document whose feed url is requried
   */
  public static URL getDocumentFeedUrl(String docId)
      throws MalformedURLException {
    String format = baseUrl + "/documents/%s";
    return createUrl(format, docId);
  }

  /**
   * Returns the feed url to use for deleting the given document.
   *
   * @param docId id of the document whose feed url is requried
   */
  public static URL getDocumentPermDeleteUrl(String docId)
      throws MalformedURLException {
    String format = baseUrl + "/documents/%s?delete=true";
    return createUrl(format, docId);
  }

  /**
   * Returns the feed url to use for downloading the given document.
   *
   * @param docId id of the document whose feed url is requried
   */
  public static URL getDocumentDownloadFeedUrl(String docId)
      throws MalformedURLException {
    String format = baseUrl + "/documents/export/%s";
    return createUrl(format, docId);
  }

  /**
   * Returns the translation memories feed url.
   */
  public static URL getTranslationMemoriesFeedUrl()
      throws MalformedURLException {
    return new URL(baseUrl + "/tm");
  }

  /**
   * Returns the feed url for given translation memory.
   *
   * @param docId id of the translation memory whose feed url is requried
   */
  public static URL getTranslationMemoryFeedUrl(String tmId)
      throws MalformedURLException {
    String format = baseUrl + "/tm/%s";
    return createUrl(format, tmId);
  }

  /**
   * Returns the glossaries feed url.
   */
  public static URL getGlossariesFeedUrl() throws MalformedURLException {
    return new URL(baseUrl + "/glossary");
  }

  /**
   * Returns the feed url for given glossary.
   *
   * @param docId id of the glossary whose feed url is requried
   */
  public static URL getGlossaryFeedUrl(String glossaryId)
      throws MalformedURLException {
    String format = baseUrl + "/glossary/%s";
    return createUrl(format, glossaryId);
  }

  /**
   * Returns the acl feeds url for given feed and entryId.
   *
   * @param feedName the name of the feed, one of {'documents, 'tm, 'glossary'}
   * @param entryId id of the document/translation memory/glossary
   *        whose feed url is requried
   */
  public static URL getAclFeedUrl(String feedName, String entryId)
      throws MalformedURLException {
    String format = baseUrl + "/acl/%s/%s";
    return createUrl(format, feedName, entryId);
  }

  /**
   * Returns the acl feeds url for given feed, entryId and email id.
   *
   * @param feedName the name of the feed, one of {'documents, 'tm, 'glossary'}
   * @param entryId id of the document/translation memory/glossary
   *        whose feed url is requried
   * @param emailId email id of the person relative to whom the feed url is
   *        required.
   */
  public static URL getAclFeedUrl(String feedName, String entryId,
      String emailId) throws MalformedURLException {
    String format = baseUrl + "/acl/%s/%s/%s";
    return createUrl(format, feedName, entryId, emailId);
  }

  private static URL createUrl(String format, Object... args)
      throws MalformedURLException  {
    String feedUrl = String.format(format, args);
    return new URL(feedUrl);
  }
}
