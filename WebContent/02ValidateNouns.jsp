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
	width : 300px;
	float : left;
}
#section2 {
	width : 300px;
	float : left;
}

#footer {
	background-color:white;
    color:black;
	padding:5px;
	text-align:center;
}

</style>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Validate Nouns</title>
</head>

<body>

<% 
	ToPlant x = new ToPlant();
	int constant = (Integer)session.getAttribute("constant");
	int startLoop = constant;
	int endLoop = constant + 10;
		
	ArrayList<String> conceptArray 	= x.getConceptArray();
	ArrayList<String> tagged 		= ToPlant.Tag(conceptArray);
	String[] validNouns 			= request.getParameterValues("id"); 
	ArrayList<String> nounsAL 		= (ArrayList<String>)session.getAttribute("nounsAL");

	//copy valid nouns from last page into ArrayList Nouns
	if(constant > 0){
		for(int i = 0 ; i < validNouns.length ; i++){
			nounsAL.add(validNouns[i]);
		}
	}
	
	ToPlant.setValidNouns(nounsAL);

	//determine if the nnoun validation need to be multi page.

	String[] nounArray = x.FindNounArray(tagged);
	int size = nounArray.length;

	//set the session variable
	session.setAttribute("nounsAL", nounsAL);
	if(constant == 0){
		session.setAttribute("concept",conceptArray);
		session.setAttribute("tagged", tagged);
	}
	session.setAttribute("constant",endLoop);
	
	int progressbarNouns = endLoop * 100 / size;
	int progressbarVerbs = 0;
	
%>

<div id="header">
<p> Please Select Valid Classes </p>
</div>
<div id="section1"><br></div>
<div id="section2">
	<%if(nounArray.length > endLoop){ %>
	<form ACTION="02ValidateNouns.jsp" METHOD="post">
		<%for(int i  = startLoop ; i < endLoop ; i++){ %>
			<input type="checkbox" name="id" value=" <% out.print(nounArray[i]); %>" > <%out.print(nounArray[i]);%>
			<BR> 
		<%}%>
	<br>
	<input type="submit" value="Submit">
	</form>
	<%
	}
	else { 
	session.setAttribute("constant",0);  %>

	<form ACTION="03ValidateVerbsInitial.jsp" METHOD="post">
	<%  for( int i  = startLoop ; i < nounArray.length ; i++){ %>
		<input type="checkbox" name="id" value=" <% out.print(nounArray[i]); %>" > <%out.print(nounArray[i]);%>
		<BR> 
	<% }%>
	<br><br>
	<input type="submit" value="Validate Verbs">
	</form>
</div>
<%} %>

</body>
</html>