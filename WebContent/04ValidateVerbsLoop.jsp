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
  	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
<title>Validate Verbs</title>
</head>
<body>
<%
//Load Session Vars
int constant = (Integer)session.getAttribute("constant");
int startLoop = constant;
int endLoop = constant + 10;




//load session vars
ArrayList<String> tagged 	= (ArrayList<String>)session.getAttribute("tagged");
ArrayList<String> concept 	= (ArrayList<String>)session.getAttribute("concept");
ArrayList<String> assocSubStr = (ArrayList<String>)session.getAttribute("assocSubStr");
ArrayList<String> verbsAL	= (ArrayList<String>)session.getAttribute("verbsAL");  //building valid verb arrayList
String[] validVerbs 		= request.getParameterValues("verb");					//validated from the pervious page.
String[] verbsUN			= (String[])session.getAttribute("verbsUN");			//all verbs un validated
String[] validNouns			= (String[])session.getAttribute("validNouns");			//FULL LIST OF VALIDATED NOUNS

int size = verbsUN.length;

ToPlant x = new ToPlant();
 
//put valid verbs on verb AL
for(int i = 0 ; i < validVerbs.length ; i++){
	verbsAL.add(validVerbs[i]);
}


//update session vars
session.setAttribute("validNouns", validNouns);
session.setAttribute("verbsAL", verbsAL);
session.setAttribute("constant",endLoop);
session.setAttribute("assocSubStr", assocSubStr);

if(constant == 0){
	session.setAttribute("concept",concept);
	session.setAttribute("tagged", tagged);
}


//*************************************************************************************************

int progressbarNouns = 100;

int progressbarVerbs = endLoop * 100 / size;

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

<div id="header"><p> Please Select Valid Verbs </p>
<p> Based on your previous selection here are the possible valid actions </p></div>
<%if (size > endLoop){ %>
	<form ACTION="04ValidateVerbsLoop.jsp" METHOD="post">
	<%	
	for(int i = startLoop ; i < endLoop ; i++){
		%>
	
		<input type="checkbox" name="verb" value="<%out.print(verbsUN[i]); %>"> <%out.print(verbsUN[i]);%> 
		
		<BR>
		<% 
	} %>

	<input type="submit" value="Submit">

	</form>
<%} 
else{ 
session.setAttribute("constant",0);%>
	<form ACTION="05ValidateAssociationsFIRST.jsp" METHOD="post">
	<%	
	for(int i = startLoop ; i < verbsUN.length ; i++){
		%>
		
		<input type="checkbox" name="verb" value="<%out.print(verbsUN[i]); %>"> <%out.print(verbsUN[i]);%> 
		
		<BR>
		<% 
	} %>

	<input type="submit" value="Next">

	</form>
<%} %>



<div id="footer"> 

</div>
</body>
</html>