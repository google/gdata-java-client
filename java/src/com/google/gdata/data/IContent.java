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


package com.google.gdata.data;

/**
 * Shared interface for content.
 * 
 * 
 */
public interface IContent {

  /**
   * Returns this content's type.  See {@link Content.Type} for a list of
   * return values.  
   * 
   * @return the type of content.
   */
  public int getType();
  
  /**
   * Returns the human language that this content is written in.
   * 
   * @return the language of the content if available, or null if not.
   */
  public String getLang();
}
