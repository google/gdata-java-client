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
import com.google.gdata.util.ServiceException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

/**
 * Tab containing all the POP information.
 */
public class TabPop extends Tab  {
  protected SpringLayout layout;
  protected JCheckBox enable;
  protected JLabel enableForLabel;
  protected JComboBox enableFor;
  protected JLabel actionLabel;
  protected JComboBox action;
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabPop() {
    super("Pop","");
    layout = new SpringLayout();
    setLayout(layout);

    enable = new JCheckBox("Enable:",Defaults.POP_ENABLE);

    enableForLabel = new JLabel("Enable for: ");
    enableFor = new JComboBox(Constants.POP_ENABLE_FOR);
    enableFor.setSelectedItem(Defaults.POP_ENABLE_FOR);

    actionLabel = new JLabel("Action: ");
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

    layout.putConstraint(SpringLayout.WEST, enableForLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, enableForLabel, 5, SpringLayout.SOUTH, enable);
    layout.putConstraint(SpringLayout.WEST, enableFor, 5, SpringLayout.EAST, enableForLabel);
    layout.putConstraint(SpringLayout.NORTH, enableFor, 5, SpringLayout.SOUTH, enable);

    layout.putConstraint(SpringLayout.WEST, actionLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, actionLabel, 5, SpringLayout.SOUTH, enableFor);
    layout.putConstraint(SpringLayout.WEST, action, 5, SpringLayout.EAST, actionLabel);
    layout.putConstraint(SpringLayout.NORTH, action, 5, SpringLayout.SOUTH, enableFor);

    layout.putConstraint(SpringLayout.WEST, submit, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, submit, 15, SpringLayout.SOUTH, action);

    add(enable);
    add(enableForLabel);
    add(enableFor);
    add(actionLabel);
    add(action);
    add(submit);
  }
}
