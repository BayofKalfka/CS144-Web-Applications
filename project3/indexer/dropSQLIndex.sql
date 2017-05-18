/*
* Seller(rating, #uid#)
* Bidder(rating, #uid#, location?, country?)
* Item(#itemID#, name, currently, first_bid, number_of_bids, addr, latitude?, longitute?, country, started, ends, seller_id, description)
* ItemBid(#itemID#, #bidder_id#, #time#, amount)
* ItemCategory(#itemID#, category)
* ItemBuyPrice(#itemID#, buy_price?)
**************************************************/


/*  Drop the spacial index on table ItemGeo */
ALTER TABLE ItemGeo  DROP INDEX pos;

/*  table called ItemGeo in the CS144 database */

DROP TABLE IF EXISTS ItemGeo;