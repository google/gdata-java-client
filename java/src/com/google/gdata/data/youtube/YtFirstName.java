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


package com.google.gdata.data.youtube;

import com.google.gdata.data.ExtensionDescription;

/**
 * yt:firstname tag, found on the user's detail page and on the 
 * contact feed.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "firstName")
public class YtFirstName extends AbstractFreeTextExtension {

  /** Creates an empty tag. */
  public YtFirstName() {
  }

  /**
   * Creates a tag and initializes its content.
   *
   * @param lastname content
   */
  public YtFirstName(String lastname) {
    super(lastname);
  }
}
