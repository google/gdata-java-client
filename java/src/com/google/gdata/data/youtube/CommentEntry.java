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
import com.google.gdata.data.Kind;

/**
 * An entry in the comment feed.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_COMMENT)
public class CommentEntry extends BaseEntry<CommentEntry> {

  public CommentEntry() {
    EntryUtils.setKind(this, YouTubeNamespace.KIND_COMMENT);
  }

  public CommentEntry(BaseEntry<?> base) {
    super(base);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_COMMENT);
  }
  
  /**
   * Get the rating of the comment.
   * 
   * Some videos don't allow comment rating.
   * 
   * @return the rating of the comment. May be null.
   */
  public Integer getTotalRating() {
    YtCommentRating rating = getExtension(YtCommentRating.class);
    return rating == null ? null : rating.getTotal();
  }
  
  /**
   * Sets the rating of the comment.
   * 
   * @param rating the rating of the comment. Null will remove the rating.
   */
  public void setTotalRating(Integer rating) {
    if (rating == null) {
      removeExtension(YtCommentRating.class);
    } else {
      setExtension(new YtCommentRating(rating));
    }
  }
  
  /**
   * Check if the comment is marked as spam.
   * 
   * @return true if the comment is marked as spam
   */
  public boolean hasSpamHint() {
    return getExtension(YtSpam.class) != null ? true : false;
  }
  
  /**
   * Hint that the entry is spam.
   * 
   * @param spam if true the comment will be marked with the spam hint. 
   *     False will remove the hint.
   */
  public void setSpamHint(boolean spam) {
    if (spam) {
      setExtension(new YtSpam());
    } else { 
      removeExtension(YtSpam.class);
    }
  }
  
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);

    extProfile.declareAdditionalNamespace(YouTubeNamespace.NS);
    extProfile.declare(CommentEntry.class, YtSpam.class);
    extProfile.declare(CommentEntry.class, YtCommentRating.class);
    
    extProfile.declareArbitraryXmlExtension(CommentEntry.class);
  }
}
