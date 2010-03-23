// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import java.io.PrintWriter;
import java.util.Map;

final class AtomJavaFileGenerator extends AbstractJavaFileGenerator {

  private final Version version;

  AtomJavaFileGenerator(Version version) {
    this.version = version;
  }

  public void generate(PrintWriter out) {
    Client client = version.client;
    String className = client.className + "Atom";
    out.println("// Copyright 2010 Google Inc. All Rights Reserved.");
    out.println();
    out.println("package " + version.getPackageName() + ".atom;");
    out.println();
    out.println("import com.google.api.data.client.v2.atom.NamespaceDictionary;");
    out.println();
    out.println("public final class " + className + " {");
    out.println();
    out.println("  public static final NamespaceDictionary NAMESPACE_DICTIONARY;");
    out.println("  static {");
    out.println("    NamespaceDictionary.Builder builder = new NamespaceDictionary.Builder();");
    for (Map.Entry<String, String> namespace : version.atom.namespaces.entrySet()) {
      out.println("    builder.addNamespace(\"" + namespace.getKey() + "\", \""
          + namespace.getValue() + "\");");
    }
    out.println("    NAMESPACE_DICTIONARY = builder.build();");
    out.println("  }");
    out.println();
    out.println("  private " + className + "() {");
    out.println("  }");
    out.println("}");
  }

  public String getOutputFilePath() {
    return "src/" + version.getPathRelativeToSrc() + "/atom/" + version.client.className
        + "Atom.java";
  }

  @Override
  public boolean isGenerated() {
    return version.atom != null;
  }
}
