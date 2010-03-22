// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client;

import com.google.api.data.client.DateTime;

import junit.framework.TestCase;

import java.util.Date;

/**
 * Test case for {@link DateTime}.
 */
public class DateTimeTest extends TestCase {

  public DateTimeTest() {
    super();
  }

  public DateTimeTest(String testName) {
    super(testName);
  }

  public void testEquals() {
    assertEquals(new DateTime(1234567890L), new DateTime(1234567890L, 120));
    assertTrue("Check equals with two identical tz specified.", new DateTime(
        1234567890L, -240).equals(new DateTime(1234567890L, -240)));
    assertTrue("Check equals with two different tz specified.", new DateTime(
        1234567890L, 60).equals(new DateTime(1234567890L, 240)));

    assertFalse("Check not equal.", new DateTime(1234567890L)
        .equals(new DateTime(9876543210L)));
    assertFalse("Check not equal with tz.", new DateTime(1234567890L, 120)
        .equals(new DateTime(9876543210L, 120)));
    assertFalse("Check not equal with Date.", new DateTime(1234567890L)
        .equals(new Date(9876543210L)));
  }

  public void testParseDateTime() {
    expectExceptionForParseRfc3339("");
    expectExceptionForParseRfc3339("abc");
    DateTime.parseRfc3339("2007-06-01");
    DateTime.parseRfc3339("2007-06-01T10:11:30.057");
  }

  private void expectExceptionForParseRfc3339(String input) {
    try {
      DateTime.parseRfc3339(input);
      fail("expected NumberFormatException");
    } catch (NumberFormatException e) {
      // expected
    }
  }
}
