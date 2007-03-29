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


package com.google.gdata.data;

import com.google.gdata.util.common.base.Pair;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlBlob;
import com.google.gdata.util.XmlNamespace;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.XmlParser.ElementHandler;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Base class for GData data types that support hosting extensions.
 * <p>
 * For example, a calendar {@code <atom:entry>} supports hosting
 * {@code <gd:when>}.
 * <p>
 * The set of accepted extensions is defined within {@link Manifest}.
 *
 * 
 * 
 */
public class ExtensionPoint {



  /** A specification of acceptable extensions within an extension point. */
  static public class Manifest {


    /** Maps (Namespace URI, local name) to corresponding extension data. */
    final Map<Pair<String, String>, ExtensionDescription> supportedExtensions =
      new HashMap<Pair<String, String>, ExtensionDescription>();


    /**
     * Specifies whether the extension point supports arbitrary XML
     * ({code xs:any}). If it does, it is available through
     * {@link ExtensionPoint#xmlBlob}.
     */
    boolean arbitraryXml = false;


    /**
     * Returns the Map from namespace/localname String pairs to supporting
     * Extension class and manifest information.
     */
    public Map<Pair<String, String>, ExtensionDescription>
        getSupportedExtensions() {

      return Collections.unmodifiableMap(supportedExtensions);
    }


    /**
     * Retrieves a collection of namespace declarations for all possible
     * extensions based on this manifest.
     */
    Collection<XmlWriter.Namespace> getNamespaceDecls() {

      Collection<XmlWriter.Namespace> nsDecls =
        new HashSet<XmlWriter.Namespace>();

      for (ExtensionDescription extDescription: supportedExtensions.values()) {
        nsDecls.add(extDescription.getNamespace());
      }

      return nsDecls;
    }
  }


  /** Collection of non-repeating extensions.  Uses {@link LinkedHashMap} in
   * order to provide a predictable generation output order based upon
   * insertion order. */
  private Map<Class, Extension> nonRepeatingExtensionMap =
    new LinkedHashMap<Class, Extension>();


  /** Collection of repeating extensions.  Uses {@link LinkedHashMap} in
   * order to provide a predictable generation output order based upon
   * insertion order. */
  private Map<Class, List<Extension>> repeatingExtensionMap =
    new LinkedHashMap<Class, List<Extension>>();


  /** Arbitrary XML (unrecognized extensions). */
  protected XmlBlob xmlBlob = new XmlBlob();


  /** Manifest for this instance. Filled on-demand. */
  private Manifest manifest;


  /**
   * Simple constructor to create a new (empty) ExtensionPoint.
   */
  public ExtensionPoint() {};


  /**
   * Simple copy constructor that does a shallow copy of extension and
   * manifest data from an existing ExtensionPoint to the constructed
   * instance.
   */
  protected ExtensionPoint(ExtensionPoint sourcePoint) {

    // WARNING: ANY NON-STATIC FIELDS ADDED ABOVE NEED TO BE COPIED HERE.
    nonRepeatingExtensionMap = sourcePoint.nonRepeatingExtensionMap;
    repeatingExtensionMap = sourcePoint.repeatingExtensionMap;
    xmlBlob = sourcePoint.xmlBlob;
    manifest = sourcePoint.manifest;
  }


  /**
   * Declares the set of expected Extension types for an ExtensionPoint
   * within the target extension profile.   The base implementation
   * does not declare any extensions, but can be overridden by specific
   * types of ExtensionPoints that always contain a well-defined set
   * of extensions.
   *
   * @param extProfile
   *          the ExtensionProfile to initialize.
   */
  public void declareExtensions(ExtensionProfile extProfile) {
    // The default implementation does not register any extensions.
  }

  /** Retrieves a non-repeating extension or {@code null} if not present. */
  public <T extends Extension> T getExtension(Class<T> extensionClass) {
    return (T)nonRepeatingExtensionMap.get(extensionClass);
  }

  /**
   * Returns an unmodifiable collection of non-repeating extensions in this 
   * ExtensionPoint.
   * 
   * @return Collection of non-repeating extensions.
   */
  public Collection<Extension> getExtensions() {
    return Collections.unmodifiableCollection(
        nonRepeatingExtensionMap.values());
  }

  /** Retrieves a repeating extension list (an empty list if not present). */
  public <T extends Extension> List<T> getRepeatingExtension(
      Class<T> extensionClass) {

    List<T> ret = (List<T>)repeatingExtensionMap.get(extensionClass);
    if (ret == null) {
      ret = new ArrayList<T>();
      repeatingExtensionMap.put(extensionClass, (List<Extension>)ret);
    }
    return ret;
  }

  /**
   * Returns an unmodifiable collection of lists of  repeating extensions in 
   * this ExtensionPoint. The Extensions that are of the same type
   * are grouped together in lists within the collection.
   * 
   * @return Collection of lists of repeating extensions.
   */
  public Collection<List<Extension>> getRepeatingExtensions() {
    return Collections.unmodifiableCollection(
        repeatingExtensionMap.values());
  }
  
  /** Internal helper method. */
  protected boolean addExtension(Extension ext, Class extClass) {

    if (nonRepeatingExtensionMap.containsKey(extClass)) {
      return false;
    }

    nonRepeatingExtensionMap.put(extClass, ext);
    return true;
  }


  /** Adds an extension object. */
  public void addExtension(Extension ext) {
    addExtension(ext, ext.getClass());
  }


  /** Sets an extension object. If one exists of this type, it's replaced. */
  public void setExtension(Extension ext) {
    nonRepeatingExtensionMap.remove(ext.getClass());
    addExtension(ext, ext.getClass());
  }
  
  /** Internal helper method. */
  protected void addRepeatingExtension(Extension ext, Class extClass) {

    List<Extension> extList = repeatingExtensionMap.get(extClass);
    if (extList == null) {
      extList = new ArrayList<Extension>();
    }

    extList.add(ext);
    repeatingExtensionMap.put(extClass, extList);
  }


  /** Adds a repeating extension object. */
  public void addRepeatingExtension(Extension ext) {
    addRepeatingExtension(ext, ext.getClass());
  }


  /** Removes an extension object. */
  public void removeExtension(Extension ext) {
    nonRepeatingExtensionMap.remove(ext.getClass());
  }


  /** Removes an extension object based on its class. */
  public void removeExtension(Class extensionClass) {
    nonRepeatingExtensionMap.remove(extensionClass);
  }


  /** Removes a repeating extension object. */
  public void removeRepeatingExtension(Extension ext) {

    List<Extension> extList = repeatingExtensionMap.get(ext.getClass());
    if (extList == null) {
      return;
    }

    extList.remove(ext);
  }


  /**
   * Retrieves the XML blob containing arbitrary (unrecognized)
   * extensions.
   */
  public XmlBlob getXmlBlob() {
    return xmlBlob;
  }


  /** Sets the XML blob containing arbitrary (unrecognized) extensions. */
  public void setXmlBlob(XmlBlob xmlBlob) {
    this.xmlBlob = xmlBlob;
  }


  /**
   * Generates an XML blob containing all recognized and unrecognized
   * extensions. This can be used by applications that persist data in
   * a store that might be accessed by other applications--ones that
   * don't necessarily recognize the same set of extensions.
   */
  public XmlBlob generateCumulativeXmlBlob(ExtensionProfile extProfile)
      throws IOException {

    XmlBlob cumulative = new XmlBlob();
    Collection<XmlNamespace> namespaces = cumulative.getNamespaces();

    StringWriter w = new StringWriter();
    XmlWriter xw = new XmlWriter(w);

    if (xmlBlob != null) {
      cumulative.setLang(xmlBlob.getLang());
      cumulative.setBase(xmlBlob.getBase());
      namespaces.addAll(xmlBlob.getNamespaces());
      w.write(xmlBlob.getBlob());
    }

    if (manifest != null) {
      for (XmlWriter.Namespace ns: manifest.getNamespaceDecls()) {
        XmlNamespace newNs = new XmlNamespace(ns.getAlias(), ns.getUri());
        if (!namespaces.contains(newNs)) {
          namespaces.add(newNs);
        }
      }
    }

    for (Extension ext: nonRepeatingExtensionMap.values()) {
      ext.generate(xw, extProfile);
    }

    for (List<Extension> extList: repeatingExtensionMap.values()) {
      xw.startRepeatingElement();
      for (Extension ext: extList) {
        ext.generate(xw, extProfile);
      }
      xw.endRepeatingElement();
    }

    cumulative.setBlob(w.toString());
    return cumulative;
  }


  /**
   * Reverses {@link #generateCumulativeXmlBlob(ExtensionProfile)}.
   * This operation overwrites the current contents of this extension
   * point.
   */
  public void parseCumulativeXmlBlob(XmlBlob xmlBlob,
                                     ExtensionProfile extProfile,
                                     Class extendedClass)
      throws IOException, ParseException {

    this.xmlBlob = new XmlBlob();
    nonRepeatingExtensionMap.clear();
    repeatingExtensionMap.clear();

    // Prepare a fake XML document from the blob.
    StringWriter sw = new StringWriter();
    XmlWriter w = new XmlWriter(sw);
    XmlBlob.startElement(w, null, "CUMULATIVE_BLOB", xmlBlob, null, null);
    XmlBlob.endElement(w, null, "CUMULATIVE_BLOB", xmlBlob);

    // Now parse it.
    StringReader sr = new StringReader(sw.toString());
    XmlParser parser = new XmlParser();
    parser.parse(sr, new CumulativeBlobHandler(extProfile, extendedClass),
                 "", "CUMULATIVE_BLOB");
  }


  /** Parser class for cumulative XML blobs. */
  public class CumulativeBlobHandler extends ElementHandler {


    public CumulativeBlobHandler(ExtensionProfile extProfile,
                                 Class extendedClass) throws IOException {

      this.extProfile = extProfile;
      this.extendedClass = extendedClass;
      initializeArbitraryXml(extProfile, extendedClass, this);
    }


    private final ExtensionProfile extProfile;
    private final Class extendedClass;


    public ElementHandler getChildHandler(String namespace,
                                          String localName,
                                          Attributes attrs)
        throws ParseException, IOException {
      // Try ExtensionPoint. It returns {@code null} if there's no handler.
      ElementHandler extensionHandler =
        getExtensionHandler(extProfile, extendedClass,
                            namespace, localName, attrs);
      if (extensionHandler != null) {
        return extensionHandler;
      }

      return super.getChildHandler(namespace, localName, attrs);
    }
  }



  /** Retrieves the manifest for the specified class. */
  protected Manifest getManifest(
      ExtensionProfile extProfile,
      Class<? extends ExtensionPoint> extendedClass) {

    if (manifest == null) {
      manifest = extProfile.getManifest(extendedClass);
    }

    return manifest;
  }


 /**
   * Generates XML corresponding to the type implementing {@link
   * ExtensionPoint}. The reason this routine is necessary is that the
   * embedded XML blob may contain namespace declarations.
   */
  protected void generateStartElement(XmlWriter w,
                                      XmlWriter.Namespace namespace,
                                      String elementName,
                                      Collection<XmlWriter.Attribute>
                                       additionalAttrs,
                                      Collection<XmlWriter.Namespace>
                                       additionalNs)
      throws IOException {

    XmlBlob.startElement(w, namespace, elementName, xmlBlob,
                         additionalAttrs, additionalNs);
  }


  /**
   * Generates XML corresponding to extended properties. Implementations in
   * extended classes should always call the base class to allow for nested
   * extensions.
   *
   * @param   w
   *            Output writer.
   *
   * @param   extProfile
   *            Extension profile for use by nested extensions.
   *
   * @throws  IOException
   */
  protected void generateExtensions(XmlWriter w,
                                    ExtensionProfile extProfile)
      throws IOException {

    for (Extension ext: nonRepeatingExtensionMap.values()) {
      ext.generate(w, extProfile);
    }

    for (List<Extension> extList: repeatingExtensionMap.values()) {
      w.startRepeatingElement();
      for (Extension ext: extList) {
        ext.generate(w, extProfile);
      }
      w.endRepeatingElement();
    }

    if (xmlBlob != null) {
      w.innerXml(xmlBlob.getBlob());
    }
  }


  /**
   * Initializes parser handler's XML blob state.
   * Should be called by the handler's constructor in order to honor
   * {@link ExtensionProfile#declareArbitraryXmlExtension(Class)}.
   */
  protected void initializeArbitraryXml(ExtensionProfile profile,
                                        Class extPoint,
                                        ElementHandler handler)
      throws IOException {

    Manifest manifest = getManifest(profile, extPoint);
    if (manifest != null && manifest.arbitraryXml) {
      handler.initializeXmlBlob(xmlBlob,
                                /* mixedContent */ false,
                                /* fullTextIndex */ false);
    }
  }


  /**
   * XML parser callback for extended properties. Implementations in
   * extended classes should always call the base class to allow for
   * nested extensions.
   *
   * @param   extProfile
   *            Extension profile for use by nested element handlers.
   *
   * @param   extPoint
   *            Current active ExtensionPoint class within which you're
   *            looking for a handler for a nested extension element.
   *
   * @param   namespaceUri
   *            Namespace URI of the XML element.
   *
   * @param   localName
   *            Name of the XML element.
   *
   * @param   attrs
   *            Child element attributes. These attributes will be
   *            communicated to the returned {@link ElementHandler}
   *            through its {@link
   *            ElementHandler#processAttribute(String, String,
   *            String)} method. They are passed here because sometimes
   *            the value of some attribute determines the element's
   *            content type, so different element handlers may be
   *            needed.
   *
   * @return  Element handler for the custom tag or {@code null} if the tag
   *          is not recognized. Unrecognized tags are stored in the XML blob.
   *
   * @throws  ParseException
   *            XML schema error. Could be a result of having a
   *            duplicate entry, illegal contents (such as unrecognized
   *            attributes or nested elements), etc.
   */
  protected ElementHandler getExtensionHandler(ExtensionProfile extProfile,
                                               Class extPoint,
                                               String namespaceUri,
                                               String localName,
                                               Attributes attrs)
      throws ParseException, IOException {

    Manifest manifest = getManifest(extProfile, extPoint);
    if (manifest == null) {
      return null;
    }

    // Look for an explicit match, followed by a wildcarded namespace match.
    ExtensionDescription extDescription =
      manifest.supportedExtensions.get(new Pair(namespaceUri, localName));

    if (extDescription == null) {

      extDescription =
        manifest.supportedExtensions.get(new Pair(namespaceUri, "*"));

      if (extDescription == null) {

        // extension point) could just live here.
        return null;
      }
    }

    Class<? extends Extension> extClass = extDescription.getExtensionClass();
    if (extClass == null) {
      return null;
    }

    Extension extension = null;

    // If an aggregate extension type, retrieve existing instance (if any)
    if (extDescription.isAggregate()) {
      extension = getExtension(extClass);
    }

    boolean needsAdd = true;
    if (extension == null) {
      // Create an extension instance.
      try {
        extension = extClass.newInstance();
      } catch (InstantiationException e) {
        throw new ParseException("Unable to create extension", e);
      } catch (IllegalAccessException e) {
        throw new ParseException("Unable to create extension", e);
      }
    } else {
      needsAdd = false;
    }

    // Retrieve the handler.
    ElementHandler handler = extension.getHandler(extProfile, namespaceUri,
                                                  localName, attrs);

    // Store the new extension instance.
    if (needsAdd) {

      if (extDescription.isRepeatable()) {
        addRepeatingExtension(extension, extClass);
      } else {
        boolean added = addExtension(extension, extClass);
        if (!added) {
          throw new ParseException(
            "Duplicate extension element " + namespaceUri + ":" + localName);
        }
      }
    }
    return handler;
  }


  /** Checks whether all required extensions are present. */
  protected void checkRequiredExtensions(Manifest manifest)
      throws ParseException {

    for (ExtensionDescription extDescription:
         manifest.supportedExtensions.values()) {

      if (extDescription.isRequired()) {
        Class extClass = extDescription.getExtensionClass();
        boolean found = (extDescription.isRepeatable()
                         ? repeatingExtensionMap.containsKey(extClass)
                         : nonRepeatingExtensionMap.containsKey(extClass));
        if (!found) {
          throw new ParseException(
            "Required extension element " +
            extDescription.getNamespace().getUri() + ":" +
            extDescription.getLocalName() + " not found.");
        }
      }
    }
  }

  /**
   * ElementHandler implementation for handlers associated with an
   * ExtensionPoint class. Provides common initialization and
   * code for looking up handlers defined within the ExtensionProfile
   * associated with the ExtensionPoint.
   */
  public class ExtensionHandler extends XmlParser.ElementHandler {

    protected ExtensionProfile extProfile;
    protected Class extendedClass;
    protected boolean hasExtensions;
    protected Manifest manifest;


    /**
     * Constructs a new Handler instance that process extensions on
     * a class associated with the ExtensionPoint
     *
     * @param   profile
     *            The extension profile associatd with the Handler.
     *
     * @param   extendedClass
     *            The extended class within the profile for this handler
     */
    public ExtensionHandler(ExtensionProfile profile, Class extendedClass)
        throws java.io.IOException {

      this.extProfile = profile;
      this.extendedClass = extendedClass;

      // If the extended class is defined within the profile, then enable
      // extension handling and arbitrary XML handling.
      this.manifest = profile.getManifest(extendedClass);
      if (this.manifest != null) {

        hasExtensions = true;

        if (manifest.arbitraryXml) {
          initializeArbitraryXml(extProfile, extendedClass, this);
        }
      }
    }


    public XmlParser.ElementHandler getChildHandler(String namespace,
                                                    String localName,
                                                    Attributes attrs)
        throws ParseException, IOException {

      // If extensions have been defined for the extended class, then
      // look for a handler.
      if (hasExtensions) {

        XmlParser.ElementHandler extensionHandler =
          getExtensionHandler(extProfile, extendedClass,
                              namespace, localName, attrs);
        if (extensionHandler != null) {
          return extensionHandler;
        }
      }

      return super.getChildHandler(namespace, localName, attrs);
    }


    public void processEndElement() throws ParseException {

      super.processEndElement();

      if (this.manifest != null) {
        checkRequiredExtensions(this.manifest);
      }

      //
      // Iterate through all contained Extension instances and enable them
      // to validate against the full ExtensionPoint state (including
      // sibling Extension instances).
      //
      for (Extension extension :  nonRepeatingExtensionMap.values()) {
        if (extension instanceof ValidatingExtension) {
          ((ValidatingExtension) extension).validate(ExtensionPoint.this);
        }
      }

      for (List<Extension> extList : repeatingExtensionMap.values()) {
        for (Extension extension : extList) {
          if (extension instanceof ValidatingExtension) {
            ((ValidatingExtension) extension).validate(ExtensionPoint.this);
          }
        }
      }
    }
  }
}
