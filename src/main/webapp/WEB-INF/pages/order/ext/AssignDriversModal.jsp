<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="modal fade modal-wide" id="assign-driver" tabindex="-1"
    role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    Assign drivers to order #
                    <c:out value="${order.orderId}"></c:out>
                    who had less than <fmt:formatNumber value="${maxWorkingHoursLimit - routeInfo.estimatedTime}" pattern="0.0"/>
                    working hours in this month
                </h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" id="assignDriverForm" method="POST" action="addDriverToTruck">
                <input type="hidden" name="truckId" value='<c:if test="${!empty order.assignedTruck }">${order.assignedTruck.truckId}</c:if>'>
                
                <c:if test="${!empty order.assignedTruck && empty order.assignedTruck.driversIdsAndNames}">
                    <input type="hidden" name="maxDriversToAssign" value='${order.assignedTruck.driverCount}'>
                </c:if>
                <c:if test="${!empty order.assignedTruck && !empty order.assignedTruck.driversIdsAndNames}">
                    <input type="hidden" name="maxDriversToAssign" value='${order.assignedTruck.driverCount - fn:length(order.assignedTruck.driversIdsAndNames)}'>
                </c:if>
                
                    <fieldset>
                    
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="driverId">Suggested drivers</label>
                            <div class="col-md-9">

                                <c:forEach items="${suggestedDrivers}" var="driver">
                                    <div>
                                        <label for="driver-checkbox-${driver.driverId}"> <input
                                            type="checkbox" name="driversIds"
                                            id="driver-checkbox-${driver.driverId}" value="${driver.driverId}">
                                            ${driver.firstName} ${driver.lastName} | Current city:
                                            ${cities[driver.currentCityId].name} | This month working hours: <fmt:formatNumber
                                                type="number" value="${driver.workingHoursThisMonth}"
                                                pattern=".#" />
                                        </label>
                                    </div>
                                </c:forEach>

                            </div>
                        </div>
                        
                    </fieldset>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" 
                    onclick="postFormByAjax('#assignDriverForm')">Save</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>