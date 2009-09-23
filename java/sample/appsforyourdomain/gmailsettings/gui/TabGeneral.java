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
 * Tab containing all the general information.
 */
public class TabGeneral extends Tab  {
  protected SpringLayout layout;
  protected JLabel pageSizeLabel;
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
    layout = new SpringLayout();
    setLayout(layout);

    pageSizeLabel = new JLabel("Page size: ");
    pageSize = new JComboBox(Constants.GENERAL_ALLOWED_PAGE_SIZES);

    enableShortcuts = new JCheckBox("Enable shortcuts:", Defaults.GENERAL_ENABLE_SHORTCUTS);

    enableArrows = new JCheckBox("Enable arrows:", Defaults.GENERAL_ENABLE_ARROWS);

    enableSnippets = new JCheckBox("Enable snippets:", Defaults.GENERAL_ENABLE_SNIPPETS);

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

    layout.putConstraint(SpringLayout.WEST, pageSizeLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, pageSizeLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, pageSize, 5, SpringLayout.EAST, pageSizeLabel);
    layout.putConstraint(SpringLayout.NORTH, pageSize, 5, SpringLayout.NORTH, this);

    layout.putConstraint(SpringLayout.WEST, enableShortcuts, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, enableShortcuts, 5, SpringLayout.SOUTH, pageSize);

    layout.putConstraint(SpringLayout.WEST, enableArrows, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, enableArrows, 5, SpringLayout.SOUTH, enableShortcuts);

    layout.putConstraint(SpringLayout.WEST, enableSnippets, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, enableSnippets, 5, SpringLayout.SOUTH, enableArrows);

    layout.putConstraint(SpringLayout.WEST, enableUnicode, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, enableUnicode, 5, SpringLayout.SOUTH, enableSnippets);

    layout.putConstraint(SpringLayout.WEST, submit, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, submit, 15, SpringLayout.SOUTH, enableUnicode);

    add(pageSizeLabel);
    add(pageSize);
    add(enableShortcuts);
    add(enableArrows);
    add(enableSnippets);
    add(enableUnicode);
    add(submit);
  }
}
