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


package com.google.gdata.util;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.IFeed;

import java.io.IOException;

/**
 * Helper class with static generate methods to generate atom for old or new
 * data model as appropriate.
 * 
 * 
 */
public class GenerateUtil {

  /**
   * Generates the atom output for an entry using the given xml writer and
   * extension profile.
   */
  public static void generateAtom(XmlWriter writer,
      IEntry entry, ExtensionProfile extProfile) throws IOException {
    if (entry instanceof BaseEntry) {
      ((BaseEntry<?>) entry).generateAtom(writer, extProfile);
    } else {
    }
  }
  
  /**
   * Generates the atom output for a feed using the given xml writer and
   * extension profile.
   */
  public static void generateAtom(XmlWriter writer,
      IFeed feed, ExtensionProfile extProfile) throws IOException {
    if (feed instanceof BaseFeed) {
      ((BaseFeed<?, ?>) feed).generateAtom(writer, extProfile);
    } else {
    }
  }
}
