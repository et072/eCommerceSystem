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

		<form action="UpdateAccountServlet?action=updateAccountCC" method="post">
			<h2>Update Credit Card Information</h2>
			<br>
			Credit Card Number: <input type="text" placeholder="Card Number" name="creditCardU" />
			<br>
			Expiry Month: <input type="text" placeholder="MM" name="ccExpiryMonU" />
			<br>
			Expiry Year: <input type="text" placeholder="YYYY" name="ccExpiryYrU" />
			<br>
			CVV: <input type="text" placeholder="CVV" name="cvvU" />
			<br>
			<br>
			<button type="submit">Update Credit Card Information</button>
			
			<br>
			<br>
			
			<a href="jsp/account.jsp">Back to My Account</a>
		</form>
	</div>
</body>
</html>