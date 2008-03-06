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
package com.google.gdata.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 *
 * Note! The class is used only for logging in verbose mode - it provides a
 * feature to get content of the XML data sent.
 *
 * 
 *
 */
public class LoggableInputStream extends InputStream {
  StringWriter sw = new StringWriter();
  private InputStream stream;
  private Logger logger;

  public LoggableInputStream(Logger logger, InputStream stream) {
    this.logger = logger;
    this.stream = stream;
  }

  @Override
  public int available() throws IOException {
    return stream.available();
  }

  @Override
  public void close() throws IOException {
    logger.finest(sw.toString());
    sw = new StringWriter();
    stream.close();
  }

  @Override
  public boolean equals(Object obj) {
    return stream.equals(obj);
  }

  @Override
  public int hashCode() {
    return stream.hashCode();
  }

  @Override
  public void mark(int readlimit) {
    stream.mark(readlimit);
  }

  @Override
  public boolean markSupported() {
    return stream.markSupported();
  }

  @Override
  public int read() throws IOException {
    int readInt = stream.read();
    sw.write(readInt);
    return readInt;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int read = stream.read(b, off, len);
    if (read > 0) {
      for (int i = off; i < off + read; i++) {
        sw.write(b[i]);
      }
    }
    return read;
  }

  @Override
  public int read(byte[] b) throws IOException {
    int read = stream.read(b);
    if (read > 0) {
      for (int i = 0; i < read; i++) {
        sw.write(b[i]);
      }
    }
    return read;
  }

  @Override
  public void reset() throws IOException {
    stream.reset();
  }

  @Override
  public long skip(long n) throws IOException {
    return stream.skip(n);
  }

  @Override
  public String toString() {
    return stream.toString();
  }
}
