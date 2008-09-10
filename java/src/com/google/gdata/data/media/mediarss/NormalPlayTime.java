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


package com.google.gdata.data.media.mediarss;

import java.text.ParseException;

/**
 * Time specification object which tries to conform to section 3.6
 * of RFC 2326 (Normal Play Time).
 *
 * It does not support ranges.
 *
 * It only supports a millisecond precision. Any time more precise than
 * that will be lost when parsing.
 *
 * 
 */
public class NormalPlayTime {
  /** The NormalPlayType 'now' */
  public static final NormalPlayTime NOW = new NormalPlayTime(true, -1l);

  private boolean isNow;
  private long ms;

  /**
   * Creates a NormalPlayTime object.
   *
   * If you want 'NOW', use the constant {@link #NOW}.
   *
   * @param ms time offset, in milliseconds
   */
  public NormalPlayTime(long ms) {
    this(false, ms);
  }

  private NormalPlayTime(boolean now, long ms) {
    isNow = now;
    this.ms = ms;
  }

  /** Check whether this time is the special 'now' time. */
  public boolean isNow() {
    return isNow;
  }

  /** Returns the offset it milliseconds, -1l for NOW. */
  public long getTimeOffsetMs() {
    return ms;
  }

  /**
   * Parses a NormalPlayTime object and return it.
   *
   * @param stringRep string representation
   * @return a NormalPlayTime, null if and only if stringRep == null
   * @throws ParseException if the string representation could not
   *   be parsed
   */
  public static NormalPlayTime parse(String stringRep)
      throws ParseException {
    if (stringRep == null) {
      return null;
    }

    if ("now".equals(stringRep)) {
      return NOW;
    }

    NPTParser parser = new NPTParser(stringRep);
    return new NormalPlayTime(parser.parse());
  }

  /** Gets a valid string representation (seconds "." fraction). */
  @Override
  public String toString() {
    return getNptSecondsRepresentation();
  }

  /**
   * Gets the standard {@code seconds.fraction } representation
   * for this object.
   *
   * @return {@code seconds.fraction} or {@code "now"}
   */
  public String getNptSecondsRepresentation() {
    if (isNow) {
      return "now";
    }

    long seconds = ms / 1000l;
    long fraction = ms % 1000l;
    if (fraction == 0) {
      return Long.toString(seconds);
    }
    return String.format("%1$d.%2$03d", seconds, fraction);
  }

  /**
   * Gets the standard {@code  hh:mm:ss.fraction } representation
   * for this object.
   *
   * @return {@code hh:mm:ss.fraction} or {@code "now"}
   */
  public String getNptHhmmssRepresentation() {
    if (isNow) {
      return "now";
    }

    long fraction = ms % 1000l;
    long totalseconds = ms / 1000l;
    long seconds = totalseconds % 60l;
    long totalminutes = totalseconds / 60l;
    long minutes = totalminutes % 60l;
    long hours = totalminutes / 60l;
    if (fraction > 0) {
      return String.format("%1$02d:%2$02d:%3$02d.%4$03d",
          hours, minutes, seconds, fraction);
    } else {
      return String.format("%1$02d:%2$02d:%3$02d", hours, minutes, seconds);
    }
  }

  /**
   * Parser class for a NormalPlayTime that supports both time representations.
   */
  private static class NPTParser {
    private final String text;
    private final int length;
    private int currentIndex;
    /** Current character, 0 when the end is reached. */
    private char current;

    private static final char EOF = '\0';

    public NPTParser(String text) {
      this.text = text;
      this.length = text.length();
      currentIndex = -1;
      next();
    }

    private long parse() throws ParseException {
      long ms;
      int first = parseNumber();

      if (current == ':') {
        int hours = first;
        next();
        long minutes = parseNumber();
        assertCurrentIs(':');
        next();
        long seconds = parseNumber();
        ms = ((((hours * 60l) + minutes) * 60l) + seconds) * 1000l;
      } else {
        ms = first * 1000l;
      }
      if (current == '.') {
        next();
        int exp = 100;
        for (int i = 0; i <= 3 && isDigit(); next(), i++, exp /= 10) {
          ms += exp * digitValue();
        }
        // Ignore extra fraction which can't be stored
        parseNumber();
      }
      assertCurrentIs(EOF);
      return ms;
    }

    private int parseNumber() {
      int retval;
      for (retval = 0; isDigit(); next()) {
        retval *= 10;
        retval += digitValue();
      }
      return retval;
    }

    private int digitValue() {
      return current - '0';
    }

    private boolean isDigit() {
      return current >= '0' && current <= '9';
    }

    private void assertCurrentIs(char c) throws ParseException {
      if (c != current) {
        throw new ParseException("Unexpected character", currentIndex);
      }
    }

    private void next() {
      currentIndex ++;
      if (currentIndex >= length) {
        current = EOF;
      } else {
        current = text.charAt(currentIndex);
      }
    }
  }
}
