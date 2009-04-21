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

import com.google.gdata.wireformats.XmlGenerator.ElementGenerator;

/**
 * The XmlWireFormatProperties class represents the collection of properties
 * that can be optionally configured to customize XML parsing and generation.
 */
public class XmlWireFormatProperties {
  
  // Element generator used to customize XML output for an element
  private ElementGenerator elementGenerator;
  
  /**
   * Sets the {@link ElementGenerator} that should be used emit the XML
   * for an element.   A value of {@code null} indicates that the
   * default XML generation algorithm should be used.
   * 
   * @param elementGenerator the element generator.
   * @returns the target properties instance (to allow chaining)
   */
  public XmlWireFormatProperties setElementGenerator(
      ElementGenerator elementGenerator) {
    this.elementGenerator = elementGenerator;
    return this;
  }
  
  /**
   * Returns the {@link ElementGenerator} that should be used emit the XML for
   * an element. A value of {@code null} indicates that the default XML
   * generation algorithm should be used.
   * 
   * @returns elementGenerator the element generator.
   */
  public ElementGenerator getElementGenerator() {
    return elementGenerator;
  }
}
