
package db;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bson.Document;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;

/**
 *Function:
 *		Create tables for MarketPlace
 *Tables:
 *		1. sellers: seller_id, creation_date, cur_projects, history
 *		2. buyers: buyer_id, creation_date, cur_bids, history
 *		3. projects: project_id, seller_id, category, cur_price, buyer_id,
 *					 description, creation_date, expire_date, status 
 *Notes:
 *		1. A testSeller, a testBuyer and a testProject is created when this program is run.
 */


public class DBTableCreation {
	
  // Run as Java application to create MongoDB tables
  public static void main(String[] args) throws ParseException {
    MongoClient mongoClient = new MongoClient();
    MongoDatabase db = mongoClient.getDatabase(DBUtilization.DB_NAME);
    
    // Step 1: remove old tables if exist.
    db.getCollection("sellers").drop();
    db.getCollection("buyers").drop();
    db.getCollection("projects").drop();
    
    // Step 2: create new tables, populate data
    Set<String> dummyProjectId = new HashSet<>();
    dummyProjectId.add("testProject");
    
    db.getCollection("sellers")
        .insertOne(new Document().append("seller_id", "testSeller").append("creation_date", new Date().toString())
        		.append("cur_projects", dummyProjectId).append("history", new HashSet<String>()));

    db.getCollection("buyers").insertOne(new Document().append("buyer_id", "testBuyer").append("creation_date", new Date().toString())
    		.append("cur_bids", dummyProjectId).append("history", new HashSet<String>()));
    
    db.getCollection("projects").insertOne(new Document().append("project_id", "testProject").append("seller_id", "testSeller").append("category", "Software Engineering")
    		.append("cur_price", 2000.00).append("buyer_id", "testBuyer").append("description", "Web service development").append("creation_date", new Date())
    		.append("expire_date", new Date((new Date().getTime()) + 1000*10)).append("status", true));
    
    // Step 3: create index for each table.
    IndexOptions indexOptions = new IndexOptions().unique(true);
    db.getCollection("sellers").createIndex(new Document("seller_id", 1), indexOptions);
    db.getCollection("buyers").createIndex(new Document("buyer_id", 1), indexOptions);
    db.getCollection("projects").createIndex(Indexes.ascending("project_id", "category"));

    //close connection. 
    mongoClient.close();
    System.out.println("Import is done successfully.");
  }
}
