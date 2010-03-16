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


package com.google.gdata.client.appsforyourdomain.adminsettings;

import com.google.gdata.util.common.util.Base64;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class with helper methods
 * 
 * 
 * 
 */
public class EncodeUtil {

  /**
   * Private constructor to prevent instantiation.
   */
  private EncodeUtil() {
  }

  /**
   * Base64 encodes the given file from path.
   * 
   * @param filePath location of the file to be encoded.
   * @return string base64 encoded contents of the file
   */
  public static String encodeBinaryFile(String filePath) throws IOException {
    InputStream stream = null;
    String base64encodedValue = null;
    try {
      stream = new FileInputStream(filePath);
      byte[] bytes = new byte[stream.available()];
      stream.read(bytes);
      base64encodedValue = Base64.encode(bytes);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      stream.close();
    }
    return base64encodedValue;
  }
}

