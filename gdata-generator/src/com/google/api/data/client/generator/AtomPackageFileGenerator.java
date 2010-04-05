/* Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.api.data.client.generator;

import java.io.PrintWriter;

public final class AtomPackageFileGenerator extends AbstractHtmlFileGenerator {

  private final Version version;

  AtomPackageFileGenerator(Version version) {
    this.version = version;
  }

  public void generate(PrintWriter out) {
    out.println("<body>");
    out.println("Small optional Java library for the Atom XML format for " + version.client.name
        + " version " + version.id + ".");
    out.println("</body>");
    out.close();
  }

  public String getOutputFilePath() {
    return "src/" + version.getPathRelativeToSrc() + "/atom/package.html";
  }

  @Override
  public boolean isGenerated() {
    return version.atom != null;
  }
}
