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
import com.google.api.gbase.client.GoogleBaseService;
import com.google.api.gbase.client.NumberUnit;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.Set;


import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Inserts, updates or deletes a recipe, 
 * depending on the "action" servlet initialization parameter.
 */
@SuppressWarnings("serial")
public class RecipeActionServlet extends HttpServlet {

  public static final String DISPLAY_JSP = "/WEB-INF/recipeEdit.jsp";
  
  private static final int ACTION_ADD = 0;
  private static final int ACTION_UPDATE = 1;
  private static final int ACTION_DELETE = 2;

  protected FeedURLFactory urlFactory;
  
  /** The operation this servlet has to perform. */
  protected int action = -1;
  
  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    ServletContext context = servletConfig.getServletContext();
    urlFactory = (FeedURLFactory) 
        context.getAttribute(RecipeListener.FEED_URL_FACTORY_ATTRIBUTE);
    String action = servletConfig.getInitParameter("action");
    if ("add".equals(action)) {
      this.action = ACTION_ADD;
    } else if ("update".equals(action)) {
      this.action = ACTION_UPDATE;
    } else if ("delete".equals(action)) {
      this.action = ACTION_DELETE;
    } else {
      throw new ServletException("Unknown action: " + action);
    }
  }
  
  private boolean isAdd() { return ACTION_ADD == action; }
  private boolean isUpdate() { return ACTION_UPDATE == action; }
  private boolean isDelete() { return ACTION_DELETE == action; }

  @Override
  public void destroy() {
    super.destroy();
  }
  
  /** Inserts or updates the submitted recipe and redirects to recipeList. */
  @Override
  protected void doPost(HttpServletRequest request,
                        HttpServletResponse response)
      throws ServletException, IOException {
    GoogleBaseService service = (GoogleBaseService) request.getAttribute(
        AuthenticationFilter.SERVICE_ATTRIBUTE);
    Recipe recipe = getPostedRecipe(request);
    if (!recipe.isComplete()) {
      String message = "<div class='errormessage'>Please fill out " +
                       "all the mandatory fields.</div>";
      editRecipe(request, response, recipe, message);
    } else {
      try {
        if (isAdd()) {
          recipeAdd(service, recipe);
        } else if (isUpdate()) {
          recipeUpdate(service, recipe);
        } else {
          throw new ServletException("Unknown POST action: " + action);
        }
      } catch (ServiceException e) {
        RecipeUtil.logServiceException(this, e);
        RecipeUtil.forwardToErrorPage(request, response, e);
        return;
      }
      listOwnRecipes(response);
    }
  }

  /** Redirect to the page that lists customer's recipes. */
  protected void listOwnRecipes(HttpServletResponse response) 
      throws IOException {
    String redirectUrl = "recipeList";
    response.sendRedirect(response.encodeRedirectURL(redirectUrl));
  }

  /**
   * Inserts a recipe using the specified authenticated service.
   * 
   * @param service an authenticated GoogleBaseService
   * @param recipe recipe to be inserted
   * @throws IOException
   * @throws ServiceException
   */
  protected void recipeAdd(GoogleBaseService service,
                           Recipe recipe) 
    throws IOException, ServiceException {
    URL feedUrl = urlFactory.getItemsFeedURL();
    GoogleBaseEntry entry = recipe.toGoogleBaseEntry(null);
    service.insert(feedUrl, entry);
  }
  
  /**
   * Updates a recipe using the specified authenticated service.
   * 
   * The recipe must have a valid GoogleBase id.
   * 
   * @param service an authenticted GoogleBaseService
   * @param recipe recipe to be updated
   * @throws ServiceException
   * @throws IOException
   */
  protected void recipeUpdate(GoogleBaseService service,
                              Recipe recipe) 
      throws ServiceException, IOException {
    URL feedUrl = urlFactory.getItemsEntryURL(recipe.getId());
    GoogleBaseEntry entry = recipe.toGoogleBaseEntry(feedUrl.toString());
    service.update(feedUrl, entry);
  }

  /**
   * Uses the specified authenticated service to delete a recipe.
   * 
   * @param service an authenticated service
   * @param id the id of the recipe
   * @throws ServiceException
   * @throws IOException
   */
  protected void recipeDelete(GoogleBaseService service, 
                              String id)
      throws ServiceException, IOException {
    URL feedUrl = urlFactory.getItemsEntryURL(id);
    service.delete(feedUrl);
  }

  /**
   * Builds a Recipe from the parameters submitted with the specified request.
   * 
   * @param request http request to be processed
   * @return a submitted recipe
   */
  static Recipe getPostedRecipe(HttpServletRequest request) {
    String id = getNullIfEmpty(request.getParameter(RecipeUtil.ID_PARAMETER));
    String title = getNullIfEmpty(request.getParameter(
      RecipeUtil.TITLE_PARAMETER));
    String url = getNullIfEmpty(request.getParameter(RecipeUtil.URL_PARAMETER));
    String description = getNullIfEmpty(request.getParameter(
      RecipeUtil.DESCRIPTION_PARAMETER));
    Set<String> mainIngredient = RecipeUtil.validateValues(
        request.getParameterValues(RecipeUtil.MAIN_INGREDIENT_PARAMETER));
    Set<String> cuisine = RecipeUtil.validateValues(
        request.getParameterValues(RecipeUtil.CUISINE_PARAMETER));
    NumberUnit<Integer> cookingTime;
    try {
      cookingTime = new NumberUnit<Integer>(
          new Integer(request.getParameter(RecipeUtil.COOKING_TIME_PARAMETER)),
          RecipeUtil.COOKING_TIME_UNIT);
    } catch (Exception e) {
      // If anything goes bad, we set cookingTime to null
      cookingTime = null;
    }    
    Recipe recipe = new Recipe(id,
                               title, 
                               url, 
                               description, 
                               mainIngredient,
                               cuisine,
                               cookingTime);
    return recipe;
  }
  
  static private String getNullIfEmpty(String s) {
    return s != null && "".equals(s) ? null : s;
  }
  
  
  /** Shows the page for inserting or updating a recipe or deletes a recipe. */
  @Override
  protected void doGet(HttpServletRequest request,
                       HttpServletResponse response)
      throws ServletException, IOException {
    GoogleBaseService service = (GoogleBaseService) request.getAttribute(
        AuthenticationFilter.SERVICE_ATTRIBUTE);
    String id = request.getParameter(RecipeUtil.ID_PARAMETER);
    try {
      if (isDelete()) {
          recipeDelete(service, id);
          listOwnRecipes(response);
      } else {
        // The recipe that will be used on the edit page 
        // for inserting or updating.
        Recipe recipe = null;
        if (isAdd()) {
          recipe = new Recipe();
        } else if (isUpdate()) {
          URL entryUrl = urlFactory.getItemsEntryURL(id);
          GoogleBaseEntry entry = service.getEntry(entryUrl);
          recipe = new Recipe(entry);
        }
        if (recipe != null) {
          // Ready to add or update
          editRecipe(request, response, recipe, null);
        }
      }
    } catch (ServiceException e) {
      RecipeUtil.logServiceException(this, e);
      RecipeUtil.forwardToErrorPage(request, response, e);
      return;
    }
  }

  /**
   * Sets the {@value RecipeUtil#RECIPE_ATTRIBUTE} attribute of the request to
   * contain the specified recipe and forwards the request to the
   * {@value #DISPLAY_JSP} page.
   * 
   * @param request
   * @param response
   * @param recipe the recipe to be passed to the edit jsp page
   * @param message HTML code to be displayed at the top of the page, usually an
   *          error message
   */
  private void editRecipe(HttpServletRequest request,
                         HttpServletResponse response,
                         Recipe recipe,
                         String message)
      throws ServletException, IOException {
    request.setAttribute(RecipeUtil.RECIPE_ATTRIBUTE, recipe);
    request.setAttribute(RecipeUtil.MESSAGE_ATTRIBUTE, 
                         message == null ? "" : message);
    // Forward to the JSP
    request.getRequestDispatcher(DISPLAY_JSP).forward(request, response);
  }
}
