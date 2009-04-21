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


package com.google.gdata.data.books;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.dublincore.Creator;
import com.google.gdata.data.dublincore.Date;
import com.google.gdata.data.dublincore.Description;
import com.google.gdata.data.dublincore.Format;
import com.google.gdata.data.dublincore.Identifier;
import com.google.gdata.data.dublincore.Language;
import com.google.gdata.data.dublincore.Publisher;
import com.google.gdata.data.dublincore.Subject;
import com.google.gdata.data.dublincore.Title;
import com.google.gdata.data.extensions.Comments;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes an entry in a feed of Book Search volumes.
 *
 * 
 */
@Kind.Term(VolumeEntry.KIND)
public class VolumeEntry extends BaseEntry<VolumeEntry> {

  /**
   * Volume kind term value.
   */
  public static final String KIND = BooksNamespace.GBS_PREFIX + "volume";

  /**
   * Volume kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public VolumeEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public VolumeEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(VolumeEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(VolumeEntry.class,
        new ExtensionDescription(Comments.class, new XmlNamespace("gd",
        "http://schemas.google.com/g/2005"), "comments", false, false, false));
    new Comments().declareExtensions(extProfile);
    extProfile.declare(VolumeEntry.class, Creator.getDefaultDescription(false,
        true));
    extProfile.declare(VolumeEntry.class, Date.getDefaultDescription(false,
        true));
    extProfile.declare(VolumeEntry.class,
        Description.getDefaultDescription(false, true));
    extProfile.declare(VolumeEntry.class, Embeddability.class);
    extProfile.declare(VolumeEntry.class, Format.getDefaultDescription(false,
        true));
    extProfile.declare(VolumeEntry.class,
        Identifier.getDefaultDescription(false, true));
    extProfile.declare(VolumeEntry.class, Language.getDefaultDescription(false,
        true));
    extProfile.declare(VolumeEntry.class, OpenAccess.class);
    extProfile.declare(VolumeEntry.class, Publisher.getDefaultDescription(false,
        true));
    extProfile.declare(VolumeEntry.class, new ExtensionDescription(Rating.class,
        new XmlNamespace("gd", "http://schemas.google.com/g/2005"), "rating",
        false, false, false));
    extProfile.declare(VolumeEntry.class, Review.class);
    extProfile.declare(VolumeEntry.class, Subject.getDefaultDescription(false,
        true));
    extProfile.declare(VolumeEntry.class, Title.getDefaultDescription(false,
        true));
    extProfile.declare(VolumeEntry.class, Viewability.class);
  }

  /**
   * Returns the Comments class.
   *
   * @return Comments class
   */
  public Comments getComments() {
    return getExtension(Comments.class);
  }

  /**
   * Sets the Comments class.
   *
   * @param comments Comments class or <code>null</code> to reset
   */
  public void setComments(Comments comments) {
    if (comments == null) {
      removeExtension(Comments.class);
    } else {
      setExtension(comments);
    }
  }

  /**
   * Returns whether it has the Comments class.
   *
   * @return whether it has the Comments class
   */
  public boolean hasComments() {
    return hasExtension(Comments.class);
  }

  /**
   * Returns the creators.
   *
   * @return creators
   */
  public List<Creator> getCreators() {
    return getRepeatingExtension(Creator.class);
  }

  /**
   * Adds a new creator.
   *
   * @param creator creator
   */
  public void addCreator(Creator creator) {
    getCreators().add(creator);
  }

  /**
   * Returns whether it has the creators.
   *
   * @return whether it has the creators
   */
  public boolean hasCreators() {
    return hasRepeatingExtension(Creator.class);
  }

  /**
   * Returns the dates.
   *
   * @return dates
   */
  public List<Date> getDates() {
    return getRepeatingExtension(Date.class);
  }

  /**
   * Adds a new date.
   *
   * @param date date
   */
  public void addDate(Date date) {
    getDates().add(date);
  }

  /**
   * Returns whether it has the dates.
   *
   * @return whether it has the dates
   */
  public boolean hasDates() {
    return hasRepeatingExtension(Date.class);
  }

  /**
   * Returns the descriptions.
   *
   * @return descriptions
   */
  public List<Description> getDescriptions() {
    return getRepeatingExtension(Description.class);
  }

  /**
   * Adds a new description.
   *
   * @param description description
   */
  public void addDescription(Description description) {
    getDescriptions().add(description);
  }

  /**
   * Returns whether it has the descriptions.
   *
   * @return whether it has the descriptions
   */
  public boolean hasDescriptions() {
    return hasRepeatingExtension(Description.class);
  }

  /**
   * Returns the embeddability.
   *
   * @return embeddability
   */
  public Embeddability getEmbeddability() {
    return getExtension(Embeddability.class);
  }

  /**
   * Sets the embeddability.
   *
   * @param embeddability embeddability or <code>null</code> to reset
   */
  public void setEmbeddability(Embeddability embeddability) {
    if (embeddability == null) {
      removeExtension(Embeddability.class);
    } else {
      setExtension(embeddability);
    }
  }

  /**
   * Returns whether it has the embeddability.
   *
   * @return whether it has the embeddability
   */
  public boolean hasEmbeddability() {
    return hasExtension(Embeddability.class);
  }

  /**
   * Returns the formats.
   *
   * @return formats
   */
  public List<Format> getFormats() {
    return getRepeatingExtension(Format.class);
  }

  /**
   * Adds a new format.
   *
   * @param format format
   */
  public void addFormat(Format format) {
    getFormats().add(format);
  }

  /**
   * Returns whether it has the formats.
   *
   * @return whether it has the formats
   */
  public boolean hasFormats() {
    return hasRepeatingExtension(Format.class);
  }

  /**
   * Returns the identifiers.
   *
   * @return identifiers
   */
  public List<Identifier> getIdentifiers() {
    return getRepeatingExtension(Identifier.class);
  }

  /**
   * Adds a new identifier.
   *
   * @param identifier identifier
   */
  public void addIdentifier(Identifier identifier) {
    getIdentifiers().add(identifier);
  }

  /**
   * Returns whether it has the identifiers.
   *
   * @return whether it has the identifiers
   */
  public boolean hasIdentifiers() {
    return hasRepeatingExtension(Identifier.class);
  }

  /**
   * Returns the languages.
   *
   * @return languages
   */
  public List<Language> getLanguages() {
    return getRepeatingExtension(Language.class);
  }

  /**
   * Adds a new language.
   *
   * @param language language
   */
  public void addLanguage(Language language) {
    getLanguages().add(language);
  }

  /**
   * Returns whether it has the languages.
   *
   * @return whether it has the languages
   */
  public boolean hasLanguages() {
    return hasRepeatingExtension(Language.class);
  }

  /**
   * Returns the open access.
   *
   * @return open access
   */
  public OpenAccess getOpenAccess() {
    return getExtension(OpenAccess.class);
  }

  /**
   * Sets the open access.
   *
   * @param openAccess open access or <code>null</code> to reset
   */
  public void setOpenAccess(OpenAccess openAccess) {
    if (openAccess == null) {
      removeExtension(OpenAccess.class);
    } else {
      setExtension(openAccess);
    }
  }

  /**
   * Returns whether it has the open access.
   *
   * @return whether it has the open access
   */
  public boolean hasOpenAccess() {
    return hasExtension(OpenAccess.class);
  }

  /**
   * Returns the publishers.
   *
   * @return publishers
   */
  public List<Publisher> getPublishers() {
    return getRepeatingExtension(Publisher.class);
  }

  /**
   * Adds a new publisher.
   *
   * @param publisher publisher
   */
  public void addPublisher(Publisher publisher) {
    getPublishers().add(publisher);
  }

  /**
   * Returns whether it has the publishers.
   *
   * @return whether it has the publishers
   */
  public boolean hasPublishers() {
    return hasRepeatingExtension(Publisher.class);
  }

  /**
   * Returns the rating.
   *
   * @return rating
   */
  public Rating getRating() {
    return getExtension(Rating.class);
  }

  /**
   * Sets the rating.
   *
   * @param rating rating or <code>null</code> to reset
   */
  public void setRating(Rating rating) {
    if (rating == null) {
      removeExtension(Rating.class);
    } else {
      setExtension(rating);
    }
  }

  /**
   * Returns whether it has the rating.
   *
   * @return whether it has the rating
   */
  public boolean hasRating() {
    return hasExtension(Rating.class);
  }

  /**
   * Returns the review.
   *
   * @return review
   */
  public Review getReview() {
    return getExtension(Review.class);
  }

  /**
   * Sets the review.
   *
   * @param review review or <code>null</code> to reset
   */
  public void setReview(Review review) {
    if (review == null) {
      removeExtension(Review.class);
    } else {
      setExtension(review);
    }
  }

  /**
   * Returns whether it has the review.
   *
   * @return whether it has the review
   */
  public boolean hasReview() {
    return hasExtension(Review.class);
  }

  /**
   * Returns the subjects.
   *
   * @return subjects
   */
  public List<Subject> getSubjects() {
    return getRepeatingExtension(Subject.class);
  }

  /**
   * Adds a new subject.
   *
   * @param subject subject
   */
  public void addSubject(Subject subject) {
    getSubjects().add(subject);
  }

  /**
   * Returns whether it has the subjects.
   *
   * @return whether it has the subjects
   */
  public boolean hasSubjects() {
    return hasRepeatingExtension(Subject.class);
  }

  /**
   * Returns the titles.
   *
   * @return titles
   */
  public List<Title> getTitles() {
    return getRepeatingExtension(Title.class);
  }

  /**
   * Adds a new title.
   *
   * @param title title
   */
  public void addTitle(Title title) {
    getTitles().add(title);
  }

  /**
   * Returns whether it has the titles.
   *
   * @return whether it has the titles
   */
  public boolean hasTitles() {
    return hasRepeatingExtension(Title.class);
  }

  /**
   * Returns the viewability.
   *
   * @return viewability
   */
  public Viewability getViewability() {
    return getExtension(Viewability.class);
  }

  /**
   * Sets the viewability.
   *
   * @param viewability viewability or <code>null</code> to reset
   */
  public void setViewability(Viewability viewability) {
    if (viewability == null) {
      removeExtension(Viewability.class);
    } else {
      setExtension(viewability);
    }
  }

  /**
   * Returns whether it has the viewability.
   *
   * @return whether it has the viewability
   */
  public boolean hasViewability() {
    return hasExtension(Viewability.class);
  }

  /**
   * Returns the annotation link to submit review, rating, labels.
   *
   * @return Annotation link to submit review, rating, labels or {@code null}
   *     for none.
   */
  public Link getAnnotationLink() {
    return getLink(BooksLink.Rel.ANNOTATION, Link.Type.ATOM);
  }

  /**
   * Returns the link that provides the URI of an alternate format of the
   * entry's or feed's contents.
   *
   * @return Link that provides the URI of an alternate format of the entry's or
   *     feed's contents or {@code null} for none.
   */
  public Link getAtomAlternateLink() {
    return getLink(Link.Rel.ALTERNATE, Link.Type.ATOM);
  }

  /**
   * Returns the epub download link.
   *
   * @return Epub download link or {@code null} for none.
   */
  public Link getEpubDownloadLink() {
    return getLink(BooksLink.Rel.EPUBDOWNLOAD, BooksLink.Type.EPUB);
  }

  /**
   * Returns the link to a description page.
   *
   * @return Link to a description page or {@code null} for none.
   */
  public Link getInfoLink() {
    return getLink(BooksLink.Rel.INFO, Link.Type.HTML);
  }

  /**
   * Returns the link to a preview page.
   *
   * @return Link to a preview page or {@code null} for none.
   */
  public Link getPreviewLink() {
    return getLink(BooksLink.Rel.PREVIEW, Link.Type.HTML);
  }

  /**
   * Returns the link that provides the URI of a thumbnail image.
   *
   * @return Link that provides the URI of a thumbnail image or {@code null} for
   *     none.
   */
  public Link getThumbnailLink() {
    return getLink(BooksLink.Rel.THUMBNAIL, null);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{VolumeEntry " + super.toString() + "}";
  }

}
