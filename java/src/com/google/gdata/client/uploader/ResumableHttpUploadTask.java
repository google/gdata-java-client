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


package com.google.gdata.client.uploader;

import com.google.gdata.client.uploader.ResumableHttpFileUploader.ResponseMessage;
import com.google.gdata.client.uploader.ResumableHttpFileUploader.UploadState;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Task for generating HTTP requests used to upload files and resume uploads
 * for partially uploaded files.  Performs the blocking upload work of
 * {@link ResumableHttpFileUploader} instances.  Typically these tasks are
 * submitted to an executor service with multiple threads, allowing
 * {@link ResumableHttpFileUploader} instances to execute asynchronously.
 */
class ResumableHttpUploadTask implements Callable<ResponseMessage> {

  /**
   * Uploader which created this task, and with which the resultant HTTP
   * requests should be associated (e.g., progress, state, etc.).
   */
  private final ResumableHttpFileUploader uploader;

  /**
   * Identifies if the upload should be resumed or not.
   */
  private final boolean resume;

  /**
   * Factory for creating HTTP connections.
   */
  private final UrlConnectionFactory urlConnectionFactory;

  /**
   * Content length header name.
   */
  private static final String CONTENT_LENGTH_HEADER_NAME = "Content-Length";

  /**
   * Content range header name.
   */
  private static final String CONTENT_RANGE_HEADER_NAME = "Content-Range";

  /**
   * Constructs an upload task.
   *
   * @param uploader with which this task should be associated
   * @param resume <code>true</code> if this upload should be resumed
   */
  public ResumableHttpUploadTask(UrlConnectionFactory urlConnectionFactory,
      ResumableHttpFileUploader uploader, boolean resume) {
    this.urlConnectionFactory = urlConnectionFactory;
    this.uploader = uploader;
    this.resume = resume;
  }

  public ResponseMessage call() throws Exception {
    return upload();
  }

  /**
   * Makes an HTTP request to determine the range of bytes for this upload that
   * the server has already received. The index of the first byte not yet
   * received by the server is returned.  This method ignores several possible
   * server errors and returns <code>0</code> in those cases.  If the server
   * errors persist, they can be appropriately handled in the {@link #upload()}
   * method (where this method should be called from).
   *
   * @return the index of the first byte not yet received by the server for this
   *     upload
   * @throws IOException if the HTTP request cannot be made
   */
  private long getNextStartByteFromServer() throws IOException {
    HttpURLConnection connection =
        urlConnectionFactory.create(uploader.getUrl());
    connection.setRequestMethod(uploader.getHttpRequestMethod().toString());
    connection.setRequestProperty(CONTENT_LENGTH_HEADER_NAME, "0");
    connection.connect();

    if (connection.getResponseCode() != 308) {
      return 0L;
    }

    return getNextByteIndexFromRangeHeader(connection.getHeaderField("Range"));
  }

  /**
   * Returns the next byte index identifying data that the server has not
   * yet received, obtained from an HTTP Range header (e.g., a header of
   * "Range: 0-55" would cause 56 to be returned).  <code>null</code> or
   * malformed headers cause 0 to be returned.
   *
   * @param rangeHeader in the server response
   * @return the byte index beginning where the server has yet to receive data
   */
  private long getNextByteIndexFromRangeHeader(String rangeHeader) {
    if (rangeHeader == null || rangeHeader.indexOf('-') == -1) {

      // No valid range header, start from the beginning of the file.
      return 0L;
    }

    Matcher rangeMatcher =
        Pattern.compile("[0-9]+-[0-9]+").matcher(rangeHeader);
    if (!rangeMatcher.find(1)) {

      // No valid range header, start from the beginning of the file.
      return 0L;
    }

    try {
      String[] rangeParts = rangeMatcher.group().split("-");

      // Ensure that the start of the range is 0.
      long firstByteIndex = Long.parseLong(rangeParts[0]);
      if (firstByteIndex != 0) {
        return 0L;
      }

      // Return the next byte index after the end of the range.
      long lastByteIndex = Long.parseLong(rangeParts[1]);
      uploader.setNumBytesUploaded(lastByteIndex + 1);
      return  lastByteIndex + 1;
    } catch (NumberFormatException e) {
      return 0L;
    }
  }

  /**
   * Sets required and relevant HTTP headers that should be used in the upload
   * request.
   *
   * @param start byte index from which to begin sending data
   * @param length of the byte range to send in the request
   */
  private void setHeaders(HttpURLConnection conn, long start, long length) {
    long fileSize = uploader.getData().length();

    // Generate the content length header.
    conn.setRequestProperty(CONTENT_LENGTH_HEADER_NAME, String.valueOf(length));

    // Generate content range header
    // (in the form "Content-range: bytes 10-19/50".
    String contentRange = "bytes " + (fileSize == 0 ? "*/0"
        : start + "-" + (start + length - 1) + "/" + String.valueOf(fileSize));
    conn.setRequestProperty(CONTENT_RANGE_HEADER_NAME, contentRange);

    // NOTE: We intentionally leave out the "Content-type" header because
    // the upload server does a better job of detecting file types by looking
    // at the extension, and the content of the file, than we can do here.
    // Leaving out the header causes the scotty server to try to determine the
    // content type.

    // add user headers
    for (Map.Entry<String, String> header : uploader.getHeaders().entrySet()) {
      conn.setRequestProperty(header.getKey(), header.getValue());
    }
  }

  /**
   * Writes an HTTP PUT or POST request to the output stream of the url
   * connection . The request specifies appropriate HTTP headers and the
   * specified byte range of <code>file</code>.
   *
   * @return the input stream from which the response to the HTTP request can
   *     be read
   * @throws IOException if no connection can be made to the server
   */
  private ResponseMessage upload() throws IOException {
    long start = resume ? getNextStartByteFromServer() : 0L;

    while (uploader.getUploadState().equals(UploadState.IN_PROGRESS)) {

      // Compute the length to upload.
      long length = Math.min(
          (uploader.getData().length() - start), uploader.getChunkSize());

      // Establish a writable connection at the request URL.
      HttpURLConnection connection =
          urlConnectionFactory.create(uploader.getUrl());
      connection.setDoOutput(true);
      connection.setDoInput(true);
      connection.setRequestMethod(uploader.getHttpRequestMethod().toString());
      setHeaders(connection, start, length);
      OutputStream out = connection.getOutputStream();

      try {

        // Write the contents of the file (slice) to the output stream and
        // close the stream when completed.
        writeSlice(start, length, out);
        out.close();

        // Check for 308 and 503, and handle accordingly, otherwise return
        // the response stream.
        switch (connection.getResponseCode()) {
          case 308:

            // Incomplete, set the byte range to the next chunk of bytes.
            String range = connection.getHeaderField("Range");
            if (range != null) {
              start = getNextByteIndexFromRangeHeader(range);
            } else {
              start = start + length;
            }

            // Check for a new location.
            String location = connection.getHeaderField("Location");
            if (location != null) {
              uploader.setUrl(new URL(location));
            }
            uploader.getBackoffPolicy().reset();
            break;
          case 503:

            // Server error, request the uploaded range, and start at the next
            // byte index.
            if (!uploader.isPaused()) {
              start = getNextStartByteFromServer();

              // Correct the number of total uploaded bytes.
              uploader.addNumBytesUploaded(-length);

              // Backoff before making another request (pausing the upload
              // if the backoff has terminated).
              try {
                long backoffMs = uploader.getBackoffPolicy().getNextBackoffMs();
                if (backoffMs == BackoffPolicy.STOP) {
                  uploader.pause();
                } else {
                  Thread.sleep(backoffMs);
                }
              } catch (InterruptedException e) {

                // Ignore.
              }
            }
            break;
          default:

            // Complete, return the input stream for the caller to read and send
            // a completion notification.
            uploader.setUploadState(UploadState.COMPLETE);
            uploader.sendCompletionNotification();
            uploader.getBackoffPolicy().reset();
            return new ResponseMessage(connection.getContentLength(),
                connection.getInputStream());
        }
      } catch (ServerException e) {

        // If the connection was broken, try again.
        if (!uploader.isPaused()) {
          start = getNextStartByteFromServer();
        }
      } catch (IOException e) {

        // There was a file read error.
        uploader.setUploadState(UploadState.CLIENT_ERROR);
      }
    }

    // Return the input stream from which the response can be read.
    return null;
  }

  /**
   * Writes the contents of <code>file</code> specified by the byte range
   * beginning at <code>start</code> and ending at
   * <code>start + length - 1</code> inclusive.  Chunks of 64 KB are written
   * to the output stream successively, checking before each write to see
   * if the uploader has been paused, until <code>length</code> bytes have
   * been written to <code>out</code>.
   *
   * @param start byte index from which to begin sending data
   * @param length of the byte range to send in the request
   * @param out stream to write the request to
   * @throws IOException if the contents of <code>file</code> cannot be read
   *     or written properly
   * @throws ServerException if the connection to the server is broken
   */
  void writeSlice(long start, long length, OutputStream out)
      throws IOException, ServerException {

    // The number of bytes read from the file to be uploaded.
    int numRead = 0;

    // The number of expected remaining bytes to read/write. This number could
    // actually differ from the number of bytes available in the file. When
    // there is a difference, an InvalidStateException will be thrown.
    long numRemaining = length;

    // Buffer to read bytes from the file into (64 KB).
    byte[] chunk = new byte[65536];

    // Input stream to the file to upload (starting at <code>start</code>).
    UploadData uploadData = uploader.getData();
    uploadData.setPosition(start);

    synchronized (uploadData) {
      while (!uploader.isPaused()) {

        // Buffer some bytes from the file.
        if (numRemaining < chunk.length) {
          numRead = uploadData.read(chunk, 0, (int) numRemaining);
        } else {
          numRead = uploadData.read(chunk, 0, chunk.length);
        }

        try {
          // Break out of the loop if the end of the file has been reached.
          if (numRead < 0) {

            // If we expected to read more bytes from the file, but the end of
            // the file has been reached, fail the upload.
            if (numRemaining > 0) {
              out.flush();
              uploader.setUploadState(UploadState.CLIENT_ERROR);
            }
            break;
          }

          // Write a chunk of bytes to the output stream.
          out.write(chunk, 0, numRead);
          out.flush();
          numRemaining -= numRead;
          uploader.addNumBytesUploaded(numRead);

          // Break out of the loop if the end of the slice has been reached.
          if (numRemaining == 0) {
            break;
          }
        } catch (IOException e) {
          throw new ServerException();
        }
      }
    }
  }

  /**
   * Exception that should be thrown when a connection with the server is
   * broken.
   */
  class ServerException extends Exception {
  }
}
