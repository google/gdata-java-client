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

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Provides feed URLs that can be used with a GoogleBase server.
 */
public class FeedURLFactory {

  /**
   * URL of the server to connect to by default.
   * Currently {@value #DEFAULT_BASE_URL}.
   */
  public static final String DEFAULT_BASE_URL =
      "http://www.google.com/base/";

  /** The url used as a base when creating urls. */
  private URL baseUrl;

  private URL feedSnippets;

  private URL feedItems;

  private URL feedItemsBatch;

  private URL feedLocales;

  private URL feedAttributes;

  private URL feedSnippetsBatch;

  /**
   * The default FeedURLFactory instance targeted
   * to the default URL.
   */
  private static final FeedURLFactory instance = new FeedURLFactory();


  /**
   * Gets the default instance of this factory, targeted
   * to {@value #DEFAULT_BASE_URL}.
   *
   * @return the default FeedURLFactory
   */
  public static FeedURLFactory getDefault() {
    return instance;
  }


  /**
   * Creates an URL factory targeted to {@value #DEFAULT_BASE_URL}.
   *
   * Access it using {@link #getDefault()}.
   */
  private FeedURLFactory() {
    try {
      init(DEFAULT_BASE_URL);
    } catch (MalformedURLException e) {
      throw new RuntimeException("Unexpected malformed URL", e);
    }
  }

  /**
   * Creates an URL factory targeted to a server.
   *
   * As long as you don't need to connect to a nonstandard
   * URL, different from {@value #DEFAULT_BASE_URL}, you should
   * consider calling {@link #getDefault()} instead.
   *
   * @param url an URL used as a base for the generated URLs
   * @throws MalformedURLException
   */
  public FeedURLFactory(String url) throws MalformedURLException {
    init(url);
  }
  
  private void init(String url) throws MalformedURLException {
    baseUrl = new URL(url);
    feedSnippets = new URL(baseUrl, "feeds/snippets/");
    feedSnippetsBatch = new URL(baseUrl, "feeds/snippets/batch");
    feedItems = new URL(baseUrl, "feeds/items/");
    feedItemsBatch = new URL(baseUrl, "feeds/items/batch");
    feedLocales = new URL(baseUrl, "feeds/locales/");
    feedAttributes = new URL(baseUrl, "feeds/attributes/");
  }

  /** Returns the URL used as a base for the generated URLs. */
  public URL getBaseURL() {
    return baseUrl;
  }
  
  /** 
   * Encodes a string using UTF-8. 
   * 
   * @param s string to be encoded
   * @throws RuntimeException when the JVM does not support UTF-8
   */
  private String encode(String s) {
    try {
      return URLEncoder.encode(s, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException("UTF-8 is not supported by the JVM", e);
    }
  }

  /**
   * Gets an URL for accessing a public feed, which allows users to perform
   * queries.
   * The formatting of the textual attributes returned by this feed
   * is not guaranteed. This is an unauthenticated read-only feed.
   *
   * @return an URL to be used when creating a {@link GoogleBaseQuery}
   */
  public URL getSnippetsFeedURL() {
    return feedSnippets;
  }

  /**
   * Gets an URL for accessing a public batch feed, which allows users to
   * get a series of entries.
   *
   * A query on this batch feed returns the same result as a series
   * of calls to {@link #getSnippetsFeedURL()} in one HTTP request.
   *
   * @return an URL to post a batch query to.
   */
  public URL getSnippetsBatchFeedURL() {
    return feedSnippetsBatch;
  }

  /**
   * Gets an URL for accessing a snippet.
   *
   * @see #getSnippetsFeedURL()
   * @param id the id of the snippet
   * @return an URL to be used when reading a snippet of an item with a
   *         {@link GoogleBaseService}
   * @throws MalformedURLException
   */
  public URL getSnippetsEntryURL(String id) throws MalformedURLException {
    if (id == null) {
      throw new NullPointerException("entryId is null");
    }
    return new URL(feedSnippets, encode(id));
  }

  /**
   * Gets an URL for accessing a customer feed, which allows users
   * to query, insert, update and delete their own items.
   * The textual attributes of the items are returned in their original format.
   * This is a read/write feed that is only accessible to authenticated users.
   *
   * @return an URL to be used for creating a {@link GoogleBaseQuery}
   */
  public URL getItemsFeedURL() {
    return feedItems;
  }

  /**
   * Gets an URL for accessing an item.
   *
   * @see #getItemsFeedURL()
   * @param entryId
   * @return an URL to be used when reading an item or when
   *         performing an update or delete operation with a
   *         {@link GoogleBaseService}
   * @throws MalformedURLException
   */
  public URL getItemsEntryURL(String entryId)
      throws MalformedURLException {
    if (entryId == null) {
      throw new NullPointerException("entryId is null");
    }
    return new URL(feedItems, encode(entryId));
  }


  /**
   * Gets an URL for posting batch operations
   * to the customer feed.
   *
   * @return an URL to be passed to
   *   {@link com.google.gdata.client.Service#batch(java.net.URL,
   *   com.google.gdata.data.BaseFeed)} 
   */
  public URL getItemsBatchFeedURL() {
    return feedItemsBatch;
  }

  /**
   * Gets the URL of the list of all the supported locales.
   * The feed is read-only and unauthenticated.
   *
   * @return an URL of a feed containing the supported locales
   */
  public URL getLocalesFeedURL() {
    return feedLocales;
  }

  /**
   * Gets an URL for querying the item types Google suggest using
   * for a given locale.
   * The feed is read-only and unauthenticated.
   *
   * @param locale a locale, for example "de_DE", "en_GB", "en_US"
   * @return an URL of a feed containing item types
   * @throws MalformedURLException
   */
  public URL getItemTypesFeedURL(String locale) throws MalformedURLException {
    if (locale == null) {
      throw new NullPointerException("locale is null");
    }
    return new URL(baseUrl, "feeds/itemtypes/" + encode(locale));
  }

  /**
   * Gets an URL for getting the item types Google suggest using
   * for a given itemtype for a given locale.
   * The feed is read-only and unauthenticated.
   *
   * @param locale a locale, for example "de_DE", "en_GB", "en_US"
   * @param itemType the item type to be analysed
   * @return an URL of a feed containing item types
   * @throws MalformedURLException
   */
  public URL getItemTypesEntryURL(String locale,
                                  String itemType) 
      throws MalformedURLException {
    if (locale == null) {
      throw new NullPointerException("locale is null");
    }
    if (itemType == null) {
      throw new NullPointerException("itemType is null");
    }
    return new URL(baseUrl, "feeds/itemtypes/" + encode(locale) +
                            "/" + encode(itemType));
  }

  /**
   * Gets an URL for the list of the most used attributes
   * and their most used values.
   *
   * @return an URL to be used for creating a {@link GoogleBaseQuery}
   */
  public URL getAttributesFeedURL() {
    return feedAttributes;
  }
}

