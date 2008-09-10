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


package com.google.gdata.data.photos;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.data.Kind.AdaptorException;
import com.google.gdata.data.media.MediaEntry;
import com.google.gdata.data.photos.impl.GphotoDataImpl;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * The base entry class for Picasaweb data.  This class provides access to the
 * fields that all Picasaweb data objects contain, as well as helper methods
 * for subclasses to use.  It is also responsible for making the handling of
 * the description field consistent across both atom and RSS, allowing the rss
 * entries to match the atom entries in a more consistent fashion.
 *
 * 
 */
public class GphotoEntry<E extends GphotoEntry<E>> extends MediaEntry<E>
    implements GphotoData, AtomData {

  // The gphoto data instance used as a delegate.
  private final GphotoData delegate;

  /**
   * Constructs a new Entry instance.
   */
  public GphotoEntry() {
    super();
    this.delegate = new GphotoDataImpl(this);
  }

  /**
   * Constructs a new entry instance using the source for a shallow copy.
   */
  protected GphotoEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    this.delegate = new GphotoDataImpl(this);
  }

  /*
   * Declare all extensions on our delegate.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    delegate.declareExtensions(extProfile);
    super.declareExtensions(extProfile);
  }

  /**
   * Retrieves the feed link.  This is a link to the entry-as-feed, if this
   * entry has associated data.
   */
  public Link getFeedLink() {
    Link feedLink = getLink(Link.Rel.FEED, Link.Type.ATOM);
    return feedLink;
  }

  /**
   * Get the feed of entries related to this entry. The kinds parameter is
   * a vararg array of associated kinds to return as entries.  If the
   * kinds parameter is empty, the default kind associated with this feed class
   * will be used.
   */
  public <F extends GphotoFeed<F>> F getFeed(Class<F> feedClass,
      String... kinds) throws IOException, ServiceException {
    if (state.service == null) {
      throw new ServiceException(
          "Entry is not associated with a GData service.");
    }
    Link feedLink = getFeedLink();
    if (feedLink == null) {
      throw new UnsupportedOperationException("Missing feed link.");
    }
    String feedHref = feedLink.getHref();
    if (kinds != null && kinds.length > 0) {
      StringBuilder kindBuilder = new StringBuilder();
      for (int i = 0; i < kinds.length; i++) {
        if (i > 0) kindBuilder.append(',');
        kindBuilder.append(kinds[i]);
      }
      if (feedHref.indexOf('?') == -1) {
        feedHref += "?kind=" + kindBuilder;
      } else {
        feedHref += "&kind=" + kindBuilder;
      }
    }
    URL feedUrl = new URL(feedHref);
    return state.service.getFeed(feedUrl, feedClass);
  }

  /**
   * Return the gphoto:id.
   */
  public String getGphotoId() {
    return delegate.getGphotoId();
  }

  /**
   * Set the gphoto:id.
   */
  public void setGphotoId(Long id) {
    delegate.setGphotoId(id);
  }

  /**
   * Set the gphoto:id.
   */
  public void setGphotoId(String id) {
    delegate.setGphotoId(id);
  }

  /**
   * Description on an entry is just the summary.
   */
  public TextConstruct getDescription() {
    return getSummary();
  }

  /**
   * Description on an entry is just the summary.
   */
  public void setDescription(TextConstruct description) {
    setSummary(description);
  }

  /*
   * This is a hack to allow rss to contain both description and enclosure.
   * ATOM summary maps to RSS description, the content will map to enclosure.
   */
  @Override
  public void generateRss(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    TextConstruct summary = getSummary();
    try {
      if (summary != null) {
        setSummary(new SummaryTextConstruct(summary));
      }
      super.generateRss(w, extProfile);
    } finally {
      if (summary != null) {
        setSummary(summary);
      }
    }
  }

  /**
   * Returns an adapted entry that is a subclass of GphotoEntry.
   */
  @Override
  public GphotoEntry<?> getAdaptedEntry() throws AdaptorException {
    return (GphotoEntry<?>) super.getAdaptedEntry();
  }

  /**
   * This class tricks gdata into outputing description instead of atom:summary
   * when outputing to RSS.  This keeps the handling of the feeds consistent
   * to both creators and consumers.
   */
  private static class SummaryTextConstruct extends TextConstruct {

    private final TextConstruct wrapped;

    public SummaryTextConstruct(TextConstruct wrapped) {
      this.wrapped = wrapped;
    }

    @Override
    public void generateAtom(XmlWriter w, String elementName)
        throws IOException {
      wrapped.generateRss(w, "description", RssFormat.FULL_HTML);
    }

    @Override
    public void generateRss(XmlWriter w, String elementName,
        RssFormat rssFormat) throws IOException {
      wrapped.generateRss(w, elementName, rssFormat);
    }

    @Override
    public String getPlainText() {
      return wrapped.getPlainText();
    }

    @Override
    public int getType() {
      return wrapped.getType();
    }

    @Override
    public boolean isEmpty() {
      return wrapped.isEmpty();
    }
  }
}
