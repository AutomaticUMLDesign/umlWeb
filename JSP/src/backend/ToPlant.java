package backend;

import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;







import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;





//add jar files
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
// http://www.galalaly.me/index.php/2011/05/tagging-text-with-stanford-pos-tagger-in-java-applications/

public class ToPlant{
	public static ArrayList<String> 	conceptArray 	= new ArrayList<String>();
	public static ArrayList<String> 	tag 			= new ArrayList<String>();
	public static ArrayList<String>     validNouns 		= new ArrayList<String>();
	public ArrayList<String> 			verbs 			= new ArrayList<String>();
	public static String 				filename;
	public static String[][]			associationArray;
	
	
	/*	******************************************************************************************************
	 *  READ FILE 
	 *  reads txt file and stores each sentence into a cell of the concept array
	 *  @param filename - String
	 *  @return void
	 *********************************************************************************************************/
	public void ReadFile(String filename) throws IOException { 
		ArrayList<String> concept = new ArrayList<String>();
		File file = new File(filename);
		FileReader fr = new FileReader(filename);
		BufferedReader reader = new BufferedReader(fr);
		String fullStatement="";
		String nextLine;
		
		while((nextLine = reader.readLine()) != null)
		{
			fullStatement += nextLine;
		}
		
		/* *********************************************************************************************
		 * DELIMTER
		 *  Split up the sentences in the concept statement and store them in tempAR
		 */
		String delimit = "[.]";
		String[] tempAr = fullStatement.split(delimit);
		
		//trim off leading spaces
		for(int i = 0; i < tempAr.length ; i++){
			concept.add(tempAr[i].trim());
		}  //**********************************************************************************************
		
		setConceptArray(concept); 		//updates concept array
		

	}
	// ***************************************************************************************************
	// ***************************************************************************************************
	// ***************************************************************************************************
	// ***************************************************************************************************
	

	/*  ****************************************************************************************************
	 * TAGGER
	 * 	- is an open source file that tags each word in a sentence with its grammar definition.
	 * 	Instructions on how to use tagger
	 *  http://www.galalaly.me/index.php/2011/05/tagging-text-with-stanford-pos-tagger-in-java-applications/
	 * 
	 */
	public ArrayList<String> Tag(ArrayList<String> conceptArray) throws IOException, ClassNotFoundException{
		
		MaxentTagger tagger;
			tagger = new MaxentTagger("bidirectional-distsim-wsj-0-18.tagger");
		
		ArrayList<String> temp = new ArrayList<String>();
		
		for(String str : conceptArray){
			str = tagger.tagString(str);
			temp.add(str);
		}
		//setTagArray(temp);
		return temp;
	}
	// **************************************************************************************************
	// ***************************************************************************************************
	// **************************************************************************************************
	// ***************************************************************************************************

	public static void setConceptArray(ArrayList<String> conceptIN){
		conceptArray = conceptIN;
	}
	public ArrayList<String> getConceptArray(){
		
		return conceptArray;
	}
	
	public void setTagArray(ArrayList<String> tagIN){
		tag = tagIN;
	}
	public ArrayList<String> getTagArray(){
		return tag;
	}
	
	/* *****************************************************************************
	 *  FIND NOUN ARRAY
	 *  calls find noun multiple times and stores the noun to an array
	 *  removes duplicates.
	 *  @param tagged - ArrayList<String>
	 *  @return nounAr - String[]
	 *******************************************************************************/
	public String[] FindNounArray(ArrayList<String> tagged){
		ArrayList<String> nounAr = new ArrayList<String>();
		boolean found = false;
		int x = 0;
		int arraySize = 0;
		String noun = "";
		
		for(String str: tagged){
			String delimit = "[ ]";
			String[] tempAr = str.split(delimit);

			for(int i = 0 ; i < tempAr.length;i++){
				//System.out.println("*****************" + tempAr)
				found = FindNoun(tempAr[i]);
				if (found == true){ // find index of space
					//Check if word has already been added
					if(!(nounAr.contains(tempAr[i]))){  
						nounAr.add(tempAr[i]);  
						arraySize++;
					}
				}
				found = false;
			}
		}

		//Convert ArrayList to String Array
		String[] nArray = 
				new String[arraySize];
		int j = 0;
		x = 0;
		for(String str : nounAr){
			x = str.indexOf('/');
			str = str.substring(0,x);
			nArray[j] = str;
			j++;
		}
		return nArray;
	}
	//****************************************************************************
	
	/* **************************************************************
	 * Find Noun
	 * Takes in tagged string and returns true if its a noun
	 * @param strIN - String
	 * @return boolean
	 ********************************************************************/
	public static boolean FindNoun(String strIN){
		String str = strIN.trim() + " ";
		String temp = "";
		String noun = "";
		int x = 0;
		int y = 0;
		boolean found = false;

			if(strIN.contains("/N")){ // finds noun break and return
				found = true;
			}
	
		if(found){  return true;    }
		else{  return false;  }
	}
	
	
	/* ****************************************************************************
	 *  FIND Verb ARRAY
	 *  calls find verb multiple times and stores the ver to an array
	 *  removes duplicates.
	 *  @param tagged - ArrayList<String>
	 *  @return verbAr - String[]
	 *******************************************************************************/
	public String[] FindVerbArray(ArrayList<String> tagged, ArrayList<String> concept, String[] noun){
		ArrayList<String> verbAr = new ArrayList<String>();
		ArrayList<String> tempAr   = new ArrayList<String>();
		boolean found = false;
		int x = 0;
		int arraySize = 0;
		int index = -1;
		int find2,find;
		boolean contain1, contain2;
		String verb = null,noun1=null,noun2=null;
		String conceptStr = null  , temp = null;
		for(String str: tagged){
			conceptStr = concept.get(x);

			//loop through sentence; sentence should loop back to look through the entire sentence
			while(conceptStr.length() > 0) {
				contain1 = false ; contain2 = false;
				
				//loop through all nouns and see if concept string contains two of them
				for(int i = 0; i < noun.length ; i++){
					
					if(conceptStr.contains(noun[i]) && contain1 == false){
						noun1 = noun[i];
						System.out.print("FOUND: " + noun[i]);
						contain1 = true;
					}
					else if(conceptStr.contains(noun[i]) && contain1 == true){
						noun2 = noun[i];
						System.out.println("  FOUND: " + noun[i]);
						contain2 = true;
						break;
					}
				}
				if(contain2 == true){
					//find verb between noun 1 and noun 2
					find 	= conceptStr.indexOf(noun1);
					find2 	= conceptStr.indexOf(noun2);
					conceptStr 	= conceptStr.substring(find2);
					
					
					find 	= str.indexOf(noun1);
					find2 	= str.indexOf(noun2);
					temp 	= str.substring(find,find2+noun2.length());
					tempAr.add(temp);
				}
				else {
					conceptStr = "";
				}
			} //end While Length > 0
			x++;
		} //end for (String str : tagged)
		
		
		
		//TAG TEMP ARRAY
		try {
			tempAr = Tag(tempAr);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(String str : tempAr){
			System.out.println(str);
		}
		
		
		
		String[] vArray = new String[arraySize];
		
		int j = 0;
		x = 0;
		for(String str : verbAr){
			x = str.indexOf('/');
			str = str.substring(0,x);
			vArray[j] = str;
			j++;
		}
		
		return vArray;
	}
	/* ************************************************************
	 *  Find Verb
	 *  Takes in tagged string and returns true if verb
	 *  @param strIN - String
	 *  @return boolean 
	  *************************************************************/
	public static boolean FindVerb(String strIN){
		String str = strIN.trim() + " ";
		String temp = "";
		String noun = "";
		int x = 0;
		int y = 0;
		boolean found = false;

			if(strIN.contains("/V")){ // finds noun break and return
				found = true;
			}
	
		if(found){  return true;    }
		else{  return false;  }
	} //****************************************************************************
	
	public String[][] Association(ArrayList<String> concept, String[] verb, String[] noun){
		ArrayList<String> noun1 	= new ArrayList<String>();
		ArrayList<String> noun2 	= new ArrayList<String>();
		ArrayList<String> verb1  	= new ArrayList<String>();
		ArrayList<String> delimited = new ArrayList<String>();
		int indexN1					= -1;
		int indexN2					= -1;
		int indexV					= -1;
		boolean add = false;
		for(String x : concept){
			delimited = delimiter(x);
			add = false;
			indexN1 = contains(delimited,noun, null);
			indexN2 = contains(delimited,noun, noun[indexN1]);
			indexV	= contains(delimited,verb,null);
			
			if(indexN1 != -1 && indexN2 != -1 && indexV != -1){
				add = true;
				System.out.println(noun[indexN1] + " -> " + noun[indexN2] + " : " + verb[indexV]);
			}
			
			indexN1 = -1;
			indexN2 = -1;
			indexV  = -1;
			//System.out.println(noun[indexN1] + " -> " + noun[indexN2] + " : " + verb[indexV]);
			
			
			delimited.clear();
		}
		
		String[][] asso = null;
		return asso;
	}
	
	
	/* ******************************************************************************
	 *  CONTAINS
	 *  takes in delimited array and a valid array and index. 
	 *  return newly found index.
	 *  return -1 if not found.
	 *  @param delimited 	- ArrayList<String>
	 *  @param validNouns	- String[]
	 *  @param index1		- int
	 */
	public int contains(ArrayList<String> delimited ,String[] valid ,String found){
		//for first noun indexIn = -1
		//indexIn is the index of the noun that is already found
		int index = -1;
		if( found != null){
			int indexOf = delimited.indexOf(found);
			System.out.println(delimited.get(indexOf));
		}
		
		
		for(int i = 0 ; i < valid.length ; i++){
			if(delimited.contains(valid[i]) && (!valid[i].equalsIgnoreCase(found))){
				index = i;
			}
		}
		
		if(index >= 0){  return index;}
		else{ return -1; }
	}
	

	
	public void setAssociation(String[][] arrayIn){
		associationArray = arrayIn;
	}
	public String[][] getAssociation(){
		return associationArray;
	}
	/////////////////////////////////////////////////////////////////////////////////////////
	//
	//
	//
	//
	
	

	
	public static void PrintToFile(String[][] plantArray, String directory) throws IOException{
		PrintWriter print = new PrintWriter("plantFile.txt");
		String[][] toPlantUML = plantArray;
		print.println("@startuml");
		int x = 0;
		for(int i = 0 ; i < toPlantUML.length;i++){
			if(toPlantUML[i][0] != null){
				x = toPlantUML[i][0].indexOf("/");
//				System.out.println(x);
//				System.out.println(toPlantUML[i][0].substring(0, x));
				print.print(toPlantUML[i][0].substring(0, x) +" - ");
				
				x = toPlantUML[i][2].indexOf('/');
//				System.out.println(x);
//				System.out.println(toPlantUML[i][2].substring(0, x));
				print.print(toPlantUML[i][2].substring(0, x) + " : ");
				
				x = toPlantUML[i][1].indexOf("/");
//				System.out.println(x);
//				System.out.println(toPlantUML[i][1].substring(0, x));
				print.println(toPlantUML[i][1].substring(0, x));	
			}
		}
		print.println("@enduml");
		print.close();


	}
	
	/*********************************************************************************************
	 * BUILD CLASS
	 * takes file name (that is in plantUML format) and converts to plant uml png file
	 * @param fileName - String
	 * @return void
	 */
	public void BuildClass(String fileName) throws IOException{
		//download graphviz from ubuntu app store
		// http://plantuml.sourceforge.net/api.html
		File source = new File(fileName);
		SourceFileReader reader = new SourceFileReader(source);
		List<GeneratedImage> list = reader.getGeneratedImages();
		File png = list.get(0).getPngFile();
		
	}
	
	/* *********************************************************************************************
	 * DELIMTER
	 *  Takes a sentence and stores each word into an Array
	 *  @param str -String
	 *  @return tempAr - String[]
	 */
	public ArrayList<String> delimiter(String str){

		String delimit = "[ ]";
		String[] tempAr = str.split(delimit);
		ArrayList<String> delimited = new ArrayList<String>();
		
		for(int i = 0 ; i < tempAr.length ; i++){
			delimited.add(tempAr[i]);
		}
		
		return delimited;
	} //********************************************************************************************
	
	
	
	public static String[] convertArrayList2Array(ArrayList<String> arrayList){
		int count = 0;
		for(String obj : arrayList){
			count++;
		}
		
		String[] array =  new String[count];
		
		count = 0;
		for(String obj : arrayList){
			array[count] = obj;
			count++;
		}
		
		return array;
	}
	
	

	
	
	
	public static boolean addToValidNouns(String noun){
		if(!(validNouns.contains(noun))){
			validNouns.add(noun);
			return true;
		}
		else{
			return false;
		}
	}
	
	public static ArrayList<String> getValidNouns(){
		return validNouns;
	}
	
	public static void setFileName(String filenameIN){
		filename = filenameIN;
	}
	
	public static String getFileName(){
		return filename;
	}
	
	
	
	
	


	

	
}
