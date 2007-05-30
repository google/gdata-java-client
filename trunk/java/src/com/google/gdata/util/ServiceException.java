/* Copyright (c) 2006 Google Inc.
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * The ServiceException class is the base exception class used to
 * indicate an error while processing a GDataRequest.
 *
 * 
 * 
 */
public class ServiceException extends Exception {


  public ServiceException(String message) {
    super(message);
  }


  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }


  public ServiceException(Throwable cause) {
    super(cause);
  }

  /**
   * Initializes the ServiceException using the error response data from an 
   * HTTP connection.
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
        responseBody = sb.toString();
      } finally {
        responseStream.close();
      }
    }
  }


   /** HTTP error code. */
  protected int httpErrorCodeOverride = -1;
  public int getHttpErrorCodeOverride() { return httpErrorCodeOverride; }
  public void setHttpErrorCodeOverride(int v) { httpErrorCodeOverride = v; }


  /** Optional HTTP content type for a response. */
  protected ContentType responseContentType;
  public ContentType getResponseContentType() { return responseContentType; }
  public void setResponseContentType(ContentType v) { responseContentType = v; }


  /** Optional HTTP response body. Requires the content type to be set. */
  protected String responseBody;
  public String getResponseBody() { return responseBody; }
  public void setResponseBody(String v) { responseBody = v; }
  

  /**
   * Optional HTTP headers to be output when HTTP response is generated.
   * It is a mapping from header name to list of header values.
   */
  private Map<String, List<String>> httpHeaders =
      new HashMap<String, List<String>>();
  public Map<String, List<String>> getHttpHeaders() { return httpHeaders; }


  // Override the default Throwable toString() implementation to add the
  // response body (if any) to the resulting output.  This is useful because
  // the Throwable toString() output is included in exception stack traces,
  // so this means the full response will be visible in traces.
  public String toString() {

    if (responseBody == null) {
      return super.toString();
    }

    StringBuilder sb = new StringBuilder();
    sb.append(super.toString());
    sb.append("\n");
    sb.append(responseBody);
    return sb.toString();
  }
}
