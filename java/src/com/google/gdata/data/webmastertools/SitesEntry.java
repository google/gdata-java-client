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
 *     <wt:verified>true</wt:verified>
 *     <wt:geolocation>US</wt:geolocation>
 *     <wt:crawl-rate>normal</wt:crawl-rate>
 *     <wt:preferred-domain>preferwww</wt:preferred-domain>
 *     <wt:enhanced-image-search>true</wt:enhanced-image-search>
 *     <wt:verification-method type="metatag" in-use="false>
 *       <meta name="verify-v1" content="XXX" />
 *     </wt:verification-method>
 *     <wt:verification-method type="html" in-use="false>
 *       XXX-google.html
 *     </wt:verification-method>     
 *   </entry>
 * </pre>
 *
 * The indexed and last-crawled fields used below are deprecated.
 * They are not used any more by the frontend so entries returned do not
 * include them. However they are kept here in order to ensure that clients
 * that send entries with those fields still work.
 *
 * 
 * 
 */
public class SitesEntry extends BaseEntry<SitesEntry> {

  /** These define XML node names defined inside of this entry. */
  private static final String INDEXED = "indexed";
  private static final String CRAWLED = "crawled";
  private static final String VERIFIED = "verified";
  private static final String GEOLOCATION = "geolocation";
  private static final String CRAWL_RATE = "crawl-rate";
  private static final String PREFERRED_DOMAIN = "preferred-domain";
  private static final String ENHANCED_IMAGE_SEARCH = "enhanced-image-search";
  
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
    
    extProfile.declare(
        SitesEntry.class,
        ExtensionDescription.getDefaultDescription(Geolocation.class));
    
    extProfile.declare(
        SitesEntry.class,
        ExtensionDescription.getDefaultDescription(CrawlingRate.class));
    
    extProfile.declare(
        SitesEntry.class,
        ExtensionDescription.getDefaultDescription(PreferredDomain.class));
    
    extProfile.declare(
        SitesEntry.class,
        ExtensionDescription.getDefaultDescription(EnhancedImageSearch.class));
    
    desc = ExtensionDescription.getDefaultDescription(VerificationMethod.class);
    desc.setRepeatable(true);
    extProfile.declare(
        SitesEntry.class,
        desc);
  }

  /**
   * Changes site indexing status.
   *
   * @deprecated This field is not used any more.
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
   * @deprecated This field is not used any more.
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
   * @deprecated This field is not used any more.
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
   * @deprecated This field is not used any more.
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
   * Changes site geographic location. See 
   * http://www.unicode.org/cldr/data/diff/supplemental/territory_containment_un_m_49.html.
   *
   * @param regionCode is a two-letter code representing a country.
   */
  public void setGeolocation(String regionCode) {
    Geolocation geolocation = getExtension(Geolocation.class);
    if (geolocation == null) {
      geolocation = new Geolocation();
      setExtension(geolocation);
    }
  
    geolocation.setValue(regionCode);
  }  
  
  /**
   * Returns site geographic location. See
   * http://www.unicode.org/cldr/data/diff/supplemental/territory_containment_un_m_49.html.
   *
   * @return A two-letter code representing a country.
   */
  public String getGeolocation() {
    Geolocation geolocation = getExtension(Geolocation.class);
    // If the extension is null do not return any value to prevent
    // the field from appearing in the GData XML
    return (geolocation == null) ? null : geolocation.getValue();
  }
  
  /**
   * Changes the crawl rate.
   *
   * @param rate is an integer representing the desired crawl rate.
   */
  public void setCrawlRate(CrawlRate rate) {
    CrawlingRate crawlingRate = getExtension(CrawlingRate.class);
    if (crawlingRate == null) {
      crawlingRate = new CrawlingRate();
      setExtension(crawlingRate);
    }
  
    crawlingRate.setCrawlRate(rate);
  }  
  
  /**
   * Returns site crawl rate.
   *
   * @return A {@link CrawlRate} enum representing the crawl rate.
   */
  public CrawlRate getCrawlRate() {
    CrawlingRate crawlingRate = getExtension(CrawlingRate.class);
    // If the extension is null do not return any value to prevent
    // the field from appearing in the GData XML
    return (crawlingRate == null) ? null : crawlingRate.getCrawlRate();
  }
  
  /**
   * Changes the domain preference. 
   * 
   * @param preference is a {@link DomainPreference} that indicates which one 
   * of the possibilities is preferred, using domain, www.domain, or no
   * association at all.
   */
  public void setPreferredDomain(DomainPreference preference) {
    PreferredDomain preferredDomain = getExtension(PreferredDomain.class);
    if (preferredDomain == null) {
      preferredDomain = new PreferredDomain();
      setExtension(preferredDomain);
    }
    
    preferredDomain.setPreference(preference);
  }
  
  /**
   * Returns the domain preference for the site.
   * 
   * @return A {@link DomainPreference} value that indicates which one of 
   * the domain preferences for the site is preferred.
   */
  public DomainPreference getPreferredDomain() {
    PreferredDomain preferredDomain = getExtension(PreferredDomain.class);
    return (preferredDomain == null) ? null : preferredDomain.getPreference();
  }
  
  /**
   * Changes the Enhanced Image Search setting.
   * 
   * @param enabled specifies whether enhanced image should be enabled.
   */
  public void setEnhancedImageSearch(boolean enabled) {
    EnhancedImageSearch enhanced = getExtension(EnhancedImageSearch.class);
    if (enhanced == null) {
      enhanced = new EnhancedImageSearch();
      setExtension(enhanced);
    }
    
    enhanced.setBooleanValue(enabled);
  }
  
  /**
   * Returns the Enhanced Image Search setting.
   * 
   * @return {@code true} if Enhanced Image Search is enabled, {@code false} 
   * if it is disabled.
   */
  public boolean getEnhancedImageSearch() {
    EnhancedImageSearch enhanced = getExtension(EnhancedImageSearch.class);
    return ((enhanced != null) && enhanced.getBooleanValue());
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
   * Returns a List of {@link EntryLink} for the links of this class, which
   * are the child feeds.
   */
  public List<EntryLink> getEntryLinks() {
    return getRepeatingExtension(EntryLink.class);
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
   *
   * @deprecated This class is not used any more.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = INDEXED)
  public static class Indexed extends BoolValueConstruct {
    public Indexed() {
      super(INDEXED);
    }    
  }
  
  /**
   * Date/time value construct to represent <crawled> field. 
   *
   * @deprecated This class is not used any more.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
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
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = VERIFIED)
  public static class Verified extends BoolValueConstruct {
    public Verified() {
      super(VERIFIED);
    }
  }  
  
  /**
   * Region code value construct to represent <geolocation> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = GEOLOCATION)
  public static class Geolocation extends RegionCodeValueConstruct {
    public Geolocation() {
      super(GEOLOCATION);
    }
  }  
  
  /**
   * Crawl Rate construct to represent <crawl-rate> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = CRAWL_RATE)
  public static class CrawlingRate extends CrawlRateConstruct {
    public CrawlingRate() {
      super(CRAWL_RATE);
    }
  } 
  
  /**
   * Domain Preference construct to represent <preferred-domain> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = PREFERRED_DOMAIN)
  public static class PreferredDomain extends DomainPreferenceConstruct {
    public PreferredDomain() {
      super(PREFERRED_DOMAIN);
    }
  } 
  
  /**
   * Boolean value construct to represent <enhanced-image-search> field.
   */
  @ExtensionDescription.Default(
      nsAlias = Namespaces.WT_ALIAS,
      nsUri = Namespaces.WT,
      localName = ENHANCED_IMAGE_SEARCH)
  public static class EnhancedImageSearch extends BoolValueConstruct {
    public EnhancedImageSearch() {
      super(ENHANCED_IMAGE_SEARCH);
    }
  }   
}
