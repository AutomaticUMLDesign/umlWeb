<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
#footer {
	background-color:white;
    color:black;
	padding:5px;
	text-align:center;
}

</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>UML Magic</title>
</head>
<body>
	<!-- <form name="upload" action="02ValidateNouns.jsp" enctype="multipart/form-data" ><!-- method="post"-->
	<p> Please select your text concept statement <br><br></p>
	<form method="post" action="uploadServlet" enctype="multipart/form-data">
			<table border="0">
                <tr>
                    <td>Name: </td>
                    <td><input type="text" name="firstName" size="50"/></td>
                </tr>

                <tr>
                    <td>File (.txt): </td>
                    <td><input type="file" name="file"> <br /> </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="submit" value="Submit">
                    </td>
                </tr>
            </table>
			
		<!-- -->
	</form>


</body>

<!-- jar files in jsp http://stackoverflow.com/questions/11652431/eclipse-add-jar-to-dynamic-web-project -->
</html>