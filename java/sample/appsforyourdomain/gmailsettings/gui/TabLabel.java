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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
* Tab containing all the label information.
*/
public class TabLabel extends Tab  {
  protected SpringLayout layout;
  protected JLabel labelLabel;
  protected JTextField label;
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabLabel() {
    super("Label", "");
    layout = new SpringLayout();
    setLayout(layout);

    labelLabel = new JLabel("Label: ");
    label = new JTextField(Defaults.LABEL, 25);

    submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          if (GmailSettingsClient.settings == null) {
            JOptionPane.showMessageDialog(null,
                GmailSettingsClient.ERROR_AUTHENTICATION_REQUIRED,
                GmailSettingsClient.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
          }

          try {
            GmailSettingsClient.settings.createLabel(GmailSettingsClient.users.
                getSelectedUsers(), label.getText());
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

    layout.putConstraint(SpringLayout.WEST, labelLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, labelLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.EAST, labelLabel);
    layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, this);

    layout.putConstraint(SpringLayout.WEST, submit, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, submit, 15, SpringLayout.SOUTH, label);

    add(labelLabel);
    add(label);
    add(submit);
  }
}
