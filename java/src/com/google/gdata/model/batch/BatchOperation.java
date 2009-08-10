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

import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.model.AttributeKey;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.util.Namespaces;

/**
 * Describes the batch operation to apply.
 *
 * 
 */
public class BatchOperation extends Element {

  /**
   * The key for this element.
   */
  public static final ElementKey<Void, BatchOperation> KEY = ElementKey.of(
      new QName(Namespaces.batchNs, "operation"), BatchOperation.class);

  /**
   * The operation type.
   */
  public static final AttributeKey<BatchOperationType> TYPE = AttributeKey.of(
      new QName("type"), BatchOperationType.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(KEY)) {
      return;
    }

    ElementCreator builder = registry.build(KEY);
    builder.addAttribute(TYPE).setRequired(true);
  }

  /**
   * Default mutable constructor.
   */
  public BatchOperation() {
    super(KEY);
  }

  /**
   * Constructs a new instance with the given operation type.
   *
   * @param type operation type.
   */
  public BatchOperation(BatchOperationType type) {
    this();
    setType(type);
  }

  /**
   * Returns the operation type.
   *
   * @return operation type
   */
  public BatchOperationType getType() {
    return getAttributeValue(TYPE);
  }

  /**
   * Sets the operation type.
   *
   * @param type operation type or <code>null</code> to reset
   */
  public BatchOperation setType(BatchOperationType type) {
    setAttributeValue(TYPE, type);
    return this;
  }

  /**
   * Returns whether it has the operation type.
   *
   * @return whether it has the operation type
   */
  public boolean hasType() {
    return getType() != null;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!sameClassAs(obj)) {
      return false;
    }
    BatchOperation other = (BatchOperation) obj;
    return eq(getType(), other.getType());
  }

  @Override
  public int hashCode() {
    int result = getClass().hashCode();
    if (getType() != null) {
      result = 37 * result + getType().hashCode();
    }
    return result;
  }

  @Override
  public String toString() {
    return "{BatchOperation type=" + getAttributeValue(TYPE) + "}";
  }
}
