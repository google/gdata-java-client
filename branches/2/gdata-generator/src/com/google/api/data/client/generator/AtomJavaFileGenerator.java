/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.client.generator;

import com.google.api.client.xml.XmlNamespaceDictionary;
import com.google.api.data.client.generator.model.Client;
import com.google.api.data.client.generator.model.Version;

import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Yaniv Inbar
 */
final class AtomJavaFileGenerator extends AbstractJavaFileGenerator {

  private final Version version;

  AtomJavaFileGenerator(Version version) {
    super(version.getPackageName() + ".atom", version.client.className + "Atom");
    this.version = version;
  }

  @Override
  public void generate(PrintWriter out) {
    Client client = version.client;
    String className = client.className + "Atom";
    generateHeader(out);
    out.println("import " + XmlNamespaceDictionary.class.getName() + ";");
    out.println();
    out.println("import " + Map.class.getName() + ";");
    out.println();
    DocBuilder docBuilder = new DocBuilder();
    docBuilder.comment =
        "Utilities for the Atom XML format of the " + version.client.name + ".";
    docBuilder.sinceMinor = version.sinceMinor;
    docBuilder.generate(out);
    out.println("public final class " + className + " {");
    out.println();
    DocBuilder.generateComment(out, 2, "XML namespace dictionary.");
    out.println(indent(2) + "public static final "
        + useClass(XmlNamespaceDictionary.class)
        + " NAMESPACE_DICTIONARY = new "
        + useClass(XmlNamespaceDictionary.class) + "();");
    out.println(indent(2) + "static {");
    out.println(indent(4) + useClass(Map.class) + "<" + useClass(String.class)
        + ", " + useClass(String.class)
        + "> map = NAMESPACE_DICTIONARY.namespaceAliasToUriMap;");
    for (Map.Entry<String, String> namespace : version.atom.namespaces
        .entrySet()) {
      out.println(indent(4) + "map.put(\"" + namespace.getKey() + "\", \""
          + namespace.getValue() + "\");");
    }
    out.println(indent(2) + "}");
    out.println();
    out.println(indent(2) + "private " + className + "() {");
    out.println(indent(2) + "}");
    out.println("}");
  }

  @Override
  public boolean isGenerated() {
    return version.atom != null;
  }
}
