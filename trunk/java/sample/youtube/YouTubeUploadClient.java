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


package sample.youtube;

import com.google.gdata.client.media.ResumableGDataFileUploader;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.mediarss.MediaCategory;
import com.google.gdata.data.media.mediarss.MediaDescription;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaTitle;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YouTubeNamespace;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.client.uploader.ProgressListener;
import com.google.gdata.client.uploader.ResumableHttpFileUploader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;

/**
 * Demonstrates YouTube Data API operation to upload large media files.
 *
 * 
 */
public class YouTubeUploadClient {

  /**
   * The URL used to resumable upload
   */
  public static final String RESUMABLE_UPLOAD_URL =
      "http://uploads.gdata.youtube.com/resumable/feeds/api/users/default/uploads";

  /** Time interval at which upload task will notify about the progress */
  private static final int PROGRESS_UPDATE_INTERVAL = 1000;

  /** Max size for each upload chunk */
  private static final int DEFAULT_CHUNK_SIZE = 10000000;

  /** Steam to print status messages to. */
  PrintStream output;

  /**
   * Input stream for reading user input.
   */
  private static final BufferedReader bufferedReader = new BufferedReader(
      new InputStreamReader(System.in));


  /**
   * A {@link ProgressListener} implementation to track upload progress.
   * The listener can track multiple uploads at the same time.
   */
  private class FileUploadProgressListener implements ProgressListener {
    public synchronized void progressChanged(ResumableHttpFileUploader uploader)
    {
      switch(uploader.getUploadState()) {
        case COMPLETE:
          output.println("Upload Completed");
          break;
        case CLIENT_ERROR:
          output.println("Upload Failed");
          break;
        case IN_PROGRESS:
          output.println(
              String.format("%3.0f", uploader.getProgress() * 100) + "%");
          break;
        case NOT_STARTED:
          output.println("Upload Not Started");
          break;
      }
    }
  }

  private YouTubeUploadClient(PrintStream out) {
    this.output = out;
  }

  /**
   * Uploads a new video to YouTube.
   *
   * @param service An authenticated YouTubeService object.
   * @throws IOException Problems reading user input.
   */
  private void uploadVideo(YouTubeService service)
      throws IOException, ServiceException, InterruptedException {

    output.println("First, type in the path to the movie file:");
    File videoFile = new File(readLine());
    if (!videoFile.exists()) {
      output.println("Sorry, that video doesn't exist.");
      return;
    }

    output.println(
        "What is the MIME type of this file? (ex. 'video/quicktime' for .mov)");
    MediaFileSource ms = new MediaFileSource(videoFile, readLine());

    output.println("What should I call this video?");
    String videoTitle = readLine();

    VideoEntry newEntry = new VideoEntry();
    YouTubeMediaGroup mg = newEntry.getOrCreateMediaGroup();
    mg.addCategory(new MediaCategory(YouTubeNamespace.CATEGORY_SCHEME, "Tech"));
    mg.setTitle(new MediaTitle());
    mg.getTitle().setPlainTextContent(videoTitle);
    mg.setKeywords(new MediaKeywords());
    mg.getKeywords().addKeyword("gdata-test");
    mg.setDescription(new MediaDescription());
    mg.getDescription().setPlainTextContent(videoTitle);

    FileUploadProgressListener listener = new FileUploadProgressListener();
    ResumableGDataFileUploader uploader =
        new ResumableGDataFileUploader.Builder(
            service, new URL(RESUMABLE_UPLOAD_URL), ms, newEntry)
            .title(videoTitle)
            .trackProgress(listener, PROGRESS_UPDATE_INTERVAL)
            .chunkSize(DEFAULT_CHUNK_SIZE)
            .build();

    uploader.start();
    while (!uploader.isDone()) {
      Thread.sleep(PROGRESS_UPDATE_INTERVAL);
    }

    switch(uploader.getUploadState()) {
      case COMPLETE:
        output.println("Uploaded successfully");
        break;
      case CLIENT_ERROR:
        output.println("Upload Failed");
        break;
      default:
        output.println("Unexpected upload status");
        break;
    }
  }


  /**
   * YouTubeUploadClient is a sample command line application that
   * demonstrates how to upload large media files to youtube.  This sample
   * uses resumable upload feature to upload large media.
   *
   * @param args Used to pass the username and password of a test account.
   */
  public static void main(String[] args) {
    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String username = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    String developerKey = parser.getValue("key", "k");
    boolean help = parser.containsKey("help", "h");

    if (help || username == null || password == null || developerKey == null) {
      printUsage();
      System.exit(1);
    }

    YouTubeService service = new YouTubeService("gdataSample-YouTubeAuth-1",
        developerKey);

    try {
      service.setUserCredentials(username, password);
    } catch (AuthenticationException e) {
      System.out.println("Invalid login credentials.");
      System.exit(1);
    }

    YouTubeUploadClient client = new YouTubeUploadClient(System.out);

    while (true) {
      try {
        printMenu();
        int choice = readInt();

        switch(choice) {
          case 1:
            client.uploadVideo(service);
            break;
          case 0:
            System.exit(1);
        }

      } catch (IOException e) {
        // Communications error
        System.err.println(
            "There was a problem communicating with the service.");
        e.printStackTrace();
      } catch (ServiceException se) {
        System.out.println("Sorry, your upload was invalid:");
        System.out.println(se.getResponseBody());
        se.printStackTrace();
      } catch (InterruptedException ie) {
        System.out.println("Upload interrupted");
      }
    }

  }

  /**
   * Shows the usage of how to run the sample from the command-line.
   */
  private static void printUsage() {
    System.out.println("Usage: java YouTubeUploadClient.jar "
        + " --username <user@gmail.com> " + " --password <pass> "
        + " --key <developer key>");
  }

  /**
   * Displays a menu of the main activities a user can perform.
   */
  private static void printMenu() {
    System.out.println("\n");
    System.out.println("Choose one of the following demo options:");
    System.out.println("\t1) Upload new video");
    System.out.println("\t0) Exit");
  }

  /**
   * Reads a line of text from the standard input.
   *
   * @throws IOException If unable to read a line from the standard input.
   * @return A line of text read from the standard input.
   */
  private static String readLine() throws IOException {
    return bufferedReader.readLine();
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
