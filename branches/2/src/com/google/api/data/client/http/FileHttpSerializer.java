// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public final class FileHttpSerializer extends InputStreamHttpSerializer {

  public FileHttpSerializer(File file, String contentType,
      String contentEncoding) throws FileNotFoundException {
    super(new FileInputStream(file), file.length(), contentType,
        contentEncoding);
  }
}
