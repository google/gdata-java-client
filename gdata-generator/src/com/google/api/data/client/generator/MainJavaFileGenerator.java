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
import com.google.api.data.client.generator.model.OAuthInfo;
import com.google.api.data.client.generator.model.Version;

import java.io.PrintWriter;
import java.util.Map;

/**
 * @author Yaniv Inbar
 */
final class MainJavaFileGenerator extends AbstractJavaFileGenerator {

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
    return client.isOldGDataStyle || version.rootUrl != null
        || client.authTokenType != null || client.oauth != null;
  }

  @Override
  public void generate(PrintWriter out) {
    Client client = version.client;
    generateHeader(out);
    DocBuilder classDocBuilder = new DocBuilder();
    classDocBuilder.comment = "Constants for the " + client.name + ".";
    classDocBuilder.sinceMinor = version.sinceMinor;
    classDocBuilder.generate(out);
    out.println("public final class " + className + " {");
    out.println();
    if ("moderator".equals(client.id)) {
      DocBuilder docBuilder = new DocBuilder();
      docBuilder.container = classDocBuilder;
      docBuilder.indentNumSpaces = 2;
      docBuilder.isDeprecated = true;
      docBuilder.removedMinor = 4;
      docBuilder.generate(out);
      out.println(indent(2) + "public static final String VERSION = \"1\";");
      out.println();
    } else if (client.isOldGDataStyle) {
      DocBuilder.generateComment(out, 2, "Version name.");
      out.println(indent(2) + "public static final String VERSION = \""
          + version.id.substring(1) + "\";");
      out.println();
    }
    if (version.rootUrl != null) {
      DocBuilder.generateComment(out, 2, "Root URL.");
      out.println(indent(2) + "public static final String ROOT_URL = \""
          + version.rootUrl + "\";");
      out.println();
    }
    if (client.authTokenType != null) {
      if ("moderator".equals(client.id)) {
        DocBuilder docBuilder = new DocBuilder();
        docBuilder.container = classDocBuilder;
        docBuilder.comment =
            "The authentication token type used for Client Login.";
        docBuilder.indentNumSpaces = 2;
        docBuilder.isDeprecated = true;
        docBuilder.removedMinor = 4;
        docBuilder.generate(out);
      } else {
        DocBuilder.generateComment(out, 2,
            "The authentication token type used for Client Login.");
      }
      out.println(indent(2) + "public static final String AUTH_TOKEN_TYPE = \""
          + client.authTokenType + "\";");
      out.println();
    }
    if (client.oauth != null) {
      OAuthInfo oauth = client.oauth;
      if (oauth.authorizationUrl != null) {
        DocBuilder docBuilder = new DocBuilder();
        docBuilder.comment = "OAuth authorization service endpoint.";
        docBuilder.container = classDocBuilder;
        docBuilder.sinceMinor = Math.max(3, version.sinceMinor);
        docBuilder.indentNumSpaces = 2;
        docBuilder.generate(out);
        out.println(indent(2)
            + "public static final String OAUTH_AUTHORIZATION_URL = \""
            + oauth.authorizationUrl + "\";");
        out.println();
      }
      if (oauth.scopes != null) {
        for (Map.Entry<String, String> entry : oauth.scopes.entrySet()) {
          DocBuilder docBuilder = new DocBuilder();
          docBuilder.container = classDocBuilder;
          docBuilder.sinceMinor = Math.max(3, version.sinceMinor);
          docBuilder.indentNumSpaces = 2;
          docBuilder.generate(out);
          String suffix =
              entry.getKey().length() == 0 ? "" : "_" + entry.getKey();
          out.println(indent(2) + "public static final String OAUTH_SCOPE"
              + suffix + " = \"" + entry.getValue() + "\";");
          out.println();
        }
      }
    }
    out.println(indent(2) + "private " + className + "() {");
    out.println(indent(2) + "}");
    out.println("}");
    out.close();
  }
}
