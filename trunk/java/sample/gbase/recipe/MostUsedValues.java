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

import com.google.api.gbase.client.AttributeHistogram;
import com.google.api.gbase.client.FeedURLFactory;
import com.google.api.gbase.client.GoogleBaseEntry;
import com.google.api.gbase.client.GoogleBaseFeed;
import com.google.api.gbase.client.GoogleBaseQuery;
import com.google.api.gbase.client.GoogleBaseService;
import com.google.api.gbase.client.MetadataEntryExtension;
import com.google.api.gbase.client.AttributeHistogram.UniqueValue;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContext;

/**
 * Holds the most used values of attributes in Google Base and 
 * periodically refreshes them.
 * 
 * A MostUsedValues object is focused on a GoogleBaseService and 
 * a FeedURLFactory. One object can be used to analyse only the items that
 * match a specific query.
 * 
 * The class currently works for TEXT attributes only.
 * 
 * The cache() and clear() calls are synchronized, 
 * so that the different Maps and Collections remain in sync.
 * 
 * Typically, right after you create a MostUsedValues object, you call the
 * {@link #cache(long, int, ServletContext, String[])} method to set it up
 * to cache the most used values of your attributes. This creates 
 * {@link java.util.TimerTask} objects that periodically refresh the cached 
 * values. After this, you use {@link #getMostUsedValuesForAttribute(String)}
 * to get the most used values for the attributes you previously specified. 
 * In the end, when you no longer need the cache, you can call 
 * {@link #clear()} to stop the TimerTask objects and clear the cache.
 */
public class MostUsedValues {

  protected static final String TEXT_TYPE = "(text)";

  /**
   * The initial value of the max values limit when making a query;
   * the step used to increase that limit if some of the attributes 
   * are not found.
   */
  protected static final int STEP_MAXRESULTS = 25;

  /** 
   * The max of the max values limit when making a query.
   */
  protected static final int MAX_MAXRESULTS = 200;

  /**
   * The cached most used values.
   * This is a synchronized map.
   */
  private java.util.Map<String, String[]> mostUsedValues;
  
  /** 
   * The timers that periodically refresh the cache.
   * The access to this object has to be synchronized.
   */
  private Collection<Timer> timers;

  private GoogleBaseService service;
  private FeedURLFactory urlFactory;
  private String queryString;
  
  /**
   * Creates an empty MostUsedValues.
   * 
   * @param service any GoogleBaseService used to retrieve attribute histograms
   * @param urlFactory a FeedURLFactory used to create the URLs of 
   *                   the attribute histograms
   * @param queryString the query string used to filter the analyzed items,
   *                    for example one that focuses only on the items that 
   *                    have a specific item type.
   */
  public MostUsedValues(GoogleBaseService service, 
                        FeedURLFactory urlFactory,
                        String queryString) {
    this.service = service;
    this.urlFactory = urlFactory;
    this.queryString = queryString;
    
    mostUsedValues = new Hashtable<String, String[]>();
    timers = new ArrayList<Timer>();
  }

  /**
   * Gets the cached most used values of an attribute.
   * 
   * @param attrName the name of the attribute
   * @return a cached list of the most used values 
   */
  public String[] getMostUsedValuesForAttribute(String attrName) {
    return mostUsedValues.get(attrName);
  }
  
  /**
   * Sets up the object to cache a limited number of the most used values of 
   * each of the specified attributes; the cache is refreshed periodically.
   * 
   * This method does not check if the specified attributes are already cached.
   * You have to make sure an attribute is specified only once in your calls.
   * 
   * @param interval the cache refresh period, in millis
   * @param maxValues the maximum number of values to cache
   * @param servletContext a ServletContext to be used for logging
   * @param attrNames the names of the attributes to be cached
   */
  synchronized public void cache(long interval, 
                                 final int maxValues, 
                                 ServletContext servletContext,
                                 final String... attrNames) {
    if (attrNames.length == 0) {
      return;
    }

    Timer timer = new Timer(true);
    TimerTask task = createRefresher(maxValues, servletContext, attrNames);
    task.run();
    timer.schedule(task, interval, interval);
    timers.add(timer);
  }

  /**
   * Creates a TimerTask that refreshes the cached most used values of 
   * the specified attributes.
   * 
   * @param maxValues how many values to cache for each attribute
   * @param servletContext servlet context for logging error messages
   * @param attrNames the names of the attributes
   * @return a TimerTask that refreshes the cache
   */
  private TimerTask createRefresher(final int maxValues, 
                                    final ServletContext servletContext,
                                    final String... attrNames) {
    TimerTask task = new TimerTask() {
      /** 
       * Tells the MostUsedValues object that created this TimerTask to
       * refresh the cached most used values of some of the attributes.
       */
      @Override public void run() {
        try {
          MostUsedValues.this.retrieveMostUsedValues(maxValues, attrNames);
        } catch (IOException e) {
          servletContext.log(e.getMessage(), e);
        } catch (ServiceException e) {
          servletContext.log(e.getMessage() + " " + 
                             e.getHttpErrorCodeOverride() + " " + 
                             e.getResponseContentType() + ": " + 
                             e.getResponseBody(), e);
        }
      }
    };
    return task;
  }

  /**
   * Retrieves the most used values for some attributes 
   * for the items that match the query string 
   * and stores a limited number of those values for each attribute.
   *  
   * @param numValue maximum number of values to store
   * @param attrNames the names of the attributes
   */
  protected void retrieveMostUsedValues(int numValue, final String... attrNames) 
      throws ServiceException, IOException {
    URL url = urlFactory.getAttributesFeedURL();
    GoogleBaseQuery query = new GoogleBaseQuery(url);
    
    StringBuffer queryString = createQueryString(attrNames);
    query.setGoogleBaseQuery(queryString.toString());
    query.setMaxValues(numValue);

    int numResults = 0;
    int lastNumResults = 0;
    Collection<String> attrToRetrieve = 
        new ArrayList<String>(Arrays.asList(attrNames));
    do {
      // Get the feed 
      numResults += STEP_MAXRESULTS;
      query.setMaxResults(numResults);
      GoogleBaseFeed feed = service.query(query);
      if (lastNumResults == feed.getTotalResults()) {
        // No new entries to process
        break;
      }
      lastNumResults = feed.getTotalResults();
      // Extract the values from the entries
      Iterator<String> attrIter = attrToRetrieve.iterator();
      while (attrIter.hasNext()) {
        String attrName = attrIter.next();
        // Searching for the entry with the following name
        String entryTitle = attrName + TEXT_TYPE;
        for (GoogleBaseEntry entry : feed.getEntries()) {
          if (entryTitle.equals(entry.getTitle().getPlainText())) {
            extractValuesFromEntry(numValue, attrName, entry);
            attrIter.remove();
          }
        }
      }
    } while (!attrToRetrieve.isEmpty() && numResults <= MAX_MAXRESULTS);
    
    if (!attrToRetrieve.isEmpty()) {
      throw new ServiceException("The retrieved histograms do not contain" +
          "some of the attributes. The most used values of these attributes " +
          "have not been refreshed.");
    }
  }

  /**
   * Returns the query string extended so that it filters out the items 
   * that do not have at least one of the specified attributes of type TEXT.
   * 
   * @param attrNames the attributes we are interested in
   * @return an extended query string 
   */
  protected StringBuffer createQueryString(final String... attrNames) {
    StringBuffer queryString = new StringBuffer(this.queryString);
    queryString.append("(");
    queryString.append("[").append(attrNames[0]).append(TEXT_TYPE).append("]");
    for (int i = 1; i < attrNames.length; i++) {
      String attrName = attrNames[i];
      queryString.append("|[").append(attrName).append(TEXT_TYPE).append("]");
    }
    queryString.append(")");
    return queryString;
  }

  /**
   * Caches a limited number of the values of a GoogleBaseEntry.
   * 
   * @param numValue maximum number of values to cache
   * @param attrName the name of the attribute that has the values
   * @param entry an entry with a MetadataEntryExtension
   */
  private void extractValuesFromEntry(int numValue, 
                                      String attrName, 
                                      GoogleBaseEntry entry) {
    MetadataEntryExtension metadata = entry.getGoogleBaseMetadata();
    AttributeHistogram attributeHistogram = metadata.getAttributeHistogram();
    List<? extends UniqueValue> values = attributeHistogram.getValues();
    
    int valuesCount = Math.min(numValue, values.size());
    String[] usedValues = new String[valuesCount];
    for (int i = 0; i < valuesCount; i++) {
      usedValues[i] = values.get(i).getValueAsString();
    }
    
    updateMostUsedValue(attrName, usedValues);
  }

  /**
   * Cancels all refresh timers and clears the cache.
   */
  synchronized public void clear() {
    for (Timer timer : timers) {
      timer.cancel();
    }
    timers.clear();
    mostUsedValues.clear();
  }

  /**
   * Returns the number of cached attributes.
   */
  public int size() {
    return mostUsedValues.size();
  }

  /**
   * Returns true if no attribute is cached.
   */
  public boolean isEmpty() {
    return mostUsedValues.isEmpty();
  }

  public String getQueryString() {
    return queryString;
  }
  
  /**
   * Caches the most used values of an attribute.
   * @param attrName
   * @param stringValues
   */
  protected void updateMostUsedValue(String attrName, String[] stringValues) {
    mostUsedValues.put(attrName, stringValues);
  }
}
