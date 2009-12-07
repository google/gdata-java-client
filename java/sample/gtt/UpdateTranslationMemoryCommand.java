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
import com.google.gdata.data.gtt.TranslationMemoryEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Update translation memory.
 *
 * 
 */
public class UpdateTranslationMemoryCommand implements Command {

  public static final UpdateTranslationMemoryCommand INSTANCE
      = new UpdateTranslationMemoryCommand();

  /**
   * This is a singleton.
   */
  private UpdateTranslationMemoryCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    String id = parser.getValue("id");
    URL feedUrl = FeedUris.getTranslationMemoryFeedUrl(id);

    TranslationMemoryEntry requestEntry = service.getEntry(feedUrl,
        TranslationMemoryEntry.class);

    System.out.println("You want to update translation memory with id:"
        + id + " ...");

    if (parser.containsKey("title")) {
      String title = parser.getValue("title");
      System.out.println("...by changing title to " + title);
      requestEntry.setTitle(new PlainTextConstruct(title));
    }

    if (parser.containsKey("file")) {
      String filename = parser.getValue("file");
      System.out.println("...by appending contents from file " + filename);
      File file = new File(filename);
      String mimeType = "text/xml";

      MediaFileSource fileSource = new MediaFileSource(file, mimeType);
      MediaContent content = new MediaContent();
      content.setMediaSource(fileSource);
      content.setMimeType(new ContentType(mimeType));

      requestEntry.setContent(content);
    }

    System.out.print("Updating translation memory....");
    System.out.flush();

    TranslationMemoryEntry resultEntry = null;
    if (requestEntry.getContent() == null) {
      resultEntry = service.update(feedUrl, requestEntry);
    } else {
      resultEntry = service.updateMedia(feedUrl, requestEntry);
    }

    printResults(resultEntry);
  }

  private void printResults(TranslationMemoryEntry entry) {
    System.out.println("...done, translation memory was successfully updated "
        + "with attributes.");

    String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);
    System.out.println("->"
          + "id = " + id
          + ", title = '" + entry.getTitle().getPlainText() + "'"
          + ", scope = '" + entry.getScope().getValue() + "'");
  }

  public String helpString() {
    return "Updates translation documents."
        + "\n\t--id <id>\t; id of the translation memory to update"
        + "\n\t--title <title>\t; a name for this translation memory"
        + "\n\t--file <filename>\t; Optional path of tmx file to upload";
  }
}
