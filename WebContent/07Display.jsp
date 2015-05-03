<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="backend.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.*"%>

<%@page import="java.util.UUID" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Display</title>
</head>
<body>
<%
//Load Session Vars

String[] actors 		= request.getParameterValues("actors");
ArrayList<String> actorsAL = (ArrayList<String>)session.getAttribute("actorsAL");


	for(int i = 0 ; i < actors.length ; i++){
		actorsAL.add(actors[i]);
	}
	
	ToPlant x = new ToPlant();
	x.setActors(actorsAL);
	x.GenerateUseCaseStrings();
	x.StringToPlantUseCase();
	
%>
</body>
</html>