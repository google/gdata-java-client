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

import com.google.gdata.client.spreadsheet.FeedURLFactory;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.util.ServiceException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Window for selecting a spreadsheet.
 *
 * This is a little bit of a complicated class, but the main GData
 * parts to know are:
 *
 *   - populateSpreadsheetList just makes a few calls in order to
 *   get a list of spreadsheets
 *   - populateWorksheetList will get a list of worksheets within
 *   that spreadsheet
 *
 * 
 */
public class ChooseSpreadsheetFrame extends JFrame {

  /** The Google Spreadsheets GData service. */
  private SpreadsheetService service;

  private FeedURLFactory factory;

  private List<SpreadsheetEntry> spreadsheetEntries;

  private JList spreadsheetListBox;

  private List<WorksheetEntry> worksheetEntries;

  private JList worksheetListBox;

  private JTextField ajaxLinkField;

  private JTextField worksheetsFeedUrlField;

  private JTextField cellsFeedUrlField;

  private JTextField listFeedUrlField;

  private JButton viewWorksheetsButton;

  private JButton submitCellsButton;

  private JButton submitListButton;


  /** Starts the selection off with a spreadsheet feed helper. */
  public ChooseSpreadsheetFrame(SpreadsheetService spreadsheetService) {
    service = spreadsheetService;
    factory = FeedURLFactory.getDefault();
    initializeGui();
  }


  /**
   * Gets the list of spreadsheets, and fills the list box.
   */
  private void populateSpreadsheetList() {
    if (retrieveSpreadsheetList()) {
      fillSpreadsheetListBox();
    }
  }

  /**
   * Asks Google Spreadsheets for a list of all the spreadsheets
   * the user has access to.
   * @return true if successful
   */
  private boolean retrieveSpreadsheetList() {
    SpreadsheetFeed feed;

    try {
      feed = service.getFeed(
          factory.getSpreadsheetsFeedUrl(), SpreadsheetFeed.class);
    } catch (IOException e) {
      SpreadsheetApiDemo.showErrorBox(e);
      return false;
    } catch (ServiceException e) {
      SpreadsheetApiDemo.showErrorBox(e);
      return false;
    }

    this.spreadsheetEntries = feed.getEntries();

    return true;
  }

  /**
   * Fills up the list-box of spreadsheets with the already-computed entries.
   */
  private void fillSpreadsheetListBox() {
    String[] stringsForListbox = new String[spreadsheetEntries.size()];

    for (int i = 0; i < spreadsheetEntries.size(); i++) {
      SpreadsheetEntry entry = spreadsheetEntries.get(i);

      // Title of Spreadsheet (author, updated 2006-6-20 7:30PM)
      stringsForListbox[i] =
          entry.getTitle().getPlainText()
          + " (" + entry.getAuthors().get(0).getEmail()
          + ", updated " + entry.getUpdated().toUiString()
          + ")";
    }

    spreadsheetListBox.setListData(stringsForListbox);
  }

  /**
   * Gets the list of worksheets in the specified spreadsheet,
   * and fills the list box.
   * @param spreadsheet the selected spreadsheet
   */
  private void populateWorksheetList(SpreadsheetEntry spreadsheet) {
    if (retrieveWorksheetList(spreadsheet)) {
      fillWorksheetListBox(spreadsheet.getTitle().getPlainText());
    }
  }

  /**
   * Gets the list of worksheets from Google Spreadsheets.
   * @param spreadsheet the spreadsheet to get a list of worksheets for
   * @return true if successful
   */
  private boolean retrieveWorksheetList(SpreadsheetEntry spreadsheet) {
    WorksheetFeed feed;

    try {
      feed = service.getFeed(
          spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
    } catch (IOException e) {
      SpreadsheetApiDemo.showErrorBox(e);
      return false;
    } catch (ServiceException e) {
      SpreadsheetApiDemo.showErrorBox(e);
      return false;
    }

    this.worksheetEntries = feed.getEntries();

    return true;
  }


  /**
   * Fills up the list-box of worksheets with the already computed entries.
   */
  private void fillWorksheetListBox(String spreadsheetTitle) {
    String[] stringsForListbox = new String[worksheetEntries.size()];

    for (int i = 0; i < worksheetEntries.size(); i++) {
      WorksheetEntry entry = worksheetEntries.get(i);

      // Title Of Worksheet (T)
      stringsForListbox[i] = entry.getTitle().getPlainText()
          + " (in " + spreadsheetTitle + ")";
    }

    worksheetListBox.setListData(stringsForListbox);
  }

  /**
   * Handles when a user presses the "View Worksheets" button.
   *
   */
  private void handleViewWorksheetsButton() {
    int selected = spreadsheetListBox.getSelectedIndex();

    if (spreadsheetEntries != null && selected >= 0) {
      populateWorksheetList(spreadsheetEntries.get(selected));
    }
  }

  /**
   * Handles when a user presses a "View Cells Demo" button.
   */
  private void handleSubmitCellsButton() {
    int selected = worksheetListBox.getSelectedIndex();

    if (worksheetEntries != null && selected >= 0) {
      CellBasedSpreadsheetPanel.createWindow(
          service, worksheetEntries.get(selected).getCellFeedUrl());
    }
  }

  /**
   * Handles when a user presses a "View List Demo" button.
   */
  private void handleSubmitListButton() {
    int selected = worksheetListBox.getSelectedIndex();

    if (worksheetEntries != null && selected >= 0) {
      ListBasedSpreadsheetPanel.createWindow(service,
          worksheetEntries.get(selected).getListFeedUrl());
    }
  }

  /**
   * Shows the feed URL's as you select spreadsheets.
   */
  private void handleSpreadsheetSelection() {
    int selected = spreadsheetListBox.getSelectedIndex();

    if (spreadsheetEntries != null && selected >= 0) {
      SpreadsheetEntry entry = spreadsheetEntries.get(selected);

      ajaxLinkField.setText(
          entry.getHtmlLink().getHref());
      worksheetsFeedUrlField.setText(
          entry.getWorksheetFeedUrl().toExternalForm());
    }
  }

  /**
   * Shows the feed URL's as you select worksheets within a spreadsheets.
   */
  private void handleWorksheetSelection() {
    int selected = worksheetListBox.getSelectedIndex();

    if (worksheetEntries != null && selected >= 0) {
      WorksheetEntry entry = worksheetEntries.get(selected);

      cellsFeedUrlField.setText(entry.getCellFeedUrl().toExternalForm());
      listFeedUrlField.setText(entry.getListFeedUrl().toExternalForm());
    }
  }


  // ---- GUI code from here on down ----------------------------------------

  private void initializeGui() {
    setTitle("Choose your Spreadsheet");

    Container panel = getContentPane();
    panel.setLayout(new GridLayout(2, 1));

    // Top part - choose a spreadsheet

    JPanel spreadsheetPanel = new JPanel();
    spreadsheetPanel.setLayout(new BorderLayout());

    spreadsheetListBox = new JList();
    spreadsheetPanel.add(new JScrollPane(spreadsheetListBox),
        BorderLayout.CENTER);
    spreadsheetListBox.addListSelectionListener(new ActionHandler());

    Container topButtonsPanel = new JPanel();
    topButtonsPanel.setLayout(new GridLayout(3, 1));

    viewWorksheetsButton = new JButton("View Worksheets");
    viewWorksheetsButton.addActionListener(new ActionHandler());
    topButtonsPanel.add(viewWorksheetsButton);
    panel.add(spreadsheetPanel);

    ajaxLinkField = new JTextField();
    ajaxLinkField.setEditable(false);
    topButtonsPanel.add(ajaxLinkField);
    worksheetsFeedUrlField = new JTextField();
    worksheetsFeedUrlField.setEditable(false);
    topButtonsPanel.add(worksheetsFeedUrlField);

    spreadsheetPanel.add(topButtonsPanel, BorderLayout.SOUTH);
    panel.add(spreadsheetPanel);

    // Bottom part - choose a worksheet

    JPanel worksheetPanel = new JPanel();
    worksheetPanel.setLayout(new BorderLayout());

    worksheetListBox = new JList(
        new String[] { "[Please click 'View Worksheets' for a list.]" });
    worksheetPanel.add(new JScrollPane(worksheetListBox), BorderLayout.CENTER);
    worksheetListBox.addListSelectionListener(new ActionHandler());

    Container bottomButtonsPanel = new JPanel();
    bottomButtonsPanel.setLayout(new GridLayout(4, 1));

    submitCellsButton = new JButton("Cells Demo");
    submitCellsButton.addActionListener(new ActionHandler());
    bottomButtonsPanel.add(submitCellsButton);

    cellsFeedUrlField = new JTextField();
    cellsFeedUrlField.setEditable(false);
    bottomButtonsPanel.add(cellsFeedUrlField);

    submitListButton = new JButton("List Demo");
    submitListButton.addActionListener(new ActionHandler());
    bottomButtonsPanel.add(submitListButton);

    listFeedUrlField = new JTextField();
    listFeedUrlField.setEditable(false);
    bottomButtonsPanel.add(listFeedUrlField);

    worksheetPanel.add(bottomButtonsPanel, BorderLayout.SOUTH);
    panel.add(worksheetPanel);

    populateSpreadsheetList();

    pack();
    setSize(700, 600);
    setVisible(true);

    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private class ActionHandler
      implements ActionListener, ListSelectionListener {
    public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() == viewWorksheetsButton) {
        handleViewWorksheetsButton();
      } else if (ae.getSource() == submitCellsButton) {
        handleSubmitCellsButton();
      } else if (ae.getSource() == submitListButton) {
        handleSubmitListButton();
      }
    }

    public void valueChanged(ListSelectionEvent e) {
      if (e.getSource() == spreadsheetListBox) {
        handleSpreadsheetSelection();
      } else if (e.getSource() == worksheetListBox) {
        handleWorksheetSelection();
      }
    }
  }
}
