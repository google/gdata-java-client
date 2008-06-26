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

package com.google.gdata.data.appsforyourdomain.migration;

import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ValueConstruct;
import com.google.gdata.data.appsforyourdomain.Namespaces;
import com.google.gdata.util.ParseException;


/**
 * Google Apps name space element: {@code <apps:rfc822Msg>}.  Used to model 
 * the contents of a mail message from a legacy email platform.
 * 
 * 
 */
public class Rfc822Msg extends ValueConstruct {

  public static final String EXTENSION_LOCAL_NAME = "rfc822Msg";
  
  public static final String ATTRIBUTE_ENCODING = "encoding";
  
  /**
   * A method by which mail may be encoded. Currently the only supported values
   * are:
   * 
   * <ul>
   * <li>{@link Encoding#NONE}, which means the content of this
   * {@code Rfc822Msg} is plain text; and
   * <li>{@link Encoding#BASE64}, which means that the content has been
   * Base64-encoded (possibly from a character set other than UTF-8).
   * </ul>
   * 
   * <p>
   * If no {@code Encoding} is specified, the message will be interpreted as
   * plain text.
   */
  public enum Encoding {
    NONE,
    BASE64
  }
  
  private Encoding encoding;

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
   * Creates a new Rfc822Msg with the rfc822Msg text set to null and no
   * encoding.
   */
  public Rfc822Msg() {
    this(null, Encoding.NONE);
  }
  
  /**
   * Creates a new Rfc822Msg object with the specified text and no encoding.
   * 
   * @param msg the RFC822 text
   */
  public Rfc822Msg(String msg) {    
    this(msg, Encoding.NONE);
  }
  
  /**
   * Creates a new Rfc822Msg object with the specified text and encoding.
   * 
   * @param msg the RFC822 text
   * @param encoding the {@link Encoding} of this message
   */
  public Rfc822Msg(String msg, Encoding encoding) {
    super(Namespaces.APPS_NAMESPACE, EXTENSION_LOCAL_NAME, null, msg);

    if (encoding == null) {
      throw new IllegalArgumentException("Encoding may not be null.  Use "
          + "Encoding.NONE to specify no encoding.");
    }
    this.encoding = encoding;
  }
    
  /**
   * @return the rfc822Msg text
   */
  public String getMsg() {
    return getValue();
  }
  
  /**
   * @return the {@link Encoding} of this message
   */
  public Encoding getEncoding() {
    return encoding;
  }
  
  @Override
  protected void consumeAttributes(AttributeHelper helper) throws ParseException {
    super.consumeAttributes(helper);
    
    String enc = helper.consume(ATTRIBUTE_ENCODING, false);
    if (enc != null) {
      this.encoding = Encoding.valueOf(enc.toUpperCase());
    } else {
      this.encoding = Encoding.NONE;
    }
  }

  @Override
  public void putAttributes(AttributeGenerator generator) {
    super.putAttributes(generator);
    
    if (encoding != Encoding.NONE) {
      generator.put(ATTRIBUTE_ENCODING, encoding.name().toLowerCase());
    }
  }
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + encoding.hashCode()
                            + ((getMsg() == null ? 0 : getMsg().hashCode()));
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (!(obj instanceof Rfc822Msg)) return false;
    final Rfc822Msg other = (Rfc822Msg) obj;
    
    if (getMsg() == null) {
      if (other.getMsg() != null) return false;
    } else if (!getMsg().equals(other.getMsg())) return false;
    
    if (!encoding.equals(other.encoding)) return false;
    return true;
  }

  public static ExtensionDescription getDefaultDescription() {
    return EXTENSION_DESC;
  }  
}
