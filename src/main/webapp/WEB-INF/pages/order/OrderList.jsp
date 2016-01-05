<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<jsp:include page="../Header.jsp">
	<jsp:param name="title" value="Order List" />
	<jsp:param value="common.css" name="css" />

</jsp:include>

<jsp:include page="../HeaderMenu.jsp">
	<jsp:param name="homeLink" value="/" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<div class="panel panel-default">
	<div class="panel-heading">
		<h1>List of orders</h1>
	</div>
	<div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Order ID</th>
					<th>Status</th>
					<th>Freight</th>
					<th>Assigned truck</th>
					<th class="text-center">Edit</th>
				</tr>
			</thead>
			<tbody>

				<c:forEach items="${orders}" var="order">
					<tr>

						<td>${order.orderId}</td>
						<td>${order.orderStatus}</td>

						<td><c:choose>
								<c:when test="${empty order.orderLines}">Empty</c:when>
								<c:otherwise>
									<!-- Modal button -->
									<button type="button" class="btn btn-default"
										data-toggle="modal" data-target="#myModal-${order.orderId}">
										<span class="glyphicon glyphicon-menu-down" aria-hidden="true"></span>
										View ${fn:length(order.orderLines)} freight<c:if test="${fn:length(order.orderLines) gt 1}">s</c:if>
									</button>

									<!-- Modal -->
									<div class="modal fade modal-wide" id="myModal-${order.orderId}" tabindex="-1"
										role="dialog" aria-labelledby="myModalLabel">
										<div class="modal-dialog" role="document">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal"
														aria-label="Close">
														<span aria-hidden="true">&times;</span>
													</button>
													<h4 class="modal-title" id="myModalLabel">
														Freights in order #
														<c:out value="${order.orderId}"></c:out>
													</h4>
												</div>
												<div class="modal-body">
												
													<table class="table table-bordered">
														<thead>
															<tr>
																<th>Freight ID</th>
																<th>Freight title</th>
																<th>Freight weight <small>x1000kg</small></th>
																<th>Freight status</th>
																<th>Origin city</th>
																<th>Destination city</th>
															</tr>
														</thead>
														
														<tbody>
														
														  <c:forEach items="${order.orderLines}" var="freight">
														      <tr>
														          <td>${freight.freightId}</td>
														          <td>${freight.description}</td>
														          <td>${freight.weight}</td>
														          <td>${freight.freightStatus}</td>
														          <td>${cities[freight.cityFromFK.cityId].name}</td>
														          <td>${cities[freight.cityToFK.cityId].name}</td>
														      </tr>
														  </c:forEach>
														  
														</tbody>
													</table>
												
												</div>
												<div class="modal-footer">
													<button type="button" class="btn btn-default"
														data-dismiss="modal">Close</button>
												</div>
											</div>
										</div>
									</div>
									<!-- /Modal -->

								</c:otherwise>
							</c:choose></td>

						<td><c:if test="${empty order.assignedTruckFK.truckNumber}">Not assigned</c:if>
							${order.assignedTruckFK.truckNumber}</td>

						<td class="text-center">
						  <a
                                 href="${pageContext.request.contextPath}/order/${order.orderId}/edit"><span
                                    class="glyphicon glyphicon-pencil"
                                    aria-hidden="true"></span></a>
						</td>

					</tr>
				</c:forEach>

			</tbody>
		</table>
	</div>
</div>

<jsp:include page="../Footer.jsp" />