This project is moved from
[googlecode](https://code.google.com/p/gdata-java-client/). wikis are now kept in branch 'wiki'.
Issues are all migrated, too.

# Description

Older Google Data (GData) APIs use XML as their underlying format, but most Google APIs have released newer versions of their APIs based on JSON. You should migrate your code to use the new API infrastructure that is based on JSON. Likewise, if you have an existing application that uses the GData Java client library, you need to update your code to use the [Google APIs Client Library for Java](https://github.com/google/google-api-java-client), as described on the [migration](https://github.com/google/gdata-java-client/blob/wiki/MigratingToGoogleApiJavaClient.md) page.

We have stopped actively developing the GData Java client library, and the following table shows the GData status of relevant Google APIs:
We have stopped actively developing the GData Java client library. For
information about the GData status of relevant Google APIs, see the [GData API
Directory](https://developers.google.com/gdata/docs/directory)

**Android support:**
If you are developing for Android and the Google API you want to use is included in the [Google Play Services library](https://developer.android.com/google/play-services/index.html), you should use that library for the best performance and experience. If the Google API you want to use with Android is not part of the Google Play Services library, you can use the [Google APIs Client Library for Java](https://github.com/google/google-api-java-client), which supports Android 1.5 (or higher) and provides other features such as OAuth 2.0 and Maven.

# Download
[Source](http://storage.googleapis.com/gdata-java-client-binaries/gdata-src.java-1.47.1.zip)

[Samples](http://storage.googleapis.com/gdata-java-client-binaries/gdata-samples.java-1.47.1.zip)
