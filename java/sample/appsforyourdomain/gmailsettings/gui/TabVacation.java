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
import com.google.gdata.util.ServiceException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * Tab containing all the vacation information.
 */
public class TabVacation extends Tab  {
  protected SpringLayout layout;
  protected JCheckBox enable;
  protected JLabel subjectLabel;
  protected JTextField subject;
  protected JLabel messageLabel;
  protected JTextField message;
  protected JLabel contactsOnlyLabel;
  protected JCheckBox contactsOnly;
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabVacation() {
    super("Vacation Responder", "");
    layout = new SpringLayout();
    setLayout(layout);

    enable = new JCheckBox("Enable:", Defaults.VACATION_ENABLE);

    subjectLabel = new JLabel("Subject: ");
    subject = new JTextField(Defaults.VACATION_SUBJECT, 25);

    messageLabel = new JLabel("Message: ");
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
          } catch (IllegalArgumentException e) {
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

    layout.putConstraint(SpringLayout.WEST, enable, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, enable, 5, SpringLayout.NORTH, this);

    layout.putConstraint(SpringLayout.WEST, subjectLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, subjectLabel, 5, SpringLayout.SOUTH, enable);
    layout.putConstraint(SpringLayout.WEST, subject, 5, SpringLayout.EAST, subjectLabel);
    layout.putConstraint(SpringLayout.NORTH, subject, 5, SpringLayout.SOUTH, enable);

    layout.putConstraint(SpringLayout.WEST, messageLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, messageLabel, 5, SpringLayout.SOUTH, subject);
    layout.putConstraint(SpringLayout.WEST, message, 5, SpringLayout.EAST, messageLabel);
    layout.putConstraint(SpringLayout.NORTH, message, 5, SpringLayout.SOUTH, subject);

    layout.putConstraint(SpringLayout.WEST, contactsOnly, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, contactsOnly, 5, SpringLayout.SOUTH, message);

    layout.putConstraint(SpringLayout.WEST, submit, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, submit, 15, SpringLayout.SOUTH, contactsOnly);

    add(enable);
    add(subjectLabel);
    add(subject);
    add(messageLabel);
    add(message);
    add(contactsOnly);
    add(submit);
  }
}
