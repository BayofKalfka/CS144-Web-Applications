#!/bin/bash

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files

ant 
ant run-all

# If the Java code does not handle duplicate removal, do this now
#sort test.txt | uniq | awk 'NF' > output.txt 
sort seller.txt | uniq | awk 'NF' > new_seller.txt 
sort bidder.txt | uniq | awk 'NF' > new_bidder.txt 
sort item.txt | uniq | awk 'NF' > new_item.txt 
sort bid.txt | uniq | awk 'NF' > new_bid.txt 
sort category.txt | uniq | awk 'NF' > new_category.txt 
sort buy_price.txt | uniq | awk 'NF' > new_buy_price.txt 
# Run the load.sql batch file to load the data
mysql CS144 < load.sql

# Remove all temporary files
ant clean
rm seller.txt
rm bidder.txt
rm item.txt
rm bid.txt
rm category.txt 
rm buy_price.txt
rm new_seller.txt
rm new_bidder.txt
rm new_item.txt
rm new_bid.txt
rm new_category.txt 
rm new_buy_price.txt


