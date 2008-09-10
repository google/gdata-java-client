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


package com.google.gdata.util.common.io;

import com.google.gdata.util.common.base.Nullable;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility methods for working with {@link Closeable} objects.
 *
 * 
 */
public final class Closeables {
  private static final Logger logger
      = Logger.getLogger(Closeables.class.getName());

  private Closeables() {}

  /**
   * Close a {@link Closeable}, with control over whether an
   * {@code IOException} may be thrown. This is primarily useful in a
   * finally block, where a thrown exception needs to be logged but not
   * propagated (otherwise the original exception will be lost). The
   * {@code Closeable} object is also checked to see if it implements
   * {@link Flushable} and if it does, it is flushed before closing.
   *
   * <p>If {@code flush} throws an exception, we try to close the
   * {@code Closeable} before continuing. If {@code close} also throws an
   * exception and {@code swallowIOException} is false, then we rethrow
   * {@code close} exception, otherwise we rethrow the {@code flush}
   * exception.
   *
   * <p>If {@code swallowIOException} is true then we never throw
   * {@code IOException} but merely log it.
   *
   * <p>Example:
   *
   * <p><pre>public void useStreamNicely() throws OtherException {
   * boolean threw = true;
   * SomeStream stream = null;
   * try {
   *   stream = new SomeStream("foo");
   *   // Some code which does something with the Stream. May throw a
   *   // Throwable.
   *   threw = false; // No throwable thrown.
   * } catch (SomeException e) {
   *   throw new OtherException(e);
   * } finally {
   *   // Close (and flush if Flushable) the stream.
   *   // If an exception occurs, only rethrow it if (threw==false).
   *   Closeables.close(stream, threw);
   * }
   * </pre>
   *
   * @param closeable the {@code Closeable} object to be closed, or null,
   *     in which case this method does nothing
   * @param swallowIOException if true, don't propagate IO exceptions
   *     thrown by the {@code close} or {@code flush} methods
   * @throws IOException if {@code swallowIOException} is false and
   *     {@code close} or {@code flush} throws an {@code IOException}.
   */
  public static void close(Closeable closeable,
      boolean swallowIOException) throws IOException {
    if (closeable == null) {
      return;
    }

    IOException flushException = null;

    if (closeable instanceof Flushable) {
      try {
        Flushables.flush((Flushable) closeable, false);
      } catch (IOException e) {
        flushException = e;
        if (swallowIOException) {
          logger.log(Level.WARNING,
              "IOException thrown while flushing Flushable.", flushException);
        }
      }
    }

    try {
      closeable.close();
    } catch (IOException e) {
      if (swallowIOException) {
        logger.log(Level.WARNING,
            "IOException thrown while closing Closeable.", e);
      } else {
        throw e;
      }
    }

    if (flushException != null && !swallowIOException) {
      throw flushException;
    }
  }

  /**
   * Equivalent to calling {@code close(closeable, true)}, but with no
   * IOException in the signature.
   * @param closeable the {@code Closeable} object to be closed, or null, in
   *      which case this method does nothing
   */
  public static void closeQuietly(Closeable closeable) {
    try {
      close(closeable, true);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "IOException should not have been thrown.", e);
    }
  }
}
