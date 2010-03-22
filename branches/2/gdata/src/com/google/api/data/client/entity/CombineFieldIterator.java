package com.google.api.data.client.entity;

import java.util.NoSuchElementException;

public class CombineFieldIterator implements FieldIterator {

  private boolean isNextComputed;

  private boolean hasNext = true;

  private int index = -1;
  private int nextIndex;
  private FieldIterator fieldIterator;
  private FieldIterator nextFieldIterator;
  private final FieldIterator[] fieldIterators;

  CombineFieldIterator(FieldIterator... fieldIterators) {
    this.fieldIterators = fieldIterators;
  }

  public String nextFieldName() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    this.isNextComputed = false;
    int nextIndex = this.nextIndex;
    if (this.index != nextIndex) {
      this.fieldIterator = this.nextFieldIterator;
      this.index = nextIndex;
    }
    return this.fieldIterator.nextFieldName();
  }

  public Object getFieldValue() {
    return this.fieldIterator.getFieldValue();
  }

  public boolean hasNext() {
    if (this.isNextComputed) {
      return this.hasNext;
    }
    this.isNextComputed = true;
    FieldIterator fieldIterator = this.fieldIterator;
    if (fieldIterator != null && fieldIterator.hasNext()) {
      return true;
    }
    int nextIndex = this.index;
    FieldIterator[] fieldIterators = this.fieldIterators;
    int size = fieldIterators.length;
    do {
      if (++nextIndex == size) {
        fieldIterator = null;
        this.hasNext = false;
        break;
      }
      fieldIterator = fieldIterators[nextIndex];
    } while (!fieldIterator.hasNext());
    this.nextIndex = nextIndex;
    this.nextFieldIterator = fieldIterator;
    return this.hasNext;
  }
}
