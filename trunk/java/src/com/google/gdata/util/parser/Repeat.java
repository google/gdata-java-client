
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
 * The <code>Repeat</code> parser returns a successful match if its
 * <code>subject</code> parser matches at least <code>min</code> times, but not
 * more than <code>max</code> times. The <code>Repeat</code> parser is used to
 * implement the <code>Parser.star()</code> and <code>Parser.plus()</code>
 * methods by specifying appropriate values for <code>min</code> and
 * <code>max</code>.
 *
 * The following matches a string of 1 to 3 letters:
 *
 *   Parse p = Chset.ALPHA.repeat(1, 3);
 *   p.parse("")     -> no match
 *   p.parse("a")    -> matches "a"
 *   p.parse("aa")   -> matches "aa"
 *   p.parse("aaa")  -> matches "aaa"
 *   p.parse("aaaa") -> matches "aaa"
 *
 * @param <T>
 * 
 */
public class Repeat<T> extends Parser<T> {
  private Parser<T> subject;
  private int min;
  private int max;

  /**
   * Class constructor that is used for creating a <code>Repeat</code> object
   * that will match its <code>subject</code> <code>min</code> or more times.
   *
   * @param subject The <code>Parse</code> that this parser will repeatedly
   * match.
   *
   * @param min The minimum number of times the <code>subject</code> parser
   * must match.
   */
  public Repeat(Parser<T> subject, int min) {
    this(subject, min, -1);
  }

  /**
   * Class constructor that is used for creating a <code>Repeat</code> object
   * that will match its <code>subject</code> at least <code>min</code> times
   * but not more than <code>max</code> times. Specifying <code>-1</code> for
   * <code>max</code> causes the parser to match as many times as possible.
   *
   * @param subject The <code>Parse</code> that this parser will repeatedly
   * match.
   *
   * @param min The minimum number of times the <code>subject</code> parser
   * must match.
   *
   * @param max The maximum number of times the <code>subject</code> parser can
   * match.
   */
  public Repeat(Parser<T> subject, int min, int max) {
    this.subject = subject;
    this.min = min;
    this.max = max;
  }

  /**
   * Matches the <code>subject</code> parser against the prefix of the buffer
   * (<code>buf[start,end)</code>) being parsed if the <code>subject</code>
   * parser matches at least <code>min</code> times and not more than
   * <code>max</code> times in sequence.
   *
   * @see Parser#parse
   */
  @Override
  public int parse(char[] buf, int start, int end, T data) {
    int hit = 0;

    for (int i = 0; i != max; i++) {
      int next = subject.parse(buf, start + hit, end, data);

      if (next == 0) {
        break;
      }
      if (next == NO_MATCH) {
        if (i < min) {
          return NO_MATCH;
        }
        break;
      }

      hit += next;
    }

    return hit;
  }
}
