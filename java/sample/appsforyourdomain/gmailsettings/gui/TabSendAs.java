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
 * Tab containing all the send as information.
 */
public class TabSendAs extends Tab {
  protected SpringLayout layout;
  protected JLabel nameLabel;
  protected JTextField nameField;
  protected JLabel addressLabel;
  protected JTextField address;
  protected JLabel replyToLabel;
  protected JTextField replyTo;
  protected JCheckBox makeDefault;
  protected JButton submit;

 /**
  * Setup all the components on the tab.
  */
  public TabSendAs() {
    super("Send As", "");
    layout = new SpringLayout();
    setLayout(layout);

    nameLabel = new JLabel("Name: ");
    nameField = new JTextField(Defaults.SEND_AS_NAME, 25);

    addressLabel = new JLabel("Address: ");
    address = new JTextField(Defaults.SEND_AS_ADDRESS, 25);

    replyToLabel = new JLabel("Send As: ");
    replyTo = new JTextField(Defaults.SEND_AS_REPLY_TO, 25);

    makeDefault = new JCheckBox("Make Default:", Defaults.SEND_AS_MAKE_DEFAULT);

    submit = new JButton("Set Send As");
    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          if (GmailSettingsClient.settings == null) {
            JOptionPane.showMessageDialog(null, GmailSettingsClient.ERROR_AUTHENTICATION_REQUIRED,
                GmailSettingsClient.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
          }

          try {
            GmailSettingsClient.settings.createSendAs(GmailSettingsClient.users.
                getSelectedUsers(), nameField.getText(), address.getText(), replyTo.getText(),
                makeDefault.isSelected());
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

    layout.putConstraint(SpringLayout.WEST, nameLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, nameLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, nameField, 5, SpringLayout.EAST, nameLabel);
    layout.putConstraint(SpringLayout.NORTH, nameField, 5, SpringLayout.NORTH, this);

    layout.putConstraint(SpringLayout.WEST, addressLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, addressLabel, 5, SpringLayout.SOUTH, nameField);
    layout.putConstraint(SpringLayout.WEST, address, 5, SpringLayout.EAST, addressLabel);
    layout.putConstraint(SpringLayout.NORTH, address, 5, SpringLayout.SOUTH, nameField);

    layout.putConstraint(SpringLayout.WEST, replyToLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, replyToLabel, 5, SpringLayout.SOUTH, address);
    layout.putConstraint(SpringLayout.WEST, replyTo, 5, SpringLayout.EAST, replyToLabel);
    layout.putConstraint(SpringLayout.NORTH, replyTo, 5, SpringLayout.SOUTH, address);

    layout.putConstraint(SpringLayout.WEST, makeDefault, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, makeDefault, 5, SpringLayout.SOUTH, replyTo);

    layout.putConstraint(SpringLayout.WEST, submit, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, submit, 15, SpringLayout.SOUTH, makeDefault);

    add(nameLabel);
    add(nameField);
    add(addressLabel);
    add(address);
    add(replyToLabel);
    add(replyTo);
    add(makeDefault);
    add(submit);
  }
}
