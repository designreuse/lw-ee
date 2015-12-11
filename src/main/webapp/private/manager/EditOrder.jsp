<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@page import="ru.tsystems.javaschool.kuzmenkov.logiweb.entities.status.OrderStatus"%>
<% pageContext.setAttribute("orderNotReady", OrderStatus.CREATED); %>

<jsp:include page="/Header.jsp">
	<jsp:param name="title" value="Edit order # ${orderId}" />
	<jsp:param value="manager.css" name="css" />
	<jsp:param
		value="/PostFormByAjax.js, /ChangeOrderStatus.js, /LimitCheckboxesForDriverAssignment.js"
		name="js" />
</jsp:include>

<jsp:include page="/Menu.jsp">
	<jsp:param name="homeLink" value="/private/manager" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<!-- Edit Order -->
<div class="panel panel-primary">
	<div class="panel-heading">
		<h1>
			Edit order #<c:out value="${orderId}"/>
		</h1>
	</div>

	<div class="panel-body">

		<!-- Info -->
		<div class="panel panel-default">
			<div class="panel-heading">
				<h3 class="panel-title">Order info</h3>
			</div>
			<div class="panel-body">

			 <h4>Max weight<small> x1000kg</small>:<span class="label label-info">${orderRoute.maxWeightOnCourse}</span></h4>
			 <h4>Estimated time to deliver<small> hours</small>:<span class="label label-info"><fmt:formatNumber value="${orderRoute.estimatedTime}" pattern="0.0"/></span></h4>
			 
			 <h4>Assigned truck<small> truck number</small>:
			     <c:choose>
			         <c:when test="${empty order.assignedTruckFK}"><span class="label label-warning">Not assigned</span></c:when>
			         <c:otherwise><span class="label label-success">${order.assignedTruckFK.truckNumber }</span></c:otherwise>
			     </c:choose>
			 </h4>
			 
			 <h4>Assigned drivers 
			     <c:if test="${!empty order.assignedTruckFK}">
			         (${fn:length(order.assignedTruckFK.driversInTruck)} / ${order.assignedTruckFK.driverCount})
                 </c:if>:
                 
                 <c:choose>
                     <c:when test="${!empty order.assignedTruckFK && !empty order.assignedTruckFK.driversInTruck}">
                        <c:forEach items="${order.assignedTruckFK.driversInTruck}" var="driver">
                            <a href="
                                    <c:url value="/private/manager/ShowDriver">
                                        <c:param name="driverId" value="${driver.driverId}" />
                                    </c:url>">${driver.firstName} ${driver.lastName}</a><span class="comma-separator">,</span>
                        </c:forEach>
                     </c:when>
                     <c:otherwise><span class="label label-danger">Not assigned</span></c:otherwise>
                 </c:choose>
             </h4>
			
			<h4>Status: ${order.orderStatus}</h4>




				<div class="btn-group-vertical pull-left" role="group" aria-label="...">

					<!-- Add cargo -->
					<button type="button" class="btn btn-default btn-lg <c:if test="${order.orderStatus != orderNotReady || !empty order.assignedTruckFK}">disabled</c:if>" data-toggle="modal" data-target="#add-cargo">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-oil" aria-hidden="true"></span> Add
						freight
					</button>

					<!-- Assign truck -->
					<button type="button"
							class="btn btn-default btn-lg <c:if test="${order.orderStatus != orderNotReady || !empty order.assignedTruckFK }">disabled</c:if>"
							data-toggle="modal" data-target="#assign-truck">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-bed" aria-hidden="true"></span> Assign
						truck
					</button>

					<!-- Assign driver to tuck -->
					<button type="button" class="btn btn-default btn-lg <c:if test="${empty order.assignedTruckFK || (!empty order.assignedTruckFK.driversInTruck && fn:length(order.assignedTruckFK.driversInTruck) >= order.assignedTruckFK.driverCount)}">disabled</c:if>"
							data-toggle="modal" data-target="#assign-driver">
						<span class="glyphicon glyphicon-plus" aria-hidden="true"></span><span
							class="glyphicon glyphicon-user" aria-hidden="true"></span>
						Assign drivers to Truck
					</button>

					<!-- Remove drivers and truck -->
					<c:if test="${order.orderStatus == orderNotReady && !empty order.assignedTruckFK}">
						<form action="RemoveDriversAndTruckFromOrder?orderId=${order.orderId}" method="post">
							<button type="submit" class="btn btn-default btn-lg">
								<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
								Remove truck and drivers
							</button>
						</form>
					</c:if>

					<!-- Status change -->

					<form action="ChangeOrderStatus?orderId=${order.orderId}" method="post">
						<button type="submit" class="btn btn-default btn-lg

					    <c:choose>
	                       <c:when test="${order.orderStatus == orderNotReady && !empty order.assignedTruckFK && (!empty order.assignedTruckFK.driversInTruck && fn:length(order.assignedTruckFK.driversInTruck) >= order.assignedTruckFK.driverCount)}"></c:when>

	                       <c:otherwise>disabled</c:otherwise>
                        </c:choose>"><span class="glyphicon glyphicon-thumbs-up" aria-hidden="true"></span>
							Ready!
						</button>
					</form>

				</div>
			</div>
		</div>
		<!-- /Info -->


        <!-- Cargo list -->
		<div class="panel panel-info">
			<div class="panel-heading">
				<h5>
					Freights in order
				</h5>
			</div>
			<div class="panel-body">
				<table class="table">
					<thead>
						<tr>
							<th>Freight ID</th>
							<th>Freight description</th>
							<th>Freight weight <small>x1000kg</small></th>
							<th>Freight status</th>
							<th>City from</th>
							<th>City to</th>
						</tr>
					</thead>

					<tbody>

						<c:forEach items="${order.orderLines}" var="freight">
							<tr>
								<td>${freight.freightId}</td>
								<td>${freight.description}</td>
								<td>${freight.weight}</td>
								<td>${freight.freightStatus}</td>
								<td>${freight.cityFromFK.name}</td>
								<td>${freight.cityToFK.name}</td>
							</tr>
						</c:forEach>

					</tbody>
				</table>
			</div>
		</div>
		<!-- /Cargo list -->

		<!-- Print waypoints-->
		<jsp:include page="ext/WaypointsSnippet.jsp">
			<jsp:param name="routeInfo" value="${orderRoute}" />
		</jsp:include>

	</div>

</div>

<!-- Modal: Add cargo -->
<div class="modal fade modal-wide" id="add-cargo" tabindex="-1"
	 role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					Add cargo to order #
					<c:out value="${order.orderId}"></c:out>
				</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal" id="addCargoForm" method="POST" action="AddFreight">
					<input type="hidden" name="orderId" value="${order.orderId}">

					<fieldset>

						<!-- Title: Text input-->
						<div class="form-group">
							<label class="col-md-4 control-label" for="cargoTitle">Cargo
								Title</label>
							<div class="col-md-4">
								<input id="cargoTitle" name="cargoTitle" type="text"
									   placeholder="Title" class="form-control input-md" required="">

							</div>
						</div>

						<!-- Weight: Text input-->
						<div class="form-group">
							<label class="col-md-4 control-label" for="cargoWeight">Cargo
								Weight <small>x1000kg</small></label>
							<div class="col-md-4">
								<input id="cargoWeight" name="cargoWeight" type="text"
									   placeholder="Wieght" class="form-control input-md" required="">

							</div>
						</div>


						<!-- Origin: Select Basic -->
						<div class="form-group">
							<label class="col-md-4 control-label" for="originCity">Origin
								City</label>
							<div class="col-md-4">
								<select id="originCity" name="originCity" class="form-control">
									<c:forEach items="${cities}" var="cityOption">
										<option value="${cityOption.cityId}">${cityOption.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<!-- Origin: Select Basic -->
						<div class="form-group">
							<label class="col-md-4 control-label" for="destinationCity">Destination
								City</label>
							<div class="col-md-4">
								<select id="destinationCity" name="destinationCity"
										class="form-control">
									<c:forEach items="${cities}" var="cityOption">
										<option value="${cityOption.cityId}">${cityOption.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>

					</fieldset>
				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-success"
						onclick="postFormByAjax('#addCargoForm')">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- /Modal -->

<!-- Modal: Assign truck -->
<div class="modal fade modal-wide" id="assign-truck" tabindex="-1"
	 role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel1">
					Assign truck to order #
					<c:out value="${order.orderId}"></c:out>
				</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal" id="assignTruckForm" method="POST" action="AssignTruck">
					<input type="hidden" name="orderId" value="${order.orderId}">

					<fieldset>

						<div class="form-group">
							<label class="col-md-3 control-label" for="truck">Suggested trucks</label>
							<div class="col-md-9">
								<select id="truck" name="truckId" class="form-control">
									<c:forEach items="${suggestedTrucks}" var="truck">
										<option value="${truck.truckId}">
											Truck number: ${truck.truckNumber} |
											Max weight: ${truck.capacity} |
											Currnet city: ${truck.currentCityFK.name} |
											Driver count: ${truck.driverCount}
										</option>
									</c:forEach>
								</select>
							</div>
						</div>

					</fieldset>
				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-success"
						onclick="postFormByAjax('#assignTruckForm')">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- /Modal Assign truck -->


<!-- Modal: Assign driver -->
<div class="modal fade modal-wide" id="assign-driver" tabindex="-1"
	 role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel2">
					Assign drivers to order #
					<c:out value="${order.orderId}"></c:out>
					who had less than <fmt:formatNumber value="${maxWorkingHoursLimit - orderRoute.estimatedTime}" pattern="0.0"/>
					working hours in this month
				</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal" id="assignDriverForm" method="POST" action="AssignDriver">
					<input type="hidden" name="truckId" value='<c:if test="${!empty order.assignedTruckFK }">${order.assignedTruckFK.truckId}</c:if>'>

					<c:if test="${!empty order.assignedTruckFK && empty order.assignedTruckFK.driversInTruck}">
						<input type="hidden" name="maxDriversToAssign" value='${order.assignedTruckFK.driverCount}'>
					</c:if>
					<c:if test="${!empty order.assignedTruckFK && !empty order.assignedTruckFK.driversInTruck}">
						<input type="hidden" name="maxDriversToAssign" value='${order.assignedTruckFK.driverCount - fn:length(order.assignedTruckFK.driversInTruck)}'>
					</c:if>

					<fieldset>

						<div class="form-group">
							<label class="col-md-3 control-label" for="driverId">Suggested drivers</label>
							<div class="col-md-9">

								<c:forEach items="${suggestedDrivers}" var="driver">
									<div>
										<label for="driver-checkbox-${driver.driverId}"> <input
												type="checkbox" name="driversIds"
												id="driver-checkbox-${driver.driverId}" value="${driver.driverId}">
												${driver.firstName} ${driver.lastName} | Currnet city:
												${driver.currentCityFK.name} | This month working hours: ${workingHoursForDrivers[driver]}

										</label>
									</div>
								</c:forEach>

							</div>
						</div>

					</fieldset>
				</form>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-success"
						onclick="postFormByAjax('#assignDriverForm')">Save</button>
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			</div>
		</div>
	</div>
</div>
<!-- /Modal Assign driver -->


<jsp:include page="/Footer.jsp" />