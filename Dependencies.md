# Introduction #

GData Java Client package depends on several third party libraries.  This page lists all the dependency packages.

# Dependency Packages #
## Required ##
  * Java Development Kit version 5.0 or greater.  Latest version of JDK is available for download from http://java.sun.com.
  * [Google Collections](http://code.google.com/p/google-collections/) version 1.0 or higher.  This is included in the release package under "java/deps/google-collect-1.0-rc2.jar".
  * [Apache ANT](http://ant.apache.org) version 1.7.0 or higher.  This is required only if you are rebuilding the Core API libraries.

## Optional ##
  * mail.jar in Sun's [Java Mail API](http://java.sun.com/products/javamail) (version 1.4 or greater).  Package javax.mail is contained in mail.jar. This is required only for APIs dependent on Media capabilities which include [Base Data API](http://code.google.com/apis/base/), [Document List Data API](http://code.google.com/apis/documents/overview.html), [Maps Data API](http://code.google.com/apis/maps), [Picasa Web Album API](http://code.google.com/apis/picasaweb/overview.html) and [YouTube Data API](http://code.google.com/apis/youtube/overview.html).
  * activation.jar in Sun's [Java Beans Activation Framework](http://java.sun.com/products/javabeans/jaf).   The package javax.activation is contained in activation.jar. This is required only if using JDK version 1.5 or less and for Data APIs dependent on Media capabilities which include [Base Data API](http://code.google.com/apis/base/), [Document List Data API](http://code.google.com/apis/documents/overview.html), [Maps Data API](http://code.google.com/apis/maps), [Picasa Web Album API](http://code.google.com/apis/picasaweb/overview.html) and [YouTube Data API](http://code.google.com/apis/youtube/overview.html).
  * servlet.jar in Sun's [Servlet API](http://java.sun.com/products/servlet) (version 2.4 or greater).  This is required only for code samples 'sample.authsub' and 'sample.gbase.recipe' packages.