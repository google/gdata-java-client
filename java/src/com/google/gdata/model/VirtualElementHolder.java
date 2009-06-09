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

import com.google.gdata.model.ElementMetadata.MultipleVirtualElement;
import com.google.gdata.model.ElementMetadata.SingleVirtualElement;

/**
 * An element that contains either a {@link SingleVirtualElement} or a {@link
 * MultipleVirtualElement}.
 *
 * <p>This class useful when the two different virtual element interfaces need
 * to be treated as one field, since they're mutually exclusive.
 *
 * 
 */
class VirtualElementHolder {
  private final SingleVirtualElement single;
  private final MultipleVirtualElement multiple;

  /**
   * Creates a holder for a single virtual element, returns {@code null} if the
   * virtual element is {@code null}.
   */
  static VirtualElementHolder of(SingleVirtualElement single) {
    if (single == null) {
      return null;
    }
    return new VirtualElementHolder(single, null);
  }

  /**
   * Creates a holder for a multiple virtual element, returns {@code null} if
   * the virtual element is {@code null}.
   */
  static VirtualElementHolder of(MultipleVirtualElement multiple) {
    if (multiple == null) {
      return null;
    }
    return new VirtualElementHolder(null, multiple);
  }

  private VirtualElementHolder(SingleVirtualElement single, 
      MultipleVirtualElement multiple) {
    this.single = single;
    this.multiple = multiple;
  }

  /** Returns a {@link SingleVirtualElement} or {@code null}. */
  SingleVirtualElement getSingleVirtualElement() {
    return single;
  }

  /** Returns a {@link SingleVirtualElement} or {@code null}. */
  MultipleVirtualElement getMultipleVirtualElement() {
    return multiple;
  }
}
