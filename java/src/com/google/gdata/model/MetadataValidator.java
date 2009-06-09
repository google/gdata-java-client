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

import com.google.gdata.client.CoreErrorDomain;

/**
 * Default {@link ElementValidator} implementation that validates an
 * {@link Element} based upon constraints expressed in its
 * {@link ElementMetadata}.
 *
 * 
 */
public class MetadataValidator implements ElementValidator {

  public void validate(ValidationContext vc, Element e,
      ElementMetadata<?, ?> metadata) {

    ElementKey<?, ?> key = metadata.getKey();

    // Check text node content.
    if (e.hasTextValue()) {
      if (key.getDatatype() == Void.class) {
        vc.addError(e,
            CoreErrorDomain.ERR.invalidTextContent.withInternalReason(
                "Element " + key.getId() + " must not contain text content."));
      }

    } else if (key.getDatatype() != Void.class
        && metadata.isContentRequired()) {
      vc.addError(e, CoreErrorDomain.ERR.missingTextContent.withInternalReason(
          "Element " + key.getId() + " must contain a text content value."));
    }

    // Check that all required attributes are present.
    for (AttributeKey<?> attributeKey : metadata.getAttributes()) {
      AttributeMetadata<?> attMeta = metadata.bindAttribute(attributeKey);
      if (attMeta.isRequired() &&  e.getAttributeValue(attributeKey) == null) {
        vc.addError(e, CoreErrorDomain.ERR.missingAttribute.withInternalReason(
            "Element must contain value for attribute "
            + attributeKey.getId()));
      }
    }

    // Check that all required child elements are present.
    for (ElementKey<?, ?> childKey : metadata.getElements()) {
      ElementMetadata<?, ?> childMeta = metadata.bindElement(childKey);
      if (childMeta.isRequired() && !e.hasElement(childKey)) {
        vc.addError(e,
            CoreErrorDomain.ERR.missingExtensionElement.withInternalReason(
                "Element must contain a child named " + childKey.getId()));
      }
    }
  }
}
