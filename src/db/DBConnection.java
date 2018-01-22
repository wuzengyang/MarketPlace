package db;
import org.json.JSONArray;
import org.json.JSONObject;
import model.Project;

/**
 * 
 * Function:
 *		Interface.
 *		Simplify the utilization of MongoDB for users.
 */

public interface DBConnection {
  /**
   * Close the connection.
   */
  public void close();
  /**
   * Search all available project
   */
  public JSONArray searchCurProj();
  /**
   * Search by project ID
   * 
   * @param projectId
   */
  public JSONObject searchByProjectId(String projectId);
  
  /**
   * Search by Seller's ID
   * 
   * @param sellerID
   */
  public JSONArray searchBySeller(String sellerID);
  
  /**
   * Search by Category
   * 
   * @param category
   */
  public JSONArray searchByCategory(String category);
  
  /**
   * Search by bidder
   * 
   * @param bidderID
   */
  public JSONArray searchByBidder(String bidderID);
  
  /**
   * check current project status 
   * Determine bidder if a project is expired.
   * 
   */
  public void checkStatus();
  
  /**
   *post a new project
   * 
   * @param project
   */
  public boolean postProject(Project project);
  
  /**
   *bid for a project
   * 
   * @param buyerId
   * @param price
   */
  public int bidProject(String buyerId, double price, String projectId);
  
  /**
   *Register a new buyer
   * 
   * @param buyerId
   * 
   */
  public boolean registerBuyer(String buyerId);
  
  /**
   *Register a new seller
   * 
   * @param sellerId
   * 
   */
  public boolean registerSeller(String sellerId);
  

}
