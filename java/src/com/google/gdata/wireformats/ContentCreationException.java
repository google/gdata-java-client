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


package com.google.gdata.wireformats;

import com.google.gdata.util.ErrorContent;
import com.google.gdata.util.ServiceException;

/**
 * The ContentCreationException indicates that content cannot
 * be created. E.g. object instantiation via reflection fails.
 */
public class ContentCreationException extends ServiceException {

  public ContentCreationException(String message) {
    super(message);
  }

  public ContentCreationException(String message, Throwable cause) {
    super(message, cause);
  }

  public ContentCreationException(Throwable cause) {
    super(cause);
  }
  
  public ContentCreationException(ErrorContent errorCode) {
    super(errorCode);
  }

  public ContentCreationException(ErrorContent errorCode, Throwable cause) {
    super(errorCode, cause);
  }
}
