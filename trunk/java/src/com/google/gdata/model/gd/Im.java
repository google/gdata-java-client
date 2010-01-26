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
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes an instant messaging address.
 *
 * 
 */
public class Im extends Element {

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

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      AIM,
      GOOGLE_TALK,
      ICQ,
      JABBER,
      MSN,
      NETMEETING,
      QQ,
      SKYPE,
      YAHOO};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Protocol() {}
  }

  /** Im type. */
  public static final class Rel {

    /** Home instant messaging address. */
    public static final String HOME = Namespaces.gPrefix + "home";

    /** Other instant messaging address. */
    public static final String OTHER = Namespaces.gPrefix + "other";

    /** Work instant messaging address. */
    public static final String WORK = Namespaces.gPrefix + "work";

    /** Array containing all available values. */
    private static final String[] ALL_VALUES = {
      HOME,
      OTHER,
      WORK};

    /** Returns an array of all values defined in this class. */
    public static String[] values() {
      return ALL_VALUES;
    }

    private Rel() {}
  }

  /**
   * The key for this element.
   */
  public static final ElementKey<Void,
      Im> KEY = ElementKey.of(new QName(Namespaces.gNs, "im"), Void.class,
      Im.class);

  /**
   * IM address.
   */
  public static final AttributeKey<String> ADDRESS = AttributeKey.of(new
      QName(null, "address"), String.class);

  /**
   * Label.
   */
  public static final AttributeKey<String> LABEL = AttributeKey.of(new
      QName(null, "label"), String.class);

  /**
   * Whether this is the primary instant messaging address.
   */
  public static final AttributeKey<Boolean> PRIMARY = AttributeKey.of(new
      QName(null, "primary"), Boolean.class);

  /**
   * Im protocol.
   */
  public static final AttributeKey<String> PROTOCOL = AttributeKey.of(new
      QName(null, "protocol"), String.class);

  /**
   * Im type.
   */
  public static final AttributeKey<String> REL = AttributeKey.of(new QName(null,
      "rel"), String.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    // The builder for this element
    ElementCreator builder = registry.build(KEY);

    // Local properties
    builder.addAttribute(ADDRESS).setRequired(true);
    builder.addAttribute(LABEL);
    builder.addAttribute(PRIMARY);
    builder.addAttribute(PROTOCOL);
    builder.addAttribute(REL);
  }

  /**
   * Constructs an instance using the default key.
   */
  public Im() {
    super(KEY);
  }

  /**
   * Subclass constructor, allows subclasses to supply their own element key.
   */
  protected Im(ElementKey<?, ? extends Im> key) {
    super(key);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link Element} instance. Will use the given {@link ElementKey} as the key
   * for the element. This constructor is used when adapting from one element
   * key to another. You cannot call this constructor directly, instead use
   * {@link Element#createElement(ElementKey, Element)}.
   *
   * @param key The key to use for this element.
   * @param source source element
   */
  protected Im(ElementKey<?, ? extends Im> key, Element source) {
    super(key, source);
  }

  @Override
  public Im lock() {
    return (Im) super.lock();
  }

  /**
   * Returns the IM address.
   *
   * @return IM address
   */
  public String getAddress() {
    return super.getAttributeValue(ADDRESS);
  }

  /**
   * Sets the IM address.
   *
   * @param address IM address or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Im setAddress(String address) {
    super.setAttributeValue(ADDRESS, address);
    return this;
  }

  /**
   * Returns whether it has the IM address.
   *
   * @return whether it has the IM address
   */
  public boolean hasAddress() {
    return super.hasAttribute(ADDRESS);
  }

  /**
   * Returns the label.
   *
   * @return label
   */
  public String getLabel() {
    return super.getAttributeValue(LABEL);
  }

  /**
   * Sets the label.
   *
   * @param label label or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Im setLabel(String label) {
    super.setAttributeValue(LABEL, label);
    return this;
  }

  /**
   * Returns whether it has the label.
   *
   * @return whether it has the label
   */
  public boolean hasLabel() {
    return super.hasAttribute(LABEL);
  }

  /**
   * Returns the whether this is the primary instant messaging address.
   *
   * @return whether this is the primary instant messaging address
   */
  public Boolean getPrimary() {
    return super.getAttributeValue(PRIMARY);
  }

  /**
   * Sets the whether this is the primary instant messaging address.
   *
   * @param primary whether this is the primary instant messaging address or
   *     {@code null} to reset
   * @return this to enable chaining setters
   */
  public Im setPrimary(Boolean primary) {
    super.setAttributeValue(PRIMARY, primary);
    return this;
  }

  /**
   * Returns whether it has the whether this is the primary instant messaging
   * address.
   *
   * @return whether it has the whether this is the primary instant messaging
   *     address
   */
  public boolean hasPrimary() {
    return super.hasAttribute(PRIMARY);
  }

  /**
   * Returns the im protocol.
   *
   * @return im protocol
   */
  public String getProtocol() {
    return super.getAttributeValue(PROTOCOL);
  }

  /**
   * Sets the im protocol.
   *
   * @param protocol im protocol or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Im setProtocol(String protocol) {
    super.setAttributeValue(PROTOCOL, protocol);
    return this;
  }

  /**
   * Returns whether it has the im protocol.
   *
   * @return whether it has the im protocol
   */
  public boolean hasProtocol() {
    return super.hasAttribute(PROTOCOL);
  }

  /**
   * Returns the im type.
   *
   * @return im type
   */
  public String getRel() {
    return super.getAttributeValue(REL);
  }

  /**
   * Sets the im type.
   *
   * @param rel im type or {@code null} to reset
   * @return this to enable chaining setters
   */
  public Im setRel(String rel) {
    super.setAttributeValue(REL, rel);
    return this;
  }

  /**
   * Returns whether it has the im type.
   *
   * @return whether it has the im type
   */
  public boolean hasRel() {
    return super.hasAttribute(REL);
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
    return eq(getAddress(), other.getAddress())
        && eq(getLabel(), other.getLabel())
        && eq(getPrimary(), other.getPrimary())
        && eq(getProtocol(), other.getProtocol())
        && eq(getRel(), other.getRel());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getAddress() != null) {
      result = 37 * result + getAddress().hashCode();
    }
    if (getLabel() != null) {
      result = 37 * result + getLabel().hashCode();
    }
    if (getPrimary() != null) {
      result = 37 * result + getPrimary().hashCode();
    }
    if (getProtocol() != null) {
      result = 37 * result + getProtocol().hashCode();
    }
    if (getRel() != null) {
      result = 37 * result + getRel().hashCode();
    }
    return result;
  }
}


