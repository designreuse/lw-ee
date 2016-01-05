<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<nav class="navbar navbar-inverse navbar-static-top">
	<div class="container-fluid">

		<!-- Logo -->
		<div class="navbar-header">
			<span class="navbar-brand">
			
				<sec:authorize access="hasRole('ROLE_MANAGER')">
					Manager@LogiWeb
				</sec:authorize>

				<sec:authorize access="hasRole('ROLE_DRIVER')">
					Driver@LogiWeb
				</sec:authorize>
			
			</span>
		</div>
		<!-- /Logo -->

		<!-- Menu Items -->
		<div>
			<ul class="nav navbar-nav">
				<li><a href="<c:url value="${param.homeLink}"/>">Home</a></li>

				<sec:authorize access="hasRole('ROLE_MANAGER')">
					<li><a href="<c:url value="/driver"/>">Drivers</a></li>
					<li><a href="<c:url value="/truck"/>">Trucks</a></li>
					<li><a href="<c:url value="/order"/>">Orders</a></li>
					<li><a href="<c:url value="/freight"/>">Freights</a></li>
				</sec:authorize>

			</ul>

			<ul class="nav navbar-nav navbar-right">
				<li><a href="<c:url value="/logout"/>">Logout</a></li>
			</ul>
		</div>
		

	</div>
</nav>