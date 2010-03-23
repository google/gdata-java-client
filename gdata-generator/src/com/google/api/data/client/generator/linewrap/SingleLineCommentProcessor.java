// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator.linewrap;

/**
 * Processes any single-line comments in the line.
 */
final class SingleLineCommentProcessor {

  private final String commentPrefix;

  private boolean inComment;

  /**
   * @param commentPrefix prefix to recognize the start of a single-line
   *        comment, e.g. {@code "//"} in Java.
   */
  SingleLineCommentProcessor(String commentPrefix) {
    this.commentPrefix = commentPrefix;
  }

  /**
   * Process the given line to check if it starts with a single-line comment.
   * 
   * @param line current line content to be cut
   * @param prefix current line prefix to be updated if necessary
   * @param firstCut whether this is the first cut in the original input line
   * @return whether the line starts with the single-line comment prefix
   */
  boolean start(String line, StringBuilder prefix, boolean firstCut) {
    if (firstCut) {
      inComment = false;
    }
    if (!inComment && isCuttingPoint(line, 0)) {
      inComment = true;
      prefix.append(commentPrefix).append(' ');
    }
    return inComment;
  }

  /**
   * Returns wehther the given index is the location of the single-line comment
   * prefix.
   * 
   * @param line current line content to be cut
   */
  boolean isCuttingPoint(String line, int index) {
    return line.startsWith(commentPrefix, index);
  }
}
