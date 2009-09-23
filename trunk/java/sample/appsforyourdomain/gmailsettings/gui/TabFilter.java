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
 * Tab containing all the filter information.
 */
public class TabFilter extends Tab {
  protected SpringLayout layout;
  protected JLabel fromLabel;
  protected JTextField from;
  protected JLabel toLabel;
  protected JTextField to;
  protected JLabel subjectLabel;
  protected JTextField subject;
  protected JLabel hasTheWordLabel;
  protected JTextField hasTheWord;
  protected JLabel doesNotHaveTheWordLabel;
  protected JTextField doesNotHaveTheWord;
  protected JCheckBox hasAttachment;
  protected JCheckBox shouldMarkAsRead;
  protected JCheckBox shouldArchive;
  protected JLabel labelLabel;
  protected JTextField label;
  protected JButton submit;

  /**
   * Setup all the components on the tab.
   */
  public TabFilter() {
    super("Filters", "");
    layout = new SpringLayout();
    setLayout(layout);

    fromLabel = new JLabel("From: ");
    from = new JTextField(Defaults.FILTER_FROM, 25);

    toLabel = new JLabel("To: ");
    to = new JTextField(Defaults.FILTER_TO, 25);

    subjectLabel = new JLabel("Subject: ");
    subject = new JTextField(Defaults.FILTER_SUBJECT, 25);

    hasTheWordLabel = new JLabel("Has the word: ");
    hasTheWord = new JTextField(Defaults.FILTER_HAS_THE_WORD, 25);

    doesNotHaveTheWordLabel = new JLabel("Does not have the word: ");
    doesNotHaveTheWord = new JTextField(Defaults.FILTER_DOES_NOT_HAVE_THE_WORD, 25);

    hasAttachment = new JCheckBox("Has attachment:", Defaults.FILTER_HAS_ATTACHMENT);

    shouldMarkAsRead = new JCheckBox("Should mark as read:", Defaults.FILTER_SHOULD_MARK_AS_READ);

    shouldArchive = new JCheckBox("Should archive:", Defaults.FILTER_SHOULD_ARCHIVE);

    labelLabel = new JLabel("Label: ");
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

    layout.putConstraint(SpringLayout.WEST, fromLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, fromLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, from, 5, SpringLayout.EAST, fromLabel);
    layout.putConstraint(SpringLayout.NORTH, from, 5, SpringLayout.NORTH, this);

    layout.putConstraint(SpringLayout.WEST, toLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, toLabel, 5, SpringLayout.SOUTH, from);
    layout.putConstraint(SpringLayout.WEST, to, 5, SpringLayout.EAST, toLabel);
    layout.putConstraint(SpringLayout.NORTH, to, 5, SpringLayout.SOUTH, from);

    layout.putConstraint(SpringLayout.WEST, subjectLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, subjectLabel, 5, SpringLayout.SOUTH, to);
    layout.putConstraint(SpringLayout.WEST, subject, 5, SpringLayout.EAST, subjectLabel);
    layout.putConstraint(SpringLayout.NORTH, subject, 5, SpringLayout.SOUTH, to);

    layout.putConstraint(SpringLayout.WEST, hasTheWordLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, hasTheWordLabel, 5, SpringLayout.SOUTH, subject);
    layout.putConstraint(SpringLayout.WEST, hasTheWord, 5, SpringLayout.EAST, hasTheWordLabel);
    layout.putConstraint(SpringLayout.NORTH, hasTheWord, 5, SpringLayout.SOUTH, subject);

    layout.putConstraint(SpringLayout.WEST, doesNotHaveTheWordLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, doesNotHaveTheWordLabel, 5, SpringLayout.SOUTH,
        hasTheWord);
    layout.putConstraint(SpringLayout.WEST, doesNotHaveTheWord, 5, SpringLayout.EAST,
        doesNotHaveTheWordLabel);
    layout.putConstraint(SpringLayout.NORTH, doesNotHaveTheWord, 5, SpringLayout.SOUTH, hasTheWord);

    layout.putConstraint(SpringLayout.WEST, hasAttachment, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, hasAttachment, 5, SpringLayout.SOUTH,
        doesNotHaveTheWord);

    layout.putConstraint(SpringLayout.WEST, shouldMarkAsRead, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, shouldMarkAsRead, 5, SpringLayout.SOUTH,
        hasAttachment);

    layout.putConstraint(SpringLayout.WEST, shouldArchive, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, shouldArchive, 5, SpringLayout.SOUTH,
        shouldMarkAsRead);

    layout.putConstraint(SpringLayout.WEST, labelLabel, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, labelLabel, 5, SpringLayout.SOUTH, shouldArchive);
    layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.EAST, labelLabel);
    layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.SOUTH, shouldArchive);

    layout.putConstraint(SpringLayout.WEST, submit, 5, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, submit, 15, SpringLayout.SOUTH, label);

    add(fromLabel);
    add(from);
    add(toLabel);
    add(to);
    add(subjectLabel);
    add(subject);
    add(hasTheWordLabel);
    add(hasTheWord);
    add(doesNotHaveTheWordLabel);
    add(doesNotHaveTheWord);
    add(hasAttachment);
    add(shouldMarkAsRead);
    add(shouldArchive);
    add(labelLabel);
    add(label);
    add(submit);
  }
}
