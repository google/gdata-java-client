<?xml version="1.0" ?> 
<%@ page language="java" contentType="text/html" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
  <title>Column List</title>
  <link rel="stylesheet" type="text/css" href="css/app.css" />
</head>
<body>
  <form method="post" action="?action=listEvents">
    <h1>Event Publisher</h1>
    <h2>Please map needed data to the appropriate columns:</h2>
    <input type="hidden" name="action" value="listEvents" />
    <ul>
    <li>
      Title:
        <select name="fdTitle">
          <c:forEach var="column" items="${columnList}">
            <option value="<c:out value='${column}' />">
              <c:out value='${column}' />
            </option>
          </c:forEach>
        </select>
    </li>
    <li>
      Description:
        <select name="fdDescription">
          <c:forEach var="column" items="${columnList}">
            <option value="<c:out value='${column}' />">
              <c:out value='${column}' />
            </option>
          </c:forEach>
        </select>
    </li>
    <li>
      Start Date:
        <select name="fdStartDate">
          <c:forEach var="column" items="${columnList}">
            <option value="<c:out value='${column}' />">
              <c:out value='${column}' />
            </option>
          </c:forEach>
        </select>	
    </li>
    <li>	
      End Date:
        <select name="fdEndDate">
          <c:forEach var="column" items="${columnList}">
            <option value="<c:out value='${column}' />">
              <c:out value='${column}' />
            </option>
          </c:forEach>
        </select>	
    </li>
    <li>
      Location:
        <select name="fdLocation">
          <c:forEach var="column" items="${columnList}">
            <option value="<c:out value='${column}' />">
              <c:out value='${column}' /><br />
            </option>
          </c:forEach>
        </select>
    </li>
    <li>
      Web Site:
        <select name="fdWebSite">
          <c:forEach var="column" items="${columnList}">
            <option value="<c:out value='${column}' />">
              <c:out value='${column}' /><br />
            </option>
          </c:forEach>
        </select>
    </li>
    <li>
      Calendar Event Url:
        <select name="fdCalendarUrl">
          <c:forEach var="column" items="${columnList}">
            <option value="<c:out value='${column}' />">
              <c:out value='${column}' /><br />
            </option>
          </c:forEach>
        </select>
    </li>
    <li>
      Base Event Url:
        <select name="fdBaseUrl">
          <c:forEach var="column" items="${columnList}">
            <option value="<c:out value='${column}' />">
              <c:out value='${column}' /><br />
            </option>
          </c:forEach>
        </select>
    </li>
    </ul>
    <input type="submit" value="Publish Events" />
    </form>	
</body>
</html>
