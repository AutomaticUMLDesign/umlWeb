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
String[] validNouns 		= request.getParameterValues("id"); 
ArrayList<String> tagged 	= (ArrayList<String>)session.getAttribute("tagged");
ArrayList<String> concept 	= (ArrayList<String>)session.getAttribute("concept");
ToPlant x = new ToPlant();
String[] verbArray = x.FindVerbArray(concept,validNouns);
ArrayList<String> assocSubStr = x.getAssocSubStr();

//update session vars
session.setAttribute("validNoun", validNouns);
session.setAttribute("assocSubStr", assocSubStr);


//*************************************************************************************************



%>


<div id="header"><p> Please Select Valid Verbs </p>
<p> Based on your previous selection here are the possible valid actions </p></div>

<form ACTION="04ValidateAssociations.jsp" METHOD="post">
<%
	for(int i = 0 ; i < verbArray.length ; i++){
		if(i % 3 == 0) { %>
		<div id="section1">
		<input type="checkbox" name="verb" value="<%out.print(verbArray[i]); %>"> <%out.print(verbArray[i]);%> 
		</div> 
		<%}
		else if(i % 3 == 1) {%>
		
		<div id="section2">
		<input type="checkbox" name="verb" value="<%out.print(verbArray[i]); %>"> <%out.print(verbArray[i]);%>
		</div> 
		<%}
		
		else {%>
		
		<div id="section3">
		<input type="checkbox" name="verb" value="<%out.print(verbArray[i]); %>"> <%out.print(verbArray[i]);%>
		 </div> 
		
		<% }
	}
%>
<div id="footer"><br>
<input type="submit" value="Submit">
</div>
</form>
<%//FOR IMAGE PRINTING *************************************8***************************************
ArrayList<String> associations = x.FindAssociations(assocSubStr, verbArray, validNouns);
String[] ass = new String[associations.size()];
for(int i = 0; i < associations.size(); i++){
	ass[i] = associations.get(i);
	System.out.println(associations.get(i));
}
x.StringToPlant(ass);
//************************************************************************************************* %>
<!-- CLASS DIAGRAM IMAGE With Not Validation   -->
<img src="<%=request.getContextPath()%>/images/ClassDiagram.jpg" width="50%" height="50%" />


<div id="footer"> 

</div>
</body>
</html>