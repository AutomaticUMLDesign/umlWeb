<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="backend.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.*"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%response.setHeader( "Pragma", "no-cache" );
response.setHeader( "Cache-Control", "no-cache" );
response.setDateHeader( "Expires", 0 ); %>
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
	

	
	
	session.setAttribute("concept",conceptArray);
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

<%	
//FOR IMAGE PRINTING *************************************8***************************************

x.NounToPlant(nounArray);
//************************************************************************************************* %>
<!-- CLASS DIAGRAM IMAGE With Not Validation  --> 
<img src="<%=request.getContextPath()%>/NounDiagram.jpg" width="50%" height="50%" /> 
<img src="NounDiagram.jpg" />


</body>
</html>