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
 * Servlet implementation class buyerRegister
 * 
 * Function: 
 * 		1. Register a buyer 2. Return registration status.
 * Method : 
 * 		POST
 * URL: 
 * 		http://localhost:8080/MarketPlace/buyer
 * Input sample : 
 * 		{ "buyer_id":"testBuyer03" }
 * Return:
 * 		Registration status.
 */



@WebServlet("/buyer")
public class buyerRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = MongoDBConnection.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public buyerRegister() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try {
			JSONObject input = jasonHelper.readJsonObject(request);

			if (input.has("buyer_id") && conn.registerBuyer(input.getString("buyer_id"))) {
				jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Buyer registered."));
			} else {
				jasonHelper.writeJsonObject(response,
						new JSONObject().put("status", "Error, incorrect ID format or buyer ID exists."));
			}

		} catch (JSONException e) {
			e.printStackTrace();

		}
	}

}
