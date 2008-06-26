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


package sample.spreadsheet.gui;

import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.CellEntry;
import com.google.gdata.data.spreadsheet.CellFeed;
import com.google.gdata.util.ServiceException;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * Widget for displaying and editing a spreadsheet.
 *
 * This is just for demonstrative purposes, it is not very featureful.
 *
 * 
 */
public class CellBasedSpreadsheetPanel extends JPanel {

  /** The underlying Google Spreadsheets service. */
  private SpreadsheetService service;

  /** The URL of the cell feed. */
  private URL cellFeedUrl;

  private SpreadsheetTableModel model;
  private JTable table;
  private JButton refreshButton;

  /**
   * Creates the cell-based spreadsheet widget.
   *
   * @param service the Google Spreadsheets service to connect to
   * @param cellFeedUrl the URL of the cell feed
   */
  public CellBasedSpreadsheetPanel(
      SpreadsheetService service, URL cellFeedUrl) {
    this.service = service;
    this.cellFeedUrl = cellFeedUrl;
    model = new SpreadsheetTableModel();
    initializeGui();
  }

  /**
   * Sets a cell at the specified row and column.
   *
   * @return the newly set cell, returned back from the server
   */
  private CellEntry actuallySetCell(int row, int col, String valueOrFormula) {
    try {
      // This method ignores collisions.  Use update() if you are afraid
      // of overwriting other users' data.
      CellEntry entry = new CellEntry(row, col, valueOrFormula);
      return service.insert(cellFeedUrl, entry);
    } catch (IOException ioe) {
      SpreadsheetApiDemo.showErrorBox(ioe);
      return null;
    } catch (ServiceException se) {
      SpreadsheetApiDemo.showErrorBox(se);
      return null;
    }
  }

  /**
   * Gets a list of all cells.
   *
   * This just calls the method getAllCells in cellFellFeedHelper.
   */
  private CellFeed getCellFeed() {
    try {
      return service.getFeed(cellFeedUrl, CellFeed.class);
    } catch (IOException ioe) {
      SpreadsheetApiDemo.showErrorBox(ioe);
      return null;
    } catch (ServiceException se) {
      SpreadsheetApiDemo.showErrorBox(se);
      return null;
    }
  }

  // -- Mostly GUI code from here on down --

  /**
   * The Swing model for the spreadsheet.
   *
   * This is mostly GUI code.
   */
  private class SpreadsheetTableModel extends AbstractTableModel {

    /** All the cells indexed for easy display purposes. */
    private Map<Point, CellEntry> cells = new HashMap<Point, CellEntry>();

    /** The last row to display (1-based). */
    private int maxRow;

    /** The last column to display (1-based). */
    private int maxCol;


    public SpreadsheetTableModel() {
      refresh();
    }


    /**
     * Load all the cells from Google Spreadsheets.
     */
    public synchronized void refresh() {
      cells.clear();

      CellFeed cellFeed = getCellFeed();

      if (cellFeed != null) {
        for (CellEntry entry : cellFeed.getEntries()) {
          doAddCell(entry);
        }
      }

      int oldMaxRow = maxRow;
      int oldMaxCol = maxCol;

      maxRow = cellFeed.getRowCount();
      maxCol = cellFeed.getColCount();

      fireTableDataChanged();

      if (maxRow != oldMaxRow || maxCol != oldMaxCol) {
        fireTableStructureChanged();
      }
    }

    /**
     * Implements the Swing method for handling cell edits.
     */
    public void setValueAt(Object value, int screenRow, int screenCol) {
      int row = screenRow + 1; // account for the fact Swing is 0-indexed
      int col = screenCol + 1;

      // Pop up a little window to indicate that this is busy.
      JFrame statusIndicatorFrame = new JFrame();
      statusIndicatorFrame.getContentPane().add(new JButton("Updating..."));
      statusIndicatorFrame.setVisible(true);
      statusIndicatorFrame.setSize(200, 100);

      CellEntry entry = actuallySetCell(row, col, value.toString());

      if (entry != null) {
        doAddCell(entry);
      }

      statusIndicatorFrame.dispose();
      fireTableDataChanged();
    }

    /** Tells Swing how many rows are in this table. */
    public int getRowCount() {
      return maxRow; // Allow user to insert up to two rows
    }

    /** Tells Swing how many columns are in this table. */
    public int getColumnCount() {
      return maxCol;
    }

    /** Gets the cached version of the specified cell. */
    public CellEntry getCell(int row, int col) {
      return cells.get(new Point(row, col));
    }

    /** Tells Swing the value at a particular location. */
    public Object getValueAt(int screenRow, int screenCol) {
      CellEntry cell = getCell(screenRow + 1, screenCol + 1);
      if (cell == null) {
        return null;
      } else {
        return cell.getCell().getValue();
      }
    }

    public void addCell(CellEntry entry) {
      doAddCell(entry);
    }


    private void doAddCell(CellEntry entry) {
      int row = entry.getCell().getRow();
      int col = entry.getCell().getCol();

      cells.put(new Point(row, col), entry);
    }

    /** Tells Swing whether the cell is editable. */
    public boolean isCellEditable(int screenRow, int screenCol) {
      CellEntry cell = getCell(screenRow + 1, screenCol + 1);

      if (cell == null) {
        // Although this can be wrong, assume editable unless told otherwise.
        return true;
      } else {
        return cell.getEditLink() != null;
      }
    }
  }

  public static JFrame createWindow(
      SpreadsheetService service, URL cellFeedUrl) {
    JFrame frame = new JFrame();
    frame.setSize(600, 480);
    frame.getContentPane().add(new CellBasedSpreadsheetPanel(
        service, cellFeedUrl));
    frame.setVisible(true);
    frame.setTitle("Cells Demo - Positional Editing");
    return frame;
  }

  private void initializeGui() {
    table = new JTable(model);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    JScrollPane scrollpane = new JScrollPane(table);
    setLayout(new BorderLayout());
    add(scrollpane, BorderLayout.CENTER);
    refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(new ActionHandler());
    add(refreshButton, BorderLayout.SOUTH);
  }

  private class ActionHandler implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() == refreshButton) {
        model.refresh();
      }
    }
  }
}
