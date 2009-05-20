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


package com.google.gdata.model;

import com.google.gdata.util.common.base.Join;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.ErrorContent;
import com.google.gdata.util.ParseException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Data structure used to accumulate data model validation errors.
 * <p>
 * would be used as follows:
 * <pre>
 *   public interface Validatable {
 *     public static class ValidationContext { ... }
 *     public boolean validate(ValidationContext vc);
 *   }
 *
 *   public class Element implements Validatable { ... }
 * </pre>
 *
 *  Then we could eventually reuse the context class and the accumulation
 *  model in other places (attribute validation? metadata validation?).
 *  In that case, we might want to make the 1st arg of addError
 *  more generic (like 'Object relatedTo') or something.
 */
public class ValidationContext {

  /**
   * Collection of validation error messages associated with elements.
   */
  private Map<Element, List<ErrorContent>> errors = Maps.newLinkedHashMap();

  /**
   * @return map of validation errors
   */
  public Map<Element, List<ErrorContent>> getErrors() {
    return Collections.unmodifiableMap(errors);
  }

  /**
   * Add an error by error string.  This will register an error in the core
   * gdata domain with an error code of "invalidElement" and the internal reason
   * set to the given error string.  For a more useful error message,
   * {@link #addError(Element, ErrorContent)} should be used instead.
   */
  public void addError(Element element, String error) {
    addError(element,
        CoreErrorDomain.ERR.invalidExtension.withInternalReason(error));
  }

  /**
   * Add a validation error to list.
   *
   * @param element element that was found to be invalid
   * @param error error message
   */
  public void addError(Element element, ErrorContent error) {
    List<ErrorContent> list = errors.get(element);
    if (list == null) {
      list = Lists.newArrayList();
      errors.put(element, list);
    }
    list.add(error);
  }

  /**
   * @return true if instance data is valid
   */
  public boolean isValid() {
    return errors.isEmpty();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Map.Entry<Element, List<ErrorContent>> error : errors.entrySet()) {
      sb.append(error.getKey().getElementId());
      sb.append(" { ");
      List<String> errors = Lists.newArrayList();
      for (ErrorContent element : error.getValue()) {
        errors.add(new ParseException(element).toXmlErrorMessage());
      }
      sb.append(Join.join(", ", errors));
      sb.append(" }");
    }
    return sb.toString();
  }
}
