<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="../Header.jsp">
	<jsp:param name="title" value="Freights List" />
	<jsp:param value="common.css" name="css" />
</jsp:include>

<jsp:include page="../HeaderMenu.jsp">
	<jsp:param name="homeLink" value="/" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<div class="panel panel-default">
	<div class="panel-heading">
		<h1>List of freights</h1>
	</div>
	<div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Freight ID</th>
					<th>Status</th>
					<th>Title</th>
					<th>Weight <small>x1000kg</small></th>
					<th>Origin city</th>
					<th>Destination city</th>
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

						<td class="text-center">
                            <a href="${pageContext.request.contextPath}/order/${freight.orderForThisFreightFK.orderId}">
                                ${freight.orderForThisFreightFK.orderId}
                            </a>
                        </td>

					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../Footer.jsp" />