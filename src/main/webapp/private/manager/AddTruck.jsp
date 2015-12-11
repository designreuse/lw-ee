<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/Header.jsp">
	<jsp:param name="title" value="Add truck" />
</jsp:include>

<jsp:include page="/Menu.jsp">
	<jsp:param name="homeLink" value="/private/manager" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>


<form class="form-horizontal" method="POST" action="">
	<fieldset>

		<!-- Form Name -->
		<legend>Add truck</legend>
		
		<%--Error message --%>
		<c:if test="${not empty error}">
			<div class="form-group">
			    <div class="col-md-4"><!-- blank --></div>
				<div class="col-md-4 alert alert-warning">
					<strong>Warning!</strong> ${error}
				</div>
			</div>
		</c:if>

		<!-- License Plate: Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="truckNumber">Truck number</label>
			<div class="col-md-4">
				<input id="truckNumber" name="truckNumber" type="text"
					placeholder="Truck number" class="form-control input-md"
					required value="${truckNumber}"> <span class="help-block">2 letters + 5
					digits</span>
			</div>
		</div>

		<!-- Capacity: Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="capacity">Freight capacity</label>
			<div class="col-md-4">
				<input id="capacity" name="capacity" type="text"
					   placeholder="Tons" class="form-control input-md" value="${cargoCapacity}">
				<span class="help-block">x1000kg</span>
			</div>
		</div>

		<!-- Crew Size: Multiple Radios (inline) -->
		<div class="form-group">
			<label class="col-md-4 control-label" for="driverCount">Driver count</label>
			<div class="col-md-4">
				<label class="radio-inline" for="driverCount-0"> <input
					type="radio" name="driverCount" id="driverCount-0" value="1"
					<c:if test="${empty driverCount || driverCount == 1}">checked="checked"</c:if>> 1
				</label> <label class="radio-inline" for="driverCount-1"> <input
					type="radio" name="driverCount" id="driverCount-1" value="2"
					<c:if test="${not empty driverCount && driverCount == 2}">checked="checked"</c:if>> 2
				</label>
			</div>
		</div>

		<!-- City: Select Basic -->
		<div class="form-group">
			<label class="col-md-4 control-label" for="city">City</label>
			<div class="col-md-4">
				<select id="city" name="city" class="form-control">
					<c:forEach items="${cities}" var="cityOption">
						<option value="${cityOption.cityId}">
						${cityOption.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>

		<!-- Submit -->
		<div class="form-group">
			<label class="col-md-4 control-label"></label>
			<div class="col-md-4">
				<button class="btn btn-primary" type="submit">Create Truck</button>
			</div>
		</div>

	</fieldset>
</form>


<jsp:include page="/Footer.jsp" />