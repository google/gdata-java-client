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

public class GetFeature {
  
  public static void main(String[] args) {
    MapsService myService = new MapsService("getFeature");
    try {
     // Replace username and password with your authentication credentials
      myService.setUserCredentials("username","password");
      getFeature(myService);
    } catch(AuthenticationException e) {
      System.out.println("Authentication Exception");
    } catch(ServiceException e) {
      System.out.println("Service Exception: " + e.getMessage());
    } catch(IOException e) {
      System.out.println("I/O Exception");
    } catch (Throwable t) {
      // Print the stack trace for any unknown errors
      t.printStackTrace();
    }
  }

  public static void getFeature(MapsService myService)
    throws ServiceException, IOException {

    // Get a feature entry's #self URL (returned in the feature feed)
	// Replace userID, mapID and featureID with appropriate values for your map's feature
    final URL featureEntryUrl = new URL("http://maps.google.com/maps/feeds/features/userID/mapID/full/featureID");
    
	// Get the feature entry using the #self URL
    FeatureEntry entry = myService.getEntry(featureEntryUrl, FeatureEntry.class);

    // Print out the Feature title
    System.out.println(entry.getTitle().getPlainText());

    // Print out the KML
    // Note that we use GData's getXml() method to print this out, as KML is XML
    try {
      XmlBlob kml = ((OtherContent) entry.getContent()).getXml();
      System.out.println(kml.getBlob());  
    } catch(NullPointerException e) {
      System.out.println("Null Pointer Exception");
    }        
  }
}
