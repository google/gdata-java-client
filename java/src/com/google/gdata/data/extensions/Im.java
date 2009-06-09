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

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;

/**
 * Describes an instant messaging address.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.gAlias,
    nsUri = Namespaces.g,
    localName = Im.XML_NAME)
public class Im extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "im";

  /** XML "address" attribute name */
  private static final String ADDRESS = "address";

  /** XML "label" attribute name */
  private static final String LABEL = "label";

  /** XML "primary" attribute name */
  private static final String PRIMARY = "primary";

  /** XML "protocol" attribute name */
  private static final String PROTOCOL = "protocol";

  /** XML "rel" attribute name */
  private static final String REL = "rel";

  /** IM address */
  private String address = null;

  /** Label */
  private String label = null;

  /** Whether this is the primary instant messaging address */
  private Boolean primary = null;

  /** Im protocol */
  private String protocol = null;

  /** Im type */
  private String rel = null;

  /** Im protocol. */
  public static final class Protocol {

    /** AIM. */
    public static final String AIM = Namespaces.gPrefix + "AIM";

    /** Google Talk. */
    public static final String GOOGLE_TALK = Namespaces.gPrefix + "GOOGLE_TALK";

    /** ICQ. */
    public static final String ICQ = Namespaces.gPrefix + "ICQ";

    /** Jabber. */
    public static final String JABBER = Namespaces.gPrefix + "JABBER";

    /** MSN. */
    public static final String MSN = Namespaces.gPrefix + "MSN";

    /** NetMeeting. */
    public static final String NETMEETING = Namespaces.gPrefix + "NETMEETING";

    /** Tencent QQ. */
    public static final String QQ = Namespaces.gPrefix + "QQ";

    /** Skype. */
    public static final String SKYPE = Namespaces.gPrefix + "SKYPE";

    /** Yahoo. */
    public static final String YAHOO = Namespaces.gPrefix + "YAHOO";

  }

  /** Im type. */
  public static final class Rel {

    /** Home instant messaging address. */
    public static final String HOME = Namespaces.gPrefix + "home";

    /** Other instant messaging address. */
    public static final String OTHER = Namespaces.gPrefix + "other";

    /** Work instant messaging address. */
    public static final String WORK = Namespaces.gPrefix + "work";

  }

  /**
   * Default mutable constructor.
   */
  public Im() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param address IM address.
   * @param label label.
   * @param primary whether this is the primary instant messaging address.
   * @param protocol im protocol.
   * @param rel im type.
   */
  public Im(String address, String label, Boolean primary, String protocol,
      String rel) {
    super();
    setAddress(address);
    setLabel(label);
    setPrimary(primary);
    setProtocol(protocol);
    setRel(rel);
    setImmutable(true);
  }

  /**
   * Returns the IM address.
   *
   * @return IM address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the IM address.
   *
   * @param address IM address or <code>null</code> to reset
   */
  public void setAddress(String address) {
    throwExceptionIfImmutable();
    this.address = address;
  }

  /**
   * Returns whether it has the IM address.
   *
   * @return whether it has the IM address
   */
  public boolean hasAddress() {
    return getAddress() != null;
  }

  /**
   * Returns the label.
   *
   * @return label
   */
  public String getLabel() {
    return label;
  }

  /**
   * Sets the label.
   *
   * @param label label or <code>null</code> to reset
   */
  public void setLabel(String label) {
    throwExceptionIfImmutable();
    this.label = label;
  }

  /**
   * Returns whether it has the label.
   *
   * @return whether it has the label
   */
  public boolean hasLabel() {
    return getLabel() != null;
  }

  /**
   * Returns the whether this is the primary instant messaging address.
   *
   * @return whether this is the primary instant messaging address
   */
  public Boolean getPrimary() {
    return primary;
  }

  /**
   * Sets the whether this is the primary instant messaging address.
   *
   * @param primary whether this is the primary instant messaging address or
   *     <code>null</code> to reset
   */
  public void setPrimary(Boolean primary) {
    throwExceptionIfImmutable();
    this.primary = primary;
  }

  /**
   * Returns whether it has the whether this is the primary instant messaging
   * address.
   *
   * @return whether it has the whether this is the primary instant messaging
   *     address
   */
  public boolean hasPrimary() {
    return getPrimary() != null;
  }

  /**
   * Returns the im protocol.
   *
   * @return im protocol
   */
  public String getProtocol() {
    return protocol;
  }

  /**
   * Sets the im protocol.
   *
   * @param protocol im protocol or <code>null</code> to reset
   */
  public void setProtocol(String protocol) {
    throwExceptionIfImmutable();
    this.protocol = protocol;
  }

  /**
   * Returns whether it has the im protocol.
   *
   * @return whether it has the im protocol
   */
  public boolean hasProtocol() {
    return getProtocol() != null;
  }

  /**
   * Returns the im type.
   *
   * @return im type
   */
  public String getRel() {
    return rel;
  }

  /**
   * Sets the im type.
   *
   * @param rel im type or <code>null</code> to reset
   */
  public void setRel(String rel) {
    throwExceptionIfImmutable();
    this.rel = rel;
  }

  /**
   * Returns whether it has the im type.
   *
   * @return whether it has the im type
   */
  public boolean hasRel() {
    return getRel() != null;
  }

  @Override
  protected void validate() {
    if (address == null) {
      throwExceptionForMissingAttribute(ADDRESS);
    }
  }

  /**
   * Returns the extension description, specifying whether it is required, and
   * whether it is repeatable.
   *
   * @param required   whether it is required
   * @param repeatable whether it is repeatable
   * @return extension description
   */
  public static ExtensionDescription getDefaultDescription(boolean required,
      boolean repeatable) {
    ExtensionDescription desc =
        ExtensionDescription.getDefaultDescription(Im.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(ADDRESS, address);
    generator.put(LABEL, label);
    generator.put(PRIMARY, primary);
    generator.put(PROTOCOL, protocol);
    generator.put(REL, rel);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    address = helper.consume(ADDRESS, true);
    label = helper.consume(LABEL, false);
    primary = helper.consumeBoolean(PRIMARY, false);
    protocol = helper.consume(PROTOCOL, false);
    rel = helper.consume(REL, false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    Im other = (Im) obj;
    return eq(address, other.address)
        && eq(label, other.label)
        && eq(primary, other.primary)
        && eq(protocol, other.protocol)
        && eq(rel, other.rel);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (address != null) {
      result = 37 * result + address.hashCode();
    }
    if (label != null) {
      result = 37 * result + label.hashCode();
    }
    if (primary != null) {
      result = 37 * result + primary.hashCode();
    }
    if (protocol != null) {
      result = 37 * result + protocol.hashCode();
    }
    if (rel != null) {
      result = 37 * result + rel.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{Im address=" + address + " label=" + label + " primary=" + primary
        + " protocol=" + protocol + " rel=" + rel + "}";
  }

}
