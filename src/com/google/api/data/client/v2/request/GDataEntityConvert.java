package com.google.api.data.client.v2.request;

import com.google.api.data.client.http.Response;
import com.google.api.data.client.v2.ClassInfo;
import com.google.api.data.client.v2.jsonc.jackson.Jackson;

import org.codehaus.jackson.JsonParser;

import java.io.IOException;
import java.io.InputStream;

public class GDataEntityConvert {
  
  static <T> T toObject(Response response, Class<T> cls)
      throws IOException {
    if (!response.getContentType().startsWith("application/json")) {
      throw new RuntimeException(
          "Not supported response type: " + response.getContentType());
    }
    InputStream inputStream = response.getContent();
    JsonParser parser = Jackson.JSON_FACTORY.createJsonParser(inputStream);
    T newInstance = ClassInfo.newInstance(cls);
    try {
      parser.nextToken();
      Jackson.skipToKey(parser, "data");
      Jackson.parseAndClose(parser, newInstance);
      parser = null;
    } finally {
      if (parser != null) {
        parser.close();
      }
      inputStream = null;
    }    
    return newInstance;
  }
  
}
