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

<div class="rightcontent">
<p class=title>Google AuthSub Demo </p>
<div class="content">
<h1>Google AuthSub Demo</h1>
<p>
This page demonstrates a basic version of AuthSub in action.</p>
<p>
The AuthSub token is first retrieved from Google and then used to retrieve your Calendar entries.
</p>

<h2><a name="authenticate">Authenticate</a></h2>
<p>

<%
    StringBuffer continueUrl = request.getRequestURL();
    int index = continueUrl.lastIndexOf("/");
    continueUrl.delete(index, continueUrl.length());
    continueUrl.append("/LoginServlet");
%>

<a href="<%=continueUrl.toString()%>">Login here</a>

</p>
</div>
</div>

<div class="leftcontent">
<p class="footerimage"><img src="http://www.google.com/images/art.gif">
</p></div><div class="rightcontent">
<p>
<img src="http://www.google.com/images/cleardot.gif" width="1" height="45">
</p> </div>
</body>
</html>
