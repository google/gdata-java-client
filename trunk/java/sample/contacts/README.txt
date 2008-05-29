Google Contacts data API Java Sample - README.txt
-------------------------------------------------

The Java ContactExample is simple application that shows sample usage to
create/read/update/delete data on feeds from Google Contacts using the
GData Java client library.


The applications can be built and run using the provided Ant build file found at
gdata/java/build-samples.xml.  The sample can be run in the following manner:


1.  Invoke the samples using the appropriate command:

    ant -f gdata/java/build-samples.xml sample.contacts.run

NOTE: These samples will insert, update, and delete contact entries using
read-write feed for the account specified.  They will clean up after
themselves, but it is recommended that you use a test account.
You need to edit appropriate variables for username and password inx 
build-samples/build.properties (sample.credentials.username and 
sample.credentials.password)
