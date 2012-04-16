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
 * A simple POJO that holds account information request details
 * 
 * 
 * 
 */
public class AccountInfo {

  private String requestId;
  private String userEmailAddress;
  private String adminEmailAddress;
  private String status;
  private Date requestDate;
  private Date completedDate;
  private int numberOfFiles;
  private String[] fileUrls;
  private Date expiredDate;

  private static DateFormat DATE_FORMAT = null;

  static {
    DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    DATE_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    DATE_FORMAT.setLenient(false);
  }

  public AccountInfo() {
    super();
  }

  /**
   * Parameterized constructor to create a POJO from a GenericEntry instance.
   * 
   * @param entry a GenericEntry instance with properties.
   */
  public AccountInfo(GenericEntry entry) {  
    completedDate = checkDateAndParse(entry.getProperty("completedDate"));
    requestDate = checkDateAndParse(entry.getProperty("requestDate"));
    expiredDate = checkDateAndParse(entry.getProperty("expiredDate"));
    requestId = entry.getProperty("requestId");
    userEmailAddress = entry.getProperty("userEmailAddress");
    adminEmailAddress = entry.getProperty("adminEmailAddress");
    status = entry.getProperty("status");
    
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

  private Date checkDateAndParse(String date) {
    return (date != null) ? DATE_FORMAT.parse(date, new ParsePosition(0)) : null;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  public void setAdminEmailAddress(String adminEmailAddress) {
    this.adminEmailAddress = adminEmailAddress;
  }

  public String getAdminEmailAddress() {
    return adminEmailAddress;
  }

  public void setUserEmailAddress(String userEmailAddress) {
    this.userEmailAddress = userEmailAddress;
  }

  public String getUserEmailAddress() {
    return userEmailAddress;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getRequestDate() {
    return requestDate;
  }

  public void setRequestDate(Date requestDate) {
    this.requestDate = requestDate;
  }

  public Date getCompletedDate() {
    return completedDate;
  }

  public void setCompletedDate(Date completedDate) {
    this.completedDate = completedDate;
  }

  public int getNumberOfFiles() {
    return numberOfFiles;
  }

  public void setNumberOfFiles(int numberOfFiles) {
    this.numberOfFiles = numberOfFiles;
  }

  public String[] getFileUrls() {
    return fileUrls;
  }

  public void setFileUrls(String[] fileUrls) {
    this.fileUrls = fileUrls;
  }

  public Date getExpiredDate() {
    return expiredDate;
  }

  public void setExpiredDate(Date expiredDate) {
    this.expiredDate = expiredDate;
  }

  /*
   * convert an AccountInfo instance into a GenericEntry instance
   */
  public GenericEntry toGenericEntry() {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("requestId", requestId);
    if (completedDate != null) {
      entry.addProperty("completedDate", DATE_FORMAT.format(completedDate));
    }
    if (requestDate != null) {
      entry.addProperty("beginDate", DATE_FORMAT.format(requestDate));
    }
    if (numberOfFiles != 0) {
      entry.addProperty("numberOfFiles", String.valueOf(numberOfFiles));
    }
    entry.addProperty("userEmailAddress", userEmailAddress);
    entry.addProperty("adminEmailAddress", adminEmailAddress);
    entry.addProperty("status", status);
    return entry;
  }

}
