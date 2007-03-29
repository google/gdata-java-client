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

import com.google.gdata.util.ServiceException;
import com.google.api.gbase.client.ServiceErrors;
import com.google.api.gbase.client.ServiceError;

import java.util.List;

/**
 * An example tool that helps manage customer items on 
 * Google Base using Google data API.
 *
 * This tool deals directly with XML feeds. If you would
 * like to use a higher-level API, have a look at QueryExample.
 * 
 * Have a look at the different <code>*Command</code> classes for some more
 * interesting code. 
 */
public class CustomerTool {

  public static void main(String[] args) throws Exception {
    Command command = CommandFactory.createCommand(args);
    try {
      command.execute();
    } catch (ServiceException e) {
      /* Display the error message sent by the server, if it 
       * is available. A real application would need to parse
       * the body (as HTML or XML, depending on e.getContentType())
       * and display it nicely.
       */
      StringBuffer message = new StringBuffer("Response code:");
      ServiceErrors errors = new ServiceErrors(e);

      if (e.getHttpErrorCodeOverride() > 0) {
        message.append(" ");
        message.append(e.getHttpErrorCodeOverride());
      }
      message.append(" ");
      message.append(e.getMessage());
      System.err.println(message);

      List<? extends ServiceError> allErrors = errors.getAllErrors();
      for (ServiceError error: allErrors) {
        String field = error.getField();
        StringBuffer buffer = new StringBuffer();
        buffer.append("  ");
        if (field != null) {
          buffer.append("in field '");
          buffer.append(field);
          buffer.append("'");
          buffer.append(": ");
        }
        buffer.append(error.getReason());
        System.err.println(buffer);
      }
      System.exit(10);
    }
  }
}