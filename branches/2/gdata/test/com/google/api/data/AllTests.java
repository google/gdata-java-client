// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data;

import com.google.api.data.client.v2.DateTimeTest;

import junit.framework.TestSuite;

public class AllTests extends TestSuite {

  public static TestSuite suite() {
    TestSuite result = new TestSuite();
    result.addTestSuite(DateTimeTest.class);
    return result;
  }
}
