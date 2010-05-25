package com.google.api.client.xml;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

/**
 * Generic XML data that stores all unknown key name/value pairs.
 * <p>
 * Each data key name maps into the name of the XPath expression value for the
 * XML element, attribute, or text content (using {@code "text()"}). Subclasses
 * can declare fields for known XML content using the {@link Key} annotation.
 * {@code null} unknown data key names are not allowed, but {@code null} data
 * values are allowed.
 * 
 * @since 2.2
 * @author Yaniv Inbar
 */
public class GenericXml extends GenericData implements Cloneable {

  /**
   * Optional XML element local name prefixed by its namespace alias -- for
   * example {@code "atom:entry"} -- or {@code null} if not set.
   */
  public String name;

  /** Optional namespace dictionary or {@code null} if not set. */
  public XmlNamespaceDictionary namespaceDictionary;

  @Override
  public GenericXml clone() {
    return (GenericXml) super.clone();
  }

  @Override
  public String toString() {
    XmlNamespaceDictionary namespaceDictionary = this.namespaceDictionary;
    if (namespaceDictionary == null) {
      namespaceDictionary = new XmlNamespaceDictionary();
    }
    return namespaceDictionary.toStringOf(this.name, this);
  }
}
