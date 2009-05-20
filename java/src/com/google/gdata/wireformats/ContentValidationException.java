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


package com.google.gdata.wireformats;

import com.google.gdata.model.Element;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ErrorContent;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * The ContentValidationException indicates that an instance
 * document does not conform to the content model.
 */
public class ContentValidationException extends ServiceException {

  protected ValidationContext vc;

  public ContentValidationException(String message, ValidationContext vc) {
    super(message);
    this.vc = vc;
    setResponse(ContentType.TEXT_PLAIN, vc.toString());
  }

  public ValidationContext getContext() {
    return vc;
  }

  /**
   * Converts a ContentValidationException to a parse exception, with a
   * separate exception for each parser error that was encountered.
   */
  public ParseException toParseException() {
    ParseException result = null;
    Map<Element, List<ErrorContent>> errors = vc.getErrors();
    for (Map.Entry<Element, List<ErrorContent>> entry : errors.entrySet()) {
      Element element = entry.getKey();
      String location = element.getElementKey().getId().toString();
      List<ErrorContent> codes = entry.getValue();
      for (ErrorContent errorCode : codes) {
        ParseException pe = new ParseException(errorCode);
        pe.setLocation(location);
        if (result == null) {
          result = pe;
        } else {
          result.addSibling(pe);
        }
      }
    }

    return result;
  }
}
