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

package sample.gbase.recipe;

import com.google.api.gbase.client.FeedURLFactory;
import com.google.api.gbase.client.GoogleBaseService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.util.AuthenticationException;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides an authenticated GoogleBaseService
 * to the servlet that will process the request.
 */
public class AuthenticationFilter implements Filter {

  /**
   * The request attribute that contains the authenticated service.
   */
  static final String SERVICE_ATTRIBUTE = "googleBaseService";

  /**
   * The parameter used by AuthSub to specify the authentication token. 
   * Also used as the name of the http session attribute that contains
   * the session token.
   */
  static final String TOKEN_PARAMETER = "token";

  static final String TOKEN_COOKIE_NAME = "AuthSubSessionToken";

  static final String DEFAULT_AUTHSUB_PROTOCOL = "https";
  static final String DEFAULT_AUTHSUB_HOSTNAME = "www.google.com";

  protected String authsubProtocol;
  protected String authsubHostname;

  protected FeedURLFactory urlFactory;

  protected String applicationName;
  
  /**
   * Developer key, used for identification against the Google Base data
   * API servers.
   */
  protected String key;
  
  private ServletContext servletContext;

  public void init(FilterConfig filterConfig) throws ServletException {
    servletContext = filterConfig.getServletContext();
    key = servletContext.getInitParameter(RecipeUtil.DEVELOPER_KEY_PARAMETER);
    if (key == null || "".equals(key.trim())) {
      String errorMessage = "No developer key specified.\n Please edit " +
            "web.xml and add your developer key in the \"key\" context " +
            "parameter. \n You can obtain a developer key at: \n\t" +
            "http://code.google.com/api/base/signup.html";
      System.err.println(errorMessage);
      throw new ServletException(errorMessage);
    }
    
    urlFactory = (FeedURLFactory) 
        servletContext.getAttribute(RecipeListener.FEED_URL_FACTORY_ATTRIBUTE);
    authsubProtocol = filterConfig.getInitParameter("authsubProtocol");
    if (authsubProtocol == null) {
      authsubProtocol = DEFAULT_AUTHSUB_PROTOCOL;
    }
    authsubHostname = filterConfig.getInitParameter("authsubHostname");
    if (authsubHostname == null) {
      authsubHostname = DEFAULT_AUTHSUB_HOSTNAME;
    }
  }

  public void destroy() {
    servletContext = null;
    urlFactory = null;
    applicationName = null;
    authsubProtocol = null;
    authsubHostname = null;
  }

  /**
   * Starts or stops an authenticated session, depending on the value
   * of the {@value #TOKEN_PARAMETER} parameter, or provides the servlets 
   * with an authenticated 
   * {@link com.google.api.gbase.client.GoogleBaseService GoogleBaseService},
   * during an authenticated session.
   */
  public void doFilter(ServletRequest request,
                       ServletResponse response,
                       FilterChain filterChain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    String oneTimeToken = httpRequest.getParameter(TOKEN_PARAMETER);
    String sessionToken = getSessionTokenCookie(httpRequest);

    if (oneTimeToken != null) {
      if ("".equals(oneTimeToken)) {
        // Revoke the session token
        if (sessionToken != null) {
          stopAuthenticatedSession(httpRequest, httpResponse, sessionToken);
          return;
        }
      } else {
        // Convert the token to a session token and keep it
        try {
          startAuthenticatedSession(httpRequest, httpResponse, oneTimeToken);
          return;
        } catch (GeneralSecurityException e) {
          throw new ServletException(e);
        } catch (AuthenticationException e) {
          // Log and then continue as if this token was not there.
          // (It was probably bookmarked.) 
          servletContext.log("Invalid one-time token", e);
        }
      }
    }

    // Request a new token if we don't have one at this point
    if (sessionToken == null) {
      redirectToAuthSub(httpRequest, httpResponse);
      return;
    }

    // Create a service that authenticates using the session token
    GoogleBaseService service = new GoogleBaseService(
        applicationName, key, authsubProtocol, authsubHostname);
    service.setAuthSubToken(sessionToken);

    // Make the service available to the servlet
    httpRequest.setAttribute(SERVICE_ATTRIBUTE, service);
    
    // Execute the servlet
    try {
      filterChain.doFilter(request, response);
    } catch(ServletException e) {
      Throwable cause = e.getRootCause();
      if (cause instanceof AuthenticationException &&
          !response.isCommitted()) {
        // Token has been revoked. Re-run AuthSub.
        redirectToAuthSub(httpRequest, httpResponse);
      } else {
        // Let the exception be handled as usual.
        throw e;
      }
    }
  }

  /**
   * Gets the AuthSub session token from a cookie. 
   * 
   * @param httpRequest
   * @return session token or null
   */
  String getSessionTokenCookie(HttpServletRequest httpRequest) {
    Cookie[] cookies = httpRequest.getCookies();
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals(TOKEN_COOKIE_NAME)) {
        return cookie.getValue();
      }
    }
    return null;
  }

  /**
   * Revokes the specified session token and redirects the browser to
   * the context of the request.
   * 
   * @param request
   * @param response
   * @param sessionToken a session token
   * @throws IOException
   * @throws ServletException
   */
  private void stopAuthenticatedSession(HttpServletRequest request,
                                        HttpServletResponse response,
                                        String sessionToken)
      throws IOException, ServletException {
    if (sessionToken != null) {
      revokeSessionToken(sessionToken);
      clearSessionTokenCookie(request, response);
    }
    String url = request.getContextPath();
    response.sendRedirect(response.encodeRedirectURL(url));
  }

  /**
   * Force cookie expiration by sending an expired cookie to
   * the browser.
   * 
   * @param request
   * @param response
   */
  private void clearSessionTokenCookie(HttpServletRequest request,
                                       HttpServletResponse response) 
      throws ServletException {
    response.addCookie(newExpiredSessionTokenCookie(request));
  }

  private Cookie newExpiredSessionTokenCookie(HttpServletRequest request)
      throws ServletException {
    Cookie cookie = newSessionTokenCookie(request, "");
    cookie.setMaxAge(0);
    return cookie;
  }

  /**
   * Explicitely revoke the session token. 
   * 
   * @param token
   * @throws IOException
   * @throws ServletException
   */
  protected void revokeSessionToken(String token) throws IOException, ServletException
  {
    try {
      AuthSubUtil.revokeToken(authsubProtocol, authsubHostname, token, null);
    } catch (AuthenticationException e) {
      throw new ServletException(e);
    } catch (GeneralSecurityException e) {
      throw new ServletException(e);
    }
  }

  /**
   * Exchanges the single use token for a session token and
   * redirects the browser to the same address with the
   * token specification part removed.
   * 
   * @param request
   * @param response
   * @param oneTimeToken a single use AuthSub token
   * @throws IOException
   * @throws GeneralSecurityException
   * @throws AuthenticationException if the one-time token is invalid
   */
  private void startAuthenticatedSession(HttpServletRequest request,
                                         HttpServletResponse response,
                                         String oneTimeToken)
      throws IOException, GeneralSecurityException, AuthenticationException,
      ServletException {
    String sessionToken = exchangeForSessionToken(oneTimeToken);
    
    // Store the authentication token in a cookie
    response.addCookie(newSessionTokenCookie(request, sessionToken));
    
    // Redirect the browser to the same address
    // with the "token=value" part removed from the query string
    StringBuffer url = request.getRequestURL();    
    String queryString = request.getQueryString();
    if (queryString != null) {
      queryString = queryString.replaceFirst("token=[^&]*&?", "");
      if (queryString.length() > 0) {
        url.append("?").append(queryString);
      }
    }
    response.sendRedirect(response.encodeRedirectURL(url.toString()));
  }

  protected Cookie newSessionTokenCookie(HttpServletRequest request,
                                         String sessionToken) 
      throws ServletException {
    Cookie cookie = new Cookie(TOKEN_COOKIE_NAME, sessionToken);
    // AuthSub session tokens effectively don't expire. Hang on to it.
    // If the AuthSub token is revoked, the filter will request
    // a new token.
    cookie.setMaxAge(365*24*60*60);
    cookie.setPath(request.getContextPath() + "/");
    try {
      cookie.setDomain(new URL(request.getRequestURL().toString()).getHost());  
    } catch(MalformedURLException e) {
      throw new ServletException(e);
    }
    
    // Cookie domain set automatically by the server.
    return cookie;
  }

  /**
   * Converts a one-time token into a reusable session token.
   * @param oneTimeToken
   * @throws IOException
   */
  protected String exchangeForSessionToken(String oneTimeToken)
      throws IOException, GeneralSecurityException, AuthenticationException {
    return AuthSubUtil.exchangeForSessionToken(authsubProtocol,
                                               authsubHostname,
                                               oneTimeToken,
                                               null);
  }

  /**
   * Redirects to the AuthSub authentication page.
   * 
   * @param request
   * @param response
   * @throws IOException
   */
  private void redirectToAuthSub(HttpServletRequest request,
                                 HttpServletResponse response)
      throws IOException {
    StringBuffer next = request.getRequestURL();
    String queryString = request.getQueryString();
    if (queryString != null && !"".equals(queryString) ) {
        next.append("?").append(queryString);
    }
    String scope = new URL(urlFactory.getBaseURL(), "feeds").toExternalForm();
    String url = AuthSubUtil.getRequestUrl(authsubProtocol,
                                           authsubHostname,
                                           next.toString(),
                                           scope,
                                           false,
                                           true);
    response.sendRedirect(response.encodeRedirectURL(url));
  }

}
