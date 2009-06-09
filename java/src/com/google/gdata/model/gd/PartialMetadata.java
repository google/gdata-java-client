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


package com.google.gdata.model.gd;

import com.google.common.collect.ImmutableList;
import com.google.gdata.model.DefaultRegistry;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.ForwardingElementMetadata;
import com.google.gdata.model.QName;

import java.util.Collection;

/**
 * The PartialMetadata class enables the customization of the base metadata
 * for the {@link Partial} model type to wrap a child representation of
 * another type.   Because the {@link Partial} element is used to wrap partial
 * response representation as well as the container for partial patch documents,
 * the element can wrap a wide variety of different data types.   This class
 * provides the dynamic construction and runtime child metadata lookup that
 * enables partial wrapping.
 * 
 * 
 */
public class PartialMetadata 
    extends ForwardingElementMetadata<Void, Partial> {

  /** Metadata for the resource representation inside the partial element */
  private final ElementMetadata<?, ?> childMetadata;
  
  /** Metadata for the partial element itself */
  private final ElementMetadata<Void, Partial> partialMetadata;

  /**
   * Constructs a new PartialMetadata instance that wraps the provided data
   * model.
   * 
   * @param childMetadata metadata for the element type inside of the partial
   *        wrapper element
   */
  public PartialMetadata(ElementMetadata<?, ?> childMetadata) {
    this.childMetadata = childMetadata;
    
    // For the base metadata, use the gd:partial metadata bound to the same
    // metadata context as the provided child metadata
    this.partialMetadata = 
        DefaultRegistry.get().bind(Partial.KEY, 
            childMetadata.getContext());
  }
  
  @Override
  protected ElementMetadata<Void, Partial> delegate() {
    return partialMetadata;
  }

  @Override
  public <K, L extends Element> ElementMetadata<K, L> bindElement(
      ElementKey<K, L> key) {

    // Use the provided partial metadata for any child with a matching id
    if (childMetadata.getKey().getId().equals(key.getId())) {
      @SuppressWarnings("unchecked")
      ElementMetadata<K, L> metadata = (ElementMetadata<K, L>) childMetadata;
      return metadata;
    }
    return super.bindElement(key);
  }

  @Override
  public ElementKey<?, ?> findElement(QName id) {
    
    // Use the provided partial key for any child with a matching id
    if (childMetadata.getKey().getId().equals(id)) {
      return childMetadata.getKey();
    }
    return super.findElement(id);
  }

  @Override
  public Collection<ElementKey<?, ?>> getElements() {
    return ImmutableList.<ElementKey<?, ?>>of(childMetadata.getKey());
  }

  @Override
  public boolean isDeclared(ElementKey<?, ?> key) {
    if (childMetadata.getKey().getId().equals(key.getId())) {
      return true;
    }
    return super.isDeclared(key);
  }
}
