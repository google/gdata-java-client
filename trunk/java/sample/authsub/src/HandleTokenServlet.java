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


package sample.authsub.src;

import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.util.AuthenticationException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles the processing of an AuthSub token.
 * <p>
 * The user will login to the Google account and lend permission for
 * this service to impersonate the user.  Upon completion of the login
 * and permission-grant, the user will be redirected to this servlet
 * with the token in the URL.
 *
 * 
 */
public class HandleTokenServlet extends HttpServlet {

  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // Retrieve the AuthSub token assigned by Google
    String token = AuthSubUtil.getTokenFromReply(req.getQueryString());
    if (token == null) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "No token specified.");
      return;
    }

    // Exchange the token for a session token
    String sessionToken;
    try {
      sessionToken =
        AuthSubUtil.exchangeForSessionToken(token,
                                            Utility.getPrivateKey());
    } catch (IOException e1) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                     "Exception retrieving session token.");
      return;
    } catch (GeneralSecurityException e1) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                     "Security error while retrieving session token.");
      return;
    } catch (AuthenticationException e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                     "Server rejected one time use token.");
      return;
    }

    try {
      // Sanity checking usability of token
      Map<String, String> info =
        AuthSubUtil.getTokenInfo(sessionToken, Utility.getPrivateKey());
      for (Iterator<String> iter = info.keySet().iterator(); iter.hasNext();) {
        String key = iter.next();
        System.out.println("\t(key, value): (" + key + ", " + info.get(key)
                           + ")");
      }
    } catch (IOException e1) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                     "Exception retrieving info for session token.");
      return;
    } catch (GeneralSecurityException e1) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                     "Security error while retrieving session token info.");
      return;
    } catch (AuthenticationException e) {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                     "Auth error retrieving info for session token: " +
                     e.getMessage());
      return;
    }

    // Retrieve the authentication cookie to identify user
    String principal =
      Utility.getCookieValueWithName(req.getCookies(), Utility.LOGIN_COOKIE_NAME);
    if (principal == null) {
      resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
                     "Unidentified principal.");
      return;
    }

    // Store the token
    TokenManager.storeToken(principal, sessionToken);

    // Redirect to main.jsp where the token will be used
    StringBuffer continueUrl = req.getRequestURL();
    int index = continueUrl.lastIndexOf("/");
    continueUrl.delete(index, continueUrl.length());
    continueUrl.append(LoginServlet.NEXT_URL);
    resp.sendRedirect(continueUrl.toString());
  }
}
