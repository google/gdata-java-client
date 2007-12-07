/* Copyright (c) 2007 Google Inc.
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

package sample.gbase.cmdline;

import com.google.gdata.client.Service;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.data.media.MediaFileSource;
import com.google.gdata.data.media.MediaSource;
import com.google.gdata.util.ContentType;

import java.io.File;
import java.net.URL;

/**
 * Inserts a new media attachment to a Google Base item, by using
 * a media binary POST. See 
 * {@link com.google.api.gbase.client.GoogleBaseService#insert(URL, Class, MediaSource)}
 * for an easier way to do a media binary POST, or look at 
 * {@link com.google.api.gbase.client.GoogleBaseService#insert(URL, com.google.gdata.data.BaseEntry)}
 * for an easier way to insert an attachment together with the media entry (meta-data) describing it.
 * This command is called from {@link CustomerTool}.
 */
class InsertMediaCommand extends Command {

  private String itemMediaUrl;
  private String attachmentFile;
  private String attachmentMimeType;
  private String caption;
  
  @Override
  public void execute() throws Exception {
    MediaFileSource media = new MediaFileSource(new File(attachmentFile), attachmentMimeType);
    
    Service service = createService();
    Service.GDataRequest request = service.createRequest(GDataRequest.RequestType.INSERT, 
        new URL(itemMediaUrl), new ContentType(attachmentMimeType));

    if (caption != null) {
      request.setHeader("Slug", caption);
    }
    
    MediaSource.Output.writeTo(media, request.getRequestStream());

    // Send the request (HTTP POST)
    request.execute();

    // Save the response
    outputRawResponse(request);
  }

  /** Set the url of the media feed for the item to insert the attachment into. */
  public void setItemMediaUrl(String itemMediaUrl) {
    this.itemMediaUrl = itemMediaUrl;
  }
  
  /** Sets the path to the file on the disk containing the attachment to upload. */
  public void setAttachmentFile(String attachmentFile) {
    this.attachmentFile = attachmentFile;
  }
  
  /** Sets the mime-type of the attachment. */
  public void setAttachmentMimeType(String attachmentMimeType) {
    this.attachmentMimeType = attachmentMimeType;
  }
  
  /** Sets the caption (title) of the attachment. */
  public void setCaption(String caption) {
    this.caption = caption;
  }
}
