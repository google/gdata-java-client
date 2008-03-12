Generic Tester Client for Google Data API - README.txt
------------------------------------------------------

A generic client for querying a GData feed and optionally
inserting/updating/deleting entries.

It uses the specified Google Account username and password to
query the specified feed URL and displays the title and content
of each entry returned. If --update is specified, it will insert
an entry, update it, and then delete it.

The application can be built and run using the provided Ant build file found at
gdata/java/build-samples.xml.  The sample can be run in the following manner:

1.  Edit gdata/java/build-samples/build.properties to enter your Google Account username and
    password.

2.  Invoke the sample using the following commandline:

    ant -f gdata/java/build-samples.xml sample.tester.run

NOTE: This sample will insert, update, and delete an entry if --update
      is specified. If you don't specify --update, the tester will not
      modify any entries.
