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
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.extensions.FeedLink;

import java.util.Set;

/**
 * An atom entry containing a channel.
 * 
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_CHANNEL)
public class ChannelEntry extends BaseEntry<ChannelEntry>{
  /**
   * Nonstandard categories that might be found in this entry.
   */
  public static final String[] CATEGORIES = {
    YouTubeNamespace.CHANNELTYPE_SCHEME
  };
  
  public ChannelEntry() {
    EntryUtils.setKind(this, YouTubeNamespace.KIND_CHANNEL);    
  }
  
  /**
   * Returns the category with the given scheme
   * @param categories the set of categories.
   * @param scheme the scheme that the return value should have
   * @return the category or null if not found
   */
  private Category getCategoryWithScheme(Set<Category> categories, 
      String scheme) {
    for (Category c : categories) {
      if (c.getScheme().equals(scheme)) {
        return c;
      }
    }
    return null;
  }
  
  /**
   * Returns the term of the channel type category. 
   * If no category with scheme channel type is found, null is returned.
   * 
   * @return term of the category with the channel type scheme.
   */
  public String getChannelType() {
    return getCategoryWithScheme(this.getCategories(), 
        YouTubeNamespace.CHANNELTYPE_SCHEME).getTerm();
  }

  /**
   * Set channel type of category tag.
   * @param channelTypeTerm If null, the channel type category is removed. 
   */
  public void setChannelType(String channelTypeTerm) {
    if (channelTypeTerm == null) {
      this.getCategories().remove(getCategoryWithScheme(this.getCategories(),
          YouTubeNamespace.CHANNELTYPE_SCHEME));
      return;
    } else {
      getCategories().add(
          new Category(YouTubeNamespace.CHANNELTYPE_SCHEME, channelTypeTerm));
    }
  }
  
  /** Returns a link to the user uploads feed. */
  public FeedLink<?> getUploadsFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.UPLOADS_REL);
  }
  
  /** Returns a link to the featured video of the user's profile. */
  public Link getFeaturedVideoLink() {
    return getLink(YouTubeNamespace.FEATURED_VIDEO_REL, Link.Type.ATOM);
  } 
  
  /**
   * Declares the {@link ExtensionDescription} of each {@link Extension} 
   * expected by the implementing {@link ExtensionPoint} in the target profile.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declareAdditionalNamespace(YouTubeNamespace.NS);
    ExtensionDescription feedLinkDescription = FeedLink.getDefaultDescription();
    feedLinkDescription.setRepeatable(true);
    extProfile.declare(UserProfileEntry.class, feedLinkDescription);    
    extProfile.declareArbitraryXmlExtension(ChannelEntry.class);
  }
}

