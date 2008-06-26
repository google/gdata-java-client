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
package com.google.gdata.util;

import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 *
 * Logs content of the data sent to the stream if log level is
 * set to FINEST.
 *
 * 
 *
 */
public class LoggableOutputStream extends FilterOutputStream {
  private ByteArrayOutputStream bos = new ByteArrayOutputStream();

  private Logger logger;

  public LoggableOutputStream(Logger logger, OutputStream stream) {
    super(stream);
    this.logger = logger;
  }

  @Override
  public void write(int b) throws IOException {
    // Write to the original stream
    super.write(b);
    // Write also to log
    bos.write(b);
  }

  @Override
  public void close() throws IOException {
    super.close();
    logger.finest(bos.toString()); //convert using the default charset
  }

  @Override
  public void flush() throws IOException {
    super.flush();
    logger.finest(bos.toString());
    bos = new ByteArrayOutputStream();
  }
}
