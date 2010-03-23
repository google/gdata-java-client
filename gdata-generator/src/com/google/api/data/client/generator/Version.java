// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;

public final class Version implements Comparable<Version> {
  final Client client;
  public String id;
  public AtomInfo atom;

  Version(Client client) {
    this.client = client;
  }

  public int compareTo(Version other) {
    if (this == other) {
      return 0;
    }
    int compare = client.compareTo(other.client);
    if (compare != 0) {
      return compare;
    }
    return id.compareTo(other.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof Client)) {
      return false;
    }
    Client other = (Client) obj;
    return id.equals(other.id);
  }

  String getJarName() {
    return client.id + "-v" + id;
  }

  String getPathRelativeToSrc() {
    return "com/google/api/data/" + client.id + "/v" + id;
  }

  String getPackageName() {
    return "com.google.api.data." + client.id + ".v" + id;
  }

  void parse(JsonParser parser) throws IOException {
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.getCurrentName();
      parser.nextToken();
      if (key == "atom") {
        atom = new AtomInfo();
        atom.parse(parser);
      } else if (key == "id") {
        id = parser.getText();
      } else {
        throw new IllegalArgumentException("unrecognized key: " + key);
      }
    }
  }
}
