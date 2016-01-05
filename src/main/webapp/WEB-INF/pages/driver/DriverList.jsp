<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="../Header.jsp">
	<jsp:param name="title" value="Drivers List" />
	<jsp:param value="common.css" name="css" />
	<jsp:param value="DeleteDriver.js" name="js" />
</jsp:include>

<jsp:include page="../HeaderMenu.jsp">
	<jsp:param name="homeLink" value="/" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<jsp:include page="ext/DriverListSnippet.jsp">
    <jsp:param name="privelege" value="edit" />
</jsp:include>

<jsp:include page="../Footer.jsp" />