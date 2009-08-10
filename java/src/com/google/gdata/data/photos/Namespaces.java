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


package com.google.gdata.data.photos;

import com.google.gdata.util.common.xml.XmlNamespace;

/**
 * Namespace definitions related to Picasa Web Albums Data API.
 *
 * 
 */
public class Namespaces {

  private Namespaces() {}

  /** Exif namespace (EXIF) namespace */
  public static final String EXIF =
      "http://schemas.google.com/photos/exif/2007";

  /** Exif namespace (EXIF) namespace prefix */
  public static final String EXIF_PREFIX = EXIF + "#";

  /** Exif namespace (EXIF) namespace alias */
  public static final String EXIF_ALIAS = "exif";

  /** XML writer namespace for Exif namespace (EXIF) */
  public static final XmlNamespace EXIF_NAMESPACE = new XmlNamespace(EXIF_ALIAS,
      EXIF);

  /** Google Photos namespace (GPHOTO) namespace */
  public static final String PHOTOS = "http://schemas.google.com/photos/2007";

  /** Google Photos namespace (GPHOTO) namespace prefix */
  public static final String PHOTOS_PREFIX = PHOTOS + "#";

  /** Google Photos namespace (GPHOTO) namespace alias */
  public static final String PHOTOS_ALIAS = "gphoto";

  /** XML writer namespace for Google Photos namespace (GPHOTO) */
  public static final XmlNamespace PHOTOS_NAMESPACE = new
      XmlNamespace(PHOTOS_ALIAS, PHOTOS);

  /** Pheed namespace (PHEED) namespace */
  public static final String PHEED = "http://www.pheed.com/pheed/";

  /** Pheed namespace (PHEED) namespace prefix */
  public static final String PHEED_PREFIX = PHEED + "#";

  /** Pheed namespace (PHEED) namespace alias */
  public static final String PHEED_ALIAS = "pheed";

  /** XML writer namespace for Pheed namespace (PHEED) */
  public static final XmlNamespace PHEED_NS = new XmlNamespace(PHEED_ALIAS,
      PHEED);


  /** KML namespace (KML) namespace */
  public static final String KML = "http://earth.google.com/kml/2.1";

  /** KML namespace (KML) namespace prefix */
  public static final String KML_PREFIX = KML + "#";

  /** KML namespace (KML) namespace alias */
  public static final String KML_ALIAS = "kml";

  /** XML writer namespace for KML namespace (KML) */
  public static final XmlNamespace KML_NS = new XmlNamespace(KML_ALIAS, KML);


  public static final String SLIDESHOW_REL = PHOTOS_PREFIX + "slideshow";

  public static final String REPORT_REL = PHOTOS_PREFIX + "report";
}
