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


package com.google.gdata.data.spreadsheet;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.Content;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.TextConstruct;

/**
 * Atom Entry for a single cell for the Google Spreadsheets cells feed.
 *
 * 
 * 
 */
@Kind.Term(CellEntry.KIND)
public class CellEntry
    extends BaseEntry<CellEntry> {

  /**
   * Kind category term used to label the entries that contains cell data.
   */
  public static final String KIND = Namespaces.gSpreadPrefix + "cell";

  /**
   * Category used to label entries that contain cell data.
   */
  public static final Category CATEGORY =
    new Category(Namespaces.gSpread, KIND);

  /**
   * Constructs an empty CellEntry to be populated by the XML parsing process.
   */
  public CellEntry() {
    getCategories().add(CATEGORY);
  }

  /**
   * Constructs a CellEntry that can be posted to overwrite an existing cell.
   *
   * @param row the row location of the cell
   * @param col the column location of the cell
   * @param newInputValue the string to write the cell with; if it starts with
   *        an "=" sign it is a formula, otherwise it is a literal value
   */
  public CellEntry(int row, int col, String newInputValue) {
    this(new Cell(row, col, newInputValue));
  }

  /**
   * Constructs a CellEntry with a new cell.
   */
  public CellEntry(Cell cell) {
    this();

    if (cell != null) {
      setExtension(cell);
    }
  }

  /**
   * Constructs from a copy.
   * 
   * @param sourceEntry source to copy from
   */
  public CellEntry(BaseEntry sourceEntry) {
    super(sourceEntry);
    getCategories().add(CATEGORY);
  }


  /**
   * Yields the &lt;gd:cell&gt; element that makes up this Cell Entry.
   */
  public Cell getCell() {
    return getExtension(Cell.class);
  }

  /** Throws an exception, preventing setting the server-generated field. */
  @Override
  public void setTitle(TextConstruct v) {
    throw new UnsupportedOperationException("Field is server-generated.");
  }

  /** Throws an exception, preventing setting the server-generated field. */
  @Override
  public void setContent(TextConstruct v) {
    throw new UnsupportedOperationException("Field is server-generated.");
  }

  /** Throws an exception, preventing setting the server-generated field. */
  @Override
  public void setContent(Content v) {
    throw new UnsupportedOperationException("Field is server-generated.");
  }

  /** Throws an exception, preventing setting the server-generated field. */
  @Override
  public void setSummary(TextConstruct v) {
    throw new UnsupportedOperationException("Field is server-generated.");
  }

  /**
   * Updates the cell's formula so it can be sent back to the server.
   * 
   * Note that this destroys the cell's value!
   * 
   * @param newInputValue the new input value (starts with '=' if a formula)
   */
  public void changeInputValueLocal(String newInputValue) {
    setExtension(getCell().withNewInputValue(newInputValue));
  }

  /**
   * Declares the extensions used by the XML parser in the given profile object.
   */
  public void declareExtensions(ExtensionProfile extProfile) {
    extProfile.declare(CellEntry.class, Cell.getDefaultDescription(false));
  }

  /**
   * Lets the GData server set the title.
   */
  public void setAutomaticallyGeneratedTitle(TextConstruct v) {
    state.title = v;
  }

  /**
   * Lets the GData server set the content.
   */
  public void setAutomaticallyGeneratedContent(Content v) {
    state.content = v;
  }
}
