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


package sample.docs;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gdata.client.media.ResumableGDataFileUploader;
import sample.util.SimpleCommandLineParser;
import com.google.gdata.data.Link;
import com.google.gdata.data.docs.DocumentListEntry;
import com.google.gdata.data.docs.DocumentListFeed;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ServiceException;
import com.google.gdata.client.uploader.FileUploadData;
import com.google.gdata.client.uploader.ProgressListener;
import com.google.gdata.client.uploader.ResumableHttpFileUploader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A console aplication to demonstrate interaction with Google Docs API to
 * upload/update large media files using Resumable Upload protocol.
 *
 * 
 */
public class DocumentResumableUploadDemo {

  /** Default document list feed url. */
  private static final String DEFAULT_DOCLIST_FEED_URL =
      "https://docs.google.com/feeds/default/private/full";

  /** Default create-media-url for uploading documents */
  private static final String DEFAULT_RESUMABLE_UPLOAD_URL =
      "https://docs.google.com/feeds/upload/create-session/default/private/full";

  /** Maximum number of concurrent uploads */
  private static final int MAX_CONCURRENT_UPLOADS = 10;

  /** Time interval at which upload task will notify about the progress */
  private static final int PROGRESS_UPDATE_INTERVAL = 1000;

  /** Max size for each upload chunk */
  private static final int DEFAULT_CHUNK_SIZE = 10000000;

  /**
   * Welcome message, introducing the program.
   */
  private static final String[] WELCOME_MESSAGE = {
      "", "This is a demo of the resumable upload feature for Docs GData API",
      "Using this interface, you can upload/update large documents.", ""};

  private static final String APPLICATION_NAME = "JavaGDataClientSampleAppV3.0";

  private static final String[] USAGE_MESSAGE = {
      "Usage: java DocumentResumableUploadDemo.jar --username <user> --password <pass>",
  };

  private static final String[] COMMAND_HELP_MESSAGE = {
      "Commands:",
      "   list [object_type]                                 "
          + "                             [[list objects]]",
      "   upload <file_path1:title1> ... <file_pathN:titleN> "
          + "[<chunk_size_in_byes>]       [[uploads set of files]]",
      "   update <ducument_id> <updated_file_path> [<chunk_size_in_bytes>] "
          + "               [[updates content of an object]]",
  };

  /** Instance of {@link DocumentList} */
  private final DocumentList docs;

  /** Steam to print status messages to. */
  PrintStream output;

  /**
   * Constructor.
   *
   * @param docs {@link DocumentList} for interface to DocList API service.
   * @param out printstream to output status messages to.
   */
  DocumentResumableUploadDemo(DocumentList docs, PrintStream out) {
    this.docs = docs;
    this.output = out;
  }

  /**
   * Prints out a message.
   *
   * @param msg the message to be printed.
   */
  private static void printMessage(String[] msg) {
    for (String s : msg) {
      System.out.println(s);
    }
  }

  private String[] parseCommand(String command) {
    return command.trim().split(" ");
  }

  /**
   * Prints out the specified document entry.
   *
   * @param doc the document entry to print.
   */
  public void printDocumentEntry(DocumentListEntry doc) {
    StringBuffer outputBuffer = new StringBuffer();

    outputBuffer.append(" -- " + doc.getTitle().getPlainText() + " ");
    if (!doc.getParentLinks().isEmpty()) {
      for (Link link : doc.getParentLinks()) {
        outputBuffer.append("[" + link.getTitle() + "] ");
      }
    }
    outputBuffer.append(doc.getResourceId());

    output.println(outputBuffer);
  }

  /**
   * Uploads given collection of files.  The call blocks until all uploads are
   * done.
   *
   * @param url create-session url for initiating resumable uploads for
   *            documents API.
   * @param files list of absolute filepaths to files to upload.
   * @param chunkSize max size of each upload chunk.
   */
  public Collection<DocumentListEntry> uploadFiles(String url,
      List<String> files, int chunkSize)
      throws IOException, ServiceException, InterruptedException {
    // Create a listener
    FileUploadProgressListener listener = new FileUploadProgressListener();
    // Pool for handling concurrent upload tasks
    ExecutorService executor =
        Executors.newFixedThreadPool(MAX_CONCURRENT_UPLOADS);
    // Create {@link ResumableGDataFileUploader} for each file to upload
    List<ResumableGDataFileUploader> uploaders = Lists.newArrayList();
    for (String fileName : files) {
      MediaFileSource mediaFile = getMediaFileSource(fileName);
      ResumableGDataFileUploader uploader =
          new ResumableGDataFileUploader.Builder(
              docs.service, new URL(url), mediaFile, null /*empty meatadata*/)
              .title(mediaFile.getName())
              .chunkSize(chunkSize).executor(executor)
              .trackProgress(listener, PROGRESS_UPDATE_INTERVAL)
              .build();
      uploaders.add(uploader);
    }
    // attach the listener to list of uploaders
    listener.listenTo(uploaders);

    // Start the upload
    for (ResumableGDataFileUploader uploader : uploaders) {
      uploader.start();
    }

    // wait for uploads to complete
    while (!listener.isDone()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException ie) {
        listener.printResults();
        throw ie; // rethrow
      }
    }

    // print upload results
    listener.printResults();

    // return list of uploaded entries
    return listener.getUploaded();
  }

  private MediaFileSource getMediaFileSource(String fileName) {
    File file = new File(fileName);
    MediaFileSource mediaFile = new MediaFileSource(file,
        DocumentListEntry.MediaType.fromFileName(file.getName())
            .getMimeType());
    return mediaFile;
  }

  /**
   * Execute 'list' command.
   */
  private void executeList(String[] args) throws IOException,
      ServiceException, DocumentListException {
    DocumentListFeed feed = null;
    String msg = "";

    switch (args.length) {
      case 1:
        msg = "List of docs: ";
        feed = docs.getDocsListFeed("all");
        break;
      case 2:
        msg = "List of all " + args[1] + ": ";
        feed = docs.getDocsListFeed(args[1]);
        break;
      case 3:
        if (args[1].equals("folder")) {
          msg = "Contents of folder_id '" + args[2] + "': ";
          feed = docs.getFolderDocsListFeed(args[2]);
        }
        break;
    }

    if (feed != null) {
      output.println(msg);
      for (DocumentListEntry entry : feed.getEntries()) {
        printDocumentEntry(entry);
      }
    } else {
      printMessage(COMMAND_HELP_MESSAGE);
    }
  }

  /**
   * Execute 'upload' command.
   */
  private void executeUpload(String[] args)
      throws IOException, ServiceException, InterruptedException {
    if (args.length > 1) {
      int chunkSize = DEFAULT_CHUNK_SIZE;
      List<String> files = Lists.newArrayList();
      for (int index = 1; index < args.length; index++) {
        String arg = args[index];
        if (index < args.length - 1) {
          files.add(arg);
          continue;
        }
        // Last argument can be a file or chunk size
        try {
          chunkSize = Integer.parseInt(arg);
        } catch (NumberFormatException nfe) {
          files.add(arg);
        }
      }
      uploadFiles(DEFAULT_RESUMABLE_UPLOAD_URL, files, chunkSize);
      output.println("Finished upload");
    }
  }

  /**
   * Execute 'update' command.
   */
  private void executeUpdate(String[] args)
      throws IOException, ServiceException, InterruptedException {
    String docIdToUpdate = args[1];
    String filePath = args[2];

    // retrieve latest entry
    DocumentListEntry currentEntry = docs.service.getEntry(
        new URL(DEFAULT_DOCLIST_FEED_URL  + "/" + docIdToUpdate),
        DocumentListEntry.class);

    MediaFileSource mediaFile = getMediaFileSource(filePath);
    ResumableGDataFileUploader uploader =
        new ResumableGDataFileUploader
            .Builder(docs.service, mediaFile, currentEntry)
            .title(mediaFile.getName())
            .requestType(
                ResumableGDataFileUploader.RequestType.UPDATE_MEDIA_ONLY)
            .build();

    uploader.start();

    // wait for upload to complete
    while (!uploader.isDone()) {
      try {
        Thread.sleep(100);
      } catch (InterruptedException ie) {
        output.println("Media update interrupted at: "
            + String.format("%3.0f", uploader.getProgress() * 100) + "%");
        throw ie; // rethrow
      }
    }
    DocumentListEntry updatedEntry =
        uploader.getResponse(DocumentListEntry.class);

    output.println("Finished update");
  }

  private boolean executeCommand(BufferedReader reader) throws IOException,
      ServiceException, InterruptedException {

    output.println("Enter a command");

    try {
      String command = reader.readLine();
      if (command == null) {
        return false;
      }

      String[] args = parseCommand(command);
      String name = args[0];

      if (name.equals("list")) {
        executeList(args);
      } else if (name.equals("upload")) {
        executeUpload(args);
      } else if (name.equals("update")) {
        executeUpdate(args);
      } else if (name.startsWith("q") || name.startsWith("exit")) {
        return false;
      } else if (name.equals("help")) {
        printMessage(COMMAND_HELP_MESSAGE);
      } else {
        output.println("Unknown command. Type 'help' for list of commands");
      }
    } catch (DocumentListException e) {
      e.printStackTrace();
    }
    return true;
  }

  void run() throws IOException, ServiceException, InterruptedException {
    printMessage(WELCOME_MESSAGE);
    printMessage(COMMAND_HELP_MESSAGE);

    BufferedReader reader = new BufferedReader(
        new InputStreamReader(System.in));

    while (executeCommand(reader)) {
    }
  }

  public static void main(String[] args) throws DocumentListException,
      IOException, ServiceException, InterruptedException {

    SimpleCommandLineParser parser = new SimpleCommandLineParser(args);
    String user = parser.getValue("username", "user", "u");
    String password = parser.getValue("password", "pass", "p");
    boolean help = parser.containsKey("help", "h");

    if (help || (user == null || password == null)) {
      printMessage(USAGE_MESSAGE);
      System.exit(1);
    }

    // authenticate
    DocumentList docs = new DocumentList(APPLICATION_NAME);
    docs.login(user, password);

    DocumentResumableUploadDemo demo = new DocumentResumableUploadDemo(
        docs, System.out);
    demo.run();
    System.exit(1);
  }

  /**
   * A {@link ProgressListener} implementation to track upload progress.
   * The listener can track multiple uploads at the same time.
   * Use {@link #isDone} to check if all uploads are completed and
   * use {@link #getUploaded} to access results of successful uploads.
   */
  private class FileUploadProgressListener implements ProgressListener {

    private Collection<ResumableGDataFileUploader> trackedUploaders
        = Lists.newArrayList();
    private int pendingRequests;
    Map<String, DocumentListEntry> uploaded = Maps.newHashMap();
    Map<String, String> failed = Maps.newHashMap();

    boolean processed;

    public FileUploadProgressListener() {
      this.pendingRequests = 0;
    }

    public void listenTo(Collection<ResumableGDataFileUploader> uploaders) {
      this.trackedUploaders.addAll(uploaders);
      this.pendingRequests = trackedUploaders.size();
    }

    public synchronized void progressChanged(ResumableHttpFileUploader uploader)
    {
      String fileId = ((FileUploadData) uploader.getData()).getFileName();
      switch(uploader.getUploadState()) {
        case COMPLETE:
        case CLIENT_ERROR:
          pendingRequests -= 1;
          output.println(fileId + ": Completed");
          break;
        case IN_PROGRESS:
          output.println(fileId + ":"
              + String.format("%3.0f", uploader.getProgress() * 100) + "%");
          break;
        case NOT_STARTED:
          output.println(fileId + ":" + "Not Started");
          break;
      }
    }

    public synchronized boolean isDone() {
      // not done if there are any pending requests.
      if (pendingRequests > 0) {
        return false;
      }
      // if all responses are processed., nothing to do.
      if (processed) {
        return true;
      }
      // check if all response streams are available.
      for (ResumableGDataFileUploader uploader : trackedUploaders) {
        if (!uploader.isDone()) {
          return false;
        }
      }
      // process all responses
      for (ResumableGDataFileUploader uploader : trackedUploaders) {
        String fileId = ((FileUploadData) uploader.getData()).getFileName();
        switch(uploader.getUploadState()) {
          case COMPLETE:
            try {
              DocumentListEntry entry =
                  uploader.getResponse(DocumentListEntry.class);
              uploaded.put(fileId, entry);
            } catch (IOException e) {
              failed.put(fileId, "Upload completed, but unexpected error "
                  + "reading server response");
            } catch (ServiceException e) {
              failed.put(fileId,
                  "Upload completed, but failed to parse server response");
            }
            break;
          case CLIENT_ERROR:
            failed.put(fileId, "Failed at " + uploader.getProgress());
            break;
        }
      }
      processed = true;
      output.println("All requests done");
      return true;
    }

    public synchronized Collection<DocumentListEntry> getUploaded() {
      if (!isDone()) {
        return null;
      }
      return uploaded.values();
    }

    public synchronized void printResults() {
      if (!isDone()) {
        return;
      }
      output.println("Result: " + uploaded.size() + ", " + failed.size());
      if (uploaded.size() > 0) {
        output.println(" Successfully Uploaded:");
        for (Map.Entry<String, DocumentListEntry> entry : uploaded.entrySet()) {
          printDocumentEntry(entry.getValue());
        }
      }
      if (failed.size() > 0) {
        output.println(" Failed to upload:");
        for (Map.Entry entry : failed.entrySet()) {
          output.println("  " + entry.getKey() + ":" + entry.getValue());
        }
      }
    }

    /**
     * Prints out the specified document entry.
     *
     * @param doc the document entry to print.
     */
    public void printDocumentEntry(DocumentListEntry doc) {
      StringBuffer buffer = new StringBuffer();

      buffer.append(" -- " + doc.getTitle().getPlainText() + " ");
      if (!doc.getParentLinks().isEmpty()) {
        for (Link link : doc.getParentLinks()) {
          buffer.append("[" + link.getTitle() + "] ");
        }
      }
      buffer.append(doc.getResourceId());

      output.println(buffer);
    }

  }


}
