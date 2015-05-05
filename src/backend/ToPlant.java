package backend;


import java.io.BufferedReader;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
//import java.io.PrintWriter;
import java.util.*;
//add sql libraries
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.SQLException;


//add jar files
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import net.sourceforge.plantuml.SourceStringReader;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
// http://www.galalaly.me/index.php/2011/05/tagging-text-with-stanford-pos-tagger-in-java-applications/

public class ToPlant {
	public static ArrayList<String> 	conceptArray 	= new ArrayList<String>();
	public static ArrayList<String> 	tag 			= new ArrayList<String>();
	public static ArrayList<String>     validNouns 		= new ArrayList<String>();
	public static ArrayList<String> 	verbs 			= new ArrayList<String>();
	public static ArrayList<String>		AssocSubStr  	= new ArrayList<String>();
	public static String 				filename;
	public static String[][]			associationArray;
	public static ArrayList<String> 	actors      	= new ArrayList<String>();
	public static ArrayList<String> 	useCaseStrings  = new ArrayList<String>();
	public static ArrayList<String> 	ssdStrings  	= new ArrayList<String>();
	public static HashMap<String, ArrayList<String>> aMapforSSD = new HashMap<String, ArrayList<String>>();
	public static ArrayList<String>     useCaseVerbs    = new ArrayList<String>();
	public static DBConnection connect = new DBConnection();
	
	
	/*	******************************************************************************************************
	 *  READ FILE  - HANDLED BY upLoadConceptStatment.java
	 *  reads txt file and stores each sentence into a cell of the concept array
	 *  @param filename - String
	 *  @return void
	 *********************************************************************************************************

	

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
		

		for(String str : conceptArray){
			str = tagger.tagString(str);
			temp.add(str);
		}
		
		return temp;
	}
	
	
	/** ----------------------------------------------------------------
	 * Tag individual string
	 * @param str
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static String tagStr(String str) throws IOException, ClassNotFoundException{
		MaxentTagger tagger;
		tagger = new MaxentTagger("bidirectional-distsim-wsj-0-18.tagger");
		str = tagger.tagString(str);
		
		return str;
	}
	
	
	/** -------------------------------------------------------------------
	 * un taggs strings.
	 * @param str
	 * @return
	 */
	public static String unTagger(String str){
		
		String temp = "";
		int space = 0;
		str = str + " ";
		int x = str.indexOf('/');
		while(x > 0)
		{
			temp =  temp + str.substring(0,x);
			
			str = str.trim();
			space = str.indexOf(" ");
			if(space > 0)
			{
				str = str.substring(space);
//				System.out.println(": " + str);
				x = str.indexOf('/');	
			}
			else { x = -1; }
		}
		
		if(temp.length() > 0) { return temp; }
		else {  return str; }
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
			
				found = IsNoun(tempAr[i]);
				if (found == true){ // find index of space
					int j = i; 
					int num = 0;
					noun = "";
					
					while(j < tempAr.length && IsNoun(tempAr[j])){
						
						noun += " " + tempAr[j];
						num++;
						j++;
					}
					i = j;
					j = 0;
					int space = 0;
					noun = noun.trim();
					String temp = "", temp2 = "";
					for(int q = 0 ; q < num-1; q++){
						j = noun.indexOf('/');
						temp = noun.substring(0,j);
						space = noun.indexOf(' ');
						noun = noun.substring(space);
						noun=noun.trim();
						temp2 += " " + temp;
					}
					temp2+= " " +noun;
					noun = temp2;
					 
					//Check if word has already been added
					if(!(nounAr.contains(noun))){  
						nounAr.add(noun);  
						arraySize++;
					}
				}
				found = false;
			}
		}

		//Convert ArrayList to String Array
		String[] nArray = returnUnTaggedArray(nounAr);
		return nArray;
	}
	//****************************************************************************
	
	/* **************************************************************
	 * Find Noun
	 * Takes in tagged string and returns true if its a noun
	 * @param strIN - String
	 * @return boolean
	 ********************************************************************/
	public static boolean IsNoun(String strIN){
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
	 *  FIND Verb ARRAY  ==== THIS IS NASTY
	 *  Takes in concept Statement Array
	 *  Stores sentences that contain more than one noun into temp Array
	 *  then searches the temp array for noun1 and noun2 
	 *  	repeat until entire string is parsed
	 *  	repeats until temparray is fully parsed
	 *  When two nouns are found search for verb in that noun1 to noun2 substring 
	 *  then store verb and store that substring for associations.
	 *  removes duplicates.
	 *  @param tagged - ArrayList<String>
	 *  @return verbAr - String[]
	 *******************************************************************************/
	public String[] FindVerbArray(ArrayList<String> conceptIN, String[] noun){
		ArrayList<String> concept 	= conceptIN;
		ArrayList<String> verbAr 	= new ArrayList<String>();
		ArrayList<String> tempAr   	= new ArrayList<String>();
		ArrayList<String> assoc 	= new ArrayList<String>();	//pre store association substring for find associations
		ArrayList<String> nounAr 	= new ArrayList<String>();  //To better search the noun Array convert to ArrayList
		for(int i = 0 ; i < noun.length ; i++)
		{   
			nounAr.add(noun[i].trim());
		}
		setValidNouns(nounAr);
		String[] loop1 = new String[3];
		String[] loop2 = new String[3];
		int indexN1 = 0 , indexN2 = 0;
		int find;
		boolean contain1, contain2;
		String noun1=null,noun2=null;
		String temp = null;
		
		/* *******************************************************
		 * FILTER CONCEPT ARRAY
		 * Find Every line that contains multiple valid nouns
		 * this reduces the number of sentences we have to search for verbs
		 */
		for(String str: concept)							
		{	
			//System.out.println(str)	;											
			find = 0;
			for(int i = 0 ; i < noun.length ; i++)
			{
				//System.out.println("NOUNS: |" + noun[i].trim() + "|");
				if(str.contains(nounAr.get(i)))  {   find++;   }
			}
			if(find > 1) { tempAr.add(str); } 
			
		} //end Filter ----------------------------------------------------------

		concept.clear(); //finished with concept array clear for reuse

		//loop through Filtered concept statement line by line
		for(String str : tempAr) {  //****************************************************
			concept = delimiter(str);  //see Delimiter method

			indexN1 = 0; indexN2 = 0;
						
			while(indexN2 < concept.size())										//this loop looks at each word in tempAr
			{																	//via the newly delimited concept ArrayList.
				noun1 = ""; noun2 = ""; contain1 = false ; contain2 = false;    //searches for 2 nouns. hopefully there is a
				for(int i = 0 ; i < 3 ; i++){
					loop1[i] = "";
					loop2[i] = "";												// verb between them. loops back until there are no														 
				}
				
				
				//find first valid noun											//more words to search.
				for(int i = indexN2 ; i < concept.size() ; i++){				
					
					if(nounAr.contains(concept.get(i))){						//Find First Noun in the sentence.
						noun1 = concept.get(i);									//then break
						indexN1 = i;
						contain1 = true ;
						break;
					}
				} //------------------------------------------------------
				
				//find second valid noun --------------------------------
				for(int i = indexN1+1 ; i < concept.size(); i++){
					
					if(nounAr.contains(concept.get(i))){
						noun2 = concept.get(i);
						indexN2 = i;
						contain2 = true;
						break;
					}
				} //-----------------------------------------------------------

				if(contain1 && contain2){										//if 2 nouns were found store substring onto assoc Array
					temp = "";													//assoc arraylist will be used in find associations method.

					for(int i = indexN1 ; i <= indexN2 ; i++){					//loop from noun 1 to noun 2;
						temp = temp + " " +concept.get(i);
					}
					assoc.add(temp);					
				}
				else  {  indexN2++; }
				
			} //end while // finish delimited string
			
		}  //end String str :tempAr  **************************************************
		
		
		setAssocSubStr(assoc); //set association substring array for later use.
		
		verbAr = FindVerb(assoc);
		String[] vArray = new String[verbAr.size()];
		for(int i = 0 ; i < vArray.length; i++){
			//System.out.println(vArray[i]);
		}
		int j = 0;
		int x = 0;
		for(String str : verbAr){ //--------
			
			x = str.indexOf('/');
			str = str.substring(0,x);
			vArray[j] = str;
			j++;
		} //---------------------------------
		
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

			slash = noun2.indexOf('/');
			noun2 = noun2.substring(0,slash);

			
			String plant = "";
			for(String x : temp){
				slash = 0;
				if(IsVerb(x)){
					
					slash = x.indexOf('/');
					x = x.substring(0,slash);

					if(verbAL.contains(x)){
						plant = noun1 + " - " + noun2 + " : " + x ;
						
						if(!(tempAssoc.contains(plant))){
							tempAssoc.add(plant);
						}
					}
				}
			}
			
		} //end for(String str ...
		

		Collections.sort(tempAssoc); //organize 
		
		return tempAssoc;
	}
	
	
	
	/** --------------------------------------------------------------------------------
	 * STRING TO PLANT
	 * CONVETS String array Assoications to PLANTUML pic
	 * @param Array
	 * @throws IOException
	 * ------------------------------------------------------------------------------------*/
	public void StringToPlant(String[] Array, UUID idNumber) throws IOException{
		
		String fileName = "/home/kullen/workspace/UML-Designer/umlWeb/WebContent/images/ClassDiagram.png";
		
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

	
	/** --------------------------------------------------------------------------------
	 * STRING TO PLANT
	 * CONVETS String array Assoications to PLANTUML pic
	 * @param Array
	 * @throws IOException
	 * ------------------------------------------------------------------------------------*/
	public static void StringToPlantUseCase() throws IOException{
		
		//double timeStamp = UploadConceptStatement.getTimeStamp();
		//System.out.println(timeStamp);

		String fileName = "/home/kullen/workspace/UML-Designer/umlWeb/WebContent/images/UseCaseDiagram.png";
		
		OutputStream png = new FileOutputStream(fileName);
		String source = "@startuml\n";
		source += "left to right direction\n";
		source += "skinparam packageStyle rect\n";
		
		
		for(String str : actors){
			if(str.contains(" ")){
				str = str.replaceAll(" ", "");
			}
			source += "actor " + str + "\n";
		}
		 
		source += "rectangle {\n";
		for(int i  = 0 ; i < useCaseStrings.size() ; i++){
			source += useCaseStrings.get(i) +"\n";
		}
		source += " }\n@enduml\n";
		
		//System.out.println(source);
		
		
		SourceStringReader reader = new SourceStringReader(source);
		String desc = reader.generateImage(png);
		
	}
	
	
	/*********************************************************************************************
	 * GENERATE USE CASE STRINGS
	 * makes plant readable strings from actors and associations
	 * 
	 * @return void
	 * --------------------------------------------------------------------------------------------
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 */
	public void GenerateUseCaseStrings() throws ClassNotFoundException, IOException{
		boolean found = false;

		ArrayList<String>tagActors = Tag(actors);
//		for (String s: actors){
//			s = tagStr(s);
//		}

		boolean toggle = false;

		ArrayList<String> AssocSubStrx = Tag(AssocSubStr);
		for(String s: tagActors){
			String first = s;
			for(String y: AssocSubStrx){
				found = false;

				String verb = "";
				if(y.contains(first.trim())) {
					String xv = y;
					Scanner linescan = new Scanner(xv);
					while (linescan.hasNext()){
						found = false;
						String kp = linescan.next();
						System.out.println(kp);
						if(IsVerb(kp)){
							verb = kp;
							found = true;
							break;
						}
					}
				}
				if(found == true){
					first = unTagger(first);
					verb = unTagger(verb);
					if(first.contains(" ")){
						first = first.replaceAll(" ", "");
					}
					if(!toggle){
					first += " -> (" + verb + ")";
					toggle = true;
					}
					else{
						String kc = first;
						first = "(" + verb + ") <- " + first;
						toggle = false;
					}
					useCaseVerbs.add(verb);
					useCaseStrings.add(first);
					/*System.out.println("First: " +first);*/
					if(!(aMapforSSD.containsKey(verb))){
						ArrayList<String> aList = new ArrayList<String>();
						aList.add(s);
						aMapforSSD.put(verb, aList);
					}
					else {
						ArrayList<String> aList = aMapforSSD.get(verb);
						aList.add(s);
	
					}
					
				} //if found
				first = s;
			}
		}

	}
	/*********************************************************************************************
	 * GENERATE USE SSD STRINGS
	 * makes plant readable strings from s and associations
	 * 
	 * @return void
	 * --------------------------------------------------------------------------------------------
	 */

	public void GenerateSSDStrings(){

		for(String v: useCaseVerbs){
			ArrayList<String> list = aMapforSSD.get(v);
			String[] myAr = new String[list.size()];
			for(int x =0; x< myAr.length;x++){
				myAr[x] = list.get(x);

			}
			for(int x=0; x<myAr.length - 1; x++){
				for(int y=1; y<myAr.length; y++){
					String first = myAr[x];
					String second = myAr[y];
					String toAdd = first + " -> " + second + ": " + v;
					ssdStrings.add(toAdd);
				}
			}

		}
	}
	
	/** -------------------------------------------------------------
	 *  
	 * 	@throws IOException
	 */
	public static void StringToPlantSSD() throws IOException{
		
		//double timeStamp = UploadConceptStatement.getTimeStamp();

		String fileName = "/home/kullen/workspace/UML-Designer/umlWeb/WebContent/images/SSDDiagram.png";
		
		OutputStream png = new FileOutputStream(fileName);
		String source = "@startuml\n";

		for(String str : actors){
			if(str.contains(" ")){
				str = str.replaceAll(" ", "");
			}
			source += "actor " + str + "\n";
		}
		for(int i  = 0 ; i < ssdStrings.size() ; i++){
			
			source += ssdStrings.get(i) +"\n";
		}
		source += "@enduml\n";
		
		System.out.println(source);
		
		SourceStringReader reader = new SourceStringReader(source);
		String desc = reader.generateImage(png);
	} // -------------------------------------------------------------
	
	
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
	} //***
	
	
	
	
	/**********************************************************************************************
	 * converts arrayList to Array and Untags 
	 * @param arrayListIN	- ArrayList<String>
	 * @return array 		- String[]
	 */
	public static String[] returnUnTaggedArray(ArrayList<String> arrayListIN){
		ArrayList<String> arrayList = arrayListIN;
		String[] array = new String[arrayList.size()];
		String temp = "";
		int x = 0;
		for(int i = 0 ; i < arrayList.size() ; i++){
			temp = arrayList.get(i);
			x = temp.indexOf('/');
			temp = temp.substring(0,x);
			array[i] = temp;
		}
		return array;
	}
	
	
	public static String[] contains(ArrayList<String> conceptAL, ArrayList<String> nounAL, int indexIN){
		String[] contain = new String[3];
		String noun = null;
		int index = indexIN;
		String boo = "false";
		for(int i = index ; i < conceptAL.size() ; i++){
			if(nounAL.contains(conceptAL.get(i))){
				noun = conceptAL.get(i);
				index = i;
				boo = "true";
			}
		}
		contain[0] = noun;
		contain[1] = index+"";
		contain[2] = boo;
		
		return contain;
	}
	
	
	//GETTERS AND SETTERS 
	
	public void setAssociation(String[][] arrayIn){
		associationArray = arrayIn;
	}
	public String[][] getAssociation(){
		return associationArray;
	}
	
	public static void setValidNouns(ArrayList<String> nouns){
		validNouns = nouns;
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
	public static void setConceptArray(ArrayList<String> conceptIN) throws ClassNotFoundException, SQLException{
		String concept;
		conceptArray = conceptIN;
		concept = DBConnection.convertToString(conceptIN);
//		System.out.print(concept);
		try {
			DBConnection.conceptIn(concept);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
//			String sql = "INSERT INTO uml_design (concept_statement, tagged, valid_nouns, valid_verbs, valid_associations) VALUES (" + concept + ", null, null, null, null);";

//			System.out.print("test");
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

	public void setActors(ArrayList<String> actorsIn){
		actors = actorsIn;
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
