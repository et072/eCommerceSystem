<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Shopping Cart</title>
	<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
	<base href="/eCommerceSystem/">
</head>
<body>
	<%-- Header for the website --%>
	<jsp:include page="header.jsp" flush="true" />
	<br>

	<%-- Header for the Shopping Cart --%>
	<div class="cartheader">
		<h1>Shopping Cart</h1>
		<hr>
		<br>
	</div>

	<c:if test="${cart.isEmpty()}">
		<h2>There are no items in your cart.</h2>
	</c:if>
	<%-- Shopping Cart Items. For Loop --%>
	<c:if test="${!cart.isEmpty()}">
		<c:forEach items="${cart.getItems()}" var="item">
			<div class="product">
				<h2>${item.name}</h2>
				<div class="productinfo">
					<%-- Product descriptions --%>
					<p>${items.description}</p>
				</div>

				<br>

				<div class="cartqty">
					<form action="CartServlet?action=updateItem&itemID=${item.itemID}" method=post>
						Quantity:
						<input type="text" size="3" name="qty" value="${item.quantity}" />
						<input type="submit" value="Update" />
					</form>
				</div>

				<div class="cartqty">
					<form action="CartServlet?action=removeItem&itemID=${item.itemID}" method=post>
						<input type="submit" value="Remove From Cart" />
					</form>
				</div>

				<div class="cartprice">
					<%-- Product prices --%>
					<p>$${item.price * item.quantity}</p>
				</div>
			</div>
		</c:forEach>
	
		<div class="cartredirect">
			<p>Total Price: $${cart.getTotalPrice()}</p>
			
			<form action="jsp/checkOut.jsp" method="post">
				<button type="submit" id="checkoutButton">CHECKOUT</button>
			</form>
			
			<br>
			
			<a href="CatalogServlet">Continue Shopping</a>
		</div>
	</c:if>

</body>
</html>