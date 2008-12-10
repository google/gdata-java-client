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

import com.google.gdata.util.common.base.CharEscapers;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The ServiceException class is the base exception class used to
 * indicate an error while processing a GDataRequest.
 *
 * 
 * 
 * 
 */
public class ServiceException extends Exception {

  // The instance variables of this class are package-private.
  // This is done so that the class ServiceExceptionInitializer
  // (which is in essence an extension of the HttpURLConnection
  // constructor) can set them without violating the taboo about
  // calling instance methods such as setters on a partially
  // constructed object.

  // Instance variables for HTTP

  /** HTTP error code. */
  int httpErrorCodeOverride = -1;

  /**
   * Optional HTTP headers to be output when HTTP response is generated.
   * It is a mapping from header name to list of header values.
   */
  Map<String, List<String>> httpHeaders;

  /**
   * Optional HTTP content type for a response.
   */
  ContentType responseContentType;

  /**
   * Retrieve HTTP response body or error message.
   * The content type must be set correctly.
   */
  String responseBody;

  // Error domain related instance variables

  String errorDomainName;
  String errorCodeName;
  String errorLocation;
  LocationType errorLocationType;
  String errorInternalReason;
  String errorExtendedHelp;
  String errorSendReport;
  String errorDebugInfo;

  // Instance variables for siblings

  /**
   * Set to the exception we are filling during parsing.
   */
  List<ServiceException> siblings =
      new ArrayList<ServiceException>(1);
  {
    siblings.add(this);
  }


  // Server-side constructors for reporting exceptions to client

  public ServiceException(String message) {
    super(nullsafe(message));
    httpHeaders = new HashMap<String, List<String>>();
  }

  public ServiceException(String message, Throwable cause) {
    super(nullsafe(message), cause);
    httpHeaders = new HashMap<String, List<String>>();
  }

  public ServiceException(Throwable cause) {
    super(nullsafe(cause.getMessage()));
    httpHeaders = new HashMap<String, List<String>>();
  }

  /**
   * Initializes the ServiceException using the error response data from an
   * HTTP connection.  This constructor is used in the client library
   * to approximately reconstitute the constructor as it appeared
   * in the server.
   * <p>This constructor uses a ServiceExceptionInitializer to do
   * the work of parsing the connection and calling our setters to
   * initialize our fields.  The initializer object may also create
   * sibling ServiceExceptions.
   * @param httpConn is the http connection from which the error message
   *     (structured or simple) is read
   * @throws IOException if network error receiving the error response
   */
  public ServiceException(HttpURLConnection httpConn) throws IOException {
    super(nullsafe(httpConn.getResponseMessage()));
    ServiceExceptionInitializer initializer =
        new ServiceExceptionInitializer(this);
    try {
      initializer.parse(httpConn);
    } catch (ParseException pe) {
      // Clean up after failed parse
      errorDomainName = null;
      errorCodeName = null;
      errorLocation = null;
      errorLocationType = null;
      errorInternalReason = null;
      errorExtendedHelp = null;
      errorSendReport = null;
      errorDebugInfo = null;
      siblings.clear();
      siblings.add(this);
      responseContentType = ContentType.TEXT_PLAIN;
    }
  }

  /**
   * Initializes the ServiceException using an {@link ErrorCode}
   * object that encapsulates most of the information about the
   * error.  ErrorCodes are declared in a subclass of
   * {@link ErrorDomain} containing all the errors for this
   * GData domain (service or portion of service).
   */
  public ServiceException(ErrorDomain.ErrorCode errorCode) {
    super(nullsafe(errorCode.getInternalReason()));
    httpHeaders = new HashMap<String, List<String>>();
    errorDomainName = errorCode.getDomainName();
    errorCodeName = errorCode.getCodeName();
    errorInternalReason = errorCode.getInternalReason();
    errorExtendedHelp = errorCode.getExtendedHelp();
    errorSendReport = errorCode.getSendReport();
  }

  /**
   * Initializes the ServiceException using an {@link ErrorCode} object
   * that encapsulates most of the information about the error, and
   * an embedded exception.  ErrorCodes are declared in a subclass of
   * {@link ErrorDomain} containing all the errors for this GData domain
   * (service or portion of service).
   */
  public ServiceException(ErrorDomain.ErrorCode errorCode, Throwable cause) {
    super(nullsafe(errorCode.getInternalReason()), cause);
    httpHeaders = new HashMap<String, List<String>>();
    errorDomainName = errorCode.getDomainName();
    errorCodeName = errorCode.getCodeName();
    errorInternalReason = errorCode.getInternalReason();
    errorExtendedHelp = errorCode.getExtendedHelp();
    errorSendReport = errorCode.getSendReport();
  }

  // Private method used in constructors
  private static String nullsafe(String src) {
     return (src != null) ? src : "Exception message unavailable";
  }

  // Getters and setters
  public int getHttpErrorCodeOverride() { return httpErrorCodeOverride; }
  public void setHttpErrorCodeOverride(int v) { httpErrorCodeOverride = v; }


  public ContentType getResponseContentType() {
    return responseContentType;
  }

  public void setResponseContentType(ContentType v) {
    if (v == null) {
      throw new NullPointerException("Null content type");
    }
    responseContentType = v;
  }

  public String getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(String body) {
    if (body == null) {
      throw new NullPointerException("Null response body");
    }
    responseBody = body;
    // Create and invoke a ServiceExceptionInitializer to
    // do the dirty work of reading the stream, possibly
    // parsing the result if it is a GData structured error,
    // and set the instance variables accordingly.
    // In C++ terms, SEI is a friend class.
    ServiceExceptionInitializer initializer =
        new ServiceExceptionInitializer(this);
    try {
      initializer.parse(responseContentType, responseBody);
    } catch (ParseException pe) {
        throw new RuntimeException(pe.getMessage(), pe);
    }
  }

  /**
   * Set HTTP response type and body simultaneously.
   * They are inherently coupled together: a body without a content type
   * is meaningless; a content type without a body is useless.
   */
  public void setResponse(ContentType contentType, String body) {
    if (contentType == null) {
      throw new NullPointerException("Null content type");
    }
    if (body == null) {
      throw new NullPointerException("Null response body");
    }
    responseContentType = contentType;
    setResponseBody(body);
  }

  /** Generate error message in XML format. */

  public String toXmlErrorMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append("<errors xmlns='http://schemas.google.com/g/2005'>\n");
    for (ServiceException sibling : siblings) {
      addXmlError(sibling, sb);
    }
    sb.append("</errors>\n");
    return sb.toString();
  }

  private String escape(String src) {
    return CharEscapers.xmlEscaper().escape(src);
  }

  private void addXmlError(ServiceException se, StringBuilder sb) {
    // Simplistic StringBuffer implementation because the XML is trivial.
    sb.append("<error>\n");

    String domainName = se.getDomainName();
    sb.append("<domain>");
    sb.append(escape(domainName));
    sb.append("</domain>\n");

    String codeName = se.getCodeName();
    sb.append("<code>");
    sb.append(escape(codeName));
    sb.append("</code>\n");

    String location = se.getLocation();
    String locationType = (se.getLocationType() != null)
        ? se.getLocationType().toString() : "other";
    if (location != null) {
      sb.append("<location type='");
      sb.append(escape(locationType));
      sb.append("'>");
      sb.append(escape(location));
      sb.append("</location>\n");
    }

    String internalReason = se.getInternalReason();
    if (internalReason != null) {
      sb.append("<internalReason>");
      sb.append(escape(internalReason));
      sb.append("</internalReason>\n");
    }

    String extendedHelp = se.getExtendedHelp();
    if (extendedHelp != null) {
      sb.append("<extendedHelp>");
      sb.append(escape(extendedHelp));
      sb.append("</extendedHelp>\n");
    }

    String sendReport = se.getSendReport();
    if (sendReport != null) {
      sb.append("<sendReport>");
      sb.append(escape(sendReport));
      sb.append("</sendReport>\n");
    }

    String debugInfo = se.getDebugInfo();
    if (debugInfo != null) {
      sb.append("<debugInfo>");
      sb.append(escape(debugInfo));
      sb.append("</debugInfo>\n");
    }

    sb.append("</error>\n");
  }

  /**
   * Return the internal HTTP headers in modifiable form.
   */
  public Map<String, List<String>> getHttpHeaders() { return httpHeaders; }


   // Override the default Throwable toString() implementation to add
   // the response body (either an explicitly set one or the default XML
   // error message) to the resulting output.  This is useful because the
   // Throwable toString() output is included in exception stack traces,
   // so this means the full response will be visible in traces.
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString());
    if (responseBody != null) {
      sb.append('\n');
      sb.append(responseBody);
    }
    //   purposes, but don't do this until all GData services are converted.
    return sb.toString();
  }

  // Error model getters and setters


  /**
   * Return error domain.
   * Defaults to "GData", indicating an error that has
   * not yet been upgraded to the new architecture.
   */
  public String getDomainName() {
    if (errorDomainName == null) {
      return "GData";
    } else {
      return errorDomainName;
    }
  }

  /**
   * Set error domain.
   */
  public void setDomain(String domain) {
    if (domain == null) {
      throw new NullPointerException("Null exception domain name");
    }
    errorDomainName = domain;
  }

  /**
   * Return error code.
   * Defaults to the class name of <tt>this</tt>.
   */
  public String getCodeName() {
    if (errorCodeName == null) {
      return this.getClass().getSimpleName();
    } else {
      return errorCodeName;
    }
  }

  /**
   * Set error code.
   */
  public void setCode(String code) {
    if (code == null) {
      throw new NullPointerException("Null exception code name");
    }
    errorCodeName = code;
  }

  /**
   * Return error location.
   * No default.
   */
  public String getLocation() {
    return errorLocation;
  }

  /** Enumerated constants for error location types. */
  public static enum LocationType {
    XPATH, OTHER, HEADER;

    @Override
    public String toString() {
      return super.toString().toLowerCase();
    }

  }

  public LocationType getLocationType() {
    return errorLocationType;
  }

  /**
   * Set XPath-based error location.
   * This must be a valid XPath expression sibling to the atom:entry
   * element (or the atom:feed element if we are not in an entry).
   */
  public void setXpathLocation(String location) {
    if (location == null) {
      throw new NullPointerException("Null exception location");
    }
    errorLocation = location;
    errorLocationType = LocationType.XPATH;
  }

  /**
   * Set header name for an error in a header.
   */
  public void setHeaderLocation(String location) {
    if (location == null) {
      throw new NullPointerException("Null exception location");
    }
    errorLocation = location;
    errorLocationType = LocationType.HEADER;
  }

  /**
   * Set generic error location.
   */
  public void setLocation(String location) {
    if (location == null) {
      throw new NullPointerException("Null exception location");
    }
    errorLocation = location;
    errorLocationType = LocationType.OTHER;
  }

  /**
   * Return error internal reason.
   * Defaults to the message set at construction time.
   */
  public String getInternalReason() {
    if (errorInternalReason == null) {
      return super.getMessage();
    } else {
      return errorInternalReason;
    }
  }

  /**
   * Return message: same as getInternalReason.
   */
  @Override
  public String getMessage() {
    return getInternalReason();
  }

  /**
   * Set error internal reason.
   */
  public void setInternalReason(String internalReason) {
    if (internalReason == null) {
      throw new NullPointerException("Null exception internal reason");
    }
    errorInternalReason = internalReason;
  }

  /**
   * Return URI for extended help
   * No default.
   */
  public String getExtendedHelp() {
    return errorExtendedHelp;
  }

  // Matches a valid Google URI.
  private static final String GOOGLE_URI_PATTERN = "http://.*google\\.com/.*";

  /**
   * Set URI for extended help.
   */
  public void setExtendedHelp(String extendedHelp) {
    if (extendedHelp == null) {
      throw new NullPointerException("Null exception extended help");
    }
    if (!extendedHelp.matches(GOOGLE_URI_PATTERN)) {
      throw new IllegalArgumentException("Invalid extended help URI");
    }
    errorExtendedHelp = extendedHelp;
  }

  /**
   * Return URI to send report to.
   * No default.
   */
  public String getSendReport() {
    return errorSendReport;
  }

  /**
   * Set URI to send report to.
   * No default.
   */
  public void setSendReport(String sendReport) {
    if (sendReport == null) {
      throw new NullPointerException("Null exception send report URI");
    }
    if (!sendReport.matches(GOOGLE_URI_PATTERN)) {
      throw new IllegalArgumentException("Invalid send report URI");
    }
    errorSendReport = sendReport;
  }

  /**
   * Return debugging information.
   * Defaults to the stack trace.
   */
  public String getDebugInfo() {
    // until we figure out how to determine who gets it.
    if (true) {
     return null;
    } else {
      if (errorDebugInfo == null) {
        return generateTrace(this, new StringBuilder(10000));
      } else {
        return errorDebugInfo;
      }
    }
  }

  /**
   * Set debugging information.
   */
  public void setDebugInfo(String debugInfo) {
    if (debugInfo == null) {
      throw new NullPointerException("Null exception debug info");
    }
    errorDebugInfo = debugInfo;
  }

  // Do what printStackTrace does, but to the StringBuilder.
  private String generateTrace(Throwable th, StringBuilder sb) {
    sb.append(toString());
    sb.append('\n');
    for (StackTraceElement element : getStackTrace()) {
      sb.append("\tat ");
      sb.append(element.toString());
      sb.append('\n');
    }
    Throwable cause = getCause();
    if (cause != null) {
      sb.append("Caused by: ");
      sb.append(cause.toString());
      sb.append('\n');
      generateTrace(cause, sb);
    }
    return sb.toString();
  }

  // Logic for handling sibling exceptions.

  // All the siblings, including this one.  We keep ourselves
  // in the sibling list so that all siblings can share a common
  // list.  There is no hierarchy among siblings, so throwing any
  // of them will produce the same result.

  /**
   * Return an unmodifiable copy of the sibling list.
   */
  public List<ServiceException> getSiblings() {
    return Collections.unmodifiableList(
        new ArrayList<ServiceException>(siblings));
  }

  /**
   * Make {@tt this} and {@tt newbie} siblings, returning {@tt this}.
   * All sibling exceptions are jointly converted to an
   * error message when any of them are thrown.
   */
  public ServiceException addSibling(ServiceException newbie) {
    if (newbie == null) {
      throw new NullPointerException("Null exception being added");
    }
    for (ServiceException newbieSibling : newbie.siblings) {
      if (!siblings.contains(newbieSibling)) {
        siblings.add(newbieSibling);
      }
      newbieSibling.siblings = siblings;
    }
    return this;
  }

  /**
   * Return true if this ServiceException matches the
   * specified {@link ErrorDomain.ErrorCode} in domain name and code name.
   * Sibling exceptions are not checked.
   */
  public boolean matches(ErrorDomain.ErrorCode errorCode) {
    return errorDomainName.equals(errorCode.getDomainName())
        && errorCodeName.equals(errorCode.getCodeName());
  }

  /**
   * Return true if this ServiceException or any of its
   * sibling exceptions matches the
   * specified {@link ErrorDomain.ErrorCode} in domain name and code name.
   * If you want to know <i>which</i> particular ServiceException
   * matched, call {@link #getSiblings} and examine the
   * individual ServiceExceptions with {@link #match}.
   */
  public boolean matchesAny(ErrorDomain.ErrorCode errorCode) {
    for (ServiceException se : siblings) {
      if (se.matches(errorCode)) {
        return true;
      }
    }
    return false;
  }

}
