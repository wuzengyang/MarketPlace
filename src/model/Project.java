package model;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Function: 
 * 		A model designed to simplify project-post procedure.  
 * Method:
 *  	1. toJSONObject() : convert project to a JSONObject
 */

public class Project {
	
	private String projectId;
	private String sellerId;
	private String category;
	private double curPrice;
	private String curBidder; 
	private String description;
	private Date creationDate;
	private Date expireDate;
	private boolean status;
	
	public Project() {
		
	}

	
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	public double getCurPrice() {
		return curPrice;
	}
	public void setCurPrice(double curPrice) {
		this.curPrice = curPrice;
	}
	
	public String getCurBidder() {
		return curBidder;
	}
	public void setCurBidder(String curBidder) {
		this.curBidder = curBidder;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	
	public Date getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}
	
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getSellerId() {
		return sellerId;
	}
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * Convert project to JSON format
	 * @return
	 */
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("project_id", projectId);
			obj.put("seller_id", sellerId);
			obj.put("category", category);
			obj.put("description", description);
			obj.put("cur_price", curPrice);
			obj.put("expire_date", expireDate);
			obj.put("status", status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}

	
	

}
