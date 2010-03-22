package com.google.api.data.client.entity;

import static java.lang.annotation.ElementType.FIELD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Use this annotation to override the name to use for the field. */
@Target(FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
  String value();
}
