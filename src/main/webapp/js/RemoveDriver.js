/**
 * Remove driver.
 * 
 * @param element --
 *            reference to element, that triggered this function (needed to
 *            remove row after succesful truck deletion)
 * @param id -
 *            of the driver
 *
 */
function removeDriver(element, id) {
	bootbox.confirm("Delete driver?", function(result) {
		if (result) {
			$.ajax({
				url : "DeleteDriver",
				type : "POST",
				data : { driverId: id },
				success : function(result) {
					$(element).closest("tr").fadeOut(1000, function() {
						$(this).remove();
					});
				},
				error : function(result) {
					bootbox.alert("Driver is assigned in order and cannt be deleted.");
				}
			});
		}
	});
}