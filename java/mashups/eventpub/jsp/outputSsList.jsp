<?xml version="1.0" ?> 
<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Spreadsheet List</title>
  <link rel="stylesheet" type="text/css" href="css/app.css" />
</head>
<body>
  <h1>Event Publisher</h1>
  <h2>Spreadsheets available:</h2>
  <ul>
  <c:forEach var="ss" items="${ssList}">
    <li><a href="?action=outputWsList&wsFeed=<c:out value='${ss["wsFeed"]}' />">
      <c:out value='${ss["title"]}' />
    </a></li>
  </c:forEach>
  </ul>
</body>
</html>
