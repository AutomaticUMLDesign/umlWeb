package backend;

//add sql libraries
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;


//add libraries for converting ArrayLists into strings
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.imageio.ImageIO;

public class DBConnection {
	
	static String dbDriver = "org.sqlite.JDBC";
    static String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
    static String createDatabase = "jdbc:sqlite:sample.db"; // used to create a database from inside the class.
    public static String concept_statement;
    public static String nouns;
    public static String verbs;
    public static String tags;
    public static byte[] classDiagram;
    public static byte[] useCaseDiagram;
    public static byte[] SSDiagram;
    public static byte[] test;
    
	public static void main(String[] args) throws SQLException, ClassNotFoundException {

		
//		try{
//		Class.forName(dbDriver);
//		} catch (Exception e) {
//			System.out.println("DriverName: " + dbDriver
//                    + " was not available");
//            System.err.println(e);
//            throw e;
//		}
//		Connection connection = DriverManager.getConnection(database);
//		
//		Statement statement = connection.createStatement();
//		
//		statement.executeUpdate("DROP TABLE IF EXISTS uml_design");
//		
//		statement.executeUpdate("create table uml_design (concept_statement VARCHAR, tagged VARCHAR, valid_nouns VARCHAR, valid_verbs VARCHAR, valid_associations VARCHAR)");
		

	}
	/*********************************************************************************************
	 * convertToString
	 * converts the given string ArrayList to a string separated by commas. 
	 * @param list - ArrayList<String>
	 * @return commaSeparatedValues - String
	 */
	public static String convertToString(ArrayList<String> list) {
		String commaSeparatedValues = "";
		
		if(list != null)
		{
			/**Iterate through the ArrayList and append comma after each value**/
			Iterator<String> iter = list.iterator();
			while (iter.hasNext()) {
				commaSeparatedValues += iter.next() + ",";
			}
			/**Remove the last comma**/
			if(commaSeparatedValues.endsWith(",")){
				commaSeparatedValues = commaSeparatedValues.substring(0,commaSeparatedValues.lastIndexOf(","));
			}
		}
		return commaSeparatedValues;
		
	}
	/*********************************************************************************************
	 * convertToArrayList
	 * converts the given string to a string ArrayList, removing the commas separating the values. 
	 * @param str - String
	 * @return list - ArrayList<String>
	 */
	public static ArrayList<String> convertToArrayList(String str)
	{
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(str.split("\\s*,\\s*")));
		
		return list;
	}
	/*********************************************************************************************
	 * conceptIn
	 * inserts the concept_statement string into the database 
	 * @param str - String
	 * @return void
	 * @throws ClassNotFoundException, SQLException 
	 */
	public static void conceptIn(String str) throws SQLException, ClassNotFoundException {
		concept_statement = str;
		try {
		Class.forName(dbDriver);
		} catch (Exception e) {
			System.out.println("DriverName: " + dbDriver
                    + " was not available");
            System.err.println(e);
            throw e;
		}
		String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		Connection connection = DriverManager.getConnection(database);
		
		Statement statement = connection.createStatement();
		
		String sql = "INSERT INTO uml_design (concept_statement, tagged, valid_nouns, valid_verbs, valid_associations) VALUES ('" + str + "', null, null, null, null);";
		
		statement.executeUpdate("DROP TABLE IF EXISTS uml_design");
		
		statement.executeUpdate("create table uml_design (concept_statement VARCHAR, tagged VARCHAR, valid_nouns VARCHAR, valid_verbs VARCHAR, valid_associations VARCHAR, actor_images BLOB, useCase_images BLOB, ssd_images BLOB)");
		
//		statement.executeUpdate(sql);
	}
	
	/*********************************************************************************************
	 * conceptOut ==== NOT TESTED
	 * outputs the concept_statement string from the database and converts it to an arrayList, it then returns
	 * the newly created arrayList. 
	 * @param str - String
	 * @return void
	 * @throws ClassNotFoundException, SQLException 
	 */
	public static ArrayList<String> conceptOut() throws SQLException, ClassNotFoundException {
		String concept = null;
		ArrayList<String> concept_stmt;
		try {
		Class.forName(dbDriver);
		} catch (Exception e) {
			System.out.println("DriverName: " + dbDriver
                    + " was not available");
            System.err.println(e);
            throw e;
		}
		String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		Connection connection = DriverManager.getConnection(database);
		
		
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("select concept_statement from uml_design;");
		while(rs.next() ) {
			 concept = rs.getString("concept_statement");
		}
		rs.close();
		concept_stmt = convertToArrayList(concept);
		return concept_stmt;
	}
	
	/*********************************************************************************************
	 * tagsIn
	 * inserts the tags string into the database 
	 * @param str - String
	 * @return void
	 * @throws ClassNotFoundException, SQLException 
	 */
	public static void tagsIn(String str) throws SQLException, ClassNotFoundException {
		tags = str;
		try {
		Class.forName(dbDriver);
		} catch (Exception e) {
			System.out.println("DriverName: " + dbDriver
                    + " was not available");
            System.err.println(e);
            throw e;
		}
		String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		Connection connection = DriverManager.getConnection(database);
		
		Statement statement = connection.createStatement();
				
		String sql = "UPDATE uml_design SET tagged = '" + str + "' WHERE concept_statement IS NOT NULL;";
		
//		statement.executeUpdate(sql);
	}
	/*********************************************************************************************
	 * nounsIn
	 * inserts the noun string into the database 
	 * @param str - String
	 * @return void
	 * @throws SQLException 
	 */
	public static void nounsIn(String str) throws SQLException {
		nouns = str;
	    String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		Connection connection = DriverManager.getConnection(database);
		
		Statement statement = connection.createStatement();
		
		String sql = "UPDATE uml_design SET valid_nouns = '" + str + "' WHERE concept_statement IS NOT NULL;";
		
//		statement.executeUpdate(sql);
	}
	/*********************************************************************************************
	 * verbsIn
	 * inserts the verbs string into the database 
	 * @param str - String
	 * @return void
	 * @throws SQLException 
	 */
	public static void verbsIn(String str) throws SQLException {
		verbs = str;
	    String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		Connection connection = DriverManager.getConnection(database);
		
		Statement statement = connection.createStatement();
		
		String sql = "UPDATE uml_design SET valid_verbs = '" + str + "' WHERE concept_statement IS NOT NULL;";
		
//		statement.executeUpdate(sql);
	}
	//Unused function to convert image to byte array. 
	public static byte[] extractBytes (String ImageName) throws IOException {
		 // open image
		 File imgPath = new File(ImageName);
		 BufferedImage bufferedImage = ImageIO.read(imgPath);

		 // get DataBufferBytes from Raster
		 WritableRaster raster = bufferedImage .getRaster();
		 DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

		 return ( data.getData() );
		}
	
	/*********************************************************************************************
	 * actorsInByte
	 * inserts the byte array containing image data into the database
	 *  === at the moment also inserts the other values into the database, subject to change this summer
	 * @param img - byte[]
	 * @return void
	 * @throws SQLException 
	 */
	public static void actorsInByte(byte[] img) throws SQLException {
		classDiagram = img;
	    String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		String str = null;
		
		String query = "INSERT INTO uml_design (concept_statement,tagged,valid_nouns,valid_verbs,valid_associations,actor_images, useCase_images, ssd_images) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    Connection connection = DriverManager.getConnection(database);
		
		Statement statement = connection.createStatement();
		
		PreparedStatement prepStmt = null;
		
		connection.setAutoCommit(false);
		
		prepStmt=connection.prepareStatement(query);
		
		prepStmt.setString(1, concept_statement);
		prepStmt.setString(2, tags);
		prepStmt.setString(3, nouns);
		prepStmt.setString(4, verbs);
		prepStmt.setString(5, null);
		prepStmt.setBytes(6, img);
		prepStmt.setBytes(7, null);
		prepStmt.setBytes(8, null);
		
		prepStmt.executeUpdate();
		connection.commit();
	//	String sql = "UPDATE uml_design SET actor_images = " + str + " WHERE concept_statement IS NOT NULL;";
		
		
		
	//	statement.executeUpdate(sql);
	}
	
	/*********************************************************************************************
	 * actorsInByte
	 * inserts the byte array containing image data into the database
	 *  === at the moment also inserts the other values into the database, subject to change this summer
	 * @param img - byte[]
	 * @return void
	 * @throws SQLException 
	 */
	public static void useCaseInByte(byte[] img) throws SQLException {
		useCaseDiagram = img;
		
	    String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		String str = null;
		
		String query = "INSERT INTO uml_design (concept_statement,tagged,valid_nouns,valid_verbs,valid_associations,actor_images, useCase_images, ssd_images) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    Connection connection = DriverManager.getConnection(database);
		
		Statement statement = connection.createStatement();
		
		statement.executeUpdate("DROP TABLE IF EXISTS uml_design");
		
		statement.executeUpdate("create table uml_design (concept_statement VARCHAR, tagged VARCHAR, valid_nouns VARCHAR, valid_verbs VARCHAR, valid_associations VARCHAR, actor_images BLOB, useCase_images BLOB, ssd_images BLOB)");
		
		PreparedStatement prepStmt = null;
		
		connection.setAutoCommit(false);
		
		prepStmt=connection.prepareStatement(query);
		
		prepStmt.setString(1, concept_statement);
		prepStmt.setString(2, tags);
		prepStmt.setString(3, nouns);
		prepStmt.setString(4, verbs);
		prepStmt.setString(5, null);
		prepStmt.setBytes(6, classDiagram);
		prepStmt.setBytes(7, img);
		prepStmt.setBytes(8, null);
		
		prepStmt.executeUpdate();
		connection.commit();
	//	String sql = "UPDATE uml_design SET actor_images = " + str + " WHERE concept_statement IS NOT NULL;";
		
		
		
	//	statement.executeUpdate(sql);
	}
	
	/*********************************************************************************************
	 * actorsInByte
	 * inserts the byte array containing image data into the database
	 *  === at the moment also inserts the other values into the database, subject to change this summer
	 * @param img - byte[]
	 * @return void
	 * @throws SQLException 
	 */
	public static void SSDInByte(byte[] img) throws SQLException {
		SSDiagram = img;
	    String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		String str = null;
		
		String query = "INSERT INTO uml_design (concept_statement,tagged,valid_nouns,valid_verbs,valid_associations,actor_images, useCase_images, ssd_images) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    
	    Connection connection = DriverManager.getConnection(database);
		
		Statement statement = connection.createStatement();
		
		statement.executeUpdate("DROP TABLE IF EXISTS uml_design");
		
		statement.executeUpdate("create table uml_design (concept_statement VARCHAR, tagged VARCHAR, valid_nouns VARCHAR, valid_verbs VARCHAR, valid_associations VARCHAR, actor_images BLOB, useCase_images BLOB, ssd_images BLOB)");
		
		PreparedStatement prepStmt = null;
		
		connection.setAutoCommit(false);
		
		prepStmt=connection.prepareStatement(query);
		
		prepStmt.setString(1, concept_statement);
		prepStmt.setString(2, tags);
		prepStmt.setString(3, nouns);
		prepStmt.setString(4, verbs);
		prepStmt.setString(5, null);
		prepStmt.setBytes(6, classDiagram);
		prepStmt.setBytes(7, useCaseDiagram);
		prepStmt.setBytes(8, img);
		
		prepStmt.executeUpdate();
		connection.commit();
	//	String sql = "UPDATE uml_design SET actor_images = " + str + " WHERE concept_statement IS NOT NULL;";
		
		
		
	//	statement.executeUpdate(sql);
	}
	
	/*********************************************************************************************
	 * actorsInFile
	 * inserts the string filepath for locating the image into the database
	 * @param str - String
	 * @return void
	 * @throws SQLException 
	 */
	public static void actorsInFile(String img) throws SQLException {
		
	    String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		String str = null;
	    
	    Connection connection = DriverManager.getConnection(database);
		
		Statement statement = connection.createStatement();
		
		String sql = "UPDATE uml_design SET actor_images = " + img + " WHERE concept_statement IS NOT NULL;";
		
		statement.executeUpdate(sql);
	}
	
	/*********************************************************************************************
	 * actorsOutByte
	 * returns string filepath for locating the image from the database
	 * @param img - String
	 * @return String
	 * @throws Exception 
	 */
	public static String actorsOutFile(String img) throws Exception {
		String filename = null;
		try {
		Class.forName(dbDriver);
		} catch (Exception e) {
			System.out.println("DriverName: " + dbDriver
                    + " was not available");
            System.err.println(e);
            throw e;
		}
		String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		Connection connection = DriverManager.getConnection(database);
		
		
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("select actors_image from uml_design;");
		while(rs.next() ) {
			 filename = rs.getString("actors_image");
		}
		rs.close();
		return filename;
	}
	
	/*********************************************************************************************
	 * actorsOutByte
	 * returns the byte array containing the image from the database
	 * @param name - String
	 * @return Image
	 * @throws Exception 
	 */
	public static String actorsOutByte() throws Exception {
		BufferedImage bimg;
		String image = null;
		Image img = null;
		String filename = null;
		
		byte[] imgArr = null;

		Class.forName(dbDriver);

		String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		Connection connection = DriverManager.getConnection(database);
		
		
		Statement statement = connection.createStatement();
		ResultSet rs = statement.executeQuery("select actor_images from uml_design;");
		while(rs.next() ) {
			 imgArr = rs.getBytes("actor_images");
		//	 ByteArrayInputStream bais = new ByteArrayInputStream(imgArr);

				
			//	bimg = ImageIO.read(bais);
				
			//	bimg.getGraphics();
			// img=Toolkit.getDefaultToolkit().createImage(imgArr);
			 image = new sun.misc.BASE64Encoder().encode(imgArr);
	//		 System.out.print(image);
			// image = new String(imgArr, "UTF-8");
		}
		rs.close();
		
	//	 ByteArrayInputStream bais = new ByteArrayInputStream(imgArr);

		
//		bimg = ImageIO.read(bais);
		
//		bimg.getGraphics();
		image = image.replaceAll("[\n\r]", "");
	    System.out.print(image);
	    System.out.print("\n");
		return image;
	}
	
	/*********************************************************************************************
	 * getByteArrayFromFile
	 * converts a image file into a byte array. 
	 * @param filePath - String
	 * @return byte[]
	 * @throws no Exception thrown
	 */
	 public static byte[] getByteArrayFromFile(String filePath){  
	        byte[] result=null;  
	        FileInputStream fileInStr=null;  
	        try{  
	            File imgFile=new File(filePath);  
	            fileInStr=new FileInputStream(imgFile);  
	            long imageSize=imgFile.length();  
	              
	            if(imageSize>Integer.MAX_VALUE){  
	             System.out.print("THE IMAGE IS TOO LARGE.");
	            	return null;   //image is too large 
	            }  
	              
	            if(imageSize>0){  
	                result=new byte[(int)imageSize];  
	                fileInStr.read(result);  
	            }  
	        }catch(Exception e){  
	            e.printStackTrace();  
	        } finally {  
	            try {  
	                fileInStr.close();  
	            } catch (Exception e) {  
	            }  
	        }  
	        return result;  
	    }  
	 public static void setByteArray(byte[] img){
		 test = img;
	 }
	 
//	 public static String getByteArrayStr() throws IOException{
//		 String test2;
//		 String test3;
//		 test2 = new sun.misc.BASE64Encoder().encode(test);
//	//	 System.out.print(test2);
////		 PrintWriter out = new PrintWriter("test.txt");
////		 out.print(test2);
//		 
////		 BufferedReader br = new BufferedReader(new FileReader("text.txt"));
////		    try {
////		        StringBuilder sb = new StringBuilder();
////		        String line = br.readLine();
////
////		        while (line != null) {
////		            sb.append(line);
////		            line = br.readLine();
////		        }
////		        test3 = sb.toString();
////		    } finally {
////		        br.close();
////		    }
//		 
//		 test2 = test2.replaceAll("[\n\r]", "");
//		    System.out.print(test2);
//		 return test2;
//	 }
	 
	 
	
}