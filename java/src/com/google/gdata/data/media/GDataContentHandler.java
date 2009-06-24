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


package com.google.gdata.data.media;

import com.google.gdata.util.common.base.Preconditions;
import com.google.gdata.client.media.MediaService;
import com.google.gdata.data.Entry;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.IAtom;
import com.google.gdata.data.ParseSource;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ParseException;
import com.google.gdata.util.ServiceException;
import com.google.gdata.wireformats.AltFormat;
import com.google.gdata.wireformats.AltRegistry;
import com.google.gdata.wireformats.input.ForwardingInputProperties;
import com.google.gdata.wireformats.input.InputProperties;
import com.google.gdata.wireformats.input.InputParser;
import com.google.gdata.wireformats.input.InputPropertiesBuilder;
import com.google.gdata.wireformats.output.ForwardingOutputProperties;
import com.google.gdata.wireformats.output.OutputGenerator;
import com.google.gdata.wireformats.output.OutputProperties;
import com.google.gdata.wireformats.output.OutputPropertiesBuilder;

import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataContentHandler;
import javax.activation.DataSource;

/**
 * The GDataContentHandler class implements the {@link DataContentHandler}
 * interface of the <a href="http://java.sun.com/products/javabeans/jaf/ ">
 * JavaBeans Activation Framework</a> to enable the parsing and generation
 * of Atom feed and entry XML from MIME media data.   This data content
 * handler is capable of generating MIME media output in Atom, RSS, and
 * JSON formats, as well as parsing content in Atom format.
 * <p>
 * The implementation includes support for customized types driven by
 * <a href="http://code.google.com/apis/gdata/common-elements.html">
 * GData Kinds</a>, where the type of object returned might be triggered
 * by the GData kind category tags included within the content.
 * <p>
 * The current implementation does not include DataFlavor transfer support,
 * only mapping from raw MIME data content to object model (and vice versa).
 *
 * 
 */
public class GDataContentHandler implements DataContentHandler {

  /**
   * Defines the default {@link InputProperties} that are used for content
   * parsing.  These properties will use the default media registry, expect
   * Atom content, produces generic {@link Entry} instances, and use an
   * empty extension profile.
   */
  private static final InputProperties DEFAULT_INPUT_PROPERTIES =
    new InputPropertiesBuilder()
        .setAltRegistry(MediaService.getDefaultAltRegistry())
        .setContentType(ContentType.ATOM)
        .setExpectType(Entry.class)
        .setExtensionProfile(new ExtensionProfile())
        .build();
  
  /**
   * Thread local storage that defines the input properties that should be
   * used when parsing GData content
   */
  private static final ThreadLocal<InputProperties> threadInputProperties =
      new ThreadLocal<InputProperties>() {
        @Override
        protected InputProperties initialValue() {
          return DEFAULT_INPUT_PROPERTIES;
        }
  };
  
  /**
   * Defines the default {@link OutputProperties} that are used for content
   * parsing.   These properties will use a default media registry and an
   * empty extension profile.
   */
  private static final OutputProperties DEFAULT_OUTPUT_PROPERTIES =
    new OutputPropertiesBuilder()
        .setAltRegistry(MediaService.getDefaultAltRegistry())
        .setExtensionProfile(new ExtensionProfile())
        .build();
  
  /**
   * Thread local storage that defines the output properties that should be
   * used when generating GData content
   */
  private static final ThreadLocal<OutputProperties> threadOutputProperties =
      new ThreadLocal<OutputProperties>() {
        @Override
        protected OutputProperties initialValue() {
          return DEFAULT_OUTPUT_PROPERTIES;
        }
  };

  /**
   * Sets the input properties for the current {@link java.lang.Thread} and
   * returns any existing input properties that have been set (so they can be
   * restored later).
   */
  public static InputProperties setThreadInputProperties(
      InputProperties inputProperties) {
    Preconditions.checkNotNull(inputProperties, "inputProperties");
    
    InputProperties currentProperties = getThreadInputProperties();
    threadInputProperties.set(inputProperties);
    return currentProperties;
  }

  /**
   * Returns the input properties for the current {@link java.lang.Thread}.
   */
  public static InputProperties getThreadInputProperties() {
    return threadInputProperties.get();
  }
  
  /**
   * Sets the output properties for the current {@link java.lang.Thread} and
   * returns any existing input properties that have been set (so they can be
   * restored later).
   */
  public static OutputProperties setThreadOutputProperties(
      OutputProperties outputProperties) {
    Preconditions.checkNotNull(outputProperties, "outputProperties");
    
    OutputProperties currentProperties = getThreadOutputProperties();
    threadOutputProperties.set(outputProperties);
    return currentProperties;
  }

  /**
   * Returns the output properties for the current {@link java.lang.Thread}.
   */
  public static OutputProperties getThreadOutputProperties() {
    return threadOutputProperties.get();
  }

  public DataFlavor[] getTransferDataFlavors() {
    throw new UnsupportedOperationException("No DataFlavor support");
  }

  public Object getTransferData(DataFlavor df,
                                DataSource ds) {
    throw new UnsupportedOperationException("No DataFlavor support");
  }
  
  @SuppressWarnings("unchecked")
  private <T> Object parseAtom(InputParser<?> parser, InputStream inputStream,
      final ContentType contentType, InputProperties inputProperties, 
      Class<T> resultClass) throws IOException, ServiceException {
    Preconditions.checkArgument(
        parser.getResultType().isAssignableFrom(IAtom.class),
        "Parser does not handle atom content");
    return ((InputParser<T>) parser).parse(
        new ParseSource(inputStream), 
        new ForwardingInputProperties(inputProperties) {
          @Override
          public ContentType getContentType() {
            return contentType;
          }
        }, resultClass);
  }

  @SuppressWarnings("unchecked")
  public Object getContent(DataSource ds) throws IOException {
    
    // Get the input properties to use when parsing content
    InputProperties inputProperties = getThreadInputProperties();
    
    // Find the parser to handle the input content type
    ContentType contentType = new ContentType(ds.getContentType());
    AltRegistry altRegistry = inputProperties.getAltRegistry();
    AltFormat altFormat = altRegistry.lookupType(contentType);
    InputParser<?> parser = altRegistry.getParser(altFormat);
    if (parser == null) {
      throw new IOException("Invalid multipart content: " + contentType);
    }
    
    try {
      return parseAtom(parser, ds.getInputStream(), contentType, 
          inputProperties, inputProperties.getRootType());
    } catch (ServiceException se) {
      IOException ioe = new IOException("Error parsing content");
      ioe.initCause(se);
      throw ioe;
    }
  }

  private void generateAtom(OutputGenerator<?> generator, 
      OutputStream outputStream, OutputProperties outputProperties, 
      Object source) throws IOException {
    
    // Make sure the parser will accept any IAtom type
    Preconditions.checkArgument(
        generator.getSourceType().isAssignableFrom(IAtom.class),
        "Generator does not handle atom content");
    Preconditions.checkArgument(source instanceof IAtom,
        "Source object must be Atom content");
    IAtom atomSource = (IAtom) source;
    @SuppressWarnings("unchecked")  // safe given above check
    OutputGenerator<IAtom> atomGenerator = (OutputGenerator<IAtom>) generator;
    atomGenerator.generate(outputStream, outputProperties, atomSource);
  }

  public void writeTo(Object obj, String mimeType, OutputStream os)
      throws IOException {

    Preconditions.checkNotNull(obj, "obj");

    // Get the output properties to use when generating content
    OutputProperties outputProperties = getThreadOutputProperties();
  
    AltRegistry altRegistry = outputProperties.getAltRegistry();
    ContentType contentType = new ContentType(mimeType);
    final AltFormat altFormat = altRegistry.lookupType(contentType);
    OutputGenerator<?> generator = altRegistry.getGenerator(altFormat);
    if (generator == null) {
      throw new IllegalStateException("Unable to generate media: " +
          contentType);
    }
    generateAtom(generator, os,
        new ForwardingOutputProperties(outputProperties) {
          @Override
          public ContentType getContentType() {
            return altFormat.getContentType();
          }
        }, obj);
  }
}
