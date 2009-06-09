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

import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.MetadataContext;
import com.google.gdata.util.ContentType;

import java.util.Collection;

/**
 * The StreamProperties interface defines the common set of properties for
 * input or output data streams that are being used to consume/produce data.
 */
public interface StreamProperties {

  /**
   * Returns the MIME content type of data on the stream.
   *
   * @return data content type
   */
  public ContentType getContentType();

  /**
   * Returns a {@link Collection} that contains the name of all query parameters
   * found in the request.
   */
  public Collection<String> getQueryParameterNames();

  /**
   * Returns the value of a request query parameter by name or {@code null}
   * if not found.
   */
  public String getQueryParameter(String name);

  /**
   * Returns the {@link ExtensionProfile} associated with the stream.
   *
   * @return extension profile or {@code null} for requests that use the
   * data model classes in com.google.gdata.model.
   */
  public ExtensionProfile getExtensionProfile();

  /**
   * Returns the {@link AltRegistry} that contains the set of supported
   * representations and the parser/generator configuration for them.
   */
  public AltRegistry getAltRegistry();
  
  /**
   * Returns the {@link ElementMetadata} for the root object that is being
   * read from or written to the stream.  This metadata will already be
   * bound to the appropriate {@link MetadataContext} for the currently
   * executing request.
   */
  public ElementMetadata<?, ?> getRootMetadata();
}
