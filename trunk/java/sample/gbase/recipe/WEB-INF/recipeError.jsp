<%-- 
Copyright (c) 2007 Google Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
--%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="com.google.api.gbase.client.ServiceErrors"%>
<%@ page import="sample.gbase.recipe.RecipeUtil"%>
<%@page import="sample.gbase.recipe.DisplayUtils"%>
<%@page import="com.google.api.gbase.client.ServiceError"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Google Base API demo: Recipe Book</title>
<link rel="stylesheet" href="style.css">
</head>
<body>

<div id="content">
<%@ include file="demoLabel.inc" %>
  <%
    String error = RecipeUtil.getRecipeError(request);
    String description = RecipeUtil.getRecipeErrorDescription(request);
    ServiceErrors serviceErrors = RecipeUtil.getRecipeErrorObject(request);
  %>

  <div style="height:70%; padding-top: 20px;">
	An error has occured. Hit the browser's Back button for continuing to use
	the Recipe Demo Application. <br/>
  
  <%if (error != null) { %>
    <div class="errordiv">
      <span class="errormessage"><%= DisplayUtils.escape(error) %></span>
      <br/>
    </div>
  <%} // has error %>   

  <%if (description != null) { %>
    <div class="errordiv">
      Detailed information about the error: <br/>
      <pre><%= DisplayUtils.escape(description) %></pre>
    </div>
  <%} // has description %>

  <%if (serviceErrors != null) { %>  
    <div class="errordiv">
      Detailed information about the Google Base service error: <br/>
      <ul>
      <%for (ServiceError serviceError : serviceErrors.getAllErrors()) { %>
        <li> <%= DisplayUtils.escape(serviceError.getType()) %> : <%= DisplayUtils.escape(serviceError.getReason()) %>
      <%} // for each service error %>
      </ul>
      
      <%if (serviceErrors.getAllErrors().size() == 0) { %>
        <i>No service errors could be parsed</i>
      <%} // if no service errors registered  %>
    </div>
  <%} // has errors object %>
  
  </div>

<br clear="all">
<%@ include file="demoLabel.inc" %>
</div>

<%@ include file="footer.inc" %>
</body>
</html>
