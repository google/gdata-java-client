AuthSub Java Sample - README.txt
--------------------------------

The AuthSub sample is a simple Tomcat web-application that demonstrates usage
of the Google Authentication Proxy Interface, AuthSub, to access a user's
private Calendar feed.  AuthSub enables a web-application to access a user's
data without ever handling the user's account login information.  The provided
sample will retrieve an AuthSub token for the current user, authenticate to
Calendar using the AuthSub token, and retrieve the private calendar feed.


Overview of the sample
----------------------
The sample consists of two main parts.  The Java files in src/ handle the
retrieval, (basic) storage, and usage of the AuthSub token for authentication.
The javascript files in the web/ directory handle the parsing and rendering of
the retrieved Calendar data.

The Java files mainly use the com.google.gdata.client.http.AuthSubUtil utility
class to interface with AuthSub.  LoginServlet.java forms the URL to redirect
to the Google Accounts page to request an AuthSub token.
HandleTokenServlet.java handles the retrieval of the AuthSub token when Google
Accounts redirects back to the web application.  RetrieveFeedServlet.java is a
simple proxy to retrieve the requested feed using the AuthSub token for
authentication.  (The proxy is a solution for the same origin policy of
Javascript).


Deploying the sample web application
------------------------------------
The sample web application can be easily deployed on a running instance of
Tomcat.  The dist/authsub_sample.war web archive file can just be dropped into
the webapps/ directory of your Tomcat installation.  Tomcat should
automatically deploy the web application.  The application should be accessible
at the '/authsub_sample' path of your Tomcat server
(eg. 'http://localhost:8080/authsub_sample').

Note: If the application needs to be rebuilt, the servlet.jar property defined
in the build-samples/build.properties file should be updated to point to the
jar file with the servlet definitions.


Securing against URL command attacks
------------------------------------
Due to the same origin policy of Javascript, a basic proxy is required to
retrieve the private Calendar feed.  (The proxy's functionality is implemented
in src/RetrieveFeedServlet.java.)  The requested URL is specified as a query
parameter.  The proxy will use the AuthSub token of the respective user to
retrieve the feed at the provided URL.  The proxy also takes a token as another
query parameter to ensure that the request is from an authorized host.

A URL command attack is a cross site scripting vulnerability. It is applicable
when cookies are used for authentication and user commands are done through
HTTP requests (GET or POST).  This attack is illustrated through the following
example. Assume goodsite.com and evilsite.com. Assume goodsite.com offers a URL
of the following form http://goodsite.com/addtophonebook?email=hello@gmail.com
that adds hello@gmail.com to the current logged in user's phonebook.
An attack may look like this:

1. User logs into goodsite.com and gets an login cookie
2. User goes to evilsite.com
3. On evilsite.com there is an HTML element with a URL source to some
   command for goodsite.com (perhaps through an invisible image) that accesses
   http://goodsite.com/addtophonebook?email=viagra@spam.com
4. The browser will fetch the URL and the login cookie will go along with
   it
5. The user's account is now modified by an evil site

Thus to protect the GData URL proxy, we require requests to the GData proxy
servlet to contain a secure token that is a hash of the user's login cookie
issued by the web service and the URL to be accessed.  Since evilsite.com does
not have access to the login cookie, it will not be able to generate a valid
secure token for this user.

The token used to secure the URL has the following format:
token = HMAC<sub>SHA1(login-cookie)</sub>({data})
   where {data} is a string formed in the following manner:
   data = http-url SP http-method SP timestamp
          SP        is a single ASCII space character.
          http-url  is the GData feed URl being requested.
          timestamp is an integer representing the time
                    the request was sent, in seconds since Jan 1, 1970 UTC,
                    formatted as an ASCII string (in decimal).

The token used in the URL will be Base64 encoded.  The above functionality is
implemented in src/SecureUrl.java.  The token is checked in
src/RetrieveFeedServlet.java and generated through JSP in web/main.jsp.


Limitations
-----------
The sample shows the basic structure of how a server that uses AuthSub should
be setup.  The current sample may not work well with secure AuthSub with
signatures if the Calendar server sends redirects with the gsessionid in the
URL.  In this case, the client should handle the 302, recalculate the signature
for the new URL, and then issue the request.
