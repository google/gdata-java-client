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


package sample.webmastertools;

import com.google.gdata.client.webmastertools.WebmasterToolsService;
import com.google.gdata.data.OutOfLineContent;
import com.google.gdata.data.webmastertools.CrawlRate;
import com.google.gdata.data.webmastertools.DomainPreference;
import com.google.gdata.data.webmastertools.SitemapsEntry;
import com.google.gdata.data.webmastertools.SitemapsFeed;
import com.google.gdata.data.webmastertools.SitemapsRegularEntry;
import com.google.gdata.data.webmastertools.SitesEntry;
import com.google.gdata.data.webmastertools.SitesFeed;
import com.google.gdata.data.webmastertools.VerificationMethod;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import sample.util.SimpleCommandLineParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Demonstrates how to use the Google Data API's Java client library to
 * interface with the Webmaster Tools service.
 *
 * Google Data API: http://code.google.com/apis/gdata/
 * Webmaster Tools Data API: http://code.google.com/apis/webmastertools/
 *
 * There are methods for the following operations:
 * <ol>
 *   <li>List user's sites</li>
 *   <li>Add site</li>
 *   <li>List verification options</li>
 *   <li>Verify site</li>
 *   <li>List site settings</li>
 *   <li>Update site settings</li>
 *   <li>Delete site</li>
 *   <li>List user's Sitemaps</li>
 *   <li>Add Sitemap</li>
 *   <li>Delete Sitemap</li>
 * </ol>
 *
 * This sample program will only perform:
 * <ol>
 *   <li>List user's sites</li>
 *   <li>Add site: SAMPLE_SITE</li>
 *   <li>List verification values for SAMPLE_SITE</li>
 *   <li>List site settings (will not appear for unverified sites)</li>
 *   <li>List user's Sitemaps</li>
 *   <li>Add SAMPLE_SITEMAP</li>
 *   <li>Delete SAMPLE_SITEMAP</li>
 *   <li>Delete SAMPLE_SITE</li>
 * </ol>
 */
public class WebmasterToolsClient {
  
  // URL of the base feed
  private static final String FEED_URI_BASE
      = "https://www.google.com/webmasters/tools/feeds/";

  // Directory of the sites feed
  private static final String SITES_FEED_DIRECTORY = "sites/";
  
  // Directory of the sitemaps feed for a particular site
  private static final String SITEMAPS_FEED_DIRECTORY = "/sitemaps/";

  // Sample site URL. No verification required to add or remove sites
  // Please keep the trailing slash, it is required to access the site
  private static final String SAMPLE_SITE = "http://www.example.com/";

  // Sample sitemaps URL. No verification required to add or remove sitemaps
  private static final String SAMPLE_SITEMAP
      = "http://www.example.com/sitemap.xml";

  // Geographic location, two letter region code
  private static final String SAMPLE_LOCATION = "CA";

  // Desired crawl rate, SLOWER, NORMAL, FASTER. See CrawlRate
  private static final CrawlRate SAMPLE_RATE = CrawlRate.SLOWER;

  // Preferred domain. NONE, PREFER_WWW, PREFER_NO_WWW. See Domain Preference
  private static final DomainPreference SAMPLE_PREFERRED_DOMAIN
      = DomainPreference.PREFER_WWW;

  // Enable enhanced image search for the site
  private static final boolean SAMPLE_ENHANCED_IMAGE_SEARCH = true;

  // Sitemap type. Supported types include WEB, MOBILE, CODE, VIDEO, and NEWS.
  // See http://code.google.com/apis/webmastertools/docs/developers_guide.html#Sitemaps
  private static final String GENERAL_WEB_SITEMAP = "WEB";

  /**
   * Public empty constructor
   */
  public WebmasterToolsClient() {
  }

  /**
   * Creates the URL for the sites feed.
   *
   * @return URL of Webmaster Tools Sites feed.
   * @throws IOException if the URL is malformed.
   */
  private static URL getSitesFeedUrl() throws IOException {
    try {
      return new URL(FEED_URI_BASE + SITES_FEED_DIRECTORY);
    } catch (MalformedURLException e) {
      throw new IOException("URL for sites feed is malformed.");
    }
  }  
  
  /**
   * Lists all sites in a user's Webmaster Tools account.
   * Sites may be sorted randomly. Do not rely on the ordering of the sites.
   *
   * @param myService Authenticated WebmasterTools Service object
   * @throws ServiceException if the service is unable to handle the request.
   * @throws IOException if the URL is malformed.
   */
  public static void printUserSites(WebmasterToolsService myService)
      throws ServiceException, IOException {
    try {
      System.out.println("Printing user's sites:");
      
      // Request the feed
      URL feedUrl = getSitesFeedUrl();
      SitesFeed sitesResultFeed = myService.getFeed(feedUrl, SitesFeed.class);

      // Print the results
      for (SitesEntry entry : sitesResultFeed.getEntries()) {
        System.out.println("\t" + entry.getTitle().getPlainText());
      }
    } catch (MalformedURLException e) {
      throw new IOException("URL for sites feed is malformed.");
    }
  }
  
  /**
   * Adds a new site for the given user.
   *
   * @param myService authenticated WebmasterTools Service object
   * @param siteUrl URL of site to add to account
   * @return a SitesEntry with the newly-created site
   * @throws ServiceException if the service is unable to handle the request.
   * @throws IOException if there was an error communicating with the server.
   */
  public static SitesEntry insertSite(WebmasterToolsService myService, 
      String siteUrl) throws IOException, ServiceException {
    SitesEntry entry = new SitesEntry();
    OutOfLineContent content = new OutOfLineContent();
    content.setUri(siteUrl);
    entry.setContent(content);
    System.out.println("Site: " + siteUrl + " now being added.");
    return myService.insert(getSitesFeedUrl(), entry);
  }

  /**
   * List verification options, currently filename and metatag values.
   *
   * @param myService authenticated WebmasterTools Service object.
   * @param siteUrl URL of site to be verified.
   * @throws ServiceException if the service is unable to handle the request.
   * @throws IOException if there was an error communicating with the server.
   */
  public static void listVerificationValues(WebmasterToolsService myService, 
      String siteUrl) throws IOException, ServiceException {
    // Request the entry
    String siteId = URLEncoder.encode(siteUrl, "UTF-8");
    URL feedUrl = new URL(getSitesFeedUrl() + siteId);
    SitesEntry entry = myService.getEntry(feedUrl, SitesEntry.class);
    
    // Print verification options
    for (VerificationMethod method : entry.getVerificationMethods()) {
      System.out.println("Verification method: " + method.getMethodType());
      if (method.getMethodType() == VerificationMethod.MethodType.METATAG) {
        System.out.println("Meta verification tag value: " + method.getValue());
      } else if (
          method.getMethodType() == VerificationMethod.MethodType.HTMLPAGE) {
        System.out.println("HTML verification page is: " + method.getValue());
      }
    }
  }
 
  /**
   * Submit site for verification. This example verifies through an
   * HTML page. A webmaster may also verify through
   * VerificationMethod.MethodType.METATAG.
   *
   * @param myService authenticated WebmasterTools Service object.
   * @param siteUrl URL of site to be verified.
   * @return a SitesEntry for the verified site.
   * @throws ServiceException if the service is unable to handle the request.
   * @throws IOException if there was an error communicating with the server.
   */
  public static SitesEntry verifySite(WebmasterToolsService myService,
      String siteUrl) throws IOException, ServiceException {
    VerificationMethod method = new VerificationMethod();
    method.setMethodType(VerificationMethod.MethodType.HTMLPAGE);
    // Or method.setMethodType(VerificationMethod.MethodType.METATAG);
    method.setInUse(true);

    // Create the new SitesEntry to be verified
    SitesEntry entry = new SitesEntry();
    entry.addVerificationMethod(method);
    String siteId = URLEncoder.encode(siteUrl, "UTF-8");
    System.out.println("Now verifying site: " + siteUrl);
    URL updateUrl = new URL(getSitesFeedUrl() + siteId);
    return myService.update(updateUrl, entry);
  }

  /**
   * Print the settings for all sites in the feed. The sites have to be
   * verified in order to be able to access settings information.
   * 
   * @param myService authenticated WebmasterTools Service object.
   * @throws IOException if the URL is malformed.
   * @throws ServiceException if the service is unable to handle the request.
   */
  public static void printSiteSettings(WebmasterToolsService myService)
      throws IOException, ServiceException {
    try {
      System.out.println("Printing site settings:");

      // Get the sites feed
      URL feedUrl = getSitesFeedUrl();
      SitesFeed sitesResultFeed = myService.getFeed(feedUrl, SitesFeed.class);
      for (SitesEntry entry : sitesResultFeed.getEntries()) {
        // Print site name and settings
        System.out.println("\t" + entry.getTitle().getPlainText());
        System.out.println("\t\tGeographic location:"
            + entry.getGeolocation());
        System.out.println("\t\tDesired Crawl Rate:"
            + entry.getCrawlRate());
        System.out.println("\t\tPreferred Domain Association:"
            + entry.getPreferredDomain());
        System.out.println("\t\tEnhanced Image Search:"
            + entry.getEnhancedImageSearch());
      }
    } catch (MalformedURLException e) {
      throw new IOException("URL for site URL is malformed.");
    }
  }
  
  /**
   * Update the settings for a site. The settings supported are
   * geographic location, desired crawl rate, preferred domain and
   * enhanced image search. Please update one setting at a time.
   * Updating multiple settings at a time is not supported.
   * 
   * @param myService authenticated WebmasterTools Service object.
   * @param siteUrl is the URL of the site to update.
   * @return a SitesEntry with the last updated site.
   * @throws IOException if there was an error communicating with the server.
   * @throws ServiceException if the service is unable to handle the request.
   */
  public static SitesEntry updateSiteSettings(WebmasterToolsService myService,
      String siteUrl) throws IOException, ServiceException {
    System.out.println("Site: " + siteUrl);
    String siteId = URLEncoder.encode(siteUrl, "UTF-8");
    URL updateUrl = new URL(getSitesFeedUrl() + siteId);

    try {
      // Update geographic location
      System.out.println("Updating geographic location...");
      SitesEntry entryUpdate = new SitesEntry();
      entryUpdate.setGeolocation(SAMPLE_LOCATION);
      myService.update(updateUrl, entryUpdate);

      // Update desired crawl rate
      System.out.println("Updating desired crawl rate...");
      entryUpdate = new SitesEntry();
      entryUpdate.setCrawlRate(SAMPLE_RATE);
      myService.update(updateUrl, entryUpdate);

      // Update preferred domain
      System.out.println("Updating preferred domain...");
      entryUpdate = new SitesEntry();
      entryUpdate.setPreferredDomain(SAMPLE_PREFERRED_DOMAIN);
      myService.update(updateUrl, entryUpdate);

      // Update enhanced image search and return the last updated entry
      System.out.println("Updating enhanced image search...");
      entryUpdate = new SitesEntry();
      entryUpdate.setEnhancedImageSearch(SAMPLE_ENHANCED_IMAGE_SEARCH);
      return myService.update(updateUrl, entryUpdate);
    } catch (ServiceException e) {
      System.out.println("Please make sure that the site to update "
          + "contains a trailing forward slash");
      throw(e);
    }
  }
    
  /**
   * Delete the site from the user's Webmaster Tools account.
   *
   * @param myService authenticated WebmasterTools Service object.
   * @param siteUrl URL of site to delete from account.
   * @throws IOException if there was an error communicating with the server.
   * @throws ServiceException if the service is unable to handle the request.
   */
  public static void deleteSite(WebmasterToolsService myService, String siteUrl)
      throws IOException, ServiceException {
    String siteId = URLEncoder.encode(siteUrl, "UTF-8");
    URL feedUrl = new URL(getSitesFeedUrl() + siteId);
    SitesEntry entry = myService.getEntry(feedUrl, SitesEntry.class);
    System.out.println("Now deleting site: " + siteUrl);
    entry.delete();
  }

  /**
   * Creates the URL for the sitemaps feed.
   *
   * @return URL of Webmaster Tools Sitemaps feed.
   * @throws IOException if the URL is malformed.
   */
  private static URL getSitemapsFeedUrl(String siteUrl) throws IOException {
    try {
      String siteId = URLEncoder.encode(siteUrl, "UTF-8");
      return new URL(FEED_URI_BASE + siteId + SITEMAPS_FEED_DIRECTORY);
    } catch (MalformedURLException e) {
      throw new IOException("URL for site URL is malformed.");
    }
  }
  
  /**
   * Prints all Sitemap URLs for a given site.
   *
   * @param myService authenticated WebmasterTools Service object.
   * @param siteUrl URL of site to view existing Sitemaps.
   * @throws ServiceException if the service is unable to handle the request.
   * @throws IOException if the URL is malformed.
   */
  public static void printUserSitemaps(WebmasterToolsService myService, 
      String siteUrl) throws ServiceException, IOException {
    System.out.println("Printing user's Sitemaps:");
    try {
      String siteId = URLEncoder.encode(siteUrl, "UTF-8");
      // Request the feed
      URL feedUrl = getSitemapsFeedUrl(siteUrl);
      SitemapsFeed resultFeed = myService.getFeed(feedUrl, SitemapsFeed.class);

      // Print the results    
      for(SitemapsEntry entry : resultFeed.getEntries()) {
        System.out.println("\t" + entry.getTitle().getPlainText());
      }
    } catch (MalformedURLException e) {
      throw new IOException("URL for site URL is malformed.");
    }
  }

  /**
   * Submit a Sitemap for the user's Webmaster Tools account.
   *
   * @param myService authenticated WebmasterTools Service object
   * @param siteUrl URL of site hosting the Sitemap
   * @param sitemapUrl is a Sitemap URL to submit. If the Sitemap has been
   *     added already, then this method results in resubmitting the Sitemap.
   * @throws ServiceException if the service is unable to handle the request.
   * @throws IOException if there was an error communicating with the server.
   */
  public static SitemapsEntry insertSitemap(WebmasterToolsService myService, 
      String siteUrl, String sitemapUrl) throws IOException, ServiceException {
    SitemapsRegularEntry entry = new SitemapsRegularEntry();
    entry.setId(sitemapUrl);
    entry.setSitemapType(GENERAL_WEB_SITEMAP);
    System.out.println("Sitemap: " + sitemapUrl + " now being added.");
    return myService.insert(getSitemapsFeedUrl(siteUrl), entry);
  }

  /**
   * Delete the Sitemap from the user's Webmaster Tools account.
   *
   * @param myService authenticated WebmasterTools Service object.
   * @param siteUrl site where the Sitemap is located
   *        (e.g. "http:/www.example.com/").
   * @param sitemapUrl full path to Sitemap file.
   * @throws ServiceException if the service is unable to handle the request.
   * @throws IOException if there was an error communicating with the server.
   */
  public static void deleteSitemap(WebmasterToolsService myService,
      String siteUrl, String sitemapUrl) throws IOException, ServiceException {
    String siteId = URLEncoder.encode(siteUrl, "UTF-8");
    String sitemapId = URLEncoder.encode(sitemapUrl, "UTF-8");
    URL feedUrl = new URL(getSitemapsFeedUrl(siteId) + sitemapId);
    SitemapsRegularEntry entry
        = myService.getEntry(feedUrl, SitemapsRegularEntry.class);
    System.out.println("Now deleting Sitemap: " + sitemapUrl);
    entry.delete();
  }

  /**
   * Uses the command line arguments to authenticate with the WebmasterTools
   * service, then invokes all the other methods to demonstrate how to use it.
   *
   * @param args See the usage method.
   */
  public static void main(String[] args) {
    // Get username and password from command-line arguments.
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String userName = parser.getValue("username", "user", "u");
    String userPassword = parser.getValue("password", "pass", "p");
    boolean help = parser.containsKey("help", "h");
    if (help || (userName == null) || (userPassword == null)) {
      usage();
      System.exit(1);
    }

    // Connect with the service and authenticate
    WebmasterToolsService myService =
        new WebmasterToolsService("exampleCo-exampleApp-1");
    try {
      myService.setUserCredentials(userName, userPassword);
    } catch (AuthenticationException e) {
      System.out.println("The username or password entered are not valid");
      System.exit(1);
    }
    System.out.println("Running with user: " + userName);

    // Run the example methods, list sites, insert, update, etc.
    try {
      runExamples(myService);
    } catch (ServiceException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("Sample run completed.");
  }

  /**
   * Prints the command line usage of this sample application.
   */
  private static void usage() {
    System.out.println("Usage: WebmasterToolsClient --username <username>"
        + " --password <password>");
    System.out.println("\nA simple application that lists sites, "
        + "and Sitemaps using the provided username and "
        + "password for authentication.");
  }
  
  /**
   * Runs through many of the methods using the Webmaster Service instance.
   * Users may comment or uncomment methods that they would like to run.
   *
   * @param myService an authenticated GoogleService object.
   * @throws ServiceException if the service is unable to handle the request.
   * @throws IOException if there is an error communicating with the server.
   */
  public static void runExamples(WebmasterToolsService myService) 
      throws ServiceException, IOException {
    printUserSites(myService);
    insertSite(myService, SAMPLE_SITE);
    printSiteSettings(myService);
    listVerificationValues(myService, SAMPLE_SITE);
    
    // After the verification process is done, for example by uploading
    // the verification file to your server run this verification method
    // verifySite(myService, SAMPLE_SITE);
    
    // Uncomment to update the settings for the site once it is verified
    // updateSiteSettings(myService, SAMPLE_SITE);
    
    printUserSitemaps(myService, SAMPLE_SITE);
    insertSitemap(myService, SAMPLE_SITE, SAMPLE_SITEMAP);
    deleteSitemap(myService, SAMPLE_SITE, SAMPLE_SITEMAP);
    
    deleteSite(myService, SAMPLE_SITE);
  }
}
