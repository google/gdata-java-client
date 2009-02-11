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


package com.google.gdata.util;

import com.google.gdata.util.common.base.Preconditions;

/**
 * A mutable {@link ErrorContent} to hold structured error information.
 * 
 * 
 */
public class ErrorElement implements ErrorContent {

  // Error info instance variables.
  private String errorDomainName;
  private String errorCodeName;
  private String errorLocation;
  private LocationType errorLocationType;
  private String errorInternalReason;
  private String errorExtendedHelp;
  private String errorSendReport;
  private String errorDebugInfo;
  
  /**
   * Construct a new empty error info.
   */
  ErrorElement() {}
  
  /**
   * Construct a new error info as a copy of the given error element.
   */
  ErrorElement(ErrorContent source) {
    this.errorDomainName = source.getDomainName();
    this.errorCodeName = source.getCodeName();
    this.errorLocation = source.getLocation();
    this.errorLocationType = source.getLocationType();
    this.errorInternalReason = source.getInternalReason();
    this.errorExtendedHelp = source.getExtendedHelp();
    this.errorSendReport = source.getSendReport();
    this.errorDebugInfo = source.getDebugInfo();
  }
  
  /**
   * Return the error domain.
   */
  public String getDomainName() {
    return errorDomainName;
  }
  
  /**
   * Set the domain name.
   * 
   * @throws NullPointerException if {@code domainName} is {@code null}.
   */
  public ErrorElement setDomain(String domainName) {
    Preconditions.checkNotNull(domainName, "Error domain must not be null.");
    errorDomainName = domainName;
    return this;
  }
  
  /**
   * Gets the name of this ErrorInfo, which must be unique within
   * its domain.  The value will appear as the content of the {@code code}
   * element in the XML error format.
   */
  public String getCodeName() {
    return errorCodeName;
  }

  /**
   * Set the codename.
   * 
   * @throws NullPointerException if {@code codeName} is {@code null}.
   */
  public ErrorElement setCode(String codeName) {
    Preconditions.checkNotNull(codeName, "Error code must not be null.");
    errorCodeName = codeName;
    return this;
  }
  
  /**
   * Return the location of the error.
   */
  public String getLocation() {
    return errorLocation;
  }

  /**
   * Return the type of error location.  See {@link ErrorContent.LocationType}
   * for the available values.
   */
  public LocationType getLocationType() {
    return errorLocationType;
  }
  
  /**
   * Set XPath-based error location.
   * 
   * <p>This must be a valid XPath expression sibling to the atom:entry
   * element (or the atom:feed element if we are not in an entry).
   * 
   * @throws NullPointerException if {@code location} is {@code null}.
   */
  public ErrorElement setXpathLocation(String location) {
    return setLocation(location, LocationType.XPATH);
  }

  /**
   * Set header name for an error in a header.
   * 
   * @throws NullPointerException if {@code location} is {@code null}.
   */
  public ErrorElement setHeaderLocation(String location) {
    return setLocation(location, LocationType.HEADER);
  }

  /**
   * Set a generic error location, using {@link ErrorContent.LocationType#OTHER}
   * as the location type.
   * 
   * @throws NullPointerException if {@code location} is {@code null}.
   */
  public ErrorElement setLocation(String location) {
    return setLocation(location, LocationType.OTHER);
  }

  /**
   * Set the location and location type.
   * 
   * @throws NullPointerException if {@code location} or {@code locationType}
   *     are {@code null}.
   */
  public ErrorElement setLocation(String location, LocationType locationType) {
    Preconditions.checkNotNull(location, "Location must not be null.");
    Preconditions.checkNotNull(locationType, "Location type must not be null.");
    errorLocation = location;
    errorLocationType = locationType;
    return this;
  }
  
  /**
   * Gets the internal reason (unlocalized explanation) associated with this
   * ErrorInfo.  The value will appear as the content of the
   * {@code internalReason} element in the XML error format.
   */
  public String getInternalReason() {
    return errorInternalReason;
  }

  /**
   * Set the error's internal reason.
   * 
   * @throws NullPointerException if {@code internalReason} is {@code null}.
   */
  public ErrorElement setInternalReason(String internalReason) {
    Preconditions.checkNotNull(
        internalReason, "Internal Reason must not be null.");
    errorInternalReason = internalReason;
    return this;
  }

  /**
   * Gets the extended help URI.  This can be used to retrieve a
   * detailed explanation of the error code.  The value will appear
   * as the content of the {@code extendedHelp} element in the XML
   * error format.
   */
  public String getExtendedHelp() {
    return errorExtendedHelp;
  }

  // Matches a valid Google URI.
  private static final String GOOGLE_URI_PATTERN = "http://.*google\\.com/.*";

  /**
   * Set URI for extended help.
   * 
   * @throws NullPointerException if {@code extendedHelp} is {@code null}.
   * @throws IllegalArgumentException if {@code extendedHelp} isn't a valid
   *     google URI.
   */
  public ErrorElement setExtendedHelp(String extendedHelp) {
    Preconditions.checkNotNull(
        extendedHelp, "Extended help uri must not be null.");
    Preconditions.checkArgument(
        extendedHelp.matches(GOOGLE_URI_PATTERN),
        "Invalid extended help URI: %s", extendedHelp);
    errorExtendedHelp = extendedHelp;
    return this;
  }
  
  /**
   * Gets the URI to which a report should be sent when this error
   * is received.  The value will appear as the content of the 
   * {@code sendReport} element in the XML error format.
   */
  public String getSendReport() {
    return errorSendReport;
  }
  
  /**
   * Set URI to send report to.
   * 
   * @throws NullPointerException if {@code extendedHelp} is {@code null}.
   * @throws IllegalArgumentException if {@code extendedHelp} isn't a valid
   *     google URI.
   */
  public ErrorElement setSendReport(String sendReport) {
    Preconditions.checkNotNull(sendReport, "Send report uri must not be null.");
    Preconditions.checkArgument(
        sendReport.matches(GOOGLE_URI_PATTERN),
        "Invalid send report URI: %s", sendReport);
    errorSendReport = sendReport;
    return this;
  }

  /**
   * Returns debugging information.
   */
  public String getDebugInfo() {
    return errorDebugInfo;
  }

  /**
   * Set debugging information.
   * 
   * @throws NullPointerException if {@code debugInfo} is {@code null}.
   */
  public ErrorElement setDebugInfo(String debugInfo) {
    Preconditions.checkNotNull(debugInfo, "Debug info must not be null.");
    errorDebugInfo = debugInfo;
    return this;
  }
}
