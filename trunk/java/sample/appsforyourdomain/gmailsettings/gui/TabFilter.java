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
 * Tab containing all the filter information. 
 */
public class TabFilter extends Tab {
  
  protected JTextField from;
  protected JTextField to;
  protected JTextField subject;
  protected JTextField hasTheWord;
  protected JTextField doesNotHaveTheWord;
  protected JCheckBox hasAttachment;
  protected JCheckBox shouldMarkAsRead;
  protected JCheckBox shouldArchive;
  protected JTextField label;
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabFilter() {
    super("Filters", "");

    from = new JTextField(Defaults.FILTER_FROM, 25);
    to = new JTextField(Defaults.FILTER_TO, 25);
    subject = new JTextField(Defaults.FILTER_SUBJECT, 25);
    hasTheWord = new JTextField(Defaults.FILTER_HAS_THE_WORD, 25);
    doesNotHaveTheWord = new JTextField(Defaults.FILTER_DOES_NOT_HAVE_THE_WORD, 25);

    hasAttachment = new JCheckBox("Has attachment:", Defaults.FILTER_HAS_ATTACHMENT);
    shouldMarkAsRead = new JCheckBox("Should mark as read:", 
        Defaults.FILTER_SHOULD_MARK_AS_READ);
    shouldArchive = new JCheckBox("Should archive:", Defaults.FILTER_SHOULD_ARCHIVE);

    label = new JTextField(Defaults.FILTER_LABEL, 25);
    submit = new JButton("Submit");
    submit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent event) {
          if (GmailSettingsClient.settings == null) {
            JOptionPane.showMessageDialog(null, GmailSettingsClient.ERROR_AUTHENTICATION_REQUIRED,
                GmailSettingsClient.APP_TITLE, JOptionPane.ERROR_MESSAGE); 
            return;
          }

          try {
            GmailSettingsClient.settings.createFilter(GmailSettingsClient.users.
                getSelectedUsers(), from.getText(), to.getText(), subject.getText(), 
                hasTheWord.getText(), doesNotHaveTheWord.getText(), hasAttachment.isSelected(), 
                shouldMarkAsRead.isSelected(), shouldArchive.isSelected(), label.getText());
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
  
    add(new JLabel("From:"));
    add(from);
    add(new JLabel("To:"));
    add(to);
    add(new JLabel("Subject:"));
    add(subject);
    add(new JLabel("Has the word:"));
    add(hasTheWord);
    add(new JLabel("Does not have the word:"));
    add(doesNotHaveTheWord);
    add(hasAttachment);
    add(shouldMarkAsRead);
    add(shouldArchive);
    add(new JLabel("Label:"));
    add(label);
    add(submit);
  }
}
