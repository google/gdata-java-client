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

import com.google.common.collect.Maps;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.util.ContentType;

import java.util.Collection;
import java.util.Map;

/**
 * The StreamPropertiesBuilder class is a abstract builder base class that aids
 * in the construction of new {@link StreamProperties} instances.
 *
 * @param <T> the concrete subtype of StreamPropertiesBuilder that will be used
 *        as the return type for property setters to enable chaining.
 *
 * 
 */
public abstract class StreamPropertiesBuilder
    <T extends StreamPropertiesBuilder<T>> {

  protected AltRegistry altRegistry;
  protected ContentType contentType;
  protected ExtensionProfile extensionProfile;
  protected ElementMetadata<?, ?> rootMetadata;
  protected final Map<String, String> queryMap;

  /**
   * Constructs a new StreamPropertiesBuilder with no properties set.
   */
  protected StreamPropertiesBuilder() {
    queryMap = Maps.newHashMap();
  }

  /**
   * Constructs a new StreamPropertiesBuilder instance with the initial value of
   * all properties copied from another {@link StreamProperties} instance.
   *
   * @param source stream properties instance to copy from.
   */
  protected StreamPropertiesBuilder(StreamProperties source) {
    altRegistry = source.getAltRegistry();
    contentType = source.getContentType();
    extensionProfile = source.getExtensionProfile();
    rootMetadata = source.getRootMetadata();
    queryMap = Maps.newHashMap();
    for (String name : source.getQueryParameterNames()) {
      queryMap.put(name, source.getQueryParameter(name));
    }
  }

  @SuppressWarnings("unchecked")
  public final T thisInstance() {
    return (T) this;
  }

  /**
   * Sets the {@link AltRegistry} property that should be used for instances
   * created by the builder.
   *
   * @param altRegistry alt registry to set in built instances.
   * @return this builder (to enable initialization chaining).
   */
  public T setAltRegistry(AltRegistry altRegistry) {
    this.altRegistry = altRegistry;
    return thisInstance();
  }
  /**
   * Sets the {@link ContentType} property that should be used for instances
   * created by the builder.
   *
   * @param contentType content type to set in built instances.
   * @return this builder (to enable initialization chaining).
   */
  public T setContentType(ContentType contentType) {
    this.contentType = contentType;
    return thisInstance();
  }

  /**
   * Sets the {@link ExtensionProfile} property that should be used for
   * instances created by the builder.
   *
   * @param extensionProfile extension profile to set in built instances.
   * @return this builder (to enable initialization chaining).
   */
  public T setExtensionProfile(
      ExtensionProfile extensionProfile) {
    this.extensionProfile = extensionProfile;
    return thisInstance();
  }

  /**
   * Sets the value of a query parameter in the query {@link Map} that should be
   * used for instances created by the builder.   Any existing value with the
   * same name will be overwritten.
   *
   * @param name query parameter name
   * @param value query parameter value
   * @return this builder (to enable initialization chaining).
   */
  public T setQueryParameter(String name, String value) {
    thisInstance().queryMap.put(name, value);
    return thisInstance();
  }

  /**
   * Adds all query parameter values defined in the provided {@link Map} to
   * the set of query parameters used for instances created by the builder.
   * Any existing query parameter mappings with names contained in the map will
   * be overwritten.
   *
   * @param queryMap query map to set in built instances.
   * @return this builder (to enable initialization chaining).
   */
  public T setQueryParameters(Map<String, String> queryMap) {
    this.queryMap.putAll(queryMap);
    return thisInstance();
  }
  
  /**
   * Sets the {@link ElementMetadata} that should be used for instances created
   * by the builder.
   * 
   * @param elementMetadata element metadata to set in built instances.
   * @return this builder (to enable initialization chaining).
   */
  public T setElementMetadata(ElementMetadata<?, ?> elementMetadata) {
    this.rootMetadata = elementMetadata;
    return thisInstance();
  }

  /**
   * The StreamPropertiesImpl class is a simple immutable value object that
   * implements the {@link StreamProperties} interface.
   */
  protected static class StreamPropertiesImpl 
      implements StreamProperties {

    private final AltRegistry altRegistry;
    private final ContentType contentType;
    private final ExtensionProfile extensionProfile;
    private final Map<String, String> queryMap;
    private final ElementMetadata<?, ?> elementMetadata;

    /**
     * Constructs a new StreamPropertiesImpl instance from the values contained
     * in a builder instance.
     *
     * @param builder build instance.
     */
    protected StreamPropertiesImpl(StreamPropertiesBuilder<?> builder) {
      this.altRegistry = builder.altRegistry;
      this.contentType = builder.contentType;
      this.extensionProfile = builder.extensionProfile;
      this.queryMap = builder.queryMap;
      this.elementMetadata = builder.rootMetadata;
    }

    public AltRegistry getAltRegistry() {
      return altRegistry;
    }

    public ContentType getContentType() {
      return contentType;
    }

    public ExtensionProfile getExtensionProfile() {
      return extensionProfile;
    }

    public Collection<String> getQueryParameterNames() {
      return queryMap.keySet();
    }

    public String getQueryParameter(String name) {
      return queryMap.get(name);
    }

    public ElementMetadata<?, ?> getRootMetadata() {
      return elementMetadata;
    }
  }
}
