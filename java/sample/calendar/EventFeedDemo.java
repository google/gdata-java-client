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

import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.Link;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.WebContent;
import com.google.gdata.data.extensions.ExtendedProperty;
import com.google.gdata.data.extensions.Recurrence;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.data.extensions.Reminder.Method;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Demonstrates basic Calendar Data API operations on the event feed using the
 * Java client library:
 * 
 * <ul>
 * <li>Retrieving the list of all the user's calendars</li>
 * <li>Retrieving all events on a single calendar</li>
 * <li>Performing a full-text query on a calendar</li>
 * <li>Performing a date-range query on a calendar</li>
 * <li>Creating a single-occurrence event</li>
 * <li>Creating a recurring event</li>
 * <li>Creating a quick add event</li>
 * <li>Creating a web content event</li>
 * <li>Updating events</li>
 * <li>Adding reminders and extended properties</li>
 * <li>Deleting events via batch request</li>
 * </ul>
 */
public class EventFeedDemo {

  // The base URL for a user's calendar metafeed (needs a username appended).
  private static final String METAFEED_URL_BASE = 
      "https://www.google.com/calendar/feeds/";

  // The string to add to the user's metafeedUrl to access the event feed for
  // their primary calendar.
  private static final String EVENT_FEED_URL_SUFFIX = "/private/full";

  // The URL for the metafeed of the specified user.
  // (e.g. http://www.google.com/feeds/calendar/jdoe@gmail.com)
  private static URL metafeedUrl = null;

  // The URL for the event feed of the specified user's primary calendar.
  // (e.g. http://www.googe.com/feeds/calendar/jdoe@gmail.com/private/full)
  private static URL eventFeedUrl = null;

  /**
   * Prints a list of all the user's calendars.
   * 
   * @param service An authenticated CalendarService object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server
   */
  private static void printUserCalendars(CalendarService service)
      throws IOException, ServiceException {
    // Send the request and receive the response:
    CalendarFeed resultFeed = service.getFeed(metafeedUrl, CalendarFeed.class);

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
   * @throws IOException Error communicating with the server.
   */
  private static void printAllEvents(CalendarService service)
      throws ServiceException, IOException {
    // Send the request and receive the response:
    CalendarEventFeed resultFeed = service.getFeed(eventFeedUrl,
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
   * @throws IOException Error communicating with the server.
   */
  private static void fullTextQuery(CalendarService service, String query)
      throws ServiceException, IOException {
    Query myQuery = new Query(eventFeedUrl);
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
   * @throws IOException Error communicating with the server.
   */
  private static void dateRangeQuery(CalendarService service,
      DateTime startTime, DateTime endTime) throws ServiceException,
      IOException {
    CalendarQuery myQuery = new CalendarQuery(eventFeedUrl);
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
   * Helper method to create either single-instance or recurring events. For
   * simplicity, some values that might normally be passed as parameters (such
   * as author name, email, etc.) are hard-coded.
   * 
   * @param service An authenticated CalendarService object.
   * @param eventTitle Title of the event to create.
   * @param eventContent Text content of the event to create.
   * @param recurData Recurrence value for the event, or null for
   *        single-instance events.
   * @param isQuickAdd True if eventContent should be interpreted as the text of
   *        a quick add event.
   * @param wc A WebContent object, or null if this is not a web content event.
   * @return The newly-created CalendarEventEntry.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static CalendarEventEntry createEvent(CalendarService service,
      String eventTitle, String eventContent, String recurData,
      boolean isQuickAdd, WebContent wc) throws ServiceException, IOException {
    CalendarEventEntry myEntry = new CalendarEventEntry();

    myEntry.setTitle(new PlainTextConstruct(eventTitle));
    myEntry.setContent(new PlainTextConstruct(eventContent));
    myEntry.setQuickAdd(isQuickAdd);
    myEntry.setWebContent(wc);

    // If a recurrence was requested, add it. Otherwise, set the
    // time (the current date and time) and duration (30 minutes)
    // of the event.
    if (recurData == null) {
      Calendar calendar = new GregorianCalendar();
      DateTime startTime = new DateTime(calendar.getTime(), TimeZone
          .getDefault());

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
    return service.insert(eventFeedUrl, myEntry);
  }

  /**
   * Creates a single-occurrence event.
   * 
   * @param service An authenticated CalendarService object.
   * @param eventTitle Title of the event to create.
   * @param eventContent Text content of the event to create.
   * @return The newly-created CalendarEventEntry.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static CalendarEventEntry createSingleEvent(CalendarService service,
      String eventTitle, String eventContent) throws ServiceException,
      IOException {
    return createEvent(service, eventTitle, eventContent, null, false, null);
  }

  /**
   * Creates a quick add event.
   * 
   * @param service An authenticated CalendarService object.
   * @param quickAddContent The quick add text, including the event title, date
   *        and time.
   * @return The newly-created CalendarEventEntry.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static CalendarEventEntry createQuickAddEvent(
      CalendarService service, String quickAddContent) throws ServiceException,
      IOException {
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
   * @throws IOException Error communicating with the server.
   */
  private static CalendarEventEntry createWebContentEvent(
      CalendarService service, String title, String type, String url,
      String icon, String width, String height) throws ServiceException,
      IOException {
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
   * @throws IOException Error communicating with the server.
   */
  private static CalendarEventEntry createRecurringEvent(
      CalendarService service, String eventTitle, String eventContent)
      throws ServiceException, IOException {
    // Specify a recurring event that occurs every Tuesday from May 1,
    // 2007 through September 4, 2007. Note that we are using iCal (RFC 2445)
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
   * @throws IOException Error communicating with the server.
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
   * @param methodType Method of notification (e.g. email, alert, sms).
   * @return The updated EventEntry object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static CalendarEventEntry addReminder(CalendarEventEntry entry,
      int numMinutes, Method methodType) throws ServiceException, IOException {
    Reminder reminder = new Reminder();
    reminder.setMinutes(numMinutes);
    reminder.setMethod(methodType);
    entry.getReminder().add(reminder);
   
    return entry.update();
  }

  /**
   * Adds an extended property to a calendar event.
   * 
   * @param entry The event to update.
   * @return The updated EventEntry object.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static CalendarEventEntry addExtendedProperty(
      CalendarEventEntry entry) throws ServiceException, IOException {
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
   * Makes a batch request to delete all the events in the given list. If any of
   * the operations fails, the errors returned from the server are displayed.
   * The CalendarEntry objects in the list given as a parameters must be entries
   * returned from the server that contain valid edit links (for optimistic
   * concurrency to work). Note: You can add entries to a batch request for the
   * other operation types (INSERT, QUERY, and UPDATE) in the same manner as
   * shown below for DELETE operations.
   * 
   * @param service An authenticated CalendarService object.
   * @param eventsToDelete A list of CalendarEventEntry objects to delete.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
  private static void deleteEvents(CalendarService service,
      List<CalendarEventEntry> eventsToDelete) throws ServiceException,
      IOException {

    // Add each item in eventsToDelete to the batch request.
    CalendarEventFeed batchRequest = new CalendarEventFeed();
    for (int i = 0; i < eventsToDelete.size(); i++) {
      CalendarEventEntry toDelete = eventsToDelete.get(i);
      // Modify the entry toDelete with batch ID and operation type.
      BatchUtils.setBatchId(toDelete, String.valueOf(i));
      BatchUtils.setBatchOperationType(toDelete, BatchOperationType.DELETE);
      batchRequest.getEntries().add(toDelete);
    }

    // Get the URL to make batch requests to
    CalendarEventFeed feed = service.getFeed(eventFeedUrl,
        CalendarEventFeed.class);
    Link batchLink = feed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
    URL batchUrl = new URL(batchLink.getHref());

    // Submit the batch request
    CalendarEventFeed batchResponse = service.batch(batchUrl, batchRequest);

    // Ensure that all the operations were successful.
    boolean isSuccess = true;
    for (CalendarEventEntry entry : batchResponse.getEntries()) {
      String batchId = BatchUtils.getBatchId(entry);
      if (!BatchUtils.isSuccess(entry)) {
        isSuccess = false;
        BatchStatus status = BatchUtils.getBatchStatus(entry);
        System.out.println("\n" + batchId + " failed (" + status.getReason()
            + ") " + status.getContent());
      }
    }
    if (isSuccess) {
      System.out.println("Successfully deleted all events via batch request.");
    }
  }

  /**
   * Instantiates a CalendarService object and uses the command line arguments
   * to authenticate. The CalendarService object is used to demonstrate
   * interactions with the Calendar data API's event feed.
   * 
   * @param args Must be length 2 and contain a valid username/password
   */
  public static void main(String[] args) {
    CalendarService myService = new CalendarService("exampleCo-exampleApp-1");

    // Set username and password from command-line arguments.
    if (args.length != 2) {
      usage();
      return;
    }

    String userName = args[0];
    String userPassword = args[1];

    // Create the necessary URL objects.
    try {
      metafeedUrl = new URL(METAFEED_URL_BASE + userName);
      eventFeedUrl = new URL(METAFEED_URL_BASE + userName
          + EVENT_FEED_URL_SUFFIX);
    } catch (MalformedURLException e) {
      // Bad URL
      System.err.println("Uh oh - you've got an invalid URL.");
      e.printStackTrace();
      return;
    }

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
          "World Cup", "image/gif",
          "http://www.google.com/logos/worldcup06.gif",
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
      singleEvent = addReminder(singleEvent, 15, Method.EMAIL);
      System.out.println("Set a "
          + singleEvent.getReminder().get(0).getMinutes()
          + " minute " + singleEvent.getReminder().get(0).getMethod()
          + " reminder for the event.");

      // Demonstrate adding an extended property.
      singleEvent = addExtendedProperty(singleEvent);

      // Demonstrate deleting the entries with a batch request.
      List<CalendarEventEntry> eventsToDelete = 
          new ArrayList<CalendarEventEntry>();
      eventsToDelete.add(singleEvent);
      eventsToDelete.add(quickAddEvent);
      eventsToDelete.add(webContentEvent);
      eventsToDelete.add(recurringEvent);
      deleteEvents(myService, eventsToDelete);

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
    System.out.println("Syntax: EventFeedDemo <username> <password>");
    System.out.println("\nThe username and password are used for "
        + "authentication.  The sample application will modify the specified "
        + "user's calendars so you may want to use a test account.");
  }
}
