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
import java.util.SortedSet;

final class AntBuildFileGenerator implements FileGenerator {

  private static final String LIBRARY_VERSION = "2.2.0-alpha";

  private final SortedSet<Client> clients;

  AntBuildFileGenerator(SortedSet<Client> clients) {
    this.clients = clients;
  }

  public void generate(PrintWriter out) {
    int size = clients.size();
    out.println("<?xml version='1.0'?>");
    out.println();
    out.println("<project basedir='.' default='apijars' name='google'>");
    out.println();
    out.println("  <import file='build-common.xml'/>");
    out.println();
    out.print("  <property name='api.packagenames' value='");
    boolean first = true;
    for (Client client : clients) {
      for (Version version : client.versions.values()) {
        if (first) {
          first = false;
        } else {
          out.print(",");
        }
        out.print(version.getPackageName());
        if (version.atom != null) {
          out.print("," + version.getPackageName() + ".atom");
        }
      }
    }
    out.println("' />");
    out.println();
    out.println("  <target name='api-init' depends='compile,dist-init'>");
    out
        .println("    <property name='api.dist' value='${android.dir}/googleapis' />");
    out.println("    <mkdir dir='${api.dist}' />");
    out.println("  </target>");
    for (Client client : clients) {
      for (Version version : client.versions.values()) {
        out.println();
        out.println("  <target name='" + version.getJarName()
            + "-jar' depends='api-init'>");
        out.println("    <jar destfile='${api.dist}/" + version.getJarName()
            + "-android-" + LIBRARY_VERSION
            + ".jar' basedir='${classes.dir}' includes='"
            + version.getPathRelativeToSrc() + "/*.class' />");
        out.println("  </target>");
        if (version.atom != null) {
          out.println();
          out.println("  <target name='" + version.getJarName()
              + "-atom-jar' depends='api-init'>");
          out.println("    <jar destfile='${api.dist}/" + version.getJarName()
              + "-atom-android-" + LIBRARY_VERSION
              + ".jar' basedir='${classes.dir}' includes='"
              + version.getPathRelativeToSrc() + "/atom/*.class' />");
          out.println("  </target>");
        }
      }
    }
    out.println();
    out
        .print("  <target name='apijars' description='Compile all API jars' depends='");
    first = true;
    for (Client client : clients) {
      if (first) {
        first = false;
      } else {
        out.print(",");
      }
      boolean firstVersion = true;
      for (Version version : client.versions.values()) {
        if (firstVersion) {
          firstVersion = false;
        } else {
          out.print(",");
        }
        out.print(version.getJarName() + "-jar");
        if (version.atom != null) {
          out.print("," + version.getJarName() + "-atom-jar");
        }
      }
    }
    out.println("' />");
    out.println("</project>");
    out.close();
  }

  public LineWrapper getLineWrapper() {
    return XmlLineWrapper.get();
  }

  public String getOutputFilePath() {
    return "build-google.xml";
  }

  public boolean isGenerated() {
    return true;
  }
}
