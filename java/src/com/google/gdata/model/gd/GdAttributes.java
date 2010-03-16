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


package com.google.gdata.model.gd;

import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.QName;

/**
 * The GdAttributes class contains common mix-in attributes that are defined
 * in the GD namespace but used to decorate elements in other namespaces.
 *
 * 
 */
public class GdAttributes {
  
  /**
   * The gd:etag attribute is placed on resources to return the entity tag
   * associated with the resource that can be used for conditional retrieval
   * and conditional update (if strong).
   *
   * See RFC 2616, Section 3.11.
   */
  public static final AttributeKey<String> ETAG = AttributeKey.of(
      new QName(Namespaces.gNs, "etag"));
  
  /**
   * The gd:kind attribute is placed on resources to indicate the GData
   * kind of the resource.   The attribute value contains the URI that
   * identifies the kind and, by association, and expected extension model
   * for the resource.
   */
  public static final AttributeKey<String> KIND = AttributeKey.of(
      new QName(Namespaces.gNs, "kind"));
  
  /**
   * The gd:fields attributes is placed on resources to indicate that the
   * resource representation is a partial representation of the full resource.
   * The attribute value will contain the GData selection that describes the
   * fields that are present in the resource.
   */
  public static final AttributeKey<String> FIELDS = AttributeKey.of(
      new QName(Namespaces.gNs, "fields"));
}
