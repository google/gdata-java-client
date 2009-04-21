
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
 * The <code>Intersection</code> parser provides one of the more powerful
 * pieces of functionality in the parser framework. It returns a successful
 * match only if both the <code>left</code> and <code>right</code> sub-parsers
 * match. Additionally, the amount of parse data passed to the
 * <code>right</code> parser is restricted to the size of the match for the
 * <code>left</code> parser. This allows certain types of recursively defined
 * grammars to be more easily specified. Note that this definition of
 * intersection is not the same as a set-theoretic definition. In particular,
 * this definition is not commutative.
 *
 *   Parser i = Parser.intersection(Chset.ALPHA.plus(), Chset.ALNUM.plus());
 *   i.parse("a", null)  -> matches "a"
 *   i.parse("a1", null) -> matches "a"
 *
 *   Parser j = Parser.intersection(Chset.ALNUM.plus(), Chset.ALPHA.plus());
 *   j.parse("a", null)  -> matches "a"
 *   j.parse("a1", null) -> no match, because ALNUM+ matches "a1" which doesn't
 *                          match ALPHA+
 *
 * @param <T>
 * @see Parser
 * 
 */
public class Intersection<T> extends Parser<T> {
  private Parser<? super T> left;
  private Parser<? super T> right;

  /**
   * Class constructor.
   *
   * @param left The <code>Parser</code> that is first matched against the
   * parse buffer. It restricts the region of the parse buffer that is matched
   * against the <code>right</code> parser.
   *
   * @param right The <code>Parser</code> that is matched against the parse
   * buffer if the <code>left</code> parses has already matched.
   */
  public Intersection(Parser<? super T> left, Parser<? super T> right) {
    this.left = left;
    this.right = right;
  }

  /**
   * Matches the prefix of the buffer (<code>buf[start,end)</code>) being
   * parsed against the <code>left</code> and <code>right</code> sub-parsers.
   *
   * @see Parser#parse
   */
  @Override
  public int parse(char[] buf, int start, int end, T data) {
    int left_hit = left.parse(buf, start, end, data);
    if (left_hit != NO_MATCH) {
      int right_hit = right.parse(buf, start, start + left_hit, data);
      if (left_hit == right_hit) {
        return left_hit;
      }
    }
    return NO_MATCH;
  }
}
