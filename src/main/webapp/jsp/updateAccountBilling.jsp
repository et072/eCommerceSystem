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
			
		<form action="UpdateAccountServlet?action=updateAccountBilling" method="post">
			<h2>Update Billing Information</h2>
			<br>
			Street Address: <input type="text" placeholder="Example: 15 Street Address" name="addressBillingU" />
			<br>
			Province: <input type="text" name="provinceBillingU" />
			<br>
			Country: <input type="text" name="countryBillingU" />
			<br>
			ZIP Code: <input type="text" name="zipCodeBillingU" />
			<br>
			Phone Number: <input type="text" placeholder="(xxx)-xxx-xxxx" name="phoneBillingU" />
			<br>
			<br>
			<button type="submit">Update Billing Information</button>
			
			<br>
			<br>
			
			<a href="jsp/account.jsp">Back to My Account</a>
		</form>
	</div>
</body>
</html>