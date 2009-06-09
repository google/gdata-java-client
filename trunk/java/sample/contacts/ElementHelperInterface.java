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


package sample.contacts;

import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactGroupEntry;

import java.io.PrintStream;

/**
 * Interface to define the common methods of the {@link ElementHelper}.
 * 
 * 
 */
public interface ElementHelperInterface {

  /**
   * Parses an element from the textual description, and sets or adds it to
   * the contact entry.
   * 
   * @param contact  the contact the parsed element should be added or set. 
   * @param parser   the parser used for the parsing of the description.
   * 
   * @see ElementParser
   */
  public void parse(ContactEntry contact, ElementParser parser); 
  
  /**
   * Parses an element from the textual description, and sets or adds it to
   * the group entry.
   * 
   * @param group  the group the parsed element should be added or set. 
   * @param parser the parser used for the parsing of the description.
   * 
   * @throws UnsupportedOperationException in case the specific element
   *         can not be set on a ContactGroupEntry.
   * 
   * @see ElementParser
   */
  public void parseGroup(ContactGroupEntry group, ElementParser parser); 

  /**
   * Prints the content of the element to a stream.
   * 
   * @param out      output stream.
   * @param contact  the contact containing the element to print.
   */
  public void print(PrintStream out, ContactEntry contact); 
  
  /**
   * Updates element of destination contact with data from source contact.
   * If the source contact entry does not has the specific element, it should
   * leave the destination contact entry as is, otherwise is should copy the
   * element from the source to the destination contact entry.
   *  
   * @param dest  the destination contact entry.
   * @param src   the source contact entry.
   */
  public void update(ContactEntry dest, ContactEntry src);

  /**
   * Returns the usage help text regarding the formating of an element
   * description.
   * 
   * @return the usage help text for the element description.
   */
  public String getUsage();

}
