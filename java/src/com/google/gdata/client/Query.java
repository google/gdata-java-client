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

import com.google.gdata.util.common.base.CharEscapers;
import com.google.gdata.data.ICategory;
import com.google.gdata.data.DateTime;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Query class is a helper class that aids in the construction of a
 * GData query.  It provides a simple API and object model that exposes
 * query parameters.  Once constructed, the query can be executed against
 * a GData service.
 * <p>
 * The Query class also acts as a simple base class for GData services
 * that support custom query parameters.  These services can subclass
 * the base Query class, add APIs to represent service query parameters,
 * and participate in the Query URI generation process.
 *
 * @see Service#query(Query, Class)
 *
 * 
 */
public class Query {


  /**
   * Magic value indicating that a numeric field is not set.
   */
  public static final int UNDEFINED = -1;


  /**
   * Defines all query return formats.  Return format "json-xd" is
   * not supported.
   */
  public static enum ResultFormat {

    DEFAULT("default"), // default for target resource (Atom or media)
    ATOM("atom"),
    RSS("rss"),
    JSON("json"),
    JSONC("jsonc"),
    ATOM_IN_SCRIPT("atom-in-script"),
    RSS_IN_SCRIPT("rss-in-script"),
    JSON_IN_SCRIPT("json-in-script"),
    JSONC_IN_SCRIPT("jsonc-in-script"),
    JSON_XD("json-xd"),
    ATOM_SERVICE("atom-service");
    
    
    /**
     * Value to use for the "alt" param.
     */
    private String paramValue;

    /**
     * Constructs a new ResultFormat object using a given value to use for the
     * "alt" parameter.
     *
     * @param value value to use for the "alt" parameter.
     */
    private ResultFormat(String value) {
      this.paramValue = value;
    }

    /**
     * Returns the value to use for the "alt" parameter.
     *
     * @return value to use for the "alt" parameter.
     */
    public String paramValue() {
      return paramValue;
    }
  }


  /** Base feed URL against which the query will be applied. */
  private URL feedUrl;


  /** The list of category filters associate with the query. */
  private List<CategoryFilter> categoryFilters =
                                new LinkedList<CategoryFilter>();

  /** Fields partial selection query parameter */
  private String fields;

  /** Full-text search query string. */
  private String queryString;


  /** Author name or e-mail address for matched entries. */
  private String author;


  /** Minimum updated timestamp for matched entries. */
  private DateTime updatedMin;


  /** Maximum updated timestamp for matched entries. */
  private DateTime updatedMax;


  /** Minimum published timestamp for matched entries. */
  private DateTime publishedMin;


  /** Maximum published timestamp for matched entries. */
  private DateTime publishedMax;


  /**
   * The start index for query results.  A value of {@link #UNDEFINED}
   * indicates that no start index has been set.
   */
  private int startIndex = UNDEFINED;


  /**
   * The maximum number of results to return for the query.  A value of
   * {@link #UNDEFINED} indicates the server can determine the maximum size.
   */
  private int maxResults = UNDEFINED;


  /**
   * The expected result format for the query.  The default is
   * {@link ResultFormat#DEFAULT}.
   */
  private ResultFormat resultFormat = ResultFormat.DEFAULT;

  
  /**
   * The strictness of the query parameter parsing on the server.  If strict
   * mode is enabled any unknown query parameters will be rejected.  The
   * default value is false.
   */
  private boolean strict = false;
  
  
  /**
   * The list of custom parameters associated with the query.
   */
  private List<CustomParameter> customParameters =
    new ArrayList<CustomParameter>();


  /**
   * Constructs a new Query object that targets a feed.  The initial
   * state of the query contains no parameters, meaning all entries
   * in the feed would be returned if the query was executed immediately
   * after construction.
   *
   * @param feedUrl the URL of the feed against which queries will be
   *                executed.
   */
  public Query(URL feedUrl) {
    this.feedUrl = feedUrl;
  }


  /**
   * Returns the feed URL of this query.
   * 
   * @return Feed URL.
   */
  public URL getFeedUrl() {
    return feedUrl;
  }

  /**
   * Sets the "fields" partial selection query parameter.
   *
   * @param fields query value
   */
  public void setFields(String fields) {
    this.fields = fields;
  }

  /**
   * Returns the fields query string that will be used for the query.
   */
  public String getFields() {
    return fields;
  }
  
  
  /**
   * Sets the full text query string that will be used for the query.
   *
   * @param query the full text search query string.  A value of
   *                    {@code null} disables full text search for this Query.
   */
  public void setFullTextQuery(String query) {
    this.queryString = query;
  }


  /**
   * Returns the full text query string that will be used for the query.
   */
  public String getFullTextQuery() {
    return queryString;
  }


  /**
   * The CategoryFilter class is used  to define sets of category conditions 
   * that must be met in order for an entry to match.
   * <p>
   * The CategoryFilter can contain multiple category criteria (inclusive
   * or exclusive).  If it does contain multiple categories, then the
   * query matches if any one of the category filter criteria is met,
   * i.e. it is a logical 'OR' of the contained category criteria.   
   * To match, an entry must contain at least one included category or 
   * must not contain at least one excluded category.
   * <p>
   * It is also possible to add multiple CategoryFilters to a Query.  In
   * this case, each individual CategoryFilter must be true for an entry 
   * to match, i.e. it is a logical 'AND' of all CategoryFilters.
   *
   * @see Query#addCategoryFilter(CategoryFilter)
   */
  public static class CategoryFilter {


    /** List of categories that returned entries must match. */
    private final List<ICategory> categories;
    public List<ICategory> getCategories() { return categories; }

    /** List of categories that returned entries must match. */
    private final List<ICategory> excludeCategories;
    public List<ICategory> getExcludeCategories() { return excludeCategories; }


    /**
     * Creates an empty category filter.
     */
    public CategoryFilter() {
      categories = new LinkedList<ICategory>();
      excludeCategories = new LinkedList<ICategory>();
    }

    /**
     * Creates a new category filter using the supplied inclusion and
     * exclusion lists.  A null value for either is equivalent to an
     * empty list.
     */
    public CategoryFilter(List<ICategory> included,
                          List<ICategory> excluded) {

      if (included != null) {
        categories = included;
      } else {
        categories = new LinkedList<ICategory>();
      }
      if (excluded != null) {
        excludeCategories = excluded;
      } else {
        excludeCategories = new LinkedList<ICategory>();
      }
    }

    /**
     * Creates a simple category filter containing only a single
     * {@link ICategory}.
     *
     * @param category an initial category to add to the filter.
     */
    public CategoryFilter(ICategory category) {
      this();
      categories.add(category);
    }


    /**
     * Adds a new {@link ICategory} to the query, indicating that entries
     * containing the category should be considered to match.
     *
     * @param category the category to add to query parameters.
     */
    public void addCategory(ICategory category) {
      categories.add(category);
    }


    /**
     * Adds a new {@link ICategory} to the query, indicating that entries
     * that do not contain the category should be considered to
     * match.
     *
     * @param category the category to add to query parameters.
     */
    public void addExcludeCategory(ICategory category) {
      excludeCategories.add(category);
    }


    private String getQueryString(ICategory category) {
      StringBuilder sb = new StringBuilder();

      String scheme = category.getScheme();
      if (scheme != null) {
        sb.append("{");
        sb.append(scheme);
        sb.append("}");
      }
      sb.append(category.getTerm());

      return sb.toString();
    }


    /**
     * Returns a string representation for the category conditions in
     * the CategoryFilter, in the format used by a Query URI.
     */
    @Override public String toString() {
      StringBuilder sb = new StringBuilder();
      boolean isFirst = true;
      for (ICategory category : categories) {

          if (isFirst) {
            isFirst = false;
          } else {
            sb.append("|");
          }
          sb.append(getQueryString(category));
      }
      for (ICategory category : excludeCategories) {

          if (isFirst) {
            isFirst = false;
          } else {
            sb.append("|");
          }
          sb.append("-");
          sb.append(getQueryString(category));
      }
      return sb.toString();
    }
  }


  /**
   * Adds a new CategoryFilter to the query.   For an entry to match the
   * query criteria, it must match against <b>all</b> CategoryFilters
   * that have been associated with the query.
   */
  public void addCategoryFilter(CategoryFilter categoryFilter) {
    categoryFilters.add(categoryFilter);
  }


  /**
   * Returns the current list of CategoryFilters associated with the query.
   *
   * @return list of category filters.
   */
  public List<CategoryFilter> getCategoryFilters() {
    return Collections.unmodifiableList(categoryFilters);
  }


  /**
   * Sets the author name or email address used for the query.  Only entries
   * with an author whose name or email address match the specified value
   * will be returned.
   *
   * @param author the name or email address for matched entries.  A value of
   *               {@code null} disables author-based matching.
   */
  public void setAuthor(String author) {
    this.author = author;
  }


  /**
   * Returns the author name or email address used for the query.  Only entries
   * with an author whose name or email address match the specified value
   * will be returned.
   *
   * @return the name or email address for matched entries.  A value of
   *          {@code null} means no author-based matching.
   */
  public String getAuthor() {
    return this.author;
  }


  /**
   * Sets the minimum updated timestamp used for the query.  Only entries with
   * an updated timestamp equal to or later than the specified timestamp will be
   * returned.
   *
   * @param updatedMin minimum updated timestamp for matched entries.  A value
   *        of {@code null} disables minimum timestamp filtering.
   */
  public void setUpdatedMin(DateTime updatedMin) {
    this.updatedMin = updatedMin;
  }


  /**
   * Returns the minimum updated timestamp used for this query.   Only entries
   * with an updated timestamp equal to or later than the specified timestamp
   * will be returned.
   *
   * @return minimum updated timestamp for matched entries.  A value of
   *         {@code null} indicates no minimum timestamp.
   */
  public DateTime getUpdatedMin() {
    return this.updatedMin;
  }


  /**
   * Sets the maximum updated timestamp used for the query.  Only entries with
   * an updated timestamp earlier than the specified timestamp will be returned.
   *
   * @param updatedMax maximum updated timestamp for matched entries.  A value
   *        of {@code null} disables maximum timestamp filtering.
   */
  public void setUpdatedMax(DateTime updatedMax) {
    this.updatedMax = updatedMax;
  }


  /**
   * Returns the maximum updated timestamp used for this query.   Only entries
   * with an updated timestamp earlier than the specified timestamp will be
   * returned.
   *
   * @return maximum updated timestamp for matched entries.  A value of
   *         {@code null} indicates no maximum timestamp.
   */
  public DateTime getUpdatedMax() {
    return this.updatedMax;
  }


  /**
   * Sets the minimum published timestamp used for the query.  Only entries with
   * a published time equal to or later than the specified timestamp will be
   * returned.
   *
   * @param publishedMin minimum published timestamp for matched entries.  A
   *        value of {@code null} disables minimum timestamp filtering.
   */
  public void setPublishedMin(DateTime publishedMin) {
    this.publishedMin = publishedMin;
  }


  /**
   * Returns the minimum published timestamp used for this query.   Only entries
   * with a published time equal to or later than the specified timestamp will
   * be returned.
   *
   * @return minimum published timestamp for matched entries.  A value of
   *         {@code null} indicates no minimum timestamp.
   */
  public DateTime getPublishedMin() {
    return this.publishedMin;
  }


  /**
   * Sets the maximum published timestamp used for the query.  Only entries with
   * a published time earlier than the specified timestamp will be returned.
   *
   * @param publishedMax maximum published timestamp for matched entries.  A
   *        value of {@code null} disables maximum timestamp filtering.
   */
  public void setPublishedMax(DateTime publishedMax) {
    this.publishedMax = publishedMax;
  }


  /**
   * Returns the maximum published timestamp used for this query.   Only entries
   * with a published timestamp earlier than the specified timestamp will be
   * returned.
   *
   * @return maximum published timestamp for matched entries.  A value of
   *         {@code null} indicates no maximum timestamp.
   */
  public DateTime getPublishedMax() {
    return this.publishedMax;
  }


  /**
   * Sets the start index for query results.  This is a 1-based index.
   *
   * @param startIndex the start index for query results.
   * @throws IllegalArgumentException if index is less than or equal to zero.
   */
  public void setStartIndex(int startIndex) {

    if (startIndex != UNDEFINED && startIndex < 1) {
      throw new IllegalArgumentException("Start index must be positive");
    }
    this.startIndex = startIndex;
  }


  /**
   * Returns the current start index value for the query,
   * or {@link #UNDEFINED} if start index has not been set.
   */
  public int getStartIndex() {
    return startIndex;
  }


  /**
   * Sets the maximum number of results to return for the query.  Note:
   * a GData server may choose to provide fewer results, but will
   * never provide more than the requested maximum.
   *
   * @param maxResults the maximum number of results to return for the query.
   *                   A value of zero indicates that the server is free
   *                   to determine the maximum value.
   * @throws IllegalArgumentException if the provided value is less than zero.
   */
  public void setMaxResults(int maxResults) {
    if (maxResults != UNDEFINED && maxResults < 0) {
      throw new IllegalArgumentException("Max results must be zero or larger");
    }
    this.maxResults = maxResults;
  }


  /**
   * Returns the maximum number of results to return for the query,
   * or {@link #UNDEFINED} if max results has not been set.
   * <p>
   * Note: a GData server may choose to provide fewer results, but will
   * never provide more than the requested maximum.
   */
  public int getMaxResults() {
    return this.maxResults;
  }


  /**
   * Sets the expected query result format.
   *
   * @param resultFormat ResultFormat value indicating the desired format.
   */
  public void setResultFormat(ResultFormat resultFormat) {
    this.resultFormat = resultFormat;
  }


  /**
   * Returns the query result format.
   *
   * @return ResultFormat associated with the query instance.
   */
  public ResultFormat getResultFormat() {
    return resultFormat;
  }
  
  /**
   * Sets the strictness of parameter parsing.
   * 
   * @param strict true if strict parsing should be enabled for this query.
   */
  public void setStrict(boolean strict) {
    this.strict = strict;
  }
  
  /**
   * Returns the strictness setting for query parameter parsing on the server.
   * 
   * @return true if strict parsing is enabled for this query.
   */
  public boolean isStrict() {
    return strict;
  }

  /**
   * The CustomParameter class defines a base representation for custom query
   * parameters.
   */
  public static class CustomParameter {

    private String name;
    private String value;

    /**
     * Constructs a new custom parameter with the specified name/value
     * pair.
     */
    public CustomParameter(String name, String value) {
      this.name = name;
      this.value = value;
    }

    /**
     * Returns the name of the custom parameter.
     */
    public String getName() { return name; }

    /**
     * Returns the value of the custom parameter.
     */
    public String getValue() { return value; }
  }


  /**
   * Adds a new CustomParameter.
   *
   * @param customParameter the new custom parameter to add.
   */
  public void addCustomParameter(CustomParameter customParameter) {
    if (customParameter == null) {
      throw new NullPointerException("Null custom parameter");
    }
    customParameters.add(customParameter);
  }

  /**
   * Returns the list of custom parameters.
   *
   * @return all custom parameters for the query.  An empty list will be
   *         returned if there are no custom parameters.
   */
  public List<CustomParameter> getCustomParameters() {
    return customParameters;
  }


  /**
   * Returns the list of custom parameters that match a specified name.
   *
   * @param name the name value to match for returned parameters.
   * @return all parameters that have the specified name.  An empty list will
   *         be returned if there are no matching parameters.
   */
  public List<CustomParameter> getCustomParameters(String name) {
    List<CustomParameter> matchList = new ArrayList<CustomParameter>();
    for (CustomParameter param : customParameters) {
      if (param.name.equals(name)) {
        matchList.add(param);
      }
    }
    return matchList;
  }

  /**
   * Appends specified query (parameter, value) to provided query URL buffer.
   *
   * @param queryBuf    base URI buffer to append to.
   * @param paramName   query parameter name.
   * @param paramValue  query parameter value.
   */
  protected void appendQueryParameter(StringBuilder queryBuf, String paramName,
      String paramValue) throws UnsupportedEncodingException {
    queryBuf.append(queryBuf.length() != 0 ? '&' : '?');
    queryBuf.append(paramName);
    queryBuf.append("=");
    queryBuf.append(paramValue);
  }

  /**
   * Check if current query state is supported.
   *
   * @return <code>true</code> if supported.
   */
  public boolean isValidState() {
    // Check if requested ResultFormat is supported
    return (resultFormat != ResultFormat.JSON_XD);
  }

  /**
   * Returns the relative query URI that represents only the query
   * parameters without any components related to the target feed.
   * Subclasses of the Query class may override this method to add
   * additional URI path elements or HTTP query parameters to represent
   * service-specific parameters.
   *
   * @return URI representing current query.
   */
  public URI getQueryUri() {

    if (!isValidState()) {
      throw new IllegalStateException("Unsupported Query");
    }

    StringBuilder pathBuf = new StringBuilder();

    try {

      if (categoryFilters.size() != 0) {

        pathBuf.append("-");   // signals beginning of query path elements
        for (CategoryFilter categoryFilter : categoryFilters) {
          pathBuf.append("/");
          pathBuf.append(
              CharEscapers.uriEscaper().escape(categoryFilter.toString()));
        }
      }

      StringBuilder queryBuf = new StringBuilder();
      if (queryString != null) {
        appendQueryParameter(queryBuf, GDataProtocol.Query.FULL_TEXT,
            CharEscapers.uriEscaper().escape(queryString));
      }

      if (author != null) {
        appendQueryParameter(queryBuf, GDataProtocol.Query.AUTHOR,
            CharEscapers.uriEscaper().escape(author));
      }

      if (resultFormat != ResultFormat.DEFAULT) {
        appendQueryParameter(queryBuf, GDataProtocol.Parameter.ALT, 
            resultFormat.paramValue());
      }

      if (updatedMin != null) {
        appendQueryParameter(queryBuf, GDataProtocol.Query.UPDATED_MIN,
            CharEscapers.uriEscaper().escape(updatedMin.toString()));
      }

      if (updatedMax != null) {
        appendQueryParameter(queryBuf, GDataProtocol.Query.UPDATED_MAX,
            CharEscapers.uriEscaper().escape(updatedMax.toString()));
      }

      if (publishedMin != null) {
        appendQueryParameter(queryBuf, GDataProtocol.Query.PUBLISHED_MIN,
            CharEscapers.uriEscaper().escape(publishedMin.toString()));
      }

      if (publishedMax != null) {
        appendQueryParameter(queryBuf, GDataProtocol.Query.PUBLISHED_MAX,
            CharEscapers.uriEscaper().escape(publishedMax.toString()));
      }

      if (startIndex != UNDEFINED) {
        appendQueryParameter(queryBuf, GDataProtocol.Query.START_INDEX,
            Integer.toString(startIndex));
      }

      if (maxResults != UNDEFINED) {
        appendQueryParameter(queryBuf, GDataProtocol.Query.MAX_RESULTS,
            Integer.toString(maxResults));
      }

      if (fields != null) {
        appendQueryParameter(queryBuf, GDataProtocol.Query.FIELDS,
            CharEscapers.uriEscaper().escape(fields));
      }
      
      if (strict) {
        appendQueryParameter(queryBuf, GDataProtocol.Parameter.STRICT, "true");
      }

      for (CustomParameter customParameter : customParameters) {
        appendQueryParameter(queryBuf,
            CharEscapers.uriEscaper().escape(customParameter.name),
            CharEscapers.uriEscaper().escape(customParameter.value));
      }

      return new URI(pathBuf.toString() + queryBuf.toString());

    } catch (UnsupportedEncodingException uee) {

      throw new IllegalStateException("Unable to encode query URI", uee);

    } catch (URISyntaxException use) {

      // This would indicate a programming error above, not user error
      throw new IllegalStateException("Unable to construct query URI", use);

    }
  }


  /**
   * Returns the Query URL that encapsulates the current state of this
   * query object.
   *
   * @return URL that represents the query against the target feed.
   */
  public URL getUrl() {

    try {
      String queryUri = getQueryUri().toString();
      if (queryUri.length() == 0) {
        return feedUrl;
      }

      // Build the full query URL.  An earlier implementation of this
      // was done using URI.resolve(), but there are issues if both the
      // base and relative URIs contain path components (the last path
      // element on the base will be removed).
      String feedRoot = feedUrl.toString();
      StringBuilder urlBuf = new StringBuilder(feedRoot);
      if (!feedRoot.endsWith("/") && !queryUri.startsWith("?")) {
        urlBuf.append('/');
      }
      urlBuf.append(queryUri);

      return new URL(urlBuf.toString());

    // Since we are combining a valid URL and a valid URI,
    // any exception thrown below is not a user error.
    } catch (MalformedURLException mue) {
      throw new IllegalStateException("Unable to create query URL", mue);
    }
  }

  /**
   * Sets a string custom parameter, with null signifying to clear the
   * parameter.
   *
   * @param name the name of the parameter
   * @param value the value to set it to
   */
  public final void setStringCustomParameter(String name, String value) {
    List<CustomParameter> customParams = getCustomParameters();

    for (CustomParameter existingValue : getCustomParameters(name)) {
      customParams.remove(existingValue);
    }

    if (value != null) {
      customParams.add(new CustomParameter(name, value));
    }
  }

  /**
   * Gets an existing String custom parameter, with null signifying that the
   * parameter is not specified.
   *
   * @param name the name of the parameter
   * @return the value, or null if there is no parameter
   */
  public final String getStringCustomParameter(String name) {
    List<CustomParameter> params = getCustomParameters(name);

    if (params.size() == 0) {
      return null;
    } else {
      return params.get(0).getValue();
    }
  }

  /**
   * Sets an integer custom paramter, with null signifying to clear the
   * parameter.
   * 
   * @param name the parameter name
   * @param value the value to set it to
   */
  public final void setIntegerCustomParameter(String name, Integer value) {
    if (value == null) {
      setStringCustomParameter(name, null);
    } else {
      setStringCustomParameter(name, value.toString());
    }
  }

  /**
   * Gets an existing Integer custom paramter, with null signifying that
   * the parameter is not specified or not an integer.
   * 
   * @param name the name of the parameter
   * @return the value of the parameter, or null if it is unspecified
   *         or non-integer
   */
  public final Integer getIntegerCustomParameter(String name) {
    String strValue = getStringCustomParameter(name);
    Integer intValue;

    if (strValue != null) {
      try {
        intValue = Integer.valueOf(Integer.parseInt(strValue));
      } catch (NumberFormatException nfe) {
        intValue = null;
      }
    } else {
      intValue = null;
    }

    return intValue;
  }
}
