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


package com.google.gdata.wireformats.input;

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.ContentCreationException;

/**
 * The AbstractParser class is an abstract base class that can be used in the
 * creation of new {@link InputParser} implementations.   It provides the
 * basic storage and getters for the alternate representation and result type
 * and utility code for constructing new result type implementation instances.
 * 
 * 
 */
public abstract class AbstractParser<T> implements InputParser<T> {
  
  protected final AltFormat altFormat;
  protected final Class<? extends T> resultType;
  
  /**
   * Constructs a new AbstractParser instance for the specified representation
   * and result type.
   * 
   * @param altFormat parsed alternate representation
   * @param resultType type of result
   */
  protected AbstractParser(AltFormat altFormat, 
      Class<? extends T> resultType) {
    Preconditions.checkNotNull(altFormat, "altFormat");
    Preconditions.checkNotNull(resultType, "resultType");
    this.altFormat = altFormat;
    this.resultType = resultType;
  }
  
  public AltFormat getAltFormat() {
    return altFormat;
  }

  public Class<? extends T> getResultType() {
    return resultType;
  }

  /**
   * Creates a new result object instance using the result type passed to
   * the constructor.   This form can be used by subclasses where the result
   * type is a concrete implementation class.
   * 
   * @return result instance
   * @throws ContentCreationException
   */
  protected T createResult() throws ContentCreationException {
    return createResult(resultType);
  }

  /**
   * Creates a new result object instance using the provided result
   * implementation class. This form can be used by subclasses where the result
   * type is an interface or where results may be an implementation class that
   * is a subclass of the result type.
   * 
   * @param <R> type of the created result
   * @param resultImplClass Result implementation class. The type must implement
   *        the result type of the parser and provide a null argument
   *        constructor.
   * @return result instance
   * @throws ContentCreationException if unable to create result object
   */
  protected <R extends T> R createResult(Class<R> resultImplClass)
      throws ContentCreationException {
    try {
      return resultImplClass.newInstance();
    } catch (IllegalAccessException iae) {
      throw new IllegalStateException("Can't create parse target", iae);
    } catch (InstantiationException ie) {
      throw new IllegalStateException("Can't create parse target", ie);
    }
  }
}
