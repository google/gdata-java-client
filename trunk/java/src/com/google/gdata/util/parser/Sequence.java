
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.gdata.util.parser;

/**
 * The <code>Sequence</code> parser matches if its <code>left</code> parser
 * matches the prefix of the parse buffer and then its <code>right</code>
 * parser matches (in sequence) the prefix of whatever remains in the parse
 * buffer. Contrast this with the <code>Alternative</code> and
 * <code>Intersection</code> parsers which apply their sub-parsers to the same
 * portion of the parse buffer.
 *
 * The following matches a string composed of letters followed by digits:
 *
 *   Parser p = Parser.sequence(Chset.ALPHA.plus(), Chset.DIGIT.plus());
 *   p.parse("a0")     -> matches "a0"
 *   p.parse("aaa0")   -> matches "aaa0"
 *   p.parse("aaa000") -> matches "aaa0000"
 *   p.parse("a1a")    -> matches "a1"
 *   p.parse("a")      -> no match, does not end in a digit
 *   p.parse("0")      -> no match, does not start with a letter
 *
 * @param <T>
 * @see Parser
 * 
 */
public class Sequence<T> extends Parser<T> {
  private Parser<? super T> left;
  private Parser<? super T> right;

  /**
   * Class constructor.
   *
   * @param left The <code>Parser</code> that is matched against the parse
   * buffer first.
   *
   * @param right The <code>Parser</code> that is matched against what remains
   * of the parse buffer if the <code>left</code> parser matched.
   */
  public Sequence(Parser<? super T> left, Parser<? super T> right) {
    this.left = left;
    this.right = right;
  }

  /**
   * Matches the prefix of the buffer (<code>buf[start,end)</code>) being
   * parsed against the <code>left</code> and <code>right</code> sub-parsers in
   * sequence.
   *
   * @see Parser#parse
   */
  @Override
  public int parse(char[] buf, int start, int end, T data) {
    int left_hit = left.parse(buf, start, end, data);
    if (left_hit != NO_MATCH) {
      int right_hit = right.parse(buf, start + left_hit, end, data);
      if (right_hit != NO_MATCH) {
        return left_hit + right_hit;
      }
    }
    return NO_MATCH;
  }
};
