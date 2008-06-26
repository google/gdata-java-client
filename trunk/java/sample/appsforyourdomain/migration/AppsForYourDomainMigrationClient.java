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


package sample.appsforyourdomain.migration;

import com.google.gdata.client.appsforyourdomain.migration.MailItemService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.appsforyourdomain.migration.Label;
import com.google.gdata.data.appsforyourdomain.migration.MailItemEntry;
import com.google.gdata.data.appsforyourdomain.migration.MailItemFeed;
import com.google.gdata.data.appsforyourdomain.migration.MailItemProperty;
import com.google.gdata.data.appsforyourdomain.migration.Rfc822Msg;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is the client library for the Google Apps Email Migration API. It
 * shows how the MailItemService can be used to migrate mail into GMail.
 */
public class AppsForYourDomainMigrationClient {

  private static final Logger LOGGER = 
      Logger.getLogger(AppsForYourDomainMigrationClient.class.getName());
  
  // Number of email messages to insert.
  private static final int ITEMS_TO_BATCH = 5;
  
  // The mail message to migrate.
  private static String rfcTxt =
    "Received: by 10.143.160.15 with HTTP; Mon, 16 Jul 2007 10:12:26 -0700 (P"
    + "DT)\r\n"
    + "Message-ID: <c8acb6980707161012i5d395392p5a6d8d14a8582613@mail." 
    + "gmail.com>\r\n" 
    + "Date: Mon, 16 Jul 2007 10:12:26 -0700\r\n"
    + "From: \"Mr. Serious\" <serious@domain.com>\r\n"
    + "To: \"Mr. Admin\" <admin@domain.com>\r\n"
    + "Subject: Subject \r\n" 
    + "MIME-Version: 1.0\r\n"
    + "Content-Type: text/plain; charset=ISO-8859-1; format=flowed\r\n"
    + "Content-Transfer-Encoding: 7bit\r\n"
    + "Content-Disposition: inline\r\n"
    + "Delivered-To: admin@domain.com\r\n" 
    + "\r\n"
    + "This is a message delivered via the Email Migration API\r\n"
    + "\r\n"; 
  
  private static final String MIGRATED_LABEL = "Migrated Email";
  
  private final String domain;
  private final String destinationUser;
  
  private final MailItemService mailItemService;


  /**
   * Constructs an AppsForYourDomainMigrationClient for the given domain using
   * the given admin credentials.
   * 
   * @param username The username (not email) of a domain user or administrator
   * @param password The user's password on the domain
   * @param domain The domain in which mail is being migrated
   * @param destinationUser the destination user to whom mail should be migrated
   */
  public AppsForYourDomainMigrationClient(String username, String password,
      String domain, String destinationUser) throws Exception {

    this.domain = domain;
    
    if (destinationUser == null) {
      this.destinationUser = username;
    } else {
      this.destinationUser = destinationUser;
    }

    // Set up the mail item service.
    mailItemService = new MailItemService("exampleCo-exampleApp-1");
    mailItemService.setUserCredentials(username + "@" + domain, password);

    // Run the sample.
    runSample();
  }

  /**
   * Main driver for the sample; migrates a batch feed of email messages
   * and prints the results.
   */
  private void runSample() {
    
      // Create labels for mail items to be inserted.
    List<String> labels = new ArrayList<String>();
    labels.add(MIGRATED_LABEL);

    // Set properties for mail items to be inserted. We want all these
    // mail items to be unread and sent to the inbox (in addition to being
    // labeled).
    List<MailItemProperty> properties = new ArrayList<MailItemProperty>();
    properties.add(MailItemProperty.UNREAD);
    properties.add(MailItemProperty.INBOX);

    MailItemEntry[] entries = new MailItemEntry[ITEMS_TO_BATCH];
    for (int i = 0; i < entries.length; i++) {
      entries[i] = setupMailItem(rfcTxt, properties, labels);
    }

    // Send several emails in a batch.
    LOGGER.log(Level.INFO, "Inserting " + Integer.toString(ITEMS_TO_BATCH)
        + " mail items in a batch.");
    try {
      MailItemFeed feed = batchInsertMailItems(entries);

      // Check for failure in the returned entries.
      int failedInsertions = 0, successfulInsertions = 0;
      for (MailItemEntry returnedEntry : feed.getEntries()) {
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
   * Inserts one or more MailItem entries in a single batch operation. Using
   * batch insertion is helpful in reducing HTTP overhead.
   * 
   * @param mailItems one or more {@link MailItemEntry} objects
   * @return a feed with the result of each operation in a separate
   *         {@link MailItemEntry} object.
   * @throws IOException if an error occurs while communicating with the GData
   *         service.
   * @throws ServiceException if the insert request failed due to system error.
   */  
  private MailItemFeed batchInsertMailItems(MailItemEntry ... mailItems)
      throws ServiceException, IOException {
    LOGGER.log(Level.INFO, "Batch inserting " + Integer.toString(
        mailItems.length) + " mailItems");
    
    MailItemFeed feed = new MailItemFeed();
    for (int i = 0; i < mailItems.length; i++) {
      BatchUtils.setBatchId(mailItems[i], Integer.toString(i));
      feed.getEntries().add(mailItems[i]);
    }
    
    return mailItemService.batch(domain, destinationUser, feed);
  }
  
  /**
   * Helper method to read a file and return its contents as a text string,
   * with lines separated by "\r\n" (CR+LF) style newlines.
   * @param location the path to the file
   * @return the String contents of the file
   * @throws IOException if an error occurs reading the file
   */
  private static String readFile(String location) throws IOException {
    FileInputStream is = new FileInputStream(new File(location)); 
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    StringBuffer fileContents = new StringBuffer();

    String newLine = br.readLine();
    while (newLine != null) {
      fileContents.append(newLine);
      fileContents.append("\r\n");

      newLine = br.readLine();        
    }
      
    br.close();
    return fileContents.toString(); 
  }
  
  /**
   * Helper method to set up a new MailItemEntry with the given values.
   * 
   * @param rfcText the RFC822 text of the message
   * @param properties a list of properties to be applied to the message
   * @param labels a list of labels the message should have when inserted into
   * Gmail
   * @return the {@code MailItemEntry} set up with the message, labels and
   * properties
   */
  private MailItemEntry setupMailItem(String rfcText,
      List<MailItemProperty> properties, List<String> labels) {

    // Unique subject and message id are required so that GMail does not
    // suppress duplicate messages
    String randomFactor = Integer.toString(100000 + 
        (new Random()).nextInt(900000));
    rfcText = rfcText.replace("Subject: Subject",
        "Subject: Unique Subject " + randomFactor);
    rfcText = rfcText.replace("Message-ID: <", "Message-ID: <" + randomFactor);
    Rfc822Msg rfcMsg = new Rfc822Msg(rfcText);
    
    // create MailItemEntry with appropriate data
    MailItemEntry mailItem = new MailItemEntry();
    mailItem.setRfc822Msg(rfcMsg);
    for (String label : labels) {
      mailItem.addLabel(new Label(label));
    }
    
    for (MailItemProperty property : properties) {
      mailItem.addMailProperty(property);
    }
    
    return mailItem;
  }
  
  /**
   * Prints the command line usage of this sample application.
   */
  private static void usage() {
    System.out.println("Usage: java AppsForYourDomainMigrationClient"
        + " --username <username> --password <password> --domain <domain>\n"
        + " [--destination_user <destination_user>] [--data_file <file>]");

    System.out.println();
    System.out.println("A simple application that demonstrates how to migrate"
        + " email mesages to a Google Apps email account.  Authenticates using"
        + " the provided login credentials, then migrates a sample message to"
        + " your own account (if you are a user) or to the specified"
        + " destination account (if you are a domain administrator).");
    
    System.out.println();
    System.out.println("If --data_file is specified, an RFC822 message will be"
        + " read from the given file; otherwise, a sample message will be"
        + "used.");
    
    System.out.println();
    System.out.println("Specify username and destination_user as just the name,"
        + " not email address.  For example, to migrate mail to joe@example.com"
        + " use these options:  --username joe --password your_password"
        + " --domain example.com");
  }  
  
  /**
   * Main entry point.  Parses arguments and creates and invokes the
   * AppsForYourDomainMigrationClient.
   *
   * Usage: java AppsForYourDomainMigrationClient --username &lt;user&gt;
   * --password &lt;pass&gt; --domain &lt;domain&gt;
   * [--destination_user &lt;destination_user&gt;] [--data_file &lt;file&gt;]
   */
  public static void main(String[] arg) throws Exception {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(arg);

    // Parse command-line flags
    String username = parser.getValue("username");
    String password = parser.getValue("password");
    String domain = parser.getValue("domain");
    String destinationUser = parser.getValue("destination_user");
    String emailFileName = parser.getValue("data_file");
    
    boolean help = parser.containsKey("help");
    if (help || (username == null) || (password == null)
             || (domain == null)) {
      usage();
      System.exit(1);
    }

    // If the user supplied a filename, read the email data from it.
    if (emailFileName != null) {      
      // Read email text
      LOGGER.log(Level.INFO, "Reading email data from file");
      rfcTxt = readFile(emailFileName);
      LOGGER.log(Level.INFO, "Finished reading email data");
    }
    
    new AppsForYourDomainMigrationClient(username, password, domain,
        destinationUser);
  }
}