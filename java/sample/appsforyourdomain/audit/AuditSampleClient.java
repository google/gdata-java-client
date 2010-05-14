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


package sample.appsforyourdomain.audit;

import com.google.gdata.client.appsforyourdomain.audit.AccountInfo;
import com.google.gdata.client.appsforyourdomain.audit.AuditService;
import com.google.gdata.client.appsforyourdomain.audit.MailBoxDumpRequest;
import com.google.gdata.client.appsforyourdomain.audit.MailMonitor;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainErrorCode;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A sample client for Google Apps Audit API service that helps you to audit
 * user's emails, email drafts, and archived chats.
 * 
 * 
 * 
 */
public class AuditSampleClient {

  private static Logger LOGGER = Logger.getLogger(AuditSampleClient.class.toString());

  /**
   * Google Apps Audit API sample run.
   * 
   * Usage: java AuditSampleClient admin@example.com adminpassword domain
   * srcUserName destUserName
   * 
   */
  public static void main(String[] args) {
    AuditService service = null;
    try {

      if (args.length != 5) {
        System.out.println("Usage: java AuditSampleClient <admin@example.com> <adminpassword> "
            + "<domain> <srcUserName> <destUserName>");
        System.out.println("A simple demo for Audit API features like managing email  \n"
            + "monitors, mailbox dump requests, retrieving account info and uploading \n"
            + "domain key for encrypting files. The demo creates mail monitor for 'srcUserName' \n"
            + "with 'destUserName' as the auditor and uses 'srcUserName' as the test user for \n"
            + "the other listed operations above");
        System.exit(1);
      }
      String adminEmail = args[0];
      String adminPassword = args[1];
      String domain = args[2];
      String user = args[3];
      String destUser = args[4];
      
      //A sample base64 encoded PGP format key.
      String sampleKey =
          "LS0tLS1CRUdJTiBQR1AgUFVCTElDIEtFWSBCTE9DSy0tLS0tDQpWZXJzaW9uOiBHbn"
              + "VQRyB2MS40LjEwIChHTlUvTGludXgpDQoNCm1RRU5CRXJXYUQ0QkNBQ3QybmdmczYv"
              + "K1FPR1lieE5iYzNnTG5YSHRxcDdOVFRYTlc0U0pvKy9BMW9VWm9HeEENClF4NnpGWG"
              + "hRLzhNWFc2Nis4U1RTMVlxTkpPQVJGdGpiSUtQd2pyZGN1a2RQellWS0dacmUwUmF4"
              + "Q25NeUNWKzYNCkY0WU5RRDFVZWdIVHUyd0NHUjF1aVlPZkx4VWE3L2RvNnMzMVdSVE"
              + "g4dmJ0aVBZOS82b2JFSXhEakR6S0lxWU8NCnJ2UkRXcUFMQllrbE9rSjNIYmdmeWw0"
              + "MkVzbkxpQWhTK2RNczJQQ0RpMlgwWkpDUFo4ZVRqTHNkQXRxVlpKK1INCldDMUozVU"
              + "R1RmZtY3BzRFlSdFVMOXc2WU10bGFwQys5bW1KM0FCRUJBQUcwVjBSaGMyaGxjaUJV"
              + "WlhOMElDaFUNCmRHVnlNa0JrWVhOb1pYSXRhSGxrTFhSbGMzUXVZMjl0UG9rQk9BUV"
              + "RBUUlBSWdVQ1N0Wm9QZ0liRFFZTENRZ0gNCmsxOVFja1Rwd0Jkc2tFWXVtRnZtV3Zl"
              + "NVVYMlNWVjdmek9DMG5adGdGeHRaR2xKaEdtanNBM3J4RlRsYitJcmENCldaYXlYQ1"
              + "dZaUN6ZDdtOXo1L0t5R0QyR0ZUSy85NG1kbTI1TjZHWGgvYjM1cElGWlhCSS9yWmpy"
              + "WXJoWVJCRnUNCkd0ekdGSXc5QUFuRnlVekVVVVZmUFdVdEJlNXlITVc1NEM2MG5Iaz"
              + "V4WUlhNnFGaGlMcDRQWXFaQ3JZWDFpSXMNCmZSUk9GQT09DQo9U1RIcg0KLS0tLS1F"
              + "TkQgUEdQIFBVQkxJQyBLRVkgQkxPQ0stLS0tLQ==";

      service = new AuditService(adminEmail, adminPassword, domain, "audit-test-" + domain);

      LOGGER.log(Level.INFO, "\n-------------uploadPublicKey-------------");
      GenericEntry sampleEntry = null;
      sampleEntry = service.uploadPublicKey(sampleKey);
      LOGGER.log(Level.INFO, "UploadedKey - " + sampleEntry.getAllProperties());

      // retrieve all MailboxDump requests with fromDate query.
      LOGGER.log(Level.INFO, "\n------retrieveAllMailboxDumpRequests with fromDate---- ");
      Calendar c = Calendar.getInstance();
      List<GenericEntry> entries = service.retrieveAllMailboxDumpRequests(c.getTime());
      for (GenericEntry sampleEntry2 : entries) {
        MailBoxDumpRequest request = new MailBoxDumpRequest(sampleEntry2);
        LOGGER.log(Level.INFO, "All requests -" + sampleEntry2.getAllProperties().toString());
      }

      /*
       * retrieve all MailboxDump requests without fromDate query. This will
       * retrieve all requests made in the last 3 weeks.
       */
      LOGGER.log(Level.INFO, "\n------------retrieveAllMailboxDumpRequests-------------");
      entries = service.retrieveAllMailboxDumpRequests(null);
      for (GenericEntry sampleEntry2 : entries) {
        MailBoxDumpRequest request = new MailBoxDumpRequest(sampleEntry2);
        LOGGER.log(Level.INFO, "All requests -" + sampleEntry2.getAllProperties().toString());
      }

      // Create MailboxDumpRequest
      MailBoxDumpRequest request = new MailBoxDumpRequest();
      request.setAdminEmailAddress(adminEmail);
      c.add(Calendar.MONTH, -1);
      request.setEndDate(c.getTime());
      c.add(Calendar.MONTH, -1);
      request.setBeginDate(c.getTime());

      request.setPackageContent("FULL_MESSAGE");
      request.setSearchQuery("in:chats");
      request.setIncludeDeleted(false);
      request.setUserEmailAddress(user + "@" + domain);

      LOGGER.log(Level.INFO, "\n-------------createMailboxDumpRequest-------------");
      sampleEntry = service.createMailboxDumpRequest(request);
      LOGGER.log(Level.INFO, "\nCreated request - " + sampleEntry.getAllProperties().toString());
      String createdId = sampleEntry.getProperty("requestId");

      LOGGER.log(Level.INFO, "\n-------------retrieveMailboxDumpRequest-------------");
      sampleEntry = service.retrieveMailboxDumpRequest(user, createdId);
      LOGGER.log(Level.INFO, "\nRetrieved dump request - "
          + sampleEntry.getAllProperties().toString());

      LOGGER.log(Level.INFO, "\n-------------deleteMailboxDumpRequest-------------");
      boolean isDeleted = service.deleteMailboxDumpRequest(user, createdId);
      LOGGER.log(Level.INFO, "Deleted mailbox dump request - " + isDeleted);

      MailMonitor monitor = new MailMonitor();
      c = Calendar.getInstance();
      c.add(Calendar.MONTH, 1);
      monitor.setBeginDate(c.getTime());
      c.add(Calendar.MONTH, 1);
      monitor.setEndDate(c.getTime());
      monitor.setDestUserName(destUser);
      monitor.setIncomingEmailMonitorLevel("FULL_MESSAGE");
      monitor.setOutgoingEmailMonitorLevel("HEADER_ONLY");
      monitor.setChatMonitorLevel("FULL_MESSAGE");
      monitor.setDraftMonitorLevel("FULL_MESSAGE");

      LOGGER.log(Level.INFO, "\n-------------createMailMonitor-------------");
      LOGGER.log(Level.INFO, "\nCreating mail monitor for the user: " + user);
      service.createMailMonitor(user, monitor);

      // Retrieve all monitors for the user
      LOGGER.log(Level.INFO, "\n-------------retrieveMonitors-------------");
      LOGGER.log(Level.INFO, "\nRetrieving monitors for the user: " + user);
      GenericFeed feed = service.retrieveMonitors(user);

      for (GenericEntry entry : feed.getEntries()) {
        monitor = new MailMonitor(entry);
        LOGGER.log(Level.INFO, "Request Id: " + monitor.getRequestId());
        LOGGER.log(Level.INFO, "Destination User: " + monitor.getDestUserName());
        LOGGER.log(Level.INFO, "Monitor Begin Date: " + monitor.getBeginDate());
        LOGGER.log(Level.INFO, "Monitor End Date: " + monitor.getEndDate());
        LOGGER.log(Level.INFO, "Outgoing Email Monitor Level: "
            + monitor.getOutgoingEmailMonitorLevel());
        LOGGER.log(Level.INFO, "Incoming Email Monitor Level: "
            + monitor.getIncomingEmailMonitorLevel());
        LOGGER.log(Level.INFO, "Draft Email Monitor Level: " + monitor.getDraftMonitorLevel());
        LOGGER.log(Level.INFO, "Chat Monitor Level: " + monitor.getChatMonitorLevel());
      }

      // Delete the monitor for the user
      LOGGER.log(Level.INFO, "\n-------------deleteMonitor-------------");
      LOGGER.log(Level.INFO, "Deleting monitor for the user...");
      service.deleteMonitor(user, destUser);

      //Account Info requests
      
      LOGGER.log(Level.INFO, "\n-------------createAccountInfoRequest-------------");
      sampleEntry = service.createAccountInfoRequest(user);
      LOGGER.log(Level.INFO, sampleEntry.getAllProperties().toString());
      AccountInfo info = new AccountInfo(sampleEntry);

      LOGGER.log(Level.INFO, "\n-------------retrieveAccountInfoRequest-------------");
      sampleEntry = service.retrieveAccountInfoRequest(user, info.getRequestId());
      info = new AccountInfo(sampleEntry);
      LOGGER.log(Level.INFO, info.getRequestId() + " : " + info.getStatus());

      if (info.getStatus().equalsIgnoreCase("COMPLETED")) {
        for ( String url : info.getFileUrls())
        LOGGER.log(Level.INFO, "File: " + url);  
      }

      // retrieve all account info requests from the given date.
      LOGGER.log(Level.INFO, "\n------retrieveAllAccountInfoRequests with fromDate-----");
      Calendar temp = Calendar.getInstance();
      temp.add(Calendar.MONTH, -1);
      
      entries = service.retrieveAllAccountInfoRequests(temp.getTime());
      for (GenericEntry entry : entries) {
        LOGGER.log(Level.INFO, entry.getAllProperties().toString());
      }

      /*
       * retrieve all account info requests without fromDate query. This will
       * retrieve all requests made in the last 3 weeks.
       */
      LOGGER.log(Level.INFO, "\n-------------retrieveAllAccountInfoRequests-------------");
      entries = service.retrieveAllAccountInfoRequests(null);
      for (GenericEntry entry : entries) {
        LOGGER.log(Level.INFO, entry.getAllProperties().toString());
      }

      LOGGER.log(Level.INFO, "\n-------------deleteAccountInfoRequest-------------");
      try {
        service.deleteAccountInfoRequest(user, info.getRequestId());
      } catch (AppsForYourDomainException e) {
        if (e.getErrorCode() == AppsForYourDomainErrorCode.InvalidValue) {
          LOGGER.log(Level.INFO, e.getMessage());
        } else {
          e.printStackTrace();
        }
      }

      LOGGER.log(Level.INFO, "End Audit API demo run");

    } catch (AuthenticationException e) {
      LOGGER.log(Level.SEVERE, "Authentication Error: " + e.getMessage(), e);
    } catch (AppsForYourDomainException e) {
      LOGGER.log(Level.SEVERE, "AppsForYourDomain Error: " + e.getMessage(), e);
    } catch (MalformedURLException e) {
      LOGGER.log(Level.SEVERE, "Malformed URL Error: " + e.getMessage(), e);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, "Network I/O Error: " + e.getMessage(), e);
    } catch (ServiceException e) {
      LOGGER.log(Level.SEVERE, "Google Data Service Error: " + e.getMessage(), e);
    }
  }
}
