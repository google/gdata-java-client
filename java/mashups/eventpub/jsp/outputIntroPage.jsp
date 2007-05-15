<?xml version="1.0" ?>
<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Welcome to the Event Publisher</title>
  <link rel="stylesheet" type="text/css" href="css/app.css" />
</head>
<body>
  <h1>Event Publisher</h1>
  <p>
    You can publish events to a Google Calendar and/or to Google Base from
    a private spreadsheet on Google Spreadsheets.
  </p>
  <h2>Publish from a private spreadsheet</h2>
  <p>
    To access the private Google Spreadsheet to use for publishing, please 
    <a href="<c:out value="${ssAuthUrl}" />">authenticate</a> to your Google 
    Account
  </p>
</body>
</html>
