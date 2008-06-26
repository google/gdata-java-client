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

import java.util.HashMap;

/**
 * Store the mapping between Spreadsheet columns and needed data 
 * 
 * 
 */
public class SpreadsheetCustomFieldMap { 

  public static final long serialVersionUID = 1L;
  
  private HashMap<String, String> fieldMap = null; 

  /**
   * Constructs a new SpreadsheetCustomFieldMap
   *
   * @param title Column name for event title
   * @param description Column name for event description
   * @param start Column name for event start date 
   * @param end Column name for event end date 
   * @param location Column name for the event location
   * @param website Column name for the event website
   * @param calendarUrl Column name for the calendar event edit URL 
   * @param baseUrl Column name for the base event edit URL 
   */
  public SpreadsheetCustomFieldMap(String title, String description,
      String start, String end, String location, String website,
      String calendarUrl, String baseUrl) {
    fieldMap = new HashMap<String, String>();
    setField("title", title); 
    setField("description", description); 
    setField("start", start); 
    setField("end", end); 
    setField("location", location); 
    setField("website", website); 
    setField("calendarUrl", calendarUrl); 
    setField("baseUrl", baseUrl); 
  }

  /**
   * Sets the field in the fieldMap, stripping any whitespaces from the
   * name of the column in the spreadsheet
   *
   * @param name The name of the field needed
   * @param value The name of the column in the spreadsheet 
   */ 
  public void setField(String name, String value) {
    fieldMap.put(name, value.replace(" ", ""));  
  }

  /**
   * Returns the name of the column identifying the title
   *
   * @return Column name for the event title
   */
  public String getTitleColumn() {
    return (String)fieldMap.get("title");
  }

  /**
   * Returns the name of the column identifying the description
   *
   * @return Column name for the event description
   */
  public String getDescriptionColumn() {
    return (String)fieldMap.get("description");
  }

  /**
   * Returns the name of the column identifying the start
   *
   * @return Column name for the event start
   */
  public String getStartColumn() {
    return (String)fieldMap.get("start");
  }

  /**
   * Returns the name of the column identifying the end
   *
   * @return Column name for the event end
   */
  public String getEndColumn() {
    return (String)fieldMap.get("end");
  }

  /**
   * Returns the name of the column identifying the location
   *
   * @return Column name for the event location
   */
  public String getLocationColumn() {
    return (String)fieldMap.get("location");
  }

  /**
   * Returns the name of the column identifying the website
   *
   * @return Column name for the event website
   */
  public String getWebsiteColumn() {
    return (String)fieldMap.get("website");
  }

  /**
   * Returns the name of the column identifying the calendarUrl
   *
   * @return Column name for the event calendarUrl
   */
  public String getCalendarUrlColumn() {
    return (String)fieldMap.get("calendarUrl");
  }

  /**
   * Returns the name of the column identifying the baseUrl
   *
   * @return Column name for the event baseUrl
   */
  public String getBaseUrlColumn() {
    return (String)fieldMap.get("baseUrl");
  }
}
