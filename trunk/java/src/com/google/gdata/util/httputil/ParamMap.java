
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gdata.util.httputil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * Utility data structure for maintaining URL/CGI params.
 *
 * 
 *
 * @deprecated Use the general {@link com.google.common.collect.Multimap}
 *     interface to represent name-value pairs. If order of insertion or support
 *     of duplicate name-value pairs is required, use {@link
 *     com.google.common.collect.LinkedListMultimap}. For the particular task of
 *     parsing and storing URI parameters, consider {@link
 *     com.google.gdata.util.common.net.UriParameterMap}, which is also the type returned
 *     by {@link com.google.gdata.util.common.net.UriBuilder} instances for URI query
 *     parameters. If you got here because you use {@link FormUrlDecoder} or
 *     {@link CgiParams}, then see the deprecation comments in those classes.
 */
@Deprecated
final public class ParamMap {

  /**
   * Get list of values associated with name
   *
   * @deprecated Use {@code Multimap.get(name)} and convert to an array only if
   *     you absolutely have to.
   */
  @Deprecated
  public String[] get(final String name) {
    List<String> l = map.get(name);
    return (l == null) ? null : l.toArray(new String[l.size()]);
  }

  /**
   * Currently used only by unit tests.
   */
  public boolean containsKey(final String name) {
    return map.containsKey(name);
  }

  /**
   * Add a new value for name
   *
   * @deprecated use {@code Multimap.put(name, value)}
   */
  @Deprecated
  public void append(final String name, final String value) {
    List<String> l = map.get(name);
    if (l != null) {
      l.add(value);
    } else {
      l = Lists.newArrayList(value);
      map.put(name, l);
    }
  }

  /**
   * Set values for name
   *
   * @deprecated Use {@code Multimap.putAll(name, collection)}, first converting
   *     an array to a list with {@code Arrays.asList(values)} if the collection
   *     is not already available.
   */
  @Deprecated
  public void put(final String name, final String[] values) {
    map.put(name, Lists.newArrayList(values));
  }

  /**
   * Returns a Map containing (String name) -> (String[] values) mappings.
   * Runs in O(number of mappings).
   *
   * @deprecated Use {@code Multimap.asMap()} to get a (String name) ->
   *     (Collection values) mapping and convert to an array only if you
   *     absolutely have to. Or, use the multimap directly.
   */
  @Deprecated
  public Map<String, String[]> toMap() {
    Map<String, String[]> newMap = Maps.newHashMap();

    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
      List<String> l = entry.getValue();
      newMap.put(entry.getKey(), l.toArray(new String[l.size()]));
    }

    return newMap;
  }

  final private Map<String, List<String>> map = Maps.newHashMap();
}
