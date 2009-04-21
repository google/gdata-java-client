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


package com.google.gdata.wireformats.input;

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.Entry;
import com.google.gdata.data.Feed;
import com.google.gdata.data.IAtom;
import com.google.gdata.data.XmlEventSource;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.ContentCreationException;

import java.io.IOException;
import java.io.Reader;

/**
 * Parses Atom feed or entry data using classes based upon the old data
 * model in {@link com.google.gdata.data}.
 * 
 * 
 */
public class AtomDataParser extends XmlInputParser<IAtom> {

  /**
   * Constructs a new AtomDataParser instance.
   */
  public AtomDataParser() {
    super(AltFormat.ATOM, IAtom.class);
  }


  @Override
  public <R extends IAtom> R parse(Reader inputReader, InputProperties inProps,
      Class<R> resultClass) throws IOException, ServiceException {
    
    Preconditions.checkNotNull(inProps.getExtensionProfile(),
        "No extension profile");
    
    R result = createResult(resultClass);
    if (result instanceof BaseEntry) {
      BaseEntry<?> entryResult = (BaseEntry<?>) result;
      entryResult.parseAtom(inProps.getExtensionProfile(), inputReader); 
      if (resultClass == Entry.class) {
        BaseEntry<?> adaptedEntry = entryResult.getAdaptedEntry();
        if (resultClass.isInstance(adaptedEntry)) {
          result = resultClass.cast(adaptedEntry);
        }
      }
    } else if (result instanceof BaseFeed) {
      BaseFeed<?, ?> feedResult = (BaseFeed<?, ?>) result;
      feedResult.parseAtom(inProps.getExtensionProfile(), inputReader);
      if (resultClass == Feed.class) {
        BaseFeed<?, ?> adaptedFeed = feedResult.getAdaptedFeed();
        if (resultClass.isInstance(adaptedFeed)) {
          result = resultClass.cast(adaptedFeed);
        }
      }
    } else {
      throw new ContentCreationException("Invalid result class: " + 
          resultClass);
    }
    return result;
  }

  @Override
  protected <R extends IAtom> R parse(XmlEventSource eventSource,
      InputProperties inProps, Class<R> resultClass) throws IOException,
      ServiceException {
    Preconditions.checkNotNull(inProps.getExtensionProfile(),
    "No extension profile");

    R result = createResult(resultClass);
    if (result instanceof BaseEntry) {
      BaseEntry<?> entryResult = (BaseEntry<?>) result;
      entryResult.parseAtom(inProps.getExtensionProfile(), eventSource);   
    } else if (result instanceof BaseFeed) {
      BaseFeed<?, ?> feedResult = (BaseFeed<?, ?>) result;
      feedResult.parseAtom(inProps.getExtensionProfile(), eventSource);  
    } else {
      throw new ContentCreationException("Invalid result class: " + 
          resultClass);
    }
    return result;
  }
}
