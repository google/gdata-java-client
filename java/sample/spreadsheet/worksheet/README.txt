Google Spreadsheets data API Java Sample - README.txt
-----------------------------------------------------

This is a very simple Java example of reading and writing to worksheets in
a spreadsheet.  Using it, you can fully experiment with creating, listing,
updating, and deleting worksheets.


This can be built an run with Ant:

1.  Edit gdata/java/build-samples/build.properties with your Google Account
    username, password, and a URL to your Google spreadsheet
    (in the format ....spreadsheets.google.com/ccc?id=...)

2.  Run the Ant rule:

    ant -f gdata/java/build-samples/spreadsheet.xml sample.spreadsheet.worksheetdemo.run

Alternately, you can compile and run it from the command line:

    java sample.spreadsheet.worksheet.WorksheetDemo
        --username [user]
        --password [pass]

