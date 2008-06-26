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


package sample.calendar;

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.Link;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.acl.AclNamespace;
import com.google.gdata.data.acl.AclRole;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.data.calendar.CalendarAclRole;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Demonstrates basic Calendar Data API operations on the ACL feed using the
 * Java client library:
 * 
 * <ul>
 * <li>Parsing the metafeed for ACL feed URLs.</li>
 * <li>Retrieving access control lists (ACLs) for each calendar.</li>
 * <li>Adding users to access control lists.</li>
 * <li>Updating users on access control lists.</li>
 * <li>Removing users from access control lists.</li>
 * </ul>
 */
public class AclFeedDemo {

  // The base URL for a user's calendar metafeed (needs a username appended).
  private static final String METAFEED_URL_BASE = 
      "http://www.google.com/calendar/feeds/";

  // The string to add to the user's metafeedUrl to access the ACL feed for
  // their primary calendar.
  private static final String ACL_FEED_URL_SUFFIX = "/acl/full";

  // The URL for the metafeed of the specified user.
  // (e.g. http://www.google.com/feeds/calendar/jdoe@gmail.com)
  private static URL metafeedUrl = null;

  // The URL for the ACL feed of the specified user's primary calendar.
  // (e.g. http://www.googe.com/feeds/calendar/jdoe@gmail.com/acl/full)
  private static URL aclFeedUrl = null;

  /**
   * Retrieves the calendar metafeed to get the ACL feed URLs for each calendar.
   * Then prints the access control lists for each of the user's calendars.
   * 
   * @param service An authenticated CalendarService object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static void printAclList(CalendarService service)
      throws ServiceException, IOException {
    CalendarFeed calendarFeed = service
        .getFeed(metafeedUrl, CalendarFeed.class);

    // After accessing the meta-feed, get the ACL link for each calendar.
    System.out.println("Access control lists for your calendars:");
    for (CalendarEntry calEntry : calendarFeed.getEntries()) {
      Link link = calEntry.getLink(AclNamespace.LINK_REL_ACCESS_CONTROL_LIST,
          Link.Type.ATOM);

      // For each calendar that exposes an access control list, retrieve its ACL
      // feed. If link is null, then we are not the owner of that calendar
      // (e.g., it is a public calendar) and its ACL feed cannot be accessed.
      if (link != null) {
        AclFeed aclFeed = service.getFeed(new URL(link.getHref()),
            AclFeed.class);
        System.out.println("\tCalendar \"" + calEntry.getTitle().getPlainText()
            + "\":");
        for (AclEntry aclEntry : aclFeed.getEntries()) {
          System.out.println("\t\tScope: Type=" + aclEntry.getScope().getType()
              + " (" + aclEntry.getScope().getValue() + ")");
          System.out.println("\t\tRole: " + aclEntry.getRole().getValue());
        }
      }
    }
  }

  /**
   * Adds a user in the read-only role to the calendar's access control list.
   * Note that this method will not run by default.
   * 
   * @param service An authenticated CalendarService object.
   * @param userEmail The email address of the user with whom to share the
   *        calendar.
   * @param role The access privileges to grant this user.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static void addAccessControl(CalendarService service,
      String userEmail, AclRole role) throws ServiceException, IOException {
    AclEntry entry = new AclEntry();
    entry.setScope(new AclScope(AclScope.Type.USER, userEmail));
    entry.setRole(role);

    AclEntry insertedEntry = service.insert(aclFeedUrl, entry);

    System.out.println("Added user to access control list:");
    System.out.println("\tScope: Type=" + insertedEntry.getScope().getType()
        + " (" + insertedEntry.getScope().getValue() + ")");
    System.out.println("\tRole: " + insertedEntry.getRole().getValue());
  }

  /**
   * Updates a user to have new access permissions over a calendar. Note that
   * this method will not run by default.
   * 
   * @param service An authenticated CalendarService object.
   * @param userEmail The email address of the user to update.
   * @param newRole The new access privileges to grant this user.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static void updateAccessControl(CalendarService service,
      String userEmail, AclRole newRole) throws ServiceException, IOException {
    AclFeed aclFeed = service.getFeed(aclFeedUrl, AclFeed.class);

    for (AclEntry aclEntry : aclFeed.getEntries()) {
      if (userEmail.equals(aclEntry.getScope().getValue())) {
        aclEntry.setRole(newRole);
        AclEntry updatedEntry = aclEntry.update();

        System.out.println("Updated user's access control:");
        System.out.println("\tScope: Type=" + updatedEntry.getScope().getType()
            + " (" + updatedEntry.getScope().getValue() + ")");
        System.out.println("\tRole: " + updatedEntry.getRole().getValue());

        break;
      }
    }
  }

  /**
   * Deletes a user from a calendar's access control list, preventing that user
   * from accessing the calendar. Note that this method will not run by default.
   * 
   * @param service An authenticated CalendarService object.
   * @param userEmail The email address of the user to remove from the ACL.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static void deleteAccessControl(CalendarService service,
      String userEmail) throws ServiceException, IOException {
    AclFeed aclFeed = service.getFeed(aclFeedUrl, AclFeed.class);

    for (AclEntry aclEntry : aclFeed.getEntries()) {
      if (userEmail.equals(aclEntry.getScope().getValue())) {
        aclEntry.delete();
        System.out.println("Deleted " + userEmail + "'s access control.");

        break;
      }
    }
  }

  /**
   * Instantiates a CalendarService object and uses the command line arguments
   * to authenticate. The CalendarService object is used to demonstrate
   * interactions with the Calendar data API's ACL feed.
   * 
   * @param args Must be length 2 or 3 and contain a valid username/password
   */
  public static void main(String[] args) {
    String userToShareWith = null;

    // Set username, password and feed URI from command-line arguments.
    if (args.length < 2 || args.length > 3) {
      usage();
      return;
    } else if (args.length == 3) {
      userToShareWith = args[2];
    }

    CalendarService myService = new CalendarService("demo-AclFeedDemo-1");
    String userName = args[0];
    String userPassword = args[1];

    // Create necessary URL objects
    try {
      metafeedUrl = new URL(METAFEED_URL_BASE + userName);
      aclFeedUrl = new URL(METAFEED_URL_BASE + userName + ACL_FEED_URL_SUFFIX);
    } catch (MalformedURLException e) {
        // Bad URL
        System.err.println("Uh oh - you've got an invalid URL.");
        e.printStackTrace();
        return;
    }
    
    try {
      myService.setUserCredentials(userName, userPassword);

      // Demonstrate retrieving access control list feeds.
      printAclList(myService);

      if (userToShareWith != null) {
        // Allow given user to see free/busy information on this calendar.
        addAccessControl(myService, userToShareWith, CalendarAclRole.FREEBUSY);

        // Allow given user to have full read access on this calendar.
        updateAccessControl(myService, userToShareWith, CalendarAclRole.READ);

        // Remove given user's access to this calendar.
        deleteAccessControl(myService, userToShareWith);
      }

    } catch (IOException e) {
      // Communications error
      System.err.println("There was a problem communicating with the service.");
      e.printStackTrace();
    } catch (ServiceException e) {
      // Server side error
      System.err.println("The server had a problem handling your request.");
      e.printStackTrace();
    }
  }

  /**
   * Prints the command line usage of this sample application.
   */
  private static void usage() {
    System.out.println("Syntax: AclFeedDemo <username> <password>"
        + " [userToShareWith]");
    System.out.println("\nThe username and password are used for "
        + "authentication.  The 'userToShareWith' is an optional parameter "
        + "that specifies a second user to share the first user's primary "
        + "calendar with.  If this parameter is not given then the first "
        + "user's ACL will not be modified.");
  }
}
