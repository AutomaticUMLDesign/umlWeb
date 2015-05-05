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

ArrayList<String> tagged 		= (ArrayList<String>)session.getAttribute("tagged");
ArrayList<String> concept 		= (ArrayList<String>)session.getAttribute("concept");
ArrayList<String> associations  = (ArrayList<String>)session.getAttribute("AssocALUN");  //unvalidated
String[] validAssociation		= request.getParameterValues("assoc");
ArrayList<String> assALL		= (ArrayList<String>)session.getAttribute("assALL");	//cumulative validation

for(int i = 0 ; i < validAssociation.length ; i++){
	//System.out.println(validAssociation[i]);
	assALL.add(validAssociation[i]);
}
//for(String str : assALL){
//	System.out.println("ASSO: " + str);
//}

int size = associations.size();
int constant = (Integer)session.getAttribute("constant");
int startLoop = constant;
int endLoop = constant + 10;
session.setAttribute("constant", endLoop);
session.setAttribute("assALL",assALL);

int progressbarNouns = 100;

int progressbarVerbs = 100;

int progressbarAsso = endLoop * 100 / size;

%>


<div id="header"><p> Please Select Valid Associations </p>
<p> Based on your previous selection here are the possible valid actions </p></div>

<div id="section1"><br></div>
<%if (size > endLoop){ %>
	<form ACTION="06ValidateAssociationsLOOP.jsp" METHOD="post">
	<%	
	for(int i = startLoop ; i < endLoop ; i++){
		%>
		
		<input type="checkbox" name="assoc" value="<%out.print(associations.get(i)); %>"> <%out.print(associations.get(i));%> 
		
		<BR>
		<% 
	} %>
	<div id="footer"><br>
	<input type="submit" value="Submit">
	</div>
	</form>
<%} 
else{ 
session.setAttribute("constant",0);%>
	<form ACTION="07ValidateActors.jsp" METHOD="post">
	<%	
	for(int i = startLoop ; i < associations.size() ; i++){
		%>
		
		<input type="checkbox" name="assoc" value="<%out.print(associations.get(i)); %>"> <%out.print(associations.get(i));%> 
		
		<BR>
		<% 
	} %>
	<div id="footer"><br>
	<input type="submit" value="Next">
	</div>
	</form>
<%} %>




</body>
</html>