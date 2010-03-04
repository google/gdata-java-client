// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.v2;

import java.io.IOException;
import java.io.OutputStream;

public interface HttpSerializer {
  void writeTo(OutputStream out) throws IOException;
}
