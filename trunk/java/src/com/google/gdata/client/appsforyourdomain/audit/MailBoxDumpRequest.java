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
 * A simple POJO for a Mailbox dump request.
 * 
 * 
 */
public class MailBoxDumpRequest {

  private String packageContent;
  private boolean includeDeleted;
  private String searchQuery;
  private Date completedDate;
  private String adminEmailAddress;
  private int numberOfFiles;
  private String userEmailAddress;
  private String requestId;
  private Date endDate;
  private Date requestDate;
  private String status;
  private String[] fileUrls;
  private Date expiredDate;
  
  /**
   * Date for the first email included in the exported mailbox. This is an
   * optional element. If you want all emails starting from the date when the
   * account was created, do not enter a value for this field.
   */
  private Date beginDate;


  private static DateFormat DATE_FORMAT = null;

  static {
    DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    DATE_FORMAT.setLenient(false);
  }
  
  /**
   * Parameterized constructor to create a POJO from GenericEntry.
   * 
   * @param entry GenericEntry
   */
  public MailBoxDumpRequest(GenericEntry entry) {
    packageContent = entry.getProperty("packageContent");
    requestId = entry.getProperty("requestId");
    completedDate = checkDateAndParse(entry.getProperty("completedDate"));
    endDate = checkDateAndParse(entry.getProperty("endDate"));
    requestDate = checkDateAndParse(entry.getProperty("requestDate"));
    expiredDate = checkDateAndParse(entry.getProperty("expiredDate"));
    beginDate = checkDateAndParse(entry.getProperty("beginDate"));
    searchQuery = entry.getProperty("searchQuery");
    adminEmailAddress = entry.getProperty("adminEmailAddress");
    userEmailAddress = entry.getProperty("userEmailAddress");
    status = entry.getProperty("status");
    includeDeleted =
        entry.getProperty("includeDeleted") != null ? Boolean.parseBoolean(entry
            .getProperty("includeDeleted")) : includeDeleted;

    numberOfFiles =
        entry.getProperty("numberOfFiles") != null ? Integer.parseInt(entry
            .getProperty("numberOfFiles")) : numberOfFiles;

    if (numberOfFiles > 0) {
      fileUrls = new String[numberOfFiles];
      for (int fileIndex = 0; fileIndex < numberOfFiles; fileIndex++) {
        fileUrls[fileIndex] = entry.getProperty("fileUrl" + fileIndex);
      }
    }

  }

  /**
   * Default constructor
   */
  public MailBoxDumpRequest() {
    this.includeDeleted = false;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  
  public String[] getFileUrls() {
    return fileUrls;
  }

  public void setFileUrls(String[] fileUrls) {
    this.fileUrls = fileUrls;
  }

  public void setNumberOfFiles(int numberOfFiles) {
    this.numberOfFiles = numberOfFiles;
  }

  public int getNumberOfFiles() {
    return numberOfFiles;
  }

  public String getPackageContent() {
    return packageContent;
  }

  public void setPackageContent(String packageContent) {
    this.packageContent = packageContent;
  }

  public boolean isIncludeDeleted() {
    return includeDeleted;
  }

  public void setIncludeDeleted(boolean includeDeleted) {
    this.includeDeleted = includeDeleted;
  }

  public String getSearchQuery() {
    return searchQuery;
  }

  public void setSearchQuery(String searchQuery) {
    this.searchQuery = searchQuery;
  }

  public Date getCompletedDate() {
    return completedDate;
  }

  public void setCompletedDate(Date completedDate) {
    this.completedDate = completedDate;
  }

  public String getAdminEmailAddress() {
    return adminEmailAddress;
  }

  public void setAdminEmailAddress(String adminEmailAddress) {
    this.adminEmailAddress = adminEmailAddress;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public Date getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(Date requestDate) {
    this.requestDate = requestDate;
  }

  public Date getBeginDate() {
    return beginDate;
  }

  public void setBeginDate(Date beginDate) {
    this.beginDate = beginDate;
  }

  public String getUserEmailAddress() {
    return userEmailAddress;
  }

  public void setUserEmailAddress(String userEmailAddress) {
    this.userEmailAddress = userEmailAddress;
  }

  public Date getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
  }

  /**
   * converts a {@link MailBoxDumpRequest} to {@link GenericEntry}.
   * 
   * @return GenericEntry
   */
  public GenericEntry toGenericEntry() {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("packageContent", packageContent);
    if (completedDate != null) {
      entry.addProperty("completedDate", DATE_FORMAT.format(completedDate));
    }
    if (endDate != null) {
      entry.addProperty("endDate", DATE_FORMAT.format(endDate));
    }     
    if (beginDate != null) {
      entry.addProperty("beginDate", DATE_FORMAT.format(beginDate));
    }
    if (searchQuery != null) {
      entry.addProperty("searchQuery", searchQuery);
    }
    if (numberOfFiles != 0) {
      entry.addProperty("numberOfFiles", String.valueOf(numberOfFiles));
    }
    entry.addProperty("includeDeleted", String.valueOf(includeDeleted));
    entry.addProperty("adminEmailAddress", adminEmailAddress);
    return entry;
  }

  private Date checkDateAndParse(String date) {
    return (date != null) ? DATE_FORMAT.parse(date, new ParsePosition(0)) : null;
  }

}
