<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>My Account</title>
	<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
	<base href="/eCommerceSystem/">
</head>
<body>
	<%-- Header for the website --%>
	<%@include file="header.jsp" %>
	<br>
	
	<div class="center">
		<%-- Update Account Page Header --%>
		<h1>Update Account Details</h1>
		<br>
		<br>

		<form action="UpdateAccountServlet?action=updateAccountInfo" method="post">
			<h2>Update Account Information</h2>
			<br>
			Username: <input type="text" name="usernameU" />
			<br>
			Password: <input type="password" name="passwordU" />
			<br>
			First Name: <input type="text" name="firstNameU" />
			<br>
			Last Name: <input type="text" name="lastNameU" />
			<br>
			Email <input type="text" name="emailU" />
			<br>
			<br>
			<button type="submit">Update Account Information</button>
			
			<br>
			<br>
			
			<a href="jsp/account.jsp">Back to My Account</a>
		</form>
	</div>
</body>
</html>