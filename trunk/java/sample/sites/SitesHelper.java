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


package sample.sites;

import com.google.gdata.client.http.HttpGDataRequest;
import com.google.gdata.client.sites.SitesService;
import com.google.gdata.data.Link;
import com.google.gdata.data.MediaContent;
import com.google.gdata.data.OutOfLineContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.XhtmlTextConstruct;
import com.google.gdata.data.acl.AclEntry;
import com.google.gdata.data.acl.AclFeed;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.data.sites.ActivityFeed;
import com.google.gdata.data.sites.AnnouncementEntry;
import com.google.gdata.data.sites.AnnouncementsPageEntry;
import com.google.gdata.data.sites.AttachmentEntry;
import com.google.gdata.data.sites.BaseActivityEntry;
import com.google.gdata.data.sites.BaseContentEntry;
import com.google.gdata.data.sites.BasePageEntry;
import com.google.gdata.data.sites.CommentEntry;
import com.google.gdata.data.sites.ContentFeed;
import com.google.gdata.data.sites.FileCabinetPageEntry;
import com.google.gdata.data.sites.ListItemEntry;
import com.google.gdata.data.sites.ListPageEntry;
import com.google.gdata.data.sites.RevisionFeed;
import com.google.gdata.data.sites.SiteFeed;
import com.google.gdata.data.sites.SiteEntry;
import com.google.gdata.data.sites.SitesLink;
import com.google.gdata.data.sites.Theme;
import com.google.gdata.data.sites.WebAttachmentEntry;
import com.google.gdata.data.sites.WebPageEntry;
import com.google.gdata.data.spreadsheet.Column;
import com.google.gdata.data.spreadsheet.Field;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.util.XmlBlob;
import com.google.gdata.util.XmlParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.MimetypesFileTypeMap;

/**
 * Wrapper class for lower level Sites API calls.
 *
 * 
 */
public class SitesHelper {

    private String domain;
    private String siteName;

    public SitesService service;
    public MimetypesFileTypeMap mediaTypes;

    public static final String[] KINDS = {
        "announcement", "announcementspage", "attachment", "comment",
        "filecabinet", "listitem", "listpage", "webpage", "webattachment"
    };

    /**
     * Constructor
     *
     * @param applicationName The name of the application.
     * @param domain The Site's Google Apps domain or "site".
     * @param siteName The webspace name of the Site.
     */
    public SitesHelper(String applicationName, String domain, String siteName) {
      this(applicationName, domain, siteName, false);
    }

    /**
     * Constructor
     *
     * @param applicationName The name of the application.
     * @param domain The Site's Google Apps domain or "site".
     * @param siteName The webspace name of the Site.
     * @param logging Whether to enable HTTP requests to stdout
     */
    public SitesHelper(String applicationName, String domain, String siteName,
        boolean logging) {
      if (logging) {
        turnOnLogging(false);
      }

      this.domain = domain;
      this.siteName = siteName;
      this.service = new SitesService(applicationName);

      registerMediaTypes();
    }

    private void registerMediaTypes() {
      // Common MIME types used for uploading attachments.
      mediaTypes = new MimetypesFileTypeMap();
      mediaTypes.addMimeTypes("application/msword doc");
      mediaTypes.addMimeTypes("application/vnd.ms-excel xls");
      mediaTypes.addMimeTypes("application/pdf pdf");
      mediaTypes.addMimeTypes("text/richtext rtx");
      mediaTypes.addMimeTypes("text/csv csv");
      mediaTypes.addMimeTypes("text/tab-separated-values tsv tab");
      mediaTypes.addMimeTypes("application/x-vnd.oasis.opendocument.spreadsheet ods");
      mediaTypes.addMimeTypes("application/vnd.oasis.opendocument.text odt");
      mediaTypes.addMimeTypes("application/vnd.ms-powerpoint ppt pps pot");
      mediaTypes.addMimeTypes("application/vnd.openxmlformats-officedocument."
          + "wordprocessingml.document docx");
      mediaTypes.addMimeTypes("application/vnd.openxmlformats-officedocument."
          + "spreadsheetml.sheet xlsx");
      mediaTypes.addMimeTypes("audio/mpeg mp3 mpeg3");
      mediaTypes.addMimeTypes("image/png png");
      mediaTypes.addMimeTypes("application/zip zip");
      mediaTypes.addMimeTypes("application/x-tar tar");
      mediaTypes.addMimeTypes("video/quicktime qt mov moov");
      mediaTypes.addMimeTypes("video/mpeg mpeg mpg mpe mpv vbs mpegv");
      mediaTypes.addMimeTypes("video/msvideo avi");
    }

    /**
     * Authenticates the user using ClientLogin
     *
     * @param username User's email address.
     * @param password User's password.
     */
    public void login(String username, String password) throws AuthenticationException {
      service.setUserCredentials(username, password);
    }

    /**
     * Authenticates the user using AuthSub
     *
     * @param authSubToken A valid AuthSub session token.
     */
    public void login(String authSubToken) {
      service.setAuthSubToken(authSubToken);
    }

    /**
     * Logs HTTP requests and XML to stdout.
     */
    private void turnOnLogging(boolean logXML) {
      // Create a log handler which prints all log events to the console
      ConsoleHandler logHandler = new ConsoleHandler();
      logHandler.setLevel(Level.ALL);

      // Configure the logging mechanisms
      Logger httpLogger =
        Logger.getLogger(HttpGDataRequest.class.getName());
      httpLogger.setLevel(Level.ALL);
      httpLogger.addHandler(logHandler);

      if (logXML) {
        Logger xmlLogger = Logger.getLogger(XmlParser.class.getName());
        xmlLogger.setLevel(Level.ALL);
        xmlLogger.addHandler(logHandler);
      }
    }

    /**
     * Returns an entry's numeric ID.
     */
    private String getEntryId(BaseContentEntry<?> entry) {
      String selfLink = entry.getSelfLink().getHref();
      return selfLink.substring(selfLink.lastIndexOf("/") + 1);
    }

    /**
     * Returns an entry's numeric ID.
     */
    private String getEntryId(String selfLink) {
      return selfLink.substring(selfLink.lastIndexOf("/") + 1);
    }

    public String getContentFeedUrl() {
      return "https://sites.google.com/feeds/content/" + domain + "/" + siteName + "/";
    }

    public String getRevisionFeedUrl() {
      return "https://sites.google.com/feeds/revision/" + domain + "/" + siteName + "/";
    }

    public String getActivityFeedUrl() {
      return "https://sites.google.com/feeds/activity/" + domain + "/" + siteName + "/";
    }

    public String getSiteFeedUrl() {
      return "https://sites.google.com/feeds/site/" + domain + "/";
    }
    
    public String getAclFeedUrl(String siteName) {
      return "https://sites.google.com/feeds/acl/site/" + domain + "/" + siteName + "/";
    }

    /**
     * Fetches and displays the user's site feed.
     */
    public void getSiteFeed() throws IOException, ServiceException {
      SiteFeed siteFeed = service.getFeed(
          new URL(getSiteFeedUrl()), SiteFeed.class);
      for (SiteEntry entry : siteFeed.getEntries()){
        System.out.println("title: " + entry.getTitle().getPlainText());
        System.out.println("site name: " + entry.getSiteName().getValue());
        System.out.println("theme: " + entry.getTheme().getValue());
        System.out.println("");
      }
    }

    /**
     * Fetches and displays a Site's acl feed.
     */
    public void getAclFeed(String siteName) throws IOException, ServiceException {
      AclFeed aclFeed = service.getFeed(
          new URL(getAclFeedUrl(siteName)), AclFeed.class);
      for (AclEntry entry : aclFeed.getEntries()) {
        System.out.println(entry.getScope().getValue() + " (" +
                           entry.getScope().getType() + ") : " + entry.getRole().getValue());
      }
    }

    /**
     * Fetches and displays the Site's activity feed.
     */
    public void getActivityFeed() throws IOException, ServiceException {
      ActivityFeed activityFeed = service.getFeed(
          new URL(getActivityFeedUrl()), ActivityFeed.class);
      for (BaseActivityEntry<?> entry : activityFeed.getEntries()){
        System.out.println(entry.getSummary().getPlainText());
      }
    }

    /**
     * Fetches and displays the revisions feed for an entry.
     */
    public void getRevisionFeed(String contentEntryId) throws IOException, ServiceException {
      URL url = new URL(getRevisionFeedUrl() + contentEntryId);
      RevisionFeed revisionFeed = service.getFeed(url, RevisionFeed.class);
      for (BaseContentEntry<?> entry : revisionFeed.getEntries()) {
        System.out.println(entry.getTitle().getPlainText());
        System.out.println("  updated: " + entry.getUpdated().toUiString() + " by " +
            entry.getAuthors().get(0).getEmail());
        System.out.println("  revision #: " + entry.getRevision().getValue());
      }
    }

    /**
     * Fetches and displays entries from the content feed.
     *
     * @param kind An entry kind to fetch. For example, "webpage". If null, the
     *     entire content feed is returned.
     */
    public void listSiteContents(String kind) throws IOException, ServiceException {
      String url = kind.equals("all") ? getContentFeedUrl() : getContentFeedUrl() + "?kind=" + kind;
      ContentFeed contentFeed = service.getFeed(new URL(url), ContentFeed.class);

      for (WebPageEntry entry : contentFeed.getEntries(WebPageEntry.class)) {
        System.out.println("WebPageEntry:");
        System.out.println("  title: " + entry.getTitle().getPlainText());
        System.out.println("  id: " + getEntryId(entry));
        if (entry.getParentLink() != null) {
          System.out.println("  parent id: " + getEntryId(entry.getParentLink().getHref()));
        }
        System.out.println("  authors: " + entry.getAuthors().get(0).getEmail());
        System.out.println("  content: " + getContentBlob(entry));
        System.out.println("");
      }

      for (ListPageEntry entry : contentFeed.getEntries(ListPageEntry.class)) {
        System.out.println("ListPageEntry:");
        System.out.println("  title: " + entry.getTitle().getPlainText());
        System.out.println("  id: " + getEntryId(entry));
        if (entry.getParentLink() != null) {
          System.out.println("  parent id: " + getEntryId(entry.getParentLink().getHref()));
        }
        for (Column col : entry.getData().getColumns()) {
          System.out.print("  [" + col.getIndex() + "] " + col.getName() + "\t");
        }
        System.out.println("");
      }

      for (ListItemEntry entry : contentFeed.getEntries(ListItemEntry.class)) {
        if (entry.getParentLink() != null) {
          System.out.println("  parent id: " + getEntryId(entry.getParentLink().getHref()));
        }
        for (Field field : entry.getFields()) {
          System.out.print("  [" + field.getIndex() + "] " + field.getValue() + "\t");
        }
        System.out.println("\n");
      }

      for (FileCabinetPageEntry entry : contentFeed.getEntries(FileCabinetPageEntry.class)) {
        System.out.println("FileCabinetPageEntry:");
        System.out.println("  title: " + entry.getTitle().getPlainText());
        System.out.println("  id: " + getEntryId(entry));
        if (entry.getParentLink() != null) {
          System.out.println("  parent id: " + getEntryId(entry.getParentLink().getHref()));
        }
        System.out.println("  content: " + getContentBlob(entry));
        System.out.println("");
      }

      for (CommentEntry entry : contentFeed.getEntries(CommentEntry.class)) {
        System.out.println("CommentEntry:");
        System.out.println("  id: " + getEntryId(entry));
        if (entry.getParentLink() != null) {
          System.out.println("  parent id: " + getEntryId(entry.getParentLink().getHref()));
        }
        System.out.println("  in-reply-to: " + entry.getInReplyTo().toString());
        System.out.println("  content: " + getContentBlob(entry));
        System.out.println("");
      }

      for (AnnouncementsPageEntry entry : contentFeed.getEntries(AnnouncementsPageEntry.class)) {
        System.out.println("AnnouncementsPageEntry:");
        System.out.println("  title: " + entry.getTitle().getPlainText());
        System.out.println("  id: " + getEntryId(entry));
        if (entry.getParentLink() != null) {
          System.out.println("  parent id: " + getEntryId(entry.getParentLink().getHref()));
        }
        System.out.println("  content: " + getContentBlob(entry));
        System.out.println("");
      }

      for (AnnouncementEntry entry : contentFeed.getEntries(AnnouncementEntry.class)) {
        System.out.println("AnnouncementEntry:");
        System.out.println("  title: " + entry.getTitle().getPlainText());
        System.out.println("  id: " + getEntryId(entry));
        if (entry.getParentLink() != null) {
          System.out.println("  parent id: " + getEntryId(entry.getParentLink().getHref()));
        }
        System.out.println("  draft?: " + entry.isDraft());
        System.out.println("  content: " + getContentBlob(entry));
        System.out.println("");
      }

      for (AttachmentEntry entry : contentFeed.getEntries(AttachmentEntry.class)) {
        System.out.println("AttachmentEntry:");
        System.out.println("  title: " + entry.getTitle().getPlainText());
        System.out.println("  id: " + getEntryId(entry));
        if (entry.getParentLink() != null) {
          System.out.println("  parent id: " + getEntryId(entry.getParentLink().getHref()));
        }
        if (entry.getSummary() != null) {
          System.out.println("  description: " + entry.getSummary().getPlainText());
        }
        System.out.println("  revision: " + entry.getRevision().getValue());
        MediaContent content = (MediaContent) entry.getContent();
        System.out.println("  src: " + content.getUri());
        System.out.println("  content type: " + content.getMimeType().getMediaType());
        System.out.println("");
      }

      for (WebAttachmentEntry entry : contentFeed.getEntries(WebAttachmentEntry.class)) {
        System.out.println("WebAttachmentEntry:");
        System.out.println("  title: " + entry.getTitle().getPlainText());
        System.out.println("  id: " + getEntryId(entry));
        if (entry.getParentLink() != null) {
          System.out.println("  parent id: " + getEntryId(entry.getParentLink().getHref()));
        }
        if (entry.getSummary() != null) {
          System.out.println("  description: " + entry.getSummary().getPlainText());
        }
        System.out.println("  src: " + ((MediaContent) entry.getContent()).getUri());
        System.out.println("");
      }
    }

    public String getContentBlob(BaseContentEntry<?> entry) {
     return ((XhtmlTextConstruct) entry.getTextContent().getContent()).getXhtml().getBlob();
    }

    private void setContentBlob(BaseContentEntry<?> entry) {
      XmlBlob xml = new XmlBlob();
      xml.setBlob(String.format(
          "content for %s", entry.getCategories().iterator().next().getLabel()));
      entry.setContent(new XhtmlTextConstruct(xml));
    }

    public SiteEntry createSite(String title, String summary)
        throws MalformedURLException, IOException, ServiceException {
      return createSite(title, summary, null);
    }

    /**
     * Creates a new Google Sites.
     *
     * @param title A name for the site.
     * @param summary A description for the site.
     * @param theme A theme to create the site with.
     * @return The created site entry.
     * @throws ServiceException 
     * @throws IOException 
     * @throws MalformedURLException 
     */
    public SiteEntry createSite(String title, String summary, String theme)
        throws MalformedURLException, IOException, ServiceException {
      SiteEntry entry = new SiteEntry();
      entry.setTitle(new PlainTextConstruct(title));
      entry.setSummary(new PlainTextConstruct(summary));

      // Set theme if user specified it.
      if (theme !=  null) {
        Theme tt = new Theme();
        tt.setValue(theme);
        entry.setTheme(tt);
      }

      return service.insert(new URL(getSiteFeedUrl()), entry);
    }

    /**
     * Copies a Google Site from an existing site.
     *
     * @param title A title heading for the new site.
     * @param summary A description for the site.
     * @param theme A theme to create the site with.
     * @param sourceHref The self link of the site entry to copy this site from.
     *     If null, an empty site is created.
     * @return The created site entry.
     * @throws ServiceException 
     * @throws IOException 
     * @throws MalformedURLException 
     */
    public SiteEntry copySite(String title, String summary, String sourceHref)
        throws MalformedURLException, IOException, ServiceException {
      SiteEntry entry = new SiteEntry();
      entry.setTitle(new PlainTextConstruct(title));
      entry.setSummary(new PlainTextConstruct(summary));
      entry.addLink(SitesLink.Rel.SOURCE, Link.Type.ATOM, sourceHref);

      return service.insert(new URL(getSiteFeedUrl()), entry);
    }

    public BaseContentEntry<?> createPage(String kind, String title)
        throws MalformedURLException, SitesException, IOException, ServiceException {
      return createPage(kind, title, null);
    }

    /**
     * Creates a new (sub)page.
     *
     * @param kind The type of page to created (e.g. webpage, filecabinet, listpage, etc).
     * @param title A title heading for the page
     * @param parent The full URL of the parent to create the subpage under
     *     or the parent entry's ID (e.g. 1234567890).
     * @return The created entry.
     * @throws SitesException
     * @throws ServiceException
     * @throws IOException
     * @throws MalformedURLException
     */
    public BaseContentEntry<?> createPage(String kind, String title, String parent)
        throws SitesException, MalformedURLException, IOException, ServiceException {
      BaseContentEntry<?> entry = null;
      if (kind.equals("announcement")) {
        entry = new AnnouncementEntry();
      } else if (kind.equals("announcementspage")) {
        entry = new AnnouncementsPageEntry();
      } else if (kind.equals("comment")) {
        entry = new CommentEntry();
      } else if (kind.equals("filecabinet")) {
        entry = new FileCabinetPageEntry();
      } else if (kind.equals("listitem")) {
        entry = new ListItemEntry();
      } else if (kind.equals("listpage")) {
        entry = new ListPageEntry();
      } else if (kind.equals("webpage")) {
        entry = new WebPageEntry();
      } else {
        if (kind.equals("attachment") || kind.equals("webattachment")) {
          throw new SitesException("Trying to create " + kind
                                   + ". Please use upload command instead.");
        } else {
          throw new SitesException("Unknown kind '" + kind + "'");
        }
      }

      entry.setTitle(new PlainTextConstruct(title));
      setContentBlob(entry);

      // Upload to a parent page?
      if (parent !=  null) {
        if (parent.lastIndexOf("/") == -1) {
          parent = getContentFeedUrl() + parent;
        }
        entry.addLink(SitesLink.Rel.PARENT, Link.Type.ATOM, parent);
      }

      return service.insert(new URL(getContentFeedUrl()), entry);
    }

    /**
     * Downloads a file from the specified URL to disk.
     *
     * @param downloadUrl The full URL to download the file from.
     * @param fullFilePath The local path to save the file to on disk.
     * @throws ServiceException
     * @throws IOException
     */
    private void downloadFile(String downloadUrl, String fullFilePath) throws IOException,
        ServiceException {
      System.out.println("Downloading file from: " + downloadUrl);

      MediaContent mc = new MediaContent();
      mc.setUri(downloadUrl);
      MediaSource ms = service.getMedia(mc);

      InputStream inStream = null;
      FileOutputStream outStream = null;

      try {
        inStream = ms.getInputStream();
        outStream = new FileOutputStream(fullFilePath);

        int c;
        while ((c = inStream.read()) != -1) {
          outStream.write(c);
        }
      } finally {
        if (inStream != null) {
          inStream.close();
        }
        if (outStream != null) {
          outStream.flush();
          outStream.close();
        }
      }
    }

    /**
     * Downloads an attachment.
     *
     * @param entry The attachment (entry) to download.
     * @param directory Path to a local directory to download the attach to.
     * @throws ServiceException
     * @throws IOException
     */
    public void downloadAttachment(AttachmentEntry entry, String directory)
        throws IOException, ServiceException {
      String url = ((OutOfLineContent) entry.getContent()).getUri();
      downloadFile(url, directory + entry.getTitle().getPlainText());
    }

    /**
     * Downloads an attachment.
     *
     * @param entry The attachment (entry) to download.
     * @param entryId The content entry id of the attachment (e.g. 1234567890)
     * @throws ServiceException
     * @throws IOException
     */
    public void downloadAttachment(String entryId, String directory)
        throws IOException, ServiceException {
      AttachmentEntry attachment = service.getEntry(
          new URL(getContentFeedUrl() + entryId), AttachmentEntry.class);
      String url = ((OutOfLineContent) attachment.getContent()).getUri();
      ///*MediaSource mediaSource = service.getMedia((MediaContent) entry.getContent());*/
      downloadFile(url, directory + attachment.getTitle().getPlainText());
    }

    /**
     * Downloads all attachments from a Site and assumes they all have different names.
     *
     * @param entry The attachment (entry) to download.
     * @param directory Path to a local directory to download the attach to.
     * @throws ServiceException
     * @throws IOException
     */
    public void downloadAllAttachments(String directory) throws IOException, ServiceException {
      URL contentFeedUrl = new URL(getContentFeedUrl() + "?kind=attachment");
      ContentFeed contentFeed = service.getFeed(contentFeedUrl, ContentFeed.class);
      for (AttachmentEntry entry : contentFeed.getEntries(AttachmentEntry.class)) {
        downloadAttachment(entry, directory);
      }
    }

    /**
     * Uploads an attachment to a selected parent page.
     *
     * @param file The file contents to upload.
     * @param parentPage Entry of the page to upload the attachment to.
     * @return Created attachment entry.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry uploadAttachment(File file, BasePageEntry<?> parentPage)
        throws IOException, ServiceException {
      return uploadAttachment(file, parentPage.getSelfLink().getHref(), file.getName(), "");
    }

    /**
     * Uploads an attachment to a selected parent page.
     *
     * @param file The file contents to upload,
     * @param parentPage Entry of the page to upload the attachment to.
     * @param title A title to name the attachment.
     * @return Created attachment entry.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry uploadAttachment(File file, BasePageEntry<?> parentPage, String title)
        throws IOException, ServiceException {
      return uploadAttachment(file, parentPage.getSelfLink().getHref(), title, "");
    }

    /**
     * Uploads an attachment to a selected parent page.
     *
     * @param file The file contents to upload.
     * @param parentPage Entry of the page to upload the attachment to.
     * @param title A title to name the attachment.
     * @param description A description for the file.
     * @return Created attachment entry.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry uploadAttachment(File file, BasePageEntry<?> parentPage,
        String title, String description) throws IOException, ServiceException {
      return uploadAttachment(file, parentPage.getSelfLink().getHref(), title, description);
    }

    /**
     * Uploads an attachment to a selected parent page.
     *
     * @param filename Path of the file to upload.
     * @param parentPage Entry of the page to upload the attachment to.
     * @return Created attachment entry.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry uploadAttachment(String filename, BasePageEntry<?> parentPage)
        throws IOException, ServiceException {
      File file = new File(filename);
      return uploadAttachment(file, parentPage.getSelfLink().getHref(), file.getName(), "");
    }

    /**
     * Uploads an attachment to a selected parent page.
     *
     * @param filename Path of the file to upload.
     * @param parentUrl Full self id of the parent entry to upload the attachmen to.
     * @param description A file description.
     * @return Created attachment entry.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry uploadAttachment(String filename, String parentUrl, String description)
        throws IOException, ServiceException {
      File file = new File(filename);
      return uploadAttachment(file, parentUrl, file.getName(), description);
    }

    /**
     * Uploads an attachment to a parent page using the file name for a title.
     *
     * @param file The file contents to upload.
     * @param parentUrl Feed URL of the page to upload the attachment to.
     * @return Created attachment entry.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry uploadAttachment(File file, String parentUrl)
        throws IOException, ServiceException  {
      return uploadAttachment(file, parentUrl, file.getName(), "");
    }

    /**
     * Uploads an attachment to a parent page using the file name for a title.
     *
     * @param file The file contents to upload
     * @param parentLink Full self id of the parent entry to upload the attach to.
     * @param description A file description.
     * @return The created attachment entry.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry uploadAttachment(File file, String parentLink, String description)
        throws IOException, ServiceException  {
      return uploadAttachment(file, parentLink, file.getName(), description);
    }

    /**
     * Uploads an attachment to a parent page.
     *
     * @param file The file contents to upload
     * @param parentLink Full self id of the parent entry to upload the attach to.
     * @param title A title for the attachment.
     * @param description A description for the attachment.
     * @return The created attachment entry.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry uploadAttachment(File file, String parentLink, String title,
        String description) throws IOException, ServiceException  {
      String fileMimeType = mediaTypes.getContentType(file);

      AttachmentEntry newAttachment = new AttachmentEntry();
      newAttachment.setMediaSource(new MediaFileSource(file, fileMimeType));
      newAttachment.setTitle(new PlainTextConstruct(title));
      newAttachment.setSummary(new PlainTextConstruct(description));
      newAttachment.addLink(SitesLink.Rel.PARENT, Link.Type.ATOM, parentLink);

      return service.insert(new URL(getContentFeedUrl()), newAttachment);
    }

    /**
     * Creates a web attachment under the selected file cabinet.
     *
     * @param contentUrl The full URL of the hosted file.
     * @param filecabinet File cabinet to create the web attachment on.
     * @param title A title for the attachment.
     * @param description A description for the attachment.
     * @return The created web attachment.
     * @throws ServiceException
     * @throws IOException
     * @throws MalformedURLException
     */
    public WebAttachmentEntry uploadWebAttachment(String contentUrl,
        FileCabinetPageEntry filecabinet, String title, String description)
        throws MalformedURLException, IOException, ServiceException {
      MediaContent content = new MediaContent();
      content.setUri(contentUrl);

      WebAttachmentEntry webAttachment = new WebAttachmentEntry();
      webAttachment.setTitle(new PlainTextConstruct(title));
      webAttachment.setSummary(new PlainTextConstruct(description));
      webAttachment.setContent(content);
      webAttachment.addLink(SitesLink.Rel.PARENT, Link.Type.ATOM,
          filecabinet.getSelfLink().getHref());

      return service.insert(new URL(getContentFeedUrl()), webAttachment);
    }

    /**
     * Updates an existing attachment with new content.
     *
     * @param entry Attachment entry to update.
     * @param newFile The replacement file content.
     * @return The created attachment.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry updateAttachment(AttachmentEntry entry, File newFile)
        throws IOException, ServiceException  {
      return updateAttachment(entry, newFile, null, null);
    }

    /**
     * Updates an existing attachment's metadata.
     *
     * @param entry Attachment entry to update.
     * @param newTitle A new title for the attachment.
     * @param newDescription A new description for the attachment.
     * @return The created attachment.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry updateAttachment(AttachmentEntry entry, String newTitle,
        String newDescription) throws IOException, ServiceException  {
      entry.setTitle(new PlainTextConstruct(newTitle));
      entry.setSummary(new PlainTextConstruct(newDescription));

      return entry.update();
    }

    /**
     * Updates an existing attachment's metadata and content.
     *
     * @param entry Attachment entry to update.
     * @param newFile The replacement file content.
     * @param newTitle A new title for the attachment.
     * @param newDescription A new description for the attachment.
     * @return The created attachment.
     * @throws ServiceException
     * @throws IOException
     */
    public AttachmentEntry updateAttachment(AttachmentEntry entry, File newFile,
        String newTitle, String newDescription) throws IOException, ServiceException  {
      entry.setMediaSource(new MediaFileSource(newFile, mediaTypes.getContentType(newFile)));
      if (newTitle != null) {
        entry.setTitle(new PlainTextConstruct(newTitle));
      }
      if (newDescription != null) {
        entry.setSummary(new PlainTextConstruct(newDescription));
      }

      return entry.updateMedia(true);
    }

}
