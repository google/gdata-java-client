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


package sample.contacts;

import com.google.gdata.client.authn.oauth.GoogleOAuthParameters;
import com.google.gdata.client.authn.oauth.OAuthException;
import com.google.gdata.client.authn.oauth.OAuthHmacSha1Signer;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.Status;
import com.google.gdata.util.ServiceException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * UnshareProfiles sample main class.
 *
 * 
 */
public class UnshareProfiles {

  /**
   * Class holding batch processing results.
   *
   * 
   */
  public static class BatchResult {
    private int success;
    private int error;
    private List<ContactEntry> errorEntries = new ArrayList<ContactEntry>();

    /**
     * @return the success
     */
    public int getSuccess() {
      return this.success;
    }

    /**
     * @param success the success to set
     */
    public void setSuccess(int success) {
      this.success = success;
    }

    /**
     * @return the error
     */
    public int getError() {
      return this.error;
    }

    /**
     * @param error the error to set
     */
    public void setError(int error) {
      this.error = error;
    }

    /**
     * @return the errorEntries
     */
    public List<ContactEntry> getErrorEntries() {
      return this.errorEntries;
    }

    public List<ContactEntry> addErrorEntry(ContactEntry erroEntry) {
      this.errorEntries.add(erroEntry);
      return this.errorEntries;
    }
  }

  /**
   * Sample main class. Retrieve all profiles for the domain and unshare their
   * contacts information.
   *
   * 
   */
  public static class ProfilesManager {
    private String domain;
    private String adminEmail;
    private int batchSize = 100;
    private List<ContactEntry> profiles;
    private ContactsService myService;

    /**
     * Constructor initializing the ContactsService object using 2-Legged OAuth authentication
     * @param consumerKey domain consumer key
     * @param consumerSecret domain consumer secret
     * @param adminEmail domain administrator to authenticate as
     */
    public ProfilesManager(String consumerKey, String consumerSecret, String adminEmail)
        throws OAuthException {
      this.adminEmail = adminEmail;
      this.domain = adminEmail.substring(adminEmail.indexOf('@') + 1);

      GoogleOAuthParameters oauthParameters = new GoogleOAuthParameters();
      oauthParameters.setOAuthConsumerKey(consumerKey);
      oauthParameters.setOAuthConsumerSecret(consumerSecret);

      this.myService = new ContactsService("GoogleInc-UnshareProfiles-1");
      this.myService.setOAuthCredentials(oauthParameters, new OAuthHmacSha1Signer());
    }

    /**
     * @return batch processing size
     */
    public int getBatchSize() {
      return this.batchSize;
    }

    /**
     * @param value the batch processing size to set
     */
    public void setBatchSize(int value) {
      this.batchSize = value;
    }

    /**
     * @return domain's profiles list
     */
    public List<ContactEntry> getProfiles() {
      return this.profiles;
    }

    /**
     * Retrieve all porfiles for the domain
     */
    public void getAllProfiles() throws IOException, ServiceException {
      ArrayList<ContactEntry> profiles = new ArrayList<ContactEntry>();

      URL feedUrl =
          new URL("https://www.google.com/m8/feeds/profiles/domain/" + this.domain
              + "/full?xoauth_requestor_id=" + this.adminEmail);
      while (feedUrl != null) {
        ContactFeed resultFeed = this.myService.getFeed(feedUrl, ContactFeed.class);

        profiles.addAll(resultFeed.getEntries());

        if (resultFeed.getNextLink() != null && resultFeed.getNextLink().getHref() != null
            && resultFeed.getNextLink().getHref().length() > 0) {
          feedUrl = new URL(resultFeed.getNextLink().getHref());
        } else {
          feedUrl = null;
        }
      }
      this.profiles = profiles;
    }

    /**
     * Unshare all profiles information from the GAD/Google Apps Directory
     * @return batch processing result
     * @throws IOException
     * @throws ServiceException
     */
    public BatchResult unshareProfiles() throws IOException, ServiceException {
      if (this.profiles == null) {
        this.getAllProfiles();
      }
      BatchResult result = new BatchResult();
      Status status = new Status();
      int index = 0;

      status.setIndexed(false);
      while (index < this.profiles.size()) {
        ContactFeed requestFeed = new ContactFeed();

        for (int i = 0; i < this.batchSize && index < this.profiles.size(); ++i, ++index) {
          ContactEntry entry = this.profiles.get(index);

          entry.setStatus(status);
          BatchUtils.setBatchOperationType(entry, BatchOperationType.UPDATE);
          requestFeed.getEntries().add(entry);
        }

        ContactFeed responseFeed =
            myService.batch(new URL("https://www.google.com/m8/feeds/profiles/domain/"
                + this.domain + "/full/batch?xoauth_requestor_id=" + this.adminEmail), requestFeed);

        // Check the status of each operation.
        for (ContactEntry entry : responseFeed.getEntries()) {
          BatchStatus batchStatus = BatchUtils.getBatchStatus(entry);

          if (batchStatus.getCode() == 200) {
            result.setSuccess(result.getSuccess() + 1);
          } else {
            result.setError(result.getError() + 1);
            result.addErrorEntry(entry);
          }
        }
      }
      return result;
    }
  }

  /**
   * Run the sample app with the provided arguments.
   * @param args
   * @throws OAuthException
   * @throws IOException
   * @throws ServiceException
   */
  public static void main(String[] args) throws OAuthException, IOException, ServiceException {
    if (args.length != 3) {
      System.out.println("Usage: unshare_profile <consumerKey> <consumerSecret> <adminEmail>");
    } else {
      String consumerKey = args[0];
      String consumerSecret = args[1];
      String adminEmail = args[2];
      ProfilesManager manager = new ProfilesManager(consumerKey, consumerSecret, adminEmail);

      BatchResult result = manager.unshareProfiles();

      System.out.println("Success: " + result.getSuccess() + " - Error: " + result.getError());
      for (ContactEntry entry : result.getErrorEntries()) {
        BatchStatus status = BatchUtils.getBatchStatus(entry);

        System.out.println(" > Failed to update " + entry.getId() + ": (" + status.getCode() + ") "
            + status.getReason());
      }
    }
  }
}
