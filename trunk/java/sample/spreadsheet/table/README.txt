Google Spreadsheets data API Java Sample - README.txt
-----------------------------------------------------

This is a very simple Java example of how the API can be used on a
Google spreadsheet to create a table object.  This table object 
can be updated, and deleted and you can query it based on 
it's title.  It can be placed anywhere on the grid.  The 
records in the table (see RecordDemo) can be sorted and queried
without modifying the position of the table headers and records.

Before starting this demo, make sure you create a spreadsheet. 
The spreadsheet can be empty as you will be adding a table to it 
that will contain column headers.  

An example table might look like:

    Name      Day      Phone

We suggest you leave your browser open while trying this out.

This can be built and run with Ant:

// TODO down here.
1.  Edit gdata/java/build-samples/build.properties with your Google Account
    username and password.

2.  Run the Ant rule:

    ant -f gdata/java/build-samples.xml sample.spreadsheet.list.run

Alternately, you can compile and run it from the command line:

    java sample.list.TableDemo
        --username [user]
        --password [pass]
