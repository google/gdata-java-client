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
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * Runs the spreadsheets GUI API demo.
 */
public final class SpreadsheetApiDemo {

  /** Prevents instantiation. */
  private SpreadsheetApiDemo() {
  }

  /**
   * Runs the demo.
   *
   * @param args IGNORED
   */
  public static void main(String[] args) {
    new LoginFrame(new SpreadsheetService("SpreadsheetApiDemo-1"),
        "(username)", "");
  }

  /**
   * Shows a pop-up dialog box alerting the user of an error.
   *
   * @param e the exception
   */
  public static void showErrorBox(Exception e) {
    if (e instanceof IOException) {
      JOptionPane.showMessageDialog(null,
          "There was an error contacting Google: " + e.getMessage(),
          "Error contacting Google",
          JOptionPane.ERROR_MESSAGE);
    } else if (e instanceof AuthenticationException) {
      JOptionPane.showMessageDialog(null,
        "Your username and/or password were rejected: " + e.getMessage(),
        "Authentication Error",
        JOptionPane.ERROR_MESSAGE);
    } else if (e instanceof ServiceException) {
      JOptionPane.showMessageDialog(null,
          "Google returned the error: " + e.getMessage(),
          "Google had an error processing the request",
          JOptionPane.ERROR_MESSAGE);
    } else {
      JOptionPane.showMessageDialog(null,
          "There was an unexpected error: " + e.getMessage(),
          "Unexpected error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
