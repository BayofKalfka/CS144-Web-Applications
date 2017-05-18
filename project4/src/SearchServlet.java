package edu.ucla.cs.cs144;

import java.util.*;
import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        // create an  AuctionSearch object
        AuctionSearch as = new AuctionSearch();

        // get the query from web client 
        String query = request.getParameter("q");    
        int numResultsToSkip = 0;
        int numResultsToReturn = 0;
        if(query != null) {
        	numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip")); 
        	numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn")); 
        }
        // submit a keyword query and get search results
        SearchResult[] totalResults = as.basicSearch(query, 0, Integer.MAX_VALUE);
        SearchResult[] basicResults = as.basicSearch(query, numResultsToSkip, numResultsToReturn);
        List<SearchResult> result = new ArrayList<SearchResult>();
        // pass search results to JSP
        request.setAttribute("numOfResults", totalResults.length);
        request.setAttribute("results", basicResults);
        request.getRequestDispatcher("/searchResults.jsp").forward(request, response);  
    }
}
