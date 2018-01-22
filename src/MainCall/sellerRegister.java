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
 * Servlet implementation class sellerRegister
 * 
 * Function:
 * 		Register a seller
 * Method:
 * 		POST
 * URL:
 * 		http://localhost:8080/MarketPlace/seller
 * Input Sample:
 * 		{"seller_id":"testSeller"}
 * Return:
 * 		Registration status.
 * 
 */
@WebServlet("/seller")
public class sellerRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = MongoDBConnection.getInstance();
    /**
     * @see HttpServlet#HttpServlet()
     */
    public sellerRegister() {
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
			
			if (input.has("seller_id") && conn.registerSeller(input.getString("seller_id"))) {
				jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Seller registered."));
			} else {
				jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Error, incorrect ID format or seller ID exists."));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();

		}
	}

}
