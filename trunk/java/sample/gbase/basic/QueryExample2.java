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

package sample.gbase.basic;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *  Display the titles of the Google Base items that match a specific query. 
 */
public class QueryExample2 {
  /**
   * Url of the Google Base data API snippet feed.
   */
  private static final String SNIPPETS_FEED = "http://base.google.com/base/feeds/snippets";

  /**
   * The query that is sent over to the Google Base data API server.
   */
  private static final String QUERY = "cars [item type : products]";

  /**
   * Create a <code>QueryExample2</code> instance and
   * call <code>displayItems</code>. 
   */
  public static void main(String[] args) throws IOException, SAXException,
          ParserConfigurationException {
    new QueryExample2().displayItems();
  }
  
  /**
   * Connect to the Google Base data API server, retrieve the items that match
   * <code>QUERY</code> and call <code>DisplayTitlesHandler</code> to extract
   * and display the titles from the XML response.
   */
  public void displayItems() throws IOException, SAXException,
          ParserConfigurationException {
    /*
     * Create a URL object, open an Http connection on it and get the input
     * stream that reads the Http response.
     */
    URL url = new URL(SNIPPETS_FEED + "?bq=" + 
        URLEncoder.encode(QUERY, "UTF-8"));
    HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
    InputStream inputStream = httpConnection.getInputStream();

    /*
     * Create a SAX XML parser and pass in the input stream to the parser.
     * The parser will use DisplayTitleHandler to extract the titles from the 
     * XML stream. 
     */
    SAXParserFactory factory = SAXParserFactory.newInstance();
    SAXParser parser = factory.newSAXParser();
    parser.parse(inputStream, new DisplayTitlesHandler());
  }

  /**
   * Simple SAX event handler, which prints out the titles of all entries in the 
   * Atom response feed. 
   */
  private static class DisplayTitlesHandler extends DefaultHandler {
    /**
     * Stack containing the opening XML tags of the response.
     */
    private Stack<String> xmlTags = new Stack<String>(); 
    
    /**
     * Counter that keeps track of the currently parsed item.
     */
    private int itemNo = 0;
    
    /**
     * True if we are inside of a data entry's title, false otherwise.
     */
    private boolean insideEntryTitle = false;
    
    /**
     * Receive notification of an opening XML tag: push the tag to 
     * <code>xmlTags</code>. If the tag is a title tag inside an entry tag, 
     * turn <code>insideEntryTitle</code> to <code>true</code>.
     */
    @Override
    public void startElement(String uri, String localName, String qName,
        Attributes attributes) throws SAXException {
      if (qName.equals("title") && xmlTags.peek().equals("entry")) {
        insideEntryTitle = true;
        System.out.print("Item " + ++itemNo + ": ");
      } 
      xmlTags.push(qName);
    }
    
    /**
     * Receive notification of a closing XML tag: remove the tag from teh stack.
     * If we were inside of an entry's title, turn <code>insideEntryTitle</code>
     * to <code>false</code>.
     */
    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
      // If a "title" element is closed, we start a new line, to prepare
      // printing the new title.
      xmlTags.pop();
      if (insideEntryTitle) {
        insideEntryTitle = false;
        System.out.println();
      }
    }
    
    /**
     * Callback method for receiving notification of character data inside an
     * XML element. 
     */
    @Override
    public void characters(char[] ch, int start, int length)
        throws SAXException {
      // display the character data if the opening tag is "title" and its parent is 
      // "entry"
      if (insideEntryTitle) {
        System.out.print(new String(ch, start, length));
      }
    }
  }
}
