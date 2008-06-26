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
package mashups.eventpub;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

import com.google.api.gbase.client.DateTimeRange;
import com.google.api.gbase.client.FeedURLFactory;
import com.google.api.gbase.client.GoogleBaseAttributesExtension;
import com.google.api.gbase.client.GoogleBaseEntry;
import com.google.api.gbase.client.GoogleBaseService;
import com.google.gdata.util.ServiceException;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.httputil.FastURLEncoder;


/**
 * Publisher for pushing events from Spreadsheets to Calendar and Base
 *
 * 
 */ 
public class EventPublisher {

  /**
   * The URL for the Spreadsheets meta feed, containing entries representing
   * individual spreadsheets accessible to the authenticated user
   */ 
  private static final String SPREADSHEETS_META_FEED =  
      "http://spreadsheets.google.com/feeds/spreadsheets/private/full";

  /**
   * The URL for the Spreadsheets feed scope, as passed to the AuthSub 
   * service in the AuthSubRequest URL scope parameter. 
   */ 
  private static final String SPREADSHEETS_SCOPE =  
      "http://spreadsheets.google.com/feeds/";

  /**
   * The app identity string used for the 'source' required by the
   * ClientLogin service.  The GData Java Client Library also sends this value
   * to the GData services as part of the <code>User-Agent</code> HTTP header.
   */
  private static final String APP_IDENTITY = "google-mashups-EventPublisher";

  /**
   * The authenticated CalendarService object used for publishing
   */
  private CalendarService calService = null;

  /**
   * The authenticated CalendarService object used for publishing
   */
  private GoogleBaseService baseService = null;

  /**
   * The authenticated SpreadsheetService object used for retrieving events 
   */
  private SpreadsheetService ssService = null;
 
  /**
   * URL representing the Google Spreadsheets list feed from which the events 
   * are published. Note, this feed is not the meta feed of spreadsheets.  It
   * is the list feed containing entries representing each row of the 
   * spreadsheet.
   */ 
  private URL ssUrl = null;

  /* Spreadsheets credentials for authentication */
  private String ssUsername = null;
  private String ssPassword = null;
  private String ssAuthSubToken = null;

  /**
   * URL representing the Google Calendar feed to which events can be 
   * published
   */ 
  private URL calUrl = null;

  /* Calendar credentials for authentication */ 
  private String calUsername = null;
  private String calPassword = null;
 
  /* Google Base credentials for authentication */ 
  private String baseUsername = null;
  private String basePassword = null;
 
  /**
   * Contains the mapping between field names used by the 
   * spreadsheet and those required in Calendar and Base
   */
  private SpreadsheetCustomFieldMap fieldMap = null; 
  
  public void setFieldMap(SpreadsheetCustomFieldMap fieldMap) {
    this.fieldMap = fieldMap;
  }
  
  public void setSsUrl(String ssUrlText) throws MalformedURLException {
    this.ssUrl = new URL(ssUrlText);
  }
  
  public void setSsUsernamePassword(String username, String password) {
    this.ssUsername = username;
    this.ssPassword = password;
  }

  public void setCalUsernamePassword(String username, String password) {
    this.calUsername = username;
    this.calPassword = password;
  }

  public void setBaseUsernamePassword(String username, String password) {
    this.baseUsername = username;
    this.basePassword = password;
  }

  public void setCalUrl(String calUrl) throws MalformedURLException {
    this.calUrl = new URL(calUrl);
  }
  
  public String getSsAuthSubToken() {
    return this.ssAuthSubToken;
  }
 
  /**
   * Sets the AuthSub token used for Google Spreadsheets data API auth.
   * The method can, optionally, exchange the token for a session token
   * should a session token be desired.
   * 
   * @param token The AuthSub token (either a a single-use or session token)
   * @param exchange True if the token supplied is a single-use token and
   * should be exchanged for a session token.
   * @throws EPAuthenticationException
   */ 
  public void setSsAuthSubToken(String token, boolean exchange) 
      throws EPAuthenticationException {
    if (exchange) {
      try {
        this.ssAuthSubToken = AuthSubUtil.exchangeForSessionToken(token, null);
      } catch (com.google.gdata.util.AuthenticationException authEx) {
        throw new 
          EPAuthenticationException(
            "Single use token could not be exchanged", authEx);
      } catch (IOException ex) {
        throw new 
          EPAuthenticationException(
            "Single use token could not be exchanged", ex);
      } catch (GeneralSecurityException ex) {
        throw new 
          EPAuthenticationException(
            "Single use token could not be exchanged", ex);
      }      
    } else {
      this.ssAuthSubToken = token;
    }
  }
 
  /**
   * Retrieves events from the spreadsheet and creates a <code>List</code>
   * of <code>Event</code> instances representing the events.
   *
   * @throws EPAuthenticatonException
   */
  public List<Event> getEventsFromSpreadsheet() 
      throws EPAuthenticationException {
    List<Event> eventList = new LinkedList<Event>();
    List<ListEntry> ssEntryList = getSsEntryListHelper();
    // counter used to store row number in event
    int i = 1;
    for (ListEntry ssRow : ssEntryList) {
      i++;
      // CustomElementCollection represents elements in the gsx namespace
      CustomElementCollection elements = ssRow.getCustomElements();
      try {
        Event e = new Event(
          elements.getValue(fieldMap.getTitleColumn()),
          elements.getValue(fieldMap.getDescriptionColumn()),
          elements.getValue(fieldMap.getWebsiteColumn()),
          elements.getValue(fieldMap.getStartColumn()),
          elements.getValue(fieldMap.getEndColumn()),
          elements.getValue(fieldMap.getCalendarUrlColumn()),
          elements.getValue(fieldMap.getBaseUrlColumn()));
        e.ssRow = i;
        e.setSsEditUrl(ssRow.getEditLink().getHref());
        eventList.add(e);
      } catch (MalformedURLException urlEx) {
        System.err.println("Could not read event titled '" +
            elements.getValue(fieldMap.getTitleColumn()) +
            "' due to a bad URL for the Calendar or Base URL");
      }
    }
    return eventList;
  }
 
  /**
   * Retrieves a list of Spreadsheets for the authenticated user
   *
   * @return Returns a list of HashMaps with meta-data about each spreadsheet. 
   * @throws EPAuthenticationException
   */ 
  public List<HashMap> getSsList() 
    throws EPAuthenticationException {
    List<HashMap> returnList = new LinkedList<HashMap>();
    List<SpreadsheetEntry> ssList = getSsListHelper();
    for (SpreadsheetEntry ssEntry : ssList) {
      HashMap<String, String> hm = new HashMap<String, String>();
      hm.put("title", ssEntry.getTitle().getPlainText());
      try {
        hm.put("wsFeed", FastURLEncoder.encode(
            ssEntry.getWorksheetFeedUrl().toString(),
        "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        System.err.println("Encoding error: " + e.getMessage());
      }
      returnList.add(hm);
    }
    return returnList;
  }

  /**
   * Retrieves a list of Worksheets for the specified 
   * spreadsheet 
   *
   * @param wsFeedUrl The URL for the feed containing the list of worksheets
   * @return A <code>List</code> of <code>HashMap</code> meta data about each 
   * worksheet
   * @throws EPAuthenticationException
   */
  public List<HashMap> getWsList(String wsFeedUrl)
  throws EPAuthenticationException {
    List<HashMap> returnList = new LinkedList<HashMap>();
    List<WorksheetEntry> wsList = getWsListHelper(wsFeedUrl);
    for (WorksheetEntry wsEntry : wsList) {
      HashMap<String, String> hm = new HashMap<String, String>();
      hm.put("title", wsEntry.getTitle().getPlainText());
      try {
        hm.put("cellFeed", FastURLEncoder.encode(
            wsEntry.getCellFeedUrl().toString(),
            "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        System.err.println("Encoding error: " + e.getMessage());
      }
      returnList.add(hm);
    }
    return returnList;
  }
 
  /**
   * Returns the URL for redirecting users to in order to authenticate to 
   * the AuthSub service for access to a Google Spreadsheets account via 
   * the API.
   *
   * @param nextUrl The URL to which the authenticated user will be redirected
   * to after successfully authenticating
   */
  public static String getSsAuthSubUrl(String nextUrl) {
    // requests a single-use token which can be upgraded to a session token
    // and is not set with the AuthSub secure flag
    return AuthSubUtil.getRequestUrl(nextUrl,
        SPREADSHEETS_SCOPE,
        false,
        true);
  }

  /**
   * Returns a <code>SpreadsheetService</code> object representing the
   * set APP_IDENTITY and credentials.  If both an AuthSub token and 
   * ClientLogin (username/password) credentials are set, the AuthSub
   * token takes precedence for authentication
   *
   * @return an authentication <code>SpreadsheetService</code> instance
   * @throws EPAuthenticationException
   */
  private SpreadsheetService getSsService() 
    throws EPAuthenticationException {
    if (this.ssService == null) { 
      SpreadsheetService ssService = new SpreadsheetService(APP_IDENTITY);
      try {
        if (ssAuthSubToken != null) {
          ssService.setAuthSubToken(ssAuthSubToken);
        } else if (ssUsername != null && ssPassword != null) {
          ssService.setUserCredentials(ssUsername, ssPassword);
        }
      } catch (com.google.gdata.util.AuthenticationException authEx) {
        throw new EPAuthenticationException("Bad spreadsheets credentials");
      }
      this.ssService = ssService;
      return ssService;
    } else {
      return this.ssService;
    }
  }
  
  /**
   * Returns a <code>CalendarService</code> object representing the
   * set APP_IDENTITY and credentials.  If both an AuthSub token and 
   * ClientLogin (username/password) credentials are set, the AuthSub
   * token takes precedence for authentication
   *
   * @return an authentication <code>CalendarService</code> instance
   * @throws EPAuthenticationException
   */
  private CalendarService getCalService() 
      throws EPAuthenticationException {
    if (this.calService == null) { 
      CalendarService calService = new CalendarService(APP_IDENTITY);
      try {
        calService.setUserCredentials(calUsername, calPassword);
      } catch (com.google.gdata.util.AuthenticationException authEx) {
        throw new EPAuthenticationException("Bad calendar credentials");
      }
      this.calService = calService;
      return calService;
    } else {
      return this.calService;
    }
  }
  
  /**
   * Returns a <code>BaseService</code> object representing the
   * set APP_IDENTITY and credentials.  If both an AuthSub token and 
   * ClientLogin (username/password) credentials are set, the AuthSub
   * token takes precedence for authentication
   *
   * @return an authentication <code>BaseService</code> instance
   * @throws EPAuthenticationException
   */
  private GoogleBaseService getBaseService() 
      throws EPAuthenticationException {
    if (this.baseService == null) { 
      GoogleBaseService baseService = new GoogleBaseService(APP_IDENTITY,"none");
      try {
        baseService.setUserCredentials(baseUsername, basePassword);
      } catch (com.google.gdata.util.AuthenticationException authEx) {
        throw new EPAuthenticationException("Bad calendar credentials");
      }
      this.baseService = baseService;
      return baseService;
    } else {
      return this.baseService;
    }
  }
 
  /**
   * Takes the event objects passed and publishes each of them to Google Base
   *
   * @param eventList The list of <code>Event</code>s to publish
   */ 
  public void publishEventsToBase(List<Event> eventList) {
    Iterator<Event> i = eventList.iterator();
    while (i.hasNext()) {
      Event event = i.next();
      try {
        publishEventToBase(event);
      } catch (EPAuthenticationException e) {
        System.err.println("Authentication problem when publishing events: " +
            e.getMessage());
      } catch (IOException e) {
        System.err.println("IOException when publishing events: " +
            e.getMessage());
      } catch (ServiceException e) {
        e.printStackTrace();
        System.err.println("ServiceException when publishing events: " +
            e.getMessage());
      }
    }
  }
  
  /**
   * Takes the event objects passed and publishes each of them to Calendar
   *
   * @param eventList The list of <code>Event</code>s to publish
   */ 
  public void publishEventsToCalendar(List<Event> eventList) {
    Iterator<Event> i = eventList.iterator();
    while (i.hasNext()) {
      Event event = i.next();
      try {
        publishEventToCalendar(event);
      } catch (EPAuthenticationException e) {
        System.err.println("Authentication problem when publishing events: " +
            e.getMessage());
      } catch (IOException e) {
        System.err.println("IOException when publishing events: " +
            e.getMessage());
      } catch (ServiceException e) {
        e.printStackTrace();
        System.err.println("ServiceException when publishing events: " +
            e.getMessage());
      }
    }
  }

  /**
   * Publishes an individual event to Calendar 
   *
   * @param event The <code>Event</code> to publish
   * @throws EPAuthenticationException 
   * @throws IOException 
   * @throws ServiceException
   */ 
  private void publishEventToCalendar(Event event)
  throws EPAuthenticationException, IOException, ServiceException {
    CalendarService calService = getCalService();
    CalendarEventEntry entry = null;
    if (event.getCalendarUrl() != null) {
      // updating event
      entry = calService.getEntry(event.getCalendarUrl(), 
          CalendarEventEntry.class);
    } else {
      // publishing new event
      entry = new CalendarEventEntry();   
    }

    // set data on event
    entry.setTitle(new PlainTextConstruct(event.getTitle()));
    entry.setContent(new PlainTextConstruct(event.getDescription()));
    When when = new When();
    DateTime startDateTime = new DateTime(event.getStartDate());
    startDateTime.setDateOnly(true);

    // we must add 1 day to the event as the end-date is exclusive
    Calendar endDateCal = new GregorianCalendar();
    endDateCal.setTime(event.getEndDate());
    endDateCal.add(Calendar.DATE, 1); 
    DateTime endDateTime = new DateTime(endDateCal.getTime());
    endDateTime.setDateOnly(true);

    when.setStartTime(startDateTime);
    when.setEndTime(endDateTime);
    entry.getTimes().add(when);
    if (event.getCalendarUrl() != null) {
      // updating event
      entry.update();  
    } else {
      // insert event
      CalendarEventEntry resultEntry = calService.insert(calUrl, entry);
      updateSsEventEditUrl(event.getSsEditUrl(), 
          resultEntry.getEditLink().getHref(),
          null);
    }
  }

  /**
   * Publishes an individual event to Base 
   *
   * @param event The <code>Event</code> to publish
   * @throws EPAuthenticationException 
   * @throws IOException 
   * @throws ServiceException
   */ 
  private void publishEventToBase(Event event)
      throws EPAuthenticationException, IOException, ServiceException {
    GoogleBaseService baseService = getBaseService();
    GoogleBaseEntry entry = null;
    if (event.getBaseUrl() != null) {
      // updating base entry
      entry = baseService.getEntry(event.getBaseUrl(), 
          GoogleBaseEntry.class);
      entry = new GoogleBaseEntry();
    } else {
      // publishing new entry
      entry = new GoogleBaseEntry();   
    }

    // prepare an 'events and activities' item for publishing
    GoogleBaseAttributesExtension gbaseAttributes = 
      entry.getGoogleBaseAttributes();
    entry.setTitle(new PlainTextConstruct(event.getTitle()));
    entry.setContent(new PlainTextConstruct(event.getDescription()));
    gbaseAttributes.setItemType("events and activities");

    // this sample currently only demonstrates publishing all-day events
    // an event in Google Base must have a start-time and end-time, so
    // this simulates that by adding 1 day to the end-date specified, if the
    // start and end times are identical
    DateTime startDateTime = new DateTime(event.getStartDate());
    startDateTime.setDateOnly(true);

    DateTime endDateTime = null;

    if (event.getStartDate().equals(event.getEndDate())) {
      Calendar endDateCal = new GregorianCalendar();
      endDateCal.setTime(event.getEndDate());
      endDateCal.add(Calendar.DATE, 1); 
      endDateTime = new DateTime(endDateCal.getTime());
    } else {
      endDateTime = new DateTime(event.getEndDate());
    }
    endDateTime.setDateOnly(true);
  
    gbaseAttributes.addDateTimeRangeAttribute("event date range", 
        new DateTimeRange(startDateTime, endDateTime));
    
    gbaseAttributes.addTextAttribute("event performer", "Google mashup test");
    gbaseAttributes.addUrlAttribute("performer url", "http://code.google.com/apis/gdata.html");
    
    if (event.getBaseUrl() != null) {
      // updating event
      baseService.update(event.getBaseUrl(), entry);
    } else {
      // insert event
      GoogleBaseEntry resultEntry = baseService.insert(
          FeedURLFactory.getDefault().getItemsFeedURL(), 
          entry);
      updateSsEventEditUrl(event.getSsEditUrl(), 
          null,
          resultEntry.getEditLink().getHref() );
    }
}
 
  /**
   * Updates the Google Base and/or Calendar edit URLs in the spreadsheet.
   * The storage of these edit URLs enables further runs of this code to 
   * publish events as new Calendar and Base entries only if the events had
   * not been previously published
   *
   * @param ssEditUrl The edit <code>URL</code> for the spreadsheet 
   * @param calEditUrl The edit URL to be set in the spreadsheet or null
   * @param baseEditUrl The edit URL to be set in the spreadsheet or null
   * @throws EPAuthenticationException
   * @throws IOException
   * @throws ServiceException 
   */
  private void updateSsEventEditUrl(URL ssEditUrl, String calEditUrl, 
      String baseEditUrl) 
      throws EPAuthenticationException, IOException, ServiceException {
    SpreadsheetService ssService = getSsService();
    ListEntry ssEntry = ssService.getEntry(ssEditUrl, ListEntry.class);
    // remove spaces in the URL
    String calUrlFieldName = fieldMap.getCalendarUrlColumn();
    if (calEditUrl == null && 
        (ssEntry.getCustomElements().getValue(calUrlFieldName) == null || 
         "".equals(ssEntry.getCustomElements().getValue(calUrlFieldName)))){
      calEditUrl = " ";
    }
    if (calEditUrl != null) {
      ssEntry.getCustomElements().setValueLocal(
          calUrlFieldName,
          calEditUrl);
    }
    // remove spaces in the URL
    String baseUrlFieldName = fieldMap.getBaseUrlColumn();
    if (baseEditUrl == null &&
        (ssEntry.getCustomElements().getValue(baseUrlFieldName) == null ||
      "".equals(ssEntry.getCustomElements().getValue(baseUrlFieldName)))){
      baseEditUrl = " ";
    }    
    if (baseEditUrl != null) {
      ssEntry.getCustomElements().setValueLocal(
          baseUrlFieldName,
          baseEditUrl);
    }
    ssEntry.update();
  }
 
  /**
   * Retrieves a <code>List</code> of <code>SpreadsheetEntry</code> instances
   * available from the list of Spreadsheets accessible from the authenticated
   * account.
   *
   * @throws EPAuthenticationException
   */
  private List<SpreadsheetEntry> getSsListHelper() 
    throws EPAuthenticationException {
    List<SpreadsheetEntry> returnList = null;

    try {
      SpreadsheetService ssService = getSsService();
      SpreadsheetFeed ssFeed = ssService.getFeed(
          new URL(SPREADSHEETS_META_FEED),
          SpreadsheetFeed.class);
      returnList = ssFeed.getEntries();
    } catch (com.google.gdata.util.AuthenticationException authEx) {
      throw new EPAuthenticationException(
          "SS list read access not available");
    } catch (com.google.gdata.util.ServiceException svcex) {
      System.err.println("ServiceException while retrieving " +
          "available spreadsheets: " + svcex.getMessage());
      returnList = null;
    } catch (java.io.IOException ioex) {
      System.err.println("IOException while retrieving " +
          "available spreadsheets: " + ioex.getMessage());
      returnList = null;
    }
    return returnList;    
  }
 
  /**
   * Retrieves a <code>List</code> of <code>WorksheetEntry</code> instances
   * available from the list of Worksheets in the specified feed
   *
   * @param wsFeedUrl The feed of worksheets
   * @throws EPAuthenticationException 
   * @return List of worksheets in the specified feed
   */
  private List<WorksheetEntry> getWsListHelper(String wsFeedUrl) 
  throws EPAuthenticationException {
    List<WorksheetEntry> returnList = null;

    try {
      SpreadsheetService ssService = getSsService();
      WorksheetFeed wsFeed = ssService.getFeed(
          new URL(wsFeedUrl), 
          WorksheetFeed.class);
      returnList = wsFeed.getEntries();
    } catch (com.google.gdata.util.AuthenticationException authEx) {
      throw new EPAuthenticationException(
          "WS list read access not available");
    } catch (com.google.gdata.util.ServiceException svcex) {
      System.err.println("ServiceException while retrieving " +
          "available worksheets: " + svcex.getMessage());
      returnList = null;
    } catch (java.io.IOException ioex) {
      System.err.println("IOException while retrieving " +
          "available worksheets: " + ioex.getMessage());
      returnList = null;
    }
    return returnList;    
  }
 
  /**
   * Retrieves a list of column headers in the specified cell feed 
   *
   * @param cellFeedUrl The cell feed
   * @return <code>List</code> of column headers as <code>String</code>s
   * @throws EPAuthenticationException
   */
  public List<String> getColumnList(String cellFeedUrl)
  throws EPAuthenticationException {
    List<String> returnList = new LinkedList<String>();
    List<CellEntry> columnList = getColumnListHelper(cellFeedUrl);
    for (CellEntry columnEntry : columnList) {
      returnList.add(columnEntry.getCell().getValue());
    }
    return returnList;
  } 
 
  /**
   * Retrieves a list of column headers in the specified cell feed
   *
   * @param cellFeedUrl The cell feed
   * @return <code>List</code> of column headers as <code>CellEntry</code>s
   * @trhows EPAuthenticationException
   */ 
  private List<CellEntry> getColumnListHelper(String cellFeedUrl) 
    throws EPAuthenticationException {
    List<CellEntry> returnList = null;

    try {
      SpreadsheetService ssService = getSsService();
      CellFeed cellFeed = ssService.getFeed(
          new URL(cellFeedUrl + "?min-row=1&max-row=1"), 
          CellFeed.class);
      returnList = cellFeed.getEntries();
    } catch (com.google.gdata.util.AuthenticationException authEx) {
      throw new 
      EPAuthenticationException(
          "SS read access not available");
    } catch (com.google.gdata.util.ServiceException svcex) {
      // log general service exception
      System.err.println("ServiceException while retrieving " +
          "column list: " + svcex.getMessage());
      returnList = null;
    } catch (java.io.IOException ioex) {
      System.err.println("IOException while retrieving " +
          "column list: " + ioex.getMessage());
      returnList = null;
    }
    return returnList;
  }
 
  /**
   * Retrieves all <code>ListEntry</code> objects from the spreadsheet
   *
   * @return <code>List</code> of <code>ListEntry</code> instances
   * @throws EPAuthenticationException 
   */
  private List<ListEntry> getSsEntryListHelper() 
    throws EPAuthenticationException {
    List<ListEntry> returnList = null;

    try {
      SpreadsheetService ssService = getSsService();
      ListFeed listFeed = ssService.getFeed(ssUrl, ListFeed.class);
      returnList = listFeed.getEntries();
    } catch (com.google.gdata.util.AuthenticationException authEx) {
      throw new EPAuthenticationException("SS read access not available");
    } catch (com.google.gdata.util.ServiceException svcex) {
      System.err.println("ServiceException while retrieving " +
          "entry list: " + svcex.getMessage());
      returnList = null;
    } catch (java.io.IOException ioex) {
      System.err.println("IOException while retrieving " +
          "entry list: " + ioex.getMessage());
      returnList = null;
    }
    return returnList;
  }
}
