<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="backend.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
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
<title>Choose Valid Associations</title>
</head>
<body>
<%
ToPlant x = new ToPlant();


ArrayList<String> tagged 		= (ArrayList<String>)session.getAttribute("tagged");
ArrayList<String> concept 		= (ArrayList<String>)session.getAttribute("concept");
ArrayList<String> assocSubStr 	= (ArrayList<String>)session.getAttribute("assocSubStr");
String[] validNouns				= (String[])session.getAttribute("validNoun");

// FINISH VALIDATE VERBS ------------------------------------------------------------------------------------------
ArrayList<String> verbsAL	= (ArrayList<String>)session.getAttribute("verbsAL");  //building valid verb arrayList
String[] validVerbs 		= request.getParameterValues("verb");					//validated from the pervious page.
String[] verbsUN			= (String[])session.getAttribute("verbsUN");			//all verbs un validated

for(int i = 0 ; i < validVerbs.length ; i++){
		verbsAL.add(validVerbs[i]);
}

String[] validVerbsFinal = new String[verbsAL.size()];

for(int i = 0 ; i < verbsAL.size() ; i++){
	validVerbsFinal[i] = verbsAL.get(i);
}
//---------------------------------------------------------------------------------------------------------------------


//update session vars
session.setAttribute("validVerb", validVerbsFinal);



ArrayList<String> assALL 		=new ArrayList<String>(); 
ArrayList<String> associations 	= x.FindAssociations(assocSubStr, validVerbsFinal, validNouns);
int size = associations.size();
int constant = (Integer)session.getAttribute("constant");
int startLoop = constant;
int endLoop = constant + 10;
session.setAttribute("constant", endLoop);
session.setAttribute("AssocALUN", associations);
session.setAttribute("assALL", assALL);

int progressbarNouns = 100;

int progressbarVerbs = 100;

int progressbarAsso = endLoop * 100 / size;

%>


<div id="header"><h2> Please Select Valid Associations </h2>
<p> From the nouns and verbs that you have selected.<br> 
Here are your possible associations </p></div>

<div id="section1"><br></div>
<%if (size > endLoop){ %>
<div id = "section2">
	<form ACTION="06ValidateAssociationsLOOP.jsp" METHOD="post">
	<%	
	for(int i = startLoop ; i < endLoop ; i++){
		%>
		
		<input type="checkbox" name="assoc" value="<%out.print(associations.get(i)); %>"> <%out.print(associations.get(i));%> 
		 
		<BR>
		<% 
	} %>
	<br>
	<input type="submit" value="Submit">
	
	</form>
	</div>
<%} 
else{ 
	session.setAttribute("constant",0);%>
	<div id="section2">
	<form ACTION="07ValidateActors.jsp" METHOD="post">
	<%	
	for(int i = startLoop ; i < associations.size() ; i++){ %>
	
		<input type="checkbox" name="assoc" value="<%out.print(associations.get(i)); %>"> <%out.print(associations.get(i));%> 

		<BR>
		<% 
	} %>
	
	<p> Move to validate actors -><input type="submit" value="Next"></p>

	</form>
	</div>
<%} %>




</body>
</html>