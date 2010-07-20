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

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * YouTube namespace definition.
 *
 * 
 */
public class YouTubeNamespace {

  /** Namespace URI */
  public static final String URI = "http://gdata.youtube.com/schemas/2007";

  /** Standard namespace prefix. */
  public static final String PREFIX = "yt";

  /** Namespace object. */
  public static final XmlNamespace NS =
      new XmlNamespace(PREFIX, URI);

  /**
   * Kind linked to {@link VideoEntry}.
   */
  public static final String KIND_VIDEO = URI + "#video";

  /**
   * Kind linked to {@link CaptionTrackEntry}.
   */
  public static final String KIND_CAPTION_TRACK = URI + "#captionTrack";

  /**
   * Kind linked to {@link ComplaintEntry}.
   */
  public static final String KIND_COMPLAINT = URI + "#complaint";

  /**
   * Kind linked to {@link CommentEntry}.
   */
  public static final String KIND_COMMENT = URI + "#comment";

  /**
   * Kind linked to {@link PlaylistLinkEntry}.
   */
  public static final String KIND_PLAYLIST_LINK = URI + "#playlistLink";

  /**
   * Kind linked to {@link SubscriptionEntry}.
   */
  public static final String KIND_SUBSCRIPTION = URI + "#subscription";

  /**
   * Kind linked to {@link FriendEntry}.
   */
  public static final String KIND_FRIEND = URI + "#friend";

  /**
   * Kind linked to {@link RatingEntry}.
   */
  public static final String KIND_RATING = URI + "#rating";

  /**
   * Kind linked to {@link UserProfileEntry}.
   */
  public static final String KIND_USER_PROFILE = URI + "#userProfile";
  
  /**
   * Kind linked to {@link UserEventEntry}.
   */
  public static final String KIND_USER_EVENT = URI + "#userEvent";
  
  /**
   * Kind linked to {@link ChannelEntry}.
   */
  public static final String KIND_CHANNEL = URI + "#channel";  

  /**
   * Kind linked to {@link PlaylistEntry}.
   */
  public static final String KIND_PLAYLIST = URI + "#playlist";

  /**
   * Kind linked to {@link FavoriteEntry}.
   */
  public static final String KIND_FAVORITE = URI + "#favorite";

  /**
   * Kind linked to {@link VideoMessageEntry}.
   */
  public static final String KIND_VIDEO_MESSAGE = URI + "#videoMessage";
  
  /**
   * Kind linked to {@link StationDetailsEntry}.
   */  
  public static final String KIND_STATION = URI + "#station";
  
  /**
   * Scheme used for atom:categories and media:categories.
   */
  public static final String CATEGORY_SCHEME = URI + "/categories.cat";

  /**
   * Scheme used for keywords in atom:categories.
   */
  public static final String KEYWORD_SCHEME = URI + "/keywords.cat";

  /**
   * Scheme used for keywords in atom:categories.
   */
  public static final String MEDIA_RATING_SCHEME = URI + "#mediarating";

  /**
   * Scheme used for atom:categories, which is mapped to playlist tags.
   */
  public static final String TAG_SCHEME = URI + "/tags.cat";

  /**
   * Scheme used for atom:categories which correspond to contact lists.
   */
  public static final String CONTACT_LIST_SCHEME = URI + "/contact.cat";

  /**
   * Scheme used for atom:categories which corresponds to channels
   * on the user profile feed.
   */
  public static final String CHANNELTYPE_SCHEME = URI + "/channeltypes.cat";
  
  /**
   * Scheme used for atom:categories which describes the type of event in 
   * the entry. 
   */
  public static final String USEREVENTS_SCHEME = URI + "/userevents.cat";

  /**
   * Scheme used for atom:categories which correspond to complaint reasons.
   */
  public static final String COMPLAINT_REASON_SCHEME = URI + "/complaint-reasons.cat";

  /**
   * Scheme used for atom:categories which describes the type of subscription
   * defined in the entry.
   */
  public static final String SUBSCRIPTIONTYPE_SCHEME = URI
      + "/subscriptiontypes.cat";

  /**
   * Scheme used for atom:categories which contain developer-specific tags.
   */
  public static final String DEVELOPER_TAG_SCHEME = URI + "/developertags.cat";

  /**
   * Link Rel value for the feed of a playlist (containing playlist entries)
   */
  public static final String PLAYLIST_REL = URI + "#playlist";

  /**
   * Link Rel value for user upload links.
   */
  public static final String UPLOADS_REL = URI + "#user.uploads";

  /**
   * Link Rel for the featured video of a feed (currently the user feed).
   */
  public static final String FEATURED_VIDEO_REL = URI + "#featured-video";

  /**
   * Link Rel value for user subscriptions links.
   */
  public static final String SUBSCRIPTIONS_REL = URI + "#user.subscriptions";

  /**
   * Link Rel value for user playlists links.
   */
  public static final String PLAYLISTS_REL = URI + "#user.playlists";
  
  /**
   * Link Rel value for user's friends activity.
   */
  public static final String FRIENDSACTIVITY_REL = URI + "#user.friendsactivity";
  
  /**
   * Link Rel value for user's recent activity.   */
  public static final String RECENTACTIVITY_REL = URI + "#user.recentactivity";

  /**
   * Link Rel value for user favorites links.
   */
  public static final String FAVORITES_REL = URI + "#user.favorites";

  /**
   * Link Rel value for user contacts links.
   */
  public static final String CONTACTS_REL = URI + "#user.contacts";

  /**
   * Link Rel value for user inbox links.
   */
  public static final String INBOX_REL = URI + "#user.inbox";
    
  /**
   * Link Rel value for the mobile page link of a video.
   */
  public static final String MOBILE_REL = URI + "#mobile";

  /**
   * Link Rel value for video responses links.
   */
  public static final String RESPONSES_REL = URI + "#video.responses";

  /**
   * Link Rel value for video ratings links.
   */
  public static final String RATINGS_REL = URI + "#video.ratings";

  /**
   * Link Rel value for video comments links.
   */
  public static final String COMMENTS_REL = URI + "#comments";

  /**
   * Link Rel value for video caption track links.
   */
  public static final String CAPTION_TRACKS_REL = URI + "#video.captionTracks";

  /**
   * Link Rel value for video complaints links.
   */
  public static final String COMPLAINTS_REL = URI + "#video.complaints";

  /**
   * Link Rel value for related videos links.
   */
  public static final String RELATED_REL = URI + "#video.related";
  
  /**
   * Link Rel value for the channel content.
   */
  public static final String CHANNEL_CONTENT_REL = URI + "#channel.content";
  
  /**
   * Link Rel value for the station content.
   */
  public static final String STATION_CONTENT_REL = URI + "#station.content";

  /**
   * Link Rel value for parent comment link on the comments feed.
   */
  public static final String IN_REPLY_TO = URI + "#in-reply-to";

  /**
   * Link Rel value for video queries (eg. /feeds/api/videos?q=dog)
   */
  public static final String VIDEO_QUERY_REL = URI + "#video.query";

  /**
   * Link Rel value for new subscription videos (/feeds/api/users/janedoe/newsubscriptionvideos)
   */
  public static final String NEW_SUBSCRIPTION_VIDEOS_REL = URI + "#user.newsubscriptionvideos";
  
  /**
   * Link Rel value for a user's video log link.
   */
  public static final String VLOG_REL = URI + "#user.vlog";

  /**
   * Link Rel value for get upload token action.
   */
  public static final String GET_UPLOAD_TOKEN_REL = URI + "#action.getUploadToken";
  
  /**
   * Link Rel value for video. 
   */
  public static final String VIDEO_REL = URI + "#video";

  /**
   * Custom {@code media:credit} scheme.
   * This overrides the default value of {@code urn:ebu} for {@code media:credit}.
   */
  public static final String CREDIT_SCHEME = "urn:youtube";

  /**
   * Uploader role inside {@link #CREDIT_SCHEME} for {@code media:credit}.
   */
  public static final String CREDIT_UPLOADER_ROLE = "uploader";  
}
