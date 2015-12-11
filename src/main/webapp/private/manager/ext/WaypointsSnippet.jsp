<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- OrderRoute info: print waypoints -->
<div class="panel panel-default">
	<!-- Default panel contents -->
	<div class="panel-heading">Waypoints</div>
	
	<table class="table">
        <tr>
            <th>Operation</th>
            <th>City</th>
            <th>Freight</th>
        </tr>
        
        <c:forEach var="waypoint" items="${orderRoute.bestOrderOfDelivery}">
                <tr>
                    <td>
	                    <c:choose>
	                    
						<c:when test="${waypoint.wayPointStatus == 'PICKUP'}">
							<span class="glyphicon glyphicon-import"
								aria-hidden="true"></span> Pickup</c:when>
								
						<c:when test="${waypoint.wayPointStatus == 'DELIVER'}">
							<span class="glyphicon glyphicon-export"
								aria-hidden="true"></span> Deliver</c:when>
						
					</c:choose>
				</td>
                    
                    <td>${waypoint.city.name}</td>
                    <td>#<c:out value="${waypoint.freight.freightId}"/> &mdash; ${waypoint.freight.description} </td>
                </tr>
        </c:forEach>
        
	</table>

</div>