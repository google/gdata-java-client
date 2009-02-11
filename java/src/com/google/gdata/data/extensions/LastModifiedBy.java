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

package com.google.gdata.data.extensions;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.Attribute;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Person;
import com.google.gdata.util.Namespaces;

import java.io.IOException;
import java.util.List;

/**
 * The user who last modified the object.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = LastModifiedBy.XML_NAME)
public class LastModifiedBy extends Person {

  /** XML element name **/
  static final String XML_NAME = "lastModifiedBy";
  
  /**
   * Default mutable constructor.
   */
  public LastModifiedBy() {
    super();
  }

  /**
   * Constructor (mutable or immutable).
   *
   * @param person the user
   */
  public LastModifiedBy(Person person) {
    super(person.getName(), person.getUri(), person.getEmail());
  }

  @Override
  protected void generate(XmlWriter w, ExtensionProfile p, XmlNamespace namespace,
      String localName, List<Attribute> attrs, AttributeGenerator generator) throws IOException {
    generate(p, w, namespace, localName, attrs);
  }
}
