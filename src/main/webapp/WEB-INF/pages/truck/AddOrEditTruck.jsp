<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>


<c:choose>
    <c:when test="${formAction == 'new'}">
       <c:set var="title" value="Add truck"></c:set>
       <c:set var="buttonText" value="Create truck"></c:set>
    </c:when>
    <c:when test="${formAction == 'edit'}">
       <c:set var="title" value="Edit truck"></c:set>
       <c:set var="buttonText" value="Edit truck"></c:set>
    </c:when>
</c:choose>

<jsp:include page="../Header.jsp">
	<jsp:param name="title" value="${title}" />
	<jsp:param value="PlateValidator.js" name="js"/>
</jsp:include>

<jsp:include page="../HeaderMenu.jsp">
	<jsp:param name="homeLink" value="/" />
	<jsp:param name="userRoleForTitle" value="Manager" />
</jsp:include>

<form:form modelAttribute="truckFromForm" method="POST" cssClass="form-horizontal">
    <fieldset>

		<!-- Form Name -->
		<legend>${title}</legend>
		
		<%--Error message --%>
		<c:if test="${not empty error}">
			<div class="form-group">
			    <div class="col-md-4"><!-- blank --></div>
				<div class="col-md-4 alert alert-warning">
					<strong>Warning!</strong> ${error}
				</div>
			</div>
		</c:if>
		
		<form:hidden path="truckId" />

		<!-- Truck number: Text input-->
		<spring:bind path="truckNumber">
          <div class="form-group ${status.error ? 'has-error' : ''}">
            <label class="col-md-4 control-label">Truck number</label>
            <div class="col-md-4">
                <form:input path="truckNumber" type="text" class="form-control input-md"
                                id="truckNumber" placeholder="Truck number" />
                <form:errors path="truckNumber" class="control-label has-error" />
            </div>
          </div>
        </spring:bind>

		<!-- Driver count: Multiple Radios (inline) -->
		<spring:bind path="driverCount">
			<div class="form-group ${status.error ? 'has-error' : ''}">
				<label class="col-md-4 control-label">Driver count</label>
				<div class="col-md-4">
				
					<c:forEach var="i" begin="1" end="2">
					    <label class="radio-inline">
	                        <form:radiobutton path="driverCount" value="${i}"/> ${i}
	                    </label>
					</c:forEach>
					
					<form:errors path="driverCount" class="control-label has-error" />
				</div>
			</div>
		</spring:bind>

		<!-- Capacity: Text input-->
		<spring:bind path="capacity">
          <div class="form-group ${status.error ? 'has-error' : ''}">
            <label class="col-md-4 control-label">Freight Capacity</label>
            <div class="col-md-4">
                <form:input path="capacity" type="text" class="form-control input-md"
                                id="capacity" placeholder="Freight capacity" />
                <form:errors path="capacity" class="control-label has-error" />
            </div>
          </div>
        </spring:bind>

		<!-- City: Select Basic -->
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
        
        <!-- Status -->
        <c:if test="${formAction == 'edit'}">
            <spring:bind path="truckStatus">
                <div class="form-group ${status.error ? 'has-error' : ''}">
                    <label class="col-md-4 control-label">Status</label>
                    <div class="col-md-4">
                        <form:select path="truckStatus" class="form-control">
                            <form:options items="${truckStatuses}"/>
                        </form:select>
                        <form:errors path="truckStatus" class="control-label" />
                    </div>
                </div>
            </spring:bind>
        </c:if>
        
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