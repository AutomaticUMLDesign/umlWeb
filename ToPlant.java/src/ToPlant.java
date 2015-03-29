
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
	public static ArrayList<String> conceptArray = new ArrayList<String>();
	public static ArrayList<String> tag = new ArrayList<String>();
	public ArrayList<String> nouns = new ArrayList<String>();
	public ArrayList<String> verbs = new ArrayList<String>();
	public static String filename;
	
	
	public ToPlant(){  
		initUI(); 

	}
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
	
	public final void initUI(){
		final JPanel panel = new JPanel();
		final JPanel panel2 = new JPanel();
	    final JFrame frame = new JFrame();
	   
	    getContentPane().add(panel);

	    JButton Button = new JButton("Choose File");
	    Button.setBounds(30, 60, 250, 30);
	    Button.addActionListener(new ActionListener() {
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
					try{
						//conceptArray = 
						ReadFile(filename);
					}
					catch(IOException e){
						System.out.println("NOTHING");
					} 

					//TAG
					try {
						Tag();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						System.out.println("CLASSNOTFOUND");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String[][] toPlantUML = Process();
					
					try {
						PrintToFile(toPlantUML,directory);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					

				} //end else
	        } // end action
	        
	    }); //end action listener
	    
	    JButton Quit = new JButton("Quit");
	    Quit.setBounds(30, 60, 250, 30);
	    Quit.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent event) {
	               System.exit(0);
	          }
	     });
	    
	    panel.add(Button);
	    panel.add(Quit);
	    setTitle("Browse for file");
	    setSize(300, 200);
	    setLocationRelativeTo(null);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    
	} //end initUI
	
	
	
	public static void ReadFile(String filename) throws IOException { 
		
		ArrayList<String> concept = new ArrayList<String>();
		FileReader fr = new FileReader(filename);
		BufferedReader reader = new BufferedReader(fr);
		String fullStatement="";
		String nextLine;
		
		while((nextLine = reader.readLine()) != null){
			fullStatement += nextLine;
		}
		int periods = 0;
		for(char c : fullStatement.toCharArray()){
			if(c == '.'){
				periods++;				
			}
		}
		//System.out.println(periods);
		String delimit = "[.]";
		String[] tempAr = fullStatement.split(delimit);
		
		for(int i = 0; i < tempAr.length ; i++){
			concept.add(tempAr[i].trim());
		}
		setConceptArray(concept);
	}
	
	public final void Tag() throws ClassNotFoundException, IOException{
		
		MaxentTagger tagger = new MaxentTagger("taggers/bidirectional-distsim-wsj-0-18.tagger");
		ArrayList<String> temp = new ArrayList<String>();
		
		for(String str : conceptArray){
			str = tagger.tagString(str);
			temp.add(str);
		}
		setTagArray(temp);
	}
	
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
		
		
		String[][] plantArray = new String[row][3];
		for(int i = 0; i < row ; i++){
			plantArray[i][0] = toPlant[i][0];
			plantArray[i][1] = toPlant[i][1];
			plantArray[i][2] = toPlant[i][2];
		}
		return plantArray;
	}
	
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
		//download graphviz from ubuntu app store
		// http://plantuml.sourceforge.net/api.html
		File source = new File("plantFile.txt");
		SourceFileReader reader = new SourceFileReader(source);
		List<GeneratedImage> list = reader.getGeneratedImages();
		File png = list.get(0).getPngFile();
	}

	public static void main(String[] args) throws ClassNotFoundException, IOException{
		ToPlant ex = new ToPlant();
	    ex.setVisible(true); 
	           
	}
	
}