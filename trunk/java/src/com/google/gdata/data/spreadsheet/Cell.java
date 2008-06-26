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

import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.data.Extension;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.XmlParser;

import org.xml.sax.Attributes;

import java.io.IOException;
import java.util.ArrayList;


/**
 * GData schema extension describing a spreadsheet formula.
 *
 * 
 * 
 */
public class Cell implements Extension {

  /** The positional row number starting with 1  (-1 for unspecified) */
  private int row = -1;

  /** The positional column number starting with 1 (-1 for unspecified). */
  private int col = -1;
  
  /** The formula of the cell (null if the formula is omitted). */
  private String inputValue = null;
  
  /** The calculated numeric value of this cell. */
  private Number numericValue = null;

  /**
   * The evaluated value of the cell in String from (null if unspecified).
   */
  private String value = null;
  
  
  /**
   * Initializes to blank for XML parsing.
   */
  public Cell() {
  }
  
  /**
   * Constructs this Cell with all fields, but since some of these fields
   * cannot be written to the server, this constructor is private.
   * 
   * @param inRow row number
   * @param inCol column number
   * @param inNumericValue the input of the cell
   * @param inValue the result of its calculation
   */
  private Cell(int inRow, int inCol,
      String inInputValue, Number inNumericValue, String inValue) {
    row = inRow;
    col = inCol;
    inputValue = inInputValue;
    value = inValue;
    numericValue = inNumericValue;
  }
  
  /**
   * Initializes a cell where the column is known.
   * 
   * @param inRow the row number starting with 1 (-1 for unspecified)
   * @param inCol the column number starting with 1  (-1 for unspecified)
   * @param inInputValue the formula (null for unspecified)
   */
  public Cell(int inRow, int inCol, String inInputValue) {
    this(inRow, inCol, inInputValue, null, null);
  }  
  
  /**
   * Creates a cell for the server library; it is not appropriate for
   * client side use (the server may reject these cells).
   */
  public static Cell createFullCell(int inRow, int inCol,
      String inInputValue, Number inCalculatedValue, String inValue) {
    return new Cell(inRow, inCol, inInputValue,
        inCalculatedValue, inValue);
  }
  
  
  /**
   * Yields the positional row number starting with 1.
   * 
   * @return the row number, or -1 if the row is not specified
   */
  public int getRow() {
    return row;
  }

  /**
   * Yields the column number starting with 1.
   * 
   * @return the positional column number, or -1 if the column is not specified
   */
  public int getCol() {
    return col;
  }

  /**
   * Yields the formula reference of the cell.
   * 
   * 
   * An "=" sign signifies that there is a formula computed on the fly.
   * Otherwise it is simply data that is entered into the sheet.
   */
  public String getInputValue() {
    return inputValue;
  }

  /**
   * Gets the calculated numeric value.
   *
   * @return the raw numeric value, or null if it is non-numeric
   */
  public Number getNumericValue() {
    return numericValue;
  }

  /**
   * Gets the double-precision value.
   * 
   * @return the double value, or Double.NaN if no number specified
   */
  public double getDoubleValue() {
    if (numericValue == null) {
      return Double.NaN;
    } else {
      return numericValue.doubleValue();
    }
  }

  /**
   * Yields the evaluated, formatted value of this cell.
   * 
   * 
   * @return the evaluated and formatted value (null if not specified)
   */
  public String getValue() {
    return value;
  }

  /**
   * Creates a new cell with a new input value, for the purpose of updating.
   * 
   * The new cell cannot contain a calculation result value, because values
   * cannot be updated.
   * 
   * @param newInputValue the new input value, starting with '=' for a formula,
   *        otherwise just a plain string
   * @return a newly created "cell" object
   */
  public Cell withNewInputValue(String newInputValue) {
    return new Cell(row, col, newInputValue, null, null);
  }
  
  /**
   * Returns the suggested extension description.
   * @param repeats whether this cell might be repeated in parent context
   */
  public static ExtensionDescription getDefaultDescription(boolean repeats) {
    ExtensionDescription desc = new ExtensionDescription();
    desc.setExtensionClass(Cell.class);
    desc.setNamespace(Namespaces.gSpreadNs);
    desc.setLocalName("cell");
    desc.setRepeatable(repeats);
    return desc;
  }
  
  /**
   * Writes this cell as XML, omitting any unspecified fields.
   */
  public void generate(XmlWriter w, ExtensionProfile extProfile)
      throws IOException {
    ArrayList<XmlWriter.Attribute> attrs =
      new ArrayList<XmlWriter.Attribute>();

    if (row > 0) {
      attrs.add(new XmlWriter.Attribute("row", String.valueOf(row)));
    }

    if (col > 0) {
      attrs.add(new XmlWriter.Attribute("col", String.valueOf(col)));
    }
    
    if (inputValue != null) {
      attrs.add(new XmlWriter.Attribute("inputValue", inputValue));
    }

    if (numericValue != null) {
      attrs.add(new XmlWriter.Attribute("numericValue",
          numericValue.toString()));
    }
    
    w.simpleElement(Namespaces.gSpreadNs, "cell", attrs, value);
  }
  
  /**
   * Yields an XML handler for parsing a Cell element.
   */
  public XmlParser.ElementHandler getHandler(ExtensionProfile extProfile,
                                             String namespace,
                                             String localName,
                                             Attributes attrs)
      throws ParseException, IOException {
    return new Handler();
  }


  /**
   * Parser for a &lt;gd:cell&gt;.
   */
  private class Handler extends XmlParser.ElementHandler {
    
    /**
     * Initializes this instance given the extension profile.
     */
    public Handler() {
    }

    /**
     * Handles an attribute (such as row, col, inputValue).
     */
    public void processAttribute(String namespace,
                                 String localName,
                                 String attributeData)
        throws ParseException {
      if (namespace.equals("")) {
        if (localName.equals("row")) {
          Cell.this.row = Integer.parseInt(attributeData);
        } else if (localName.equals("col")) {
          Cell.this.col = Integer.parseInt(attributeData);
        } else if (localName.equals("inputValue")) {
          Cell.this.inputValue = attributeData;
        } else if (localName.equals("numericValue")) {
          try {

            // (right now their backend is entirely double anyway)
            Cell.this.numericValue = Double.valueOf(attributeData);
          } catch (NumberFormatException nfe) {
            throw new ParseException("Invalid numericValue.");
          }
        }
      }
    }
    
    /**
     * Processes the end of the tag.
     */
    public void processEndElement() throws ParseException {

      /*
       * This insidious-looking line of code copies
       * this handler's value (<gd:cell>..stuff..</gd:cell>)
       * into the cell value.
       */
      Cell.this.value = this.value;
      
      // Take empty fields and make them null.
      if (Cell.this.value != null && Cell.this.value.equals("")) {
        Cell.this.value = null;
      }
    }
  }
}

