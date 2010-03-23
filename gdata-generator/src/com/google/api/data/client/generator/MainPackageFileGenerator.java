// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import java.io.PrintWriter;

public final class MainPackageFileGenerator extends AbstractHtmlFileGenerator {

  private final Version version;

  MainPackageFileGenerator(Version version) {
    this.version = version;
  }

  public void generate(PrintWriter out) {
    out.println("<body>");
    out.println("Small optional Java library for " + version.client.name + " version " + version.id
        + ".");
    out.println("</body>");
    out.close();
  }

  public String getOutputFilePath() {
    return "src/" + version.getPathRelativeToSrc() + "/package.html";
  }
}
