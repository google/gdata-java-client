/* Copyright (c) 2007 Google Inc.
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

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.util.ParseException;

/**
 * Java representation for the gm:item_type tag found in the item type feed.
 */
@ExtensionDescription.Default(
    nsAlias = GoogleBaseNamespaces.GM_ALIAS,
    nsUri = GoogleBaseNamespaces.GM_URI,
    localName = "item_type")
public class GmItemType extends AbstractExtension {
  private String itemType;

  /**
   * Creates an unset item type object.
   */
  public GmItemType() {

  }

  /**
   * Creates and initializez an item type object.
   *
   * @param itemType itemType value
   */
  public GmItemType(String itemType) {
    this.itemType = itemType;
  }

  /** Gets the item type value. */
  public String getItemType() {
    return itemType;
  }

  /** Sets the item type value. */
  public void setItemType(String itemType) {
    this.itemType = itemType;
  }


  /**
   * Makes sure the item type is not null.
   *
   * {@inheritDoc}
   */
  @Override
  protected void validate() {
    super.validate();
    
    if (itemType == null) {
      throw new IllegalStateException("An item type should be set.");
    }
  }

  protected void putAttributes(AttributeGenerator generator) {
    generator.setContent(itemType);
  }

  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    this.itemType = helper.consumeContent(true);
  }
}