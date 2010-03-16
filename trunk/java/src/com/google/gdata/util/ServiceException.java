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
import com.google.gdata.client.authn.oauthproxy.OAuthProxyProtocol;
import com.google.gdata.client.authn.oauthproxy.OAuthProxyResponse;
import com.google.gdata.util.ErrorContent.LocationType;

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

  // Structured error element.
  ErrorElement errorElement = new ErrorElement();

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
    this(message);
    initCause(cause);
  }

  public ServiceException(Throwable cause) {
    this(cause.getMessage());
    initCause(cause);
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
      errorElement = new ErrorElement();
      siblings.clear();
      siblings.add(this);
      responseContentType = ContentType.TEXT_PLAIN;
    }
  }

  /**
   * Initializes the ServiceException using an {@link ErrorContent} object that
   * encapsulates most of the information about the error.  Most ErrorContent
   * instances are declared in a subclass of {@link ErrorDomain} containing all
   * the errors for a GData domain (service or portion of service).
   */
  public ServiceException(ErrorContent errorCode) {
    this(errorCode.getInternalReason());
    this.errorElement = new ErrorElement(errorCode);
  }

  /**
   * Initializes the ServiceException using an {@link ErrorContent} object that
   * encapsulates most of the information about the error, and an embedded
   * exception.  Most ErrorContent instances are declared in a subclass of
   * {@link ErrorDomain} containing all the errors for this GData domain
   * (service or portion of service).
   */
  public ServiceException(ErrorContent errorCode, Throwable cause) {
    this(errorCode);
    initCause(cause);
  }

  /**
   * {@inheritDoc}
   *
   * <p>If {@code cause} is a {@link ServiceException}, it is added to
   * the list of siblings so that the original exception appears when
   * using XML error output.
   */
  @Override
  public Throwable initCause(Throwable cause) {
    super.initCause(cause);
    if (cause instanceof ServiceException) {
      addSibling((ServiceException) cause);
    }
    return this;
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

  /**
   * Converts the exception into a well-formated XML error
   * message suitable for external uses.
   */
  public String toXmlErrorMessage() {
    return toXmlErrorMessage(false);
  }

  /**
   * Converts the exception into a well-formated XML error
   * message suitable for external uses.
   *
   * @param includeDebugInfo if {@code true}, include debug information.
   *        Such error message should only be returned to internal clients.
   */
  public String toXmlErrorMessage(boolean includeDebugInfo) {
    StringBuilder sb = new StringBuilder();
    sb.append("<errors xmlns='http://schemas.google.com/g/2005'>\n");
    for (ServiceException sibling : siblings) {
      addXmlError(sibling, sb, includeDebugInfo);
    }
    sb.append("</errors>\n");
    return sb.toString();
  }

  private String escape(String src) {
    return CharEscapers.xmlEscaper().escape(src);
  }

  private void addXmlError(ServiceException se, StringBuilder sb,
      boolean includeDebugInfo) {
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
    LocationType locationType = se.getLocationTypeWithDefault();
    if (location != null) {
      sb.append("<location type='");
      sb.append(escape(locationType.toString()));
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

    if (includeDebugInfo) {
      String debugInfo = se.getDebugInfo();
      if (debugInfo != null) {
        sb.append("<debugInfo>");
        sb.append(escape(debugInfo));
        sb.append("</debugInfo>\n");
      }
    }

    sb.append("</error>\n");
  }

  /**
   * Return the internal HTTP headers in modifiable form.
   */
  public Map<String, List<String>> getHttpHeaders() { return httpHeaders; }


  /**
   * Return the value for requested internal HTTP header or {@code null} if
   * the header is not present.
   *
   * @param header requested header name
   * @return list of string values for the specified header or {@code null} if
   *         requested header is not found.
   */
  public List<String> getHttpHeader(String header) {
    if (header == null) {
      return httpHeaders.get(header);
    }
    for (String key : httpHeaders.keySet()) {
      if (key != null && key.toLowerCase().equals(header.toLowerCase())) {
        return httpHeaders.get(key);
      }
    }
    return null;
  }

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
   *
   * <p>Defaults to "GData", indicating an error that has
   * not yet been upgraded to the new architecture.
   */
  public String getDomainName() {
    String domainName = errorElement.getDomainName();
    return (domainName != null) ? domainName : "GData";
  }

  /**
   * Set error domain.
   *
   * @throws NullPointerException if {@code domain} is {@code null}.
   */
  public void setDomain(String domain) {
    errorElement.setDomain(domain);
  }

  /**
   * Return error code.
   *
   * <p>Defaults to the class name of {@code this}.
   */
  public String getCodeName() {
    String codeName = errorElement.getCodeName();
    return (codeName != null) ? codeName : getClass().getSimpleName();
  }

  /**
   * Set error code.
   *
   * @throws NullPointerException if {@code code} is {@code null}.
   */
  public void setCode(String code) {
    errorElement.setCode(code);
  }

  /**
   * Return error location.
   */
  public String getLocation() {
    return errorElement.getLocation();
  }

  /**
   * Return error location type.
   */
  public LocationType getLocationType() {
    return errorElement.getLocationType();
  }

  /**
   * Returns error location type, defaulting to {@link LocationType#OTHER}.
   */
  private LocationType getLocationTypeWithDefault() {
    LocationType type = getLocationType();
    return type != null ? type : LocationType.OTHER;
  }

  /**
   * Set XPath-based error location.
   * This must be a valid XPath expression sibling to the atom:entry
   * element (or the atom:feed element if we are not in an entry).
   *
   * @throws NullPointerException if {@code location} is {@code null}.
   */
  public void setXpathLocation(String location) {
    errorElement.setXpathLocation(location);
  }

  /**
   * Set header name for an error in a header.
   *
   * @throws NullPointerException if {@code location} is {@code null}.
   */
  public void setHeaderLocation(String location) {
    errorElement.setHeaderLocation(location);
  }

  /**
   * Set generic error location.
   *
   * @throws NullPointerException if {@code location} is {@code null}.
   */
  public void setLocation(String location) {
    errorElement.setLocation(location);
  }

  /**
   * Return error internal reason.
   *
   * <p>Defaults to the message set at construction time.
   */
  public String getInternalReason() {
    String internalReason = errorElement.getInternalReason();
    return (internalReason != null) ? internalReason : super.getMessage();
  }

  /**
   * Return message: same as getInternalReason.
   */
  @Override
  public String getMessage() {
    return getInternalReason();
  }

  /**
   * Sets the internal reason of the error.
   *
   * @throws NullPointerException if {@code internalReason} is {@code null}.
   */
  public void setInternalReason(String internalReason) {
    errorElement.setInternalReason(internalReason);
  }

  /**
   * Return URI for extended help
   */
  public String getExtendedHelp() {
    return errorElement.getExtendedHelp();
  }

  /**
   * Set URI for extended help.
   *
   * @throws NullPointerException if {@code extendedHelp} is {@code null}.
   */
  public void setExtendedHelp(String extendedHelp) {
    errorElement.setExtendedHelp(extendedHelp);
  }

  /**
   * Return URI to send report to.
   */
  public String getSendReport() {
    return errorElement.getSendReport();
  }

  /**
   * Set URI to send report to.
   *
   * @throws NullPointerException if {@code sendReport} is {@code null}.
   */
  public void setSendReport(String sendReport) {
    errorElement.setSendReport(sendReport);
  }

  /**
   * Return debugging information.
   * Defaults to the stack trace.
   */
  public String getDebugInfo() {
    return errorElement.getDebugInfo();
  }

  /**
   * Set debugging information.
   *
   * @throws NullPointerException if {@code debugInfo} is {@code null}.
   */
  public void setDebugInfo(String debugInfo) {
    errorElement.setDebugInfo(debugInfo);
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
   * Return true if this ServiceException matches the specified
   * {@link ErrorContent} in domain name and code name. Sibling exceptions are
   * not checked.
   */
  public boolean matches(ErrorContent code) {
    return getDomainName().equals(code.getDomainName())
        && getCodeName().equals(code.getCodeName());
  }

  /**
   * Return true if this ServiceException or any of its sibling exceptions
   * matches the specified {@link ErrorContent} in domain name and code name.
   * If you want to know <i>which</i> particular ServiceException matched, call
   * {@link #getSiblings} and examine the individual ServiceExceptions with
   * {@link #matches}.
   */
  public boolean matchesAny(ErrorContent errorCode) {
    for (ServiceException se : siblings) {
      if (se.matches(errorCode)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns whether or not the request has OAuth Proxy-related headers.
   */
  public boolean hasOAuthProxyResponse() {
    return httpHeaders.containsKey(
        OAuthProxyProtocol.Header.X_OAUTH_APPROVAL_URL)
    || httpHeaders.containsKey(OAuthProxyProtocol.Header.X_OAUTH_STATE)
    || httpHeaders.containsKey(OAuthProxyProtocol.Header.X_OAUTH_ERROR)
    || httpHeaders.containsKey(OAuthProxyProtocol.Header.X_OAUTH_ERROR);
  }

  /**
   * Returns the headers related to the OAuth Proxy.  If there are no OAuth
   * Proxy related headers on the request, this method will return an
   * {@link OAuthProxyResponse} object with null values for the headers.  Use
   * {@link #hasOAuthProxyResponse()} to determine whether the request has
   * OAuth Proxy information.
   */
  public OAuthProxyResponse getOAuthProxyResponse() {
    return new OAuthProxyResponse(httpHeaders);
  }
}
