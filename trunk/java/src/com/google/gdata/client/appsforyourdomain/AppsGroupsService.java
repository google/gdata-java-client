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


package com.google.gdata.client.appsforyourdomain;

import com.google.gdata.data.Link;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainErrorCode;
import com.google.gdata.data.appsforyourdomain.AppsForYourDomainException;
import com.google.gdata.data.appsforyourdomain.generic.GenericEntry;
import com.google.gdata.data.appsforyourdomain.generic.GenericFeed;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Service class specializing AppsPropertyService for groups management
 * 
 * 
 * 
 */
public class AppsGroupsService extends AppsPropertyService {

  public static final String BASE_URL =
      "https://apps-apis.google.com/a/feeds/group/2.0/";
  public final String baseDomainUrl;

  public static final String APPS_PROP_GROUP_ID = "groupId";
  public static final String APPS_PROP_GROUP_NAME = "groupName";
  public static final String APPS_PROP_GROUP_DESC = "description";
  public static final String APPS_PROP_GROUP_PERMISSION = "emailPermission";
  public static final String APPS_PROP_GROUP_MEMBER_ID = "memberId";
  public static final String APPS_PROP_GROUP_ROLE = "role";
  public static final String APPS_PROP_GROUP_EMAIL = "email";

  /**
   * Parameterized constructor to setup a Service object with user credentials.
   * 
   * @param adminUser Administrator username
   * @param adminPassword Administrator password
   * @param domain Domain being configured
   * @param applicationName Application name consuming the API
   * @throws AuthenticationException If an authentication error occurs
   */
  public AppsGroupsService(String adminUser, String adminPassword,
      String domain, String applicationName) throws AuthenticationException {
    this(domain, applicationName);
    setUserCredentials(adminUser, adminPassword);
  }

  /**
   * Parameterized constructor to setup a Service object which can be used to
   * initialize the service without obtaining a token. The user should
   * explicitly authorize the service by calling either
   * {@code setUserCredentials} or {@code setUserToken} when using this
   * constructor.
   *
   * @param domain Domain being configured
   * @param applicationName Application name consuming the API
   * @throws AuthenticationException If an authentication error occurs
   */
  public AppsGroupsService(String domain, String applicationName)
      throws AuthenticationException {
    super(applicationName);
    baseDomainUrl = BASE_URL + domain + "/";
  }

  /**
   * Get the next page from the atom:next link
   * 
   * @param atomLink The feed link for next page
   * @return A GenericFeed of entries of next page from the atom:next link
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed getNextPage(Link atomLink)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return getFeed(new URL(atomLink.getHref()), GenericFeed.class);
  }

  /**
   * Checks for an available feed from a given Url. This method is used to
   * discover members and owners
   * 
   * @param feedUrlSuffix The suffix of the feed URL to be investigated.
   * @return True if entity exists, false otherwise
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public boolean doesEntityExist(String feedUrlSuffix)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    try {
      GenericEntry entry = getEntry(new URL(baseDomainUrl + feedUrlSuffix),
          GenericEntry.class);
      return (entry.getAllProperties().size() > 0);
    } catch (AppsForYourDomainException e) {
      if (e.getErrorCode() == AppsForYourDomainErrorCode.EntityDoesNotExist) {
        return false;
      } else {
        throw e;
      }
    }
  }

  /**
   * Checks if an user/group is a member of the group specified.
   * 
   * @param groupId Group to be checked for
   * @param memberId Username/Group
   * @return True, if given entity is a member
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public boolean isMember(String groupId, String memberId)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return doesEntityExist(groupId  + "/member/" + memberId);
  }

  /**
   * Checks if an user is a owner of the group specified.
   * 
   * @param groupId Group ownership to be checked for
   * @param email Owner's email
   * @return True, if given entity is an owner
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public boolean isOwner(String groupId, String email)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return doesEntityExist(groupId  + "/owner/" + email);
  }

  /**
   * Creates a new group in the domain.
   * 
   * @param groupId A unique identifier for the group
   * @param groupName Name of the group
   * @param groupDescription A description for the group
   * @param emailPermission Email permission for user
   * @return GenericEntry object with details of the newly created group
   * 
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry createGroup(String groupId, String groupName,
      String groupDescription, String emailPermission)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {

    GenericEntry entry = new GenericEntry();
    entry.addProperty(APPS_PROP_GROUP_ID, groupId);
    entry.addProperty(APPS_PROP_GROUP_NAME, groupName);
    entry.addProperty(APPS_PROP_GROUP_DESC, groupDescription);
    entry.addProperty(APPS_PROP_GROUP_PERMISSION, emailPermission);
    return insert(new URL(baseDomainUrl), entry);
  }

  /**
   * Updates the group properties. When a null or empty value is sent, the field
   * is not updated the old value is preserved. Sending all null or empty values
   * will not update the group and will fetch the original group entry.
   * 
   * @param groupId Unique ID of the group
   * @param groupName Name of the group. A null or empty group name will
   *        preserve the old value
   * @param groupDescription Description of the group. A null or empty value
   *        will preserve the old value
   * @param emailPermission Email permissions associated with the group. A null
   *        or empty value will preserve the old value
   * @return GenericEntry object with updated details of the group
   * 
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry updateGroup(String groupId, String groupName,
      String groupDescription, String emailPermission)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    GenericEntry entry = new GenericEntry();
    if (!(groupName == null || groupName.equals(""))) {
      entry.addProperty(APPS_PROP_GROUP_NAME, groupName);
    }
    if (!(groupDescription == null || groupDescription.equals(""))) {
      entry.addProperty(APPS_PROP_GROUP_DESC, groupDescription);
    }
    if (!(emailPermission == null || emailPermission.equals(""))) {
      entry.addProperty(APPS_PROP_GROUP_PERMISSION, emailPermission);
    }
    if (entry.getAllProperties().size() == 0) {
      // All null or empty values. Nothing will be updated.
      return retrieveGroup(groupId);
    }
    return update(new URL(baseDomainUrl + groupId), entry);
  }

  /**
   * Deletes a group from the domain. Deleting the group does not delete 
   * the user accounts for group members.
   *
   * @param groupId Unique ID of the group
   *
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public void deleteGroup(String groupId) throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    delete(new URL(baseDomainUrl + groupId));

  }

  /**
   * Retrieves the group details.
   * 
   * @param groupId Unique ID of the group
   * @return GenericEntry with the details of the group.
   * 
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry retrieveGroup(String groupId)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return getEntry(new URL(baseDomainUrl + groupId), GenericEntry.class);
  }

  /**
   * Adds a new member to the group. The member can either be a user/group in 
   * the domain or an arbitrary email address
   * 
   * @param groupId Unique ID of the group
   * @param memberName Member username, group name or an email address
   * @return GenericEntry object with details of the newly added member
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry addMemberToGroup(String groupId, String memberName)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty(APPS_PROP_GROUP_MEMBER_ID, memberName);
    return insert(new URL(baseDomainUrl + groupId + "/member"), entry);
  }

  /**
   * Removes member subscription to the group.
   * 
   * @param groupId Unique ID of the group
   * @param memberName Member username or group name
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException
   * 
   */
  public void deleteMemberFromGroup(String groupId, String memberName)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    delete(new URL(baseDomainUrl + groupId + "/member/" + memberName));
  }

  /**
   * Retrieves all the members of a group as a GenericFeed.
   * 
   * @param groupId Unique ID of the group
   * @return List of GenericEntry objects containing member details
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException
   */
  public GenericFeed retrieveAllMembers(String groupId)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {

    return getFeed(new URL(baseDomainUrl + groupId + "/member"),
        GenericFeed.class);
  }

  /**
   * Retrieves next page of members of a group as a GenericFeed.
   * 
   * @param next Atom link to the next page
   * @return List of GenericEntry objects containing member details
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException
   */
  public GenericFeed retrievePageOfMembers(Link next)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return getNextPage(next);

  }

  /**
   * Adds a new member as owner to the group. The owner can either be user or
   * another group. This does not add the owner to the member feed. The
   * owners can be retrieved from the owner feed.
   *
   * @param groupId Unique ID of the group
   * @param ownerName Member username or group name
   * @return GenericEntry object with details of the newly added owner
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericEntry addOwnerToGroup(String groupId, String ownerName)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    GenericEntry entry = new GenericEntry();
    entry.addProperty("email", ownerName);
    return insert(new URL(baseDomainUrl + groupId + "/owner"), entry);
  }

  /**
   * Removes an owner from the group. 
   *
   * @param email Owner email address
   * @param groupId Unique ID of the group
   *
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public void removeOwnerFromGroup(String email, String groupId)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    delete(new URL(baseDomainUrl + groupId + "/owner/" + email));
  }

  /**
   * Retrieves all groups in a domain as a GenericFeed. The client should
   * follow the next feed link to retrieve subsequent pages.
   * 
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed retrieveAllGroups() throws AppsForYourDomainException,
      MalformedURLException, IOException, ServiceException {
    return getFeed(new URL(baseDomainUrl), GenericFeed.class);
  }

  /**
   * Retrieves next page of groups in a domain as a GenericFeed.
   * 
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed retrievePageOfGroups(Link next)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return getNextPage(next);
  }

  /**
   * Retrieves all groups of the given member.
   * 
   * @param memberName Member username
   * @param directOnly If true, members with direct association only will be
   *        considered
   * @return List of GenericEntry with membership details.
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed retrieveGroups(String memberName, boolean directOnly)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return getFeed(new URL(baseDomainUrl + "?member=" + memberName
        + "&directOnly=" + String.valueOf(directOnly)), GenericFeed.class);
  }

  /**
   * Retrieves all the owners of a given Group.
   * 
   * @param groupId The unique ID of the group
   * @throws AppsForYourDomainException If a Provisioning API error occurs
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public GenericFeed retreiveGroupOwners(String groupId)
      throws AppsForYourDomainException, MalformedURLException, IOException,
      ServiceException {
    return getFeed(new URL(baseDomainUrl + groupId + "/owner"),
        GenericFeed.class);
  }

  /**
   * Retrieves all the pages of the groups feed. This method can be used by
   * clients to retrieve a List of entries from all the pages of group feed.
   * 
   * @return List of GenericEntry instances 
   * @throws MalformedURLException If a URL related error occurs
   * @throws IOException If a network I/O related error occurs
   * @throws ServiceException If the API service fails
   */
  public List<GenericEntry> retrieveAllPagesOfGroups() 
      throws MalformedURLException, IOException, ServiceException {
    return getAllPages(new URL(baseDomainUrl), GenericFeed.class);
  }
}
