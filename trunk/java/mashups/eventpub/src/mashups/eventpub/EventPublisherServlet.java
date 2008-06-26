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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * Servlet to control requests for publishing events from Spreadsheets to
 * Calendar and Base.
 *
 * 
 */
public class EventPublisherServlet extends javax.servlet.http.HttpServlet 
     implements javax.servlet.Servlet {

  public static final long serialVersionUID = 1;

  /**
   * The session attribute in which to store the AuthSub token.  The
   * AuthSub token, after being upgraded from a single-use to a session token,
   * is held between the requests using the servlet container's session 
   * management capability.
   */
  public static final String SESSION_ATTR_SS_AUTH_TOKEN = "ssAuthSubToken";

  /**
   * Names of other session attributes
   */
  public static final String SESSION_ATTR_SS_CELL_FEED = "ssCellFeed";
  public static final String SESSION_ATTR_FIELD_MAP = "fieldMap";
  public static final String SESSION_ATTR_EVENTS_TO_PUBLISH = "eventsToPublish";

  private Configuration config;
  
  public EventPublisherServlet() throws Exception {
    super();
    config = new PropertiesConfiguration("EventPublisher.properties");
  }
  
  private String getCurrentUrl(HttpServletRequest request) 
      throws MalformedURLException {
    URL currentUrl = new URL(request.getScheme(),
        request.getServerName(), 
        request.getServerPort(),
        request.getRequestURI() );
    return currentUrl.toString();
  }

  /**
   * Save AuthSubToken into session
   * 
   * @param request The request 
   * @param response The response 
   */ 
  private void processAcceptAuthSubToken(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException {
    /*
     * Request is caused by a user being redirected back from AuthSub login
     */
    if (request.getParameter("token") != null ) {
      EventPublisher ep = new EventPublisher();
      try {
        ep.setSsAuthSubToken(request.getParameter("token"), true);
        request.getSession().setAttribute(SESSION_ATTR_SS_AUTH_TOKEN, 
            ep.getSsAuthSubToken());
        /*
         * Redirect to clear the token from the URL and list available 
         * spreadsheets.
         */
        response.sendRedirect("?action=outputSsList");
      } catch (EPAuthenticationException e) {
        System.err.println("Authentication exception: " + e.getMessage());  
      }
    }
  }

  /**
   * Default action - output intro page 
   *
   * @param request The request 
   * @param response The response 
   */
  private void processOutputIntroPage(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException {
    String ssAuthUrl = EventPublisher.getSsAuthSubUrl(
        getCurrentUrl(request) + "?action=acceptAuthSubToken" );
    request.setAttribute("ssAuthUrl", ssAuthUrl);
    RequestDispatcher dispatcher = 
        getServletContext().getRequestDispatcher(
        "/WEB-INF/jsp/outputIntroPage.jsp");
    dispatcher.forward(request, response);
  }

  /**
   * Output list of spreadsheets owned by authenticated account 
   *
   * @param request The request 
   * @param response The response 
   */
  private void processOutputSsList(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException {
    EventPublisher ep = new EventPublisher();
    try {
      ep.setSsAuthSubToken(
        (String)request.getSession().getAttribute(SESSION_ATTR_SS_AUTH_TOKEN), 
        false);
      request.setAttribute("ssList", ep.getSsList());
    } catch (EPAuthenticationException e) {
      System.err.println("Authentication exception: " + e.getMessage());
    }
    RequestDispatcher dispatcher = 
        getServletContext().getRequestDispatcher(
        "/WEB-INF/jsp/outputSsList.jsp");
    dispatcher.forward(request, response);
  }

  /**
   * Output list of worksheets in chosen spreadsheet 
   *
   * @param request The request 
   * @param response The response 
   */
  private void processOutputWsList(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException {
    EventPublisher ep = new EventPublisher();
    try {
      ep.setSsAuthSubToken(
          (String)request.getSession().getAttribute(
          SESSION_ATTR_SS_AUTH_TOKEN), false);
      request.setAttribute("token", ep.getSsAuthSubToken());
      request.setAttribute("wsList", 
          ep.getWsList((String)request.getParameter("wsFeed")));
    } catch (EPAuthenticationException e) {
      System.err.println("Authentication exception: " + e.getMessage());  
    }

    RequestDispatcher dispatcher = 
        getServletContext().getRequestDispatcher(
        "/WEB-INF/jsp/outputWsList.jsp");
    dispatcher.forward(request, response);
  }

  /**
   * Output list of columns in chosen worksheet
   *
   * @param request The request 
   * @param response The response 
   */
  private void processOutputColumnList(HttpServletRequest request, 
        HttpServletResponse response) throws ServletException, IOException {
    EventPublisher ep = new EventPublisher();
    try {
      ep.setSsAuthSubToken(
          (String)request.getSession().getAttribute(SESSION_ATTR_SS_AUTH_TOKEN), 
          false);
      request.setAttribute("token", ep.getSsAuthSubToken());
      request.setAttribute("columnList", 
          ep.getColumnList((String)request.getParameter("cellFeed")));
      request.getSession().setAttribute(SESSION_ATTR_SS_CELL_FEED, 
          (String)request.getParameter("cellFeed"));
    } catch (EPAuthenticationException e) {
      System.err.println("Authentication exception: " + e.getMessage());  
    }

    RequestDispatcher dispatcher = 
        getServletContext().getRequestDispatcher(
        "/WEB-INF/jsp/outputColumnList.jsp");
    dispatcher.forward(request, response);
  }

  /**
   * Lists events to be published and also sets the custom field map.
   *
   * @param request The request 
   * @param response The response 
   */
  private void processListEvents(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
    EventPublisher ep = new EventPublisher();

    /*
     * Set the EventPublisher's list feed
     */
    ep.setSsUrl(
        ((String)request.getSession().getAttribute(
        SESSION_ATTR_SS_CELL_FEED)).replace("cells","list"));
    
    if (request.getParameter("fdTitle") != null) {
      SpreadsheetCustomFieldMap fieldMap = new SpreadsheetCustomFieldMap(
          (String)request.getParameter("fdTitle"),
          (String)request.getParameter("fdDescription"),
          (String)request.getParameter("fdStartDate"),
          (String)request.getParameter("fdEndDate"),
          (String)request.getParameter("fdLocation"),
          (String)request.getParameter("fdWebSite"),
          (String)request.getParameter("fdCalendarUrl"),
          (String)request.getParameter("fdBaseUrl"));
      ep.setFieldMap(fieldMap);
      request.getSession().setAttribute(SESSION_ATTR_FIELD_MAP, fieldMap);
    } else {
      ep.setFieldMap(
          (SpreadsheetCustomFieldMap)
          request.getSession().getAttribute(SESSION_ATTR_FIELD_MAP));
    }
    
    try {
      ep.setSsAuthSubToken(
          (String)request.getSession().getAttribute(SESSION_ATTR_SS_AUTH_TOKEN),
          false);
      List<Event> listEvents = ep.getEventsFromSpreadsheet();
        request.setAttribute("events", listEvents);
        request.getSession().setAttribute(SESSION_ATTR_EVENTS_TO_PUBLISH, 
            listEvents);
      javax.servlet.RequestDispatcher dispatcher = 
          getServletContext().getRequestDispatcher(
          "/WEB-INF/jsp/outputEventList.jsp");
      dispatcher.forward(request, response);
    } catch (EPAuthenticationException e) {
      System.err.println("Authentication exception: " + e.getMessage());  
    }
  }

  /**
   * Publish all events to Calendar and/or Base 
   *
   * @param request The request 
   * @param response The response 
   */
  @SuppressWarnings("unchecked")
  private void processPublishEvents(HttpServletRequest request,
        HttpServletResponse response) throws ServletException, IOException {
    EventPublisher ep = new EventPublisher();
    ep.setFieldMap((SpreadsheetCustomFieldMap)
        request.getSession().getAttribute(SESSION_ATTR_FIELD_MAP));
    try {
      ep.setSsAuthSubToken(
          (String)request.getSession().getAttribute(SESSION_ATTR_SS_AUTH_TOKEN),
          false);
    } catch (EPAuthenticationException e) {
      System.err.println("Authentication exception: " + e.getMessage());  
    }

    LinkedList<Event> eventList = 
        (LinkedList<Event>)request.getSession().getAttribute(
        SESSION_ATTR_EVENTS_TO_PUBLISH);

    if (request.getParameter("calendar") != null &&
        "checked".equals(request.getParameter("calendar"))) {
      String calUsername = config.getString("calendar.username");
      String calPassword = config.getString("calendar.password");
      String calUrl = config.getString("calendar.url");
      ep.setCalUsernamePassword(calUsername, calPassword);
      ep.setCalUrl(calUrl);
      ep.publishEventsToCalendar(eventList);
    }
    if (request.getParameter("base") != null &&
        "checked".equals(request.getParameter("base"))) {
      String baseUsername = config.getString("gbase.username");
      String basePassword = config.getString("gbase.password");
      ep.setBaseUsernamePassword(baseUsername, basePassword);
      ep.publishEventsToBase(eventList);
    }
    javax.servlet.RequestDispatcher dispatcher = 
        getServletContext().getRequestDispatcher(
        "/WEB-INF/jsp/outputPublishingResults.jsp");
    dispatcher.forward(request, response);
  }

  /** 
   * Process all requests to this servlet
   *
   * @param request The request 
   * @param response The response 
   */
  protected void doPost(HttpServletRequest request, 
      HttpServletResponse response) throws ServletException, IOException {
    /* 
     * Determine action attempted
     */
    String action = (request.getParameter("action"))==null?
        "outputIntroPage":request.getParameter("action");

    if ("outputIntroPage".equals(action)) {
      processOutputIntroPage(request, response);
    } else if ("acceptAuthSubToken".equals(action)) {
      processAcceptAuthSubToken(request, response);
    } else if ("outputSsList".equals(action)) {
      processOutputSsList(request, response);
    } else if ("outputWsList".equals(action)) {
      processOutputWsList(request, response);
    } else if ("outputColumnList".equals(action)) {
      processOutputColumnList(request, response);
    } else if ("listEvents".equals(action)) {
      processListEvents(request, response);
    } else if ("publish".equals(action)) {
      processPublishEvents(request, response);
    }
  } 
  
  /** 
   * Process GET requests by calling the doPost method 
   *
   * @param request The request 
   * @param response The response 
   */
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
  }
}
