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


package com.google.gdata.data.photos.pheed;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Namespaces for Pheed.  http://pheed.com/pheed
 *
 * @deprecated the pheed namespace is deprecated in favor of media rss.
 *
 * 
 */
@Deprecated
public class Namespaces {

  /** Pheed <a href="http://www.pheed.com/pheed/">pheed</a> namespace. */
  public static final XmlNamespace PHEED_NAMESPACE
      = new XmlNamespace("photo", "http://www.pheed.com/pheed/");
}
