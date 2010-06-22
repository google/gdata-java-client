/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.client.http;

import com.google.api.client.util.ArrayMap;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.api.client.util.Objects;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests {@link UrlEncodedParser}.
 * 
 * @author Yaniv Inbar
 */
public class UrlEncodedParserTest extends TestCase {

  public UrlEncodedParserTest() {
  }

  public UrlEncodedParserTest(String name) {
    super(name);
  }

  public static class Simple {
    @Key
    String a;
    @Key
    String b;
    @Key
    String c;
    @Key
    List<String> q;

    @Override
    public boolean equals(Object obj) {
      Simple other = (Simple) obj;
      return Objects.equal(a, other.a) && Objects.equal(b, other.b)
          && Objects.equal(c, other.c) && Objects.equal(q, other.q);
    }

    public Simple() {
    }

    @Override
    public String toString() {
      return "Simple [a=" + a + ", b=" + b + ", c=" + c + ", q=" + q + "]";
    }

  }

  public static class Generic extends GenericData {
    @Key
    String a;
    @Key
    String b;
    @Key
    String c;
    @Key
    List<String> q;
  }

  public void testParse_simple() {
    Simple actual = new Simple();
    UrlEncodedParser.parse("q=1&a=x&b=y&c=z&q=2", actual);
    Simple expected = new Simple();
    expected.a = "x";
    expected.b = "y";
    expected.c = "z";
    expected.q = new ArrayList<String>(Arrays.asList(new String[] {"1", "2"}));
    assertEquals(expected, actual);
  }

  public void testParse_generic() {
    Generic actual = new Generic();
    UrlEncodedParser.parse("p=4&q=1&a=x&p=3&b=y&c=z&d=v&q=2&p=5", actual);
    Generic expected = new Generic();
    expected.a = "x";
    expected.b = "y";
    expected.c = "z";
    expected.q = new ArrayList<String>(Arrays.asList(new String[] {"1", "2"}));
    expected.set("d", "v");
    expected.set("p", new ArrayList<String>(Arrays.asList(new String[] {"4",
        "3", "5"})));
    assertEquals(expected, actual);
  }

  public void testParse_map() {
    ArrayMap<String, Object> actual = new ArrayMap<String, Object>();
    UrlEncodedParser.parse("p=4&q=1&a=x&p=3&b=y&c=z&d=v&q=2&p=5", actual);
    ArrayMap<String, Object> expected = ArrayMap.create();
    expected.add("p", new ArrayList<String>(Arrays.asList(new String[] {"4",
        "3", "5"})));
    expected.add("q", new ArrayList<String>(Arrays.asList(new String[] {"1",
    "2"})));
    expected.add("a", "x");
    expected.add("b", "y");
    expected.add("c", "z");
    expected.add("d", "v");
    assertEquals(expected, actual);
  }
}
