/*
 * Copyright (c) 2010 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.api.data.sample.buzz.model;

import com.google.api.client.googleapis.GoogleTransport;
import com.google.api.client.googleapis.json.JsonContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.util.Key;

import java.io.IOException;

/**
 * Buzz activity, such as a Buzz post.
 * 
 * <p>
 * The JSON of a typical activity looks like this:
 * 
 * <pre>
 * <code>{
 *  id: "tag:google.com,2010:buzz:z12puk22ajfyzsz",
 *  object: {
 *   content: "Hey, this is my first Buzz Post!",
 *   ...
 *  },
 *  ...
 * }</code>
 * </pre>
 * 
 * @author Yaniv Inbar
 */
public final class BuzzActivity {

  /** Activity identifier. */
  @Key
  public String id;

  /** Buzz Object. */
  @Key
  public BuzzObject object;

  /**
   * Post this Buzz Activity.
   * 
   * @param transport Google transport
   * @return posted Buzz Activity response from the Buzz server
   * @throws IOException any I/O exception
   */
  public BuzzActivity post(GoogleTransport transport) throws IOException {
    HttpRequest request = transport.buildPostRequest();
    request.url = BuzzUrl.fromRelativePath("activities/@me/@self");
    JsonContent content = new JsonContent();
    content.item = this;
    request.content = content;
    return request.execute().parseAs(BuzzActivity.class);
  }

  /**
   * Post this Buzz Activity.
   * 
   * @param transport Google transport
   * @throws IOException any I/O exception
   */
  public void delete(GoogleTransport transport) throws IOException {
    HttpRequest request = transport.buildDeleteRequest();
    request.url = BuzzUrl.fromRelativePath("activities/@me/@self/" + this.id);
    request.execute().ignore();
  }
}
