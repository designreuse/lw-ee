/**
 * Remove truck.
 * 
 * @param element --
 *            reference to element, that triggered this function (needed to
 *            remove row after succesful truck deletion)
 * @param id -
 *            of the truck
 */
function removeTruck(element, id) {
	bootbox.confirm("Delete truck?", function(result) { // bootboxjs.com
		if (result) {
			$.ajax({
				url : "DeleteTruck",
				type : "POST",
				data : { truckId : id },
				success : function(result) {
					$(element).closest("tr").fadeOut(1000, function() {
						$(this).remove();
					});
				},
				error : function(result) {
					bootbox.alert("Truck is assigned in order and cannt be deleted.");
				}
			});
		}
	});
}