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

import com.google.gdata.util.common.xml.*;
import com.google.gdata.client.*;
import com.google.gdata.client.maps.*;
import com.google.gdata.data.*;
import com.google.gdata.data.maps.*;
import com.google.gdata.util.*;
import java.io.*;
import java.net.URL;

public class CreateFeatureFromText {

  public static void main(String[] args) {
    MapsService myService = new MapsService("createFeature");
    try {
      // Replace username and password with your authentication credentials
      myService.setUserCredentials("<i>username</i>","<i>password</i>");
      String kml = "<Placemark xmlns=\"http://www.opengis.net/kml/2.2\">" +
       "<name>Aunt Joan&apos;s Ice Cream Shop</name>" +
       "<Point>" + 
       "<coordinates>-87.74613826475604,41.90504663195118,0</coordinates>" +
       "</Point></Placemark>";
      createFeature(myService,kml);
    } catch(AuthenticationException e) {
      System.out.println("Authentication Exception");
    } catch(ServiceException e) {
      System.out.println("Service Exception: " + e.getResponseBody());
    } catch(IOException e) {
      System.out.println("I/O Exception");
    }
  }

  public static FeatureEntry createFeature(MapsService myService, String KML)
      throws ServiceException, IOException {

    // Use the feature feed's #post URL as the Edit URL for this map
	// Replace userID and mapID with appropriate values for your map
 
    final URL featureEditUrl = new URL("http://maps.google.com/maps/feeds/features/<i>userID</i>/<i>mapID</i>/full");

    // Create a blank FeatureEntry object
    FeatureEntry featureEntry = new FeatureEntry();
    
    try {
      // KML is simply XML so we'll use the XmlBlob object to store it
      XmlBlob kml = new XmlBlob();
      kml.setFullText(KML);

      // Set the KML for this feature
      // Note that the KML should only include one <Placemark> entry
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
