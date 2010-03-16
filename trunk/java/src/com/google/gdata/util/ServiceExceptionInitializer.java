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

import com.google.gdata.util.ErrorContent.LocationType;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.zip.GZIPInputStream;

/**
 * The ServiceExceptionInitializer class is used to initialize
 * a ServiceException from either an HTTP connection or an
 * XML structured error represented as a string.
 *
 * 
 * 
 * 
 */
public class ServiceExceptionInitializer {

  // Set to the exception we begin by initializing;
  // initialized when this object is constructed.
  // WARNING: This object may not be fully constructed.
  // We set its instance variables, but you MUST NOT
  // invoke any instance methods on it.
  private final ServiceException initialException;

  // Set to the exception we are filling during parsing.
  // WARNING: This object may not be fully constructed.
  // We set its instance variables, but you MUST NOT
  // invoke any instance methods on it.
  private ServiceException currentException = null;

  // Constructor

  public ServiceExceptionInitializer(ServiceException se) {
    initialException = se;
  }

  /**
   * Initializes the ServiceException using the error response data from an
   * HTTP connection.  This constructor is used in the client library
   * to approximately reconstitute the constructor as it appeared
   * in the server.
   * @param httpConn is the http connection from which the error message
   *     (structured or simple) is read
   * @throws IOException if network error receiving the error response
   */
  public void parse(HttpURLConnection httpConn)
      throws ParseException, IOException {
    // Save response code
    initialException.httpErrorCodeOverride = httpConn.getResponseCode();

    // Save the HTTP headers in the exception.
    initialException.httpHeaders =
        Collections.unmodifiableMap(httpConn.getHeaderFields());

    // Save the response content type and body in the exception also
    ContentType responseContentType =
        new ContentType(httpConn.getContentType());
    initialException.responseContentType = responseContentType;

    StringBuilder sb;
    int responseLength = httpConn.getContentLength();
    if (responseLength < 0) {
      sb = new StringBuilder();
    } else if (responseLength > 0) {
      sb = new StringBuilder(responseLength);
    } else {  // no response data
      return;
    }

    // read an arbitrary response stream.
    InputStream responseStream =
        (initialException.httpErrorCodeOverride >= 400)
        ? httpConn.getErrorStream() : httpConn.getInputStream();
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
        String body = sb.toString();
        initialException.responseBody = body;
        parse(responseContentType, body);
      } finally {
        responseStream.close();
      }

    }
  }

  public void parse(ContentType contentType, String body)
      throws ParseException {
    if (ContentType.GDATA_ERROR.equals(contentType)) {
      XmlParser xp = new XmlParser();
      try {
        xp.parse(new StringReader(body), new ErrorsHandler(),
            "http://schemas.google.com/g/2005", "errors");
      } catch (IOException ioe) {
        // Impossible case: we are always parsing from a String
        throw new RuntimeException("Impossible parser I/O", ioe);
      }
    }
  }

  /**
   * Creates a new exception, registers it and sets {@code currentException}.
   * The first time this method is called, {@code currentException} is set
   * to {@code initialException}.
   * @return the new value of {@code currentException}
   */
  private ServiceException nextException() {
    // nextException/currentException
    if (currentException == null) {
      currentException = initialException;
      return currentException;
    } 
    try {
      // Create an exception of the same type as this one
      // and make it a sibling.
      currentException = initialException.getClass()
          .getConstructor(String.class).newInstance(
              initialException.getMessage());
      initialException.addSibling(currentException);
      return currentException;
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

  // ElementHandler subclasses for client-side parsing.

  private class ErrorsHandler extends ElementHandler {

    @Override
    public ElementHandler getChildHandler(String namespace, String localName,
        Attributes attrs) throws ParseException, IOException {
      if (Namespaces.g.equals(namespace)) {
        if ("error".equals(localName)) {
          nextException();
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
    @Override public void processEndElement() {
      currentException.errorElement.setDomain(value);
    }
  }

  private class CodeHandler extends ElementHandler {
    @Override public void processEndElement() {
      currentException.errorElement.setCode(value);
    }
  }

  private class LocationHandler extends ElementHandler {

    private LocationType locationType = LocationType.OTHER;

    @Override
    public void processAttribute(String namespace, String localName,
        String value) {
      if ("type".equals(localName)) {
        locationType = LocationType.fromString(value);
      }
    }

    @Override
    public void processEndElement() {
      currentException.errorElement.setLocation(value, locationType);
    }
  }

  private class InternalReasonHandler extends ElementHandler {
    @Override public void processEndElement() {
      currentException.errorElement.setInternalReason(value);
    }
  }

  private class ExtendedHelpHandler extends ElementHandler {
    @Override public void processEndElement() {
      currentException.errorElement.setExtendedHelp(value);
    }
  }

  private class SendReportHandler extends ElementHandler {
    @Override public void processEndElement() {
      currentException.errorElement.setSendReport(value);
    }
  }

  private class DebugInfoHandler extends ElementHandler {
    @Override public void processEndElement() {
      currentException.errorElement.setDebugInfo(value);
    }
  }
}
