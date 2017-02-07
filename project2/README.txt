1. List of relations

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

2.Nontrivial functional dependencies (excluding keys)
Seller: no nontrivial functional dependencies
Bidder: no nontrivial functional dependencies
Item: no nontrivial functional dependencies
ItemBid: no nontrivial functional dependencies
ItemCategory: no nontrivial functional dependencies
ItemBuyPrice: no nontrivial functional dependencies

3.BCNF validation
All relations are in BCNF.

4.4NF validation
All relations are in 4NF

