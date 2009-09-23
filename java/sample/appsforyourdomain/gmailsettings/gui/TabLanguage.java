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
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SpringLayout;

/**
 * Tab containing all the language information.
 */
public class TabLanguage extends Tab  {
  protected SpringLayout layout;
  protected JLabel languagesLabel;
  protected JComboBox languages;
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabLanguage() {
    super("Language", "");
    layout = new SpringLayout();
    setLayout(layout);

    languagesLabel = new JLabel("Languages: ");
    languages = new JComboBox(Constants.LANGUAGE_VALID_KEY);
    languages.setSelectedItem(Defaults.LANGUAGE);

    submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          if (GmailSettingsClient.settings == null) {
            JOptionPane.showMessageDialog(null, GmailSettingsClient.ERROR_AUTHENTICATION_REQUIRED,
                GmailSettingsClient.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
          }

          try {
            GmailSettingsClient.settings.changeLanguage(GmailSettingsClient.users.
                getSelectedUsers(), languages.getSelectedItem().toString());
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

    layout.putConstraint(SpringLayout.WEST, languagesLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, languagesLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, languages, 5, SpringLayout.EAST, languagesLabel);
    layout.putConstraint(SpringLayout.NORTH, languages, 5, SpringLayout.NORTH, this);

    layout.putConstraint(SpringLayout.WEST, submit, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, submit, 15, SpringLayout.SOUTH, languages);

    add(languagesLabel);
    add(languages);
    add(submit);
  }
}
