package MainCall;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.MongoDBConnection;

/**
 * Servlet implementation class searchByBuyer
 * 
 * Function: search the bid history of a buyer.
 * Method: 
 * 		GET
 * URL: 
 * 		http://localhost:8080/MarketPlace/searchbuyer?buyer_id=testBuyer
 * Return: 
 * 		Bid history of this buyer.
 * 
 */
@WebServlet("/searchbuyer")
public class searchByBuyer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = MongoDBConnection.getInstance();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public searchByBuyer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String buyerId = request.getParameter("buyer_id");
		try {
			JSONArray projects = conn.searchByBidder(buyerId);
			if (projects == null) {
				jasonHelper.writeJsonObject(response, new JSONObject().put("status", "No available projects."));
			}
			jasonHelper.writeJsonArray(response, projects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
