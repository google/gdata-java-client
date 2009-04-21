
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
 * Actions are fired when their <code>subject</code> parser matches. The
 * <code>Callback</code> class is passed the matching portion of the parse
 * text. Note that the <code>Action</code> parser wraps around its subject. It
 * will parse exactly the same text that its subject parser parses.
 *
 *   Parser p = Chset.ALPHA.plus().action(new Callback() {
 *       public void handle(char[] buf, int start, int end, Object data) {
 *         StringBuffer str = (StringBuffer) data;
 *         str.delete(0, str.length());
 *         str.append(buf, start, end - start);
 *       }
 *     });
 *
 *   StringBuffer buf = new StringBuffer();
 *   p.parse("a")     -> matches "a", buf == "a"
 *   p.parse("abcde") -> matches "abcde", buf == "abcde"
 *   p.parse("a0")    -> matches "a", buf == "a"
 *   p.parse("0")     -> no match, buf unchanged
 *
 * @param <T>
 * @param <U>
 * @see Callback
 * @see Parser
 * 
 */
public class Action<T, U extends T> extends Parser<U> {
  private Parser<T> subject;
  private Callback<U> callback;

  /**
   * Class constructor.
   *
   * @param subject The <code>Parser</code> that this action is wrapping
   * around.
   *
   * @param callback The <code>Callback</code> that will be invoked if the
   * <code>subject</code> parser returns a result different than
   * <code>NO_MATCH</code>.
   */
  public Action(Parser<T> subject, Callback<U> callback) {
    this.subject = subject;
    this.callback = callback;
  }

  /**
   * Matches the <code>subject</code> parser against the current state of the
   * buffer being parsed. If the <code>subject</code> parser hits (returns a
   * result other than <code>NO_MATCH</code>), <code>callback.handle</code>
   * will be invoked and passed the matching region of the parse buffer.
   *
   * @see Parser#parse
   */
  @Override
  public int parse(char[] buf, int start, int end, U data) {
    int hit = subject.parse(buf, start, end, data);
    if (hit != NO_MATCH) {
      callback.handle(buf, start, start + hit, data);
    }
    return hit;
  }
}
