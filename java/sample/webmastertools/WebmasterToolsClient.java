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
import com.google.gdata.data.webmastertools.SitemapsEntry;
import com.google.gdata.data.webmastertools.SitemapsFeed;
import com.google.gdata.data.webmastertools.SitemapsRegularEntry;
import com.google.gdata.data.webmastertools.SitesEntry;
import com.google.gdata.data.webmastertools.SitesFeed;
import com.google.gdata.data.webmastertools.VerificationMethod;
import com.google.gdata.util.ServiceException;

import sample.util.SimpleCommandLineParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Demonstrates how to use the Google Data API's Java client library to
 * interface with the Webmaster Tools  service. There are methods for the following
 * operations:
 * <ol>
 * <li>List user's sites</li>
 * <li>Add site</li>
 * <li>List user's Sitemaps</li>
 * <li>Add Sitemap</li>
 * <li>Verify site</li>
 * <li>Delete Sitemap</li>
 * <li>Delete site</li>
 * </ol>
 *
 * This sample program will only perform:
 * <ol>
 * <li>List user's sites</li>
 * <li>Add site: SAMPLE_SITE</li>
 * <li>List user's Sitemaps</li>
 * <li>Add SAMPLE_SITEMAP</li>
 * <li>Delete SAMPLE_SITEMAP</li>
 * <li>Delete SAMPLE_SITE</li>
 * </ol>
 * Additional methods are available if uncommented below.
 */
public class WebmasterToolsClient {

  private static final String FEED_URI_BASE = 
      "http://www.google.com/webmasters/tools/feeds/";

  private static final String SITES_FEED_DIRECTORY = "sites/";
  private static final String SITEMAPS_FEED_DIRECTORY = "/sitemaps/";

  /* SAMPLE_SITE and SAMPLE_SITEMAP provide an example site for test methods */
  private static final String SAMPLE_SITE = "http://www.example.com/";
  private static final String SAMPLE_SITEMAP =
      "http://www.example.com/sitemap.xml";

  /* Sitemap type declared at Sitemap submission.
   * Additional types are VIDEO, CODE, and NEWS.
   * Submission for other types  will require more information. */
  private static final String GENERAL_WEB_SITEMAP = "WEB";

  /* Public empty constructor*/
  public WebmasterToolsClient() {
  }

  /**
   * Adds a new site for the given user.
   *
   * @param myService Authenticated WebmasterTools Service object
   * @param siteUrl URL of site to add to account
   * @return A SitesEntry with the newly-created site
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  public static SitesEntry insertSite(
      WebmasterToolsService myService, String siteUrl)
      throws IOException, ServiceException {
    SitesEntry entry = new SitesEntry();
    OutOfLineContent content = new OutOfLineContent();
    content.setUri(siteUrl);
    entry.setContent(content);
    System.out.println("Site: " + siteUrl + " now being added.");
    return myService.insert(newSitesFeedUrl(), entry);
  }

  /**
   * List verification filename and metatag values.
   *
   * @param myService Authenticated WebmasterTools Service object.
   * @param siteUrl URL of site to be verified.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  public static void listVerificationValues(
      WebmasterToolsService myService, String siteUrl)
      throws IOException, ServiceException {
    /* Request the feed */
    String siteId = URLEncoder.encode(siteUrl, "UTF-8");
    URL feedUrl = new URL(newSitesFeedUrl() + siteId);
    SitesEntry entry = myService.getEntry(feedUrl, SitesEntry.class);
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
   * @param myService Authenticated WebmasterTools Service object.
   * @param siteUrl URL of site to be verified.
   * @return A SitesEntry for the verified site.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  public static SitesEntry verifySite(
      WebmasterToolsService myService, String siteUrl)
      throws IOException, ServiceException {
    VerificationMethod method = new VerificationMethod();
    method.setMethodType(VerificationMethod.MethodType.HTMLPAGE);
    /* Or method.setMethodType(VerificationMethod.MethodType.METATAG);*/
    method.setInUse(true);

    /* Create the new SitesEntry to be verified */
    SitesEntry entry = new SitesEntry();
    entry.addVerificationMethod(method);
    String siteId = URLEncoder.encode(siteUrl, "UTF-8");
    System.out.println("Now verifying site: " + siteUrl);
    URL updateUrl = new URL(newSitesFeedUrl() + siteId);
    return myService.update(updateUrl, entry);
  }

  /**
   * Delete the site from the user's Webmaster Tools account.
   *
   * @param myService Authenticated WebmasterTools Service object.
   * @param siteUrl URL of site to delete from account.
   * @throws IOException If there is a problem communicating with the server.
   * @throws ServiceException If the service is unable to handle the request.
   */
  public static void deleteSite(WebmasterToolsService myService, String siteUrl)
      throws IOException, ServiceException {
    String siteId = URLEncoder.encode(siteUrl, "UTF-8");
    URL feedUrl = new URL(newSitesFeedUrl() + siteId);
    SitesEntry entry = myService.getEntry(feedUrl, SitesEntry.class);
    System.out.println("Now deleting site: " + siteUrl);
    entry.delete();
  }

  /**
   * Delete the Sitemap from the user's Webmaster Tools account.
   *
   * @param myService Authenticated WebmasterTools Service object.
   * @param siteUrl Site where the Sitemap is located
   *        (e.g. "http:/www.example.com/").
   * @param sitemapUrl Full path to Sitemap file.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  public static void deleteSitemap(
      WebmasterToolsService myService, String siteUrl,
      String sitemapUrl) throws IOException, ServiceException {
    String siteId = URLEncoder.encode(siteUrl, "UTF-8");
    String sitemapId = URLEncoder.encode(sitemapUrl, "UTF-8");
    URL feedUrl = new URL(newSitemapsFeedUrl(siteId) + sitemapId);
    SitemapsRegularEntry entry =
        myService.getEntry(feedUrl, SitemapsRegularEntry.class);
    System.out.println("Now deleting Sitemap: " + sitemapUrl);
    entry.delete();
  }

  /**
   * Submit a Sitemap for the user's Webmaster Tools account.
   *
   * @param myService Authenticated WebmasterTools Service object
   * @param siteUrl URL of site hosting the Sitemap
   * @param sitemapUrl is a Sitemap URL to submit. If the Sitemap has been
   *     added already, then this method results in resubmitting the Sitemap.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed
   */
  public static SitemapsEntry insertSitemap(
      WebmasterToolsService myService, String siteUrl,
      String sitemapUrl)
    throws IOException, ServiceException {
    SitemapsRegularEntry entry = new SitemapsRegularEntry();
    entry.setId(sitemapUrl);
    entry.setSitemapType(GENERAL_WEB_SITEMAP);
    System.out.println("Sitemap: " + sitemapUrl + " now being added.");
    return myService.insert(newSitemapsFeedUrl(siteUrl), entry);
  }

  /**
   * Creates the URL for the sites feed.
   *
   * @return URL of Webmaster Tools Sites feed.
   * @throws IOException if the URL is malformed.
   */
  private static URL newSitesFeedUrl() throws IOException {
    try {
      return new URL(FEED_URI_BASE + SITES_FEED_DIRECTORY);
    } catch (MalformedURLException e) {
      throw new IOException("URL for sites is malformed.");
    }
  }

  /**
   * Creates the URL for the sitemaps feed.
   *
   * @return URL of Webmaster Tools Sitemaps feed.
   * @throws IOException if the URL is malformed.
   */
  private static URL newSitemapsFeedUrl(String siteUrl) throws IOException {
    try {
      String siteId = URLEncoder.encode(siteUrl, "UTF-8");
      return new URL(FEED_URI_BASE + siteId + SITEMAPS_FEED_DIRECTORY);
    } catch (MalformedURLException e) {
      throw new IOException("URL for site URL is malformed.");
    }
  }

  /**
   * Lists all sites in a user's Webmaster Tools account.
   *
   * @param myService Authenticated WebmasterTools Service object
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  public static void printUserSites(WebmasterToolsService myService)
      throws ServiceException, IOException {
    try {
      System.out.println("Printing user's sites:");
      /* Request the feed */
      URL feedUrl = newSitesFeedUrl();
      SitesFeed sitesResultFeed = myService.getFeed(feedUrl, SitesFeed.class);

      /* Print the results */
      for (SitesEntry entry : sitesResultFeed.getEntries()) {
        System.out.println("\t" + entry.getTitle().getPlainText());
      }
    } catch (MalformedURLException e) {
      throw new IOException("URL for site URL is malformed.");
    }
  }

  /**
   * Prints all Sitemap URLs for a given site.
   *
   * @param myService Authenticated WebmasterTools Service object.
   * @param siteUrl URL of site to view existing Sitemaps.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If the URL is malformed.
   */
  public static void printUserSitemaps(
      WebmasterToolsService myService, String siteUrl)
      throws ServiceException, IOException {
    System.out.println("Printing user's Sitemaps:");
    try {
      String siteId = URLEncoder.encode(siteUrl, "UTF-8");
      /* Request the feed */
      URL feedUrl = newSitemapsFeedUrl(siteUrl);
      SitemapsFeed resultFeed = myService.getFeed(feedUrl, SitemapsFeed.class);

      /* Print the results */
      for (int i = 0; i < resultFeed.getEntries().size(); i++) {
        SitemapsEntry entry = resultFeed.getEntries().get(i);
        System.out.println("\t" + entry.getTitle().getPlainText());
      }
    } catch (MalformedURLException e) {
      throw new IOException("URL for site URL is malformed.");
    }
  }

  /**
   * Runs through many of the methods using the Webmaster Service instance.
   * Users may comment or uncomment methods that they would like to run.
   *
   * @param myService An authenticated GoogleService object.
   * @param userName Username of user to authenticate (e.g. jdoe@gmail.com).
   * @param userPassword Password for user's authentication.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException If there is an error communicating with the server.
   */
  public void run(WebmasterToolsService myService, String userName,
      String userPassword) throws ServiceException, IOException {
    /* Authenticate using ClientLogin */
    System.out.println("Running with user: " + userName);
    myService.setUserCredentials(userName, userPassword);
    printUserSites(myService);
    insertSite(myService, SAMPLE_SITE);
    insertSitemap(myService, SAMPLE_SITE, SAMPLE_SITEMAP);
    printUserSitemaps(myService, SAMPLE_SITE);
    listVerificationValues(myService, SAMPLE_SITE);

    /* Here you can add a function to create verification files or metatags then
     * uncomment verifySite() below */
    /* verifySite(myService, SAMPLE_SITE);*/

    deleteSitemap(myService, SAMPLE_SITE, SAMPLE_SITEMAP);
    deleteSite(myService, SAMPLE_SITE);
    printUserSites(myService);
    System.out.println("Sample run completed.");
  }

  /**
   * Uses the command line arguments to authenticate the WebmasterToolsService
   * and build the basic feed URI, then invokes all the other methods to
   * demonstrate how to interface with the Webmastertools service.
   *
   * @param args See the usage method.
   */
  public static void main(String[] args) {
    /* Set username, password and feed URI from command-line arguments. */
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String userName = parser.getValue("username", "user", "u");
    String userPassword = parser.getValue("password", "pass", "p");
    boolean help = parser.containsKey("help", "h");
    if (help || (userName == null) || (userPassword == null)) {
      usage();
      System.exit(1);
    }

    WebmasterToolsService myService =
        new WebmasterToolsService("exampleCo-exampleApp-1");
    WebmasterToolsClient demo = new WebmasterToolsClient();

    try {
      demo.run(myService, userName, userPassword);
    } catch (ServiceException se) {
      se.printStackTrace();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * Prints the command line usage of this sample application.
   */
  private static void usage() {
    System.out.println("Usage: WebmasterToolsClient --username <username>"
        + " --password <password>");
    System.out.println("\nA simple application that lists sites,\n"
        + "and Sitemaps using the provided username and\n"
        + "password for authentication.");
  }
}
