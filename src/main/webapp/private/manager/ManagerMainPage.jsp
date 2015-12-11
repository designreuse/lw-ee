<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/Header.jsp">
    <jsp:param name="title" value="Manager main page" />
</jsp:include>

<jsp:include page="/Menu.jsp">
    <jsp:param name="homeLink" value="/private/manager" />
    <jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<div class="row">

	<!-- Drivers -->
	<div class="col-md-6">
		<div class="well">
			<legend class="the-legend">
				Drivers <span class="glyphicon glyphicon-user" aria-hidden="true"></span>
			</legend>

            <div class="btn-group btn-group-justified" role="group"> 
            
				<a href="${pageContext.request.contextPath}/private/manager/DriverList"
					role="button" class="btn btn-default btn-large btn-block"><span
					class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a>
	
				<a href="${pageContext.request.contextPath}/private/manager/AddDriver"
					role="button" class="btn btn-default btn-large btn-block"><span
					class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</a>
			
			</div>

		</div>
	</div>
	<!-- /Drivers -->

	<!-- Truck -->
	<div class="col-md-6">
		<div class="well">
			<legend class="the-legend">
				Trucks <span class="glyphicon glyphicon-bed" aria-hidden="true"></span>
			</legend>

            <div class="btn-group btn-group-justified" role="group"> 
            
				<a href="${pageContext.request.contextPath}/private/manager/TruckList"
					role="button" class="btn btn-default btn-large"><span
					class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a>
	
				<a href="${pageContext.request.contextPath}/private/manager/AddTruck"
				    role="button" class="btn btn-default btn-large"><span
					class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</a>
				
			</div>
		</div>
	</div>
	<!-- /Truck -->

</div>

<div class="row">

	<!-- Orders -->
	<div class="col-md-6">
		<div class="well">
			<legend class="the-legend">
				Orders <span class="glyphicon glyphicon-briefcase"
					aria-hidden="true"></span>
			</legend>

            <div class="btn-group btn-group-justified" role="group"> 
				<a href="${pageContext.request.contextPath}/private/manager/OrderList"
					role="button" class="btn btn-default btn-large"><span
					class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a>
	
				<a href="${pageContext.request.contextPath}/private/manager/AddOrder"
					role="button" class="btn btn-default btn-large"><span
					class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add</a>
			</div>
			
		</div>
	</div>
	<!-- /Orders -->

    <!-- Cargoes -->
	<div class="col-md-6">
		<div class="well">
			<legend class="the-legend">
				Freights <span class="glyphicon glyphicon-oil" aria-hidden="true"></span>
			</legend>

            <div class="btn-group btn-group-justified" role="group"> 
			<a href="${pageContext.request.contextPath}/private/manager/FreightList" role="button" class="btn btn-default btn-large"><span
				class="glyphicon glyphicon-list" aria-hidden="true"></span> Show All</a>
			</div>
			
		</div>
	</div>
	<!-- /Cargoes -->
	
</div>
<!-- /.row --> 

<jsp:include page="/Footer.jsp"/>