/* queries.sql
* sql script file created by Xiongfeng Hu
* 2016-02-02 
/*
* Seller(rating, #uid#)
* Bidder(rating, #uid#, location?, country?)
* Item(#itemID#, name, currently, first_bid, number_of_bids, addr, latitude?, longitute?, country, started, ends, seller_id, description)
* ItemBid(#itemID#, #bidder_id#, #time#, amount)
* ItemCategory(#itemID#, category)
* ItemBuyPrice(#itemID#, buy_price?)
*/

/* 1. Find the number of users in the database. 13422 DONE*/
SELECT COUNT(*)
FROM (
	SELECT uid FROM Seller as s_id
UNION
	SELECT uid FROM Bidder as b_id	
) as u;
/* 2. Find the number of items in "New York" 103  DONE*/
SELECT COUNT(DISTINCT itemID)
FROM Item
WHERE BINARY addr ="New York";
/* 3. Find the number of auctions belonging to exactly four categories. 8365 DONE*/
SELECT COUNT(*)
FROM (
	SELECT itemID, COUNT(*) c
	FROM ItemCategory
	GROUP BY itemID
	HAVING c = 4
) item_4;
/* 4. Find the ID(s) of current (unsold) auction(s) with the highest bid.
* Remember that the data was captured at the point in time December 20th, 2001,
* one second after midnight, so you can use this time point to decide which auction(s) are current. 
* Pay special attention to the current auctions without any bid.
number_of_bids can not be 0
1046740686 DONE
*/

SELECT itemID
FROM Item
WHERE ends >= '2001-12-20 00:00:01' AND number_of_bids <> 0
ORDER BY currently DESC
LIMIT 1;

/* 5. Find the number of sellers whose rating is higher than 1000. 3130 DONE*/
SELECT COUNT(*) FROM Seller s WHERE s.rating > 1000;
/* 6. Find the number of users who are both sellers and bidders. 6717 6717 */
SELECT COUNT(*)
FROM Seller
INNER JOIN Bidder
ON Seller.uid = Bidder.uid;
/* 7. Find the number of categories that include at least one item with a bid of more than $100. 150 DONE */

SELECT COUNT(*)
FROM (
SELECT category
FROM ItemCategory ic
WHERE ic.itemID IN (
SELECT itemID
FROM Item
WHERE Item.currently > 100 AND Item.number_of_bids <> 0
)
GROUP BY category
) combine;




