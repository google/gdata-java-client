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


package sample.appsforyourdomain.gmailsettings.gui;

import sample.appsforyourdomain.gmailsettings.GmailSettingsService;

import javax.swing.JFrame;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * This is the example GUI client for the Google Apps Gmail Settings API.
 */
public class GmailSettingsClient {
  public static final String APP_TITLE = "GUI Gmail Settings Client";
  public static final String ERROR_AUTHENTICATION_REQUIRED = "You must authenticate first.";

  protected static final int DEFAULT_APP_HEIGHT = 350;
  protected static final int DEFAULT_APP_WIDTH = 600;
  protected static final int DEFAULT_PANE_DIVIDER_LOCATION = 150;

  public static GmailSettingsService settings;
  public static UsersPanel users;

  /**
   * Prevents the class from being instantiated.
   */
  private GmailSettingsClient() {}

  /**
   * Entry point for Graphical GMail Settings Client.
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        JFrame frame = new JFrame(APP_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(DEFAULT_APP_WIDTH, DEFAULT_APP_HEIGHT);

        users = new UsersPanel();

        TabbedPane settingTabs = new TabbedPane();
        JSplitPane splitpane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, users, settingTabs);
        splitpane.setDividerLocation(DEFAULT_PANE_DIVIDER_LOCATION);

        frame.add(splitpane);
        frame.setVisible(true);
      }
    });
  }
}
