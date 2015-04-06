

# How to handle com.google.gdata.util.RedirectRequiredException #
This means your request to Google Data APIs server is redirected more than once by a http proxy.  If the redirection is valid, you can choose to follow the redirected url as follows:
```
CalendarFeed feed;
try {
  feed = calendarService.getFeed(feedUrl, CalendarFeed.class)
} catch (RedirectRequiredException e) {
  feedUrl = new URL(re.getRedirectLocation());
  feed = cs.getFeed(feedUrl, CalendarFeed.class);
}
```
Caution: You may want to evaluate the redirected url, before retrying to the new url.


# Process hangs when retrieving a Feed or Entry using Service#getFeed or Service#getEntry in JDK 1.5 in MacOS #
This is a known JDK bug.  You can workaround this problem by running in interpreted mode, using JVM option "-Xint".