package MainCall;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.MongoDBConnection;

/**
 * Servlet implementation class bidProject
 * 
 * Function:
 * 		1. Bid for a open project
 * Method: 
 * 		Post
 * URL: 
 * 		http://localhost:8080/MarketPlace/bid
 * Input sample :
 * 		{
 *   		"buyer_id": "testBuyer",
 *   		"project_id": "testProject",
 *  		"price": 500
 *		}	
 * Return:
 * 		Bid status.
 * 
 */
@WebServlet("/bid")
public class bidProject extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = MongoDBConnection.getInstance();   
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public bidProject() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			JSONObject input = jasonHelper.readJsonObject(request);
			if (input.has("buyer_id") && input.has("price") && input.has("project_id")) {
				int status = conn.bidProject(input.getString("buyer_id"), input.getDouble("price"), input.getString("project_id"));
				
		        switch (status) 
		        {
		            case 1:  jasonHelper.writeJsonObject(response, new JSONObject().put("status", "success!"));
		                     break;
		            case 2:  jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Error, insufficient information"));;
		                     break;
		            case 3:  jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Error, incorrect project ID or project unavailable."));;
		                     break;
		            case 4:  jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Error, invalid price."));;
		                     break;
		            case 5:  jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Error, invalid action."));;
		                     break;
		            default: jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Unknown error."));;
		                     break;
		        }
			}
		} catch (JSONException e) {
			e.printStackTrace();

		}
	}

}
