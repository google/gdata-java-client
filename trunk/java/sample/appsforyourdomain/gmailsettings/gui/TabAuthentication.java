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

import sample.appsforyourdomain.gmailsettings.Constants;
import sample.appsforyourdomain.gmailsettings.GmailSettingsService;
import com.google.gdata.util.ServiceException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * Tab containing all the authentication information.
 */
public class TabAuthentication extends Tab {
  protected SpringLayout layout;
  protected JLabel domainLabel;
  protected JTextField domain;
  protected JLabel usernameLabel;
  protected JTextField username;
  protected JLabel passwordLabel;
  protected JPasswordField password;
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabAuthentication() {
    super("Authentication", "Authenticate with a server");
    layout = new SpringLayout();
    setLayout(layout);

    domainLabel = new JLabel("Domain: ");
    domain = new JTextField(12);
    domain.setText(Constants.DEFAULT_DOMAIN);

    usernameLabel = new JLabel("Admin username: ");
    username = new JTextField(12);
    username.setText(Constants.DEFAULT_USERNAME);

    passwordLabel = new JLabel("Admin password: ");
    password = new JPasswordField(12);
    password.setText(Constants.DEFAULT_PASSWORD);

    submit = new JButton("Update");
    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          try {
            GmailSettingsClient.settings = new GmailSettingsService(GmailSettingsClient.APP_TITLE,
                domain.getText(), username.getText(), new String (password.getPassword()));
            GmailSettingsClient.users.refresh(domain.getText(), username.getText(),
                new String (password.getPassword()));
          } catch (ServiceException e) {
            JOptionPane.showMessageDialog(null, e, GmailSettingsClient.APP_TITLE,
                JOptionPane.ERROR_MESSAGE);
          }
        }
      });

    layout.putConstraint(SpringLayout.WEST, domainLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, domainLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, domain, 5, SpringLayout.EAST, domainLabel);
    layout.putConstraint(SpringLayout.NORTH, domain, 5, SpringLayout.NORTH, this);

    layout.putConstraint(SpringLayout.WEST, usernameLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, usernameLabel, 5, SpringLayout.SOUTH, domain);
    layout.putConstraint(SpringLayout.WEST, username, 5, SpringLayout.EAST, usernameLabel);
    layout.putConstraint(SpringLayout.NORTH, username, 5, SpringLayout.SOUTH, domain);

    layout.putConstraint(SpringLayout.WEST, passwordLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, passwordLabel, 5, SpringLayout.SOUTH, username);
    layout.putConstraint(SpringLayout.WEST, password, 5, SpringLayout.EAST, passwordLabel);
    layout.putConstraint(SpringLayout.NORTH, password, 5, SpringLayout.SOUTH, username);

    layout.putConstraint(SpringLayout.WEST, submit, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, submit, 15, SpringLayout.SOUTH, password);

    add(domainLabel);
    add(domain);
    add(usernameLabel);
    add(username);
    add(passwordLabel);
    add(password);
    add(submit);
  }
}
