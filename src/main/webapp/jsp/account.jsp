<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
		<%-- My Account Page Header --%>
		<h1>My Account</h1>
		<br>
		<br>
	</div>
	
	<div class="split">
		<div class="section">
			<%-- Account Details Header --%>
			<h1>Current Account Details</h1>
			<br>
			<br>
		
			<%-- Current Customer Information, with the option to update the information. --%>
			<form action="jsp/updateAccountInfo.jsp" method="post">
				<h2>Current Account Information</h2>
				<br>
				Username: ${sessionScope.username}
				<br>
				Password: ${sessionScope.password}
				<br>
				First Name: ${sessionScope.firstName}
				<br>
				Last Name: ${sessionScope.lastName}
				<br>
				Email ${sessionScope.email}
				<br>
				<br>
				<button type="submit">Update Account Information</button>
				
				<br>
				<br>
			</form>
			
			<form action="jsp/updateAccountShipping.jsp" method="post">
				<h2>Current Shipping Information</h2>
				<br>
				Street Address: ${sessionScope.shippingAddress}
				<br>
				Province: ${sessionScope.shippingProvince}
				<br>
				Country: ${sessionScope.shippingCountry}
				<br>
				Postal/ZIP Code: ${sessionScope.shippingZip}
				<br>
				Phone Number: ${sessionScope.shippingPhone}
				<br>
				<br>
				<button type="submit">Update Shipping Information</button>
				
				<br>
				<br>
			</form>
	
			<form action="jsp/updateAccountCC.jsp" method="post">
				<h2>Current Credit Card Information</h2>
				<br>
				Credit Card Number: ${sessionScope.creditCardNum}
				<br>
				Expiry Month: ${sessionScope.ccExpiryMon}
				<br>
				Expiry Year: ${sessionScope.ccExpiryYr}
				<br>
				CVV: ${sessionScope.cvv}
				<br>
				<br>
				<button type="submit">Update Credit Card Information</button>
				
				<br>
				<br>
			</form>
				
			<form action="jsp/updateAccountBilling.jsp" method="post">
				<h2>Current Billing Information</h2>
				<br>
				Street Address: ${sessionScope.billingAddress}
				<br>
				Province: ${sessionScope.billingProvince}
				<br>
				Country: ${sessionScope.billingCountry}
				<br>
				ZIP Code: ${sessionScope.billingZip}
				<br>
				Phone Number: ${sessionScope.billingPhone}
				<br>
				<br>
				<button type="submit">Update Billing Information</button>
				
				<br>
				<br>
			</form>
		</div>
		<div class="section center">
			<%-- Purchase History Header --%>
			<h1>Purchase History</h1>
			<br>
			<br>
			
			<%-- Table containing purchase history --%>
			<table>
				<%-- Table headers --%>
				<tr>
					<th>Order ID</th>
					<th>Name</th>
					<th>Date</th>
					<th>Product Name</th>
					<th>Quantity</th>
					<th>Price</th>
				</tr>
	
				<%-- Loop through items in the cart and display their information --%>
				<c:forEach var="order" items="${sessionScope.orders}">
					<tr>
						<td>${order.orderID }</td>
						<td>${order.clientFullName }</td>
						<td>${order.date }</td>
						
						<td>
						<table style="border-collapse: collapse; border: none;">
						<c:forEach var="item" items="${order.items }">
							<tr>
							<td style="border-style : hidden!important;">${item.name }</td>
							</tr>
						</c:forEach>
						</table>
						</td>
						
						<td>
						<table style="border-collapse: collapse; border: none;">
						<c:forEach var="item" items="${order.items }">
							<tr>
							<td style="border-style : hidden!important;">${item.quantity }</td>
							</tr>
						</c:forEach>
						</table>
						</td>
						
						<td>
						<table style="border-collapse: collapse; border: none;">
						<c:forEach var="item" items="${order.items }">
							<tr>
							<td style="overflow: hidden;white-space: nowrap;border-style : hidden!important;">$${item.price }</td>
							</tr>
						</c:forEach>
						</table>
						</td>
						
					</tr>
				</c:forEach>
				
				<%-- Total price of the purchase. Using align here since we cannot add div inside table. --%>
				<tr>
					<td colspan="100%" class="totalprice">Total Price: $
						<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${sessionScope.ordersTotalPrice}"/>
					</td>
				</tr>
			</table>
			
		</div>

	</div>


</body>
</html>