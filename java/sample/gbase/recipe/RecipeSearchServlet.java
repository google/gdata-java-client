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
import com.google.api.gbase.client.GoogleBaseService;
import com.google.gdata.util.ServiceException;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Setup a {@link RecipeSearch} object and optionally fill it.
 */
@SuppressWarnings("serial")
public class RecipeSearchServlet extends HttpServlet  {

  private static final String START_INDEX_PARAMETER = "startIndex";

  private static final String MAX_RESULTS_PARAMETER = "maxResults";

  private static final String QUERY_PARAMETER = "query";

  public static final String DISPLAY_JSP = "/WEB-INF/recipeSearch.jsp";
  
  protected boolean ownItems;

  protected FeedURLFactory urlFactory;
  
  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    ServletContext context = servletConfig.getServletContext();
    urlFactory = (FeedURLFactory) 
        context.getAttribute(RecipeListener.FEED_URL_FACTORY_ATTRIBUTE);
    String scope = servletConfig.getInitParameter("scope");
    ownItems = "own".equals(scope);  
  }

  @Override
  public void destroy() {
    super.destroy();
  }


  /**
   * Runs a recipe search.
   *
   * @param request
   * @param response
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response)
      throws ServletException, IOException {
    GoogleBaseService service = RecipeUtil.getGoogleBaseService(request, 
        this.getServletContext());
    RecipeSearch recipeSearch;
    try {
      if (request.getParameter("query") == null) {
        recipeSearch = new RecipeSearch(service, urlFactory, ownItems);
      } else {
        recipeSearch = createRecipeSearch(service, request);
      }
      recipeSearch.runQuery();
    } catch (RecipeValidationException rve) {
      // internal exception in the argument handling (possibly incorrect args)
      RecipeUtil.forwardToErrorPage(request, response, rve.getMessage());
      return;
    } catch (ServiceException se) {
      // exception comming from Google Base
      RecipeUtil.logServiceException(this, se);
      RecipeUtil.forwardToErrorPage(request, response, se);
      return;
    } 
    
    RecipeUtil.setRecipeSearch(request, recipeSearch);
    // Forward to the JSP
    request.getRequestDispatcher(DISPLAY_JSP).forward(request, response);
  }

  /**
   * Creates and fills in a {@link RecipeSearch} object based on the content 
   * of the {@code request}.
   *
   * @param service the object used to connect to the Google Base service
   * @param request the representation of the Http request
   * @throws RecipeValidationException if a parameter from the request has an
   *    invalid value (cooking time, start index, max results are not valid
   *    numbers).
   */
  private RecipeSearch createRecipeSearch(GoogleBaseService service,
                                          HttpServletRequest request) 
      throws RecipeValidationException {
    RecipeSearch search = new RecipeSearch(service, urlFactory, ownItems);

    String query = request.getParameter(QUERY_PARAMETER);
    if (isSet(query)) {
      search.setQuery(query);
    }

    String[] mainIngredient = request.getParameterValues(
        RecipeUtil.MAIN_INGREDIENT_PARAMETER);
    if (isSet(mainIngredient)) {
      search.setMainIngredientValues(mainIngredient);
    }

    String[] cuisine = request.getParameterValues(
        RecipeUtil.CUISINE_PARAMETER);
    if (isSet(cuisine)) {
      search.setCuisineValues(cuisine);
    }

    String cookingTime = request.getParameter(
        RecipeUtil.COOKING_TIME_PARAMETER);
    if (isSet(cookingTime)) {
      try {
        search.setCookingTime(new Integer(cookingTime));
      } catch (NumberFormatException e) {
        throw new RecipeValidationException(String.format(
            "Cooking time is not a number (%s).", cookingTime));
      }
    }

    String startIndex = request.getParameter(START_INDEX_PARAMETER);
    if (isSet(startIndex)) {
      try {
        search.setStartIndex(Integer.parseInt(startIndex));
      } catch (NumberFormatException e) {
        throw new RecipeValidationException(String.format(
            "Start index is not a number (%s).", startIndex));
      }
    }

    String maxResults = request.getParameter(MAX_RESULTS_PARAMETER);
    if (isSet(maxResults)) {
      try {
        search.setMaxResults(Integer.parseInt(maxResults));
      } catch (NumberFormatException e) {
        throw new RecipeValidationException(String.format(
            "Max results is not a number (%s).", maxResults));
      }
    }

    search.setOwnItems(ownItems);

    return search;
  }

  private boolean isSet(String parameter) {
    return parameter != null && !"".equals(parameter);
  }
  
  private boolean isSet(String[] parameter) {
    return parameter != null && parameter.length > 0;
  }
}
