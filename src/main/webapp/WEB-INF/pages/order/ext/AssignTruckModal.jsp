<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="modal fade modal-wide" id="assign-truck" tabindex="-1"
	role="dialog">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">
					Assign truck to order #
					<c:out value="${order.orderId}"></c:out>
				</h4>
			</div>
			<div class="modal-body">

				<form class="form-horizontal" id="assignTruckForm" method="POST"
					action="assignTruck">
					<input type="hidden" name="orderId" value="${order.orderId}">

					<fieldset>

						<div class="form-group">
							<label class="col-md-3 control-label" for="truck">Suggested
								trucks</label>
							<div class="col-md-9">
								<select id="truck" name="truckId" class="form-control">
									<c:forEach items="${suggestedTrucks}" var="truck">
										<option value="${truck.truckId}">Number:
											${truck.truckNumber} | Max weight: ${truck.capacity} |
											Current city: ${cities[truck.currentCityFK.cityId].name} | Driver
											count: ${truck.driverCount}</option>
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