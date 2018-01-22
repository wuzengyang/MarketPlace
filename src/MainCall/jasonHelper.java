package MainCall;

import java.io.BufferedReader;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;


/**
 * A helper class to handle JSON-related parsing logics.
 * 
 * Methods:
 * 		1. readJSONObject(HttpServletRequest request)
 * 		   Function : parse a HTTP request and return a JSONObject
 * 
 * 		2. writeJsonObject(HttpServletResponse response, JSONObject obj)
 * 			Function: print a JSONObject
 * 
 * 		3. writeJsonArray(HttpServletResponse response, JSONArray array)
 * 			Function: print a JSONArray
 *  	
 */
public class jasonHelper {
	// Parses a JSONObject from http request.
	public static JSONObject readJsonObject(HttpServletRequest request) {
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
			reader.close();
			return new JSONObject(jb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	// Writes a JSONObject to Http response.
	public static void writeJsonObject(HttpServletResponse response, JSONObject obj) {
		try {
			response.setContentType("application/json");
			response.addHeader("Access-Control-Allow-Origin", "*");
			PrintWriter out = response.getWriter();
			out.print(obj);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// Writes a JSONArray to http response.
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) {
		try {
			response.setContentType("application/json");
			response.addHeader("Access-Control-Allow-Origin", "*");
			PrintWriter out = response.getWriter();
			out.print(array);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

