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


package com.google.gdata.client.uploader;

import java.io.IOException;

/**
 * A seekable, read-only buffer of data to be upload uploading. 
 * 
 * 
 */
public interface UploadData {
  /**
   * Returns the total number of bytes of data in the buffer.
   */
  public long length();
  
  /**
   * Reads up to {@code destination.length} bytes from the current position into
   * the destination buffer.
   *
   * @throws IOException if the data could not be read.
   */
  public void read(byte[] destination) throws IOException;

  /**
   * Reads up to {@code length} bytes into the {@code chunk} buffer.
   * 
   * @param i the start offset in the destination buffer.
   * @throws IOException if the data could not be read.
   */
  public int read(byte[] chunk, int i, int length) throws IOException;
  
  /**
   * Sets the offset from the start of the the source data from which the next
   * {@code read} will begin.
   *
   * @throws IOException if position is negative or past the end of the data.
   */
  public void setPosition(long position) throws IOException;
}
