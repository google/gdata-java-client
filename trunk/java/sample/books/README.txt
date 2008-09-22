Google Books Data API Java Samples - README.txt
-------------------------------------------------

The Java Books sample is a simple application that shows sample usage
of the various feeds that comprise the Book Search Data API. It works
in two modes. On the one hand as a readonly sample that allows
anonymous searching for volumes; on the other hand as an authenticated
application that performs read and write on the user's annotation and 
my library feeds.

The application can be built and run using the provided Ant build file found at
gdata/java/build-samples.xml.  The sample can be run in the following manner:

1.  Invoke the samples using the appropriate command line:

    ant -f gdata/java/build-samples.xml sample.books.run

    or

    ant -f gdata/java/build-samples.xml sample.books.run --username
    <username> --password <password>


