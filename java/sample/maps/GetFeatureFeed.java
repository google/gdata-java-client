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

public class GetFeatureFeed {
  
  public static void main(String[] args) {
    MapsService myService = new MapsService("getFeatureFeed");
    try {
     // Replace username and password with your authentication credentials
      myService.setUserCredentials("username","password");
      getFeatures(myService);
    } catch(AuthenticationException e) {
      System.out.println("Authentication Exception");
    } catch(ServiceException e) {
      System.out.println("Service Exception: " + e.getMessage());
    } catch(IOException e) {
      System.out.println("I/O Exception: ");
    }
  }
  
  public static void getFeatures(MapsService myService)
    throws ServiceException, IOException {

    // Get a feature feed for a specific map
	// Replace userID and mapID with appropriate values for your map
    final URL featureFeedUrl = new URL("http://maps.google.com/maps/feeds/features/userID/mapID/full");
    FeatureFeed featureFeed = myService.getFeed(featureFeedUrl, FeatureFeed.class);
    
    System.out.println("Features of the Map:");

    for (int i = 0; i < featureFeed.getEntries().size(); i++) {
      FeatureEntry entry = featureFeed.getEntries().get(i);
      
      // Print the title of the feature , #self and #post links and the KML
      System.out.println("\nTitle: " + entry.getTitle().getPlainText());
      System.out.println("Self Link: " + entry.getSelfLink().getHref());
      System.out.println("Edit Link: " + entry.getEditLink().getHref());

      System.out.println("KML: \n");

      // Print out the KML
      try {
        XmlBlob kml = entry.getKml();
        System.out.println(kml.getBlob());  
      } catch(NullPointerException e) {
        System.out.println("Null Pointer Exception");
      }        
    }
  }
}
