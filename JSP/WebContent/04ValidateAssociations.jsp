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
<title>Choose Valid Associations</title>
</head>
<body>
<%
ToPlant x = new ToPlant();

String[] validVerbs 		= request.getParameterValues("verb"); 
ArrayList<String> tagged 	= (ArrayList<String>)session.getAttribute("tagged");
ArrayList<String> concept 	= (ArrayList<String>)session.getAttribute("concept");
String[] validNouns			= (String[])session.getAttribute("validNoun");

//update session vars
session.setAttribute("validVerb", validVerbs);

for(int i = 0 ; i < validVerbs.length ; i++){
	out.println(validVerbs[i]);
}
%> <BR>
<%
for(int i = 0 ; i < validNouns.length ; i++){
	out.println(validNouns[i]);
}

x.FindAssociations(concept, validVerbs, validNouns);

//String[][] asso = x.Association(concept,validVerbs, validNouns);
%>
</body>
</html>