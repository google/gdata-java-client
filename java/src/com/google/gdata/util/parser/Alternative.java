
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
 * The <code>Alternative</code> parser parses either the <code>left</code> or
 * <code>right</code> sub-parsers. If the <code>left</code> parser matches, the
 * <code>right</code> parser is not invoked. This short-circuiting is similar
 * to the way the evaluation of the logical-or (<code>||</code>) operator is
 * short-circuited in C++ and Java. It is important to be aware of the
 * short-circuiting in cases where the <code>right</code> parser contains a
 * <code>Action</code>.
 *
 * The following is a complicated way of constructing a parser which matches
 * letters:
 *
 *   Parser p = Parser.alternative(Chset.LOWER, Chset.UPPER);
 *   p.parse("a") -> matches "a"
 *   p.parse("A") -> matches "A"
 *
 * @param <T>
 * @see Parser
 * 
 */
public class Alternative<T> extends Parser<T> {
  private Parser<? super T> left;
  private Parser<? super T> right;

  /**
   * Class constructor.
   *
   * @param left The <code>Parser</code> that is matched against the parse
   * buffer first.
   *
   * @param right The <code>Parser</code> that is matched against the parse
   * buffer if the <code>left</code> parser does not match.
   */
  public Alternative(Parser<? super T> left, Parser<? super T> right) {
    this.left = left;
    this.right = right;
  }

  /**
   * Matches the current prefix of the buffer being parsed
   * <code>buf[start,end]</code> against <code>left</code> or
   * <code>right</code> sub-parsers.
   *
   * @see Parser#parse
   */
  @Override
  public int parse(char[] buf, int start, int end, T data) {
    int hit = left.parse(buf, start, end, data);
    if (hit != NO_MATCH) {
      return hit;
    }
    return right.parse(buf, start, end, data);
  }
};
