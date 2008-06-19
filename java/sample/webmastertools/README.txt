Google Webmaster Tools Data API Java Sample - README.txt
-------------------------------------------------

The Java Webmaster Tools sample provides examples to list 
sites/sitemaps, add site/sitemap, delete site/sitemap, and verify a site in 
Webmaster Tools through the Webmaster Tools GData Java client library.

The application can be built and run using the provided Ant build file found at
gdata/java/build-samples.xml.  The sample can be run in the following manner:

1.  Edit gdata/java/build-samples/build.properties to enter your Google Account 
    username and password with which you want to test the sample.

2.  Invoke the sample using the following command line in the gdata/java directory:

    ant sample.webmastertools.run

NOTE: This sample will modify your Webmaster Tools account by adding a site and 
sitemap. Before completion, the sample will delete the added sitemap and site, 
cleaning up after itself.
