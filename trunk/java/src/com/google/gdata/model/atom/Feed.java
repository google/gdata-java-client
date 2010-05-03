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

import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.client.Query;
import com.google.gdata.client.Service;
import com.google.gdata.data.IFeed;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.model.batch.BatchOperation;
import com.google.gdata.model.gd.GdAttributes;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.NotModifiedException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.ContentCreationException;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The Feed class is a base class that represents a generic GData feed object,
 * based primarily on the data model for an {@code <atom:feed>} element.
 * It is extended to represent OpenSearch RSS channel elements and other gdata
 * standard elements.
 *
 * <p>The Feed Class contains all the necessary parsing and generation code for
 * feed data, but can be subclassed to create subtypes that contain convenience
 * APIs for accessing additional elements and entries.
 *
 * <p>An instance can be initialized by directly initializing its component
 * elements.
 *
 * <p>Here is the Relax-NG schema that represents an Atom 1.0 feed:
 *
 * <pre>
 * AtomFeed =
 *  element atom:feed {
 *    atomCommonAttributes,
 *    (atomAuthor*
 *     atomCategory*
 *     atomContributor*
 *     atomGenerator?
 *     atomIcon?
 *     atomId
 *     atomLink*
 *     atomLogo?
 *     atomRights?
 *     atomSubtitle?
 *     atomTitle
 *     atomUpdated
 *     extensionElement*),
 *     atomEntry*
 *   }
 * </pre>
 *
 * <p>Because the Feed schema differs from the Source schema only by the
 * presence of the entries, the Feed class derives its base property model
 * from the {@link Source} class.
 */
public class Feed extends Source implements IFeed {

  /**
   * The key for this element.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<Void, Feed> KEY = ElementKey.of(
      new QName(Namespaces.atomNs, "feed"), Feed.class);

  /**
   * The xml:base attribute.
   */
  public static final AttributeKey<URI> XML_BASE = AttributeKey.of(
      new QName(Namespaces.xmlNs, "base"), URI.class);
  
  /**
   * The opensearch:itemsPerPage element.
   */
  public static final ElementKey<Integer, Element> ITEMS_PER_PAGE =
      ElementKey.of(new QName(Namespaces.openSearch1_1Ns, "itemsPerPage"),
          Integer.class, Element.class);

  /**
   * The opensearch:startIndex element.
   */
    public static final ElementKey<Integer, Element> START_INDEX =
      ElementKey.of(new QName(Namespaces.openSearch1_1Ns, "startIndex"),
          Integer.class, Element.class);

  /**
   * The opensearch:totalResults element.
   */
    public static final ElementKey<Integer, Element> TOTAL_RESULTS =
      ElementKey.of(new QName(Namespaces.openSearch1_1Ns, "totalResults"),
          Integer.class, Element.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // Register superclass metadata.
    Source.registerMetadata(registry);

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addAttribute(GdAttributes.ETAG);
    builder.addAttribute(GdAttributes.KIND);
    builder.addAttribute(GdAttributes.FIELDS);
    builder.addAttribute(XML_BASE);
    builder.addElement(TOTAL_RESULTS);
    builder.addElement(START_INDEX);
    builder.addElement(ITEMS_PER_PAGE);
    builder.addElement(BatchOperation.KEY);
    builder.addUndeclaredElementMarker();
    builder.addElement(Entry.KEY);
  }

  /**
   * The FeedState class provides a simple structure that encapsulates the
   * attributes of an Atom feed that should be shared with a shallow copy if the
   * feed is adapted to a more specific Feed subtypes.
   *
   * <p><b>Note: Feed entries are not part of feed shared state, because the
   * entry lists will need to be typed differently for adapted instances.</b>
   * This means that entries that are created, updated, or deleted in an adapted
   * feed will not be reflected in the base feed used to construct it. The
   * reverse is also true: changes made to a base feed will not be reflected in
   * any adapted instances of the feed.
   */
  protected static class FeedState {

    /** Service associated with the feed. */
    public Service service;

    /** Specifies whether the feed can be posted to. */
    public boolean canPost = true;

    /**
     * Version ID. This is a unique number representing this particular
     * entry. Every update changes the version ID (unless the update
     * doesn't modify anything, in which case it's permissible for
     * version ID to stay the same). Services are free to interpret this
     * string in the most convenient way. Some services may choose to use
     * a monotonically increasing sequence of version IDs. Other services
     * may compute a hash of entry properties and use that.
     *
     * <p>This property is only used for services to communicate the current
     * version ID back to the servlet. It is NOT set when entries are
     * parsed (either from requests or from arbitrary XML).
     */
    public String versionId;
  }

  /**
   * Basic state for this feed. May be shared across multiple adapted instances
   * associated with the same logical feed.
   */
  protected final FeedState feedState;

  /**
   * Constructs a new Feed instance, using default metadata.
   */
  public Feed() {
    this(KEY);
  }

  /**
   * Creates a new feed instance using the specified metadata.
   *
   * @param key the feed key.
   */
  protected Feed(ElementKey<?, ? extends Feed> key) {
    super(key);
    feedState = new FeedState();
  }

  /**
   * Copy constructor that initializes a new Feed instance to have identical
   * contents to another instance, using a shared reference to the same
   * {@link FeedState}. Subclasses of {@code Feed} can use this constructor to
   * create adaptor instances of a feed that share state with the original but
   * use a different set of metadata.
   */
  protected Feed(ElementKey<?, ? extends Feed> key, Feed source) {
    super(key, source);
    feedState = source.feedState;
  }

  /**
   * Returns that GData {@link Service} instance tassociated with this feed.
   */
  public Service getService() {
    return feedState.service;
  }

  /**
   * Sets that GData {@link Service} instance associated with this feed.
   */
  public void setService(Service v) {
    feedState.service = v;

    // Propagate service information to nested entries
    for (Entry entry : getEntries()) {
      entry.setService(v);
    }
  }

  /**
   * Gets the property that indicates if it is possible to post new entries to
   * the feed.
   */
  public boolean getCanPost() {
    return feedState.canPost;
  }

  /**
   * Sets the property that indicates if it is possible to post new entries to
   * the feed.
   */
  public void setCanPost(boolean v) {
    feedState.canPost = v;
  }

  /**
   * Returns the resource version id for this feed. This will be
   * used to generate an etag value on output. This is never set
   * when the Feed has been parsed.
   */
  public String getVersionId() {
    return feedState.versionId;
  }

  /**
   * Set the resource version id for this feed. This will be
   * used to generate an etag value on output. If {@code null},
   * the updated time will be used instead to generate an etag.
   */
  public void setVersionId(String v) {
    feedState.versionId = v;
  }

  /**
   * Returns the {@link GdAttributes#ETAG} value for this feed. A value of
   * {@code null} indicates the value is unknown.
   */
  public String getEtag() {
    return getAttributeValue(GdAttributes.ETAG);
  }

  /**
   * Sets the {@link GdAttributes#ETAG} value for this feed. A value of
   * {@code null} indicates the value is unknown.
   */
  public void setEtag(String v) {
    setAttributeValue(GdAttributes.ETAG, v);
  }

  /**
   * Returns the {@link GdAttributes#KIND} value for this feed. The kind
   * attribute may be null if this feed does not have a kind.
   */
  public String getKind() {
    return getAttributeValue(GdAttributes.KIND);
  }

  /**
   * Sets the {@link GdAttributes#KIND} value for this feed. The kind may be set
   * to null to remove the attribute value.
   */
  public void setKind(String v) {
    setAttributeValue(GdAttributes.KIND, v);
  }

  /**
   * Returns the {@link GdAttributes#FIELDS} value for this feed. The
   * fields attribute may be null if this feed contains a full representation.
   */
  public String getSelectedFields() {
    return getAttributeValue(GdAttributes.FIELDS);
  }

  /**
   * Sets the{@link GdAttributes#FIELDS} value for this feed. The fields
   * attribute may be set to null to remove the attribute value.
   */
  public void setSelectedFields(String v) {
    setAttributeValue(GdAttributes.FIELDS, v);
  }

  /**
   * Returns the current xml:base attribute for this feed.  The base attribute
   * may be {@code null} if this feed does not have an xml:base.
   */
  public URI getXmlBase() {
    return getAttributeValue(XML_BASE);
  }
  
  /**
   * Sets the current xml:base attribute for this feed.  The base may be set to
   * {@code null} to remove the attribute value.
   */
  public void setXmlBase(URI v) {
    setAttributeValue(XML_BASE, v);
  }
  
  /**
   * Gets the total number of results associated with this feed. The value may
   * be larger than the number of contained entries for paged feeds. A value of
   * {@link Query#UNDEFINED} indicates the total size is undefined.
   */
  public int getTotalResults() {
    Integer v = getElementValue(TOTAL_RESULTS);
    if (v == null) {
      return Query.UNDEFINED;
    }
    return v;
  }

  /**
   * Sets the total number of results associated with this feed. The value may
   * be larger than the number of contained entries for paged feeds. A value of
   * {@link Query#UNDEFINED} indicates the total size is undefined.
   */
  public void setTotalResults(int v) {
    if (v != Query.UNDEFINED) {
      setElement(TOTAL_RESULTS, new Element(TOTAL_RESULTS).setTextValue(v));
    } else {
      removeElement(TOTAL_RESULTS);
    }
  }

  /**
   * Gets the starting index of the contained entries for paged feeds. A value
   * of {@link Query#UNDEFINED} indicates the start index is undefined.
   */
  public int getStartIndex() {
    Integer v = getElementValue(START_INDEX);
    if (v == null) {
      return Query.UNDEFINED;
    }
    return v;
  }

  /**
   * Sets the starting index of the contained entries for paged feeds. A value
   * of {@link Query#UNDEFINED} indicates the start index is undefined.
   */
  public void setStartIndex(int v) {
    if (v != Query.UNDEFINED) {
      setElement(START_INDEX, new Element(START_INDEX).setTextValue(v));
    } else {
      removeElement(START_INDEX);
    }
  }

  /**
   * Gets the number of items that will be returned per page for paged feeds. A
   * value of {@link Query#UNDEFINED} indicates the page item count is
   * undefined.
   */
  public int getItemsPerPage() {
    Integer v = getElementValue(ITEMS_PER_PAGE);
    if (v == null) {
      return Query.UNDEFINED;
    }
    return v;
  }

  /**
   * Sets the number of items that will be returned per page for paged feeds. A
   * value of {@link Query#UNDEFINED} indicates the page item count is
   * undefined.
   */
  public void setItemsPerPage(int v) {
    if (v != Query.UNDEFINED) {
      setElement(ITEMS_PER_PAGE, new Element(ITEMS_PER_PAGE).setTextValue(v));
    } else {
      removeElement(ITEMS_PER_PAGE);
    }
  }

  /** Returns the list of entries in this feed */
  public List<? extends Entry> getEntries() {
    return getElements(Entry.KEY);
  }

  /** Returns a list of entries matching the given entry key. */
  protected <T extends Entry> List<T> getEntries(ElementKey<?, T> key) {
    return getElements(key);
  }

  /** Sets the entries in this feed to the given entries. */
  public void setEntries(Collection<? extends Entry> entries) {
    clearEntries();
    for (Entry entry : entries) {
      addElement(Entry.KEY, entry);
    }
  }

  /** Clears the list of entries on this feed. */
  public void clearEntries() {
    removeElement(Entry.KEY);
  }

  /** Adds an entry to this feed. */
  public void addEntry(Entry entry) {
    addElement(entry);
  }

  /** Removes a single entry from this feed. */
  public boolean removeEntry(Entry entry) {
    return removeElement(Entry.KEY, entry);
  }

  /**
   * Creates a new entry for the feed.
   */
  public Entry createEntry() {
    return createEntry(Entry.KEY);
  }

  /**
   * Creates a new entry for the feed.
   */
  public <E extends Entry> E createEntry(ElementKey<?, E> entryKey) {
    E entry;
    try {
      entry = Element.createElement(entryKey);
    } catch (ContentCreationException cce) {
      throw new IllegalStateException(cce);
    }

    // Propagate the associated service (if any)
    if (feedState.service != null) {
      entry.setService(feedState.service);
    }

    return entry;
  }

  /** Returns the entry post link for the feed. */
  public Link getEntryPostLink() {
    Link postLink = getLink(Link.Rel.ENTRY_POST, Link.Type.ATOM);
    return postLink;
  }

  /** Returns the self link for the feed. */
  public Link getSelfLink() {
    Link postLink = getLink(Link.Rel.SELF, Link.Type.ATOM);
    return postLink;
  }

  /**
   * Returns the link that provides the URI of next page in a paged feed.
   *
   * @return Link that provides the URI of next page in a paged feed or {@code
   *         null} for none.
   */
  public Link getNextLink() {
    return getLink(Link.Rel.NEXT, Link.Type.ATOM);
  }

  /**
   * Returns the link that provides the URI of previous page in a paged feed.
   *
   * @return Link that provides the URI of previous page in a paged feed or
   *         {@code null} for none.
   */
  public Link getPreviousLink() {
    return getLink(Link.Rel.PREVIOUS, Link.Type.ATOM);
  }

  /**
   * Returns the link that provides the URI that can be used to batch operations
   * to query, insert, update and delete entries on this feed.
   *
   * @return Link that provides the URI that can be used to batch operations to
   *         query, insert, update and delete entries on this feed or
   *         {@code null} for none.
   */
  public Link getFeedBatchLink() {
    return getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
  }

  /**
   * Returns the current representation of the feed by requesting it from the
   * associated service using the feed's self link.
   *
   * @return the current state of the feed.
   */
  public Feed getSelf() throws IOException, ServiceException {
    if (feedState.service == null) {
      throw new ServiceException(
          CoreErrorDomain.ERR.feedNotAssociated);
    }
    Link selfLink = getSelfLink();
    if (selfLink == null) {
      throw new UnsupportedOperationException("Feed cannot be retrieved");
    }
    URL feedUrl = selfLink.getHrefUri().toURL();
    try {
      // Use Etag if available to conditionalize the retrieval, otherwise use
      // the updated value.
      String etag = getEtag();
      if (etag != null) {
        return feedState.service.getFeed(feedUrl, this.getClass(), etag);
      } else {
        return feedState.service.getFeed(
            feedUrl, this.getClass(), getUpdated());
      }
    } catch (NotModifiedException e) {
      return this;
    }
  }

  /**
   * Removes all links.
   */
  public void removeLinks() {
    removeElement(Link.KEY);
  }

  /**
   * Inserts a new Entry into the feed, if the feed is currently associated with
   * a Service.
   *
   * @return the inserted Entry returned by the Service.
   *
   * @throws ServiceException If there is no associated GData service or the
   *         service is unable to perform the insertion.
   *
   * @throws UnsupportedOperationException If insert is not supported for the
   *         target feed.
   *
   * @throws IOException If there is an error communicating with the GData
   *         service.
   */
  public <T extends Entry> T insert(T newEntry) throws ServiceException,
      IOException {
    if (feedState.service == null) {
      throw new ServiceException(
          CoreErrorDomain.ERR.entryNotAssociated);
    }
    Link postLink = getEntryPostLink();
    if (postLink == null) {
      throw new UnsupportedOperationException("Media cannot be inserted");
    }
    URL postUrl = postLink.getHrefUri().toURL();
    return feedState.service.insert(postUrl, newEntry);
  }

  /**
   * Narrows this feed using categories with an appropriate kind value.
   * This will loop through the categories, checking if they represent kinds,
   * and adapting the feed to that kind if an appropriate adaptation was
   * found. This will return the most specific subtype of the narrowed type.
   */
  @Override
  protected Element narrow(ElementMetadata<?,?> meta, ValidationContext vc) {
    String kind = Kinds.getElementKind(this);
    if (kind != null) {
      return adapt(this, meta, kind);
    } 
    return super.narrow(meta, vc);
  }

  @Override
  public Element resolve(ElementMetadata<?, ?> metadata, ValidationContext vc) {

    // Fix "setCanPost" based on the existence of an entry post link.
    feedState.canPost = getEntryPostLink() != null;

    // Continue parent resolution.
    return super.resolve(metadata, vc);
  }

  /**
   * Gets a list of entries of a particular kind.  Will only return entries
   * that match the expected return class.
   */
  public <T extends Entry> List<T> getEntries(Class<T> returnClass) {
    List<T> result = new ArrayList<T>();

    for (Entry entry : getEntries()) {
      if (returnClass.isInstance(entry)) {
        result.add(returnClass.cast(entry));
      }
    }

    return result;
  }
}
