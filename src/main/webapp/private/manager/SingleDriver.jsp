<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<jsp:include page="/Header.jsp">
    <jsp:param name="title" value="Driver info" />
    <jsp:param value="manager/manager.css" name="css" />
    <jsp:param value="manager/RemoveDriver.js" name="js" />
</jsp:include>

<jsp:include page="/Menu.jsp">
    <jsp:param name="homeLink" value="/private/manager" />
    <jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<jsp:include page="ext/SingleDriverInfoSnippet.jsp">
    <jsp:param name="privelege" value="edit" />
</jsp:include>

<c:if test="${!empty orderRoute}">
	<!-- Print waypoints-->
	<jsp:include page="ext/WaypointsSnippet.jsp">
		<jsp:param name="orderRouteInfo" value="${orderRoute}" />
	</jsp:include>
</c:if>

<c:if test="${!empty shiftRecords}">
	<!-- Print shift records-->
	<jsp:include page="ext/ShiftRecordsSnippet.jsp">
		<jsp:param name="comment" value="this month" />
		<jsp:param name="shiftRecords" value="${shiftRecords}" />
	</jsp:include>
</c:if>

<jsp:include page="/Footer.jsp" />