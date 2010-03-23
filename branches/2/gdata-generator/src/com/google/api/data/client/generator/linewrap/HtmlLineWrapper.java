// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator.linewrap;

/**
 * Line wrapper for HTML files.
 */
public final class HtmlLineWrapper extends XmlLineWrapper {

  private static final HtmlLineWrapper INSTANCE = new HtmlLineWrapper();

  /** Returns the instance of the HTML line wrapper. */
  public static LineWrapper get() {
    return INSTANCE;
  }

  /** Computes line wrapping result for the given HTML text. */
  public static String wrap(String text) {
    return INSTANCE.compute(text);
  }

  private HtmlLineWrapper() {
  }
}