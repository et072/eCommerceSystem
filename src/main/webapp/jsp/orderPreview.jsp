<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Order Previews</title>
</head>
<body>
	<jsp:include page="header.jsp" flush="true" />
	<div class="center">
		<h1>All Orders</h1>
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
				<c:forEach var="order" items="${requestScope.orders}">
					<tr>
						<td>${order.orderID }</td>
						<td>${order.clientFullName }</td>
						<td>${order.date }</td>
						
						<td>
						<table style="border-collapse: collapse; border: none;">
						<c:forEach var="item" items="${order.items }">
							<tr>
							<td>${item.name }</td>
							</tr>
						</c:forEach>
						</table>
						</td>
						
						<td>
						<table style="border-collapse: collapse; border: none;">
						<c:forEach var="item" items="${order.items }">
							<tr>
							<td>${item.quantity }</td>
							</tr>
						</c:forEach>
						</table>
						</td>
						
						<td>
						<table style="border-collapse: collapse; border: none;">
						<c:forEach var="item" items="${order.items }">
							<tr>
							<td>${item.price }</td>
							</tr>
						</c:forEach>
						</table>
						</td>
						
					</tr>
				</c:forEach>
			<!-- total price obtained from AdminServlet-->
			<tr>
				<td colspan="100%" class="totalprice">Total Price: $ <fmt:formatNumber
						type="number" minFractionDigits="2" maxFractionDigits="2"
						value="${requestScope.ordersTotalPrice}" />
				</td>
			</tr>
		</table>
	</div>
	
	<div class="center">
		<a href="AdminServlet?action=account"><button>Go to Accounts</button></a>
		<a href="AdminServlet?action=item"><button>Go to Items</button></a>
	</div>
	
</body>
</html>