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


package com.google.gdata.data.appsforyourdomain;

import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_INTERNAL_ERROR;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * The AppsForYourDomainException indicates a failure in the use of the 
 * Google Apps for Your Domain API. 
 * 
 */
public class AppsForYourDomainException extends ServiceException {

  protected AppsForYourDomainErrorCode errorCode;
  protected String invalidInput;
  protected static DocumentBuilderFactory factory
      = DocumentBuilderFactory.newInstance();

  public AppsForYourDomainException(AppsForYourDomainErrorCode errorCode,
      String invalidInput, int httpReturnCode) {
    super("AppsForYourDomainException");
    if (errorCode == null) {
      if (httpReturnCode >= HTTP_BAD_REQUEST
          && httpReturnCode < HTTP_INTERNAL_ERROR) {
        this.errorCode = AppsForYourDomainErrorCode.InvalidValue; 
      } else {
        this.errorCode = AppsForYourDomainErrorCode.UnknownError;
      }
    } else {
      this.errorCode = errorCode;  
    }
    this.invalidInput = invalidInput;
  }
  
  public AppsForYourDomainException(AppsForYourDomainErrorCode errorCode,
      String invalidInput) {
    this(errorCode, invalidInput, 0);
  }

  public AppsForYourDomainException(AppsForYourDomainErrorCode errorCode) {
    this(errorCode, "");
  }

  public AppsForYourDomainException() {
    this(AppsForYourDomainErrorCode.UnknownError);
  }

  public AppsForYourDomainErrorCode getErrorCode() {
    return errorCode;
  }

  public String getInvalidInput() {
    return invalidInput;
  }
  
  @Override
  public String toString() {
    return errorCode.toString() + ": " + invalidInput;
  }

  /**
   * This is a helper method for Clients to use to obtain specific information
   * regarding a ServiceException. Method will return an
   * AppsForYourDomainException with populated errorCode and invalidInput
   * values, or it will return null if the ServiceException is not an
   * AppsForYourDomainException.
   */
  public static AppsForYourDomainException narrow(ServiceException se) {
    if ((se.getResponseContentType() == null) ||
        (se.getResponseBody() == null)) {
      return null;
    }
    
    if (se.getHttpErrorCodeOverride() == HttpURLConnection.HTTP_BAD_GATEWAY) {
      return new AppsForYourDomainException(
          AppsForYourDomainErrorCode.ServerBusy,
          "The server is busy and could not complete your request.  Please "
              + "try again in 30 seconds.");
    }

    // Check contentType and contentBody
    if ((se.getResponseContentType().equals(new ContentType("text/xml")))
        && (se.getResponseBody().contains("AppsForYourDomainErrors"))) {
      try {
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document =
            builder.parse(new InputSource(
                new StringReader(se.getResponseBody())));
        Element root = document.getDocumentElement();
        NodeList errorList = root.getElementsByTagName("error");
        Node error = errorList.item(0);
        NamedNodeMap attributes = error.getAttributes();

        // Create AppsForYourDomainException from Response Body.
        int errorCode =
            Integer.parseInt(attributes.getNamedItem("errorCode")
                .getNodeValue());
        String invalidInput =
            attributes.getNamedItem("invalidInput").getNodeValue();
        AppsForYourDomainException exception =
            new AppsForYourDomainException(AppsForYourDomainErrorCode
                .getEnumFromInt(errorCode), invalidInput, se.getHttpErrorCodeOverride());
        return exception;

        // If any exceptions occur, method will return null;
      } catch (NumberFormatException e) {
        return null;
      } catch (ParserConfigurationException e) {
        return null;
      } catch (SAXException e) {
        return null;
      } catch (IOException e) {
        return null;
      }
    }

    return null;
  }

}
