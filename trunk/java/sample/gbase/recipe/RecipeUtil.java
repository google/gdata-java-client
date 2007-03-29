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

import com.google.api.gbase.client.GoogleBaseService;
import com.google.gdata.util.ServiceException;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Contains the names of the HTML input fields used to edit a recipe.
 * Has methods that are generally useful, for example for logging 
 * or validating values.
 * Has methods to extract values from servlet context init parameters.
 * Has methods used for passing objects from a servlet to a JSP.
 */
public class RecipeUtil {

  public static final String ID_PARAMETER = "oid";
  public static final String APPLICATION_NAME_PARAMETER = "applicationName";
  public static final String TITLE_PARAMETER = "title";
  public static final String URL_PARAMETER = "url";
  public static final String DESCRIPTION_PARAMETER = "description";
  public static final String MAIN_INGREDIENT_PARAMETER = "mainIngredient";
  public static final String CUISINE_PARAMETER = "cuisine";
  public static final String COOKING_TIME_PARAMETER = "cookingTime";
  public static final String DEVELOPER_KEY_PARAMETER = "key";

  public static final String COOKING_TIME_UNIT = "minutes";

  public static final String RECIPE_ATTRIBUTE = "recipe";
  public static final String RECIPESEARCH_ATTRIBUTE = "recipeSearch";
  public static final String MESSAGE_ATTRIBUTE = "message";

  public static final String RECIPE_ITEMTYPE_QUERY = 
      "[item type : recipe | recipes]";

  /** Pattern used for finding the unsupported characters in the query string.*/
  private static final Pattern QUERY_REPLACE_PATTERN = 
      Pattern.compile("\\p{Punct}");

  /**
   * Builds a HashSet containing the specified values,
   * filtering the null and empty ones.
   * 
   * @param values usually an array returned by request.getParameterValues()
   * @return a HashSet containing the nonempty values
   */
  public static Set<String> validateValues(String[] values) {
    Set<String> valuesList = new HashSet<String>();
    if (values != null) {
      for (String value : values) {
        value = cleanQueryString(value);
        if (value != null && ! "".equals(value)) {
          valuesList.add(value);
        }
      }
    }
    return valuesList;
  }
  
  /**
   * Cleans the {@code searchString} set by the user, by removing the special 
   * punctuation characters not allowed directly in a query. 
   * 
   * @param searchString the string set by the user
   * @return the String to be used for executing the query to Google Base.
   */
  public static String cleanQueryString(String searchString) {
    Matcher matcher = QUERY_REPLACE_PATTERN.matcher(searchString);
    return matcher.replaceAll(" ").trim();    
  }
  
  /**
   * Logs an exception in a convenient format,
   * using the log method of a servlet context.
   * 
   * @param servlet the servlet used to log the exception
   * @param e exception to be logged
   */
  public static void logServiceException(HttpServlet servlet,
                                         ServiceException e) {
    if (e.getResponseBody() != null) {
      // Log the full error message and response code. 
      servlet.log(e.getMessage() + " " + 
                  e.getHttpErrorCodeOverride() + " " + 
                  e.getResponseContentType() + ": " + 
                  e.getResponseBody(), e);
    }
  }
  
  /** 
   * Sets a RecipeSearch as an attribute of a HttpServletRequest. 
   * 
   * @param request a request that will be passed to a JSP
   * @param results the RecipeSearch, executed or not
   */
  public static void setRecipeSearch(HttpServletRequest request,
                                     RecipeSearch results) {
    request.setAttribute(RECIPESEARCH_ATTRIBUTE, results);
  }
  
  /** 
   * Gets from a HttpServletRequest a RecipeSearch that was previously set 
   * using {@link #setRecipeSearch}. 
   * If it is missing, a NullPointerException is thrown.
   * 
   * @param request a request passed from a Servlet
   * @return a non-null RecipeSearch
   */
  public static RecipeSearch getRecipeSearch(HttpServletRequest request) {
    RecipeSearch results = (RecipeSearch)request.getAttribute(
        RECIPESEARCH_ATTRIBUTE);
    if (null == results) {
      throw new NullPointerException("recipe search results are missing");
    }
    return results;
  }

  /**
   * Gets the GoogleBaseService object created by {@link AuthenticationFilter}
   * or creates a new one if <code>AuthenticationFilter</code> has not been 
   * applied yet.
   * 
   * @param req
   * @param servletContext
   * @return a GoogleBaseService object
   */
  public static GoogleBaseService getGoogleBaseService(HttpServletRequest req,
          ServletContext servletContext) {
    GoogleBaseService service;
    service = (GoogleBaseService) req.getAttribute(
        AuthenticationFilter.SERVICE_ATTRIBUTE);
    if (service == null) {
      service = new GoogleBaseService(
          servletContext.getInitParameter(APPLICATION_NAME_PARAMETER), 
          servletContext.getInitParameter(DEVELOPER_KEY_PARAMETER));
      req.setAttribute(AuthenticationFilter.SERVICE_ATTRIBUTE, service);
    }
    return service;
  }
  
  /** 
   * Gets a MostUsedValues that was previously set using 
   * {@link #setMostUsedValues}. 
   * 
   * @param servletContext
   * @return a non-null initialized MostUsedValues
   */
  public static MostUsedValues getMostUsedValues(ServletContext servletContext) 
      throws ServletException {
    MostUsedValues mostUsedValues = (MostUsedValues)
        servletContext.getAttribute(RecipeListener.MOST_USED_VALUES_ATTRIBUTE);
    if (null == mostUsedValues) {
      throw new ServletException("Most used values cache is missing");
    }
    return mostUsedValues;
  }

  public static void setMostUsedValues(ServletContext servletContext,
                                       MostUsedValues mostUsedValues) {
    servletContext.setAttribute(RecipeListener.MOST_USED_VALUES_ATTRIBUTE, 
                                mostUsedValues);
  }
}
