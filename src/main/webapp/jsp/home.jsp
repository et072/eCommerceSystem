<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Home Page</title>
	<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
	<base href="/eCommerceSystem/">
</head>
<body>
	<%-- Header for the website --%>
	<%@include file="header.jsp" %>
	<br>
	
	<%-- Get updated list of top selling items --%>
	
	<%-- Table of featured items --%>
	<div class="center">
		<%-- Header for the Featured Items --%>
		<h1>Featured Items</h1>
		
		<div class="featured">
			<table>
				<tr>
					<td><img src="images/14900k.jpg" /></td>
					<td><img src="images/asus_4090.png" /></td>
					<td><img src="images/gigabyte_b650-aorus-elite.jpg" /></td>
					<td><img src="images/rm750e.jpg" /></td>
				</tr>
			</table>
		</div>
		
	</div>
</body>
</html>