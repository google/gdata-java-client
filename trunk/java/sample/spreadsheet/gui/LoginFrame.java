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

import com.google.gdata.client.GoogleService.CaptchaRequiredException;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.util.AuthenticationException;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * A Swing window for logging in to Google Spreadsheets.
 *
 * 
 */
public class LoginFrame extends JFrame {

  /** The spreadsheet service. */
  private SpreadsheetService service;

  /** Field for entering username. */
  private JTextField usernameField;

  /** Field for entering password. */
  private JPasswordField passwordField;

  /** Image to display as a CAPTCHA (distorted letters to verify human). */
  private JLabel captchaImage;

  /** Place where user types the captcha answer. */
  private JTextField captchaAnswerField;

  /** Button to log in. */
  private JButton submitButton;

  /**
   * The captcha token that is issued,
   * null if there was no CAPTCHA challenge.
   */
  private String captchaToken = null;


  /**
   * Starts out the login window, for a particular service and
   * feed root, with initial username and password (these can
   * be blank strings).
   */
  public LoginFrame(SpreadsheetService service,
      String username, String password) {
    this.service = service;

    initializeGui();

    usernameField.setText(username);
    passwordField.setText(password);
  }

  /**
   * Try authenticating the user with the provided username and
   * password.
   */
  private boolean authenticate(
      String username, String password) {
    try {
      if (captchaToken == null) {

        // No CAPTCHA challenge was presented.
        // Proceed to the next step.
        service.setUserCredentials(username, password);
      } else {

        // Use the CAPTCHA token and answer to help the
        // authentication.
        service.setUserCredentials(username, password, captchaToken,
            captchaAnswerField.getText());
      }
      return true;
    } catch (CaptchaRequiredException e) {

      // Get the CAPTCHA token and display the image.
      captchaToken = e.getCaptchaToken();
      try {
        captchaImage.setIcon(new ImageIcon(new URL(e.getCaptchaUrl())));
        captchaAnswerField.setText("(Please write the above letters here)");
      } catch (IOException ioe) {
        captchaImage.setText("(Error parsing captcha image URL)");
      }
      return false;
    } catch (AuthenticationException e) {
      SpreadsheetApiDemo.showErrorBox(e);
      return false;
    }
  }

  /**
   * Handles the submit button being pressed.
   */
  private void handleSubmitButton() {
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());

    if (authenticate(username, password)) {
      new ChooseSpreadsheetFrame(service);
      dispose();
    }
  }


  // ---- GUI code from here on down ----------------------------------------

  /**
   * Handles all clicks.
   */
  private class ActionHandler implements ActionListener {
    public void actionPerformed(ActionEvent ae) {
      if (ae.getSource() == submitButton) {
        handleSubmitButton();
      }
    }
  }

  /**
   * Initializes all the GUI widgets.
   */
  private void initializeGui() {
    setTitle("Log in to Google Spreadsheets");

    Container panel = getContentPane();

    panel.setLayout(new BorderLayout());

    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(4, 1));

    topPanel.add(new JLabel("Log in to Google Spreadsheets!"));

    usernameField = new JTextField();
    topPanel.add(usernameField);

    passwordField = new JPasswordField();
    topPanel.add(passwordField);

    submitButton = new JButton("Log in!");
    submitButton.addActionListener(new ActionHandler());
    topPanel.add(submitButton);

    panel.add(topPanel, BorderLayout.NORTH);

    captchaImage = new JLabel("(A CAPTCHA may appear here)",
        SwingConstants.CENTER);
    panel.add(captchaImage, BorderLayout.CENTER);

    captchaAnswerField = new JTextField();
    captchaAnswerField.setText("(type captcha answer here)");
    panel.add(captchaAnswerField, BorderLayout.SOUTH);

    setSize(300, 240);
    setVisible(true);
  }
}
