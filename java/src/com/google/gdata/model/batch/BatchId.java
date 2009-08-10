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


package com.google.gdata.model.batch;

import com.google.gdata.model.Element;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Identifies an entry inside of a batch feed.
 *
 * 
 */
public class BatchId extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<String, BatchId> KEY = ElementKey.of(
      new QName(Namespaces.batchNs, "id"), String.class, BatchId.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    registry.build(KEY);
  }

  /**
   * Convenience method for getting a batchId from
   * an element if it's there.
   *
   * @param element element to get id from.
   * @return the id or {@code null} if it's not defined.
   */
  public static String getIdFrom(Element element) {
    BatchId tag = element.getElement(KEY);
    return tag == null ? null : tag.getId();
  }

  /**
   * Default mutable constructor.
   */
  public BatchId() {
    super(KEY);
  }

  /**
   * Constructs a new instance with the given value.
   *
   * @param id value.
   */
  public BatchId(String id) {
    this();
    setId(id);
  }

  /**
   * Returns the value.
   *
   * @return value
   */
  public String getId() {
    return (String) super.getTextValue();
  }

  /**
   * Sets the value.
   *
   * @param id value or <code>null</code> to reset
   */
  public BatchId setId(String id) {
    super.setTextValue(id);
    return this;
  }

  /**
   * Returns whether it has the value.
   *
   * @return whether it has the value
   */
  public boolean hasId() {
    return super.hasTextValue();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    BatchId other = (BatchId) obj;
    return eq(getId(), other.getId());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getId() != null) {
      result = 37 * result + getId().hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{BatchId id=" + getTextValue() + "}";
  }
}
