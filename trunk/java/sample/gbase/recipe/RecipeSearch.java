/* Copyright (c) 2006 Google Inc.
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

package sample.gbase.recipe;

import com.google.api.gbase.client.FeedURLFactory;
import com.google.api.gbase.client.GoogleBaseEntry;
import com.google.api.gbase.client.GoogleBaseFeed;
import com.google.api.gbase.client.GoogleBaseQuery;
import com.google.api.gbase.client.GoogleBaseService;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * A recipe search.
 *
 * There will be such an object in all cases, even
 * if no query has been run. This object is created
 * by RecipeSearchServlet and displayed by the JSP.
 */
public class RecipeSearch {  
  private final GoogleBaseService service;
  private Set<String> mainIngredient;
  private Set<String> cuisine;
  private Integer cookingTime;
  private String query;
  
  /** The query string, with the unsupported characters replaced with spaces.*/
  private String queryClean;

  /** The index of the first retrieved recipe. */
  private int startIndex = 0;
  
  /** Set to true to perform the search on the user's items. */
  private boolean ownItems;

  /** Total number of results, -1 means the query hasn't been run yet. */
  protected int total = -1;
  
  protected List<Recipe> recipes;

  private static final int DEFAULT_MAX_RESULTS = 10;
  private int maxResults = DEFAULT_MAX_RESULTS;

  private final FeedURLFactory urlFactory;

  /**
   * Create a new search.
   *
   * @param service Google data API service
   * @param urlFactory feed URL factory to be used when creating a Query
   * @param ownItems true to show only the items of the authenticated user
   */
  public RecipeSearch(GoogleBaseService service, 
                      FeedURLFactory urlFactory, 
                      boolean ownItems) {
    this.service = service;
    this.urlFactory = urlFactory;
    this.ownItems = ownItems;
    
    mainIngredient = null;
    cuisine = null;
    cookingTime = null;
    query = null;
    queryClean = null;
  }

  /**
   * Checks whether the search has been run and there is at least one result.
   *
   * @return true if the search has been run and there is at least one result
   */
  public boolean hasResults() {
    return recipes != null && ! recipes.isEmpty();
  }

  /**
   * Gets the result of the search, if it has been run.
   *
   * @return a list of Recipe which might be empty if the
   *   search has been run, or null if the search has not
   *   been run yet
   */
  public List<Recipe> getRecipes() {
    return recipes;
  }

  /**
   * Checks if the search will be done for the authenticated user's items only.
   * 
   * @return true if the search returns only the items that belong to the
   *         authenticated user
   */
  public boolean isOwnItems() {
    return ownItems;
  }

  /**
   * Specifies if we are searching the authenticated user's items.
   * 
   * @param ownItems true to search only the authenticated user's items
   */
  public void setOwnItems(boolean ownItems) {
    this.ownItems = ownItems;
  }

  /** Gets the current main ingredients, or null. */
  public Set<String> getMainIngredientValues() {
    return mainIngredient;
  }

  /** Sets the main ingredient. */
  public void setMainIngredientValues(String[] mainIngredient) {
    this.mainIngredient = RecipeUtil.validateValues(mainIngredient);
  }

  /** Gets the current cuisines, or null. */
  public Set<String> getCuisineValues() {
    return cuisine;
  }

  /** Sets the cuisine. */
  public void setCuisineValues(String[] cuisine) {
    this.cuisine = RecipeUtil.validateValues(cuisine);
  }

  /** Gets the current (maximum) cooking time, or null. */
  public Integer getCookingTime() {
    return cookingTime;
  }

  /** Sets the current maximum cooking time. */
  public void setCookingTime(Integer cookingTime) {
    this.cookingTime = cookingTime;
  }

  /** Gets the current page length. */
  public int getMaxResults() {
    return maxResults;
  }

  /** Sets the page length. */
  public void setMaxResults(int maxResults) {
    this.maxResults = maxResults;
  }

  /**
   * Gets the total number of recipes that matched the query, which
   * might be larger than the page length.
   *
   * @return the total, or -1 if the total is unknown, either because
   *   the query has not been run or because the total was not in
   *   the result
   */
  public int getTotal() {
    return total;
  }

  /** 
   * Gets the index of the first result to return. 
   * 
   * @return a positive value
   */
  public int getStartIndex() {
    return startIndex;
  }

  /** 
   * Sets the index of the first result to return. 
   * @param startIndex a positive value
   */
  public void setStartIndex(int startIndex) {
    this.startIndex = startIndex;
  }

  /** Gets the current page. */
  public int getCurrentPage() {
    return startIndex / maxResults;
  }

  /**
   * Gets a description of the retrieved (current) interval, showing
   * the index of the first item and the index of the last item.
   * 
   * @return short description of the current page interval
   */
  public String getCurrentPageInterval() {
    return "" + (startIndex + 1) + " - " +
        Math.min(startIndex + maxResults, total);
  }
  
  /** Gets the number of pages needed to contain all the results. */
  public int getTotalPages() {
    if (total == 0) {
      return 0;
    }
    /* Using total-1, because if we have 10 results and 10 items per page,
     * we still want one page.
     */
    return (total - 1) / maxResults;
  }

  /** Runs the query and fills the result. */
  public void runQuery() throws IOException, ServiceException {
    GoogleBaseQuery query = createQuery();
    System.out.println("Searching: " + query.getUrl());
    GoogleBaseFeed feed = service.query(query);
    List<Recipe> result = new ArrayList<Recipe>(maxResults);
    for (GoogleBaseEntry entry : feed.getEntries()) {
      result.add(new Recipe(entry));
    }
    this.recipes = result;
    total = feed.getTotalResults();
  }

  /**
   * Creates a GoogleBaseQuery that searches for recipes, according to 
   * the various properties of the RecipeSearch.
   * 
   * @return a query to be used for querying with a 
   *         {@link com.google.api.gbase.client.GoogleBaseService 
   *         GoogleBaseService}
   * @see com.google.api.gbase.client.GoogleBaseService#query(com.google.gdata.client.Query)
   */
  private GoogleBaseQuery createQuery() {
    URL queryUrl;
    if (ownItems) {
      queryUrl = urlFactory.getItemsFeedURL();
    } else {
      queryUrl = urlFactory.getSnippetsFeedURL();
    }
    GoogleBaseQuery query = new GoogleBaseQuery(queryUrl);
    query.setMaxResults(maxResults);
    if (startIndex > 0) {
      // the first index is 1
      query.setStartIndex(startIndex + 1);
    }
    query.setGoogleBaseQuery(createQueryString());
    return query;
  }

  /**
   * Creates a full text query out of the values of the query, mainIngredient, 
   * cuisine and cookingTime.
   * 
   * @return a query to be used for setting the full text query of a 
   *         {@link com.google.api.gbase.client.GoogleBaseQuery}
   * @see com.google.api.gbase.client.GoogleBaseQuery#setFullTextQuery(String)
   */
  private String createQueryString() {
    StringBuffer retval = new StringBuffer(RecipeUtil.RECIPE_ITEMTYPE_QUERY);
    if (queryClean != null) {
      retval.append(queryClean);
    }
    appendAttributeCondition(retval, "main ingredient", mainIngredient, true);
    appendAttributeCondition(retval, "cuisine", cuisine, false);
    if (cookingTime != null) {
      Collection<String> cookingTimes = new ArrayList<String>();
      cookingTimes.add("0.." + cookingTime + " min");
      cookingTimes.add("0.." + cookingTime + " minutes");
      appendAttributeCondition(retval, "cooking time", cookingTimes, false);
    }
    return retval.toString();
  }
  
  /**
   * Appends a filtering condition to a full text query.
   * It is composed by simple [name: value] conditions 
   * joined by and AND or an OR operation.
   * 
   * @param sb a StringBuffer for creating a full text query
   * @param name name of the attributes
   * @param values values the attributes have to match
   * @param isAnd true if the attributes have to match all the values,
   *              false if the attributes have to match at least one value
   */
  private static void appendAttributeCondition(StringBuffer sb, 
                                               String name, 
                                               Collection<String> values, 
                                               boolean isAnd) {
    if (values != null && !values.isEmpty()) {
      sb.append(" (");
      Iterator iter = values.iterator();
      while (iter.hasNext()) {
        sb.append("[").append(name).append(": ").append(iter.next()).append("]");
        if (iter.hasNext()) {
          sb.append(isAnd ? " " : "|");
        }
      }
      sb.append(")");
    }
  }

  /** Returns true when the current page is not the first page. */
  public boolean hasPreviousPage() {
    return getCurrentPage() > 0;
  }
  
  /** Returns true when the current page is not the last one. */
  public boolean hasNextPage() {
    return getTotalPages() > getCurrentPage();
  }

  /** Gets the expected number of recipes for the next page. */
  public int getNextPageSize() {
    return Math.min(maxResults, total - (getCurrentPage() + 1) * maxResults);
  }

  /** Gets a description of the query used in the search. */
  public StringBuffer getFilterDescription() {
    StringBuffer retval = new StringBuffer();
    if (queryClean != null && ! "".equals(queryClean)) {
      retval.append("<b>keywords</b> are <b>").
          append(queryClean).
          append("</b> ");
    }
    addCollectionDescription(retval, cuisine, "cuisine", false);
    addCollectionDescription(retval, mainIngredient, "main ingredient", true);
    if (cookingTime != null) {
      if (retval.length() > 0) {
        retval.append("and ");
      }
      retval.append("<b>cooking time</b> is under <b>").
          append(cookingTime).
          append(" ").
          append(RecipeUtil.COOKING_TIME_UNIT).
          append("</b> ");
    }
    if (retval.length() > 0) {
      retval.insert(0, "where ");
    }
    return retval;
  }

  private static void addCollectionDescription(StringBuffer buffer,
                                               Collection<String> collection, 
                                               String name,
                                               boolean isAnd) {
    if (collection != null && ! collection.isEmpty()) {
      if (buffer.length() > 0) {
        buffer.append("and ");
      }
      buffer.append("<b>").append(name).append("</b> is");
      Iterator<String> iter = collection.iterator();
      while (iter.hasNext()) {
        buffer.append(" <b>").append(iter.next()).append("</b> ");
        if (iter.hasNext()) {
          buffer.append(isAnd ? "and " : "or ");
        }
      }
    }
  }
  
  /**
   * Sets the query string.
   * 
   * @param query the query string, as provided by user
   * @throws NullPointerException if the {@code query} is null.
   */
  public void setQuery(String query) {
    if (query != null) {
      this.query = query;      
      this.queryClean = RecipeUtil.cleanQueryString(query);
    } else {
      throw new NullPointerException("Query must not be null.");
    }
  }

  /**
   * Returns the original query string, as specified in the 
   * {@link #setQuery(String)} method.
   */
  public String getQuery() {
    return query;
  }  
}
