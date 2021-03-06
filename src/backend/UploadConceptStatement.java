package backend;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;



//This Class allows users to upload their files
//

@WebServlet("/uploadServlet")
@MultipartConfig(maxFileSize = 16177215)    // upload file's size up to 16MB
public class UploadConceptStatement extends HttpServlet {
	ToPlant plant = new ToPlant();
	private static double timeStamp;
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

		InputStream inputStream = null; // input stream of the upload file
		String name = request.getParameter("firstName"); 
		
		
		double timeStamp = Math.random();
		
        // obtains the upload file part in this multipart request
        Part filePart = request.getPart("file");
        if (filePart != null) {
            // prints out some information for debugging
            
            //System.out.println(filePart.getSize());
            //System.out.println(filePart.getContentType());
             
            // obtains input stream of the upload file
            inputStream = filePart.getInputStream();
            
            ArrayList<String> conceptArray = new ArrayList<String>();
            ArrayList<String> delimited = new ArrayList<String>();
            Scanner scan  = new Scanner(inputStream);
            
			            //******************************************************************
						//REMOVE ***********************************************************
						//PrintWriter toText = new PrintWriter(new File("/home/kullen/workspace/UML-Designer/umlWeb/src/tempFiles/conceptArray.txt"));
						// ******************************************************************
						//*******************************************************************
            
            
        	while(scan.hasNext())	{
        		delimited = delimiter(scan.nextLine());
        		for(String str : delimited){
        			conceptArray.add(str.toLowerCase().trim());
        		}
        		delimited.clear();
        	}
        	scan.close();
        	//for(String str : conceptArray){
        		//toText.println(str + ".");
        	//}
        	//toText.close();
            
            //set concept array in toplant
        	
				try {
					ToPlant.setConceptArray(conceptArray);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println("CLASS NOT");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("SQL:");
				}
			
			
            
			request.setAttribute("concept", conceptArray);
            request.setAttribute("name", name);
            request.setAttribute("timeStamp",timeStamp);
            
            //got to Next PAGE
            getServletContext().getRequestDispatcher("/02ValidateNouns.jsp").forward(request,response);
            
        }
	}

	/* *********************************************************************************************
	 * DELIMTER
	 *  Takes a sentence and stores each word into an Array
	 *  @param str -String
	 *  @return tempAr - ArrayList
	 */
	public ArrayList<String> delimiter(String str){

		String delimit = "[.]";
		String[] tempAr = str.split(delimit);
		ArrayList<String> delimited = new ArrayList<String>();
		
		for(int i = 0 ; i < tempAr.length ; i++){
			delimited.add(tempAr[i]);
		}
		
		return delimited;
	} //********************************************************************************************
	
		public static double getTimeStamp(){
			return timeStamp;
		}
}
