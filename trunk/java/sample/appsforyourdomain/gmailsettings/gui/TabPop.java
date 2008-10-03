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
import sample.appsforyourdomain.gmailsettings.Defaults;
import sample.appsforyourdomain.gmailsettings.InvalidUserException;
import com.google.gdata.util.ServiceException;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * Tab containing all the POP information. 
 */
public class TabPop extends Tab  {

  protected JCheckBox enable;
  protected JComboBox enableFor;
  protected JComboBox action;
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabPop() {
    super("Pop","");

    enable = new JCheckBox("Enable:",Defaults.POP_ENABLE);
    enableFor = new JComboBox(Constants.POP_ENABLE_FOR);
    enableFor.setSelectedItem(Defaults.POP_ENABLE_FOR);
    action = new JComboBox(Constants.POP_ACTION);
    action.setSelectedItem(Defaults.POP_ACTION);
    submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          if (GmailSettingsClient.settings == null) {
            JOptionPane.showMessageDialog(null, GmailSettingsClient.ERROR_AUTHENTICATION_REQUIRED,
                GmailSettingsClient.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
          }

          try {
            GmailSettingsClient.settings.changePop(GmailSettingsClient.users.
                getSelectedUsers(), enable.isSelected(), enableFor.getSelectedItem().toString(), 
                action.getSelectedItem().toString());
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
    add(new JLabel("Enable for:"));
    add(enableFor);
    add(new JLabel("Action:"));
    add(action);
    add(submit);
  }
}
