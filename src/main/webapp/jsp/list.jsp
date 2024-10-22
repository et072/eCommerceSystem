<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<script src="list.js"></script>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>"
	type="text/css" />
<title>Insert title here</title>
</head>
<body>
<form method='get' action="CartServlet">
		<!-- Displays the brand/category/searchterm -->
		<div>
			<c:if test="${displayMsg != null}">
				<span class="label" style="margin-left: 15px;"> ${displayMsg}
				</span>
			</c:if>
		</div>
		<!-- Displays how the list is sorted -->
		<div>
			<c:if test="${displaySort != null}">
				<span class="label" style="margin-left: 15px;">
					${displaySort} </span>
			</c:if>
		</div>

		<table>
			<tr>
				<th id="th-title">ITEM NAME</th>
				<th id="th-price">PRICE</th>
				<th id="th-details">DETAILS</th>
			</tr>

			<c:forEach items="${itemList}" var="item">
				<tr>
					<!-- Display name -->
					<td>${item.getName() }</td>

					<!-- Display price -->
					<td>$${item.getPrice()}</td>
					<!-- Button to view more details -->
					<td>
						<a href="CatalogServlet?action=itemSelected&itemID=${item.itemID}"> View Details</a>
						
					</td>
				</tr>
			</c:forEach>
		</table>
</form>
</body>
</html>