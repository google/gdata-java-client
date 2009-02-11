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


package com.google.gdata.util.common.xml;

import com.google.gdata.util.common.base.Preconditions;

/**
 * Represents an XML namespace, including the associated namespace URI and an
 * optional alias prefix to use in XML output.
 * 
 * 
 */
public class XmlNamespace {

  final String alias;
  final String uri;

  /** 
   * Constructs a new namespace with the specified namespace URI and no defined
   * prefix alias. 
   */
  public XmlNamespace(String uri) {
    this(null, uri);
  }
  
  /** 
   * Constructs a new namespace with the specified prefix alias and namespace
   * URI. 
   */
  public XmlNamespace(String alias, String uri) {
    Preconditions.checkNotNull(uri, "Null namespace URI");
    this.alias = alias;
    this.uri = uri;
  }

  /**
   * Returns the prefix alias for the namespace or {@code null} if undefined.
   * @returns namespace alias.
   */
  public final String getAlias() { return alias; }

  /**
   * Returns the fully qualified URI for the namespace.
   * @returns namespace URI
   */
  public final String getUri() { return uri; }

  /**
   * Returns true if the target object is a Namespace instance that has a
   * matching namespace URI and prefix (if specified).
   */
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof XmlNamespace)) {
      return false;
    }
    XmlNamespace other = (XmlNamespace) obj;
    if (alias == null) {
      return (other.alias == null) && uri.equals(other.uri);
    } else {
      return alias.equals(other.alias) && uri.equals(other.uri);
    }
  }

  @Override
  public int hashCode() {
    if (alias == null) {
      return uri.hashCode();
    } else {
      return alias.hashCode() & uri.hashCode();
    }
  }
}
