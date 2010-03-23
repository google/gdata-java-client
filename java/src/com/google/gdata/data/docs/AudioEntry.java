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


package com.google.gdata.data.docs;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.util.Namespaces;

/**
 * An entry representing a single audio file with a {@link DocumentListFeed}.
 * 
 * 
 */
@Kind.Term(AudioEntry.KIND)
public class AudioEntry extends DocumentListEntry {

  /**
   * Label for category.
   */
  public static final String LABEL = "audio";
  
  /**
   * Kind category term used to label the entries which are of this document type.
   */
  public static final String KIND = DocsNamespace.DOCS_PREFIX + AudioEntry.LABEL;
  
  /**
   * Category used to label entries which are of this document type.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND, LABEL);
  
  /** 
   * Constructs a new uninitialized entry, to be populated by the GData parsers.
   */
  public AudioEntry() {
    super();
    getCategories().remove(DocumentListEntry.CATEGORY);
    getCategories().add(CATEGORY);
  }
  
  /**
   * Constructs a new entry by doing a shallow copy from another BaseEntry instance.
   */
  public AudioEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }
  
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(AudioEntry.class)) {
      return;
    }
    
    super.declareExtensions(extProfile);
    extProfile.declare(AudioEntry.class, Album.class);
    extProfile.declare(AudioEntry.class, AlbumArt.class);
    extProfile.declare(AudioEntry.class, AlbumArtist.class);
    extProfile.declare(AudioEntry.class, Artist.class);
    extProfile.declare(AudioEntry.class, Composer.class);
    extProfile.declare(AudioEntry.class, Duration.class);
    extProfile.declare(AudioEntry.class, Genre.class);
    extProfile.declare(AudioEntry.class, Size.class);
    extProfile.declare(AudioEntry.class, Track.class);
    extProfile.declare(AudioEntry.class, Year.class);
  }
  
  /**
   * Returns the album.
   */
  public Album getAlbum() {
    return getExtension(Album.class);
  }
  
  /**
   * Sets the album.
   * @param album album or <code>null</code> to reset
   */
  public void setAlbum(Album album) {
    if (album == null) {
      removeExtension(Album.class);
    } else {
      setExtension(album);
    }
  }
  
  /**
   * Returns whether it has the album.
   */
  public boolean hasAlbum() {
    return hasExtension(Album.class);
  }
  
  /**
   * Returns the album art.
   */
  public AlbumArt getAlbumArt() {
    return getExtension(AlbumArt.class);
  }
  
  /**
   * Sets the album art.
   * @param albumArt album art or <code>null</code> to reset
   */
  public void setAlbumArt(AlbumArt albumArt) {
    if (albumArt == null) {
      removeExtension(AlbumArt.class);
    } else {
      setExtension(albumArt);
    }
  }
  
  /**
   * Returns whether it has the album art.
   */
  public boolean hasAlbumArt() {
    return hasExtension(AlbumArt.class);
  }
  
  /**
   * Returns the album artist.
   */
  public AlbumArtist getAlbumArtist() {
    return getExtension(AlbumArtist.class);
  }
  
  /**
   * Sets the album artist.
   * @param albumArtist album artist or <code>null</code> to reset
   */
  public void setAlbumArtist(AlbumArtist albumArtist) {
    if (albumArtist == null) {
      removeExtension(AlbumArtist.class);
    } else {
      setExtension(albumArtist);
    }
  }
  
  /**
   * Returns whether it has the album artist.
   */
  public boolean hasAlbumArtist() {
    return hasExtension(AlbumArtist.class);
  }

  /**
   * Returns the artist.
   */
  public Artist getArtist() {
    return getExtension(Artist.class);
  }
  
  /**
   * Sets the artist.
   * @param artist artist or <code>null</code> to reset
   */
  public void setArtist(Artist artist) {
    if (artist == null) {
      removeExtension(Artist.class);
    } else {
      setExtension(artist);
    }
  }
  
  /**
   * Returns whether it has the artist.
   */
  public boolean hasArtist() {
    return hasExtension(Artist.class);
  }

  
  /**
   * Returns the composer.
   */
  public Composer getComposer() {
    return getExtension(Composer.class);
  }
  
  /**
   * Sets the composer.
   * @param composer composer or <code>null</code> to reset
   */
  public void setComposer(Composer composer) {
    if (composer == null) {
      removeExtension(Composer.class);
    } else {
      setExtension(composer);
    }
  }
  
  /**
   * Returns whether it has the composer.
   */
  public boolean hasComposer() {
    return hasExtension(Composer.class);
  }

  /**
   * Returns the duration.
   */
  public Duration getDuration() {
    return getExtension(Duration.class);
  }
  
  /**
   * Sets the duration
   * @param duration duration or <code>null</code> to reset
   */
  public void setDuration(Duration duration) {
    if (duration == null) {
      removeExtension(Duration.class);
    } else {
      setExtension(duration);
    }
  }
  
  /**
   * Returns whether it has the duration.
   */
  public boolean hasDuration() {
    return hasExtension(Duration.class);
  }

  /**
   * Returns the genre.
   */
  public Genre getGenre() {
    return getExtension(Genre.class);
  }
  
  /**
   * Sets the genre.
   * @param genre genre or <code>null</code> to reset
   */
  public void setGenre(Genre genre) {
    if (genre == null) {
      removeExtension(Genre.class);
    } else {
      setExtension(genre);
    }
  }
  
  /**
   * Returns whether it has the genre.
   */
  public boolean hasGenre() {
    return hasExtension(Genre.class);
  }

  /**
   * Returns the size.
   */
  public Size getSize() {
    return getExtension(Size.class);
  }
  
  /**
   * Sets the size.
   * @param size size or <code>null</code> to reset
   */
  public void setSize(Size size) {
    if (size == null) {
      removeExtension(Size.class);
    } else {
      setExtension(size);
    }
  }
  
  /**
   * Returns whether it has the size.
   */
  public boolean hasSize() {
    return hasExtension(Size.class);
  }

  /**
   * Returns the track.
   */
  public Track getTrack() {
    return getExtension(Track.class);
  }
  
  /**
   * Sets the track.
   * @param track track or <code>null</code> to reset
   */
  public void setTrack(Track track) {
    if (track == null) {
      removeExtension(Track.class);
    } else {
      setExtension(track);
    }
  }
  
  /**
   * Returns whether it has the track.
   */
  public boolean hasTrack() {
    return hasExtension(Track.class);
  }

  /**
   * Returns the year.
   */
  public Year getYear() {
    return getExtension(Year.class);
  }
  
  /**
   * Sets the year.
   * @param year year or <code>null</code> to reset
   */
  public void setYear(Year year) {
    if (year == null) {
      removeExtension(Year.class);
    } else {
      setExtension(year);
    }
  }
  
  /**
   * Returns whether it has the year.
   */
  public boolean hasYear() {
    return hasExtension(Year.class);
  }
}
