<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<script src="js/jquery-1.9.1.js"></script>
<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
<!-- add js link -->
<title>Catalog</title>
<base href="/eCommerceSystem/">
</head>
<body>
	<div id="centered">
		<!-- website header -->
		<jsp:include page="header.jsp" flush="true" />
		<div class="leftbar"><jsp:include page="leftColumn.jsp" flush="true" /> <!-- category sorting --></div>
		<div class='list'><jsp:include page="list.jsp" flush="true" /> <!-- list of items --></div>
		<!-- need to right align -->
	</div>
</body>
</html>