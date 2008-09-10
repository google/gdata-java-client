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


package com.google.gdata.data.media;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.IFeed;
import com.google.gdata.data.Kind;
import com.google.gdata.data.ParseSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.InvalidEntryException;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ParseUtil;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.io.base.UnicodeReader;

import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PushbackInputStream;
import java.util.regex.Pattern;

import javax.activation.DataContentHandler;
import javax.activation.DataSource;

/**
 * The GDataContentHandler class implements the {@link DataContentHandler}
 * interface of the <a href="http://java.sun.com/products/javabeans/jaf/ ">
 * JavaBeans Activation Framework</a> to enable the parsing and generation
 * of Atom feed and entry XML from MIME media data.   This data content
 * handler is capable of generating MIME media output in Atom, RSS, and
 * JSON formats, as well as parsing content in Atom format.
 * <p>
 * The implementation includes support for customized types driven by
 * <a href="http://code.google.com/apis/gdata/common-elements.html">
 * GData Kinds</a>, where the type of object returned might be triggered
 * by the GData kind category tags included within the content.
 * <p>
 * The current implentation does not include DataFlavor transfer support,
 * only mapping from raw MIME data content to object model (and vice versa).
 *
 * 
 */
public class GDataContentHandler implements DataContentHandler {

  /**
   * The DataContext class represents the (optional) contextual information
   * that can be configured on a per-thread basis when parsing GData content
   * using Java activation.
   *
   * A value of {@code null} for entryClass or feedClass indicates that
   * dynamic adaptation based upon kind category tags should be used to
   * determine the resulting return type.
   *
   * A value of {@code null} for extProfile indicates that a new
   * profile should be created for parsing and either initialized by
   * the specified type (feed or entry class) for static typing or
   * via auto-extension for dynamic typing.
   */
  public static class DataContext {

    private ExtensionProfile extProfile;
    private Class <? extends IEntry> entryClass;
    private Class <? extends IFeed> feedClass;

    public DataContext(ExtensionProfile extProfile,
                       Class <? extends IEntry> entryClass,
                       Class <? extends IFeed> feedClass) {
      this.extProfile = extProfile;
      this.entryClass = entryClass;
      this.feedClass = feedClass;
    }

    public ExtensionProfile getExtensionProfile() { return extProfile; }
    public Class <? extends IEntry> getEntryClass() { return entryClass; }
    public Class <? extends IFeed> getFeedClass() { return feedClass; }
  }

  private static final ThreadLocal<DataContext> threadDataContext =
      new ThreadLocal<DataContext>();

  /**
   * Sets the DataContext for the current {@link java.lang.Thread}. If
   * {@code null}, the default behavior is dynamic adaptation with
   * extension profile creation and auto-extension.
   */
  public static void setThreadDataContext(DataContext dataContext) {
    threadDataContext.set(dataContext);
  }

  /**
   * Returns the DataContext for the current {@link java.lang.Thread}. If
   * {@code null}, the default behavior is dynamic adaptation with
   * extension profile creation and auto-extension.
   */
  public static DataContext getThreadDataContext() {
    return threadDataContext.get();
  }

  public DataFlavor[] getTransferDataFlavors() {
    throw new UnsupportedOperationException("No DataFlavor support");
  }

  public Object getTransferData(DataFlavor df,
                                DataSource ds) {
    throw new UnsupportedOperationException("No DataFlavor support");
  }

  /**
   * Regex string that describes XML directives or whitespace that might
   * precede the first entry in an Atom XML document.
   */
  static final String XML_PREAMBLE = "((<\\?[^?]*\\?>)|(\\s))*";

  /**
   * See if the root element of the document is named 'feed'
   */
  static private final Pattern FEED_DOCUMENT_PATTERN =
      Pattern.compile(XML_PREAMBLE + "<(\\w*:)?feed[\\s>].*", Pattern.DOTALL);

  /**
   * See if the root element of the document is named 'entry'
   */
  static private final Pattern ENTRY_DOCUMENT_PATTERN =
      Pattern.compile(XML_PREAMBLE + "<(\\w*:)?entry[\\s>].*", Pattern.DOTALL);

  public Object getContent(DataSource ds) throws IOException {

    // Only parsing of Atom content is currently supported by this handler.
    ContentType contentType = new ContentType(ds.getContentType());
    if (!contentType.getMediaType().equals("application/atom+xml")) {
      throw new UnsupportedOperationException("Unable to parse media: " +
          ds.getContentType());
    }
    String charset = contentType.getCharset();

    // Sniff ahead in the content stream to try and identify if this
    // is a feed or an entry.  The regex matching is based upon the
    // assumption that the feed or entry element <b>must</b> be the first
    // element in the document for this to be a valid Atom syntax
    // document, as defined by RFC4287, which defines the two valid
    // formats:  Feed Document and Entry Document for the Atom MIME
    // type.
    //
    byte [] buf = new byte[1024];        // search first 1k bytes (512 chars)
    PushbackInputStream pushbackStream =
      new PushbackInputStream(ds.getInputStream(), buf.length);
    int n = pushbackStream.read(buf, 0, buf.length);
    if (n == -1) {
      throw new IOException("No content available from data source");
    }

    String parseString;
    if (charset == null) {
      parseString = new String(buf, 0, n);
    } else {
      parseString = new String(buf, 0, n, charset);
    }
    boolean isFeed;     // true = feed, false = entry
    if (FEED_DOCUMENT_PATTERN.matcher(parseString).matches()) {
      isFeed = true;
    } else if (ENTRY_DOCUMENT_PATTERN.matcher(parseString).matches()) {
      isFeed = false;
    } else {
      ServiceException e = new InvalidEntryException(
          "Unable to find Atom feed or entry element");
      throw new IOException(e.getMessage());
    }
    pushbackStream.unread(buf, 0, n);

    // If the charset is known, construct a reader with the appropriate
    // encoding otherwise, we'll use a stream and let the XML parser try to
    // determine the encoding.
    ParseSource source = null;
    if (charset != null) {
      if (charset.toLowerCase().startsWith("utf-")) {
        // For unicode, use a special reader that includes BOM handling.
        source = new ParseSource(new UnicodeReader(pushbackStream, charset));
      } else {
        source =
            new ParseSource(new InputStreamReader(pushbackStream, charset));
      }
    }

    Object retObject;
    DataContext dataContext = getThreadDataContext();
    try {
      if (isFeed) {
        if (dataContext != null) {
          retObject = ParseUtil.readFeed(
              source, dataContext.feedClass, dataContext.extProfile);
        } else {
          retObject = BaseFeed.readFeed(source);
        }
      } else {
        if (dataContext != null) {
          retObject = ParseUtil.readEntry(
              source, dataContext.entryClass, dataContext.extProfile);
        } else {
          retObject = BaseEntry.readEntry(source);
        }
      }
    } catch (ParseException pe) {
      IOException ioe = new IOException("Unable to parse DataSource");
      ioe.initCause(pe);
      throw ioe;
    } catch (ServiceException se) {
      IOException ioe = new IOException("Error reading content");
      ioe.initCause(se);
      throw ioe;
    }
    return retObject;
  }

  public void writeTo(Object obj, String mimeType, OutputStream os)
      throws IOException {

    if (obj == null) {
      throw new NullPointerException("Invalid source object");
    }

    ContentType contentType = new ContentType(mimeType);

    OutputStreamWriter osw;
    String charset = contentType.getCharset();
    if (contentType.getCharset() != null) {
      osw = new OutputStreamWriter(os, contentType.getCharset());
    } else {
      osw = new OutputStreamWriter(os);
    }
    XmlWriter xw = new XmlWriter(osw);

    // Create an extension profile, that will be extended by the target
    // object as needed.
    ExtensionProfile extProfile = new ExtensionProfile();
    if (obj instanceof Kind.Adaptor) {
      extProfile.addDeclarations((Kind.Adaptor)obj);
    }
    if (obj instanceof BaseFeed) {

      BaseFeed<?, ?> feed = (BaseFeed<?, ?>) obj;
      if (mimeType.equals("application/rss+xml")) {
        feed.generateRss(xw, extProfile);
      } else {
        xw.setDefaultNamespace(Namespaces.atomNs);
        ((BaseFeed<?, ?>) obj).generateAtom(xw, extProfile);
      }

    } else if (obj instanceof BaseEntry) {

      BaseEntry<?> entry = (BaseEntry<?>) obj;
      if (mimeType.equals("application/rss+xml")) {
        ((BaseEntry<?>) obj).generateRss(xw, extProfile);
      } else {
        xw.setDefaultNamespace(Namespaces.atomNs);
        ((BaseEntry<?>) obj).generateAtom(xw, extProfile);
      }

    } else {
      throw new IllegalArgumentException("Cannot convert " + obj.getClass() +
          " object to " + mimeType);
    }
    osw.flush();
  }
}
