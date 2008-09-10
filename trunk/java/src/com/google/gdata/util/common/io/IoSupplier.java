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


package com.google.gdata.util.common.io;

import java.io.IOException;

/**
 * A class that can supply objects of a single type. Similar to
 * {@link com.google.gdata.util.common.base.Supplier} except that the act of supplying may
 * throw an {@code IOException}.
 *
 * 
 * @param <T> the type of object being supplied
 */
public interface IoSupplier<T> {
  T get() throws IOException;
}
