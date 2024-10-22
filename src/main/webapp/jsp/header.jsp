<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<link rel="stylesheet" href="<c:url value='/css/style.css'/>" type="text/css" />
	<base href="/eCommerceSystem/">
</head>
<body>
	<%-- Site wide header/menu --%>
	<div class='menu'>
		<nav>
			<ul id='menu'>
				<li><a href="jsp/home.jsp">Home</a></li>
				<li><a href="CatalogServlet?action=allItems">Catalogue</a></li>
				
				<c:choose>
					<%-- If user is not logged in, hide "My Account" and "Sign Out" buttons --%>
					<c:when test="${sessionScope.loggedIn=='yes' and sessionScope.status=='client' }">
						<li><a href="LoginServlet?action=refresh">My Account</a></li>
						<li>
							<form action="LoginServlet?action=signout" method="post">
								<input type="hidden" name="signout" value="true" />
								<button type="submit">Sign Out</button>
							</form>
						</li>

						<%-- <li><a href="jsp/home.jsp">Sign Out</a></li> --%>
					</c:when>
					
					<c:when test="${sessionScope.loggedIn=='yes' and sessionScope.status=='admin' }">
						<li><a href="AdminServlet">Admin</a></li>
						<li>
							<form action="home" method="post">
								<input type="hidden" name="signout" value="true" />
								<button type="submit">Sign Out</button>
							</form>
						</li>
					</c:when>
					<%-- If user is signed in, hide "Sign In" button --%>					
					<c:otherwise>
						<li><a href="jsp/login.jsp">Sign In</a></li>
					</c:otherwise>	
				</c:choose>
				
				<li><a href="jsp/cart.jsp">My Cart</a></li>
			</ul>
		</nav>
	</div>
	
</body>
</html>
