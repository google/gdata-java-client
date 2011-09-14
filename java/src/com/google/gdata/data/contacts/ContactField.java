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


package com.google.gdata.data.contacts;

/**
 * Describes an interface that is common to all Contact fields.
 *
 * 
 */
public interface ContactField {
  /**
   * Returns whether this field has a readonly attribute.
   *
   * @return whether it has a readonly attribute.
   */
  public boolean hasReadonly();

  /**
   * Returns the value of the read-only attribute.
   *
   * @return the value of the readonly attribute.
   */
  public Boolean getReadonly();

  /**
   * Returns whether this field is read-only.
   * This is different than getReadonly in that the former can return a null
   * value if the attribute doesn't exist, whereas this method will return false
   * in that case.
   * @return whether the field is read-only.
   */
  public boolean isReadonly();

  /**
   * Sets whether this field is read-only.
   *
   * @param readonly whether this field is read-only or <code>null</code> to
   *     reset
   */
  public void setReadonly(Boolean readonly);

  /**
   * Returns whether it has the source of the data in this field.
   *
   * @return whether it has the source of the data in this field
   */
  public boolean hasSource();

  /**
   * Returns the source of the data in this field, or null if there is no
   * source attribute.
   *
   * @return source of the data in this field
   */
  public String getSource();

  /**
   * Sets the source of the data in this field.
   *
   * @param source source of the data in this field or <code>null</code> to
   *     reset
   */
  public void setSource(String source);

  /**
   * Returns the profile email(s) to which this field causes a link, if any.
   *
   * @return profile email(s) to which this field causes a link.
   */
  public String getLinksto();

  /**
   * Sets the profile email(s) to which this field causes a link.
   *
   * @param linksto The profile email(s) to which this field causes a link or
   *     <code>null</code> to reset
   */
  public void setLinksto(String linksto);

  /**
   * Returns whether it has the profile email(s) to which this field causes a
   * link.
   *
   * @return whether it has the profile email(s) to which this field causes a
   *     link.
   */
  public boolean hasLinksto();
}
