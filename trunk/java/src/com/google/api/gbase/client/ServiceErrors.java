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

package com.google.api.gbase.client;

import com.google.gdata.util.ServiceException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.ParseException;
import com.google.gdata.data.HtmlTextConstruct;
import com.google.gdata.data.batch.BatchStatus;

import org.xml.sax.Attributes;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.io.StringReader;
import java.io.IOException;

/**
 * Extracts and organizes error messages from a
 * {@link com.google.gdata.util.ServiceException} or from a
 * {@link com.google.gdata.data.batch.BatchStatus}.
 *
 * This object parses the body of a ServiceException
 * or the content of a BatchStatus
 * and gives programmatic access to the error messages embedded in the body
 * of the exception.
 */
public class ServiceErrors {

  /** Errors with type != data. */
  private List<ServiceError> requestErrors = new ArrayList<ServiceError>();

  /** Errors with type = data. */
  private List<ServiceError> dataErrors = new ArrayList<ServiceError>();

  private static final ContentType DEFAULT_CONTENT_TYPE = 
      new ContentType("text/plain");


  /**
   * Returns a convenient text representation, for debugging.
   */
  @Override
  public String toString() {
    StringBuffer retval = new StringBuffer();
    appendErrors(retval, requestErrors);
    appendErrors(retval, dataErrors);
    return retval.toString();
  }

  private static void appendErrors(StringBuffer retval,
      List<ServiceError> list) {
    for (ServiceError error: list) {
      if (retval.length() > 0) {
        retval.append(", ");
      }
      retval.append(error.toString());
    }
  }

  /**
   * Creates a ServiceErrors object corresponding
   * to the errors contained in a {@link ServiceException}.
   *
   * @param e
   */
  public ServiceErrors(ServiceException e) {
    addErrors(e);
  }

  /**
   * Creates a ServiceErrors object corresponding
   * to the errors contained in {@link BatchStatus}.
   * 
   * @param status
   */
  public ServiceErrors(BatchStatus status) {
    addErrors(status);
  }
  
  /**
   * Empty constructor.
   */
  public ServiceErrors()
  {
  }

  /**
   * Extracts errors from a {@link ServiceException}.
   * 
   * @param e the ServiceException to be parsed
   */
  public void addErrors(ServiceException e) {
    addErrors(e.getMessage(), e.getResponseContentType(), e.getResponseBody());
  }

  /**
   * Extracts errors from a {@link BatchStatus}.
   * 
   * @param status the BatchStatus to be parsed
   */
  public void addErrors(BatchStatus status) {
    addErrors(status.getReason(), status.getContentType(), status.getContent());
  }

  /**
   * Registers errors.
   *
   * @param reason short error message, if nothing else helps, this
   *   will be used
   * @param contentType content type, if null the {@link #DEFAULT_CONTENT_TYPE}
   *   will be used
   * @param body extended error message, or null
   */
  private void addErrors(String reason, ContentType contentType, String body) {
    if (body == null) {
      addError(new ServiceError(reason));
      return;
    }

    if (contentType == null) {
      contentType = DEFAULT_CONTENT_TYPE;
    }

    if (contentType.toString().startsWith("application/xml")) {
      try {
        XmlParser parser = new XmlParser();
        parser.parse(new StringReader(body), new ErrorsElementHandler(),
            "", "errors");
      } catch (IOException ioe) {
        // This should never happen, but if it does, treat it
        // like a Parse error.
        addInvalidXmlServiceError(reason, body);
      } catch (ParseException pe) {
        // Best effort: display the original error message
        // as text. We're already handling an error, let's
        // not hide it with another one.
        addInvalidXmlServiceError(reason, body);
      }
    } else if (contentType.toString().startsWith("text/html")) {
      // This usually happens when the feed URL actually
      // points to a web page. Just get the error message
      // from the HTML as more usable plain text.
      HtmlTextConstruct construct = new HtmlTextConstruct(body);
      addError(new ServiceError(construct.getPlainText()));
    } else {
      // Treat it as plain text
      addError(new ServiceError(body));
    }
  }

  /**
   * If parsing the XML-formatted message failed, still
   * forward the message as a raw string, which is still
   * better than nothing...
   *
   * An invalid XML can be caused by either a bug on
   * the server or a wrong URL that would return an
   * XML file of a different nature.  
   *
   * @param reason
   * @param body
   */
  private void addInvalidXmlServiceError(String reason, String body) {
    addError(new ServiceError(reason +
        "(badly formatted xml error message: " + body));
  }

  /**
   * Registers a new error.
   *
   * @param error
   */
  public void addError(ServiceError error) {
    if (ServiceError.DATA_TYPE.equals(error.getType())) {
      dataErrors.add(error);
    } else {
      requestErrors.add(error);
    }
  }

  /**
   * Gets all errors.
   *
   * @return both request and data errors. May be empty but not null.
   */
  public List<? extends ServiceError> getAllErrors() {
    List<ServiceError> retval =
        new ArrayList<ServiceError>(requestErrors.size() + dataErrors.size());
    retval.addAll(requestErrors);
    retval.addAll(dataErrors);
    return retval;
  }

  /**
   * Gets non-data errors, which apply to the whole request.
   *
   * @return non-data errors. May be empty but not null.
   */
  public List<? extends ServiceError> getRequestErrors() {
    return requestErrors;
  }

  /**
   * Gets data errors, which apply to the item content, often
   * to one field in particular.
   *
   * @return data errors. May be empty but not null.
   */
  public Collection<? extends ServiceError> getDataErrors() {
    return dataErrors;
  }

  /**
   * Gets the set of all fields that have errors.
   *
   * @return field names. May be empty but not null.
   */
  public Set<? extends String> getErrorFields() {
    Set<String> fields = new HashSet<String>();
    for (ServiceError error: dataErrors) {
      String field = error.getField();
      if (field != null) {
        fields.add(field);
      }
    }
    return fields;
  }


  /**
   * Gets all errors for one specific field.
   *
   * @param field field name, which usually comes from
   *   {@link #getErrorFields()}
   * @return all errors applied to the field. May be empty but not null.
   */
  public List<? extends ServiceError> getFieldErrors(String field) {
    List<ServiceError> retval = new ArrayList<ServiceError>();
    for (ServiceError error: dataErrors) {
      if (equalsMaybeNull(field, error.getField())) {
        if (retval == null) {
          retval = new ArrayList<ServiceError>();
        }
        retval.add(error);
      }
    }
    return retval;
  }

  private static boolean equalsMaybeNull(String a, String b) {
    if (a == null) {
      return b == null;
    } else {
      return a.equals(b);
    }
  }

  /**
   * Handles the root element, {@code <errors>}.
   */
  private class ErrorsElementHandler extends XmlParser.ElementHandler {

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
        String localName, Attributes attrs) throws ParseException, IOException {
      if ("error".equals(localName)) {
        return new ErrorElementHandler(attrs);
      }
      return super.getChildHandler(namespace, localName, attrs);
    }
  }

  /**
   * Handles the element {@code <error>} and adds the corresponding
   * {@link ServiceError}.
   */
  private class ErrorElementHandler extends XmlParser.ElementHandler {
    public ErrorElementHandler(Attributes attrs) {
      addError(new ServiceError(attrs.getValue("type"),
          attrs.getValue("field"),
          attrs.getValue("reason")));
    }
  }
}
