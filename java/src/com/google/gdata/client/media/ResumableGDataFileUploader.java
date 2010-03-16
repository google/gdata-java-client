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


package com.google.gdata.client.media;

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.data.IEntry;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ResumableUploadException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.client.uploader.ProgressListener;
import com.google.gdata.client.uploader.ResumableHttpFileUploader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Nullable;

/**
 * Provides Google Data API specific capabilities to
 * {@link ResumableHttpFileUploader}. 
 *
 * 
 */
public class ResumableGDataFileUploader extends ResumableHttpFileUploader {

  public static final String RESUMABLE_EDIT_MEDIA_REL = "resumable-edit-media";

  /**
   * Types of resumable update request types.
   * INSERT - add new media
   * UPDATE - update an existing media with metadata if available.
   * UPDATE_MEDIA_ONLY - update only media (not metadata).
   */
  public enum RequestType {
    INSERT, UPDATE, UPDATE_MEDIA_ONLY
  }

  private final MediaService service;

  /**
   * Constructor to update a media with progress update.  Use {@link Builder}
   * to construct this instance.
   *
   * @param uploadUrl resumable media upload url.
   * @param file media file to upload.
   * @param service {@link MediaService} for target service.
   * @param chunkSize max chunk size in bytes to include in each request.
   * @param executor resource pool to execute upload task.
   * @param progressListener callback to listen to progress updates.
   * @param progressIntervalMillis how often to notify about update progress.
   * @throws IOException any read/write error
   * @throws ServiceException any service specific error
   */
  private ResumableGDataFileUploader(URL uploadUrl, MediaFileSource file,
      MediaService service, long chunkSize, ExecutorService executor,
      ProgressListener progressListener, long progressIntervalMillis)
      throws IOException, ServiceException {
    super(uploadUrl, file.getMediaFile(), executor, progressListener, chunkSize,
        progressIntervalMillis);
    this.service = service;
  }

  /**
   * Returns the upload response as an instance of parsed entryClass.
   *
   * @param entryClass the class that will be used to represent the resulting
   *        entry.
   * @return the inserted media Entry returned by the service.
   * @throws IOException error communicating with the GData Service.
   * @throws ServiceException insert request failed due to service error.
   */
  public <E extends IEntry> E getResponse(Class<E> entryClass)
      throws IOException, ServiceException {
    InputStream response = super.getResponse().getInputStream();
    if (getUploadState() == UploadState.CLIENT_ERROR) {
      throw new ResumableUploadException("client error");
    } else if (response == null) {
      System.out.println("No response found");
      return null;
    }
    return service.parseResumableUploadResponse(response, ContentType.ATOM,
        entryClass);
  }


  /**
   * Builder to construct a {@link ResumableGDataFileUploader}.
   */
  public static class Builder {

    /* resumable create or edit media url */
    private URL mediaUrl;

    /* media entry to update */
    private IEntry mediaEntry;

    /* service instance for authenticated requests */
    private MediaService mediaService;

    /* Media file to upload */
    private MediaFileSource mediaFile;

    /* Title for the media to upload */
    private String mediaTitle;

    /* Max size in bytes for a single media resumable upload request */
    private long chunkSize = ResumableHttpFileUploader.DEFAULT_MAX_CHUNK_SIZE;

    /* Executor service to execute asynchronous upload tasks */
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    /* Callback to receive upload progress notifications */
    private ProgressListener listener;

    /* Interval between progress updates in millis */
    private long progressInterval = 100;

    /* Resumable media request type */
    RequestType requestType = RequestType.INSERT;

    /**
     * Builds a {@link ResumableGDataFileUploader} to upload new media.
     *
     * @param service media service
     * @param mediaUrl resumable-create-media url.
     * @param mediaFile file to upload.
     * @param mediaEntry metadata for the uploaded file.
     */
    public Builder(MediaService service, URL mediaUrl,
        MediaFileSource mediaFile, IEntry mediaEntry) {
      this.mediaService = service;
      this.mediaUrl = mediaUrl;
      this.mediaFile = mediaFile;
      this.mediaEntry = mediaEntry;
    }

    /**
     * Builds a {@link ResumableGDataFileUploader} to update an existing media.
     *
     * @param service media service.
     * @param mediaFile updated file to upload.
     * @param entryToUpdate updated metadata associated with the existing entry.
     */
    public Builder(
        MediaService service, MediaFileSource mediaFile, IEntry entryToUpdate) {
      this.mediaEntry = entryToUpdate;
      this.mediaFile = mediaFile;
      this.mediaService = service;
    }

    /**
     * Sets title for uploaded media.  This value passed as Slug header in
     * media requests.
     *
     * @param mediaTitle title for new media.
     * @return builder.
     */
    public Builder title(String mediaTitle) {
      this.mediaTitle = mediaTitle;
      return this;
    }

    /**
     * Max content size for media upload request.
     *
     * @param chunkSize max upload size in bytes.
     * @return builder.
     */
    public Builder chunkSize(long chunkSize) {
      this.chunkSize = chunkSize;
      return this;
    }

    /**
     * Sets {@link ExecutorService} to execute asynchronous tasks.
     *
     * @param executor executor service to use.
     * @return builder.
     */
    public Builder executor(ExecutorService executor) {
      this.executor = executor;
      return this;
    }

    /**
     * Sets parameters to track upload progress.
     *
     * @param listener {@link ProgressListener} callback for tracking progress.
     * @param progressInterval time interval in millis for progress
     *        notifications.
     * @return builder.
     */
    public Builder trackProgress(
        ProgressListener listener, long progressInterval) {
      this.listener = listener;
      this.progressInterval = progressInterval;
      return this;
    }

    /**
     * Sets the media upload request type.
     *
     * @param requestType one of INSERT, UPDATE, UPDATE_MEDIA_ONLY
     * @return builder.
     */
    public Builder requestType(RequestType requestType) {
      this.requestType = requestType;
      return this;
    }

    /**
     * Creates a {@link ResumableGDataFileUploader} instance.
     *
     * @return uploader.
     * @throws IOException any read/write error.
     * @throws ServiceException any server error.
     */
    public ResumableGDataFileUploader build()
        throws IOException, ServiceException {
      Preconditions.checkState(mediaUrl != null || mediaEntry != null);
      Preconditions.checkNotNull(mediaService);
      Preconditions.checkNotNull(mediaFile);

      // make GData request to create resumable upload session
      URL uploadUrl = null;
      switch(requestType) {
        case INSERT:
          if (mediaEntry != null) {
            uploadUrl = mediaService.createResumableUploadSession(
                mediaUrl, mediaEntry, mediaFile);
          } else {
            uploadUrl = mediaService.createResumableUploadSession(
                mediaUrl, mediaTitle, mediaFile);
          }
          break;
        case UPDATE:
          uploadUrl = mediaService.createResumableUpdateSession(
              new URL(mediaEntry.getResumableEditMediaLink().getHref()),
              mediaEntry, mediaFile, false);
          break;
        case UPDATE_MEDIA_ONLY:
          if (mediaEntry != null) {
            mediaUrl = new URL(
                mediaEntry.getResumableEditMediaLink().getHref());
          }
          uploadUrl = mediaService.createResumableUpdateSession(
              mediaUrl, mediaEntry, mediaFile, true);
          break;
      }

      ResumableGDataFileUploader uploader = new ResumableGDataFileUploader(
          uploadUrl, mediaFile, mediaService, chunkSize, executor, listener,
          progressInterval);

      // Set additional headers
      uploader.addHeader("Content-Type", mediaFile.getContentType());
      return uploader;
    }
  }

}
