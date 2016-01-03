/**
 * Change status of the order to READY TO GO.
 *
 */
function changeOrderStatusToReady(orderId) {
	bootbox.confirm("Status change can't be reversed. Are you sure?", function(result) { // bootboxjs.com
		if (result) {
			$.ajax({
				url : window.location.pathname + "/setStatusReady",
				type : "POST",
				data : { orderId : orderId },
				success : function(result) {
					location.reload(true);
				},
				error : function(result) {
					bootbox.alert(result.responseJSON.msg);
				}
			});
		}
	});
}