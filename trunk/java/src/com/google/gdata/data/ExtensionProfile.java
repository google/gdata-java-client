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

import com.google.gdata.util.common.base.Pair;
import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.Attribute;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;

/**
 * Specifies a complete extension profile for an extended GData schema.
 * A profile is a set of allowed extensions for each type together with
 * additional properties.
 * <p>
 * For example, Calendar might allow {@code <gd:who>} within {@code
 * <atom:feed>}, and {@code <gd:when>}, {@code <gd:who>}, and {@code
 * <gd:where>} within {@code <atom:entry>}.
 *
 * 
 * 
 */
public class ExtensionProfile {

  /** Set of previously declared Kind.Adaptor classes. */
  private HashSet<Class<? extends Kind.Adaptor>> declared =
      new HashSet<Class<? extends Kind.Adaptor>>();

  /**
   * Adds the extension declarations associated with an {@link Kind.Adaptor}
   * instance, if the declaring class has not already added to this
   * profile.  The method is optimized to reduce the overhead of declaring
   * the same adaptor type multiple times within the same profile.
   */
  public void addDeclarations(Kind.Adaptor adaptor) {
    Class<? extends Kind.Adaptor> adaptorClass = adaptor.getClass();
    if (declared.add(adaptorClass)) {
      adaptor.declareExtensions(this);
    }
  }

  // Simple helper method to avoid cast warnings in very specific cases
  // where we know the cast is safe.
  @SuppressWarnings("unchecked")
  private Class<? extends ExtensionPoint> extensionPointClass(Class clazz) {
    return clazz;
  }

  /**
   * Specifies that type {@code extendedType} can contain an extension
   * described by {@code extDescription}.
   */
  public synchronized void declare(Class<? extends ExtensionPoint> extendedType,
                                   ExtensionDescription extDescription) {
    // When configuring an extension profile that is auto-extensible, remap
    // th extension point assocations from the specific type down to any
    // base adaptable type.  This ensures that extensions will be parseable
    // on a more generic base type.   As an example, this would map extensions
    // that are normally associated with EventEntry "up" to BaseEntry.
    boolean wasRequirednessRemoved = false;
    while (isAutoExtending &&
        Kind.Adaptable.class.isAssignableFrom(extendedType.getSuperclass())) {
      /* it is unsafe to pass require-ness to a the super class, so here we are
         removing the required-ness from the extension description. See for
         example bug 191364. */
      if (!wasRequirednessRemoved && extDescription.isRequired()) {
        wasRequirednessRemoved = true;
        extDescription = new ExtensionDescription(
            extDescription.getExtensionClass(),
            extDescription.getNamespace(),
            extDescription.getLocalName(),
            false,
            extDescription.isRepeatable(),
            extDescription.isAggregate(),
            extDescription.allowsArbitraryXml(),
            extDescription.allowsMixedContent());
      }
        extendedType = extensionPointClass(extendedType.getSuperclass());
    }

    ExtensionManifest manifest = getOrCreateManifest(extendedType);
    profile.put(extendedType, manifest);

    Pair<String, String> extensionQName =
        new Pair<String,String>(extDescription.getNamespace().getUri(),
            extDescription.getLocalName());

    manifest.supportedExtensions.put(extensionQName, extDescription);

    // Propagate the declarations down to any profiled subtypes.
    for(ExtensionManifest subclassManifest : manifest.subclassManifests) {
      subclassManifest.supportedExtensions.put(extensionQName, extDescription);
    }

    if (extDescription.allowsArbitraryXml()) {
      Class<? extends ExtensionPoint> extType =
          (Class<? extends ExtensionPoint>) extDescription.getExtensionClass();
      ExtensionManifest extManifest = getOrCreateManifest(extType);
      profile.put(extType, extManifest);
      declareArbitraryXmlExtension(extType, extDescription.allowsMixedContent());
    }

    nsDecls = null;
  }


  /**
   * Specifies that type {@code extendedType} can contain an extension described
   * by {@code extClass}, as determined by
   * {@link ExtensionDescription#getDefaultDescription(Class)}.
   */
  public synchronized void declare(Class<? extends ExtensionPoint> extendedType,
      Class<? extends Extension> extClass) {
    declare(extendedType, ExtensionDescription.getDefaultDescription(extClass));
  }


  /**
   * Declares that {@code extDesc} defines a feed extension.
   *
   * @deprecated Calls to this API should be replaced with calls to
   * {@link ExtensionProfile#declare(Class,ExtensionDescription)} where
   * the first argument is a specific {@link BaseFeed} subtype. The
   * {@link BaseFeed} class should only be used for mix-in types that
   * might appear in multiple feed types.  Its use for all feed declarations
   * can result in conflicts when mutiple feed types are declared into a
   * single extension profile, a common practice in client library service
   * initialization for services that return multiple feed types.
   */
  @Deprecated
  public synchronized void declareFeedExtension(ExtensionDescription extDesc) {
    declare(BaseFeed.class, extDesc);
  }


  /**
   * Declares that {@code extClass} defines a feed extension.
   *
   * @deprecated Calls to this API should be replaced with calls to
   * {@link ExtensionProfile#declare(Class,ExtensionDescription)} where
   * the first argument is a specific {@link BaseFeed} subtype. The
   * {@link BaseFeed} class should only be used for mix-in types that
   * might appear in multiple feed types.  Its use for all feed declarations
   * can result in conflicts when mutiple feed types are declared into a
   * single extension profile, a common practice in client library service
   * initialization for services that return multiple feed types.
   */
  @Deprecated
  public synchronized void declareFeedExtension(
      Class<? extends Extension> extClass) {
    declare(BaseFeed.class, extClass);
  }


  /**
   * Declares that {@code extDesc} defines an entry extension.
   *
   * @deprecated Calls to this API should be replaced with calls to
   * {@link ExtensionProfile#declare(Class,ExtensionDescription)} where
   * the first argument is a specific {@link BaseEntry} subtype. The
   * {@link BaseEntry} class should only be used for mix-in types that
   * might appear in multiple entry types.  Its use for all entry declarations
   * can result in conflicts when mutiple feed types are declared into a
   * single extension profile, a common practice in client library service
   * initialization for services that return multiple entry types.
   */
  @Deprecated
  public synchronized void declareEntryExtension(ExtensionDescription extDesc) {
    declare(BaseEntry.class, extDesc);
  }


  /**
   * Declares that {@code extClass} defines an entry extension.
   *
   * @deprecated Calls to this API should be replaced with calls to
   * {@link ExtensionProfile#declare(Class,ExtensionDescription)} where
   * the first argument is a specific {@link BaseEntry} subtype. The
   * {@link BaseEntry} class should only be used for mix-in types that
   * might appear in multiple entry types.  Its use for all entry declarations
   * can result in conflicts when mutiple feed types are declared into a
   * single extension profile, a common practice in client library service
   * initialization for services that return multiple entry types.
   */
  @Deprecated
  public synchronized void declareEntryExtension(
      Class<? extends Extension> extClass) {
    declare(BaseEntry.class, extClass);
  }

  /**
   * Specifies that type {@code extendedType} can contain arbitrary XML.
   *
   * @param extendedType the type being declared for
   */
  public synchronized void declareArbitraryXmlExtension(
      Class<? extends ExtensionPoint> extendedType) {
    declareArbitraryXmlExtension(extendedType, false);
  }

  /**
   * Specifies that type {@code extendedType} can contain arbitrary XML.
   *
   * @param extendedType the type being declared for
   * @param mixedContent if true, permit mixed content in the arbitrary XML.
   */
  public synchronized void declareArbitraryXmlExtension(
      Class<? extends ExtensionPoint> extendedType,
      boolean mixedContent) {
    ExtensionManifest manifest = getOrCreateManifest(extendedType);
    manifest.arbitraryXml = true;
    manifest.mixedContent = mixedContent;

    // Propagate the arbitrary xml declaration to any profiled subtypes.
    for(ExtensionManifest subclassManifest : manifest.subclassManifests) {
      subclassManifest.arbitraryXml = true;
      subclassManifest.mixedContent = mixedContent;
    }

    profile.put(extendedType, manifest);
    nsDecls = null;
  }


  /** Specifies additional top-level namespace declarations. */
  public synchronized void declareAdditionalNamespace(XmlNamespace ns) {
    additionalNamespaces.add(ns);
  }


  /** Specifies the type of feeds nested within {@code <gd:feedLink>}. */
  public synchronized void declareFeedLinkProfile(ExtensionProfile profile) {
    feedLinkProfile = profile;
    nsDecls = null;
  }


  /** Retrieves the type of feeds nested within {@code <gd:feedLink>}. */
  public synchronized ExtensionProfile getFeedLinkProfile() {
    return feedLinkProfile;
  }


  /** Specifies the type of entries nested within {@code <gd:entryLink>}. */
  public synchronized void declareEntryLinkProfile(ExtensionProfile profile) {
    entryLinkProfile = profile;
    nsDecls = null;
  }


  /** Retrieves the type of entries nested within {@code <gd:entryLink>}. */
  public synchronized ExtensionProfile getEntryLinkProfile() {
    return entryLinkProfile;
  }


  /**
   * Retrieves an extension manifest for a specific class (or one of
   * its superclasses) or {@code null} if not specified.
   */
  public ExtensionManifest getManifest(Class<?> extendedType) {
    ExtensionManifest manifest = null;
    while (extendedType != null) {
      manifest = profile.get(extendedType);
      if (manifest != null)
        return manifest;
      extendedType = extendedType.getSuperclass();
    }
    return null;
  }


  /**
   * Returns whether the given extended type has already been declared.  Note
   * that unlike {@link #getManifest(Class)}, it does not check the super
   * classes.
   */
  public boolean isDeclared(Class<?> extendedType) {
    return profile.containsKey(extendedType);
  }


  /** Retrieves a collection of all namespaces used by this profile. */
  public synchronized Collection<XmlNamespace> getNamespaceDecls() {

    if (nsDecls == null) {
      nsDecls = computeNamespaceDecls();
    }

    return nsDecls;
  }


  /** Internal storage for the profile. */
  private final Map<Class<?>, ExtensionManifest> profile =
    new HashMap<Class<?>, ExtensionManifest>();


  /** Additional namespaces. */
  private Collection<XmlNamespace> additionalNamespaces =
    new LinkedHashSet<XmlNamespace>();


  /** Nested feed link profile. */
  private ExtensionProfile feedLinkProfile;


  /** Nested entry link profile. */
  private ExtensionProfile entryLinkProfile;


  /** Namespace declarations cache. */
  private Collection<XmlNamespace> nsDecls = null;


  /** Profile supports auto-extension declaration */
  private boolean isAutoExtending = false;

  public void setAutoExtending(boolean v) { isAutoExtending = v; }
  public boolean isAutoExtending() { return isAutoExtending; }

  /**
   * When {@code true}, indicates that arbitrary XML is acceptable on any
   * {@link ExtensionPoint} when parsing using this profile.  The default
   * value is {@code true} to provide compliance with sections 6.3 of
   * RFC4287 (Atom Syntax) and section 6.2 of the AtomPub spec.
   */
  private boolean allowsArbitraryXml = true;

  /**
   * Configures the extension profile to specify whether any foreign XML
   * elements found when parsing within an {@link ExtensionPoint} should
   * be preserved.  If {@code false}, the presence of foreign XML will result
   * in parsing errors.  Arbitrary XML support is enabled by default in a
   * newly created profile.
   *
   * @param v {@code true} to enable foreign XML preservation, {@code false}
   *          otherwise.
   *
   * #see ExtensionPoint.getXmlBlob()
   */
  public void setArbitraryXml(boolean v) { allowsArbitraryXml = v; }

  /**
   * Returns whether foreign XML elements will be preserved within any
   * {@link ExtensionPoint}.
   *
   * @return {@code true} if foreign XML elements are preserved, {@code false}
   * otherwise.
   */
  public boolean allowsArbitraryXml() { return allowsArbitraryXml; }

  /** Internal helper routine. */
  private ExtensionManifest getOrCreateManifest(
      Class<? extends ExtensionPoint> extendedType) {

    // Look for a manifest associated with the extend type, and if it is
    // a precise match then return it.
    ExtensionManifest manifest = getManifest(extendedType);
    if (manifest != null && manifest.extendedType == extendedType) {
        return manifest;
    }

    ExtensionManifest newManifest = new ExtensionManifest(extendedType);

    // Compute the list of manifests for supertypes.  Do this using a stack,
    // so we can process them in reverse order (from the deepest superclass
    // to the closest).
    Stack<ExtensionManifest> superManifests = new Stack<ExtensionManifest>();
    while (manifest != null) {
      superManifests.push(manifest);
      manifest = getManifest(manifest.extendedType.getSuperclass());
    }

    // Propagate declarations from any superclass that is already in the
    // extension profile, and set up an association from the super manifest
    // to the subclass one so future declarations will propagate.
    while (!superManifests.empty()) {
      ExtensionManifest superManifest = superManifests.pop();
      newManifest.supportedExtensions.putAll(
          superManifest.supportedExtensions);
      newManifest.arbitraryXml = superManifest.arbitraryXml;
      superManifest.subclassManifests.add(newManifest);
    }

    // Look for any existing profile types that extend the newly added
    // one and set up a relationship mapping so future declarations on this
    // manifest will be propagated
    for(Map.Entry<Class<?>, ExtensionManifest> profileMapping :
        profile.entrySet()) {

      if (extendedType.isAssignableFrom(profileMapping.getKey())) {
        newManifest.subclassManifests.add(profileMapping.getValue());
      }
    }

    return newManifest;
  }


  private synchronized Collection<XmlNamespace> computeNamespaceDecls() {

    HashSet<XmlNamespace> result = new HashSet<XmlNamespace>();

    result.addAll(additionalNamespaces);

    for (ExtensionManifest manifest: profile.values()) {
      result.addAll(manifest.getNamespaceDecls());
    }

    if (feedLinkProfile != null) {
      result.addAll(feedLinkProfile.computeNamespaceDecls());
    }

    if (entryLinkProfile != null) {
      result.addAll(entryLinkProfile.computeNamespaceDecls());
    }

    return Collections.unmodifiableSet(result);
  }

  /**
   * Reads the ExtensionProfile XML format.
   */
  public class Handler extends XmlParser.ElementHandler {

    private ExtensionProfile configProfile;
    private ClassLoader configLoader;
    private List<XmlNamespace> namespaces =
              new ArrayList<XmlNamespace>();

    public Handler(ExtensionProfile configProfile, ClassLoader configLoader,
                   Attributes attrs) throws ParseException {
      this.configProfile = configProfile;
      this.configLoader = configLoader;

      if (attrs != null) {
        Boolean val = getBooleanAttribute(attrs, "arbitraryXml");
        if (val != null) {
          allowsArbitraryXml = val;
        }
      }
    }

    public void validate() {
    }


    @Override
    public void processEndElement() {
      validate();
    }


    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.gdataConfig)) {

        if (localName.equals("namespaceDescription")) {

          String alias = attrs.getValue("", "alias");
          if (alias == null) {
            ParseException pe = new ParseException(
                CoreErrorDomain.ERR.missingAttribute);
            pe.setInternalReason(
                      "NamespaceDescription alias attribute is missing");
            throw pe;
          }
          String uri = attrs.getValue("", "uri");
          if (uri == null) {
            ParseException pe = new ParseException(
                CoreErrorDomain.ERR.missingAttribute);
            pe.setInternalReason(
                      "NamespaceDescription uri attribute is missing");
            throw pe;
          }

          XmlNamespace declaredNs = new XmlNamespace(alias, uri);
          namespaces.add(declaredNs);
          declareAdditionalNamespace(declaredNs);
          return new XmlParser.ElementHandler();

        } else if (localName.equals("extensionPoint")) {

          return new ExtensionPointHandler(configProfile, configLoader,
                                           namespaces, attrs);
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }

  /**
   * Reads the ExtensionPoint XML format
   */
  public class ExtensionPointHandler extends XmlParser.ElementHandler {

    private ExtensionProfile configProfile;
    private ClassLoader configLoader;

    private Class<? extends ExtensionPoint> extensionPoint;
    private boolean arbitraryXml;
    private List<ExtensionDescription> extDescriptions =
      new ArrayList<ExtensionDescription>();
    private List<XmlNamespace> namespaces;

    public ExtensionPointHandler(ExtensionProfile configProfile,
                                 ClassLoader configLoader,
                                 List<XmlNamespace> namespaces,
                                 Attributes attrs)
        throws ParseException {

      this.configProfile = configProfile;
      this.configLoader = configLoader;
      this.namespaces = namespaces;

      String extendedClassName = attrs.getValue("", "extendedClass");
      if (extendedClassName == null) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.missingAttribute);
        pe.setInternalReason(
            "ExtensionPoint extendedClass attribute is missing");
        throw pe;
      }

      Class<?> loadedClass;
      try {
        loadedClass = configLoader.loadClass(extendedClassName);
      } catch (ClassNotFoundException e) {
        throw new ParseException(
            CoreErrorDomain.ERR.cantLoadExtensionPoint, e);
      }
      if (!ExtensionPoint.class.isAssignableFrom(loadedClass)) {
        throw new ParseException(
            CoreErrorDomain.ERR.mustExtendExtensionPoint);
      }
      extensionPoint = extensionPointClass(loadedClass);

      Boolean val = getBooleanAttribute(attrs, "arbitraryXml");
      if (val != null) {
        arbitraryXml = val;
      }
    }

    @Override
    public void processEndElement() {

      if (arbitraryXml) {
        declareArbitraryXmlExtension(extensionPoint);
      }

      for (ExtensionDescription extDescription: extDescriptions) {
        declare(extensionPoint, extDescription);
      }
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      if (namespace.equals(Namespaces.gdataConfig)) {
        if (localName.equals("extensionDescription")) {

          ExtensionDescription extDescription = new ExtensionDescription();
          extDescriptions.add(extDescription);
          return extDescription.new Handler(configProfile, configLoader,
                                            namespaces, attrs);
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }


  /**
   * Parses XML in the ExtensionProfile format.
   *
   * @param   configProfile
   *            Extension profile description configuration extensions.
   *
   * @param   classLoader
   *            ClassLoader to load extension classes
   *
   * @param   stream
   *            InputStream from which to read the description
   */
  public void parseConfig(ExtensionProfile configProfile,
                          ClassLoader classLoader,
                          InputStream stream) throws IOException,
                                                   ParseException {

    Handler handler = new Handler(configProfile, classLoader, null);
    new XmlParser().parse(stream, handler, Namespaces.gdataConfig,
                          "extensionProfile");
  }

  /**
   * Generates XML in the external config format.
   *
   * @param   w
   *          Output writer.
   *
   * @param   extProfile
   *          Extension profile.
   *
   * @throws  IOException
   */
  public void generateConfig(XmlWriter w,
                             ExtensionProfile extProfile) throws IOException {


    List<Attribute> epAttrs = new ArrayList<Attribute>();
    epAttrs.add(new Attribute("arbitraryXml", allowsArbitraryXml));
    w.startElement(Namespaces.gdataConfigNs, "extensionProfile", epAttrs,
        nsDecls);

    for (XmlNamespace namespace : additionalNamespaces) {

      List<Attribute> nsAttrs = new ArrayList<Attribute>();
      nsAttrs.add(new Attribute("alias", namespace.getAlias()));
      nsAttrs.add(new Attribute("uri", namespace.getUri()));
      w.simpleElement(Namespaces.gdataConfigNs, "namespaceDescription",
                      nsAttrs, null);
    }

    //
    // Get a list of the extended classes sorted by class name
    //
    TreeSet<Class<?>> extensionSet = new TreeSet<Class<?>>(
        new Comparator<Class<?>>() {
          public int compare(Class<?> c1, Class<?> c2) {
            return c1.getName().compareTo(c2.getName());
          }
        });

    for (Class<?> extensionPoint : profile.keySet()) {
      extensionSet.add(extensionPoint);
    }

    for (Class<?> extensionPoint : extensionSet) {

      ExtensionManifest  manifest = profile.get(extensionPoint);

      List<Attribute> ptAttrs = new ArrayList<Attribute>();
      ptAttrs.add(new Attribute("extendedClass", extensionPoint.getName()));
      ptAttrs.add(new Attribute("arbitraryXml", manifest.arbitraryXml));
      w.startElement(Namespaces.gdataConfigNs, "extensionPoint", ptAttrs, null);

      // Create an ordered list of the descriptions in this profile
      TreeSet<ExtensionDescription> descSet =
        new TreeSet<ExtensionDescription>();

      for (ExtensionDescription extDescription :
           manifest.getSupportedExtensions().values()) {
        descSet.add(extDescription);
      }

      // Now output based upon the ordered list
      for (ExtensionDescription extDescription : descSet) {
        extDescription.generateConfig(w, extProfile);
      }

      w.endElement(Namespaces.gdataConfigNs, "extensionPoint");
    }

    w.endElement(Namespaces.gdataConfigNs, "extensionProfile");
  }
}
