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


package com.google.gdata.util;

import com.google.gdata.util.common.base.Charsets;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Entry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Feed;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.IFeed;
import com.google.gdata.data.ParseSource;



import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Helper class with static parse methods to parse entries and feeds based on
 * the old or new data model as appropriate.
 * 
 * 
 */
public class ParseUtil {
  
  /**
   * Reads an entry from a parse source.  This will use dynamic typing to adapt
   * the response to the most specific subtype available.
   */
  public static IEntry readEntry(ParseSource source)
      throws IOException, ParseException, ServiceException {
    return readEntry(source, null, null);
  }

  /**
   * Reads an entry from a parse source, returning an instance of the requested
   * class, and using the given extension profile if parsing into the old data
   * model.  If the requested class is null the response will be parsed into
   * the most specific subtype possible, using dynamic typing.  If the requested
   * class is not null an instance of the requested type or a subtype will be
   * returned.
   */
  @SuppressWarnings("unchecked")
  public static <T extends IEntry> T readEntry(ParseSource source,
      Class<T> requestedClass, ExtensionProfile extProfile)
      throws IOException, ParseException, ServiceException {

    if (source == null) {
      throw new NullPointerException("Null source");
    }

    Class<? extends IEntry> entryClass = requestedClass;
    Class<? extends IEntry> responseClass = requestedClass;
    
    // Determine the parse entry type, if it is null we parse into an old
    // data model entry class.
    if (entryClass == null) {
      entryClass = Entry.class;
      responseClass = BaseEntry.class;
    }
    boolean isAdapting = isAdapting(entryClass);

    // Create a new entry instance.
    IEntry entry;
    try {
      entry = entryClass.newInstance();
    } catch (IllegalAccessException iae) {
      throw new ServiceException(
          CoreErrorDomain.ERR.cantCreateEntry, iae);
    } catch (InstantiationException ie) {
      throw new ServiceException(
          CoreErrorDomain.ERR.cantCreateEntry, ie);
    }

    BaseEntry<?> baseEntry = (BaseEntry<?>) entry;
    
    // Initialize the extension profile (if not provided)
    if (extProfile == null) {
      extProfile = getExtProfile(baseEntry, isAdapting);
    }

    parseEntry(source, baseEntry, extProfile);
    
    // Adapt if requested and the entry contained a kind tag
    if (isAdapting) {
      BaseEntry<?> adaptedEntry = baseEntry.getAdaptedEntry();
      if (responseClass.isInstance(adaptedEntry)) {
        entry = adaptedEntry;
      }
    }


    return (T) responseClass.cast(entry);
  }

  /**
   * Reads a feed from a parse source.  This will use dynamic typing to adapt
   * the response to the most specific subtype available.
   */
  public static IFeed readFeed(ParseSource source)
      throws IOException, ParseException, ServiceException {
    return readFeed(source, null, null);
  }
  
  /**
   * This method provides the base implementation of feed reading using either
   * static or dynamic typing.  If feedClass is non-null, the method is
   * guaranteed to return an instance of this type, otherwise adaptation will
   * be used to determine the type.  The source object may be either an
   * InputStream, Reader, or XmlParser.
   */
  @SuppressWarnings("unchecked")
  public static <F extends IFeed> F readFeed(ParseSource source,
      Class <F> requestedClass, ExtensionProfile extProfile)
      throws IOException, ParseException, ServiceException {

    if (source == null) {
      throw new NullPointerException("Null source");
    }

    Class<? extends IFeed> feedClass = requestedClass;
    Class<? extends IFeed> responseClass = requestedClass;

    // Determine the parse feed type
    if (feedClass == null) {
      feedClass = Feed.class;
      responseClass = BaseFeed.class;
    }
    boolean isAdapting = isAdapting(feedClass);

    // Create a new feed instance.
    IFeed feed;
    try {
      feed = feedClass.newInstance();
    } catch (IllegalAccessException iae) {
      throw new ServiceException(
          CoreErrorDomain.ERR.cantCreateFeed, iae);
    } catch (InstantiationException ie) {
      throw new ServiceException(
          CoreErrorDomain.ERR.cantCreateFeed, ie);
    }

    // Parse the content
    BaseFeed<?, ?> baseFeed = (BaseFeed<?, ?>) feed;
    
    // Initialize the extension profile (if not provided)
    if (extProfile == null) {
      extProfile = getExtProfile(baseFeed, isAdapting);
    }

    parseFeed(source, baseFeed, extProfile);
    
    // Adapt if requested and the feed contained a kind tag
    if (isAdapting) {
      BaseFeed<?, ?> adaptedFeed = baseFeed.getAdaptedFeed();
      if (responseClass.isInstance(adaptedFeed)) {
        feed = adaptedFeed;
      }
    }

    return (F) responseClass.cast(feed);
  }
  

  private static void parseEntry(ParseSource source, BaseEntry<?> entry,
      ExtensionProfile extProfile) throws ParseException, IOException {
    if (source.getReader() != null) {
      entry.parseAtom(extProfile, source.getReader());
    } else if (source.getInputStream() != null) {
      entry.parseAtom(extProfile, source.getInputStream());
    } else if (source.getEventSource() != null) {
      entry.parseAtom(extProfile, source.getEventSource());
    } else {
      throw new IllegalStateException("Unexpected source: " + source);
    }
  }
  
  private static void parseFeed(ParseSource source, BaseFeed<?, ?> feed,
      ExtensionProfile extProfile) throws ParseException, IOException {
    if (source.getReader() != null) {
      feed.parseAtom(extProfile, source.getReader());
    } else if (source.getInputStream() != null) {
      feed.parseAtom(extProfile, source.getInputStream());
    } else if (source.getEventSource() != null) {
      feed.parseAtom(extProfile, source.getEventSource());
    } else {
      throw new IllegalStateException("Unexpected source: " + source);
    }
  }
  
  private static boolean isAdapting(Class<?> clazz) {
    return clazz == Entry.class
        || clazz == Feed.class;
  }
  
  private static ExtensionProfile getExtProfile(BaseEntry<?> entry,
      boolean isAdapting) {
    ExtensionProfile extProfile = null;
    extProfile = new ExtensionProfile();
    ((BaseEntry<?>) entry).declareExtensions(extProfile);
    if (isAdapting) {
      extProfile.setAutoExtending(true);
    }
    return extProfile;
  }
  
  private static ExtensionProfile getExtProfile(BaseFeed<?, ?> feed,
      boolean isAdapting) {
    ExtensionProfile extProfile = null;
    extProfile = new ExtensionProfile();
    feed.declareExtensions(extProfile);
    if (isAdapting) {
      extProfile.setAutoExtending(true);
    }
    return extProfile;
  }
}

