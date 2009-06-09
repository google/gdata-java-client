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

import com.google.gdata.util.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.gdata.util.ContentType;

import java.util.List;

/**
 * The AltFormat class represents an alternate representation format for a GData
 * resources. An alternate format has a name (which may be used as the {@code
 * alt} query parameter for GData requests), a primary MIME content type (which
 * may act as an alias for the name if unique to the representation), and
 * several other attributes.
 * <p>
 * Two AltFormat instances are considered to be equal if they have the same
 * name.
 * <p>
 * This class also exposes static constants for common GData alternate
 * representation formats that are available across multiple GData services.
 *
 * 
 */
public class AltFormat {

  /**
   * Constant value representing the Atom Syndication Format, as defined by
   * <a href="http://www.ietf.org/rfc/rfc4287.txt">RFC 4287</a>.
   */
  public static final AltFormat ATOM =
      new AltFormat("atom", WireFormat.XML, ContentType.ATOM,
          xmlAcceptTypes(ContentType.ATOM), true);

  /**
   * Constant value representing the Really Simple Syndication (RSS) format, as
   * defined by <a href="http://cyber.law.harvard.edu/rss/rss.html">RSS 2.0
   * </a>.
   */
  public static final AltFormat RSS =
      new AltFormat("rss", WireFormat.XML, ContentType.RSS,
          xmlAcceptTypes(ContentType.RSS), true);







  /**
   * Constant value representing the OpenSearch Description Document, as
   * described by <a href="http://www.opensearch.org/Home">OpenSearch 1.1</a>.
   * This representation is only able for feed resources.
   */
  public static final AltFormat OPENSEARCH =
    new AltFormat("opensearch", WireFormat.XML, ContentType.OPENSEARCH,
        xmlAcceptTypes(ContentType.OPENSEARCH), true);

  /**
   * Constant value representing the AtomPub Service Document, as described
   * by <a href="http://www.ietf.org/rfc/rfc5023.txt">RFC 5023</a>.  This
   * representation is only available for feed resources.
   */
  public static final AltFormat ATOM_SERVICE =
    new AltFormat("atom-service", WireFormat.XML, ContentType.ATOM_SERVICE,
        xmlAcceptTypes(ContentType.ATOM_SERVICE), true);
  
  /**
   * Constant value representing application/xml, which is used for partial
   * document representations of xml formats.
   */
  public static final AltFormat APPLICATION_XML =
    new AltFormat("application-xml", WireFormat.XML, 
        ContentType.APPLICATION_XML,
        xmlAcceptTypes(ContentType.APPLICATION_XML), false);

  /**
   * Constant value representing the media content associated with a GData
   * resource. The actual content type returned will depend upon the native
   * media representation of the target resource.
   */
  public static final AltFormat MEDIA =
       new AltFormat("media", null, ContentType.ANY, null, false);

  /**
   * Constant value representing the MIME Multipart Document format, as
   * described by <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>.
   * The multipart document contains both the Atom representation (as one part)
   * and the associated media content (as another part).  This representation is
   * available only for GData media resources.
   */
  public static final AltFormat MEDIA_MULTIPART =
      new AltFormat("media-multipart", null, ContentType.MULTIPART_RELATED,
          null, true);

  /**
   * Returns the list of acceptable MIME content types for an XML media type.
   * Used as a helper method when constructing matching content type lists for
   * XML representations.
   *
   * @param contentType xml media type
   * @return immutable list of acceptable matching types derived from (and
   *         including) the input type.
   */
  public static List<ContentType> xmlAcceptTypes(ContentType contentType) {
    return ImmutableList.of(contentType, ContentType.TEXT_XML,
        ContentType.TEXT_PLAIN);
  }

  /**
   * Returns the list of acceptable MIME content types for a text media type.
   * Used as a helper method when constructing matching content type lists for
   * text representations.
   *
   * @param contentType text media type
   * @return immutable list of acceptable matching types derived from (and
   *         including) the input type.
   */
  public static List<ContentType> textAcceptTypes(ContentType contentType) {
    return ImmutableList.of(contentType, ContentType.TEXT_PLAIN);
  }

  /** Formal name of alternate representation */
  private final String name;

  /**
   * Wire format to use when parsing or generating the class (or {@code null}
   * if no associated wire format exists).
   */
  private final WireFormat wireFormat;

  /** Primary MIME content type for alternate representation */
  private final ContentType contentType;

  /** List of acceptable matching types for alternate representation */
  private final List<ContentType> acceptList;

  /**
   * Boolean used to indicate that the primary MIME type can be used to select
   * the representation.
   */
  private final boolean isSelectableByType;

  /**
   * Constructs a new alternate representation format with the provided
   * attributes.
   *
   * @param name the short name for this format. This values is suitable for use
   *        as the value of the {@code alt} query parameter.
   * @param wireFormat the content wire format or {@code null} if there is no
   *        associated wire format for the representation.
   * @param contentType the primary MIME content type used for the
   *        representation.
   * @param acceptList a list of all MIME types that will be considered to
   *        potentially match the representation for the purposes of content
   *        negotiation. A value of {@code null} is equivalent to a single item
   *        list containing only the primary content type.
   * @param isSelectableByType if {@code true}, indicates that the MIME content
   *        type can be used as an alias to select the representation.
   */
  public AltFormat(String name, WireFormat wireFormat, ContentType contentType,
      List<ContentType> acceptList, boolean isSelectableByType) {
    Preconditions.checkNotNull(name, "Name must be set");
    Preconditions.checkNotNull(contentType, "contentType must be set");
    this.name = name;
    this.wireFormat = wireFormat;
    this.contentType = contentType;
    this.acceptList =
        (acceptList != null) ? acceptList : ImmutableList.of(contentType);
    this.isSelectableByType = isSelectableByType;
  }

  /**
   * Returns the short name for this format. This values is suitable for use as
   * the value of the {@code alt} query parameter.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the {@link WireFormat} that is used to parse and generate the
   * representation or {@code null} if no supporting wire format exists.
   */
  public WireFormat getWireFormat() {
    return wireFormat;
  }

  /**
   * Returns the primary MIME content type used for the representation.
   */
  public ContentType getContentType() {
    return contentType;
  }

  /**
   * Returns a list of all MIME types that will be considered to potentially
   * match the representation for the purposes of content negotiation.
   */
  public List<ContentType> getMatchingTypes() {
    return acceptList;
  }

  /**
   * Returns {@code true}, indicates that the MIME content type can be used as
   * an alias to select the representation.
   */
  public boolean isSelectableByType() {
    return isSelectableByType;
  }

  @Override
  public boolean equals(Object o) {
    return o == this
        || (o instanceof AltFormat && name.equals(((AltFormat) o).name));
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return name + "[" + contentType + "]";
  }
}

