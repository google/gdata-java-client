Google Code Issues Data API Java Sample - README.txt
----------------------------------------------------

These Java samples are simple applications that show how to create, read issues
and comments from Google Code using the GData Java client library.


The applications can be built and run using the provided Ant build file found at
gdata/java/build-samples.xml. The samples can be run in the following manner:

1.  Edit gdata/java/build-samples/build.properties to enter your
    Google Account username and password.

2.  Invoke the samples using the appropriate commandline:

    ant -f gdata/java/build-samples.xml sample.projecthosting.read.run
    ant -f gdata/java/build-samples.xml sample.projecthosting.write.run

Alternately, you can compile and run them from the command line:

    java sample.projecthosting.ProjectHostingReadDemo
        --project [project]
        --username [user]
        --password [pass]

    java sample.projecthosting.ProjectHostingWriteDemo
        --project [project]
        --username [user]
        --password [pass]

NOTE: The ProjectHostingWriteDemo will create issues and add comments to them,
so it is recommended that you use a test project.
