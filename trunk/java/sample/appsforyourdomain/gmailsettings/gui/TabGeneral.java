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
 * Tab containing all the general information.
 */
public class TabGeneral extends Tab  {

  protected JComboBox pageSize;
  protected JCheckBox enableShortcuts;
  protected JCheckBox enableArrows;
  protected JCheckBox enableSnippets;
  protected JCheckBox enableUnicode;
  protected JButton submit;


  /**
   * Setup all the components on the tab.
   */
  public TabGeneral() {
    super("General", "");

    pageSize = new JComboBox(Constants.GENERAL_ALLOWED_PAGE_SIZES);
    enableShortcuts = new JCheckBox("Enable shortcuts:",
        Defaults.GENERAL_ENABLE_SHORTCUTS);
    enableArrows = new JCheckBox("Enable arrows:", Defaults.GENERAL_ENABLE_ARROWS);
    enableSnippets = new JCheckBox("Enable snippets:", 
        Defaults.GENERAL_ENABLE_SNIPPETS);
    enableUnicode = new JCheckBox("Enable unicode:", Defaults.GENERAL_ENABLE_UNICODE);

    submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          if (GmailSettingsClient.settings == null) {
            JOptionPane.showMessageDialog(null, GmailSettingsClient.ERROR_AUTHENTICATION_REQUIRED,
                GmailSettingsClient.APP_TITLE, JOptionPane.ERROR_MESSAGE);
            return;
          }

          try {
            GmailSettingsClient.settings.changeGeneral(GmailSettingsClient.users.
                getSelectedUsers(), pageSize.getSelectedItem().toString(), 
                enableShortcuts.isSelected(), enableArrows.isSelected(), 
                enableSnippets.isSelected(), 
                enableUnicode.isSelected());
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

    add(new JLabel("Page size:"));
    add(pageSize);
    add(enableShortcuts);
    add(enableArrows);
    add(enableSnippets);
    add(enableUnicode);
    add(submit);
  }
}
