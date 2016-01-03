<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="../Header.jsp">
    <jsp:param name="title" value="Driver info" />
    <jsp:param value="common.css, cal-heatmap.css" name="css" />
    <jsp:param value="RemoveRecord.js, d3.min.js, cal-heatmap.min.js, showCalHeatMap.js" name="js" />
</jsp:include>

<jsp:include page="../HeaderMenu.jsp">
    <jsp:param name="homeLink" value="/manager" />
</jsp:include>

<jsp:include page="ext/SingleDriverInfoSnippet.jsp"/>

<c:if test="${!empty driver.orderRouteInfoForThisDriver}">
	<!-- Print waypoints-->
	<c:set var="routeInfo" value="${driver.orderRouteInfoForThisDriver}" scope="request" />
	<jsp:include page="../order/ext/WaypointsSnippet.jsp"/>
</c:if>

<c:if test="${!empty driver.driverShiftRecords}">
	<!-- Print shift records-->
	<c:set var="shiftRecords" value="${driver.driverShiftRecords}" scope="request" />
	<jsp:include page="ext/ShiftRecordsSnippet.jsp">
		<jsp:param name="comment" value="this month" />
	</jsp:include>
</c:if>

<jsp:include page="../Footer.jsp" />