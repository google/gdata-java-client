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


package com.google.gdata.model.atom;

import com.google.gdata.util.common.base.StringUtil;
import com.google.common.collect.ImmutableMap;
import com.google.gdata.util.common.html.HtmlToText;
import com.google.gdata.client.CoreErrorDomain;
import com.google.gdata.client.Service;
import com.google.gdata.data.IContent;
import com.google.gdata.data.ITextConstruct;
import com.google.gdata.data.ITextContent;
import com.google.gdata.data.TextConstruct;
import com.google.gdata.model.Element;
import com.google.gdata.model.ElementCreator;
import com.google.gdata.model.ElementKey;
import com.google.gdata.model.ElementMetadata;
import com.google.gdata.model.ElementValidator;
import com.google.gdata.model.MetadataRegistry;
import com.google.gdata.model.QName;
import com.google.gdata.model.ValidationContext;
import com.google.gdata.model.XmlBlob;
import com.google.gdata.util.Namespaces;

/**
 * Variant of {@link Content} for entries containing text.
 */
public class TextContent extends Content
    implements ITextContent, ITextConstruct {

  /** The kind name for adaptation. */
  public static final String KIND = "text";

  /**
   * The key for TextContent used as a construct.  This will apply to all uses
   * of TextContent regardless of QName.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<String, TextContent> CONSTRUCT =
      ElementKey.of(null, String.class, TextContent.class);

  /**
   * The key for atom:content when it contains TextContent.
   */
  @SuppressWarnings("hiding")
  public static final ElementKey<String, TextContent> KEY = ElementKey.of(
      Content.KEY.getId(), String.class, TextContent.class);

  /**
   * The key for xhtml:div.
   */
  public static final ElementKey<String, XmlBlob> DIV = ElementKey.of(
      new QName(Namespaces.xhtmlNs, "div"), String.class, XmlBlob.class);

  /**
   * Registers the metadata for this element.
   */
  public static void registerMetadata(MetadataRegistry registry) {
    if (registry.isRegistered(CONSTRUCT)) {
      return;
    }

    Content.registerMetadata(registry);

    ElementCreator constructBuilder = registry.build(CONSTRUCT)
        .setValidator(new TextContentValidator());
    constructBuilder.addElement(DIV);

    ElementCreator builder = registry.build(KEY);
    registry.adapt(Content.KEY, KIND, KEY);
  }

  // Unknown type attributes will map to this value.
  private static final int UNKNOWN_TYPE = -1;

  // A map of type attribute values to content types.
  private static final ImmutableMap<String, Integer> TYPE_MAP =
      ImmutableMap.<String, Integer>builder()
      .put("plain", IContent.Type.TEXT)
      .put("text", IContent.Type.TEXT)
      .put("text/plain", IContent.Type.TEXT)
      .put("html", IContent.Type.HTML)
      .put("text/html", IContent.Type.HTML)
      .put("xhtml", IContent.Type.XHTML)
      .build();

  /**
   * Validator for {@link TextContent}.
   */
  private static class TextContentValidator implements ElementValidator {

    /**
     * Validates that plain text or html text content has a text value and no
     * nested elements, and validates that xhtml contains a single DIV element.
     * <p>
     * Also adds an error if the tyep attribute on the given element is unknown.
     */
    public void validate(ValidationContext vc, Element e,
        ElementMetadata<?, ?> metadata) {
      int type = getType(e);
      switch (type) {
        case UNKNOWN_TYPE:
          vc.addError(e,
              CoreErrorDomain.ERR.invalidTextContentType.withInternalReason(
                  "Invalid type: " + type));
          break;

        case IContent.Type.TEXT:
        case IContent.Type.HTML:
          if (!e.hasTextValue()) {
            vc.addError(e, CoreErrorDomain.ERR.missingTextContent);
          }
          if (e.getElementCount() != 0) {
            vc.addError(e,
                CoreErrorDomain.ERR.invalidChildElement.withInternalReason(
                    "Child elements not allowed on text content"));
          }
          break;

        case IContent.Type.XHTML:
          if (!e.hasElement(DIV)) {
            vc.addError(e,
                CoreErrorDomain.ERR.missingExtensionElement.withInternalReason(
                    "xhtml text content must have a div element"));
          } else if (e.getElementCount() != 1) {
            vc.addError(e,
                CoreErrorDomain.ERR.invalidChildElement.withInternalReason(
                    "xhtml must only have a single child element"));
          }
          break;

        default:
          throw new IllegalStateException("Shouldn't be possible, "
              + "TYPE_MAP can only map to text, html, or xhtml.");
      }
    }
  }

  /**
   * Creates a text content. This method is convenient for some service
   * implementations. Note that XHTML is treated somewhat differently from text
   * and HTML, as it is stored as a separate element instead of in the value
   * portion of this element.
   *
   * @param   type
   *            the type of the new text construct (TEXT, HTML, or XHTML)
   *
   * @param   textOrHtml
   *            the contents to put in this text construct, if the type is
   *            TEXT or HTML.
   *            If type is XHTML, set this parameter to {@code null}.
   *
   * @param   xhtml
   *            the contents to put in this text construct, if the type is
   *            XHTML.
   *            If type is TEXT or HTML, set this parameter to {@code null}.
   *
   * @return  a {@link TextConstruct} of the appropriate type.
   */
  public static TextContent create(int type, String textOrHtml, XmlBlob xhtml) {
    switch (type) {
      case IContent.Type.TEXT:
        return plainText(textOrHtml);

      case IContent.Type.HTML:
        return html(textOrHtml);

      case IContent.Type.XHTML:
        return xhtml(xhtml);

      default:
        throw new IllegalArgumentException("Invalid type: " + type);
    }
  }

  /**
   * Construct a new plain text content with the given text.
   */
  public static TextContent plainText(String text) {
    TextContent content = new TextContent();
    content.setText(text);
    return content;
  }

  /**
   * Construct a new html text content with the given html.
   */
  public static TextContent html(String html) {
    TextContent content = new TextContent();
    content.setHtml(html);
    return content;
  }

  /**
   * Construct a new Xhtml text content from the given div.
   */
  public static TextContent xhtml(XmlBlob div) {
    TextContent content = new TextContent();
    content.setXhtml(div);
    return content;
  }

  /**
   * Returns the int value (old school enum) for the type attribute on the
   * given element.  This method will return {@link #UNKNOWN_TYPE} if the type
   * attribute is unknown, otherwise it will return {@link Content.Type#TEXT}
   * for plain text, {@link Content.Type#HTML} for html, or
   * {@link Content.Type#XHTML} for XHTML content.
   */
  private static int getType(Element e) {
    String type = e.getAttributeValue(Content.TYPE);
    Integer typeVal = (type == null) ? IContent.Type.TEXT : TYPE_MAP.get(type);
    return (typeVal == null) ? UNKNOWN_TYPE : typeVal.intValue();
  }

  /**
   * Constructs a new plain text instance using the default key.
   */
  public TextContent() {
    super(CONSTRUCT);
  }

  /**
   * Constructs a new instance using the specified key.
   *
   * @param key the element key for this element
   */
  protected TextContent(ElementKey<?, ?> key) {
    super(key);
  }

  /**
   * Constructs a new instance from a more generic {@link Content} type.
   *
   * @param key the element key to use for this instance
   * @param content generic content
   */
  protected TextContent(ElementKey<?, ?> key, Content content) {
    super(key, content);
  }

  /**
   * Returns the type of this content, either {@link Content.Type#TEXT},
   * {@link Content.Type#HTML}, or {@link Content.Type#XHTML}.  If the value
   * of the {@link #TYPE} attribute is unknown, plain text
   * {@link Content.Type#TEXT} will be returned.
   */
  @Override
  public int getType() {
    int type = getType(this);
    return (type == UNKNOWN_TYPE) ? IContent.Type.TEXT : type;
  }

  /**
   * Returns {@code true} if there is no content element for this text content.
   */
  public boolean isEmpty() {
    return StringUtil.isEmpty(getText()) && getElementCount() == 0;
  }

  /**
   * Returns a plain-text representation of this text content.  For content that
   * is already plain text, this just returns the text value, but for html and
   * xhtml it first converts the data into a plain text representation before
   * returning it.
   */
  public String getPlainText() {
    switch (getType()) {
      case IContent.Type.XHTML:
        return getXhtml().getBlob();

      case IContent.Type.TEXT:
        return getText();

      case IContent.Type.HTML:
        return HtmlToText.htmlToPlainText(getText());

      default:
        throw new IllegalStateException("Shouldn't be possible, "
            + "getType can only return TEXT, HTML, or XHTML.");
    }
  }

  /**
   * Returns the text content of this element, if this is a plain text or html
   * text content.  If this is an XHTML text content this method returns null.
   * If you want a plain-text representation of the XHTML content call
   * {@link #getPlainText()} instead.
   */
  public String getText() {
    return getTextValue(KEY);
  }

  /**
   * Backwards-compatibility method, exactly the same as {@link #getText()}.
   */
  public String getHtml() {
    return getText();
  }

  /**
   * Returns the XHTML content of this text content, or {@code null} if no such
   * element exists.
   */
  public XmlBlob getXhtml() {

    // Non-xhtml types don't have a div element.
    if (getType() != Content.Type.XHTML) {
      return null;
    }

    XmlBlob div = getElement(DIV);
    if (div == null) {
      div = new XmlBlob(DIV);
      setXhtml(div);
    }
    return div;
  }
  /**
   * Specifies the text of this element, turning this into a plain-text
   * content element if it wasn't already.
   */
  public void setText(String text) {
    if (Service.getVersion().isBefore(Service.Versions.V2)) {
      setAttributeValue(Content.TYPE, "text");
    } else {
      setAttributeValue(Content.TYPE, null);
    }
    setTextValue(text);
  }

  /**
   * Specifies the text of this element, turning this into an html text content
   * element if it wasn't already.
   */
  public void setHtml(String html) {
    setAttributeValue(Content.TYPE, "html");
    setTextValue(html);
  }

  /**
   * Specifies the XHTML content of this element, turning this into an xhtml
   * text content element if it wasn't already.
   */
  public void setXhtml(XmlBlob div) {
    setAttributeValue(Content.TYPE, "xhtml");
    setElement(DIV, div);
  }

  /**
   * {@inheritDoc}
   *
   * In the case of plain text or html content, we resolve a null content body
   * to an empty string.
   */
  @Override
  public Element resolve(ElementMetadata<?,?> metadata, ValidationContext vc) {
    int type = getType();
    if (type == IContent.Type.TEXT
        && Service.getVersion().isBefore(Service.Versions.V2)
        && getAttributeValue(Content.TYPE) == null) {
      setAttributeValue(Content.TYPE, "text");
    }
    if (type == IContent.Type.TEXT || type == IContent.Type.HTML) {
      if (getTextValue() == null) {
        setTextValue("");
      }
    }

    return super.resolve(metadata, vc);
  }

  public ITextConstruct getContent() {
    return this;
  }
}
