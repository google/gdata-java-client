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


package sample.gtt;

import com.google.gdata.client.gtt.GttService;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.acl.AclRole;
import com.google.gdata.data.acl.AclScope;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;

/**
 * Update entry sharing.
 *
 * 
 */
public class ShareCommand implements Command {

  public static final ShareCommand DOCUMENTS_INSTANCE
      = new ShareCommand("documents");

  public static final ShareCommand TMS_INSTANCE
      = new ShareCommand("tm");

  public static final ShareCommand GLOSSARIES_INSTANCE
      = new ShareCommand("glossary");

  protected final String feedName;

  public ShareCommand(String feedName) {
    this.feedName = feedName;
  }

  public void execute(GttService service, String[] args)
      throws IOException, ServiceException {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);

    String entryId = parser.getValue("id");

    if (parser.containsKey("list")) {
      URL feedUrl = FeedUris.getAclFeedUrl(feedName, entryId);

      System.out.println("Listing all accessors for " + feedName
          + " with id" + entryId + "  ...");
      // Get the list of accessors for this entry
      AclFeed aclFeed = service.getFeed(feedUrl, AclFeed.class);
      printAclInfo(aclFeed);

    } else if (parser.containsKey("changetype")) {
      String changeType = parser.getValue("changetype");
      String emailId = parser.getValue("email");

      if ("add".equals(changeType)) {
        AclScope scope = new AclScope(AclScope.Type.USER, emailId);
        AclRole role = new AclRole(parser.getValue("role"));

        // Add a new accessor for this entry
        AclEntry entry = new AclEntry();
        entry.setRole(role);
        entry.setScope(scope);

        System.out.println("Adding user " + emailId + " as " + role.getValue()
            + " to " + feedName + " with id " + entryId + " ...");
        URL feedUrl = FeedUris.getAclFeedUrl(feedName, entryId);
        service.insert(feedUrl, entry);
        System.out.println("...done");

      } else if ("change".equals(changeType)) {
        AclScope scope = new AclScope(AclScope.Type.USER, emailId);
        AclRole role = new AclRole(parser.getValue("role"));

        // Change the role of an accessor for this entry
        AclEntry entry = new AclEntry();
        entry.setRole(role);
        entry.setScope(scope);

        System.out.println("Changing user " + emailId + "'s access to "
            + role.getValue() + " for " + feedName + " with id "
            + entryId + " ...");
        URL feedUrl = FeedUris.getAclFeedUrl(feedName, entryId, emailId);
        service.update(feedUrl, entry);
        System.out.println("...done");

      } else if ("remove".equals(changeType)) {
        System.out.println("Removing user " + emailId + "'s access to "
            + feedName + " with id " + entryId + " ...");
        URL feedUrl = FeedUris.getAclFeedUrl(feedName, entryId, emailId);
        // Remove an accessor for this entry
        service.delete(feedUrl);
        System.out.println("...done");
      }
    }
  }

  private void printAclInfo(AclFeed aclFeed)
      throws IOException, ServiceException {
    System.out.println("...done, currently their are "
        + aclFeed.getEntries().size() + " accessors for this entry.\n");

    int i = 1;
    for (AclEntry entry : aclFeed.getEntries()) {
      System.out.println(String.valueOf(i++) +  ") "
          + " scope = '" + entry.getScope().getValue() + "'"
          + ", role = '" + entry.getRole().getValue() + "'");
    }
  }

  public String helpString() {
    return "Updates sharing info."
        + "\n\t--id <id>\t; the id of the entry whose acl needs updation"
        + "\n\t--list\t; just list the current collaborators, no "
        + "updation"
        + "\n\t--changetype <type>\t; one of 'add', 'change', 'remove'"
        + "\n\t--email <emailid>\t; email id of user who acl is to be "
        + "updated"
        + "\n\t--role <role>\t; one of 'owner', 'reader', 'writer', "
        + "'commenter'";
  }
}
