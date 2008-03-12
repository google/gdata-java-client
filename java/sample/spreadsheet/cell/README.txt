Google Spreadsheets data API Java Sample - README.txt
-----------------------------------------------------

This is a very simple Java example of reading and writing to cells on the
spreadsheet.  Using it, you can fully experiment with setting cells,
using both formulas, and values.


This can be built an run with Ant:

1.  Edit gdata/java/build-samples/build.properties with your Google Account
    username, password, and a URL to your Google spreadsheet
    (in the format ....spreadsheets.google.com/ccc?id=...)

2.  Run the Ant rule:

    ant -f gdata/java/build-samples.xml sample.spreadsheet.celldemo.run

Alternately, you can compile and run it from the command line:

    java sample.spreadsheet.cell.CellDemo
        --username [user]
        --password [pass]
        --spreadsheetUrl http://....spreadsheets.google.com/ccc?id=...
