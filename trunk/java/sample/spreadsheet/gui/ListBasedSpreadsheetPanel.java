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

import com.google.gdata.client.spreadsheet.ListQuery;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.TextContent;
import com.google.gdata.data.spreadsheet.CustomElementCollection;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.VersionConflictException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

/**
 * Widget for displaying and editing a spreadsheet.
 *
 * This is just for demonstrative purposes, it is not very featureful.
 *
 * 
 */
public class ListBasedSpreadsheetPanel extends JPanel {

  /** The Google Spreadsheets service. */
  private SpreadsheetService service;

  /** The list feed URL. */
  private URL listFeedUrl;

  /** The model that Swing uses to represent the table. */
  private ListTableModel model;

  /** The table widget. */
  private JTable table;

  private JButton refreshButton;
  private JButton deleteOneButton;
  private JButton revertOneButton;
  private JButton commitOneButton;
  private JButton commitAllButton;

  private JTextField fulltextField;
  private JTextField spreadsheetQueryField;
  private JTextField orderbyField;

  /**
   * Creates a list-based spreadsheet editing panel.
   * @param service the spreadsheet service
   * @param listFeedUrl the URL of the list feed
   */
  public ListBasedSpreadsheetPanel(
      SpreadsheetService service, URL listFeedUrl) {
    this.service = service;
    this.listFeedUrl = listFeedUrl;
    model = new ListTableModel();
    initializeGui();
  }

  /**
   * Refreshes the contents of the table, applying any queries the user
   * specifies.
   */
  private void refreshFromServer() {
    try {
      ListQuery query = new ListQuery(listFeedUrl);

      if (!fulltextField.getText().equals("")) {
        query.setFullTextQuery(fulltextField.getText());
      }
      if (!spreadsheetQueryField.getText().equals("")) {
        query.setSpreadsheetQuery(spreadsheetQueryField.getText());
      }
      if (!orderbyField.getText().equals("")) {
        query.setOrderBy(orderbyField.getText());
      }

      ListFeed feed = service.query(query, ListFeed.class);

      model.resetEntries(feed.getEntries());
    } catch (ServiceException e) {
      SpreadsheetApiDemo.showErrorBox(e);
    } catch (IOException e) {
      SpreadsheetApiDemo.showErrorBox(e);
    }
  }

  /**
   * This class models one particular row in the list, tracking both its old
   * contents and its new contents.
   *
   * This is responsible for the "commit/revert/delete" behavior that you
   * see in the GUI.
   */
  private class ListEntryModel {

    /**
     * The original entry downloaded.
     * If this is an entry to add, originalEntry will be null.
     */
    private ListEntry originalEntry = null;

    /**
     * The new contents to add.
     * If this is not being edited, this will be null.
     */
    private CustomElementCollection newContents = null;

    /** Makes an existing row to be edited. */
    public ListEntryModel(ListEntry originalEntry) {
      this.originalEntry = originalEntry;
    }

    /** Creates a blank row to be edited. */
    public ListEntryModel() {
    }

    /** Gets the visible contents of a particular column. */
    public String getContents(String column) {
      String result = null;

      if (newContents != null) {
        result = newContents.getValue(column);
      } else if (originalEntry != null) {
        result = originalEntry.getCustomElements().getValue(column);
      }

      if (result == null) {
        result = "";
      }

      return result;
    }

    /** Gets whether this row neither has data nor is being edited. */
    public boolean isBlank() {
      return originalEntry == null && newContents == null;
    }

    /** Gets whether this row is oepn for edit. */
    public boolean isBeingEdited() {
      return newContents != null;
    }

    /** Open the row up for editing. */
    public void startEdit() {
      newContents = new CustomElementCollection();
      if (originalEntry != null) {
        newContents.replaceWithLocal(originalEntry.getCustomElements());
      }
    }

    /** Sets the contents of a particular cell. */
    public void setContents(String column, String newText) {
      if (!isBeingEdited()) {
        startEdit();
      }

      newContents.setValueLocal(column, newText);
    }

    /** Loses all changes. */
    public void revert() {
      newContents = null;
    }

    /** Actually adds this new entry to the spreadsheet. */
    private void doAddNew() throws ServiceException, IOException {
      ListEntry newEntry = new ListEntry();
      newEntry.getCustomElements().replaceWithLocal(newContents);
      originalEntry = service.insert(listFeedUrl, newEntry);
      newContents = null;
    }

    /** Actually updates an existing entry on the spreadsheet,
     * checking for edit conflicts. */
    private void doUpdateExisting() throws ServiceException, IOException {
      try {
        originalEntry.getCustomElements().replaceWithLocal(newContents);
        originalEntry = originalEntry.update();
        newContents = null;
      } catch (VersionConflictException e) {
        originalEntry = originalEntry.getSelf();
        TextContent content = (TextContent) originalEntry.getContent();
        JOptionPane.showMessageDialog(null,
            "Someone has edited the row in the meantime to:\n"
            + originalEntry.getTitle().getPlainText()
            + " (" + content.getContent().getPlainText() + ")\n"
            + "Commit again to confirm.",
            "Version Conflict",
            JOptionPane.WARNING_MESSAGE);
      }
    }

    /** Commits all changes. */
    public void commit() {
      if (isBeingEdited()) {
        boolean success = false;

        try {
          if (originalEntry != null) {
            doUpdateExisting();
          } else {
            doAddNew();
          }

          success = true;
        } catch (ServiceException e) {
          SpreadsheetApiDemo.showErrorBox(e);
        } catch (IOException e) {
          SpreadsheetApiDemo.showErrorBox(e);
        }
      }
    }

    /** Deletes this entry from the backend, but not from the list. */
    public void delete() {
      if (originalEntry != null) {
        try {
          originalEntry.delete();
        } catch (ServiceException e) {
          SpreadsheetApiDemo.showErrorBox(e);
        } catch (IOException e) {
          SpreadsheetApiDemo.showErrorBox(e);
        }
      }
      originalEntry = null;
      newContents = null;
    }
  }

  /**
   * The Swing model for the spreadsheet.
   *
   * This is mostly GUI code.
   */
  private class ListTableModel extends AbstractTableModel {

    /** The name of each column (in the XML). */
    private List<String> columnNames = new ArrayList<String>();

    /** All entries of the list. */
    private List<ListEntryModel> list = new ArrayList<ListEntryModel>();


    /**
     * Resets all the entries from a new list.
     *
     * This also tries to figure out what the set of valid columns is.
     */
    public synchronized void resetEntries(List<ListEntry> entries) {
      TreeSet<String> columnSet = new TreeSet<String>();

      list.clear();
      columnNames.clear();

      for (ListEntry entry : entries) {
        list.add(new ListEntryModel(entry));
        columnSet.addAll(entry.getCustomElements().getTags());
      }

      // Always have an empty row to edit.
      list.add(new ListEntryModel());

      columnNames.add("(Edit)");
      columnNames.addAll(columnSet);

      fireTableStructureChanged();
      fireTableDataChanged();
    }

    /** Writes back all modified entries to the actual spreadsheet. */
    public synchronized void commitAll() {
      for (ListEntryModel entryModel : list) {
        entryModel.commit();
      }
      fireTableDataChanged();
    }

    /** Reverts all entries, losing all changes. */
    public synchronized void revertAll() {
      for (ListEntryModel entryModel : list) {
        entryModel.revert();
      }
      fireTableDataChanged();
    }

    /** Delete one entry by index. */
    public synchronized void deleteOne(int row) {
      if (!list.get(row).isBeingEdited()) {
        list.get(row).delete();
        list.remove(row);
        fireTableRowsDeleted(row, row);
      }
    }

    /** Commit one entry by index. */
    public synchronized void commitOne(int row) {
      if (row >= 0 && row < list.size()) {
        if (list.get(row).isBeingEdited()) {
          list.get(row).commit();
          fireTableRowsUpdated(row, row);
        }
      }
    }

    /** Revert one entry by index. */
    public synchronized void revertOne(int row) {
      if (list.get(row).isBeingEdited()) {
        list.get(row).revert();
        fireTableRowsUpdated(row, row);
      }
    }

    /** Gets whether this is the special column. */
    private boolean isSpecialColumn(int col) {
      return col == 0;
    }

    /**
     * Implements the Swing method for handling cell edits.
     */
    public synchronized void setValueAt(Object value, int row, int col) {
      ListEntryModel entryModel = list.get(row);

      if (isSpecialColumn(col)) {
        setRowEditing(row, ((Boolean) value).booleanValue());
      } else {
        setRowEditing(row, true);
        entryModel.setContents(columnNames.get(col), value.toString());
        fireTableCellUpdated(row, col);
      }
    }

    /** Sets whether a row is being edited. */
    private void setRowEditing(int row, boolean edit) {
      ListEntryModel entryModel = list.get(row);

      if (edit && !entryModel.isBeingEdited()) {
        if (entryModel.isBlank()) {
          // Always have at least two blank rows.
          list.add(new ListEntryModel());
          fireTableRowsInserted(list.size() - 1, list.size() - 1);
        }
        entryModel.startEdit();
        fireTableRowsUpdated(row, row);
      } else if (!edit) {
        commitOne(row);
      }
    }

    /** Tells Swing the value at a particular location. */
    public synchronized Object getValueAt(int row, int col) {
      ListEntryModel entryModel = list.get(row);

      if (isSpecialColumn(col)) {
        return Boolean.valueOf(entryModel.isBeingEdited());
      } else {
        return entryModel.getContents(columnNames.get(col));
      }
    }

    /** Tells Swing whether the cell is editable. */
    public synchronized boolean isCellEditable(int row, int col) {
      return true;
    }

    /** Gets the column name by index. */
    public synchronized String getColumnName(int columnIndex) {
      return columnNames.get(columnIndex);
    }

    /** Gets the column class for editing. */
    public synchronized Class<?> getColumnClass(int columnIndex) {
      if (isSpecialColumn(columnIndex)) {
        return Boolean.class;
      } else {
        return String.class;
      }
    }

    /** Tells Swing how many rows are in this table. */
    public synchronized int getRowCount() {
      return list.size();
    }

    /** Tells Swing how many columns are in this table. */
    public synchronized int getColumnCount() {
      return columnNames.size();
    }
  }

  // GUI code

  public static JFrame createWindow(SpreadsheetService service,
       URL listFeedUrl) {
    JFrame frame = new JFrame();
    frame.setSize(600, 480);
    frame.getContentPane().add(new ListBasedSpreadsheetPanel(
        service, listFeedUrl));
    frame.setVisible(true);
    frame.setTitle("List Demo - Row-based Editing");
    return frame;
  }

  private void initializeGui() {
    table = new JTable(model);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    JScrollPane scrollpane = new JScrollPane(table);
    setLayout(new BorderLayout());
    add(scrollpane, BorderLayout.CENTER);

    Container southPanel = new JPanel();
    southPanel.setLayout(new GridBagLayout());

    deleteOneButton = new JButton("Delete");
    deleteOneButton.addActionListener(new ActionHandler());
    southPanel.add(deleteOneButton, getTopConstraints(0));
    revertOneButton = new JButton("Revert");
    revertOneButton.addActionListener(new ActionHandler());
    southPanel.add(revertOneButton, getTopConstraints(1));
    commitOneButton = new JButton("Commit");
    commitOneButton.addActionListener(new ActionHandler());
    southPanel.add(commitOneButton, getTopConstraints(2));
    commitAllButton = new JButton("Commit All");
    commitAllButton.addActionListener(new ActionHandler());
    southPanel.add(commitAllButton, getTopConstraints(3));
    refreshButton = new JButton("Refresh");
    refreshButton.addActionListener(new ActionHandler());
    southPanel.add(refreshButton, getTopConstraints(4));

    southPanel.add(new JLabel("Full text:"), getLeftConstraints(1));
    fulltextField = new JTextField();
    southPanel.add(fulltextField, getRightConstraints(1));

    southPanel.add(new JLabel("Structured:"), getLeftConstraints(2));
    spreadsheetQueryField = new JTextField();
    southPanel.add(spreadsheetQueryField, getRightConstraints(2));

    southPanel.add(new JLabel("Order By:"), getLeftConstraints(3));
    orderbyField = new JTextField();
    southPanel.add(orderbyField, getRightConstraints(3));

    add(southPanel, BorderLayout.SOUTH);

    refreshFromServer();
  }

  private GridBagConstraints getTopConstraints(int col) {
    GridBagConstraints c = new GridBagConstraints();

    c.gridy = 0;
    c.gridx = col;
    c.fill = GridBagConstraints.HORIZONTAL;

    return c;
  }

  private GridBagConstraints getLeftConstraints(int row) {
    GridBagConstraints c = new GridBagConstraints();

    c.gridy = row;
    c.gridx = 0;
    c.fill = GridBagConstraints.HORIZONTAL;

    return c;
  }

  private GridBagConstraints getRightConstraints(int row) {
    GridBagConstraints c = new GridBagConstraints();

    c.gridy = row;
    c.gridx = 1;
    c.gridwidth = 4;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1.0; // take up as much space as possible

    return c;
  }

  private class ActionHandler implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() == refreshButton) {
        refreshFromServer();
      } else if (ae.getSource() == deleteOneButton) {
        model.deleteOne(table.getSelectedRow());
      } else if (ae.getSource() == revertOneButton) {
        model.revertOne(table.getSelectedRow());
      } else if (ae.getSource() == commitOneButton) {
        model.commitOne(table.getSelectedRow());
      } else if (ae.getSource() == commitAllButton) {
        model.commitAll();
      }
    }
  }
}
