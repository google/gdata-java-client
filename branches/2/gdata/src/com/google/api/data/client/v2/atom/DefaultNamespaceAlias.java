// Copyright 2010 Google Inc. All Rights Reserved.
package com.google.api.data.client.v2.atom;

import com.google.api.data.client.entity.Name;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to specify the default namespace alias to use when the
 * {@link Name} annotation is missing from the fields in this type or if the
 * namespace alias is missing from the value of the {@link Name} annotation.
 * This overrides for the fields in this type the what has been specified as the
 * general default {@code ""} namespace.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Deprecated
public @interface DefaultNamespaceAlias {
  String value();
}
