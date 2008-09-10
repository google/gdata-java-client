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


package com.google.gdata.data.threading;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionPoint;
import com.google.gdata.util.ParseException;

/**
 * Indicates that this entry is a response to another resource.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = ThreadingNamespace.THR_ALIAS,
    nsUri = ThreadingNamespace.THR,
    localName = InReplyTo.XML_NAME)
public class InReplyTo extends ExtensionPoint {

  /** XML element name */
  static final String XML_NAME = "in-reply-to";

  /** XML "href" attribute name */
  private static final String HREF = "href";

  /** XML "ref" attribute name */
  private static final String REF = "ref";

  /** XML "source" attribute name */
  private static final String SOURCE = "source";

  /** XML "type" attribute name */
  private static final String TYPE = "type";

  /** IRI that may be used to retrieve a representation of the resource being
   * responded to */
  private String href = null;

  /** Persistent universally unique identifier of the resource being responded
   * to */
  private String ref = null;

  /** IRI of an Atom Feed or Entry Document containing an atom:entry with an
   * atom:id value equal to the value of the "ref" attribute */
  private String source = null;

  /** Hint to the client about the media type of the resource identified by the
   * "href" attribute */
  private String type = null;

  /**
   * Default mutable constructor.
   */
  public InReplyTo() {
    super();
  }

  /**
   * Immutable constructor.
   *
   * @param href IRI that may be used to retrieve a representation of the
   *     resource being responded to.
   * @param ref persistent universally unique identifier of the resource being
   *     responded to.
   * @param source IRI of an Atom Feed or Entry Document containing an
   *     atom:entry with an atom:id value equal to the value of the "ref"
   *     attribute.
   * @param type hint to the client about the media type of the resource
   *     identified by the "href" attribute.
   */
  public InReplyTo(String href, String ref, String source, String type) {
    super();
    setHref(href);
    setRef(ref);
    setSource(source);
    setType(type);
    setImmutable(true);
  }

  /**
   * Returns the IRI that may be used to retrieve a representation of the
   * resource being responded to.
   *
   * @return IRI that may be used to retrieve a representation of the resource
   *     being responded to
   */
  public String getHref() {
    return href;
  }

  /**
   * Sets the IRI that may be used to retrieve a representation of the resource
   * being responded to.
   *
   * @param href IRI that may be used to retrieve a representation of the
   *     resource being responded to or <code>null</code> to reset
   */
  public void setHref(String href) {
    throwExceptionIfImmutable();
    this.href = href;
  }

  /**
   * Returns whether it has the IRI that may be used to retrieve a
   * representation of the resource being responded to.
   *
   * @return whether it has the IRI that may be used to retrieve a
   *     representation of the resource being responded to
   */
  public boolean hasHref() {
    return getHref() != null;
  }

  /**
   * Returns the persistent universally unique identifier of the resource being
   * responded to.
   *
   * @return persistent universally unique identifier of the resource being
   *     responded to
   */
  public String getRef() {
    return ref;
  }

  /**
   * Sets the persistent universally unique identifier of the resource being
   * responded to.
   *
   * @param ref persistent universally unique identifier of the resource being
   *     responded to or <code>null</code> to reset
   */
  public void setRef(String ref) {
    throwExceptionIfImmutable();
    this.ref = ref;
  }

  /**
   * Returns whether it has the persistent universally unique identifier of the
   * resource being responded to.
   *
   * @return whether it has the persistent universally unique identifier of the
   *     resource being responded to
   */
  public boolean hasRef() {
    return getRef() != null;
  }

  /**
   * Returns the IRI of an Atom Feed or Entry Document containing an atom:entry
   * with an atom:id value equal to the value of the "ref" attribute.
   *
   * @return IRI of an Atom Feed or Entry Document containing an atom:entry with
   *     an atom:id value equal to the value of the "ref" attribute
   */
  public String getSource() {
    return source;
  }

  /**
   * Sets the IRI of an Atom Feed or Entry Document containing an atom:entry
   * with an atom:id value equal to the value of the "ref" attribute.
   *
   * @param source IRI of an Atom Feed or Entry Document containing an
   *     atom:entry with an atom:id value equal to the value of the "ref"
   *     attribute or <code>null</code> to reset
   */
  public void setSource(String source) {
    throwExceptionIfImmutable();
    this.source = source;
  }

  /**
   * Returns whether it has the IRI of an Atom Feed or Entry Document containing
   * an atom:entry with an atom:id value equal to the value of the "ref"
   * attribute.
   *
   * @return whether it has the IRI of an Atom Feed or Entry Document containing
   *     an atom:entry with an atom:id value equal to the value of the "ref"
   *     attribute
   */
  public boolean hasSource() {
    return getSource() != null;
  }

  /**
   * Returns the hint to the client about the media type of the resource
   * identified by the "href" attribute.
   *
   * @return hint to the client about the media type of the resource identified
   *     by the "href" attribute
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the hint to the client about the media type of the resource identified
   * by the "href" attribute.
   *
   * @param type hint to the client about the media type of the resource
   *     identified by the "href" attribute or <code>null</code> to reset
   */
  public void setType(String type) {
    throwExceptionIfImmutable();
    this.type = type;
  }

  /**
   * Returns whether it has the hint to the client about the media type of the
   * resource identified by the "href" attribute.
   *
   * @return whether it has the hint to the client about the media type of the
   *     resource identified by the "href" attribute
   */
  public boolean hasType() {
    return getType() != null;
  }

  @Override
  protected void validate() {
    if (ref == null) {
      throwExceptionForMissingAttribute(REF);
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
        ExtensionDescription.getDefaultDescription(InReplyTo.class);
    desc.setRequired(required);
    desc.setRepeatable(repeatable);
    return desc;
  }

  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(HREF, href);
    generator.put(REF, ref);
    generator.put(SOURCE, source);
    generator.put(TYPE, type);
  }

  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException
      {
    href = helper.consume(HREF, false);
    ref = helper.consume(REF, true);
    source = helper.consume(SOURCE, false);
    type = helper.consume(TYPE, false);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    InReplyTo other = (InReplyTo) obj;
    return eq(href, other.href)
        && eq(ref, other.ref)
        && eq(source, other.source)
        && eq(type, other.type);
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (href != null) {
      result = 37 * result + href.hashCode();
    }
    if (ref != null) {
      result = 37 * result + ref.hashCode();
    }
    if (source != null) {
      result = 37 * result + source.hashCode();
    }
    if (type != null) {
      result = 37 * result + type.hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{InReplyTo href=" + href + " ref=" + ref + " source=" + source +
        " type=" + type + "}";
  }

}
