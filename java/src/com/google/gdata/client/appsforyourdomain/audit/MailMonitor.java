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


package com.google.gdata.client.appsforyourdomain.audit;

import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * A simple POJO describing an Email Monitor.
 * 
 * 
 */
public class MailMonitor {

  private String requestId;
  private String destUserName;
  private Date beginDate;
  private Date endDate;
  private String incomingEmailMonitorLevel;
  private String outgoingEmailMonitorLevel;
  private String draftMonitorLevel;
  private String chatMonitorLevel;
  private static DateFormat DATE_FORMAT = null;

  static {
    DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    DATE_FORMAT.setLenient(false);
  }

  /**
   * Default constructor.
   */
  public MailMonitor() {
  }

  /**
   * Parameterized constructor that creates a MailMonitor instance from a 
   * {@link GenericEntry}
   * 
   * @param entry GenericEntry instance describing an Audit Monitor Entry.
   */
  public MailMonitor(GenericEntry entry) {
    requestId = entry.getProperty("requestId");
    destUserName = entry.getProperty("destUserName");
    beginDate = DATE_FORMAT.parse(entry.getProperty("beginDate"), new ParsePosition(0));
    endDate = DATE_FORMAT.parse(entry.getProperty("endDate"), new ParsePosition(0));
    incomingEmailMonitorLevel = entry.getProperty("incomingEmailMonitorLevel");
    outgoingEmailMonitorLevel = entry.getProperty("outgoingEmailMonitorLevel");
    draftMonitorLevel = entry.getProperty("draftMonitorLevel");
    chatMonitorLevel = entry.getProperty("chatMonitorLevel");
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public String getDestUserName() {
    return destUserName;
  }

  public void setDestUserName(String destUserName) {
    this.destUserName = destUserName;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public String getIncomingEmailMonitorLevel() {
    return incomingEmailMonitorLevel;
  }

  public void setIncomingEmailMonitorLevel(String incomingEmailMonitorLevel) {
    this.incomingEmailMonitorLevel = incomingEmailMonitorLevel;
  }

  public String getOutgoingEmailMonitorLevel() {
    return outgoingEmailMonitorLevel;
  }

  public void setOutgoingEmailMonitorLevel(String outgoingEmailMonitorLevel) {
    this.outgoingEmailMonitorLevel = outgoingEmailMonitorLevel;
  }

  public String getDraftMonitorLevel() {
    return draftMonitorLevel;
  }

  public void setDraftMonitorLevel(String draftMonitorLevel) {
    this.draftMonitorLevel = draftMonitorLevel;
  }

  public String getChatMonitorLevel() {
    return chatMonitorLevel;
  }

  public void setChatMonitorLevel(String chatMonitorLevel) {
    this.chatMonitorLevel = chatMonitorLevel;
  }

  /**
   * Creates a GenericEntry from the MailMonitor fields.
   * 
   * @return a GenericEntry with field values as properties.
   */
  public GenericEntry toGenericEntry() {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("destUserName", destUserName);
    entry.addProperty("beginDate", DATE_FORMAT.format(beginDate));
    entry.addProperty("endDate", DATE_FORMAT.format(endDate));
    entry.addProperty("incomingEmailMonitorLevel", incomingEmailMonitorLevel);
    entry.addProperty("outgoingEmailMonitorLevel", outgoingEmailMonitorLevel);
    entry.addProperty("draftMonitorLevel", draftMonitorLevel);
    entry.addProperty("chatMonitorLevel", chatMonitorLevel);
    return entry;
  }
}
