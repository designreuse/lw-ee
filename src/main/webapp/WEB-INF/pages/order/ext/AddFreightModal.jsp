<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="modal fade modal-wide" id="add-freight" tabindex="-1"
    role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                    aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">
                    Add freight to order #
                    <c:out value="${order.orderId}"></c:out>
                </h4>
            </div>
            <div class="modal-body">

                <form class="form-horizontal" id="addFreightForm" method="POST" action="addFreight">
                <input type="hidden" name="orderId" value="${order.orderId}">
                
                    <fieldset>
                    
                        <!-- Title: Text input-->
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="freightTitle">Freight
                                title</label>
                            <div class="col-md-4">
                                <input id="freightTitle" name="freightTitle" type="text"
                                    placeholder="Title" class="form-control input-md" required="">

                            </div>
                        </div>

                        <!-- Weight: Text input-->
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="freightWeight">Freight
                                weight <small>x1000kg</small></label>
                            <div class="col-md-4">
                                <input id="freightWeight" name="freightWeight" type="text"
                                    placeholder="Weight" class="form-control input-md" required="">

                            </div>
                        </div>
                        

                        <!-- Origin: Select Basic -->
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="originCityId">Origin
                                City</label>
                            <div class="col-md-4">
                                <select id="originCityId" name="originCityId" class="form-control">
                                    <c:forEach items="${cities}" var="entry">
                                        <option value="${entry.key}">${entry.value.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <!-- Origin: Select Basic -->
                        <div class="form-group">
                            <label class="col-md-4 control-label" for="destinationCityId">Destination
                                City</label>
                            <div class="col-md-4">
                                <select id="destinationCityId" name="destinationCityId"
                                    class="form-control">
                                    <c:forEach items="${cities}" var="entry">
                                        <option value="${entry.key}">${entry.value.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                    </fieldset>
                </form>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" 
                    onclick="postFormByAjax('#addFreightForm')">Save</button>
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>