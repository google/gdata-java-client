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

package sample.gbase.cmdline;

import com.google.api.gbase.client.FeedURLFactory;
import com.google.api.gbase.client.GoogleBaseService;
import com.google.gdata.client.Service;
import com.google.gdata.util.AuthenticationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * One command that is part of CustomerTool.
 * This class contains code that is common to all
 * commands. It deals, in particular, with the creation
 * of the GData service object.
 */
abstract class Command {

  /**
   * URL of the google authentication server to use for log in.
   */
  private static final String DEFAULT_AUTH_HOSTNAME = "www.google.com";

  /**
   * Username for google base (e-mail address).
   */
  protected String username;

  /**
   * The user's password.
   */
  protected String password;

  /**
   * Base url of the google base server.
   */
  protected FeedURLFactory urlFactory = FeedURLFactory.getDefault();

  /**
   * Base url of the google authentication server.
   */
  private String authenticationServer = DEFAULT_AUTH_HOSTNAME;

  /**
   * Protocol (http or https) to use to connect to the authentication server.
   */
  private String authenticationProtocol = "https";
  
  /**
   * Developer key used for identification against the Google Base data API 
   * servers.
   */
  private String key;

  /**
   * Enables dry-run mode for edit operations.
   */
  private boolean dryRun;

  /**
   * Executes the command.
   *
   * Call this method only after setting the username and password.
   */
  public abstract void execute() throws Exception;

  /**
   * Sets the username, which is required for {@link #execute()} to work.
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Sets the password, which is required for {@link #execute()} to work.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Sets the Url of the google base server to connect to.
   */
  public void setGoogleBaseServerUrl(String url) throws MalformedURLException {
    this.urlFactory = new FeedURLFactory(url);
  }

  /**
   * Sets the name of the google authentication server to connect to.
   */
  public void setAuthenticationServerUrl(String urlString)
      throws MalformedURLException {
    URL url = new URL(urlString);
    this.authenticationProtocol = url.getProtocol();
    this.authenticationServer = url.getHost();
    if (url.getPort() != -1) {
      this.authenticationServer += ":" + url.getPort();
    }
  }
  
  public void setKey(String key) {
    this.key = key;
  }

  /**
   * Makes sure username and password have been set.
   */
  public boolean hasAllIdentificationInformation() {
    return username != null && password != null;
  }

  /**
   * Creates the service and sets the username and password for
   * authentication.
   *
   * @return the GData service object to use
   * @throws com.google.gdata.util.AuthenticationException
   *  if authentication failed
   */
  protected GoogleBaseService createService()
      throws AuthenticationException {
    GoogleBaseService service = 
        new GoogleBaseService("Google.-CustomerTool-1.0", 
                              key,
                              authenticationProtocol,
                              authenticationServer);                                                      
    service.setUserCredentials(username, password);
    return service;
  }

  /**
   * Gets the URL of the customer feed.
   *
   * The feed contains only the items uploaded by this
   * specific customer. This is also the feed that is
   * used to upload customer data.
   *
   * @return the url to the customer feed
   */
  protected URL getCustomerFeedURL() throws MalformedURLException {
    return urlFactory.getItemsFeedURL();
  }

  /**
   * Writes the response (XML feed) to standard output.
   */
  protected void outputRawResponse(Service.GDataRequest request)
      throws IOException {
    InputStream responseStream = request.getResponseStream();
    try {
      copyStreamContent(responseStream, System.out);
    } finally {
      responseStream.close();
    }
    System.out.println();
  }

  /**
   * Reads data from in input stream and write it into an
   * output stream.
   */
  protected void copyStreamContent(InputStream in, OutputStream out)
      throws IOException {
    byte[] buffer = new byte[1024];
    int l;
    while ( (l=in.read(buffer)) > 0 ) {
      out.write(buffer, 0, l);
    }
  }

  /**
   * Reads data from standard input and use it in the request.
   *
   * The data must be the XML feed appropriate for the command.
   * For update, get or insert it should be one Atom XML entry.
   */
  protected void inputRawRequest(Service.GDataRequest request)
      throws IOException {
    OutputStream outputStream = request.getRequestStream();
    try {
      copyStreamContent(System.in, outputStream);
    } finally {
      outputStream.close();
    }
  }

  /**
   * Puts the command in dry-run mode, in which nothing will
   * really happen on the server.
   *
   * @param dryRun
   */
  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  /**
   * Builds an edit URL from a string, adding the dry-run parameter
   * if necessary.
   *
   * This method is not applicable to query URLs, for which the dry-run
   * parameter is not supported.
   *
   * @param url the original url
   * @return the same URL with maybe some parameters
   * @throws MalformedURLException
   */
  protected URL fixEditUrl(URL url) throws MalformedURLException {
    return fixEditUrl(url.toExternalForm());
  }

  /**
   * Builds an edit URL from a string, adding the dry-run parameter
   * if necessary.
   *
   * This method is not applicable to query URLs, for which the dry-run
   * parameter is not supported.
   *
   * @param url the original url, as a string
   * @return the same URL with maybe some parameters
   * @throws MalformedURLException
   */
  protected URL fixEditUrl(String url) throws MalformedURLException {
    if (dryRun) {
      char separator = url.contains("?") ? '&' : '?';
      url = url + separator + "dry-run=true";
    }
    return new URL(url);
  }

}
