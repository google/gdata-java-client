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

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Tests {@link GenericUrl}.
 * 
 * @author Yaniv Inbar
 */
@SuppressWarnings("deprecation")
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
      "https://www.google.com:223/m8/feeds/contacts/someone=%23%25&%20%3F%3Co%3E%7B%7D@gmail.com/"
          + "full?"
          + "foo=bar&"
          + "alt=json&"
          + "max-results=3&"
          + "prettyprint=true&" + "q=Go%3D%23/%25%26%20?%3Co%3Egle";

  public void testFull_build() {
    TestUrl url = new TestUrl();
    url.scheme = "https";
    url.host = "www.google.com";
    url.port = 223;
    url.path = "/m8/feeds/contacts/someone=#%& ?<o>{}@gmail.com/full";
    url.set("alt", "json");
    url.set("max-results", 3);
    url.set("prettyprint", true);
    url.set("q", "Go=#/%& ?<o>gle");
    url.foo = "bar";
    url.hidden = "invisible";
    assertEquals(FULL, url.build());
  }

  public void testFull_parse() {
    TestUrl url = new TestUrl(FULL);
    assertEquals("https", url.scheme);
    assertEquals("www.google.com", url.host);
    assertEquals(223, url.port);
    assertEquals("/m8/feeds/contacts/someone=#%& ?<o>{}@gmail.com/full",
        url.path);
    assertEquals("json", url.get("alt"));
    assertEquals("3", url.get("max-results"));
    assertEquals("true", url.get("prettyprint"));
    assertEquals("Go=#/%& ?<o>gle", url.get("q"));
    assertNull(url.hidden);
    assertEquals("bar", url.foo);
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
      "foo://bar?B=true&D=-3.14&I=-3&b=true&d=-3.14&i=-3&s=a&a=b";

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

  private static String FRAGMENT1 =
      "foo://bar/path/to/resource#fragme=%23/%25&%20?%3Co%3Ent";

  public void testFragment1_build() {
    GenericUrl url = new GenericUrl();
    url.scheme = "foo";
    url.host = "bar";
    url.path = "/path/to/resource";
    url.fragment = "fragme=#/%& ?<o>nt";
    assertEquals(FRAGMENT1, url.build());
  }

  public void testFragment1_parse() {
    GenericUrl url = new GenericUrl(FRAGMENT1);
    assertEquals("foo", url.scheme);
    assertEquals("bar", url.host);
    assertEquals("/path/to/resource", url.path);
    assertEquals("fragme=#/%& ?<o>nt", url.fragment);
  }

  private static String FRAGMENT2 = "foo://bar/path/to/resource?a=b#fragment";

  public void testFragment2_build() {
    GenericUrl url = new GenericUrl();
    url.scheme = "foo";
    url.host = "bar";
    url.path = "/path/to/resource";
    url.set("a", "b");
    url.fragment = "fragment";
    assertEquals(FRAGMENT2, url.build());
  }

  public void testFragment2_parse() {
    GenericUrl url = new GenericUrl(FRAGMENT2);
    assertEquals("foo", url.scheme);
    assertEquals("bar", url.host);
    assertEquals("/path/to/resource", url.path);
    assertEquals("b", url.get("a"));
    assertEquals("fragment", url.fragment);
  }

  private static final String PATH_WITH_SLASH =
      "http://www.google.com/m8/feeds/contacts/someone%2Fis%2F@gmail.com/full/";

  private static final List<String> PATH_WITH_SLASH_PARTS =
      Arrays.asList(new String[] {"", "m8", "feeds", "contacts",
          "someone/is/@gmail.com", "full", ""});

  public void testPathWithSlash_build() {
    GenericUrl url = new GenericUrl();
    url.scheme = "http";
    url.host = "www.google.com";
    url.pathParts = PATH_WITH_SLASH_PARTS;
    assertEquals(PATH_WITH_SLASH, url.build());
  }

  public void testPathWithSlash_parse() {
    GenericUrl url = new GenericUrl(PATH_WITH_SLASH);
    assertEquals("http", url.scheme);
    assertEquals("www.google.com", url.host);
    assertEquals(PATH_WITH_SLASH_PARTS, url.pathParts);
  }

  public void testToPathParts() {
    subtestToPathParts(null, (String[]) null);
    subtestToPathParts(null, "");
    subtestToPathParts("/", "", "");
    subtestToPathParts("a", "a");
    subtestToPathParts("/a", "", "a");
    subtestToPathParts("/a/", "", "a", "");
    subtestToPathParts("path/to/resource", "path", "to", "resource");
    subtestToPathParts("/path/to/resource", "", "path", "to", "resource");
    subtestToPathParts("/path/to/resource/", "", "path", "to", "resource", "");
    subtestToPathParts("/Go%3D%23%2F%25%26%20?%3Co%3Egle/2nd", "",
        "Go=#/%& ?<o>gle", "2nd");
  }

  private void subtestToPathParts(String encodedPath,
      String... expectedDecodedParts) {
    List<String> result = GenericUrl.toPathParts(encodedPath);
    if (encodedPath == null) {
      assertNull(result);
    } else {
      assertEquals(Arrays.asList(expectedDecodedParts), result);
    }
  }

  public void testAppendPath() {
    GenericUrl url = new GenericUrl("http://google.com");
    assertNull(url.pathParts);
    url.appendRawPath(null);
    assertNull(url.pathParts);
    url.appendRawPath("");
    assertNull(url.pathParts);
    url.appendRawPath("/");
    assertEquals(Arrays.asList(new String[] {"", ""}), url.pathParts);
    url.appendRawPath("/");
    assertEquals(Arrays.asList(new String[] {"", "", ""}), url.pathParts);
    url.appendRawPath("/a");
    assertEquals(Arrays.asList(new String[] {"", "", "", "a"}), url.pathParts);
    url.appendRawPath("b");
    assertEquals(Arrays.asList(new String[] {"", "", "", "ab"}), url.pathParts);
    url.appendRawPath("c/d");
    assertEquals(Arrays.asList(new String[] {"", "", "", "abc", "d"}),
        url.pathParts);
    url.appendRawPath("/e");
    assertEquals(Arrays.asList(new String[] {"", "", "", "abc", "d", "e"}),
        url.pathParts);
    url.appendRawPath("/");
    assertEquals(Arrays.asList(new String[] {"", "", "", "abc", "d", "e", ""}),
        url.pathParts);
  }

  private static final String PREFIX = "https://www.googleapis.com";

  private static final String REPEATED_PARAM_PATH = "/latitude/v1/location";

  private static final String REPEATED_PARAM =
      PREFIX + REPEATED_PARAM_PATH + "?q=c&q=a&q=b";

  public void testRepeatedParam_build() {
    GenericUrl url = new GenericUrl();
    url.scheme = "https";
    url.host = "www.googleapis.com";
    url.setRawPath(REPEATED_PARAM_PATH);
    url.set("q", Arrays.asList(new String[] {"c", "a", "b"}));
    assertEquals(REPEATED_PARAM, url.build());
  }

  public void testRepeatedParam_parse() {
    GenericUrl url = new GenericUrl(REPEATED_PARAM);
    assertEquals("https", url.scheme);
    assertEquals("www.googleapis.com", url.host);
    assertEquals(REPEATED_PARAM_PATH, url.getRawPath());
    Collection<?> q = (Collection<?>) url.get("q");
    Iterator<?> i = q.iterator();
    assertEquals("c", i.next());
    assertEquals("a", i.next());
    assertEquals("b", i.next());
    assertFalse(i.hasNext());
  }

  public void testBuild_noValue() {
    GenericUrl url = new GenericUrl();
    url.scheme = "https";
    url.host = "www.googleapis.com";
    url.setRawPath(REPEATED_PARAM_PATH);
    url.set("noval", "");
    assertEquals(PREFIX + REPEATED_PARAM_PATH + "?noval", url.build());
  }
}
