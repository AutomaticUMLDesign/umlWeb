<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@page import="backend.*"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
ToPlant x = new ToPlant();
String[] associations =request.getParameterValues("associations"); 
x.StringToPlant(associations);
%>

<!-- <img src="${pageContext.request.contextPath }/images/firefly.jpg" alt="site banner: tv show Firefly" ></img>
-->
<img src="${pageContext.request.contextPath }/images/ClassDiagram.png" > 


</body>
</html>