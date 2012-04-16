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
import com.google.common.collect.ImmutableSet;
import com.google.gdata.util.ContentType;

import java.util.List;
import java.util.Set;

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
   * Constant value representing the Really Simple Syndication (RSS) format, as
   * defined by <a href="http://cyber.law.harvard.edu/rss/rss.html">RSS 2.0
   * </a>.
   */
  public static final AltFormat RSS = builder()
      .setName("rss")
      .setWireFormat(WireFormat.XML)
      .setContentType(ContentType.RSS)
      .setAcceptableXmlTypes()
      .setSelectableByType(true)
      .build();

  /**
   * Constant value representing the OpenSearch Description Document, as
   * described by <a href="http://www.opensearch.org/Home">OpenSearch 1.1</a>.
   * This representation is only able for feed resources.
   */
  public static final AltFormat OPENSEARCH = builder()
      .setName("opensearch")
      .setWireFormat(WireFormat.XML)
      .setContentType(ContentType.OPENSEARCH)
      .setAcceptableXmlTypes()
      .setSelectableByType(true)
      .build();

  /**
   * Constant value representing the AtomPub Service Document, as described
   * by <a href="http://www.ietf.org/rfc/rfc5023.txt">RFC 5023</a>.  This
   * representation is only available for feed resources.
   */
  public static final AltFormat ATOM_SERVICE = builder()
      .setName("atom-service")
      .setWireFormat(WireFormat.XML)
      .setContentType(ContentType.ATOM_SERVICE)
      .setAcceptableXmlTypes()
      .setSelectableByType(true)
      .build();
  
  /**
   * Constant value representing application/xml document
   */
  public static final AltFormat APPLICATION_XML = builder()
      .setName("application-xml")
      .setWireFormat(WireFormat.XML)
      .setContentType(ContentType.APPLICATION_XML)
      .setAcceptableXmlTypes()
      .setSelectableByType(true)
      .build();
  
  /**
   * Constant value representing the media content associated with a GData
   * resource. The actual content type returned will depend upon the native
   * media representation of the target resource.
   */
  public static final AltFormat MEDIA = builder()
      .setName("media")
      .setContentType(ContentType.ANY)
      .build();

  /**
   * Constant value representing the MIME Multipart Document format, as
   * described by <a href="http://www.ietf.org/rfc/rfc2045.txt">RFC 2045</a>.
   * The multipart document contains both the Atom representation (as one part)
   * and the associated media content (as another part).  This representation is
   * available only for GData media resources.
   */
  public static final AltFormat MEDIA_MULTIPART = builder()
      .setName("media-multipart")
      .setContentType(ContentType.MULTIPART_RELATED)
      .setSelectableByType(true)
      .build();

  /**
   * Constant value representing the Atom Syndication Format, as defined by
   * <a href="http://www.ietf.org/rfc/rfc4287.txt">RFC 4287</a>.
   */
  public static final AltFormat ATOM = builder()
      .setName("atom")
      .setWireFormat(WireFormat.XML)
      .setContentType(ContentType.ATOM)
      .setAcceptableXmlTypes()
      .addAllowedInputFormats(MEDIA, MEDIA_MULTIPART, APPLICATION_XML)
      .setSelectableByType(true)
      .build();

  /**
   * Creates a builder for a new AltFormat.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a builder for a new AltFormat. Pre-initializes the builder with a
   * prototype format.
   */
  public static Builder builder(AltFormat format) {
    return new Builder(format);
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

  private final Set<AltFormat> extraInputFormats;

  /**
   * The base alt format this alt format wraps another one, for the *-in-script
   * variants.
   */
  private final AltFormat base;

  private AltFormat(Builder builder) {
    base = builder.base;
    name = builder.name;
    wireFormat = builder.wireFormat;
    contentType = builder.contentType;
    ImmutableList.Builder<ContentType> acceptListBuilder = 
        ImmutableList.builder();
    acceptListBuilder.add(contentType);
    if (builder.acceptableTypes != null) {
      acceptListBuilder.addAll(builder.acceptableTypes);
    }
    acceptList = acceptListBuilder.build();
    isSelectableByType = builder.isSelectableByType;
    extraInputFormats = builder.extraInputFormats.build();
  }

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

   * @deprecated Please use the {@link Builder} instead. See {@link #builder()}.
   */
  @Deprecated
  public AltFormat(String name, WireFormat wireFormat, ContentType contentType,
      List<ContentType> acceptList, boolean isSelectableByType) {
    this(builder()
        .setName(name)
        .setWireFormat(wireFormat)
        .setContentType(contentType)
        .setAcceptableTypes(acceptList)
        .setSelectableByType(isSelectableByType)
        .check());
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

  /**
   * Returns {@code true} if {@code inputFormat} is allowed as input format when
   * this format with this format as the output format.
   */
  public boolean allowInputFormat(AltFormat inputFormat) {
    return inputFormat == this || extraInputFormats.contains(inputFormat);
  }

  /**
   * Returns {@code true} if this format is a variant, such as *-in-script.
   */
  public boolean hasBaseFormat() {
    return base != null;
  }

  /**
   * Returns the base format, if this format is a variant. Otherwise returns
   * {@code null}.
   */
  public AltFormat getBaseFormat() {
    return base;
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

  /**
   * A builder for AltFormat. Create new builders using {@link
   * AltFormat#builder()} or {@link AltFormat#builder(AltFormat)}.
   */
  public static class Builder {
    private String name;
    private WireFormat wireFormat;
    private ContentType contentType;
    private Set<ContentType> acceptableTypes;
    private final ImmutableSet.Builder<AltFormat> extraInputFormats =
        ImmutableSet.<AltFormat>builder();
    private boolean isSelectableByType;
    private AltFormat base;

    private Builder() {
    }

    private Builder(AltFormat prototype) {
      name = prototype.name;
      wireFormat = prototype.wireFormat;
      contentType = prototype.contentType;
      acceptableTypes = ImmutableSet.copyOf(prototype.acceptList);
      extraInputFormats.addAll(prototype.extraInputFormats);
      isSelectableByType = prototype.isSelectableByType;
      base = prototype.base;
    }

    /** 
     * Sets the short name for this format. This value is suitable for
     * use as the value of the {@code alt} query parameter. This is
     * required.
     */
    public Builder setName(String name) {
      this.name = name;
      return this;
    }

    /**
     * Sets the content wire format.
     */
    public Builder setWireFormat(WireFormat wireFormat) {
      this.wireFormat = wireFormat;
      return this;
    }

    /**
     * Sets the primary MIME content type used for the representation.
     * This is the content type expected as input and the default content
     * type returned as output.
     */
    public Builder setContentType(ContentType contentType) {
      this.contentType = contentType;
      return this;
    }

    /**
     * Indicates whether the MIME content type can be used as an alias to select
     * the representation.
     */
    public Builder setSelectableByType(boolean isSelectableByType) {
      this.isSelectableByType = isSelectableByType;
      return this;
    }

    /**
     * Declares another {@code AltFormat} as the base for this one.
     *
     * <p>This is set for *-in-script variants and to allows access to the
     * original format.
     */
    public Builder setBaseFormat(AltFormat base) {
      this.base = base;
      return this;
    }

    /**
     * Declares text/plain to be an acceptable match for the purpose
     * of content negotiation. 
     */
    public Builder setAcceptableTextTypes() {
      return setAcceptableTypes(ContentType.TEXT_PLAIN);
    }

    /**
     * Declares text/xml and text/plain to be acceptable matches for the
     * purpose of content negotiation.
     */
    public Builder setAcceptableXmlTypes() {
      return setAcceptableTypes(ContentType.TEXT_XML, ContentType.TEXT_PLAIN);
    }

    /**
     * Declares MIME types to be acceptable matches for the purpose
     * of content negotiation. By default, only the content type set
     * using {@link #setContentType} is acceptable.
     */
    public Builder setAcceptableTypes(ContentType... types) {
      if (types == null) {
        acceptableTypes = ImmutableSet.of();
      } else {
        acceptableTypes = ImmutableSet.copyOf(types);
      }
      return this;
    }

    /**
     * Declares MIME types to be acceptable matches for the purpose
     * of content negotiation. By default, only the content type set
     * using {@link #setContentType} is acceptable.
     */
    private Builder setAcceptableTypes(Iterable<ContentType> types) {
      if (types == null) {
        acceptableTypes = ImmutableSet.of();
      } else {
        acceptableTypes = ImmutableSet.copyOf(types);
      }
      return this;
    }

    /**
     * Declares formats as being acceptable input formats when the
     * current AltFormat is selected as output.
     *
     * <p>When posting or putting data, the request content type is
     * expected to be the same as the response content type selected
     * with the alt query parameter. This is enforced in the server.
     *
     * <p>For example, this is acceptable:
     * <pre>
     * POST http://gdata.example.com/feeds/myfeed?alt=jsonc
     * ContentType: application/json
     * </pre>
     * and this is not:
     * <pre>
     * POST http://gdata.example.com/feeds/myfeed?alt=jsonc
     * ContentType: application/atom+xml
     * </pre>
     *
     * <p>Some cases violate this rule. For example, when posting a media
     * file, the request content type is the type of the media while
     * the response content type is either atom or json. 
     * 
     * <p>Such special cases should be declared using this method.
     */
    public Builder addAllowedInputFormats(AltFormat... formats) {
      for (AltFormat format : formats) {
        extraInputFormats.add(format);
      }
      return this;
    }

    /**
     * Builds the {@link AltFormat}. This can be called only once.
     *
     * @throws IllegalStateException unless both name and content type are set
     */
    public AltFormat build() {
      check();
      return new AltFormat(this);
    }

    private Builder check() {
      Preconditions.checkState(name != null, "Name must be set");
      Preconditions.checkState(contentType != null, "contentType must be set");
      return this;
    }
  }
}
