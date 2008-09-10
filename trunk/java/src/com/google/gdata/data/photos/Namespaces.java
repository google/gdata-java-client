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

import com.google.gdata.util.common.xml.XmlWriter;

/**
 * GData namespace definitions related to Google Photos.
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

  /** Google Photos namespace (GPHOTO) namespace */
  public static final String PHOTOS = "http://schemas.google.com/photos/2007";

  /** Google Photos namespace (GPHOTO) namespace prefix */
  public static final String PHOTOS_PREFIX = PHOTOS + "#";

  /** Google Photos namespace (GPHOTO) namespace alias */
  public static final String PHOTOS_ALIAS = "gphoto";

  /** XML writer namespace for Google Photos namespace (GPHOTO) */
  public static final XmlWriter.Namespace PHOTOS_NAMESPACE = new
      XmlWriter.Namespace(PHOTOS_ALIAS, PHOTOS);



  /** Exif namespace (EXIF) namespace alias */
  public static final String EXIF_ALIAS = "exif";

  /** XML writer namespace for Exif namespace (EXIF) */
  public static final XmlWriter.Namespace EXIF_NAMESPACE = new
      XmlWriter.Namespace(EXIF_ALIAS, EXIF);

  public static final String SLIDESHOW_REL = PHOTOS_PREFIX + "slideshow";

  public static final String REPORT_REL = PHOTOS_PREFIX + "report";
}
