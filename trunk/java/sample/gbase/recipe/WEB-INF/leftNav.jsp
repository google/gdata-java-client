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
<%@ page import="sample.gbase.recipe.DisplayUtils"%>
<%@ page import="sample.gbase.recipe.Recipe"%>
<%@ page import="sample.gbase.recipe.RecipeSearch"%>
<%@ page import="sample.gbase.recipe.RecipeUtil"%>
<%RecipeSearch searchResults = RecipeUtil.getRecipeSearch(request);%>
<div id="leftnav">
<div id="search">
    <form name="searchForm" action="recipe<%= searchResults.isOwnItems() ? "List" : "Search" %>" method="GET"><h2>Find a recipe</h2>
<table border="0" cellpadding="0" cellspacing="0" id="advancedsearch">
    <tr><th colspan="2">With the word or phrase</th></tr>
    <tr>
        <td><input type="text" name="query" size="25" class="txt"
                 value='<%=DisplayUtils.escape(searchResults.getQuery()) %>'>
    </td>
    </tr>
    <tr>
        <th>Cuisine type</th>
    </tr>
    <tr>
        <td><%DisplayUtils.printCheckboxes(out, 
                  RecipeUtil.CUISINE_PARAMETER, 
                  RecipeUtil.getMostUsedValues(pageContext.getServletContext()).getMostUsedValuesForAttribute(Recipe.CUISINE_ATTRIBUTE),
                  searchResults.getCuisineValues()); %></td>
    </tr>
    <tr>
        <th>Main ingredient</th>
    </tr>
    <tr>
        <td><%DisplayUtils.printCheckboxes(out, 
                  RecipeUtil.MAIN_INGREDIENT_PARAMETER, 
                  RecipeUtil.getMostUsedValues(pageContext.getServletContext()).getMostUsedValuesForAttribute(Recipe.MAIN_INGREDIENT_ATTRIBUTE), 
                  searchResults.getMainIngredientValues()); %></td>
    </tr>
    <tr>
        <th>Cooking time</th>
    </tr>
    <tr>
      <td><input name="cookingTime" type="text" size="4"
               value='<%=DisplayUtils.escape(searchResults.getCookingTime()) %>'
          > <%=RecipeUtil.COOKING_TIME_UNIT %></td>
    </tr>
</table>
<br/>
<button onclick="document.searchForm.startIndex.value='0'; document.searchForm.submit();" 
    style="font-weight:bold; -moz-border-radius:3px;border: 2px outset #bbbbbb;font-size:124%; padding:2px 1em;">Search</button>
<input type="hidden" name="startIndex" value="0"></form>
</div>

<p id="poweredby">
&nbsp;Powered by<br>
<a href="http://base.google.com/"><img src="googleBase.gif" border="0" alt="Google Base" vspace="2"></a>
</p>
</div>
