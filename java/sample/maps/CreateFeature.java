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
import java.io.*;
import java.net.URL;

public class CreateFeature {

  public static void main(String[] args) {
    MapsService myService = new MapsService("createFeature");
    try {
      // Replace username and password with your authentication credentials
      myService.setUserCredentials("username","password");
      createFeature(myService);
    } catch(AuthenticationException e) {
      System.out.println("Authentication Exception");
    } catch(ServiceException e) {
      System.out.println("Service Exception: " + e.getMessage());
    } catch(IOException e) {
      System.out.println("I/O Exception");
    }
  }
  
  // Utility method to read KML from a file and create a string
  private static String getKml(String filename) throws IOException {
    StringBuilder sb = new StringBuilder();
    BufferedReader r = new BufferedReader(new FileReader(filename));
    int c;
    while ((c = r.read()) != -1) {
      sb.append((char) c); 
    } 
    return sb.toString();
  }

  public static FeatureEntry createFeature(MapsService myService)
      throws ServiceException, IOException {

    // Use the feature feed's #post URL as the Edit URL for this map
	// Replace userID and mapID with appropriate values for your map
    final URL featureEditUrl = new URL("http://maps.google.com/maps/feeds/features/userID/mapID/full");

    // Create a blank FeatureEntry object
    FeatureEntry featureEntry = new FeatureEntry();
    
    try {
      // KML is simply XML so we'll use the XmlBlob object to store it
      // Replace placemark.kml file with your own KML <Placemark> file
      XmlBlob kml = new XmlBlob();
      kml.setBlob(CreateFeature.getKml("placemark.kml"));

      // Set the KML for this feature
      // Note that the KML should only include <Placemark> entries
      featureEntry.setKml(kml);

      // Set the tile for the feature. Each atom entry requires a feature
	  // This feature title will be displayed within My Maps
      featureEntry.setTitle(new PlainTextConstruct("Feature Title"));
    } catch(NullPointerException e) {
      System.out.println("Error: " + e.getClass().getName());
    }
    
    // Insert the feature entry using the #post URL
    return myService.insert(featureEditUrl, featureEntry);
  }
}
