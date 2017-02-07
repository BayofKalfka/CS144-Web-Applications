/*
* Seller(rating, #uid#)
* Bidder(rating, #uid#, location?, country?)
* Item(#itemID#, name, currently, first_bid, number_of_bids, addr, latitude?, longitute?, country, started, ends, seller_id, description)
* ItemBid(#itemID#, #bidder_id#, #time#, amount)
* ItemCategory(#itemID#, category)
* ItemBuyPrice(#itemID#, buy_price?)
**************************************************
* TIMESTAMP type :
*Item: started, ends - done
*Bid: time - done
--------------------------------------------------
* DECIMAL(8,2) :
*Item: currently, first_bid - done
*Bid: amount - done
*BuyPrice: buy_price - done
--------------------------------------------------
1.	specify a PRIMARY KEY for each table that has at least one key.
2.	SQL type
3.	NOT NULL
4.	reference
5.	foreign key
*/

/* Create a table called Seller in the CS144 database */
CREATE TABLE Seller(
	rating INT NOT NULL,
	uid VARCHAR(40) NOT NULL,
	PRIMARY KEY (uid) 
);
/* Create a table called Bidder in the CS144 database */
CREATE TABLE Bidder(
	rating INT NOT NULL, 
	uid VARCHAR(40) NOT NULL,
	location VARCHAR(40), 
	country VARCHAR(40),
	PRIMARY KEY(uid)
);
/* Create a table called Item in the CS144 database */
CREATE TABLE Item(
	itemID INT NOT NULL, 
	name VARCHAR(40) NOT NULL, 
	currently DECIMAL(8,2) NOT NULL, 
	first_bid DECIMAL(8,2) NOT NULL, 
	number_of_bids INT NOT NULL, 
	addr VARCHAR(40),
	latitude INT, 
	longitute INT, 
	country VARCHAR(40) NOT NULL, 
	started TIMESTAMP, 
	ends TIMESTAMP, 
	seller_id VARCHAR(40) NOT NULL, 
	description VARCHAR(4000),
	PRIMARY KEY(itemID)
);
/* Create a table called ItemBid in the CS144 database */
CREATE TABLE ItemBid(
	itemID INT NOT NULL, 
	bidder_id VARCHAR(40) NOT NULL, 
	time TIMESTAMP, 
	amount DECIMAL(8,2),
	PRIMARY KEY(itemID, bidder_id, time)
);
/* Create a table called ItemCategory in the CS144 database */
CREATE TABLE ItemCategory(
	itemID INT NOT NULL, 
	category VARCHAR(40),
	PRIMARY KEY(itemID, category)
);
/* Create a table called ItemBuyPrice in the CS144 database */
CREATE TABLE ItemBuyPrice(
	itemID INT NOT NULL,
	buy_price DECIMAL(8,2),
	PRIMARY KEY(itemID)
);












