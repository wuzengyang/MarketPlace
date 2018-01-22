package db;

import static com.mongodb.client.model.Filters.eq;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import model.Project;

/**
 * Function:
 * 		Main mongodb connection.
 * Methods:
 * 		1.getInstance()
 * 			Get a mongodb connection.
 * 
 * 		2.close()
 * 			Close current connection.
 * 
 * 		3.searchCurProj()
 * 			Search all open projects
 * 
 * 		4.toJSONObject(Document doc)
 * 			A helper function. Parse a document to a JSONObject
 * 
 * 		5.searchByProjectId(String projectId)
 * 			Search project by projectId
 * 
 * 		6.searchBySeller(String sellerId)
 * 			Search open project by seller Id
 * 
 *		7. findProjectIds(String sellerId)
 *			A helper function for method 6. Find all open project id of a seller
 *
 *		8. searchByBidder(String buyerId)
 *			Search project by buyer Id
 *
 *		9. findBidIds(String buyerId)
 *			A helper function of method 8. Find all bids history of a buyer
 *
 *		10. searchByCategoty(String category)
 *			Search open projects by category
 *
 *		11. checkStatus()
 *			Check all open project and change project status if a project has expired. 
 *
 *		12. findExpiredProjectIds(Date curDate)
 *			A helper function of method 11. Find all projects that are expired.
 *
 *		13. postProject(Project project)
 *			Post a project by a seller. 
 *
 *		14. bidProject(String buyerId, double price, String projectId)
 *			Bid a project by a buyer.
 *
 *		15. registerBuyer(String buyerId)
 *			Buyer registration. A new Buyer will be inserted into buyers table.
 *
 *		16. registerSeller(String sellerId)
 *			Seller registration. A new seller will be inserted into sellers table.
 */


public class MongoDBConnection implements DBConnection {

	private static DBConnection instance;
	private MongoClient mongoClient;
	private MongoDatabase db;

	public static DBConnection getInstance() {
		if (instance == null) {
			instance = new MongoDBConnection();
		}
		return instance;
	}

	public MongoDBConnection() {
		mongoClient = new MongoClient();
		db = mongoClient.getDatabase(DBUtilization.DB_NAME);
	}

	/**
	 * Close the connection.
	 */
	@Override
	public void close() {
		if (mongoClient != null) {
			mongoClient.close();
		}
	}

	/**
	 * Search all available project
	 */
	public JSONArray searchCurProj() {

		checkStatus();
		List<JSONObject> list = new ArrayList<>();
		MongoCursor<Document> iterable = db.getCollection("projects").find(eq("status", true)).iterator();
		while (iterable.hasNext()) {
			Document doc = iterable.next();
			list.add(toJSONObject(doc));
		}

		if (list.size() == 0) {

			JSONObject obj = new JSONObject();
			try {
				obj.put("NoResults", "No available projects.");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			list.add(obj);
		}
		return new JSONArray(list);
	}

	public JSONObject toJSONObject(Document doc) {
		Project curProj = new Project();
		curProj.setProjectId(doc.getString("project_id"));
		curProj.setSellerId(doc.getString("seller_id"));
		curProj.setCategory(doc.getString("category"));
		curProj.setDescription(doc.getString("description"));
		curProj.setCurPrice(doc.getDouble("cur_price"));
		curProj.setExpireDate(doc.getDate("expire_date"));
		curProj.setStatus(doc.getBoolean("status"));
		return curProj.toJSONObject();
	}

	@Override
	public JSONObject searchByProjectId(String projectId) {
		FindIterable<Document> iterable = db.getCollection("projects").find(eq("project_id", projectId));
		Document doc = iterable.first();
		return toJSONObject(doc);
	}

	/**
	 * Search by Seller's ID
	 * 
	 * @param sellerID
	 */
	public JSONArray searchBySeller(String sellerId) {
		
		checkStatus();
		Set<String> projectIds = findProjectIds(sellerId);
		if (projectIds.size() == 0) {
			return null;
		}
		
		List<JSONObject> list = new LinkedList<>();

		for (String projectId : projectIds) {
			list.add(searchByProjectId(projectId));
		}
		return new JSONArray(list);

	}

	@SuppressWarnings("unchecked")
	public Set<String> findProjectIds(String sellerId) {
		
		Set<String> projects = new HashSet<>();
		FindIterable<Document> iterable = db.getCollection("sellers").find(eq("seller_id", sellerId));
		if (iterable.first().containsKey("cur_projects")) {
			List<String> list = (List<String>) iterable.first().get("cur_projects");
			projects.addAll(list);
			return projects;
		}
		return new HashSet<String>();

	}

	/**
	 * Search by Category
	 * 
	 * @param category
	 */
	public JSONArray searchByCategory(String category) {
		
		checkStatus();
		List<JSONObject> list = new ArrayList<>();
//		Filter filter = 
		BasicDBObject query = new BasicDBObject();
		query.put("category", category);
		query.put("status", true);
		MongoCursor<Document> iterable = db.getCollection("projects").find(query).iterator();
		while (iterable.hasNext()) {
			Document doc = iterable.next();
			list.add(toJSONObject(doc));
		}

		return new JSONArray(list);
	}

	/**
	 * Search by bidder
	 * 
	 * @param bidderID
	 */
	public JSONArray searchByBidder(String buyerId) {
		
		checkStatus();
		Set<String> projectIds = findBidIds(buyerId);
		if (projectIds.size() == 0) {
			return null;
		}
		
		List<JSONObject> list = new LinkedList<>();
		for (String projectId : projectIds) {
			FindIterable<Document> iterable = db.getCollection("projects").find(eq("project_id", projectId));
			Document doc = iterable.first();
			list.add(toJSONObject(doc));
		}
		return new JSONArray(list);
	}

	@SuppressWarnings("unchecked")
	public Set<String> findBidIds(String buyerId) {
		Set<String> projects = new HashSet<>();
		FindIterable<Document> iterable = db.getCollection("buyers").find(eq("buyer_id", buyerId));
		if (iterable.first().containsKey("history")) {
			List<String> list = (List<String>) iterable.first().get("history");
			projects.addAll(list);
			return projects;
		}
		return new HashSet<String>();

	}

	/**
	 * check current project status Determine bidder if a project is expired.
	 * 
	 */
	public void checkStatus() {

		Set<String> expiredProjIds = findExpiredProjectIds(new Date());
		if (expiredProjIds.size() == 0) {
			return;
		}

		for (String projectId : expiredProjIds) {
			// change project status
			// staus - false : project will not be visible
			db.getCollection("projects").updateOne(eq("project_id", projectId),
					new Document("$set", new Document("status", false)));
			// move curprject to project/bid history
			FindIterable<Document> iterableProj = db.getCollection("projects").find(eq("project_id", projectId));
			Document doc = iterableProj.first();
			// update sellers history
			db.getCollection("sellers").updateOne(new Document("seller_id", doc.getString("seller_id")),
					new Document("$pull", new Document("cur_projects", doc.getString("project_id"))));
			db.getCollection("sellers").updateOne(new Document("seller_id", doc.getString("seller_id")),
					new Document("$push", new Document("history", doc.getString("project_id"))));

			// update buyers history
			String buyer = doc.getString("buyer_id");
			if (buyer == null) {
				return;
			}

			db.getCollection("buyers").updateOne(new Document("buyer_id", doc.getString("buyer_id")),
					new Document("$pull", new Document("cur_bids", doc.getString("project_id"))));
			db.getCollection("buyers").updateOne(new Document("buyer_id", doc.getString("buyer_id")),
					new Document("$push", new Document("history", doc.getString("project_id"))));
		}
	}

	public Set<String> findExpiredProjectIds(Date curDate) {

		Set<String> set = new HashSet<>();
		MongoCursor<Document> iterable = db.getCollection("projects").find(eq("status", true)).iterator();
		while (iterable.hasNext()) {
			Document doc = iterable.next();
			if (curDate.after((Date) doc.get("expire_date"))) {
				set.add(doc.getString("project_id"));
			}
		}
		return set;

	}

	/**
	 * post a new project
	 * 
	 * @param project
	 */
	public boolean postProject(Project project) {

		if (project == null) {
			return false;
		}
		MongoCursor<Document> iterable = db.getCollection("projects").find(eq("project_id", project.getProjectId())).iterator();
		if (iterable.hasNext()) {
			return false;
		}
		

		db.getCollection("projects").insertOne(new Document().append("project_id", project.getProjectId())
				.append("seller_id", project.getSellerId()).append("category", project.getCategory())
				.append("cur_price", project.getCurPrice()).append("buyer_id", project.getCurBidder())
				.append("description", project.getDescription()).append("creation_date", project.getCreationDate())
				.append("expire_date", project.getExpireDate()).append("status", true));
		// add to seller's project list

		db.getCollection("sellers").updateOne(new Document("seller_id", project.getSellerId()),
				new Document("$push", new Document("cur_projects", project.getProjectId())));
		return true;
	}

	/**
	 * bid for a project
	 * 
	 * @param buyerId
	 * @param price
	 * @param projectId
	 */
	public int bidProject(String buyerId, double price, String projectId) {

		checkStatus();
		// case 2: insufficient information
		if (buyerId == null || price < 0 || projectId == null) {
			return 2;
		}

		// case 3: wrong project Id or project no longer available
		FindIterable<Document> iterable = db.getCollection("projects").find(eq("project_id", projectId));
		Document doc = iterable.first();
		if (iterable == null || !doc.getBoolean("status")) {
			return 3;
		}
		
		// case 4: invalid price
		if (price >= doc.getDouble("cur_price")) {
			return 4;
		}

		// case 5: Currenty bidder cannot bid more than one time
		if (buyerId.equals(doc.getString("buyer_id"))) {
			return 5;
		}
		
		// valid price, update project table and buyer table
		// 1. get previous buyer, and delete the projectID form his table
		db.getCollection("buyers").updateOne(new Document("buyer_id", doc.getString("buyer_id")),
				new Document("$pull", new Document("cur_bids", doc.getString("project_id"))));
		// 2. update buyer id and price
		db.getCollection("projects").updateOne(eq("project_id", projectId),
				new Document("$set", new Document("cur_price", price).append("buyer_id", buyerId)));
		// 3. update current buyers table.
		db.getCollection("buyers").updateOne(new Document("buyer_id", buyerId),
				new Document("$push", new Document("cur_bids", projectId)));
		return 1;
	}

	/**
	 * Register a buyer with buyer ID
	 */
	public boolean registerBuyer(String buyerId) {
	
		MongoCursor<Document> iterable = db.getCollection("buyers").find(eq("buyer_id", buyerId)).iterator();
		if (iterable.hasNext()) {
			return false;
		}
		
		db.getCollection("buyers").insertOne(new Document().append("buyer_id", buyerId).append("creation_date", new Date().toString())
	    		.append("cur_bids", new HashSet<String>()).append("history", new HashSet<String>()));
		return true;
	}

	/**
	 * Register a seller with seller ID
	 */
	public boolean registerSeller(String sellerId) {
		
		MongoCursor<Document> iterable = db.getCollection("sellers").find(eq("seller_id", sellerId)).iterator();
		if (iterable.hasNext()) {
			return false;
		}
		
	    db.getCollection("sellers")
        .insertOne(new Document().append("seller_id", sellerId).append("creation_date", new Date().toString())
        		.append("cur_projects", new HashSet<String>()).append("history", new HashSet<String>()));
		return true;
	}

}
