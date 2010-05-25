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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yaniv Inbar
 */
final class MainJavaFileGenerator extends AbstractJavaFileGenerator {

  private static final Set<String> GENERATE_VERSION_NAME =
      new HashSet<String>(Arrays.asList(new String[] {"analytics", "blogger",
          "books", "buzz", "calendar", "codesearch", "contacts", "docs",
          "finance", "gbase", "health", "maps", "migration", "moderator",
          "picasa", "sidewiki", "sites", "spreadsheet", "webmastertools",
          "youtube"}));

  private final Version version;

  MainJavaFileGenerator(Version version) {
    super(version.getPackageName(), version.client.className);
    this.version = version;
  }

  @Override
  public boolean isGenerated() {
    return isGenerated(version);
  }

  static boolean isGenerated(Version version) {
    Client client = version.client;
    return GENERATE_VERSION_NAME.contains(client.id) || version.rootUrl != null
        || client.authTokenType != null;
  }

  @Override
  public void generate(PrintWriter out) {
    Client client = version.client;
    generateHeader(out);
    out.println("/**");
    out.println(" * Constants for the " + client.name + ".");
    out.println(" *");
    out.println(" * @since 2.2");
    out.println(" */");
    out.println("public final class " + className + " {");
    out.println();
    if (GENERATE_VERSION_NAME.contains(client.id)) {
      out.println(indent(2) + "/** Version name. */");
      out.println(indent(2) + "public static final String VERSION = \""
          + version.id + "\";");
      out.println();
    }
    if (version.rootUrl != null) {
      out.println(indent(2) + "/** Root URL. */");
      out.println(indent(2) + "public static final String ROOT_URL = \""
          + version.rootUrl + "\";");
      out.println();
    }
    if (client.authTokenType != null) {
      out.println(indent(2)
          + "/** The authentication token type used for Client Login. */");
      out.println(indent(2) + "public static final String AUTH_TOKEN_TYPE = \""
          + client.authTokenType + "\";");
      out.println();
    }
    out.println(indent(2) + "private " + className + "() {");
    out.println(indent(2) + "}");
    out.println("}");
    out.close();
  }
}
