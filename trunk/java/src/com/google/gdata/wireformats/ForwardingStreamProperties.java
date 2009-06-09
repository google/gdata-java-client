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
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.util.ContentType;

import java.util.Collection;

/**
 * A {@link StreamProperties} implementation that forwards all
 * calls to another {@link StreamProperties}.
 *
 * <p>Subclass this and override the methods you want modified
 * to create a wrapper for {@link StreamProperties}.
 *
 * 
 */
public class ForwardingStreamProperties implements StreamProperties {
  private final StreamProperties delegate;

  public ForwardingStreamProperties(StreamProperties delegate) {
    Preconditions.checkNotNull(delegate, "delegate");

    this.delegate = delegate;
  }

  public ContentType getContentType() {
    return delegate.getContentType();
  }

  public Collection<String> getQueryParameterNames() {
    return delegate.getQueryParameterNames();
  }

  public String getQueryParameter(String name) {
    return delegate.getQueryParameter(name);
  }

  public ExtensionProfile getExtensionProfile() {
    return delegate.getExtensionProfile();
  }

  public AltRegistry getAltRegistry() {
    return delegate.getAltRegistry();
  }

  public ElementMetadata<?, ?> getRootMetadata() {
    return delegate.getRootMetadata();
  }
}
