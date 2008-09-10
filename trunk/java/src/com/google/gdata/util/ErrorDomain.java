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


package com.google.gdata.util;

/**
 * This is the parent class of all error domain classes.
 * Error domain classes package up a group of related error codes
 * (and associated information)
 * into an error domain with a unique name,
 * by default the fully qualified name of the class.
 *
 * Create an error domain as follows:
 * <ul>
 * <li>Subclass ErrorDomain.
 * <li>In the subclass, provide an empty private constructor and
 * a {@code public static final} variable named {@code ERR}
 * with the same type as the subclass; initialize it to an
 * instance of the subclass.  (This is eager singleton
 * initialization.)
 * <li>Alternatively, if you want to specify a non-default
 * domain name, use {@code super("foo")} in the constructor.
 * <li>Declare {@code public final ErrorCode} fields
 * thus: {@code public final ErrorCode
 * foo = new ErrorCode("foo")}
 * (Note: not {@static}).
 * <li>Append to each field declaration
 * optional calls to {@code setInternalReason}, {@code
 * setExtendedHelp}, or {@code setSendReport}.
 * </ul>
 * <p>Here's a complete example:
 * <pre>
 * public class FooErrorDomain extends ErrorDomain {
 *   private FooErrorDomain() {
 *     super("Foo");
 *   }
 * 
 *   public static final FooErrorDomain ERR = new FooErrorDomain();
 * 
 *   public final ErrorCode bar = new ErrorCode("brokenBar")
 *       .setInternalReason("Bar is not working yet");
 * 
 *   public final ErrorCode unsupported = new ErrorCode("unsupported")
 *       .setInternalReason("Requested key is not supported");
 * 
 *   public final ErrorCode invalid = new ErrorCode("invalidValue")
 *       .setInternalReason("Invalid value for attribute")
 *       .setExtendedHelp("http://www.google.com/foo/validAttributes.html");
 * 
 * }
 * </pre>
 * 
 * <p>Note that all setters return {@code this}.
 *
 * 
 */
public abstract class ErrorDomain {

  private final String domainName;

  /**
   * Returns the name associated with this ErrorDomain..
   */
  public String getDomainName() {
    return domainName;
  }

  /**
   * Constructs an ErrorDomain with a specified name.  This can be called
   * only from the constructor of a subclass. The value will appear as
   * the content of the {@code domain} element in the XML error format.
   */
  protected ErrorDomain(String domainName) {
    this.domainName = domainName;
  }

  /**
   * Constructs an ErrorDomain with a default name.
   */
  protected ErrorDomain() {
    domainName = getClass().getName();
  }

  /**
   * ErrorCode objects represent an error code within an error domain.
   * An inner class is used to make it difficult to construct an ErrorCode
   * that accidentally refers to the wrong subclass of ErrorDomain,
   * or to have more than one ErrorDomain object of the same subclass.
   */
  public class ErrorCode {

    private final String codeName;
    /**
     * Gets the name of this ErrorCode, which must be unique within
     * its domain.  The value will appear as the content of the {@code code}
     * element in the XML error format.
     */
    public String getCodeName() {
      return codeName;
    }
    // Set only through the constructor

    private String internalReason = null;
    /**
     * Gets the internal reason (un-internationalized explanation)
     * associated with this ErrorCode.  The value will appear as the
     * content of the {@code internalReason} element in the XML error
     * format.
     */
    public String getInternalReason() {
      return internalReason;
    }

    public ErrorCode setInternalReason(String newInternalReason) {
      this.internalReason = newInternalReason;
      return this;
    }

    private String extendedHelp = null;
    /**
     * Gets the extended help URI.  This can be used to retrieve a
     * detailed explanation of the error code.  The value will appear
     * as the content of the {@code extendedHelp} element in the XML
     * error format.
     */
    public String getExtendedHelp() {
      return extendedHelp;
    }

    public ErrorCode setExtendedHelp(String newExtendedHelp) {
      this.extendedHelp = newExtendedHelp;
      return this;
    }

    private String sendReport = null;

    /**
     * Gets the URI to which a report should be sent when this error
     * is received.  The value will appear as the content of the 
     * {@code sendReport} element in the XML error format.
     */
    public String getSendReport() {
      return sendReport;
    }

    public ErrorCode setSendReport(String newSendReport) {
      this.sendReport = newSendReport;
      return this;
    }

    public ErrorCode(String codeName) {
      this.codeName = codeName;
    }

    /**
     * Convenience method to retrieve the name of the domain
     * given an error code.
     */
    public String getDomainName() {
      return ErrorDomain.this.getDomainName();
    }
  }
}
