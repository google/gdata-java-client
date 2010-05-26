/*
 * Copyright (c) 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.sample.googleapps;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthDomainWideDelegation;
import com.google.api.client.googleapis.auth.oauth.GoogleOAuthDomainWideDelegation.Url;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.InputStreamContent;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * This class can be used as a simple command-line utility to interact with a
 * Google Api that supports two-legged OAuth, such as the APIs for Google Apps.
 *
 * Often, the Server for the Protected Resource will have a means of authorizing
 * the OAuth client out-of-band, before the HTTP request is sent. This code
 * assumes the request it is generating has been approved.
 *
 * For more info, see
 * http://code.google.com/apis/accounts/docs/OAuth.html#GoogleAppsOAuth
 *
 * To run using ant rules:
 *
 * ant run -Dargs=" \ --consumer_key=myapp.com \
 * --consumer_secret=***************** \
 * --url=http://www.google.com/m8/feeds/contacts/default/full \
 * --user=user@somedomain.com \
 * --verbose"
 *
 * Alternatively, create a self-contained executable jar, then execute:
 *
 * java -jar 2locurl.jar \ --consumer_key=myapp.com \
 * --consumer_secret=***************** \
 * --url=http://www.google.com/m8/feeds/contacts/default/full \
 * --user=user@somedomain.com \
 * --verbose
 *
 * The default HTTP method is GET. To POST, specify --method=POST and add this
 * example POST body for contacts API to add a contact:
 *
 * body = "<atom:entry xmlns:atom='http://www.w3.org/2005/Atom' " +
 * "xmlns:gd='http://schemas.google.com/g/2005'> " +
 * "<atom:category scheme='http://schemas.google.com/g/2005#kind' " +
 * "term='http://schemas.google.com/contact/2008#contact' /> " +
 * "<atom:title type='text'>Elizabeth Bennet</atom:title> " +
 * "<gd:email rel='http://schemas.google.com/g/2005#home' " +
 * "address='liz@example.org' /> " + "</atom:entry>";
 *
 * If POSTing JSON, specify --content_type=application/json
 */
public class TwoLeggedOauthSample {

  private static final String DEFAULT_CONTENT_TYPE = "application/atom+xml";
  private static final HttpMethod DEFAULT_METHOD = HttpMethod.GET;
  private static final String APP_NAME = "google-two-legged-oauth-sample-1.0";
  private static String consumer_key;
  private static String consumer_secret;
  private static String url;
  private static String user;
  private static HttpMethod method;
  private static String content_type;
  private static String body = "";
  private static boolean verbose = false;

  /** Available HTTP method types */
  public enum HttpMethod {
    GET,
    POST,
    DELETE,
    PUT,
  }

  public static void main(String[] args) throws IOException {

    HttpResponse response;
    parseArgs(args);
    if (verbose) {
      enableLogging();
    }

    GoogleOAuthDomainWideDelegation googleOAuthDomainWideDelegation =
        new GoogleOAuthDomainWideDelegation();
    Url requestUrl = new Url(url);
    requestUrl.prettyprint = true;
    googleOAuthDomainWideDelegation.requestorId = user;
    GoogleTransport transport = new GoogleTransport();
    transport.applicationName = APP_NAME;
    googleOAuthDomainWideDelegation.signRequests(transport,
        getOauthHmacParameters());
    HttpRequest request = getHttpRequest(transport, method, body);
    request.url = requestUrl;
    try {
      printResponse(request.execute());
    } catch (HttpResponseException e) {
      printResponse(e.response);
      System.out.println("-------------- ERROR --------------");
      System.out.println(e.getMessage());
    }
  }

  private static void printResponse(HttpResponse response) throws IOException {
    // Just parsing will print the response if verbose is on
    String res = response.parseAsString();
    // Always print the response, even when not verbose.
    if (!verbose) {
      System.out.println(res);
    }
  }

  private static OAuthParameters getOauthHmacParameters() {
    OAuthHmacSigner signer = new OAuthHmacSigner();
    signer.clientSharedSecret = consumer_secret;
    OAuthParameters oauthParameters = new OAuthParameters();
    oauthParameters.signer = signer;
    oauthParameters.consumerKey = consumer_key;
    return oauthParameters;
  }

  private static HttpRequest getHttpRequest(GoogleTransport transport,
      HttpMethod method, String body) {
    switch (method) {
      case GET:
        return transport.buildGetRequest();
      case POST:
        return setRequestContent(transport.buildPostRequest(), body);
      case DELETE:
        return transport.buildDeleteRequest();
      case PUT:
        return setRequestContent(transport.buildPutRequest(), body);
       default:
        throw new RuntimeException("Unexpected HTTP method: " + method);
    }
  }

  private static HttpRequest setRequestContent(HttpRequest req, String body) {
    if (body != null && body.length() > 0) {
      InputStreamContent content = new InputStreamContent();
      content.type = content_type;
      content.length = body.getBytes().length;
      content.inputStream = new ByteArrayInputStream(body.getBytes());
      req.content = content;
    }
    return req;
  }

  @SuppressWarnings("static-access")
  private static void parseArgs(String[] args) {
    Options options = new Options();

    options.addOption("v", "verbose", false, "Print request and response");
    options.addOption(OptionBuilder.withArgName("h")
                                   .withLongOpt("help")
                                   .withDescription("Print this message")
                                   .create());
    options.addOption(OptionBuilder.withArgName("c")
                                   .withLongOpt("consumer_key")
                                   .withDescription("Consumer key")
                                   .withType(String.class)
                                   .hasArg()
                                   .isRequired()
                                   .create());
    options.addOption(OptionBuilder.withArgName("s")
                                   .withLongOpt("consumer_secret")
                                   .withDescription("Consumer Secret")
                                   .withType(String.class)
                                   .hasArg()
                                   .isRequired()
                                   .create());
    options.addOption(OptionBuilder.withArgName("u")
                                   .withLongOpt("user")
                                   .withDescription("User")
                                   .withType(String.class)
                                   .hasArg()
                                   .isRequired()
                                   .create());
    options.addOption(OptionBuilder.withArgName("u")
                                   .withLongOpt("url")
                                   .withDescription(
                                       "Url of the Resource, must be escaped")
                                   .withType(String.class)
                                   .hasArg()
                                   .isRequired()
                                   .create());
    options.addOption(OptionBuilder.withArgName("m")
                                   .withLongOpt("method")
                                   .withDescription("HTTP Method, default=GET")
                                   .withType(String.class)
                                   .hasArg()
                                   .create());
    options.addOption(OptionBuilder.withArgName("b")
                                   .withLongOpt("body")
                                   .withDescription("Request body")
                                   .withType(String.class)
                                   .hasArg()
                                   .create());
    options.addOption(OptionBuilder.withArgName("t")
                                   .withLongOpt("mimetype")
                                   .withDescription("Content mime type, " +
                                   		"default=application/atom+xml")
                                   .withType(String.class)
                                   .hasArg()
                                   .create());

    // create the parser
    CommandLineParser parser = new GnuParser();
    HelpFormatter formatter = new HelpFormatter();
    try {
      // parse the command line arguments
      CommandLine cmdLine = parser.parse(options, args);
      consumer_key = (String) cmdLine.getParsedOptionValue("consumer_key");
      consumer_secret =
          (String) cmdLine.getParsedOptionValue("consumer_secret");
      url = (String) cmdLine.getParsedOptionValue("url");
      user = (String) cmdLine.getParsedOptionValue("user");
      String strMethod = (String) cmdLine.getParsedOptionValue("method");
      method = (strMethod != null) ? HttpMethod.valueOf(strMethod.toUpperCase())
              : DEFAULT_METHOD;
      body = (String) cmdLine.getParsedOptionValue("body");
      content_type = cmdLine.hasOption("mimetype") ? (String) cmdLine
          .getParsedOptionValue("mimetype") : DEFAULT_CONTENT_TYPE;
      verbose = cmdLine.hasOption("verbose");
    } catch (ParseException exp) {
      // oops, something went wrong
      System.err.println("Parsing failed.  Reason: " + exp.getMessage());
      formatter.printHelp( "help", options );
      System.exit(0);
    } catch (IllegalArgumentException exp) {
      System.err.println("Parsing failed.  Reason: Unexpected HTTP method "
          + method);
    }
  }

  private static void enableLogging() {
    Logger logger = Logger.getLogger("com.google.api.client");
    // We set level "all" so that the Authorization Header is displayed
    logger.setLevel(Level.ALL);
    logger.addHandler(new Handler() {

      @Override
      public void close() throws SecurityException {
      }

      @Override
      public void flush() {
      }

      @Override
      public void publish(LogRecord record) {
        // default ConsoleHandler will take care of >= INFO
        if (record.getLevel().intValue() < Level.INFO.intValue()) {
          System.out.println(record.getMessage());
        }
      }
    });
  }
}
