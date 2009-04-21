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

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Content model of a particular feed or entry.
 */
public class ContentModel {

  /**
   * Content model id.
   */
  protected String id;

  /**
   * Metadata of root element.
   */
  protected ElementMetadata<?, ?> root;

  /**
   * Default namespace.
   */
  protected XmlNamespace defaultNs;

  /**
   * Constructs a new metadata structure.
   * 
   * @param id content model id
   * @param root metadata of root element
   * @param defaultNs default namespace
   */
  public ContentModel(String id, ElementMetadata<?, ?> root,
                      XmlNamespace defaultNs) {
    this.id = id;
    this.root = root;
    this.defaultNs = defaultNs;
  }

  /**
   * @return content model id
   */
  public String getId() {
    return id;
  }

  /**
   * @return metadata of root element
   */
  public ElementMetadata<?, ?> getRoot() {
    return root;
  }

  /**
   * @return default namespace
   */
  public XmlNamespace getDefaultNs() {
    return defaultNs;
  }

  /**
   * Cardinality of an element.
   */
  public enum Cardinality { SINGLE, MULTIPLE, SET }

}
