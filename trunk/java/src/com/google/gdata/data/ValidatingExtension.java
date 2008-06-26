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

import com.google.gdata.util.ParseException;




/**
 * Extends the base {@link Extension} interface to enable validation
 * of extension contents.
 *
 * 
 */
public interface ValidatingExtension extends Extension {

  /**
   * Enables an extension to perform final validation after all extension
   * elements of the parent ExtensionPoint have been processed.
   *
   * @param   parent
   *            parent ExtensionPoint
   *
   * @throws  ParseException
   */
  void validate(ExtensionPoint parent) throws ParseException;
}

