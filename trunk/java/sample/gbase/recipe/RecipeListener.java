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

import java.net.MalformedURLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Creates objects needed by the servlets and makes them available by
 * setting them as attributes of the global servlet context.
 * 
 * Makes sure the required initialization parameters are present.
 * 
 */
public class RecipeListener implements ServletContextListener {

  public static final String MOST_USED_VALUES_ATTRIBUTE = "mostUsedValues";

  public static final String FEED_URL_FACTORY_ATTRIBUTE = "feedUrlFactory";
  
  FeedURLFactory urlFactory;

  protected MostUsedValues mostUsedValues;

  /**
   * Creates an initialised MostUsedValues object and a FeedURLFactory 
   * to be used by the servlets.
   * Makes sure the applicationName init parameter is set.
   * 
   * @throws RuntimeException
   */
  public void contextInitialized(ServletContextEvent event) {
    ServletContext servletContext = event.getServletContext();
    
    String applicationName = 
        servletContext.getInitParameter(RecipeUtil.APPLICATION_NAME_PARAMETER);
    if (applicationName == null) {
      RuntimeException re = 
          new RuntimeException("applicationName context parameter is missing");
      servletContext.log(re.getMessage(), re.getCause());
      throw re;
    }
    
    String baseUrl = servletContext.getInitParameter("baseUrl");
    if (baseUrl == null) {
      urlFactory = FeedURLFactory.getDefault();
    } else {
      try {
        urlFactory = new FeedURLFactory(baseUrl);
      } catch (MalformedURLException e) {
        RuntimeException re = 
            new RuntimeException("Cannot use the baseUrl context parameter", e);
        servletContext.log(re.getMessage(), re.getCause());
        throw re;
      }
    }
    servletContext.setAttribute(FEED_URL_FACTORY_ATTRIBUTE, urlFactory);
    
    String key = servletContext.getInitParameter(RecipeUtil.DEVELOPER_KEY_PARAMETER);

    GoogleBaseService service = new GoogleBaseService(applicationName, key);
    mostUsedValues = new MostUsedValues(service,
                                        urlFactory, 
                                        RecipeUtil.RECIPE_ITEMTYPE_QUERY);
    initMostUsedValues(mostUsedValues, servletContext);
    RecipeUtil.setMostUsedValues(servletContext, mostUsedValues);
  }
  
  public void contextDestroyed(ServletContextEvent event) {
    mostUsedValues.clear();
  }

  /**
   * Initializes a MostUsedValues object to cache the most used values 
   * of some attributes, suitable to be used in the web pages.
   * 
   * @param mostUsedValues object to initialize
   * @param servletContext the servlet context used by mostUsedValues 
   *                       to log exceptions
   */
  public static void initMostUsedValues(MostUsedValues mostUsedValues,
                                        ServletContext servletContext) {
    long interval = 1000L * 60L * 60L; // 1 hour
    mostUsedValues.cache(interval, 14, servletContext, 
                         Recipe.CUISINE_ATTRIBUTE);
    mostUsedValues.cache(interval, 16, servletContext, 
                         Recipe.MAIN_INGREDIENT_ATTRIBUTE);
  }

}
