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

import sample.appsforyourdomain.gmailsettings.Defaults;
import sample.appsforyourdomain.gmailsettings.InvalidUserException;
import com.google.gdata.util.ServiceException;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * Tab containing all the vacation information.
 */
public class TabVacation extends Tab  {

  protected JCheckBox enable;
  protected JTextField subject;
  protected JTextField message;
  protected JCheckBox contactsOnly;
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabVacation() {
    super("Vacation Responder", "");

    enable = new JCheckBox("Enable:", Defaults.VACATION_ENABLE);
    subject = new JTextField(Defaults.VACATION_SUBJECT, 25);
    message = new JTextField(Defaults.VACATION_MESSAGE, 25);
    contactsOnly = new JCheckBox("Contacts only:", Defaults.VACATION_CONTACTS_ONLY);
    submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          if (GmailSettingsClient.settings == null) {
            JOptionPane.showMessageDialog(null, GmailSettingsClient.ERROR_AUTHENTICATION_REQUIRED,
                GmailSettingsClient.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
          }

          try {
            GmailSettingsClient.settings.changeVacation(GmailSettingsClient.users.
                getSelectedUsers(), enable.isSelected(), subject.getText(), message.getText(),
                contactsOnly.isSelected());
          } catch (InvalidUserException e) {
             JOptionPane.showMessageDialog(null, e, GmailSettingsClient.APP_TITLE,
                 JOptionPane.ERROR_MESSAGE);
          } catch (ServiceException e) {
             JOptionPane.showMessageDialog(null, e, GmailSettingsClient.APP_TITLE,
                 JOptionPane.ERROR_MESSAGE);
          } catch (MalformedURLException e) {
             JOptionPane.showMessageDialog(null, e, GmailSettingsClient.APP_TITLE,
                 JOptionPane.ERROR_MESSAGE);
          } catch (IOException e) {
             JOptionPane.showMessageDialog(null, e, GmailSettingsClient.APP_TITLE,
                 JOptionPane.ERROR_MESSAGE);
          } 
        }
      });

    setLayout(new FlowLayout());

    add(enable);
    add(new JLabel("Subject:"));
    add(subject);
    add(new JLabel("Message:"));
    add(message);
    add(contactsOnly);
    add(submit);
  }
}
