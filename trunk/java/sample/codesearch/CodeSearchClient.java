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


package sample.codesearch;

import com.google.gdata.client.codesearch.CodeSearchService;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.codesearch.CodeSearchFeed;
import com.google.gdata.data.codesearch.CodeSearchEntry;
import com.google.gdata.data.codesearch.Match;


import sample.util.SimpleCommandLineParser;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;


/**
 * An application that serves as a sample to show how the CodeSearchService
 * can be used to retrieve data from Google Code Search.
 *
 * 
 */


public class CodeSearchClient {
  private static final String CODESEARCH_FEEDS_URL =
    "http://www.google.com/codesearch/feeds/search?";

  private CodeSearchService codesearchService;
  private URL privateFeedUrl;

  public CodeSearchClient(String query,  String nresults, String start)
      throws Exception {
    codesearchService = new CodeSearchService("gdata-sample-codesearch");
    privateFeedUrl = new URL(CODESEARCH_FEEDS_URL + "q=" + query
                             + "&start-index=" + start +
                             "&max-results="+ nresults);
  }

  /**
   * Driver for the sample.
   *
   * @param out outputStream to which to write status and messages
   */

  public void run(PrintStream out) throws Exception {
    retrieveFeed(out);
  }

  /**
   * Retrieves a query feed.
   *
   * @param out outputStream on which to write status info
   * @throws Exception if error in retrieving feed
   */
  private void retrieveFeed(PrintStream out)
      throws Exception {
    PrintWriter writer = new PrintWriter(out);
    XmlWriter xmlWriter = new XmlWriter(writer);
    CodeSearchFeed myFeed =
      codesearchService.getFeed(privateFeedUrl, CodeSearchFeed.class);
    out.println("Retrieved feed: ");
    out.println("Title: " + myFeed.getTitle().getPlainText());
    out.println("Entries: " + myFeed.getEntries().size());
    out.println("Updated: " + myFeed.getUpdated());
    out.println("Start in: " + myFeed.getStartIndex());
    out.println("Entries:");
    for (CodeSearchEntry entry: myFeed.getEntries() ){
      // Default Gdata elements
      out.println("\tId: " + entry.getId());
      out.println("\tTitle: " + entry.getTitle());
      out.println("\tLink: " + entry.getHtmlLink().getHref());
      out.println("\tUpdated: " + entry.getUpdated());
      out.println("\tAuthor: " + entry.getAuthors().get(0).getName());
      if (entry.getRights() != null)
        out.println("\tLicense:" + entry.getRights().getPlainText());
      // Codesearch Elements
      out.println("\tPackage: ");
      out.println("\t\t Name:" +
                  entry.getPackage().getName());
      out.println("\t\t URI:" +
                  entry.getPackage().getUri());
      entry.getPackage().generate(
          xmlWriter,
          codesearchService.getExtensionProfile());
      out.println("XML: ");
      writer.flush();
      out.println("");
      out.println("\tFile: " + entry.getFile().getName());
      entry.getFile().generate(
          xmlWriter,
          codesearchService.getExtensionProfile());
      out.println("XML: ");
      writer.flush();
      out.println("");
      out.println("\tMatches: ");
      for (Match m : entry.getMatches()) {
        out.println(m.getLineNumber() + ": " +
                    m.getLineText().getPlainText());
        m.generate(
            xmlWriter,
            codesearchService.getExtensionProfile());
        out.println("XML: ");
        writer.flush();
        out.println("");
      }
    }
  }

  /**
   * Main entry point.  Parses arguments and creates and invokes the
   * CodeSearchClient.
   */
  public static void main(String[] arg)
      throws Exception {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(arg);
    String query = parser.getValue("query", "query", "q");
    String nresults = parser.getValue("nresults", "nresults", "nr");
    String start = parser.getValue("start", "index", "start");

    boolean help = parser.containsKey("help", "h");
    if (help || (query == null)) {
      usage();
      System.exit(1);
    }

    CodeSearchClient client = new CodeSearchClient(query, nresults, start);
    client.run(System.out);
  }

  /**
   * Prints usage of this application.
   */
  private static void usage() {
    System.out.println(
        "Usage: java CodeSearchClient --query query_regex [--nresults number_of_results]"
        + " [--start start_index] ");
    System.out.println(
        "\nA simple application that uses the provided query\n" +
        "and returns the results provided by the Google CodeSearch Service\n");
  }
}
