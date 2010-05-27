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

package com.google.api.client.json;

import com.google.api.client.http.HttpContent;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Serializes JSON content based on the data key/value mapping object for an
 * item.
 * <p>
 * Sample usage:
 * 
 * <pre>
 * <code>
 * static void setContent(HttpRequest request, Object item) {
 *     JsonHttpContent content = new JsonHttpContent(Json.CONTENT_TYPE);
 *     content.item = item;
 *     request.content = content;
 * }
 * </code>
 * </pre>
 * 
 * @since 2.3
 * @author Yaniv Inbar
 */
public class JsonHttpContent implements HttpContent {
  // TODO: ability to annotate fields as only needed for POST?

  /** Content type, for example {@link Json#CONTENT_TYPE}. */
  public final String contentType;

  /**
   * @param contentType Content type, for example {@link Json#CONTENT_TYPE}
   */
  public JsonHttpContent(String contentType) {
    this.contentType = contentType;
  }

  public Object item;

  public long getLength() {
    // TODO
    return -1;
  }

  public final String getEncoding() {
    return null;
  }

  public String getType() {
    return Json.CONTENT_TYPE;
  }

  public void writeTo(OutputStream out) throws IOException {
    JsonGenerator generator =
        Json.JSON_FACTORY.createJsonGenerator(out, JsonEncoding.UTF8);
    Json.serialize(generator, this.item);
    generator.close();
  }
}
