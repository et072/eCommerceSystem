<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<table>
		<tr>
			<th id="th-title">ITEM NAME</th>
			<th id="th-price">PRICE</th>
		</tr>

		<c:forEach items="${itemList}" var="item">
			<tr>
				<!-- Display name -->
				<td>${item.getName() }</td>

				<!-- Display price -->
				<td>$${item.getPrice()}</td>
				
			</tr>
		</c:forEach>
	</table>
</body>
</html>