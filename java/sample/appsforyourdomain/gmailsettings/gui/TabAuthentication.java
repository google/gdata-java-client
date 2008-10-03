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

import sample.appsforyourdomain.gmailsettings.GmailSettings;
import sample.appsforyourdomain.gmailsettings.Constants;
import com.google.gdata.util.ServiceException;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;

/**
 * Tab containing all the authentication information. 
 */
public class TabAuthentication extends Tab {
  
  protected JTextField domain;
  protected JTextField username;
  protected JPasswordField password;  
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabAuthentication() {
    super("Authentication", "Authenticate with a server");

    domain = new JTextField(12);
    domain.setText(Constants.DEFAULT_DOMAIN);

    username = new JTextField(12);
    username.setText(Constants.DEFAULT_USERNAME);
    
    password = new JPasswordField(12);
    password.setText(Constants.DEFAULT_PASSWORD);

    submit = new JButton("Update");
    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          try {
            GmailSettingsClient.settings = new GmailSettings(GmailSettingsClient.APP_TITLE, 
                domain.getText(), username.getText(), new String (password.getPassword()));
            GmailSettingsClient.users.refresh(domain.getText(), username.getText(),
                new String (password.getPassword()));
          } catch (ServiceException e) {
             JOptionPane.showMessageDialog(null, e, GmailSettingsClient.APP_TITLE, 
                 JOptionPane.ERROR_MESSAGE);
          } 
        }
      });

    setLayout(new FlowLayout());
    
    add(new JLabel("Domain:"));
    add(domain);
    add(new JLabel("Admin username:"));
    add(username);
    add(new JLabel("Admin password:"));
    add(password);
    add(submit);
  }
}
