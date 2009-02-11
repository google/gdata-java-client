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


package com.google.gdata.data.introspection;

import java.util.List;

/**
 * Shared workspace interface.
 * 
 * 
 */
public interface IWorkspace {

  /**
   * Returns the collections associated with this workspace.
   */
  List<? extends ICollection> getCollections();
  
  /**
   * Add a new collection to the list of collections associated with this
   * workspace.
   */
  ICollection addCollection(String collectionUri, String title,
      String... acceptedTypes);
}
