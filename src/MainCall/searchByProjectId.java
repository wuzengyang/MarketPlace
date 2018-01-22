package MainCall;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.DBConnection;
import db.MongoDBConnection;


/**
 * Servlet implementation class searchByProjectId
 * 
 * Function:
 * 		Search a project by project ID.
 * Method:
 * 		GET
 * URL:
 * 		http://localhost:8080/MarketPlace/searchID?project_id=testProject
 * Return:
 * 		Project details or query response.
 * 
 */
@WebServlet("/searchID")
public class searchByProjectId extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = MongoDBConnection.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public searchByProjectId() {
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

		String projectId = request.getParameter("project_id");
		try {
			JSONObject project = conn.searchByProjectId(projectId);
			if (project == null) {
				jasonHelper.writeJsonObject(response, new JSONObject().put("status", "Project not found."));
			}
			jasonHelper.writeJsonObject(response, project);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
