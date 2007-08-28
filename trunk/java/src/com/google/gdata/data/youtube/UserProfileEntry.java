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


package com.google.gdata.data.youtube;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.media.mediarss.MediaThumbnail;

import java.util.List;

/**
 * An atom entry containing a user profile.
 *
 * 
 */
@Kind.Term(YouTubeNamespace.KIND_USER_PROFILE)
public class UserProfileEntry extends BaseEntry<UserProfileEntry> {

  public UserProfileEntry() {
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_USER_PROFILE);
  }

  public UserProfileEntry(BaseEntry base) {
    super(base);
    EntryUtils.addKindCategory(this, YouTubeNamespace.KIND_USER_PROFILE);
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

  /** Gets the channel description. */
  public String getDescription() {
    YtDescription description = getExtension(YtDescription.class);
    return description == null ? null : description.getContent();
  }

  /** Sets the channel description. */
  public void setDescription(String description) {
    if (description == null) {
      removeExtension(YtDescription.class);
    } else {
      setExtension(new YtDescription(description));
    }
  }

  /** Returns a modifiable list of {@link FeedLink}s. */
  public List<FeedLink> getFeedLinks() {
    return getRepeatingExtension(FeedLink.class);
  }

  /** Returns a link to the user favorites feed. */
  public FeedLink getFavoritesFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.FAVORITES_REL);
  }

  /** Returns a link to the user contacts (friends) feed. */
  public FeedLink getContactsFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.CONTACTS_REL);
  }

  /** Returns a link to the user playlists feed. */
  public FeedLink getPlaylistsFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.PLAYLISTS_REL);
  }

  /** Returns a link to the users subscriptions feed. */
  public FeedLink getSubscriptionsFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.SUBSCRIPTIONS_REL);
  }

  /** Returns a link to the user uploads feed. */
  public FeedLink getUploadsFeedLink() {
    return EntryUtils.getFeedLink(this, YouTubeNamespace.UPLOADS_REL);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);

    extProfile.declareAdditionalNamespace(YouTubeNamespace.NS);

    extProfile.declare(UserProfileEntry.class, YtAge.class);
    extProfile.declare(UserProfileEntry.class, YtBooks.class);
    extProfile.declare(UserProfileEntry.class, YtCompany.class);
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
    extProfile.declare(UserProfileEntry.class, MediaThumbnail.class);

    ExtensionDescription feedLinkDescription = FeedLink.getDefaultDescription();
    feedLinkDescription.setRepeatable(true);
    extProfile.declare(UserProfileEntry.class, feedLinkDescription);

    extProfile.declareArbitraryXmlExtension(UserProfileEntry.class);
  }
}
