/* Copyright (c) 2006 Google Inc.
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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.extensions.EntryLink;

import java.util.BitSet;
import java.util.List;

/**
 * This represents a single web site entry. It conveys information about a
 * given site that is identified by site URL. It specifies whether site is
 * indexed by Google, verified, last time the web site has been crawled, etc.
 * The entry list all the available verification methods that can be used by
 * this web site for verification with one of the methods marked to be in-use
 * at the time. See {@link VerificationMethod} for more information.
 * 
 * Example (common Atom nodes are omitted):
 * <pre class="code">
 *   <entry>
 *     <id>http://www.websiteurl.com/</id>
 *     <wt:indexed>true</wt:indexed>
 *     <wt:verified>true</wt:verified>
 *     <wt:last-crawled>2007-1-1T18:30:00.000Z</wt:last-crawled>
 *     <wt:verification-method type="metatag" in-use="false>
 *       <meta name="verify-v1" content="XXX" />
 *     </wt:verification-method>
 *     <wt:verification-method type="html" in-use="false>
 *       XXX-google.html
 *     </wt:verification-method>     
 *   </entry>
 * </pre>
 *
 * 
 */
public class SitesEntry extends BaseEntry<SitesEntry> {

  /** These define XML node names defined inside of this entry. */
  private static final String INDEXED = "indexed";
  private static final String CRAWLED = "crawled";
  private static final String VERIFIED = "verified";
  
  /**
   * Kind category used to label entry.
   */
  private static final Category CATEGORY
      = Namespaces.createCategory(Namespaces.KIND_SITE_INFO);

  /**
   * Constructs a new SitesEntry instance
   */
  public SitesEntry() {
    super();
    this.getCategories().add(CATEGORY);
  }

  /**
   * Initializes an ExtensionProfile based upon the extensions expected
   * by a SitesEntry. Extension profile specifies which nodes are expected to
   * be in the entry.
   */
  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    ExtensionDescription desc = EntryLink.getDefaultDescription();
    desc.setRepeatable(true);
    extProfile.declare(
        SitesEntry.class,
        desc);

    extProfile.declare(
        SitesEntry.class,
        ExtensionDescription.getDefaultDescription(Indexed.class));

    extProfile.declare(
        SitesEntry.class,
        ExtensionDescription.getDefaultDescription(Crawled.class));
    
    extProfile.declare(
        SitesEntry.class,
        ExtensionDescription.getDefaultDescription(Verified.class));
    
    desc = ExtensionDescription.getDefaultDescription(VerificationMethod.class);
    desc.setRepeatable(true);
    extProfile.declare(
        SitesEntry.class,
        desc);
  }

  /**
   * Changes site indexing status.
   *
   * @param isIndexed specifies if site is indexed or not.
   */
  public void setIndexed(boolean isIndexed) {
    Indexed indexed = getExtension(Indexed.class);
    if (indexed == null) {
      indexed = new Indexed();
      setExtension(indexed);
    }

    indexed.setBooleanValue(isIndexed);
  }

  /**
   * Returns site indexing status.
   *
   * @return {@code true} if site is indexed or {@code false} otherwise.
   */
  public boolean getIndexed() {
    Indexed indexed = getExtension(Indexed.class);
    if (indexed == null) {
      return false;
    }

    return indexed.getBooleanValue();
  }

  /**
   * Changes the latest crawl time.
   *
   * @param crawlTime specifies site last crawl time.
   */
  public void setCrawled(DateTime crawlTime) {
    Crawled crawled = getExtension(Crawled.class);
    if (crawled == null) {
      crawled = new Crawled();
      setExtension(crawled);
    }

    crawled.setDateTime(crawlTime);
  }

  /**
   * Returns site last crawl time.
   *
   * @return last crawl time or {@code null} if time is not set.
   */
  public DateTime getCrawled() {
    Crawled crawled = getExtension(Crawled.class);
    if (crawled == null) {
      return null;
    }

    return crawled.getDateTime();
  }
  
  /**
   * Changes site verification status.
   *
   * @param isVerified specifies if site is verified or not.
   */
  public void setVerified(boolean isVerified) {
    Verified verified = getExtension(Verified.class);
    if (verified == null) {
      verified = new Verified();
      setExtension(verified);
    }

    verified.setBooleanValue(isVerified);
  }

  /**
   * Returns site verification status.
   *
   * @return {@code true} if site is indexed or {@code false} otherwise.
   */
  public boolean getVerified() {
    Verified verified = getExtension(Verified.class);
    if (verified == null) {
      return false;
    }

    return verified.getBooleanValue();
  }
  
  /**
   * Add available verification method for the site.
   */
  public void addVerificationMethod(VerificationMethod method) {
    addRepeatingExtension(method);
  }
  
  /**
   * Returns {@link List} of the verification methods for the site.
   */
  public List<VerificationMethod> getVerificationMethods() {
    return getRepeatingExtension(VerificationMethod.class);
  }
  
  /**
   * Validates that the list of verification methods contains no duplicate
   * method types and in-use flag is only set once.
   */
  @Override
  protected void validate() throws IllegalStateException {
    boolean inUseFound = false;
    int numMethods = VerificationMethod.MethodType.values().length;
    BitSet foundMethods = new BitSet(numMethods);
    
    for (VerificationMethod m : getVerificationMethods()) {
      // Check if we have already seen method of this type.
      int methodIndex = m.getMethodType().ordinal();
      if (foundMethods.get(methodIndex)) {
        throw new IllegalStateException("Duplicate verification method.");
      }
      foundMethods.set(methodIndex);

      // Increment in-use counter if the flag is set.
      if (m.getInUse()) {
        if (inUseFound) {
          throw new IllegalStateException(
              "Only one method can be marked as in-use.");
        }
        inUseFound = true;
      }
    }
  }  
  
  /**
   * Boolean value construct to represent <indexed> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_PREFIX,
      nsUri = Namespaces.WT_NAMESPACE_URI,
      localName = INDEXED)
  public static class Indexed extends BoolValueConstruct {
    public Indexed() {
      super(INDEXED);
    }    
  }
  
  /**
   * Date/time value construct to represent <crawled> field. 
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_PREFIX,
      nsUri = Namespaces.WT_NAMESPACE_URI,
      localName = CRAWLED)
  public static class Crawled extends DateTimeValueConstruct {
    public Crawled() {
      super(CRAWLED);
    }    
  }  

  /**
   * Boolean value construct to represent <verified> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_PREFIX,
      nsUri = Namespaces.WT_NAMESPACE_URI,
      localName = VERIFIED)
  public static class Verified extends BoolValueConstruct {
    public Verified() {
      super(VERIFIED);
    }
  }  
}
