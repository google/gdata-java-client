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
import com.google.gdata.data.gtt.ScopeEntry;
import com.google.gdata.data.gtt.TranslationMemoryEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Adds translation memories.
 *
 * 
 */
public class AddTranslationMemoryCommand implements Command {

  public static final AddTranslationMemoryCommand INSTANCE
      = new AddTranslationMemoryCommand();

  /**
   * This is a singleton.
   */
  private AddTranslationMemoryCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    TranslationMemoryEntry requestEntry = createEntryFromArgs(args);

    System.out.print("Adding tm....");
    System.out.flush();

    URL feedUrl = FeedUris.getTranslationMemoriesFeedUrl();
    TranslationMemoryEntry resultEntry = service.insert(feedUrl, requestEntry);

    printResults(resultEntry);
  }

  private TranslationMemoryEntry createEntryFromArgs(String[] args)
      throws IOException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    System.out.println("You asked to add translation memory...");

    TranslationMemoryEntry entry = new TranslationMemoryEntry();

    String title = parser.getValue("title");
    System.out.println("...with title " + title);
    entry.setTitle(new PlainTextConstruct(title));

    if (parser.containsKey("file")) {
      String filename = parser.getValue("file");
      System.out.println("...with contents from " + filename);
      File file = new File(filename);
      String mimeType = "text/xml";

      MediaFileSource fileSource = new MediaFileSource(file, mimeType);
      MediaContent content = new MediaContent();
      content.setMediaSource(fileSource);
      content.setMimeType(new ContentType(mimeType));

      entry.setContent(content);
    }

    if (parser.containsKey("private")) {
      System.out.println("...with private access");
      entry.setScope(new ScopeEntry(ScopeEntry.Value.PRIVATE));
    } else {
      System.out.println("...with public access");
      entry.setScope(new ScopeEntry(ScopeEntry.Value.PUBLIC));
    }

    return entry;
  }

  private void printResults(TranslationMemoryEntry entry) {
    System.out.println("...done, translation memory was successfully created "
        + "with given attributes.");

    String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);
    System.out.println("->"
          + "id = " + id
          + ", title = '" + entry.getTitle().getPlainText() + "'"
          + ", scope = '" + entry.getScope().getValue() + "'");
  }

  public String helpString() {
    return "Creates a new translation memory."
        + "\n\t--title <title>\t; a name for this translation memory"
        + "\n\t--file <filename>\t; Optional path of tmx file to upload"
        + "\n\t--private\t; if data in tmx shouldn't be shared with public";
  }
}
