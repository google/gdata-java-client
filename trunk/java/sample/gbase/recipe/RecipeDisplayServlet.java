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
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Displays a recipe.
 */
@SuppressWarnings("serial")
public class RecipeDisplayServlet extends HttpServlet {

  public static final String DISPLAY_JSP = "/WEB-INF/recipeDisplay.jsp";
  
  protected FeedURLFactory urlFactory;
  
  @Override
  public void init(ServletConfig servletConfig) throws ServletException {
    super.init(servletConfig);
    ServletContext context = servletConfig.getServletContext();
    urlFactory = (FeedURLFactory) 
        context.getAttribute(RecipeListener.FEED_URL_FACTORY_ATTRIBUTE);
  }

  @Override
  public void destroy() {
    super.destroy();
  }

  /**
   * Shows the page for displaying a Recipe.
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
    // This is a public page, so we use a simple, nonauthenticated service
    GoogleBaseService service = RecipeUtil.getGoogleBaseService(request, 
        this.getServletContext());
    String id = request.getParameter(RecipeUtil.ID_PARAMETER);
    recipeDisplay(request, response, service, id);
  }

  /**
   * Retrieves a recipe and forwards the request 
   * to the {@link #DISPLAY_JSP} jsp page that displays the recipe.
   *
   * @param request
   * @param response
   * @param service the service used to retrieve the recipe
   * @param id the id of the recipe
   */
  private void recipeDisplay(HttpServletRequest request,
                         HttpServletResponse response,
                         GoogleBaseService service,
                         String id)
      throws ServletException, IOException {
    GoogleBaseEntry entry;
    try {
      URL feedUrl = urlFactory.getSnippetsEntryURL(id);
      entry = service.getEntry(feedUrl, GoogleBaseEntry.class);
    } catch (ServiceException e) {
      RecipeUtil.logServiceException(this, e);
      RecipeUtil.forwardToErrorPage(request, response, e);
      return;
    }
    Recipe recipe = new Recipe(entry);
    request.setAttribute(RecipeUtil.RECIPE_ATTRIBUTE, recipe);
    RecipeSearch results = new RecipeSearch(service, urlFactory, false);
    RecipeUtil.setRecipeSearch(request, results);
    // Forward to the JSP
    request.getRequestDispatcher(DISPLAY_JSP).forward(request, response);
  }
}
