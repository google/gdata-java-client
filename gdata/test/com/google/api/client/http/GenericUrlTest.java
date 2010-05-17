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

import com.google.api.client.util.Key;

import junit.framework.TestCase;

/**
 * Tests {@link GenericUrl}.
 * 
 * @author Yaniv Inbar
 */
public class GenericUrlTest extends TestCase {

  public GenericUrlTest() {
  }

  public GenericUrlTest(String name) {
    super(name);
  }

  private static String MINIMAL = "foo://bar";

  public void testMinimal_build() {
    GenericUrl url = new GenericUrl();
    url.scheme = "foo";
    url.host = "bar";
    assertEquals(MINIMAL, url.build());
  }

  public void testMinimal_parse() {
    GenericUrl url = new GenericUrl(MINIMAL);
    assertEquals("foo", url.scheme);
  }

  private static String NO_PATH = "foo://bar?a=b";

  public void testNoPath_build() {
    GenericUrl url = new GenericUrl();
    url.scheme = "foo";
    url.host = "bar";
    url.set("a", "b");
    assertEquals(NO_PATH, url.build());
  }

  public void testNoPath_parse() {
    GenericUrl url = new GenericUrl(NO_PATH);
    assertEquals("foo", url.scheme);
    assertEquals("bar", url.host);
    assertEquals("b", url.get("a"));
  }

  private static String SHORT_PATH = "foo://bar/path?a=b";

  public void testShortPath_build() {
    GenericUrl url = new GenericUrl();
    url.scheme = "foo";
    url.host = "bar";
    url.path = "/path";
    url.set("a", "b");
    assertEquals(SHORT_PATH, url.build());
  }

  public void testShortPath_parse() {
    GenericUrl url = new GenericUrl(SHORT_PATH);
    assertEquals("foo", url.scheme);
    assertEquals("bar", url.host);
    assertEquals("/path", url.path);
    assertEquals("b", url.get("a"));
  }

  private static String LONG_PATH = "foo://bar/path/to/resource?a=b";

  public void testLongPath_build() {
    GenericUrl url = new GenericUrl();
    url.scheme = "foo";
    url.host = "bar";
    url.path = "/path/to/resource";
    url.set("a", "b");
    assertEquals(LONG_PATH, url.build());
  }

  public void testLongPath_parse() {
    GenericUrl url = new GenericUrl(LONG_PATH);
    assertEquals("foo", url.scheme);
    assertEquals("bar", url.host);
    assertEquals("/path/to/resource", url.path);
    assertEquals("b", url.get("a"));
  }

  public static class TestUrl extends GenericUrl {
    @Key
    String foo;

    public String hidden;

    public TestUrl() {
    }

    public TestUrl(String encodedUrl) {
      super(encodedUrl);
    }
  }

  private static String FULL =
      "https://www.google.com:223/m8/feeds/contacts/someone%20%3F@gmail.com/"
          + "full?" + "alt=json&" + "foo=bar&" + "max-results=3&"
          + "prettyprint=true&" + "q=Go%26%20?%3Co%3Egle";

  public void testFull_build() {
    TestUrl url = new TestUrl();
    url.scheme = "https";
    url.host = "www.google.com";
    url.port = 223;
    url.path = "/m8/feeds/contacts/someone ?@gmail.com/full";
    url.set("alt", "json");
    url.set("max-results", 3);
    url.set("prettyprint", true);
    url.set("q", "Go& ?<o>gle");
    url.foo = "bar";
    url.hidden = "invisible";
    assertEquals(FULL, url.build());
  }

  public void testFull_parse() {
    System.out.println(FULL.charAt(52));
    TestUrl url = new TestUrl(FULL);
    assertEquals("https", url.scheme);
    assertEquals("www.google.com", url.host);
    assertEquals(223, url.port);
    assertEquals("/m8/feeds/contacts/someone ?@gmail.com/full", url.path);
    assertEquals("json", url.get("alt"));
    assertEquals("3", url.get("max-results"));
    assertEquals("true", url.get("prettyprint"));
    assertEquals("Go& ?<o>gle", url.get("q"));
    assertNull(url.hidden);
    assertEquals("bar", url.foo);
    assertEquals(FULL, url.build());
  }

  public static class FieldTypesUrl extends GenericUrl {

    @Key
    Boolean B;

    @Key
    Double D;

    @Key
    Integer I;

    @Key
    boolean b;

    @Key
    double d;

    @Key
    int i;

    @Key
    String s;

    String hidden;

    FieldTypesUrl() {
    }

    FieldTypesUrl(String encodedUrl) {
      super(encodedUrl);
    }
  }

  private static String FIELD_TYPES =
      "foo://bar?B=true&D=-3.14&I=-3&a=b&b=true&d=-3.14&i=-3&s=a";

  public void testFieldTypes_build() {
    FieldTypesUrl url = new FieldTypesUrl();
    url.scheme = "foo";
    url.host = "bar";
    url.set("a", "b");
    url.B = true;
    url.D = -3.14;
    url.I = -3;
    url.b = true;
    url.d = -3.14;
    url.i = -3;
    url.s = "a";
    url.hidden = "notHere";
    assertEquals(FIELD_TYPES, url.build());
  }

  public void testFieldTypes_parse() {
    FieldTypesUrl url = new FieldTypesUrl(FIELD_TYPES);
    assertEquals("foo", url.scheme);
    assertEquals("bar", url.host);
    assertEquals("b", url.get("a"));
    assertNull(url.hidden);
    assertEquals(true, url.b);
    assertEquals(Boolean.TRUE, url.B);
    assertEquals(-3.14d, url.d, 1e-5d);
    assertEquals(-3.14d, url.D.doubleValue(), 1e-5d);
    assertEquals(-3, url.i);
    assertEquals(-3, url.I.intValue());
    assertEquals("a", url.s);
  }

}
