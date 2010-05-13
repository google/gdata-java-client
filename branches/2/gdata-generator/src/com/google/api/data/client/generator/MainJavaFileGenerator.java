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

/**
 * @author Yaniv Inbar
 */
public final class MainJavaFileGenerator extends AbstractJavaFileGenerator {

  private final Version version;

  MainJavaFileGenerator(Version version) {
    super(version.getPackageName(), version.client.className);
    this.version = version;
  }

  public void generate(PrintWriter out) {
    Client client = version.client;
    generateHeader(out);
    out.println("/**");
    out.println(" * Constants for the " + client.name + ".");
    out.println(" *");
    out.println(" * @since 2.2");
    out.println(" */");
    out.println("public final class " + className + " {");
    if (client.authTokenType != null) {
      out.println();
      out.println(indent(2)
          + "/** The authentication token type or service name. */");
      out.println(indent(2) + "public static final String AUTH_TOKEN_TYPE = \""
          + client.authTokenType + "\";");
    }
    out.println();
    out.println(indent(2) + "/** Version name. */");
    out.println(indent(2) + "public static final String VERSION = \""
        + version.id + "\";");
    out.println();
    out.println(indent(2) + "private " + className + "() {");
    out.println(indent(2) + "}");
    out.println("}");
    out.close();
  }
}
