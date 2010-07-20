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
 * Object representation for <code>yt:derived</code> which describes
 * how this caption track was derived from other data.
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = YouTubeNamespace.PREFIX,
    nsUri = YouTubeNamespace.URI,
    localName = "derived")
public class YtDerived extends AbstractFreeTextExtension {

  /** Value is how the track was derived. */
  public static final class Value {

    /** AutoSync derived caption track. */
    public static final String AUTO_SYNC = "autoSync";

    /** SpeechRecognition derived caption track. */
    public static final String SPEECH_RECOGNITION = "speechRecognition";

    private Value() {}
  }

  /** Creates an empty tag. */
  public YtDerived() {
  }

  /** Creates a tag and initializes its content.
   *
   * @param derived content
   */
  public YtDerived(String derived) {
    super(derived);
  }

}
