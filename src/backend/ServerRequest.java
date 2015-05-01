package backend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class ServerRequest {

	public int getTimeStamp(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
		String timeString = request.getParameter("timeStamp");
		
		int timeStamp = Integer.parseInt(timeString);
		return timeStamp;
	}
	
	public String getName(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException{
		
		String name= request.getParameter("name");
		
		return name;
		
	}
}