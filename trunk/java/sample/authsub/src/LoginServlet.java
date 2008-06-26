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

import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.client.http.AuthSubUtil;
import com.google.gdata.util.common.util.Base64;

import java.io.IOException;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Logging into this application is trivial and just consists of visiting
 * this servlet.  This servlet will set an authentication cookie for the
 * user and redirect to the page to authorize to Google.  Typically, the
 * user will be authenticated to the server via the login cookie prior to
 * accessing this servlet.
 *
 * 
 */
public class LoginServlet extends HttpServlet {

  // On successfully acquiring a token, the servlet will redirect the user to
  // the following next URL
  /*package*/ static final String NEXT_URL = "/main.jsp";

  private static final SecureRandom srng = new SecureRandom();

  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    // State to indicate if the user parameters have changed.
    boolean stateChanged = false;

    // Retrieve user specified hosted domain.
    boolean useDefaultDomain = 
        Boolean.parseBoolean(req.getParameter("defaultdomain"));
    boolean useSsl = 
        Boolean.parseBoolean(req.getParameter("secure"));
    String userDomain = req.getParameter("domain");

    // Save any persistent data to user session
    String calendarFeedRootUrl =  CalendarService.CALENDAR_ROOT_URL;
    if (useSsl && !calendarFeedRootUrl.startsWith("https")) {
      calendarFeedRootUrl = calendarFeedRootUrl.replaceFirst("http", "https");
    }

    HttpSession userSession = req.getSession(true);

    String currentRootUrl = 
        (String) userSession.getAttribute("feedRootUrl");
    String currentDomain = 
        (String) userSession.getAttribute("userDomain");

    if ((currentRootUrl != null && !currentRootUrl.equals(calendarFeedRootUrl))
        || (currentDomain != null && !currentDomain.equals(userDomain))) {
      stateChanged = true;
    }

    userSession.putValue("feedRootUrl", calendarFeedRootUrl);
    userSession.putValue("userDomain", userDomain);

    String authSubToken = null;
    String principal =
      Utility.getCookieValueWithName(req.getCookies(), Utility.LOGIN_COOKIE_NAME);
    if (!stateChanged && (principal != null)) {
      authSubToken = TokenManager.retrieveToken(principal);
    }

    // Form continue URL
    StringBuffer continueUrl = req.getRequestURL();
    int index = continueUrl.lastIndexOf("/");
    continueUrl.delete(index, continueUrl.length());

    // If the user doesn't have an AuthSub token yet, redirect the user to
    // the Google page to request an AuthSub token. Otherwise redirect to
    // the main page
    if (authSubToken == null) {
      continueUrl.append("/HandleTokenServlet");

      // Check whether to use https for authentication
      boolean secure = (Utility.getPrivateKey() != null);

      String authSubLogin;
      if (useDefaultDomain) {
        authSubLogin = AuthSubUtil.getRequestUrl(continueUrl.toString(),
                                  calendarFeedRootUrl,
                                  secure,
                                  true /*session*/);
      } else {
        authSubLogin = AuthSubUtil.getRequestUrl(req.getParameter("domain"),
                                  continueUrl.toString(),
                                  calendarFeedRootUrl,
                                  secure,
                                  true /*session*/);
      }

      // Set "authentication" cookie.  Typically, a user would have an
      // login-cookie for the web service which should be associated to the
      // AuthSub token retrieved from Google.  For this example, a random
      // authentication cookie is assigned.
      byte[] randomBytes = new byte[12];
      srng.nextBytes(randomBytes);
      String cookieValue = Base64.encodeWebSafe(randomBytes, false);
      resp.addCookie(new Cookie(Utility.LOGIN_COOKIE_NAME, cookieValue));
      resp.sendRedirect(authSubLogin);
    } else {
      continueUrl.append(NEXT_URL);
      resp.sendRedirect(continueUrl.toString());
    }
  }
}
