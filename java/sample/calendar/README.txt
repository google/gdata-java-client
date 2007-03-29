Google Calendar data API Java Sample - README.txt
-------------------------------------------------

The Java Calendar sample is a simple application that shows sample usage to
create/read/update/delete data on Google Calendar using the GData Java client
library.


The application can be built and run using the provided Ant build file found at
gdata/java/build.xml.  The sample can be run in the following manner:

1.  Edit gdata/java/build.properties to enter your Google Account username and
    password, as well as the feed URI on which you want to test the sample.

2.  Invoke the sample using the following commandline:

    ant -f gdata/java/build.xml sample.calendar.run

NOTE: This sample will insert, update, and delete events.  Thus it will use the
      read-write feed.  It will clean up after itself.
