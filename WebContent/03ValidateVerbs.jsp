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
	width : 150px;
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
<title>Validate Verbs</title>
</head>
<body>
<%
//Load Session Vars

//load all valid Nouns
ArrayList<String> nounsAL 	= (ArrayList<String>)session.getAttribute("nounsAL");
String[] Nouns 				= request.getParameterValues("id"); 


for(int i = 0 ; i < Nouns.length ; i++){	nounsAL.add(Nouns[i]);		}

String[] validNouns = new String[nounsAL.size()];

for(int i = 0; i < nounsAL.size(); i++){	
	validNouns[i] = nounsAL.get(i);	
}

session.setAttribute("validNoun", validNouns);


ArrayList<String> tagged 	= (ArrayList<String>)session.getAttribute("tagged");
ArrayList<String> concept 	= (ArrayList<String>)session.getAttribute("concept");


ToPlant x = new ToPlant();
String[] verbArray = x.FindVerbArray(concept,validNouns);

ArrayList<String> assocSubStr = x.getAssocSubStr();

//update session vars

session.setAttribute("assocSubStr", assocSubStr);


//*************************************************************************************************



%>


<div id="header"><p> Please Select Valid Verbs </p>
<p> Based on your previous selection here are the possible valid actions </p></div>

<form ACTION="04ValidateAssociations.jsp" METHOD="post">
<%
	for(int i = 0 ; i < verbArray.length ; i++){
		%>
		<div id="section1">
		<input type="checkbox" name="verb" value="<%out.print(verbArray[i]); %>"> <%out.print(verbArray[i]);%> 
		</div> 

		
		<% 
	}
%>
<div id="footer"><br>
<input type="submit" value="Submit">
</div>
</form>



<div id="footer"> 

</div>
</body>
</html>