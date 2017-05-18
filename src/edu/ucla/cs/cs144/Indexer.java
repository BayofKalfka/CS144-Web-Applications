package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLWarning;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    /*********************************************/
    // add code here
    public static IndexWriter getIndexWriter(IndexWriter indexWriter, boolean create, String url) throws IOException, SQLException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File(url));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        }
        return indexWriter;
   }
   
   public static void closeIndexWriter(IndexWriter indexWriter) throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
   /***************************************************/
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}


	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */
        // use JDBC API to retrieve MySQL data from Java.
        Statement stmt1 = null;
        Statement stmt2 = null;
        //Statement stmt2 = conn.createStatement();// statement object for table ItemCategory
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        try {
            stmt1 = conn.createStatement(); // statement object for queries on table Item
            stmt2 = conn.createStatement();
            //rs1 = stmt1.executeQuery("SELECT Item.itemID, Item.name, ItemCategory.category, Item.description FROM Item INNER JOIN ItemCategory ON Item.itemID = ItemCategory.itemID");
            //ResultSet rs2 = stmt2.executeQuery("SELECT itemID, category FROM ItemCategory");
            rs1 = stmt1.executeQuery("SELECT itemID, name, description FROM Item");
            SQLWarning warn1 = stmt1.getWarnings();
            if (warn1 != null) System.out.println("Message: " + warn1.getMessage());
            SQLWarning warning1 = rs1.getWarnings();
            if (warning1 != null) System.out.println("Message: " + warning1.getMessage());
        }catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }

        // SQLWarning warn2 = stmt2.getWarnings();
        // if (warn2 != null) System.out.println("Message: " + warn2.getMessage());
        //  SQLWarning warning2 = rs2.getWarnings();
        // if (warning2 != null) System.out.println("Message: " + warning2.getMessage());
         // use Lucene IndexWriter and Document classes to create inversed indexes and populate it with mysql CS144 data
        IndexWriter indexWriter = null;
        try{
            indexWriter = getIndexWriter(indexWriter, true, "/var/lib/lucene/index1/"); // inversed indexes dir: /var/lib/lucene/index1/
            indexWriter.deleteAll();
            while(rs1.next()) {
                rs2 = stmt2.executeQuery("SELECT category FROM ItemCategory WHERE itemID = " + rs1.getString("itemID"));
                StringBuilder category = new StringBuilder();
                while(rs2.next()) {
                    category.append(rs2.getString("category"));
                    category.append(" "); // do not forget to use space seperator 
                }
                Document doc = new Document();
                doc.add(new StringField("itemID", rs1.getString("itemID"), Field.Store.YES)); 
                doc.add(new StringField("name", rs1.getString("name"), Field.Store.YES));
                //doc.add(new StringField("category", rs1.getString(3), Field.Store.NO));
                doc.add(new StringField("description", rs1.getString("description"), Field.Store.NO));
                String fullSearchableText = rs1.getString("name") + " " + rs1.getString("description") + " " + new String(category);
                doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
                indexWriter.addDocument(doc);
            }
            closeIndexWriter(indexWriter);
        }catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }catch (IOException ex) {
            System.err.println("IOException: " + ex.getMessage());
        }

        // close the database connection
	   try {
	       conn.close();
	   } catch (SQLException ex) {
	       System.out.println(ex);
	   }
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }   
}
