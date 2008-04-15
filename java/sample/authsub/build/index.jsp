<html>

<head>
<title>Google Calendar AuthSub demo</title>
<link rel="stylesheet" type="text/css" href="css/google.css">
<link rel="stylesheet" type="text/css" href="css/frameless.css">
<!--[if IE]>
  <link rel="stylesheet" type="text/css" href="css/ieonly.css" />
<![endif]-->
</head>


<body>
<div class="leftcontent">
<p class="topimage"><a href="http://www.google.com/">
<img src="http://www.google.com/images/google_sm.gif" border="0" alt="Return to Google homepage" /></a>
</p>
</div>

<%
    StringBuffer continueUrl = request.getRequestURL();
    int index = continueUrl.lastIndexOf("/");
    continueUrl.delete(index, continueUrl.length());
    continueUrl.append("/LoginServlet");
%>

<FORM NAME=index METHOD=POST ACTION="<%=continueUrl.toString()%>">
<div class="rightcontent">
<p class=title>Google AuthSub Demo </p>
<div class="content">
<h1>Google AuthSub Demo</h1>
<p>
This page demonstrates a basic version of AuthSub in action.</p>
<p>
The AuthSub token is first retrieved from Google and then used to retrieve your Calendar entries.
</p>
<p>
Note: <br>
If your user account is registered to a domain other than 'gmail.com', specify the domain name as well.
If the calendar service in your domain requires secure connection (ssl) for retrieving feeds, make sure to check the ssl option.
</p>

<p>

  <br><b>Specify user domain:</b>
  <br>
  <input type="radio" name="defaultdomain" value="true" onclick="javascript:document.index.domain.disabled=true" checked>
    Use default domain (gmail.com)
  <br>
  <input type="radio" name="defaultdomain" value="false" onclick="javascript:document.index.domain.disabled=false">
    Use my application domain (ex: mydomain.com): <input type=text name=domain disabled>
  <br>
  <br>
  <input type="checkbox" name="secure" value="true">Use secure connection to retrieve calendar feed.
  <p><input type=submit value="Authenticate">
</p>
</div>
</div>
</FORM>

<div class="leftcontent">
<p class="footerimage"><img src="http://www.google.com/images/art.gif">
</p></div><div class="rightcontent">
<p>
<img src="http://www.google.com/images/cleardot.gif" width="1" height="45">
</p> </div>
</body>
</html>
