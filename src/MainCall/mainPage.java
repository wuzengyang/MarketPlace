package MainCall;

import db.DBConnection;
import db.MongoDBConnection;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

/**
 * Servlet implementation class mainPage
 * 
 * Function: 
 * 		1. check and print all available project. 
 * Method : 
 * 		GET
 * URL: 
 * 		http://localhost:8080/MarketPlace/main
 * Return:
 * 		All available project with details.
 * 	
 * 	
 */
@WebServlet("/main")
public class mainPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private DBConnection conn = MongoDBConnection.getInstance();   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public mainPage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//conn.checkStatus();
		JSONArray array = conn.searchCurProj();
		jasonHelper.writeJsonArray(response, array);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
