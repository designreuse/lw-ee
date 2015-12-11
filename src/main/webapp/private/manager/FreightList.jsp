<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="/Header.jsp">
	<jsp:param name="title" value="Cargoes List" />
	<jsp:param value="manager/manager.css" name="css" />
</jsp:include>

<jsp:include page="/Menu.jsp">
	<jsp:param name="homeLink" value="/private/manager" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<div class="panel panel-default">
	<div class="panel-heading">
		<h1>List of cargoes</h1>
	</div>
	<div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Freight ID</th>
					<th>Status</th>
					<th>Description</th>
					<th>Weight <small>x1000kg</small></th>
					<th>City from</th>
					<th>City to</th>
					<th class="text-center">Order</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${freights}" var="freight">
					<tr>
					   <td>${freight.freightId}</td>
					   <td>${freight.freightStatus}</td>
					   <td>${freight.description}</td>
					   <td>${freight.weight}</td>
					   <td>${freight.cityFromFK.name}</td>
					   <td>${freight.cityToFK.name}</td>

						<td class="text-center"><a href="
                            <c:url value="/private/manager/EditOrder">
                                <c:param name="orderId" value="${freight.orderForThisFreightFK.orderId}" />
                            </c:url>">${freight.orderForThisFreightFK.orderId}</a>
                        </td>

					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>
</div>

<jsp:include page="/Footer.jsp" />