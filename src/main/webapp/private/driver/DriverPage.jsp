<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="/Header.jsp">
    <jsp:param name="title" value="Driver info" />
</jsp:include>

<jsp:include page="/DriverMenu.jsp">
    <jsp:param name="userRoleForTitle" value="Driver" />
</jsp:include>

<jsp:include page="../manager/ext/SingleDriverInfoSnippet.jsp">
    <jsp:param name="privelege" value="viewOnly" />
</jsp:include>

<c:if test="${driver.driverStatus == 'REST'}">
	<form action="StartShift?driverId=${driverId}" method="POST">
		<button type="submit" class="btn btn-default btn-lg">
			<span class="glyphicon glyphicon-refresh" aria-hidden="true"></span>
			Stat shift
		</button>
	</form>
</c:if>
<div>

</div>

<c:if test="${!empty orderRoute}">
	<!-- Print waypoints-->
	<jsp:include page="../manager/ext/WaypointsSnippet.jsp">
		<jsp:param name="routeInfo" value="${orderRoute}" />
	</jsp:include>
</c:if>

<c:if test="${!empty shiftRecords}">
	<!-- Print shift records-->
	<jsp:include page="../manager/ext/ShiftRecordsSnippet.jsp">
		<jsp:param name="comment" value="this month" />
		<jsp:param name="journals" value="${shiftRecords}" />
	</jsp:include>
</c:if>

<jsp:include page="/Footer.jsp" />