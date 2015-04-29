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
	
	ToPlant x = new ToPlant();

	ArrayList<String> conceptArray = x.getConceptArray();
	
	ArrayList<String> tagged = ToPlant.Tag(conceptArray);
	String[] nounArray = x.FindNounArray(tagged);
	String[] verbArray = x.FindVerbArray(conceptArray,nounArray);		//TEMP
	ArrayList<String> assocSubStr = x.getAssocSubStr();					//temp
	ArrayList<String> associations = x.FindAssociations(assocSubStr, verbArray, nounArray); //TEMP
	String[] ass = new String[associations.size()];
	for(int i  = 0 ; i < associations.size() ; i++){
		ass[i] = associations.get(i);
	}
	
	x.StringToPlant(ass);
	
	
	
	session.setAttribute("concept",conceptArray);
	session.setAttribute("tagged", tagged);
	
	
%>
<img src="<c:url value='/images/ClassDiagram.jpg'/>" > 
<div id="header">
<p> Please Select Valid Classes </p>
</div>

<form ACTION="03ValidateVerbs.jsp" METHOD="post">
<%
	for(int i = 0 ; i < nounArray.length ; i++){
		if(i % 3 == 0) { %>
		<div id="section1">
		
		<input type="checkbox" name="id" value="<%out.print(nounArray[i]); %>"> <%out.print(nounArray[i]);%>
		<BR> 
		
		</div> 
		<% } else if(i % 3 == 1) {%>
		
		<div id="section2">
		<input type="checkbox" name="id" value="<%out.print(nounArray[i]); %>"> <%out.print(nounArray[i]);
		%> <BR> </div> 
		<%} else {%>
		
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