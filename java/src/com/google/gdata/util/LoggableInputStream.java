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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Logger;

/**
 *
 * Logs content of the data sent to the stream if log level is
 * set to FINEST.
 *
 * 
 */

public class LoggableInputStream extends FilterInputStream {
  private final StringWriter sw = new StringWriter();
  private final Logger logger;
  private boolean closed = false;

  public LoggableInputStream(Logger logger, InputStream stream) {
    super(stream);
    this.logger = logger;
  }

  @Override
  public void close() throws IOException {
    // circumvent double close
    if (!closed){
      logger.finest(sw.toString());
      closed = true;
    }
    super.close();
  }

  @Override
  public int read() throws IOException {
    int readInt = super.read();
    sw.write(readInt);
    return readInt;
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    int read = super.read(b, off, len);
    if (read > 0) {
      String s = new String(b, off, read);
      sw.write(s);
    }
    return read;
  }

  @Override
  public int read(byte[] b) throws IOException {
    int read = super.read(b);
    if (read > 0) {
      String s = new String(b, 0, read);
      sw.write(s);
    }
    return read;
  }
}
