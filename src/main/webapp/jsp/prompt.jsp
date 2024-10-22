<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Error Prompt</title>
	<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
	<base href="/eCommerceSystem/">
</head>
<body>
	<c:choose>
		<%-- Otherwise, prompt user to log in. --%>
		<c:when test="${sessionScope.loggedIn=='no'}">
			<br>
			<p>Checkout failed. You must log in or register an account before completing the checkout.</p>
			<br>
			
			<%-- Indicates to the login or register page that the user is coming from the checkout page. --%>
			<%-- <c:set var="redirect" value="redirect" scope="request" /> --%>
			
			<div class="prompt">
				<%-- Redirect to Login page. --%>
				<a href="jsp/login.jsp?redirect=redirect"><button type="submit">Login and Proceed with Checkout</button></a>
				
				<%-- Redirect to Register page --%>
				<a href="jsp/register.jsp?redirect=redirect"><button type="submit">Register and Proceed with Checkout</button></a>				
			</div>
		<%-- If user is logged in and payment is not valid --%>
		
			<br>	
			<br>
			<hr>
			<br>
		</c:when>	
	
	</c:choose>

</body>
</html>