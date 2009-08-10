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


package com.google.gdata.data.maps;

import com.google.gdata.util.common.xml.XmlNamespace;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.Link;
import com.google.gdata.data.extensions.CustomProperty;
import com.google.gdata.data.extensions.Deleted;
import com.google.gdata.data.extensions.FeedLink;
import com.google.gdata.data.extensions.ResourceId;
import com.google.gdata.util.Namespaces;

import java.util.List;

/**
 * Describes a map entry.
 *
 * 
 */
@Kind.Term(MapEntry.KIND)
public class MapEntry extends BaseEntry<MapEntry> {

  /**
   * Map map category kind term value.
   */
  public static final String KIND = MapsNamespace.MAPS_PREFIX + "map";

  /**
   * Map map category kind category.
   */
  public static final Category CATEGORY = new Category(Namespaces.gKind, KIND);

  /**
   * Default mutable constructor.
   */
  public MapEntry() {
    super();
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a new instance by doing a shallow copy of data from an existing
   * {@link BaseEntry} instance.
   *
   * @param sourceEntry source entry
   */
  public MapEntry(BaseEntry<?> sourceEntry) {
    super(sourceEntry);
  }

  @Override
  public void declareExtensions(ExtensionProfile extProfile) {
    if (extProfile.isDeclared(MapEntry.class)) {
      return;
    }
    super.declareExtensions(extProfile);
    extProfile.declare(MapEntry.class,
        CustomProperty.getDefaultDescription(false, true));
    extProfile.declare(MapEntry.class, new ExtensionDescription(Deleted.class,
        new XmlNamespace("gd", "http://schemas.google.com/g/2005"), "deleted",
        false, false, false));
    extProfile.declare(MapEntry.class, new ExtensionDescription(FeedLink.class,
        new XmlNamespace("gd", "http://schemas.google.com/g/2005"), "feedLink",
        false, false, false));
    new FeedLink().declareExtensions(extProfile);
    extProfile.declare(MapEntry.class, ResourceId.class);
  }

  /**
   * Returns the custom properties.
   *
   * @return custom properties
   */
  public List<CustomProperty> getCustomProperties() {
    return getRepeatingExtension(CustomProperty.class);
  }

  /**
   * Adds a new custom property.
   *
   * @param customProperty custom property
   */
  public void addCustomProperty(CustomProperty customProperty) {
    getCustomProperties().add(customProperty);
  }

  /**
   * Returns whether it has the custom properties.
   *
   * @return whether it has the custom properties
   */
  public boolean hasCustomProperties() {
    return hasRepeatingExtension(CustomProperty.class);
  }

  /**
   * Returns the marker for deleted entries.
   *
   * @return marker for deleted entries
   */
  public Deleted getDeleted() {
    return getExtension(Deleted.class);
  }

  /**
   * Sets the marker for deleted entries.
   *
   * @param deleted marker for deleted entries or <code>null</code> to reset
   */
  public void setDeleted(Deleted deleted) {
    if (deleted == null) {
      removeExtension(Deleted.class);
    } else {
      setExtension(deleted);
    }
  }

  /**
   * Returns whether it has the marker for deleted entries.
   *
   * @return whether it has the marker for deleted entries
   */
  public boolean hasDeleted() {
    return hasExtension(Deleted.class);
  }

  /**
   * Returns the nested feed link.
   *
   * @return nested feed link
   */
  public FeedLink getFeedLink() {
    return getExtension(FeedLink.class);
  }

  /**
   * Sets the nested feed link.
   *
   * @param feedLink nested feed link or <code>null</code> to reset
   */
  public void setFeedLink(FeedLink feedLink) {
    if (feedLink == null) {
      removeExtension(FeedLink.class);
    } else {
      setExtension(feedLink);
    }
  }

  /**
   * Returns whether it has the nested feed link.
   *
   * @return whether it has the nested feed link
   */
  public boolean hasFeedLink() {
    return hasExtension(FeedLink.class);
  }

  /**
   * Returns the resource id.
   *
   * @return resource id
   */
  public ResourceId getResourceId() {
    return getExtension(ResourceId.class);
  }

  /**
   * Sets the resource id.
   *
   * @param resourceId resource id or <code>null</code> to reset
   */
  public void setResourceId(ResourceId resourceId) {
    if (resourceId == null) {
      removeExtension(ResourceId.class);
    } else {
      setExtension(resourceId);
    }
  }

  /**
   * Returns whether it has the resource id.
   *
   * @return whether it has the resource id
   */
  public boolean hasResourceId() {
    return hasExtension(ResourceId.class);
  }

  /**
   * Returns the link that provides the URI of an alternate format of the
   * entry's or feed's contents.
   *
   * @return Link that provides the URI of an alternate format of the entry's or
   *     feed's contents or {@code null} for none.
   */
  public Link getAtomAlternateLink() {
    return getLink(Link.Rel.ALTERNATE, Link.Type.ATOM);
  }

  /**
   * Returns the link that provides the URI of the full feed (without any query
   * parameters).
   *
   * @return Link that provides the URI of the full feed (without any query
   *     parameters) or {@code null} for none.
   */
  public Link getAtomFeedLink() {
    return getLink(Link.Rel.FEED, Link.Type.ATOM);
  }

  @Override
  protected void validate() {
  }

  @Override
  public String toString() {
    return "{MapEntry " + super.toString() + "}";
  }


  /**
   * Gets the URL of the enclosed feature feed.
   *
   * @return URL to the enclosed feed or {@code null}
   */
  public java.net.URL getFeatureFeedUrl() {
    if (getContent() instanceof com.google.gdata.data.OutOfLineContent) {
      try {
        return new java.net.URL(((com.google.gdata.data.OutOfLineContent)
            getContent()).getUri());
      } catch (java.net.MalformedURLException e) {
        return null;
      }
    }
    return null;
  }

}
