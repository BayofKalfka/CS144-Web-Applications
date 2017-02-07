/*
* Seller(rating, #uid#)
* Bidder(rating, #uid#, location?, country?)
* Item(#itemID#, name, currently, first_bid, number_of_bids, addr, latitude?, longitute?, country, started, ends, seller_id, description)
* ItemBid(#itemID#, #bidder_id#, #time#, amount)
* ItemCategory(#itemID#, category)
* ItemBuyPrice(#itemID#, buy_price?)
*/
/* Drop a table called Seller in the CS144 database */
DROP TABLE IF EXISTS Seller ;
/* Drop a table called Bidder in the CS144 database */
DROP TABLE IF EXISTS Bidder;
/* Drop a table called Item in the CS144 database */
DROP TABLE IF EXISTS Item;
/* Drop a table called ItemBid in the CS144 database */
DROP TABLE IF EXISTS ItemBid;
/* Drop a table called ItemCategory in the CS144 database */
DROP TABLE IF EXISTS ItemCategory;
/* Drop a table called ItemBuyPrice in the CS144 database */
DROP TABLE IF EXISTS ItemBuyPrice;











