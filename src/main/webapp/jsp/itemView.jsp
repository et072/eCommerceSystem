<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>${item.name }</title> <!-- name of item -->
<base href="/eCommerceSystem/">
</head>
<body>
	<%@include file="header.jsp" %>

	<form method='post' action="CartServlet?action=addItem&itemID=${item.itemID}">
	<!-- add img if desired -->
	<div id="centered" style="margin: 1vw">
			<h2>${item.name }</h2><br>
			<p><b>$${item.price }</b></p><br>
		<!--	<p>Quantity Remaining: ${item.quantity }</p>  Inventory managment not required, possible later addition-->
			<p>Description:</p>
			<p>${item.description }</p><br>
			<p>
			<label>Qty: </label>
			<input type='text' name='qty' placeholder="Enter quantity" value="1">
			<input type='submit' value='Add to Shopping Cart'>
			</p>
	</div>
	</form>
</body>
</html>