package edu.ucla.cs.cs144;

import java.util.*;
import java.io.IOException;
import java.io.StringReader;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.*;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;


public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        // create an  AuctionSearch object
        AuctionSearch as = new AuctionSearch();
        // get the parameter from web page
        String itemId = request.getParameter("id");  
        String query = request.getParameter("q"); 
        // get the item information
        String itemXML = as.getXMLDataForItemId(itemId);

        DocumentBuilder builder = null;
        Document doc = null;
        try{
        	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        	builder = factory.newDocumentBuilder();
        	InputSource input = new InputSource(new StringReader(itemXML));
       	 	doc = builder.parse(input);
       	}catch (SAXException e) {
            System.out.println("SAXException !!");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }

        String item_name = doc.getElementsByTagName("Name").item(0).getTextContent(); 
        String currently = doc.getElementsByTagName("Currently").item(0).getTextContent();
        String first_bid = doc.getElementsByTagName("First_Bid").item(0).getTextContent();
        String num_of_bid = doc.getElementsByTagName("Number_of_Bids").item(0).getTextContent();
        String location = doc.getElementsByTagName("Location").item(0).getTextContent();
        String country = doc.getElementsByTagName("Country").item(0).getTextContent();
        String started = doc.getElementsByTagName("Started").item(0).getTextContent();
        String ends = doc.getElementsByTagName("Ends").item(0).getTextContent();
        String description = doc.getElementsByTagName("Description").item(0).getTextContent();
 		String buy_price = null;

		String seller_id = null;
        String seller_rating = null;
        String latitude = null;
        String longitude = null;
        latitude = new String("hello001");
        longitude = new String("hello002");
        List<String> categoryList = new ArrayList<String>();
		List<Bid> bidList = new ArrayList<Bid>();
            // #1. get buy_price info - null check first
            if(doc.getElementsByTagName("Buy_Price") != null && doc.getElementsByTagName("Buy_Price").item(0) != null) {
                buy_price = new String(doc.getElementsByTagName("Buy_Price").item(0).getTextContent());
            }
			// #2. get seller info - seller_id, seller_rating
		org.w3c.dom.NamedNodeMap seller_attrib = doc.getElementsByTagName("Seller").item(0).getAttributes();
		if(seller_attrib != null && seller_attrib.getLength() > 0) {
			seller_rating = new String(seller_attrib.item(0).getNodeValue());
       		seller_id = new String(seller_attrib.item(1).getNodeValue());
        }
			// #3. get latitude, longitude info
        	// dumps out attributes of this location node 
        org.w3c.dom.NamedNodeMap lattrib = doc.getElementsByTagName("Location").item(0).getAttributes();
       	if(lattrib != null && lattrib.getLength() == 2) { // has both latitude attrib and longitude attrib
       		latitude = new String(lattrib.item(0).getNodeValue());
       		longitude = new String(lattrib.item(1).getNodeValue());
        } else if(lattrib != null && lattrib.getLength() == 1) {
            if(lattrib.item(0).getNodeName().equals("Latitude")) {  // only has latitude attrib
               latitude = new String(lattrib.item(0).getNodeValue());
       			longitude = new String(""); 
        	}else if(lattrib.item(0).getNodeName().equals("Longitude")) {  // only has longitude attrib
                latitude = new String("");
       			longitude = new String(lattrib.item(0).getNodeValue());
            }
        }else {  // has neither latitude attrib nor longitude attrib
            latitude = new String("NaN");
       		longitude = new String("NaN");
        }
        	// #4. get bid history - use java bean
        if(!num_of_bid.equals("0")) { 
        	getBidInfo(doc, bidList);    
        }
        	// #5. get category info
        	getCategoryInfo(doc, categoryList);
        // pass data to JSP
        request.setAttribute("itemId", itemId); // done
        request.setAttribute("itemName", item_name);  // done
        request.setAttribute("category", categoryList); // done
        request.setAttribute("currently", currently); 
        request.setAttribute("firstBid", first_bid); // done
        request.setAttribute("buyPrice", buy_price); // done
        request.setAttribute("num_of_bid", num_of_bid);  // done
        request.setAttribute("location", location); //done
        request.setAttribute("latitude", latitude); //done
        request.setAttribute("longitude", longitude); //done
        request.setAttribute("country", country); // done
        request.setAttribute("started", started); // done
        request.setAttribute("ends", ends); // done
        request.setAttribute("sellerId", seller_id); // done
        request.setAttribute("sellerRating", seller_rating); // done
        request.setAttribute("description", truncate(description, 4000)); // done
        request.setAttribute("bids", bidList); // done
        request.getRequestDispatcher("/itemDetails.jsp").forward(request, response);  
    }
    // helper # 1 - getBidInfo()
    public static void getBidInfo(Document doc, List<Bid> bidList) {
    	// get child elements - Bids
        org.w3c.dom.NodeList bids = doc.getElementsByTagName("Bid");
        for(int i = 0; i < bids.getLength(); i++) {
            // cur Bid
            Bid curBid = new Bid();
            NodeList bidChildren = bids.item(i).getChildNodes();
            for(int j = 0; j < bidChildren.getLength(); j++) { //Node bidChildren(j) : bidChildren(j)ren
                // add bidderRating, bidderId to curBid
                if(bidChildren.item(j).getNodeName().equals("Bidder")) {
                    // get attributes - bidder_id, bidder_rating
                    org.w3c.dom.NamedNodeMap bidder_attrib = bidChildren.item(j).getAttributes();
                    if(bidder_attrib != null && bidder_attrib.getLength() > 0) {
                        String bidder_rating = new String(bidder_attrib.item(0).getNodeValue());
                        String bidder_id = new String(bidder_attrib.item(1).getNodeValue());
                        curBid.setBidderId(bidder_id );
                        curBid.setBidderRating(bidder_rating);
                     }
                     // get child nodes - bidder_location, bidder_country
                     String bidder_location = new String(bidChildren.item(j).getFirstChild().getNextSibling().getTextContent());
                     String bidder_country = new String(bidChildren.item(j).getLastChild().getTextContent());
                     curBid.setBidderLocation(bidder_location);
                     curBid.setBidderCountry(bidder_country);
                }
                // add bidTime to curBid
                if(bidChildren.item(j).getNodeName().equals("Time")) {
                    curBid.setBidTime(bidChildren.item(j).getTextContent());
                }
                // add bid Amount to curBid
                if(bidChildren.item(j).getNodeName().equals("Amount")) {
                    curBid.setBidAmount(bidChildren.item(j).getTextContent());
                }
            }
            bidList.add(curBid);
        }
            
    }
    // helper #2 - getCategoryInfo
    public static void getCategoryInfo(Document doc, List<String> categoryList) {
    	NodeList categories = doc.getElementsByTagName("Category");
        for(int i = 1; i < categories.getLength(); i++) {
            categoryList.add(categories.item(i).getTextContent());
        }
    }
    // heper #3 - String truncate method
    public static String truncate(String value, int length) {
        // Ensure String length is longer than requested size.
        if (value.length() > length) {
            return value.substring(0, length);
        } else {
            return value;
        }
    }

}
