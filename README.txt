Google data API - README.TXT
Mar 21, 2007

Overview
========
The Google data APIs ("GData" for short) provide a simple standard protocol for
reading and writing data on the web. GData combines common XML-based
syndication formats (Atom and RSS) with a feed-publishing system based on the
Atom publishing protocol, plus some extensions for handling queries.

Google also provides a set of client libraries for interacting with
GData-enabled services, in a variety of programming languages. Using these
libraries, you can construct GData requests, send them to a service, and
receive responses.

Several services currently support the GData API. A complete up-to-date list
along with respective documentation can be found on the GData site:
http://code.google.com/apis/gdata


Package information
===================
The GData package contains:
 o  documentation for GData, the GData-enabled services, and the client
    libraries
 o  the Java client library, its sources, and build files

The documentation can be found in the 'doc' folder.  The package contains just
the 'java' library which can be found in the 'java' folder. Refer to
INSTALL.txt for details on package dependencies and install instructions. 
There are client libraries in various other languages including C#, PHP, 
JavaScript and more. Please see the GData web page for information on obtaining
other client libraries:
http://code.google.com/apis/gdata


Known Issues
============

The Google data APIs and libraries are still in beta so please use one of
the API support Google Groups to give us any feedback, issues or bugs:
http://groups.google.com/group/google-help-dataapi
