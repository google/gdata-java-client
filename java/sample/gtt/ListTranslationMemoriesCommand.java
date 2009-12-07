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
import com.google.gdata.client.gtt.TranslationMemoryQuery;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.gtt.ScopeEntry;
import com.google.gdata.data.gtt.TranslationMemoryEntry;
import com.google.gdata.data.gtt.TranslationMemoryFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * Lists translation memories.
 *
 * 
 */
public class ListTranslationMemoriesCommand implements Command {

  public static final ListTranslationMemoriesCommand INSTANCE
      = new ListTranslationMemoriesCommand();

  /**
   * This is a singleton.
   */
  private ListTranslationMemoriesCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    TranslationMemoryQuery query = createQueryFromArgs(args);

    System.out.print("Fetching translation memories....");
    System.out.flush();

    TranslationMemoryFeed resultFeed
        = service.getFeed(query, TranslationMemoryFeed.class);

    printResults(resultFeed);
  }

  private TranslationMemoryQuery createQueryFromArgs(String[] args)
      throws IOException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    URL feedUrl = FeedUris.getTranslationMemoriesFeedUrl();
    TranslationMemoryQuery query = new TranslationMemoryQuery(feedUrl);

    if (parser.containsKey("onlyprivate")) {
      System.out.println("You asked to list all private translation "
          + "memories...");
      query.setScope(ScopeEntry.Value.PRIVATE.toString());
    } else if (parser.containsKey("onlypublic")) {
      System.out.println("You asked to list all public translation "
          + "memories...");
      query.setScope(ScopeEntry.Value.PUBLIC.toString());
    } else {
      System.out.println("You asked to list all translation "
          + "memories...");
    }

    return query;
  }

  private void printResults(TranslationMemoryFeed resultFeed) {
    System.out.println("...done, there are " + resultFeed.getEntries().size()
        + " translation memories matching the query.\n");

    int i = 1;
    for (TranslationMemoryEntry entry : resultFeed.getEntries()) {
      String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);
      System.out.println(String.valueOf(i++) +  ") "
          + "id = " + id
          + ", title = '" + entry.getTitle().getPlainText() + "'"
          + ", scope = '" + entry.getScope().getValue() + "'");
    }
    System.out.println();
  }

  public String helpString() {
    return "Lists translation memories accessible for given user."
        + "\n\t--onlypublic\t; Optional param to show only "
        + "public translation memories"
        + "\n\t--onlyprivate\t; Optional param to show only "
        + "translation memories private to the given user";
  }
}
