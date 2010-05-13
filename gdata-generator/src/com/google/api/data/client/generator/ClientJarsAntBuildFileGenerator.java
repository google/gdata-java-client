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

import com.google.api.data.client.generator.linewrap.LineWrapper;
import com.google.api.data.client.generator.linewrap.XmlLineWrapper;

import java.io.PrintWriter;

/**
 * @author Yaniv Inbar
 */
final class ClientJarsAntBuildFileGenerator implements FileGenerator {

  /** Names of packages needed for Android. */ 
  private static final String[] PACKAGE_NAMES = {
    "com.google.api.client.android.xml",
    "com.google.api.client.apache",
    "com.google.api.client.auth",
    "com.google.api.client.auth.oauth",
    "com.google.api.client.escape",
    "com.google.api.client.googleapis",
    "com.google.api.client.googleapis.auth",
    "com.google.api.client.googleapis.auth.clientlogin",
    "com.google.api.client.googleapis.auth.oauth",
    "com.google.api.client.googleapis.discovery",
    "com.google.api.client.googleapis.json",
    "com.google.api.client.googleapis.xml.atom",
    "com.google.api.client.http",
    "com.google.api.client.json",
    "com.google.api.client.util",
    "com.google.api.client.xml",
    "com.google.api.client.xml.atom",
  };

  ClientJarsAntBuildFileGenerator() {
  }

  public void generate(PrintWriter out) {
    out.println("<?xml version='1.0'?>");
    out.println();
    out.println("<!-- This is a generated file.  Do not modify by hand. -->");
    out.println("<project basedir='.' default='clientjars' name='clientjars'>");
    out.println();
    out.println("  <import file='build-common.xml'/>");
    out.println();
    out.print("  <property name='client.packagenames' value='");
    boolean first = true;
    for (String packageName : PACKAGE_NAMES) {
      if (first) {
        first = false;
      } else {
        out.print(",");
      }
      out.print(packageName);
    }
    out.println("' />");
    for (String packageName : PACKAGE_NAMES) {
      out.println();
      String jarName =
          packageName.substring("com.google.api.".length()).replace('.', '-');
      out.println("  <target name='" + jarName + "-jar'>");
      out.println("    <jar destfile='${android.dir}/gdata-${version}-"
          + jarName + ".jar' basedir='${classes.dir}' includes='"
          + packageName.replace('.', '/') + "/*.class' />");
      out.println("  </target>");
    }
    out.println();
    out
        .print("  <target name='clientjars' description='Compile all Client package jars' depends='");
    first = true;
    for (String packageName : PACKAGE_NAMES) {
      if (first) {
        first = false;
      } else {
        out.print(",");
      }
      String jarName =
          packageName.substring("com.google.api.".length()).replace('.', '-');
      out.print(jarName + "-jar");
    }
    out.println("' />");
    out.println("</project>");
    out.close();
  }

  public LineWrapper getLineWrapper() {
    return XmlLineWrapper.get();
  }

  public String getOutputFilePath() {
    return "build-clientjars.xml";
  }

  public boolean isGenerated() {
    return true;
  }
}
