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

import com.google.gdata.client.appsforyourdomain.AppsForYourDomainQuery;
import com.google.gdata.client.appsforyourdomain.UserService;
import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.provisioning.UserEntry;
import com.google.gdata.data.appsforyourdomain.provisioning.UserFeed;
import com.google.gdata.util.ServiceException;

import java.awt.GridLayout;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Panel that uses the provisioning API to display all the users from a domain.
 */
public class UsersPanel extends JPanel {

  protected JList users;
  protected DefaultListModel usersListModel;
  protected JScrollPane usersPane;

  /**
   * Sets up the panel.
   */
  public UsersPanel() {
    usersListModel = new DefaultListModel();
    users = new JList();
    users.setModel(usersListModel);
    usersPane = new JScrollPane(users);
    setLayout(new GridLayout(1, 1));
    add(usersPane);
  }

  /**
   * @return Returns a list of all the users that were selected in the panel.
   */
  public List<String> getSelectedUsers() {
    Object[] tmp = users.getSelectedValues();
    List<String> susers = new ArrayList<String>();

    for (int i = 0; i < tmp.length; i++) {
      susers.add(tmp[i].toString());
    }

    return susers;
  }

  /**
   * Refreshes the panel to display the users.
   *
   * @param domain The domain in which settings will be modified.
   * @param username The user name (not email) of a domain administrator.
   * @param password The user's password on the domain.
   */
  public void refresh(String domain, String username, String password) {
    try {
      UserFeed usersFeed = getUsers(domain, username, password);
      usersListModel.clear();
      Iterator<UserEntry> userIterator = usersFeed.getEntries().iterator();
      while (userIterator.hasNext()) {
        usersListModel.addElement(userIterator.next().getLogin().getUserName());
      }
    } catch (MalformedURLException e) {
      JOptionPane.showMessageDialog(null, e, GmailSettingsClient.APP_TITLE,
          JOptionPane.ERROR_MESSAGE);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, e, GmailSettingsClient.APP_TITLE,
          JOptionPane.ERROR_MESSAGE);
    } catch (ServiceException e) {
      JOptionPane.showMessageDialog(null, e, GmailSettingsClient.APP_TITLE,
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Retrieves the first 100 users from the domain.
   *
   * @param domain The domain in which settings will be modified.
   * @param username The user name (not email) of a domain administrator.
   * @param password The user's password on the domain.
   * @return UserFeed containing all the user accounts in the domain.
   * @throws MalformedURLException if the batch feed URL cannot be constructed.
   * @throws IOException if an error occurs while communicating with the GData
   *       service.
   * @throws ServiceException if the insert request failed due to system error.
   */
  protected UserFeed getUsers(String domain, String username, String password)
      throws MalformedURLException, IOException, ServiceException {
    String domainUrlBase = null;
    UserFeed allUsers = null;

    UserService userService = new UserService(GmailSettingsClient.APP_TITLE);
    userService.setUserCredentials(username + "@" + domain, password);

    domainUrlBase = "https://www.google.com/a/feeds/" + domain + "/";
    URL retrieveUrl = new URL(domainUrlBase + "user/2.0/");
    AppsForYourDomainQuery query = new AppsForYourDomainQuery(retrieveUrl);
    query.setStartUsername(null);
    allUsers = new UserFeed();
    UserFeed currentPage;
    Link nextLink;
    do {
      currentPage = userService.query(query, UserFeed.class);
      allUsers.getEntries().addAll(currentPage.getEntries());
      nextLink = currentPage.getLink(Link.Rel.NEXT, Link.Type.ATOM);
      if (nextLink != null) {
        retrieveUrl = new URL(nextLink.getHref());
      }
    } while (nextLink != null);

    return allUsers;
  }
}
