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

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.util.common.xml.XmlWriter.Attribute;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.util.Namespaces;
import com.google.gdata.util.ParseException;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * The ExtensionDescription class describes the attributes of an XML extension
 * type.  This description can be declared within an {@link ExtensionProfile}
 * to indicate that the extension is expected within a particular
 * {@link ExtensionPoint}.
 *
 * 
 * 
 *
 * @see ExtensionProfile#declare(Class, ExtensionDescription)
 */
public class ExtensionDescription extends ExtensionPoint
    implements Comparable<ExtensionDescription> {

  /**
   * The namespace of the XML extension type.
   */
  private XmlNamespace namespace;

  /**
   * Local name of the XML extension type. A value of '*'
   * indicates that all elements in the namespace will be handled
   * by the Extension class.
   */
  private String localName;

  /**
   * The Extension class used to parse and generate the extension type
   */
  private Class<? extends Extension> extensionClass;

  /**
   * Specifies whether the extension is required within its parent extension
   * point.
   */
  private boolean required = false;

  /**
   * Specifies whether the extension type can be repeated within its parent
   * extension point.
   */
  private boolean repeatable = false;

  /**
   * Specifies whether the extension type aggregates the contents of multiple
   * elements within its parent.
   */
  private boolean aggregate = false;

  /**
   * Specifies whether the extension type allows arbitrary xml children.
   */
  private boolean arbitraryXml = false;

  /**
   * If the extension type allows arbitrary xml, specifies whether the
   * extension allows mixed content. If the extension type does not permit
   * arbitrary xml, then mixed content is meaningless.
   */
  private boolean mixedContent = false;

  /**
   * The Default interface defines a simple annotation model for describing
   * the default {@link ExtensionDescription} of an {@link Extension} class.  If
   * this annotation is place on an @{link Extension} class, the
   * {@link ExtensionDescription#getDefaultDescription(Class)} method can be
   * used to retrieve default description for the class.
   *
   * @see ExtensionDescription#getDefaultDescription(Class)
   */
  @Retention(RetentionPolicy.RUNTIME)
  @Inherited
  public @interface Default {

    /**
     * The default namespace alias associated with this extension.
     */
    public String nsAlias();

    /**
     * The default namespace uri associated with this extension.
     */
    public String nsUri();

    /**
     * The default XML element local name associated with this extension.
     */
    public String localName();

    /**
     * {@code true} if the extension is required by default, {@code false}
     * otherwise.
     */
    public boolean isRequired() default false;

    /**
     * {@code true} if the extension is repeatable by default, {@code false}
     * otherwise.
     */
    public boolean isRepeatable() default false;

    /**
     * {@code true} if the extension is aggregate by default, {@code false}
     * otherwise.
     */
    public boolean isAggregate() default false;

    /**
     * {@code true} if the extension allows arbitrary XML, {@code false}
     * otherwise.
     */
    public boolean allowsArbitraryXml() default false;

    /**
     * {@code true} if the extension allows mixed content, {@code false}
     * otherwise.
     */
    public boolean allowsMixedContent() default false;
  }

  /**
   * Returns the default {@link ExtensionDescription} for the specified
   * Extension class.
   *
   * @param extensionClass the target extension class.
   * @return default description for the target extension class.
   *
   * @throws IllegalArgumentException if a default description could not be
   *         fourn for the extension class.
   */
  public static ExtensionDescription getDefaultDescription(
      Class<? extends Extension> extensionClass) {

    Default defAnnot = extensionClass.getAnnotation(Default.class);
    if (defAnnot == null) {
      throw new IllegalArgumentException("No default description found for "
          + extensionClass);
    }
    return new ExtensionDescription(
        extensionClass,
        new XmlNamespace(defAnnot.nsAlias(), defAnnot.nsUri()),
        defAnnot.localName(),
        defAnnot.isRequired(),
        defAnnot.isRepeatable(),
        defAnnot.isAggregate(),
        defAnnot.allowsArbitraryXml(),
        defAnnot.allowsMixedContent());
  }

  /**
   * Constructs an uninitialized ExtensionDescription.
   */
  public ExtensionDescription() {}

  /**
   * Constructs a new ExtensionDescription populated with the parameter
   * values.
   */
  public ExtensionDescription(Class<? extends Extension> extensionClass,
                              XmlNamespace namespace,
                              String localName,
                              boolean required,
                              boolean repeatable,
                              boolean aggregate) {
    this(extensionClass, namespace, localName, required, repeatable, aggregate, false, false);
  }

  /**
   * Constructs a new ExtensionDescription populated with the parameter
   * values.
   */
  public ExtensionDescription(Class<? extends Extension> extensionClass,
                              XmlNamespace namespace,
                              String localName,
                              boolean required,
                              boolean repeatable,
                              boolean aggregate,
                              boolean arbitraryXml,
                              boolean mixedContent) {
    this.namespace = namespace;
    this.localName = localName;
    this.extensionClass = extensionClass;
    this.required = required;
    this.repeatable = repeatable;
    this.aggregate = aggregate;
    this.arbitraryXml = arbitraryXml;
    this.mixedContent = mixedContent;
  }

  /**
   * Constructs a new ExtensionDescription for an optional, non-repeating
   * simple element.
   */
  public ExtensionDescription(Class<? extends Extension> extensionClass,
                              XmlNamespace namespace,
                              String localName) {
    this(extensionClass, namespace, localName, false, false, false);
  }

  public void setNamespace(XmlNamespace namespace) {
    this.namespace = namespace;
  }

  final public XmlNamespace getNamespace() { return namespace; }

  public void setLocalName(String localName) {
    this.localName = localName;
  }

  final public String getLocalName() { return localName; }

  public void setExtensionClass(Class<? extends Extension> extensionClass) {
    this.extensionClass = extensionClass;
  }

  final public Class<? extends Extension> getExtensionClass() {
    return extensionClass;
  }

  public void setRequired(boolean required) {
    this.required = required;
  }

  final public boolean isRequired() { return required; }

  public void setRepeatable(boolean repeatable) {
    this.repeatable = repeatable;
  }

  final public boolean isRepeatable() { return repeatable; }

  public void setAggregate(boolean aggregate) {
    this.aggregate = aggregate;
  }

  final public boolean isAggregate() { return aggregate; }

  public void setArbitraryXml(boolean arbitraryXml) {
    this.arbitraryXml = arbitraryXml;
  }

  final public boolean allowsArbitraryXml() { return arbitraryXml; }

  public void setMixedContent(boolean mixedContent) {
    this.mixedContent = mixedContent;
  }

  final public boolean allowsMixedContent() { return mixedContent; }

  /**
   * Defines a natural ordering for ExtensionDescription based upon
   * the qualified name of the mapped XML element.  Elements with no
   * namespace are considered to precede all others.
   */
  public int compareTo(ExtensionDescription desc) {

    String ns1 = namespace.getUri();
    if (ns1 == null) {
      ns1 = "";
    }
    String ns2 = desc.namespace.getUri();
    if (ns2 == null) {
      ns2 = "";
    }

    int nscomp = ns1.compareTo(ns2);
    if (nscomp != 0) {
      return nscomp;
    }

    return localName.compareTo(desc.localName);
  }

  /** Namespace of the corresponding XML element. */

  /**
   * Reads the ExtensionDescription XML format
   */
  public class Handler extends ExtensionPoint.ExtensionHandler {

    public Handler(ExtensionProfile configProfile, ClassLoader configLoader,
                   List<XmlNamespace> namespaces, Attributes attrs)
        throws ParseException {
      super(configProfile, ExtensionDescription.class);

      String nsValue = attrs.getValue("", "namespace");
      if (nsValue == null) {
        throw new ParseException(
            CoreErrorDomain.ERR.missingNamespace);
      }

      // Find the namespace in the list of declared NamespaceDescriptions.
      // The attribute value can match either the alias or the uri
      for (XmlNamespace declaredNs : namespaces) {
        if (declaredNs.getAlias().equals(nsValue) ||
            declaredNs.getUri().equals(nsValue)) {
          namespace = declaredNs;
          break;
        }
      }
      if (namespace == null) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.missingNamespaceDescription);
        pe.setInternalReason("No matching NamespaceDescription for " +
            nsValue);
        throw pe;
      }

      localName = attrs.getValue("", "localName");
      if (localName == null) {
        throw new ParseException(
            CoreErrorDomain.ERR.missingLocalName);
      }

      String extensionClassName = attrs.getValue("", "extensionClass");
      if (extensionClassName == null) {
        throw new ParseException(
            CoreErrorDomain.ERR.missingExtensionClass);
      }
      try {
        Class<?> extClass = configLoader.loadClass(extensionClassName);
        if (!Extension.class.isAssignableFrom(extClass)) {
          throw new ParseException(
              CoreErrorDomain.ERR.mustImplementExtension);
        }
        extensionClass = (Class<? extends Extension>) extClass;
      } catch (ClassNotFoundException e) {
        ParseException pe = new ParseException(
            CoreErrorDomain.ERR.cantLoadExtensionClass, e);
        pe.setInternalReason("Unable to load extensionClass: " +
            extensionClassName);
        throw pe;
      }

      Boolean bool = getBooleanAttribute(attrs, "required");
      required = (bool != null) && bool.booleanValue();

      bool = getBooleanAttribute(attrs, "repeatable");
      repeatable = (bool != null) && bool.booleanValue();

      bool = getBooleanAttribute(attrs, "aggregate");
      aggregate = (bool != null) && bool.booleanValue();

      bool = getBooleanAttribute(attrs, "arbitraryXml");
      arbitraryXml = (bool != null) && bool.booleanValue();

      bool = getBooleanAttribute(attrs, "mixedContent");
      mixedContent = (bool != null) && bool.booleanValue();
    }
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

    List<Attribute> attrs = new ArrayList<Attribute>();
    attrs.add(new Attribute("namespace", namespace.getUri()));
    attrs.add(new Attribute("localName", localName));
    attrs.add(new Attribute("extensionClass", extensionClass.getName()));
    attrs.add(new Attribute("required", required));
    attrs.add(new Attribute("repeatable", repeatable));
    attrs.add(new Attribute("aggregate", aggregate));
    attrs.add(new Attribute("arbitraryXml", arbitraryXml));
    attrs.add(new Attribute("mixedContent", mixedContent));
    generateStartElement(w, Namespaces.gdataConfigNs, "extensionDescription",
                         attrs, null);

    generateExtensions(w, extProfile);

    w.endElement(Namespaces.gdataConfigNs, "extensionDescription");
  }
}
