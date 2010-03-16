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

/**
 * The GDataProtocol class defines various constant values used within the
 * Google Data API.
 */
public class GDataProtocol {
  
  // Not instantiable, container class only
  private GDataProtocol() {}
  
  /**
   * The Method interface defines extended method names used within the GData
   * protocol.
   */
  public interface Method {
    /**
     * The PATCH method is used to request a GData partial update, where parts
     * of the target resource can be updated or deleted.
     */
    public static final String PATCH = "PATCH";
  }

  /**
   * The Header interface defines various header names used within the GData
   * protocol.
   */
  public interface Header {

    /**
     * The ACCEPT_LANGUAGE header is used to choose the best language for 
     * the output when the parameter {@code hl} is not specified.
     */
    public static final String ACCEPT_LANGUAGE = "Accept-Language";

    /**
     * The ETAG header is used to return an HTTP entity tag for a resource in
     * a GData response.
     */
    public static final String ETAG = "ETag";

    /**
     * The IF_MATCH header is used to request specific entity versions. The
     * value of the parameter will be a space-separated list of Etags.
     */
    public static final String IF_MATCH = "If-Match";

    /**
     * The IF_MODIFIED_SINCE header is used to reject requests unmodified
     * since the given date.
     */
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";

    /**
     * The IF_UNMODIFIED_SINCE header is used to reject requests modified
     * since the given date.
     */
    public static final String IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    /**
     * The IF_NONE_MATCH header is used to disallow specific entity versions. The
     * value of the parameter will be a space-separated list of Etags.
     */
    public static final String IF_NONE_MATCH = "If-None-Match";
    

    /**
     * The ETAG header is used to return an last modification time for a
     * resource in a GData response.
     */
    public static final String LAST_MODIFIED = "Last-Modified";

    /**
     * The METHOD_OVERRIDE parameter overrides the normal HTTP method. The value
     * is the HTTP method that should be used to process the request. This is
     * used to allow clients that are unable to issue PUT or DELETE methods to
     * emulate such methods. The client would issue a POST method with this
     * header value set to "PUT" or "DELETE", and the service translates the
     * invocation to the corresponding request type.
     */
    public static final String METHOD_OVERRIDE = "X-HTTP-Method-Override";

    /**
     * The VERSION header is used to request a specific service version. The
     * value of the parameter will be in the format {@code <major>.<minor>} and
     * will be interpreted relative to the version model of the target service.
     */
    public static final String VERSION = "GData-Version";

    /**
     * The CACHE_CONTROL header is used to specify how fresh a result should
     * be in a request.
     */
    public static final String CACHE_CONTROL  = "Cache-Control";

    /**
     * The X_UPLOAD_CONTENT_TYPE header is used in Scotty requests
     * for the content type of the uploaded file.
     */
    public static final String X_UPLOAD_CONTENT_TYPE = "X-Upload-Content-Type";

    /**
     * The X_UPLOAD_CONTENT_LENGTH header is used in Scotty requests
     * for the content length of the uploaded file.
     */
    public static final String X_UPLOAD_CONTENT_LENGTH = "X-Upload-Content-Length";

  }

  /**
   * The Parameters interface defines basic query parameter names used within
   * the Data protocol.
   */
  public interface Parameter {

    /**
     * The ALT parameter is used to request an alternate output representation.
     */
    public static final String ALT = "alt";

    /**
     * The VERSION parameter used to request a specific version.  The value of
     * the parameter will be in the format {@code <major>.<minor>} and will be
     * interpreted relative to the version model of the target service.   The
     * {@link Header#VERSION} header (if present on the same request) will have
     * precedence.
     */
    public static final String VERSION = "v";
    
    /**
     * The STRICT parameter is used to tell the server to parse in strict mode,
     * which will reject any unknown query parameters or xml content.
     */
    public static final String STRICT = "strict";

    /**
     * The HL query parameter selects the language of the
     * automatically-generated text in the result. The value is a language code
     * as defined by BCP 47.
     */
    public static final String LANGUAGE = "hl";
    
    /**
     * The PRETTYPRINT parameter requests that character-based output 
     * representations have their content be pretty-printed for debugging.  A
     * value of "true" selects pretty-printing, otherwise responses will not be
     * formatted.
     */
    public static final String PRETTYPRINT = "prettyprint";
    
    /**
     * The CALLBACK parameter is used to specify the name of the 
     * JavaScript callback function that should be invoked when the response is
     * received for in-script response formats.   The default callback (if
     * unspecified) is "gdata.io.handleScriptLoaded".
     */
    public static final String CALLBACK = "callback";

    /**
     * The REQID parameter is forwarded to a JavaScript callback function
     * that is invoked. It must be a number. By default no reqid is available
     * so the callback function will only get one parameter.
     */
    public static final String REQID = "reqid";
    
    /**
     * The CONTEXT parameter is added into the envelope of any JSONC element
     * that is output. It can be any string.
     */
    public static final String CONTEXT = "context";
    
    /**
     * The FIELDS parameters is used to specify a field selection that will be
     * used to provide a partial response containing only the selected fields.
     */
    public static final String FIELDS = "fields";
    
    /**
     * The XOAUTH_REQUESTOR_ID parameter is used in 2 Legged OAuth to specify
     * the email address of the user whose data is being accessed.
     */
    public static final String XOAUTH_REQUESTOR_ID = "xoauth_requestor_id";
  }

  /**
   * The Query interface extends the basic set of parameter names with the
   * common set that are used for GData feed queries.
   */
  public interface Query extends Parameter {

    /**
     * The FULL_TEXT query parameter used to request a full text query.
     */
    public static final String FULL_TEXT = "q";

    /**
     * The AUTHOR query parameter is used to request an author query.
     */
    public static final String AUTHOR = "author";

    /**
     * The UPDATED_MIN query parameter is used to request that all entries have
     * an updated value greater than or equal to the parameter value.
     */
    public static final String UPDATED_MIN = "updated-min";

    /**
     * The UPDATED_MAX query parameter is used to request that all entries have
     * an updated value less than the parameter value.
     */
    public static final String UPDATED_MAX = "updated-max";

    /**
     * The PUBLISHED_MIN query parameter is used to request that all entries
     * have a published value greater than or equal to the parameter value.
     */
    public static final String PUBLISHED_MIN = "published-min";

    /**
     * The PUBLISHED_MAX query parameter is used to request that all entries
     * have a published value less than the parameter value.
     */
    public static final String PUBLISHED_MAX = "published-max";

    /**
     * The START_INDEX query parameter is used to request that the returned
     * entries start at the specified index. The parameter value is 1-based.
     */
    public static final String START_INDEX = "start-index";

    /**
     * The START_TOKEN query parameter is used to request a specific page of
     * a feed. This is an alternative to START_INDEX, which allows
     * accessing the next/previous page of a feed but does not allow
     * accessing arbitrary pages. Its content is an opaque string which
     * was generated by the server to be included in a future request.
     *
     * <p>It is illegal to specify both START_TOKEN and START_INDEX for
     * one request.
     */
    public static final String START_TOKEN = "start-token";
    
    /**
     * The MAX_RESULTS query parameter is used to specify the maximum number of
     * matches that should be returned from a query request.
     */
    public static final String MAX_RESULTS = "max-results";
    
    /**
     * The CATEGORY query parameter is an alternative syntax that can be used
     * to specify a category query that is used to match returned entries.  The
     * value is a comma-separated list of category query expressions that all
     * must match.
     */
    public static final String CATEGORY = "category";
  }
  
  /**
   * The Error interface defines GData protocol response codes that are not
   * defined in the base HTTP specification.
   */
  public interface Error {
    
    /**
     * The UNPROCESSABLE_ENTITY error is defined in the
     * <a href="http://tools.ietf.org/html/rfc4918#section-11.2">WebDAV</a>
     * specification and is returned in cases where a request is understood but
     * its execution would leave the system or a resource in an inconsistent
     * state.
     */
    public static final int UNPROCESSABLE_ENTITY = 422;
  }
  
  /**
   * Returns {@code true} if an entity tag value is a weak Etag, as defined in
   * {@code RFC2616, Section 3.11}.   Weak Etags can be used for weak 
   * comparisons, such as in some caching scenarios, but may not be used for
   * strong validation (like update preconditions).
   * 
   * @param etag entity tag value.
   * @return {@code true} if value is a weak Etag.
   */
  public static boolean isWeakEtag(String etag) {
    return etag != null && etag.startsWith("W/");
  }
}
