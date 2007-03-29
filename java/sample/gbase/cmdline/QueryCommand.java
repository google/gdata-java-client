/* Copyright (c) 2006 Google Inc.
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

import com.google.api.gbase.client.GoogleBaseQuery;
import com.google.gdata.client.Service;

/**
 * Runs a query on Google Base items.
 * This command is called from {@link CustomerTool}.
 */
class QueryCommand extends Command {

  private String query;

  public void execute() throws Exception {

    // Build the query URL 
    GoogleBaseQuery queryObject = new GoogleBaseQuery(getCustomerFeedURL());
    queryObject.setGoogleBaseQuery(query);

    Service service = createService();
    Service.GDataRequest request =
        service.createFeedRequest(queryObject.getUrl());

    // Send the request (HTTP GET)
    request.execute();

    outputRawResponse(request);
  }

  /** Sets the Google Base query to run. */
  public void setQuery(String query) {
    this.query = query;
  }
}
