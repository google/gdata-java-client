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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.media.MediaEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ContentType;

import java.io.File;
import java.io.IOException;

/**
 * An entry representing a single document of any type within a
 * {@link DocumentListFeed}.
 *
 * 
 * 
 */
@Kind.Term(DocumentListEntry.UNKNOWN_KIND)
public class DocumentListEntry extends MediaEntry<DocumentListEntry> {

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
        return valueOf(fileName.substring(index+1).toUpperCase());
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
  public static final Category UNKNOWN_CATEGORY =
    new Category(com.google.gdata.util.Namespaces.gKind, UNKNOWN_KIND, 
        UNKNOWN_LABEL);
  
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
}
