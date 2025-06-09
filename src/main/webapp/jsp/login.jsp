<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Login</title>
	<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
	<base href="/eCommerceSystem/">
</head>
<body>
	<%-- Header for the website --%>
	<%@include file="header.jsp" %>
	<br>
	
	<div class="center">
		<%-- Login Header --%>
		<h1>Login Page</h1>
		<br>
		
		<%-- Login Button. POST method to hide username and password from URL --%>
		<form action="LoginServlet?action=login" method="post">
			Username: <input type="text" name="usernameL" />
			<br>
			Password: <input type="password" name="passwordL" />
			<br><br>
			
			<c:choose>
				<c:when test="${requestScope.error=='error'}">
					<p>Login Failed. Please try again.</p>
				</c:when>
			</c:choose>
			
			<c:choose>
				<%-- Show the Login button if not coming from checkout page --%>
				<c:when test="${param.redirect != 'redirect'}">
					<button type="submit" name="login">Login</button>
				</c:when>
				
				<%-- Came from checkout page. Attempt to process order after logging in by showing this button. --%>
				<c:otherwise>
					<button type="submit" name="loginCheckout">Login and Complete Purchase</button>
				</c:otherwise>	
			</c:choose>
		</form>
		
		<%-- Show the Register button if not coming from checkout page --%>
		<c:if test="${param.redirect != 'redirect'}">
			<br>
			<hr>
			<br>
			New to our site? Create an account.
			<br>
		
			<%-- Register Button --%>
			<form action="jsp/register.jsp">
				<button type="submit">Register</button>
			</form>
		</c:if>
	</div>
</body>
</html>