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

import com.google.gdata.client.photos.PicasawebService;
import com.google.gdata.data.OtherContent;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.photos.AlbumEntry;
import com.google.gdata.data.photos.CommentEntry;
import com.google.gdata.data.photos.GphotoEntry;
import com.google.gdata.data.photos.PhotoEntry;
import com.google.gdata.data.photos.TagEntry;
import com.google.gdata.util.ContentType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

/**
 * A Command-line application using the PicasawebClient to make calls to the
 * Picasa Web Album GData API.
 *
 * 
 */
public class PicasawebCommandLine extends PicasawebClient {

  // Input stream for reading user input.
  private static final BufferedReader IN
      = new BufferedReader(new InputStreamReader(System.in));

  /**
   * Constructs the command line application.
   */
  public PicasawebCommandLine(PicasawebService service, String uname,
      String passwd) {
    super(service, uname, passwd);
  }

  /**
   * Main loop, runs the client application against the standard service, and
   * asks the user for their username and password.
   */
  public static void main(String[] args) throws Exception {

    try {
      PicasawebService service = new PicasawebService("exampleClient");

      String uname, passwd;
      if (args.length == 2) {
        uname = args[0];
        passwd = args[1];
      } else if (args.length != 0) {
        println("Syntax: PicasawebCommandLine <username> <password>");
        return;
      } else {
        uname = getString("Username");
        passwd = getString("Password");
      }

      new PicasawebCommandLine(service, uname, passwd).mainLoop();

    } catch (ExitException ee) {
      println("Exiting...");
    }
  }

  private void mainLoop() throws Exception {
    while (true) {
      println("Main Menu:");
      println("0) Exit Program");
      println("1) Show albums");
      println("2) Show user tags");
      int choice = getInt("Action");
      switch (choice) {
      case -1:
        continue;
      case 0:
        return;
      case 1:
        showAlbums();
        break;
      case 2:
        showUserTags();
        break;
      default:
        println("Invalid choice " + choice);
        break;
      }
    }
  }

  private void showAlbums() throws Exception {
    List<AlbumEntry> albums = getAlbums();
    int count = 1;
    if (albums.size() == 0) {
      println("No albums found.");
    } else {
      for (AlbumEntry entry : albums) {
        println("Album " + count++ + ") " + entry.getTitle().getPlainText());
        println(entry.getDescription().getPlainText());
      }
    }
    while (true) {
      println("Album Menu:");
      println("0) Exit to main menu");
      println("1) Show an album's photos");
      println("2) Show an album's tags");
      println("3) Create a new album");
      println("4) Delete an album");
      int choice = getInt("Action");
      switch (choice) {
      case -1:
        continue;
      case 0:
        return;
      case 1:
        showAlbumPhotos(albums);
        break;
      case 2:
        showAlbumTags(albums);
        break;
      case 3:
        createAlbum();
        showAlbums();
        return;
      case 4:
        deleteEntry(albums, "album");
        showAlbums();
        return;
      default:
        println("Invalid choice " + choice);
        break;
      }
    }
  }

  private void showUserTags() throws Exception {
    printTags(getTags());
  }

  private void showAlbumTags(List<AlbumEntry> albums) throws Exception {
    AlbumEntry albumEntry;
    try {
      albumEntry = getEntry(albums, "album");
    } catch (ExitException ee) {
      return;
    }

    printTags(getTags(albumEntry));
  }

  private void createAlbum() throws Exception {
    AlbumEntry album = new AlbumEntry();

    String title = getString("Title");
    album.setTitle(new PlainTextConstruct(title));
    String description = getString("Description");
    album.setDescription(new PlainTextConstruct(description));

    String access = "";
    while (!access.equals("public") && !access.equals("private")) {
      access = getString("Access (public | private)");
    }

    album.setAccess(access);

    String location = getString("Location");
    album.setLocation(location);
    album.setDate(new Date());

    insertAlbum(album);
  }

  private void createComment(PhotoEntry photoEntry) throws Exception {
    CommentEntry comment = new CommentEntry();

    String gphotoId = photoEntry.getGphotoId();
    comment.setPhotoId(Long.parseLong(gphotoId));
    comment.setTitle(new PlainTextConstruct("Inserted Comment"));

    String commentStr = getString("Comment");
    comment.setContent(new PlainTextConstruct(commentStr));

    insert(photoEntry, comment);
  }

  private void showAlbumPhotos(List<AlbumEntry> albums) throws Exception {

    AlbumEntry albumEntry;
    try {
      albumEntry = getEntry(albums, "album");
    } catch (ExitException ee) {
      return;
    }

    List<PhotoEntry> photos = getPhotos(albumEntry);
    int count = 1;
    if (photos.size() == 0) {
      println("No photos found.");
    } else {
      for (PhotoEntry entry : photos) {
        println("Photo " + count++ + ") " + entry.getTitle().getPlainText());
        println(entry.getDescription().getPlainText());
      }
    }
    while (true) {
      println("Photo Menu:");
      println("0) Exit to album menu");
      println("1) Show photo comments");
      println("2) Show photo tags");
      println("3) Create a new photo");
      println("4) Delete a photo");
      int choice = getInt("Action");
      switch (choice) {
      case -1:
        continue;
      case 0:
        return;
      case 1:
        showPhotoComments(albumEntry, photos);
        break;
      case 2:
        showPhotoTags(albumEntry, photos);
        break;
      case 3:
        createPhoto(albumEntry);
        break;
      case 4:
        deleteEntry(photos, "photo");
        break;
      default:
        println("Invalid choice " + choice);
        break;
      }
    }
  }

  private void createPhoto(AlbumEntry albumEntry) throws Exception {
    PhotoEntry photo = new PhotoEntry();

    String title = getString("Title");
    photo.setTitle(new PlainTextConstruct(title));
    String description = getString("Description");
    photo.setDescription(new PlainTextConstruct(description));
    photo.setTimestamp(new Date());

    OtherContent content = new OtherContent();


    File file = null;
    while (file == null || !file.canRead()) {
      file = new File(getString("Photo location"));
    }
    content.setBytes(getBytes(file));
    content.setMimeType(new ContentType("image/jpeg"));
    photo.setContent(content);

    insert(albumEntry, photo);
  }

  private byte[] getBytes(File file) throws FileNotFoundException, IOException {
    byte[] result = new byte[(int) file.length()];
    new FileInputStream(file).read(result);
    return result;
  }

  private void showPhotoTags(AlbumEntry albumEntry, List<PhotoEntry> photos)
      throws Exception {
    PhotoEntry photoEntry;
    try {
      photoEntry = getEntry(photos, "photo");
    } catch (ExitException ee) {
      return;
    }

    List<TagEntry> photoTags = getTags(photoEntry);

    printTags(photoTags);
    while (true) {
      println("Tag Menu:");
      println("0) Exit to photo menu");
      println("1) Add a tag");
      println("2) Delete a tag");
      int choice = getInt("Action");
      switch (choice) {
      case -1:
        continue;
      case 0:
        return;
      case 1:
        createTag(photoEntry);
        showPhotoTags(albumEntry, photos);
        return;
      case 2:
        deleteEntry(photoTags, "tag");
        showPhotoTags(albumEntry, photos);
        return;
      default:
        println("Invalid choice " + choice);
        break;
      }
    }
  }

  private void printTags(List<TagEntry> tags) throws Exception {
    int count = 1;
    if (tags.size() == 0) {
      println("No tags found.");
    } else {
      for (TagEntry tag : tags) {
        String tagWeight = tag.getWeight() == null
            ? "" : " Weight: " + tag.getWeight();
        println("Tag " + count++ + ") " + tag.getTitle().getPlainText()
            + tagWeight);
      }
    }
  }

  private void createTag(PhotoEntry photoEntry) throws Exception {
    TagEntry tag = new TagEntry();

    String title = getString("Tag");
    tag.setTitle(new PlainTextConstruct(title));

    insert(photoEntry, tag);
  }

  private void showPhotoComments(AlbumEntry albumEntry, List<PhotoEntry> photos)
      throws Exception {

    PhotoEntry photoEntry;
    try {
      photoEntry = getEntry(photos, "photo");
    } catch (ExitException ee) {
      return;
    }

    List<CommentEntry> comments = getComments(photoEntry);
    int count = 1;
    if (comments.size() == 0) {
      println("No comments found.");
    } else {
      for (CommentEntry comment : comments) {
        println("Comment " + count++ + ") " + comment.getTitle().getPlainText());
        println(comment.getPlainTextContent());
      }
    }
    while (true) {
      println("Comment Menu:");
      println("0) Exit to photo menu");
      println("1) Add a comment");
      println("2) Delete a comment");
      int choice = getInt("Action");
      switch (choice) {
      case -1:
        continue;
      case 0:
        return;
      case 1:
        createComment(photoEntry);
        showPhotoComments(albumEntry, photos);
        return;
      case 2:
        deleteEntry(comments, "comment");
        showPhotoComments(albumEntry, photos);
        return;
      default:
        println("Invalid choice " + choice);
        break;
      }
    }
  }

  private void deleteEntry(List<? extends GphotoEntry> entries, String name)
      throws Exception {
    GphotoEntry entry;
    try {
      entry = getEntry(entries, name);
    } catch (ExitException ee) {
      return;
    }

    entry.delete();
  }

  private <T extends GphotoEntry> T getEntry(List<T> entries, String name)
      throws Exception {
    while (true) {
      int index = getInt("which " + name) - 1;
      if (index >= 0 && index < entries.size()) {
        return entries.get(index);
      } else {
        println("Invalid index " + index);
      }
    }
  }


  private static int getInt(String name) throws ExitException, IOException {
    while (true) {
      String input = getString(name);
      try {
        return Integer.parseInt(input);
      } catch (NumberFormatException nfe) {
        println("Invalid number " + input);
      }
    }
  }

  private static String getString(String name) throws ExitException,
      IOException {
    print("Please enter ");
    print(name);
    println(":");
    System.out.flush();
    String result = IN.readLine();
    result = result.trim();
    if (result.length() == 0) {
      return "-1";
    }
    return result;
  }

  private static void print(String str) {
    System.out.print(str);
  }

  private static void println(String str) {
    System.out.println(str);
  }

  private static class ExitException extends Exception {
    // Empty, just used to exit quickly.
  }
}
