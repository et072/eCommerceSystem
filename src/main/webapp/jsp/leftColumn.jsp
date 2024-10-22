<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%-- <%@page import="java.util.Enumeration"%> --%>
<%-- <%@page import="java.util.Hashtable"%> --%>
<%-- <%@page import="java.util.List"%> --%>
<%-- <%@page import="java.util.ArrayList"%> --%>
<%-- <%@page import="java.util.Iterator"%> --%>



<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



<div class="leftbar">
	<ul id="menu">
		<li><div>
				<a class="link1" href="CatalogServlet?action=allItems"><span
					style="margin-left: 15px;" class="label">All Items</span></a>
			</div></li>



		<!-- Sort -->
		<li>
			<form action="CatalogServlet?action=sortItems">
				<label for="SortMenu">Sorting Options:</label> <input type="hidden"
					name="action" value="sortItems" /> <select name="SortMenu">
					<option value="PriceAsc">Price:Ascending</option>
					<option value="PriceDesc">Price: Descending</option>
					<option value="AlphaAsc">Name: Ascending</option>
					<option value="AlphaDesc">Name:Descending</option>
				</select> <input type="submit" value="Sort">
			</form>

		</li>
		<!-- Brands -->
		<li><div>
				<span class="brandLabel" style="margin-left: 15px;">Brands </span>
			</div>
			<ul>

				<c:forEach items="${brandList}" var="item">
					<li><a class="brandlabel"
						href="CatalogServlet?action=brand&brand=${item}"> <span
							class="label" style="margin-left: 30px;"> ${item} </span></a></li>

				</c:forEach>

			</ul></li>

		<!-- Categories -->
		<li><div>
				<span class="categoryLabel" style="margin-left: 15px;">Categories
				</span>
			</div>
			<ul>

				<c:forEach items="${categoryList}" var="item">
					<li><a class="categorylabel"
						href="CatalogServlet?action=category&category=${item.categoryDescription}">
							<span class="label" style="margin-left: 30px;">
								${item.categoryDescription} </span>
					</a></li>

				</c:forEach>


			</ul></li>
	</ul>
	
	<form class="search" action="CatalogServlet?action=search">
		Search: <input type="hidden" name="action" value="search" /> <input
			id="text" type="text" name="keyWord" size="12" /> <span
			class="tooltip_message">?</span>
		<p />
		<input id="submit" type="submit" value="Search" />
	</form>

</div>