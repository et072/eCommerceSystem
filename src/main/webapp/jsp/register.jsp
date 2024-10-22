<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Account Registration Page</title>
	<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
	<base href="/eCommerceSystem/">
</head>
<body>
	<%-- Header for the website --%>
	<%@include file="header.jsp" %>
	<br>
	
	<div class="center">
		<%-- Register Header --%>
		<h1>Account Registration Page</h1>
		<br>
		<h3>Please fill out all the registration fields and provide your default billing and shipping information.</h3>
		<br>
		<br>
	
		<%-- Registration fields. POST method to user information from URL --%>
		<form action="RegisterServlet" method="post">
			<h2>Account Information</h2>
			Username: <input type="text" name="usernameR" />
			<br>
			Password: <input type="password" name="passwordR" />
			<br>
			First Name: <input type="text" name="firstNameR" />
			<br>
			Last Name: <input type="text" name="lastNameR" />
			<br>
			Email <input type="text" name="emailR" />
			<br>
			<br>
			
			<h2>Shipping Information</h2>
			Street Address: <input type="text" placeholder="Example: 15 Street Address" name="addressShippingR" />
			<br>
			Province: <input type="text" name="provinceShippingR" />
			<br>
			Country: <input type="text" name="countryShippingR" />
			<br>
			Postal/ZIP Code: <input type="text" name="zipCodeShippingR" />
			<br>
			Phone Number: <input type="text" placeholder="(xxx)-xxx-xxxx" name="phoneShippingR" />
			<br>
			<br>
			
			<h2>Credit Card Information</h2>
			Credit Card Number: <input type="text" placeholder="Card Number" name="creditCardR" />
			<br>
			Expiry Month: <input type="text" placeholder="MM" name="ccExpiryMonR" />
			<br>
			Expiry Year: <input type="text" placeholder="YYYY" name="ccExpiryYrR" />
			<br>
			CVV: <input type="text" placeholder="CVV" name="cvvR" />
			<br>
			<br>
			
			<h2>Billing Information</h2>
			Street Address: <input type="text" placeholder="Example: 15 Street Address" name="addressBillingR" />
			<br>
			Province: <input type="text" name="provinceBillingR" />
			<br>
			Country: <input type="text" name="countryBillingR" />
			<br>
			ZIP Code: <input type="text" name="zipCodeBillingR" />
			<br>
			Phone Number: <input type="text" placeholder="(xxx)-xxx-xxxx" name="phoneBillingR" />
			<br>
			<br>
			
			<br>
			<br>
			
			<c:choose>
				<c:when test="${requestScope.error=='error'}">
					<p>Registration Failed. Format not correct.</p>
				</c:when>
			</c:choose>
			
			<%-- Register button --%>
			<c:choose>
				<%-- Show Register button if not coming from checkout page. --%>
				<c:when test="${param.redirect != 'redirect'}">
					<button type="submit" name="register">Register</button>
				</c:when>	
				
				<%-- Came from checkout page. Attempt to process order after registering by showing this button. --%>
				<c:otherwise>
					
					<button type="submit" name="registerCheckout" >Register and Complete Purchase</button>
				</c:otherwise>
			</c:choose>
		</form>
	</div>
	
</body>
</html>