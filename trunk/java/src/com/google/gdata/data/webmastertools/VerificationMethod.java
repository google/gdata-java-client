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


package com.google.gdata.data.webmastertools;

import com.google.gdata.data.AbstractExtension;
import com.google.gdata.data.AttributeGenerator;
import com.google.gdata.data.AttributeHelper;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.util.ParseException;

/**
 * GData schema extension describing a node specifying verification method. The
 * verification method specifies a type of verification (HTML meta tag or file
 * verification), verification value to be used by web site owner for the given
 * type, and whether the given verification method is currently in use.
 *
 * Example:
 * <pre class="code">
 *   <wt:verification-method type="METATAG" in-use="false">
 *      SOMEVALUE
 *   </wt:verification-method>
 * </pre>
 *
 * 
 */
@ExtensionDescription.Default(
    nsAlias = Namespaces.WT_ALIAS,
    nsUri = Namespaces.WT,
    localName = VerificationMethod.METHOD_NODE)
public class VerificationMethod extends AbstractExtension {

  /** Enumerates supported verification method types. */
  public enum MethodType { METATAG, HTMLPAGE }

  /** Verification method type. */
  private MethodType methodType;

  /** Specifies whether the method is currently in use. */
  private boolean inUse;

  /** Specifies the file content for HTML file verification. */
  private String fileContent;

  /**
   * Specifies verification value which is interpreted accordingly to
   * verification type. For instance for HTML file verification, the value
   * specifies the file name that must be used by web site owner.
   */
  private String value;

  /** XML element names used by this class. */
  public static final String METHOD_NODE = "verification-method";
  private static final String METHODTYPE = "type";
  private static final String INUSE = "in-use";
  private static final String FILECONTENT = "file-content";

  /**
   * Checks whether specified node name matches node name for the verification
   * method.
   *
   * @param nodeName is node name to check against.
   * @return {@code true} if the name matches or {@code false} otherwise.
   */
  public static boolean matchesNode(String nodeName) {
    return nodeName.equals(METHOD_NODE);
  }

  /**
   * Constructs {@link AbstractExtension} to represent verification method node.
   */
  public VerificationMethod() {
    super(Namespaces.WT_NAMESPACE, METHOD_NODE);
    this.methodType = MethodType.METATAG;
    this.inUse = false;
    this.fileContent = "";
    this.value = "";
  }

  /**
   * Compares {@link VerificationMethod} objects based on the verification
   * method type, value, file-content and in-use flag.
   */
  @Override
  public boolean equals(Object rhs) {
    if (!sameClassAs(rhs)) {
      return false;
    }

    VerificationMethod r = (VerificationMethod) rhs;
    if (!methodType.equals(r.methodType)) {
      return false;
    }
    if (inUse != r.inUse) {
      return false;
    }
    if (!fileContent.equals(r.fileContent)) {
      return false;
    }
    if (!value.equals(r.value)) {
      return false;
    }

    return true;
  }

  /**
   * Returns hash code which is based on the method type.
   */
  @Override
  public int hashCode() {
    return methodType.hashCode();
  }

  /**
   * Set verification method type.
   */
  public void setMethodType(MethodType method) {
    methodType = method;
  }

  /**
   * Get verification method type.
   */
  public MethodType getMethodType() {
    return methodType;
  }

  /**
   * Mark this method as the verification method that is currently in use.
   */
  public void setInUse(boolean inUse) {
    this.inUse = inUse;
  }

  /**
   * Returns verification method that is currently in use.
   */
  public boolean getInUse() {
    return inUse;
  }

  /**
   * Sets file content for HTML file verification.
   */
  public void setFileContent(String fileContent) {
    this.fileContent = fileContent;
  }

  /**
   * Returns file content for HTML file verification.
   */
  public String getFileContent() {
    return fileContent;
  }

  /**
   * Sets verification method value. The value is a string that is interpreted
   * according to the method type.
   */
  public void setValue(String v) {
    value = v;
  }

  /**
   * Get verification method value. The value is a string that is interpreted
   * according to the method type.
   */
  public String getValue() {
    return value;
  }

  /**
   * Overrides base class method to output attributes defined by this class.
   */
  @Override
  protected void putAttributes(AttributeGenerator generator) {
    generator.put(METHODTYPE, methodType.toString().toLowerCase());
    generator.put(INUSE, inUse);
    generator.put(FILECONTENT, fileContent);
    generator.setContent(value);
  }

  /**
   * Overrides base class method to parse attributes specific to this class.
   */
  @Override
  protected void consumeAttributes(AttributeHelper helper)
      throws ParseException {
    methodType = helper.consumeEnum(
        METHODTYPE, true, MethodType.class, MethodType.METATAG);
    inUse = helper.consumeBoolean(INUSE, true);
    fileContent = helper.consume(FILECONTENT, false);
    String content = helper.consumeContent(false);
    if (content != null) {
      value = content;
    }
  }
}
