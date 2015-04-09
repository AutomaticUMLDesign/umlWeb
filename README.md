# TestTOPLANT
README - ish

Folder Structure: everything is saved in the same folder
WIZARD
  -ToPlant.java
  -plantuml.jar
  -stanford-postagger.jar
  -concept.txt
  -tagger (folder)
	-tagger File


compile : 
	javac -cp plantuml.jar:stanford-postagger.jar ToPlant.java

run	: 
	java -cp ".:stanford-postagger.jar":".:plantuml.jar" ToPlant


Current output:
	classPlant.txt  //text of plant UML
	classPlant.png  //image of plant UML





*i'm not sure what we have to include -cp to compile and run but oh well.
