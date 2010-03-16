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


package com.google.gdata.model.atom;

import com.google.gdata.model.Element;
import com.google.gdata.model.gd.GdAttributes;
import com.google.gdata.util.Namespaces;

/**
 * The Kind class contains constants and static helper methods related to
 * GData kind processing 
 *
 * 
 */
class Kinds {
  
  /**
   * Returns the GData kind of a target entity by checking for the 
   * {@code gd:kind} attribute or a kind {@link Category} element.   If both
   * are present, the kind attribute value will have precedence and be returned.
   * 
   * @param source source feed or entry element to check
   * @return kind value or {@code null} if no kind information is found.
   */
  static String getElementKind(Element source) {
    String term = source.getAttributeValue(GdAttributes.KIND);
    if (term != null) {
      return term;
    }
    for (Category category : source.getElements(Category.KEY)) {
      if (Namespaces.gKind.equals(category.getScheme())) {
        return category.getTerm();
      }
    }
    return null;
  }
  
  // Not instantiable
  private Kinds() {}
}
