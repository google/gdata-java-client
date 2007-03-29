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

import com.google.api.gbase.client.FeedURLFactory;
import com.google.api.gbase.client.GoogleBaseService;
import com.google.api.gbase.client.ServiceError;
import com.google.api.gbase.client.ServiceErrors;
import com.google.gdata.util.ServiceException;

import java.io.IOException;

/**
 * Utility class that allows creating simple Example Tools.
 * 
 * Contains methods for parsing the arguments and for printing errors.
 */
public abstract class Example {

  protected static FeedURLFactory urlFactory = FeedURLFactory.getDefault();
  
  protected static GoogleBaseService service;
 

  /**
   * Parses the arguments, creates a FeedURLFactory and a GoogleBaseService.
   * @return the remaining arguments
   */
  public static String[] init(String[] args, String applicationName) 
      throws IOException {
    String baseUrl = null;

    int argsIndex = 0;
    while (argsIndex < args.length && args[argsIndex].startsWith("-")) {
      String arg = args[argsIndex];
      argsIndex++;
      if ( argsIndex >= args.length) {
        throw new IllegalArgumentException("Expected a parameter value " +
            "after " + arg);
      }
      String value = args[argsIndex];
      argsIndex++;
      if ("--url".equals(arg)) {
        baseUrl = value;
      } else if("--key".equals(arg)) {
        // This parameter used to contain the developer key.
        // It is still accepted so as not to break scripts that used it, but
        // it is now ignored.
      } else {
        throw new IllegalArgumentException("unknown parameter: " + arg);
      }
    }

    if(baseUrl != null) {
      urlFactory = new FeedURLFactory(baseUrl);
    }

    // service.query does a GET on the url above and parses the result,
    // which is an ATOM feed with some extensions (called the Google Base
    // data API items feed).
    service = new GoogleBaseService(applicationName);
    if (argsIndex > 0) {
      String[] newargs = new String[args.length - argsIndex];
      System.arraycopy(args, argsIndex, newargs, 0, newargs.length);
      args = newargs;
    }
    return args;
  }

  /**
   * Prints an error message returned by the server, if any.
   * 
   * @param e an exception that may contain an error message from the server
   */
  protected static void printServiceException(ServiceException e) {

    System.err.print("Error");
    if (e.getHttpErrorCodeOverride() > 0) {
      System.err.print(e.getHttpErrorCodeOverride());
    }
    System.err.print(": ");
    System.err.println(e.getMessage());

    ServiceErrors errors = new ServiceErrors(e);
    for (ServiceError error: errors.getAllErrors()) {
      String field = error.getField();
      System.err.print("  ");
      if (field != null) {
        System.err.print("in field '");
        System.err.print(field);
        System.err.print("' ");
      }
      System.err.println(error.getReason());
    }
  }


}
