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

import com.google.gdata.client.Query;
import com.google.gdata.client.gtt.GttService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.gtt.GlossaryEntry;
import com.google.gdata.data.gtt.GlossaryFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * Lists glossaries.
 *
 * 
 */
public class ListGlossariesCommand implements Command {

  public static final ListGlossariesCommand INSTANCE
      = new ListGlossariesCommand();

  /**
   * This is a singleton.
   */
  private ListGlossariesCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    Query query = createQueryFromArgs(args);

    System.out.print("Fetching glossaries....");
    System.out.flush();

    GlossaryFeed resultFeed
        = service.getFeed(query, GlossaryFeed.class);

    printResults(resultFeed);
  }

  private Query createQueryFromArgs(String[] args)
      throws IOException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    URL feedUrl = FeedUris.getGlossariesFeedUrl();
    Query query = new Query(feedUrl);

    return query;
  }

  private void printResults(GlossaryFeed resultFeed) {
    System.out.println("...done, there are " + resultFeed.getEntries().size()
        + " glossaries matching the query.\n");

    int i = 1;
    for (GlossaryEntry entry : resultFeed.getEntries()) {
      String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);
      System.out.println(String.valueOf(i++) +  ") "
          + "id = " + id
          + ", title = '" + entry.getTitle().getPlainText() + "'");
    }
    System.out.println();
  }

  public String helpString() {
    return "Lists glossaries accessible for given user.";
  }
}
