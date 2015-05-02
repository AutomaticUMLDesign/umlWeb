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
	
	ArrayList<String> conceptArray 	= x.getConceptArray();
	ArrayList<String> tagged 		= ToPlant.Tag(conceptArray);
	String[] validNouns 			= request.getParameterValues("id"); 
	ArrayList<String> nounsAL 		= (ArrayList<String>)session.getAttribute("nounsAL");
	int nounLength 					= (Integer)session.getAttribute("nounLength");
	int startLoop = nounLength;

	
	//copy valid nouns from last page into ArrayList Nouns
	if(nounLength > 0){
		for(int i = 0 ; i < validNouns.length ; i++){
			nounsAL.add(validNouns[i]);
		}
	}
	
	//inc noun page count   4 pages total
	int nounCount = (Integer)session.getAttribute("nounCount");
	session.setAttribute("nounCount",nounCount + 1);
	
	//determine if the nnoun validation need to be multi page.
	//if progressNuon = noun Length move to next page
	String[] nounArray = x.FindNounArray(tagged);
	int progressNoun = nounLength + nounArray.length/4;
	int size = nounArray.length;
	System.out.println(size);

	
	//set the session variable
	session.setAttribute("nounsAL", nounsAL);
	session.setAttribute("nounLength", progressNoun);
	if(nounCount == 0){
	session.setAttribute("concept",conceptArray);
	session.setAttribute("tagged", tagged);
	}
	
	int progressbarNouns = nounLength * 100 / size;
	int progressbarVerbs = 0;
%>

<!-- PROGRESS BAR **************************************************************************-->
<!-- Nouns -->
<div class="progress"> 
	 <div class="progress-bar" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" style="width:<%out.print(progressbarNouns); %>%">
	 	NOUNS<span class="sr-only"><% out.print(progressbarNouns); %></span>
	 </div>
</div> 
<!-- verb -->
<div class="progress"> 
	 <div class="progress-bar" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" style="width:<%out.print(progressbarVerbs); %>%">
	 	Verbs<span class="sr-only"><% out.print(progressbarVerbs); %></span>
	 </div>
</div>

<!-- *************************************************************************** -->


<div id="header">
<p> Please Select Valid Classes </p>
</div>
<div id="section1"></div>
<div id="section2">
<%if(nounCount < 3){ %>
<form ACTION="02ValidateNouns.jsp" METHOD="post">
<%  for( int i  = startLoop ; i < progressNoun ; i++){ %>
		<input type="checkbox" name="id" value=" <% out.print(nounArray[i]); %>" > <%out.print(nounArray[i]);%>
		<BR> 
<% }%>
<br>
<input type="submit" value="Submit">
</form>

<%}else{ %>
<form ACTION="03ValidateVerbs.jsp" METHOD="post">
<%  for( int i  = startLoop ; i < nounArray.length ; i++){ %>
		<input type="checkbox" name="id" value=" <% out.print(nounArray[i]); %>" > <%out.print(nounArray[i]);%>
		<BR> 
<% }%>
<br><br>
<input type="submit" value="Validate Verbs">
</form>
</div>
<%} 

//FOR IMAGE PRINTING *************************************8***************************************

x.NounToPlant(nounArray);
//************************************************************************************************* %>
<!-- CLASS DIAGRAM IMAGE With Not Validation  	
<img src="< %
//=request.getContextPath()%>/NounDiagram.jpg" width="50%" height="50%" /> 
<!-- <img src="NounDiagram.jpg" /> -->


</body>
</html>