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


package com.google.gdata.data.photos;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.photos.impl.GphotoDataImpl;

/**
 * This class customizes the generic BaseFeed class to define a feed of
 * GphotoEntries. It also adds support for the GphotoData interface methods so
 * all subclasses have the base access methods available. In addition it
 * provides access to a consistent description field across both RSS and ATOM.
 *
 * 
 */
public class GphotoFeed<F extends GphotoFeed> extends BaseFeed<F, GphotoEntry>
    implements GphotoData, AtomData {

  // The delegating instance to deal with extensions.
  private final GphotoData delegate;

  /**
   * Construct a new {@code GphotoFeed} instance that is parameterized to
   * contain {@code GphotoEntry} instances.
   */
  public GphotoFeed() {
    this(GphotoEntry.class);
  }

  /**
   * Construct a new {@code GphotoFeed} instance parameterized to contain the
   * given {@code GphotoEntry} type. This should be used by subclasses to set up
   * subclassed entry types as needed.
   */
  protected GphotoFeed(Class<? extends GphotoEntry> entryClass) {
    super(entryClass);
    this.delegate = new GphotoDataImpl(this);
  }

  /**
   * Construct a new {@code GphotoFeed} instance parameterized to contain
   * {@code GphotoEntry} instances. The source data for the feed will be pulled
   * from the {@code BaseFeed} instance that was passed as source.
   */
  public GphotoFeed(BaseFeed sourceFeed) {
    this(GphotoEntry.class, sourceFeed);
  }

  /**
   * Construct a new {@code GphotoFeed} instance parameterized to contain the
   * given {@code GphotoEntry} subclass. The base state of the feed will be
   * drawn from the passed in {@code BaseFeed} instance. This constructor should
   * be used by subclasses to change the entry type supported by the feed.
   */
  protected GphotoFeed(Class<? extends GphotoEntry> entryClass,
      BaseFeed sourceFeed) {
    super(entryClass, sourceFeed);
    this.delegate = new GphotoDataImpl(this);
  }

  /*
   * Declare the extensions that are valid on every feed.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    delegate.declareExtensions(extProfile);
    super.declareExtensions(extProfile);
  }

  /**
   * Description on a feed is just the subtitle.
   */
  public TextConstruct getDescription() {
    return getSubtitle();
  }

  /**
   * Description on a feed is just the subtitle.
   */
  public void setDescription(TextConstruct description) {
    setSubtitle(description);
  }

  /**
   * Return the gphoto:id of this feed.
   */
  public String getGphotoId() {
    return delegate.getGphotoId();
  }

  /**
   * Set the gphoto:id of this feed as a long.
   */
  public void setGphotoId(Long id) {
    delegate.setGphotoId(id);
  }

  /**
   * Set the gphoto:id of this feed as a string.
   */
  public void setGphotoId(String id) {
    delegate.setGphotoId(id);
  }
}
