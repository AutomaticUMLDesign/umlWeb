package backend;

//add sql libraries
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;


//add libraries for converting ArrayLists into strings
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class DBConnection {
	
	static String dbDriver = "org.sqlite.JDBC";
    static String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
    static String createDatabase = "jdbc:sqlite:sample.db"; // used to create a database from inside the class.
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
		
//		Statement statement = connection.createStatement();
		
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
	 * converts the given string to a string ArrayList, removing the commas separating the values. 
	 * @param str - String
	 * @return list - ArrayList<String>
	 * @throws ClassNotFoundException 
	 */
	public static void conceptIn(String str) throws SQLException, ClassNotFoundException {
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
		
		statement.executeUpdate(sql);
	}
	
	public static void nounsIn(String str) throws SQLException {
		
	    String database = "jdbc:sqlite:C:/Sqlite/UML_Magic.db"; // calls to a local database that has already been created.
		
		Connection connection = DriverManager.getConnection(database);
		
		Statement statement = connection.createStatement();
		
		String sql = "INSERT INTO uml_design (valid_nouns) VALUES (" + str + ");";
		
		statement.executeUpdate(sql);
	}
	
}