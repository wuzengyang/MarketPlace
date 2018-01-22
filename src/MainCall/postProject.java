package MainCall;

import java.io.IOException;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.MongoDBConnection;
import model.Project;

/**
 * Servlet implementation class postProject
 * 
 * Function:
 * 		Post a project by a seller. 
 * Method: 
 * 		POST
 * URL: 
 * 		http://localhost:8080/MarketPlace/post
 * Input Sample :
 * 		{
			"project_id":"testProject",
			"seller_id":"testSeller",
			"category":"Software Engineer",
			"description":"Backend development",
			"cur_price": 2000.00,
			"expire_date": 100000;  // post validation duration in milliseconds .
		}
 *
 *Return: 
 *		Post status. 
 */


@WebServlet("/post")
public class postProject extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = MongoDBConnection.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public postProject() {
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
			Project project = new Project();
			if (input.has("project_id")) {
				project.setProjectId(input.getString("project_id"));
			}
			if (input.has("seller_id")) {
				project.setSellerId(input.getString("seller_id"));
			}
			if (input.has("category")) {
				project.setCategory(input.getString("category"));
			}
			if (input.has("description")) {
				project.setDescription(input.getString("description"));
			}
			if (input.has("cur_price")) {
				project.setCurPrice(input.getDouble("cur_price"));
			}
			project.setCreationDate(new Date());
			if (input.has("expire_date")) {
				project.setExpireDate(new Date(new Date().getTime() +input.getLong("expire_date")));
			}
			project.setCurBidder(null);
			project.setStatus(true);
			if (conn.postProject(project)) {
				jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Project posted!"));
			} else {
				jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Error, project ID exists."));
			}
			
		} catch (JSONException e) {
			e.printStackTrace();

		}
	}

}
