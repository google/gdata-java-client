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

package sample.appsforyourdomain.gmailsettings;

import com.google.gdata.client.appsforyourdomain.gmailsettings.GmailFilterService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the client library for the Google Apps Gmail Settings API. It
 * shows how the GmailFilterService can be used to create filters into Gmail
 * account.
 *
 * 
 */
public class AppsForYourDomainGmailFilterClient {

  private static final Logger LOGGER =
    Logger.getLogger(AppsForYourDomainGmailFilterClient.class.getName());

  private final String domain;
  private final String destinationUser;

  // Number of filters to insert.
  private static final int ITEMS_TO_BATCH = 5;
  private final GmailFilterService gmailFilterService;

  // Change the value of these field for your own filter
  private final String from = "me@google.com";
  private final String to = "you@google.com";
  private final String subject = "subject";
  private final String hasTheWord = "has";
  private final String doesNotHaveTheWord = "no";
  private final String hasAttachment = "true";
  private final String shouldMarkAsRead = "true";
  private final String shouldArchive = "true";
  private final String label = "label";
  private final String forwardTo = "someone@google.com";
  private final String neverSpam = "true";
  private final String shouldStar = "true";
  private final String shouldTrash = "false";

  /**
   * Constructs an AppsForYourDomainGmailFilterClient for the given domain
   * using the given admin credentials.
   *
   * @param username The user name (not email) of a domain administrator.
   * @param password The user's password on the domain.
   * @param domain The domain in which filter is being created.
   * @param destinationUser the user who owns the new filter.
   */
  public AppsForYourDomainGmailFilterClient(String username, String password,
      String domain, String destinationUser) throws Exception {

    this.domain = domain;

    if (destinationUser == null) {
      this.destinationUser = username;
    } else {
      this.destinationUser = destinationUser;
    }

    // Set up the gmail filter service.
    gmailFilterService = new GmailFilterService("exampleCo-exampleApp-1");
    gmailFilterService.setUserCredentials(username + "@" + domain, password);

    // Run the sample.
    runSample();
  }

  /**
   * Main driver for the sample.
   * <ul>
   *   <li>Create an entry of Gmail filter and print the results </li>
   *   <li>Create an feed of Gmail filters and using batch to send the
   *     request</li>
   * </ul>
   */
  private void runSample() {

    GenericEntry entry = new GenericEntry();

    entry.addProperty("user", destinationUser);
    entry.addProperty("key", domain);
    entry.addProperty("from", from);
    entry.addProperty("to", to);
    entry.addProperty("subject", subject);
    entry.addProperty("hasTheWord", hasTheWord);
    entry.addProperty("doesNotHaveTheWord", doesNotHaveTheWord);
    entry.addProperty("hasAttachment", hasAttachment);
    entry.addProperty("shouldMarkAsRead", shouldMarkAsRead);
    entry.addProperty("shouldArchive", shouldArchive);
    entry.addProperty("label", label);
    entry.addProperty("forwardTo", forwardTo);
    entry.addProperty("neverSpam", neverSpam);
    entry.addProperty("shouldStar", shouldStar);
    entry.addProperty("shouldTrash", shouldTrash);

    LOGGER.log(Level.INFO, "Inserting 1 gmail filter.");

    GenericEntry[] entries = new GenericEntry[ITEMS_TO_BATCH];
    for (int i = 0; i < ITEMS_TO_BATCH; i ++) {
      GenericEntry newEntry = new GenericEntry();

      newEntry.addProperty("user", destinationUser);
      newEntry.addProperty("key", domain);
      newEntry.addProperty("from", from);
      newEntry.addProperty("to", to);
      newEntry.addProperty("subject", subject);
      newEntry.addProperty("hasTheWord", hasTheWord);
      newEntry.addProperty("doesNotHaveTheWord", doesNotHaveTheWord);
      newEntry.addProperty("hasAttachment", hasAttachment);
      newEntry.addProperty("shouldMarkAsRead", shouldMarkAsRead);
      newEntry.addProperty("shouldArchive", shouldArchive);

      // Apply different label to different filter
      newEntry.addProperty("label", String.valueOf(i));

      entries[i] = newEntry;
    }

    try {
      LOGGER.log(Level.INFO, "Inserting 1 Gmail filter.");
      GenericEntry resultEntry = insertGmailFilter(entry);
      LOGGER.log(Level.INFO, "Insert 1 filter succeeded.");

      LOGGER.log(Level.INFO, "Batch inserting " + ITEMS_TO_BATCH +
          " Gmail filters");
      GenericFeed resultFeed = batchInsertGmailFilters(entries);

      // Check for failure in the returned entries.
      int failedInsertions = 0, successfulInsertions = 0;
      for (GenericEntry returnedEntry : resultFeed.getEntries()) {
        if (BatchUtils.isFailure(returnedEntry)) {
          BatchStatus status = BatchUtils.getBatchStatus(returnedEntry);
          LOGGER.log(Level.SEVERE, "Entry "
              + BatchUtils.getBatchId(returnedEntry) + " failed insertion: "
              + status.getCode() + " " + status.getReason());
          failedInsertions++;
        } else {
          successfulInsertions++;
        }
      }

      LOGGER.log(Level.INFO, "Batch insertion: "
          + Integer.toString(successfulInsertions) + " succeeded, "
          + Integer.toString(failedInsertions) + " failed.");
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Caught IOException: " + e.toString());
      e.printStackTrace();
    } catch (ServiceException e) {
      LOGGER.log(Level.SEVERE, "Caught ServiceException: " + e.toString());
      e.printStackTrace();
    }
  }

  /**
   * Inserts one Gmail filter entry.
   *
   * @param filter an {@link GenericEntry} objects that has all the Gmail filter
   *        properties set.
   * @return an entry with the result of the operation.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the insert request failed due to system error.
   */
  private GenericEntry insertGmailFilter(GenericEntry filter)
      throws ServiceException, IOException {
    return gmailFilterService.insert(domain, filter);
  }

  /**
   * Insert one or more Gmail filter entries in a single batch operation. Using
   * batch insertion is helpful in reducing HTTP overhead.
   *
   * @param filters one or more {@link GenericEntry} objects containing Gmail
   *        filter properties.
   * @return a feed with the result of each operation in a separate
   *         {@link GenericEntry} object.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the insert request failed due to system error.
   */
  private GenericFeed batchInsertGmailFilters(GenericEntry ... filters)
      throws ServiceException, IOException {
    GenericFeed feed = new GenericFeed();
    for (int i = 0; i < filters.length; i++) {
      BatchUtils.setBatchId(filters[i], Integer.toString(i));
      feed.getEntries().add(filters[i]);
    }
    return gmailFilterService.batch(domain, feed);
  }

  /**
   * Prints the command line usage of this sample application.
   */
  private static void usage() {
    System.out.println("Usage: java AppsForYourDomainGmailFilterClient"
        + " --username <username> --password <password> --domain <domain>\n"
        + " --destination_user <destination_user>");

    System.out.println();
    System.out.println("A simple application that demonstrates how to create"
        + " filters to a Google Apps email account. Authenticates using the"
        + " provided login credentials, then create sample filters to the"
        + " specified destination account.");

    System.out.println();
    System.out.println("Specify username and destination_user as just the name,"
        + " not email address.  For example, to create filter to joe@example.com"
        + " use these options:  --username joe --password your_password"
        + " --domain example.com");
  }

  /**
   * Main entry point. Parses arguments and creates and invokes the
   * AppsForYourDomainGmailFilterClient.
   *
   * Usage: java AppsForYourDomainGmailFilterClient --username &lt;user&gt;
   * --password &lt;pass&gt; --domain &lt;domain&gt;
   * --destination_user &lt;destination_user&gt;
   */
  public static void main(String[] arg) throws Exception {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(arg);

    // Parse command-line flags
    String username = parser.getValue("username");
    String password = parser.getValue("password");
    String domain = parser.getValue("domain");
    String destinationUser = parser.getValue("destination_user");

    boolean help = parser.containsKey("help");
    if (help || (username == null) || (password == null) || (domain == null)) {
      usage();
      System.exit(1);
    }

    new AppsForYourDomainGmailFilterClient(username, password, domain,
        destinationUser);
  }
}
