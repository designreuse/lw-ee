<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/Header.jsp">
	<jsp:param name="title" value="Add driver" />
</jsp:include>

<jsp:include page="/Menu.jsp">
	<jsp:param name="homeLink" value="/private/manager" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>


<form class="form-horizontal" method="POST" action="">
	<fieldset>

		<!-- Form Name -->
		<legend>Add driver</legend>

		<%--Error message --%>
		<c:if test="${not empty error}">
			<div class="form-group">
				<div class="col-md-4">
					<!-- blank -->
				</div>
				<div class="col-md-4 alert alert-warning">
					<strong>Warning!</strong> ${error}
				</div>
			</div>
		</c:if>

		<!-- ID : Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="personal number">Personal Number</label>
			<div class="col-md-4">
				<input id="personal number" name="personalNumber" type="text"
					placeholder="Personal Number" class="form-control input-md" value="${personalNumber}" required="">
			</div>
		</div>

		<!-- Name : Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="first name">First Name</label>
			<div class="col-md-4">
				<input id="first name" name="firstName" type="text" placeholder="First Name"
					class="form-control input-md" required value="${firstName}">

			</div>
		</div>

		<!-- Surname : Text input-->
		<div class="form-group">
			<label class="col-md-4 control-label" for="last name">Last Name</label>
			<div class="col-md-4">
				<input id="last name" name="lastName" type="text" placeholder="Last Name"
					class="form-control input-md" required value="${lastName}">

			</div>
		</div>

		<!-- City: Select Basic -->
		<div class="form-group">
			<label class="col-md-4 control-label" for="city">City</label>
			<div class="col-md-4">
				<select id="city" name="city" class="form-control">
					<c:forEach items="${cities}" var="cityOption">
						<option value="${cityOption.cityId}">
							${cityOption.name}
						</option>
					</c:forEach>
				</select>
			</div>
		</div>

		<!-- Submit -->
		<div class="form-group">
			<label class="col-md-4 control-label"></label>
			<div class="col-md-4">
				<button class="btn btn-primary" type="submit">Create Driver</button>
			</div>
		</div>

	</fieldset>
</form>


<jsp:include page="/Footer.jsp" />