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

// All Rights Reserved.

package sample.maps;

import com.google.gdata.util.common.base.StringUtil;
import com.google.gdata.util.common.xml.XmlWriter;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.maps.MapsService;
import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.extensions.CustomProperty;
import com.google.gdata.data.extensions.ResourceId;
import com.google.gdata.data.maps.FeatureEntry;
import com.google.gdata.data.maps.FeatureFeed;
import com.google.gdata.data.maps.MapEntry;
import com.google.gdata.data.maps.MapFeed;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Sample client for Maps GData API.
 *
 * maps VERB FEED FLAG*
 *   VERB is one of {query, create, read, update, delete, batch, clear}
 *   FEED is one of {maps, features}
 *   FLAG (note the non-gnu style) can be
 *   -chunk CHUNK-SIZE
 *   -content KML-CONTENT
 *   -count CHUNK-COUNT
 *   -fid FEATURE-ID
 *   -file FILENAME
 *   -host HOST:PORT
 *   -id ATOM-ID
 *   -maxresults MAX-RESULTS
 *   -mid MAP-ID
 *   -output OUTPUT-FILENAME
 *   -password PASSWORD
 *   -previd PREV_ID
 *   -preview
 *   -projection (one of full, public, unlisted, owned, bookmarked)
 *   -prop NAME VALUE
 *   -sep SEPARATOR (default ".")
 *   -startindex START-INDEX
 *   -title TITLE
 *   -uid USER-ID (default "default")
 *   -user USER-EMAIL
 *   -v VERSION
 */
public class Maps {

  private static final HashSet XML_PP = new HashSet(Arrays.asList(
      XmlWriter.WriterFlags.PRETTY_PRINT));

  private static  boolean draft = false;

  protected Maps() {
  }

  /**
   * Generic client for Maps API.
   */
  public static class ApiClient<E extends BaseEntry, F extends BaseFeed> {

    private Class<E> entryClass;
    private Class<F> feedClass;
    private MapsService maps;
    private String base;        // URI of base feed
    private String uriFormat;
    private List<String> params;

    /**
     * @param entryClass must be supplied, used to direct Atom parser
     * @param feedClass must be supplied, used to direct Atom parser
     */
    private ApiClient(Class<E> entryClass, Class<F> feedClass, String uriFormat,
                      List<String> params) {
      this.entryClass = entryClass;
      this.feedClass = feedClass;
      this.uriFormat = uriFormat;
      this.params = params;
    }

    private MapsService authenticate(String base, String u, String p)
        throws Exception {
      this.base = base;
      maps = new MapsService("client");
      if (!StringUtil.isEmpty(u) && !StringUtil.isEmpty(p)) {
        maps.setUserCredentials(u, p);
      }
      return maps;
    }

    private F createFeed(F feed, String... id) throws Exception {
      return maps.batch(makeUri(id), feed);
    }

    private F readFeed(String... id) throws Exception {
      return maps.getFeed(makeUri(id), feedClass);
    }

    private E createEntry(E entry, String... id) throws Exception {
      return maps.insert(makeUri(id), entry);
    }

    private E readEntry(String... id) throws Exception {
      return maps.getEntry(makeUri(id), entryClass);
    }

    private E updateEntry(E entry, String... id) throws Exception {
      return maps.update(makeUri(id), entry);
    }

    /**
     * Attempt to delete an entry.
     *
     * @param id list of id parts
     * @return null on success, an error message on failure
     */
    private String deleteEntry(String... id) {
      try {
        maps.delete(makeUri(id));
        return null;
      } catch (Throwable t) {
        return t.getMessage();
      }
    }

    /**
     * Construct a URI from a list of id components.
     */
    private URL makeUri(String... id) throws Exception {
      String uri = base + String.format(uriFormat, id);
      boolean first = true;
      for (String param : params) {
        uri += (first ? "?" : "&") + param;
        first = false;
      }
      System.err.println(uri);
      return new URL(uri);
    }

    /**
     * Hack helper to allow callers to provide one less param on query.
     */
    private void trimFormat(String tail) {
      if (uriFormat.endsWith(tail)) {
        uriFormat = uriFormat.substring(0, uriFormat.lastIndexOf(tail));
      } else {
        System.err.println(uriFormat + " has no " + tail);
      }
    }

    private void addFormat(String tail) {
      uriFormat += tail;
    }

    private E parseAtom(InputStream is) throws Exception {
      E entry = entryClass.newInstance();
      entry.parseAtom(maps.getExtensionProfile(), is);
      return entry;
    }

    private F parseFeed(InputStream is) throws Exception {
      F feed = feedClass.newInstance();
      feed.parseAtom(maps.getExtensionProfile(), is);
      return feed;
    }

    public E buildEntry(String title, String content, String summary)
        throws Exception {
      E entry = entryClass.newInstance();
      entry.setTitle(new PlainTextConstruct(title));
      if (!StringUtil.isEmpty(content)) {
        entry.setContent(new PlainTextConstruct(content));
      }
      if (!StringUtil.isEmpty(summary)) {
        entry.setSummary(new PlainTextConstruct(summary));
      }
      if (draft) {
        entry.setDraft(true);
      }
      return entry;
    }

    public E newEntry() throws Exception {
      return entryClass.newInstance();
    }

    public F newFeed() throws Exception {
      return feedClass.newInstance();
    }
  }

  /** Pretty-print a feed. */
  private static void pp(GoogleService service, BaseFeed feed, Writer w)
      throws IOException {
    XmlWriter xw = new XmlWriter(w, XML_PP, null);
    ExtensionProfile extProfile = service.getExtensionProfile();
    feed.generateAtom(xw, extProfile);
    xw.flush();
    w.write("\n");
    w.flush();
  }

  /** Pretty-print an entry. */
  private static void pp(GoogleService service, BaseEntry entry, Writer w)
      throws IOException {
    XmlWriter xw = new XmlWriter(w, XML_PP, null);
    ExtensionProfile extProfile = service.getExtensionProfile();
    entry.generateAtom(xw, extProfile);
    xw.flush();
    w.write("\n");
    w.flush();
  }

  /**
   * Create an Entry from an Atom input stream.
   *
   * @param actor
   * @param is
   * @param maps
   * @param preview if true, print parsed entry to pw
   * @param pw
   * @return
   * @throws Exception
   */
  private static BaseEntry parseEntry(ApiClient actor, InputStream is,
      MapsService maps, boolean preview, PrintWriter pw) throws Exception {
    BaseEntry entry;
    entry = actor.parseAtom(is);
    if (draft) {
      entry.setDraft(true);
    }
    if (preview) {
      pp(maps, entry, pw);
    }
    return entry;
  }

  /**
   * Create a Feed from an Atom input stream.
   *
   * @param actor
   * @param is
   * @param maps
   * @param preview if true, print parsed entry to pw
   * @param pw
   * @return
   * @throws Exception
   */
  private static BaseFeed parseFeed(ApiClient actor, InputStream is,
      MapsService maps, boolean preview, PrintWriter pw) throws Exception {
    BaseFeed feed;
    feed = actor.parseFeed(is);
    if (preview) {
      pp(maps, feed, pw);
    }
    return feed;
  }

  enum Action {query, create, read, update, delete, batch, clear, bulk, rawpost, revise};
  enum Feed {maps, features};
  enum Arg {
    CHUNK,      // bulk chunk size
    CONTENT,    // kml content
    COUNT,      // bulk chunk count
    DRAFT,      // true if a private map
    FID,        // feature id
    FILE,       // file name for uploads
    FUDGE,      // mess with stuff
    HOST,       // name of API server
    ID,         // atom id
    MAXRESULTS, // limit number of results (query)
    MID,        // map id
    OUTPUT,     // output filename
    PASSWORD,   // for authentication
    PREVID,     // previd (previous id)
    PREVIEW,    // show upload data before sending
    PROJECTION,  // map feed projection
    PROP,       // custom property
    SEP,        // uid/mid separator (default ".")
    STARTINDEX, // starting index (query)
    SUMMARY,    // atom:summary
    TITLE,      // of a new map or feature
    UID,        // user id ("default" by default)
    USER,       // for authentication
    V           // version
  };

  public static void main(String arg[]) throws Exception {
    Action action = enumValueOrDie(Action.class, arg[0]);
    Feed feed = enumValueOrDie(Feed.class, arg[1]);

    String content = " ";             // avoid creating ghost maps
    String fid = "";
    String host = "maps.google.com";
    String id = "";                   // client-supplied id
    String mid = "";
    String password = "";
    String previd = null;
    String projection = "full";
    String sep = "/";
    String summary = null;
    String title = null;
    String uid = "default";
    String user = "";
    boolean fudge = false;
    boolean preview = false;
    int chunk = 10;
    int count = 10;
    int maxResults = 0;
    int startIndex = 0;
    int version = 2;

    InputStream is = System.in;
    OutputStream os = System.out;
    Map<String, String> props = new HashMap<String, String>();

    // Parse command line arguments
    for (int i = 2; i < arg.length;) {
      String key = arg[i++].substring(1).toUpperCase();
      switch (enumValueOrDie(Arg.class, key)) {
        case CHUNK:
          chunk = Integer.parseInt(arg[i++]);
          break;
        case CONTENT:
          content = arg[i++];
          break;
        case COUNT:
          count = Integer.parseInt(arg[i++]);
          break;
        case DRAFT:
          draft = true;
          break;
        case FID:
          fid = arg[i++];
          break;
        case FILE:
          is = new FileInputStream(arg[i++]);
          break;
        case FUDGE:
          fudge = true;
          break;
        case HOST:
          host = arg[i++];
          break;
        case ID:
          id = arg[i++];
          break;
        case MID:
          mid = arg[i++];
          break;
        case MAXRESULTS:
          maxResults = Integer.parseInt(arg[i++]);
          break;
        case OUTPUT:
          os = new FileOutputStream(arg[i++]);
          break;
        case PASSWORD:
          password = arg[i++];
          break;
        case PREVID:
          previd = arg[i++];
          break;
        case PROJECTION:
          projection = arg[i++];
          break;
        case PREVIEW:
          preview = !preview;
          break;
        case PROP:
          props.put(arg[i], arg[i+1]);
          i += 2;
          break;
        case SEP:
          sep = arg[i++];
          break;
        case STARTINDEX:
          startIndex = Integer.parseInt(arg[i++]);
          break;
        case SUMMARY:
          summary = arg[i++];
          break;
        case TITLE:
          title = arg[i++];
          break;
        case UID:
          uid = arg[i++];
          break;
        case USER:
          user = arg[i++];
          break;
        case V:
          version = Integer.parseInt(arg[i++]);
          break;
      }
    }

    // Set up API client.
    PrintWriter pw = new PrintWriter(os);
    String base = "http://" + host + "/maps/feeds/" + feed.toString() + "/";
    List<String> params = new ArrayList<String>();
 
    if (startIndex > 0) {
      params.add("start-index=" + startIndex);
    }
    if (maxResults > 0) {
      params.add("max-results=" + maxResults);
    }
    if (null != previd) {
      params.add("previd=" + previd);
    }
    ApiClient client = Feed.maps == feed
        ? new ApiClient(MapEntry.class, MapFeed.class,
            "%s/" + projection + "/%s", params)
        : new ApiClient(FeatureEntry.class, FeatureFeed.class,
                        "%s" + sep + "%s/" + projection + "/%s", params);
    MapsService maps = client.authenticate(base, user, password);
    if (version < 2) {
      maps.setProtocolVersion(MapsService.Versions.V1);
    } else if (version == 2){
      maps.setProtocolVersion(MapsService.Versions.V2);
    }

    // Perform action.
    BaseEntry entry;
    switch (action) {

      // For now, just get the whole feed.
      case query:
        pp(maps, client.readFeed(uid, mid, fid), pw);
        break;

      // Create an entry from scratch or read from input.
      case create:
        client.trimFormat("/%s");     // sleazy hack to remove trailing id
        entry = title == null
          ? parseEntry(client, is, maps, preview, pw)
          : client.buildEntry(title, content, summary);
        if (id.length() > 0) {
          entry.setExtension(new ResourceId(id));
        }
        setProperties(props, entry);
        if (preview) {
          pp(maps, entry, pw);
        }
        pp(maps, client.createEntry(entry, uid, mid), pw);
        break;

      // Read an individual entry.
      case read:
        pp(maps, client.readEntry(uid, mid, fid), pw);
        break;

      // Update an existing entry.
      case update:
        entry = title == null
          ? parseEntry(client, is, maps, preview, pw)
          : client.buildEntry(title, content, summary);
        setProperties(props, entry);
        if (preview) {
          pp(maps, entry, pw);
        }
        pp(maps, client.updateEntry(entry, uid, mid, fid), pw);
        break;

      // Delete an existing entry.
      case delete:
        String err = client.deleteEntry(uid, mid, fid);
        if (null != err) {
          System.err.println("delete error: " + err);
        }
        break;

      case batch:
        client.trimFormat("/%s");     // sleazy hack to remove trailing id
        client.addFormat("/batch");
        BaseFeed bf = parseFeed(client, is, maps, preview, pw);
        pp(maps, client.createFeed(bf, uid, mid), pw);
        break;

      // Clear out a feed.
      case clear:
        client.trimFormat("/%s");     // sleazy hack to remove trailing id
        bf = client.readFeed(uid, mid, fid);
        client.addFormat("/batch");
        BaseFeed batch = client.newFeed();
        count = 0;
        for (Object o : bf.getEntries()) {
          String entryId = ((BaseEntry) o).getSelfLink().getHref();
          if (count == 0 && fudge) {
            entryId += "-xx";
          }
          BaseEntry delete = client.newEntry();
          delete.setId(replaceUserId(uid, entryId));
          BatchUtils.setBatchOperationType(delete, BatchOperationType.DELETE);
          BatchUtils.setBatchId(delete, Integer.toString(++count));
          batch.getEntries().add(delete);
        }
        if (preview) {
          pp(maps, batch, pw);
        }
        BaseFeed response = maps.batch(client.makeUri(uid, mid, fid), batch);
        pp(maps, response, pw);
        break;

      // Test bulk insert latency.
      case bulk:
        client.trimFormat("/%s");     // sleazy hack to remove trailing id
        String baseXml = "";
        if (title == null) {
          char buf[] = new char[1024 * 64];
          int n = new InputStreamReader(is).read(buf);
          baseXml = new String(buf, 0, n);
        }
        client.addFormat("/batch");
        for (int i = 0; i < count; i++) {
          batch = client.newFeed();
          long t0 = System.currentTimeMillis();
          for (int j = 0; j < chunk; j++) {
            String entryId = "" + i + "-" + j;
            String xml = baseXml.replace("COUNT", entryId);
            System.err.println(xml);
            entry = title == null
                ? parseEntry(client, new ByteArrayInputStream(xml.getBytes()),
                    maps, preview, pw)
                : client.buildEntry(title, content, summary);
            setProperties(props, entry);
            entry.setId(entryId);
            BatchUtils.setBatchId(entry, entryId);
            BatchUtils.setBatchOperationType(entry, BatchOperationType.INSERT);
            batch.getEntries().add(entry);
          }
          if (preview) {
            pp(maps, batch, pw);
          }
          response = maps.batch(
              client.makeUri(uid, mid, fid), batch);
          pp(maps, response, pw);
          System.err.println("chunk insert time: " +
              (System.currentTimeMillis() - t0) + "ms");
        }
        break;

      // Post a file as is, no interpretation.
      case rawpost:
        break;

      // Update a bloc of entries.
      case revise:
        client.trimFormat("/%s");     // sleazy hack to remove trailing id
        baseXml = "";
        if (title == null) {
          char buf[] = new char[1024 * 64];
          int n = new InputStreamReader(is).read(buf);
          baseXml = new String(buf, 0, n);
        }
        bf = client.readFeed(uid, mid, fid);
        int n = bf.getEntries().size();
        client.addFormat("/batch");
        int dex = 0;
        count = Math.min(count, n / chunk);
        for (int i = 0; i < count; i++) {
          long t0 = System.currentTimeMillis();
          batch = client.newFeed();
          for (int j = 0; j < Math.min(n, chunk); j++) {
            Object o = bf.getEntries().get(j + i*chunk);
            String entryId = ((BaseEntry) o).getSelfLink().
                getHref().replaceAll("\\.", sep);
            if (dex == 0 && fudge) {
              entryId += "-xx";
            }
            String xml = baseXml.replace("COUNT", entryId.substring(entryId.lastIndexOf('/')));
            BaseEntry update = title == null
                ? parseEntry(client, new ByteArrayInputStream(xml.getBytes()),
                    maps, preview, pw)
                : client.buildEntry(title, content, summary);
            update.setId(replaceUserId(uid, entryId));
            setProperties(props, update);
            BatchUtils.setBatchOperationType(update, BatchOperationType.UPDATE);
            BatchUtils.setBatchId(update, Integer.toString(++dex));
            batch.getEntries().add(update);
          }
          n -= chunk;
          if (preview) {
            pp(maps, batch, pw);
          }
          response = maps.batch(
              client.makeUri(uid, mid, fid), batch);
          pp(maps, response, pw);
          System.err.println("chunk update time: " +
              (System.currentTimeMillis() - t0) + "ms");
        }
        break;
    }
  }

  /**
   * Returns the enum value of a string, or exits after printing
   * the list of known values.
   *
   * @param ec enum class
   * @param key string to turn into an enum
   * @return the right enum
   */
  private static <T extends Enum<T>> T enumValueOrDie(Class<T> ec, String key) {
    try {
      return T.valueOf(ec, key);
    } catch (IllegalArgumentException e) {
      System.err.println("unknown " + ec.getSimpleName() + ": " + key);
      System.err.println("allowed: {" +
          StringUtil.join(ec.getEnumConstants(), ", ").toLowerCase() + "}");
      System.exit(1);
      return null;
    }
  }

  /**
   * Attempts to replace the user component in an entry id with the given uid.
   */
  private static String replaceUserId(String uid, String s) {
    String[] parts = s.split("/");
    for (int i = 0; i < parts.length; i++) {
      if (parts[i].matches("[0-9]+")) {
        parts[i] = uid;
        break;
      }
    }
    return StringUtil.join(parts, "/");
  }

  private static void setProperties(Map<String, String> props,
      BaseEntry entry) {
    List<CustomProperty> cust = (entry instanceof MapEntry)
        ? ((MapEntry) entry).getCustomProperties()
        : ((FeatureEntry) entry).getCustomProperties();
    cust.clear();
    for (String key : props.keySet()) {
      cust.add(new CustomProperty(key, null, null, props.get(key)));
    }
  }
}
