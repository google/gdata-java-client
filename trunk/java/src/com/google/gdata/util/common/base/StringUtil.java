/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.gdata.util.common.base;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Some common string manipulation utilities.
 */
public class StringUtil {

  public static final String EMPTY_STRING = "";

  // \u3000 is the double-byte space character in UTF-8
  // \u00A0 is the non-breaking space character (&nbsp;)
  // \u2007 is the figure space character (&#8199;)
  // \u202F is the narrow non-breaking space character (&#8239;)
  public static final String WHITE_SPACES = " \r\n\t\u3000\u00A0\u2007\u202F";

  public static final String LINE_BREAKS = "\r\n";

  private static final Pattern htmlTagPattern =
    Pattern.compile("</?[a-zA-Z][^>]*>");

  private static final Pattern characterReferencePattern =
    Pattern.compile("&#?[a-zA-Z0-9]{1,8};");

  private static final Pattern dbSpecPattern =
    Pattern.compile("(.*)\\{(\\d+),(\\d+)\\}(.*)");

  // This class should not be instantiated, hence the private constructor
  private StringUtil() {}

  /** Split "str" by run of delimiters and return. */
  public static String[] split(String str, String delims) {
    return split(str, delims, false);
  }

  /**
   * Split "str" into tokens by delimiters and optionally remove white spaces
   * from the splitted tokens.
   *
   * @param trimTokens if true, then trim the tokens
   */
  public static String[] split(String str, String delims, boolean trimTokens) {
    StringTokenizer tokenizer = new StringTokenizer(str, delims);
    int n = tokenizer.countTokens();
    String[] list = new String[n];
    for (int i = 0; i < n; i++) {
      if (trimTokens) {
        list[i] = tokenizer.nextToken().trim();
      } else {
        list[i] = tokenizer.nextToken();
      }
    }
    return list;
  }

  /**
   * Short hand for <code>split(str, delims, true)</code>
   */
  public static String[] splitAndTrim(String str, String delims) {
    return split(str, delims, true);
  }

  /** Parse comma-separated list of ints and return as array. */
  public static int[] splitInts(String str) throws IllegalArgumentException {
    StringTokenizer tokenizer = new StringTokenizer(str, ",");
    int n = tokenizer.countTokens();
    int[] list = new int[n];
    for (int i = 0; i < n; i++) {
      String token = tokenizer.nextToken();
      list[i] = Integer.parseInt(token);
    }
    return list;
  }

  /** Parse comma-separated list of longs and return as array. */
  public static long[] splitLongs(String str) throws IllegalArgumentException {
    StringTokenizer tokenizer = new StringTokenizer(str, ",");
    int n = tokenizer.countTokens();
    long[] list = new long[n];
    for (int i = 0; i < n; i++) {
      String token = tokenizer.nextToken();
      list[i] = Long.parseLong(token);
    }
    return list;
  }

  /**
   * Concatenates the given int[] array into one String, inserting a delimiter
   * between each pair of elements.
   */
  public static String joinInts(int[] tokens, String delimiter) {
    if (tokens == null) return "";
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < tokens.length; i++) {
      if (i > 0 && delimiter != null) {
        result.append(delimiter);
      }
      result.append(String.valueOf(tokens[i]));
    }
    return result.toString();
  }

  /**
   * Concatenates the given long[] array into one String, inserting a delimiter
   * between each pair of elements.
   */
  public static String joinLongs(long[] tokens, String delimiter) {
    if (tokens == null) return "";
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < tokens.length; i++) {
      if (i > 0 && delimiter != null) {
        result.append(delimiter);
      }
      result.append(String.valueOf(tokens[i]));
    }
    return result.toString();
  }

  /**
   * Concatenates the String representations of the elements of a
   * String[] array into one String, and inserts a delimiter between
   * each pair of elements.
   * <p>
   * This includes the String[] case, because if s is a String, then
   * s.toString() returns s.
   *
   * @deprecated Please use
   *     But note that {@code Join} does not consider null elements to be
   *     equivalent to the empty string, as this method does.
   */
  @Deprecated public static String join(Object[] tokens, String delimiter) {
    if (tokens == null || tokens.length == 0) return "";
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < tokens.length; i++) {
      if (i > 0 && delimiter != null)
        result.append(delimiter);

      if (tokens[i] != null)
        result.append(tokens[i].toString());
      }
    return result.toString();
  }

  /**
   * Same as {@link #join(Object[],String)}, but takes a {@link Collection}
   * instead.
   *
   * @deprecated Please use
   *     But note that {@code Join} does not consider null elements to be
   *     equivalent to the empty string, as this method does.
   */
  @Deprecated public static String join(Collection tokens, String delimiter) {
    return join(tokens.toArray(), delimiter);
  }

  /** This replaces the occurances of 'what' in 'str' with 'with'
   * @param str - the string o process
   * @param what - to replace
   * @param with - replace with this
   * @return String str whete 'what' was repalced with 'with'
   *
   * @deprecated Please use {@link String#replace(CharSequence, CharSequence)}.
   */
  @Deprecated
  public static String replace(String str, String what, String with) {
    // Have to check this argument, for compatibility with the old impl.
    // For the record, String.replace() is capable of handling an empty target
    // string... but it does something kind of weird in that case.
    assert(what.length() > 0);
    return str.replace(what, with);
  }

  /**
   * Reformats the given string to a fixed width by inserting
   * carriage returns and trimming unnecessary whitespace.
   *
   * @param str the string to format
   * @param width the fixed width (in characters)
   */
  public static String fixedWidth(String str, int width) {
    String[] lines = split(str, "\n");
    return fixedWidth(lines, width);
  }

  /**
   * Reformats the given array of lines to a fixed width by inserting
   * carriage returns and trimming unnecessary whitespace.
   *
   * @param lines - array of lines to format
   * @param width - the fixed width (in characters)
   */
  public static String fixedWidth(String[] lines, int width) {
    StringBuilder formatStr = new StringBuilder();

    for (int i = 0; i < lines.length; i++) {
      int curWidth = 0;
      if (i != 0) {
        formatStr.append("\n");
      }
      // a small optimization
      if (lines[i].length() <= width) {
        formatStr.append(lines[i]);
        continue;
      }
      String[] words = splitAndTrim(lines[i], WHITE_SPACES);
      for (int j = 0; j < words.length; j++) {
        if (curWidth == 0 || (curWidth + words[j].length()) < width) {
          // add a space if we're not at the beginning of a line
          if (curWidth != 0) {
            formatStr.append(" ");
            curWidth += 1;
          }
          curWidth += words[j].length();
          formatStr.append(words[j]);
        } else {
          formatStr.append("\n");
          curWidth = words[j].length();
          formatStr.append(words[j]);
        }
      }
    }

    return formatStr.toString();
  }

   /**
   * Inserts spaces every splitLen characters so that the string will wrap.
   *
   * @param    lineLen    the length of the substrings to separate with spaces.
   * @param    original   the original String
   *
   * @return   original String with spaces inserted every lineLen characters.
   *
   */
  public static String insertBreakingWhitespace(int lineLen, String original) {
    if (original == null || lineLen <= 0) throw new IllegalArgumentException();

    int length = original.length();
     if (length <= lineLen)
     // we can avoid the overhead of instantiating a StringBuilder
       return original;
    int currPos = 0;
    StringBuilder retval = new StringBuilder();
    while (length - currPos > lineLen) {
      retval.append(original.substring(currPos, currPos + lineLen));
      currPos += lineLen;
      retval.append(" ");
    }
    retval.append(original.substring(currPos, length));
    return retval.toString();
  }

  /**
   * Indents the given String per line.
   * @param iString The string to indent.
   * @param iIndentDepth The depth of the indentation.
   * @return The indented string.
   */
  public static String indent(String iString, int iIndentDepth) {
    StringBuilder spacer = new StringBuilder();
    spacer.append("\n");
    for (int i = 0; i < iIndentDepth; i ++) {
      spacer.append("  ");
    }
    return replace(iString, "\n", spacer.toString());
  }

  /**
   * This is a both way strip
   *
   * @param str the string to strip
   * @param left strip from left
   * @param right strip from right
   * @param what character(s) to strip
   * @return the stripped string
   * @deprecated ensure the string is not null and use 
   *	<ul>
   *		<li> {@code CharMatcher.anyOf(what).trimFrom(str)}
   *	if {@code left == true} and {@code right == true}
   *		<li> {@code CharMatcher.anyOf(what).trimLeadingFrom(str)}
   *	if {@code left == true} and {@code right == false}
   *		<li> {@code CharMatcher.anyOf(what).trimTrailingFrom(str)}
   *	if {@code left == false} and {@code right == true}
   *	</ul>
   */
  @Deprecated
  public static String megastrip(String str,
                                 boolean left, boolean right,
                                 String what) {
    if (str == null) {
      return null;
    }

    int limitLeft = 0;
    int limitRight = str.length() - 1;

    while (left && limitLeft <= limitRight &&
           what.indexOf(str.charAt(limitLeft)) >= 0) {
      limitLeft ++;
    }
    while (right && limitRight>=limitLeft &&
           what.indexOf(str.charAt(limitRight)) >= 0) {
      limitRight --;
     }
    return str.substring(limitLeft, limitRight + 1);
  }

  /** lstrip - strips spaces from left
   * @param str what to strip
   * @return String the striped string
   * @deprecated ensure the string is not null and use {@code
   *     CharMatcher.LEGACY_WHITESPACE.trimLeadingFrom(str)}; also consider whether you
   *     really want the legacy whitespace definition, or something more
   *     standard like {@link CharMatcher#WHITESPACE}.
   */
  @Deprecated public static String lstrip(String str) {
      return (str == null) ? null : CharMatcher.LEGACY_WHITESPACE.trimLeadingFrom(str);
  }

  /** rstrip - strips spaces from right
   * @param str what to strip
   * @return String the striped string
   * @deprecated ensure the string is not null and use {@code
   *     CharMatcher.LEGACY_WHITESPACE.trimTrailingFrom(str)}; also consider whether you
   *     really want the legacy whitespace definition, or something more
   *     standard like {@link CharMatcher#WHITESPACE}.
   */
  @Deprecated public static String rstrip(String str) {
      return (str == null) ? null : CharMatcher.LEGACY_WHITESPACE.trimTrailingFrom(str);
  }

  /** strip - strips both ways
   * @param str what to strip
   * @return String the striped string
   */
  public static String strip(String str) {
    return megastrip(str, true, true, WHITE_SPACES);
  }

  /** Strip white spaces from both end, and collapse white spaces
   * in the middle.
   * @param str what to strip
   * @return String the striped and collapsed string
   */
  public static String stripAndCollapse(String str) {
    return collapseWhitespace(strip(str));
  }

  /**
   * Give me a string and a potential prefix, and I return the string
   * following the prefix if the prefix matches, else null.
   * Analogous to the c++ functions strprefix and var_strprefix.
   */
  public static String stripPrefix(String str, String prefix) {
    return str.startsWith(prefix) ? str.substring(prefix.length()) : null;
  }

  /**
   * Case insensitive version of stripPrefix.
   * Analogous to the c++ functions strcaseprefix and var_strcaseprefix.
   */
  public static String stripPrefixIgnoreCase(String str, String prefix) {
    if (str.length() >= prefix.length()
        && str.substring(0, prefix.length()).equalsIgnoreCase(prefix)) {
      return str.substring(prefix.length());
    }

    return null;
  }

  /**
   * Strips all non-digit characters from a string.
   *
   * The resulting string will only contain characters for which isDigit()
   * returns true.
   *
   * @param str the string to strip
   * @return a string consisting of digits only, or an empty string
   */
  public static String stripNonDigits(String str) {
    StringBuffer result = new StringBuffer(str.length());
    for (char candidate : str.toCharArray()) {
      if (Character.isDigit(candidate)) {
        result.append(candidate);
      }
    }
    return result.toString();
  }

  /**
   * Counts the number of (not necessarily distinct) characters in the
   * string that also happen to be in 'chars'
   */
  public static int numSharedChars(final String str, final String chars) {

    if (str == null || chars == null) {
      return 0;
    }

    int total = 0, pos = -1;
    while ((pos = indexOfChars(str, chars, pos + 1)) != -1) {
      total++;
    }

    return total;
  }

  /**
   * Like String.indexOf() except that it will look for any of the
   * characters in 'chars' (similar to C's strpbrk)
   */
  public static int indexOfChars(String str, String chars, int fromIndex) {
    final int len = str.length();

    for (int pos = fromIndex; pos < len; pos++) {
      if (chars.indexOf(str.charAt(pos)) >= 0) {
        return pos;
      }
    }

    return -1;
  }

  /**
   * Like String.indexOf() except that it will look for any of the
   * characters in 'chars' (similar to C's strpbrk)
   */
  public static int indexOfChars(String str, String chars) {
    return indexOfChars(str, chars, 0);
  }

  /**
   * Finds the last index in str of a character not in the characters
   * in 'chars' (similar to ANSI string.find_last_not_of).
   *
   * Returns -1 if no such character can be found.
   */
  public static int lastIndexNotOf(String str, String chars, int fromIndex) {
    fromIndex = Math.min(fromIndex, str.length() - 1);

    for (int pos = fromIndex; pos >= 0; pos--) {
      if (chars.indexOf(str.charAt(pos)) < 0) {
        return pos;
      }
    }

    return -1;
  }

  /**
   * Like String.replace() except that it accepts any number of old chars.
   * Replaces any occurrances of 'oldchars' in 'str' with 'newchar'.
   * Example: replaceChars("Hello, world!", "H,!", ' ') returns " ello  world "
   */
  public static String replaceChars(String str, String oldchars, char newchar) {
    int pos = indexOfChars(str, oldchars);
    if (pos == -1) {
      return str;
    }

    StringBuilder buf = new StringBuilder(str);
    do {
      buf.setCharAt(pos, newchar);
      pos = indexOfChars(str, oldchars, pos + 1);
    } while (pos != -1);

    return buf.toString();
  }

  /**
   * Remove any occurrances of 'oldchars' in 'str'.
   * Example: removeChars("Hello, world!", ",!") returns "Hello world"
   */
  public static String removeChars(String str, String oldchars) {
    int pos = indexOfChars(str, oldchars);
    if (pos == -1) {
      return str;
    }

    StringBuilder buf = new StringBuilder();
    int start = 0;
    do {
      buf.append(str.substring(start, pos));
      start = pos + 1;
      pos = indexOfChars(str, oldchars, start);
    } while (pos != -1);

    if (start < str.length()) {
      buf.append(str.substring(start));
    }
    return buf.toString();
  }

  /**
   * Removes all characters from 'str' that are not in 'retainChars'.
   * Example: retainAllChars("Hello, world!", "lo") returns "llool"
   */
  public static String retainAllChars(String str, String retainChars) {
    int pos = indexOfChars(str, retainChars);
    if (pos == -1) {
      return "";
    }
    StringBuilder buf = new StringBuilder();
    do {
      buf.append(str.charAt(pos));
      pos = indexOfChars(str, retainChars, pos + 1);
    } while (pos != -1);
    return buf.toString();
  }

  /**
   * Replaces microsoft "smart quotes" (curly " and ') with their
   * ascii counterparts.
   */
  public static String replaceSmartQuotes(String str) {
    // See http://www.microsoft.com/typography/unicode/1252.htm
    str = replaceChars(str, "\u0091\u0092\u2018\u2019", '\'');
    str = replaceChars(str, "\u0093\u0094\u201c\u201d", '"');
    return str;
  }

  /**
   * Convert a string of hex digits to a byte array, with the first
   * byte in the array being the MSB. The string passed in should be
   * just the raw digits (upper or lower case), with no leading
   * or trailing characters (like '0x' or 'h').
   * An odd number of characters is supported.
   * If the string is empty, an empty array will be returned.
   *
   * This is significantly faster than using
   *   new BigInteger(str, 16).toByteArray();
   * especially with larger strings. Here are the results of some
   * microbenchmarks done on a P4 2.8GHz 2GB RAM running
   * linux 2.4.22-gg11 and JDK 1.5 with an optimized build:
   *
   * String length        hexToBytes (usec)   BigInteger
   * -----------------------------------------------------
   * 16                       0.570                 1.43
   * 256                      8.21                 44.4
   * 1024                    32.8                 526
   * 16384                  546                121000
   */
  public static byte[] hexToBytes(String str) {
    byte[] bytes = new byte[(str.length() + 1) / 2];
    if (str.length() == 0) {
      return bytes;
    }
    bytes[0] = 0;
    int nibbleIdx = (str.length() % 2);
    for (int i = 0; i < str.length(); i++) {
      char c = str.charAt(i);
      if (!isHex(c)) {
        throw new IllegalArgumentException("string contains non-hex chars");
      }
      if ((nibbleIdx % 2) == 0) {
        bytes[nibbleIdx >> 1] = (byte) (hexValue(c) << 4);
      } else {
        bytes[nibbleIdx >> 1] += (byte) hexValue(c);
      }
      nibbleIdx++;
    }
    return bytes;
  }

  /**
   * Converts any instances of "\r" or "\r\n" style EOLs into "\n" (Line Feed).
   */
  public static String convertEOLToLF(String input) {
    StringBuilder res = new StringBuilder(input.length());
    char[] s = input.toCharArray();
    int from = 0;
    final int end = s.length;
    for (int i = 0; i < end; i++) {
      if (s[i] == '\r') {
        res.append(s, from, i - from);
        res.append('\n');
        if (i + 1 < end && s[i + 1] == '\n') {
          i++;
        }

        from = i + 1;
      }
    }

    if (from == 0) {   // no \r!
      return input;
    }

    res.append(s, from, end - from);
    return res.toString();
  }

  /** @deprecated Please inline this method. */
  @Deprecated public static String convertEOLToCRLF(String input) {
    return input.replaceAll("(\r\n|\r|\n)", "\r\n");
  }

  /**
   * Returns a string consisting of "s", plus enough copies of "pad_ch" on the
   * left hand side to make the length of "s" equal to or greater than len (if
   * "s" is already longer than "len", then "s" is returned).
   */
  public static String padLeft(String s, int len, char pad_ch) {
    if (s.length() >= len) {
      return s;
    } else {
      StringBuilder sb = new StringBuilder();
      int n = len - s.length();
      for (int i = 0; i < n; i++) {
        sb.append(pad_ch);
      }
      sb.append(s);
      return sb.toString();
    }
  }

  /**
   * Returns a string consisting of "s", plus enough copies of "pad_ch" on the
   * right hand side to make the length of "s" equal to or greater than len (if
   * "s" is already longer than "len", then "s" is returned).
   */
  public static String padRight(String s, int len, char pad_ch) {
    if (s.length() >= len) {
      return s;
    } else {
      StringBuilder sb = new StringBuilder();
      int n = len - s.length();
      sb.append(s);
      for (int i = 0; i < n; i++) {
        sb.append(pad_ch);
      }
      return sb.toString();
    }
  }

  /**
   * Returns a string consisting of "s", with each of the first "len" characters
   * replaced by "mask_ch" character.
   */
  public static String maskLeft(String s, int len, char mask_ch) {
    if (len <= 0) {
      return s;
    }
    len = Math.min(len, s.length());
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < len; i++) {
      sb.append(mask_ch);
    }
    sb.append(s.substring(len));
    return sb.toString();
  }

  /**
   * Returns a string consisting of "s", with each of the last "len" characters
   * replaces by "mask_ch" character.
   */
  public static String maskRight(String s, int len, char mask_ch) {
    if (len <= 0) {
      return s;
    }
    len = Math.min(len, s.length());
    StringBuilder sb = new StringBuilder();
    sb.append(s.substring(0, s.length() - len));
    for (int i = 0; i < len; i++) {
      sb.append(mask_ch);
    }
    return sb.toString();
  }

  private static boolean isOctal(char c) {
    return (c >= '0') && (c <= '7');
  }

  private static boolean isHex(char c) {
    return ((c >= '0') && (c <= '9')) ||
           ((c >= 'a') && (c <= 'f')) ||
           ((c >= 'A') && (c <= 'F'));
  }

  private static int hexValue(char c) {
    if ((c >= '0') && (c <= '9')) {
      return (c - '0');
    } else if ((c >= 'a') && (c <= 'f')) {
      return (c - 'a') + 10;
    } else {
      return (c - 'A') + 10;
    }
  }

  /**
   * Unescape any C escape sequences (\n, \r, \\, \ooo, etc) and return the
   * resulting string.
   */
  public static String unescapeCString(String s) {
    if (s.indexOf('\\') < 0) {
      // Fast path: nothing to unescape
      return s;
    }

    StringBuilder sb = new StringBuilder();
    int len = s.length();
    for (int i = 0; i < len;) {
      char c = s.charAt(i++);
      if (c == '\\' && (i < len)) {
        c = s.charAt(i++);
        switch (c) {
          case 'a':  c = '\007';  break;
          case 'b':  c = '\b';    break;
          case 'f':  c = '\f';    break;
          case 'n':  c = '\n';    break;
          case 'r':  c = '\r';    break;
          case 't':  c = '\t';    break;
          case 'v':  c = '\013';  break;
          case '\\': c = '\\';    break;
          case '?':  c = '?';     break;
          case '\'': c = '\'';    break;
          case '"':  c = '\"';    break;

          default: {
            if ((c == 'x') && (i < len) && isHex(s.charAt(i))) {
              // "\xXX"
              int v = hexValue(s.charAt(i++));
              if ((i < len) && isHex(s.charAt(i))) {
                v = v*16 + hexValue(s.charAt(i++));
              }
              c = (char)v;
            } else if (isOctal(c)) {
              // "\OOO"
              int v = (c - '0');
              if ((i < len) && isOctal(s.charAt(i))) {
                v = v*8 + (s.charAt(i++) - '0');
              }
              if ((i < len) && isOctal(s.charAt(i))) {
                v = v*8 + (s.charAt(i++) - '0');
              }
              c = (char)v;
            } else {
              // Propagate unknown escape sequences.
              sb.append('\\');
            }
            break;
          }
        }
      }
      sb.append(c);
    }
    return sb.toString();
  }

  /**
   * Unescape any MySQL escape sequences.
   * See MySQL language reference Chapter 6 at
   * <a href="http://www.mysql.com/doc/">http://www.mysql.com/doc/</a>.
   * This function will <strong>not</strong> work for other SQL-like
   * dialects.
   * @param s string to unescape, with the surrounding quotes.
   * @return unescaped string, without the surrounding quotes.
   * @exception IllegalArgumentException if s is not a valid MySQL string.
   */
  public static String unescapeMySQLString(String s)
      throws IllegalArgumentException {
    // note: the same buffer is used for both reading and writing
    // it works because the writer can never outrun the reader
    char chars[] = s.toCharArray();

    // the string must be quoted 'like this' or "like this"
    if (chars.length < 2 || chars[0] != chars[chars.length-1] ||
        (chars[0] != '\'' && chars[0] != '"')) {
      throw new IllegalArgumentException("not a valid MySQL string: " + s);
    }

    // parse the string and decode the backslash sequences; in addition,
    // quotes can be escaped 'like this: ''', "like this: """, or 'like this: "'
    int j = 1;  // write position in the string (never exceeds read position)
    int f = 0;  // state: 0 (normal), 1 (backslash), 2 (quote)
    for (int i = 1; i < chars.length - 1; i++) {
      if (f == 0) {             // previous character was normal
	if (chars[i] == '\\') {
	  f = 1;  // backslash
	} else if (chars[i] == chars[0]) {
	  f = 2;  // quoting character
	} else {
	  chars[j++] = chars[i];
	}
      } else if (f == 1) {      // previous character was a backslash
	switch (chars[i]) {
	  case '0':   chars[j++] = '\0';   break;
	  case '\'':  chars[j++] = '\'';   break;
	  case '"':   chars[j++] = '"';    break;
	  case 'b':   chars[j++] = '\b';   break;
	  case 'n':   chars[j++] = '\n';   break;
	  case 'r':   chars[j++] = '\r';   break;
	  case 't':   chars[j++] = '\t';   break;
	  case 'z':   chars[j++] = '\032'; break;
	  case '\\':  chars[j++] = '\\';   break;
	  default:
	    // if the character is not special, backslash disappears
	    chars[j++] = chars[i];
	    break;
	}
	f = 0;
      } else {                  // previous character was a quote
	// quoting characters must be doubled inside a string
	if (chars[i] != chars[0]) {
	  throw new IllegalArgumentException("not a valid MySQL string: " + s);
	}
	chars[j++] = chars[0];
	f = 0;
      }
    }
    // string contents cannot end with a special character
    if (f != 0) {
      throw new IllegalArgumentException("not a valid MySQL string: " + s);
    }

    // done
    return new String(chars, 1, j - 1);
  }

  static Map<String, Character> escapeStrings;

  static {
    // HTML character entity references as defined in HTML 4
    // see http://www.w3.org/TR/REC-html40/sgml/entities.html
    escapeStrings = new HashMap<String, Character>(252);

    escapeStrings.put("&nbsp;", new Character('\u00A0'));
    escapeStrings.put("&iexcl;", new Character('\u00A1'));
    escapeStrings.put("&cent;", new Character('\u00A2'));
    escapeStrings.put("&pound;", new Character('\u00A3'));
    escapeStrings.put("&curren;", new Character('\u00A4'));
    escapeStrings.put("&yen;", new Character('\u00A5'));
    escapeStrings.put("&brvbar;", new Character('\u00A6'));
    escapeStrings.put("&sect;", new Character('\u00A7'));
    escapeStrings.put("&uml;", new Character('\u00A8'));
    escapeStrings.put("&copy;", new Character('\u00A9'));
    escapeStrings.put("&ordf;", new Character('\u00AA'));
    escapeStrings.put("&laquo;", new Character('\u00AB'));
    escapeStrings.put("&not;", new Character('\u00AC'));
    escapeStrings.put("&shy;", new Character('\u00AD'));
    escapeStrings.put("&reg;", new Character('\u00AE'));
    escapeStrings.put("&macr;", new Character('\u00AF'));
    escapeStrings.put("&deg;", new Character('\u00B0'));
    escapeStrings.put("&plusmn;", new Character('\u00B1'));
    escapeStrings.put("&sup2;", new Character('\u00B2'));
    escapeStrings.put("&sup3;", new Character('\u00B3'));
    escapeStrings.put("&acute;", new Character('\u00B4'));
    escapeStrings.put("&micro;", new Character('\u00B5'));
    escapeStrings.put("&para;", new Character('\u00B6'));
    escapeStrings.put("&middot;", new Character('\u00B7'));
    escapeStrings.put("&cedil;", new Character('\u00B8'));
    escapeStrings.put("&sup1;", new Character('\u00B9'));
    escapeStrings.put("&ordm;", new Character('\u00BA'));
    escapeStrings.put("&raquo;", new Character('\u00BB'));
    escapeStrings.put("&frac14;", new Character('\u00BC'));
    escapeStrings.put("&frac12;", new Character('\u00BD'));
    escapeStrings.put("&frac34;", new Character('\u00BE'));
    escapeStrings.put("&iquest;", new Character('\u00BF'));
    escapeStrings.put("&Agrave;", new Character('\u00C0'));
    escapeStrings.put("&Aacute;", new Character('\u00C1'));
    escapeStrings.put("&Acirc;", new Character('\u00C2'));
    escapeStrings.put("&Atilde;", new Character('\u00C3'));
    escapeStrings.put("&Auml;", new Character('\u00C4'));
    escapeStrings.put("&Aring;", new Character('\u00C5'));
    escapeStrings.put("&AElig;", new Character('\u00C6'));
    escapeStrings.put("&Ccedil;", new Character('\u00C7'));
    escapeStrings.put("&Egrave;", new Character('\u00C8'));
    escapeStrings.put("&Eacute;", new Character('\u00C9'));
    escapeStrings.put("&Ecirc;", new Character('\u00CA'));
    escapeStrings.put("&Euml;", new Character('\u00CB'));
    escapeStrings.put("&Igrave;", new Character('\u00CC'));
    escapeStrings.put("&Iacute;", new Character('\u00CD'));
    escapeStrings.put("&Icirc;", new Character('\u00CE'));
    escapeStrings.put("&Iuml;", new Character('\u00CF'));
    escapeStrings.put("&ETH;", new Character('\u00D0'));
    escapeStrings.put("&Ntilde;", new Character('\u00D1'));
    escapeStrings.put("&Ograve;", new Character('\u00D2'));
    escapeStrings.put("&Oacute;", new Character('\u00D3'));
    escapeStrings.put("&Ocirc;", new Character('\u00D4'));
    escapeStrings.put("&Otilde;", new Character('\u00D5'));
    escapeStrings.put("&Ouml;", new Character('\u00D6'));
    escapeStrings.put("&times;", new Character('\u00D7'));
    escapeStrings.put("&Oslash;", new Character('\u00D8'));
    escapeStrings.put("&Ugrave;", new Character('\u00D9'));
    escapeStrings.put("&Uacute;", new Character('\u00DA'));
    escapeStrings.put("&Ucirc;", new Character('\u00DB'));
    escapeStrings.put("&Uuml;", new Character('\u00DC'));
    escapeStrings.put("&Yacute;", new Character('\u00DD'));
    escapeStrings.put("&THORN;", new Character('\u00DE'));
    escapeStrings.put("&szlig;", new Character('\u00DF'));
    escapeStrings.put("&agrave;", new Character('\u00E0'));
    escapeStrings.put("&aacute;", new Character('\u00E1'));
    escapeStrings.put("&acirc;", new Character('\u00E2'));
    escapeStrings.put("&atilde;", new Character('\u00E3'));
    escapeStrings.put("&auml;", new Character('\u00E4'));
    escapeStrings.put("&aring;", new Character('\u00E5'));
    escapeStrings.put("&aelig;", new Character('\u00E6'));
    escapeStrings.put("&ccedil;", new Character('\u00E7'));
    escapeStrings.put("&egrave;", new Character('\u00E8'));
    escapeStrings.put("&eacute;", new Character('\u00E9'));
    escapeStrings.put("&ecirc;", new Character('\u00EA'));
    escapeStrings.put("&euml;", new Character('\u00EB'));
    escapeStrings.put("&igrave;", new Character('\u00EC'));
    escapeStrings.put("&iacute;", new Character('\u00ED'));
    escapeStrings.put("&icirc;", new Character('\u00EE'));
    escapeStrings.put("&iuml;", new Character('\u00EF'));
    escapeStrings.put("&eth;", new Character('\u00F0'));
    escapeStrings.put("&ntilde;", new Character('\u00F1'));
    escapeStrings.put("&ograve;", new Character('\u00F2'));
    escapeStrings.put("&oacute;", new Character('\u00F3'));
    escapeStrings.put("&ocirc;", new Character('\u00F4'));
    escapeStrings.put("&otilde;", new Character('\u00F5'));
    escapeStrings.put("&ouml;", new Character('\u00F6'));
    escapeStrings.put("&divide;", new Character('\u00F7'));
    escapeStrings.put("&oslash;", new Character('\u00F8'));
    escapeStrings.put("&ugrave;", new Character('\u00F9'));
    escapeStrings.put("&uacute;", new Character('\u00FA'));
    escapeStrings.put("&ucirc;", new Character('\u00FB'));
    escapeStrings.put("&uuml;", new Character('\u00FC'));
    escapeStrings.put("&yacute;", new Character('\u00FD'));
    escapeStrings.put("&thorn;", new Character('\u00FE'));
    escapeStrings.put("&yuml;", new Character('\u00FF'));
    escapeStrings.put("&fnof;", new Character('\u0192'));
    escapeStrings.put("&Alpha;", new Character('\u0391'));
    escapeStrings.put("&Beta;", new Character('\u0392'));
    escapeStrings.put("&Gamma;", new Character('\u0393'));
    escapeStrings.put("&Delta;", new Character('\u0394'));
    escapeStrings.put("&Epsilon;", new Character('\u0395'));
    escapeStrings.put("&Zeta;", new Character('\u0396'));
    escapeStrings.put("&Eta;", new Character('\u0397'));
    escapeStrings.put("&Theta;", new Character('\u0398'));
    escapeStrings.put("&Iota;", new Character('\u0399'));
    escapeStrings.put("&Kappa;", new Character('\u039A'));
    escapeStrings.put("&Lambda;", new Character('\u039B'));
    escapeStrings.put("&Mu;", new Character('\u039C'));
    escapeStrings.put("&Nu;", new Character('\u039D'));
    escapeStrings.put("&Xi;", new Character('\u039E'));
    escapeStrings.put("&Omicron;", new Character('\u039F'));
    escapeStrings.put("&Pi;", new Character('\u03A0'));
    escapeStrings.put("&Rho;", new Character('\u03A1'));
    escapeStrings.put("&Sigma;", new Character('\u03A3'));
    escapeStrings.put("&Tau;", new Character('\u03A4'));
    escapeStrings.put("&Upsilon;", new Character('\u03A5'));
    escapeStrings.put("&Phi;", new Character('\u03A6'));
    escapeStrings.put("&Chi;", new Character('\u03A7'));
    escapeStrings.put("&Psi;", new Character('\u03A8'));
    escapeStrings.put("&Omega;", new Character('\u03A9'));
    escapeStrings.put("&alpha;", new Character('\u03B1'));
    escapeStrings.put("&beta;", new Character('\u03B2'));
    escapeStrings.put("&gamma;", new Character('\u03B3'));
    escapeStrings.put("&delta;", new Character('\u03B4'));
    escapeStrings.put("&epsilon;", new Character('\u03B5'));
    escapeStrings.put("&zeta;", new Character('\u03B6'));
    escapeStrings.put("&eta;", new Character('\u03B7'));
    escapeStrings.put("&theta;", new Character('\u03B8'));
    escapeStrings.put("&iota;", new Character('\u03B9'));
    escapeStrings.put("&kappa;", new Character('\u03BA'));
    escapeStrings.put("&lambda;", new Character('\u03BB'));
    escapeStrings.put("&mu;", new Character('\u03BC'));
    escapeStrings.put("&nu;", new Character('\u03BD'));
    escapeStrings.put("&xi;", new Character('\u03BE'));
    escapeStrings.put("&omicron;", new Character('\u03BF'));
    escapeStrings.put("&pi;", new Character('\u03C0'));
    escapeStrings.put("&rho;", new Character('\u03C1'));
    escapeStrings.put("&sigmaf;", new Character('\u03C2'));
    escapeStrings.put("&sigma;", new Character('\u03C3'));
    escapeStrings.put("&tau;", new Character('\u03C4'));
    escapeStrings.put("&upsilon;", new Character('\u03C5'));
    escapeStrings.put("&phi;", new Character('\u03C6'));
    escapeStrings.put("&chi;", new Character('\u03C7'));
    escapeStrings.put("&psi;", new Character('\u03C8'));
    escapeStrings.put("&omega;", new Character('\u03C9'));
    escapeStrings.put("&thetasym;", new Character('\u03D1'));
    escapeStrings.put("&upsih;", new Character('\u03D2'));
    escapeStrings.put("&piv;", new Character('\u03D6'));
    escapeStrings.put("&bull;", new Character('\u2022'));
    escapeStrings.put("&hellip;", new Character('\u2026'));
    escapeStrings.put("&prime;", new Character('\u2032'));
    escapeStrings.put("&Prime;", new Character('\u2033'));
    escapeStrings.put("&oline;", new Character('\u203E'));
    escapeStrings.put("&frasl;", new Character('\u2044'));
    escapeStrings.put("&weierp;", new Character('\u2118'));
    escapeStrings.put("&image;", new Character('\u2111'));
    escapeStrings.put("&real;", new Character('\u211C'));
    escapeStrings.put("&trade;", new Character('\u2122'));
    escapeStrings.put("&alefsym;", new Character('\u2135'));
    escapeStrings.put("&larr;", new Character('\u2190'));
    escapeStrings.put("&uarr;", new Character('\u2191'));
    escapeStrings.put("&rarr;", new Character('\u2192'));
    escapeStrings.put("&darr;", new Character('\u2193'));
    escapeStrings.put("&harr;", new Character('\u2194'));
    escapeStrings.put("&crarr;", new Character('\u21B5'));
    escapeStrings.put("&lArr;", new Character('\u21D0'));
    escapeStrings.put("&uArr;", new Character('\u21D1'));
    escapeStrings.put("&rArr;", new Character('\u21D2'));
    escapeStrings.put("&dArr;", new Character('\u21D3'));
    escapeStrings.put("&hArr;", new Character('\u21D4'));
    escapeStrings.put("&forall;", new Character('\u2200'));
    escapeStrings.put("&part;", new Character('\u2202'));
    escapeStrings.put("&exist;", new Character('\u2203'));
    escapeStrings.put("&empty;", new Character('\u2205'));
    escapeStrings.put("&nabla;", new Character('\u2207'));
    escapeStrings.put("&isin;", new Character('\u2208'));
    escapeStrings.put("&notin;", new Character('\u2209'));
    escapeStrings.put("&ni;", new Character('\u220B'));
    escapeStrings.put("&prod;", new Character('\u220F'));
    escapeStrings.put("&sum;", new Character('\u2211'));
    escapeStrings.put("&minus;", new Character('\u2212'));
    escapeStrings.put("&lowast;", new Character('\u2217'));
    escapeStrings.put("&radic;", new Character('\u221A'));
    escapeStrings.put("&prop;", new Character('\u221D'));
    escapeStrings.put("&infin;", new Character('\u221E'));
    escapeStrings.put("&ang;", new Character('\u2220'));
    escapeStrings.put("&and;", new Character('\u2227'));
    escapeStrings.put("&or;", new Character('\u2228'));
    escapeStrings.put("&cap;", new Character('\u2229'));
    escapeStrings.put("&cup;", new Character('\u222A'));
    escapeStrings.put("&int;", new Character('\u222B'));
    escapeStrings.put("&there4;", new Character('\u2234'));
    escapeStrings.put("&sim;", new Character('\u223C'));
    escapeStrings.put("&cong;", new Character('\u2245'));
    escapeStrings.put("&asymp;", new Character('\u2248'));
    escapeStrings.put("&ne;", new Character('\u2260'));
    escapeStrings.put("&equiv;", new Character('\u2261'));
    escapeStrings.put("&le;", new Character('\u2264'));
    escapeStrings.put("&ge;", new Character('\u2265'));
    escapeStrings.put("&sub;", new Character('\u2282'));
    escapeStrings.put("&sup;", new Character('\u2283'));
    escapeStrings.put("&nsub;", new Character('\u2284'));
    escapeStrings.put("&sube;", new Character('\u2286'));
    escapeStrings.put("&supe;", new Character('\u2287'));
    escapeStrings.put("&oplus;", new Character('\u2295'));
    escapeStrings.put("&otimes;", new Character('\u2297'));
    escapeStrings.put("&perp;", new Character('\u22A5'));
    escapeStrings.put("&sdot;", new Character('\u22C5'));
    escapeStrings.put("&lceil;", new Character('\u2308'));
    escapeStrings.put("&rceil;", new Character('\u2309'));
    escapeStrings.put("&lfloor;", new Character('\u230A'));
    escapeStrings.put("&rfloor;", new Character('\u230B'));
    escapeStrings.put("&lang;", new Character('\u2329'));
    escapeStrings.put("&rang;", new Character('\u232A'));
    escapeStrings.put("&loz;", new Character('\u25CA'));
    escapeStrings.put("&spades;", new Character('\u2660'));
    escapeStrings.put("&clubs;", new Character('\u2663'));
    escapeStrings.put("&hearts;", new Character('\u2665'));
    escapeStrings.put("&diams;", new Character('\u2666'));
    escapeStrings.put("&quot;", new Character('\u0022'));
    escapeStrings.put("&amp;", new Character('\u0026'));
    escapeStrings.put("&lt;", new Character('\u003C'));
    escapeStrings.put("&gt;", new Character('\u003E'));
    escapeStrings.put("&OElig;", new Character('\u0152'));
    escapeStrings.put("&oelig;", new Character('\u0153'));
    escapeStrings.put("&Scaron;", new Character('\u0160'));
    escapeStrings.put("&scaron;", new Character('\u0161'));
    escapeStrings.put("&Yuml;", new Character('\u0178'));
    escapeStrings.put("&circ;", new Character('\u02C6'));
    escapeStrings.put("&tilde;", new Character('\u02DC'));
    escapeStrings.put("&ensp;", new Character('\u2002'));
    escapeStrings.put("&emsp;", new Character('\u2003'));
    escapeStrings.put("&thinsp;", new Character('\u2009'));
    escapeStrings.put("&zwnj;", new Character('\u200C'));
    escapeStrings.put("&zwj;", new Character('\u200D'));
    escapeStrings.put("&lrm;", new Character('\u200E'));
    escapeStrings.put("&rlm;", new Character('\u200F'));
    escapeStrings.put("&ndash;", new Character('\u2013'));
    escapeStrings.put("&mdash;", new Character('\u2014'));
    escapeStrings.put("&lsquo;", new Character('\u2018'));
    escapeStrings.put("&rsquo;", new Character('\u2019'));
    escapeStrings.put("&sbquo;", new Character('\u201A'));
    escapeStrings.put("&ldquo;", new Character('\u201C'));
    escapeStrings.put("&rdquo;", new Character('\u201D'));
    escapeStrings.put("&bdquo;", new Character('\u201E'));
    escapeStrings.put("&dagger;", new Character('\u2020'));
    escapeStrings.put("&Dagger;", new Character('\u2021'));
    escapeStrings.put("&permil;", new Character('\u2030'));
    escapeStrings.put("&lsaquo;", new Character('\u2039'));
    escapeStrings.put("&rsaquo;", new Character('\u203A'));
    escapeStrings.put("&euro;", new Character('\u20AC'));
  }

  /**
   * Replace all the occurences of HTML escape strings with the
   * respective characters.
   *
   * @param s a <code>String</code> value
   * @return a <code>String</code> value
   */
  public static final String unescapeHTML(String s) {
    char[] chars = s.toCharArray();
    char[] escaped = new char[chars.length];

    // Note: escaped[pos] = end of the escaped char array.
    int pos = 0;

    for (int i = 0; i < chars.length;) {
      if (chars[i] != '&') {
        escaped[pos++] = chars[i++];
        continue;
      }

      // Allow e.g. &#123;
      int j = i + 1;
      if (j < chars.length && chars[j] == '#')
        j++;

      // Scan until we find a char that is not letter or digit.
      for (; j < chars.length; j++) {
        if (!Character.isLetterOrDigit(chars[j]))
          break;
        }

      boolean replaced = false;
      if (j < chars.length && chars[j] == ';') {
        if (s.charAt(i + 1) == '#') { // Check for &#D; and &#xD; pattern
          try {
            long charcode = 0;
            char ch = s.charAt(i + 2);
            if (ch == 'x' || ch == 'X') {
              charcode = Long.parseLong(new String(chars, i + 3, j - i - 3),
                                        16);
            } else if (Character.isDigit(ch)) {
              charcode = Long.parseLong(new String(chars, i + 2, j - i - 2));
            }
            if (charcode > 0 && charcode < 65536) {
              escaped[pos++] = (char) charcode;
              replaced = true;
            }
          } catch (NumberFormatException ex) {
            // Failed, not replaced.
          }

        } else {
          String key = new String(chars, i, j - i + 1);
          Character repl = escapeStrings.get(key);
          if (repl != null) {
            escaped[pos++] = repl.charValue();
            replaced = true;
          }
        }
        j++;                            // Skip over ';'
      }

      if (!replaced) {
        // Not a recognized escape sequence, leave as-is
        System.arraycopy(chars, i, escaped, pos, j - i);
        pos += j - i;
      }
      i = j;
    }
    return new String(escaped, 0, pos);
  }

  /**
   * Given a <code>String</code>, returns an equivalent <code>String</code> with
   * all HTML tags stripped. Note that HTML entities, such as "&amp;amp;" will
   * still be preserved.
   */
  public static String stripHtmlTags(String string) {
    if ((string == null) || "".equals(string)) {
      return string;
    }
    return htmlTagPattern.matcher(string).replaceAll("");
  }

  /**
   * We escape some characters in s to be able to make the string executable
   * from a python string
   */
  public static String pythonEscape(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '\n': sb.append("\\n"); break;
        case '\r': sb.append("\\r"); break;
        case '\t': sb.append("\\t"); break;
        case '\\': sb.append("\\\\"); break;
        case '\"': sb.append("\\\""); break;
        case '\'': sb.append("\\\'"); break;
        default:   sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * We escape some characters in s to be able to insert strings into JavaScript
   * code. Also, make sure that we don't write out --> or </scrip, which may
   * close a script tag.
   */
  public static String javaScriptEscape(String s) {
    return javaScriptEscapeHelper(s, false);
  }

  /**
   * We escape some characters in s to be able to insert strings into JavaScript
   * code. Also, make sure that we don't write out --> or &lt;/scrip, which may
   * close a script tag. Turns all non-ascii characters into ASCII javascript
   * escape sequences (eg \udddd)
   */
  public static String javaScriptEscapeToAscii(String s) {
    return javaScriptEscapeHelper(s, true);
  }

  private static final String[] UNSAFE_TAGS = { "script", "style",
                                                "object", "applet", "!--" };

  /**
   * Helper for javaScriptEscape and javaScriptEscapeToAscii
   */
  private static String javaScriptEscapeHelper(String s,
                                               boolean escapeToAscii) {

    /*
     * IMPORTANT: If you change the semantics of this method (by escaping
     * extra characters, for example), please make similar changes to
     *   com.google.javascript.util.Escape.toJsString
     */
    StringBuilder sb = new StringBuilder(s.length() * 9 / 8);
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '\n': sb.append("\\n"); break;
        case '\r': sb.append("\\r"); break;
        case '\t': sb.append("\\t"); break;
        case '\\': sb.append("\\\\"); break;
        case '\"': sb.append("\\\""); break;
        case '\'': sb.append("\\\'"); break;

        // escape '=' so that javascript won't be executed within tags
        case '=': appendHexJavaScriptRepresentation(sb, c); break;

        case '<':  // for text that could potentially be interpreted as an
        case '/':  // unsafe opening or closing tag, escape the char to hex
          boolean isUnsafe = false;
          for (String tag : UNSAFE_TAGS) {
            if (s.regionMatches(true, i + 1, tag, 0, tag.length())) {
              isUnsafe = true;
              break;
            }
          }
          if (isUnsafe) {
            appendHexJavaScriptRepresentation(sb, c);
          } else {
            sb.append(c);
          }
          break;
        case '>' :
          if (sb.length() > 0 && sb.charAt(sb.length() - 1) == '-') {
            sb.append('\\');
          }
          sb.append(c);
          break;
        // Note: Mozilla browser treats the line/paragraph separator
        // as string terminators, so we need to escape them.
        case '\u2028': sb.append("\\u2028"); break;
        case '\u2029': sb.append("\\u2029"); break;
        default:
          if (c >= 128 && escapeToAscii) {
            appendHexJavaScriptRepresentation(sb, c);
          } else {
            sb.append(c);
          }
      }
    }
    return sb.toString();
  }

  /**
   * Returns a javascript representation of the character in a hex escaped
   * format. Although this is a rather specific method, it is made public
   * because it is also used by the JSCompiler.
   *
   *
   * @param sb The buffer to which the hex representation should be appended.
   * @param c The character to be appended.
   */
  public static void appendHexJavaScriptRepresentation(StringBuilder sb,
                                                       char c) {
    sb.append("\\u");
    String val = Integer.toHexString(c);
    for (int j = val.length(); j < 4; j++) {
      sb.append('0');
    }
    sb.append(val);
  }


  /**
   * Undo escaping as performed in javaScriptEscape(.)
   * Throws an IllegalArgumentException if the string contains
   * bad escaping.
   */
  public static String javaScriptUnescape(String s) {
    StringBuilder sb = new StringBuilder(s.length());
    for (int i = 0; i < s.length(); ) {
      char c = s.charAt(i);
      if (c == '\\') {
        i = javaScriptUnescapeHelper(s, i + 1, sb);
      } else {
        sb.append(c);
        i++;
      }
    }
    return sb.toString();
  }

  /**
   * Looks for an escape code starting at index i of s,
   * and appends it to sb.
   * @return the index of the first character in s
   * after the escape code.
   * @throws IllegalArgumentException if the escape code
   * is invalid
   */
  private static int javaScriptUnescapeHelper(String s, int i,
                                              StringBuilder sb) {
    if (i >= s.length()) {
      throw new IllegalArgumentException(
          "End-of-string after escape character in [" + s + "]");
    }

    char c = s.charAt(i++);
    switch (c) {
      case 'n': sb.append('\n'); break;
      case 'r': sb.append('\r'); break;
      case 't': sb.append('\t'); break;
      case '\\':
      case '\"':
      case '\'':
      case '>':
        sb.append(c);
        break;
      case 'u':
        String hexCode;
        try {
          hexCode = s.substring(i, i + 4);
        } catch (IndexOutOfBoundsException ioobe) {
          throw new IllegalArgumentException(
              "Invalid unicode sequence [" + s.substring(i) + "] at index " + i +
              " in [" + s + "]");
        }
        int unicodeValue;
        try {
          unicodeValue = Integer.parseInt(hexCode, 16);
        } catch (NumberFormatException nfe) {
          throw new IllegalArgumentException(
              "Invalid unicode sequence [" + hexCode + "] at index " + i +
              " in [" + s + "]");
        }
        sb.append((char)unicodeValue);
        i += 4;
        break;
      default:
        throw new IllegalArgumentException(
            "Unknown escape code [" + c + "] at index " + i + " in [" + s + "]");
    }

    return i;
  }

  /**
   * Escape a string for use inside as XML element content. This escapes
   * less-than and ampersand, only.
   */
  public static String xmlContentEscape(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '&': sb.append("&amp;"); break;
        case '<': sb.append("&lt;"); break;

        case '\000': case '\001': case '\002': case '\003': case '\004':
        case '\005': case '\006': case '\007': case '\010': case '\013':
        case '\014': case '\016': case '\017': case '\020': case '\021':
        case '\022': case '\023': case '\024': case '\025': case '\026':
        case '\027': case '\030': case '\031': case '\032': case '\033':
        case '\034': case '\035': case '\036': case '\037':
          // do nothing, these are disallowed characters
          break;
        default:   sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * Escape a string for use inside as XML single-quoted attributes. This
   * escapes less-than, single-quote, ampersand, and (not strictly necessary)
   * newlines.
   */
  public static String xmlSingleQuotedEscape(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '\'': sb.append("&quot;"); break;
        case '&': sb.append("&amp;"); break;
        case '<': sb.append("&lt;"); break;
        case '\n': sb.append("&#xA;"); break;

        case '\000': case '\001': case '\002': case '\003': case '\004':
        case '\005': case '\006': case '\007': case '\010': case '\013':
        case '\014': case '\016': case '\017': case '\020': case '\021':
        case '\022': case '\023': case '\024': case '\025': case '\026':
        case '\027': case '\030': case '\031': case '\032': case '\033':
        case '\034': case '\035': case '\036': case '\037':
          // do nothing, these are disallowed characters
          break;
        default:   sb.append(c);
      }
    }
    return sb.toString();
  }

  // C0 control characters except \t, \n, and \r and 0xFFFE and 0xFFFF
  private static final CharMatcher CONTROL_MATCHER = CharMatcher.anyOf(
      "\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007" +
      "\u0008\u000B\u000C\u000E\u000F" +
      "\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017" +
      "\u0018\u0019\u001A\u001B\u001C\u001D\u001E\u001F" +
      "\uFFFE\uFFFF");

  /**
   * Escape a string that is meant to be embedded in a CDATA section.
   * The returned string is guaranteed to be valid CDATA content.
   * The syntax of CDATA sections is the following:
   * <blockquote>
   *   <code>&lt;[!CDATA[...]]&gt;</code>
   * </blockquote>
   * The only invalid character sequence in a CDATA tag is "]]&gt;".
   * If this sequence is present in the input string, we replace
   * it by closing the current CDATA field, then write ']]&amp;gt;',
   * then reopen a new CDATA section.
   */
  public static String xmlCDataEscape(String s) {
     // Make sure there are no illegal control characters.
     s = CONTROL_MATCHER.removeFrom(s);
    // Return the original reference if the string doesn't have a match.
    int found = s.indexOf("]]>");
    if (found == -1) {
      return s;
    }

    // For each occurrence of "]]>", append a string that adds "]]&gt;" after
    // the end of the CDATA which has just been closed, then opens a new CDATA.
    StringBuilder sb = new StringBuilder();
    int prev = 0;
    do {
      sb.append(s.substring(prev, found + 3));
      sb.append("]]&gt;<![CDATA[");
      prev = found + 3;
    } while ((found = s.indexOf("]]>", prev)) != -1);
    sb.append(s.substring(prev));
    return sb.toString();
  }

  /**
   * We escape some characters in s to be able to insert strings into Java code
   */
  public static String javaEscape(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '\n': sb.append("\\n"); break;
        case '\r': sb.append("\\r"); break;
        case '\t': sb.append("\\t"); break;
        case '\\': sb.append("\\\\"); break;
        case '\"': sb.append("\\\""); break;
        case '&': sb.append("&amp;"); break;
        case '<': sb.append("&lt;"); break;
        case '>': sb.append("&gt;"); break;
        case '\'': sb.append("\\\'"); break;
        default:   sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * Escape a string so that it can be safely placed as value of an
   * attribute.  This is essentially similar to the {@link
   * javaEscape(java.lang.String)} except that it escapes double quote
   * to the HTML literal &amp;quot;. This is to prevent the double
   * quote from being interpreted as the character closing the
   * attribute.
   */
  public static String javaEscapeWithinAttribute(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      switch (c) {
        case '\n': sb.append("\\n"); break;
        case '\r': sb.append("\\r"); break;
        case '\t': sb.append("\\t"); break;
        case '\\': sb.append("\\\\"); break;
        case '\"': sb.append("&quot;"); break;
        case '&': sb.append("&amp;"); break;
        case '<': sb.append("&lt;"); break;
        case '>': sb.append("&gt;"); break;
        case '\'': sb.append("\\\'"); break;
        default:   sb.append(c);
      }
    }
    return sb.toString();
  }

  /**
   * Returns a form of "s" appropriate for including in an XML document, after
   * escaping certain special characters (e.g. '&' => '&amp;', etc.)
   */
  public static String xmlEscape(String s) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      switch (ch) {
        case '"':
          sb.append("&quot;");
          break;
        case '&':
          sb.append("&amp;");
          break;
        case '\'':
          sb.append("&apos;");
          break;
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        case '\n':
          sb.append("&#xA;");
          break;
        case '\r':
          sb.append("&#xD;");
          break;
        case '\t':
          sb.append("&#x9;");
          break;
        case '\0':
          // \0 is not a valid XML char - skip it
          break;
        default:
          sb.append(ch);
          break;
      }
    }
    return sb.toString();
  }

  /**
   * Escapes special characters (& < > ") from a string so it can safely be
   * included in an HTML document. (same as <code>xmlEscape</code> except that
   * <code>htmlEscape</code> does not escape the apostrophe character).
   */
  public static String htmlEscape(String s) {
    // This method gets called A LOT so it has to be excruciatingly efficient.
    // Older versions were responsible for several percent of all objects
    // created on the heap, and 10% of total execution time.
    // In particular, if the String has no characters that need escaping, this
    // method should return its argument.

    StringBuilder sb = null;
    String replacement;
    int start = 0; // the earliest input position we haven't copied yet.
    for (int i = 0; i < s.length(); i++) {
      switch (s.charAt(i)) {
        case '"':
          replacement = "&quot;";
          break;
        case '&':
          replacement = "&amp;";
          break;
        case '<':
          replacement = "&lt;";
          break;
        case '>':
          replacement = "&gt;";
          break;
        default:
          replacement = null;
      }

      if (replacement != null) {
        if (sb == null) {
          // This is the first time we have found a replacement. Allocate the
          // StringBuilder now.
          // This initial size for the StringBuilder below will be exact if
          // this initial replacement is the only one. If not, sb will expand.
          sb = new StringBuilder(s.length() + replacement.length() - 1);
        }
        if (i > start) {
          // we have to copy some of the earlier string.
          sb.append(s.substring(start, i));
        }
        sb.append(replacement);
        start = i+1;
      }
    }
    // now possibly also copy what's leftover in the input string.
    if (start > 0) {
      sb.append(s.substring(start));
    }

    if (sb != null) {
      return sb.toString();
    }
    return s;
  }

  /**
   * Escapes the special characters from a string so it can be used as part of
   * a regex pattern. This method is for use on gnu.regexp style regular
   * expressions.
   */
  public static String regexEscape(String s) {
    StringBuilder sb = new StringBuilder();

    for(int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      // Test if c is an escapable character
      if ("()|*+?.{}[]$^\\".indexOf(c) != -1) {
        sb.append('\\');
        sb.append(c);
      }
      else {
        sb.append(c);
      }
    }

    return sb.toString();
  }

  /**
   * Escapes the special characters from a string so it can be used as part of
   * a regex pattern. This method is for use on regexes in the flavor of the
   * java.util.regex package. This method should be removed when we move to
   * the java version 1.5 (Tiger) release, since that release gives us
   * a literal regex flag as well as a quote method to produce literal regexes.
   */
  public static String javaUtilRegexEscape(String s) {
    // Use the quoting mechanism in Pattern unless it is interfered with
    // by the contents of the string.
    if (s.indexOf("\\E") == -1) {
      return "\\Q" + s + "\\E";
    }

    // Very rare case: must escape each character.
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < s.length(); i++) {
      sb.append('\\');
      sb.append(s.charAt(i));
    }

    return sb.toString();
  }

  /**
   * Escapes the '\' and '$' characters, which comprise the subset of regex
   * characters that has special meaning in methods such as:
   *
   * <pre>java.util.regex.Matcher.appendReplacement(sb, replacement);</pre>
   * <pre>java.lang.String.replaceAll(str, replacement);</pre>
   *
   * Note that this method is offered in java version 1.5 as the method
   *
   * <pre>java.util.regex.Matcher.quoteReplacement(String);</pre>
   */
  public static String regexReplacementEscape(String s) {
    StringBuilder sb = null;

    for (int i = 0, n = s.length(); i < n; i++) {
      char c = s.charAt(i);

      switch (c) {
        case '\\':
        case '$':
          if (sb == null) {
            // This is the first replacement necessary. Initialize the
            // string buffer to contain the all of the previously checked
            // characters in 's'
            sb = new StringBuilder(s.substring(0, i));
          }
          sb.append('\\');
        default:
          if (sb != null) {
            sb.append(c);
          }
          break;
      }
    }

    return (sb == null) ? s : sb.toString();
  }



  /**
   * The old interface to cropBetween - using a single char limit
   */
  public static String cropBetween(String in, char limit) {
    return cropBetween(in, String.valueOf(new char[]{limit}));
  }

  /**
   * This removes characters between maching charLimit chars. For example
   * cropBetween("ab^cd^ef^gh^hi", '^') will return "abefhi" It will consider
   * squences of 2 charLimit as one charLimit in the output
   *
   * @param in - the string to process
   * @param limit - the limit of the string(s) to remove
   *
   * @return String - the cropped string
   */
  public static String cropBetween(String in, String limit) {
    StringBuilder out = new StringBuilder();
    int lastPos = 0;
    int lenLimit = limit.length();
    boolean modeAdd = true;
    int pos = -1;
    while ((pos = in.indexOf(limit, lastPos)) >= 0) {
      if (modeAdd) {
        out.append(in.substring(lastPos, pos));
      }
      modeAdd = !modeAdd;
      lastPos = pos + lenLimit;
    }

    // add the remainings
    if (modeAdd) {
      out.append(in.substring(lastPos));
    }

    return out.toString();
  }

  /**
   * This converts a String to a list of strings by extracting the substrings
   * between delimiter
   *
   * @param in - what to process
   * @param delimiter - the delimiting string
   * @param doStrip - to strip the substrings before adding to the list
   *
   * @return LinkedList
   */
  public static LinkedList<String> string2List(String in, String delimiter,
                                               boolean doStrip) {
    if (in == null) {
      return null;
    }

    LinkedList<String> out = new LinkedList<String>();
    string2Collection(in, delimiter, doStrip, out);
    return out;
  }

  /**
   * This converts a String to a Set of strings by extracting the substrings
   * between delimiter
   *
   * @param in - what to process
   * @param delimiter - the delimiting string
   * @param doStrip - to strip the substrings before adding to the list
   *
   * @return Set
   */
  public static Set string2Set(String in, String delimiter, boolean doStrip) {
    if (in == null) {
      return null;
    }

    HashSet<String> out = new HashSet<String>();
    string2Collection(in, delimiter, doStrip, out);
    return out;
  }

  /**
   * Converts a delimited string to a collection of strings. Substrings between
   * delimiters are extracted from the string and added to a collection that is
   * provided by the caller.
   *
   * @param in The delimited input string to process
   * @param delimiter The string delimiting entries in the input string.
   * @param doString Whether to strip the substrings before adding to the
   *          collection
   * @param collection The collection to which the strings will be added. If
   *          <code>null</code>, a new <code>List</code> will be created.
   *
   * @return The collection to which the substrings were added. This is
   *         syntactic sugar to allow call chaining.
   */
  public static Collection<String> string2Collection(String in,
      String delimiter,
      boolean doStrip,
      Collection<String> collection) {
    if (in == null) {
      return null;
    }
    if (collection == null) {
      collection = new ArrayList<String>();
    }
    if (delimiter == null || delimiter.length() == 0) {
      collection.add(in);
      return collection;
    }

    int fromIndex = 0;
    int pos;
    while ((pos = in.indexOf(delimiter, fromIndex)) >= 0) {
      String interim = in.substring(fromIndex, pos);
      if (doStrip) {
        interim = strip(interim);
      }
      if (!doStrip || interim.length() > 0) {
        collection.add(interim);
      }

      fromIndex = pos + delimiter.length();
    }

    String interim = in.substring(fromIndex);
    if (doStrip) {
      interim = strip(interim);
    }
    if (!doStrip || interim.length() > 0) {
      collection.add(interim);
    }

    return collection;
  }

  /**
   * Lots of people called list2String when in fact it was implemented as
   * Collection2String. I added Collection2String as a new function and am
   * leaving the list2String function signature here so it can continue to be
   *
   * @deprecated Please use
   *     But note that {@code Join} does not consider null elements to be
   *     equivalent to the empty string, as this method does.
   */
  @Deprecated public static String list2String(
      Collection<?> in, String separator) {
    return Collection2String(in, separator);
  }

  /**
   * This concatenates the elements of a collection in a string
   *
   * @param in - the collection that has to be conatenated
   * @param separator - a string to sepparate the elements from the list
   *
   * @return String
   *
   * @deprecated Please use
   *     But note that {@code Join} does not consider null elements to be
   *     equivalent to the empty string, as this method does.
   */
  @Deprecated public static String Collection2String(
      Collection<?> in, String separator) {
    if (in == null) {
      return null;
    }
    return Iterator2String(in.iterator(), separator);
  }

  /**
   * @deprecated Please use
   *     But note that {@code Join} does not consider null elements to be
   *     equivalent to the empty string, as this method does.
   */
  @Deprecated public static String Iterator2String(
      Iterator<?> it, String separator) {
    if (it == null) {
      return null;
    }

    StringBuilder out = new StringBuilder();
    while (it.hasNext()) {
      if (out.length() > 0) {
        out.append(separator);
      }
      out.append(it.next().toString());
    }

    return out.toString();
  }

  /**
   * This converts a string to a Map. It will first split the string into
   * entries using delimEntry. Then each entry is split into a key and a value
   * using delimKey. By default we strip the keys. Use doStripEntry to strip
   * also the entries
   *
   * @param in - the string to be processed
   * @param delimEntry - delimiter for the entries
   * @param delimKey - delimiter between keys and values
   * @param doStripEntry - strip entries before inserting in the map
   *
   * @return HashMap
   */
  public static HashMap<String, String> string2Map(String in, String delimEntry,
      String delimKey, boolean doStripEntry) {

    if (in == null) {
      return null;
    }

    HashMap<String, String> out = new HashMap<String, String>();

    if (isEmpty(delimEntry) || isEmpty(delimKey)) {
      out.put(strip(in), "");
      return out;
    }

    Iterator<String> it = string2List(in, delimEntry, false).iterator();
    int len = delimKey.length();
    while (it.hasNext()) {
      String entry = it.next();
      int pos = entry.indexOf(delimKey);
      if (pos > 0) {
        String value = entry.substring(pos + len);
        if (doStripEntry) {
          value = strip(value);
        }
        out.put(strip(entry.substring(0, pos)), value);
      } else {
        out.put(strip(entry), "");
      }
    }

    return out;
  }

  /**
   * This function concatenates the elements of a Map in a string with form
   *  "<key1><sepKey><value1><sepEntry>...<keyN><sepKey><valueN>"
   *
   * @param in - the map to be converted
   * @param sepKey - the separator to put between key and value
   * @param sepEntry - the separator to put between map entries
   *
   * @return String
   */
  public static <K, V> String map2String(Map<K,V> in, String sepKey,
                                         String sepEntry) {
    if (in == null) {
      return null;
    }

    StringBuilder out = new StringBuilder();
    Iterator<Entry<K, V>> it = in.entrySet().iterator();
    while (it.hasNext()) {
      if (out.length() > 0) {
        out.append(sepEntry);
      }
      Entry<K, V> entry = it.next();
      out.append(entry.getKey() + sepKey + entry.getValue());
    }

    return out.toString();
  }

  /**
   * Given a map, creates and returns a new map in which all keys are the
   * lower-cased version of each key.
   *
   * @param map A map containing String keys to be lowercased
   * @throws IllegalArgumentException if the map contains duplicate string keys
   *           after lower casing
   */
  public static <V> Map lowercaseKeys(Map<String, V> map) {
    Map<String, V> result = new HashMap<String, V>(map.size());
    for (Iterator<String> it = map.keySet().iterator(); it.hasNext(); ) {
      String key = it.next();
      if (result.containsKey(key.toLowerCase())) {
        throw new IllegalArgumentException(
          "Duplicate string key in map when lower casing");
      }
      result.put(key.toLowerCase(), map.get(key));
    }
    return result;
  }

  /**
   * Replaces any string of adjacent whitespace characters with the whitespace
   * character " ".
   *
   * @param str the string you want to munge
   * @return String with no more excessive whitespace!
   *
   * @see collapse
   */
  public static String collapseWhitespace(String str) {
    return collapse(str, WHITE_SPACES, " ");
  }

  /**
   * Replaces any string of matched characters with the supplied string.<p>
   *
   * This is a more general version of collapseWhitespace.
   *
   * <pre>
   *   E.g. collapse("hello     world", " ", "::")
   *   will return the following string: "hello::world"
   * </pre>
   *
   * @param str the string you want to munge
   * @param chars all of the characters to be considered for munge
   * @param replacement the replacement string
   * @return String munged and replaced string.
   */
  public static String collapse(String str, String chars, String replacement) {
    if (str == null) {
      return null;
    }

    StringBuilder newStr = new StringBuilder();

    boolean prevCharMatched = false;
    char c;
    for (int i = 0; i < str.length(); i++) {
      c = str.charAt(i);
      if (chars.indexOf(c) != -1) {
        // this character is matched
        if (prevCharMatched) {
          // apparently a string of matched chars, so don't append anything
          // to the string
          continue;
        }
        prevCharMatched = true;
        newStr.append(replacement);
      }
      else {
        prevCharMatched = false;
        newStr.append(c);
      }
    }

    return newStr.toString();
  }

  /**
   * Read a String of up to maxLength bytes from an InputStream
   *
   * @param is input stream
   * @param maxLength max number of bytes to read from "is". If this is -1, we
   *          read everything.
   *
   * @return String up to maxLength bytes, read from "is"
   */
  public static String stream2String(InputStream is, int maxLength)
      throws IOException {
    byte[] buffer = new byte[4096];
    StringWriter sw = new StringWriter();
    int totalRead = 0;
    int read = 0;

    do {
      sw.write(new String(buffer, 0, read));
      totalRead += read;
      read = is.read(buffer, 0, buffer.length);
    } while (((-1 == maxLength) || (totalRead < maxLength)) && (read != -1));

    return sw.toString();
  }

  /**
   * Parse a list of substrings separated by a given delimiter. The delimiter
   * can also appear in substrings (just double them):
   *
   * parseDelimitedString("this|is", '|') returns ["this","is"]
   * parseDelimitedString("this||is", '|') returns ["this|is"]
   *
   * @param list String containing delimited substrings
   * @param delimiter Delimiter (anything except ' ' is allowed)
   *
   * @return String[] A String array of parsed substrings
   */
  public static String[] parseDelimitedList(String list,
                                            char delimiter) {
    String delim = "" + delimiter;
    // Append a sentinel of delimiter + space
    // (see comments below for more info)
    StringTokenizer st = new StringTokenizer(list + delim + " ",
                                             delim,
                                             true);
    ArrayList<String> v = new ArrayList<String>();
    String lastToken = "";
    String word = "";

    // We keep a sliding window of 2 tokens
    //
    // delimiter : delimiter -> append delimiter to current word
    //                          and clear most recent token
    //                          (so delim : delim : delim will not
    //                          be treated as two escaped delims.)
    //
    // tok : delimiter -> append tok to current word
    //
    // delimiter : tok -> add current word to list, and clear it.
    //                    (We append a sentinel that conforms to this
    //                    pattern to make sure we've pushed every parsed token)
    while (st.hasMoreTokens()) {
      String tok = st.nextToken();
      if (lastToken != null) {
        if (tok.equals(delim)) {
          word = word + lastToken;
          if (lastToken.equals(delim)) tok = null;
        } else {
          if (!word.equals("")) v.add(word);
          word = "";
        }
      }
      lastToken = tok;
    }

    return v.toArray(new String[0]);
  }

  /**
   * Helper function for null and empty string testing.
   *
   * @return true iff s == null or s.equals("");
   */
  public static boolean isEmpty(String s) {
    return makeSafe(s).length() == 0;
  }

  /**
   * Helper function for null, empty, and whitespace string testing.
   *
   * @return true if s == null or s.equals("") or s contains only whitespace
   *         characters.
   */
  public static boolean isEmptyOrWhitespace(String s) {
    s = makeSafe(s);
    for (int i = 0, n = s.length(); i < n; i++) {
      if (!Character.isWhitespace(s.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Helper function for making null strings safe for comparisons, etc.
   *
   * @return (s == null) ? "" : s;
   */
  public static String makeSafe(String s) {
    return (s == null) ? "" : s;
  }

  /**
   * Helper function for making empty strings into a null.
   *
   * @return null if s is zero length.  otherwise, returns s.
   */
  public static String toNullIfEmpty(String s) {
    return (StringUtil.isEmpty(s)) ? null : s;
  }

  /**
   * Helper function for turning empty or whitespace strings into a null.
   *
   * @return null if s is zero length or if s contains only whitespace
   *         characters.  otherwise, returns s.
   */
  public static String toNullIfEmptyOrWhitespace(String s) {
    return (StringUtil.isEmptyOrWhitespace(s)) ? null : s;
  }

  /**
   * Serializes a map
   *
   * @param map A map of String keys to arrays of String values
   * @param keyValueDelim Delimiter between keys and values
   * @param entryDelim Delimiter between entries
   *
   * @return String A string containing a serialized representation of the
   *         contents of the map.
   *
   * e.g. arrayMap2String({"foo":["bar","bar2"],"foo1":["bar1"]}, "=", "&")
   * returns "foo=bar&foo=bar2&foo1=bar1"
   */
  public static String arrayMap2String(Map<String, String[]> map,
                                       String keyValueDelim,
                                       String entryDelim) {
    Set<Entry<String, String[]>> entrySet = map.entrySet();
    Iterator<Entry<String, String[]>> itor = entrySet.iterator();
    StringWriter sw = new StringWriter();
    while (itor.hasNext()) {
      Entry<String, String[]> entry = itor.next();
      String key = entry.getKey();
      String[] values = entry.getValue();
      for (int i = 0; i < values.length; i++) {
        sw.write(entry.getKey() + keyValueDelim + values[i]);
        if (i < values.length - 1) {
          sw.write(entryDelim);
        }
      }
      if (itor.hasNext()) {
        sw.write(entryDelim);
      }
    }
    return sw.toString();
  }

  /**
   * Compares two strings, guarding against nulls If both Strings are null we
   * return true
   */
  public static boolean equals(String s1, String s2) {
    if (s1 == s2) {
      return true; // Either both the same String, or both null
    }
    if (s1 != null) {
      if (s2 != null) {
        return s1.equals(s2);
      }
    }
    return false;
  }

  /**
   * Splits s with delimiters in delimiter and returns the last token
   */
  public static String lastToken(String s, String delimiter) {
    String[] parts = split(s, delimiter);
    return (parts.length == 0) ? "" : parts[parts.length -1];
  }

  /**
   * Determines if a string contains only ascii characters
   */
  public static boolean allAscii(String s) {
    int len = s.length();
    for (int i = 0; i < len; ++i) {
      if ((s.charAt(i) & 0xff80) != 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Determines if a string contains what looks like an html character
   * reference. Useful for deciding whether unescaping is necessary.
   */
  public static boolean containsCharRef(String s) {
    return characterReferencePattern.matcher(s).find();
  }

  /**
   * Determines if a string is a Hebrew word. A string is considered to be
   * a Hebrew word if {@link #isHebrew(int)} is true for any of its characters.
   */
  public static boolean isHebrew(String s) {
    int len = s.length();
    for (int i = 0; i < len; ++i) {
      if (isHebrew(s.codePointAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Determines if a character is a Hebrew character.
   */
  public static boolean isHebrew(int codePoint) {
    return Character.UnicodeBlock.HEBREW.equals(
               Character.UnicodeBlock.of(codePoint));
  }

  /**
   * Determines if a string is a CJK word. A string is considered to be CJK
   * if {@link #isCjk(char)} is true for any of its characters.
   */
  public static boolean isCjk(String s) {
    int len = s.length();
    for (int i = 0; i < len; ++i) {
      if (isCjk(s.codePointAt(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Unicode code blocks containing CJK characters.
   */
  private static final Set<Character.UnicodeBlock> CJK_BLOCKS;
  static {
    Set<Character.UnicodeBlock> set = new HashSet<Character.UnicodeBlock>();
    set.add(Character.UnicodeBlock.HANGUL_JAMO);
    set.add(Character.UnicodeBlock.CJK_RADICALS_SUPPLEMENT);
    set.add(Character.UnicodeBlock.KANGXI_RADICALS);
    set.add(Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION);
    set.add(Character.UnicodeBlock.HIRAGANA);
    set.add(Character.UnicodeBlock.KATAKANA);
    set.add(Character.UnicodeBlock.BOPOMOFO);
    set.add(Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO);
    set.add(Character.UnicodeBlock.KANBUN);
    set.add(Character.UnicodeBlock.BOPOMOFO_EXTENDED);
    set.add(Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS);
    set.add(Character.UnicodeBlock.ENCLOSED_CJK_LETTERS_AND_MONTHS);
    set.add(Character.UnicodeBlock.CJK_COMPATIBILITY);
    set.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A);
    set.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS);
    set.add(Character.UnicodeBlock.HANGUL_SYLLABLES);
    set.add(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS);
    set.add(Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS);
    set.add(Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
    set.add(Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B);
    set.add(Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT);
    CJK_BLOCKS = Collections.unmodifiableSet(set);
  }

  /**
   * Determines if a character is a CJK ideograph or a character typically
   * used only in CJK text.
   *
   * Note: This function cannot handle supplementary characters. To handle all
   * Unicode characters, including supplementary characters, use the function
   * {@link #isCjk(int)}.
   */
  public static boolean isCjk(char ch) {
    return isCjk((int) ch);
  }

  /**
   * Determines if a character is a CJK ideograph or a character typically
   * used only in CJK text.
   */
  public static boolean isCjk(int codePoint) {
    // Time-saving early exit for all Latin-1 characters.
    if ((codePoint & 0xFFFFFF00) == 0) {
      return false;
    }

    return CJK_BLOCKS.contains(Character.UnicodeBlock.of(codePoint));
  }

  /**
   * Replaces each non-ascii character in s with its Unicode escape sequence
   * \\uxxxx where xxxx is a hex number. Existing escape sequences won't be
   * affected.
   */
  public static String unicodeEscape(String s) {
    if (allAscii(s)) {
      return s;
    }
    StringBuilder sb = new StringBuilder(s.length());
    int len = s.length();
    for (int i = 0; i < len; ++i) {
      char ch = s.charAt(i);
      if (ch <= 127) {
        sb.append(ch);
      } else {
        sb.append("\\u");
        String hexString = Integer.toHexString(ch);
        // Pad with zeros if necessary
        int numZerosToPad = 4- hexString.length();
        for (int j = 0; j < numZerosToPad; ++j) {
          sb.append('0');
        }
        sb.append(hexString);
      }
    }
    return sb.toString();
  }

  /**
   * Returns the approximate display width of the string, measured in units of
   * ascii characters.
   *
   * @see displayWidth(char)
   */
  public static int displayWidth(String s) {
    int width = 0;
    int len = s.length();
    for (int i = 0; i < len; ++i) {
      width += displayWidth(s.charAt(i));
    }
    return width;
  }

  /**
   * Returns the approximate display width of the character, measured
   * in units of ascii characters.
   *
   * This method should err on the side of caution. By default, characters
   * are assumed to have width 2; this covers CJK ideographs, various
   * symbols and miscellaneous weird scripts. Given below are some Unicode
   * ranges for which it seems safe to assume that no character is
   * substantially wider than an ascii character:
   *   - Latin, extended Latin, even more extended Latin.
   *   - Greek, extended Greek, Cyrillic.
   *   - Some symbols (including currency symbols) and punctuation.
   *   - Half-width Katakana and Hangul.
   *   - Hebrew
   *   - Thai
   * Characters in these ranges are given a width of 1.
   *
   * IMPORTANT: this function has an analog in strutil.cc named
   * UnicodeCharWidth, which needs to be updated if you change the
   * implementation here.
   */
  public static int displayWidth(char ch) {
    if (ch <= '\u04f9' ||
        ch == '\u05be' ||
        (ch >= '\u05d0' && ch <= '\u05ea') ||
        ch == '\u05F3' ||
        ch == '\u05f4' ||
        (ch >= '\u0e00' && ch <= '\u0e7f') ||
        (ch >= '\u1e00' && ch <= '\u20af') ||
        (ch >= '\u2100' && ch <= '\u213a') ||
        (ch >= '\uff61' && ch <= '\uffdc')) {
      return 1;
    }
    return 2;
  }

  /**
   * @return a string representation of the given native array.
   */
  public static String toString(float[] iArray) {
    if (iArray == null) {
      return "NULL";
    }

    StringBuilder buffer = new StringBuilder();
    buffer.append("[");
    for (int i = 0; i < iArray.length; i ++) {
      buffer.append(iArray[i]);
      if (i != (iArray.length - 1)) {
        buffer.append(", ");
      }
    }
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * @return a string representation of the given native array.
   */
  public static String toString(long[] iArray) {
    if (iArray == null) {
      return "NULL";
    }

    StringBuilder buffer = new StringBuilder();
    buffer.append("[");
    for (int i = 0; i < iArray.length; i ++) {
      buffer.append(iArray[i]);
      if (i != (iArray.length - 1)) {
        buffer.append(", ");
      }
    }
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * @return a string representation of the given native array
   */
  public static String toString(int[] iArray) {
    if (iArray == null) {
      return "NULL";
    }

    StringBuilder buffer = new StringBuilder();
    buffer.append("[");
    for (int i = 0; i < iArray.length; i ++) {
      buffer.append(iArray[i]);
      if (i != (iArray.length - 1)) {
        buffer.append(", ");
      }
    }
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * @return a string representation of the given array.
   */
  public static String toString(String[] iArray) {
    if (iArray == null) return "NULL";

    StringBuilder buffer = new StringBuilder();
    buffer.append("[");
    for (int i = 0; i < iArray.length; i ++) {
      buffer.append("'").append(iArray[i]).append("'");
      if (i != iArray.length-1) {
        buffer.append(", ");
      }
    }
    buffer.append("]");

    return buffer.toString();
  }

  /**
   * Returns the string, in single quotes, or "NULL". Intended only for
   * logging.
   *
   * @param s - the string
   * @return the string, in single quotes, or the string "null" if it's null.
   */
  public static String toString(String s) {
    if (s == null) {
      return "NULL";
    } else {
      return new StringBuilder(s.length() + 2).append("'").append(s)
                                              .append("'").toString();
    }
  }


  /**
   * @return a string representation of the given native array
   */
  public static String toString(int[][] iArray) {
    if (iArray == null) {
      return "NULL";
    }

    StringBuilder buffer = new StringBuilder();
    buffer.append("[");
    for (int i = 0; i < iArray.length; i ++) {
      buffer.append("[");
      for (int j = 0; j < iArray[i].length; j ++) {
        buffer.append(iArray[i][j]);
        if (j != (iArray[i].length - 1)) {
          buffer.append(", ");
        }
      }
      buffer.append("]");
      if (i != iArray.length-1) {
        buffer.append(" ");
      }
    }
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * @return a string representation of the given native array.
   */
  public static String toString(long[][] iArray) {
    if (iArray == null) return "NULL";

    StringBuilder buffer = new StringBuilder();
    buffer.append("[");
    for (int i = 0; i < iArray.length; i ++) {
      buffer.append("[");
      for (int j = 0; j < iArray[i].length; j ++) {
        buffer.append(iArray[i][j]);
        if (j != (iArray[i].length - 1)) {
          buffer.append(", ");
        }
      }
      buffer.append("]");
      if (i != iArray.length-1) {
        buffer.append(" ");
      }
    }
    buffer.append("]");
    return buffer.toString();
  }

  /**
   * @return a String representation of the given object array.
   * The strings are obtained by calling toString() on the
   * underlying objects.
   */
  public static String toString(Object[] obj) {
    if (obj == null) return "NULL";
    StringBuilder tmp = new StringBuilder();
    tmp.append("[");
    for(int i = 0; i < obj.length; i ++) {
      tmp.append(obj[i].toString());
      if (i != obj.length-1) {
        tmp.append(",");
      }
    }
    tmp.append("]");
    return tmp.toString();
  }

  /**
   * Replacement for deprecated StringBufferInputStream(). Instead of:
   *   InputStream is = new StringBuilderInputStream(str);
   * do:
   *   InputStream is = StringUtil.toUTF8InputStream(str);
   */
  public static InputStream toUTF8InputStream(String str) {
    InputStream is = null;
    try {
      is = new ByteArrayInputStream(str.getBytes("UTF-8"));
    } catch (UnsupportedEncodingException e) {
      // UTF-8 should always be supported
      throw new AssertionError();
    }
    return is;
  }

  /**
   * Copy all data from in to out in 4096 byte chunks.
   */
  public static void copyStreams (InputStream in, OutputStream out)
      throws IOException {
    if (in == null || out == null) {
      throw new IllegalArgumentException();
    }
    final byte[] buffer = new byte[4096];
    int len;
    while (-1 != (len = in.read (buffer, 0, buffer.length))) {
      out.write (buffer, 0, len);
    }
  }

  /**
   * Convert a byte array to a String using Latin-1 (aka ISO-8859-1) encoding.
   *
   * Note: something is probably wrong if you're using this method. Either
   *       you're dealing with legacy code that doesn't support i18n or you're
   *       using a third-party library that only deals with Latin-1. New code
   *       should (almost) always uses UTF-8 encoding.
   *
   * @return the decoded String or null if ba is null
   */
  public static String bytesToLatin1(final byte[] ba) {
    // ISO-8859-1 should always be supported
    return bytesToEncoding(ba, "ISO-8859-1");
  }

  private static char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7',
                                    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

  /**
   * Convert a byte array to a hex-encoding string: "a33bff00..."
   */
  public static String bytesToHexString(final byte[] bytes) {
    return bytesToHexString(bytes, null);
  }

  /**
   * Convert a byte array to a hex-encoding string with the specified
   * delimiter: "a3&lt;delimiter&gt;3b&lt;delimiter&gt;ff..."
   */
  public static String bytesToHexString(final byte[] bytes,
    Character delimiter) {
    StringBuffer hex =
      new StringBuffer(bytes.length * (delimiter == null ? 2 : 3));
    int nibble1, nibble2;
    for (int i = 0; i < bytes.length; i++) {
      nibble1 = (bytes[i] >>> 4) & 0xf;
      nibble2 = bytes[i] & 0xf;
      if (i > 0 && delimiter != null) hex.append(delimiter.charValue());
      hex.append(hexChars[nibble1]);
      hex.append(hexChars[nibble2]);
    }
    return hex.toString();
  }

  /**
   * Convert a String to a byte array using Latin-1 (aka ISO-8859-1) encoding.
   * If any character in the String is not Latin-1 (meaning it's high 8 bits
   * are not all zero), then the returned byte array will contain garbage.
   * Therefore, only use this if you know all your characters are within
   * Latin-1.
   *
   * Note: something is probably wrong if you're using this method. Either
   *       you're dealing with legacy code that doesn't support i18n or you're
   *       using a third-party library that only deals with Latin-1. New code
   *       should (almost) always uses UTF-8 encoding.
   *
   * @return the encoded byte array or null if str is null
   */
  public static byte[] latin1ToBytes(final String str) {
    // ISO-8859-1 should always be supported
    return encodingToBytes(str, "ISO-8859-1");
  }

  /**
   * Convert a byte array to a String using UTF-8 encoding.
   *
   * @return the decoded String or null if ba is null
   */
  public static String bytesToUtf8(final byte[] ba) {
    // UTF-8 should always be supported
    return bytesToEncoding(ba, "UTF8");
  }

  /**
   * Convert a String to a byte array using UTF-8 encoding.
   *
   * @return the encoded byte array or null if str is null
   */
  public static byte[] utf8ToBytes(final String str) {
    // UTF-8 should always be supported
    return encodingToBytes(str, "UTF8");
  }

  /**
   * Convert a byte array to a String using the specified encoding.
   * @param encoding the encoding to use
   * @return the decoded String or null if ba is null
   */
  private static String bytesToEncoding(final byte[] ba, final String encoding) {
    if (ba == null) {
      return null;
    }

    try {
      return new String(ba, encoding);
    } catch (final UnsupportedEncodingException e) {
      throw new Error(encoding + " not supported! Original exception: " + e);
    }
  }

  /**
   * Convert a String to a byte array using the specified encoding.
   * @param encoding the encoding to use
   * @return the encoded byte array or null if str is null
   */
  public static byte[] encodingToBytes(
    final String str, final String encoding) {

    if (str == null) {
      return null;
    }

    try {
      return str.getBytes(encoding);
    } catch (final UnsupportedEncodingException e) {
      throw new Error(encoding + " not supported! Original exception: " + e);
    }
  }

  /**
   * Convert an array of bytes into a List of Strings using UTF-8. A line is
   * considered to be terminated by any one of a line feed ('\n'), a carriage
   * return ('\r'), or a carriage return followed immediately by a linefeed.<p/>
   *
   * Can be used to parse the output of
   *
   * @param bytes the array to convert
   * @return A new mutable list containing the Strings in the input array. The
   *         list will be empty if bytes is empty or if it is null.
   */
  public static List<String> bytesToStringList(byte[] bytes) {
    List<String> lines = new ArrayList<String>();

    if (bytes == null) {
      return lines;
    }

    BufferedReader r = null;

    try {
      r = new BufferedReader(
              new InputStreamReader(
                  new ByteArrayInputStream(bytes),
                  "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      // If UTF-8 is not supported we are in big trouble.
      throw new RuntimeException(e);
    }

    try {
      try {
        for (String line = r.readLine(); line != null; line = r.readLine()) {
          lines.add(line);
        }
      } finally {
        r.close();
      }
    } catch (IOException e) {
      // I can't think of a reason we'd get here.
      throw new RuntimeException(e);
    }

    return lines;
  }

  /**
   * Safely convert the string to uppercase.
   * @return upper case representation of the String; or null if
   * the input string is null.
   */
  public static String toUpperCase(String src) {
    if (src == null) {
      return null;
    } else {
      return src.toUpperCase();
    }
  }

  /**
   * @param dbSpecComponent a single component of a DBDescriptor spec
   * (e.g. the host or database component). The expected format of the string is:
   * <br>
   *             <center>(prefix){(digits),(digits)}(suffix)
   * </br>
   * @return a shard expansion of the given String.
   * Note that unless the pattern is matched exactly, no expansion is
   * performed and the original string is returned unaltered.
   * For example, 'db{0,1}.adz' is expanded into 'db0.adz, db1.adz'.
   * Note that this method is added to StringUtil instead of
   * DBDescriptor to better encapsulate the choice of regexp implementation.
   * @throws IllegalArgumentException if the string does not parse.
   */
  public static String expandShardNames(String dbSpecComponent)
      throws IllegalArgumentException, IllegalStateException {

    Matcher matcher = dbSpecPattern.matcher(dbSpecComponent);
    if (matcher.find()) {
      try {
        String prefix = dbSpecComponent.substring(
          matcher.start(1), matcher.end(1));
        int minShard =
          Integer.parseInt(
            dbSpecComponent.substring(
              matcher.start(2), matcher.end(2)));
        int maxShard =
          Integer.parseInt(
            dbSpecComponent.substring(
              matcher.start(3), matcher.end(3)));
        String suffix = dbSpecComponent.substring(
          matcher.start(4), matcher.end(4));
        //Log2.logEvent(prefix + " " + minShard + " " + maxShard + " " + suffix);
        if (minShard > maxShard) {
          throw new IllegalArgumentException(
            "Maximum shard must be greater than or equal to " +
            "the minimum shard");
        }
        StringBuilder tmp = new StringBuilder();
        for(int shard = minShard; shard <= maxShard; shard ++) {
          tmp.append(prefix).append(shard).append(suffix);
          if (shard != maxShard) {
            tmp.append(",");
          }
        }
        return tmp.toString();
      } catch (NumberFormatException nfex) {
        throw new IllegalArgumentException(
          "Malformed DB specification component: " + dbSpecComponent);
      }
    } else {
      return dbSpecComponent;
    }
  }

  /**
   * Returns sourceString concatenated together 'factor' times.
   *
   * @param sourceString The string to repeat
   * @param factor  The number of times to repeat it.
   */
  public static String repeat(String sourceString, int factor) {
    if (factor < 1) {
      return "";
    }
    if (factor == 1) {
      return sourceString;
    }

    StringBuilder sb = new StringBuilder(factor * sourceString.length());

    while (factor > 0) {
      sb.append(sourceString);
      factor--;
    }

    return sb.toString();
  }

  /**
  * Returns a string that is equivalent to the specified string with its
  * first character converted to uppercase as by {@link String#toUpperCase}.
  * The returned string will have the same value as the specified string if
  * its first character is non-alphabetic, if its first character is already
  * uppercase, or if the specified string is of length 0.
  *
  * <p>For example:
  * <pre>
  *    capitalize("foo bar").equals("Foo bar");
  *    capitalize("2b or not 2b").equals("2b or not 2b")
  *    capitalize("Foo bar").equals("Foo bar");
  *    capitalize("").equals("");
  * </pre>
  *
  * @param s the string whose first character is to be uppercased
  * @return a string equivalent to <tt>s</tt> with its first character
  *     converted to uppercase
  * @throws NullPointerException if <tt>s</tt> is null
  */
  public static String capitalize(String s) {
    if (s.length() == 0)
      return s;
    char first = s.charAt(0);
    char capitalized = Character.toUpperCase(first);
    return (first == capitalized)
        ? s
        : capitalized + s.substring(1);
  }
}
