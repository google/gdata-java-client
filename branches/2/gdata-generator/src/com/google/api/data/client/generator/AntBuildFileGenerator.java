// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import com.google.api.data.client.generator.linewrap.LineWrapper;
import com.google.api.data.client.generator.linewrap.XmlLineWrapper;

import java.io.PrintWriter;
import java.util.SortedSet;

final class AntBuildFileGenerator implements FileGenerator {

  private final SortedSet<Client> clients;

  AntBuildFileGenerator(SortedSet<Client> clients) {
    this.clients = clients;
  }

  public void generate(PrintWriter out) {
    int size = clients.size();
    out.println("<?xml version='1.0'?>");
    out.println();
    out.println("<project basedir='.' default='apijars' name='apis'>");
    out.println();
    out.println("  <import file='build-common.xml'/>");
    out.println();
    out.print("  <property name='api.packagenames' value='");
    boolean first = true;
    for (Client client : clients) {
      for (Version version : client.versions) {
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
    out.println("    <property name='api.dist' value='${dist.dir}/apis' />");
    out.println("    <mkdir dir='${api.dist}' />");
    out.println("  </target>");
    for (Client client : clients) {
      for (Version version : client.versions) {
        out.println();
        out.println("  <target name='" + version.getJarName()
            + "-jar' depends='api-init'>");
        out.println("    <jar destfile='${api.dist}/" + version.getJarName()
            + ".jar' basedir='${classes.dir}' includes='" + version.getPathRelativeToSrc()
            + "/*.class' />");
        out.println("  </target>");
        if (version.atom != null) {
          out.println();
          out.println("  <target name='" + version.getJarName()
              + "-atom-jar' depends='api-init'>");
          out.println("    <jar destfile='${api.dist}/" + version.getJarName()
              + "-atom.jar' basedir='${classes.dir}' includes='" + version.getPathRelativeToSrc()
              + "/atom/*.class' />");
          out.println("  </target>");
        }
      }
    }
    out.println();
    out.print("   <target name='apijars' description='Compile all API jars' depends='");
    first = true;
    for (Client client : clients) {
      if (first) {
        first = false;
      } else {
        out.print(",");
      }
      for (Version version : client.versions) {
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
    return "build-apis.xml";
  }

  public boolean isGenerated() {
    return true;
  }
}
