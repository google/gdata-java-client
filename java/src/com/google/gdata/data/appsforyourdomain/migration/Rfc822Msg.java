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

package com.google.gdata.data.appsforyourdomain.migration;

import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.appsforyourdomain.Namespaces;


/**
 * Google Apps name space element: <apps:Rfc822Msg>.  Used to model 
 * the contents of a mail message from a legacy email platform
 * 
 * 
 *
 */
public class Rfc822Msg extends ValueConstruct {

  public static final String EXTENSION_LOCAL_NAME = "rfc822Msg";

  /**
   * the extension description is shared by all instances of this class
   */  
  private static ExtensionDescription EXTENSION_DESC = 
      new ExtensionDescription();
  static {
    EXTENSION_DESC.setExtensionClass(Rfc822Msg.class);
    EXTENSION_DESC.setNamespace(Namespaces.APPS_NAMESPACE);
    EXTENSION_DESC.setLocalName(EXTENSION_LOCAL_NAME);
    EXTENSION_DESC.setRepeatable(false);
  }
  
  /**
   * Creates a new Rfc822Msg with the rfc822Msg text set to null
   */
  public Rfc822Msg() {
    super(Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME, null, null);
  }
  
  /**
   * Creates a new Rfc822Msg object with the specified text
   * 
   * @param msg the rfc822 text
   */
  public Rfc822Msg(String msg) {
    
    super(Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME, null, msg);
  }
    
  /**
   * @return the rfc822Msg text
   */
  public String getMsg() {
    return getValue();
  }
  
  public static ExtensionDescription getDefaultDescription() {
    return EXTENSION_DESC;
  }  
}
