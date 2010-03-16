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


package com.google.gdata.data.maps;

/**
 * KML feature data.
 *
 * 
 */
public class KmlContent {

  /** Type of text construct (typically 'text', 'html' or 'xhtml'). */
  public static final class Type {

    /** Application/vnd google-earth kml+xml kml content. */
    public static final String APPLICATION_VND_GOOGLE_EARTH_KML_XML =
        "application/vnd.google-earth.kml+xml";

  }

  /** Private constructor to ensure class is not instantiated. */
  private KmlContent() {}

}

