<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/Header.jsp">
    <jsp:param name="title" value="Driver info" />
</jsp:include>

<h1>Sometimes it happens.</h1>

<h1>Work was interrupted. Please check logs</h1>

Message: <br> ${error}

<jsp:include page="/Footer.jsp" />
