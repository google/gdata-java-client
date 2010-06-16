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

import com.google.api.data.client.generator.model.Client;
import com.google.api.data.client.generator.model.Version;

import java.io.PrintWriter;
import java.util.SortedSet;

/**
 * @author Yaniv Inbar
 */
final class DataJarsAntBuildFileGenerator extends AbstractXmlFileGenerator {

  private final SortedSet<Client> clients;

  DataJarsAntBuildFileGenerator(SortedSet<Client> clients) {
    this.clients = clients;
  }

  @Override
  public void generate(PrintWriter out) {
    int size = clients.size();
    out.println("<?xml version='1.0'?>");
    out.println();
    out.println("<!-- This is a generated file.  Do not modify by hand. -->");
    out.println("<project basedir='.' default='datajars' name='datajars'>");
    out.println();
    out.println("  <import file='build-common.xml'/>");
    out.println();
    out.print("  <property name='data.packagenames' value='");
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
          out.print("," + version.getPackageName() + "."
              + client.getXmlFormatId());
        }
      }
    }
    out.println("' />");
    for (Client client : clients) {
      for (Version version : client.versions.values()) {
        out.println();
        out.println("  <target name='" + version.getJarName() + "-jar'>");
        out.println("    <jar destfile='${android.dir}/gdata-${version}-"
            + version.getJarName()
            + ".jar' basedir='${classes.dir}' includes='"
            + version.getPathRelativeToSrc() + "/*.class' />");
        out.println("  </target>");
        if (version.atom != null) {
          out.println();
          out.println("  <target name='" + version.getJarName() + "-"
              + client.getXmlFormatId() + "-jar'>");
          out.println("    <jar destfile='${android.dir}/gdata-${version}-"
              + version.getJarName() + "-" + client.getXmlFormatId()
              + ".jar' basedir='${classes.dir}' includes='"
              + version.getPathRelativeToSrc() + "/" + client.getXmlFormatId()
              + "/*.class' />");
          out.println("  </target>");
        }
      }
    }
    out.println();
    out
        .print("  <target name='datajars' description='Compile all Data package jars' depends='");
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
          out.print("," + version.getJarName() + "-" + client.getXmlFormatId()
              + "-jar");
        }
      }
    }
    out.println("' />");
    out.println("</project>");
    out.close();
  }

  @Override
  public String getOutputFilePath() {
    return "build-datajars.xml";
  }
}
