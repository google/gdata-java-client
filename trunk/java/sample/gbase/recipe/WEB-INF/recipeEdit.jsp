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
<link rel="stylesheet" href="style.css">
<style type="text/css">
.main { 
	margin-left: 0; 
}
</style>
</head>

<body>

<div id="content">
<%@ include file="demoLabel.inc" %>

<%Recipe recipe = (Recipe) request.getAttribute(RecipeUtil.RECIPE_ATTRIBUTE); %>
<%String message = (String) request.getAttribute(RecipeUtil.MESSAGE_ATTRIBUTE); %>
<h1><a href="recipeList" class="on">My Recipes</a> &gt; <%=recipe.isNew() ? "Add" : "Update" %> a recipe</h1>

<div id="body" class="main">

    <div id="newitem">
    <%=message %>
    <form name="newitem" method="POST" action="recipe<%=recipe.isNew() ? "Add" : "Update?" + RecipeUtil.ID_PARAMETER + "=" + DisplayUtils.escape(recipe.getId()) %>"/>

    <table cellpadding="0" cellspacing="0" border="0" id="createrecipe">
        <tr>
            <th>Recipe title *</th>
            <td><input type="text" name="<%=RecipeUtil.TITLE_PARAMETER %>"
                value="<%=DisplayUtils.escape(recipe.getTitle()) %>" 
                class="txt" size="35"></td>
        </tr>
        <tr>
            <th>Cuisine type *</th>
            <td>
                <%DisplayUtils.printCheckboxes(out, 
                  RecipeUtil.CUISINE_PARAMETER, 
                  RecipeUtil.getMostUsedValues(pageContext.getServletContext()).getMostUsedValuesForAttribute(Recipe.CUISINE_ATTRIBUTE),
                  recipe.getCuisine()); %>
            </td>
        </tr>
        <tr>

            <th>Instructions *</th>
            <td><textarea name="<%=RecipeUtil.DESCRIPTION_PARAMETER %>"
                cols="40" rows="8"
                ><%=DisplayUtils.escape(recipe.getDescription()) %></textarea>
            </td>
        </tr>
        <tr>
            <th>Main ingredients *</th>
            <td>
                <%DisplayUtils.printCheckboxes(out, 
                  RecipeUtil.MAIN_INGREDIENT_PARAMETER, 
                  RecipeUtil.getMostUsedValues(pageContext.getServletContext()).getMostUsedValuesForAttribute(Recipe.MAIN_INGREDIENT_ATTRIBUTE), 
                  recipe.getMainIngredient()); %>
            </td>
        </tr>
        <tr>
            <th>URL</th>
            <td><input type="text" name="<%=RecipeUtil.URL_PARAMETER %>"
                value="<%=DisplayUtils.escape(recipe.getUrl()) %>" 
                class="txt" size="40"></td>
        </tr>
        <tr>
            <th>Cooking time</th>
            <td><input type="text" name="<%=RecipeUtil.COOKING_TIME_PARAMETER %>" 
                value="<%=recipe.hasCookingTime() ? recipe.getCookingTime().getValue() : "" %>"
                class="txt" size="4"> <%=RecipeUtil.COOKING_TIME_UNIT %></td>
        </tr>
        </table>
        <p>
        This recipe will be publicly viewable on the internet once you click "Publish". <br> <br>
        <input type="submit" value="Publish this recipe" style="font-weight:bold;"> &nbsp; 
        <button onclick="history.go(-1); return false;">Cancel</button>
        </p>
        <p>* Mandatory attributes</p>
    </form>
    </div>

</div>
<br clear="all">

<%@ include file="demoLabel.inc" %>
</div>

<%@ include file="footer.inc" %>

</body>
</html>


