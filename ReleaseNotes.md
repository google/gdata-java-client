#summary The current release notes
#labels Featured

# Introduction #

The release notes show bugfixes and feature additions over the course of the project.
The wiki page shows the release notes for the current binary download, the subversion project contains a text version with the most recent changes.

# Details #
## v1.46.0 ##
  * Change in behaviour for adding new users to site permissions.
  * Add additional roles to AclEntry.
  * Add support for the new gContact:status element for the User Profiles API.

## v1.45.0 ##
  * Updated Document List Data API

## v1.44.0 ##
  * Adds missing removeOwnerFromGroup() method in appsforyourdomain API
  * Updates Translator Toolkit sample to use https instead of http.
  * Adds support for adding, fetching and deleting email delegates in the Email Settings API.

## v1.43.0 ##
  * Add HealthDemo to the Health API.
  * Fixes [issue 249](https://code.google.com/p/gdata-java-client/issues/detail?id=249): setUserCredentials method hangs from sample code.
  * Sites API adds version 1.3 which includes TagCategory.
  * Updates appsforyourdomain API for Gmail Settings.
  * Updates GData client URL scheme to use Https. Also Updates several samples.

## v1.42.0 ##
  * Update Analytics API with new fields

## v1.41.5 ##
  * Update Analytics API.
  * Fix AuthSub encoding/decoding issue.

## v1.41.4 ##
  * Updates Apps For Your Domain API.
  * Updates Youtube API.

## v1.41.3 ##
  * Updated Apps Audit API.

## v1.41.2 ##
  * Updated YouTube API.
  * Fixed bug cuased by a new attribute in responses.

## 1.41.1 ##
  * Updated Document List Data API.
  * Updates Sites Data API.

## 1.41.0 ##
  * Added support for resumable media upload feature.
  * Added support for Apps for your domain Admin Settings API.
  * Bug fix in webmaster tools API to handle file-content attribute.

## 1.40.3 ##
  * Updated Google Sites API to include sites and user access control feeds.

## 1.40.2 ##
  * Added support for 2 Legged OAuth authentication.
  * Removed requirement to set !com.google.gdata.DisableCookieHandler property when running in Google App Engine environment.

## 1.40.1 ##
  * Updated Project Hosting Data API.
  * Updated Google Analytics Data API.

## 1.40.0 ##
  * Added support for Google Translator Toolkit Data API.
  * Updated Google Finance Data API to use version 2 of !GData protocol.

## 1.39.1 ##
  * Added site usefulness element in Google Sidewiki Data API.  For additional details see [changelog](http://code.google.com/apis/sidewiki/changelog.html).

## 1.39.0 ##
  * Added support for Google Project Hosting Data API.

## 1.38.0 ##
  * Added support for Google Sites Data API.

## 1.37.0 ##
  * Added support for Google Sidewiki Data API.

## 1.36.0 ##
  * Added support for version 3 of Documents List Data API.  This update includes support for PDF uploads, folder sharing, domain and group level !ACLs, and document revision history.

## 1.35.1 ##
  * Expanded Google Maps Data API sample code and minor bug fixes.

## 1.35.0 ##
  * Updated Photos Data API to use Version 2 of Google Data Protocol.
  * Removed com.google.gdata.util.common.annotations package in favor of com.google.common.annotations available in google-collections library.
  * Fixed bug java.lang.UnsupportedClassVersionError in version 1.34.

## 1.34.0 ##
  * Updated YouTube API data model classes.
  * Fixed encoding issue with atom content in a multipart/related body.

## 1.33.0 ##
  * Update Google Contacts Data API to support version 3 of Contacts API. Version 3.0 brings greatly expanded data schema, including support for structured name and postal address.
  * Added support for generic group attributes in Google Base Data API.
  * Updated OAuth implementation to support changes related to OAuth 1.0a specification.

## 1.32.1 ##
  * Added support for Table and Record feeds in Google Spreadsheets Data API

## 1.32.0 ##
  * Added support for Analytics Google Data API.
  * Updated Books API, Docs API and !OAuth samples code.
  * Updated Apps Groups API to use https instead of http.
  * Fixed non-blocking bugs related to comptability with Google Java App Engine.

## 1.31.1 ##
  * Fixed bug in com.google.gdata.util.net.UriEncoder which causes infinite when run in JDK 1.5 in !MacOS because of a JDK bug.

## 1.31.0 ##
  * Added support for Analytics Google Data API.
  * Updated Books API, Docs API and OAuth samples code.
  * Updated Apps Groups API to use https instead of http.
  * Fixed non-blocking bugs related to comptability with Google Java App Engine.
  * Introduced dependency on Google Collections library.  This library is shipped with the src and samples package under java/deps/google-collect-1.0-rc1.jar and java/deps/jsr305.jar.

## 1.30.0 ##
  * Updated ant build configuration to reduce build time by ~60%.  Thanks to "mchenryc" for this [contribution](http://code.google.com/p/gdata-java-client/issues/detail?id=82).
  * Added support for ChannelFeed and UserEventFeed in YouTube Data API.
  * Fixed batch support issue in YouTube Data API Feed classes.
  * Fixed bug relaed to GoogleService#delete fails in case of redirection in GData Protocol version 2. See [bug](http://code.google.com/p/gdata-java-client/issues/detail?id=82) for details.

## 1.29.0 ##
  * Added support for AppsForYourDomain Groups API.

## 1.28.0 ##
  * Fixed bug in com.google.gdata.util.common.base.CharMatcher which shows up only when executing in eclipse with JDK 1.5.

## 1.27.0 ##
  * Updated Contacts, Webmaster Tools Google Data APIs to use Version 2 of Google Data Protocol. With this change all requests from the above mentioned APIs will send requests with the "GData-Version" http header set to "2.0".  If you need to request older versions of the feed, you can do so by calling "{Property}Service#setProtocolVersion".
  * Contacts Version 2 API has few breaking changes.  For details refer to Contacts API version 2 [migration guide](http://code.google.com/apis/contacts/docs/2.0/migration_guide.html).
  * Webmaster Tools Version 2 API does not contain any new features.  But it inherits all of the GData Protocol Version 2 core capabilities including Etags and user of new opensearch namespace.
  * Added new data model classes for Attendee Feed and Access Control Entry in Calendar Data API.

## 1.26.0 ##
  * Fixed bug related to failure to load com.google.gdata.utuil.common.xml.XmlWriter when executing in eclipse with JDK 1.5.

## 1.25.0 ##
  * Updated Google Base, Blogger, Calendar, Code Search, Document List, Finance, Health, Spreadsheet Data API to use version 2 of Google Data Protocol.  With this change all requests from above mentioned API will send requests with "GData-Version" http header set to "2.0".  If you need to request older version of the feed, you can do so by calling "Service#setProtocolVersion".
  * The Version 2 protocol for above API does not contain any new features.  But they inherit all of the GData Protocol Version 2 core capabilities including Etags, use of new opensearch namespace.

## 1.24.0 ##
  * Added support for version 2 of Google Data API.  [API versioning](http://code.google.com/apis/youtube/2.0/developers_guide_protocol.html#API_Versioning) is rolled-out in phases. As of this release only YouTube Data API supports version 2.  Version 2 provides significant feature enhancements including:
    * Added Support for caching through http ETag headers.  The implementation is compatible with specification in RFC 2616 with support for If-Match, If-None-Match, If-Modified-Since headers.  Other conditional headers are not supported.
    * Updated OpenSearch namespace from http://a9.com/-/spec/opensearchrss/1.0/ to http://a9.com/-/spec/opensearch/1.1/.
    * Introduced "GData-Version" header to indicate requested protocol version.  The GData-Version header value is of format "

<major\_version>

.

<minor\_version>

".  Possible values as of this release are "1.0" and "2.0".  The version information can also be specified using "v=2.0" query parameter.
    * All version 2 enabled Google Data API Services will continue to support version 1 of the protocol.  If both "GData-Version" header and "v" query parameter are not specified in the request, the server defaults to version 1.0.
  * Updated com.google.gdata.client.youtube.YouTubeService class to use version 2 of the YouTube Data API as default version. Refer to [YouTube API developer guide](http://code.google.com/apis/youtube/2.0/developers_guide_protocol.html) for details on version 2 API.  Refer to[YouTube Data API Migration Guide](http://code.google.com/apis/youtube/2.0/migration.html) for details on breaking changes from version 1 to version 2, and details on how to migrate your code to work with version 2 of the YouTube Data API. NOTE: There are breaking API changes between version 1 and version 2.  Though it is not required to upgrade to version 2.0 immediately, it is highly recommended to upgrade version 2.0.  Older versions of YouTube Data API libraries will continue to work until version 1.0 is deprecated.

## 1.23.0 ##
  * Added samples for Google [Apps Email Settings API](http://code.google.com/apis/apps/email_settings/developers_guide_protocol.html).

## 1.22.0 ##
  * Added support for Book Search Data API.
  * Updated data model for Blogger Data API and Youtube Data API.

## 1.21.0 ##
  * Added support for OAuth authentication with options to sign requests using RSA-SHA1 or HMAC-SHA1.  See OAuth sample included in this package for usage details.
  * Updated PicasaWeb Album API and YouTube API data model classes.
  * Added Atom threading extensions (RFC 4685) support in Blogger API.
  * Fixed a bug related to retries related to session expiry.
  * Added chunking support for large video uploads.

## 1.20.0 ##
  * Added support for Finance GData API.  For details refer to [Finance API docs](http://code/google.com/apis/finance).

## 1.19.0 ##
  * Added support for Webmaster Tools API.

## 1.18.0 ##
  * Added support to handle GData Protocol Version 2.  This provides capability for the APIs to be fully compliant with [Atom Publishing Protocol](http://bitworking.org/projects/atom/rfc5023.html). This introduces a new HTTP request header "GData-Version" which indicates whether a request/response is v1 or v2.  Not all services are v2 compatible yet.  As of this release only Blogger API is v2 compatible.  By default all requests will use GData Protocol Version 1.
  * Added support to handle ETags and if-match, if-none-match, if-modified-since conditional headers as defined in [RFC 2616](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.19).  This is applicable only for services that support GData Protocol Version 2.
  * Added several new features in Contacts API.  For details refer to [Contacts API docs](http://code.google.com/apis/contacts/).
    * Added support to group contacts.
    * Added support to access/update contact photos.
    * Added ability to append custom contact fields.

## 1.17.0 ##
  * Added support for [Google Health GData API](http://code.google.com/apis/health).  This provides minimal interface to interact with Google Health Service.  This API does not include data model classes for CCR health records.
  * Added new error types in Google Apps API.

## 1.16.4 ##
  * Fixed compiled libraries in 1.16.3 to be compatible with Java 5.  No code change introduced in this release.

## 1.16.3 ##
  * Added support to enable Authentication to Proxy Service for hosted Google Apps.  See AuthSub for Web Application documentation for details http://code.google.com/apis/accounts/docs/AuthForWebApps.html.
  * Added support for RFC 822 text message encoding in Email Migration API.
  * Fixed issue related to handling YouTube video entries with empty description.

## 1.16.2 ##
  * Added write support including video upload feature in [YouTube Data API](http://code.google.com/apis/youtube/overview.html).

## 1.16.1 ##
  * Fixed [YouTube Data API](http://code.google.com/apis/youtube/overview.html) video feed parsing error related to unknown attribute 'favouriteCount'.

## 1.16.0 ##
  * Added Java client support for Google Contacts API.
  * Samples are packaged separately from source code.  With this change, there will be two packages, gdata-src.java.zip which contains the API source and gdata-samples.java.zip which contains samples and precompiled lib.

## 1.15.2 ##
  * Added support to upload image files using Document List Feed Data API.

## 1.15.1 ##
  * Added support for media feed in Google Base data API.  With the media feed support you can manage binary attachments to your Google Base items.  With this change you will require Java Mail and Java Activation Framework to use Google Base data API.

## 1.15.0 ##
  * Added Java client support for Google Apps Email Migration API.
  * Removed GData API documentation from the release package.  Online documentation is available at http://code.google.com/apis/gdata.

## 1.14.1 ##
  * Added batch support for Event Feed in Calendar Data API.
  * Added batch support for Cells Feed in Spreadsheet Data API.
  * Added support to handle foreign markup constraints as specified in RFC4287 sec 6.3 and AtomPub section 6.2.
  * Lowered visibility of log messages reporting on unrecognized foreign markup in xml feeds from INFO level to FINE level.

## 1.14.0 ##
  * Added support for [YouTube Data API](http://code.google.com/apis/youtube/overview.html).
  * Refactored core GData code to remove dependency on, [JavaMail](http://java.sun.com/products/javamail) and [Java Activation Framework](http://java.sun.com/products/javabeans/jaf) for non-media services. With this change, only media dependent services such as, [Document List Data API](http://code.google.com/apis/documents/overview.html), [Picasa Web Album API](http://code.google.com/apis/picasaweb/overview.html) and [YouTube Data API](http://code.google.com/apis/youtube/overview.html) require Java Mail and Java Activation Framework.
  * Simplified ant build configuration into multiple files.  This may require updates to your IDE settings to point to new ant targets.  For the list of available build targets refer to java/build.xml ant build file.

## 1.13.0 ##
  * Added support for Google Documents List Data API.
  * Added support for Calendar Gadgets in Calendar Data API.
  * Added samples for interacting with read/write calendar feeds in Calendar Data API.
  * Deprecated Geo format gd:geoPt from data model.

## 1.12.0 ##
  * Added support for standard Geo formats including GeoRSS, W3C Semantic Web IG and GML format.  The use of gd:geoPt is discouraged.
  * Updated GData Java client library versioning to follow the standard format "

<major\_release>

.

<minor\_release>

.

<patch\_release>

". In this release the version number changed from "1.0.11" to "1.12.0" to reflect the updated versioning scheme.

## 1.0.11a ##
  * Fixed bug related to Calendar sample when retrieving ACL feed for public calendar.

## 1.0.11 ##
  * Added event publisher mashup sample that publishes list of events stored in a Google Spreadsheet to other Google services.  This sample shows how to retrieve the event data from Google Spreadsheet and publish those events to Google Calendar and Google Base.

## 1.0.10 ##
  * Added Java client support and extenstions for Google Calendar access control lists (ACLs).
  * Added Blogger GData API sample.
  * Changed inheritance hierarchy for CalendarEventFeed.  As a side effect, getEntries() method for Calendar event feed will return list of objects of type CalendarEventEntry instead of EventEntry.
  * Changed participant types for CalendarEventEntry.  As a side effect, getParticipants() method for Calendar event entry will return list of objects of type EventWho instead of Who.

## 1.0.9 ##
  * Added Java client support and extensions for GData Picasa Web Album API.
  * Added Java client support and extensions for GData Apps For Your Domain API.
  * Added "method" attribute to 

&lt;gd:reminder&gt;

.  The "method" attribute can be used to specify notification method for Calendar event related reminders. The "method" value can be one of ALERT, EMAIL, SMS, ALL or NONE.
  * Introduced dependency on Sun's JavaMail API.  To use client package, download and install activation.jar, mail.jar libraries from Sun's JavaMail API package version 1.4 or later.

## 1.0.8 ##
  * Added Java client support and extensions for GData Google Base API.
  * Added Java client support and extensions for GData Notebook API.