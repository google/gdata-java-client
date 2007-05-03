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

import com.google.gdata.util.common.xml.XmlWriter;


/** Collection of various namespace URIs. */
public final class Namespaces {


  /**
   * Standard XML namespace used for {@code xml:lang}, {@code xml:base},
   * ...
   */
  static final public String xml =
    "http://www.w3.org/XML/1998/namespace";


  /** Atom namespace. */
  static final public String atom =
    "http://www.w3.org/2005/Atom";

  /** Atom XML writer namespace, for assigning {@code atom:} prefix. */
  static final public XmlWriter.Namespace atomNs =
    new XmlWriter.Namespace("atom", atom);


  /** Atom Publishing Protocol namespace. */
  static final public String atomPub =
    "http://purl.org/atom/app#";

  /** Atom Publishing Protocol XML writer namespace. */
  static final public XmlWriter.Namespace atomPubNs =
    new XmlWriter.Namespace("app", atomPub);


  /** RSS XML writer namespace. */
  static final public XmlWriter.Namespace rssNs = null;


  /** Amazon OpenSearch/RSS namespace. */
  static final public String openSearch =
    "http://a9.com/-/spec/opensearchrss/1.0/";

  /** Amazon OpenSearch/RSS XML writer namespace. */
  static final public XmlWriter.Namespace openSearchNs =
    new XmlWriter.Namespace("openSearch", openSearch);


  /** Amazon OpenSearch/RSS Description Document namespace. */
  static final public String openSearchDesc =
    "http://a9.com/-/spec/opensearchdescription/1.0/";

  /** Amazon OpenSearch/RSS Description Document XML writer namespace. */
  static final public XmlWriter.Namespace openSearchDescNs =
    new XmlWriter.Namespace("openSearchDesc", openSearchDesc);


  /** XHTML namespace. */
  static final public String xhtml = "http://www.w3.org/1999/xhtml";

  /** XHTML XML writer namespace. */
  static final public XmlWriter.Namespace xhtmlNs =
    new XmlWriter.Namespace("xh", xhtml);


  /** GData configuration namespace. */
  static final public String gdataConfig =
    "http://schemas.google.com/gdata/config/2005";

  /** GData XML writer namespace. */
  static final public XmlWriter.Namespace gdataConfigNs =
    new XmlWriter.Namespace("gc", gdataConfig);


  /** Google data (GD) namespace */
  static final public String g = "http://schemas.google.com/g/2005";
  static final public String gPrefix = g + "#";
  public static final String gAlias = "gd";

  /** Google data XML writer namespace. */
  static final public XmlWriter.Namespace gNs =
    new XmlWriter.Namespace(gAlias, g);


  /** Google data runtime namespace. */
  static final public String gr = gPrefix + "runtime";

  /** Google data runtime XML writer namespace. */
  static final public XmlWriter.Namespace grNs =
    new XmlWriter.Namespace("gr", gr);


  /** Google data kind scheme. */
  static final public String gKind = gPrefix + "kind";

  /** Google data batch feeds namespace. */
  static final public String batch = "http://schemas.google.com/gdata/batch";
  static final public String batchAlias = "batch";
  static final public XmlWriter.Namespace batchNs =
      new XmlWriter.Namespace(batchAlias, batch);
}
