/* Copyright (c) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.google.gdata.data.books;

/**
 * Extends the base Link class with Books extensions.
 *
 * 
 */
public class BooksLink {

  /** Link relation type. */
  public static final class Rel {

    /** Annotation link to submit review, rating, labels. */
    public static final String ANNOTATION =
        "http://schemas.google.com/books/2008/annotation";

    /** Epub download link. */
    public static final String EPUBDOWNLOAD =
        "http://schemas.google.com/books/2008/epubdownload";

    /** Link to a description page. */
    public static final String INFO =
        "http://schemas.google.com/books/2008/info";

    /** Link to a preview page. */
    public static final String PREVIEW =
        "http://schemas.google.com/books/2008/preview";

    /** Link that provides the URI of a thumbnail image. */
    public static final String THUMBNAIL =
        "http://schemas.google.com/books/2008/thumbnail";

  }

  /** MIME type of link target. */
  public static final class Type {

    /** Link type used for any image. */
    public static final String ANY_IMAGE = "image/x-unknown";

    /** Link type used for epub downloads. */
    public static final String EPUB = "application/epub";

    /** Link type used for JPEG images. */
    public static final String JPEG = "image/jpeg";

    /** Link type used for PNG images. */
    public static final String PNG = "image/png";

  }

  /** Private constructor to ensure class is not instantiated. */
  private BooksLink() {}

}
