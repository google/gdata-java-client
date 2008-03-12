Google Blogger data API Java Sample - README.txt
-------------------------------------------------

The Java Blogger sample is a simple application that shows sample usage to
create/read/update/delete data on Blogger using the GData Java client
library.

The application can be built and run using the provided Ant build file found at
gdata/java/build-samples.xml.  The sample can be run in the following manner:

1.  Edit gdata/java/build-samples/build.properties to enter your
    Google Account username and password with which you want to test the sample.

2.  Invoke the sample using the following commandline:

    ant -f gdata/java/build-samples.xml sample.blogger.run

NOTE: This sample will insert, update, and delete blog entries using the
read-write feed.  When finished, the sample will clean up after itself.
