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
 * Shared Category interface.
 * 
 * 
 */
public interface ICategory {

  /** 
   * Returns the category scheme or {@code null} if the category does not have
   * a scheme.
   * 
   * @return category scheme or {@code null}.
   */
  public String getScheme();

  /** 
   * Returns the category label or {@code null} if there is no label value.
   * 
   * @return category label (or @code null}.
   */
  public String getLabel();
  
  /** 
   * Returns the category term.
   * 
   * @return category term value.
   */
  public String getTerm();
}
