This file contains a simple utility class to simplify opening database
connections in Java applications, such as the one you will write to build
your Lucene index. 

To build and run the code, use the "run" ant target inside
the directory with build.xml by typing "ant run".

1.List of tables in mysql CS144 database
Seller(rating, #uid#)   
PRIMARY KEY : uid
Bidder(rating, #uid#, location?, country?)
PRIMARY KEY : uid
Item(#itemID#, name, currently, first_bid, number_of_bids, addr, latitude?, longitute?, country, started, ends, seller_id, description)
PRIMARY KEY : itemID
ItemBid(#itemID#, #bidder_id#, #time#, amount)
PRIMARY KEY : itemID, bidder_id, time
ItemCategory(#itemID#, category)
PRIMARY KEY : itemID
ItemBuyPrice(#itemID#, buy_price?)
PRIMARY KEY : itemID

2.Document your design choices
I have created indexes on below attributes:
TABLE: ATTRIBUTES
Item: itemID, name, description
ItemCategory: itemID, category

For each itemID in table Item, I create a document object and create indexes on itemID, name and content which is a text combination of name, category(es) and description.

3.Briefly discuss why you've chosen to create your particular index(es)
With content (A text combination of name, category(es) and description), I can search for the keyword on the union of name, category and description data.
With itemID and name, I can return the corresponding itemID and name of the searching results. 
