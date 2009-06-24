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
import com.google.gdata.data.media.mediarss.MediaThumbnail;

import java.util.List;
import java.util.Set;

/**
 * An atom entry containing a user profile.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_USER_PROFILE)
public class UserProfileEntry extends BaseEntry<UserProfileEntry> {

  /**
   * Nonstandard categories that might be found in this entry.
   */
  public static final String[] CATEGORIES = {
    YouTubeNamespace.CHANNELTYPE_SCHEME
  };


  public UserProfileEntry() {
    EntryUtils.setKind(this, YouTubeNamespace.KIND_USER_PROFILE);
  }

  public UserProfileEntry(BaseEntry<?> base) {
    super(base);
    EntryUtils.setKind(this, YouTubeNamespace.KIND_USER_PROFILE);
  }

  public void setBooks(String books) {
    if (books == null) {
      removeExtension(YtBooks.class);
    } else {
      setExtension(new YtBooks(books));
    }
  }

  public String getBooks() {
    YtBooks books = getExtension(YtBooks.class);
    if (books != null) {
      return books.getContent();
    } else {
      return null;
    }
  }
  
  public void setAge(Integer age) {
    if (age == null) {
      removeExtension(YtAge.class);
    } else {
      setExtension(new YtAge(age));
    }
  }

  public Integer getAge() {
    YtAge age = getExtension(YtAge.class);
    if (age == null) {
      return null;
    } else {
      return age.getAge();
    }
  }
  
  public void setThumbnail(MediaThumbnail thumb) {
    if (thumb == null) {
      removeExtension(MediaThumbnail.class);
    } else {
      setExtension(thumb);
    }
  }
  
  public MediaThumbnail getThumbnail() {
    MediaThumbnail thumb = getExtension(MediaThumbnail.class);
    if (thumb == null) {
      return null;
    } else {
      return thumb;
    }
  }
  
  public String getCompany() {
    YtCompany company = getExtension(YtCompany.class);
    if (company != null) {
      return company.getContent();
    } else {
      return null;
    }
  }

  public void setCompany(String company) {
    if (company == null) {
      removeExtension(YtCompany.class);
    } else {
      setExtension(new YtCompany(company));
    }
  }

  public void setGender(YtGender.Value gender) {
    if (gender == null) {
      removeExtension(YtGender.class);
    } else {
      setExtension(new YtGender(gender));
    }
  }

  public YtGender.Value getGender() {
    YtGender gender = getExtension(YtGender.class);
    if (gender != null) {
      return gender.getGender();
    } else {
      return null;
    }
  }

  public void setHobbies(String hobbies) {
    if (hobbies == null) {
      removeExtension(YtHobbies.class);
    } else {
      setExtension(new YtHobbies(hobbies));
    }
  }

  public String getHobbies() {
    YtHobbies hobbies = getExtension(YtHobbies.class);
    if (hobbies != null) {
      return hobbies.getContent();
    } else {
      return null;
    }
  }

  public void setHometown(String hometown) {
    if (hometown == null) {
      removeExtension(YtHometown.class);
    } else {
      setExtension(new YtHometown(hometown));
    }
  }

  public String getHometown() {
    YtHometown hometown = getExtension(YtHometown.class);
    if (hometown != null) {
      return hometown.getContent();
    } else {
      return null;
    }
  }

  public void setLocation(String location) {
    if (location == null) {
      removeExtension(YtLocation.class);
    } else {
      setExtension(new YtLocation(location));
    }
  }

  public String getLocation() {
    YtLocation location = getExtension(YtLocation.class);
    if (location != null) {
      return location.getContent();
    } else {
      return null;
    }
  }

  public void setMovies(String movies) {
    if (movies == null) {
      removeExtension(YtMovies.class);
    } else {
      setExtension(new YtMovies(movies));
    }
  }

  public String getMovies() {
    YtMovies movies = getExtension(YtMovies.class);
    if (movies != null) {
      return movies.getContent();
    } else {
      return null;
    }
  }

  public void setMusic(String music) {
    if (music == null) {
      removeExtension(YtMusic.class);
    } else {
      setExtension(new YtMusic(music));
    }
  }

  public String getMusic() {
    YtMusic music = getExtension(YtMusic.class);
    if (music != null) {
      return music.getContent();
    } else {
      return null;
    }
  }

  public void setOccupation(String occupation) {
    if (occupation == null) {
      removeExtension(YtOccupation.class);
    } else {
      setExtension(new YtOccupation(occupation));
    }
  }

  public String getOccupation() {
    YtOccupation occupation = getExtension(YtOccupation.class);
    if (occupation != null) {
      return occupation.getContent();
    } else {
      return null;
    }
  }

  public void setRelationship(YtRelationship.Status relationship) {
    if (relationship == null) {
      removeExtension(YtRelationship.class);
    } else {
      setExtension(new YtRelationship(relationship));
    }
  }

  public YtRelationship.Status getRelationship() {
    YtRelationship relationship = getExtension(YtRelationship.class);
    if (relationship != null) {
      return relationship.getStatus();
    } else {
      return null;
    }
  }

  public void setSchool(String school) {
    if (school == null) {
      removeExtension(YtSchool.class);
    } else {
      setExtension(new YtSchool(school));
    }
  }

  public String getSchool() {
    YtSchool school = getExtension(YtSchool.class);
    if (school != null) {
      return school.getContent();
    } else {
      return null;
    }
  }

  /** Gets the youtube username. */
  public String getUsername() {
    YtUsername username = getExtension(YtUsername.class);
    return username == null ? null : username.getContent();
  }

  /** Sets the youtube username. */
  public void setUsername(String name) {
    if (name == null) {
      removeExtension(YtUsername.class);
    } else {
      setExtension(new YtUsername(name));
    }
  }
  
  /** Gets the first name of the youtube user */
  public String getFirstName() {
    YtFirstName firstname = getExtension(YtFirstName.class);
    return firstname == null ? null : firstname.getContent();
  }
  
  /** Sets the first name of the youtube user */
  public void setFirstName(String firstname) {
    if (firstname == null) {
      removeExtension(YtFirstName.class);
    } else {
      setExtension(new YtFirstName(firstname));
    }
  }
  
  /** Gets the last name of the youtube user */
  public String getLastName() {
    YtLastName lastname = getExtension(YtLastName.class);
    return lastname == null ? null : lastname.getContent();
  }
  
  /** Sets the last name of the youtube user */
  public void setLastName(String lastname) {
    if (lastname == null) {
      removeExtension(YtLastName.class);
    } else {
      setExtension(new YtLastName(lastname));
    }
  }

  /**
   * Gets the channel description.
   *
   * @deprecated Valid only in version 1. Replaced in version 2.0 with
   *             yt:aboutMe for the user self description and atom:summary for
   *             the user channel description
   */
  @Deprecated
  public String getDescription() {
    YtDescription description = getExtension(YtDescription.class);
    return description == null ? null : description.getContent();
  }

  /**
   * Sets the channel description.
   *
   * @deprecated Valid only in version 1. Replaced in version 2.0 with
   *             yt:aboutMe for the user self description and atom:summary for
   *             the user channel description
   */
  @Deprecated
  public void setDescription(String description) {
    if (description == null) {
      removeExtension(YtDescription.class);
    } else {
      setExtension(new YtDescription(description));
    }
  }

  /**
   * Gets the user description.
   *
   * @since version 2.0 of the protocol
   */
  public String getAboutMe() {
    YtAboutMe aboutMe = getExtension(YtAboutMe.class);
    return aboutMe == null ? null : aboutMe.getContent();
  }

  /**
   * Sets the user description.
   *
   * @since version 2.0 of the protocol
   */
  public void setAboutMe(String aboutMe) {
    if (aboutMe == null) {
      removeExtension(YtAboutMe.class);
    } else {
      setExtension(new YtAboutMe(aboutMe));
    }
  }

  public YtUserProfileStatistics getStatistics() {
    return getExtension(YtUserProfileStatistics.class);
  }
  
  public void setStatistics(YtUserProfileStatistics ups) {
    if (ups == null) {
      removeExtension(YtUserProfileStatistics.class);
    } else {
      setExtension(ups);
    }    
  }
  
  /**
   * Returns the category with the given scheme
   * @param categories the set of categories.
   * @param scheme the scheme that the return value should have
   * @return the category or null if not found
   */
  private Category getCategoryForScheme(Set<Category> categories, 
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
    return getCategoryForScheme(this.getCategories(), 
        YouTubeNamespace.CHANNELTYPE_SCHEME).getTerm();
  }

  /**
   * Set channel type of category tag.
   * @param channelTypeTerm If null, the channel type category is removed. 
   */
  public void setChannelType(String channelTypeTerm) {
    if (channelTypeTerm == null) {
      this.getCategories().remove(getCategoryForScheme(this.getCategories(),
          YouTubeNamespace.CHANNELTYPE_SCHEME));
      return;
    } else {
      getCategories().add(
          new Category(YouTubeNamespace.CHANNELTYPE_SCHEME, channelTypeTerm));
    }
  }
  
  /** Returns a modifiable list of {@link FeedLink}s. */
  public List<FeedLink> getFeedLinks() {
    return getRepeatingExtension(FeedLink.class);
  }

  /** Returns a link to the user favorites feed. */
  public FeedLink<?> getFavoritesFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.FAVORITES_REL);
  }

  /** Returns a link to the user contacts (friends) feed. */
  public FeedLink<?> getContactsFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.CONTACTS_REL);
  }

  /** Returns a link to the user inbox feed. */
  public FeedLink<?> getInboxFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.INBOX_REL);
  }

  /** Returns a link to the user playlists feed. */
  public FeedLink<?> getPlaylistsFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.PLAYLISTS_REL);
  }

  /** Returns a link to the users subscriptions feed. */
  public FeedLink<?> getSubscriptionsFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.SUBSCRIPTIONS_REL);
  }

  /** Returns a link to the user uploads feed. */
  public FeedLink<?> getUploadsFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.UPLOADS_REL);
  }

  /** Returns a link to the user's new subscription videos feed. */
  public FeedLink<?> getNewSubscriptionVideosFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.NEW_SUBSCRIPTION_VIDEOS_REL);
  }  
  
  /** Returns a link to the user's friends activity feed. */
  public FeedLink<?> getFriendsActivityFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.FRIENDSACTIVITY_REL);
  }  
  
  /** Returns a link to the user's recent activity feed. */
  public FeedLink<?> getRecentActivityFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.RECENTACTIVITY_REL);
  }  

  /** Returns a link to the playlist defined by the user as video log. */
  public Link getVideoLogLink() {
    return getLink(YouTubeNamespace.VLOG_REL, Link.Type.ATOM);
  }
  
  /** Returns a link to the featured video of the user's profile. */
  public Link getFeaturedVideoLink() {
    return getLink(YouTubeNamespace.FEATURED_VIDEO_REL, Link.Type.ATOM);
  }  
  
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);

    extProfile.declareAdditionalNamespace(YouTubeNamespace.NS);

    // yt:aboutMe only in version 2
    extProfile.declare(UserProfileEntry.class, YtAboutMe.class);
    extProfile.declare(UserProfileEntry.class, YtAge.class);
    extProfile.declare(UserProfileEntry.class, YtBooks.class);
    extProfile.declare(UserProfileEntry.class, YtCompany.class);
    // yt:description only in version 1
    extProfile.declare(UserProfileEntry.class, YtDescription.class);
    extProfile.declare(UserProfileEntry.class, YtGender.class);
    extProfile.declare(UserProfileEntry.class, YtHobbies.class);
    extProfile.declare(UserProfileEntry.class, YtHometown.class);
    extProfile.declare(UserProfileEntry.class, YtLocation.class);
    extProfile.declare(UserProfileEntry.class, YtMovies.class);
    extProfile.declare(UserProfileEntry.class, YtMusic.class);
    extProfile.declare(UserProfileEntry.class, YtOccupation.class);
    extProfile.declare(UserProfileEntry.class, YtRelationship.class);
    extProfile.declare(UserProfileEntry.class, YtSchool.class);
    extProfile.declare(UserProfileEntry.class, YtUsername.class);

    ExtensionDescription mediaThumbnailDescription =
        ExtensionDescription.getDefaultDescription(MediaThumbnail.class);
    mediaThumbnailDescription.setRepeatable(false);
    extProfile.declare(UserProfileEntry.class, mediaThumbnailDescription);
    
    extProfile.declare(UserProfileEntry.class, YtUserProfileStatistics.class);
    
    ExtensionDescription feedLinkDescription = FeedLink.getDefaultDescription();
    feedLinkDescription.setRepeatable(true);
    extProfile.declare(UserProfileEntry.class, feedLinkDescription);
    
    extProfile.declare(UserProfileEntry.class, YtFirstName.class);
    extProfile.declare(UserProfileEntry.class, YtLastName.class);

    extProfile.declareArbitraryXmlExtension(UserProfileEntry.class);
  }
}
