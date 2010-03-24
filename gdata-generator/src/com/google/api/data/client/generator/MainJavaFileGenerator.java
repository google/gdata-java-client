// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import java.io.PrintWriter;

public final class MainJavaFileGenerator extends AbstractJavaFileGenerator {

  private final Version version;

  MainJavaFileGenerator(Version version) {
    this.version = version;
  }

  public void generate(PrintWriter out) {
    Client client = version.client;
    out.println("// Copyright 2010 Google Inc. All Rights Reserved.");
    out.println();
    out.println("package " + version.getPackageName() + ";");
    out.println();
    out.println("/** Constants for the " + client.name + ". */");
    out.println("public final class " + client.className + " {");
    if (client.authTokenType != null) {
      out.println();
      out.println("/** The authentication token type or service name. */");
      out.println("  public static final String AUTH_TOKEN_TYPE = \"" + client.authTokenType
          + "\";");
    }
    out.println();
    out.println("/** Version name. */");
    out.println("  public static final String VERSION = \"" + version.id + "\";");
    out.println();
    out.println("  private " + client.className + "() {");
    out.println("  }");
    out.println("}");
    out.close();
  }

  public String getOutputFilePath() {
    return "src/" + version.getPathRelativeToSrc() + "/" + version.client.className + ".java";
  }
}
