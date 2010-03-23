// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator.linewrap;

/**
 * Processes quotes in a line.  Used to detect if there are any quotes.  Both
 * {@code '"'} and {@code '\''} are valid quote characters.
 */
final class QuoteProcessor {

  private boolean lastSlash = false;

  private char lastQuote = 0;

  /**
   * Returns whether to skip processing the given character because it is part
   * of a quote.  Note that this method must be called for all previous
   * characters on the line in order for the result to be correct.
   */
  boolean isSkipped(char ch) {
    if (lastSlash) {
      lastSlash = false;
      return true;
    }
    if (lastQuote != 0) {
      if (ch == lastQuote) {
        // end of quote sequence
        lastQuote = 0;
      } else if (ch == '\\') {
        // skip escaped character
        lastSlash = true;
      }
      return true;
    }
    if (ch == '\'' || ch == '"') {
      // start of quote sequence
      lastQuote = ch;
      return true;
    }
    return false;
  }
}
