/* Copyright (c) 2006 Google Inc.
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

import com.google.gdata.client.*;
import com.google.gdata.client.calendar.*;
import com.google.gdata.data.*;
import com.google.gdata.data.acl.*;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Demonstrates basic Calendar Data API operations using the Java client
 * library:
 *
 *    (1) Retrieving the list of all the user's calendars;
 *    (2) Retrieving all events on a single calendar;
 *    (3) Performing a full-text query on a calendar;
 *    (4) Performing a date-range query on a calendar;
 *    (5) Creating a single-occurrence event;
 *    (6) Creating a recurring event;
 *    (7) Creating a quick add event;
 *    (8) Creating a web content event;
 *    (9) Updating events;
 *    (10) Adding reminders and extended properties;
 *    (11) Deleting events;
 *    (12) Retrieving access control lists (ACLs);
 *    (13) Adding users to access control lists;
 *    (14) Updating users on access control lists;
 *    (15) Removing users from access control lists.
 */
public class CalendarClient {

  private static String feedUri, aclFeedUri;

  // The meta-feed URL of all the user's calendars.
  private static final String METAFEED_URL =
      "http://www.google.com/calendar/feeds/default";

  /**
   * Prints a list of all the user's calendars.
   *
   * @param service An authenticated CalendarService object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static void printUserCalendars(CalendarService service)
      throws IOException, ServiceException {
    URL feedUrl = new URL(METAFEED_URL);

    // Send the request and receive the response:
    CalendarFeed resultFeed = service.getFeed(feedUrl, CalendarFeed.class);

    System.out.println("Your calendars:");
    System.out.println();
    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
      CalendarEntry entry = resultFeed.getEntries().get(i);
      System.out.println("\t" + entry.getTitle().getPlainText());
    }
    System.out.println();
  }

  /**
   * Prints the titles of all events on the calendar specified by
   * {@code feedUri}.
   *
   * @param service An authenticated CalendarService object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static void printAllEvents(CalendarService service)
      throws ServiceException, IOException {
    // Set up the URL and the object that will handle the connection:
    URL feedUrl = new URL(feedUri);

    // Send the request and receive the response:
    CalendarEventFeed resultFeed = service.getFeed(feedUrl,
        CalendarEventFeed.class);

    System.out.println("All events on your calendar:");
    System.out.println();
    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
      CalendarEventEntry entry = resultFeed.getEntries().get(i);
      System.out.println("\t" + entry.getTitle().getPlainText());
    }
    System.out.println();
  }

  /**
   * Prints the titles of all events matching a full-text query.
   *
   * @param service An authenticated CalendarService object.
   * @param query The text for which to query.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static void fullTextQuery(CalendarService service, String query)
      throws ServiceException, IOException {
    Query myQuery = new Query(new URL(feedUri));
    myQuery.setFullTextQuery("Tennis");

    CalendarEventFeed resultFeed = service.query(myQuery,
        CalendarEventFeed.class);

    System.out.println("Events matching " + query + ":");
    System.out.println();
    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
      CalendarEventEntry entry = resultFeed.getEntries().get(i);
      System.out.println("\t" + entry.getTitle().getPlainText());
    }
    System.out.println();
  }

  /**
   * Prints the titles of all events in a specified date/time range.
   *
   * @param service An authenticated CalendarService object.
   * @param startTime Start time (inclusive) of events to print.
   * @param endTime End time (exclusive) of events to print.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static void dateRangeQuery(CalendarService service,
      DateTime startTime, DateTime endTime) throws ServiceException,
      IOException {
    URL feedUrl = new URL(feedUri);

    CalendarQuery myQuery = new CalendarQuery(feedUrl);
    myQuery.setMinimumStartTime(startTime);
    myQuery.setMaximumStartTime(endTime);

    // Send the request and receive the response:
    CalendarEventFeed resultFeed = service.query(myQuery,
        CalendarEventFeed.class);

    System.out.println("Events from " + startTime.toString() + " to "
        + endTime.toString() + ":");
    System.out.println();
    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
      CalendarEventEntry entry = resultFeed.getEntries().get(i);
      System.out.println("\t" + entry.getTitle().getPlainText());
    }
    System.out.println();
  }

  /**
   * Helper method to create either single-instance or recurring events.
   * For simplicity, some values that might normally be passed as parameters
   * (such as author name, email, etc.) are hard-coded.
   *
   * @param service An authenticated CalendarService object.
   * @param eventTitle Title of the event to create.
   * @param eventContent Text content of the event to create.
   * @param recurData Recurrence value for the event, or null for
   * single-instance events.
   * @param isQuickAdd True if eventContent should be interpreted as the
   * text of a quick add event.
   * @param wc A WebContent object, or null if this is not a web content event.
   * @return The newly-created CalendarEventEntry.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static CalendarEventEntry createEvent(CalendarService service,
      String eventTitle, String eventContent, String recurData,
      boolean isQuickAdd, WebContent wc)
      throws ServiceException, IOException {
    URL postUrl = new URL(feedUri);
    CalendarEventEntry myEntry = new CalendarEventEntry();

    myEntry.setTitle(new PlainTextConstruct(eventTitle));
    myEntry.setContent(new PlainTextConstruct(eventContent));
    myEntry.setQuickAdd(isQuickAdd);
    myEntry.setWebContent(wc);

    Person author = new Person("Jo March", null, "jo@gmail.com");
    myEntry.getAuthors().add(author);

    // If a recurrence was requested, add it. Otherwise, set the
    // time (the current date and time) and duration (30 minutes)
    // of the event.
    if (recurData == null) {
      Calendar calendar = new GregorianCalendar();
      DateTime startTime = new DateTime(calendar.getTime(),
          TimeZone.getDefault());

      calendar.add(Calendar.MINUTE, 30);
      DateTime endTime = new DateTime(calendar.getTime(),
          TimeZone.getDefault());

      When eventTimes = new When();
      eventTimes.setStartTime(startTime);
      eventTimes.setEndTime(endTime);
      myEntry.addTime(eventTimes);
    } else {
      Recurrence recur = new Recurrence();
      recur.setValue(recurData);
      myEntry.setRecurrence(recur);
    }

    // Send the request and receive the response:
    return service.insert(postUrl, myEntry);
  }

  /**
   * Creates a single-occurrence event.
   *
   * @param service An authenticated CalendarService object.
   * @param eventTitle Title of the event to create.
   * @param eventContent Text content of the event to create.
   * @return The newly-created CalendarEventEntry.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static CalendarEventEntry createSingleEvent(CalendarService service,
      String eventTitle, String eventContent)
      throws ServiceException, IOException {
    return createEvent(service, eventTitle, eventContent, null, false, null);
  }

  /**
   * Creates a quick add event.
   *
   * @param service An authenticated CalendarService object.
   * @param quickAddContent The quick add text, including the event
   * title, date and time.
   * @return The newly-created CalendarEventEntry.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static CalendarEventEntry createQuickAddEvent(CalendarService service,
      String quickAddContent)
      throws ServiceException, IOException {
    return createEvent(service, null, quickAddContent, null, true, null);
  }

  /**
   * Creates a web content event.
   *
   * @param service An authenticated CalendarService object.
   * @param title The title of the web content event.
   * @param type The MIME type of the web content event, e.g. "image/gif"
   * @param url The URL of the content to display in the web content window.
   * @param icon The icon to display in the main Calendar user interface.
   * @param width The width of the web content window.
   * @param height The height of the web content window.
   * @return The newly-created CalendarEventEntry.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static CalendarEventEntry createWebContentEvent(CalendarService
      service, String title, String type, String url, String icon,
      String width, String height) throws ServiceException, IOException {
    WebContent wc = new WebContent();

    wc.setHeight(height);
    wc.setWidth(width);
    wc.setTitle(title);
    wc.setType(type);
    wc.setUrl(url);
    wc.setIcon(icon);

    return createEvent(service, title, null, null, false, wc);
  }

  /**
   * Creates a new recurring event.
   *
   * @param service An authenticated CalendarService object.
   * @param eventTitle Title of the event to create.
   * @param eventContent Text content of the event to create.
   * @return The newly-created CalendarEventEntry.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static CalendarEventEntry createRecurringEvent(CalendarService
      service, String eventTitle, String eventContent)
      throws ServiceException, IOException {
    // Specify a recurring event that occurs every Tuesday from May 1,
    // 2007 through September 4, 2007.  Note that we are using iCal (RFC 2445)
    // syntax; see http://www.ietf.org/rfc/rfc2445.txt for more information.
    String recurData = "DTSTART;VALUE=DATE:20070501\r\n"
        + "DTEND;VALUE=DATE:20070502\r\n"
        + "RRULE:FREQ=WEEKLY;BYDAY=Tu;UNTIL=20070904\r\n";

    return createEvent(service, eventTitle, eventContent, recurData, false,
        null);
  }

  /**
   * Updates the title of an existing calendar event.
   *
   * @param entry The event to update.
   * @param newTitle The new title for this event.
   * @return The updated CalendarEventEntry object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static CalendarEventEntry updateTitle(CalendarEventEntry entry,
      String newTitle) throws ServiceException, IOException {
    entry.setTitle(new PlainTextConstruct(newTitle));
    return entry.update();
  }

  /**
   * Adds a reminder to a calendar event.
   *
   * @param entry The event to update.
   * @param numMinutes Reminder time, in minutes.
   * @return The updated EventEntry object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static CalendarEventEntry addReminder(CalendarEventEntry entry,
      int numMinutes) throws ServiceException, IOException {
    Reminder reminder = new Reminder();
    reminder.setMinutes(numMinutes);
    entry.getReminder().add(reminder);

    return entry.update();
  }

  /**
   * Adds an extended property to a calendar event.
   *
   * @param entry The event to update.
   * @return The updated EventEntry object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static CalendarEventEntry addExtendedProperty(CalendarEventEntry
      entry) throws ServiceException, IOException {
    // Add an extended property "id" with value 1234 to the EventEntry entry.
    // We specify the complete schema URL to avoid namespace collisions with
    // other applications that use the same property name.
    ExtendedProperty property = new ExtendedProperty();
    property.setName("http://www.example.com/schemas/2005#mycal.id");
    property.setValue("1234");

    entry.addExtension(property);

    return entry.update();
  }

  /**
   * Prints the access control lists for each of the user's calendars.
   *
   * @param service An authenticated CalendarService object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static void printAclList(CalendarService service) throws
      ServiceException, IOException {
    CalendarFeed calendarFeed =
        service.getFeed(new URL(METAFEED_URL), CalendarFeed.class);

    // After accessing the meta-feed, get the ACL link for each calendar.
    System.out.println("Access control lists for your calendars:");
    for (CalendarEntry calEntry : calendarFeed.getEntries()) {
      Link link = calEntry.getLink(AclNamespace.LINK_REL_ACCESS_CONTROL_LIST,
          Link.Type.ATOM);

      // For each calendar that exposes an access control list, retrieve its ACL
      // feed.  If link is null, then we are not the owner of that calendar
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
   * Adds a user in the read-only role to the calendar's access control
   * list.  Note that this method will not run by default.
   *
   * @param service An authenticated CalendarService object.
   * @param userEmail The email address of the user with whom to share
   * the calendar.
   * @param role The access privileges to grant this user.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static void addAccessControl(CalendarService service,
      String userEmail, AclRole role) throws ServiceException,
      IOException {
    AclEntry entry = new AclEntry();
    entry.setScope(new AclScope(AclScope.Type.USER, userEmail));
    entry.setRole(role);

    URL url = new URL(aclFeedUri);

    AclEntry insertedEntry = service.insert(url, entry);

    System.out.println("Added user to access control list:");
    System.out.println("\tScope: Type=" + insertedEntry.getScope().getType() +
        " (" + insertedEntry.getScope().getValue() + ")");
    System.out.println("\tRole: " + insertedEntry.getRole().getValue());
  }

  /**
   * Updates a user to have new access permissions over a calendar.
   * Note that this method will not run by default.
   *
   * @param service An authenticated CalendarService object.
   * @param userEmail The email address of the user to update.
   * @param newRole The new access privileges to grant this user.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static void updateAccessControl(CalendarService service,
      String userEmail, AclRole newRole) throws ServiceException,
      IOException {
    URL url = new URL(aclFeedUri);
    AclFeed aclFeed = service.getFeed(url, AclFeed.class);

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
   * Deletes a user from a calendar's access control list, preventing
   * that user from accessing the calendar.  Note that this method will
   * not run by default.
   * 
   * @param service An authenticated CalendarService object.
   * @param userEmail The email address of the user to remove from the ACL.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  private static void deleteAccessControl(CalendarService service,
      String userEmail) throws ServiceException, IOException {
    URL url = new URL(aclFeedUri);
    AclFeed aclFeed = service.getFeed(url, AclFeed.class);

    for (AclEntry aclEntry : aclFeed.getEntries()) {
      if (userEmail.equals(aclEntry.getScope().getValue())) {
        aclEntry.delete();
        System.out.println("Deleted " + userEmail + "'s access control.");

        break;
      }
    }
  }

  public static void main(String[] args) {
    CalendarService myService = new CalendarService("exampleCo-exampleApp-1");

    // Set username, password and feed URI from command-line arguments.
    if (args.length != 4) {
      System.err.println("Syntax: CalendarClient <username> <password>"
          + " <calendar feed URI> <ACL feed URI>");
      return;
    }

    String userName = args[0];
    String userPassword = args[1];
    feedUri = args[2];
    aclFeedUri = args[3];

    try {
      myService.setUserCredentials(userName, userPassword);

      // Demonstrate retrieving a list of the user's calendars.
      printUserCalendars(myService);

      // Demonstrate various feed queries.
      System.out.println("Printing all events");
      printAllEvents(myService);
      System.out.println("Full text query");
      fullTextQuery(myService, "Tennis");
      dateRangeQuery(myService, DateTime.parseDate("2007-01-05"), DateTime
          .parseDate("2007-01-07"));

      // Demonstrate creating a single-occurrence event.
      CalendarEventEntry singleEvent = createSingleEvent(myService,
          "Tennis with Mike", "Meet for a quick lesson.");
      System.out.println("Successfully created event "
          + singleEvent.getTitle().getPlainText());

      // Demonstrate creating a quick add event.
      CalendarEventEntry quickAddEvent = createQuickAddEvent(myService,
          "Tennis with John April 11 3pm-3:30pm");
      System.out.println("Successfully created quick add event "
          + quickAddEvent.getTitle().getPlainText());

      // Demonstrate creating a web content event.
      CalendarEventEntry webContentEvent = createWebContentEvent(myService,
          "World Cup", "image/gif", "http://www.google.com/logos/worldcup06.gif",
          "http://www.google.com/calendar/images/google-holiday.gif", "276",
          "120");
      System.out.println("Successfully created web content event "
          + webContentEvent.getTitle().getPlainText());

      // Demonstrate creating a recurring event.
      CalendarEventEntry recurringEvent = createRecurringEvent(myService,
          "Tennis with Dan", "Weekly tennis lesson.");
      System.out.println("Successfully created recurring event "
          + recurringEvent.getTitle().getPlainText());

      // Demonstrate updating the event's text.
      singleEvent = updateTitle(singleEvent, "Important meeting");
      System.out.println("Event's new title is \""
          + singleEvent.getTitle().getPlainText() + "\".");

      // Demonstrate adding a reminder. Note that this will only work on a
      // primary calendar.
      singleEvent = addReminder(singleEvent, 15);
      System.out.println("Set a "
          + singleEvent.getReminder().get(0).getMinutes()
          + " minute reminder for the event.");

      // Demonstrate adding an extended property.
      singleEvent = addExtendedProperty(singleEvent);

      // Demonstrate deleting the entries.
      singleEvent.delete();
      quickAddEvent.delete();
      webContentEvent.delete();
      recurringEvent.delete();

      // Demonstrate retrieving access control list feeds.
      printAclList(myService);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
