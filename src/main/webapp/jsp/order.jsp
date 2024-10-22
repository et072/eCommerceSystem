<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Order Summary</title>
	<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
	<base href="/eCommerceSystem/">
</head>
<body>
	<%-- Header for the website --%>
	<%@include file="header.jsp" %>
	<br>

	<div class="center">
		<%-- Order Summary Header --%>
		<h1>Order Summary</h1>
		<hr>
		<br>
		
		<%-- Thank you message for the user after their purchase. --%>
		<h3>Thank you for your order! Your purchase has been processed. Below is your order summary:</h3>
		<br>
		<br>
		
		<%-- Table of ordered products --%>
			<table>
				<%-- Table headers --%>
				<tr>
					<th>Item ID</th>
					<th>Product Name</th>
					<th>Product Description</th>
					<th>Brand</th>
					<th>Quantity</th>
					<th>Price</th>
				</tr>
	
				<%-- Loop through items in the cart and display their information --%>
				<c:forEach var="product" items="${sessionScope.summary}">
					<tr>
						<td>${product.itemID }</td>
						<td>${product.name }</td>
						<td>${product.description }</td>
						<td>${product.brand }</td>
						<td>${product.quantity }</td>
						<td style="overflow: hidden;white-space: nowrap;">$${product.price }</td>
					</tr>
				</c:forEach>
				
				<%-- Total price of the purchase. Using align here since we cannot add div inside table. --%>
				<tr>
					<td colspan="100%" class="totalprice">Total Price: $
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${requestScope.totalPrice}"/>
					</td>
				</tr>
			</table>
		
		
		<br>
		<br>
		<a href="jsp/home.jsp">Back to Home Page</a>
	</div>
</body>
</html>