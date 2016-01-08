<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<c:choose>
	<c:when test="${formAction == 'new'}">
	   <c:set var="title" value="Add driver"></c:set>
	   <c:set var="buttonText" value="Create driver"></c:set>
	</c:when>
	<c:when test="${formAction == 'edit'}">
	   <c:set var="title" value="Edit driver"></c:set>
	   <c:set var="buttonText" value="Edit driver"></c:set>
	</c:when>
</c:choose>


<jsp:include page="../Header.jsp">
	<jsp:param name="title" value="${title}" />
</jsp:include>

<jsp:include page="../HeaderMenu.jsp">
	<jsp:param name="homeLink" value="/" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<form:form modelAttribute="driverFromForm" method="POST" cssClass="form-horizontal">
	<fieldset>

		<!-- Form Name -->
		<legend>${title}</legend>

		<%--Error message --%>
		<c:if test="${not empty error}">
			<div class="form-group">
				<div class="col-md-4">
				</div>
				<div class="col-md-4 alert alert-warning">
					<strong>Warning!</strong> ${error}
					<form:errors path="*"/>
				</div>
			</div>
		</c:if>

        <form:hidden path="driverId" />

        <spring:bind path="personalNumber">
          <div class="form-group ${status.error ? 'has-error' : ''}">
            <label class="col-md-4 control-label">Personal number</label>
            <div class="col-md-4">
                <form:input path="personalNumber" type="text" class="form-control input-md"
                                id="personalNumber" placeholder="Personal number" />
                <form:errors path="personalNumber" class="control-label has-error" />
            </div>
          </div>
        </spring:bind>
        
		<spring:bind path="firstName">
          <div class="form-group ${status.error ? 'has-error' : ''}">
            <label class="col-md-4 control-label">First name</label>
            <div class="col-md-4">
                <form:input path="firstName" type="text" class="form-control input-md"
                                id="firstName" placeholder="First name" />
                <form:errors path="firstName" class="control-label has-error" />
            </div>
          </div>
        </spring:bind>
        
        <spring:bind path="lastName">
          <div class="form-group ${status.error ? 'has-error' : ''}">
            <label class="col-md-4 control-label">Last name</label>
            <div class="col-md-4">
                <form:input path="lastName" type="text" class="form-control input-md"
                                id="lastName" placeholder="Last name" />
                <form:errors path="lastName" class="control-label" />
            </div>
          </div>
        </spring:bind>
        
        <spring:bind path="currentCityId">
          <div class="form-group ${status.error ? 'has-error' : ''}">
            <label class="col-md-4 control-label">City</label>
            <div class="col-md-4">
                <form:select path="currentCityId" class="form-control">
                    <form:options items="${cities}" itemLabel="name"/>
                </form:select>
                <form:errors path="currentCityId" class="control-label" />
            </div>
          </div>
        </spring:bind>

		<!-- Submit -->
		<div class="form-group">
			<label class="col-md-4 control-label"></label>
			<div class="col-md-4">
				<button class="btn btn-primary" type="submit">${buttonText}</button>
			</div>
		</div>

	</fieldset>
</form:form>

<jsp:include page="../Footer.jsp" />