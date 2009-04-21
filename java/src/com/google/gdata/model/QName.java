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
public class QName implements Comparable<QName> {

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
   * Checks if this QName is a match for the other QName.  A QName is a match
   * if it is null, or if it is in the same namespace as the other QName and
   * the localNames are either a match or the this localName is "*".
   */
  public boolean matches(QName o) {
    if (o == null) {
      return false;
    }
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

    // Check the local names.
    if ("*".equals(localName)) {
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
        return result;
      }
    }
    String localName = getLocalName();
    int compare = localName.compareTo(o.getLocalName());
    if (compare != 0 && "*".equals(localName)) {
      return -1;
    }
    return compare;
  }
}
