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
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.Person;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.acl.AclNamespace;
import com.google.gdata.data.extensions.Deleted;
import com.google.gdata.data.extensions.Labels;
import com.google.gdata.data.extensions.LastModifiedBy;
import com.google.gdata.data.extensions.LastViewed;
import com.google.gdata.data.extensions.QuotaBytesUsed;
import com.google.gdata.data.extensions.ResourceId;
import com.google.gdata.data.media.MediaEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An entry representing a single document of any type within a
 * {@link DocumentListFeed}.
 *
 * 
 * 
 * 
 */
@Kind.Term(DocumentListEntry.KIND)
public class DocumentListEntry extends MediaEntry<DocumentListEntry> {

  /**
   * Represents the MIME types supported by the doclist GData feed
   */
  public enum MediaType {
    JPG("image/jpeg"),
    JPEG("image/jpeg"),
    PNG("image/png"),
    BMP("image/bmp"),
    GIF("image/gif"),
    TXT("text/plain"),
    HTML("text/html"),
    HTM("text/html"),
    ODT("application/vnd.oasis.opendocument.text"),
    SXW("application/vnd.sun.xml.writer"),
    DOC("application/msword"),
    DOCX("application/vnd.openxmlformats-officedocument." +
         "wordprocessingml.document"),
    RTF("application/rtf"),
    PDF("application/pdf"),
    PPS("application/vnd.ms-powerpoint"),
    PPT("application/vnd.ms-powerpoint"),
    XLS("application/vnd.ms-excel"),
    XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
    ODS("application/x-vnd.oasis.opendocument.spreadsheet"),
    CSV("text/csv"),
    TAB("text/tab-separated-value"),
    TSV("text/tab-separated-value"),
    SWF("application/x-shockwave-flash"),
    ZIP("application/zip"),
    WMF("application/x-msmetafile")
    ;

    private String mimeType;

    private MediaType(String mimeType) {
      this.mimeType = mimeType;
    }

    public String getMimeType() {
      return mimeType;
    }

    public static MediaType fromFileName(String fileName) {
      int index = fileName.lastIndexOf('.');
      if (index > 0) {
        return valueOf(fileName.substring(index + 1).toUpperCase());
      } else {
        return valueOf(fileName);
      }
    }
  }

  /**
   * Returns the mime type given a file name.  Not intended for extenal
   * use, see setFile(File, String) instead.  protected access for
   * testing.
   *
   * @throws IllegalArgumentException if the mime type is not known
   * given the file name
   */
  protected static String getMimeTypeFromFileName(String fileName) {
    return MediaType.fromFileName(fileName).getMimeType();
  }

  /**
   * Label for category.
   *
   * @deprecated Use LABEL instead.
   */
  @Deprecated
  public static final String UNKNOWN_LABEL = "unknown";

  /**
   * Kind category term used to label the entries which are
   * of document type.
   *
   * @deprecated Use KIND instead.
   */
  @Deprecated
  public static final String UNKNOWN_KIND = DocsNamespace.DOCS_PREFIX
      + DocumentListEntry.UNKNOWN_LABEL;

  /**
   * Category used to label entries which are of document type.
   *
   * @deprecated Use CATEGORY instead.
   */
  @Deprecated
  public static final Category UNKNOWN_CATEGORY = new Category(
      Namespaces.gKind, UNKNOWN_KIND, UNKNOWN_LABEL);

  /**
   * Label for category.
   */
  public static final String LABEL = "item";

  /**
   * Kind category term used to label the entries which are
   * of item type.
   */
  public static final String KIND = DocsNamespace.DOCS_PREFIX
      + DocumentListEntry.LABEL;

  /**
   * Category used to label entries which are of item type.
   */
  public static final Category CATEGORY = new Category(
      Namespaces.gKind, KIND, LABEL);

  public static final String FOLDERS_NAMESPACE =
      DocsNamespace.DOCS + "/folders";

  public static final String PARENT_NAMESPACE =
      DocsNamespace.DOCS_PREFIX + "parent";

  public static final String REVISIONS_NAMESPACE =
      DocsNamespace.DOCS + "/revisions";

  public static final String THUMBNAIL_NAMESPACE =
    DocsNamespace.DOCS + "/thumbnail";

  /**
   * Constructs a new uninitialized entry, to be populated by the
   * GData parsers.
   */
  public DocumentListEntry() {
    super();
  }

  /**
   * Constructs a new entry by doing a shallow copy from another BaseEntry
   * instance.
   */
  public DocumentListEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    super.declareExtensions(extProfile);
    extProfile.declare(DocumentListEntry.class, Description.class);
    extProfile.declare(DocumentListEntry.class, DocumentListAclFeedLink.class);
    extProfile.declare(DocumentListEntry.class, Filename.class);
    extProfile.declare(DocumentListEntry.class, LastCommented.class);
    extProfile.declare(DocumentListEntry.class, LastModifiedBy.class);
    extProfile.declare(DocumentListEntry.class, LastViewed.class);
    extProfile.declare(DocumentListEntry.class, Md5Checksum.class);
    extProfile.declare(DocumentListEntry.class, QuotaBytesUsed.class);
    extProfile.declare(DocumentListEntry.class, ResourceId.class);
    extProfile.declare(DocumentListEntry.class, SuggestedFilename.class);
    extProfile.declare(DocumentListEntry.class, WritersCanInvite.class);
  }

  /**
   * Gets the link with which you can open up the document in a Web
   * browser.  This is a link to the full document-specific UI (for
   * edit if the requesting user has edit permission, and a read-only
   * HTML rendering otherwise).
   *
   * @return a link to open up the web browser with
   */
  public Link getDocumentLink() {
    return super.getHtmlLink();
  }

  /**
   * Gets the resource id that is used to access the document entry or export
   * the document.
   *
   * <code>http://docs.google.com/getdoc?id={id}</code>
   * <code>http://spreadsheets.google.com/ccc?key={id}</code>
   *
   * @return the Google Docs &amp; Spreadsheets id
   *
   * @deprecated use getResourceId() instead.
   */
  @Deprecated
  public String getKey() {
    String result = state.id;
    if (result != null) {
      int position = result.lastIndexOf("/");

      if (position > 0) {
        result = result.substring(position + 1);
      }
    }

    return result;
  }

  /**
   * Gets the docId or spreadsheet key from the resource id. This is the id that
   * can be used to construct the export url or link to google docs.
   *
   * <code>http://docs.google.com/present/edit?id={id}</code>
   * <code>http://spreadsheets.google.com/ccc?key={id}</code>
   *
   * @return the Google Docs doc id
   * .
   */
  public String getDocId() {
    String result = getResourceId();
    if (result != null) {
      int position = result.lastIndexOf(":");
      if (position > 0) {
        result = result.substring(position + 1);
      }
    }

    return result;
  }

  /**
   * Returns the type document entry from the resource id. If the resource id
   * id "document:12345", then "document" is returned.
   *
   * @return the document type
   * .
   */
  public String getType() {
    String result = getResourceId();
    if (result != null) {
      int position = result.lastIndexOf(":");
      if (position > 0) {
        return result.substring(0, position);
      }
    }
    return result;
  }

  /**
   * Associate a File with this entry, implicitly determining the mime type
   * from the file's extension.
   *
   * @deprecated use setFile(File, String) instead.
   */
  @Deprecated
  public void setFile(File file) {
    setFile(file, getMimeTypeFromFileName(file.getName()));
  }

  /**
   * Associate a File with this entry with the specified mime type
   */
  public void setFile(File file, String mimeType) {
    MediaFileSource fileSource = new MediaFileSource(file, mimeType);
    MediaContent content = new MediaContent();
    content.setMediaSource(fileSource);
    content.setMimeType(new ContentType(mimeType));
    setContent(content);
  }

  /**
   * Sets the hidden status of this document for the user this feed request
   * has been authenticated under.
   *
   * @param hidden true if the document should be hidden
   */
  public void setHidden(boolean hidden) {
    if (hidden) {
      this.getCategories().add(Labels.HIDDEN);
    } else {
      this.getCategories().remove(Labels.HIDDEN);
    }
  }

  /**
   * @return true if the document represented by this entry has been hidden
   * by the user this feed request has been authenticated under.
   */
  public boolean isHidden() {
    return this.getCategories().contains(Labels.HIDDEN);
  }

  /**
   * Sets the starred status of this document for the user this feed request
   * has been authenticated under.
   *
   * @param starred true if the document should be starred
   */
  public void setStarred(boolean starred) {
    if (starred) {
      this.getCategories().add(Labels.STARRED);
    } else {
      this.getCategories().remove(Labels.STARRED);
    }
  }

  /**
   * @return true if the document represented by this entry has been starred
   * by the user this feed request has been authenticated under.
   */
  public boolean isStarred() {
    return this.getCategories().contains(Labels.STARRED);
  }

  /**
   * Sets the viewed status of this document for the user this feed request
   * has been authenticated under.
   *
   * @param viewed true if the document has been viewed
   */
  public void setViewed(boolean viewed) {
    if (viewed) {
      this.getCategories().add(Labels.VIEWED);
    } else {
      this.getCategories().remove(Labels.VIEWED);
    }
  }

  /**
   * @return true if the document represented by this entry has been viewed by
   * the user this feed request has been authenticated under.
   */
  public boolean isViewed() {
    return this.getCategories().contains(Labels.VIEWED);
  }

  /**
   * Sets the trashed status of this document for the user this feed request
   * has been authenticated under.
   *
   * @param trashed true if the document should be trashed
   */
  public void setTrashed(boolean trashed) {
    if (trashed) {
      this.getCategories().add(Labels.TRASHED);
      this.setExtension(new Deleted());
    } else {
      this.getCategories().remove(Labels.TRASHED);
      this.removeExtension(Deleted.class);
    }
  }

  /**
   * @return true if the document represented by this entry has been trashed
   * by the user this feed request has been authenticated under.
   */
  public boolean isTrashed() {
    return this.getCategories().contains(Labels.TRASHED)
        || this.hasExtension(Deleted.class);
  }

  /**
   * Sets the restricted download status for this document.
   *
   * @param restrictedDownload {@code true} if the document has restricted downloading
   */
  public void setRestrictedDownload(boolean restrictedDownload) {
    if (restrictedDownload) {
      this.getCategories().add(Labels.RESTRICTED_DOWNLOAD);
    } else {
      this.getCategories().remove(Labels.RESTRICTED_DOWNLOAD);
    }
  }

  /**
   * @return {@code true} if the document has restricted downloading
   */
  public boolean isRestrictedDownload() {
    return this.getCategories().contains(Labels.RESTRICTED_DOWNLOAD);
  }

  /**
   * Adds a user-specific folder that parents this document
   *
   * @param owner the owner of the folder
   * @param folderName the name of the folder
   * @deprecated use {@link #addLink(Link)} with link relation PARENT_NAMESPACE
   *    instead.
   */
  @Deprecated
  public void addFolder(Person owner, String folderName) {
    String scheme = FOLDERS_NAMESPACE + "/" + owner.getEmail();
    Category folderCategory = new Category(scheme, folderName, folderName);
    this.getCategories().add(folderCategory);
  }

  private static final Pattern FOLDER_PATTERN =
      Pattern.compile("^" + Pattern.quote(FOLDERS_NAMESPACE) + "(:?/[^/]+)?$");

  /**
   * @deprecated use {@link #getParentLinks()} instead.
   */
  @Deprecated
  public Set<String> getFolders() {
    Set<String> folders = new HashSet<String>();
    for (Category category : this.getCategories()) {
      Matcher matcher = FOLDER_PATTERN.matcher(category.getScheme());
      if (matcher.matches()) {
        String folderName = category.getLabel();
        if ((folderName == null) || (folderName.length() == 0)) {
          folderName = category.getTerm();
        }
        folders.add(folderName);
      }
    }
    return folders;
  }

  public DocumentListAclFeedLink getAclFeedLink() {
    List<DocumentListAclFeedLink> links =
        getRepeatingExtension(DocumentListAclFeedLink.class);
    for (DocumentListAclFeedLink feedLink : links) {
      if (AclNamespace.LINK_REL_ACCESS_CONTROL_LIST.equals(feedLink.getRel())) {
        return feedLink;
      }
    }
    return null;
  }

  public AclFeed getAclFeed() {
    DocumentListAclFeedLink feedLink = getAclFeedLink();
    return (feedLink != null) ? feedLink.getFeed() : null;
  }

  public List<Link> getParentLinks() {
    return getLinks(PARENT_NAMESPACE, Link.Type.ATOM);
  }

  /**
   * Returns the description of this document.
   *
   * @return the description ({@code null} means no description was set, whereas the empty-string
   *    means the description is empty)
   */
  public String getDescription() {
    Description description = getExtension(Description.class);
    if (description == null) {
      return null;
    }
    return description.getValue() == null ? "" : description.getValue();
  }

  /**
   * Sets the description of this document.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    if (description == null) {
      removeExtension(Description.class);
    } else {
      setExtension(new Description(description));
    }
  }

  /**
   * Returns the original filename for this document.
   *
   * @return the filename
   */
  public String getFilename() {
    Filename filename = getExtension(Filename.class);
    return filename == null ? null : filename.getValue();
  }

  /**
   * Sets the filename of this document.
   *
   * @param filename the filename
   */
  public void setFilename(String filename) {
    if (filename == null) {
      removeExtension(Filename.class);
    } else {
      setExtension(new Filename(filename));
    }
  }

  /**
   * Returns the suggested filename for this document.
   *
   * @return the filename
   */
  public String getSuggestedFilename() {
    SuggestedFilename filename = getExtension(SuggestedFilename.class);
    return filename == null ? null : filename.getValue();
  }

  /**
   * Sets the suggested filename for this document.
   *
   * @param filename the filename
   */
  public void setSuggestedFilename(String filename) {
    if (filename == null) {
      removeExtension(SuggestedFilename.class);
    } else {
      setExtension(new SuggestedFilename(filename));
    }
  }

  /**
   * Returns the time when the document was last commented by the user.
   *
   * @return the last commented time
   */
  public DateTime getLastCommented() {
    LastCommented lastCommented = getExtension(LastCommented.class);
    return lastCommented == null ? null : lastCommented.getValue();
  }

  /**
   * Sets the time when the document was last commented by the user.
   *
   * @param lastCommented the last commented time
   */
  public void setLastCommented(DateTime lastCommented) {
    if (lastCommented == null) {
      removeExtension(LastCommented.class);
    } else {
      setExtension(new LastCommented(lastCommented));
    }
  }

  /**
   * Returns the time when the document was last viewed by the user.
   *
   * @return the last viewed time
   */
  public DateTime getLastViewed() {
    LastViewed lastViewed = getExtension(LastViewed.class);
    return lastViewed == null ? null : lastViewed.getValue();
  }

  /**
   * Sets the time when the document was last viewed by the user.
   *
   * @param lastViewed the last viewed time
   */
  public void setLastViewed(DateTime lastViewed) {
    if (lastViewed == null) {
      removeExtension(LastViewed.class);
    } else {
      setExtension(new LastViewed(lastViewed));
    }
  }

  /**
   * Returns the MD5 checksum calculated for the document.
   *
   * @return the MD5 checksum
   */
  public String getMd5Checksum() {
    Md5Checksum md5Checksum = getExtension(Md5Checksum.class);
    return md5Checksum == null ? null : md5Checksum.getValue();
  }

  /**
   * Set the MD5 checksum calculated for the document.
   *
   * @param md5Checksum the MD5 checksum
   */
  public void setMd5Checksum(String md5Checksum) {
    if (md5Checksum == null) {
      removeExtension(Md5Checksum.class);
    } else {
      setExtension(new Md5Checksum(md5Checksum));
    }
  }

  /**
   * Returns the amount of quota consumed by the document.
   *
   * @return the quota used
   */
  public Long getQuotaBytesUsed() {
    QuotaBytesUsed quotaBytes = getExtension(QuotaBytesUsed.class);
    return quotaBytes == null ? null : quotaBytes.getValue();
  }

  /**
   * Sets the amount of quota consumed by the document.
   *
   * @param quotaBytesUsed the quota used
   */
  public void setQuotaBytesUsed(Long quotaBytesUsed) {
    if (quotaBytesUsed == null) {
      removeExtension(QuotaBytesUsed.class);
    } else {
      setExtension(new QuotaBytesUsed(quotaBytesUsed));
    }
  }

  /**
   * Returns a flag for whether writers can invite other collaborators
   *
   * @return whether writers can invite
   */
  public Boolean isWritersCanInvite(){
    WritersCanInvite writersCanInvite = getExtension(WritersCanInvite.class);
    return writersCanInvite == null ? null : writersCanInvite.getValue();
  }

  /**
   * Sets whether users classed as writers can invite other collaborators
   *
   * @param writersCanInvite true if writers can invite
   */
  public void setWritersCanInvite(Boolean writersCanInvite) {
    if (writersCanInvite == null) {
      removeExtension(WritersCanInvite.class);
    } else {
      this.setExtension(new WritersCanInvite(writersCanInvite));
    }
  }

  /**
   * Returns the user who last modified the document.
   *
   * @return the user who last modified the document
   */
  public LastModifiedBy getLastModifiedBy() {
    LastModifiedBy lastModifiedBy = getExtension(LastModifiedBy.class);
    return lastModifiedBy == null ? null : lastModifiedBy;
  }

  /**
   * Sets the amount of quota consumed by the document.
   *
   * @param lastModifiedBy the quota used
   */
  public void setLastModifiedBy(LastModifiedBy lastModifiedBy) {
    if (lastModifiedBy == null) {
      removeExtension(LastModifiedBy.class);
    } else {
      setExtension(lastModifiedBy);
    }
  }

  /**
   * Returns the document's resource id.
   *
   * @return the resource id.
   */
  public String getResourceId() {
    ResourceId resourceId = getExtension(ResourceId.class);
    return resourceId == null ? null : resourceId.getValue();
  }

  /**
   * Sets the document's resource id.
   *
   * @param resourceId the resource id.
   */
  public void setResourceId(String resourceId) {
    if (resourceId == null) {
      removeExtension(ResourceId.class);
    } else {
      setExtension(new ResourceId(resourceId));
    }
  }
}
