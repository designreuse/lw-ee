<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<nav class="navbar navbar-inverse navbar-static-top">
    <div class="container-fluid">

        <!-- Logo -->
        <div class="navbar-header">
			<span class="navbar-brand">

				<c:choose>
                    <c:when test="${!empty param.userRoleForTitle}">${param.userRoleForTitle}@LogiWeb</c:when>
                    <c:otherwise>User@Logiweb</c:otherwise>
                </c:choose>

			</span>
        </div>


        <!-- Menu Items -->
        <div>
            <ul class="nav navbar-nav">
                <li><a href="<c:url value="${param.homeLink}"/>">Home</a></li>
            </ul>

            <ul class="nav navbar-nav navbar-right">
                <li><a href="${pageContext.request.contextPath}/Logout">Logout</a></li>
            </ul>
        </div>

    </div>
</nav>