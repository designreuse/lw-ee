<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- Print Journals -->
<div class="panel panel-default">
    <!-- Default panel contents -->
    <div class="panel-heading">Shift records <small>${param.comment}</small></div>
    
    <table class="table">
        <tr>
            <th>Record ID</th>
            <th>Started</th>
            <th>Ended</th>
        </tr>

		<c:forEach var="record" items="${shiftRecords}">
			<tr>
				<td>#<c:out value="${record.driverShiftId}" /></td>
				<td>${record.driverShiftBegin}</td>

				<td>${record.driverShiftEnd}</td>

			</tr>
		</c:forEach>

	</table>

</div>

<div class="container"> 
    <div class="row">
        <div class="col-md-6 col-md-offset-5">
            <div id="cal-heatmap"></div>
        </div>
    </div>
</div>

