This document describes how to migrate existing code from the Google Data Java client library (gdata-java-client) to the Google APIs Client Library for Java (google-api-java-client).

Contents:

# Migrating away from GData #

Older Google Data (GData) APIs use XML as their underlying format, but most Google APIs have released newer versions of their APIs based on JSON. You should migrate your code to use the new API infrastructure that is based on JSON, where we can provide a much better experience.

To do this, visit the documentation for the API that you are using to find out what is available for that API, or check the [current GData status](https://code.google.com/p/gdata-java-client/) table. If the GData version of the API still exists but there is an alternative built on the new Google API infrastructure, we recommend that you switch to the new version of the API.

Samples based on the Google APIs Client Library for Java are available for [various Google APIs](https://developers.google.com/api-client-library/java/apis/).  If you need a sample for a different API, ask on the [google-api-java-client Google group](https://groups.google.com/forum/?fromgroups#!forum/google-api-java-client) whether anyone has sample code they are willing to share.

Likewise, if you have an existing application that uses the GData Java client library, you need to update your code to use the [Google APIs Client Library for Java](https://code.google.com/p/google-api-java-client/), as described in this document.

## Not all code has to migrate ##

You do not need to migrate away from the GData Java client library if you are using the [Google Sites API](https://developers.google.com/google-apps/sites/), the [Google Spreadsheets API](https://developers.google.com/google-apps/spreadsheets/), or the [Picasa Web Albums Data API](https://developers.google.com/picasa-web/). For these Google APIs, keep using the GData Java client library for now.

## Some benefits of migrating ##

Unlike the GData Java client library, the [Google APIs Client Library for Java](https://code.google.com/p/google-api-java-client/) supports Android, JSON, Maven, OAuth 2.0, and efficient partial-response.

The GData library needed updates whenever our APIs added XML elements and attributes, and our releases were too infrequent to keep up. The Google APIs Client Library for Java has a completely different model, in which classes represent the data format of our API responses. You control these classes, and you don't depend on our release process to provide you with them.


# Migrating to the Google APIs Client Library for Java #

Start by reading about the [Google APIs Client Library for Java](http://code.google.com/p/google-api-java-client/) and viewing the [samples](http://code.google.com/p/google-api-java-client/source/browse?repo=samples). Join the [google-api-java-client Google group](https://groups.google.com/forum/?fromgroups#!forum/google-api-java-client) to keep up to date with the latest discussions and announcements, or ask questions or provide feedback.

Since gdata-java-client and google-api-java-client are independent projects, it is completely safe to include both in your Java classpath.  The easiest way to get started is to write new code using google-api-java-client, and migrate existing code from gdata-java-client to google-api-java-client over time as you gain confidence using google-api-java-client.

## Architectural overview ##

The architecture of a typical application based on google-api-java-client uses a Model-View-Controller style.
  * Model: Write a custom XML data model for the Google Data API based on the fields your application needs.  See the [JavaDoc](http://javadoc.google-api-java-client.googlecode.com/hg/latest/com/google/api/client/googleapis/xml/atom/package-summary.html) for more information on writing a custom data model.
  * Controller: Write the request execution methods.  The [JavaDoc](http://javadoc.google-api-java-client.googlecode.com/hg/latest/com/google/api/client/googleapis/xml/atom/package-summary.html) also explains how to do that.
  * View: Write the user interface, including authentication and authorization.  The recommended approach is to use [OAuth 2.0](https://code.google.com/p/google-api-java-client/wiki/OAuth2), or for Android, the [AccountManager](http://developer.android.com/reference/android/accounts/AccountManager.html).


## Migrate your code ##

First, you need to invest in writing a custom data model that lets your application access the Google API fields it needs.  See the [JavaDoc for the new XML data model](http://javadoc.google-api-java-client.googlecode.com/hg/latest/com/google/api/client/googleapis/xml/atom/package-summary.html) in google-api-java-client.

If there is already a sample for the API you are using, you have a great starting point.  You can copy the data model classes it uses directly into your application.  Of course, since it is just a sample, it shows only the data classes and fields that the sample needs, so you'll probably need to add more data classes and fields.  It's easy to do this, or even to use google-api-java-client with an API that doesn't yet have a sample, by applying the same techniques demonstrated in the existing samples.

There are three other key differences between how you set up the API using the new library and the old one:

First, there is a much different calling style with google-api-java-client.  Instead of calling a service by name, you start with the generic HttpTransport class and set the application name in its headers.   So, in the case of YouTube, you previously you might have written code like this:

```
  YouTubeService service = new YouTubeService("google-youtubesample-1.0");
```

With google-api-client, it's a few more lines of code:

```
    HttpTransport transport = GoogleTransport.create();
    GoogleHeaders headers = (GoogleHeaders) transport.defaultHeaders;
    headers.setApplicationName("google-youtubesample-1.0");
    headers.gdataVersion = "2";
```

Second, authentication is different in gdata-java-client and google-api-java-client.  For example, if you are using ClientLogin, you used to do something like the following:

```
  service.setUserCredentials(username, password);
```

But with the new library, you write code like this:

```
    ClientLogin authenticator = new ClientLogin();
    authenticator.authTokenType = "youtube";
    authenticator.username = username;
    authenticator.password = password;
    authenticator.authenticate().setAuthorizationHeader(transport);
```

If you are migrating to the new client library and are still using ClientLogin, your application still needs to ask your end users for their username and password, which is insecure.  We strongly recommend that you migrate to [OAuth 2.0](https://developers.google.com/accounts/docs/OAuth2).

Third, you write your own API-specific class that extends `GoogleUrl` and adds the query parameters you want to use; there is no `YouTubeQuery` to help you construct a URL with google-api-java-client.  Here is an example for YouTube:

```
public static class YouTubeUrl extends GoogleUrl {
  @Key public String orderby;
  @Key public String q;
  @Key public String safeSearch;
  public YouTubeUrl(String encodedUrl) {
    super(encodedUrl);
  }
}
```

Once you've set up the basics of the API, you're ready to use it in your application.  Previously, the code you wrote when you used gdata-java-client looked something like this:
```
public static void printPuppyVideos(YouTubeService service) {
  YouTubeQuery query = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));
  // order results by the number of views (most viewed first)
  query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);

  // search for puppies and include restricted content in the search results
  query.setFullTextQuery("puppy");
  query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);

  VideoFeed videoFeed = service.query(query, VideoFeed.class);
  for (VideoEntry videoEntry : videoFeed.getEntries()) {
    System.out.println("Title: " + videoEntry.getTitle().getPlainText());
  }
}
```

Now, with the google-api-java-client, the code you write looks more like this:

```
public static void printPuppyVideos(HttpTransport transport) {
  YouTubeUrl url = new YouTubeUrl("http://gdata.youtube.com/feeds/api/videos");
  // order results by the number of views (most viewed first)
  url.orderBy = "viewCount";

  // search for puppies and include restricted content in the search results
  url.q = "puppy";
  url.safeSearch = "none";

  HttpRequest request = transport.buildGetRequest();
  request.url = url;
  VideoFeed videoFeed = request.execute().parseAs(VideoFeed.class);
  for (VideoEntry videoEntry : videoFeed.videos) {
    System.out.println("Title: " + videoEntry.title);
  }
}
```

There are other differences as well.  For example, how you handle XML namespaces and the way you insert new items into a feed is different.