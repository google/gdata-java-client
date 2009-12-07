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
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.gtt.DocumentEntry;
import com.google.gdata.data.gtt.GlossariesElement;
import com.google.gdata.data.gtt.TmsElement;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * Updates translation documents.
 *
 * 
 */
public class UpdateDocumentCommand implements Command {

  public static final UpdateDocumentCommand INSTANCE
      = new UpdateDocumentCommand();

  /**
   * This is a singleton.
   */
  private UpdateDocumentCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    String id = parser.getValue("id");
    URL feedUrl = FeedUris.getDocumentFeedUrl(id);

    DocumentEntry requestEntry = service.getEntry(feedUrl, DocumentEntry.class);
    requestEntry.setLastModifiedBy(null);

    System.out.println("You want to update document with id:" + id + "...");

    if (parser.containsKey("title")) {
      String title = parser.getValue("title");
      System.out.println("...by changing title to " + title);
      requestEntry.setTitle(new PlainTextConstruct(title));
    }

    if (parser.containsKey("tmids")) {
      String tmIds = parser.getValue("tmids");
      System.out.println("...by adding translation memories with ids: "
          + tmIds);
      TmsElement tm = new TmsElement();
      for (String tmId : tmIds.split(",")) {
        String tmHref = FeedUris.getTranslationMemoryFeedUrl(tmId).toString();

        Link tmLink = new Link();
        tmLink.setHref(tmHref);

        tm.addLink(tmLink);
      }
      requestEntry.setTranslationMemory(tm);
    }

    if (parser.containsKey("glids")) {
      String glIds = parser.getValue("glids");
      System.out.println("...by adding glossaries with ids: "
          + glIds);
      GlossariesElement gl = new GlossariesElement();
      for (String glId : glIds.split(",")) {
        String glHref = FeedUris.getGlossaryFeedUrl(glId).toString();

        Link glLink = new Link();
        glLink.setHref(glHref);

        gl.addLink(glLink);
      }
      requestEntry.setGlossary(gl);
    }

    System.out.print("Updating document....");
    System.out.flush();
    DocumentEntry resultEntry = service.update(feedUrl, requestEntry);
    printResults(resultEntry);
  }

  private void printResults(DocumentEntry entry) {
    System.out.println("...done, document was successfully updated with "
        + "attributes.");

    String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);
    System.out.println("->"
          + "id = " + id
          + ", title = '" + entry.getTitle().getPlainText() + "'");
  }

  public String helpString() {
    return "Updates translation documents."
        + "\n\t--id <id>\t; the id of the document to update"
        + "\n\t--title <title>\t; a name for the document"
        + "\n\t--tmids <id, id, ...>\t; Optional param to attach translation "
        + "memories to document, value must be comma separated tm ids"
        + "\n\t--glids <id, id, ...>\t; Optional param to attach glossaries "
        + "to document, value must be comma separated glossary ids";
  }
}
