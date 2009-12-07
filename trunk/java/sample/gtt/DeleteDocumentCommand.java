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
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * Deletes translation documents.
 *
 * 
 */
public class DeleteDocumentCommand implements Command {

  public static final DeleteDocumentCommand INSTANCE
      = new DeleteDocumentCommand();

  /**
   * This is a singleton.
   */
  private DeleteDocumentCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    String docId = parser.getValue("id");
    boolean deletePermanently = parser.containsKey("perm");

    URL feedUrl;
    if (deletePermanently) {
      feedUrl = FeedUris.getDocumentPermDeleteUrl(docId);
    } else {
      feedUrl = FeedUris.getDocumentFeedUrl(docId);
    }

    if (deletePermanently) {
      System.out.print("Permanently ");
    }
    System.out.print("Deleting document with id: " + docId + " ...");
    System.out.flush();

    service.delete(feedUrl);

    System.out.println("...done");
  }

  public String helpString() {
    return "Deletes specified translation document."
        + "\n\t--id <id>\t; id of the document to delete"
        + "\n\t--perm\t; if document is to be deleted permanently";
  }
}
