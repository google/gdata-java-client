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

/**
 * Data interface for all of the photos data items.  This is the base interface
 * for the methods that all photos items should support.
 *
 * This interface contains the setters and getters for the system id of an
 * element, which is the permanent id on the server.
 *
 * 
 */
public interface GphotoData extends Extensible {

  /**
   * Gets the gphoto:id of the data object.  The gphoto:id is the
   * photos-specific system id of the object, provided by the server at object
   * creation time.
   * 
   * @return the Gphoto id.
   */
  public String getGphotoId();

  /**
   * Sets the id of this feed or entry.  This field is normally read-only, but
   * can be used on the client to perform a copy operation when creating a new
   * object based on an existing object.
   * 
   * @param id the long version of the id of this feed or entry.
   */
  public void setGphotoId(Long id);

  /**
   * Sets the id of this feed or entry.  This field is normally read-only, but
   * can be used on the client to perform a copy operation when creating a new
   * object based on an existing object.
   *
   * @param id the string version of the id of this feed or entry.
   */
  public void setGphotoId(String id);
}
