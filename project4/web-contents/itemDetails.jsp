<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="edu.ucla.cs.cs144.*" %>
<html>
	<head>
		<%	
			float latitude = 0;
			float longitude = 0;
			if(!request.getAttribute("latitude").equals("NaN") && !request.getAttribute("longitude").equals("NaN")) {
				latitude =  Float.valueOf((String)request.getAttribute("latitude")) ;
				longitude =  Float.valueOf((String)request.getAttribute("longitude")) ;
			}
		%>
		<title>eBay Search: Project 4</title>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" /> 
		<style>
      
        table,td {
             border : 1px solid black;
        }
		.column {
    		margin-top: 0px;
    		float: right; 
   			width: 50%;
		}
  		body { margin: 0px; padding: 0px } 
  		#map_canvas { height: 100% } 
		</style>
		<script type="text/javascript" 
    		src="http://maps.google.com/maps/api/js?sensor=false"> 
		</script> 
		<script type="text/javascript"> 
  			function initialize() { 
    		var latlng = new google.maps.LatLng(<%= latitude %>, <%= longitude%>); 
    		var myOptions = { 
      			zoom: 14, // default is 8  
      			center: latlng, 
      			mapTypeId: google.maps.MapTypeId.ROADMAP 
    		}; 
    		var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions); 
  		} 
		</script> 
	</head>
    <body onload="initialize()">
    	<div class='column'>
        <table >
    	<tr>
            <td>Item Id:</td><td>
    	       <itemId><%= request.getAttribute("itemId") %></itemId>
            </td>
        </tr>
    	<tr>
            <td>Item Name: </td><td>
    	       <itemName><%= request.getAttribute("itemName") %></itemNAme>
            </td>
        </tr>
    	<tr>
            <td>Item Category:</td><td>
	      		<c:forEach items="${category}" var="tuple">
		        	
		            	<a><c:out value="${tuple}"/></a><br>
		        	
		      	</c:forEach>
		    </td>
        </tr>
    	<tr>
            <td>Currently: </td><td>
    	       <currently><%= request.getAttribute("currently") %></currently>
            </td>
        </tr>
    	<tr>
            <td>First Bid:</td><td>
    	       <firstBid><%= request.getAttribute("firstBid") %></firstBid>
            </td>
        </tr>
    	<tr>
            <td>Number of Bids: </td><td>
    	       <numOfBids><%= request.getAttribute("num_of_bid") %></numOfBids>
            </td>
        </tr>
    	<tr>
            <td>Buy Price: </td><td>
    	       <buyPrice><%= request.getAttribute("buyPrice") %></buyPrice>
            </td>
        </tr>
    	<tr>
            <td>Item Location :</td><td>
    	       <location><%= request.getAttribute("location") %></currently>
            </td>
        </tr>
    	<tr>
            <td>Latitude:</td><td>
    	       <location><%= request.getAttribute("latitude") %></currently>
            </td>
        </tr>
    	<tr>
            <td>Longitude:</td><td>
    	       <location><%= request.getAttribute("longitude") %></currently>
            </td>
        </tr>
    	<tr>
            <td>Item Country:</td><td>
    	       <country><%= request.getAttribute("country") %></country>
            </td>
        </tr>
    	<tr>
            <td>Auction Started Time:</td><td>
    	       <started><%= request.getAttribute("started") %></started>
            </td>
        </tr>
    	<tr>
            <td>Auction End Time:</td><td>
    	       <ends><%= request.getAttribute("ends") %></ends>
            </td>
        </tr>
    	<tr>
            <td>Seller Id:</td><td> 
    	       <sellerId><%= request.getAttribute("sellerId") %></sellerId>
            </td>
        </tr>
    	<tr>
            <td>Seller Rating:</td><td>
    	       <sellerRating><%= request.getAttribute("sellerRating") %></sellerRating>
            </td>
        </tr>
    	<tr>
            <td>Description:</td><td>
               <description><%= request.getAttribute("description") %></description>
            </td>
        </tr>
    	<tr><td>Bid History:</td><td>
    	
	      		<c:forEach items="${bids}" var="tuple">
		        	
		        		<hr>
		        			Bidder Id:
		            		<a><c:out value="${tuple.bidderId}"/></a><br>
		            		Bidder rating:
		            		<a><c:out value="${tuple.bidderRating}"/></a><br>
		            		Bidder Location:
		            		<a><c:out value="${tuple.bidderLocation}"/></a><br>
		            		Bidder Country:
		            		<a><c:out value="${tuple.bidderCountry}"/></a><br>
		            		Bid Time:
		            		<a><c:out value="${tuple.bidTime}"/></a><br>
		            		Bid Amount:
		            		<a><c:out value="${tuple.bidAmount}"/></a><br>
		               </hr>
		      	</c:forEach>
		</td></tr>
    </table>
    </div>
	<div id="map_canvas" class = 'column'></div> 	
    </body>
</html>





