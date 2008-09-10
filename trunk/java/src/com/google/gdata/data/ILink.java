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
 * Common interface for Links.
 * 
 * 
 */
public interface ILink extends Reference {
  
  /**
   * Returns the link relation type.  Possible values include {@code self},
   * {@code prev}, {@code next}, {@code enclosure}, etc.
   */
  public String getRel();
  
  /**
   * Sets the link relation type.
   */
  public void setRel(String rel);

  /**
   * Returns the mime type of the link.
   */
  public String getType();

  /**
   * Sets the mime type of the link.
   */
  public void setType(String type);
}
