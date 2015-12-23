/**
 * Remove truck.
 * 
 * @param element --
 *            reference to element, that triggered this function (needed to
 *            remove row after succesful truck deletion)
 * @param id -
 *            of the truck
 */
function deleteTruck(element, truckId) {
	bootbox.confirm("Delete truck?", function(result) {
		if (result) {
			$.ajax({
				url : window.location.pathname + "/" + truckId + "/delete",
				type : "POST",
				dataType : "text",
				success : function(result) {
					$(element).closest("tr").fadeOut(1000, function() {
						$(this).remove();
					});
				},
				error : function(result) {
					bootbox.alert(result.responseText);
				}
			});
		}
	});
}