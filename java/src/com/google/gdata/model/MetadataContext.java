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



import com.google.gdata.util.Version;
import com.google.gdata.wireformats.AltFormat;

/**
 * A context that metadata is operating under.  Currently this contains the
 * alt format, projection and version of the current request.  Immutable.
 *
 * <p>This class also contains static initializers for the transforms required
 * by each of our alt formats, which guarantees they will be loaded.  We may
 * want to put them somewhere else, but we need to move the constants for the
 * contexts along with them.
 *
 * 
 */
public final class MetadataContext implements Comparable<MetadataContext> {

  /** The ATOM metadata context. */
  public static final MetadataContext ATOM = MetadataContext.forAlt(
      AltFormat.ATOM);

  /** The RSS metadata context. */
  public static final MetadataContext RSS = MetadataContext.forAlt(
      AltFormat.RSS);

  /**
   * Creates a new immutable metadata context with just an alt format.  The
   * format must not be null or this will throw a null pointer exception.
   *
   * @param format the alt format for the context, not {@code null}.
   * @return a metadata context for the alt format.
   */
  public static MetadataContext forAlt(AltFormat format) {
    return forContext(format, null, null);
  }

  /**
   * Creates a new immutable metadata context with just projection.  If the
   * projection is null this will return null, which is the default context.
   *
   * @param projection the projection of the context.
   * @return an immutable metadata context with the given projection, or null
   *     if the projection was null.
   */
  public static MetadataContext forProjection(String projection) {
    return forContext(null, projection, null);
  }

  /**
   * Constructs a new immutable metadata context with just version.  If version
   * is null this will return null, which is the default context.
   *
   * @param version the version of the context.
   * @return an immutable metadata context with the given version, or null if
   *     the version was null.
   */
  public static MetadataContext forVersion(Version version) {
    return forContext(null, null, version);
  }

  /**
   * Creates a new immutable metadata context.  If version, projection and alt
   * type are all null this method will return null, which is the default
   * context.
   *
   * @param format the alt format of the context.
   * @param projection the projection for the context.
   * @param version the version of the context.
   * @return an immutable metadata context with the given alt type, projection
   *     and version, or null if all parameters are null.
   */
  public static MetadataContext forContext(AltFormat format, String projection,
      Version version) {
    if (format == null && projection == null && version == null) {
      return null;
    }
    return new MetadataContext(format, projection, version);
  }

  // The alt type of the request.
  private final AltFormat altFormat;

  // The projection of the request.
  private final String projection;

  // The version of the request.
  private final Version version;

  /**
   * Private constructor, callers must use the static factory methods.
   */
  private MetadataContext(
      AltFormat format, String projection, Version version) {
    this.altFormat = format;
    this.projection = projection;
    this.version = version;
  }

  /**
   * Returns true if this context is a match for the given context.  A context
   * is a match for another context if it is a subset of that context.  Null
   * properties are ignored, but any properties that are set must match the
   * same property on the other context.
   */
  public boolean matches(MetadataContext other) {
    return other != null
        && (altFormat == null || altFormat.equals(other.altFormat))
        && (projection == null || projection.equals(other.projection))
        && (version == null
            || (other.version != null && other.version.isCompatible(version)));
  }

  /**
   * The alt format the context represents.
   *
   * @return the alt format or null if the context doesn't have an alt format.
   */
  public AltFormat getAltFormat() {
    return altFormat;
  }

  /**
   * The projection the context represents.
   *
   * @return the projection or null if the context doesn't have a projection.
   */
  public String getProjection() {
    return projection;
  }

  /**
   * The version the context represents.
   *
   * @return the version or null if no version exists in this context.
   */
  public Version getVersion() {
    return version;
  }

  /**
   * Compare two context objects to order them.  A null value for any field is
   * treated as a low value.  Otherwise we use the normal string comparison. If
   * the given context object is {@code null} a {@link NullPointerException}
   * will be thrown.
   */
  public int compareTo(MetadataContext other) {
    if (this == other) {
      return 0;
    }
    int compare = compareAltFormat(altFormat, other.altFormat);
    if (compare != 0) {
      return compare;
    }
    compare = compareString(projection, other.projection);
    if (compare != 0) {
      return compare;
    }

    // Compare versions.
    return compareVersion(version, other.version);
  }

  /**
   * Compares two alt formats, where either may be null.  Just compares them
   * based on their names.
   */
  static int compareAltFormat(AltFormat a, AltFormat b) {
    return compareString(
        (a == null) ? null : a.getName(),
        (b == null) ? null : b.getName());
  }

  /**
   * Compares two strings, where either may be null.
   */
  static int compareString(String a, String b) {
    if (a == b) {
      return 0;
    }
    if (a == null) {
      return -1;
    }
    if (b == null) {
      return 1;
    }
    return a.compareTo(b);
  }

  /**
   * Compares two versions, where either may be null.
   */
  static int compareVersion(Version a, Version b) {
    if (a == b) {
      return 0;
    }
    if (a == null) {
      return -1;
    }
    if (b == null) {
      return 1;
    }
    int compare = MetadataKey.compareClass(
        a.getServiceClass(), b.getServiceClass());
    if (compare != 0) {
      return compare;
    }
    int aMajor = a.getMajor();
    int bMajor = b.getMajor();
    if (aMajor != bMajor) {
      return aMajor < bMajor ? -1 : 1;
    }
    int aMinor = a.getMinor();
    int bMinor = b.getMinor();
    return aMinor < bMinor ? -1 : (aMinor == bMinor ? 0 : 1);
  }

  /**
   * The hashCode for a context is just the hashcode of its parts.
   */
  @Override
  public int hashCode() {
    int hash = 0;
    if (altFormat != null) {
      hash += altFormat.hashCode();
    }
    if (projection != null) {
      hash = hash * 37;
      hash += projection.hashCode();
    }
    if (version != null) {
      hash = hash * 37;
      hash += version.hashCode();
    }
    return hash;
  }

  /**
   * Checks that the other object is a metadata context with the same alt type
   * and version.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof MetadataContext)) {
      return false;
    }

    MetadataContext other = (MetadataContext) obj;
    if (altFormat == null) {
      if (other.altFormat != null) {
        return false;
      }
    } else if (!altFormat.equals(other.altFormat)) {
      return false;
    }
    if (projection == null) {
      if (other.projection != null) {
        return false;
      }
    } else if (!projection.equals(other.projection)) {
      return false;
    }
    if (version == null) {
      if (other.version != null) {
        return false;
      }
    } else if (!version.equals(other.version)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{MetadataContext(");
    sb.append(altFormat);
    sb.append(',');
    sb.append(projection);
    sb.append(',');
    sb.append(version);
    sb.append(")}");
    return sb.toString();
  }
}
