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
 * Adds glossary.
 *
 * 
 */
public class AddGlossaryCommand implements Command {

  public static final AddGlossaryCommand INSTANCE
      = new AddGlossaryCommand();

  /**
   * This is a singleton.
   */
  private AddGlossaryCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    GlossaryEntry requestEntry = createEntryFromArgs(args);

    System.out.print("Adding glossary....");
    System.out.flush();

    URL feedUrl = FeedUris.getGlossariesFeedUrl();
    GlossaryEntry resultEntry = service.insert(feedUrl, requestEntry);

    printResults(resultEntry);
  }

  private GlossaryEntry createEntryFromArgs(String[] args)
      throws IOException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    System.out.println("You asked to add a glossary...");

    GlossaryEntry entry = new GlossaryEntry();

    String title = parser.getValue("title");
    System.out.println("...with title " + title);
    entry.setTitle(new PlainTextConstruct(title));

    String filename = parser.getValue("file");
    System.out.println("...with contents from " + filename);
    File file = new File(filename);
    String mimeType = "text/csv";
    MediaFileSource fileSource = new MediaFileSource(file, mimeType);
    MediaContent content = new MediaContent();
    content.setMediaSource(fileSource);
    content.setMimeType(new ContentType(mimeType));
    entry.setContent(content);

    return entry;
  }

  private void printResults(GlossaryEntry entry) {
    System.out.println("...done, glossary was successfully created with "
        + "attributes.");

    String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);
    System.out.println("->"
          + "id = " + id
          + ", title = '" + entry.getTitle().getPlainText() + "'");
  }

  public String helpString() {
    return "Adds glossaries."
        + "\n\t--title <title>\t; a name for this glossary"
        + "\n\t--file <filename>\t; Path of file containing glossary "
        + "entries";
  }
}
