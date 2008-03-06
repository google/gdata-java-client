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
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 *
 * Note! The class is used only for logging - it provides a feature to get
 * content of the XML data sent in the logger.
 *
 * 
 *
 */
public class LoggableOutputStream extends OutputStream {


  private OutputStream stream;

  StringWriter sw = new StringWriter();

  private Logger logger;

  public LoggableOutputStream(Logger logger, OutputStream stream) {
    this.logger = logger;
    this.stream = stream;
  }

  @Override
  public void write(int b) throws IOException {
    // Write to the original stream
    stream.write(b);
    // Write also to log
    sw.write(b);
  }

  @Override
  public void close() throws IOException {
    stream.close();
    sw.flush();
    logger.finest(sw.toString());
    sw = new StringWriter();
  }

  @Override
  public boolean equals(Object obj) {
    return stream.equals(obj);
  }

  @Override
  public void flush() throws IOException {
    stream.flush();
    sw.flush();
    logger.finest(sw.toString());
    sw = new StringWriter();
  }

  @Override
  public int hashCode() {
    return stream.hashCode();
  }

  @Override
  public String toString() {
    return stream.toString();
  }
}