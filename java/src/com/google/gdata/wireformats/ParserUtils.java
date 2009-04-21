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


package com.google.gdata.wireformats;

import com.google.gdata.util.io.base.UnicodeReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

/**
 * Utility functions shared across parser implementations.
 */
public abstract class ParserUtils {

  /**
   * This class is not meant to be instantiated.
   */
  private ParserUtils() {}

  /**
   * Returns a reader based upon the character set (encoding).
   * Use the special UnicodeReader class for Unicode encodings, to work
   * around a JDK bug related to Unicode byte order markings.
   * 
   * @param input content to parse 
   * @param cs character set (encoding) used by content
   * @return reader
   * @throws IOException if specified encoding cannot be used 
   */
  public static Reader getInputReader(InputStream input, Charset cs)
      throws IOException {
    if (cs.name().toLowerCase().startsWith("utf-")) {
      return new UnicodeReader(input, cs.name());
    }
    return new InputStreamReader(input, cs);
  }

}
