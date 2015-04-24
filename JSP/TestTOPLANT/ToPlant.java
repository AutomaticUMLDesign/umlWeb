/**
* ToPlant.java  (possible rename to UmlMagic.java)
*
* Ubuntu compile and run
* compile :  javac -cp plantuml.jar:stanford-postagger.jar ToPlant.java
* run	  :  java -cp ".:stanford-postagger.jar":".:plantuml.jar" ToPlant
*
* @version: April 2015
* @authors: 
*	Kullen Williams
*	Wayne Nolen
*	Davis Quarles
*	Slade Meriwether
*	Dillon Beck
*	Drew Hoover
*/


import java.awt.FileDialog;
import java.awt.GridLayout;
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

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

//add jar files
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

// http://www.galalaly.me/index.php/2011/05/tagging-text-with-stanford-pos-tagger-in-java-applications/

public class ToPlant extends JFrame{
	public static ArrayList<String> 	conceptArray 	= new ArrayList<String>();
	public static ArrayList<String> 	tag 			= new ArrayList<String>();
	public ArrayList<String> 			nouns 			= new ArrayList<String>();
	public ArrayList<String> 			verbs 			= new ArrayList<String>();
	public static String 				filename;
	
	public static void main(String[] args) throws ClassNotFoundException, IOException{
		ToPlant ex = new ToPlant();
	    ex.setVisible(true);        
	}
	
	public ToPlant(){   initUI(); }

	public final void initUI(){
		final JPanel panel = new JPanel();
		final JPanel panel2 = new JPanel();
	    final JFrame frame = new JFrame();
	   
	    getContentPane().add(panel);

	    
	    //  CHOOSE FILE BUTTON *************************************************************************
	    JButton Button = new JButton("Choose File");
	    Button.setBounds(30, 60, 250, 30);
	    Button.addActionListener(new ActionListener() { //click listener
	        @SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent event) {
	        	FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
				//fd.setDirectory("C:\\");
				fd.setFile("*.txt");
				fd.setVisible(true);
				String filename = fd.getFile();
				if(filename == null){
					System.out.println("Cancelled");
				}
				else { //read file
					//System.out.println("You chose "+ filename);
					String directory = fd.getDirectory();
					
					//System.out.println(directory);
					filename = directory+filename;
					setFileName(filename);
					
					frame.dispose();
					 //READ FILE *************************************************
					try{  ReadFile(filename);  }
					catch(IOException e){ System.out.println("NOTHING");  } 
					//************************************************************

					//TAG ************************************************************************
					try {  Tag();  } 
					catch (ClassNotFoundException e) {	System.out.println("CLASSNOTFOUND"); } 
					catch (IOException e) 			 {  e.printStackTrace(); }
					// ****************************************************************************
					
					String[][] toPlantUML = Process();
					
					try {  PrintToFile(toPlantUML,directory); } 
					catch (IOException e) { e.printStackTrace();}

				} //end else
	        } // end action
	        
	    }); 
	    //end listener *********************************************************************************
	    //**********************************************************************************************
	    //**********************************************************************************************
	    
	    
	    
	    //QUIT BUTTON **********************************************************************************
	    JButton Quit = new JButton("Quit");
	    Quit.setBounds(30, 60, 250, 30);
	    Quit.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent event) {
	               System.exit(0);
	          }
	     });
	    //**********************************************************************************************
	    
	    panel.add(Button);
	    panel.add(Quit);
	    setTitle("Browse for file");
	    setSize(300, 200);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    
	} //end initUI
	
	
	
	/*	******************************************************************************************************
	 *  READ FILE 
	 *  reads txt file and stores each sentence into a cell of the concept array
	 *  @param filename - String
	 *  @return void
	 *********************************************************************************************************/
	public static void ReadFile(String filename) throws IOException { 
		
		ArrayList<String> concept = new ArrayList<String>();
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
	// **************************************************************************************************
	//***************************************************************************************************

	
	
	
	
	
	/*  ****************************************************************************************************
	 * TAGGER
	 * 	- is an open source file that tags each word in a sentence with its grammar definition.
	 * 	Instructions on how to use tagger
	 *  http://www.galalaly.me/index.php/2011/05/tagging-text-with-stanford-pos-tagger-in-java-applications/
	 * 
	 */
	public final void Tag() throws ClassNotFoundException, IOException{
		
		MaxentTagger tagger = new MaxentTagger("taggers/bidirectional-distsim-wsj-0-18.tagger");
		ArrayList<String> temp = new ArrayList<String>();
		
		for(String str : conceptArray){
			str = tagger.tagString(str);
			temp.add(str);
		}
		setTagArray(temp);
	}
	// **************************************************************************************************

	
	
	
	/*
	* Process - loops through the concept sentence array and finds two nouns and if their in a verb between them
	* If there is not a verb between the 2 nouns it will exclude the pair.
	* @return plantArray[][] : String[][]
	*/
	
	public static String[][] Process(){
		ArrayList<String> tagged = getTagArray();
		String[][] toPlant = new String[1000][3];
		String noun1 = "";
		String verb = "";
		String noun2 = "";
		String verbPhrase = "";
		int x = 0;
		int y = 0;
		int row = 0;
		int column = 0;
		String temp = "";
		boolean found = true;
		String[] nounARRAY = FindNounArray();  //TESTING finds all nouns first
		
		//this giant loop will be replaced  00000000000000000000000000000000000000000000000
		for(String str : tagged){
			while (str.length()>0){
				
				found = false;
				noun1 = FindNoun(str);//Find NOUN 		need index1
				while(!found){
					x = str.indexOf(noun1);
					
					//FIND NEXT NOUN   	need index2
					noun2 = FindNoun(str.substring(x+ noun1.length()) ); // finds noun after noun1
					if(noun2.equalsIgnoreCase("NOT_FOUND")){
						str = "";
						break;
					}
					y = str.indexOf(noun2);
					//find verb between noun1 and nound2
					verb = FindVerb(str.substring(x,y));
					
					//str from noun1 to noun2
					if(verb.equalsIgnoreCase("NOT_FOUND")){
						noun1 = noun2;
						noun2 = "";
						verb = "";
					}
					else{
						
						found = true;
						toPlant[row][0] = noun1;
						toPlant[row][1] = verb;
						toPlant[row][2] = noun2;
						
						row++;
					}
					str = str.substring(y);
					//if no verb noun1 = noun2
					//loop
				}
				//break;
			}
		} //end for
		// 000000000000000000000000000000000000000000000000000000000000
		
		String[][] plantArray = new String[row][3];
		for(int i = 0; i < row ; i++){
			plantArray[i][0] = toPlant[i][0];
			plantArray[i][1] = toPlant[i][1];
			plantArray[i][2] = toPlant[i][2];
		}
		return plantArray;
	}
	
	 
	/* WORKING ON IT
	* FindNounArray
	*	Takes the concept sentence array and passes a sentence to FindNouns.
	*	Then it takes the substring of the current sentence, removing the previously
	*	found noun, and re-submits it until the sentence has been traversed.
	*	Repeat for next Sentence
	*
	* @return nArray : String[]
	*/
	public static String[] FindNounArray(){
		ArrayList<String> nounAr = new ArrayList<String>();
		int i = 0;
		boolean found = false;
		int x = 0;
		String noun = "";
		
		for(String str: tag){
			found = false;
			noun = FindNoun(str);//Find NOUN 		need index1
			nounAr.add(noun);
			x = str.indexOf(noun);
			while(str.length() > noun.length()){
				
				
				str = str.substring(x + noun.length());
				noun = FindNoun(str);
				nounAr.add(noun);
				x = str.indexOf(noun);
			}
		}
		
		for(String str1 : nounAr){
			//System.out.println(str1);
		}
		
		String[] nArray = {"1"};
		return nArray;
	}
	
	/*
	* FindNoun 
	* looks at each word in a sentence until it finds a noun and returns that noun or "NOT_FOUND"
	* @param strIN : String
	* @return noun : String
	*/
	public static String FindNoun(String strIN){
		String str = strIN.trim();
		String temp = "";
		String noun = "";
		int x = 0;
		int y = 0;
		boolean found = false;
		while(!found && str.length() > 0){
			y = str.indexOf(" ");
			if(y < 0){
				break;
			}
			temp = str.substring(x,y);
			if(temp.contains("/N")){
				found = true;
				noun = temp;
			}
			str = str.substring(y).trim();
		}		
		if(found){
			//System.out.println(noun);
			return noun;
		}
		else{
			//System.out.println("NOT");
			return "NOT_FOUND";
		}
	}
	/*
	*	FindVerbs
	*	accepts a string that is in between 2 nouns and sees if there 
	*	is a verb between them. If there is not a verb return "NOT_FOUND".
	*	ex sentence:  "noun1 word word verb noun2"
	* 	@params strIN : String
	*	@return verb : String
	*/
	public static String FindVerb(String strIN){
		//System.out.println("TEST " + strIN);
		String str = strIN.trim();
		String temp = "";
		String verb = "";
		int x = 0;
		int y = 0;
		boolean found = false;
		while(!found && str.length() > 0){
			y = str.indexOf(" ");
			//System.out.println(y);
			if(y >= 0){
				temp = str.substring(x,y-1);
				if(temp.contains("/V")){
					found = true;
					verb = temp;
				}
				str = str.substring(y).trim();
			}
			else{
				break;
			}
		}	

		//some basic banned verbs
		ArrayList<String> bannedWord = new ArrayList<String>();
		bannedWord.add("is/VB");
		bannedWord.add("are/VB");
		bannedWord.add("be/V");
		bannedWord.add("use/V");
		bannedWord.add("has/VB");
		boolean banned = bannedWord.contains(verb);
		if(found && !banned){
			return verb;
		}
		else{
			//System.out.println("not found");
			return "NOT_FOUND";
		}
	}
	
	/*
	* Takes the noun verb noun array and prints to a text file in class diagram plantUML format
	*/
	public static void PrintToFile(String[][] plantArray, String directory) throws IOException{
		String classFile = "classPlant.txt";
		PrintWriter print = new PrintWriter(classFile);
		String[][] toPlantUML = plantArray;
		print.println("@startuml");
		int x = 0;
		//print to file
		for(int i = 0 ; i < toPlantUML.length;i++){
			if(toPlantUML[i][0] != null){
			//noun1
				x = toPlantUML[i][0].indexOf("/");
//				System.out.println(x);
//				System.out.println(toPlantUML[i][0].substring(0, x));
				print.print(toPlantUML[i][0].substring(0, x) +" - ");
			//noun2	
				x = toPlantUML[i][2].indexOf('/');
//				System.out.println(x);
//				System.out.println(toPlantUML[i][2].substring(0, x));
				print.print(toPlantUML[i][2].substring(0, x) + " : ");
			//verb
				x = toPlantUML[i][1].indexOf("/");
//				System.out.println(x);
//				System.out.println(toPlantUML[i][1].substring(0, x));
				print.println(toPlantUML[i][1].substring(0, x));	
			}
		}
		print.println("@enduml");
		print.close();
		
		//IMAGE OUTPUT
		//download graphviz from ubuntu app store
		// http://plantuml.sourceforge.net/api.html
		File source = new File(classFile);
		SourceFileReader reader = new SourceFileReader(source);
		List<GeneratedImage> list = reader.getGeneratedImages();
		File png = list.get(0).getPngFile();
	}

	//SETS AND GETS
	
	public static void setFileName(String filenameIN){
		filename = filenameIN;
	}
	
	public static String getFileName(){
		return filename;
	}
	
	public static void setConceptArray(ArrayList<String> conceptIN){
		conceptArray = conceptIN;
	}
	public static ArrayList<String> getConceptArray(){
		
		return conceptArray;
	}
	public static void setTagArray(ArrayList<String> tagIN){
		tag = tagIN;
	}
	public static ArrayList<String> getTagArray(){
		return tag;
	}


	
}
