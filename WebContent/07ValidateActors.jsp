<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="backend.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.*"%>
<%@page import="java.util.UUID" %>
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
			width : 250px;
			float : left;
		}
		#section3 {
			width : 250px;
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
<title>Validate Actors</title>
</head>
<body>
<%
//Load Session Vars
int constant = (Integer)session.getAttribute("constant");
int startLoop = constant;
int endLoop = constant + 10;
//SAVED CLASS IMAGE  -----------------------------------------------------------------------------------
if(constant == 0){
	ArrayList<String> associations  = (ArrayList<String>)session.getAttribute("AssocALUN");
	String[] validAssociation		= request.getParameterValues("assoc");
	ArrayList<String> assALL		= (ArrayList<String>)session.getAttribute("assALL");

	for(int i = 0 ; i < validAssociation.length ; i++){
		assALL.add(validAssociation[i]);
		//System.out.println(validAssociation[i]);
	}
	String[] validAssocFinal = new String[assALL.size()];


	for(int i = 0 ; i < assALL.size() ; i++){
		validAssocFinal[i] = assALL.get(i);
	}
	ToPlant x = new ToPlant();

	UUID idNumber = UUID.randomUUID();
	x.StringToPlant(validAssocFinal, idNumber);
	
	String path = "/home/kullen/workspace/UML-Designer/umlWeb/WebContent/ClassDiagram" + idNumber +".png";
}  /// SAVE CLASS DIAGRAM ----------------------------------------------------------------------------------------------



//load all valid Nouns *********************************************************

	ArrayList<String> nounsAL 	= (ArrayList<String>)session.getAttribute("nounsAL");

	
	String[] validNouns = new String[nounsAL.size()];
	
	
	for(int i = 0; i < nounsAL.size(); i++){	 validNouns[i] = nounsAL.get(i); } //convert arraylist to array
	
	
	session.setAttribute("nounsAL", nounsAL);
	session.setAttribute("validNouns", validNouns);

	
//************************************************************************************************


//load session vars
ArrayList<String> tagged 	= (ArrayList<String>)session.getAttribute("tagged");
ArrayList<String> concept 	= (ArrayList<String>)session.getAttribute("concept");
ArrayList<String> verbsAL	= (ArrayList<String>)session.getAttribute("verbsAL");


//if skip button is pressed.
String param = request.getParameter("skip");
int skip = 0;
try{ 
	 skip= Integer.parseInt(param);
}
catch(NumberFormatException e){
	 //didn't select back
}

String[] actors 			= request.getParameterValues("actors");
ArrayList<String> actorsAL = (ArrayList<String>)session.getAttribute("actorsAL");

if(constant > 0 && actors.length > 0){
	for(int i = 0 ; i < actors.length ; i++){
		actorsAL.add(actors[i]);
	}
}


session.setAttribute("skip","0"); //set back to zero;

ToPlant x  =new ToPlant();

ArrayList<String> assocSubStr = x.getAssocSubStr();
session.setAttribute("assocSubStr", assocSubStr);
session.setAttribute("constant",endLoop);

//*************************************************************************************************

//int progressbarNouns = 100;

//int progressbarVerbs = endLoop * 100 / size;

%>
<!-- PROGRESS BAR **************************************************************************-->



<!-- *************************************************************************** -->

<div id="header"><h2> Please Select Valid Actors </h2></div>
<div id="section1"><br></div>
<%session.setAttribute("constant",0);%>
	<div id="section2">
	<form ACTION="07Display.jsp" METHOD="post">
	<%	
	for(int i = startLoop ; i < validNouns.length/2 ; i++){
		%>
		<input type="checkbox" name="actors" value="<%out.print(validNouns[i]); %>"> <%out.print(validNouns[i]);%> 
		
		<BR>
		<% 
	} %>
	</div>
	<div id="section3">
		<%for(int i = validNouns.length/2 ; i < validNouns.length ; i++){
		%>
		<input type="checkbox" name="actors" value="<%out.print(validNouns[i]); %>"> <%out.print(validNouns[i]);%> 
		
		<BR>
		<% 
	} %>
	</div>
	<div id="footer">Finish">
	</div>

	</form>
		
	





<div id="footer"> 


</div>
</body>
</html>