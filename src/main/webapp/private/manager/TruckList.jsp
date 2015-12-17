<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/Header.jsp">
    <jsp:param name="title" value="Trucks List" />
    <jsp:param value="/manager.css" name="css"/>
    <jsp:param value="/DeleteTruck.js" name="js"/>
    
</jsp:include>

<jsp:include page="/Menu.jsp">
    <jsp:param name="homeLink" value="/private/manager" />
    <jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading"><h1>List of trucks</h1></div>
    <div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Truck number</th>
					<th>Driver count</th>
					<th>Freight capacity <small>x1000kg</small></th>
					<th>Status</th>
					<th>Current City</th>
					<th>Current order</th>
					<th>Drivers</th>
                    <th class="text-center">Delete</th>
				</tr>
			</thead>
			<tbody>
		
				<c:forEach items="${trucks}" var="truck">
					<tr id="truck-id-${truck.truckId}-row">
					
						<td>${truck.truckNumber}</td>
						<td>${truck.driverCount}</td>
						<td>${truck.capacity}</td>
						<td>${truck.truckStatus}</td>
						<td>${truck.currentCityFK.name}</td>
						
						<td><c:if
                                test="${empty truck.orderForThisTruck}">Not assigned</c:if>
                            <a href="
                                     <c:url value="/private/manager/EditOrder">
                                <c:param name="orderId" value="${truck.orderForThisTruck.orderId}" />
                            </c:url>">${truck.orderForThisTruck.orderId}</a>

                        </td>
							
						<td><c:if test="${empty truck.driversInTruck}">Not assigned</c:if>
                            <c:forEach items="${truck.driversInTruck}" var="driver">
							    <a href="
		                            <c:url value="/private/manager/ShowDriver">
		                                <c:param name="driverId" value="${driver.driverId}" />
		                            </c:url>">${driver.lastName}</a><span class="comma-separator">,</span>
							</c:forEach>
                        </td>	

		                <td class="text-center">
		                    <span onclick="removeTruck(this, ${truck.truckId})" class="glyphicon glyphicon-remove red-on-hover" aria-hidden="true"></span>
		                </td>
		
					</tr>
				</c:forEach>
		
			</tbody>
		</table>
	</div>
</div>

<jsp:include page="/Footer.jsp"/>