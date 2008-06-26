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


package com.google.gdata.data.youtube;

import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.InputStream;

/**
 * Answer to a 'get upload token' request.
 *
 * 
 */
public class FormUploadToken {
  private final String url;
  private final String token;

  /**
   * Creates a form-upload token.
   *
   * @param url the url to which the upload should be sent
   * @param token token data to include in the upload
   */
  public FormUploadToken(String url, String token) {
    this.url = url;
    this.token = token;
  }

  /**
   * Returns the url to which the form upload should be sent.
   *
   * @return a url that accepts POST of type multipart/form-data.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Returns a token to include in the form upload.
   *
   * @param token token data to include in the multipart/form-data
   *        upload under the name 'token'.
   */
  public String getToken() {
    return token;
  }

  /**
   * Parse a form-upload token from its XML definition.
   *
   * @param input InputStream of server response
   */
  public static FormUploadToken parse(InputStream input) throws ParseException, IOException {
    ResponseElementHandler handler = new ResponseElementHandler();
    new XmlParser().parse(input, handler, "", "response");
    if (handler.getUrl() == null) {
      throw new ParseException("Missing or empty 'url' element in response");
    }
    if (handler.getToken() == null) {
      throw new ParseException("Missing or empty 'token' element in response");
    }
    return new FormUploadToken(handler.getUrl(), handler.getToken());
  }

  /**
   * Handle a "response" XML tag and its two children: "url" and "token".
   */
  private static class ResponseElementHandler extends XmlParser.ElementHandler {
    private String url;
    private String token;

    public String getUrl() {
      return url;
    }

    public String getToken() {
      return token;
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
        String localName, Attributes attrs) throws ParseException, IOException {
      if ("".equals(namespace)) {
        if ("url".equals(localName)) {
          return new XmlParser.ElementHandler() {
            @Override
            public void processEndElement() throws ParseException {
              url = value;
            }
          };
        } else if ("token".equals(localName)) {
          return new XmlParser.ElementHandler() {
            @Override
            public void processEndElement() throws ParseException {
              token = value;
            }
          };
        }
      }
      return super.getChildHandler(namespace, localName, attrs);
    }
  }
}
