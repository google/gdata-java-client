This sample web application publishes events from Google Spreadsheets 
to Google Calendar and Google Base.  A user visits this webapp, 
logs into their Google account using AuthSub and chooses the worksheet 
they wish to publish.  The events stored in the spreadsheet are then 
transferred to a pre-configured Google Calendar and/or Google Base 
account.  New events are added, while pre-existing events are updated.  
Please see the information below on how to configure credentials for 
Google Base and Google Calendar.

The following instructions assume some knowledge of building, deploying 
and running Java web applications.

In order to build and run this sample, there are a number of dependent
libraries required:
* GData Java Client Library:
  * URL:
      http://code.google.com/apis/gdata/
  * Definitions in build.properties:
      gdata_java_client_lib_client.jar
      gdata_java_client_lib_base.jar
      gdata_java_client_lib_calendar.jar
      gdata_java_client_lib_spreadsheet.jar
* Sun's Java Activation Framework (JAF)
  * URL:
      http://java.sun.com/products/javabeans/jaf/downloads/index.html
  * Definitions in build.properties:
      activation.jar
* Sun's Java Mail API
  * URL:
      http://java.sun.com/products/javamail/download.html
  * Definitions in build.properties:
      mail.jar
* Sun's Servlet API library
  * URL:
      http://java.sun.com/products/servlet/download.html
  * Definitions in build.properties:
      servlet.jar
* Jakarta Commons libraries 
  * URL:
      http://jakarta.apache.org/commons/
  * Libraries:
      collections
      configuration
      lang
  * Definitions in build.properties:
      commons-collections.jar
      commons-configuration.jar
      commons-lang.jar
* Jakarta Standard 1.1 Taglib
  * URL:
      http://jakarta.apache.org/site/downloads/downloads_taglibs-standard.cgi
  * Definitions in build.properties:
      jstl.jar
      standard.jar

There are also several tool/application dependencies:
* Apache Ant
  * URL: 
      http://ant.apache.org/
* Servlet container such as Apache Tomcat or full app server such as JBoss
  * URLs:
      http://tomcat.apache.org/
      http://www.jboss.org/downloads/index

Before you begin, please do the following: 
1) Update build.properties to point to the correct location of the 
   dependencies.  By default, the appropriate jar files should be placed
   in /tmp/gdata_dep.  Please see build.properties for a list of all
   jar files required.
2) Update resources/EventPublisher.properties to include valid 
   credentials for Google Base and Google Calendar, in addition to the URL
   for the Calendar to which you wish to publish events.  Authentication to
   retrieve the Google Spreadsheets data is done via the AuthSub proxy 
   authentication method
3) Create a Google Spreadsheet which as the following columns.  Note: 
   the name of the columns can be different, as you will be given a chance
   to map the column names to the needed data in the web interface.  However,
   columns dedicated to data of the defined types below are required.
   "Title" (text)
   "Description" (text)
   "Start Date" (Date in MM/DD/YYYY format)
   "End Date" (Date in MM/DD/YYYY format)
   "Web site" (text URL)
   "Location" (text)
   "Calendar URL" (text URL)
   "Base URL" (text URL)
 

To build, run ant.  A war file should be produced in the deploy directory.  
Deploy this file to your servlet container.  Tomcat's default configuration, 
for example, will auto-deploy the war file if it is copied to <root>/webapps.

You should then be able to access this application at:
http://hostname:port/EventPublisher

NOTE: This web application curently doesn't have much error feedback to the
end-user visiting the application.  Most exceptions are caught and logged
to stderr, so please look at the server error logs if you are experiencing
any problems with this application.  In Tomcat, for instance, these errors
would be logged to logs/catalina.out.
