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

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Qualified name of a data model element or attribute.  A qname instance is
 * immutable.
 */
public final class QName implements Comparable<QName> {
  
  /**
   * Special value for the QName namespace that will match any namespace.
   */
  public static final XmlNamespace ANY_NAMESPACE = new XmlNamespace("*", "*");
  
  /**
   * Special value for the QName local name that will match any local name.
   */
  public static final String ANY_LOCALNAME = "*";

  private final XmlNamespace namespace;
  private final String localName;

  public QName(String localName) {
    this(null, localName);
  }

  public QName(XmlNamespace namespace, String localName) {
    Preconditions.checkNotNull(localName, "localName");
    this.namespace = namespace;
    this.localName = localName;
  }

  public XmlNamespace getNs() { return namespace; }
  public String getLocalName() { return localName; }
  
  /**
   * Returns {@code true} if this qname has a namespace value that will match
   * any namespace.
   *
   * @see ANY_NAMESPACE
   */
  public boolean matchesAnyNamespace() {
    return ANY_NAMESPACE.equals(namespace);
  }

  /**
   * Returns {@code true} if this qname has a local name that will
   * match any local name.
   *
   * @see ANY_LOCALNAME
   */
  public boolean matchesAnyLocalName() {
    return ANY_LOCALNAME.equals(localName);
  }

  /**
   * Checks if this QName is a match for the other QName.  A QName is a match
   * if it is null and if 1) the local namespace is {@link ANY_NAMESPACE} or
   * the two namespaces are both null or have a matching uri and 2) the local
   * name is {@link ANY_LOCALNAME} or the two local names are equal.
   */
  public boolean matches(QName o) {
    if (o == null) {
      return false;
    }

    if (!matchesAnyNamespace()) {
      XmlNamespace otherNs = o.getNs();
      String idUri = (namespace == null) ? null : namespace.getUri();
      String otherUri = (otherNs == null) ? null : otherNs.getUri();

      // Check namespace uris.
      if (idUri == null) {
        if (otherUri != null) {
          return false;
        }
      } else if (!idUri.equals(otherUri)) {
        return false;
      }
    }

    // Check the local names.
    if (matchesAnyLocalName()) {
      return true;
    }
    return localName.equals(o.getLocalName());
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof QName)) {
      return false;
    }
    QName otherQName = (QName) other;
    if (this.getNs() == null && otherQName.getNs() == null) {
      return getLocalName().equals(otherQName.getLocalName());
    }
    if (this.getNs() != null && otherQName.getNs() != null) {
      return this.getNs().getUri().equals(otherQName.getNs().getUri())
             && getLocalName().equals(otherQName.getLocalName());
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (getNs() == null) {
      return getLocalName().hashCode();
    }
    return getNs().getUri().hashCode() * 13 + getLocalName().hashCode();
  }

  @Override
  public String toString() {
    if (getNs() == null || "".equals(getNs().getAlias())) {
      return getLocalName();
    }
    return getNs().getAlias() + ":" + getLocalName();
  }

  public int compareTo(QName o) {

    if (getNs() == null) {
      if (o.getNs() != null) {
        return -1;
      }
    } else {
      if (o.getNs() == null) {
        return 1;
      }
      int result = getNs().getUri().compareTo(o.getNs().getUri());
      if (result != 0) {
        if (ANY_NAMESPACE.equals(o.getNs())) {
          return -1;
        }
        return result;
      }
    }
    String localName = getLocalName();
    int compare = localName.compareTo(o.getLocalName());
    if (compare != 0 && ANY_LOCALNAME.equals(localName)) {
      return -1;
    }
    return compare;
  }
}
