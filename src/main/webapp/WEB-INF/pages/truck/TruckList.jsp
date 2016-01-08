<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<jsp:include page="../Header.jsp">
    <jsp:param name="title" value="Trucks List" />
    <jsp:param value="common.css" name="css"/>
    <jsp:param value="DeleteTruck.js" name="js"/>
</jsp:include>

<jsp:include page="../HeaderMenu.jsp">
    <jsp:param name="homeLink" value="/" />
    <jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<div class="panel panel-default">
    <div class="panel-heading"><h1>List of trucks</h1></div>
	<%-- Edit priveleges --%>
	<sec:authorize access="hasRole('ROLE_MANAGER')">
		<div class="panel-footer">
			<a href="${pageContext.request.contextPath}/truck/new" role="button"
			   class="btn btn-default btn-large btn-block"><span
					class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add new truck</a>
		</div>
	</sec:authorize>

    <div class="panel-body">
		<table class="table table-striped">
			<thead>
				<tr>
					<th>Truck number</th>
					<th>Driver count</th>
					<th>Capacity <small>x1000kg</small></th>
					<th>Status</th>
					<th>Current City</th>
					<th>Current order</th>
					<th>Drivers</th>
					<th class="text-center">Edit</th>
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
						<td>${cities[truck.currentCityId].name}</td>
						
						<td><c:if test="${empty truck.assignedOrderId}">Not assigned</c:if>
	                            <a href="${pageContext.request.contextPath}/order/${truck.assignedOrderId}">
	                                ${truck.assignedOrderId}
	                            </a>
                        </td>
							
						<td><c:if test="${empty truck.driversIdsAndNames}">Not assigned</c:if>
                            <c:forEach items="${truck.driversIdsAndNames}" var="entry">
							    <a href="
		                            <c:url value="/driver/${entry.key}">
		                            </c:url>">${entry.value}</a><span class="comma-separator">,</span>
							</c:forEach>
                        </td>	
                            
						<td class="text-center">
						  <c:choose>
						      <c:when test="${empty truck.assignedOrderId}">
						          <a
                                href="${pageContext.request.contextPath}/truck/${truck.truckId}/edit"><span
                                    class="glyphicon glyphicon-pencil" aria-hidden="true"></span></a>
						      </c:when>
						      <c:otherwise>
						          <span
                                    class="glyphicon glyphicon-pencil disabled-color" aria-hidden="true"></span>
						      </c:otherwise>
						  </c:choose>
							 
						</td>
		                
		                <td class="text-center">
		                    <span onclick="deleteTruck(this, ${truck.truckId})" class="glyphicon glyphicon-remove red-on-hover" aria-hidden="true"></span>
		                </td>
		
					</tr>
				</c:forEach>
		
			</tbody>
		</table>
	</div>

    
</div>

<jsp:include page="../Footer.jsp"/>