<?xml version="1.0" ?> 
<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Worksheet List</title>
  <link rel="stylesheet" type="text/css" href="css/app.css" />
</head>
<body>
  <h1>Event Publisher</h1>
  <h2>Worksheets available:</h2>
  <ul>
    <c:forEach var="ws" items="${wsList}">
      <li><a href="?action=outputColumnList&cellFeed=<c:out value='${ws["cellFeed"]}' />"><c:out value='${ws["title"]}' /></a> </li>
    </c:forEach>
  </ul>
</body>
</html>
