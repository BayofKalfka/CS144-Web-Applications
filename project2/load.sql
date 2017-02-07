/* load.sql
* sql script file created by Xiongfeng Hu
* 2016-02-02 
/*
* Seller(rating, #uid#)
* Bidder(rating, #uid#, location?, country?)
* Item(#itemID#, name, currently, first_bid, number_of_bids, addr, latitude?, longitute?, country, started, ends, seller_id, description)
* ItemBid(#itemID#, #bidder_id#, #time#, amount)
* ItemCategory(#itemID#, category)
* ItemBuyPrice(#itemID#, buy_price?)
OPTIONALLY ENCLOSED BY '"'
*/
/* Load data to table Seller */
LOAD DATA LOCAL INFILE './new_seller.txt' INTO TABLE Seller
FIELDS TERMINATED BY '\t';
/* Load data to table Bidder */
LOAD DATA LOCAL INFILE './new_bidder.txt' INTO TABLE Bidder
FIELDS TERMINATED BY '\t';
/* Load data to table Item */
LOAD DATA LOCAL INFILE './new_item.txt' INTO TABLE Item
FIELDS TERMINATED BY '\t';
/* Load data to table  ItemBid */
LOAD DATA LOCAL INFILE './new_bid.txt' INTO TABLE  ItemBid
FIELDS TERMINATED BY '\t';
/* Load data to table ItemCategory */
LOAD DATA LOCAL INFILE './new_category.txt' INTO TABLE ItemCategory
FIELDS TERMINATED BY '\t';
/* Load data to table Seller */
LOAD DATA LOCAL INFILE './new_buy_price.txt' INTO TABLE ItemBuyPrice
FIELDS TERMINATED BY '\t';









