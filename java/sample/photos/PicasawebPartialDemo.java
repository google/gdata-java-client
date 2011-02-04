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


package sample.photos;

import com.google.gdata.client.Query;
import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.AlbumFeed;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.util.ServiceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

/**
 * Demo of partial response and partial patch to change location on photo
 * albums.
 *
 * 
 */
public class PicasawebPartialDemo {

  /** Picasaweb feed url prefix. */
  private static final String API_PREFIX
      = "https://picasaweb.google.com/data/feed/api/user/";

  /** Input stream for reading user input. */
  private static final BufferedReader IN
      = new BufferedReader(new InputStreamReader(System.in));

  /** Output stream to write program output. */
  private static final PrintStream OUT = System.out;

  /** Picasaweb service to talk to the server */
  private PicasawebService service;

  /** Constructor */
  private PicasawebPartialDemo(PicasawebService service) {
    this.service = service;
  }

  /**
   * Displays a menu of the main activities a user can perform.
   */
  private void printMenu() {
    OUT.println("\n");
    OUT.println("Choose one of the following demo options:");
    OUT.println("\t1) Retrieve my albums with location");
    OUT.println("\t2) Update location for an album");
    OUT.println("\t0) Exit");
    OUT.println("\nEnter Number (0-2): ");
  }

  /**
   * Demonstrates use of partial query to retrieve album title and location
   * information for user's albums.
   */
  private void printAlbumLocation(String uname)
      throws IOException, ServiceException {
    String albumsUrl = API_PREFIX + uname;
    String fields = "entry(title,gphoto:id,gphoto:location)";

    Query albumQuery = new Query(new URL(albumsUrl));
    albumQuery.setFields(fields);

    AlbumFeed feed = service.query(albumQuery, AlbumFeed.class);
    for (GphotoEntry entry : feed.getEntries()) {
      if (entry instanceof AlbumEntry) {
        AlbumEntry albumEntry = (AlbumEntry) entry;
        OUT.println(albumEntry.getGphotoId() + ":"
            + albumEntry.getTitle().getPlainText()
            + " (" + albumEntry.getLocation()  + ")");
      }
    }
  }

  /**
   * Demonstrates update operation using partial patch to update location
   * string for specified album.
   */
  private void updateAlbumLocation(String uname)
      throws IOException, ServiceException {
    OUT.println("Enter album id to update:");
    String albumId = IN.readLine();

    // Get the current album entry
    String albumEntryUrl = API_PREFIX + uname + "/" + albumId;
    String fields = "@gd:etag,gphoto:location";
    Query patchQuery = new Query(new URL(albumEntryUrl));
    patchQuery.setFields(fields);
    AlbumEntry entry = service.getEntry(patchQuery.getUrl(), AlbumEntry.class);
    OUT.println("Current location: " + entry.getLocation());

    // Update the location in the album entry
    OUT.println("Specify new location: ");
    String newLocation = IN.readLine();
    entry.setLocation(newLocation);
    entry.setSelectedFields("gphoto:location");
    AlbumEntry updated = service.patch(new URL(albumEntryUrl), fields, entry);
    OUT.println("Location set to: " + updated.getLocation());
  }

  /** Program entry point. */
  public static void main(String args[]) throws IOException, ServiceException {
    // parse user input
    String uname, passwd;
    if (args.length == 2) {
      uname = args[0];
      passwd = args[1];
    } else if (args.length != 0) {
      OUT.println(
          "Syntax: PicasawebCommandLine <username> <password>");
      return;
    } else {
      uname = getString("Username");
      passwd = getString("Password");
    }

    PicasawebService service = new PicasawebService("gdata-PhotosPartialDemo");
    service.setUserCredentials(uname, passwd);

    PicasawebPartialDemo demo = new PicasawebPartialDemo(service);
    while (true) {
      try {
        demo.printMenu();
        int choice = readInt();

        switch (choice) {
          case 1:
            // Prints out the user's uploaded videos
            demo.printAlbumLocation(uname);
            break;
          case 2:
            demo.updateAlbumLocation(uname);
            break;
          case 0:
            System.exit(1);
            break;
        }
      } catch (IOException e) {
        // Communications error
        System.err.println(
            "There was a problem communicating with the service.");
        e.printStackTrace();
      } catch (ServiceException e) {
        // Server side error
        System.err.println("The server had a problem handling your request.");
        e.printStackTrace();
      }
    }
  }

  /** Reads a user string input. */
  private static String getString(String name) throws IOException {
    OUT.print("Please enter ");
    OUT.print(name);
    OUT.println(":");
    OUT.flush();
    String result = IN.readLine();
    result = result.trim();
    if (result.length() == 0) {
      return "-1";
    }
    return result;
  }

  /**
   * Reads a line of text from the standard input.
   *
   * @throws IOException If unable to read a line from the standard input.
   * @return A line of text read from the standard input.
   */
  private static String readLine() throws IOException {
    return IN.readLine();
  }

  /**
   * Reads a line of text from the standard input and returns the parsed
   * integer representation.
   *
   * @throws IOException If unable to read a line from the standard input.
   * @return An integer read from the standard input.
   */
  private static int readInt() throws IOException {
    String input = readLine();

    try {
      return Integer.parseInt(input);
    } catch (NumberFormatException nfe) {
      return 0;
    }
  }
}
