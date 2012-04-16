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


package com.google.gdata.util.common.net;

import static com.google.gdata.util.common.base.Preconditions.checkNotNull;
import com.google.common.collect.ForwardingMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimaps;
import com.google.gdata.util.httputil.FormUrlDecoder;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Represents a sequence of name-value pairs encoded using the
 * application/x-www-form-urlencoded content type, typically as the query part
 * of a URI, as defined by section 17.13.4 of the W3C's <a
 * href="http://www.w3.org/TR/REC-html40/interact/forms.html#h-17.13.4.1">HTML
 * 4.01 Specification</a>.
 *
 * <p>This class stores keys and values in unicode (decoded) form, allowing
 * clients to get and set parameters safely using normal strings. Encoding is
 * performed when {@link #toString(Charset)} is called. See the {@link
 * UriEncoder} class comments for an important discussion regarding encoding.
 *
 * <p>Parameter maps may not contain null values. Both keys and values are
 * allowed to be the empty string, though in most cases only values are the
 * empty string. Parsing a query string may be a "lossy" operation in the
 * trivial sense, in that the original string cannot be exactly reconstructed;
 * for example, the query string "foo=&bar" is considered equivalent to
 * "foo&bar". Also note that parsing the empty string will <i>not</i> return an
 * empty map but will return a map with a single entry of the empty string as
 * both key and value. The empty map (see {@link #EMPTY_MAP}) represents an
 * undefined query.
 *
 * <p>In addition to the {@link ListMultimap} API, this class provides a
 * convenient {@link #getFirst} method for retrieving the first value of a
 * parameter, for when you expect only a single parameter of the specified name.
 *
 * <p>Parameter maps are typically constructed either by {@code Uri} or {@code
 * UriBuilder}. However, you may also construct a parameter map from scratch
 * using the constructor, or by calling {@link #parse(String)}.
 *
 * @see UriBuilder#getQueryParameters()
 * @see Uri#getQueryParameters()
 * 
 */
public final class UriParameterMap extends ForwardingMultimap<String, String>
    implements ListMultimap<String, String>, Cloneable, Serializable {
  private static final long serialVersionUID = -3053773769157973706L;

  /** The immutable empty map. */
  public static final UriParameterMap EMPTY_MAP;

  static {
    EMPTY_MAP = new UriParameterMap(
        ImmutableListMultimap.<String, String>of());
  }

  private final ListMultimap<String, String> delegate;
  
  /**
   * Constructs a new parameter map backed by the specified map. Private because
   * this constructor should only be used when the specified map is immutable or
   * "owned" by this instance.
   *
   * @throws NullPointerException if {@code delegate} is null
   */
  private UriParameterMap(ListMultimap<String, String> delegate) {
    this.delegate = delegate;
  }

  /** Constructs a new empty parameter map. */
  public UriParameterMap() {
    this(LinkedListMultimap.<String,String>create());
  }

  /**
   * Constructs a new parameter map populated with parameters parsed from the
   * specified query string using the {@link UriEncoder#DEFAULT_ENCODING},
   * UTF-8.
   *
   * @param query the query string, e.g., "q=flowers&n=20"
   * @return a mutable parameter map representing the query string
   * @throws NullPointerException if {@code query} is null
   */
  public static UriParameterMap parse(String query) {
    return parse(query, UriEncoder.DEFAULT_ENCODING);
  }

  /**
   * Constructs a new parameter map populated with parameters parsed from the
   * specified query string using the specified encoding.
   *
   * @param query the query string, e.g., "q=flowers&n=20"
   * @param encoding the character encoding to use
   * @return a mutable parameter map representing the query string
   * @throws NullPointerException if any argument is null
   */
  public static UriParameterMap parse(String query, Charset encoding) {
    checkNotNull(query);
    UriParameterMap map = new UriParameterMap();
    map.merge(query, encoding);
    return map;
  }

  /**
   * Returns an unmodifiable view of the specified parameter map. This method
   * allows modules to provide users with "read-only" access to internal
   * parameter maps. Query operations on the returned map "read through" to the
   * specified map, and attempts to modify the returned map, whether direct or
   * via its iterator or collection views, result in an {@code
   * UnsupportedOperationException}.
   *
   * @param map the parameter map for which to return an unmodifiable view
   * @return an unmodifiable view of the specified parameter map
   * @throws NullPointerException if {@code map} is null
   */
  public static UriParameterMap unmodifiableMap(UriParameterMap map) {
    return new UriParameterMap(
        Multimaps.unmodifiableListMultimap(map.delegate()));
  }

  protected ListMultimap<String, String> delegate() {
    return delegate;
  }

  /**
   * Populates the parameter map from the specified query using the specified
   * encoding. Package-private so that it can be used by {@link
   * UriBuilder#setQuery(String)}; it might be reasonable to make this method
   * public, though.
   */
  // this package and make it private
  @SuppressWarnings("deprecation")
  void merge(String query, Charset encoding) {
    checkNotNull(query);
    checkNotNull(encoding);
    FormUrlDecoder.parseWithCallback(query, encoding.name(),
        new FormUrlDecoder.Callback() {
          public void handleParameter(String name, String value) {
            put(name, value);
          }
        });
  }

  /**
   * Returns the first parameter value for the specified {@code key} (parameter
   * name) or {@code null} if no parameters are defined for that key. If the
   * parameter is defined, equivalent to {@code get(key).get(0)}.
   *
   * @param key the name of the parameter
   * @return the value of the parameter if present, or null
   * @see javax.servlet.ServletRequest#getParameter(String)
   */
  public String getFirst(String key) {
    checkNotNull(key);
    List<String> values = get(key);
    return values.isEmpty() ? null : values.get(0);
  }

  /**
   * Appends the string representation of these parameters to the specified
   * string builder using the specified encoding.
   *
   * @param out the string builder to append to
   * @param encoding the character encoding to use
   * @throws NullPointerException if any argument is null
   */
  public void appendTo(StringBuilder out, Charset encoding) {
    try {
      appendTo((Appendable) out, encoding);
    } catch (IOException e) {
      throw new AssertionError(e); // StringBuilder doesn't throw IOException
    }
  }

  /**
   * Appends the string representation of these parameters to the specified
   * {@code Appendable} using the specified encoding.
   *
   * @param out the appendable to append to
   * @param encoding the character encoding to use
   * @throws NullPointerException if any argument is null
   * @throws IOException if the {@link Appendable} encounters an error
   */
  public void appendTo(Appendable out, Charset encoding) throws IOException {
    checkNotNull(out);
    for (Iterator<Map.Entry<String, String>> i = entries().iterator();
         i.hasNext();) {
      Map.Entry<String, String> entry = i.next();
      out.append(UriEncoder.encode(entry.getKey(), encoding));
      if (!"".equals(entry.getValue())) {
        out.append("=");
        out.append(UriEncoder.encode(entry.getValue(), encoding));
      }
      if (i.hasNext()) {
        out.append("&");
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override public UriParameterMap clone() {
    /*
     * The supertype is not cloneable. But copy-construct is safe because this
     * is a final class.
     */
    ListMultimap<String,String> multimap
        = LinkedListMultimap.<String,String>create(delegate());
    return new UriParameterMap(multimap);
  }

  /**
   * Returns the string representation of these parameters using the specified
   * encoding, e.g., "q=flowers&n=20".
   *
   * @param encoding the character encoding to use
   * @throws NullPointerException if {@code encoding} is null
   */
  public String toString(Charset encoding) {
    StringBuilder out = new StringBuilder();
    appendTo(out, encoding);
    return out.toString();
  }

  /**
   * Returns an immutable copy of this parameter map as a {@code Map} from
   * strings to string arrays.
   */
  public Map<String, String[]> copyToArrayMap() {
    ImmutableMap.Builder<String, String[]> builder = ImmutableMap.builder();
    Map<String, Collection<String>> delegateMap = delegate().asMap();
    for (Map.Entry<String, Collection<String>> entry : delegateMap.entrySet()) {
      Collection<String> values = entry.getValue();
      builder.put(entry.getKey(), values.toArray(new String[values.size()]));
    }
    return builder.build();
  }

  /**
   * Returns the string representation of these parameters using the {@link
   * UriEncoder#DEFAULT_ENCODING}, UTF-8, e.g., "q=flowers&n=20".
   */
  @Override public String toString() {
    return toString(UriEncoder.DEFAULT_ENCODING);
  }

  /* This class extends ForwardingMultimap but implements ListMultimap! */

  @Override public List<String> get(String key) {
    return delegate().get(key);
  }

  @Override public List<String> removeAll(Object key) {
    return delegate().removeAll(key);
  }

  @Override public List<String> replaceValues(String key,
      Iterable<? extends String> values) {
    return delegate().replaceValues(key, values);
  }
}
