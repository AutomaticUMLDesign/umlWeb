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
	String fileName = request.getParameter("file");
	//HARD CODED FILE PATH * * * 
	fileName = "/home/kullen/workspace/UML-Designer/umlWeb/src/"+ fileName;
	ToPlant x = new ToPlant();
	x.ReadFile(fileName);
	ArrayList<String> conceptArray = x.getConceptArray();
	
	ArrayList<String> tagged = x.Tag(conceptArray);
	String[] nounArray = x.FindNounArray(tagged);
	
	session.setAttribute("concept", conceptArray);
	session.setAttribute("tagged", tagged);
	
	
%>
<div id="header">
<p> Please Select Valid Classes </p>
</div>

<form ACTION="03ValidateVerbs.jsp" METHOD="post">
<%
	for(int i = 0 ; i < nounArray.length ; i++){
		if(i % 3 == 0) { %>
		<div id="section1">
		<input type="checkbox" name="id" value="<%out.print(nounArray[i]); %>"> <%out.print(nounArray[i]);
		 %><BR> </div> 
		<%}
		else if(i % 2 == 0) {%>
		
		<div id="section2">
		<input type="checkbox" name="id" value="<%out.print(nounArray[i]); %>"> <%out.print(nounArray[i]);
		%> <BR> </div> 
		<%}
		
		else {%>
		
		<div id="section3">
		<input type="checkbox" name="id" value="<%out.print(nounArray[i]); %>"> <%out.print(nounArray[i]);
		%> <BR> </div> 
		
		<% }
	}
%>
<div id="footer">
<input type="submit" value="Submit">
</div>
</form>


</body>
</html>