<?xml version="1.0" ?>
<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Events</title>
  <link rel="stylesheet" type="text/css" href="css/app.css" />
</head>
<body>
  <h1>Event Publisher</h1>
  <h2>Events to be published:</h2>
  <ul>
  <c:forEach var="event" items="${events}">
    <li><b><c:out value="${event.title}"/></b>
    <ul>
      <li>Description: <c:out value="${event.description}"/></li>
      <li>Start Date: <fmt:formatDate value="${event.startDate}" dateStyle="full"/></li>
      <li>End Date: <fmt:formatDate value="${event.endDate}" dateStyle="full"/></li>
    </ul>
    </li>
  </c:forEach>
  </ul>
  <form method="post" action="?action=publish">
    <h2>Publish these events to the following targets:</h2>
    <ul>
      <li><input type="checkbox" name="calendar" value="checked" /> Calendar</li>
      <li><input type="checkbox" name="base" value="checked" /> Base</li>
    </ul>
    <input type="hidden" name="action" value="publish" />
    <input type="submit" value="Publish" />
  </form>
</body>
</html>
