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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Uploads a file using resumable HTTP requests (see {@linkplain
 * "http://code.google.com/p/gears/wiki/ResumableHttpRequestsProposal"}). This
 * implementation supports time based progress notifications, polling for
 * progress, resumability, and completion notifications.
 *
 * Each instance creates a separate task (to be executed by an ExecutorService),
 * which actually generates the HTTP request and writes bytes to the server.
 * The task blocks a thread spawned by the ExecutorService) for the duration of
 * the upload (i.e., until the upload is either completed, paused, or an error
 * occurs). This frees the current thread from blocking, which allows various
 * non-blocking interaction with the uploader (like polling for progress,
 * preventing UI from being blocked, etc.).
 *
 * 
 */
public class ResumableHttpFileUploader {

  /**
   * The response message returned by the upload task when it has finished
   * uploading the last chunk. The response message instance will hold the
   * expected Content-Length header value and the {@link InputStream} returned
   * by the HTTP connection. Note that the input stream might not be ready yet
   * to read from when the upload task is finished. The connection might still
   * be receiving the message body.
   */
  public static class ResponseMessage {
    private final int contentLength;
    private final InputStream inputStream;

    public ResponseMessage(int contentLength, InputStream inputStream) {
      this.contentLength = contentLength;
      this.inputStream = inputStream;
    }

    /**
     * Returns the value of the Content-Length header of the HTTP response.
     *
     * @return the size of the HTTP response body in bytes.
     */
    public int getContentLength() {
      return contentLength;
    }

    /**
     * Returns the last request's connection's input stream to read the response
     * body from.
     *
     * @return input stream of the most recent HTTP connection.
     */
    public InputStream getInputStream() {
      return inputStream;
    }

    /**
     * Attempts to receive the entire outstanding response message body and
     * returns it as a string.
     *
     * @param timeoutMs the maximum time to wait for the message to be received.
     * @return the full message body as a string.
     * @throws InterruptedException if the task gets interrupted.
     * @throws ExecutionException if a {@link IOException} is thrown while
     *         reading from the input stream.
     * @throws TimeoutException if the entire message couldn't be received in
     *         the allotted timeout.
     */
    public String receiveMessage(long timeoutMs) throws InterruptedException,
        ExecutionException, TimeoutException {
      return Executors.newSingleThreadExecutor().submit(
          new Callable<String>() {

            public String call() throws Exception {
              int received = 0;
              StringBuilder message = new StringBuilder();
              while (received < contentLength) {
                int avail = inputStream.available();
                if (avail > 0) {
                  byte[] buf = new byte[avail];
                  received += inputStream.read(buf, 0, avail);
                  message.append(new String(buf));
                } else {
                  Thread.sleep(10L);
                }
              }
              return message.toString();
            }
          }).get(timeoutMs, TimeUnit.MILLISECONDS);
    }
  }

  /**
   * Upload state associated with this file uploader.  <code>CLIENT_ERROR</code>
   * means that the uploader was unable to execute the upload properly because
   * of a thread execution error, or if a file was manipulated between the time
   * that the upload was started and completed.
   */
  public enum UploadState {
    COMPLETE, CLIENT_ERROR, IN_PROGRESS, NOT_STARTED, PAUSED
  }

  /**
   * Http request type to use in upload requests.
   */
  public enum RequestMethod {
    POST, PUT
  }

  /**
   * Default maximum number of bytes that will be uploaded to the server in any
   * single HTTP request (set to 10 MB).
   */
  public static long DEFAULT_MAX_CHUNK_SIZE = 10485760L;

  /**
   * Default number of milliseconds for the progress notification interval.
   */
  public static final long DEFAULT_PROGRESS_INTERVAL_MS = 100L;

  /**
   * Method-override http header.
   */
  public static final String METHOD_OVERRIDE = "X-HTTP-Method-Override";

  /**
   * Timer task for sending progress notifications.  Instances should only be
   * constructed and run where all constructor parameters are non-null.
   */
  private class NotificationTask extends TimerTask {
    private final ResumableHttpFileUploader fileUploader;
    private final ProgressListener listener;
    private final Timer timer;

    public NotificationTask(ResumableHttpFileUploader fileUploader,
        ProgressListener listener, Timer timer) {
      this.fileUploader = fileUploader;
      this.listener = listener;
      this.timer = timer;
    }

    @Override
    public void run() {
      if (!fileUploader.getUploadState().equals(UploadState.IN_PROGRESS)) {
        timer.cancel();
      }
      listener.progressChanged(fileUploader);
    }
  }

  /**
   * Number of bytes that have been successfully uploaded to the server by
   * this uploader.
   */
  private long numBytesUploaded = 0L;

  /**
   * The current state of the uploader.
   */
  private UploadState uploadState = UploadState.NOT_STARTED;

  /**
   * The future which will contain the eventual response stream from the upload
   * server.
   */
  private Future<ResponseMessage> uploadResultFuture;

  /**
   * The file to upload.
   */
  private final UploadData data;

  /**
   * The URL which locates the destination of the upload.
   */
  private URL url;

  /**
   * HTTP request method to use when uploading.
   */
  private RequestMethod httpRequestMethod;

  /**
   * Extra http headers to send in each request.
   */
  private Map<String, String> headers = new HashMap<String, String>();

  /**
   * Timer for sending progress notifications on a fixed time interval.
   */
  private Timer progressNotifier;

  /**
   * Executor service to execute asynchronous upload tasks.
   */
  private final ExecutorService executor;

  /**
   * Factory for creating HTTP connections.
   */
  private final UrlConnectionFactory urlConnectionFactory;

  /**
   * Progress listener interface instance to send progress notifications to.
   */
  private final ProgressListener progressListener;

  /**
   * Number of milliseconds between progress listener notifications.
   */
  private final long progressIntervalMillis;

  /**
   * Maximum size of individual chunks that will get uploaded by single HTTP
   * requests.
   */
  private final long chunkSize;

  /**
   * Back off policy which determines the amount of time to wait before retrying
   * an HTTP request.
   */
  private final BackoffPolicy backoffPolicy;

  /**
   * Builder class for constructing {@link ResumableHttpFileUploader} instances.
   */
  public static class Builder {
    private URL url;
    private UploadData data;
    private ExecutorService executor;
    private UrlConnectionFactory urlConnectionFactory =
        UrlConnectionFactory.DEFAULT;
    private ProgressListener progressListener;
    private long chunkSize = DEFAULT_MAX_CHUNK_SIZE;
    private long progressIntervalMillis = DEFAULT_PROGRESS_INTERVAL_MS;
    private RequestMethod requestMethod = RequestMethod.PUT;
    private BackoffPolicy backoffPolicy = BackoffPolicy.DEFAULT;

    /**
     * @param url which locates the destination of the upload request
     * @return this
     */
    public Builder setUrl(URL url) {
      this.url = url;
      return this;
    }

    /**
     * @param file to be uploaded.
     * @return this
     * @throws IOException if the file could not be read. 
     */
    public Builder setFile(File file) throws IOException {
      // Ensure file exists, that it is not null, and that it is readable.
      if (file == null || !file.exists() || !file.canRead()) {
        throw new IOException("The file must exist and be readable.");
      }
      this.data = new FileUploadData(file);
      return this;
    }

    /**
     * @param data to be uploaded.
     * @return this
     */
    public Builder setData(UploadData data) {
      this.data = data;
      return this;
    }
    
    /**
     * @param executor service to execute asynchronous upload tasks with
     * @return this
     */
    public Builder setExecutorService(ExecutorService executor) {
      this.executor = executor;
      return this;
    }

    /**
     * @param urlConnectionFactory
     * @return this
     */
    public Builder setUrlConnectionFactory(
        UrlConnectionFactory urlConnectionFactory) {
      this.urlConnectionFactory = urlConnectionFactory;
      return this;
    }

    /**
     * @param progressListener for receiving progress notifications
     * @return this
     */
    public Builder setProgressListener(ProgressListener progressListener) {
      this.progressListener = progressListener;
      return this;
    }

    /**
     * @param chunkSize size of the chunks that will get uploaded by individual
     *     HTTP requests
     * @return this
     */
    public Builder setChunkSize(long chunkSize) {
      this.chunkSize = chunkSize;
      return this;
    }

    /**
     * @param progressIntervalMillis number of milliseconds between
     *     progress listener notifications
     * @return this
     */
    public Builder setProgressIntervalMillis(long progressIntervalMillis) {
      this.progressIntervalMillis = progressIntervalMillis;
      return this;
    }

    /**
     * @param requestMethod the http request type for upload.  Use either
     *     PUT request or POST request with x-http-method-override header set
     *     to PUT.
     * @return this
     */
    public Builder setRequestMethod(RequestMethod requestMethod) {
      this.requestMethod = requestMethod;
      return this;
    }

    /**
     * @param backoffPolicy to determine how long to wait until retrying HTTP
     *     requests
     * @return this
     */
    public Builder setBackoffPolicy(BackoffPolicy backoffPolicy) {
      this.backoffPolicy = backoffPolicy;
      return this;
    }

    /**
     * Constructs a ResumableHttpFileUploader instance from this builder.
     *
     * @return a new ResumableHttpFileUploader according to the builder
     *     parameters
     * @throws IOException
     */
    public ResumableHttpFileUploader build() throws IOException {
      return new ResumableHttpFileUploader(this);
    }
  }

  /**
   * Constructs a new uploader that uses the default maximum chunk size per
   * HTTP request.
   *
   * @param url which locates the destination of the upload request
   * @param file containing bytes to send to the server
   * @param executor service to execute asynchronous upload tasks with
   * @param progressListener for receiving progress notifications
   * @param progressIntervalMillis number of milliseconds between
   *     progress listener notifications
   * @throws IOException if the file is not readable or does not exist
   * @deprecated Please use {@link ResumableHttpFileUploader.Builder}
   */
  @Deprecated
  public ResumableHttpFileUploader(URL url, File file,
      ExecutorService executor, ProgressListener progressListener,
      long progressIntervalMillis) throws IOException {
    this(new Builder()
        .setUrl(url)
        .setFile(file)
        .setExecutorService(executor)
        .setProgressListener(progressListener)
        .setProgressIntervalMillis(progressIntervalMillis));
  }

  /**
   * Constructs a new uploader with configurable chunk size to use per HTTP
   * request.
   *
   * @param url which locates the destination of the upload request
   * @param file containing bytes to send to the server
   * @param executor service to execute asynchronous upload tasks with
   * @param progressListener for receiving progress notifications
   * @param chunkSize size of the chunks that will get uploaded by individual
   *     HTTP requests
   * @param progressIntervalMillis number of milliseconds between
   *     progress listener notifications
   * @throws IOException if the file is not readable or does not exist
   * @deprecated Please use {@link ResumableHttpFileUploader.Builder}
   */
  @Deprecated
  public ResumableHttpFileUploader(URL url, File file,
      ExecutorService executor, ProgressListener progressListener,
      long chunkSize, long progressIntervalMillis) throws IOException {
    this(new Builder()
        .setUrl(url)
        .setFile(file)
        .setExecutorService(executor)
        .setProgressListener(progressListener)
        .setChunkSize(chunkSize)
        .setProgressIntervalMillis(progressIntervalMillis));
  }

  /**
   * Constructs a new uploader from a builder.
   *
   * @param builder to use to construct this uploader
   * @throws IOException IOException if the file is not readable or does not
   *     exist
   */
  ResumableHttpFileUploader(Builder builder) throws IOException  {
    url = builder.url;
    data = builder.data;
    executor = builder.executor;
    urlConnectionFactory = builder.urlConnectionFactory;
    progressListener = builder.progressListener;
    progressIntervalMillis = Math.max(0, builder.progressIntervalMillis);
    chunkSize = builder.chunkSize;
    httpRequestMethod = builder.requestMethod;
    backoffPolicy =  builder.backoffPolicy;

    // Ensure a valid URL is passed.
    checkArgument(url != null && url.getHost() != null
        && url.getHost().length() > 0 && url.getPath() != null
        && url.getPath().length() > 0,
        "The url must be non null and have a non-empty host and path.");

    // Ensure a valid executor.
    checkArgument(executor != null,
        "Must provide a non-null executor service.");

    // Ensure non-null factories.
    checkArgument(urlConnectionFactory != null, "Factories must be non-null.");

    // Add method override if using POST.
    if (RequestMethod.POST.equals(httpRequestMethod)) {
      addHeader(METHOD_OVERRIDE, RequestMethod.PUT.toString());
    }
  }

  /**
   * Set the http request type for upload.  Resumable upload can accept either
   * PUT request or POST request with x-http-method-override header set to PUT.
   *
   * @param requestMethod http request type
   * @deprecated Please use {@link
   *     ResumableHttpFileUploader.Builder#setRequestMethod(RequestMethod)}
   */
  @Deprecated
  public void setHttpRequestMethod(RequestMethod requestMethod) {
    this.httpRequestMethod = requestMethod;
    if (RequestMethod.POST.equals(requestMethod)) {
      addHeader(METHOD_OVERRIDE, RequestMethod.PUT.toString());
    }
  }

  /**
   * Returns the http request method to use for upload.
   */
  public RequestMethod getHttpRequestMethod() {
    return this.httpRequestMethod;
  }

  /**
   * Add a http header to send in each of the upload requests.
   *
   * @param key http header name
   * @param value http header value
   * @return old value if any
   */
  public String addHeader(String key, String value) {
    return headers.put(key, value);
  }

  /**
   * Return list of user specified headers.  Package private to limit access to
   * {@link ResumableHttpUploadTask}.
   *
   * @return map of http headers.
   */
  Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * Gets the back off policy instance that determines how long to wait before
   * retrying an HTTP request.
   *
   * @return the back off policy instance
   */
  BackoffPolicy getBackoffPolicy() {
    return backoffPolicy;
  }

  /**
   * Gets the total number of bytes uploaded by this uploader.
   *
   * @return the number of bytes uploaded
   */
  public synchronized long getNumBytesUploaded() {
    return numBytesUploaded;
  }

  /**
   * Gets the upload progress denoting the percentage of bytes that have been
   * uploaded, represented between 0.0 (0%) and and 1.0 (100%).
   *
   * @return the upload progress
   */
  public double getProgress() {
    long fileLength = data.length();
    if (fileLength == 0) {
      return uploadState.equals(UploadState.COMPLETE) ? 1 : 0;
    } else {
      return (double) getNumBytesUploaded() / fileLength;
    }
  }

  /**
   * Gets the response from the server if it is available.  If the stream is not
   * yet available, <code>null</code> is returned.
   *
   * @return the stream containing the response from the server
   */
  public ResponseMessage getResponse() {
    if ((uploadResultFuture != null) && uploadResultFuture.isDone()) {
      try {
        return uploadResultFuture.get();
      } catch (ExecutionException e) {
        setUploadState(UploadState.CLIENT_ERROR);
      } catch (InterruptedException e) {
        setUploadState(UploadState.CLIENT_ERROR);
        throw new IllegalStateException("InterruptedException even though "
            + "upload is done (should never get here).");
      }
    }
    return null;
  }

  /**
   * Gets the current upload state of the uploader.
   *
   * @return the upload state
   */
  public synchronized UploadState getUploadState() {
    return uploadState;
  }

  /**
   * Identifies if the uploader is paused
   *
   * @return <code>true</code> if the uploader is paused
   */
  public synchronized boolean isPaused() {
    return uploadState.equals(UploadState.PAUSED);
  }

  /**
   * Causes the uploader to pause uploading.  The uploader may be resumed later
   * by calling {@link #resume()}.  This method does not block.
   */
  public synchronized void pause() {
    setUploadState(UploadState.PAUSED);
    if (progressNotifier != null) {
      progressNotifier.cancel();
    }
  }

  /**
   * Resumes an upload if it is currently paused, or if it has not yet started.
   * This should be called if the server has received some bytes for the file.
   * Note that it causes an extra HTTP request to be sent to the server in order
   * to determine the offset at which the uploader should begin sending bytes.
   * This method does not block.
   */
  public void resume() {
    if (uploadState.equals(UploadState.PAUSED)
        || uploadState.equals(UploadState.NOT_STARTED)) {
      upload(true);
    }
  }

  /**
   * Starts an upload beginning with the first byte in the file.  This method
   * does not block.
   *
   * @return future to access upload result
   */
  public Future<ResponseMessage> start() {
    upload(false);
    return uploadResultFuture;
  }

  /**
   * Identifies if the upload task has completed
   *
   * @return <code>true</code> if the uploader is done
   */
  public synchronized boolean isDone() {
    return (uploadResultFuture != null) && uploadResultFuture.isDone();
  }

  /**
   * Convenience method for incrementing the count of the total number of
   * uploaded bytes.
   *
   * @param numBytes to add to the uploaded byte count
   */
  synchronized void addNumBytesUploaded(long numBytes) {
    numBytesUploaded += numBytes;
  }

  /**
   * Gets the file associated with this uploader.
   *
   * @return the file to upload
   */
  public UploadData getData() {
    return data;
  }

  /**
   * Gets the URL to upload to.
   *
   * @return the upload URL
   */
  URL getUrl() {
    return url;
  }

  /**
   * Sets the URL to upload to.
   */
  void setUrl(URL url) {
    this.url = url;
  }

  /**
   * Gets the size of media to upload in each HTTP request.
   *
   * @return chunk size
   */
  long getChunkSize() {
    return chunkSize;
  }

  /**
   * Sends a progress notification to the progress listener if one has been
   * specified.
   */
  void sendCompletionNotification() {
    if (progressListener != null) {
      new NotificationTask(this, progressListener, progressNotifier).run();
    }
  }

  /**
   * Sets the number of bytes that have been uploaded.
   *
   * @param numBytes that have been uploaded
   */
  synchronized void setNumBytesUploaded(long numBytes) {
    numBytesUploaded = numBytes;
  }

  /**
   * Sets the upload state
   *
   * @param state value to set to
   */
  synchronized void setUploadState(UploadState state) {
    uploadState = state;
  }

  /**
   * Throws an illegal argument exception if <code>condition</code> is not
   * true.
   *
   * @param condition to be checked
   * @param errorMsg to be thrown in an exception
   */
  private void checkArgument(boolean condition, String errorMsg) {
    if (!condition) {
      throw new IllegalArgumentException(errorMsg);
    }
  }

  /**
   * Fires off an upload task.  If <code>resume<code> is <code>true</code>, an
   * HTTP request is made to the server to determine the number of bytes that
   * the server has already received (if any), otherwise the task attempts to
   * upload from the beginning of the file.  The task blocks until the upload
   * completes, is paused, or an error occurs.  The task is submitted to the
   * executor, however, and thus does not block in the current thread.
   *
   * @param resume <code>true</code> if the file should be resumed
   */
  private void upload(boolean resume) {
    setUploadState(UploadState.IN_PROGRESS);
    ResumableHttpUploadTask task = new ResumableHttpUploadTask(
        urlConnectionFactory, this, resume);

    if (progressListener != null) {
      progressNotifier = new Timer();
      progressNotifier.schedule(
          new NotificationTask(this, progressListener, progressNotifier),
          0, progressIntervalMillis);
    }

    uploadResultFuture = executor.submit(task);
  }
}
