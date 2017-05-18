package edu.ucla.cs.cs144;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchRegion;
import edu.ucla.cs.cs144.SearchResult;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */

    private static Document getDocument(IndexSearcher searcher, int docId)
    throws IOException {
        return searcher.doc(docId);
    }

	public SearchResult[] basicSearch(String queryString, int numResultsToSkip, 
			int numResultsToReturn) { //throws IOException, ParseException
		// TODO: Your code here!
		IndexSearcher searcher = null;
    	QueryParser parser = null;

    	//SearchResult[] resultArray = new SearchResult[numResultsToReturn];
    	List<SearchResult> resultList = new ArrayList<SearchResult>();
    	try{
    		searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1/"))));
        	parser = new QueryParser("content", new StandardAnalyzer());

        	Query query = parser.parse(queryString);
        	TopDocs topDocs = searcher.search(query, numResultsToReturn);
        	ScoreDoc[] hits = topDocs.scoreDocs;
        	//System.out.println("hits" + hits.length);
        	if(hits != null && hits.length > 0 && numResultsToSkip < numResultsToReturn) {
        		//System.out.println("hello2");
        		for(int i = numResultsToSkip; i < hits.length; i++) {
        			//System.out.println("hello3");
        			if(true) {//hits[i] != null && resultArray[j] != null) {
     					//System.out.println("hello4");
        				Document doc = getDocument(searcher, hits[i].doc);
        				String itemID = doc.get("itemID");
        				String name = doc.get("name");
        				//System.out.println(itemID);
        				//System.out.println("hello5");
        				resultList.add(new SearchResult(itemID, name));
        			}
        		}
        	}
    	}catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
		SearchResult[] resultArray = new SearchResult[resultList.size()];
		resultArray = resultList.toArray(resultArray);
        return resultArray;
	}

	public SearchResult[] spatialSearch(String queryString, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
        // corner case
        if(numResultsToSkip >= numResultsToReturn) {
            System.out.println("Error: numResultsToSkip should be smaller than numResultsToReturn!");
            return null;
        } 
        // create a connection to the database to retrieve Items from MySQL
        Connection conn = null;
        try {
            conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    // TODO: find all results satisfying the spacial constaints
    // use JDBC API to retrieve MySQL data from Java.
        Statement stmt = null;
        ResultSet rs = null;
        List<String> itemIDList = new ArrayList(); // use itemIDList to store all itemIDs that satisfying the spacial constaints
        // get lx, ly, rx, ry from region object
        double lx = region.getLx();
        double ly = region.getLy();
        double rx = region.getRx();
        double ry = region.getRy();

        try{
             stmt = conn.createStatement(); // statement object for queries
             rs = stmt.executeQuery("SELECT itemID FROM ItemGeo WHERE MBRContains(GeomFromText('Polygon(("+lx+" "+ly+","+rx+" "+ly+","+rx+" "+ry+","+lx+" "+ry+","+lx+" "+ly+"))'), pos);");
             while(rs.next()) {
                itemIDList.add(rs.getString("itemID"));
             }
        }catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
    /********************************************************************/
        IndexSearcher searcher = null;
        QueryParser parser = null;
        // List<SearchResult> object for SearchResult objects
        List<SearchResult> resultList = new ArrayList<SearchResult>();
        try{
            searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("/var/lib/lucene/index1/"))));
            parser = new QueryParser("content", new StandardAnalyzer());

            Query query = parser.parse(queryString);
            
            TopDocs topDocs = searcher.search(query, numResultsToReturn);
            ScoreDoc[] hits = topDocs.scoreDocs;
            
            if(hits != null && hits.length > numResultsToSkip) {
                for(int i = 0; i < hits.length ; i++) { // numResultsToSkip numResultsToReturn 之后解决numResultsToSkip < numResultsToReturn)
                    Document doc = getDocument(searcher, hits[i].doc);
                    String itemID = doc.get("itemID");
                    String name = doc.get("name");
                    if(itemIDList.contains(itemID)) { //TODO: if itemID in the results of spacial search
                        resultList.add(new SearchResult(itemID, name));
                    }
                }
            }
        }catch (ParseException ex) {
            System.err.println("ParseException: " + ex.getMessage());
        }catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }
        List<SearchResult> finalResults = new ArrayList<SearchResult>();
        //System.out.println("number of results: "+resultList.size());
        if(resultList.size() > numResultsToSkip) {
            for(int j = numResultsToSkip; j < resultList.size() && j < numResultsToReturn; j++) {
                finalResults.add(resultList.get(j)); // 
            }
        }
    /********************************************************************/
        // close the database connection
       try {
           conn.close();
       } catch (SQLException ex) {
           System.out.println(ex);
       }
       SearchResult[] resultArray = new SearchResult[finalResults.size()];
       return finalResults.toArray(resultArray); //resultArray
	}


	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
        // create a connection to the database to retrieve Items from MySQL
        Connection conn = null;
        try {
            conn = DbManager.getConnection(true);
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        // use JDBC API to retrieve MySQL data from Java.
        Statement stmt1 = null;
        Statement stmt2 = null;
        Statement stmt3 = null;
        Statement stmt4 = null;
        Statement stmt5 = null;
        Statement stmt6 = null;
        ResultSet rs1 = null; // results of query on Item(#itemID#, name, currently, first_bid, number_of_bids, addr, latitude?, longitute?, country, started, ends, seller_id, description)
        ResultSet rs2 = null; // results of query on ItemBuyPrice(#itemID#, buy_price?)
        ResultSet rs3 = null; // results of query on ItemCategory(#itemID#, category)
        ResultSet rs4 = null; // results of query on ItemBid(#itemID#, #bidder_id#, #time#, amount)
        ResultSet rs5 = null; // results of query on Bidder(rating, #uid#, location?, country?)
        ResultSet rs6 = null; // results of query on Seller(rating, #uid#)
        StringBuilder sb = new StringBuilder();

        try{ 
            // use six statements for six ResultSet objects to avoid SQLException:  Operation not allowed after ResultSet closed
             stmt1 = conn.createStatement(); // statement object for queries 
             stmt2 = conn.createStatement(); // statement object for queries
             stmt3 = conn.createStatement(); // statement object for queries
             stmt4 = conn.createStatement(); // statement object for queries
             stmt5 = conn.createStatement(); // statement object for queries
             stmt6 = conn.createStatement(); // statement object for queries
             rs1 = stmt1.executeQuery("SELECT * FROM Item WHERE itemID ="+itemId);
             while(rs1.next()) {
                sb.append("<Item ItemID=\""+itemId+"\">");
                sb.append("\n");
                sb.append("<Name>"+rs1.getString("name")+"</Name>");
                sb.append("\n");
                rs2 = stmt2.executeQuery("SELECT category FROM ItemCategory WHERE itemID ="+itemId);
                while(rs2.next()) {
                    sb.append("<Category>"+rs2.getString("category")+"</Category>");
                    sb.append("\n");
                }
                sb.append("<Currently>"+"$"+rs1.getString("currently")+"</Currently>");
                sb.append("\n");
                rs3 = stmt3.executeQuery("SELECT buy_price FROM ItemBuyPrice WHERE itemID ="+itemId);
                if(rs3.first()) {   // if the first row of ResultSet is valid(not null)
                    while(rs3.next()) {
                    sb.append("<Buy_Price>"+"$"+rs3.getString("buy_price")+"</Buy_Price>");
                    sb.append("\n");
                    }
                }
                sb.append("<First_Bid>"+"$"+rs1.getString("first_bid")+"</First_Bid>");
                sb.append("\n");
                sb.append("<Number_of_Bids>"+rs1.getString("number_of_bids")+"</Number_of_Bids>");
                sb.append("\n");
                rs4 = stmt4.executeQuery("SELECT bidder_id, time, amount FROM ItemBid WHERE itemID ="+itemId);

                if(rs4.first()) {
                    sb.append("<Bids>");
                    sb.append("\n");
                    while(rs4.next()) {
                        sb.append("\t"+"<Bid>");
                        sb.append("\n");
                        rs5 = stmt5.executeQuery("SELECT rating, location, country FROM Bidder WHERE uid =\""+rs4.getString("bidder_id")+"\"");
                        while(rs5.next()) {
                            sb.append("\t"+"\t"+"<Bidder Rating=\""+rs5.getString("rating")+"\" "+"UserID=\""+rs4.getString("bidder_id")+"\">");
                            sb.append("\n");
                            sb.append("\t"+"\t"+"\t"+"<Location>"+rs5.getString("location")+"</Location>");
                            sb.append("\n");
                            sb.append("\t"+"\t"+"\t"+"<<Country>"+rs5.getString("country")+"</Country>");
                            sb.append("\n");
                            sb.append("\t"+"\t"+"</Bidder>");
                            sb.append("\n");
                            sb.append("\t"+"\t"+"<Time>"+timeStamp(rs4.getString("time"))+"</Time>");
                            sb.append("\n");
                            sb.append("\t"+"\t"+"<Amount>"+"$"+rs4.getString("amount")+"</Amount>");
                            sb.append("\n");
                        }
                    sb.append("\t"+"</Bid>");
                    sb.append("\n");
                    }
                }
                sb.append("</Bids>");
                sb.append("\n");
                sb.append("<Location Latitude=\""+rs1.getString("latitude")+"\" "+"Longitude=\""+rs1.getString("longitute")+"\">"+rs1.getString("addr")+"</Location>");
                sb.append("\n");
                sb.append("<Country>"+rs1.getString("country")+"</Country>");
                sb.append("\n");
                sb.append("<Started>"+timeStamp(rs1.getString("started"))+"</Started>");
                sb.append("\n");
                sb.append("<Ends>"+timeStamp(rs1.getString("ends"))+"</Ends>");
                sb.append("\n");
                rs6 = stmt6.executeQuery("SELECT rating FROM Seller WHERE uid =\""+rs1.getString("seller_id")+"\"");
                while(rs6.next()) {
                    sb.append("<Seller Rating=\""+rs6.getString("rating")+"\" "+"UserID=\""+rs1.getString("seller_id")+"\" />");
                    sb.append("\n");
                }
                sb.append("<Description>"+rs1.getString("description")+"</Description>");
                sb.append("\n");
                sb.append("</Item>");
                sb.append("\n");
             }
             sb.append("");
        }catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }
         // close the database connection
       try {
           conn.close();
       } catch (SQLException ex) {
           System.out.println(ex);
       }
		return new String(sb);
	}
	// helper function - TIMESTAMP conversion method
    private static String timeStamp(String dateString) {
        StringBuilder sb = new StringBuilder();
        try{
            // format1 : input date formate as dateString
            // format2 : output date formate
            SimpleDateFormat format1 =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 =
                new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
            // See if we can parse the 
            Date parsed = format1.parse(dateString);
            sb.append(format2.format(parsed));
        }catch (java.text.ParseException ex) {  //There is no import aliasing mechanism in Java. Import one class and use the fully qualified name for the other one
           System.out.println(ex);
       }
        return new String(sb);
    }

	public String echo(String message) {
		return message;
	}

}
