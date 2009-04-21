
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
 * The <code>Difference</code> parser matches the prefix of the parse buffer if
 * the <code>left</code> parser matches and the <code>right</code> parser does
 * not.
 *
 * The following is a complicated way of constructing a parser which matches a
 * digit:
 *
 *   Parser p = Parser.difference(Chset.ALNUM, Chset.ALPHA);
 *   p.parse("0") -> matches "0"
 *   p.parse("a") -> no match
 *
 * @param <T>
 * @see Parser
 * 
 */
public class Difference<T> extends Parser<T> {
  private Parser<? super T> left;
  private Parser<? super T> right;

  /**
   * Class constructor.
   *
   * @param left The <code>Parser</code> that must match against the
   * parse buffer.
   *
   * @param right The <code>Parser</code> that must not match against
   * the parse buffer.
   */
  public Difference(Parser<? super T> left, Parser<? super T> right) {
    this.left = left;
    this.right = right;
  }

  /**
   * Matches the prefix of the buffer (<code>buf[start,end)</code>) being
   * parsed if, and only if, the <code>left</code> sub-parser matches the
   * incoming state of the buffer and the <code>right</code> sub-parser does
   * not match the incoming state of the parse buffer. It does not make much
   * sense to specify a <code>right</code> sub-parser that matches the empty
   * string as doing so will cause this function to always return
   * <code>NO_MATCH</code>.
   *
   * @see Parser#parse
   */
  @Override
  public int parse(char[] buf, int start, int end, T data) {
    int left_hit = left.parse(buf, start, end, data);
    if (left_hit != NO_MATCH) {
      int right_hit = right.parse(buf, start, end, data);
      if (right_hit == NO_MATCH) {
        return left_hit;
      }
    }
    return NO_MATCH;
  }
}
