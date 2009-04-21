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

import com.google.common.annotations.VisibleForTesting;
import com.google.gdata.client.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The VersionRegistry class is used to manage and retrieve version information
 * about executing services. The registry supports the ability to configure
 * versions for a running thread (via the {@link #setThreadVersion(Version)}
 * method) or global defaults that will apply to all threads (using the
 * {@link #addDefaultVersion(Version, boolean)} method. Thread defaults will
 * have precedence over global defaults if present for the same service.
 * 
 * The class provides a singleton instance that is being used to manage version
 * information. This instance is initialized by the {@link #ensureRegistry()}
 * method. The active VersionRegistry instance can be retrieved using the
 * {@link #get()} method. This method will throw an
 * {@link IllegalStateException} if the version registry has not been
 * initialized to aid in the detection of when version-conditional code is being
 * executed in an environment where versions have net been configured.
 * 
 * The {@link VersionRegistry#getVersion(Class)} method can be used to request
 * the version information for a particular service.
 * 
 * A model for writing version conditional code based upon the registry is:
 * <code>
 *    Version myServiceVersion = 
 *         VersionRegistry.get().getVersion(MyService.class);
 *    if (myServiceVersion.isCompatible(MyService.VERSIONS.V1) {
 *      ... execute V1-specific handling ...
 *    }
 * </code>
 * 
 * VersionRegistry access is thread-safe.
 */
public class VersionRegistry {
  
  /**
   * Singleton registry instance.   The singleton is lazily initialized when the
   * {@link #ensureRegistry()} method is called.  The reason for this design is
   * to support the detect of version-conditional code running in unit tests.
   * Such tests need to be run in a version-aware test environment (that will
   * validate the behavior against all valid versions), so having a model so
   * that they will fail by default is helpful to guarantee this.
   */
  private static VersionRegistry versionRegistry;
  
  /**
   * Maintains the per-thread version information.  The field may be 
   * {@code null} if thread tracking is not enabled and the thread local value
   * may be {@code null} if no versions have been set for the current thread.
   */
  private ThreadLocal<List<Version>> threadVersions = 
      new ThreadLocal<List<Version>>();
 
  /**
   * Maintains the global defaults.
   */
  private List<Version> defaultVersions = new ArrayList<Version>();
  
  /**
   * Returns the current VersionRegistry, creating it if necessary. The
   * {@link #get()} method is preferred for most registry usage, as it enables
   * the discovery of the execution of version-conditional code in an
   * environment (such as unit test cases) where versioning has not been
   * properly configured.
   */
  public static synchronized VersionRegistry ensureRegistry() {
    if (versionRegistry == null) {
      versionRegistry = new VersionRegistry();
    }
    return versionRegistry;
  }
  
  /**
   * Resets the VersionRegistry instance to {@code null}.   This means that any
   * subsequent attempts to run version-specific code without version 
   * configuration will result in an {@link IllegalStateException} in
   * {@link #get()}.
   */
  @VisibleForTesting
  static void reset() {
    versionRegistry = null;
  }
  
  /**
   * Returns the version registry being used to manage version information.
   * @return the active version registry instance.
   * @throws IllegalStateException if the registry has not been initialized.
   */
  public static final VersionRegistry get() {
    if (versionRegistry == null) {
      // This should never happen for client, server, or code running in a
      // unit test context.   Missing version information indicates that the
      // version registry has not been properly initialized to meet the
      // expectations of version-dependent code.   In the case of test
      // execution, this generally means the test should be annotated to
      // indicate a version dependency (see the TestVersion annotation) and
      // also should be run in the context of a VersionedTestSuite that
      // ensures all supported versions are tested.
      throw new IllegalStateException("Uninitialized version registry");
    }
    return versionRegistry;
  }
  

  /**
   * Constructs a new Version instance based upon the value of a Java system
   * property associated with a {@link Service} class. The system property name
   * is computed from the service class name with ".version" appended. The
   * syntax of the property value is {@code "[service]<major>[.<minor>]"}. The
   * default value of the service is assumed to be the initiating or target
   * service and the minor revision will be assumed to be zero if not present.
   * If the associated system property is not set, the method will return
   * {@code null}.
   * 
   * @param serviceClass service class to use in computing the version property
   *        name.
   * @return the {@link Version} computed from the property of {@code null} if
   *         the property is not set.
   * @throws IllegalStateException if the property value does not contain valid
   *         revision information.
   */
  public static Version getVersionFromProperty(
      Class<? extends Service> serviceClass) {
    String propertyName = serviceClass.getName() + ".version";
    String versionProperty = System.getProperty(propertyName);
    if (versionProperty == null) {
      return null;
    }
    try {
      return new Version(serviceClass, versionProperty);
    } catch (IllegalArgumentException iae) {
      throw new IllegalStateException(
          "Invalid version property value: " + propertyName, iae);
    }
  }

  /**
   * Takes a list of {@link Version} instances and merges it into another
   * list.   A version in the source list will overwrite any value for the
   * same service (if any) in the target list.
   * @param target the target list of versions to merge into.
   * @param source the source list of versions that will be merged.
   */
  @VisibleForTesting
  static void mergeVersions(List<Version> target, List<Version> source) {
    
    // Check for conflicts with target list before making any changes,
    // accumulating the list of changed versions.
    for (Version checkVersion : source) {
      Version currentVersion = 
          Version.findServiceVersion(target, checkVersion.getServiceClass());
      if (currentVersion != null) {
        target.remove(currentVersion);
      }
    }
    
    // Add all of the new versions.
    target.addAll(source);
  }
  
  /**
   * Takes a {@link Version} instance and merges it into another
   * list, validating that any duplicate information for a given service
   * is a compatible version.
   * @param target the target list of versions to merge into.
   * @param source the source version that will be merged.
   */
  @VisibleForTesting
  static void mergeVersions(List<Version> target, Version source) {   
    mergeVersions(target, Arrays.asList(new Version [] { source }));
  }
  
  /**
   * Returns the list of default versions for the registry.  The default version
   * is the version that will be used if no version is explicitly selected.
   * 
   * @return list of default versions.
   */
  public List<Version> getDefaultVersions() {
    return defaultVersions;
  }
  
  /**
   * Adds a default version to the version registry. This will overwrite any
   * existing default version for the same service.
   * 
   * @param newDefault default version to add to the registry
   *                   (not <code>null</code>)
   * @param includeImplied if {@code true}, indicates that all implied versions
   *        associated with the new default should be set as defaults too.
   */
  public void addDefaultVersion(Version newDefault, 
      boolean includeImplied) {
    
    // Implement the addition using a copy into a new array.  This is done to
    // avoid requiring full synchronization of access to defaultVersions, where
    // additions will be infrequent and often happen at initialization time.
    ArrayList<Version> newDefaults = new ArrayList<Version>(defaultVersions);
    if (includeImplied) {
      mergeVersions(newDefaults, newDefault.getImpliedVersions());
    } else {
      mergeVersions(newDefaults, newDefault);
    }
    // Replace the current defaults with the updated list.
    defaultVersions = Collections.unmodifiableList(newDefaults);
  }
  
  /**
   * Sets the desired version for the current thread to the provided values.
   * This method will update any existing request version information set by
   * defaults or a previous call to this method.   The specified version (and
   * any related implied versions} will be set for the current thread until the
   * {@link #resetThreadVersion()} method is called to reset to the version
   * information back to the default state.
   *
   * @param version the new active version for this request.
   */
  public void setThreadVersion(Version version) {
    
    // Set the thread local to the list of versions implied by the requested
    // version.
    threadVersions.set(
        Collections.unmodifiableList(version.getImpliedVersions()));
  }
  
  /**
   * Returns the list of versions associated with the current thread or
   * {@code null} if there are currently no thread versions.
   * 
   * @return thread version list or {@code null}
   */
  public List<Version> getThreadVersions() {
    return threadVersions.get();
  }
  
  /**
   * Resets the version information for the current thread back to the
   * default state.
   */
  public void resetThreadVersion() {
    if (threadVersions != null) {
      threadVersions.remove();
    }
  }
 
  /**
   * Returns the the current list of active versions.   This list takes both
   * global defaults and thread versions into account.
   */
  @VisibleForTesting
  List<Version> getVersions() {
    
    List<Version> defaultList = getDefaultVersions();
    List<Version> threadList = getThreadVersions();
    if (threadList == null) {
      return defaultList;
    }
    
    List<Version> combinedList = 
        new ArrayList<Version>(defaultList.size() + threadList.size());
    combinedList.addAll(defaultList);
    mergeVersions(combinedList, threadList);
    return combinedList;
  }
  
  /**
   * Returns the version of a service.
   * 
   * @param serviceClass of the service to return.
   * @return version of the service.
   * @throws IllegalStateException if no version information could be found for
   *         the requested service.
   */
  public Version getVersion(Class<? extends Service> serviceClass) {
    
    Version v = null;
    List<Version> threadList = getThreadVersions();
    if (threadList != null) {
      v = Version.findServiceVersion(threadList, serviceClass);
    }
    if (v == null) {
      v = Version.findServiceVersion(getDefaultVersions(), serviceClass);
      if (v == null) {
        // This should never happen for client, server, or code running in a
        // unit test context.   Missing version information indicates that the
        // version registry has not been properly initialized to meet the
        // expectations of version-dependent code.   In the case of test
        // execution, this generally means the test should be annotated to
        // indicate a version dependency (see the TestVersion annotation) and
        // also should be run in the context of a VersionedTestSuite that
        // ensures all supported versions are tested.
        throw new IllegalStateException(
            "Attempt to access version information for unversioned service:" +
            serviceClass);
      }
    }
    return v;
  }
  
  /**
   * Resets the VersionRegistry to a clean state with no thread local
   * configuration and the specified set of version defaults.
   * 
   * @param initialDefaults the list of default versions that should be used to
   *        initialize the version registry, or {@code null} for an empty
   *        default list.
   */
  @VisibleForTesting
  public synchronized void reset(List<Version> initialDefaults) {
    threadVersions = new ThreadLocal<List<Version>>();
    if (initialDefaults != null) {
      defaultVersions = new ArrayList<Version>(initialDefaults);
    } else {
      defaultVersions = new ArrayList<Version>();
    }
  }
}
