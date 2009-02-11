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


package com.google.gdata.data;

/**
 * Shared text construct interface.
 * 
 * 
 */
public interface ITextConstruct {

  /**
   * Defines the possible text construct types: TEXT, HTML, and XHTML.
   */
  public static class Type {
    public static final int TEXT = 1;
    public static final int HTML = 2;
    public static final int XHTML = 3;
  }
  
  /**
   * Returns this text construct's type (text, HTML, or XHTML).
   * 
   * @return an integer value of 1 to represent TEXT, 2 to represent HTML,
   * or 3 to represent XHTML.
   */
  public int getType();

  /** Returns a plain-text representation of this text construct. */
  public String getPlainText();
}
