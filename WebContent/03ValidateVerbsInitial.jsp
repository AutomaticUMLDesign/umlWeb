<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="backend.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

	<style>
		#header {
		    background-color:black;
		    color:white;
		    text-align:center;
		    padding:5px;
		}
		#section1 {
			width : 150px;
			float : left;
		}
		#section2 {
			width : 300px;
			float : left;
		}
		#section3 {
			width : 150px;
			float : left;
		}
		#footer {
			padding:5px;
			text-align:center;
		}
	</style>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<title>Validate Verbs</title>
</head>
<body>
<%
//Load Session Vars
int constant = (Integer)session.getAttribute("constant");
int startLoop = constant;
int endLoop = constant + 10;

//load all valid Nouns *********************************************************
if(constant == 0) {
	ArrayList<String> nounsAL 	= (ArrayList<String>)session.getAttribute("nounsAL");
	String[] Nouns 				= request.getParameterValues("id"); 
	for(int i = 0 ; i < Nouns.length ; i++){	nounsAL.add(Nouns[i]);		}	//add new nouns to arraylist
	
	String[] validNouns = new String[nounsAL.size()];
	
	for(int i = 0; i < nounsAL.size(); i++){	 validNouns[i] = nounsAL.get(i); } //convert arraylist to array
	session.setAttribute("nounsAL", nounsAL);
	session.setAttribute("validNouns", validNouns);
}
//************************************************************************************************


//load session vars
ArrayList<String> tagged 	= (ArrayList<String>)session.getAttribute("tagged");
ArrayList<String> concept 	= (ArrayList<String>)session.getAttribute("concept");
ArrayList<String> conceptT  = concept;
ArrayList<String> verbsAL	= (ArrayList<String>)session.getAttribute("verbsAL");
String[] validNouns			= (String[])session.getAttribute("validNouns");
String[] validVerbs 		= request.getParameterValues("verb");

ToPlant x = new ToPlant();


//add partical valid verbs to arraylist
if(constant > 0){
	for(int i = 0 ; i < validVerbs.length ; i++){
		verbsAL.add(validVerbs[i]);
	}
}


//FIND VERBS
String[] verbArray = x.FindVerbArray(concept,validNouns);
int size = verbArray.length;

ArrayList<String> assocSubStr = x.getAssocSubStr();
session.setAttribute("assocSubStr", assocSubStr);
session.setAttribute("verbsAL", verbsAL);
session.setAttribute("verbsUN", verbArray);  //UN-Validated Verbs
session.setAttribute("constant",endLoop);

//*************************************************************************************************

int progressbarNouns = 100;

int progressbarVerbs = endLoop * 100 / size;

%>

<div id="header"><h2> Please Select Valid Verbs </h2>
<p> Based on your previous selection here are the possible valid actions </p></div>
<div id="section1"><br></div>

<%if (verbArray.length > endLoop){ %>
<div id="section2">
	<form ACTION="04ValidateVerbsLoop.jsp" METHOD="post">
	<%for(int i = startLoop ; i < endLoop ; i++){ %>
		<input type="checkbox" name="verb" value="<%out.print(verbArray[i]); %>"> <%out.print(verbArray[i]);%> 
		<BR>
		<% 
	}%>
	<input type="submit" value="Submit">
	</form>
	</div>
<%} 
else{ 
session.setAttribute("constant",0);%>
	<div id="section2">
	<form ACTION="05ValidateAssociationsFIRST.jsp" METHOD="post">
	<%	
	for(int i = startLoop ; i < verbArray.length ; i++){%>		
		<input type="checkbox" name="verb" value="<%out.print(verbArray[i]); %>"> <%out.print(verbArray[i]);%> 
		<BR><% 
	}%>
	<p> Move to validate Associations -><input type="submit" value="Next"></p>

	</form>
	</div>
<%} %>



<div id="footer"> 

</div>
</body>
</html>