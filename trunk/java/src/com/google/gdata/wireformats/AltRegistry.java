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
import com.google.common.collect.Maps;
import com.google.gdata.util.ContentType;
import com.google.gdata.wireformats.input.InputParser;
import com.google.gdata.wireformats.output.OutputGenerator;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * The AltRegistry class maintains a registry of supported alternate
 * representation formats along with the configuration of input and output
 * handlers that will be used to process wire format content using the
 * representations.
 * <p>
 * The registry supports a delegation model where lookup failures can be
 * delegated to a different registry to support tiered lookup models.  It also 
 * has a locking model to avoid changes occurring after initialization has been
 * completed.
 * <p>
 * The class provides a locking model where the contents of the registry can be
 * made immutable using the {@link #lock()} method.   After this call, no
 * subsequent changes may be made to the registry and it is thread safe to
 * share across multiple threads.
 * 
 * 
 */
public class AltRegistry {

  /**
   * Simple value helper class containing the configuration of handers for a
   * registered format.
   */
  private static class AltHandlers {

    private final InputParser<?> parser;
    private final OutputGenerator<?> generator;
    
    AltHandlers(InputParser<?> parser, OutputGenerator<?> generator) {
      this.parser = parser;
      this.generator = generator;
    }
  }

  /**
   * Contains a mapping from alt parameter name values to the associated format
   * instance selected by each name.
   */
  private final Map<String, AltFormat> nameMap;
  

  /**
   * Contains a mapping from alt parameter media type names to an alt format
   * that represents the alternate content representation type
   * selected by the media type.
   */
  private final Map<String, AltFormat> typeMap;

  /**
   * Contains a mapping from an alt format instance to the configuration for the
   * format
   */
  private final Map<AltFormat, AltHandlers> altHandlers;
  
  /**
   * If {@code true}, indicates that the registry is locked against further
   * changes.
   */
  private boolean locked;

  /**
   * If non-null, contains a default registry that will be used as a delegate
   * for all lookups that cannot be satisfied by this instance.
   */
  private AltRegistry delegate;

  /**
   * Constructs a new AltRegistry with no default registry configuration.
   */
  public AltRegistry() {
    this(null);
  }

  /**
   * Constructs a new AltRegistry with identical registration state to the
   * provided registry.  If the input value is {@code null}, a clean registry
   * will be created.
   * 
   * @param origRegistry registry containing default configuration that will be
   *        used for lookups that can't be satisfied by the local registry.
   */
  public AltRegistry(AltRegistry origRegistry) {
    if (origRegistry != null) {
      nameMap =  Maps.newHashMap(origRegistry.nameMap);
      typeMap =  Maps.newHashMap(origRegistry.typeMap);
      altHandlers = Maps.newHashMap(origRegistry.altHandlers);
      delegate = origRegistry.delegate;
    } else {
      nameMap = Maps.newHashMap();
      typeMap = Maps.newHashMap();
      altHandlers = Maps.newHashMap();
    }
  }

  /**
   * Registers the name mapping(s) for an alternate representation.
   * 
   * @param format format to register name aliases for.
   */
  private void registerFormat(AltFormat format) {
    nameMap.put(format.getName(), format);
    if (format.isSelectableByType()) {
      typeMap.put(format.getContentType().getMediaType(), format);
    }
  }

  /**
   * Registers the configuration for an {@link AltFormat}, replacing any
   * existing configuration for the format in the registry.
   * 
   * @param format format to register
   * @param parser input parser to use for the format (or {@code null} if not
   *        supported for input.
   * @param generator output generator to use for the format.
   * @throws IllegalStateException if registry has been locked.
   */
  public void register(AltFormat format, InputParser<?> parser, 
      OutputGenerator<?> generator) {
    
    Preconditions.checkNotNull(format);
    Preconditions.checkNotNull(generator);
    Preconditions.checkState(!locked, "Registry is locked against changes");

    registerFormat(format);
    altHandlers.put(format, new AltHandlers(parser, generator));
  }
  
  /**
   * Locks the registry against further changes.   Any attempts to call the
   * {@link #register(AltFormat, InputParser, OutputGenerator)} API
   * after lock has been called will fail.
   */
  public void lock() {
    locked = true;
  }

  /**
   * Returns the alt format that has been registered with the specified name.
   * 
   * @param name format name.
   * @return registered format matching the name or {@code null}.
   */
  public AltFormat lookupName(String name) {
    AltFormat format = nameMap.get(name);
    if (format == null && delegate != null) {
      format = delegate.lookupName(name);
    }
    return format;
  }
  
  /**
   * Returns the alt format that has been registered with the specified content
   * type.
   * 
   * @param contentType type to look up.
   * @return registered format matching the type or {@code null}.
   */
  public AltFormat lookupType(ContentType contentType) {
    
    // Do a direct lookup by media type first (fast)
    AltFormat format = typeMap.get(contentType.getMediaType());
    if (format == null) {
      // If no match, use MIME type matching algorithm (slower but more precise)
      for (AltFormat testFormat : typeMap.values()) {
        if (contentType.match(testFormat.getContentType())) {
          format = testFormat;
          break;
        }
      }
    }
    if (format == null && delegate != null) {
      return delegate.lookupType(contentType);
    }
    return format;
  }

  /**
   * Returns a collection of all registered formats in the registry. The
   * collection does not include any values from the default registry provided
   * at construction time.
   * 
   * @return collection of all registered alt formats.
   */
  public Collection<AltFormat> registeredFormats() {
    return Collections.unmodifiableCollection(altHandlers.keySet());
  }

  /**
   * Returns the {@link InputParser} for a format or {@code null} if the format
   * is not registered or does not support input parsing.
   * 
   * @param altFormat format to locate parser for.
   * @return parser for format or {@code null}.
   */
  public InputParser<?> getParser(AltFormat altFormat) {
    AltHandlers handlers = altHandlers.get(altFormat);
    if (handlers != null) {
      return handlers.parser;
    }
    if (delegate != null) {
      return delegate.getParser(altFormat);
    }
    return null;
  }

  /**
   * Returns the {@link OutputGenerator} for the provided format or {@code null}
   * if the format is not registered.
   * 
   * @param altFormat format to locate generator for.
   * @return generator for format or {@code null}.
   */
  public OutputGenerator<?> getGenerator(AltFormat altFormat) {
    AltHandlers handlers = altHandlers.get(altFormat);
    if (handlers != null) {
      return handlers.generator;
    }
    if (delegate != null) {
      return delegate.getGenerator(altFormat);
    }
    return null;
  }
  
  /**
   * Sets a delegate registry that will be used to satisfy lookups that cannot
   * be resolved from the local registry.
   * 
   * @param delegate delegate registry.
   */
  public void setDelegate(AltRegistry delegate) {
    Preconditions.checkState(!locked, "Registry is locked against changes");
    this.delegate = delegate;
  }
  
  /**
   * Returns {@code true} has an identical registration with the target
   * registry for the specified alt format.   Currently, this is only true if
   * the copy constructor was used to do the initial registration and no
   * subsequent changes have been made.
   * 
   * @param targetRegistry target registry to test
   * @param altFormat alt format to test
   * @return {@code true} if registration for format is the same.
   */
  public boolean hasSameHandlers(AltRegistry targetRegistry, 
      AltFormat altFormat) {
    AltHandlers thisHandlers = altHandlers.get(altFormat);
    AltHandlers targetHandlers = targetRegistry.altHandlers.get(altFormat);
    if (thisHandlers == null) {
      return targetHandlers == null;
    }
    return (targetHandlers == null) ? false :
      thisHandlers.generator == targetHandlers.generator &&
      thisHandlers.parser == targetHandlers.parser;
  }
}
