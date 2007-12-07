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

import java.net.URL;

/**
 * Queries the specified media feed for all the media attachments
 * of one item. 
 * This command is called from {@link CustomerTool}.
 */
class QueryMediaCommand extends Command {

  private String itemMediaUrl;
  
  @Override
  public void execute() throws Exception {
    Service service = createService();
    Service.GDataRequest request = service.createFeedRequest(new URL(itemMediaUrl));

    // Send the request (HTTP GET)
    request.execute();

    outputRawResponse(request);
  }

  /** Set the url of the media feed. */
  public void setItemMediaUrl(String itemMediaUrl) {
    this.itemMediaUrl = itemMediaUrl;
  }
}
