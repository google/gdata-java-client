package com.google.api.data.client.generator;

import com.google.api.data.client.generator.linewrap.JavaLineWrapper;
import com.google.api.data.client.generator.linewrap.LineWrapper;

abstract class AbstractJavaFileGenerator implements FileGenerator {

  public final LineWrapper getLineWrapper() {
    return JavaLineWrapper.get();
  }

  public boolean isGenerated() {
    return true;
  }
}
