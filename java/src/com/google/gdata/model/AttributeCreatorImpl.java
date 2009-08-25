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


package com.google.gdata.model;

import com.google.gdata.model.Metadata.VirtualValue;

/**
 * Implementation of {@link AttributeCreator}.  See the superclass for the
 * implementation details, this class just delegates to the superclass and
 * changes the response types.
 *
 * 
 */
final class AttributeCreatorImpl extends MetadataCreatorImpl
    implements AttributeCreator {

  /**
   * Construct a new empty attribute metadata creator.
   */
  AttributeCreatorImpl(MetadataRegistry root, TransformKey transformKey) {
    super(root, transformKey);
  }

  /**
   * Convert the attribute creator into a transform.
   */
  AttributeTransform toTransform() {
    return AttributeTransform.create(this);
  }

  @Override
  public AttributeCreatorImpl setName(QName name) {
    return (AttributeCreatorImpl) super.setName(name);
  }

  @Override
  public AttributeCreatorImpl setRequired(boolean required) {
    return (AttributeCreatorImpl) super.setRequired(required);
  }

  @Override
  public AttributeCreatorImpl setVisible(boolean visible) {
    return (AttributeCreatorImpl) super.setVisible(visible);
  }

  @Override
  public AttributeCreatorImpl setVirtualValue(VirtualValue virtualValue) {
    return (AttributeCreatorImpl) super.setVirtualValue(virtualValue);
  }

  /**
   * When setting the source of an attribute, the virtual value of the attribute
   * is based on the path.
   */
  @Override
  void setSource(Path path, TransformKey key) {
    super.setSource(path, key);
    setVirtualValue(PathAdapter.valueAdapter(path));
  }
}
