<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="panel panel-default">
    <div class="panel-heading">
        <h1>List of drivers</h1>
    </div>
    <div class="panel-body">
        <table class="table table-striped">
            <thead>
                <tr>
                    <th class="text-center">Personal number</th>
                    <th>First name</th>
                    <th>Last name</th>
                    <th>Status</th>
                    <th>Current city</th>
                    <th>Current truck</th>
                    <th>Current order</th>
                    <th>Worked in this month <small>hours</small></th>
                    <th class="text-center">View</th>

                    <%-- Edit priveleges --%>
                    <c:if test="${param.privelege == 'edit'}">
                        <th class="text-center">Delete</th>
                    </c:if>

				</tr>
            </thead>
            <tbody>

                <c:forEach items="${drivers}" var="driver">
                    <tr>

                        <td class="text-center"><c:out value="${driver.personalNumber}" /></td>
                        <td><c:out value="${driver.firstName}" /></td>
                        <td><c:out value="${driver.lastName}" /></td>
                        <td><c:out value="${driver.driverStatus}" /></td>
                        <td><c:out value="${driver.currentCityFK.name}" /></td>
                        <td><c:if test="${empty driver.currentTruckFK.truckNumber}">Not assigned</c:if>
                            <c:out value="${driver.currentTruckFK.truckNumber}" /></td>

                        <td><c:if
                                test="${empty driver.currentTruckFK.orderForThisTruck}">Not assigned</c:if>
                            
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

                        </td>

                        <td><c:out value="${workingHoursForDrivers[driver]}" /></td>

                        <td class="text-center"><a
                            href="${pageContext.request.contextPath}/private/manager/ShowDriver?driverId=${driver.driverId}"><span
                                class="glyphicon glyphicon-info-sign" aria-hidden="true"></span></a>
                        </td>

                         <%-- Edit priveleges --%>
                        <c:if test="${param.privelege == 'edit'}">

                            <td class="text-center"><span
                                    onclick="removeDriver(this, ${driver.driverId})"
                                    class="glyphicon glyphicon-remove red-on-hover"
                                    aria-hidden="true"></span></td>

                        </c:if>

					</tr>
                </c:forEach>

            </tbody>
        </table>
    </div>
</div>