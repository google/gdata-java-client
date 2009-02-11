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

import com.google.gdata.util.common.base.Preconditions;

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
 * optional calls to {@link ErrorCode#withInternalReason}, {@link
 * ErrorCode#withExtendedHelp}, or {@link ErrorCode#withSendReport}.
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
 *       .withInternalReason("Bar is not working yet");
 * 
 *   public final ErrorCode unsupported = new ErrorCode("unsupported")
 *       .withInternalReason("Requested key is not supported");
 * 
 *   public final ErrorCode invalid = new ErrorCode("invalidValue")
 *       .withInternalReason("Invalid value for attribute")
 *       .withExtendedHelp("http://www.google.com/foo/validAttributes.html");
 * 
 * }
 * </pre>
 * 
 * <p>Note that all {@code with} methods return a mutated copy.
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
   * Constructs an ErrorDomain with a default eror domain name.  Use
   * {@link #ErrorDomain(String)} to set a different domain name.
   */
  protected ErrorDomain() {
    domainName = getClass().getName();
  }

  /**
   * ErrorCode objects represent an error code within an error domain.
   * An inner class is used to make it difficult to construct an ErrorCode
   * that accidentally refers to the wrong subclass of ErrorDomain,
   * or to have more than one ErrorDomain object of the same subclass.
   * 
   * <p>An ErrorCode is immutable.  The {@link #withInternalReason(String)},
   * {@link #withExtendedHelp(String)}, and {@link #withSendReport(String)}
   * methods can be used to create new ErrorCodes as modified versions of
   * existing ones.
   */
  public class ErrorCode implements ErrorContent {

    private final String codeName;
    private final String extendedHelp;
    private final String internalReason;
    private final String sendReport;

    /**
     * Construct a new error code with the given code name.
     * 
     * @param codeName the codename of this error code, must not be null.
     */
    public ErrorCode(String codeName) {
      this(codeName, null, null, null);
    }
    
    /**
     * Private constructor that sets all fields.  Clients must use the
     * {@code withFoo} methods to set fields.
     */
    private ErrorCode(String codeName, String extendedHelp,
        String internalReason, String sendReport) {
      Preconditions.checkNotNull(codeName, "codeName");
      this.codeName = codeName;
      this.extendedHelp = extendedHelp;
      this.internalReason = internalReason;
      this.sendReport = sendReport;
    }

    /**
     * Retrieve the name of the domain of this ErrorCode.
     */
    public String getDomainName() {
      return ErrorDomain.this.getDomainName();
    }
    
    /**
     * Gets the name of this ErrorCode, which must be unique within its domain.
     * The value will appear as the content of the {@code code} element in the
     * XML error format.
     */
    public String getCodeName() {
      return codeName;
    }
    
    /**
     * Gets the internal reason (unlocalized explanation) associated with this
     * ErrorCode.  The value will appear as the content of the
     * {@code internalReason} element in the XML error format.
     */
    public String getInternalReason() {
      return internalReason;
    }

    /**
     * @deprecated Use {@link #withInternalReason(String)} instead.
     */
    @Deprecated
    public ErrorCode setInternalReason(String newInternalReason) {
      return withInternalReason(newInternalReason);
    }
    
    /**
     * Returns a copy of this ErrorCode with the given internal reason
     * (un-internationalized explanation) set.  The value will appear as the
     * content of the {@code internalReason} element in the XML error format.
     */
    public ErrorCode withInternalReason(String newInternalReason) {
      return new ErrorCode(
          codeName, extendedHelp, newInternalReason, sendReport);
    }

    /**
     * Gets the extended help URI.  This can be used to retrieve a
     * detailed explanation of the error code.  The value will appear
     * as the content of the {@code extendedHelp} element in the XML
     * error format.
     */
    public String getExtendedHelp() {
      return extendedHelp;
    }

    /**
     * Returns a copy of this ErrorCode with the given extended help URI set.
     * This can be used to provide a detailed explanation of the error code.
     * The value will appear as the content of the {@code extendedHelp} element
     * in the XML error format.
     */
    public ErrorCode withExtendedHelp(String newExtendedHelp) {
      return new ErrorCode(
          codeName, newExtendedHelp, internalReason, sendReport);
    }

    /**
     * Gets the URI to which a report should be sent when this error
     * is received.  The value will appear as the content of the 
     * {@code sendReport} element in the XML error format.
     */
    public String getSendReport() {
      return sendReport;
    }

    /**
     * Returns a copy of this ErrorCode with the given send report URI set.
     * This can be used to provide a URI to which a report should be sent when
     * the error is received.  The value will appear as the content of the
     * {@code sendReport} element in the XML error format.
     */
    public ErrorCode withSendReport(String newSendReport) {
      return new ErrorCode(
          codeName, extendedHelp, internalReason, newSendReport);
    }

    // Local properties of error content that error codes do not specify.
    public String getLocation() {
      return null;
    }
    
    public LocationType getLocationType() {
      return null;
    }
    
    public String getDebugInfo() {
      return null;
    }
  }
}
