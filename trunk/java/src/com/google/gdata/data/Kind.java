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


package com.google.gdata.data;

import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Kind class defines annotation types, interfaces and static helper 
 * methods for GData Kind extension handling.  A GData <i>Kind</i> refers to 
 * a specific extension profile configuration for an Atom feed/entry or RSS 
 * channel/item.
 *
 * 
 */
public class Kind {

  /**
   * The location of the <code>META-INF</code> jar directory where GData kind
   * mapping information is stored.
   */
  public static final String META_DIRECTORY = "META-INF/gdata/kinds/";
  
  /**
   * Caches the mappings from a kind term to the {@link Adaptor} classes that
   * handle the kind.  Since these are configured by JAR-based metadata,
   * they are guaranteed to be constant once loaded unless/until the
   * classloader for GData java library is bounced.
   */
  private static Map<String, List<Class<Adaptor>>> kindAdaptors =
      new HashMap<String, List<Class<Adaptor>>>();

  /**
   * The Term annnotation type is used to annotate {@link Adaptor}
   * classes to declare the GData kind {@link Category} term value(s)
   * implemented by the adaptor type.
   */
  @Target(ElementType.TYPE)
  public @interface Term {

    /**
     * Specifies the term value within the {link Namespace.gKind} scheme
     * that is handled by the {@link Adaptor} class.
     */
    public String value();
  }

  /**
   * The Adaptable interface is implemented by GData {@link ExtensionPoint}
   * types that can be flexible adapted based upon the presence of GData
   * kind category elements.
   */
  public interface Adaptable {

    /**
     * Associates a new {@link Adaptor} with this {@code Adaptable} instance.
     */
    void addAdaptor(Adaptor adaptor);

    /**
     * Returns the collection of {@link Adaptor} instances associated with the
     * this {@code Adaptable} instance.
     */
    Collection<Adaptor> getAdaptors();

    /**
     * Returns a {@link Adaptor} instance associated with this
     * {@code Adaptable} instance of the specified type, or {code null}
     * if none is available..
     */
    <E extends Adaptor> E getAdaptor(Class<E> adaptorClass);
  }

  /**
   * The Adaptor interface is implemented by {@link Extension} classes
   * that provide extension declaration and data modeling support for 
   * specific GData kinds.
   */
  public interface Adaptor {

    /**
     * Declares the {@link ExtensionDescription} of each {@link Extension}
     * expected by the implementing {@link ExtensionPoint} in the target
     * profile.  This API should generally not be called directly by clients;
     * the {@link ExtensionProfile#addDeclarations(Kind.Adaptor)} method should
     * be used to declare extensions.
     *
     * @param extProfile the profile that should be extended.
     * @see ExtensionProfile#addDeclarations(Kind.Adaptor)
     */
    void declareExtensions(ExtensionProfile extProfile);
  }

  /**
   * A simple helper class implementation of the {@link Adaptable} interface.
   * Classes that need to implement {@code Adaptable} can construct an
   * instance of this class and delegate to it.
   */
  public static class AdaptableHelper implements Adaptable {

    private List<Kind.Adaptor> adaptors = new ArrayList<Kind.Adaptor>();

    public void addAdaptor(Kind.Adaptor adaptor) {
      adaptors.add(adaptor);
    }

    public Collection<Kind.Adaptor> getAdaptors() {
      return adaptors;
    }

    public <E extends Kind.Adaptor> E getAdaptor(Class<E> adaptorClass) {
      for (Kind.Adaptor adaptor : adaptors) {
        if (adaptor.getClass().equals(adaptorClass)) {
          return adaptorClass.cast(adaptor);
        }
      }
      return null;
    }
  }

  /**
   * The AdaptorException class defines a simple {@link ServiceException}
   * type that is thrown on kind adaptation failures.
   */
  public static class AdaptorException extends ServiceException {

    public AdaptorException(String message) {
      super(message);
      setHttpErrorCodeOverride(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    public AdaptorException(String message, Throwable cause) {
      super(message, cause);
      setHttpErrorCodeOverride(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    public AdaptorException(Throwable cause) {
      super(cause);
      setHttpErrorCodeOverride(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }
  }

  public static boolean isKindCategory(Category category) {
    return Namespaces.gKind.equals(category.getScheme());
  }

  /**
   * Returns the kind service name associatd with a particular Kind category
   * term value.  It converts a kind term URI to a service file name that
   * is used for dynamic discovery of {@link Adaptor} class implementations
   * for the kind.
   */
  public static String getKindServiceName(String kindTerm) {

    StringBuilder serviceName = new StringBuilder(kindTerm.length());
    try {
      URL termUrl = new URL(kindTerm);

      // Invert any dotted components of the host name
      String [] hostComponents = termUrl.getHost().split("\\W");
      int lastIndex = hostComponents.length - 1;
      for (int i = lastIndex; i >= 0; i--) {
        if (i != lastIndex) {
          serviceName.append(".");
        }
        serviceName.append(hostComponents[i]);
      }

      // Convert the path, substituting dot separators for path
      // element separators
      String [] pathComponents = termUrl.getPath().split("\\W");
      for (int i = 0; i < pathComponents.length; i++) {
        if (pathComponents[i].length() > 0) {
          serviceName.append(".");
          serviceName.append(pathComponents[i]);
        }
      }

      // Convert the ref (if any), substituting dot separators.
      if (termUrl.getRef() != null) {
        String [] refComponents = termUrl.getRef().split("\\W");
        for (int i = 0; i < refComponents.length; i++) {
          if (refComponents[i].length() > 0) {
            serviceName.append(".");
            serviceName.append(refComponents[i]);
          }
        }
      }


    } catch (java.net.MalformedURLException mue) {
      throw new IllegalArgumentException("Kind term must be a valid URL", mue);
    }
    return serviceName.toString();
  }

  /**
   * Returns that {@link Adaptor} class that handles the
   * declaration of extensions within an {@link ExtensionProfile} based
   * upon the kind term value.  A return value of {@code null} indicates
   * that no adaptor class could be located for this cobintation of kind and
   * {@link Adaptable} type.
   */
  public static Class<Adaptor> getAdaptorClass(String kindTerm,
                                               Adaptable adaptable)
      throws AdaptorException {

    ClassLoader cl = adaptable.getClass().getClassLoader();

    List<Class<Adaptor>> adaptorList = kindAdaptors.get(kindTerm);
    if (adaptorList == null) {
      // Lazily load the adaptor list for a kind on first usage and store
      // in the cache.
      adaptorList = new ArrayList<Class<Adaptor>>();
      String termService = getKindServiceName(kindTerm);
      InputStream serviceStream;
      try {
        serviceStream = cl.getResourceAsStream(META_DIRECTORY
            + termService);
        if (serviceStream == null) {
          return null;
        }
        BufferedReader rdr =
          new BufferedReader(new InputStreamReader(serviceStream));
        String line;
        while ((line = rdr.readLine()) != null) {
          if (line.charAt(0) == '#') {  // comment line
            continue;
          }
          
          adaptorList.add((Class<Adaptor>) cl.loadClass(line));
        }
      } catch (IOException ioe) {
        throw new AdaptorException("Unable to load Adaptor service info", ioe);
      } catch (ClassNotFoundException cnfe) {
        throw new AdaptorException("Unable to load Adaptor class", cnfe);
      }
      kindAdaptors.put(kindTerm, adaptorList);
    }

    // A mix-in adaptor type will only have one mapping, that can be used
    // for all valid contexts since it doesn't rely upon inheritance.
    if (adaptorList.size() == 1) {
      return adaptorList.get(0);
    }

    Class<Adaptor> adaptorclass = null;

    // Inheritance based adaptation:  Look for Adaptor instance that shares
    // a superclass relationship with the input Adaptable (for example,
    // both derive from BaseFeed).
    for (Class<Adaptor> adaptorClass : adaptorList) {
      Class<? extends Adaptable> checkClass = adaptable.getClass();
      while (Adaptable.class.isAssignableFrom(checkClass)) {
        if (checkClass.isAssignableFrom(adaptorClass)) {
          return adaptorClass;
        }
        checkClass = (Class<? extends Adaptable>) checkClass.getSuperclass();
      }
    }
    return null;
  }

  /**
   * Returns an {@link Adaptor} instance associated with the specified
   * kind that is associated with the target {@link Adaptable}.  Returns
   * {@code null} if no Adaptor can be found.
   */
  static public Adaptor getAdaptor(String kindTerm, Adaptable adaptable) 
      throws AdaptorException {

    Class<Adaptor> adaptorClass = getAdaptorClass(kindTerm, adaptable);
    if (adaptorClass == null) {
      return null;
    }
    Adaptor adaptor = adaptable.getAdaptor(adaptorClass);
    if (adaptor == null) {

      // Look for an adaptor constructor that can take the adaptable
      // instance as an argument.
      Constructor<?> adaptorConstructor = null;
      Class<?> constructorArgClass = adaptable.getClass();
      while (constructorArgClass != null) {
        try {
          adaptorConstructor = adaptorClass.getConstructor(constructorArgClass);
          break;
        } catch (NoSuchMethodException nsme) {
          // Move on to parent of adaptor class and check again
          constructorArgClass = constructorArgClass.getSuperclass();
        }
      }

      // If not found, look for one with a null-arg constructor.  This
      // means it is a mix-in style kind, rather than an entry or feed
      // extension.
      if (adaptorConstructor == null) {
        try {
          adaptorConstructor = adaptorClass.getConstructor();
        } catch (NoSuchMethodException nsme) {
          throw new AdaptorException("Unable to construct Adaptor " +
              adaptorClass + " instance for " +
              adaptable.getClass());
        }
      }

      // Construct the new Adaptor instance
      try {
        if (constructorArgClass == null) {
          adaptor = (Adaptor)adaptorConstructor.newInstance();
        } else {
          adaptor = (Adaptor)adaptorConstructor.newInstance(adaptable);
        }
      } catch (RuntimeException re) {
        throw re;
      } catch (Exception e) {
        throw new AdaptorException("Unable to create kind Adaptor", e);
      }

      // Save the adaptable
      adaptable.addAdaptor(adaptor);
    }
    return adaptor;
  }
}
