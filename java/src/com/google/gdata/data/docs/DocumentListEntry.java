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


package com.google.gdata.data.docs;

import com.google.gdata.util.common.base.StringUtil;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.Person;
import com.google.gdata.data.extensions.Labels;
import com.google.gdata.data.media.MediaEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.Namespaces;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An entry representing a single document of any type within a
 * {@link DocumentListFeed}.
 *
 * 
 * 
 */
@Kind.Term(DocumentListEntry.UNKNOWN_KIND)
public class DocumentListEntry extends MediaEntry<DocumentListEntry> {

  /**
   * Represents the MIME types supported by the doclist GData feed
   */
  public enum MediaType {
    CSV("text/comma-separated-values"),
    DOC("application/msword"),
    ODS("application/vnd.oasis.opendocument.spreadsheet"),
    ODT("application/vnd.oasis.opendocument.text"),
    PPS("application/vnd.ms-powerpoint"),
    PPT("application/vnd.ms-powerpoint"),
    RTF("application/rtf"),
    SXW("application/vnd.sun.xml.writer"),
    TXT("text/plain"),
    XLS("application/vnd.ms-excel");

    private String mimeType;

    private MediaType(String mimeType) {
      this.mimeType = mimeType;
    }

    public String getMimeType() {
      return mimeType;
    }

    public static MediaType fromFileName(String fileName) {
      int index = fileName.indexOf('.');
      if (index > 0) {
        return valueOf(fileName.substring(index + 1).toUpperCase());
      } else {
        return valueOf(fileName);
      }
    }
  }

  public static final String DOCUMENT_NAMESPACE
      = "http://schemas.google.com/docs/2007";

  /**
   * Label for category.
   */
  public static final String UNKNOWN_LABEL = "unknown";
  
  /**
   * Kind category term used to label the entries which are
   * of document type.
   */
  public static final String UNKNOWN_KIND = DocumentListFeed.DOCUMENT_NAMESPACE 
      + "#" + UNKNOWN_LABEL;
  
  /**
   * Category used to label entries which are of document type.
   */
  public static final Category UNKNOWN_CATEGORY = new Category(
      Namespaces.gKind, UNKNOWN_KIND, UNKNOWN_LABEL);

  public static final String FOLDERS_NAMESPACE =
      DOCUMENT_NAMESPACE + "/folders";

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
  public DocumentListEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
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
   * Gets the non-user-friendly key that is used to access the
   * document feed.  This is the key that can be used to construct the
   * Atom id for this document, and to access the document-specific
   * feed.
   *
   * <code>http://docs.google.com/getdoc?id={id}</code>
   * <code>http://spreadsheets.google.com/ccc?key={id}</code> 
   * 
   * @return the Google Docs &amp; Spreadsheets id
   */
  public String getId() {
    String result = state.id;
    if (result != null) {
      int position = result.lastIndexOf("/");
    
      if (position > 0) {
        result = result.substring(position + 1);
      }
    }
    
    return result;
  }

  public void setFile(File file) throws IOException {
    MediaType mediaType = MediaType.fromFileName(file.getName());
    MediaFileSource fileSource = new MediaFileSource(file,
        mediaType.getMimeType());
    MediaContent content = new MediaContent();
    content.setMediaSource(fileSource);
    content.setMimeType(new ContentType(mediaType.getMimeType()));
    setContent(content);
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
   * Sets the trashed status of this document for the user this feed request
   * has been authenticated under.
   *
   * @param trashed true if the document should be trashed
   */
  public void setTrashed(boolean trashed) {
    if (trashed) {
      this.getCategories().add(Labels.TRASHED);
    } else {
      this.getCategories().remove(Labels.TRASHED);
    }
  }

  /**
   * @return true if the document represented by this entry has been trashed
   * by the user this feed request has been authenticated under.
   */
  public boolean isTrashed() {
    return this.getCategories().contains(Labels.TRASHED);
  }

  /**
   * Adds a user-specific folder that parents this document
   *
   * @param owner the owner of the folder
   * @param folderName the name of the folder
   */
  public void addFolder(Person owner, String folderName) {
    String scheme = FOLDERS_NAMESPACE + "/" + owner.getEmail();
    Category folderCategory = new Category(scheme, folderName, folderName);
    this.getCategories().add(folderCategory);
  }

  private static final Pattern FOLDER_PATTERN =
      Pattern.compile("^" + Pattern.quote(FOLDERS_NAMESPACE) + "(:?/[^/]+)?$");

  public Set<String> getFolders() {
    Set<String> folders = new HashSet<String>();
    for (Category category : this.getCategories()) {
      Matcher matcher = FOLDER_PATTERN.matcher(category.getScheme());
      if (matcher.matches()) {
        String folderName = category.getLabel();
        if (StringUtil.isEmpty(folderName)) {
          folderName = category.getTerm();
        }
        folders.add(folderName);
      }
    }
    return folders;
  }
}
