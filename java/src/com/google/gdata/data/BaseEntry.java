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


package com.google.gdata.data;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.Attribute;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.client.GDataProtocol;
import com.google.gdata.client.Service;
import com.google.gdata.data.batch.BatchInterrupted;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.util.EventSourceParser;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.NotModifiedException;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ParseUtil;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;


/**
 * The BaseEntry class is an abstract base class that defines the
 * in-memory object model for GData entries.
 * <p>
 * It is capable of parsing the Atom XML for an {@code <atom:entry>}
 * element as well as any contained Extension elements.  It can generate
 * both Atom and RSS 2.0 representations of the entry from the object
 * model.
 * <p>
 * The BaseEntry class implements the {@link Kind.Adaptable} interface, meaning
 * it is possible to create new {@link Kind.Adaptor} subtypes that defines
 * a custom extension model (and associated convenience APIs) for a BaseEntry
 * subtypes that use Atom/RSS extensions to extend the content model for a
 * particular type of data.
 * <p>
 * An {@link Kind.Adaptor} subclass of BaseEntry should do the following:
 * <ul>
 * <li>Include a {@link Kind.Term} annotation on the class declaration that
 * defines the {@link Category} term value for the GData kind handled by the
 * adaptor.</li>
 * <li>Provide a constructor that takes a single BaseEntry parameter as an
 * argument that is used when adapting a generic entry type to a more specific
 * one.</li>
 * <li>Implement the {@link Kind.Adaptor#declareExtensions(ExtensionProfile)}
 * method and use it to declare the extension model for the adapted instance
 * within the profile passed as a parameter.   This is used to auto-extend
 * an extension profile when kind Category tags are found during parsing of
 * content.</li>
 * <li>Expose convenience APIs to retrieve and set extension attributes, with
 * an implementions that delegates to {@link ExtensionPoint} methods to
 * store/retrieve the extension data.
 * </ul>
 *
 * Here is the Relax-NG schema that represents an Atom 1.0 entry:
 * <pre>
 * atomEntry =
 *   element atom:entry {
 *     atomCommonAttributes,
 *     (atomAuthor*
 *     & atomCategory*
 *     & atomContent?
 *     & atomContributor*
 *     & atomId
 *     & atomLink*
 *     & atomPublished?
 *     & atomRights?
 *     & atomSource?
 *     & atomSummary?
 *     & atomTitle
 *     & atomUpdated
 *     & extensionElement*)
 * </pre>
 *
 * @param <E> the entry type associated with the bound subtype.
 * @see Kind.Adaptor
 * @see Kind.Adaptable
 *
 * 
 * 
 */
public abstract class BaseEntry<E extends BaseEntry>
    extends ExtensionPoint
    implements Kind.Adaptable, Kind.Adaptor, IEntry {


  /**
   * The EntryState class provides a simple structure that encapsulates
   * the attributes of an Atom entry that should be shared with a shallow
   * copy if the entry is adapted to a more specific BaseEntry
   * {@link Kind.Adaptor} subtypes.
   *
   * @see BaseEntry#BaseEntry(BaseEntry)
   */
  protected static class EntryState {

    /** Entry id. */
    public String id;

    /**
     * Version ID. This is a unique number representing this particular
     * entry. Every update changes the version ID (unless the update
     * doesn't modify anything, in which case it's permissible for
     * version ID to stay the same). Services are free to interpret this
     * string in the most convenient way. Some services may choose to use
     * a monotonically increasing sequence of version IDs. Other services
     * may compute a hash of entry properties and use that.
     * <p>
     * This property is only used for services to communicate the current
     * version ID back to the servlet. It is NOT set when entries are
     * parsed (either from requests or from arbitrary XML).
     */
    public String versionId;

    /**
     * Etag.  See RFC 2616, Section 3.11.
     * If there is no entity tag, this variable is null.
     * Etags are provided not only on top-level entries,
     * but also on entries within feeds (in the form of
     * a gd:etag attribute).
     */
    public String etag;

    /**
     * gd:fields.  This is the field selection associated with this entry.
     * If not {@code null} then this entry represents a partial entry.
     */
    public String fields;

    /**
     * gd:kind.  This is the kind attribute for this entry.  If there is no kind
     * attribute for this entry, this variable is null.
     */
    public String kind;

    /** Creation timestamp. Ignored on updates. */
    public DateTime published;

    /** Last updated timestamp. */
    public DateTime updated;

    /** Last edit timestamp */
    public DateTime edited;

    /** Categories of entry. */
    public HashSet<Category> categories = new HashSet<Category>();

    /** Title of entry. */
    public TextConstruct title;

    /** Summary of entry. */
    public TextConstruct summary;

    /** Rights of entry. */
    public TextConstruct rights;

    /** Content of entry. */
    public Content content;

    /** Links of entry. */
    public LinkedList<Link> links = new LinkedList<Link>();

    /** Authors of entry. */
    public LinkedList<Person> authors = new LinkedList<Person>();

    /** Contributors of entry. */
    public LinkedList<Person> contributors = new LinkedList<Person>();

    /** Source. */
    public Source source;

    /** Service. */
    public Service service;

    /** {code true} if the entry can be modified by a client. */
    public boolean canEdit = true;

    /**
     * Atom publication control status, which contains the draft status.
     */
    public PubControl pubControl;

    /** Adaptable helper. */
    public Kind.Adaptable adaptable = new Kind.AdaptableHelper();
  }

  /**
   * Basic state for this entry.   May be shared across multiple adapted
   * instances associated with the same logical entry.
   */
  protected EntryState state;

  /**
   * Constructs a new BaseEntry instance.
   */
  protected BaseEntry() {
    state = new EntryState();
  }

  // Locally cache the atomPub namespace value, which is dynamic based upon
  // the in-use version but constant for any instance.
  private XmlNamespace atomPubNs;
  private XmlNamespace getAtomPubNs() {
    if (atomPubNs == null) {
      atomPubNs = Namespaces.getAtomPubNs();
    }
    return atomPubNs;
  }

  /**
   * Copy constructor that initializes a new BaseEntry instance to have
   * identical contents to another instance, using a shared reference to
   * the same {@link EntryState}.  {@link Kind.Adaptor} subclasses
   * of {@code BaseEntry} can use this constructor to create adaptor
   * instances of an entry that share state with the original.
   */
  protected BaseEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
    state = sourceEntry.state;
  }

  public String getId() { return state.id; }
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
    state.id = v;
  }

  public String getVersionId() { return state.versionId; }
  public void setVersionId(String v) { state.versionId = v; }

  public String getEtag() { return state.etag; }
  public void setEtag(String v) { state.etag = v; }

  /**
   * Returns the current fields selection for this partial entry.  A
   * value of {@code null} indicates the entry is not a partial entry.
   */
  public String getSelectedFields() { return state.fields; }

  /**
   * Sets the current fields selection for this partial entry.  A
   * value of {@code null} indicates the entry is not a partial entry.
   */
  public void setSelectedFields(String v) { state.fields = v; }

  public String getKind() { return state.kind; }
  public void setKind(String v) { state.kind = v; }

  public DateTime getPublished() { return state.published; }
  public void setPublished(DateTime v) {
    if (v != null && v.getTzShift() == null) {
      throw new IllegalArgumentException(
          "Entry.published must have a timezone.");
    }
    state.published = v;
  }

  public DateTime getUpdated() { return state.updated; }
  public void setUpdated(DateTime v) {
    if (v != null && v.getTzShift() == null) {
      throw new IllegalArgumentException("Entry.updated must have a timezone.");
    }
    state.updated = v;
  }


  public DateTime getEdited() { return state.edited; }
  public void setEdited(DateTime v) {
    if (v != null && v.getTzShift() == null) {
      throw new IllegalArgumentException("Entry.edited must have a timezone.");
    }
    state.edited = v;
  }

  public Set<Category> getCategories() { return state.categories; }

  public TextConstruct getTitle() { return state.title; }
  public void setTitle(TextConstruct v) { state.title = v; }

  public TextConstruct getSummary() { return state.summary; }
  public void setSummary(TextConstruct v) { state.summary = v; }

  public TextConstruct getRights() { return state.rights; }
  public void setRights(TextConstruct v) { state.rights = v; }

  public Content getContent() { return state.content; }
  public void setContent(Content v) { state.content = v; }

  /**
   * Assumes the content element's contents are text and
   * returns them as a TextContent.
   *
   * @return A TextContent containing the value of the content tag.
   *
   * @throws IllegalStateException
   *            If the content element is not a text type.
   */
  public TextContent getTextContent() {
    Content content = getContent();
    if (!(content instanceof TextContent)) {
      throw new IllegalStateException("Content object is not a TextContent");
    }
    return (TextContent) getContent();
  }

  /**
   * Assumes the <content> element's contents are plain-text and
   * returns its value as a string
   *
   * @return A string containing the plain-text value of the content tag.
   *
   * @throws IllegalStateException
   *            If the content element is not a text type.
   */
  public String getPlainTextContent() {
    TextConstruct textConstruct = getTextContent().getContent();
    if (!(textConstruct instanceof PlainTextConstruct)) {
      throw new IllegalStateException(
          "TextConstruct object is not a PlainTextConstruct");
    }
    return textConstruct.getPlainText();
  }

  public void setContent(TextConstruct tc) {
    state.content = new TextContent(tc);
  }

  public List<Link> getLinks() { return state.links; }

  public void addLink(Link link) {
    state.links.add(link);
  }

  public Link addLink(String rel, String type, String href) {
    Link link = new Link(rel, type, href);
    addLink(link);
    return link;
  }

  public List<Person> getAuthors() { return state.authors; }

  public List<Person> getContributors() { return state.contributors; }

  public Source getSource() { return state.source; }
  public void setSource(Source v) { state.source = v; }

  /**
   * Set draft status. Passing a null value means unsetting the draft
   * status.
   *
   * @param v   Draft status, or null to unset.
   */
  public void setDraft(Boolean v) {
    if (state.pubControl == null) {
      if (!Boolean.TRUE.equals(v)) {
        // No need to create a PubControl entry for that
        return;
      }
      state.pubControl = new PubControl();
    }
    state.pubControl.setDraft(v);
  }

  /**
   * Draft status.
   *
   * @return True if draft status is set and equals true.
   */
  public boolean isDraft() {
    return state.pubControl != null ? state.pubControl.isDraft() : false;
  }

  /**
   * Gets the app:control tag.
   *
   * @return pub control tag or null if unset
   */
  public PubControl getPubControl() { return state.pubControl; }

  /**
   * Sets the app:control tag, which usually contains app:draft.
   *
   * @param value PubControl the new object or null
   */
  public void setPubControl(PubControl value) { state.pubControl = value; }

  public void setService(Service s) { state.service = s; }
  public Service getService() { return state.service; }

  public boolean getCanEdit() { return state.canEdit; }
  public void setCanEdit(boolean v) { state.canEdit = v; }

  public void addAdaptor(Kind.Adaptor adaptor) {
    state.adaptable.addAdaptor(adaptor);
  }

  public Collection<Kind.Adaptor> getAdaptors() {
    return state.adaptable.getAdaptors();
  }

  public <A extends Kind.Adaptor> A getAdaptor(Class<A> adaptorClass) {
    return state.adaptable.getAdaptor(adaptorClass);
  }

  /**
   * Retrieves the first link with the supplied {@code rel} and/or
   * {@code type} value.
   * <p>
   * If either parameter is {@code null}, doesn't return matches
   * for that parameter.
   */
  public Link getLink(String rel, String type) {

    for (Link link : state.links) {
      if (link.matches(rel, type)) {
        return link;
      }
    }

    return null;
  }


  /**
   * Return the links that match the given {@code rel} and {@code type} values.
   *
   * @param relToMatch  {@code rel} value to match or {@code null} to match any
   *                    {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *                    {@code type} value.
   * @return matching links.
   */
  public List<Link> getLinks(String relToMatch, String typeToMatch) {
    List<Link> result = new ArrayList<Link>();
    for (Link link : state.links) {
      if (link.matches(relToMatch, typeToMatch)) {
        result.add(link);
      }
    }
    return result;
  }


  /**
   * Remove all links that match the given {@code rel} and {@code type} values.
   *
   * @param relToMatch  {@code rel} value to match or {@code null} to match any
   *                    {@code rel} value.
   * @param typeToMatch {@code type} value to match or {@code null} to match any
   *                    {@code type} value.
   */
  public void removeLinks(String relToMatch, String typeToMatch) {
    for (Iterator<Link> iterator = state.links.iterator();
        iterator.hasNext();) {
      Link link = iterator.next();
      if (link.matches(relToMatch, typeToMatch)) {
        iterator.remove();
      }
    }
  }

  /**
   * Remove all links.
   */
  public void removeLinks() {
    state.links.clear();
  }


  /**
   * Adds a link pointing to an HTML representation.
   *
   * @param   htmlUri
   *            Link URI.
   *
   * @param   lang
   *            Optional language code.
   *
   * @param   title
   *            Optional title.
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

    state.links.add(link);
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
  @SuppressWarnings("deprecation")
  public Link getMediaEditLink() {
    Link mediaLink = getLink(Link.Rel.MEDIA_EDIT, null);
    if (mediaLink == null) {
      // Temporary back compat support for old incorrect media link value.
      // to the new value.
      mediaLink = getLink(Link.Rel.MEDIA_EDIT_BACKCOMPAT, null);
    }
    return mediaLink;
  }

  /** Retrieves the media resource resumable upload link. */
  public Link getResumableEditMediaLink() {
    return getLink(Link.Rel.RESUMABLE_EDIT_MEDIA, null);
  }

  /** Retrieves the first HTML link. */
  public Link getHtmlLink() {
    Link htmlLink = getLink(Link.Rel.ALTERNATE, Link.Type.HTML);
    return htmlLink;
  }


  /**
   * Retrieves the current version of this Entry by requesting it from
   * the associated GData service.
   *
   * @return the current version of the entry.
   */
  public E getSelf() throws IOException, ServiceException {
    if (state.service == null) {
      throw new ServiceException(
          CoreErrorDomain.ERR.entryNotAssociated);
    }
    Link selfLink = getSelfLink();
    if (selfLink == null) {
      throw new UnsupportedOperationException("Entry cannot be retrieved");
    }
    URL entryUrl = new URL(selfLink.getHref());
    try {
      // If an etag is available, use it to conditionalize the retrieval,
      // otherwise, use time of last edit or update.
      if (state.etag != null) {
        return (E) state.service.getEntry(entryUrl, this.getClass(),
            state.etag);
      } else {
        return (E) state.service.getEntry(entryUrl, this.getClass(),
            (state.edited != null ? state.edited : state.updated));
      }
    } catch (NotModifiedException e) {
      return (E) this;
    }
  }


  /**
   * Updates this entry by sending the current representation to the
   * associated GData service.
   *
   * @return the updated entry returned by the Service.
   *
   * @throws ServiceException
   *           If there is no associated GData service or the service is
   *           unable to perform the update.
   *
   * @throws UnsupportedOperationException
   *           If update is not supported for the target entry.
   *
   * @throws IOException
   *           If there is an error communicating with the GData service.
   */
  public E update() throws IOException, ServiceException {

    if (state.service == null) {
      throw new ServiceException(
          CoreErrorDomain.ERR.entryNotAssociated);
    }
    Link editLink = getEditLink();
    if (editLink == null) {
      throw new UnsupportedOperationException("Entry cannot be updated");
    }

    URL editUrl = new URL(editLink.getHref());
    return (E) state.service.update(editUrl, this);
  }

  /**
   * Deletes this entry by sending a request to the associated GData
   * service.
   *
   * @throws ServiceException
   *           If there is no associated GData service or the service is
   *           unable to perform the deletion.
   *
   * @throws UnsupportedOperationException
   *           If deletion is not supported for the target entry.
   *
   * @throws IOException
   *           If there is an error communicating with the GData service.
   */
  public void delete() throws IOException, ServiceException {

    if (state.service == null) {
      throw new ServiceException(
          CoreErrorDomain.ERR.entryNotAssociated);
    }
    Link editLink = getEditLink();
    if (editLink == null) {
      throw new UnsupportedOperationException("Entry cannot be deleted");
    }

    // Delete the entry, using strong etag (if available) as a precondition.
    URL editUrl = new URL(editLink.getHref());
    state.service.delete(editUrl,
        GDataProtocol.isWeakEtag(state.etag) ? null : state.etag);
  }

  /**
   * The OutOfLineReference class adapts an {@link OutOfLineContent} instance
   * to implement the {@link Reference} interface so nested content references
   * will be post-processed.
   */
  private class OutOfLineReference implements Reference, Extension {

    // This ugliness is necessary because there's no unifying base abstraction
    // for all data elements and the current visitor model only acts upon
    // Extension types (which Content is not). This is fixed in the new data
    // model, at which time we can just have OolContent be visited and wrap it
    // in something that implement the Reference interface inside of
    // ReferenceVisitor.visit().
    private OutOfLineContent oolContent;

    private OutOfLineReference(OutOfLineContent oolContent) {
      this.oolContent = oolContent;
    }

    public String getHref() {
      return oolContent.getUri();
    }

    public void setHref(String href) {
      oolContent.setUri(href);
    }

    public void generate(XmlWriter w, ExtensionProfile extProfile) {
      throw new IllegalStateException("Should not be generated");
    }

    public ElementHandler getHandler(ExtensionProfile extProfile,
        String namespace, String localName, Attributes attrs) {
      throw new IllegalStateException("Should not be parsed");
    }
  }

  @Override
  protected void visitChildren(ExtensionVisitor ev)
      throws ExtensionVisitor.StoppedException {

    // Add out of line content to the visitor pattern by wrapping in an
    // adaptor.  This is necessary so the src reference can be processed.
    if (state.content instanceof OutOfLineContent) {
      this.visitChild(ev,
          new OutOfLineReference((OutOfLineContent) state.content));
    }

    // Add nested links to the visitor pattern
    for (Link link : getLinks()) {
      this.visitChild(ev, link);
    }
    super.visitChildren(ev);
  }

  @Override
  public void generate(XmlWriter w, ExtensionProfile p) throws IOException {
    generateAtom(w, p);
  }

  /**
   * Generates XML in the Atom format.
   *
   * @param   w
   *            Output writer.
   *
   * @param   extProfile
   *            Extension profile.
   *
   * @throws  IOException
   */
  public void generateAtom(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {

    Set<XmlNamespace> nsDecls =
      new LinkedHashSet<XmlNamespace>(namespaceDeclsAtom);
    nsDecls.addAll(extProfile.getNamespaceDecls());

    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>(3);

    if (state.kind != null
        && Service.getVersion().isAfter(Service.Versions.V1)) {
      nsDecls.add(Namespaces.gNs);
      attrs.add(new XmlWriter.Attribute(Namespaces.gAlias, "kind", state.kind));
    }

    if (state.etag != null &&
        !Service.getVersion().isCompatible(Service.Versions.V1)) {
      nsDecls.add(Namespaces.gNs);
      attrs.add(new XmlWriter.Attribute(Namespaces.gAlias, "etag", state.etag));
    }

    if (state.fields != null &&
        Service.getVersion().isAfter(Service.Versions.V1)) {
      nsDecls.add(Namespaces.gNs);
      attrs.add(new XmlWriter.Attribute(
          Namespaces.gAlias, "fields", state.fields));
    }

    // Add any attribute extensions.
    AttributeGenerator generator = new AttributeGenerator();
    putAttributes(generator);
    generateAttributes(attrs, generator);

    generateStartElement(w, Namespaces.atomNs, "entry", attrs, nsDecls);

    if (state.id != null) {
      w.simpleElement(Namespaces.atomNs, "id", null, state.id);
    }

    if (state.published != null) {
      w.simpleElement(Namespaces.atomNs, "published", null,
                      state.published.toString());
    }

    if (state.updated != null) {
      w.simpleElement(Namespaces.atomNs, "updated", null,
          state.updated.toString());
    }

    if (state.edited != null) {
      w.simpleElement(getAtomPubNs(), "edited", null,
          state.edited.toString());
    }

    if (state.pubControl != null) {
      state.pubControl.generateAtom(w, extProfile);
    }

    w.startRepeatingElement();
    for (Category cat : state.categories) {
      cat.generateAtom(w);
    }
    w.endRepeatingElement();

    if (state.title != null) {
      state.title.generateAtom(w, "title");
    }

    if (state.summary != null) {
      state.summary.generateAtom(w, "summary");
    }

    if (state.rights != null) {
      state.rights.generateAtom(w, "rights");
    }

    if (state.content != null) {
      state.content.generateAtom(w, extProfile);
    }

    w.startRepeatingElement();
    for (Link link : state.links) {
      link.generateAtom(w, extProfile);
    }
    w.endRepeatingElement();

    w.startRepeatingElement();
    for (Person author : state.authors) {
      author.generateAtom(extProfile, w, "author");
    }
    w.endRepeatingElement();

    w.startRepeatingElement();
    for (Person contributor : state.contributors) {
      contributor.generateAtom(extProfile, w, "contributor");
    }
    w.endRepeatingElement();

    if (state.source != null) {
      state.source.generateAtom(w, extProfile);
    }

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.atomNs, "entry");
  }


  /**
   * Generates XML in the RSS format.
   *
   * @param   w
   *            Output writer.
   *
   * @param   extProfile
   *            Extension profile.
   *
   * @throws  IOException
   */
  public void generateRss(XmlWriter w,
                          ExtensionProfile extProfile) throws IOException {

    Vector<XmlNamespace> nsDecls = new Vector<XmlNamespace>(namespaceDeclsRss);
    nsDecls.addAll(extProfile.getNamespaceDecls());

    generateStartElement(w, Namespaces.rssNs, "item", null, nsDecls);

    if (state.id != null) {
      List<Attribute> attrs = new ArrayList<Attribute>(1);
      attrs.add(new Attribute("isPermaLink", "false"));
      w.simpleElement(Namespaces.rssNs, "guid", attrs, state.id);
    }

    String lang = null;

    if (state.content != null) {
      lang = state.content.getLang();
    }

    if (lang == null && state.summary != null) {
      lang = state.summary.getLang();
    }

    if (lang == null && state.title != null) {
      lang = state.title.getLang();
    }

    if (lang != null) {
      w.simpleElement(Namespaces.rssNs, "language", null, lang);
    }

    if (state.published != null) {
      w.simpleElement(Namespaces.rssNs, "pubDate", null,
                      state.published.toStringRfc822());
    }

    if (state.updated != null) {
      w.simpleElement(Namespaces.atomNs, "updated", null,
          state.updated.toString());
    }

    w.startRepeatingElement();
    for (Category cat : state.categories) {
      cat.generateRss(w);
    }
    w.endRepeatingElement();

    if (state.title != null) {
      state.title.generateRss(w, "title", TextConstruct.RssFormat.PLAIN_TEXT);
    }

    if (state.summary != null) {
      state.summary.generateAtom(w, "summary");
    }

    if (state.content != null) {
      state.content.generateRss(w, extProfile);
    }

    w.startRepeatingElement();
    for (Link link : state.links) {
      link.generateRss(w);
    }
    w.endRepeatingElement();

    w.startRepeatingElement();
    for (Person author : state.authors) {
      author.generateRss(w, "author");
    }
    w.endRepeatingElement();

    w.startRepeatingElement();
    for (Person contributor : state.contributors) {
      contributor.generateRss(w, "author");
    }
    w.endRepeatingElement();

    // Invoke ExtensionPoint.
    generateExtensions(w, extProfile);

    w.endElement(Namespaces.rssNs, "item");
  }


  /** Top-level namespace declarations for generated XML. */
  private static final Collection<XmlNamespace> namespaceDeclsAtom =
    new Vector<XmlNamespace>(1);

  private static final Collection<XmlNamespace> namespaceDeclsRss =
    new Vector<XmlNamespace>(1);

  static {
    namespaceDeclsAtom.add(Namespaces.atomNs);
    namespaceDeclsRss.add(Namespaces.atomNs);
  }

  /**
   * Reads an entry representation from the provided {@link ParseSource}.
   * The return type of the entry will be determined using dynamic adaptation
   * based upon any {@link Kind} category tag found in the input content. If
   * no kind tag is found an {@link Entry} instance will be returned.
   */
  public static BaseEntry<?> readEntry(ParseSource source)
      throws IOException, ParseException, ServiceException {
    return readEntry(source, null, null);
  }

  /**
   * Reads an entry of type {@code T} using the given extension profile.
   */
  public static <T extends BaseEntry> T readEntry(ParseSource source,
      Class <T> entryClass, ExtensionProfile extProfile)
      throws IOException, ParseException, ServiceException {
    return ParseUtil.readEntry(source, entryClass, extProfile, null);
  }

  /**
   * Parses XML in the Atom format.
   *
   * @param   extProfile
   *            Extension profile.
   *
   * @param   input
   *            XML input stream.
   */
  public void parseAtom(ExtensionProfile extProfile,
                        InputStream input) throws IOException,
                                              ParseException {

    AtomHandler handler = new AtomHandler(extProfile);
    new XmlParser().parse(input, handler, Namespaces.atom, "entry");
  }


  /**
   * Parses XML in the Atom format.
   *
   * @param   extProfile
   *            Extension profile.
   *
   * @param   reader
   *            XML Reader.  The caller is responsible for ensuring
   *            that the character encoding is correct.
   */
  public void parseAtom(ExtensionProfile extProfile,
                        Reader reader) throws IOException,
                                              ParseException {

    AtomHandler handler = new AtomHandler(extProfile);
    new XmlParser().parse(reader, handler, Namespaces.atom, "entry");
  }


  /**
   * Parses XML in the Atom format from a parser-defined content source.
   *
   * @param   extProfile
   *            Extension profile.
   * @param   eventSource
   *            XML event source.
   */
  public void parseAtom(ExtensionProfile extProfile,
      XmlEventSource eventSource) throws IOException, ParseException {

    AtomHandler handler = new AtomHandler(extProfile);
    new EventSourceParser(handler, Namespaces.atom, "entry")
        .parse(eventSource);
  }

  /** Returns information about the content element processing. */
  protected Content.ChildHandlerInfo getContentHandlerInfo(
      ExtensionProfile extProfile, Attributes attrs)
      throws ParseException, IOException {
    return Content.getChildHandler(extProfile, attrs);
  }

  @Override
  public ElementHandler getHandler(ExtensionProfile p, String namespace,
      String localName, Attributes attrs) {
    return new AtomHandler(p);
  }

  /** {@code <atom:entry>} parser. */
  public class AtomHandler extends ExtensionPoint.ExtensionHandler {

    public AtomHandler(ExtensionProfile extProfile) {
      super(extProfile, BaseEntry.this.getClass());
    }

    @Override
    public void processAttribute(String namespace, String localName,
        String value) throws ParseException {
      if (namespace.equals(Namespaces.g)) {
        if (localName.equals("etag")) {
          setEtag(value);
          return;
        }
        if (localName.equals("fields")) {
          setSelectedFields(value);
          return;
        }
        if (localName.equals("kind")) {
          setKind(value);
          return;
        }
      }
      super.processAttribute(namespace, localName, value);
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {


      if (namespace.equals(Namespaces.atom)) {

        if (localName.equals("id")) {

          return new IdHandler();

        } else if (localName.equals("published")) {

          return new PublishedHandler();

        } else if (localName.equals("updated")) {

          return new UpdatedHandler();

        } else if (localName.equals("title")) {

          TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);

          if (state.title != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateTitle);
          }

          state.title = chi.textConstruct;
          return chi.handler;

        } else if (localName.equals("summary")) {

          TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);

          if (state.summary != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateSummary);
          }

          state.summary = chi.textConstruct;
          return chi.handler;

        } else if (localName.equals("rights")) {

          TextConstruct.ChildHandlerInfo chi =
            TextConstruct.getChildHandler(attrs);

          if (state.rights != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateRights);
          }

          state.rights = chi.textConstruct;
          return chi.handler;

        } else if (localName.equals("content")) {

          if (state.content != null) {
            throw new ParseException(
                CoreErrorDomain.ERR.duplicateContent);
          }

          Content.ChildHandlerInfo chi =
              getContentHandlerInfo(extProfile, attrs);

          state.content = chi.content;
          return chi.handler;

        } else if (localName.equals("category")) {

          Category cat = new Category();
          return cat.new AtomHandler(extProfile, state.categories,
              BaseEntry.this);

        } else if (localName.equals("link")) {

          Link link = new Link();
          state.links.add(link);
          return link.new AtomHandler(extProfile);

        } else if (localName.equals("author")) {
          // check for an author extension
          Person author;
          ExtensionDescription extDescription = getExtensionDescription(
              extProfile, extendedClass, namespace, localName);
          if (extDescription != null
              && extDescription.getExtensionClass() != null) {
            author = (Person) createExtensionInstance(
                extDescription.getExtensionClass());
          } else {
            author = new Person();
          }
          state.authors.add(author);
          return author.getHandler(extProfile, namespace, localName, attrs);

        } else if (localName.equals("contributor")) {

          Person contributor = new Person();
          state.contributors.add(contributor);
          return contributor.new AtomHandler(extProfile);

        } else if (localName.equals("source")) {

          state.source = new Source();
          return state.source.new SourceHandler(extProfile);

        }

      } else if (namespace.equals(getAtomPubNs().getUri())) {

        if (localName.equals("control")) {

          state.pubControl = new PubControl();
          return state.pubControl.new AtomHandler(extProfile);

        } else if (localName.equals("edited")) {

          return new EditedHandler();

        }

      } else {

        return super.getChildHandler(namespace, localName, attrs);

      }

      return null;
    }

    @Override
    public void processEndElement() throws ParseException {

      // Skip extension point validation for batch response entries
      if (getExtension(BatchStatus.class) == null &&
          getExtension(BatchInterrupted.class) == null) {
        super.processEndElement();
      }
    }


    /** {@code <atom:id>} parser. */
    class IdHandler extends XmlParser.ElementHandler {

      @Override
      public void processEndElement() throws ParseException {

        if (state.id != null) {
          throw new ParseException(
              CoreErrorDomain.ERR.duplicateEntryId);
        }

        if (value == null) {
          throw new ParseException(
              CoreErrorDomain.ERR.idValueRequired);
        }

        state.id = value;
      }
    }


    /** {@code <atom:published>} parser. */
    class PublishedHandler extends Rfc3339Handler {

      @Override
      public void processEndElement() throws ParseException {
        super.processEndElement();
        state.published = getDateTime();
      }
    }


    /** {@code <atom:updated>} parser. */
    class UpdatedHandler extends Rfc3339Handler {

      @Override
      public void processEndElement() throws ParseException {
        super.processEndElement();
        state.updated = getDateTime();
      }
    }

    /** {@code <app:edited>} parser. */
    class EditedHandler extends Rfc3339Handler {

      @Override
      public void processEndElement() throws ParseException {
        super.processEndElement();
        state.edited = getDateTime();
      }
    }
  }

  /**
   * Locates and returns the most specific {@link Kind.Adaptor} BaseEntry
   * subtype for this entry.  If none can be found for the current class,
   * {@code null} will be returned.
   *
   * @throws Kind.AdaptorException for subclasses to throw.
   */
  public BaseEntry<?> getAdaptedEntry() throws Kind.AdaptorException {

    BaseEntry<?> adaptedEntry = null;

    // Find the BaseEntry adaptor instance that is most specific.
    for (Kind.Adaptor adaptor : getAdaptors()) {
      if (!(adaptor instanceof BaseEntry)) {
        continue;
      }
      // If first matching adaptor or a narrower subtype of the current one,
      // then use it.
      if (adaptedEntry == null ||
          adaptedEntry.getClass().isAssignableFrom(adaptor.getClass())) {
        adaptedEntry = (BaseEntry<?>) adaptor;
      }
    }

    return adaptedEntry;
  }
}
