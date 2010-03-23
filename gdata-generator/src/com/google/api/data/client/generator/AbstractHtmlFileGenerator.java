package com.google.api.data.client.generator;

import com.google.api.data.client.generator.linewrap.HtmlLineWrapper;
import com.google.api.data.client.generator.linewrap.LineWrapper;

abstract class AbstractHtmlFileGenerator implements FileGenerator {

  public final LineWrapper getLineWrapper() {
    return HtmlLineWrapper.get();
  }

  public boolean isGenerated() {
    return true;
  }
}
