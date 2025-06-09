<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Administrative Item View</title>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
<base href="/eCommerceSystem/">
</head>
<body>

	<%@include file="header.jsp" %>
	<%@include file="adminItemView.jsp" %>
	
	<br>
	
	<div class="center">
		<a href="AdminServlet?action=account"><button>Go to Accounts</button></a>
		<a href="AdminServlet?action=order"><button>Go to Orders</button></a>
	</div>
	
	<!-- inventory listed as optional in document -->
</body>
</html>