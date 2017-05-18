/*
* Seller(rating, #uid#)
* Bidder(rating, #uid#, location?, country?)
* Item(#itemID#, name, currently, first_bid, number_of_bids, addr, latitude?, longitute?, country, started, ends, seller_id, description)
* ItemBid(#itemID#, #bidder_id#, #time#, amount)
* ItemCategory(#itemID#, category)
* ItemBuyPrice(#itemID#, buy_price?)
**************************************************/


/* Create a table called ItemGeo in the CS144 database */
CREATE TABLE ItemGeo(
	itemID INT NOT NULL,
	pos POINT NOT NULL, /*Columns in spatial indexes must be declared NOT NULL*/
	PRIMARY KEY (itemID) 
)ENGINE = MyISAM;

/* populate the table ItemGeo with itemId, latitude, and longitude information using INSERT query andSELECT querya*/
INSERT INTO ItemGeo SELECT Item.itemID, POINT(Item.latitude, Item.longitute) FROM Item;

/* create a spatial index on latitude and longitude.*/
ALTER TABLE ItemGeo ADD SPATIAL INDEX(pos);
