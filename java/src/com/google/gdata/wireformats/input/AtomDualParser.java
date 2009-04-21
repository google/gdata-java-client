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
import com.google.gdata.data.IAtom;
import com.google.gdata.data.ParseSource;
import com.google.gdata.model.Element;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;

import java.io.IOException;

/**
 * The AtomDualModeParser class provides an {@link InputParser} implementation
 * that is capable of parsing into classes that use the old or new data
 * model.  The actual parsing is done by delegating to an instance of the
 * {@link AtomDataParser} or {@link ElementParser} class.
 * 
 * 
 */
public class AtomDualParser implements InputParser<IAtom> {

  /**
   * Parser to use for old data model results
   */
  private final InputParser<IAtom> dataParser = new AtomDataParser();

  /**
   * Parser to use for new data model results
   */
  private final InputParser<IAtom> elementParser =
      ElementParser.of(AltFormat.ATOM, IAtom.class);

  public AltFormat getAltFormat() {
    return AltFormat.ATOM;
  }

  public Class<IAtom> getResultType() {
    return IAtom.class;
  }

  public <R extends IAtom> R parse(ParseSource parseSource,
      InputProperties inProps, Class<R> resultClass) throws IOException,
      ServiceException {
    Preconditions.checkNotNull(parseSource, "parseSource");
    Preconditions.checkNotNull(inProps, "inProps");
    Preconditions.checkNotNull("resultClass", resultClass);

    // Use the new data model parser for Element subtypes, otherwise the old one
    if (Element.class.isAssignableFrom(resultClass)) {
      return elementParser.parse(parseSource, inProps, resultClass);
    }
    return dataParser.parse(parseSource, inProps, resultClass);
  }
}
