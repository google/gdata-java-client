Google Spreadsheets data API Java Sample - README.txt
-----------------------------------------------------

This is a very simple Java example of how the API can be used on a
Google spreadsheet to create record objects and add them to a table object.
The records can be updated, deleted and queried based on their contents.

Before starting this demo, make sure you create a spreadsheet with a table.
You can create a table object using the TableDemo.  The demo will prompt you
to choose a table to add records to.

An example table might look like:

    Name      Day      Phone

Example records might look like:
    Rosa      Tue      555-1212,
    Vladimir  Wed      555-3137,
    Sanjay    Thu      555-2127,
    Ejike     Fri      555-4444.

We suggest you leave your browser open while trying this out.

This can be built and run with Ant:

// TODO down here.
1.  Edit gdata/java/build-samples/build.properties with your Google Account
    username and password.

2.  Run the Ant rule:

    ant -f gdata/java/build-samples.xml sample.spreadsheet.list.run

Alternately, you can compile and run it from the command line:

    java sample.list.RecordDemo
        --username [user]
        --password [pass]
