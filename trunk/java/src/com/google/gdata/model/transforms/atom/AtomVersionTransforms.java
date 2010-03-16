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


package com.google.gdata.model.transforms.atom;

import com.google.gdata.client.Service;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.MetadataContext;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.model.atom.Entry;
import com.google.gdata.model.atom.Feed;
import com.google.gdata.model.gd.GdAttributes;
import com.google.gdata.util.Namespaces;

/**
 * Transforms for atom elements based on version information.
 *
 * 
 */
public class AtomVersionTransforms {

  private static final MetadataContext V1_CONTEXT =
      MetadataContext.forVersion(Service.Versions.V1);

  public static void addTransforms(MetadataRegistry registry) {
    addOssTransform(registry, Feed.TOTAL_RESULTS);
    addOssTransform(registry, Feed.START_INDEX);
    addOssTransform(registry, Feed.ITEMS_PER_PAGE);

    registry.build(Entry.KEY, GdAttributes.KIND, V1_CONTEXT).setVisible(false);
    registry.build(Feed.KEY, GdAttributes.KIND, V1_CONTEXT).setVisible(false);
  }

  @SuppressWarnings("deprecation")
  private static void addOssTransform(MetadataRegistry registry,
      ElementKey<?, ?> key) {
    registry.build(key, V1_CONTEXT).setName(
        new QName(Namespaces.openSearchNs, key.getId().getLocalName()));
  }
}
