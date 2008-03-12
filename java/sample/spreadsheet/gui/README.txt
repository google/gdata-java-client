Google Spreadsheets data API Java Sample - README.txt
-----------------------------------------------------

This is a fairly complete example that shows a possible application of the
Google Spreadsheets Data API.  This example shows how to handle the login
process (including CAPTCHA), how to select a spreadsheet and/or worksheet,
and a possible interface for manipulating the spreadsheet.

This example might be good for experimenting, but is harder to understand.
For a more down-to-earth example, see the cell or list demo.

This can be built an run with Ant:

1.  Edit gdata/java/build-samples/build.properties with your Google Account
    username, password, and a URL to your Google spreadsheet
    (in the format ....spreadsheets.google.com/ccc?id=...)

2.  Run the Ant rule:

    ant -f gdata/java/build-samples.xml sample.spreadsheet.guidemo.run

Alternately, you can compile and run it from the command line:

    java sample.spreadsheet.gui.GuiDemo
