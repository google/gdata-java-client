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
import com.google.gdata.client.gtt.DocumentQuery;
import com.google.gdata.client.gtt.GttService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.Category;
import com.google.gdata.data.ICategory;
import com.google.gdata.data.gtt.DocumentEntry;
import com.google.gdata.data.gtt.DocumentFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * Lists translation documents in the user's inbox.
 *
 * 
 */
public class ListDocumentsCommand implements Command {

  public static final ListDocumentsCommand INSTANCE
      = new ListDocumentsCommand();

  /**
   * This is a singleton.
   */
  private ListDocumentsCommand() {
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    DocumentQuery query = createQueryFromArgs(args);

    System.out.print("Fetching documents....");
    System.out.flush();

    DocumentFeed resultFeed = service.getFeed(query, DocumentFeed.class);

    printResults(resultFeed);
  }

  private DocumentQuery createQueryFromArgs(String[] args) throws IOException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    System.out.println("You asked to list documents....");

    URL feedUrl = FeedUris.getDocumentsFeedUrl();
    DocumentQuery query = new DocumentQuery(feedUrl);

    if (parser.containsKey("onlydeleted")) {
      System.out.println("...that are deleted");
      query.setOnlydeleted(true);
    }

    if (parser.containsKey("onlyhidden")) {
      System.out.println("...that are hidden");
      Query.CategoryFilter filter = new Query.CategoryFilter();
      filter.addCategory(new HiddenCategory());
      query.addCategoryFilter(filter);
    }

    if (parser.containsKey("excludehidden")) {
      System.out.println("...that are not hidden");
      Query.CategoryFilter filter = new Query.CategoryFilter();
      filter.addExcludeCategory(new HiddenCategory());
      query.addCategoryFilter(filter);
    }

    String sharedWithEmail = parser.getValue("sharedwith");
    if (sharedWithEmail != null) {
      System.out.println("...that are shared with " + sharedWithEmail);
      query.setSharedWithEmailId(sharedWithEmail);
    }

    String startIndex = parser.getValue("start-index");
    if (startIndex != null) {
      System.out.println("...that start from position " + startIndex);
      query.setStartIndex(Integer.parseInt(startIndex));
    }

    String maxResults = parser.getValue("max-results");
    if (maxResults != null) {
      System.out.println("...and you don't want more than " + maxResults
          + " results");
      query.setMaxResults(Integer.parseInt(maxResults));
    }

    return query;
  }

  private void printResults(DocumentFeed resultFeed) {
    System.out.println("...done, there are " + resultFeed.getEntries().size()
        + " documents matching the query in your inbox.\n");

    int i = 1;
    for (DocumentEntry entry : resultFeed.getEntries()) {
      String id = entry.getId().substring(entry.getId().lastIndexOf('/') + 1);

      StringBuilder categories = new StringBuilder();
      for (Category category : entry.getCategories()) {
        categories.append(category.getLabel())
            .append(';');
      }

      System.out.println(String.valueOf(i++) +  ") "
          + "id = " + id
          + ", title = '" + entry.getTitle().getPlainText() + "'"
          + ", % complete = '" + entry.getPercentComplete().getValue() + "'"
          + ", num source words = '" + entry.getNumberOfSourceWords()
              .getValue() + "'"
          + ", categories = '" + categories.toString() + "'");
    }
    System.out.println();
  }

  /**
   * Utility class for hidden category filter.
   */
  public static class HiddenCategory implements ICategory {
    public String getLabel() {
      return com.google.gdata.data.gtt.HiddenCategory.Label.HIDDEN;
    }

    public String getScheme() {
      return com.google.gdata.data.gtt.HiddenCategory.Scheme.LABELS;
    }

    public String getTerm() {
      return com.google.gdata.data.gtt.HiddenCategory.Term.HIDDEN;
    }
  };

  public String helpString() {
    return "Lists translation documents in user's inbox."
        + "\n\t--onlydeleted\t; Optional param to show only deleted documents"
        + "\n\t--sharedwith <email-id>\t; Optional param to show only "
        + "documents shared with given user"
        + "\n\t--start-index <number>\t; Optional param to show only "
        + "documents starting from given index"
        + "\n\t--max-results <number>\t; Optional param to show only "
        + "given number of documents"
        + "\n\t--onlyhidden\t; Optional param to show only hidden documents"
        + "\n\t--excludehidden\t; Optional param to not show hidden documents";
  }
}
