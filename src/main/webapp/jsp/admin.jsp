<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Administrative View</title>
<base href="/eCommerceSystem/">
</head>
<body>
	<div id='centered'>
		<jsp:include page="header.jsp" flush="true" />
	</div>
	<form method='post' action='AdminServlet'>
<%--  	<div id='leftAlign'>
		<!-- sales history list -->
			<!-- for each item in salesHistory list -->
			<c:forEach items="${SALES}" var="sale">
					<input type='radio' id=${sale.orderID } name='salesList' value=${sale.orderID }>
					<label for=${sale.orderID }>${sale.orderID} | ${sale.USER} {</label>
			</c:forEach>

	</div> --%>
	</form>
	
	<h1 class="center">All Accounts</h1>
	
	<div id='rightAlign'>
		<!-- customer info table -->
		<table border ='1' cellpadding='6'>
		<tr>
 		<th> USER ID</th> 
		<th>USER INFO</th>
		<th>CREDIT CARD</th>
		<th>BILLING ADDRESS</th>
		<th>SHIPPING ADDRESS</th>
		</tr>
	<c:forEach items="${accountList}" var="e">
		<tr>
		<td><p>${e.ID} </p></td>
		<td>
		<form action="AdminServlet?action=updateAccountInfo&clientIdA=${e.ID}" method=post>
			USERNAME:<input type='text' size='10' value=${e.username } name="aiu${e.ID}" /><br>
			PASSWORD:<input type='text' size='10' value=${e.password } name="aip${e.ID}" /><br>
			FIRSTNAME<input type='text' size='10' value=${e.firstName } name="aif${e.ID}" /><br>
			LASTNAME:<input type='text' size='10' value=${e.lastName } name="ail${e.ID }" /><br>
			EMAIL:<input type='text' size='16' value=${e.email } name="aie${e.ID}" /><br>
			
				<input type="submit" value="Update" />
		</form>
		<td>
		<form action="AdminServlet?action=updateAccountCC&clientIdA=${e.ID}" method=post>
 		CREDITCARD#:<input type='text' size='15' value=${e.billing.cc } name="cc${e.ID}" /><br>
		EXPIRYMONTH:<input type='text' size='5' value=${e.billing.ccExpiryMon } name="ccem${e.ID}" /><br>
		EXPIRYYEAR:<input type='text' size='5' value=${e.billing.ccExpiryYr } name="ccey${e.ID}" /><br>
		CVV:<input type='text' size='5' value=${e.billing.cvv } name="ccv${e.ID }" /><br>
						<input type="submit" value="Update" />
		</form>
		<td>
		<form action="AdminServlet?action=updateAccountBilling&clientIdA=${e.ID}" method=post>
		ADDRESS:<input type='text' size='10' value="${e.billing.addressB }" name="ba${e.ID}" /><br>
		PROVINCE:<input type='text' size='10' value=${e.billing.provinceB } name="bpr${e.ID}" /><br>
		COUNTRY:<input type='text' size='10' value=${e.billing.countryB } name="bc${e.ID}" /><br>
		ZIPCODE:<input type='text' size='10' value=${e.billing.zipB } name="bz${e.ID}" /><br>
		PHONE#:<input type='text' size='10' value=${e.billing.phoneB } name="bph${e.ID}" /><br>	
						<input type="submit" value="Update" />
		</form>
		</td>
		<td>
		<form action="AdminServlet?action=updateAccountShipping&clientIdA=${e.ID}" method=post>
		ADDRESS<input type='text' size='10' value="${e.shipping.addressS }" name="sa${e.ID}" /><br>
		PROVINCE:<input type='text' size='10' value=${e.shipping.provinceS } name="spr${e.ID}" /><br>
		COUNTRY:<input type='text' size='10' value=${e.shipping.countryS } name="sc${e.ID }" /><br>
		ZIPCODE:<input type='text' size='10' value=${e.shipping.zipS } name="sz${e.ID}" /><br>
		PHONE#:<input type='text' size='10' value=${e.shipping.phoneS } name="sph${e.ID}" /><br>
						<input type="submit" value="Update" />
		</form>
		</td>
		</tr>
	</c:forEach>
	</table>
	</div>
	
	<div class="center">
		<a href="AdminServlet?action=order"><button>Go to Orders</button></a>
		<a href="AdminServlet?action=item"><button>Go to Items</button></a>
	</div>
	
	<!-- inventory listed as optional in document -->
</body>
</html>