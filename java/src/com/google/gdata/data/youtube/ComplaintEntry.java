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


package com.google.gdata.data.youtube;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Link;
import com.google.gdata.data.Kind;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextContent;

/**
 * A complaint.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_COMPLAINT)
public class ComplaintEntry extends BaseEntry<ComplaintEntry> {

  /**
   * Nonstandard categories that might be found in this entry.
   */
  public static final String[] CATEGORIES = {
    YouTubeNamespace.COMPLAINT_REASON_SCHEME
  };


  public ComplaintEntry() {
    EntryUtils.setKind(this, YouTubeNamespace.KIND_COMPLAINT);
  }

  public ComplaintEntry(BaseEntry<?> base) {
    super(base);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_COMPLAINT);
  }
  
 /**
  * Adds a rel=related type=application/atom+xml link pointing to the
  * video.
  * 
  * @param url video url
  */
  public void addVideoUrl(String url) {
    Link link = new Link();
    link.setRel(Link.Rel.RELATED);
    link.setType(Link.Type.ATOM);
    link.setHref(url);
    getLinks().add(link);
  }

  /**
   * Gets the rel=related type=application/atom+xml link pointing to the video.
   * 
   * @return video url or null
   */
  public String getVideoUrl() {
    Link link = getLink(Link.Rel.RELATED, Link.Type.ATOM);
    return link == null ? null : link.getHref();
  }
  
  /**
   * Adds an optional comment to the complaint.
   * 
   * @param comment optional comment of the complaint
   */
  public void setComment(String comment) {
    setContent(comment == null ? null : new PlainTextConstruct(comment));
  }
  
  /**
   * Returns the comment of the complaint.
   * 
   * @return the comment of the complaint
   */
  public String getComment() {
    if (getContent() instanceof TextContent) {
      return ((TextContent) getContent()).getContent().getPlainText();
    }
    return null;
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    // Accept pretty much anything, so it's possible to post
    // videos to this feed to complain about them.
    extProfile.declareArbitraryXmlExtension(BaseEntry.class);
  }
}
