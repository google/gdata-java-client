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

/**
 * An interface for receiving progress notifications for uploads.
 *
 * 
 */
public interface ProgressListener {

  /**
   * Called to notify that progress has been changed. This method is called
   * periodically (a configurable number of milliseconds depending on its use),
   * and always immediately when the upload completes. Progress notifications
   * are not made while an upload has not been started, is paused, or after it
   * completes.
   *
   * @param uploaderWithProgress reference to the uploader instance whose
   *        progress has changed.
   */
  public void progressChanged(ResumableHttpFileUploader uploaderWithProgress);
}
