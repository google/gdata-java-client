
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
 * The <code>Strlit</code> parser performs a case-sensitive string comparison
 * of its <code>str</code> against the prefix of the parse buffer. It is not
 * necessary for the parser to match the entire parse buffer.
 *
 *   Parser p = new Strlit("hello");
 *   p.parse("hello")       -> matches "hello"
 *   p.parse("hello world") -> matches "hello"
 *   p.parse(" hello")      -> no match, string does not start with "hello"
 *   p.parse("HELLO")       -> no match
 *
 * @see Strcaselit
 * 
 */
public class Strlit extends Parser<Object> {
  private String str;

  /**
   * Class constructor.
   *
   * @param str The <code>String</code> being matched against.
   */
  public Strlit(String str) {
    this.str = str;
  }

  /**
   * Performs a case-sensitive comparison of the <code>str</code> member field
   * passed to the <code>Strlit</code> constructor against the prefix of the
   * parse buffer: <code>buf[start,end)</code>. <code>Strlit.str</code> must be
   * a prefix of <code>buf[start,end)</code> for a match to occur.
   *
   * @see Parser#parse
   */
  @Override
  public int parse(char[] buf, int start, int end, Object data) {
    for (int i = 0; i < str.length(); i++) {
      if ((start >= end) ||
          (buf[start] != str.charAt(i))) {
        return NO_MATCH;
      }
      start += 1;
    }
    return str.length();
  }
}
