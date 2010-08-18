
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


package com.google.gdata.util.httputil;

import com.google.gdata.util.common.base.StringUtil;
import com.google.common.collect.Lists;
import com.google.gdata.util.parser.Chset;
import com.google.gdata.util.parser.Parser;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class has been <b>deprecated</b>; use {@link
 * com.google.gdata.util.common.net.UriParameterMap#parse(String,java.nio.charset.Charset)}
 * Utility for parsing application/x-www-form-urlencoded content as
 * sent by user agents in the query string of GET form submissions and
 * the body of POST form submissions.
 *
 * 
 */
public class FormUrlDecoder {
  private static Parser<Result> parser;

  private static class Result {
    LinkedList<Parameter> params;
    String encoding;
    public Result(String encoding) {
      this.params = Lists.newLinkedList();
      this.encoding = (encoding == null) ? "ISO-8859-1" : encoding;
    }
  }
  
  private FormUrlDecoder() {
  }
  
  /**
   * @deprecated Do not use. (Currently only needed internally by
   * UriParameterMap.)
   */
  @Deprecated
  public interface Callback {
    void handleParameter(String name, String value);
  }

  /**
   * @deprecated Do not use. (Currently only needed internally by
   * UriParameterMap.)
   */
  @Deprecated
  public static void parseWithCallback(String str, String encoding,
      Callback callback) {
    if (StringUtil.isEmpty(str)) {
      return;
    }
    Result result = new Result(encoding);
    parser.parse(str, result);
    for (Parameter param : result.params) {
      callback.handleParameter(param.name, param.value);
    }
  }

  /**
   * @deprecated Use {@code UriParameterMap.parse(query, encoding)}. To convert
   * from the string to the {@link java.nio.charset.Charset} instance, see
   * {@link com.google.gdata.util.common.base.Charsets}. Note that an encoding of null
   * will default to ISO-8859-1 in this deprecated implementation, whereas the
   * classes in common.net default to UTF-8 encoding.
   */
  @Deprecated
  public static ParamMap parse(String str, ParamMap map, String encoding) {
    // If the parameters string is empty, we shouldn't add a key/value pair of 
    // empty strings.
    if ("".equals(str)) {
      return map == null ? new ParamMap() : map;
    }
    
    final AtomicReference<ParamMap> outMap = new AtomicReference<ParamMap>(map);
    parseWithCallback(str, encoding, new Callback() {
        public void handleParameter(String name, String value) {
          ParamMap map = outMap.get();
          if (map == null) {
            map = new ParamMap();
            outMap.set(map);
          }
          map.append(name, value);
        }
      });
    return outMap.get();
  }

  /**
   * Returns the canonical name for the specified charset.
   *
   * @param charset Some known alias for a charset
   * @return The canonical name for the charset, or the original alias
   * if no mapping was found
   */
  private static String getCanonicalEncodingName(String charset) {
    String canonicalName = charset;
    if (charset != null && charset.length() > 0) {
      try {
        canonicalName = Charset.forName(charset).name();
      } catch (UnsupportedCharsetException uce) {
        // just return the alias
      } catch (IllegalCharsetNameException ice) {
        // just return the alias
      }
    }
    return canonicalName;
  }


  /**
   * URL decodes the section of {@code buf} from {@code start} (inclusive)
   * to {@code end} (exclusive) using the given {@code encoding}. It correctly
   * handles improperly URL encoded strings for character sets in which
   * ascii bytes do not always indicate ascii characters.
   */
  private static String decodeString(char[] buf, int start, int end,
                                     String encoding) {
    String str = new String(buf, start, end - start);
    try {
      if (requiresByteLevelDecoding(encoding)) {
        // Java's specification of URLEncoding states that non-ascii-alphanum
        // characters should be represented by a URL encoded sequence of bytes.
        // Thus, the proper way perform url encoding is to at the string level,
        // encoding all non-ascii-alphanum characters to a url encoded sequence
        // of bytes according to the character set. However, most browsers
        // implement URLEncoding improperly, encoding the entire string to bytes
        // first, and then URL-escaping all of the non-ascii bytes. The two
        // behaviors work fine for UTF-8, because ascii bytes in UTF-8
        // correspond to ascii characters. However, a problem arises for
        // encodings in which ascii bytes can be part of a byte representation
        // of non-ascii characters. It is these encodings for which we have to
        // url decode directly to the byte level, and then encode the bytes with
        // the given encoding. To perform this byte level decoding, we pivot
        // through ISO-8859-1, the encoding which treats all single bytes
        // as their corresponding character values.
        byte[] rawBytes =
            URLDecoder.decode(str, "ISO-8859-1").getBytes("ISO-8859-1");
        
        return new String(rawBytes, encoding);
      }
      return URLDecoder.decode(str, encoding);
    } catch (IllegalArgumentException iae) {
      // According to the javadoc of URLDecoder, when the input string is
      // illegal, it could either leave the illegal characters alone or throw
      // an IllegalArgumentException! To deal with both consistently, we
      // ignore IllegalArgumentException and just return the original string.
      return str;
    } catch (UnsupportedEncodingException e) {
      return str;
    }
  }

  /**
   * Charsets for which a byte with an ascii value does not necessarily map
   * to the corresponding ascii character.
   */
  private static boolean requiresByteLevelDecoding(String encoding) {
    encoding = getCanonicalEncodingName(encoding).toUpperCase();
    // Use endsWith() to include our wrapper character sets, whose names are
    // of the form "X-Variant-Shift_JIS" or "X-Variant-windows-31j".
    return (encoding.endsWith("SHIFT_JIS") ||
            encoding.endsWith("WINDOWS-31J"));
  }

  private static class NameAction
      implements com.google.gdata.util.parser.Callback<Result> {
    public void handle(char[] buf, int start, int end, Result result) {
      Parameter param = new Parameter();
      param.name = decodeString(buf, start, end, result.encoding);
      result.params.addLast(param);
    }
  }

  private static class ValueAction
      implements com.google.gdata.util.parser.Callback<Result> {
    public void handle(char[] buf, int start, int end, Result result) {
      Parameter param = result.params.getLast();
      param.value = decodeString(buf, start, end, result.encoding);
    }
  }

  private static class Parameter {
    String name = null;
    String value = "";
  }

  static {
    Chset nameToken = Chset.difference(Chset.ANYCHAR, new Chset("&="));
    Chset valueToken = Chset.difference(Chset.ANYCHAR, new Chset("&"));
    Parser<Result> name = nameToken.star().action(new NameAction());
    Parser<Result> value = valueToken.plus().action(new ValueAction());
    value = value.optional();
    value = Parser.sequence(new Chset('='), value);

    Parser<Result> parameter = Parser.sequence(name, value.optional());
    parser = parameter.list(new Chset('&')).optional();
  }
}
