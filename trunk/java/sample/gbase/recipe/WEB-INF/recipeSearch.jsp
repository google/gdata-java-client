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
<%@ page import="sample.gbase.recipe.RecipeSearch"%>
<%@ page import="sample.gbase.recipe.RecipeUtil"%>
<%@ page import="java.util.Iterator"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Google Base API demo: Recipe Book</title>
<link rel="stylesheet" href="style.css">
</head>
<body>

<div id="content">
<%@ include file="demoLabel.inc" %>

  <%RecipeSearch recipeSearch = RecipeUtil.getRecipeSearch(request); %>
  <%if (recipeSearch.isOwnItems()) { %>
    <a href="recipeSearch" class="toplink">All recipes</a>
  <%} else { %>
    <a href="recipeList" class="toplink">My recipes</a>
  <%} %>
  

  <%if (recipeSearch.isOwnItems()) { %>
    <a href="recipeAdd" class="toplink">Add a recipe</a>
  <%} %>
  
  <h1>
  <%if (recipeSearch.isOwnItems()) { %>
    <a href="recipeList" class="on">My Recipes</a>
  <%} else { %>
    <a href="recipeSearch" class="on">All Recipes</a>
  <%} %>
  &gt; Search
  <%if (recipeSearch.getTotal() > 0) { %>
    results
  <%} %>
  </h1>

<%@ include file="leftNav.jsp" %>

<div id="body" class="main">
  <div id="searchresults">

<script language="JavaScript">
  function gotoPage(index) {
    document.searchForm.reset();
    document.searchForm.startIndex.value = 
      index*<%=recipeSearch.getMaxResults()%>;
    document.searchForm.submit();
  }
  function narrowSearchByIngredient(ingredient) {
    document.searchForm.reset();
    document.searchForm.othermainIngredient.value = ingredient;
    document.searchForm.submit();
  }
  function confirmDelete(oid) {
    if (confirm("Do you really want to delete this recipe?")) {
      window.location = "recipeDelete?oid=" + oid;
    }
  }
</script>   

  <%if (recipeSearch.getTotal() >= 0) {%>
    <%if (recipeSearch.getTotal() == 0) {%>
    <h4>Your search for recipes 
    <%=recipeSearch.getFilterDescription() %>
    did not match any recipes.</h4>
    <%} %>
    <%if (recipeSearch.hasResults()) {%>
      <h4><%=recipeSearch.getTotal()%> recipes 
      <%=recipeSearch.getFilterDescription() %>
      - showing <%=recipeSearch.getCurrentPageInterval()%></h4>
      <%for (Iterator iter = recipeSearch.getRecipes().iterator(); iter.hasNext(); ) {%>
        <%Recipe recipe = (Recipe)iter.next();%>
        <p>
        <a href="recipeDisplay?oid=<%=recipe.getId()%>" class="m"><%=DisplayUtils.escape(recipe.getTitle()).toUpperCase() %></a>
        Main ingredient: 
        <%for (Iterator<String> ingredientIter = recipe.getMainIngredient().iterator(); ingredientIter.hasNext(); ) { %>
          <%String ingredient = ingredientIter.next(); %>
          <a href="javascript:narrowSearchByIngredient('<%=ingredient%>')" class="r"><%=ingredient%></a><%=ingredientIter.hasNext() ? ", " : "" %>
        <%} %>
        <br>
        posted on <%=recipe.getPostedOnAsString(false)%> 
        by <%=DisplayUtils.escape(recipe.getPostedBy())%><br>
      
        <%if (recipeSearch.isOwnItems()) { %>
          <a href="recipeUpdate?oid=<%=recipe.getId() %>">Update</a> |
          <a href="javascript:confirmDelete('<%=recipe.getId() %>')">Delete</a>
        <%} %>
        </p>
      <%} // for recipe in recipeSearch.getRecipes()%>
    <%} // if recipeSearch.hasResults%>
    
    <%if (recipeSearch.getTotal() > recipeSearch.getMaxResults()) { //results don't fit on one page?%>
      <p class="pagination">
      Results <%=recipeSearch.getCurrentPageInterval() %> of <%=recipeSearch.getTotal() %><br/>
      <%if (recipeSearch.hasPreviousPage()) {%>
        <a href="javascript:gotoPage(<%=recipeSearch.getCurrentPage() - 1 %>)" 
           style="margin-right:1em;"
           class="next">&laquo; Previous <%=recipeSearch.getMaxResults() %></a>
      <%} %>
      <%if (recipeSearch.hasNextPage()) {%>
        <a href="javascript:gotoPage(<%=recipeSearch.getCurrentPage() + 1 %>)" 
           class="next">Next <%=recipeSearch.getNextPageSize() %> &raquo;</a>
      <%} %>
      </p>
    <%} // pages? %>
  <%} //total >= 0 %>
  
  </div>
    
</div>

<br clear="all">
<%@ include file="demoLabel.inc" %>
</div>

<%@ include file="footer.inc" %>
</body>
</html>
