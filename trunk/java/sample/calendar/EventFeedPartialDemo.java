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
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.Who;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

/**
 * Demonstrates partial query and patch capabilities in Calendar Data API by
 * <ul>
 * <li>Retrieving current attendeeStatus for the authenticated user using partial
 *   query and
 * <li>update the attendeeStatus using partial patch.
 * </ul>
 *
 * 
 */
public class EventFeedPartialDemo {

  private static final String CALENDAR_FEEDS_PREFIX =
      "http://www.google.com/calendar/feeds/";

  /** Input stream for reading user input. */
  private static final BufferedReader IN
      = new BufferedReader(new InputStreamReader(System.in));

  /** Output stream to print output. */
  private static final PrintStream OUT = System.out;

  /** Service instance to talk to calendar server */
  private final CalendarService service;

  /** Constructor */
  private EventFeedPartialDemo(CalendarService service) {
    this.service = service;
  }

  /**
   * Displays a menu of the main activities a user can perform.
   */
  private void printMenu() {
    OUT.println("\n");
    OUT.println("Choose one of the following demo options:");
    OUT.println("\t1) Retrieve my next week's events");
    OUT.println("\t2) Update my attendee status for an event");
    OUT.println("\t0) Exit");
    OUT.println("\nEnter Number (0-2): ");
  }

  /**
   * Prints user's attendeeStatus for next week's events.
   *
   * @param uname username whose attendeeStatus is required.
   */
  private void printAttendeeStatus(String uname)
      throws IOException, ServiceException {

    String eventsFeedUrl = CALENDAR_FEEDS_PREFIX
        + uname + "/private/composite";
    String fields = "entry(@gd:etag,id,title,gd:who[@email='" + uname + "'])";

    CalendarQuery partialQuery = new CalendarQuery(new URL(eventsFeedUrl));
    partialQuery.setFields(fields);
    DateTime startTime = DateTime.now();
    partialQuery.setMinimumStartTime(startTime);
    partialQuery.setMaximumStartTime(
        new DateTime(startTime.getValue() + 604800000, startTime.getTzShift()));

    CalendarEventFeed events = service.query(
        partialQuery, CalendarEventFeed.class);
    for (CalendarEventEntry event : events.getEntries()) {
      String eventId = event.getId()
          .substring(event.getId().lastIndexOf("/") + 1);
      String attendeeStatus =
          event.getParticipants().get(0).getAttendeeStatus();
      OUT.println(eventId + ": " + event.getTitle().getPlainText()
          + ": (" + (attendeeStatus != null ? attendeeStatus : "no status")
          + ")");
    }
  }

  /**
   * Updates user's response for a specific event using partial patch.
   *
   * @param uname username whose attendeeStatus need to be updated.
   */
  private void updateAttendeeStatus(String uname)
      throws IOException, ServiceException {
    OUT.println("Enter the id of event to update: ");
    String eventId = IN.readLine();
    OUT.println("Enter event response (1:Yes, 2:No, 3:Maybe)");
    String selection;
    switch(readInt()) {
      case 1:
        selection = Who.AttendeeStatus.EVENT_ACCEPTED;
        break;
      case 2:
        selection = Who.AttendeeStatus.EVENT_DECLINED;
        break;
      case 3:
        selection = Who.AttendeeStatus.EVENT_TENTATIVE;
        break;
      default:
        OUT.println("Invalid selection.");
        return;
    }

    // URL of calendar entry to update.
    String eventEntryUrl = CALENDAR_FEEDS_PREFIX + uname
        + "/private/full/" + eventId;
    // Selection criteria to fetch only the attendee status of specified user.
    String selectAttendee =
        "@gd:etag,title,gd:who[@email='" + uname + "']";

    Query partialQuery = new Query(new URL(eventEntryUrl));
    partialQuery.setFields(selectAttendee);

    CalendarEventEntry event = service.getEntry(partialQuery.getUrl(),
        CalendarEventEntry.class);
    // The participant list will contain exactly one attendee matching
    // above partial query selection criteria.
    event.getParticipants().get(0).setAttendeeStatus(selection);

    // Field selection to update attendeeStatus only.
    String toUpdateFields = "gd:who/gd:attendeeStatus";

    // Make patch request which returns full representation for the event.
    event = service.patch(
        new URL(eventEntryUrl), toUpdateFields, event);

    // Print the updated attendee status.
    OUT.println(event.getTitle().getPlainText() + " updated to: "
        + event.getParticipants().get(0).getAttendeeStatus());
  }

  /** Program entry point */
  public static void main(String args[]) throws IOException, ServiceException {

    // Set username and password from command-line arguments.
    if (args.length != 2) {
      usage();
      return;
    }

    String uname = args[0];
    String upassword = args[1];

    CalendarService myService = new CalendarService(
        "gdata-CalendarPartialDemo");
    myService.setUserCredentials(uname, upassword);

    EventFeedPartialDemo demo = new EventFeedPartialDemo(myService);

    while (true) {
      try {
        demo.printMenu();
        int choice = readInt();

        switch (choice) {
          case 1:
            // Prints out the user's uploaded videos
            demo.printAttendeeStatus(uname);
            break;
          case 2:
            demo.updateAttendeeStatus(uname);
            break;
          case 0:
            System.exit(1);
            break;
        }
      } catch (IOException e) {
        // Communications error
        System.err.println(
            "There was a problem communicating with the service.");
        e.printStackTrace();
      } catch (ServiceException e) {
        // Server side error
        System.err.println("The server had a problem handling your request.");
        e.printStackTrace();
      }
    }

  }

  /**
   * Prints the command line usage of this sample application.
   */
  private static void usage() {
    OUT.println("Syntax: EventFeedPartialDemo <username> <password>");
    OUT.println("\nThe username and password are used for "
        + "authentication.  The sample application will retrieve list of"
        + "user's calendar events and provides option to change event response."
        );
  }

  /**
   * Reads a line of text from the standard input.
   *
   * @throws IOException If unable to read a line from the standard input.
   * @return A line of text read from the standard input.
   */
  private static String readLine() throws IOException {
    return IN.readLine();
  }

  /**
   * Reads a line of text from the standard input and returns the parsed
   * integer representation.
   *
   * @throws IOException If unable to read a line from the standard input.
   * @return An integer read from the standard input.
   */
  private static int readInt() throws IOException {
    String input = readLine();

    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException nfe) {
      return 0;
    }
  }

}
