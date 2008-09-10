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

package com.google.gdata.util.common.annotations;

/**
 * An annotation that indicates that the visibility of a type or member has
 * been relaxed to make the code testable. The {@link #productionVisibility()}
 * field indicates the visibility applicable to production code.
 *
 * 
 */
public @interface VisibleForTesting {

  /**
   * The possible set of visibility values.
   */
  public static final class Visibility {
    /** Equivalent to no modifier */
    public static final int PACKAGE_PRIVATE = 0;
    /** Equivalent to java.lang.reflect.Modifier.PRIVATE */
    public static final int PRIVATE = 0x00000002;
    /** Equivalent to java.lang.reflect.Modifier.PROTECTED */
    public static final int PROTECTED = 0x00000004;

    private Visibility() {}
  }

  /**
   * The visibility the annotated type or member would have if it did not need
   * to be made visible for testing. E.g. {@link Visibility#PROTECTED}.
   *
   * <p>The default production visibility is private, and so, statements like
   * {@code @VisibleForTesting(productionVisibility = Visibility.PRIVATE)}
   * can always be replaced with the more terse {@code @VisibleForTesting}.
   */
  int productionVisibility() default Visibility.PRIVATE;
}
