<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
	<head>
		<style>
			table,td {
 			 border : 1px solid black;
  			border-collapse: collapse;
			}
			td {
  			padding: 5px;
			}
		</style>
		<title>eBay Search: Project 4</title>
		<script type="text/javascript" src="autosuggest.js"></script>
		<script type="text/javascript" src="statesuggest.js"></script>
		<script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("q"), new StateSuggestions());        
            }
        </script>
	</head>
    <body>
    	<div>
    		<h1>Keyword Search</h1>
    		<form method=GET action="search">			
			 	Please enter a query: <br>
				<input id="q" type="text" name="q" size="20px">
				<input type="submit" value="Search">
				<input name="numResultsToSkip" type="hidden" value="0" />
                <input name="numResultsToReturn" type="hidden" value="20" />					
			</form>
    	</div>
    	<div class= "container">
    		Number of relevant results: 
			<numOfResults><%= request.getAttribute("numOfResults") %></numOfResults><br>
			Search results:
			<%
			String query = (String) request.getParameter("q");
			int numResultsToSkip = 0;
        	int numResultsToReturn = 0;
			if(query != null) {
        		numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip")); 
        		numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn")); 
        	}
    		%>
    		<a href='/eBay/search?q=<%= query %>&numResultsToSkip=<%=  numResultsToSkip - 20 %>&numResultsToReturn=<%=  numResultsToReturn %>'>Previous</a>
			<a href='/eBay/search?q=<%= query %>&numResultsToSkip=<%=  numResultsToSkip + 20 %>&numResultsToReturn=<%= numResultsToReturn %>'>Next</a>
			<table class="table">
	      		<c:forEach items="${results}" var="search">
		        	<tr>
		            	<td><a href='item?id=<c:out value="${search.itemId}"/>'><c:out value="${search.itemId}"/></a>:</td>
		            	<td><a href='item?id=<c:out value="${search.itemId}"/>'><c:out value="${search.name}"/></a></td>
		        	</tr>
		      	</c:forEach>
			</table>
    	</div>
    </body>
</html>



