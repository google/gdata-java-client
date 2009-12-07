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


package sample.gtt;

import com.google.gdata.client.gtt.GttService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.gtt.DocumentEntry;
import com.google.gdata.data.gtt.DocumentSource;
import com.google.gdata.data.gtt.GlossariesElement;
import com.google.gdata.data.gtt.SourceLanguage;
import com.google.gdata.data.gtt.TargetLanguage;
import com.google.gdata.data.gtt.TmsElement;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Creates translation documents.
 *
 * 
 */
public class UploadDocumentCommand implements Command {

  /**
   * Represents the MIME types supported by the Translator toolkit GData feed
   */
  public enum MediaType {
    CSV("text/csv"),
    DOC("application/msword"),
    HTML("text/html"),
    HTM("text/html"),
    ODT("application/vnd.oasis.opendocument.text"),
    RTF("application/rtf"),
    TXT("text/plain"),
    AEA("application/octet-stream"),
    AES("application/octet-stream"),
    SRT("text/plain"),
    SUB("text/plain")
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

  public static final UploadDocumentCommand INSTANCE
      = new UploadDocumentCommand();

  /**
   * This is a singleton.
   */
  private UploadDocumentCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    DocumentEntry requestEntry = createEntryFromArgs(args);

    System.out.print("Creating document....");
    System.out.flush();

    URL feedUrl = FeedUris.getDocumentsFeedUrl();
    DocumentEntry resultEntry = service.insert(feedUrl, requestEntry);

    printResults(resultEntry);
  }

  private DocumentEntry createEntryFromArgs(String[] args) throws IOException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    DocumentEntry entry = new DocumentEntry();

    System.out.println("You want to create a new document...");

    String srcLang = parser.getValue("srclang");
    System.out.println("... with source language " + srcLang);
    entry.setSourceLanguage(new SourceLanguage(srcLang));

    String targetLang = parser.getValue("targetlang");
    System.out.println("... with target language " + targetLang);
    entry.setTargetLanguage(new TargetLanguage(targetLang));

    String title = parser.getValue("title");
    System.out.println("... with title " + title);
    entry.setTitle(new PlainTextConstruct(title));

    if (parser.containsKey("weburl")) {
      String url = parser.getValue("weburl");
      System.out.println("... with html contents from " + url);
      DocumentSource docSource = new DocumentSource(DocumentSource.Type.HTML,
          url);
      entry.setDocumentSource(docSource);
    } else if (parser.containsKey("wikipediaurl")) {
      String url = parser.getValue("wikipediaurl");
      System.out.println("... with mediawiki contents from " + url);
      DocumentSource docSource = new DocumentSource(DocumentSource.Type.WIKI,
          url);
      entry.setDocumentSource(docSource);
    } else if (parser.containsKey("knolurl")) {
      String url = parser.getValue("knolurl");
      System.out.println("... with knol contents from " + url);
      DocumentSource docSource = new DocumentSource(DocumentSource.Type.KNOL,
          url);
      entry.setDocumentSource(docSource);
    } else if (parser.containsKey("file")) {
      String filename = parser.getValue("file");
      System.out.println("... with contents from file at " + filename);
      File file = new File(filename);
      String mimeType = MediaType.fromFileName(filename).getMimeType();

      MediaFileSource fileSource = new MediaFileSource(file, mimeType);
      MediaContent content = new MediaContent();
      content.setMediaSource(fileSource);
      content.setMimeType(new ContentType(mimeType));

      entry.setContent(content);
    }

    if (parser.containsKey("tmids")) {
      String tmIds = parser.getValue("tmids");
      System.out.println("...by adding translation memories with ids: "
          + tmIds);
      TmsElement tm = new TmsElement();
      for (String id : tmIds.split(",")) {
        String tmHref = FeedUris.getTranslationMemoryFeedUrl(id).toString();

        Link tmLink = new Link();
        tmLink.setHref(tmHref);

        tm.addLink(tmLink);
      }
      entry.setTranslationMemory(tm);
    }

    if (parser.containsKey("glids")) {
      String glIds = parser.getValue("glids");
      System.out.println("...by adding glossaries with ids: "
          + glIds);
      GlossariesElement gl = new GlossariesElement();
      for (String id : glIds.split(",")) {
        String glHref = FeedUris.getGlossaryFeedUrl(id).toString();

        Link glLink = new Link();
        glLink.setHref(glHref);

        gl.addLink(glLink);
      }
      entry.setGlossary(gl);
    }

    return entry;
  }

  private void printResults(DocumentEntry entry) {
    System.out.println("...done, document was successfully created with "
        + "attributes.");

    String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);
    System.out.println("->"
          + "id = " + id
          + ", title = '" + entry.getTitle().getPlainText() + "'");
  }

  public String helpString() {
    return "Creates translation documents."
        + "\n\t--srclang <lang id>\t; id of source language"
        + "\n\t--targetlang <lang id>\t; id of target language"
        + "\n\t--title <title>\t; a name for the document"
        + "\n\t--file <filename>\t; if content for translation is "
        + "in a local file"
        + "\n\t--weburl <url>\t; if content for translation is a web page"
        + "\n\t--wikipediaurl <url>\t; if content for translation is a "
        + "wikipedia article"
        + "\n\t--knolurl <url>\t; if content for translation is a "
        + "knol article"
        + "\n\t--tmids <id, id, ...>\t; Optional param to attach translation "
        + "memories to document, value must be comma separated tm ids"
        + "\n\t--glids <id, id, ...>\t; Optional param to attach glossaries "
        + "to document, value must be comma separated glossary ids";
  }
}
