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
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.extensions.*;

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
 *    (5) Creating single-occurrence and recurring events;
 *    (6) Updating events;
 *    (7) Adding reminders and extended properties;
 *    (8) Deleting events.
 */
public class CalendarClient {

  private static String userName, userPassword, feedUri;

  /**
   * Prints a list of all the user's calendars.
   * 
   * @param service An authenticated CalendarService object.
   * @throws Exception If an error occurs during feed retrieval.
   */
  private static void printUserCalendars(CalendarService service)
      throws Exception {
    URL feedUrl = new URL("http://www.google.com/calendar/feeds/default");

    new CalendarFeed().declareExtensions(service.getExtensionProfile());

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
   * @throws Exception If an error occurs during feed retrieval.
   */
  private static void printAllEvents(CalendarService service) throws Exception {
    // Set up the URL and the object that will handle the connection:
    URL feedUrl = new URL(feedUri);

    // Mark the feed as an Event feed:
    new EventFeed().declareExtensions(service.getExtensionProfile());

    // Send the request and receive the response:
    CalendarEventFeed resultFeed = service.getFeed(feedUrl,
        CalendarEventFeed.class);

    System.out.println("All events on your calendar:");
    System.out.println();
    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
      CalendarEventEntry entry = (CalendarEventEntry)
          resultFeed.getEntries().get(i);
      System.out.println("\t" + entry.getTitle().getPlainText());
    }
    System.out.println();
  }

  /**
   * Prints the titles of all events matching a full-text query.
   * 
   * @param service An authenticated CalendarService object.
   * @param query The text for which to query.
   * @throws Exception If an error occurs during feed retrieval.
   */
  private static void fullTextQuery(CalendarService service, String query)
      throws Exception {
    Query myQuery = new Query(new URL(feedUri));
    myQuery.setFullTextQuery("Tennis");

    CalendarEventFeed resultFeed = service.query(myQuery,
        CalendarEventFeed.class);

    System.out.println("Events matching " + query + ":");
    System.out.println();
    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
      CalendarEventEntry entry = (CalendarEventEntry)
          resultFeed.getEntries().get(i);
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
   * @throws Exception If an error occurs during feed retrieval.
   */
  private static void dateRangeQuery(CalendarService service,
      DateTime startTime, DateTime endTime) throws Exception {
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
      CalendarEventEntry entry = (CalendarEventEntry)
          resultFeed.getEntries().get(i);
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
   * @param recurData Recurrence value for the event, or null for
   *                  single-instance events.
   * @return The newly-created CalendarEventEntry.
   * @throws Exception If an error occurs during event insertion.
   */
  private static CalendarEventEntry createEvent(CalendarService service,
      String eventTitle, String recurData) throws Exception {
    URL postUrl = new URL(feedUri);
    CalendarEventEntry myEntry = new CalendarEventEntry();

    myEntry.setTitle(new PlainTextConstruct(eventTitle));
    myEntry.setContent(new PlainTextConstruct("Meet for a quick lesson."));

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
   * @return The newly-created CalendarEventEntry.
   * @throws Exception If an error occurs during event insertion.
   */
  private static CalendarEventEntry createSingleEvent(CalendarService service,
      String eventTitle) throws Exception {
    return createEvent(service, eventTitle, null);
  }

  /**
   * Creates a new recurring event.
   * 
   * @param service An authenticated CalendarService object.
   * @param eventTitle Title of the event to create.
   * @return The newly-created CalendarEventEntry.
   * @throws Exception If an error occurs during event insertion.
   */
  private static CalendarEventEntry createRecurringEvent(CalendarService
      service, String eventTitle) throws Exception {
    // Specify a recurring event that occurs every Tuesday from May 1,
    // 2007 through September 4, 2007.  Note that we are using iCal (RFC 2445)
    // syntax; see http://www.ietf.org/rfc/rfc2445.txt for more information.
    String recurData = "DTSTART;VALUE=DATE:20070501\r\n"
        + "DTEND;VALUE=DATE:20070502\r\n"
        + "RRULE:FREQ=WEEKLY;BYDAY=Tu;UNTIL=20070904\r\n";

    return createEvent(service, eventTitle, recurData);
  }

  /**
   * Updates the title of an existing calendar event.
   * 
   * @param entry The event to update.
   * @param newTitle The new title for this event.
   * @return The updated CalendarEventEntry object.
   * @throws Exception If an error occurs during the event update.
   */
  private static CalendarEventEntry updateTitle(CalendarEventEntry entry,
      String newTitle) throws Exception {
    entry.setTitle(new PlainTextConstruct(newTitle));
    return (CalendarEventEntry)entry.update();
  }

  /**
   * Adds a reminder to a calendar event.
   * 
   * @param entry The event to update.
   * @param numMinutes Reminder time, in minutes.
   * @return The updated EventEntry object.
   * @throws Exception If an error occurs during the event update.
   */
  private static CalendarEventEntry addReminder(CalendarEventEntry entry,
      int numMinutes) throws Exception {
    Reminder reminder = new Reminder();
    reminder.setMinutes(numMinutes);
    entry.getReminder().add(reminder);

    return (CalendarEventEntry)entry.update();
  }

  /**
   * Adds an extended property to a calendar event.
   * 
   * @param entry The event to update.
   * @return The updated EventEntry object.
   * @throws Exception If an error occurs during the event update.
   */
  private static CalendarEventEntry addExtendedProperty(CalendarEventEntry
      entry) throws Exception {
    // Add an extended property "id" with value 1234 to the EventEntry entry.
    // We specify the complete schema URL to avoid namespace collisions with
    // other applications that use the same property name.
    ExtendedProperty property = new ExtendedProperty();
    property.setName("http://www.example.com/schemas/2005#mycal.id");
    property.setValue("1234");

    entry.addExtension(property);

    return (CalendarEventEntry)entry.update();
  }

  public static void main(String[] args) {
    CalendarService myService = new CalendarService("exampleCo-exampleApp-1");
    
    // Set username, password and feed URI from command-line arguments.
    if (args.length != 3) {
      System.err.println("Syntax: CalendarClient <username> <password>"
          + " <feed URI>");
      return;
    }
    
    userName = args[0];
    userPassword = args[1];
    feedUri = args[2];

    try {
      myService.setUserCredentials(userName, userPassword);

      // Demonstrate retrieving a list of the user's calendars.
      printUserCalendars(myService);

      // Demonstrate various feed queries.
      printAllEvents(myService);
      fullTextQuery(myService, "Tennis");
      dateRangeQuery(myService, DateTime.parseDate("2007-01-05"), DateTime
          .parseDate("2007-01-07"));

      // Demonstrate creating a single-occurrence event.
      CalendarEventEntry singleEvent = createSingleEvent(myService,
          "Tennis with Mike");
      System.out.println("Successfully created event "
          + singleEvent.getTitle().getPlainText());

      // Demonstrate creating a recurring event.
      CalendarEventEntry recurringEvent = createRecurringEvent(myService,
          "Tennis with Dan");
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
      recurringEvent.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
