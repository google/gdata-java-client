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

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Bean object to store event information used for synchronization
 * 
 * 
 */
public class Event implements Serializable {

  public static final long serialVersionUID = 1L;
  
  private String title;
  private String description;
  private String website;

  private Date startDate;
  private Date endDate;
  private URL baseUrl = null;
  private URL calendarUrl = null;
  private URL ssEditUrl = null;
  public int ssRow = -1;

  private static final Pattern DATE_PATTERN = 
    Pattern.compile("(\\d\\d?)/(\\d\\d?)/(\\d\\d\\d\\d)");

  /**
   * Constructs a new Event instance with the specified properties
   *
   * @param title The title of the event
   * @param description The descriptin of the event
   * @param website The website representing the event
   * @param startDate The start date for the event in MM/DD/YYYY format
   * @param endDate The end date for the event in MM/DD/YYYY format
   * @param calendarUrl The edit URL for this event in Google Calendar
   * @param baseUrl The edit URL for this event in Google Base
   * @throws MalformedURLException
   */
  public Event(String title, String description, String website,
      String startDate, String endDate, String calendarUrl, String baseUrl)
      throws MalformedURLException {
    this.setTitle(title);
    this.setDescription(description);
    this.setWebsite(website);
    this.setStartDate(startDate);
    this.setEndDate(endDate);
    this.setCalendarUrl(calendarUrl);
    this.setBaseUrl(baseUrl);
  }  
  

  /**
   * Helper method to convert a data in MM/DD/YYYY format into
   * a <code>java.util.Date</code>
   *
   * @param dateString The date string in MM/DD/YYYY format
   * @return The <code>java.util.Date</code> representing the string
   */
  private Date stringToDate(String dateString) {
    Matcher m = DATE_PATTERN.matcher(dateString);
    Calendar date = new GregorianCalendar();
    if (!m.matches()) {
      throw new NumberFormatException("Invalid date format.");
    }
    date.set(Integer.valueOf(m.group(3)),
             Integer.valueOf(m.group(1)) - 1,
             Integer.valueOf(m.group(2)),
             0,
             0,
             0);
    return date.getTime();
  }

  public Date getStartDate() {
    return startDate;
  }

  /**
   * Sets the start date for this event.  The data is stored as a 
   * <code>java.util.date</code>, so this method calls a helper to
   * parse the date string.  
   *
   * @param startDate The end date for the event in MM/DD/YYYY format
   */
  public void setStartDate(String startDate) {
    this.startDate = stringToDate(startDate); 
  }

  /**
   * Sets the end date for this event.  The data is stored as a 
   * <code>java.util.date</code>, so this method calls a helper to
   * parse the date string.  
   *
   * @param endDate The end date for the event in MM/DD/YYYY format
   */
  public void setEndDate(String endDate) {
    this.endDate = stringToDate(endDate); 
  }
  
  public Date getEndDate() {
    return endDate;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  
  public String getTitle() {
    return title;
  }
  
  public void setWebsite(String website) {
    this.website = website;
  }
  
  public String getWebsite() {
    return website;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }

  /**
   * Sets the Calendar edit URL for this event
   *
   * @param calendarUrl The edit URL for this event
   * @throws MalformedURLException
   */
  public void setCalendarUrl(String calendarUrl) throws MalformedURLException {
    if (calendarUrl != null && ! calendarUrl.trim().equals("")) {
      this.calendarUrl = new URL(calendarUrl);
    }
  }
  
  public URL getCalendarUrl() {
    return this.calendarUrl;
  }
  
  /**
   * Sets the Base edit URL for this event
   *
   * @param baseUrl The edit URL for this event
   * @throws MalformedURLException
   */
  public void setBaseUrl(String baseUrl) throws MalformedURLException {
    if (baseUrl != null && ! baseUrl.trim().equals("")) {
      this.baseUrl = new URL(baseUrl);
    }
  }

  public URL getBaseUrl() {
    return this.baseUrl;
  }
  
  /**
   * Sets the Spreadsheets edit URL for this event
   *
   * @param baseUrl The edit URL for this event
   * @throws MalformedURLException
   */
  public void setSsEditUrl(String ssEditUrl) throws MalformedURLException {
    if (ssEditUrl != null && ! ssEditUrl.trim().equals("")) {
      this.ssEditUrl = new URL(ssEditUrl);
    }
  }
  
  public URL getSsEditUrl() {
    return this.ssEditUrl;
  }

}
