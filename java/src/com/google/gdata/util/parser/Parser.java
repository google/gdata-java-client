
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

import java.io.*;

/**
 * The com.google.gdata.util.parser package provides a framework for creating recursive
 * descent parsers. A fairly straightforward transformation exists between EBNF
 * (extended Backus-Naur form) grammars and code used to construct a parser
 * using this framework that will match the grammar. The intention of this
 * package is to obviate the need to create small mini-parsers for tasks that
 * don't feel large enough for a standard compiler-compiler (like JavaCC) but
 * still need more formalism than simple string tokenization.
 *
 * The basic approach this framework takes to parsing is to define several
 * types of leaf parsers which know how to parse a particular type of object
 * (character set, string literal, etc.) and to then combine them together in
 * interesting ways. For example, a parser for a comma separated list of
 * integers would look like:
 *
 *     Parser Chset.DIGIT.plus().list(new Chset(','));
 *
 * The EBNF this represents is:
 *
 *     sent: [0-9]+ ("," [0-9]+)*
 *
 * The Parser.list() functionality is sometimes represented as the '%' operator
 * in EBNF extensions. It performs the transformation:
 *
 *     a % b --> a (b a)*
 *
 * The leaf parsers that are currently defined are:
 *
 *   @see Chset
 *   @see Strlit
 *   @see Strcaselit
 *
 * The operators which combine 1 or 2 parsers together are:
 *
 *   @see Action
 *   @see Alternative
 *   @see Difference
 *   @see Intersection
 *   @see Repeat
 *   @see Rule
 *   @see Sequence
 *
 * In general, it isn't necessary to create an operator-type parser directly as
 * an appropriate member function usually exists in Parser for creating
 * them. Note that these are purely convenience routines.
 *
 * In general, the parsers are greedy. For example, the Sequence parser will
 * match as much as possible with the left sub-parser before trying the right
 * sub-parser. This behavior can normally be avoided by using a recursive
 * grammar. Consider the following grammar:
 *
 *   token: foo* bar
 *   foo:   [a-z]+
 *   bar:   foo [0-9]+
 *
 * This grammar will fail to parse the string "aa0" because the 'foo*' rule
 * will consume all of the letters and not leave one left for the 'bar'
 * rule. An alternate definition of 'token' can prevent this behavior:
 *
 *   token: (foo token) | bar
 *
 * The parsers created by this parser framework use infinite lookahead. In
 * extreme cases, a parser can be constructed which scans over the parse buffer
 * many times trying to find a match. In practice, this doesn't happen very
 * often.
 *
 * @param <T>
 * 
 */
public abstract class Parser<T> {
  public static final int NO_MATCH = -1;

  /**
   * The parse interface that subclasses must implement.
   *
   * @param buf The character array to match against.
   *
   * @param start The start offset of data within the character array to match
   * against.
   *
   * @param end The end offset of data within the character array to match
   * against.
   *
   * @param udata User defined object that is passed to
   * <code>Callback.handle</code> when an <code>Action</code> fires.
   */
  public abstract int parse(char[] buf, int start, int end, T udata);

  /**
   * Convenience routine to parse a character array.
   */
  public final int parse(char[] buf, T udata) {
    return parse(buf, 0, buf.length, udata);
  }

  /**
   * Convenience routine to parse a string.
   */
  public final int parse(String str, T udata) {
    return parse(str.toCharArray(), udata);
  }

  /**
   * Convenience routine to parse a <code>java.io.Reader</code>.
   */
  public final int parse(Reader reader, T udata) {
    CharArrayWriter writer = new CharArrayWriter();

    try {
      char[] buf = new char[1024];
      int count;

      while ((count = reader.read(buf)) >= 0) {
        writer.write(buf, 0, count);
      }
    } catch (IOException e) {
    }

    return parse(writer.toCharArray(), udata);
  }

  /**
   * Creates a <code>Repeat</code> parser that matches <code>this</code>
   * exactly <code>count</code> times.
   *
   * <code>this{count}</code>
   *
   * @param count The number of times <code>this</code> must match in sequence.
   */
  public final Parser<T> repeat(int count) {
    return new Repeat<T>(this, count, count);
  }

  /**
   * Creates a <code>Repeat</code> parser that matches <code>this</code> at
   * least <code>min</code> times and not mroe than <code>max</code> times.
   *
   * <code>this{min,max}</code>
   *
   * @param min The minimum number of times <code>this</code> must match in
   * sequence.
   *
   * @param max The maximum number of times <code>this</code> is allowed to
   * match in sequence.
   */
  public final Parser<T> repeat(int min, int max) {
    return new Repeat<T>(this, min, max);
  }

  /**
   * Creates a <code>Repeat</code> parser that matches <code>this</code> 0 or
   * more times.
   *
   * <code>this*</code>
   */
  public final Parser<T> star() {
    return new Repeat<T>(this, 0);
  }

  /**
   * Creates a <code>Repeat</code> parser that matches <code>this</code> 1 or
   * more times.
   *
   * <code>this+</code>
   */
  public final Parser<T> plus() {
    return new Repeat<T>(this, 1);
  }

  /**
   * Creates a <code>Repeat</code> parser that matches <code>this</code> either
   * 0 or 1 times.
   *
   * <code>this?</code>
   */
  public final Parser<T> optional() {
    return repeat(0, 1);
  }

  /**
   * Creates a <code>Parser</code> that matches a sequence of <code>this</code>
   * parsers separated by <code>sep</code> parsers. These sequences occur
   * often: space separated words, comma separated words, etc.
   *
   * @param sep The parser which separates instances of <code>this</code>.
   */
  public final Parser<T> list(Parser<? super T> sep) {
    return Parser.<T>sequence(this, Parser.<T>sequence(sep, this).star());
  }

  /**
   * Creates a <code>Action</code> that will fire and call
   * <code>Callback.handle</code> whenever <code>this</code> matches.
   *
   * @param callback The <code>Callback</code> to call when
   * <code>this</code> matches.
   */
  public final <U extends T> Parser<U> action(Callback<U> callback) {
    return new Action<T, U>(this, callback);
  }

  /**
   * Creates an <code>Alternative</code> parser from the <code>left</code> and
   * <code>right</code> sub-parsers.
   *
   * <code>left | right</code>
   */
  public static <T> Parser<T> alternative(Parser<? super T> left,
                                          Parser<? super T> right) {
    return new Alternative<T>(left, right);
  }

  /**
   * Creates an <code>Intersection</code> parser from the <code>left</code> and
   * <code>right</code> sub-parsers.
   *
   * <code>left & right</code>
   */
  public static <T> Parser<T> intersection(Parser<? super T> left,
                                           Parser<? super T> right) {
    return new Intersection<T>(left, right);
  }

  /**
   * Creates a <code>Difference</code> parser from the <code>left</code> and
   * <code>right</code> sub-parsers.
   *
   * <code>left - right</code>
   */
  public static <T> Parser<T> difference(Parser<? super T> left,
                                         Parser<? super T> right) {
    return new Difference<T>(left, right);
  }

  /**
   * Creates a <code>Sequence</code> parser from the <code>left</code> and
   * <code>right</code> sub-parsers.
   *
   * <code>left right</code>
   */
  public static <T> Parser<T> sequence(Parser<? super T> left,
                                       Parser<? super T> right) {
    return new Sequence<T>(left, right);
  }

  /**
   * Creates a <code>Sequence</code> parser from parsers <code>one</code>,
   * <code>two</code> and <code>three</code> sub-parsers. Equivalent to calling
   * Parser.sequence(one, Parser.sequence(two, three)).
   */
  public static <T> Parser<T> sequence(Parser<? super T> one,
                                       Parser<? super T> two,
                                       Parser<? super T> three) {
    return Parser.<T>sequence(one, Parser.<T>sequence(two, three));
  }

  /**
   * Creates a sequence of four parsers.
   * @see #sequence(Parser,Parser,Parser)
   */
  public static <T> Parser<T> sequence(Parser<? super T> one,
                                       Parser<? super T> two,
                                       Parser<? super T> three,
                                       Parser<? super T> four) {
    return Parser.<T>sequence(
        one, Parser.<T>sequence(
            two, Parser.<T>sequence(three, four)));
  }

  /**
   * Creates a sequence of five parsers.
   * @see #sequence(Parser,Parser,Parser)
   */
  public static <T> Parser<T> sequence(Parser<? super T> one,
                                       Parser<? super T> two,
                                       Parser<? super T> three,
                                       Parser<? super T> four,
                                       Parser<? super T> five) {
    return Parser.<T>sequence(
        one, Parser.<T>sequence(
            two, Parser.<T>sequence(
                three, Parser.<T>sequence(four, five))));
  }
}
