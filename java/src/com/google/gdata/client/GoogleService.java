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


package com.google.gdata.client;

import com.google.gdata.util.common.base.StringUtil;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.client.http.GoogleGDataRequest;
import com.google.gdata.client.http.GoogleGDataRequest.GoogleCookie;
import com.google.gdata.client.http.HttpGDataRequest.AuthToken;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.DateTime;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.RedirectRequiredException;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * The GoogleService class extends the basic GData {@link Service}
 * abstraction to add support for authentication.
 *
 * 
 */
public class GoogleService extends Service {

  // Name of client application accessing Google service
  private String applicationName;

  // Name of Google service being accessed
  private String serviceName;

  // Login name of the user
  private String username;

  // Password of the user
  private String password;

  // The Google domain name used for authentication
  private String domainName;

  // The protocol used for authentication
  private String loginProtocol;

  /**
   * The path name of the Google accounts management handler.
   */
  public static final String GOOGLE_ACCOUNTS_PATH = "/accounts";

  /**
   * The path name of the Google login handler.
   */
  public static final String GOOGLE_LOGIN_PATH = "/accounts/ClientLogin";


  /**
   * Authentication failed, invalid credentials presented to server.
   */
  public static class InvalidCredentialsException 
      extends AuthenticationException {
    public InvalidCredentialsException(String message) {
      super(message);
    }
  }


  /**
   * Authentication failed, account has been deleted.
   */
  public static class AccountDeletedException extends AuthenticationException {
    public AccountDeletedException(String message) {
      super(message);
    }
  }


  /**
   * Authentication failed, account has been disabled.
   */
  public static class AccountDisabledException extends AuthenticationException {
    public AccountDisabledException(String message) {
      super(message);
    }
  }


  /**
   * Authentication failed, account has not been verified.
   */
  public static class NotVerifiedException extends AuthenticationException {
    public NotVerifiedException(String message) {
      super(message);
    }
  }


  /**
   * Authentication failed, user did not agree to the terms of service.
   */
  public static class TermsNotAgreedException extends AuthenticationException {
    public TermsNotAgreedException(String message) {
      super(message);
    }
  }


  /**
   * Authentication failed, authentication service not available.
   */
  public static class ServiceUnavailableException extends
      AuthenticationException {
    public ServiceUnavailableException(String message) {
      super(message);
    }
  }


  /**
   * Authentication failed, CAPTCHA requires answering.
   */
  public static class CaptchaRequiredException extends AuthenticationException {

    private String captchaUrl;
    private String captchaToken;

    public CaptchaRequiredException(String message,
                                    String captchaUrl,
                                    String captchaToken) {
      super(message);
      this.captchaToken = captchaToken;
      this.captchaUrl = captchaUrl;
    }

    public String getCaptchaToken() { return captchaToken; }
    public String getCaptchaUrl() { return captchaUrl; }
  }


  /**
   * Authentication failed, the token's session has expired.
   */
  public static class SessionExpiredException extends AuthenticationException {
    public SessionExpiredException(String message) {
      super(message);
    }
  }


  /**
   * The UserToken encapsulates the token retrieved as a result of
   * authenticating to Google using a user's credentials.
   */
  public static class UserToken implements AuthToken {

    private String token;

    public UserToken(String token) {
      this.token = token;
    }

    /**
     * Returns an authorization header to be used for a HTTP request
     * for the respective authentication token.
     *
     * @param requestUrl the URL being requested
     * @param requestMethod the HTTP method of the request
     * @return the "Authorization" header to be used for the request
     */
    public String getAuthorizationHeader(URL requestUrl,
                                         String requestMethod) {
      return "GoogleLogin auth=" + token;
    }
  }


  /**
   * Encapsulates the token used by web applications to login on behalf of
   * a user.
   */
  public static class AuthSubToken implements AuthToken {

    private String token;
    private PrivateKey key;

    public AuthSubToken(String token, PrivateKey key) {
      this.token = token;
      this.key = key;
    }

    /**
     * Returns an authorization header to be used for a HTTP request
     * for the respective authentication token.
     *
     * @param requestUrl the URL being requested
     * @param requestMethod the HTTP method of the request
     * @return the "Authorization" header to be used for the request
     */
    public String getAuthorizationHeader(URL requestUrl,
                                         String requestMethod) {
      try {
        return AuthSubUtil.formAuthorizationHeader(token, key, requestUrl,
                                                   requestMethod);
      } catch (GeneralSecurityException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
  }


  /**
   * Constructs a GoogleService instance connecting to the service with name
   * {@code serviceName} for an application with the name
   * {@code applicationName}. The default domain (www.google.com) will be
   * used to authenticate.
   *
   * @param serviceName     the name of the Google service to which we are
   *                        connecting. Sample names of services might include
   *                        "cl" (Calendar), "mail" (GMail), or
   *                        "blogger" (Blogger)
   * @param applicationName the name of the client application accessing the
   *                        service.  Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   */
  public GoogleService(String serviceName,
                       String applicationName) {

    this(serviceName, applicationName, "https", "www.google.com");
  }


  /**
   * Constructs a GoogleService instance connecting to the service with name
   * {@code serviceName} for an application with the name
   * {@code applicationName}.  The service will authenticate at the provided
   * {@code domainName}.
   *
   * @param serviceName     the name of the Google service to which we are
   *                        connecting. Sample names of services might include
   *                        "cl" (Calendar), "mail" (GMail), or
   *                        "blogger" (Blogger)
   * @param applicationName the name of the client application accessing the
   *                        service. Application names should preferably have
   *                        the format [company-id]-[app-name]-[app-version].
   *                        The name will be used by the Google servers to
   *                        monitor the source of authentication.
   * @param protocol        name of protocol to use for authentication
   *                        ("http"/"https")
   * @param domainName      the name of the domain hosting the login handler
   */
  public GoogleService(String serviceName,
                       String applicationName,
                       String protocol,
                       String domainName) {

    this.serviceName = serviceName;
    this.applicationName = applicationName;
    this.domainName = domainName;
    this.loginProtocol = protocol;
    requestFactory = new GoogleGDataRequest.Factory();

    if (applicationName != null) {
      requestFactory.setHeader("User-Agent",
          applicationName + " " + getServiceVersion());
    } else {
      requestFactory.setHeader("User-Agent", getServiceVersion());
    }
  }


  /**
   * Sets the credentials of the user to authenticate requests to the server.
   *
   * @param username the name of the user (an email address)
   * @param password the password of the user
   * @throws AuthenticationException if authentication failed.
   */
  public void setUserCredentials(String username, String password)
      throws AuthenticationException {

    setUserCredentials(username, password, null, null);
  }


  /**
   * Sets the credentials of the user to authenticate requests to the server.
   * A CAPTCHA token and a CAPTCHA answer can also be optionally provided
   * to authenticate when the authentication server requires that a
   * CAPTCHA be answered.
   *
   * @param username the name of the user (an email id)
   * @param password the password of the user
   * @param captchaToken the CAPTCHA token issued by the server
   * @param captchaAnswer the answer to the respective CAPTCHA token
   * @throws AuthenticationException if authentication failed
   */
  public void setUserCredentials(String username,
                                 String password,
                                 String captchaToken,
                                 String captchaAnswer)
      throws AuthenticationException {

    this.username = username;
    this.password = password;
    String token = getAuthToken(username, password, captchaToken, captchaAnswer,
                                serviceName, applicationName);
    setUserToken(token);
  }

  /**
   * Sets the AuthToken that should be used to authenticate requests to the
   * server. This is useful if the caller has some other way of accessing the
   * AuthToken, versus calling getAuthToken with credentials to request the
   * AuthToken using ClientLogin.
   *
   * @param token the AuthToken in ascii form
   */
  public void setUserToken(String token) {
    GoogleGDataRequest.Factory factory =
      (GoogleGDataRequest.Factory) requestFactory;
    factory.setAuthToken(new UserToken(token));

    // Flush any cookies that might contain session info for the previous user
    cookies.clear();
  }


  /**
   * Sets the AuthSub token to be used to authenticate a user.
   *
   * @param token the AuthSub token retrieved from Google
   */
  public void setAuthSubToken(String token) {
    setAuthSubToken(token, null);
  }


  /**
   * Sets the AuthSub token to be used to authenticate a user.  The token
   * will be used in secure-mode, and the provided private key will be used
   * to sign all requests.
   *
   * @param token the AuthSub token retrieved from Google
   * @param key the private key to be used to sign all requests
   */
  public void setAuthSubToken(String token,
                              PrivateKey key) {

    GoogleGDataRequest.Factory factory =
      (GoogleGDataRequest.Factory) requestFactory;
    factory.setAuthToken(new AuthSubToken(token, key));

    // Flush any cookies that might contain session info for the previous user
    cookies.clear();
  }

  /**
   * Retrieves the authentication token for the provided set of credentials.
   *
   * @param username the name of the user (an email address)
   * @param password the password of the user
   * @param captchaToken the CAPTCHA token if CAPTCHA is required (Optional)
   * @param captchaAnswer the respective answer of the CAPTCHA token (Optional)
   * @param serviceName the name of the service to which a token is required
   * @param applicationName the application requesting the token
   * @return the token
   * @throws AuthenticationException if authentication failed
   */
  public String getAuthToken(String username,
                             String password,
                             String captchaToken,
                             String captchaAnswer,
                             String serviceName,
                             String applicationName)
      throws AuthenticationException {

    Map<String, String> params = new HashMap<String, String>();
    params.put("Email", username);
    params.put("Passwd", password);
    params.put("source", applicationName);
    params.put("service", serviceName);
    params.put("accountType", "HOSTED_OR_GOOGLE");

    if (captchaToken != null) {
      params.put("logintoken", captchaToken);
    }
    if (captchaAnswer != null) {
      params.put("logincaptcha", captchaAnswer);
    }
    String postOutput;
    try {
      URL url = new URL(loginProtocol + "://" + domainName + GOOGLE_LOGIN_PATH);
      postOutput = makePostRequest(url, params);
    } catch (IOException e) {
      AuthenticationException ae =
        new AuthenticationException("Error connecting with login URI");
      ae.initCause(e);
      throw ae;
    }

    // Parse the output
    Map<String, String> tokenPairs =
      StringUtil.string2Map(postOutput.trim(), "\n", "=", true);
    String token = tokenPairs.get("Auth");
    if (token == null) {
      throw getAuthException(tokenPairs);
    }
    return token;
  }


  /**
   * Makes a HTTP POST request to the provided {@code url} given the
   * provided {@code parameters}.  It returns the output from the POST
   * handler as a String object.
   *
   * @param url the URL to post the request
   * @param parameters the parameters to post to the handler
   * @return the output from the handler
   * @throws IOException if an I/O exception occurs while creating, writing,
   *                     or reading the request
   */
  public static String makePostRequest(URL url,
                                       Map<String, String> parameters)
      throws IOException {

    // Open connection
    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

    // Set properties of the connection
    urlConnection.setDoInput(true);
    urlConnection.setDoOutput(true);
    urlConnection.setUseCaches(false);
    urlConnection.setRequestProperty("Content-Type",
                                     "application/x-www-form-urlencoded");

    // Form the POST parameters
    StringBuilder content = new StringBuilder();
    boolean first = true;
    for (Map.Entry<String, String> parameter : parameters.entrySet()) {
      if (!first) {
        content.append("&");
      }
      content.append(
          URLEncoder.encode(parameter.getKey(), "UTF-8")).append("=");
      content.append(URLEncoder.encode(parameter.getValue(), "UTF-8"));
      first = false;
    }

    OutputStream outputStream = null;
    try {
      outputStream = urlConnection.getOutputStream();
      outputStream.write(content.toString().getBytes("utf-8"));
      outputStream.flush();
    } finally {
      if (outputStream != null) {
        outputStream.close();
      }
    }

    // Retrieve the output
    InputStream inputStream = null;
    StringBuilder outputBuilder = new StringBuilder();
    try {
      int responseCode = urlConnection.getResponseCode();
      if (responseCode == HttpURLConnection.HTTP_OK) {
        inputStream = urlConnection.getInputStream();
      } else {
        inputStream = urlConnection.getErrorStream();
      }

      String string;
      if (inputStream != null) {
        BufferedReader reader =
          new BufferedReader(new InputStreamReader(inputStream));
        while (null != (string = reader.readLine())) {
          outputBuilder.append(string).append('\n');
        }
      }
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
    return outputBuilder.toString();
  }


  /**
   * Returns the respective {@code AuthenticationException} given the return
   * values from the login URI handler.
   *
   * @param pairs name/value pairs returned as a result of a bad authentication
   * @return the respective {@code AuthenticationException} for the given error
   */
  private AuthenticationException getAuthException(Map<String, String> pairs) {

    String errorName = pairs.get("Error");

    if ("BadAuthentication".equals(errorName)) {
      return new InvalidCredentialsException("Invalid credentials");

    } else if ("AccountDeleted".equals(errorName)) {
      return new AccountDeletedException("Account deleted");

    } else if ("AccountDisabled".equals(errorName)) {
      return new AccountDisabledException("Account disabled");

    } else if ("NotVerified".equals(errorName)) {
      return new NotVerifiedException("Not verified");

    } else if ("TermsNotAgreed".equals(errorName)) {
      return new TermsNotAgreedException("Terms not agreed");

    } else if ("ServiceUnavailable".equals(errorName)) {
      return new ServiceUnavailableException("Service unavailable");

    } else if ("CaptchaRequired".equals(errorName)) {

      String captchaPath = pairs.get("CaptchaUrl");
      StringBuilder captchaUrl = new StringBuilder();
      captchaUrl.append(loginProtocol).append("://").append(domainName)
                .append(GOOGLE_ACCOUNTS_PATH).append('/').append(captchaPath);
      return new CaptchaRequiredException("Captcha required",
                                          captchaUrl.toString(),
                                          pairs.get("CaptchaToken"));

    } else {
      return new AuthenticationException("Error authenticating " +
                                         "(check service name)");
    }
  }


  /**
   * Stores the set of GoogleCookies that have been issued during previous
   * requests.
   */
  protected Set<GoogleCookie> cookies = new HashSet<GoogleCookie>();


  /**
   * Indicates whether the GoogleService should implement a local cache
   * for cookies returned by the GData service.  If true (the default
   * value), then the GoogleService instance will maintain the cache and
   * return cookies on subsequent requests.
   */
  protected boolean handlesCookies = true;


  /**
   * Enables or disables cookie handling.
   */
  public void setHandlesCookies(boolean handlesCookies) {
    this.handlesCookies = handlesCookies;
  }


  /**
   * Returns  {@code true} if the GoogleService is handling cookies.
   */
  public boolean handlesCookies() { return handlesCookies; }


  /**
   * Adds a new GoogleCookie instance to the cache.
   */
  public void addCookie(GoogleCookie cookie) {
    assert handlesCookies;

    // Remove any previous value of this cookie, since expiration and
    // and cookie value are not part of the hashCode/equals algorithm
    // for GoogleCookie.  This ensures that we always replace with the
    // most recently received cookie state.
    cookies.remove(cookie);
    cookies.add(cookie);
  }


  /**
   * Returns the set of associated cookies returned by previous requests.
   */
  public Set<GoogleCookie> getCookies() {

    // Lazy flushing of expired cookies
    Iterator<GoogleCookie> cookieIter = cookies.iterator();
    while (cookieIter.hasNext()) {
      GoogleCookie cookie = cookieIter.next();
      if (cookie.hasExpired())
        cookieIter.remove();
    }
    return cookies;
  }


  @Override
  public void setRequestFactory(GDataRequestFactory requestFactory) {

    if (!(requestFactory instanceof GoogleGDataRequest.Factory)) {
      throw new IllegalArgumentException("Invalid factory");
    }
    this.requestFactory = requestFactory;
  }


  @Override
  public GDataRequest createRequest(GDataRequest.RequestType type,
                                       URL requestUrl,
                                       ContentType contentType)
      throws IOException, ServiceException {
    GoogleGDataRequest request =
      (GoogleGDataRequest) super.createRequest(type, requestUrl, contentType);
    request.setService(this);
    return request;
  }


  @Override
  public <E extends BaseEntry<?>> E getEntry(URL entryUrl,
                                          Class<E> entryClass,
                                          DateTime ifModifiedSince)
      throws IOException, ServiceException {

    try {
      return super.getEntry(entryUrl, entryClass, ifModifiedSince);
    } catch (RedirectRequiredException e) {
      entryUrl = handleRedirectException(e);
    } catch (SessionExpiredException e) {
      handleSessionExpiredException(e);
    }

    return super.getEntry(entryUrl, entryClass, ifModifiedSince);
  }


  @Override
  public <E extends BaseEntry<?>> E update(URL entryUrl, E entry)
      throws IOException, ServiceException {

    try {
      return super.update(entryUrl, entry);
    } catch (RedirectRequiredException e) {
      entryUrl = handleRedirectException(e);
    } catch (SessionExpiredException e) {
      handleSessionExpiredException(e);
    }

    return super.update(entryUrl, entry);
  }


  @Override
  public <E extends BaseEntry<?>> E insert(URL feedUrl, E entry)
      throws IOException, ServiceException {

    try {
      return super.insert(feedUrl, entry);
    } catch (RedirectRequiredException e) {
      feedUrl = handleRedirectException(e);
    } catch (SessionExpiredException e) {
      handleSessionExpiredException(e);
    }

    return super.insert(feedUrl, entry);
  }


  @Override
  public <F extends BaseFeed<?, ?>> F getFeed(URL feedUrl,
                                              Class<F> feedClass,
                                              DateTime ifModifiedSince)
      throws IOException, ServiceException {

    try {
      return super.getFeed(feedUrl, feedClass, ifModifiedSince);
    } catch (RedirectRequiredException e) {
      feedUrl = handleRedirectException(e);
    } catch (SessionExpiredException e) {
      handleSessionExpiredException(e);
    }

    return super.getFeed(feedUrl, feedClass, ifModifiedSince);
  }


  @Override
  public void delete(URL entryUrl) throws IOException, ServiceException {

    try {
      super.delete(entryUrl);
      return;
    } catch (RedirectRequiredException e) {
      entryUrl = handleRedirectException(e);
    } catch (SessionExpiredException e) {
      handleSessionExpiredException(e);
    }

    super.delete(entryUrl);
  }


  /**
   * Handles a session expired exception by obtaining a new authentication
   * token and updating the token in the request factory.
   */
  private void handleSessionExpiredException(
      SessionExpiredException sessionExpired)
      throws SessionExpiredException, AuthenticationException {

    if (username != null && password != null) {
      String token = getAuthToken(username, password, null, null, serviceName,
                                  applicationName);
      GoogleGDataRequest.Factory factory =
        (GoogleGDataRequest.Factory) requestFactory;
      factory.setAuthToken(new UserToken(token));
    } else {
      throw sessionExpired;
    }
  }


  /**
   * Handles a redirect exception by generating the new URL to use for the
   * redirect.
   */
  private URL handleRedirectException(RedirectRequiredException redirect)
      throws ServiceException {
    try {
      return new URL(redirect.getRedirectLocation());
    } catch (MalformedURLException e) {
      throw new ServiceException("Invalid redirected-to URL - "
                                 + redirect.getRedirectLocation());
    }
  }
}
