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

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.PubControl;

/**
 * Handle gm: attributes found in histogram and item types feeds.
 *
 * This object should be accessed using
 * {@link com.google.api.gbase.client.GoogleBaseEntry#getGoogleBaseMetadata()}.
 *
 * The metadata can contain <em>either</em>:
 * <ul>
 *  <li>histogram metadata ({@link #getAttributeHistogram()})
 *  <li>item type description metadata ({@link #getItemTypeDescription()})
 *  <li>statistics {@link #getStats()} 
 * </ul>
 * Only one of them can be defined. Which one it is depends on the
 * type of entry/feed that has been read. See also the javadoc for
 * {@link GoogleBaseEntry}.
 */
public class MetadataEntryExtension {

  private final BaseEntry<?> owner;

  /**
   * Item type description information, if it is set.
   *
   * If it is not set, this means that either this extension
   * has not been initialized yet or that this is not an
   * itemtype entry.
   */
  private final ItemTypeDescription itemType;

  /**
   * Creates a MetadataEntryExtension and link it to a
   * {@link BaseEntry} (usually a {@link GoogleBaseEntry}).
   *
   * @param owner entry this object is linked to
   */
  public MetadataEntryExtension(BaseEntry<?> owner) {
    this.owner = owner;
    this.itemType = new ItemTypeDescription(owner);
  }

  /**
   * Gets attribute histogram information.
   *
   * @return attribute histogram information fond in the feed
   * @exception IllegalStateException if the feed was not an
   *   histogram feed.
   */
  public AttributeHistogram getAttributeHistogram() {
    AttributeHistogram histogram = owner.getExtension(AttributeHistogram.class);
    if (histogram == null) {
      throw new IllegalStateException("Not a histogram feed entry.");
    }
    return histogram;
  }

  /** Gets statistics, if available. */
  public Stats getStats() {
    return owner.getExtension(Stats.class);
  }

  /**
   * Checks whether anything has been defined in this extension.
   * 
   * @return true if there is metadata about locale, histograms or
   *   item types in this object
   */
  public boolean isEmpty() {
    return hasAttributeHistogram() || hasItemTypeDescription() || hasStats();
  }

  /**
   * Checks whether statistics information is available in the current entry.
   *
   * @return true if a call to {@link #getStats()} would return a Stats
   *   object
   */
  public boolean hasStats() {
    return owner.getExtension(Stats.class) != null;
  }

  /**
   * Checks whether an attribute histogram is available in the current
   * entry.
   *
   * @return true if a call to {@link #getAttributeHistogram()} would work
   */
  public boolean hasAttributeHistogram() {
    return owner.getExtension(AttributeHistogram.class) != null;
  }

  /**
   * Gets the {@link ItemTypeDescription} associated with this extension.
   *
   * @return item type description
   * @exception IllegalStateException if no item type description could
   *   be found in the entry (only item type feeds have item type description
   *   information)
   */
  public ItemTypeDescription getItemTypeDescription() {
    if (!hasItemTypeDescription()) {
      throw new IllegalStateException("Not an item-type entry.");
    }
    return itemType;
  }

  /**
   * Checks whether item type description is available in the current entry.
   *
   * @return true if a call to {@link #getItemTypeDescription()} would work
   */
  public boolean hasItemTypeDescription() {
    return itemType.getName() != null;
  }
  
  /**
   * Checks whether the entry contains the {@code gm:disapproved} tag, marking
   * it as a disapproved item.
   * 
   * @return {@code true} if the {@code gm:disapproved} tag is present in the 
   *    {@code app:control} section, {@code false} otherwise. 
   */
  public boolean hasGmDisapproved() {
    return owner.getPubControl() != null 
        && owner.getPubControl().getExtension(GmDisapproved.class) != null;
  }

  /**
   * Returns the publishing priority for the entry or {@code null} if 
   * the entry doesn't contain this information.
   * 
   * @return the value for the {@code publishing_priority} parameter, 
   *     or {@code null} if this information is not available.
   */
  public GmPublishingPriority.Value getGmPublishingPriority() {
    PubControl pubControl = owner.getPubControl();
    if (pubControl == null) {
      return null;
    }
    
    GmPublishingPriority priority = pubControl.getExtension(
        GmPublishingPriority.class);
    if (priority == null) {
      return null;
    }
    
    return priority.getValue();
  }
  
  /**
   * Sets the publishing priority for the entry. 
   * 
   * @param value the value for the publish priority
   */
  public void setGmPublishingPriority(GmPublishingPriority.Value value) {
    GmPublishingPriority priority = new GmPublishingPriority();
    priority.setValue(value);
    
    PubControl pubControl = owner.getPubControl();
    if (pubControl == null) {
      pubControl = new PubControl();
      owner.setPubControl(pubControl);
    }
    pubControl.setExtension(priority);
  }
}
