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
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.gtt.GlossaryEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Updates glossaries.
 *
 * 
 */
public class UpdateGlossaryCommand implements Command {

  public static final UpdateGlossaryCommand INSTANCE
      = new UpdateGlossaryCommand();

  /**
   * This is a singleton.
   */
  private UpdateGlossaryCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    String id = parser.getValue("id");
    URL feedUrl = FeedUris.getGlossaryFeedUrl(id);

    GlossaryEntry requestEntry = service.getEntry(feedUrl, GlossaryEntry.class);

    System.out.println("You want to update glossary with id:" + id + " ...");

    if (parser.containsKey("title")) {
      String title = parser.getValue("title");
      System.out.println("...by changing title to " + title);
      requestEntry.setTitle(new PlainTextConstruct(title));
    }

    if (parser.containsKey("file")) {
      String filename = parser.getValue("file");
      System.out.println("...by appending contents from file " + filename);
      File file = new File(filename);
      String mimeType = "text/csv";

      MediaFileSource fileSource = new MediaFileSource(file, mimeType);
      MediaContent content = new MediaContent();
      content.setMediaSource(fileSource);
      content.setMimeType(new ContentType(mimeType));

      requestEntry.setContent(content);
    }

    System.out.print("Updating glossaries....");
    System.out.flush();
    GlossaryEntry resultEntry = null;
    if (requestEntry.getContent() == null) {
      resultEntry = service.update(feedUrl, requestEntry);
    } else {
      resultEntry = service.updateMedia(feedUrl, requestEntry);
    }

    printResults(resultEntry);
  }

  private void printResults(GlossaryEntry entry) {
    System.out.println("...done, glossary was successfully updated with "
        + " given attributes.");

    String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);
    System.out.println("->"
          + "id = " + id
          + ", title = '" + entry.getTitle().getPlainText() + "'");
  }

  public String helpString() {
    return "Updates glossary."
        + "\n\t--id <id>\t; id of glossary to update"
        + "\n\t--title <title>\t; a name for this glossary"
        + "\n\t--file <filename>\t; Path of file containing glossary "
        + "entries";
  }
}
