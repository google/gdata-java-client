<%-- 
Copyright (c) 2006 Google Inc.

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
<%@ page import="sample.gbase.recipe.DisplayUtils"%>
<%@ page import="sample.gbase.recipe.Recipe"%>
<%@ page import="sample.gbase.recipe.RecipeUtil"%>
<html>
<head>
<title>Google Base API Demo: Recipe Book</title>
<link rel="stylesheet" href="style.css"/>
</head>

<body>

<div id="content">
<%@ include file="demoLabel.inc" %>

<%Recipe recipe = (Recipe) request.getAttribute(RecipeUtil.RECIPE_ATTRIBUTE); %>
<h1><a href="recipeSearch" class="on">All Recipes</a> &gt; <%=DisplayUtils.escape(recipe.getTitle()) %></h1>

<%@ include file="leftNav.jsp" %>
<div id="body" class="main">
    <div id="singleitem">
    <h3 class="itemtitle"><%=DisplayUtils.escape(recipe.getTitle()) %></h3>
    <h4 class="postdate"><%=recipe.getPostedOnAsString(true) %></h4>
    <%if (recipe.hasCuisine()) { %>
      <h5>Cuisine: <%=DisplayUtils.printList(recipe.getCuisine()) %></h5><br/>
    <%} %>
    <%if (recipe.hasCookingTime()) { %>
      <h5>Cooking time: <%=recipe.getCookingTime().getValue() %> <%=DisplayUtils.escape(recipe.getCookingTime().getUnit()) %></h5>
    <%} %>

    <%if (recipe.hasMainIngredient()) { %>
    <div class="ingredients">
    <ul>
      <%for (String ingredient : recipe.getMainIngredient()) { %>
        <li><%=DisplayUtils.escape(ingredient) %></li>
      <%} %>
    </ul>
    </div>
    <%} %>
    <br/>
    <div class="preparation">
        <%=DisplayUtils.escape(recipe.getDescription()) %>
    </div>
    </div>

</div>
<br clear="all">

<%@ include file="demoLabel.inc" %>
</div>

<%@ include file="footer.inc" %>

</body>
</html>

