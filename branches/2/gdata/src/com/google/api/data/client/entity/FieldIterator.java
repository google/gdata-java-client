// Copyright 2010 Google Inc. All Rights Reserved.

package com.google.api.data.client.entity;

public interface FieldIterator {

  boolean hasNext();

  String nextFieldName();

  Object getFieldValue();
}
