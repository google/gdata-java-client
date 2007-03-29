/* Copyright (c) 2006 Google Inc.
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
 * Photos namespaces for the photos schema, these are the namespaces that are
 * used in the Picasaweb GData apis.
 *
 * 
 */
public class Namespaces {

  /** Picasaweb namespace. */
  public static final String PHOTOS = "http://schemas.google.com/photos/2007";
  public static final String PHOTOS_PREFIX = PHOTOS + "#";
  public static final XmlWriter.Namespace PHOTOS_NAMESPACE
      = new XmlWriter.Namespace("gphoto", PHOTOS);

  /** Exif namespace. */
  public static final String EXIF
      = "http://schemas.google.com/photos/exif/2007";
  public static final XmlWriter.Namespace EXIF_NAMESPACE
      = new XmlWriter.Namespace("exif", EXIF);

  private Namespaces() {}
}
