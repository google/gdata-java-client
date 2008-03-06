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

package com.google.gdata.util;

import com.google.gdata.client.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** 
 * The VersionRegistry class is an abstract base class used to manage and 
 * retrieve a version information about services.   It can be subclassed to
 * to implement a storage model for version information (by implementing the
 * {@link #getVersions()} method).   The class also provides management of a
 * singleton instance of a VersionRegistry subclass that is being used to
 * manage version information.   This instance is initialized by subclasses
 * via the {@link #initSingleton(VersionRegistry)} method.   The active
 * VersionRegistry instance can be retrieved using the {@link #get()} method.
 * 
 * Clients of the API can use the {@link VersionRegistry#getVersions()}
 * method to get a list of all services with version information or the 
 * {@link VersionRegistry#getVersion(Class)} method to discover version 
 * information for a particular service.
 */
public abstract class VersionRegistry {
  
  /**
   * Singleton registry instance.
   */
  private static VersionRegistry versionRegistry;
  
  /**
   * Initializes the VersionRegistry instance.
   * @param registry initialization version registry
   * @throws IllegalStateException if the registry is already initialized.
   */
  protected static void initSingleton(VersionRegistry registry) {
    if (versionRegistry != null) {
      throw new IllegalStateException("Registry already initialized");
    }
    versionRegistry = registry;
  }
  
  /**
   * Resets the VersionRegistry instance to {@code null}
   */
  protected static void resetSingleton() {
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
   * Finds a matching version for {@code serviceClass} in a list of versions,
   * or returns {@code null} otherwise.
   * @param versionList the list of versions to search.
   * @param serviceClass the service class to match.
   * @return the matching version or {@code null}.
   */
  protected static Version getVersion(List<Version> versionList, 
      Class<? extends Service> serviceClass) {
    for (Version v : versionList) {
      if (v.getServiceClass().equals(serviceClass)) {
        return v;
      }
    }
    return null;
  }  
  
  /**
   * Takes a list of {@link Version} instances and merges it into another
   * list, validating that any duplicate information for a given service
   * is a compatible version.
   * @param target the target list of versions to merge into.
   * @param source the source list of versions that will be merged.
   * @throws IllegalStateException if the source list contains a version that
   *            is incompatible with an existing version for the same service in
   *            the target list.
   */
  protected static void mergeVersions(List<Version> target, 
                                      List<Version> source)
      throws IllegalStateException {
    
    // Check for conflicts with target list before making any changes,
    // accumulating the list of changed versions.
    List<Version> newVersions = new ArrayList<Version>();
    for (Version checkVersion : source) {
      Version currentVersion = 
          getVersion(target, checkVersion.getServiceClass());
      if (currentVersion != null) {
        if (!currentVersion.isCompatible(checkVersion)) {
          throw new IllegalArgumentException("Conflicting versions:"
             + "current: " + currentVersion.toString()
             + "new: " + checkVersion.toString());
        }
      } else {
        newVersions.add(checkVersion);
      }
    }
    
    // Add all of the new versions.
    target.addAll(newVersions);   
  }
  
  /**
   * Takes a {@link Version} instance and merges it into another
   * list, validating that any duplicate information for a given service
   * is a compatible version.
   * @param target the target list of versions to merge into.
   * @param source the source version that will be merged.
   * @throws IllegalStateException if the source list contains a version that
   *            is incompatible with an existing version for the same service in
   *            the target list.
   */
  protected static void mergeVersion(List<Version> target, Version source)
      throws IllegalStateException {   
    mergeVersions(target, Arrays.asList(new Version [] { source }));
  }
  
  /**
   * Returns the the current version list.   This method is implemented by
   * concrete subclasses to provide the storage model for version tracking.
   * @return a list of versions associated with the current execution context.
   */
  public abstract List<Version> getVersions();
  
  /**
   * Returns the version of a service.
   * 
   * @param serviceClass of the service to return.
   * @return version of the service.
   * @throws IllegalStateException if no version information could be found for
   *         the requested service.
   */
  public Version getVersion(Class<? extends Service> serviceClass) {
    
    List<Version> versions = getVersions();
    Version v = getVersion(versions, serviceClass);
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
    return v;
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
   * @throws NumberFormatException if the property value does not contain valid
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
}
