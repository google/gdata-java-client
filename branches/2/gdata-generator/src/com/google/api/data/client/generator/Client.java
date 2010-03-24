// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.generator;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.IOException;
import java.util.SortedSet;
import java.util.TreeSet;

public final class Client implements Comparable<Client> {

  public String id;
  public String name;
  public String className;
  public SortedSet<Version> versions;
  public String authTokenType;

  public int compareTo(Client client) {
    if (client == this) {
      return 0;
    }
    return id.compareTo(client.id);
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

  void parse(JsonParser parser) throws IOException {
    while (parser.nextToken() != JsonToken.END_OBJECT) {
      String key = parser.getCurrentName();
      parser.nextToken();
      if (key == "authTokenType") {
        authTokenType = parser.getText();
      } else if (key == "className") {
        className = parser.getText();
      } else if (key == "id") {
        id = parser.getText();
      } else if (key == "name") {
        name = parser.getText();
      } else if (key == "versions") {
        versions = new TreeSet<Version>();
        while (parser.nextToken() != JsonToken.END_ARRAY) {
          Version version = new Version(this);
          version.parse(parser);
          versions.add(version);
        }
      } else {
        throw new IllegalArgumentException("unrecognized key: " + key);
      }
    }
    if (id == null) {
      throw new IllegalArgumentException("id required");
    }
    if (versions == null || versions.size() < 1) {
      throw new NullPointerException("at least one version required");
    }
    if (className == null) {
      className = Character.toUpperCase(id.charAt(0)) + id.substring(1);
    }
  }
}
