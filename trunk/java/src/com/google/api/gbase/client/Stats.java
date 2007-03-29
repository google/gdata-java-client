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

package com.google.api.gbase.client;

import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.Extension;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;
import com.google.gdata.util.common.xml.XmlWriter;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * Object representation for the tag gm:stats.
 */
@ExtensionDescription.Default(
    nsAlias = GoogleBaseNamespaces.GM_ALIAS,
    nsUri = GoogleBaseNamespaces.GM_URI,
    localName = "stats")
public class Stats implements Extension {

  private final Statistics impressions = new Statistics();
  private final Statistics clicks = new Statistics();
  private final Statistics pageViews = new Statistics();

  /** Gets impression count. */
  public Statistics getImpressions() {
    return impressions;
  }

  /** Gets click count. */
  public Statistics getClicks() {
    return clicks;
  }

  /** Gets page view count. */
  public Statistics getPageViews() {
    return pageViews;
  }


  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    if (impressions.getTotal() == 0 && clicks.getTotal() == 0
        && pageViews.getTotal() == 0) {
      // Nothing to generate
      return;
    }

    w.startElement(GoogleBaseNamespaces.GM, "stats", null, null);
    impressions.generate(w, "impressions");
    clicks.generate(w, "clicks");
    pageViews.generate(w, "page_views");
    w.endElement();
  }

  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
      String namespace, String localName, Attributes attrs)
      throws ParseException, IOException {

    impressions.clear();
    pageViews.clear();
    clicks.clear();

    return new XmlParser.ElementHandler() {
      @Override
      public XmlParser.ElementHandler getChildHandler(String namespace,
          String localName, Attributes attrs) throws ParseException,
          IOException {
        if (localName.equals("impressions")) {
          return new StatsSubElementHandler(impressions, attrs);
        }
        if (localName.equals("page_views")) {
          return new StatsSubElementHandler(pageViews, attrs);
        }
        if (localName.equals("clicks")) {
          return new StatsSubElementHandler(clicks, attrs);
        }
        return super.getChildHandler(namespace, localName, attrs);
      }
    };
  }


  /**
   * Information about one specific use (impressions, clicks, page views).
   */
  public static class Statistics {
    private int total;
    private Map<String, Integer> countBySource;

    /**
     * Puts the instance back to its initial state.
     */
    void clear() {
      total = 0;
      countBySource = null;
    }

    /**
     * Gets the total for this statistic.
     *
     * @return total
     */
    public int getTotal() {
      return total;
    }

    /**
     * Sets the total for this statistic.
     *
     * @param total
     */
    public void setTotal(int total) {
      this.total = total;
    }

    /**
     * Gets a set of sources for which a sub-total
     * is available.
     *
     * @return set of sources
     */
    public Set<String> getSources() {
      if (countBySource == null) {
        return Collections.emptySet();
      }
      return countBySource.keySet();
    }

    /**
     * Get a sub-total for a specific source.
     *
     * @param source source name
     * @return sub-total or -1 if it is not known
     */
    public int getCountBySource(String source) {
      if (countBySource == null) {
        return -1;
      }

      Integer value = countBySource.get(source);
      if (value == null) {
        return -1;
      }
      return value;
    }

    /**
     * Sets the sub-total for a specific source.
     *
     * @param source source name
     * @param count sub-total or -1 if it is not known
     */
    public void setCountBySource(String source, int count) {
      if (count == -1) {
        if (countBySource != null) {
          countBySource.remove(source);
        }
      } else {
        if (countBySource == null) {
          countBySource = new HashMap<String, Integer>();
        }
        countBySource.put(source, count);
      }
    }

    /** Generates the XML representation for this object. */
    private void generate(XmlWriter w, String name) throws IOException {
      if (total == 0) {
        // Nothing to generate
        return;
      }

      w.startElement(GoogleBaseNamespaces.GM, name,
          Collections.singletonList(
              new XmlWriter.Attribute("total", Integer.toString(total))), null);

      if (countBySource != null && !countBySource.isEmpty()) {
        w.startRepeatingElement();
        for (Map.Entry<String, Integer> entry : countBySource.entrySet()) {
          List<XmlWriter.Attribute> attrs =
              new ArrayList<XmlWriter.Attribute>(2);
          attrs.add(new XmlWriter.Attribute("name", entry.getKey()));
          attrs.add(
              new XmlWriter.Attribute("count", entry.getValue().toString()));
          w.simpleElement(GoogleBaseNamespaces.GM, "source", attrs, null);
        }

        w.endRepeatingElement();
      }

      w.endElement();
    }
  }


  /** Parses a sub-element of gm:stats. */
  private static class StatsSubElementHandler
      extends XmlParser.ElementHandler {

    private final Stats.Statistics stat;

    public StatsSubElementHandler(Stats.Statistics stat, Attributes attrs)
        throws ParseException {
      this.stat = stat;
      AttributeHelper helper = new AttributeHelper(attrs);
      stat.setTotal(helper.consumeInteger("total", false, 0));
      helper.assertAllConsumed();
    }

    @Override
    public XmlParser.ElementHandler getChildHandler(String namespace,
        String localName, Attributes attrs)
        throws ParseException, IOException {
      if (GoogleBaseNamespaces.GM_URI.equals(namespace)
          && "source".equals(localName)) {
        AttributeHelper helper = new AttributeHelper(attrs);
        stat.setCountBySource(helper.consume("name", true),
            helper.consumeInteger("count", true));
        helper.assertAllConsumed();
      }
      return new XmlParser.ElementHandler();
    }
  }  
}
