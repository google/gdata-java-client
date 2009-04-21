
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
 * The <code>Rule</code> parser has a very simple reason for existence: in
 * order to construct recursive grammars, a parser needs to reference
 * itself. But the normal parser construction routines don't allow that to be
 * done. An alternative to this design would be to allow the modification of
 * the sub-parsers of <code>Alternative</code>, <code>Sequence</code>,
 * etc. after the objects have been constructed.
 *
 * The following matches a sequence of letters and digits that end in a
 * digit. This could be more easily accomplished, but it demonstrates how a
 * recursive parser can be constructed:
 *
 *   Rule r = new Rule();
 *   r.set(Parser.alternative(Chset.DIGIT, Parser.sequence(Chset.ALPHA, r)));
 *   r.parse("a0")   -> matches "a0"
 *   r.parse("a0b1") -> matches "a0b1"
 *   r.parse("a")    -> no match
 *
 * @param <T>
 * @see Parser
 * 
 */
public class Rule<T> extends Parser<T> {
  private Parser<T> subject;

  /**
   * Class constructor for a <code>null</code> <code>subject</code>.
   */
  public Rule() {
    this(null);
  }

  /**
   * Class constructor.
   *
   * @param subject The <code>Parser</code> that this parser delegates parsing
   * responsibilities to.
   */
  public Rule(Parser<T> subject) {
    this.subject = subject;
  }

  /**
   * Delegates parsing responsibilties to <code>subject</code> if it is
   * non-null, and returns <code>NO_MATCH</code> otherwise.
   *
   * @see Parser#parse
   */
  @Override
  public int parse(char[] buf, int start, int end, T data) {
    if (subject != null) {
      return subject.parse(buf, start, end, data);
    } else {
      return NO_MATCH;
    }
  }

  /**
   * Sets the <code>subject</code> member.
   *
   * @param subject The<code>Parser</code> that this parser delegates
   * parsing responsibilities to.
   */
  public void set(Parser<T> subject) {
    this.subject = subject;
  }
}
