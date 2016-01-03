<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!-- Driver info -->
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">Driver info</h3>
  </div>
  
  <ul class="list-group">
    <li class="list-group-item">Personal number: <span class="label label-info"> ${driver.personalNumber}</span></li>
    <li class="list-group-item">Name: ${driver.firstName} ${driver.lastName}</li>
    <li class="list-group-item">Status: <span class="label label-info">${driver.driverStatus}</span></li>
    <li class="list-group-item">Location: ${driver.currentCityFK.name}</li>
    
    <c:if test="${!empty driver.currentTruckFK.truckNumber}">
        <li class="list-group-item">Current truck: ${driver.currentTruckFK.truckNumber}</li>
    </c:if>
    
    <c:if test="${!empty driver.currentTruckFK.driversInTruck}">
        <li class="list-group-item">Co-driver:
            <c:forEach var="coDriverId" items="${driver.currentTruckFK.driversInTruck}">
                <c:if test="${coDriverId.driverId != driver.driverId}">
                    <a href="${pageContext.request.contextPath}/driver/${coDriverId.driverId}">
                        ${coDrivers[coDriverId.driverId].lastName}</a><span class="comma-separator">,</span>
                </c:if>
            </c:forEach>
        </li>
    </c:if>

    <c:if test="${!empty driver.currentTruckFK.orderForThisTruck}">
			<li class="list-group-item">Current order: 
			   

				     <a href="${pageContext.request.contextPath}/order/${driver.currentTruckFK.orderForThisTruck.orderId}">
				     ${driver.currentTruckFK.orderForThisTruck.orderId}</a>

                <sec:authorize access="hasRole('ROLE_DRIVER')">
					   ${driver.currentTruckFK.orderForThisTruck.orderId}
                </sec:authorize>

			</li>
		</c:if>
    
    <li class="list-group-item">Working hours in this month: <span class="label label-info">
        <fmt:formatNumber type="number" value="${driver.workingHoursThisMonth}" pattern="" /></span></li>
        
    </ul>
  
</div>
<!-- /Driver info -->