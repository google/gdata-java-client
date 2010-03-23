// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator.linewrap;

/**
 * Utilities for strings.
 */
public class Strings {

  /**
   * Line separator to use for this OS, i.e. {@code "\n"} or {@code "\r\n"}.
   */
  public static final String LINE_SEPARATOR =
      System.getProperty("line.separator");

  /**
   * Returns the index of the first non-space ({@code ' '}) character in the
   * given string whose index is greater than or equal to the given lower bound.
   * 
   * @param string string or {@code null} for {@code -1} result
   * @param lowerBound lower bound for index
   * @return index or {@code -1} if not found or for {@code null} input
   */
  public static int indexOfNonSpace(String string, int lowerBound) {
    if (string == null) {
      return -1;
    }
    return indexOfNonSpace(string, lowerBound, string.length() - 1);
  }

  /**
   * Returns the index of the first non-space ({@code ' '}) character in the
   * given string whose index is greater than or equal to the given lower bound
   * and less than or equal to the given upper bound.
   * 
   * @param string string or {@code null} for {@code -1} result
   * @param lowerBound lower bound for index
   * @param upperBound upper bound for index
   * @return index or {@code -1} if not found or for {@code null} input
   */
  public static int indexOfNonSpace(String string, int lowerBound,
      int upperBound) {
    if (string != null) {
      upperBound = Math.min(upperBound, string.length() - 1);
      for (int i = Math.max(0, lowerBound); i <= upperBound; i++) {
        if (' ' != string.charAt(i)) {
          return i;
        }
      }
    }
    return -1;
  }

  /**
   * Returns the index of the last non-space ({@code ' '}) character in the
   * given string whose index is less than or equal to the given upper bound.
   * 
   * @param string string or {@code null} for {@code -1} result
   * @param upperBound upper bound for index
   * @return index or {@code -1} if not found or for {@code null} input
   */
  public static int lastIndexOfNonSpace(String string, int upperBound) {
    return lastIndexOfNonSpace(string, upperBound, 0);
  }

  /**
   * Returns the index of the first non-space ({@code ' '}) character in the
   * given string whose index is greater than or equal to the given lower bound
   * and less than or equal to the given upper bound.
   * 
   * @param string string or {@code null} for {@code -1} result
   * @param upperBound upper bound for index
   * @param lowerBound lower bound for index
   * @return index or {@code -1} if not found or for {@code null} input
   */
  public static int lastIndexOfNonSpace(String string, int upperBound,
      int lowerBound) {
    if (string != null) {
      lowerBound = Math.max(0, lowerBound);
      for (int i = Math.min(upperBound, string.length() - 1); i >= lowerBound; i--) {
        if (' ' != string.charAt(i)) {
          return i;
        }
      }
    }
    return -1;
  }

  private Strings() {
  }
}
