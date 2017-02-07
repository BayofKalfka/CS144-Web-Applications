/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;


public class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
             e.printStackTrace();
             System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */

        try{
            // create five .txt files to store relational tuples
            // create five File objs for .txt files
            File seller = new File("seller.txt"); // Seller(rating, #uid#)
            File bidder = new File("bidder.txt"); // Bidder(rating, #uid#, location?, country?)
            File item = new File("item.txt"); // Item(#itemID#, name, currently, first_bid, number_of_bids, addr, latitude?, longitute?, country, started, ends, seller_id, description)
            File bid = new File("bid.txt"); // Bid(#itemID#, #bidder_id#, #time#, amount)
            File category = new File("category.txt"); // Category(#itemID#, category)
            //File location = new File("location.txt"); // Location(addr, #latitude#?, #longitude#?)
            File buy_price = new File("buy_price.txt"); // BuyPrice(#itemID#, buy_price?)
            // if the file does not exist, create one
            if(!seller.exists()) {
                seller.createNewFile();
             }
             if(!bidder.exists()) {
                bidder.createNewFile();
             }
             if(!item.exists()) {
                item.createNewFile();
             }
             if(!bid.exists()) {
                bid.createNewFile();
             }
             if(!category.exists()) {
                category.createNewFile();
             }
             // if(!location.exists()) {
             //    location.createNewFile();
             // }
             if(!buy_price.exists()) {
                buy_price.createNewFile();
             }
             // create five FileWritter objs for tuple writing
            FileWriter fw_seller = new FileWriter(seller, true); // true,  if the second argument is true, then bytes will be written to the end of the file rather than the beginning.
            FileWriter fw_bidder = new FileWriter(bidder, true);
            FileWriter fw_item = new FileWriter(item, true);
            FileWriter fw_bid = new FileWriter(bid, true);
            FileWriter fw_category = new FileWriter(category, true);
            //FileWriter fw_location = new FileWriter(location, true);
            FileWriter fw_buy_price = new FileWriter(buy_price, true);
             /**************************************************************/
            // traverse all nodes in xml dom
            recursiveDescent(doc, 0, fw_seller, fw_bidder, fw_item, fw_bid, fw_category, fw_buy_price);
            // close FileWritters
            fw_seller.close(); 
            fw_bidder.close();
            fw_item.close();
            fw_bid.close();
            fw_category.close();
            //fw_location.close();
            fw_buy_price.close();
        }
        catch (IOException e) {
             e.printStackTrace();
             System.exit(3);
        }
        catch(ParseException pe) {
            System.out.println("ERROR: Cannot parse dateString");
        }
    }
    
    //===================================================================
    // healper - read xml data to loading datasets
    public static void recursiveDescent(Node n, int level, FileWriter fw_seller, FileWriter fw_bidder, FileWriter fw_item,
     FileWriter fw_bid, FileWriter fw_category, FileWriter fw_buy_price)
     throws IOException, ParseException {
        // current level
        
        // dump out node name, type, and value
        String ntype = typeName[n.getNodeType()];
        String nname = n.getNodeName();
        String nvalue = n.getNodeValue();

        // transforms xml data into relational schema
        // regognizes current node and reads current tuple to corresponing file
        if(nname.equals("Item")) {  
            org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
            if(nattrib != null && nattrib.getLength() > 0) {
                 String itemID = new String(nattrib.item(0).getNodeValue()); // new - String object initialization 
                addItemTuples(n, itemID, fw_item);  // done
                addCategoryTuples(n, itemID, fw_category); // done
                addBidTuples(n, itemID, fw_bid); // done
                addBuyPriceTuples(n, itemID, fw_buy_price); // done
            }
        }
        else if(nname.equals("Seller")) { // String comparison, use .equals() rather than  ==
            addSellerTuples(n, fw_seller); //done
        }
        else if(nname.equals("Bidder")) {
            addBidderTuples(n, fw_bidder); //done
        }
        // else if(nname.equals("Location")) {
        //     addLocationTuples(n, fw_location);
        // }
        // dump out attributes if any
        org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
        if(nattrib != null && nattrib.getLength() > 0)
            for(int i=0; i<nattrib.getLength(); i++)
                recursiveDescent(nattrib.item(i),  level+1,  fw_seller, fw_bidder, fw_item, fw_bid, fw_category, fw_buy_price);
        
        // now walk through its children list
        org.w3c.dom.NodeList nlist = n.getChildNodes();
        
        for(int i=0; i<nlist.getLength(); i++)
            recursiveDescent(nlist.item(i), level+1, fw_seller, fw_bidder, fw_item, fw_bid, fw_category, fw_buy_price);
    }

    // helper - writes item tuples into item.txt
    // Item(itemID, name, currently, first_bid, number_of_bids, addr, latitude, longitute, country, started, ends, seller_id, description)
    public static void addItemTuples(Node n, String itemID, FileWriter fw_item) throws IOException, ParseException{
        // write itemID into file
        fw_item.write(itemID); 
        fw_item.write("\t");
        // now walk through its children list
        org.w3c.dom.NodeList nlist = n.getChildNodes();
            if(nlist != null && nlist.getLength() > 0) {
                for(int i=0; i < nlist.getLength(); i++) {
                    if(nlist.item(i).getNodeName().equals("Seller")) {
                        // write seller_id into file
                        org.w3c.dom.NamedNodeMap seller_attrib = nlist.item(i).getAttributes();
                        if(seller_attrib != null && seller_attrib.getLength() > 0) {
                            for(int j =0; j < seller_attrib.getLength(); j++) {
                                if(seller_attrib.item(j) != null && seller_attrib.item(j).getNodeName().equals("UserID")) {
                                    fw_item.write(seller_attrib.item(j).getNodeValue());
                                    fw_item.write("\t"); // uses "\t" to seperatre attributes
                                }
                            }
                        }
                    }else if(nlist.item(i).getNodeName().equals("Location")) {
                        // get first node and write addr into file
                        if(nlist.item(i).getFirstChild() != null) {
                            fw_item.write(nlist.item(i).getFirstChild().getNodeValue());
                            fw_item.write("\t"); // uses "\t" to seperatre attributes
                        }
                        // write latitude, longitude into file
                        // dumps out attributes of this location node 
                        org.w3c.dom.NamedNodeMap lattrib = nlist.item(i).getAttributes();
                        if(lattrib != null && lattrib.getLength() == 2) { // has both latitude attrib and longitude attrib
                            for(int j =0; j < lattrib.getLength(); j++) {
                                fw_item.write(lattrib.item(j).getNodeValue());
                                fw_item.write("\t"); // uses "\t" to seperatre attributes
                            }
                        }else if(lattrib != null && lattrib.getLength() == 1) {
                            if(lattrib.item(0).getNodeName().equals("Latitude")) {  // only has latitude attrib
                                    fw_item.write(lattrib.item(0).getNodeValue());
                                    fw_item.write("\t"); // uses "\t" to seperatre attributes
                                    fw_item.write("");
                                    fw_item.write("\t"); // uses "\t" to seperatre attributes
                            }else if(lattrib.item(0).getNodeName().equals("Longitude")) {  // only has longitude attrib
                                fw_item.write("");
                                fw_item.write("\t"); // uses "\t" to seperatre attributes
                                fw_item.write(lattrib.item(0).getNodeValue());
                                fw_item.write("\t"); // uses "\t" to seperatre attributes
                            }
                        }else {  // has neither latitude attrib nor longitude attrib
                            fw_item.write("");
                            fw_item.write("\t"); // uses "\t" to seperatre attributes
                            fw_item.write("");
                            fw_item.write("\t"); // uses "\t" to seperatre attributes
                        }
                    }else if(nlist.item(i).getNodeName().equals("Currently") || nlist.item(i).getNodeName().equals("First_Bid")) {
                        // get the text child of this node
                        // and strip the $ symbol to XXXX.xx formate
                        org.w3c.dom.NodeList childList = nlist.item(i).getChildNodes();
                        if(childList != null && childList.getLength() > 0) {
                            fw_item.write(strip(childList.item(0).getNodeValue()));
                            fw_item.write("\t"); // uses "\t" to seperatre attributes
                        }
                    }else if(nlist.item(i).getNodeName().equals("Started") || nlist.item(i).getNodeName().equals("Ends")) {
                        // gets the text child of this node
                        // and tranform the date string to SQL TIMESTAMP type
                        org.w3c.dom.NodeList childList = nlist.item(i).getChildNodes();
                        if(childList != null && childList.getLength() > 0) {
                            fw_item.write(timeStamp(childList.item(0).getNodeValue()));
                            fw_item.write("\t"); // uses "\t" to seperatre attributes
                        }
                    }else if (typeName[nlist.item(i).getNodeType()].equals("Element") 
                        && !nlist.item(i).getNodeName().equals("Category") 
                        && !nlist.item(i).getNodeName().equals("Bids")
                        && !nlist.item(i).getNodeName().equals("Buy_Price")) {
                        // get the text child of this node
                        org.w3c.dom.NodeList childList = nlist.item(i).getChildNodes();
                        if(childList != null && childList.getLength() > 0) {
                            fw_item.write(truncate(childList.item(0).getNodeValue(), 4000));
                            fw_item.write("\t"); // uses "\t" to seperatre attributes
                        }
                    }
                }
            }
        // add line feed after reading a tuple
        fw_item.write(System.getProperty( "line.separator" ));   
    }
    // helper - writes category tuples into category.txt
    public static void addCategoryTuples(Node n, String itemID, FileWriter fw_category) throws IOException {
        // walk through its children list and find all category nodes
        org.w3c.dom.NodeList nlist = n.getChildNodes();
            if(nlist != null && nlist.getLength() > 0) {
                for(int j=0; j < nlist.getLength(); j++) {
                    if(nlist.item(j).getNodeName().equals("Category")) {
                        // write itemID into file
                        fw_category.write(itemID);
                        fw_category.write("\t"); // uses "\t" to seperatre attributes
                        // get first node and write category into file
                        if(nlist.item(j).getFirstChild() != null) {
                            fw_category.write(nlist.item(j).getFirstChild().getNodeValue());
                            fw_category.write("\t"); // uses "\t" to seperatre attributes
                        }
                        // add line feed after writing each tuple
                        fw_category.write(System.getProperty( "line.separator" ));   
                    }
                }
            }
    }
    // helper - writes bid tuples into bid.txt
    public static void addBidTuples(Node n, String itemID, FileWriter fw_bid) throws IOException, ParseException {
        //(getElementsByTagNameNR(n, "Bids"));
        //return (Element) child;
        // get child node - bids
        Node child = n.getFirstChild();
        Node bids = null;
        while (child != null) {
            if (child.getNodeName().equals("Bids")) {
                bids = child;
                break;
            }
             child = child.getNextSibling();
        }
        // check whether this bids has any children
        if(!bids.hasChildNodes()) {
            // if bids has no children
            fw_bid.write(itemID);
            fw_bid.write("\t"); // uses "\t" to seperatre attributes
            fw_bid.write("");
            fw_bid.write("\t"); // uses "\t" to seperatre attributes
            fw_bid.write("");
            fw_bid.write("\t"); // uses "\t" to seperatre attributes
            fw_bid.write("");
            fw_bid.write("\t"); // uses "\t" to seperatre attributes
            // add line feed after reading a tuple
            fw_bid.write(System.getProperty( "line.separator" ));   
        }else {
            // now walk through its children list
            org.w3c.dom.NodeList bidlist = bids.getChildNodes();
            if(bidlist != null && bidlist.getLength() > 0) {
                for(int i =0; i < bidlist.getLength(); i++) {
                    // write itemID of this tuple into file
                    fw_bid.write(itemID);
                    fw_bid.write("\t"); // uses "\t" to seperatre attributes
                    Node bidChild = bidlist.item(i).getFirstChild();
                    while (bidChild != null) {
                        if (bidChild.getNodeName().equals("Bidder")) {
                            // write second attribute bidder_id into file
                            org.w3c.dom.NamedNodeMap bidderAttrib = bidChild.getAttributes();
                            fw_bid.write(bidderAttrib.item(1).getNodeValue());
                            fw_bid.write("\t"); // uses "\t" to seperatre attributes
                            bidChild = bidChild.getNextSibling();
                        }else {
                            // write time and amount into file
                            if(bidChild.getFirstChild() != null) {
                                if(bidChild.getNodeName().equals("Time")) {
                                    fw_bid.write(timeStamp(bidChild.getFirstChild().getNodeValue()));
                                    fw_bid.write("\t"); // uses "\t" to seperatre attributes
                                }else if(bidChild.getNodeName().equals("Amount")) {
                                    fw_bid.write(strip(bidChild.getFirstChild().getNodeValue()));
                                    fw_bid.write("\t"); // uses "\t" to seperatre attributes
                                }
                            }
                            bidChild = bidChild.getNextSibling();
                        }
                    }
                    // add line feed after reading a tuple
                    fw_bid.write(System.getProperty( "line.separator" ));   
                }
            }
        }
    }
    // helper - writes seller(uid, rating) tuples into seller.txt
    public static void addSellerTuples(Node n, FileWriter fw_seller) throws IOException {
        // dumps out attributes of this seller node 
        org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
            if(nattrib != null && nattrib.getLength() > 0) {
                 for(int i =0; i < nattrib.getLength(); i++) {
                    fw_seller.write(nattrib.item(i).getNodeValue());
                    fw_seller.write("\t"); // uses "\t" to seperatre attributes
                 }
            }
        // adds line feed after reading a tuple
        fw_seller.write(System.getProperty( "line.separator" ));  
    }
    // helper - write bidder tuples into bidder.txt
    public static void addBidderTuples(Node n, FileWriter fw_bidder) throws IOException {
        // dump out attributes of this bidder node
        org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
            if(nattrib != null && nattrib.getLength() > 0) {
                 for(int i =0; i < nattrib.getLength(); i++) {
                    fw_bidder.write(nattrib.item(i).getNodeValue());
                    fw_bidder.write("\t"); // use "\t" to seperatre attributes
                 }
            }
        // now walk through its children list
        org.w3c.dom.NodeList nlist = n.getChildNodes();
            if(nlist != null && nlist.getLength() > 0) {
                int l_count = 0;
                int c_count = 0;
                Node location = null;
                Node country = null;
                for(int j=0; j < nlist.getLength(); j++) {
                    if(nlist.item(j).getNodeName().equals("Location")) {
                        l_count += 1;
                        location = nlist.item(j);
                    }else if(nlist.item(j).getNodeName().equals("Country")) {
                        c_count += 1;
                        country = nlist.item(j);
                    }
                }
                if(l_count == 1 && c_count == 1) {
                    fw_bidder.write(location.getFirstChild().getNodeValue());
                    fw_bidder.write("\t"); // uses "\t" to seperatre attributes
                    fw_bidder.write(country.getFirstChild().getNodeValue());
                    fw_bidder.write("\t"); // uses "\t" to seperatre attributes
                }else if(l_count == 1 && c_count == 0) {
                    fw_bidder.write(location.getFirstChild().getNodeValue());
                    fw_bidder.write("\t"); // uses "\t" to seperatre attributes
                    fw_bidder.write("");
                    fw_bidder.write("\t"); // uses "\t" to seperatre attributes
                }else if(l_count == 0 && c_count == 1) {
                    fw_bidder.write("");
                    fw_bidder.write("\t"); // uses "\t" to seperatre attributes
                    fw_bidder.write(country.getFirstChild().getNodeValue());
                    fw_bidder.write("\t"); // uses "\t" to seperatre attributes
                }else{
                    fw_bidder.write("");
                    fw_bidder.write("\t"); // uses "\t" to seperatre attributes
                    fw_bidder.write("");
                    fw_bidder.write("\t"); // uses "\t" to seperatre attributes
                }
            }
        // add line feed after reading a tuple
        fw_bidder.write(System.getProperty( "line.separator" ));  
    }
    // // helper - write Location(addr, latitude, longitude) tuples into location.txt
    // public static void addLocationTuples(Node n, FileWriter fw_location) throws IOException {
    //     // get first node and write addr into file
    //         if(n.getFirstChild() != null) {
    //             fw_location.write(n.getFirstChild().getNodeValue());
    //             fw_location.write("\t"); // uses "\t" to seperatre attributes
    //         }
    //     // dumps out attributes of this location node 
    //     org.w3c.dom.NamedNodeMap nattrib = n.getAttributes();
    //         if(nattrib != null && nattrib.getLength() == 2) { // has both latitude attrib and longitude attrib
    //              for(int i =0; i < nattrib.getLength(); i++) {
    //                 fw_location.write(nattrib.item(i).getNodeValue());
    //                 fw_location.write("\t"); // uses "\t" to seperatre attributes
    //              }
    //         }else if(nattrib != null && nattrib.getLength() == 1) {
    //             if(nattrib.item(0).getNodeName().equals("Latitude")) {  // only has latitude attrib
    //                 fw_location.write(nattrib.item(0).getNodeValue());
    //                 fw_location.write("\t"); // uses "\t" to seperatre attributes
    //                 fw_location.write("");
    //                 fw_location.write("\t"); // uses "\t" to seperatre attributes
    //             }else if(nattrib.item(0).getNodeName().equals("Longitude")) {  // only has longitude attrib
    //                 fw_location.write("");
    //                 fw_location.write("\t"); // uses "\t" to seperatre attributes
    //                 fw_location.write(nattrib.item(0).getNodeValue());
    //                 fw_location.write("\t"); // uses "\t" to seperatre attributes
    //             }
    //         }else {  // has neither latitude attrib nor longitude attrib
    //             fw_location.write("");
    //             fw_location.write("\t"); // uses "\t" to seperatre attributes
    //             fw_location.write("");
    //             fw_location.write("\t"); // uses "\t" to seperatre attributes
    //         }
    //     // adds line feed after reading a tuple
    //     fw_location.write(System.getProperty( "line.separator" ));  
    // }
    // helper - write buy_price tuples into bidder.txt
    public static void addBuyPriceTuples(Node n, String itemID, FileWriter fw_buy_price) throws IOException {
        // walk through its children list and find all buy_price nodes
        org.w3c.dom.NodeList nlist = n.getChildNodes();
            if(nlist != null && nlist.getLength() > 0) {
                int count = 0; // counter checking whether there is buy_price node
                for(int j=0; j < nlist.getLength(); j++) {
                    if(nlist.item(j).getNodeName().equals("Buy_Price")) {
                        count += 1;
                        // write itemID into file
                        fw_buy_price.write(itemID);
                        fw_buy_price.write("\t"); // uses "\t" to seperatre attributes
                        // get first node and write category into file
                        if(nlist.item(j).getFirstChild() != null) {
                            fw_buy_price.write(strip(nlist.item(j).getFirstChild().getNodeValue()));
                            fw_buy_price.write("\t"); // uses "\t" to seperatre attributes
                        }
                        // add line feed after writing each tuple
                        fw_buy_price.write(System.getProperty( "line.separator" ));   
                    }
                }
                if(count == 0) {
                    // no buy_price for this item
                    // write itemID into file
                    fw_buy_price.write(itemID);
                    fw_buy_price.write("\t"); // uses "\t" to seperatre attributes
                    // write "" into file
                    fw_buy_price.write("");
                    fw_buy_price.write("\t"); // uses "\t" to seperatre attributes
                    // add line feed after writing each tuple
                    fw_buy_price.write(System.getProperty( "line.separator" ));  
                }
            }
    }
    /************************************************************************/
    // String truncate method
    public static String truncate(String value, int length) {
        // Ensure String length is longer than requested size.
        if (value.length() > length) {
            return value.substring(0, length);
        } else {
            return value;
        }
    }
    // SQL TIMESTAMP conversion method
    public static String timeStamp(String dateString) throws ParseException {
        // format1 : input date formate as dateString
        // format2 : output date formate
        SimpleDateFormat format1 =
                new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat format2 =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // See if we can parse the 
        Date parsed = format1.parse(dateString);
        return format2.format(parsed);
    }
   //==========================================================================
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
     
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
