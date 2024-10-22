<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script src="checkout.js"></script>
<title>Checkout</title>
<base href="/eCommerceSystem/">
</head>
<body>
	<jsp:include page="header.jsp" flush="true" />

	<jsp:include page='prompt.jsp'></jsp:include>

	<c:choose>
		<c:when test="${sessionScope.loggedIn=='yes'}">
			<c:choose>
				<c:when test="${sessionScope.CCCounter % 3 == 0}">
					<br>
					<p>Credit Card Authorization Failed. Please re-confirm your payment information and try again.</p>
					<c:set var="CCCounter" value="1" scope="session" />
					<a href='jsp/account.jsp'> <input type='button' value='Go to account'></a>
				</c:when>
				<c:otherwise>
				<form method='post' action="OrderServlet">

				<!-- Display billing and shipping information -->
				<div id='billBox'>
					<h3>Billing Information</h3>
					Address:<input type='text' name='addressB' value="${sessionScope.billingAddress }" /> 
					Province:<input type='text' name='provinceB' value="${sessionScope.billingProvince }" />
					Country:<input type='text' name='countryB' value="${sessionScope.billingCountry }" /> 
					Postal/Zip Code:<input type='text' name='zipB' value="${sessionScope.billingZip }" />
					Phone Number:<input type='text' name='phoneB' value="${sessionScope.billingPhone }" />
				</div>

				<div id='shipBox'>
					<h3>Shipping Information</h3>
					Address:<input type='text' name='addressS' value="${sessionScope.shippingAddress }" /> 
					Province:<input type='text' name='provinceS' value="${sessionScope.shippingProvince }" />
					Country:<input type='text' name='countryS' value="${sessionScope.shippingCountry }" /> 
					Postal/Zip Code:<input type='text' name='zipS' value="${sessionScope.shippingZip }" />
					Phone Number:<input type='text' name='phoneS' value="${sessionScope.shippingPhone }" />
				</div>
				<input type='submit' value='Confirm Order'> 
			</form>
				
				</c:otherwise>
			</c:choose>
		</c:when>
	</c:choose>
	<a href='jsp/home.jsp'> <input type='button' value='Quit'></a>
</body>
</html>