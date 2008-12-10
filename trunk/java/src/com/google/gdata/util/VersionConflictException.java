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


package com.google.gdata.util;

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.IEntry;

import java.io.IOException;
import java.net.HttpURLConnection;


/**
 * The VersionConflictException should be thrown by a service provider
 * when an attempt is made to modify an entry based on a stale version ID.
 *
 * 
 */
public class VersionConflictException extends ServiceException {

  public VersionConflictException() {
    super("Version conflict");
    this.currentEntry = null;
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_CONFLICT);
  }

  public VersionConflictException(IEntry currentEntry) {
    super("Version conflict");
    this.currentEntry = currentEntry;
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_CONFLICT);
  }

  public VersionConflictException(IEntry currentEntry, Throwable cause) {
    super("Version conflict", cause);
    this.currentEntry = currentEntry;
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_CONFLICT);
  }

  public VersionConflictException(HttpURLConnection httpConn)
      throws IOException {
    super(httpConn);
  }

  public VersionConflictException(ErrorDomain.ErrorCode errorCode) {
    super(errorCode);
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_CONFLICT);
  }

  public VersionConflictException(ErrorDomain.ErrorCode errorCode,
      Throwable cause) {
    super(errorCode, cause);
    setHttpErrorCodeOverride(HttpURLConnection.HTTP_CONFLICT);
  }


  /**
   * If this exception is thrown from an update operation and the entry exists,
   * but has a new version ID, this field represents the current state of the
   * entry.
   */
  private IEntry currentEntry;
  public IEntry getCurrentEntry() { return currentEntry; }
  public void setCurrentEntry(IEntry entry) { currentEntry = entry; }


  /**
   * Generates XML.
   *
   * @param   extProfile
   *            Extension profile.
   *
   * @param   xw
   *            Output writer.
   *
   * @throws  IOException
   */
  public void generate(ExtensionProfile extProfile,
                       XmlWriter xw) throws IOException {
    GenerateUtil.generateAtom(xw, currentEntry, extProfile);
  }
}
