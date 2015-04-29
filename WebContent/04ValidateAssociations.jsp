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

String[] validVerbs 			= request.getParameterValues("verb"); 
ArrayList<String> tagged 		= (ArrayList<String>)session.getAttribute("tagged");
ArrayList<String> concept 		= (ArrayList<String>)session.getAttribute("concept");
ArrayList<String> assocSubStr 	= (ArrayList<String>)session.getAttribute("assocSubStr");
String[] validNouns				= (String[])session.getAttribute("validNoun");


//update session vars
session.setAttribute("validVerb", validVerbs);

ArrayList<String> associations = x.FindAssociations(assocSubStr, validVerbs, validNouns);
%>

<div id="header"><p> Please Select Valid Associations </p>
<p> Based on your previous selection here are the possible valid actions </p></div>

<form ACTION="05DisplayClass.jsp" METHOD="post">
<%
	for(String str : associations){
		%>
		
		<input type="checkbox" name="associations" value="<%out.print(str); %>"> <%out.print(str);%> 
		 <BR>
		<%

	}
%>
<div id="footer"><br>
<input type="submit" value="Submit">
</div>
</form>

</body>
</html>