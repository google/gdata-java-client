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
package sample.maps;

import com.google.gdata.client.*;
import com.google.gdata.client.maps.*;
import com.google.gdata.data.*;
import com.google.gdata.data.maps.*;
import com.google.gdata.util.*;
import java.io.IOException;
import java.net.URL;

public class ListMaps {
  
  public static void main(String[] args) {
    MapsService myService = new MapsService("listAllMaps");
    try {
     // Replace username and password with your authentication credentials
      myService.setUserCredentials("username","password");
      printUserMaps(myService);
    } catch(AuthenticationException e) {
      System.out.println("Authentication Exception");
    } catch(ServiceException e) {
      System.out.println("Service Exception: " + e.getMessage());
    } catch(IOException e) {
      System.out.println("I/O Exception: ");
    }
  }

  public static void printUserMaps(MapsService myService)
    throws ServiceException, IOException {

    // Request the default metafeed
    final URL feedUrl = new URL("http://maps.google.com/maps/feeds/maps/default/full");
    MapFeed resultFeed = myService.getFeed(feedUrl, MapFeed.class);

    // Iterate through results and print out title,summary,self and edit links
   
    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
      MapEntry entry = resultFeed.getEntries().get(i);
      System.out.println("\t" + entry.getTitle().getPlainText());
      System.out.println("\t" + entry.getSummary().getPlainText());
      System.out.println("Self link: " + entry.getSelfLink().getHref());
      System.out.println("Edit link: " + entry.getEditLink().getHref());
      try {
        System.out.println("Feature Feed: " + entry.getFeedLink().getHref());
      } catch (NullPointerException e) {
        System.out.println("Null features: ");
      }
    }
  }
}
