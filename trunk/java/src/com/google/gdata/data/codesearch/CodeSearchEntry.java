/* Copyright (c) 2008 Google Inc.
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


package com.google.gdata.data.codesearch;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

import java.util.List;


/**
 * Used to represent the results of a code query in a single file.
 *
 * 
*/
@Kind.Term(CodeSearchEntry.CODESEARCH_KIND)
public class CodeSearchEntry extends BaseEntry<CodeSearchEntry> {

  /**
   * Kind term value for CodeSearch codesearch category labels.
   */
  public static final String CODESEARCH_KIND =
    Namespaces.gCS + "#result";

  /**
   * Category used to label entries that contain CodeSearch results.
   */
  public static final Category CODESEARCH_CATEGORY =
    new Category(com.google.gdata.util.Namespaces.gKind, CODESEARCH_KIND);

  /**
   * Constructs a new CodeSearchEntry instance with the appropriate category
   * to indicate that it is a code search result.
   */
  public CodeSearchEntry() {
    super();
    getCategories().add(CODESEARCH_CATEGORY);
  }

  /**
   * Constructs a new CodeSearchEntry instance by doing a shallow copy of data
   * from an existing BaseEntry instance.
   */
  public CodeSearchEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
    getCategories().add(CODESEARCH_CATEGORY);
  }

  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by a CodeSearchEntry.
   */

  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(CodeSearchEntry.class, File.getDefaultDescription());
    extProfile.declare(CodeSearchEntry.class, Package.getDefaultDescription());
    extProfile.declare(CodeSearchEntry.class, Match.getDefaultDescription());
  }

  /**
   * Returns the Code Search file property.
   */
  public File getFile() {
    return getExtension(File.class);
  }

  /**
   * Returns the Code Search file property.
   */
  public Package getPackage() {
    return getExtension(Package.class);
  }

  /**
   * Returns the list of calendar locations
   */
  public List<Match> getMatches() {
    return getRepeatingExtension(Match.class);
  }

}
