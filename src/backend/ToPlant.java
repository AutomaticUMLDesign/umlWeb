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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


//add jar files
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.SourceStringReader;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
// http://www.galalaly.me/index.php/2011/05/tagging-text-with-stanford-pos-tagger-in-java-applications/

public class ToPlant extends HttpServlet {
	public static ArrayList<String> 	conceptArray 	= new ArrayList<String>();
	public static ArrayList<String> 	tag 			= new ArrayList<String>();
	public static ArrayList<String>     validNouns 		= new ArrayList<String>();
	public static ArrayList<String> 	verbs 			= new ArrayList<String>();
	public static ArrayList<String>		AssocSubStr  	= new ArrayList<String>();
	public static String 				filename;
	public static String[][]			associationArray;
	
	
	/*	******************************************************************************************************
	 *  READ FILE  - HANDLED BY upLoadConceptStatment.java
	 *  reads txt file and stores each sentence into a cell of the concept array
	 *  @param filename - String
	 *  @return void
	 *********************************************************************************************************/


	

	/*  ****************************************************************************************************
	 * TAGGER
	 * 	- is an open source file that tags each word in a sentence with its grammar definition.
	 * 	Instructions on how to use tagger
	 *  http://www.galalaly.me/index.php/2011/05/tagging-text-with-stanford-pos-tagger-in-java-applications/
	 * 
	 */
	public static ArrayList<String> Tag(ArrayList<String> conceptArray) throws IOException, ClassNotFoundException{
		
		MaxentTagger tagger;
			tagger = new MaxentTagger("bidirectional-distsim-wsj-0-18.tagger");
		
		ArrayList<String> temp = new ArrayList<String>();
		
		//******************************************************************
		//REMOVE ***********************************************************
		//PrintWriter toText = new PrintWriter(new File("/home/kullen/workspace/UML-Designer/umlWeb/src/tempFiles/taggedArray.txt"));
		// ******************************************************************
		//*******************************************************************
		
		for(String str : conceptArray){
			str = tagger.tagString(str);
			temp.add(str);
			//toText.println(str + ".");
		}
		//toText.close();
		
		return temp;
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
	
	/** --------------------------------------------------------------------------------
	 * STRING TO PLANT Show Noun
	 * CONVETS String array Assoications to PLANTUML pic
	 * @param Array
	 * @throws IOException
	 * ------------------------------------------------------------------------------------*/
	public void NounToPlant(String[] Array) throws IOException{
		String fileName = "/home/kullen/workspace/UML-Designer/umlWeb/WebContent/NounDiagram.jpg";
		
		//int timeStamp = request.getParameter("timeStamp");
		
		OutputStream png = new FileOutputStream(fileName);
		String source = "@startuml\n";
		for(int i  = 0 ; i < Array.length ; i++){
			source += "class " +Array[i] +"\n";
		}
		source += "@enduml\n";
		
		
		SourceStringReader reader = new SourceStringReader(source);
		String desc = reader.generateImage(png);
		
	}
	
	
	
	/* ****************************************************************************
	 *  FIND Verb ARRAY
	 *  calls find verb multiple times and stores the verb to an array
	 *  removes duplicates.
	 *  @param tagged - ArrayList<String>
	 *  @return verbAr - String[]
	 *******************************************************************************/
	public String[] FindVerbArray(ArrayList<String> concept, String[] noun){
		ArrayList<String> verbAr 	= new ArrayList<String>();
		ArrayList<String> tempAr   	= new ArrayList<String>();
		ArrayList<String> assoc 	= new ArrayList<String>();
		ArrayList<String> nounAr 	= new ArrayList<String>();
		for(int i = 0 ; i < noun.length ; i++)
		{   
			nounAr.add(noun[i]);
		}
		
		int indexN1 = 0 , indexN2 = 0;
		
		int find;
		boolean contain1, contain2;
		String noun1=null,noun2=null;
		String temp = null;
		
		//Find Every line that contains multiple valid nouns
		//this reduces the number of sentences we have to search for verbs
		for(String str: concept)
		{
			find = 0;
			for(int i = 0 ; i < noun.length ; i++)
			{			
				if(str.contains(noun[i]))
				{
						find++;
				}
			}
			if(find > 1)
			{
				tempAr.add(str);  //add tagged to tempAr
			}
			
		} //end for (String str : concept)
		
		
		concept.clear(); //finished with concept array clear for reuse
		
		//loop through entire concept statement line by line
		for(String str : tempAr) {  //****************************************************
			concept = delimiter(str);
					
			indexN1 = 0; indexN2 = 0;
			
			
			/* loop through the current string.
			 *  this loop will loop through the entire string
			 *  string may contain more that one noun verb noun combinations
			 */
			while(indexN2 < concept.size()){
				noun1 = ""; noun2 = ""; contain1 = false ; contain2 = false;
				
				//find first valid noun
				for(int i = indexN2 ; i < concept.size() ; i++){
					if(nounAr.contains(concept.get(i))){

						noun1 = concept.get(i);
						indexN1 = i;
						contain1 = true ;
						break;
					}
				}
				
				//find second valid noun
				for(int i = indexN1+1 ; i < concept.size(); i++){
					if(nounAr.contains(concept.get(i))){
						noun2 = concept.get(i);
						indexN2 = i;
						contain2 = true;
						break;
					}
				}
				
				//if 2 nouns were found store substring onto assoc Array
				//assoc arraylist will be used in find associations method.
				if(contain1 && contain2)
				{
					temp = "";
					
					for(int i = indexN1 ; i <= indexN2 ; i++){
						temp = temp + " " +concept.get(i);
					}
					assoc.add(temp);					
				}
				else 
				{ 
					indexN2++;
				}
				
			} //end while
			
			
		}  //end for concept size  **************************************************
		
		
		setAssocSubStr(assoc); //set association substring array for later use.
		
		
		
		verbAr = FindVerb(assoc);
		String[] vArray = new String[verbAr.size()];
		
		int j = 0;
		int x = 0;
		for(String str : verbAr){
			x = str.indexOf('/');
			str = str.substring(0,x);
			vArray[j] = str;
			j++;
		}
		
		return vArray;
	}
	
	/*  FindVERB **********************************************************
	 *  takes in arraylist of strings that should contain string from 
	 *  noun1 to noun2 in each cell
	 *  finds all the verbs 
	 *  @param ArrayListIN - ArrayList<String>
	 *  @return verbList - ArrayList<String>
	 ***********************************************************************/
	public static ArrayList<String> FindVerb(ArrayList<String> ArrayListIN){
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> verbList = new ArrayList<String>();
		try {
			ArrayListIN = Tag(ArrayListIN);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			System.out.println("failed to tag findVerb Array");
		}
		
		//take each cell split it into another arraylist and check word for word isVerb?
		for(String x : ArrayListIN){
			temp = delimiter(x);
		
			for(String str : temp){
				if(IsVerb(str) && !(verbList.contains(str))){
					verbList.add(str);
					
				}
			}
			temp.clear();
		}
		
		return verbList;
	}
	
	
	/* ************************************************************
	 *  Is Verb
	 *  Takes in tagged string and returns true if verb
	 *  @param strIN - String
	 *  @return boolean 
	  *************************************************************/
	public static boolean IsVerb(String strIN){
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
	
	
	
	
	/** **********************************************************************************************
	 * FIND ASSOCIATIONS - assocSubStrIN is an arrayList found when verbs were found.
	 * It contains a string that begins with a valid noun and ends with a valid now.
	 * This "substring" is checked against the array of valid verbs. If a valid verb
	 * is found the 1st noun the 2nd noun and the verb are stored in the tempAssoc ArrayList 
	 * for validation
	 * @param assocSubStrIN
	 * @param verb
	 * @param noun 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 ******************************************************************************************************/
	
	public ArrayList<String> FindAssociations(ArrayList<String> assocSubStrIN, String[] verb, String[] noun) throws ClassNotFoundException, IOException{
		
		ArrayList<String> temp = new ArrayList<String>();
		ArrayList<String> tempAssoc = new ArrayList<String>();
		String noun1 = "";
		String noun2 = "";
		
		
		ArrayList<String> verbAL = new ArrayList<String>();		//CONVERT verb[] to ArrayList
		for(int i = 0; i < verb.length ; i++){
			verbAL.add(verb[i]);
		}
		
		assocSubStrIN = Tag(assocSubStrIN);  //TAG SUBSTRING 

		for(String str : assocSubStrIN){
			temp = delimiter(str);
			
			int slash = 0;
			noun1 = temp.get(0);
			noun2 = temp.get(temp.size()-1);
			slash = noun1.indexOf('/');
			noun1 = noun1.substring(0,slash);
			//System.out.println(noun1);
			
			
			slash = noun2.indexOf('/');
			noun2 = noun2.substring(0,slash);
			//System.out.println(noun2);
			
			String plant = "";
			for(String x : temp){
				slash = 0;
				if(IsVerb(x)){
					
					slash = x.indexOf('/');
					x = x.substring(0,slash);
					//System.out.println(x);

					if(verbAL.contains(x)){
						plant = noun1 + " - " + noun2 + " : " + x ;
						
						if(!(tempAssoc.contains(plant))){
							tempAssoc.add(plant);
							System.out.println(plant);
						}
					}
				}
			}
			
		}
		
		for(String str : tempAssoc){
			System.out.println(str);
		}
		
		return tempAssoc;
	}
	
	/** --------------------------------------------------------------------------------
	 * STRING TO PLANT
	 * CONVETS String array Assoications to PLANTUML pic
	 * @param Array
	 * @throws IOException
	 * ------------------------------------------------------------------------------------*/
	public void StringToPlant(String[] Array) throws IOException{
		String fileName = "/home/kullen/workspace/UML-Designer/umlWeb/WebContent/images/ClassDiagram.jpg";
		
		OutputStream png = new FileOutputStream(fileName);
		String source = "@startuml\n";
		for(int i  = 0 ; i < Array.length ; i++){
			source += Array[i] +"\n";
		}
		source += "@enduml\n";
		
		
		SourceStringReader reader = new SourceStringReader(source);
		String desc = reader.generateImage(png);
		
	}

	
	/*********************************************************************************************
	 * BUILD CLASS DIAGRAM  - FILE TO PLANT
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
	public static ArrayList<String> delimiter(String str){

		String delimit = "[ ]";
		String[] tempAr = str.split(delimit);
		ArrayList<String> delimited = new ArrayList<String>();
		
		for(int i = 0 ; i < tempAr.length ; i++){
			delimited.add(tempAr[i]);
		}
		
		return delimited;
	} //********************************************************************************************
	
	
		
	
	//GETTERS AND SETTERS 
	
	public void setAssociation(String[][] arrayIn){
		associationArray = arrayIn;
	}
	public String[][] getAssociation(){
		return associationArray;
	}
	
	public static ArrayList<String> getValidNouns(){
		return validNouns;
	}
	
	public static void setAssocSubStr(ArrayList<String> associations){
		AssocSubStr= associations;
	}
	public ArrayList<String> getAssocSubStr(){
		return AssocSubStr;
	}
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
	
	
	
	
	
	// NOT USED FOR NOW BUT MAY BE USEFUL	
//		/** ***********************************************************************************
//		 *  
//		 * @param plantArray - String[][] 
//		 * @param directory	- directory
//		 * @throws IOException
//		 */
	//	
//		public static void PrintToFile(String[][] plantArray, String directory) throws IOException{
//			PrintWriter print = new PrintWriter("plantFile.txt");
//			String[][] toPlantUML = plantArray;
//			print.println("@startuml");
//			int x = 0;
//			for(int i = 0 ; i < toPlantUML.length;i++){
//				if(toPlantUML[i][0] != null){
//					x = toPlantUML[i][0].indexOf("/");
////					System.out.println(x);
////					System.out.println(toPlantUML[i][0].substring(0, x));
//					print.print(toPlantUML[i][0].substring(0, x) +" - ");
//					
//					x = toPlantUML[i][2].indexOf('/');
////					System.out.println(x);
////					System.out.println(toPlantUML[i][2].substring(0, x));
//					print.print(toPlantUML[i][2].substring(0, x) + " : ");
//					
//					x = toPlantUML[i][1].indexOf("/");
////					System.out.println(x);
////					System.out.println(toPlantUML[i][1].substring(0, x));
//					print.println(toPlantUML[i][1].substring(0, x));	
//				}
//			}
//			print.println("@enduml");
//			print.close();
	//
	//
//		}


	

	
}
