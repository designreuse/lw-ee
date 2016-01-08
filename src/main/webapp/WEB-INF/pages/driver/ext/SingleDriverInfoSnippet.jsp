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
    <li class="list-group-item">Location: ${cities[driver.currentCityId].name}</li>
    
    <c:if test="${!empty driver.currentTruckNumber}">
        <li class="list-group-item">Current truck: ${driver.currentTruckNumber}</li>
    </c:if>
    
    <c:if test="${!empty driver.coDriversIds}">
        <li class="list-group-item">Co-driver:
            <c:forEach var="coDriverId" items="${driver.coDriversIds}">
                <c:if test="${coDriverId != driver.driverId}">
                    <a href="${pageContext.request.contextPath}/driver/${coDriverId}">
                        ${coDrivers[coDriverId].lastName}</a><span class="comma-separator">,</span>
                </c:if>
            </c:forEach>
        </li>
    </c:if>

    <c:if test="${!empty driver.currentOrderId}">
			<li class="list-group-item">Current order:

                <sec:authorize access="hasRole('ROLE_MANAGER')">
				     <a href="${pageContext.request.contextPath}/order/${driver.currentOrderId}">
				     ${driver.currentOrderId}</a>
                </sec:authorize>
                <sec:authorize access="hasRole('ROLE_DRIVER')">
					   ${driver.currentOrderId}
                </sec:authorize>

			</li>
		</c:if>
    
    <li class="list-group-item">Working hours in this month: <span class="label label-info">
        <fmt:formatNumber type="number" value="${driver.workingHoursThisMonth}" pattern="" /></span></li>
        
    </ul>
  
</div>
<!-- /Driver info -->