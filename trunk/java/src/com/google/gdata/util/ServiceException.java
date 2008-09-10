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
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * The ServiceException class is the base exception class used to
 * indicate an error while processing a GDataRequest.
 * The setters for the error model return <tt>this</tt>
 * to facilitate chaining.
 *
 * 
 * 
 */
public class ServiceException extends Exception {

  // Server-side constructors for reporting exceptions to client

  public ServiceException(String message) {
    super(message);
  }


  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }


  public ServiceException(Throwable cause) {
    super(cause.getMessage(), cause);
  }

  /**
   * Initializes the ServiceException using the error response data from an
   * HTTP connection.  This constructor is used in the client library
   * to approximately reconstitute the constructor as it appeared
   * in the server.
   */
  public ServiceException(HttpURLConnection httpConn) throws IOException {
    super(httpConn.getResponseMessage());

    // Save response code
    httpErrorCodeOverride = httpConn.getResponseCode();

    // Save the HTTP headers in the exception.
    httpHeaders = Collections.unmodifiableMap(httpConn.getHeaderFields());

    // Save the response content type and body in the exception also
    responseContentType = new ContentType(httpConn.getContentType());

    StringBuilder sb;
    int responseLength = httpConn.getContentLength();
    if (responseLength < 0) {
      sb = new StringBuilder();
    } else if (responseLength > 0) {
      sb = new StringBuilder(responseLength);
    } else {  // no response data
      return;
    }

    InputStream responseStream = (httpErrorCodeOverride >= 400) ?
        httpConn.getErrorStream() : httpConn.getInputStream();
    if (responseStream != null) {
      if ("gzip".equalsIgnoreCase(httpConn.getContentEncoding())) {
        responseStream = new GZIPInputStream(responseStream);
      }
      try {
        String charset = responseContentType.getAttributes().get("charset");
        if (charset == null) {
          charset = "iso8859-1";  // http default encoding
        }
        BufferedReader reader =
          new BufferedReader(new InputStreamReader(responseStream, charset));
        String responseLine;
        while ((responseLine = reader.readLine()) != null) {
          sb.append(responseLine);
          sb.append('\n');
        }
        setResponseBodyWithParsing(sb.toString());
      } finally {
        responseStream.close();
      }

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
    super(errorCode.getInternalReason());
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
    super(errorCode.getInternalReason(), cause);
    errorDomainName = errorCode.getDomainName();
    errorCodeName = errorCode.getCodeName();
    errorInternalReason = errorCode.getInternalReason();
    errorExtendedHelp = errorCode.getExtendedHelp();
    errorSendReport = errorCode.getSendReport();
  }

  /** HTTP error code. */
  protected int httpErrorCodeOverride = -1;
  public int getHttpErrorCodeOverride() { return httpErrorCodeOverride; }
  public void setHttpErrorCodeOverride(int v) { httpErrorCodeOverride = v; }


  /**
   * Optional HTTP content type for a response.
   */
  protected ContentType responseContentType;
  public ContentType getResponseContentType() {
    return responseContentType;
  }

  /**
   * Set optional HTTP content type to override default.
   * @deprecated Use {@link #setResponse} to set both content type and body
   */
  @Deprecated
  public void setResponseContentType(ContentType v) {
    responseContentType = v;
  }


  /**
   * Retrieve HTTP response body or error message.
   * The content type must be set correctly.
   */
  protected String responseBody;
  public String getResponseBody() {
    return responseBody;
  }

  /**
   * Set HTTP response body to override default.
   * @deprecated Use {@link #setResponse} to set both content type and body
   */
  @Deprecated
  public void setResponseBody(String body) {
    setResponseBodyWithParsing(body);
  }

  private void setResponseBodyWithParsing(String body) {
    responseBody = body;
    if (ContentType.GDATA_ERROR.equals(responseContentType)) {
      parseErrorMessage(responseBody);
    }
  }

  /**
   * Set HTTP response type and body simultaneously.
   * They are inherently coupled together: a body without a content type
   * is meaningless; a content type without a body is useless.
   */
  public void setResponse(ContentType contentType, String body) {
    responseContentType = contentType;
    setResponseBodyWithParsing(body);
  }

  /**
   * Parse an XML error message into the error model fields.
   * If there are multiple error elements, create multiple exceptions
   * and make them all siblings.
   */
  private void parseErrorMessage(String text) {
    try {
      XmlParser xp = new XmlParser();
      xp.parse(new StringReader(text), new ErrorsHandler(),
          "http://schemas.google.com/g/2005", "errors");
    } catch (ParseException pe) {
      // Capture this exception and null out the error fields
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
    } catch (IOException ioe) {
      // Impossible case: we are always parsing from a String
      throw new RuntimeException("Impossible parser I/O", ioe);
    }
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

  private void addXmlError(ServiceException se, StringBuilder sb) {
    // Simplistic StringBuffer implementation because the XML is trivial.
    sb.append("<error>\n");
    sb.append("<domain>");
    sb.append(CharEscapers.xmlEscaper().escape(se.getDomainName()));
    sb.append("</domain>\n");
    sb.append("<code>");
    sb.append(CharEscapers.xmlEscaper().escape(se.getCodeName()));
    sb.append("</code>\n");

    String location = se.getLocation();
    if (location != null) {
      sb.append("<location type='");
      sb.append(CharEscapers.xmlEscaper().escape(se.getLocationType()));
      sb.append("'>");
      sb.append(CharEscapers.xmlEscaper().escape(location));
      sb.append("</location>\n");
    }

    String internalReason = se.getInternalReason();
    if (internalReason != null) {
      sb.append("<internalReason>");
      sb.append(CharEscapers.xmlEscaper().escape(internalReason));
      sb.append("</internalReason>\n");
    }

    String extendedHelp = se.getExtendedHelp();
    if (extendedHelp != null) {
      sb.append("<extendedHelp>");
      sb.append(CharEscapers.xmlEscaper().escape(extendedHelp));
      sb.append("</extendedHelp>\n");
    }

    String sendReport = se.getSendReport();
    if (sendReport != null) {
      sb.append("<sendReport>");
      sb.append(CharEscapers.xmlEscaper().escape(sendReport));
      sb.append("</sendReport>\n");
    }

    String debugInfo = se.getDebugInfo();
    if (debugInfo != null) {
      sb.append("<debugInfo>");
      sb.append(CharEscapers.xmlEscaper().escape(debugInfo));
      sb.append("</debugInfo>\n");
    }

    sb.append("</error>\n");
  }


  /**
   * Optional HTTP headers to be output when HTTP response is generated.
   * It is a mapping from header name to list of header values.
   */
  private Map<String, List<String>> httpHeaders =
      new HashMap<String, List<String>>();
  public Map<String, List<String>> getHttpHeaders() { return httpHeaders; }


  /** Override the default Throwable toString() implementation to add
    * the response body (either an explicitly set one or the default XML
    * error message) to the resulting output.  This is useful because the
    * Throwable toString() output is included in exception stack traces,
    * so this means the full response will be visible in traces.
    */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.toString());
    if (responseBody != null) {
      sb.append('\n');
      sb.append(responseBody);
    }
    // Eventually include the XML body, but when?
    // sb.append('\n');
    // sb.append(toXmlErrorMessage());
    return sb.toString();
  }

  // Error model getters and setters

  private String errorDomainName;

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
    errorDomainName = domain;
  }

  private String errorCodeName;

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
    errorCodeName = code;
  }

  private String errorLocation;

  /**
   * Static inner class for location types.
   */
  public static class ErrorLocation {
    public static final String XPATH_TYPE = "xpath";
    public static final String HEADER_TYPE = "header";
    public static final String OTHER_TYPE = "other";
  }

  private String errorLocationType;

  /**
   * Return error location.
   * No default.
   */
  public String getLocation() {
    return errorLocation;
  }

  /** Return error location type.
   * Defaults to "other"
   */
  public String getLocationType() {
    return errorLocationType;
  }

  /**
   * Set XPath-based error location.
   * This must be a valid XPath expression sibling to the atom:entry
   * element (or the atom:feed element if we are not in an entry).
   */
  public void setXpathLocation(String location) {
    errorLocation = location;
    errorLocationType = ErrorLocation.XPATH_TYPE;
  }

  /**
   * Set header name for an error in a header.
   */
  public void setHeaderLocation(String location) {
    errorLocation = location;
    errorLocationType = ErrorLocation.HEADER_TYPE;
  }

  /**
    * Set generic error location.
    */
  public void setLocation(String location) {
    errorLocation = location;
    errorLocationType = ErrorLocation.OTHER_TYPE;
  }

  private String errorInternalReason;

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
    errorInternalReason = internalReason;
  }

  private String errorExtendedHelp;

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
    if (!extendedHelp.matches(GOOGLE_URI_PATTERN)) {
      throw new IllegalArgumentException("Invalid extended help URI");
    }
    errorExtendedHelp = extendedHelp;
  }

  private String errorSendReport;

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
    if (!sendReport.matches(GOOGLE_URI_PATTERN)) {
      throw new IllegalArgumentException("Invalid send report URI");
    }
    errorSendReport = sendReport;
  }

  private String errorDebugInfo;

  /**
   * Return debugging information.
   * Defaults to the stack trace.
   */
  public String getDebugInfo() {
    // until we figure out how to determine who gets it.
    if ("true".equals("true")) {
      return null;
    }
    if (errorDebugInfo == null) {
      return generateTrace(this, new StringBuilder(10000));
    } else {
      return errorDebugInfo;
    }
  }

  /**
    * Set debugging information.
    */
  public void setDebugInfo(String debugReport) {
    errorDebugInfo = debugReport;
  }

  // Do what printStackTrace does, but to the StringBuilder.
  private String generateTrace(Throwable th, StringBuilder sb) {
    sb.append(toString());
    sb.append('\n');
    StackTraceElement[] trace = getStackTrace();
    for (int i = 0; i < trace.length; i++) {
      sb.append("\tat ");
      sb.append(trace[i].toString());
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

  // ElementHandler subclasses for client-side parsing

  // Set to the exception we are filling during parsing.
  private ServiceException currentException = null;
  private final ServiceException thisException = this;

  private class ErrorsHandler extends ElementHandler {

    @Override
    public ElementHandler getChildHandler(String namespace, String localName,
        Attributes attrs) throws ParseException, IOException {
      if ("http://schemas.google.com/g/2005".equals(namespace)) {
        if ("error".equals(localName)) {
          if (currentException == null) {
            currentException = thisException;
          } else {
            try {
              // Create an exception of the same type as this one
              // and make it a sibling.
              currentException = thisException.getClass().
                  getConstructor(String.class).newInstance(getMessage());
              thisException.addSibling(currentException);
            } catch (InstantiationException ie) {
              throw new IllegalStateException(ie);
            } catch (NoSuchMethodException nsme) {
              throw new IllegalStateException(nsme);
            } catch (IllegalAccessException iae) {
              throw new IllegalStateException(iae);
            } catch (InvocationTargetException ite) {
              throw new IllegalStateException(ite);
            }
          }
          return new ErrorHandler();
        }
      }
      return super.getChildHandler(namespace, localName, attrs);
    }

  }

  private class ErrorHandler extends ElementHandler {

    @Override
    public ElementHandler getChildHandler(String namespace, String localName,
        Attributes attrs) throws ParseException, IOException {
      if ("http://schemas.google.com/g/2005".equals(namespace)) {
        if ("domain".equals(localName)) {
          return new DomainHandler();
        } else if ("code".equals(localName)) {
          return new CodeHandler();
        } else if ("location".equals(localName)) {
          return new LocationHandler();
        } else if ("internalReason".equals(localName)) {
          return new InternalReasonHandler();
        } else if ("extendedHelp".equals(localName)) {
          return new ExtendedHelpHandler();
        } else if ("sendReport".equals(localName)) {
          return new SendReportHandler();
        } else if ("debugInfo".equals(localName)) {
           return new DebugInfoHandler();
        }
      }
      return super.getChildHandler(namespace, localName, attrs);
    }
  }

  private class DomainHandler extends ElementHandler {

    @Override
    public void processEndElement() {
      currentException.errorDomainName = value;
    }

  }

  private class CodeHandler extends ElementHandler {

    @Override
    public void processEndElement() {
      currentException.errorCodeName = value;
    }

  }

  private class LocationHandler extends ElementHandler {

    @Override
    public void processAttribute(String namespace, String localName,
        String value) throws ParseException {
      if ("type".equals(localName)) {
        currentException.errorLocationType = value;
      }
    }

    @Override
    public void processEndElement() {
      currentException.errorLocation = value;
      if (errorLocationType == null) {
        currentException.errorLocationType = ErrorLocation.OTHER_TYPE;
      }
    }

  }

  private class InternalReasonHandler extends ElementHandler {

    @Override
    public void processEndElement() {
      currentException.errorInternalReason = value;
    }

  }

  private class ExtendedHelpHandler extends ElementHandler {

    @Override
    public void processEndElement() {
      currentException.errorExtendedHelp = value;
    }

  }

  private class SendReportHandler extends ElementHandler {

    @Override
    public void processEndElement() {
      currentException.errorSendReport = value;
    }

  }

  private class DebugInfoHandler extends ElementHandler {

    @Override
    public void processEndElement() {
      currentException.errorDebugInfo = value;
    }

  }

  // Logic for handling sibling exceptions.

  // All the siblings, including this one.  We keep ourselves
  // in the sibling list so that all siblings can share a common
  // list.  There is no hierarchy among siblings, so throwing any
  // of them will produce the same result.

  private List<ServiceException> siblings =
      new ArrayList<ServiceException>(1);
  {
    siblings.add(this);
  }

  /**
   * Return an unmodifiable version of the sibling list.
   */
  public List<ServiceException> getSiblings() {
    return Collections.unmodifiableList(siblings);
  }

  /**
   * Make {@tt this} and {@tt newbie} siblings, returning {@tt this}.
   * All sibling exceptions are jointly converted to an
   * error message when any of them are thrown.
   **/
  public ServiceException addSibling(ServiceException newbie) {
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
    return errorDomainName.equals(errorCode.getDomainName()) &&
        errorCodeName.equals(errorCode.getCodeName());
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
