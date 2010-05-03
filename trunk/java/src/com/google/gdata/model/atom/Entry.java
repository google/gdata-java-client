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


package com.google.gdata.model.atom;

import com.google.common.collect.Lists;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.client.GDataProtocol;
import com.google.gdata.client.Service;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.IEntry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.ElementMetadata.Cardinality;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.model.atompub.Control;
import com.google.gdata.model.atompub.Edited;
import com.google.gdata.model.batch.BatchId;
import com.google.gdata.model.batch.BatchInterrupted;
import com.google.gdata.model.batch.BatchOperation;
import com.google.gdata.model.batch.BatchStatus;
import com.google.gdata.model.gd.GdAttributes;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.NotModifiedException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The Entry class is a base class that defines the in-memory object model for
 * GData entries.
 * 
 * <p>
 * Entry subclasses should expose convenience APIs to retrieve and set
 * attributes or subelements, with implementations that delegate to
 * {@link Element} methods to store/retrieve the appropriate data.
 * 
 * <p>
 * Here is the Relax-NG schema that represents an Atom 1.0 entry:
 * 
 * <pre>
 * atomEntry =
 *   element atom:entry {
 *     atomCommonAttributes,
 *     (atomAuthor*
 *     &amp; atomCategory*
 *     &amp; atomContent?
 *     &amp; atomContributor*
 *     &amp; atomId
 *     &amp; atomLink*
 *     &amp; atomPublished?
 *     &amp; atomRights?
 *     &amp; atomSource?
 *     &amp; atomSummary?
 *     &amp; atomTitle
 *     &amp; atomUpdated
 *     &amp; extensionElement*)
 * </pre>
 */
public class Entry extends Element implements IEntry {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void, Entry> KEY =
      ElementKey.of(new QName(Namespaces.atomNs, "entry"), Entry.class);

  // Some of the Keys from Source are replicated below for easier
  // discoverability via Javadoc. Since Entry doesn't directly extend Source
  // but shares many primitive types, we'll at least include the Key
  // constants for the shared types here.

  /**
   * The atom:id child element
   */
  public static final ElementKey<String, Element> ID = Source.ID;

  /**
   * The atom:updated child element
   */
  public static final ElementKey<DateTime, Element> UPDATED = Source.UPDATED;

  /**
   * The atom:published child element
   */
  public static final ElementKey<DateTime, Element> PUBLISHED =
      ElementKey.of(new QName(Namespaces.atomNs, "published"), DateTime.class,
          Element.class);

  /**
   * The atom:title child element
   */
  public static final ElementKey<String, TextContent> TITLE = Source.TITLE;

  /**
   * The atom:rights child element
   */
  public static final ElementKey<String, TextContent> RIGHTS = Source.RIGHTS;


  /**
   * The atom:summary child element.
   */
  public static final ElementKey<String, TextContent> SUMMARY =
      ElementKey.of(new QName(Namespaces.atomNs, "summary"), String.class,
          TextContent.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // Build the default metadata for our directly included elements.
    registry.build(PUBLISHED);
    registry.build(SUMMARY);

    // Build atom:entry.
    ElementCreator builder =
        registry.build(KEY).setCardinality(Cardinality.MULTIPLE);
    builder.addAttribute(GdAttributes.ETAG);
    builder.addAttribute(GdAttributes.KIND);
    builder.addAttribute(GdAttributes.FIELDS);
    builder.addElement(ID);
    builder.addElement(PUBLISHED);
    builder.addElement(UPDATED);
    builder.addElement(Edited.KEY);
    builder.addElement(Control.KEY);
    builder.addElement(Category.KEY);
    builder.addElement(TITLE);
    builder.addElement(SUMMARY);
    builder.addElement(RIGHTS);
    builder.addElement(Content.KEY).adapt(TextContent.KIND, TextContent.KEY)
        .adapt(OtherContent.KIND, OtherContent.KEY).adapt(
            OutOfLineContent.KIND, OutOfLineContent.KEY);
    builder.addElement(Link.KEY);
    builder.addElement(Author.KEY);
    builder.addElement(Contributor.KEY);
    builder.addElement(Source.KEY);
    builder.addElement(BatchId.KEY);
    builder.addElement(BatchInterrupted.KEY);
    builder.addElement(BatchOperation.KEY);
    builder.addElement(BatchStatus.KEY);


    // Register adaptations.
    TextContent.registerMetadata(registry);
    OtherContent.registerMetadata(registry);
    OutOfLineContent.registerMetadata(registry);
  }

  /**
   * The EntryState class provides a simple structure that encapsulates the
   * attributes of an Atom entry that should be shared with a shallow copy if
   * the entry is adapted to a more specific Entry subtype.
   */
  protected static class EntryState {

    /**
     * Version ID. This is a unique number representing this particular entry.
     * Every update changes the version ID (unless the update doesn't modify
     * anything, in which case it's permissible for version ID to stay the
     * same). Services are free to interpret this string in the most convenient
     * way. Some services may choose to use a monotonically increasing sequence
     * of version IDs. Other services may compute a hash of entry properties and
     * use that.
     * 
     * <p>
     * This property is only used for services to communicate the current
     * version ID back to the servlet. It is NOT set when entries are parsed
     * (either from requests or from arbitrary XML).
     */
    public String versionId;

    /** Service. */
    public Service service;

    /** {code true} if the entry can be modified by a client. */
    public boolean canEdit = true;
  }

  /**
   * Basic state for this entry. May be shared across multiple adapted instances
   * associated with the same logical entry.
   */
  protected EntryState state;

  /**
   * Constructs a new Entry instance, using default metadata.
   */
  public Entry() {
    this(KEY);
  }

  /**
   * Constructs a new Entry instance, using the given key.
   */
  protected Entry(ElementKey<?, ? extends Entry> key) {
    super(key);
    state = new EntryState();
  }

  /**
   * Copy constructor that initializes a new Entry instance to have identical
   * contents to another instance, using a shared reference to the same
   * {@link EntryState}. Subclasses of {@code Entry} can use this constructor to
   * create adaptor instances of an entry that share state with the original.
   * 
   * @param key the element key to use for this entry.
   * @param source to copy data from
   */
  protected Entry(ElementKey<?, ? extends Entry> key, Entry source) {
    super(key, source);
    state = source.state;
  }

  public String getId() {
    return getElementValue(ID);
  }

  public void setId(String v) {
    if (v != null && "-".equals(v)) {
      // Disallow dash as an entry id. It leads to ambiguity because
      // we use a dash to separate category queries in a feed URI.
      // Does /feeds/feed-id/-/X mean a feed request with a category
      // query "X", or an entry request with "-" as the entry ID
      // and X as the version number. In {@link UriTemplate} we've
      // made the choice that it means a feed request. Therefore "-"
      // cannot be an entry ID.
      throw new IllegalArgumentException("Entry.id must not be equal to '-'.");
    }
    setElement(ID, (v == null) ? null : new Element(ID).setTextValue(v));
  }

  public String getVersionId() {
    return state.versionId;
  }

  public void setVersionId(String v) {
    state.versionId = v;
  }

  /**
   * Returns the {@link GdAttributes#ETAG} value for this feed. A value of
   * {@code null} indicates the value is unknown.
   */
  public String getEtag() {
    return getAttributeValue(GdAttributes.ETAG);
  }

  /**
   * Sets the {@link GdAttributes#ETAG} value for this entry. A value of {@code
   * null} indicates the value is unknown.
   */
  public void setEtag(String v) {
    setAttributeValue(GdAttributes.ETAG, v);
  }

  /**
   * Returns the {@link GdAttributes#KIND} value for this entry. The kind
   * attribute may be null if this feed does not have a kind.
   */
  public String getKind() {
    return getAttributeValue(GdAttributes.KIND);
  }

  /**
   * Sets the {@link GdAttributes#KIND} value for this entry. The kind may be
   * set to null to remove the attribute value.
   */
  public void setKind(String v) {
    setAttributeValue(GdAttributes.KIND, v);
  }

  /**
   * Returns the {@link GdAttributes#FIELDS} value for this entry. The fields
   * attribute may be null if this entry contains a full representation.
   */
  public String getSelectedFields() {
    return getAttributeValue(GdAttributes.FIELDS);
  }

  /**
   * Sets the{@link GdAttributes#FIELDS} value for this entry. The fields
   * attribute may be set to null to remove the attribute value.
   */
  public void setSelectedFields(String v) {
    setAttributeValue(GdAttributes.FIELDS, v);
  }

  public DateTime getPublished() {
    return getElementValue(PUBLISHED);
  }

  public void setPublished(DateTime v) {
    if (v != null && v.getTzShift() == null) {
      throw new IllegalArgumentException(
          "Entry.published must have a timezone.");
    }
    setElement(PUBLISHED, (v == null) ? null : new Element(PUBLISHED)
        .setTextValue(v));
  }

  public DateTime getUpdated() {
    return getElementValue(UPDATED);
  }

  public void setUpdated(DateTime v) {
    if (v != null && v.getTzShift() == null) {
      throw new IllegalArgumentException("Entry.updated must have a timezone.");
    }
    setElement(UPDATED, (v == null) ? null : new Element(UPDATED)
        .setTextValue(v));
  }


  public DateTime getEdited() {
    return getElementValue(Edited.KEY);
  }

  public void setEdited(DateTime v) {
    if (v != null && v.getTzShift() == null) {
      throw new IllegalArgumentException("Entry.edited must have a timezone.");
    }
    setElement(Edited.KEY, (v == null) ? null : new Edited(v));
  }

  public Set<Category> getCategories() {
    return getElementSet(Category.KEY);
  }

  public void addCategory(Category v) {
    addElement(Category.KEY, v);
  }

  public void clearCategories() {
    removeElement(Category.KEY);
  }

  public TextContent getTitle() {
    return getElement(TITLE);
  }

  public void setTitle(TextContent v) {
    setElement(TITLE, v);
  }

  public TextContent getSummary() {
    return getElement(SUMMARY);
  }

  public void setSummary(TextContent v) {
    setElement(SUMMARY, v);
  }

  public TextContent getRights() {
    return getElement(RIGHTS);
  }

  public void setRights(TextContent v) {
    setElement(RIGHTS, v);
  }

  public Content getContent() {
    return getElement(Content.KEY);
  }

  public void setContent(Content v) {
    setElement(Content.KEY, v);
  }

  /**
   * Removes any content element.
   * 
   * This method is equivalent to: {@code setContent((Content) null); }, but
   * without the ugly cast.
   */
  public void removeContent() {
    removeElement(Content.KEY);
  }

  /**
   * Assumes the content element's contents are text and returns them as a
   * TextContent.
   * 
   * @return A TextContent containing the value of the content tag.
   * 
   * @throws IllegalStateException If the content element is not a text type.
   */
  public TextContent getTextContent() {
    Content content = getContent();
    if (content == null) {
      return null;
    }
    if (!(content instanceof TextContent)) {
      throw new IllegalStateException("Content object is not a TextContent");
    }
    return (TextContent) content;
  }

  /**
   * Assumes the <content> element's contents are plain-text and returns its
   * value as a string
   * 
   * @return A string containing the plain-text value of the content tag.
   * 
   * @throws IllegalStateException If the content element is not a text type.
   */
  public String getPlainTextContent() {
    TextContent content = getTextContent();
    return (content == null) ? null : content.getPlainText();
  }

  public List<Link> getLinks() {
    return getElements(Link.KEY);
  }

  public void addLink(Link v) {
    addElement(Link.KEY, v);
  }

  public Link addLink(String rel, String type, String href) {
    try {
      Link link = new Link(rel, type, new URI(href));
      addLink(link);
      return link;
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  public boolean removeLink(Link link) {
    return removeElement(Link.KEY, link);
  }

  public void removeLinks() {
    removeElement(Link.KEY);
  }

  @SuppressWarnings("unchecked")
  public List<Person> getAuthors() {
    return (List<Person>) (List<?>) getElements(Author.KEY);
  }

  public void addAuthor(Person v) {
    addElement(Author.KEY, v);
  }

  public boolean removeAuthor(Person v) {
    return removeElement(Author.KEY, v);
  }

  public void clearAuthors() {
    removeElement(Author.KEY);
  }

  @SuppressWarnings("unchecked")
  public List<Person> getContributors() {
    return (List<Person>) (List<?>) getElements(Contributor.KEY);
  }

  public void addContributor(Person v) {
    addElement(Contributor.KEY, v);
  }

  public boolean removeContributor(Person v) {
    return removeElement(Contributor.KEY, v);
  }

  public void clearContributors() {
    removeElement(Contributor.KEY);
  }

  public Source getSource() {
    return getElement(Source.KEY);
  }

  public void setSource(Source v) {
    setElement(Source.KEY, v);
  }

  /**
   * Set draft status. Passing a null value means clearing the draft status.
   * 
   * @param v Draft status, or null to clear.
   */
  public void setDraft(Boolean v) {
    Control control = null;
    if (Boolean.TRUE.equals(v)) {
      control = new Control();
      control.setDraft(true);
    }
    setElement(Control.KEY, control);
  }

  /**
   * Draft status.
   * 
   * @return True if draft status is set and equals true.
   */
  public boolean isDraft() {
    Control control = getControl();
    return (control != null && control.isDraft());
  }

  /**
   * Gets the app:control tag.
   * 
   * @return pub control tag or null if unset
   */
  public Control getControl() {
    return getElement(Control.KEY);
  }

  /**
   * Sets the app:control tag, which usually contains app:draft.
   * 
   * @param value Control the new object or null
   */
  public Entry setControl(Control value) {
    setElement(Control.KEY, value);
    return this;
  }

  /**
   * Returns whether it has the Atom publication control status.
   * 
   * @return whether it has the Atom publication control status
   */
  public boolean hasControl() {
    return hasElement(Control.KEY);
  }

  public void setService(Service s) {
    state.service = s;
  }

  public Service getService() {
    return state.service;
  }

  public boolean getCanEdit() {
    return state.canEdit;
  }

  public void setCanEdit(boolean v) {
    state.canEdit = v;
  }

  /**
   * Retrieves the first link with the supplied {@code rel} and/or {@code type}
   * value.
   * <p>
   * If either parameter is {@code null}, doesn't return matches for that
   * parameter.
   * 
   * @param rel link relation
   * @param type link type
   * @return link
   */
  public Link getLink(String rel, String type) {

    for (Link link : getLinks()) {
      if (link.matches(rel, type)) {
        return link;
      }
    }

    return null;
  }

  /**
   * Return the links that match the given {@code rel} and {@code type} values.
   * 
   * @param relToMatch {@code rel} value to match or {@code null} to match any
   *        {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *        {@code type} value.
   * @return matching links.
   */
  public List<Link> getLinks(String relToMatch, String typeToMatch) {
    List<Link> result = new ArrayList<Link>();
    for (Link link : getLinks()) {
      if (link.matches(relToMatch, typeToMatch)) {
        result.add(link);
      }
    }
    return result;
  }

  /**
   * Remove all links that match the given {@code rel} and {@code type} values.
   * 
   * @param relToMatch {@code rel} value to match or {@code null} to match any
   *        {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *        {@code type} value.
   */
  public void removeLinks(String relToMatch, String typeToMatch) {
    List<Link> toRemove = Lists.newArrayList();
    for (Link link : getLinks()) {
      if (link.matches(relToMatch, typeToMatch)) {
        toRemove.add(link);
      }
    }
    for (Link link : toRemove) {
      removeLink(link);
    }
  }

  /**
   * Adds a link pointing to an HTML representation.
   * 
   * @param htmlUri Link URI.
   * 
   * @param lang Optional language code.
   * 
   * @param title Optional title.
   */
  public void addHtmlLink(String htmlUri, String lang, String title) {

    Link link = new Link();
    link.setRel(Link.Rel.ALTERNATE);
    link.setType(Link.Type.HTML);
    link.setHref(htmlUri);

    if (lang != null) {
      link.setHrefLang(lang);
    }

    if (title != null) {
      link.setTitle(title);
    }

    addLink(link);
  }

  /** Retrieves the resource access link. */
  public Link getSelfLink() {
    Link selfLink = getLink(Link.Rel.SELF, Link.Type.ATOM);
    return selfLink;
  }

  /** Retrieves the resource edit link. */
  public Link getEditLink() {
    Link editLink = getLink(Link.Rel.ENTRY_EDIT, Link.Type.ATOM);
    return editLink;
  }

  /** Retrieves the media resource edit link. */
  public Link getMediaEditLink() {
    Link mediaLink = getLink(Link.Rel.MEDIA_EDIT, null);
    return mediaLink;
  }

  /** Retrieves the media resource resumable edit link. */
  public Link getResumableEditMediaLink() {
    Link resumableEditMediaLink = getLink(Link.Rel.RESUMABLE_EDIT_MEDIA, null);
    return resumableEditMediaLink;
  }

  /** Retrieves the first HTML link. */
  public Link getHtmlLink() {
    Link htmlLink = getLink(Link.Rel.ALTERNATE, Link.Type.HTML);
    return htmlLink;
  }

  /**
   * Retrieves the current version of this Entry by requesting it from the
   * associated GData service.
   * 
   * @return the current version of the entry.
   * @throws IOException on IO error during retrieval from the service.
   * @throws ServiceException if there is a problem retrieving the element.
   */
  public Entry getSelf() throws IOException, ServiceException {
    if (state.service == null) {
      throw new ServiceException(CoreErrorDomain.ERR.entryNotAssociated);
    }
    Link selfLink = getSelfLink();
    if (selfLink == null) {
      throw new UnsupportedOperationException("Entry cannot be retrieved");
    }

    URL entryUrl = selfLink.getHrefUri().toURL();
    try {
      // If an etag is available, use it to conditionalize the retrieval,
      // otherwise, use time of last edit or update.
      String etag = getEtag();
      if (etag != null) {
        return state.service.getEntry(entryUrl, this.getClass(), etag);
      } else {
        return state.service.getEntry(entryUrl, this.getClass(),
            (getEdited() != null ? getEdited() : getUpdated()));
      }
    } catch (NotModifiedException e) {
      return this;
    }
  }

  /**
   * Updates this entry by sending the current representation to the associated
   * GData service.
   * 
   * @return the updated entry returned by the Service.
   * 
   * @throws ServiceException If there is no associated GData service or the
   *         service is unable to perform the update.
   * 
   * @throws UnsupportedOperationException If update is not supported for the
   *         target entry.
   * 
   * @throws IOException If there is an error communicating with the GData
   *         service.
   */
  public Entry update() throws IOException, ServiceException {

    if (state.service == null) {
      throw new ServiceException(CoreErrorDomain.ERR.entryNotAssociated);
    }
    Link editLink = getEditLink();
    if (editLink == null) {
      throw new UnsupportedOperationException("Entry cannot be updated");
    }

    URL editUrl = editLink.getHrefUri().toURL();
    return state.service.update(editUrl, this);
  }

  /**
   * Deletes this entry by sending a request to the associated GData service.
   * 
   * @throws ServiceException If there is no associated GData service or the
   *         service is unable to perform the deletion.
   * 
   * @throws UnsupportedOperationException If deletion is not supported for the
   *         target entry.
   * 
   * @throws IOException If there is an error communicating with the GData
   *         service.
   */
  public void delete() throws IOException, ServiceException {

    if (state.service == null) {
      throw new ServiceException(CoreErrorDomain.ERR.entryNotAssociated);
    }
    Link editLink = getEditLink();
    if (editLink == null) {
      throw new UnsupportedOperationException("Entry cannot be deleted");
    }

    // Delete the entry, using strong etag (if available) as a precondition.
    URI editUrl = editLink.getHrefUri();
    String etag = getEtag();
    state.service.delete(editUrl, GDataProtocol.isWeakEtag(etag) ? null : etag);
  }

  /**
   * Narrows this entry using categories with an appropriate kind value. This
   * will loop through the categories, checking if they represent kinds, and
   * adapting the entry to that kind of an appropriate adaptation was found.
   * This will return the most specific subtype of the narrowed type that could
   * be found.
   */
  @Override
  protected Element narrow(ElementMetadata<?, ?> metadata, ValidationContext vc) {
    String term = Kinds.getElementKind(this);
    if (term != null) {
      return adapt(this, metadata, term);
    }
    return super.narrow(metadata, vc);
  }
}
