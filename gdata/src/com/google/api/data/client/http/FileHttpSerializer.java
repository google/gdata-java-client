// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.http;

import com.google.api.data.client.v2.GData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public final class FileHttpSerializer extends InputStreamHttpSerializer {

  FileHttpSerializer(File file, String contentType, String contentEncoding)
      throws FileNotFoundException {
    super(new FileInputStream(file), file.length(), contentType,
        contentEncoding);
  }

  public static void setContent(HttpRequest request, File file, String contentType,
      String contentEncoding) throws FileNotFoundException {
    request.setContent(new FileHttpSerializer(file, contentType,
        contentEncoding));
    GData.setSlugHeader(request, file.getName());
  }
}
