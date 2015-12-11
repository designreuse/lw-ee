<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- Driver info -->
<div class="panel panel-default">
  <div class="panel-heading">
    <h3 class="panel-title">Driver info</h3>
  </div>
  
  <ul class="list-group">
    <li class="list-group-item">Personal Number: <span class="label label-info"> ${driver.personalNumber}</span></li>
    <li class="list-group-item">${driver.firstName} ${driver.lastName}</li>
    <li class="list-group-item">Status: <span class="label label-info">${driver.driverStatus}</span></li>
    <li class="list-group-item">Location: ${driver.currentCityFK.name}</li>
    
    <c:if test="${!empty driver.currentTruckFK}">
        <li class="list-group-item">Current truck: ${driver.currentTruckFK.truckNumber}</li>
    </c:if>
    
    <c:if test="${!empty driver.currentTruckFK && fn:length(driver.currentTruckFK.driversInTruck) > 1}">
        <li class="list-group-item">Co-driver:
            <c:forEach var="coDriver" items="${driver.currentTruckFK.driversInTruck}">
                <c:if test="${coDriver.driverId != driver.driverId}">
                    <a href="
                       <c:url value="/private/manager/ShowDriver">
                           <c:param name="driverId" value="${coDriver.driverId}" />
                       </c:url>">${coDriver.firstName} ${coDriver.lastName}</a><span class="comma-separator">,</span>
                </c:if>
            </c:forEach>
        </li>
    </c:if>

    <c:if test="${!empty driver.currentTruckFK || !empty driver.currentTruckFK.orderForThisTruck}">
			<li class="list-group-item">Current order: 
			   <c:choose>
					<c:when test="${param.privelege == 'edit'}">
						<a href="
						  <c:url value="/private/manager/EditOrder">
                              <c:param name="orderId" value="${driver.currentTruckFK.orderForThisTruck.orderId}" />
                          </c:url>">${driver.currentTruckFK.orderForThisTruck.orderId}</a>
					</c:when>
					<c:otherwise>
					   ${driver.currentTruckFK.orderForThisTruck.orderId}
                    </c:otherwise>
				</c:choose>

			</li>
		</c:if>
    
    <li class="list-group-item">Working hours in this month: <span class="label label-info">${workingHours}</span></li>
        
    </ul>
  
</div>
<!-- /Driver info -->