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

package com.google.api.client.util;

import com.google.api.client.util.Objects;

import junit.framework.TestCase;

/**
 * Tests {@link Objects}.
 * 
 * @author Yaniv Inbar
 */
public class ObjectsTest extends TestCase {

  public ObjectsTest() {
  }

  public ObjectsTest(String testName) {
    super(testName);
  }

  public void testEqual() {
    assertTrue(Objects.equal(null, null));
    assertTrue(Objects.equal("a", "a"));
    assertFalse(Objects.equal(null, "a"));
    assertFalse(Objects.equal("a", null));
    assertFalse(Objects.equal("a", "b"));
  }
}
