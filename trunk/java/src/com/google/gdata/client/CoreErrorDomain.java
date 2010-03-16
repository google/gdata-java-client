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
      .withInternalReason("Version is not supported");

  public final ErrorCode accountDeleted =
      new ErrorCode("accountDeleted")
      .withInternalReason("Account deleted");

  public final ErrorCode accountDisabled =
      new ErrorCode("accountDisabled")
      .withInternalReason("Account disabled");

  public final ErrorCode accountUnverified =
      new ErrorCode("accountUnverified")
      .withInternalReason("Account unverified");

  public final ErrorCode atomFormatRequired =
      new ErrorCode("atomFormatRequired")
      .withInternalReason("Batch requires Atom format");

  public final ErrorCode batchingNotSupported =
      new ErrorCode("batchingNotSupported")
      .withInternalReason("Batching not supported by feed");

  public final ErrorCode cantAccessFeedData =
      new ErrorCode("cantAccessFeedData")
      .withInternalReason("Unable to access feed data");

  public final ErrorCode cantCreateContentGenerator =
      new ErrorCode("cantCreateContentGenerator")
      .withInternalReason("Unable to create ContentGenerator instance");

  public final ErrorCode cantCreateEntry =
      new ErrorCode("cantCreateEntry")
      .withInternalReason("Unable to create entry");

  public final ErrorCode cantCreateExtension =
      new ErrorCode("cantCreateExtension")
      .withInternalReason("Unable to create extension");

  public final ErrorCode cantCreateFeed =
      new ErrorCode("cantCreateFeed")
      .withInternalReason("Unable to create feed");

  public final ErrorCode cantCreateProvider =
      new ErrorCode("cantCreateProvider")
      .withInternalReason("Unable to instantiate provider");

  public final ErrorCode cantDecodeCategoryQuery =
      new ErrorCode("cantDecodeCategoryQuery")
      .withInternalReason("Unable to decode category query");
  
  public final ErrorCode cannotEditResource =
      new ErrorCode("cannotEditResource")
      .withInternalReason("Target resource cannot be edited by client");

  public final ErrorCode cantEncodeQueryParams =
      new ErrorCode("cantEncodeQueryParams")
      .withInternalReason("Unable to encode query parameters");

  public final ErrorCode cantExtractJsonValue =
      new ErrorCode("cantExtractJsonValue")
      .withInternalReason("Cannot extract JSON value");

  public final ErrorCode cantLoadAuthProviderClass =
      new ErrorCode("cantLoadAuthProviderClass")
      .withInternalReason("authProvider class cannot be loaded");

  public final ErrorCode cantLoadEntryClass =
      new ErrorCode("cantLoadEntryClass")
      .withInternalReason("entry class cannot be loaded");

  public final ErrorCode cantLoadExtensionClass =
      new ErrorCode("cantLoadExtensionClass")
      .withInternalReason(
      "Extension classes must implement the Extension interface");

  public final ErrorCode cantLoadExtensionPoint =
      new ErrorCode("cantLoadExtensionPoint")
      .withInternalReason(
      "Unable to load ExtensionPoint class");

  public final ErrorCode cantLoadFeedClass =
      new ErrorCode("cantLoadFeedClass")
      .withInternalReason("feed class cannot be loaded");

  public final ErrorCode cantLoadFeedProviderClass =
      new ErrorCode("cantLoadFeedProviderClass")
      .withInternalReason("feedProvider class cannot be loaded");

  public final ErrorCode cantLoadGeneratorClass =
      new ErrorCode("cantLoadGeneratorClass")
      .withInternalReason("Unable to load ContentGenerator class");

  public final ErrorCode cantLoadKindAdaptor =
      new ErrorCode("cantLoadKindAdaptor")
      .withInternalReason("Unable to load kind adaptor");

  public final ErrorCode cantLoadServiceClass =
      new ErrorCode("cantLoadServiceClass")
      .withInternalReason("Unable to load serviceClass class");

  public final ErrorCode cantWriteMimeMultipart =
      new ErrorCode("cantWriteMimeMultipart")
      .withInternalReason("Unable to write MIME multipart message");

  public final ErrorCode clientNotWhitelisted =
      new ErrorCode("clientNotWhitelisted")
      .withInternalReason("Client not whitelisted for ONLINE access");

  public final ErrorCode collectionTitleRequired =
      new ErrorCode("collectionTitleRequired")
      .withInternalReason("Collection must contain a title");

  public final ErrorCode commentsFeedLinkRequired =
      new ErrorCode("commentsFeedLinkRequired")
      .withInternalReason("g:comments/g:feedLink is required");

  public final ErrorCode deleteNotSupported =
      new ErrorCode("deleteNotSupported")
      .withInternalReason("Delete not supported by feed");

  public final ErrorCode duplicateAlt =
      new ErrorCode("duplicateAlt")
      .withInternalReason("Duplicate alt mapping");

  public final ErrorCode duplicateAttribute =
      new ErrorCode("duplicateAttribute")
      .withInternalReason("Duplicate attribute");

  public final ErrorCode duplicateAttributeValue =
      new ErrorCode("duplicateAttributeValue")
      .withInternalReason("Duplicate attribute value");

  public final ErrorCode duplicateContent =
      new ErrorCode("duplicateContent")
      .withInternalReason("Duplicate content");

  public final ErrorCode duplicateDraft =
      new ErrorCode("duplicateDraft")
      .withInternalReason("Duplicate draft");

  public final ErrorCode duplicateEmail =
      new ErrorCode("duplicateEmail")
      .withInternalReason("Duplicate email");

  public final ErrorCode duplicateEntryId =
      new ErrorCode("duplicateEntryId")
      .withInternalReason("Duplicate entry ID");

  public final ErrorCode duplicateExtension =
      new ErrorCode("duplicateExtension")
      .withInternalReason("Duplicate extension element");

  public final ErrorCode duplicateFeedId =
      new ErrorCode("duplicateFeedId")
      .withInternalReason("Duplicate feed ID");

  public final ErrorCode duplicateGenerator =
      new ErrorCode("duplicateGenerator")
      .withInternalReason("Duplicate generator");

  public final ErrorCode duplicateIcon =
      new ErrorCode("duplicateIcon")
      .withInternalReason("Duplicate icon");

  public final ErrorCode duplicateItemsPerPage =
      new ErrorCode("duplicateItemsPerPage")
      .withInternalReason("Duplicate itemsPerPage");

  public final ErrorCode duplicateLogo =
      new ErrorCode("duplicateLogo")
      .withInternalReason("Duplicate logo");

  public final ErrorCode duplicateName =
      new ErrorCode("duplicateName")
      .withInternalReason("Duplicate name");

  public final ErrorCode duplicatePathPrefix =
      new ErrorCode("duplicatePathPrefix")
      .withInternalReason("Duplicate pathPrefix element");

  public final ErrorCode duplicateRights =
      new ErrorCode("duplicateRights")
      .withInternalReason("Duplicate rights");

  public final ErrorCode duplicateStartIndex =
      new ErrorCode("duplicateStartIndex")
      .withInternalReason("Duplicate startIndex");

  public final ErrorCode duplicateSubtitle =
      new ErrorCode("duplicateSubtitle")
      .withInternalReason("Duplicate subtitle");

  public final ErrorCode duplicateSummary =
      new ErrorCode("duplicateSummary")
      .withInternalReason("Duplicate summary");

  public final ErrorCode duplicateTextNodeValue =
      new ErrorCode("duplicateTextNodeValue")
      .withInternalReason("Duplicate text node value");

  public final ErrorCode duplicateTitle =
      new ErrorCode("duplicateTitle")
      .withInternalReason("Duplicate title");

  public final ErrorCode duplicateTotalResults =
      new ErrorCode("duplicateTotalResults")
      .withInternalReason("Duplicate totalResults");

  public final ErrorCode duplicateUri =
      new ErrorCode("duplicateUri")
      .withInternalReason("Duplicate URI");

  public final ErrorCode duplicateUrlBase =
      new ErrorCode("duplicateUrlBase")
      .withInternalReason("Duplicate urlBase element");

  public final ErrorCode duplicateValue =
      new ErrorCode("duplicateValue")
      .withInternalReason("Duplicate value");

  public final ErrorCode elementNotRepeatable =
      new ErrorCode("elementNotRepeatable")
      .withInternalReason("Element is not repeatable");

  public final ErrorCode elementNotSimple =
      new ErrorCode("elementNotSimple")
      .withInternalReason("Element is not simple");

  public final ErrorCode emailValueRequired =
      new ErrorCode("emailValueRequired")
      .withInternalReason("email must have a value");

  public final ErrorCode entityTagMatches =
      new ErrorCode("entityTagMatches")
      .withInternalReason("At least one entity tag matches");

  public final ErrorCode entryNotAssociated =
      new ErrorCode("entryNotAssociated")
      .withInternalReason("Entry is not associated with a GData service");

  public final ErrorCode etagsMismatch =
      new ErrorCode("etagsMismatch")
      .withInternalReason("Etags mismatch");

  public final ErrorCode etagsUnsupported =
      new ErrorCode("etagsUnsupported")
      .withInternalReason("Resource does not support Etags");

  public final ErrorCode feedNotAssociated =
      new ErrorCode("feedNotAssociated")
      .withInternalReason("Feed is not associated with a GData service");

  public final ErrorCode geoPtLatRequired =
      new ErrorCode("geoPtLatRequired")
      .withInternalReason("g:geoPt/@lat is required");

  public final ErrorCode geoPtLonRequired =
      new ErrorCode("geoPtLonRequired")
      .withInternalReason("g:geoPt/@lon is required");

  public final ErrorCode headerRequired =
      new ErrorCode("headerRequired")
      .withInternalReason("Header required");

  public final ErrorCode iconValueRequired =
      new ErrorCode("iconValueRequired")
      .withInternalReason("icon must have a value");

  public final ErrorCode idRequired =
      new ErrorCode("idRequired")
      .withInternalReason("g:originalEvent/@id is required");

  public final ErrorCode idValueRequired =
      new ErrorCode("idValueRequired")
      .withInternalReason("ID must have a value");

  public final ErrorCode illegalInputFormat =
      new ErrorCode("illegalInputFormat")
      .withInternalReason(
      "Input format is not compatible with selected alt output format");
  
  public final ErrorCode imsNotSupported =
      new ErrorCode("imsNotSupported")
      .withInternalReason(
      "If-Modified-Since HTTP precondition not supported on POST");

  public final ErrorCode incompatiblePaginationParameters =
      new ErrorCode("incompatiblePaginationParameters")
      .withInternalReason("start-token and start-index cannot both "
          + "be specified at the same time");
  
  public final ErrorCode incorrectDataVersion =
      new ErrorCode("incorrectDataVersion");
  
  public final ErrorCode insertNotSupported =
      new ErrorCode("insertNotSupported")
      .withInternalReason("Insert not supported by feed");

  public final ErrorCode insufficientSecurityLevel =
      new ErrorCode("insufficientSecurityLevel")
      .withInternalReason("Insufficient security level");

  public final ErrorCode invalidAltValue =
      new ErrorCode("invalidAltValue")
      .withInternalReason("Invalid alt value for entry");

  public final ErrorCode invalidArbitraryXml =
      new ErrorCode("invalidArbitraryXml")
      .withInternalReason("Invalid value for arbitrary XML");

  public final ErrorCode invalidAttributeValue =
      new ErrorCode("invalidAttributeValue")
      .withInternalReason("Invalid value for attribute");

  public final ErrorCode invalidAverageRatingAttribute =
      new ErrorCode("invalidAverageRatingAttribute")
      .withInternalReason(
      "gd:rating/@average should lie in between " +
      "gd:rating/@min and gd:rating/@max");

  public final ErrorCode invalidBase64 =
      new ErrorCode("invalidBase64")
      .withInternalReason("Expected Base-64 content");

  public final ErrorCode invalidBatchOperationType =
      new ErrorCode("invalidBatchOperationType")
      .withInternalReason("Invalid type for batch:operation");

  public final ErrorCode invalidBigDecimalAttribute =
      new ErrorCode("invalidBigDecimalAttribute")
          .withInternalReason("Invalid value for big decimal attribute");

  public final ErrorCode invalidBigIntegerAttribute =
      new ErrorCode("invalidBigIntegerAttribute")
          .withInternalReason("Invalid value for big integer attribute");

  public final ErrorCode invalidBooleanAttribute =
      new ErrorCode("invalidBooleanAttribute")
      .withInternalReason("Invalid value for boolean attribute");

  public final ErrorCode invalidByteAttribute =
      new ErrorCode("invalidByteAttribute")
          .withInternalReason("Invalid value for byte attribute");

  public final ErrorCode invalidCacheControlOption =
      new ErrorCode("invalidCacheControlOption")
      .withInternalReason("Invalid option in Cache-Control header");

  public final ErrorCode invalidCategoryFilter =
      new ErrorCode("invalidCategoryFilter")
      .withInternalReason("Invalid category filter");

  public final ErrorCode invalidChildElement =
      new ErrorCode("invalidChildElement")
      .withInternalReason("Child elements are not allowed.");
  
  public final ErrorCode invalidContentType =
      new ErrorCode("invalidContentType")
      .withInternalReason("Malformed Content-Type");

  public final ErrorCode invalidCountHintAttribute =
      new ErrorCode("invalidCountHintAttribute")
      .withInternalReason("Invalid gd:feedLink/@countHint");

  public final ErrorCode invalidDatetime =
      new ErrorCode("invalidDatetime")
      .withInternalReason("Badly formatted datetime");

  public final ErrorCode invalidDoubleAttribute =
      new ErrorCode("invalidDoubleAttribute")
      .withInternalReason("Invalid value for double attribute");

  public final ErrorCode invalidDraft =
      new ErrorCode("invalidDraft")
      .withInternalReason("Invalid value for draft");

  public final ErrorCode invalidEndValue =
      new ErrorCode("invalidEndValue")
      .withInternalReason("Invalid end value");

  public final ErrorCode invalidEnumValue =
      new ErrorCode("invalidEnumValue")
      .withInternalReason("Invalid enum value");
  
  public final ErrorCode invalidErrorFormat =
      new ErrorCode("invalidErrorFormat")
      .withInternalReason("Invalid error format");
  
  public final ErrorCode invalidExtension =
      new ErrorCode("invalidExtension")
      .withInternalReason("Invalid extension element");
  
  public final ErrorCode invalidFieldSelection =
      new ErrorCode("invalidFieldSelection")      
      .withInternalReason("Invalid field selection");

  
  public final ErrorCode invalidFixedAttribute =
      new ErrorCode("invalidFixedAttribute")
      .withInternalReason("Invalid value for fixed attribute");

  public final ErrorCode invalidFloatAttribute =
      new ErrorCode("invalidFloatAttribute")
      .withInternalReason("Invalid value for float attribute");

  public final ErrorCode invalidGeoPtElev =
      new ErrorCode("invalidGeoPtElev")
      .withInternalReason("Invalid geoPt/@elev");

  public final ErrorCode invalidGeoPtLat =
      new ErrorCode("invalidGeoPtLat")
      .withInternalReason("Invalid geoPt/@lat");

  public final ErrorCode invalidGeoPtLon =
      new ErrorCode("invalidGeoPtLon")
      .withInternalReason("Invalid geoPt/@lon");

  public final ErrorCode invalidGeoPtTime =
      new ErrorCode("invalidGeoPtTime")
      .withInternalReason("Date/time value expected");

  public final ErrorCode invalidIntegerAttribute =
      new ErrorCode("invalidIntegerAttribute")
      .withInternalReason("Invalid value for integer attribute");

  public final ErrorCode invalidJson =
      new ErrorCode("invalidJson")
      .withInternalReason("Invalid JSON");

  public final ErrorCode invalidLongAttribute =
      new ErrorCode("invalidLongAttribute")
      .withInternalReason("Invalid value for long attribute");

  public final ErrorCode invalidMediaSourceUri =
      new ErrorCode("invalidMediaSourceUri")
      .withInternalReason("Invalid media source URI");

  public final ErrorCode invalidMediaType =
      new ErrorCode("invalidMediaType")
      .withInternalReason("Not a valid media type");

  public final ErrorCode invalidMethodOverrideHeader =
      new ErrorCode("invalidMethodOverrideHeader")
      .withInternalReason("Invalid method override header");

  public final ErrorCode invalidMimeType =
      new ErrorCode("invalidMimeType")
      .withInternalReason("Malformed MIME type");

  public final ErrorCode invalidMixedContent =
      new ErrorCode("invalidMixedContent")
      .withInternalReason("Invalid value for mixed content");

  public final ErrorCode invalidParameterValue =
      new ErrorCode("invalidParameterValue")
      .withInternalReason("Invalid parameter value");

  public final ErrorCode invalidRedirectedToUrl =
      new ErrorCode("invalidRedirectedToUrl")
      .withInternalReason("Invalid redirected-to URL");
  
  public final ErrorCode invalidPatchTarget =
    new ErrorCode("invalidPatchTarget")
    .withInternalReason("Target resource cannot be patched");

  public final ErrorCode invalidReminderAbsoluteTime =
      new ErrorCode("invalidReminderAbsoluteTime")
      .withInternalReason("Invalid g:reminder/@absoluteTime");

  public final ErrorCode invalidReminderDays =
      new ErrorCode("invalidReminderDays")
      .withInternalReason("Invalid g:reminder/@days");

  public final ErrorCode invalidReminderHours =
      new ErrorCode("invalidReminderHours")
      .withInternalReason("Invalid g:reminder/@hours");

  public final ErrorCode invalidReminderMethod =
      new ErrorCode("invalidReminderMethod")
      .withInternalReason("Invalid g:reminder/@method");

  public final ErrorCode invalidReminderMinutes =
      new ErrorCode("invalidReminderMinutes")
      .withInternalReason("Invalid g:reminder/@minutes");

  public final ErrorCode invalidRequestUri =
      new ErrorCode("invalidRequestUri")
      .withInternalReason("Invalid request URI");

  public final ErrorCode invalidRequestVersion =
      new ErrorCode("invalidRequestVersion")
      .withInternalReason("Invalid request version");

  public final ErrorCode invalidResourceVersion =
      new ErrorCode("invalidResourceVersion")
      .withInternalReason("Unexpected resource version ID");

  public final ErrorCode invalidSecurityProtocol =
      new ErrorCode("invalidSecurityProtocol")
      .withInternalReason("Invalid security protocol");

  public final ErrorCode invalidServiceClass =
      new ErrorCode("invalidServiceClass")
      .withInternalReason("Invalid service class attribute");

  public final ErrorCode invalidShortAttribute =
      new ErrorCode("invalidShortAttribute")
          .withInternalReason("Invalid value for short attribute");

  public final ErrorCode invalidStartValue =
      new ErrorCode("invalidStartValue")
      .withInternalReason("Invalid start value");

  public final ErrorCode invalidTextContent =
      new ErrorCode("invalidTextContent")
      .withInternalReason("Invalid text content");
  
  public final ErrorCode invalidTextContentType =
      new ErrorCode("invalidTextContentType")
      .withInternalReason("Invalid text content type");

  public final ErrorCode invalidTimeOffset =
      new ErrorCode("invalidTimeOffset")
      .withInternalReason("Invalid time offset");

  public final ErrorCode invalidToDoDueTime =
      new ErrorCode("invalidToDoDueTime")
      .withInternalReason("Invalid g:toDo/@dueTime");

  public final ErrorCode invalidToDoHours =
      new ErrorCode("invalidToDoHours")
      .withInternalReason("Invalid g:toDo/@hours");

  public final ErrorCode invalidUri =
      new ErrorCode("invalidUri")
      .withInternalReason("Badly formatted URI");

  public final ErrorCode invalidUriTemplate =
      new ErrorCode("invalidUriTemplate")
      .withInternalReason("Invalid uriTemplate");

  public final ErrorCode invalidUrl =
      new ErrorCode("invalidUrl")
      .withInternalReason("Badly formatted URL");

  public final ErrorCode invalidValueRatingAttribute =
      new ErrorCode("invalidValueRatingAttribute")
      .withInternalReason(
      "gd:rating/@value should lie in between " +
      "gd:rating/@min and gd:rating/@max");

  public final ErrorCode invalidVersion =
      new ErrorCode("invalidVersion")
      .withInternalReason("Invalid version");

  public final ErrorCode itemsPerPageNotInteger =
      new ErrorCode("itemsPerPageNotInteger")
      .withInternalReason("itemsPerPage is not an integer");

  public final ErrorCode lengthNotInteger =
      new ErrorCode("lengthNotInteger")
      .withInternalReason("Length must be an integer");

  public final ErrorCode logoValueRequired =
      new ErrorCode("logoValueRequired")
      .withInternalReason("logo must have a value");

  public final ErrorCode matchHeaderRequired =
      new ErrorCode("matchHeaderRequired")
      .withInternalReason("If-Match or If-None-Match header required");

  public final ErrorCode minGreaterThanMax =
      new ErrorCode("minGreaterThanMax")
      .withInternalReason(
      "'updatedMin' must be less than or equal to 'updatedMax'");

  public final ErrorCode missingAddressAttribute =
      new ErrorCode("missingAddressAttribute")
      .withInternalReason("g:email/@address is required");

  public final ErrorCode missingAltAttribute =
      new ErrorCode("missingAltAttribute")
      .withInternalReason("Missing alt attribute");

  public final ErrorCode missingAttribute =
      new ErrorCode("missingAttribute")
      .withInternalReason("Missing attribute");

  public final ErrorCode missingContact =
      new ErrorCode("missingContact")
      .withInternalReason("missing contact");

  public final ErrorCode missingContentType =
      new ErrorCode("missingContentType")
      .withInternalReason("Response contains no content type");

   public final ErrorCode missingContentTypeAttribute =
      new ErrorCode("missingContentTypeAttribute")
      .withInternalReason("Missing content type attribute");

  public final ErrorCode missingConverter =
      new ErrorCode("missingConverter")
      .withInternalReason("No converter for type");

  public final ErrorCode missingDescription =
      new ErrorCode("missingDescription")
      .withInternalReason("missing description");

  public final ErrorCode missingEntry =
      new ErrorCode("missingEntry")
      .withInternalReason("Entry not found");

  public final ErrorCode missingExtensionClass =
      new ErrorCode("missingExtensionClass")
      .withInternalReason("Missing extensionClass attribute");

  public final ErrorCode missingExtensionElement =
      new ErrorCode("missingExtensionElement")
      .withInternalReason("Required extension element");

  public final ErrorCode missingFeed =
      new ErrorCode("missingFeed")
      .withInternalReason("Feed not found");

  public final ErrorCode missingFeedProvider =
      new ErrorCode("missingFeedProvider")
      .withInternalReason("No FeedProvider instance");

  public final ErrorCode missingFeedProviderClass =
      new ErrorCode("missingFeedProviderClass")
      .withInternalReason("Missing feedProviderClass attribute");

  public final ErrorCode missingFeedProviderDescription =
      new ErrorCode("missingFeedProviderDescription")
      .withInternalReason(
      "At least one FeedProviderDescription must be specified");

  public final ErrorCode missingFormat =
      new ErrorCode("missingFormat")
      .withInternalReason("missing format");

  public final ErrorCode missingGeneratorClass =
      new ErrorCode("missingGeneratorClass")
      .withInternalReason("Missing generatorClass attribute");

  public final ErrorCode missingHrefAttribute =
      new ErrorCode("missingHrefAttribute")
      .withInternalReason("Link must have an 'href' attribute");

  public final ErrorCode missingLocalName =
      new ErrorCode("missingLocalName")
      .withInternalReason("Missing localName");

  public final ErrorCode missingNameAttribute =
      new ErrorCode("missingNameAttribute")
      .withInternalReason("Missing name attribute for customParam");

  public final ErrorCode missingNamespace =
      new ErrorCode("missingNamespace")
      .withInternalReason("Missing namespace");

  public final ErrorCode missingNamespaceDescription =
      new ErrorCode("missingNamespaceDescription")
      .withInternalReason("No matching NamespaceDescription");

  public final ErrorCode missingPathPrefix =
      new ErrorCode("missingPathPrefix")
      .withInternalReason("pathPrefix is missing");

  public final ErrorCode missingPatternAttribute =
      new ErrorCode("missingPatternAttribute")
      .withInternalReason("Missing pattern attribute for customParam");

  public final ErrorCode missingProviderConstructor =
      new ErrorCode("missingProviderConstructor")
      .withInternalReason("Provider constructor not found");

  public final ErrorCode missingRequiredContent =
      new ErrorCode("missingRequiredContent")
      .withInternalReason("Missing required text content");

  public final ErrorCode missingResourceVersion =
      new ErrorCode("missingResourceVersion")
      .withInternalReason("Missing resource version ID");

  public final ErrorCode missingServiceClass =
      new ErrorCode("missingServiceClass")
      .withInternalReason("Missing serviceClass attribute");

  public final ErrorCode missingShortName =
      new ErrorCode("missingShortName")
      .withInternalReason("missing shortName");

  public final ErrorCode missingSrcAttribute =
      new ErrorCode("missingSrcAttribute")
      .withInternalReason("Missing src attribute");

  public final ErrorCode missingTags =
      new ErrorCode("missingTags")
      .withInternalReason("missing tags");

  public final ErrorCode missingTermAttribute =
      new ErrorCode("missingTermAttribute")
      .withInternalReason("Category must have a 'term' attribute");

  public final ErrorCode missingTextContent =
      new ErrorCode("missingTextContent")
      .withInternalReason("Text content is required for this element.");
  
  public final ErrorCode missingUriTemplate =
      new ErrorCode("missingUriTemplate")
      .withInternalReason("Missing uriTemplate");

  public final ErrorCode missingVersion =
      new ErrorCode("missingVersion")
      .withInternalReason("Missing version attribute");

  public final ErrorCode mustBeBoolean =
      new ErrorCode("mustBeBoolean")
      .withInternalReason("Attribute must be boolean");

  public final ErrorCode mustExtendBaseEntry =
      new ErrorCode("mustExtendBaseEntry")
      .withInternalReason("entry class must derive from BaseEntry");

  public final ErrorCode mustExtendBaseFeed =
      new ErrorCode("mustExtendBaseFeed")
      .withInternalReason("feed class must derive from BaseFeed");

  public final ErrorCode mustExtendExtensionPoint =
      new ErrorCode("mustExtendExtensionPoint")
      .withInternalReason(
      "Extended classes must extend ExtensionPoint");

  public final ErrorCode mustImplementExtension =
      new ErrorCode("mustImplementExtension")
      .withInternalReason(
      "Extension classes must implement the Extension interface");

  public final ErrorCode nameRequired =
      new ErrorCode("nameRequired")
      .withInternalReason("g:extendedProperty/@name is required");

  public final ErrorCode nameValueRequired =
      new ErrorCode("nameValueRequired")
      .withInternalReason("name must have a value");

  public final ErrorCode noAcceptableType =
      new ErrorCode("noAcceptableType")
      .withInternalReason("No acceptable type available");

  public final ErrorCode noAcceptableLanguage =
      new ErrorCode("noAcceptableLanguage")
      .withInternalReason("No acceptable language available");

  public final ErrorCode noAvailableServers =
      new ErrorCode("noAvailableServers")
      .withInternalReason("Cannot find any servers");

  public final ErrorCode noPostConcurrency =
      new ErrorCode("noPostConcurrency")
      .withInternalReason("POST method does not support concurrency");

  public final ErrorCode notModifiedSinceTimestamp =
      new ErrorCode("notModifiedSinceTimestamp")
      .withInternalReason("Entity not modified since specified time");

  public final ErrorCode nullJsonValue =
      new ErrorCode("nullJsonValue")
      .withInternalReason("Null JSON values not supported");

  public final ErrorCode optionsNotSupported =
      new ErrorCode("optionsNotSupported")
      .withInternalReason("OPTIONS is not supported");
  
  public final ErrorCode optimisticConcurrencyNotSupported =
      new ErrorCode("optimisticConcurrencyNotSupported")
      .withInternalReason("Optimistic concurrency is no longer supported");
  
  public final ErrorCode partialJsoncUnsupported =
    new ErrorCode("partialJsonUnsupported")
    .withInternalReason("Partial operations are not suppported with JSONC");

  public final ErrorCode pathPrefixValueRequired =
      new ErrorCode("pathPrefixValueRequired")
      .withInternalReason("pathPrefix element must have a value");

  public final ErrorCode predicatesNotAllowed =
      new ErrorCode("predicatesNotAllowed")
      .withInternalReason(
      "Cannot specify any predicates with requested content type");

  public final ErrorCode quotaExceeded =
      new ErrorCode("quotaExceeded")
      .withInternalReason("Insufficient storage quota");

  public final ErrorCode rateLimitExceeded =
      new ErrorCode("rateLimitExceeded")
      .withInternalReason("Rate limit exceeded, lower query rate");

  public final ErrorCode responseMissingContentType =
      new ErrorCode("responseMissingContentType")
      .withInternalReason("Response contains no content type");

  public final ErrorCode responseMissingEntry =
      new ErrorCode("responseMissingEntry")
      .withInternalReason("Response contains no entry");

  public final ErrorCode responseMissingFeed =
      new ErrorCode("responseMissingFeed")
      .withInternalReason("Response contains no feed");

  public final ErrorCode rpcUnsupported =
      new ErrorCode("rpcUnsupported")
      .withInternalReason("RPC authentication not enabled");

  public final ErrorCode serverOverloaded =
      new ErrorCode("serverOverloaded")
      .withInternalReason("Servers are overloaded");

  public final ErrorCode startIndexNotInteger =
      new ErrorCode("startIndexNotInteger")
      .withInternalReason("startIndex is not an integer");

  public final ErrorCode targetFeedReadOnly =
      new ErrorCode("targetFeedReadOnly")
      .withInternalReason("Target feed is read-only");

  public final ErrorCode textNotAllowed =
      new ErrorCode("textNotAllowed")
      .withInternalReason("This element must not have any text() data");

  public final ErrorCode timestampAndEntityTagMatch =
      new ErrorCode("timestampAndEntityTagMatch")
      .withInternalReason("Timestamp and entity tag match");

  public final ErrorCode toDoCompletedRequired =
      new ErrorCode("toDoCompletedRequired")
      .withInternalReason("g:toDo/@completed is required");

  public final ErrorCode tooManyAttributes =
      new ErrorCode("tooManyAttributes")
      .withInternalReason("g:reminder must have zero or one attribute");

  public final ErrorCode totalResultsNotInteger =
      new ErrorCode("totalResultsNotInteger")
      .withInternalReason("totalResults is not an integer");

  public final ErrorCode traceNotSupported =
      new ErrorCode("traceNotSupported")
      .withInternalReason("TRACE is not supported");

  public final ErrorCode unknownMdbService =
      new ErrorCode("unknownMdbService")
      .withInternalReason("Unknown MDB service");

  public final ErrorCode unparsableS2SHeader =
      new ErrorCode("unparsableS2SHeader")
      .withInternalReason("Error parsing S2S auth header");

  public final ErrorCode unrecognizedElement =
      new ErrorCode("unrecognizedElement")
      .withInternalReason("Unrecognized element");

  public final ErrorCode unrecognizedKey =
      new ErrorCode("unrecognizedKey")
      .withInternalReason("Unrecognized key");

  public final ErrorCode unrecognizedParserEvent =
      new ErrorCode("unrecognizedParserEvent")
      .withInternalReason("Unrecognized parser event");

  public final ErrorCode unsupportedContentType =
      new ErrorCode("unsupportedContentType")
      .withInternalReason("Unsupported content type");

  public final ErrorCode unsupportedEncoding =
      new ErrorCode("unsupportedEncoding")
      .withInternalReason("Unsupported encoding");
  
  public final ErrorCode unsupportedFieldsParam =
    new ErrorCode("unsupportedFieldsParam")
    .withInternalReason("Fields query parameter is not supported");

  public final ErrorCode unsupportedHeader =
      new ErrorCode("unsupportedHeader")
      .withInternalReason("Header not supported");

  public final ErrorCode unsupportedHeaderIfModifiedSince =
      new ErrorCode("unsupportedHeaderIfModifiedSince")
      .withInternalReason("If-Unmodified-Since header not supported");

  public final ErrorCode unsupportedHeaderIfNoneMatch =
      new ErrorCode("unsupportedHeaderIfNoneMatch")
      .withInternalReason("If-None-Match: * is not supported");

  public final ErrorCode unsupportedNullJson =
      new ErrorCode("unsupportedNullJson")
      .withInternalReason("Null JSON values not supported");

  public final ErrorCode unsupportedOutputFormat =
      new ErrorCode("unsupportedOutputFormat")
      .withInternalReason("Unsupported output format");

  public final ErrorCode unsupportedQueryParam =
      new ErrorCode("unsupportedQueryParam")
      .withInternalReason("Query parameter is not supported");

  public final ErrorCode unsupportedQueryRequestType =
      new ErrorCode("unsupportedQueryRequestType")
      .withInternalReason("Unsupported query request type");

  public final ErrorCode unsupportedQueryType =
      new ErrorCode("unsupportedQueryType")
      .withInternalReason("Unsupported query type");

  public final ErrorCode unsupportedTextType =
      new ErrorCode("unsupportedTextType")
      .withInternalReason(
      "Unsupported type. Valid types are 'plain' and 'html'");

  public final ErrorCode updateNotSupported =
      new ErrorCode("updateNotSupported")
      .withInternalReason("Update not supported by feed");

  public final ErrorCode updateRequiresFullRepresentation =
      new ErrorCode("updateRequiresFullRepresentation")
      .withInternalReason("PUT requires a full resource representation.  " +
            "Use PATCH to update using a partial representation"); 
  
  public final ErrorCode uriValueRequired =
      new ErrorCode("uriValueRequired")
      .withInternalReason("URI must have a value");

  public final ErrorCode urlBaseValueRequired =
      new ErrorCode("urlBaseValueRequired")
      .withInternalReason("urlBase element must have a value");

  public final ErrorCode valueOrAverageRequired =
      new ErrorCode("valueOrAverageRequired")
      .withInternalReason(
      "at least one of g:rating/@value or gd:rating/@average is required");

  public final ErrorCode valueOrXmlRequired =
      new ErrorCode("valueOrXmlRequired")
      .withInternalReason(
      "exactly one of g:extendedProperty/@value, XML is required");

  public final ErrorCode valueXmlMutuallyExclusive =
      new ErrorCode("valueXmlMutuallyExclusive")
      .withInternalReason(
      "g:extendedProperty/@value and XML are mutually exclusive");


  public final ErrorCode whenRequired =
      new ErrorCode("whenRequired")
      .withInternalReason("g:when inside g:originalEvent is required");

  public final ErrorCode whitelistAccessFailed =
      new ErrorCode("whitelistAccessFailed")
      .withInternalReason("Failed to access whitelist");

  public final ErrorCode workspaceRequired =
      new ErrorCode("workspaceRequired")
      .withInternalReason("Service must contain at least one workspace");

  public final ErrorCode workspaceTitleRequired =
      new ErrorCode("workspaceTitleRequired")
      .withInternalReason("Workspace must contain a title");
  
  public final ErrorCode uploadTooLarge = 
      new ErrorCode("uploadTooLarge")
      .withInternalReason("The requested upload is too large");
}