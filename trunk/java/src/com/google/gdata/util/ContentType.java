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


package com.google.gdata.util;

import com.google.gdata.client.Service;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple class for parsing and generating Content-Type header values, per
 * RFC 2045 (MIME) and 2616 (HTTP 1.1).
 *
 * 
 */
public class ContentType implements Serializable {

  private static String TOKEN =
    "[\\p{ASCII}&&[^\\p{Cntrl} ;/=\\[\\]\\(\\)\\<\\>\\@\\,\\:\\\"\\?\\=]]+";

  // Precisely matches a token
  private static Pattern TOKEN_PATTERN = Pattern.compile(
    "^" + TOKEN + "$");

  // Matches a media type value
  private static Pattern TYPE_PATTERN = Pattern.compile(
    "(" + TOKEN + ")" +         // type  (G1)
    "/" +                       // separator
    "(" + TOKEN + ")" +         // subtype (G2)
    "\\s*(.*)\\s*", Pattern.DOTALL);

  // Matches an attribute value
  private static Pattern ATTR_PATTERN = Pattern.compile(
    "\\s*;\\s*" +
      "(" + TOKEN + ")" +       // attr name  (G1)
      "\\s*=\\s*" +
      "(?:" +
        "\"([^\"]*)\"" +        // value as quoted string (G3)
        "|" +
        "(" + TOKEN + ")?" +    // value as token (G2)
      ")"
    );

  /**
   * Name of the attribute that contains the encoding character set for
   * the content type.
   * @see #getCharset()
   */
  public static final String ATTR_CHARSET = "charset";

  /**
   * Special "*" character to match any type or subtype.
   */
  private static final String STAR = "*";

  /**
   * The UTF-8 charset encoding is used by default for all text and xml
   * based MIME types.
   */
  private static final String DEFAULT_CHARSET = ATTR_CHARSET + "=UTF-8";

  /**
   * A ContentType constant that describes the base unqualified Atom content
   * type.
   */
  public static final ContentType ATOM =
      new ContentType("application/atom+xml;" + DEFAULT_CHARSET).lock();

  /**
   * A ContentType constant that describes the qualified Atom entry content
   * type.
   *
   * @see #getAtomEntry()
   */
  public static final ContentType ATOM_ENTRY =
      new ContentType("application/atom+xml;type=entry;" + DEFAULT_CHARSET)
          .lock();

  /**
   * A ContentType constant that describes the qualified Atom feed content
   * type.
   *
   * @see #getAtomFeed()
   */
  public static final ContentType ATOM_FEED =
      new ContentType("application/atom+xml;type=feed;" + DEFAULT_CHARSET)
          .lock();

  /**
   * Returns the ContentType that should be used in contexts that expect
   * an Atom entry.
   */
  public static ContentType getAtomEntry() {
    // Use the unqualifed type for v1, the qualifed one for later versions
    return Service.getVersion().isCompatible(Service.Versions.V1) ?
        ATOM : ATOM_ENTRY;
  }

  /**
   * Returns the ContentType that should be used in contexts that expect
   * an Atom feed.
   */
  public static ContentType getAtomFeed() {
    // Use the unqualified type for v1, the qualified one for later versions
    return Service.getVersion().isCompatible(Service.Versions.V1) ?
        ATOM : ATOM_FEED;
  }

  /**
   * A ContentType constant that describes the Atom Service content type.
   */
  public static final ContentType ATOM_SERVICE =
      new ContentType("application/atomsvc+xml;" + DEFAULT_CHARSET).lock();

  /**
   * A ContentType constant that describes the RSS channel/item content type.
   */
  public static final ContentType RSS =
      new ContentType("application/rss+xml;" + DEFAULT_CHARSET).lock();

  /**
   * A ContentType constant that describes the JSON content type.
   */
  public static final ContentType JSON =
      new ContentType("application/json;" + DEFAULT_CHARSET).lock();

  /**
   * A ContentType constant that describes the Javascript content type.
   */
  public static final ContentType JAVASCRIPT =
      new ContentType("text/javascript;" + DEFAULT_CHARSET).lock();

  /**
   * A ContentType constant that describes the generic text/xml content type.
   */
  public static final ContentType TEXT_XML =
      new ContentType("text/xml;" + DEFAULT_CHARSET).lock();

  /**
   * A ContentType constant that describes the generic text/html content type.
   */
  public static final ContentType TEXT_HTML =
      new ContentType("text/html;" + DEFAULT_CHARSET).lock();

  /**
   * A ContentType constant that describes the generic text/plain content type.
   */
  public static final ContentType TEXT_PLAIN =
      new ContentType("text/plain;" + DEFAULT_CHARSET).lock();

  /**
   * A ContentType constant that describes the GData error content type.
   */
  public static final ContentType GDATA_ERROR =
      new ContentType("application/vnd.google.gdata.error+xml").lock();

  /**
   * A ContentType constant that describes the OpenSearch description document
   */
  public static final ContentType OPENSEARCH =
      new ContentType("application/opensearchdescription+xml").lock();
  

  /**
   * A ContentType constant that describes the MIME multipart/related content
   * type.
   */
  public static final ContentType MULTIPART_RELATED =
      new ContentType("multipart/related").lock();
  
  /**
   * A ContentType constant that describes the application/xml content
   * type.
   */
  public static final ContentType APPLICATION_XML =
      new ContentType("application/xml").lock();
  
  /**
   * A ContentType constant that indicates that the body contains an
   * encapsulated message, with the syntax of an RFC 822 email message.
   */
  public static final ContentType MESSAGE_RFC822 =
      new ContentType("message/rfc822").lock();
  
  /**
   * Wildcard content type that will match any MIME type
   */
  public static final ContentType ANY = new ContentType("*/*").lock();

  /**
   * Determines the best "Content-Type" header to use in a servlet response
   * based on the "Accept" header from a servlet request.
   *
   * @param acceptHeader       "Accept" header value from a servlet request (not
   *                           <code>null</code>)
   * @param actualContentTypes actual content types in descending order of
   *                           preference (non-empty, and each entry is of the
   *                           form "type/subtype" without the wildcard char
   *                           '*') or <code>null</code> if no "Accept" header
   *                           was specified
   * @return the best content type to use (or <code>null</code> on no match).
   */
  public static ContentType getBestContentType(String acceptHeader,
      List<ContentType> actualContentTypes) {

    // If not accept header is specified, return the first actual type
    if (acceptHeader == null) {
      return actualContentTypes.get(0);
    }

    // iterate over all of the accepted content types to find the best match
    float bestQ = 0;
    ContentType bestContentType = null;
    String[] acceptedTypes = acceptHeader.split(",");
    for (String acceptedTypeString : acceptedTypes) {

      // create the content type object
      ContentType acceptedContentType;
      try {
        acceptedContentType = new ContentType(acceptedTypeString.trim());
      } catch (IllegalArgumentException ex) {
        // ignore exception
        continue;
      }

      // parse the "q" value (default of 1)
      float curQ = 1;
      try {
        String qAttr = acceptedContentType.getAttribute("q");
        if (qAttr != null) {
          float qValue = Float.valueOf(qAttr);
          if (qValue <= 0 || qValue > 1) {
            continue;
          }
          curQ = qValue;
        }
      } catch (NumberFormatException ex) {
        // ignore exception
        continue;
      }

      // only check it if it's at least as good ("q") as the best one so far
      if (curQ < bestQ) {
        continue;
      }

      /* iterate over the actual content types in order to find the best match
      to the current accepted content type */
      for (ContentType actualContentType : actualContentTypes) {

        /* if the "q" value is the same as the current best, only check for
        better content types */
        if (curQ == bestQ && bestContentType == actualContentType) {
          break;
        }

        /* check if the accepted content type matches the current actual
        content type */
        if (actualContentType.match(acceptedContentType)) {
          bestContentType = actualContentType;
          bestQ = curQ;
          break;
        }
      }
    }

    // if found an acceptable content type, return the best one
    if (bestQ != 0) {
      return bestContentType;
    }

    // Return null if no match
    return null;
  }

 /**
   * Constructs a new instance with default media type
   */
  public ContentType() {
    this(null);
  }

  /**
   * Constructs a new instance from a content-type header value
   * parsing the MIME content type (RFC2045) format.  If the type
   * is {@code null}, then media type and charset will be
   * initialized to default values.
   *
   * @param typeHeader content type value in RFC2045 header format.
   */
  public ContentType(String typeHeader) {

    // If the type header is no provided, then use the HTTP defaults.
    if (typeHeader == null) {
      type = "application";
      subType = "octet-stream";
      attributes.put(ATTR_CHARSET, "iso-8859-1"); // http default
      return;
    }

    // Get type and subtype
    Matcher typeMatch = TYPE_PATTERN.matcher(typeHeader);
    if (!typeMatch.matches()) {
      throw new IllegalArgumentException("Invalid media type:" + typeHeader);
    }

    type = typeMatch.group(1).toLowerCase();
    subType = typeMatch.group(2).toLowerCase();
    if (typeMatch.groupCount() < 3) {
      return;
    }

    // Get attributes (if any)
    Matcher attrMatch = ATTR_PATTERN.matcher(typeMatch.group(3));
    while (attrMatch.find()) {

      String value = attrMatch.group(2);
      if (value == null) {
        value = attrMatch.group(3);
        if (value == null) {
          value = "";
        }
      }

      attributes.put(attrMatch.group(1).toLowerCase(), value);
    }

    // Infer a default charset encoding if unspecified.
    if (!attributes.containsKey(ATTR_CHARSET)) {
      inferredCharset = true;
      if (subType.endsWith("xml")) {
        if (type.equals("application")) {
          // BUGBUG: Actually have need to look at the raw stream here, but
          // if client omitted the charset for "application/xml", they are
          // ignoring the STRONGLY RECOMMEND language in RFC 3023, sec 3.2.
          // I have little sympathy.
          attributes.put(ATTR_CHARSET, "utf-8");    // best guess
        } else {
          attributes.put(ATTR_CHARSET, "us-ascii"); // RFC3023, sec 3.1
        }
      } else if (subType.equals("json")) {
        attributes.put(ATTR_CHARSET, "utf-8");    // RFC4627, sec 3
      } else {
        attributes.put(ATTR_CHARSET, "iso-8859-1"); // http default
      }
    }
  }

  /** {@code true} if parsed input didn't contain charset encoding info */
  private boolean inferredCharset = false;

  /** If set to {@code true}, the object is immutable. */
  private boolean locked;

  private String type;
  public String getType() { return type; }
  public void setType(String type) { 
    assertNotLocked();
    this.type = type; 
  }


  private String subType;
  public String getSubType() { return subType; }
  public void setSubType(String subType) { 
    assertNotLocked();
    this.subType = subType; 
  }

  /** Returns the full media type */
  public String getMediaType() {
    StringBuilder sb = new StringBuilder();
    sb.append(type);
    sb.append("/");
    sb.append(subType);
    if (attributes.containsKey("type")) {
      sb.append(";type=").append(attributes.get("type"));
    }
    return sb.toString();
  }

  private HashMap<String, String> attributes = new HashMap<String, String>();

  /**
   * Makes the object immutable and returns it.
   *
   * This should at least be used when keeping a {@link ContentType} instance as
   * a static.
   */
  public ContentType lock() {
    locked = true;
    return this;
  }

  private void assertNotLocked() {
    if (locked) {
      throw new IllegalStateException("Unmodifiable instance");
    }
  }

  /**
   * Returns the additional attributes of the content type.
   */
  public Map<String, String> getAttributes() { 
    if (locked) {
      return Collections.unmodifiableMap(attributes);
    } 
    return attributes; 
  }

  /**
   * Returns the additional attribute by name of the content type.
   *
   * @param name attribute name
   */
  public String getAttribute(String name) {
    return attributes.get(name);
  }

  /*
   * Returns the charset attribute of the content type or null if the
   * attribute has not been set.
   */
  public String getCharset() { return attributes.get(ATTR_CHARSET); }


  /**
   * Returns whether this content type is match by the content type found in the
   * "Accept" header field of an HTTP request.
   *
   * <p>For atom content type, this method will check the optional attribute
   * 'type'. If the type attribute is set in both this and {@code
   * acceptedContentType}, then they must be the same. That is, {@code
   * application/atom+xml} will match both {@code
   * application/atom+xml;type=feed} and {@code
   * application/atom+xml;type=entry}, but {@code
   * application/atom+xml;type=entry} will not match {@code
   * application/atom+xml;type=feed}.a
   *
   * @param acceptedContentType content type found in the "Accept" header field
   *                            of an HTTP request
   */
  public boolean match(ContentType acceptedContentType) {
    String acceptedType = acceptedContentType.getType();
    String acceptedSubType = acceptedContentType.getSubType();
    return STAR.equals(acceptedType) || type.equals(acceptedType) 
        && (STAR.equals(acceptedSubType) || subType.equals(acceptedSubType)) 
        && (!isAtom() || matchAtom(acceptedContentType));
  }

  /** Returns true if this is an atom content type. */
  private boolean isAtom() {
    return "application".equals(type) && "atom+xml".equals(subType);
  }

  /** 
   * Compares the optional 'type' attribute of two content types.
   *
   * <p>This method accepts atom content type without the 'type' attribute
   * but if the types are specified, they must match.
   */
  private boolean matchAtom(ContentType acceptedContentType) {
    String atomType = getAttribute("type");
    String acceptedAtomType = acceptedContentType.getAttribute("type");

    return atomType == null || acceptedAtomType == null
        || atomType.equals(acceptedAtomType);
  }

  /**
   * Generates the Content-Type value
   */
  @Override
  public String toString() {

    StringBuffer sb = new StringBuffer();
    sb.append(type);
    sb.append("/");
    sb.append(subType);
    for (String name : attributes.keySet()) {

      // Don't include any inferred charset attribute in output.
      if (inferredCharset && ATTR_CHARSET.equals(name)) {
        continue;
      }
      sb.append(";");
      sb.append(name);
      sb.append("=");
      String value = attributes.get(name);
      Matcher tokenMatcher = TOKEN_PATTERN.matcher(value);
      if (tokenMatcher.matches()) {
        sb.append(value);
      } else {
        sb.append("\"" + value + "\"");
      }
    }
    return sb.toString();
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentType that = (ContentType) o;
    return type.equals(that.type) && subType.equals(that.subType) && attributes
        .equals(that.attributes);
  }


  @Override
  public int hashCode() {
    return (type.hashCode() * 31 + subType.hashCode()) * 31 + attributes
        .hashCode();
  }
}
