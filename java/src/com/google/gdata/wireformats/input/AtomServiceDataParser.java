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
import com.google.gdata.data.XmlEventSource;
import com.google.gdata.data.introspection.ServiceDocument;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;

import java.io.IOException;
import java.io.Reader;

/**
 * The AtomServiceDataParser class provides an {@link InputParser}
 * implementation for Atom Service Documents.
 *
 * 
 */
public class AtomServiceDataParser extends XmlInputParser<ServiceDocument> {

  /**
   * Constructs a new AtomServiceDataParser.
   */
  public AtomServiceDataParser() {
    super(AltFormat.ATOM_SERVICE, ServiceDocument.class);
  }

  @Override
  protected <R extends ServiceDocument> R parse(XmlEventSource eventSource, 
      InputProperties inProps, Class<R> resultClass) {
    throw new IllegalStateException(
        "Parsing from XmlEventSource not supported");
  }

  @Override
  public <R extends ServiceDocument> R  parse(Reader inputReader, 
      InputProperties inProps, Class<R> resultClass) 
      throws IOException, ServiceException {
    Preconditions.checkNotNull(inProps.getExtensionProfile(),
    "No extension profile");

    R serviceDoc = createResult(resultClass);
    serviceDoc.parse(inProps.getExtensionProfile(), inputReader);
    return serviceDoc;
  }
}
