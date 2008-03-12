Google Spreadsheets data API Java Sample - README.txt
-----------------------------------------------------

This is a very simple Java example of how the API can be used on a
Google spreadsheet to add, remove, and modify rows like a data table.

Before starting this demo, make sure you create a spreadsheet.  In
the top row, put headers, such as 'Name', 'Day', 'Address', or any
other important piece of information.  A good example might be:

    Name      Day      Phone
    Rosa      Tue      555-1212
    Vladimir  Wed      555-3137
    Sanjay    Thu      555-2127
    Ejike     Fri      555-4444

It is best practices to freeze the top row, in the 'Sorting' tab.
This way, sorting will make sure the top row stays intact.  We
suggest you leave your browser open while trying this out.


This can be built an run with Ant:

1.  Edit gdata/java/build-samples/build.properties with your Google Account
    username, password, and a URL to your Google spreadsheet.
    (in the format ....spreadsheets.google.com/ccc?id=...)

2.  Run the Ant rule:

    ant -f gdata/java/build-samples.xml sample.spreadsheet.list.run

Alternately, you can compile and run it from the command line:

    java sample.list.ListDemo
        --username [user]
        --password [pass]
        --spreadsheetUrl http://....spreadsheets.google.com/ccc?id=...
