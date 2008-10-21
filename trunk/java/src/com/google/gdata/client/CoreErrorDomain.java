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


package com.google.gdata.client;

import com.google.gdata.util.ErrorDomain;

/**
 This is the error domain class for the Core Error Domain,
 representing errors thrown by the core server and Java client
 library.
 */
public class CoreErrorDomain extends ErrorDomain {

  private CoreErrorDomain() {
    super("GData");
    }

  public static final CoreErrorDomain ERR = new CoreErrorDomain();


  public final ErrorCode versionNotSupported =
      new ErrorCode("versionNotSupported")
      .setInternalReason("Version is not supported");

  public final ErrorCode accountDeleted =
      new ErrorCode("accountDeleted")
      .setInternalReason("Account deleted");

  public final ErrorCode accountDisabled =
      new ErrorCode("accountDisabled")
      .setInternalReason("Account disabled");

  public final ErrorCode accountUnverified =
      new ErrorCode("accountUnverified")
      .setInternalReason("Account unverified");

  public final ErrorCode atomFormatRequired =
      new ErrorCode("atomFormatRequired")
      .setInternalReason("Batch requires Atom format");

  public final ErrorCode batchingNotSupported =
      new ErrorCode("batchingNotSupported")
      .setInternalReason("Batching not supported by feed");

  public final ErrorCode cantAccessFeedData =
      new ErrorCode("cantAccessFeedData")
      .setInternalReason("Unable to access feed data");

  public final ErrorCode cantCreateContentGenerator =
      new ErrorCode("cantCreateContentGenerator")
      .setInternalReason("Unable to create ContentGenerator instance");

  public final ErrorCode cantCreateEntry =
      new ErrorCode("cantCreateEntry")
      .setInternalReason("Unable to create entry");

  public final ErrorCode cantCreateExtension =
      new ErrorCode("cantCreateExtension")
      .setInternalReason("Unable to create extension");

  public final ErrorCode cantCreateFeed =
      new ErrorCode("cantCreateFeed")
      .setInternalReason("Unable to create feed");

  public final ErrorCode cantCreateProvider =
      new ErrorCode("cantCreateProvider")
      .setInternalReason("Unable to instantiate provider");

  public final ErrorCode cantDecodeCategoryQuery =
      new ErrorCode("cantDecodeCategoryQuery")
      .setInternalReason("Unable to decode category query");

  public final ErrorCode cantEncodeQueryParams =
      new ErrorCode("cantEncodeQueryParams")
      .setInternalReason("Unable to encode query parameters");

  public final ErrorCode cantExtractJsonValue =
      new ErrorCode("cantExtractJsonValue")
      .setInternalReason("Cannot extract JSON value");

  public final ErrorCode cantLoadAuthProviderClass =
      new ErrorCode("cantLoadAuthProviderClass")
      .setInternalReason("authProvider class cannot be loaded");

  public final ErrorCode cantLoadEntryClass =
      new ErrorCode("cantLoadEntryClass")
      .setInternalReason("entry class cannot be loaded");

  public final ErrorCode cantLoadExtensionClass =
      new ErrorCode("cantLoadExtensionClass")
      .setInternalReason(
      "Extension classes must implement the Extension interface");

  public final ErrorCode cantLoadExtensionPoint =
      new ErrorCode("cantLoadExtensionPoint")
      .setInternalReason(
      "Unable to load ExtensionPoint class");

  public final ErrorCode cantLoadFeedClass =
      new ErrorCode("cantLoadFeedClass")
      .setInternalReason("feed class cannot be loaded");

  public final ErrorCode cantLoadFeedProviderClass =
      new ErrorCode("cantLoadFeedProviderClass")
      .setInternalReason("feedProvider class cannot be loaded");

  public final ErrorCode cantLoadGeneratorClass =
      new ErrorCode("cantLoadGeneratorClass")
      .setInternalReason("Unable to load ContentGenerator class");

  public final ErrorCode cantLoadKindAdaptor =
    new ErrorCode("cantLoadKindAdaptor")
    .setInternalReason("Unable to load kind adaptor");

  public final ErrorCode cantLoadServiceClass =
      new ErrorCode("cantLoadServiceClass")
      .setInternalReason("Unable to load serviceClass class");

  public final ErrorCode cantWriteMimeMultipart =
      new ErrorCode("cantWriteMimeMultipart")
      .setInternalReason("Unable to write MIME multipart message");

  public final ErrorCode clientNotWhitelisted =
      new ErrorCode("clientNotWhitelisted")
      .setInternalReason("Client not whitelisted for ONLINE access");

  public final ErrorCode collectionTitleRequired =
      new ErrorCode("collectionTitleRequired")
      .setInternalReason("Collection must contain a title");

  public final ErrorCode commentsFeedLinkRequired =
      new ErrorCode("commentsFeedLinkRequired")
      .setInternalReason("g:comments/g:feedLink is required");

  public final ErrorCode deleteNotSupported =
      new ErrorCode("deleteNotSupported")
      .setInternalReason("Delete not supported by feed");

  public final ErrorCode duplicateAlt =
      new ErrorCode("duplicateAlt")
      .setInternalReason("Duplicate alt mapping");

  public final ErrorCode duplicateAttribute =
      new ErrorCode("duplicateAttribute")
      .setInternalReason("Duplicate attribute");

  public final ErrorCode duplicateAttributeValue =
      new ErrorCode("duplicateAttributeValue")
      .setInternalReason("Duplicate attribute value");

  public final ErrorCode duplicateContent =
      new ErrorCode("duplicateContent")
      .setInternalReason("Duplicate content");

  public final ErrorCode duplicateDraft =
      new ErrorCode("duplicateDraft")
      .setInternalReason("Duplicate draft");

  public final ErrorCode duplicateEmail =
      new ErrorCode("duplicateEmail")
      .setInternalReason("Duplicate email");

  public final ErrorCode duplicateEntryId =
      new ErrorCode("duplicateEntryId")
      .setInternalReason("Duplicate entry ID");

  public final ErrorCode duplicateExtension =
      new ErrorCode("duplicateExtension")
      .setInternalReason("Duplicate extension element");

  public final ErrorCode duplicateFeedId =
      new ErrorCode("duplicateFeedId")
      .setInternalReason("Duplicate feed ID");

  public final ErrorCode duplicateGenerator =
      new ErrorCode("duplicateGenerator")
      .setInternalReason("Duplicate generator");

  public final ErrorCode duplicateIcon =
      new ErrorCode("duplicateIcon")
      .setInternalReason("Duplicate icon");

  public final ErrorCode duplicateItemsPerPage =
      new ErrorCode("duplicateItemsPerPage")
      .setInternalReason("Duplicate itemsPerPage");

  public final ErrorCode duplicateLogo =
      new ErrorCode("duplicateLogo")
      .setInternalReason("Duplicate logo");

  public final ErrorCode duplicateName =
      new ErrorCode("duplicateName")
      .setInternalReason("Duplicate name");

  public final ErrorCode duplicatePathPrefix =
      new ErrorCode("duplicatePathPrefix")
      .setInternalReason("Duplicate pathPrefix element");

  public final ErrorCode duplicateRights =
      new ErrorCode("duplicateRights")
      .setInternalReason("Duplicate rights");

  public final ErrorCode duplicateStartIndex =
      new ErrorCode("duplicateStartIndex")
      .setInternalReason("Duplicate startIndex");

  public final ErrorCode duplicateSubtitle =
      new ErrorCode("duplicateSubtitle")
      .setInternalReason("Duplicate subtitle");

  public final ErrorCode duplicateSummary =
      new ErrorCode("duplicateSummary")
      .setInternalReason("Duplicate summary");

  public final ErrorCode duplicateTextNodeValue =
      new ErrorCode("duplicateTextNodeValue")
      .setInternalReason("Duplicate text node value");

  public final ErrorCode duplicateTitle =
      new ErrorCode("duplicateTitle")
      .setInternalReason("Duplicate title");

  public final ErrorCode duplicateTotalResults =
      new ErrorCode("duplicateTotalResults")
      .setInternalReason("Duplicate totalResults");

  public final ErrorCode duplicateUri =
      new ErrorCode("duplicateUri")
      .setInternalReason("Duplicate URI");

  public final ErrorCode duplicateUrlBase =
      new ErrorCode("duplicateUrlBase")
      .setInternalReason("Duplicate urlBase element");

  public final ErrorCode duplicateValue =
      new ErrorCode("duplicateValue")
      .setInternalReason("Duplicate value");

  public final ErrorCode elementNotRepeatable =
      new ErrorCode("elementNotRepeatable")
      .setInternalReason("Element is not repeatable");

  public final ErrorCode elementNotSimple =
      new ErrorCode("elementNotSimple")
      .setInternalReason("Element is not simple");

  public final ErrorCode emailValueRequired =
      new ErrorCode("emailValueRequired")
      .setInternalReason("email must have a value");

  public final ErrorCode entityTagMatches =
      new ErrorCode("entityTagMatches")
      .setInternalReason("At least one entity tag matches");

  public final ErrorCode entryNotAssociated =
      new ErrorCode("entryNotAssociated")
      .setInternalReason("Entry is not associated with a GData service");

  public final ErrorCode etagsMismatch =
      new ErrorCode("etagsMismatch")
      .setInternalReason("Etags mismatch");

  public final ErrorCode etagsUnsupported =
      new ErrorCode("etagsUnsupported")
      .setInternalReason("Resource does not support Etags");

  public final ErrorCode feedNotAssociated =
      new ErrorCode("feedNotAssociated")
      .setInternalReason("Feed is not associated with a GData service");

  public final ErrorCode geoPtLatRequired =
      new ErrorCode("geoPtLatRequired")
      .setInternalReason("g:geoPt/@lat is required");

  public final ErrorCode geoPtLonRequired =
      new ErrorCode("geoPtLonRequired")
      .setInternalReason("g:geoPt/@lon is required");

  public final ErrorCode headerRequired =
      new ErrorCode("headerRequired")
      .setInternalReason("Header required");

  public final ErrorCode iconValueRequired =
      new ErrorCode("iconValueRequired")
      .setInternalReason("icon must have a value");

  public final ErrorCode idRequired =
      new ErrorCode("idRequired")
      .setInternalReason("g:originalEvent/@id is required");

  public final ErrorCode idValueRequired =
      new ErrorCode("idValueRequired")
      .setInternalReason("ID must have a value");

  public final ErrorCode imsNotSupported =
      new ErrorCode("imsNotSupported")
      .setInternalReason(
      "If-Modified-Since HTTP precondition not supported on POST");

  public final ErrorCode incorrectDataVersion =
      new ErrorCode("incorrectDataVersion");
  
  public final ErrorCode insertNotSupported =
      new ErrorCode("insertNotSupported")
      .setInternalReason("Insert not supported by feed");

  public final ErrorCode insufficientSecurityLevel =
      new ErrorCode("insufficientSecurityLevel")
      .setInternalReason("Insufficient security level");

  public final ErrorCode invalidAltValue =
      new ErrorCode("invalidAltValue")
      .setInternalReason("Invalid alt value for entry");

  public final ErrorCode invalidArbitraryXml =
      new ErrorCode("invalidArbitraryXml")
      .setInternalReason("Invalid value for arbitrary XML");

  public final ErrorCode invalidAttributeValue =
      new ErrorCode("invalidAttributeValue")
      .setInternalReason("Invalid value for attribute");

  public final ErrorCode invalidAverageRatingAttribute =
      new ErrorCode("invalidAverageRatingAttribute")
      .setInternalReason(
      "gd:rating/@average should lie in between " +
      "gd:rating/@min and gd:rating/@max");

  public final ErrorCode invalidBase64 =
      new ErrorCode("invalidBase64")
      .setInternalReason("Expected Base-64 content");

  public final ErrorCode invalidBatchOperationType =
      new ErrorCode("invalidBatchOperationType")
      .setInternalReason("Invalid type for batch:operation");

  public final ErrorCode invalidBooleanAttribute =
      new ErrorCode("invalidBooleanAttribute")
      .setInternalReason("Invalid value for boolean attribute");

  public final ErrorCode invalidCategoryFilter =
      new ErrorCode("invalidCategoryFilter")
      .setInternalReason("Invalid category filter");

  public final ErrorCode invalidContentType =
      new ErrorCode("invalidContentType")
      .setInternalReason("Malformed Content-Type");

  public final ErrorCode invalidCountHintAttribute =
      new ErrorCode("invalidCountHintAttribute")
      .setInternalReason("Invalid gd:feedLink/@countHint");

  public final ErrorCode invalidDatetime =
      new ErrorCode("invalidDatetime")
      .setInternalReason("Badly formatted datetime");

  public final ErrorCode invalidDoubleAttribute =
      new ErrorCode("invalidDoubleAttribute")
      .setInternalReason("Invalid value for double attribute");

  public final ErrorCode invalidDraft =
      new ErrorCode("invalidDraft")
      .setInternalReason("Invalid value for draft");

  public final ErrorCode invalidEndValue =
      new ErrorCode("invalidEndValue")
      .setInternalReason("Invalid end value");

  public final ErrorCode invalidFixedAttribute =
      new ErrorCode("invalidFixedAttribute")
      .setInternalReason("Invalid value for fixed attribute");

  public final ErrorCode invalidFloatAttribute =
      new ErrorCode("invalidFloatAttribute")
      .setInternalReason("Invalid value for float attribute");

  public final ErrorCode invalidGeoPtElev =
      new ErrorCode("invalidGeoPtElev")
      .setInternalReason("Invalid geoPt/@elev");

  public final ErrorCode invalidGeoPtLat =
      new ErrorCode("invalidGeoPtLat")
      .setInternalReason("Invalid geoPt/@lat");

  public final ErrorCode invalidGeoPtLon =
      new ErrorCode("invalidGeoPtLon")
      .setInternalReason("Invalid geoPt/@lon");

  public final ErrorCode invalidGeoPtTime =
      new ErrorCode("invalidGeoPtTime")
      .setInternalReason("Date/time value expected");

  public final ErrorCode invalidIntegerAttribute =
      new ErrorCode("invalidIntegerAttribute")
      .setInternalReason("Invalid value for integer attribute");

  public final ErrorCode invalidJson =
      new ErrorCode("invalidJson")
      .setInternalReason("Invalid JSON");

  public final ErrorCode invalidLongAttribute =
      new ErrorCode("invalidLongAttribute")
      .setInternalReason("Invalid value for long attribute");

  public final ErrorCode invalidMediaSourceUri =
      new ErrorCode("invalidMediaSourceUri")
      .setInternalReason("Invalid media source URI");

  public final ErrorCode invalidMediaType =
      new ErrorCode("invalidMediaType")
      .setInternalReason("Not a valid media type");

  public final ErrorCode invalidMethodOverrideHeader =
      new ErrorCode("invalidMethodOverrideHeader")
      .setInternalReason("Invalid method override header");

  public final ErrorCode invalidMimeType =
      new ErrorCode("invalidMimeType")
      .setInternalReason("Malformed MIME type");

  public final ErrorCode invalidParameterValue =
      new ErrorCode("invalidParameterValue")
      .setInternalReason("Invalid parameter value");

  public final ErrorCode invalidRedirectedToUrl =
      new ErrorCode("invalidRedirectedToUrl")
      .setInternalReason("Invalid redirected-to URL");

  public final ErrorCode invalidReminderAbsoluteTime =
      new ErrorCode("invalidReminderAbsoluteTime")
      .setInternalReason("Invalid g:reminder/@absoluteTime");

  public final ErrorCode invalidReminderDays =
      new ErrorCode("invalidReminderDays")
      .setInternalReason("Invalid g:reminder/@days");

  public final ErrorCode invalidReminderHours =
      new ErrorCode("invalidReminderHours")
      .setInternalReason("Invalid g:reminder/@hours");

  public final ErrorCode invalidReminderMethod =
      new ErrorCode("invalidReminderMethod")
      .setInternalReason("Invalid g:reminder/@method");

  public final ErrorCode invalidReminderMinutes =
      new ErrorCode("invalidReminderMinutes")
      .setInternalReason("Invalid g:reminder/@minutes");

  public final ErrorCode invalidRequestUri =
      new ErrorCode("invalidRequestUri")
      .setInternalReason("Invalid request URI");

  public final ErrorCode invalidRequestVersion =
      new ErrorCode("invalidRequestVersion")
      .setInternalReason("Invalid request version");

  public final ErrorCode invalidResourceVersion =
      new ErrorCode("invalidResourceVersion")
      .setInternalReason("Unexpected resource version ID");

  public final ErrorCode invalidSecurityProtocol =
      new ErrorCode("invalidSecurityProtocol")
      .setInternalReason("Invalid security protocol");

  public final ErrorCode invalidServiceClass =
      new ErrorCode("invalidServiceClass")
      .setInternalReason("Invalid service class attribute");

  public final ErrorCode invalidStartValue =
      new ErrorCode("invalidStartValue")
      .setInternalReason("Invalid start value");

  public final ErrorCode invalidTextContentType =
      new ErrorCode("invalidTextContentType")
      .setInternalReason("Invalid text content type");

  public final ErrorCode invalidTimeOffset =
      new ErrorCode("invalidTimeOffset")
      .setInternalReason("Invalid time offset");

  public final ErrorCode invalidToDoDueTime =
      new ErrorCode("invalidToDoDueTime")
      .setInternalReason("Invalid g:toDo/@dueTime");

  public final ErrorCode invalidToDoHours =
      new ErrorCode("invalidToDoHours")
      .setInternalReason("Invalid g:toDo/@hours");

  public final ErrorCode invalidUri =
      new ErrorCode("invalidUri")
      .setInternalReason("Badly formatted URI");

  public final ErrorCode invalidUriTemplate =
      new ErrorCode("invalidUriTemplate")
      .setInternalReason("Invalid uriTemplate");

  public final ErrorCode invalidUrl =
      new ErrorCode("invalidUrl")
      .setInternalReason("Badly formatted URL");

  public final ErrorCode invalidValueRatingAttribute =
      new ErrorCode("invalidValueRatingAttribute")
      .setInternalReason(
      "gd:rating/@value should lie in between " +
      "gd:rating/@min and gd:rating/@max");

  public final ErrorCode invalidVersion =
      new ErrorCode("invalidVersion")
      .setInternalReason("Invalid version");

  public final ErrorCode itemsPerPageNotInteger =
      new ErrorCode("itemsPerPageNotInteger")
      .setInternalReason("itemsPerPage is not an integer");

  public final ErrorCode lengthNotInteger =
      new ErrorCode("lengthNotInteger")
      .setInternalReason("Length must be an integer");

  public final ErrorCode logoValueRequired =
      new ErrorCode("logoValueRequired")
      .setInternalReason("logo must have a value");

  public final ErrorCode matchHeaderRequired =
      new ErrorCode("matchHeaderRequired")
      .setInternalReason("If-Match or If-None-Match header required");

  public final ErrorCode minGreaterThanMax =
      new ErrorCode("minGreaterThanMax")
      .setInternalReason(
      "'updatedMin' must be less than or equal to 'updatedMax'");

  public final ErrorCode missingAddressAttribute =
      new ErrorCode("missingAddressAttribute")
      .setInternalReason("g:email/@address is required");

  public final ErrorCode missingAltAttribute =
      new ErrorCode("missingAltAttribute")
      .setInternalReason("Missing alt attribute");

  public final ErrorCode missingAttribute =
      new ErrorCode("missingAttribute")
      .setInternalReason("Missing attribute");

  public final ErrorCode missingContact =
      new ErrorCode("missingContact")
      .setInternalReason("missing contact");

  public final ErrorCode missingContentType =
      new ErrorCode("missingContentType")
      .setInternalReason("Response contains no content type");

  public final ErrorCode missingConverter =
      new ErrorCode("missingConverter")
      .setInternalReason("No converter for type");

  public final ErrorCode missingDescription =
      new ErrorCode("missingDescription")
      .setInternalReason("missing description");

  public final ErrorCode missingEntry =
      new ErrorCode("missingEntry")
      .setInternalReason("Entry not found");

  public final ErrorCode missingExtensionClass =
      new ErrorCode("missingExtensionClass")
      .setInternalReason("Missing extensionClass attribute");

  public final ErrorCode missingExtensionElement =
      new ErrorCode("missingExtensionElement")
      .setInternalReason("Required extension element");

  public final ErrorCode missingFeed =
      new ErrorCode("missingFeed")
      .setInternalReason("Feed not found");

  public final ErrorCode missingFeedProvider =
      new ErrorCode("missingFeedProvider")
      .setInternalReason("No FeedProvider instance");

  public final ErrorCode missingFeedProviderClass =
      new ErrorCode("missingFeedProviderClass")
      .setInternalReason("Missing feedProviderClass attribute");

  public final ErrorCode missingFeedProviderDescription =
      new ErrorCode("missingFeedProviderDescription")
      .setInternalReason(
      "At least one FeedProviderDescription must be specified");

  public final ErrorCode missingFormat =
      new ErrorCode("missingFormat")
      .setInternalReason("missing format");

  public final ErrorCode missingGeneratorClass =
      new ErrorCode("missingGeneratorClass")
      .setInternalReason("Missing generatorClass attribute");

  public final ErrorCode missingHrefAttribute =
      new ErrorCode("missingHrefAttribute")
      .setInternalReason("Link must have an 'href' attribute");

  public final ErrorCode missingLocalName =
      new ErrorCode("missingLocalName")
      .setInternalReason("Missing localName");

  public final ErrorCode missingNameAttribute =
      new ErrorCode("missingNameAttribute")
      .setInternalReason("Missing name attribute for customParam");

  public final ErrorCode missingNamespace =
      new ErrorCode("missingNamespace")
      .setInternalReason("Missing namespace");

  public final ErrorCode missingNamespaceDescription =
      new ErrorCode("missingNamespaceDescription")
      .setInternalReason("No matching NamespaceDescription");

  public final ErrorCode missingPathPrefix =
      new ErrorCode("missingPathPrefix")
      .setInternalReason("pathPrefix is missing");

  public final ErrorCode missingPatternAttribute =
      new ErrorCode("missingPatternAttribute")
      .setInternalReason("Missing pattern attribute for customParam");

  public final ErrorCode missingProviderConstructor =
      new ErrorCode("missingProviderConstructor")
      .setInternalReason("Provider constructor not found");

  public final ErrorCode missingRequiredContent =
      new ErrorCode("missingRequiredContent")
      .setInternalReason("Missing required text content");

  public final ErrorCode missingResourceVersion =
      new ErrorCode("missingResourceVersion")
      .setInternalReason("Missing resource version ID");

  public final ErrorCode missingServiceClass =
      new ErrorCode("missingServiceClass")
      .setInternalReason("Missing serviceClass attribute");

  public final ErrorCode missingShortName =
      new ErrorCode("missingShortName")
      .setInternalReason("missing shortName");

  public final ErrorCode missingSrcAttribute =
      new ErrorCode("missingSrcAttribute")
      .setInternalReason("Missing src attribute");

  public final ErrorCode missingTags =
      new ErrorCode("missingTags")
      .setInternalReason("missing tags");

  public final ErrorCode missingTermAttribute =
      new ErrorCode("missingTermAttribute")
      .setInternalReason("Category must have a 'term' attribute");

  public final ErrorCode missingUriTemplate =
      new ErrorCode("missingUriTemplate")
      .setInternalReason("Missing uriTemplate");

  public final ErrorCode missingVersion =
      new ErrorCode("missingVersion")
      .setInternalReason("Missing version attribute");

  public final ErrorCode mustBeBoolean =
      new ErrorCode("mustBeBoolean")
      .setInternalReason("Attribute must be boolean");

  public final ErrorCode mustExtendBaseEntry =
      new ErrorCode("mustExtendBaseEntry")
      .setInternalReason("entry class must derive from BaseEntry");

  public final ErrorCode mustExtendBaseFeed =
      new ErrorCode("mustExtendBaseFeed")
      .setInternalReason("feed class must derive from BaseFeed");

  public final ErrorCode mustExtendExtensionPoint =
      new ErrorCode("mustExtendExtensionPoint")
      .setInternalReason(
      "Extended classes must extend ExtensionPoint");

  public final ErrorCode mustImplementExtension =
      new ErrorCode("mustImplementExtension")
      .setInternalReason(
      "Extension classes must implement the Extension interface");

  public final ErrorCode nameRequired =
      new ErrorCode("nameRequired")
      .setInternalReason("g:extendedProperty/@name is required");

  public final ErrorCode nameValueRequired =
      new ErrorCode("nameValueRequired")
      .setInternalReason("name must have a value");

  public final ErrorCode noAcceptableType =
      new ErrorCode("noAcceptableType")
      .setInternalReason("No acceptable type available");

  public final ErrorCode noAcceptableLanguage =
      new ErrorCode("noAcceptableLanguage")
      .setInternalReason("No acceptable language available");

  public final ErrorCode noAvailableServers =
      new ErrorCode("noAvailableServers")
      .setInternalReason("Cannot find any servers");

  public final ErrorCode noPostConcurrency =
      new ErrorCode("noPostConcurrency")
      .setInternalReason("POST method does not support concurrency");

  public final ErrorCode notModifiedSinceTimestamp =
      new ErrorCode("notModifiedSinceTimestamp")
      .setInternalReason("Entity not modified since specified time");

  public final ErrorCode nullJsonValue =
      new ErrorCode("nullJsonValue")
      .setInternalReason("Null JSON values not supported");

  public final ErrorCode optionsNotSupported =
      new ErrorCode("optionsNotSupported")
      .setInternalReason("OPTIONS is not supported");
  
  public final ErrorCode optimisticConcurrencyNotSupported =
      new ErrorCode("optimisticConcurrencyNotSupported")
      .setInternalReason("Optimistic concurrency is no longer supported");

  public final ErrorCode pathPrefixValueRequired =
      new ErrorCode("pathPrefixValueRequired")
      .setInternalReason("pathPrefix element must have a value");

  public final ErrorCode predicatesNotAllowed =
      new ErrorCode("predicatesNotAllowed")
      .setInternalReason(
      "Cannot specify any predicates with requested content type");

  public final ErrorCode responseMissingContentType =
      new ErrorCode("responseMissingContentType")
      .setInternalReason("Response contains no content type");

  public final ErrorCode responseMissingEntry =
      new ErrorCode("responseMissingEntry")
      .setInternalReason("Response contains no entry");

  public final ErrorCode responseMissingFeed =
      new ErrorCode("responseMissingFeed")
      .setInternalReason("Response contains no feed");

  public final ErrorCode rpcUnsupported =
      new ErrorCode("rpcUnsupported")
      .setInternalReason("RPC authentication not enabled");

  public final ErrorCode startIndexNotInteger =
      new ErrorCode("startIndexNotInteger")
      .setInternalReason("startIndex is not an integer");

  public final ErrorCode targetFeedReadOnly =
      new ErrorCode("targetFeedReadOnly")
      .setInternalReason("Target feed is read-only");

  public final ErrorCode textNotAllowed =
      new ErrorCode("textNotAllowed")
      .setInternalReason("This element must not have any text() data");

  public final ErrorCode timestampAndEntityTagMatch =
      new ErrorCode("timestampAndEntityTagMatch")
      .setInternalReason("Timestamp and entity tag match");

  public final ErrorCode toDoCompletedRequired =
      new ErrorCode("toDoCompletedRequired")
      .setInternalReason("g:toDo/@completed is required");

  public final ErrorCode tooManyAttributes =
      new ErrorCode("tooManyAttributes")
      .setInternalReason("g:reminder must have zero or one attribute");

  public final ErrorCode totalResultsNotInteger =
      new ErrorCode("totalResultsNotInteger")
      .setInternalReason("totalResults is not an integer");

  public final ErrorCode traceNotSupported =
      new ErrorCode("traceNotSupported")
      .setInternalReason("TRACE is not supported");

  public final ErrorCode unknownMdbService =
      new ErrorCode("unknownMdbService")
      .setInternalReason("Unknown MDB service");

  public final ErrorCode unparsableS2SHeader =
      new ErrorCode("unparsableS2SHeader")
      .setInternalReason("Error parsing S2S auth header");

  public final ErrorCode unrecognizedElement =
      new ErrorCode("unrecognizedElement")
      .setInternalReason("Unrecognized element");

  public final ErrorCode unrecognizedKey =
      new ErrorCode("unrecognizedKey")
      .setInternalReason("Unrecognized key");

  public final ErrorCode unrecognizedParserEvent =
      new ErrorCode("unrecognizedParserEvent")
      .setInternalReason("Unrecognized parser event");

  public final ErrorCode unsupportedContentType =
      new ErrorCode("unsupportedContentType")
      .setInternalReason("Unsupported content type");

  public final ErrorCode unsupportedEncoding =
      new ErrorCode("unsupportedEncoding")
      .setInternalReason("Unsupported encoding");

  public final ErrorCode unsupportedHeader =
      new ErrorCode("unsupportedHeader")
      .setInternalReason("Header not supported");

  public final ErrorCode unsupportedHeaderIfModifiedSince =
      new ErrorCode("unsupportedHeaderIfModifiedSince")
      .setInternalReason("If-Unmodified-Since header not supported");

  public final ErrorCode unsupportedHeaderIfNoneMatch =
      new ErrorCode("unsupportedHeaderIfNoneMatch")
      .setInternalReason("If-None-Match: * is not supported");

  public final ErrorCode unsupportedNullJson =
      new ErrorCode("unsupportedNullJson")
      .setInternalReason("Null JSON values not supported");

  public final ErrorCode unsupportedOutputFormat =
      new ErrorCode("unsupportedOutputFormat")
      .setInternalReason("Unsupported output format");

  public final ErrorCode unsupportedQueryParam =
      new ErrorCode("unsupportedQueryParam")
      .setInternalReason("Query parameter is not supported");

  public final ErrorCode unsupportedQueryRequestType =
      new ErrorCode("unsupportedQueryRequestType")
      .setInternalReason("Unsupported query request type");

  public final ErrorCode unsupportedQueryType =
      new ErrorCode("unsupportedQueryType")
      .setInternalReason("Unsupported query type");

  public final ErrorCode unsupportedTextType =
      new ErrorCode("unsupportedTextType")
      .setInternalReason(
      "Unsupported type. Valid types are 'plain' and 'html'");

  public final ErrorCode updateNotSupported =
      new ErrorCode("updateNotSupported")
      .setInternalReason("Update not supported by feed");

  public final ErrorCode uriValueRequired =
      new ErrorCode("uriValueRequired")
      .setInternalReason("URI must have a value");

  public final ErrorCode urlBaseValueRequired =
      new ErrorCode("urlBaseValueRequired")
      .setInternalReason("urlBase element must have a value");

  public final ErrorCode valueOrAverageRequired =
      new ErrorCode("valueOrAverageRequired")
      .setInternalReason(
      "at least one of g:rating/@value or gd:rating/@average is required");

  public final ErrorCode valueOrXmlRequired =
      new ErrorCode("valueOrXmlRequired")
      .setInternalReason(
      "exactly one of g:extendedProperty/@value, XML is required");

  public final ErrorCode valueXmlMutuallyExclusive =
      new ErrorCode("valueXmlMutuallyExclusive")
      .setInternalReason(
      "g:extendedProperty/@value and XML are mutually exclusive");


  public final ErrorCode whenRequired =
      new ErrorCode("whenRequired")
      .setInternalReason("g:when inside g:originalEvent is required");

  public final ErrorCode whitelistAccessFailed =
      new ErrorCode("whitelistAccessFailed")
      .setInternalReason("Failed to access whitelist");

  public final ErrorCode workspaceRequired =
      new ErrorCode("workspaceRequired")
      .setInternalReason("Service must contain at least one workspace");

  public final ErrorCode workspaceTitleRequired =
      new ErrorCode("workspaceTitleRequired")
      .setInternalReason("Workspace must contain a title");

}
