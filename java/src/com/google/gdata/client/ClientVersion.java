/* Copyright (c) 2006 Google Inc.
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


package com.google.gdata.client;

import com.google.gdata.util.VersionRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The ClientVersion class provides a model for managing and accessing service
 * version information within the GData Java client library. It manages a single
 * global list of version data for all services that can be retrieved using the
 * {@link ClientVersion#get()} method and then information on service versions
 * can be accessed using the {@link VersionRegistry#getVersions()} and
 * {@link VersionRegistry#getVersion(Class)} methods.
 */
public class ClientVersion extends VersionRegistry {

  private static final ClientVersion clientRegistry = new ClientVersion();
  
  /**
   * The current list of versions across all client services.
   */
  private static final List<Version> versions = new ArrayList<Version>();
  
  /**
   * Called to initialize the GData client version tracking model for a 
   * particular service.  The method may be called by multiple {@link Service} 
   * subclasses to initialize service-specific version information.   It will 
   * manage version data across all services and detect version conflicts.
   * @param serviceVersion version information for the initialized service.
   * @throws IllegalStateException unable to initialize version system.
   * @throws IllegalArgumentException a version in the input list conflicts
   *         with previously initialize information for the same service. 
   */
  /* package*/ static void init(Version serviceVersion)
      throws IllegalStateException, IllegalArgumentException {
    
    // Initialize the global registry on first use.
    VersionRegistry registry = VersionRegistry.get();
    if (registry == null) {
      VersionRegistry.initSingleton(clientRegistry);
      registry = clientRegistry;
    }
    
    // Merge this serviceVersion into the global list, since init() may be
    // called multiple times for different services.
    mergeVersions(registry.getVersions(),
        Arrays.asList(new Version [] {serviceVersion}));
  }

  /**
   * Resets all version state as if the {@link #init} method had
   * never been called.   Used only for unit testing.
   */
  public static void reset() {
    versions.clear();
    VersionRegistry.resetSingleton();
  }
  
  // Protected constructor to ensure singleton behavior.
  protected ClientVersion() {}
  
  @Override
  public List<Version> getVersions() {
   return versions;
  }
}
