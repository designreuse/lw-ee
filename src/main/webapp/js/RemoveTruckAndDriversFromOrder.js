/**
 * Send request to remove Drivers and Truck from order.
 * @param order id
 *
 */
function removeTruckAndDriverFromOrder(orderId) {
	$.ajax({
		type : "POST",
		url : window.location.pathname + "/removeDriversAndTruck",
		dataType : "text",
		success : function(data) {
			location.reload(true);
		},
		error : function(result) {
			bootbox.alert(result.responseText);
		}
	});
}