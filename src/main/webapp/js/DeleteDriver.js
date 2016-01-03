/**
 * Remove driver.
 * 
 * @param element - reference to element, that triggered this function (needed to
 *            remove row after succesful truck deletion).
 * @param id - of the driver.
 *
 */
function deleteDriver(element, id) {
	bootbox.confirm("Delete driver?", function(result) {
		if (result) {
			$.ajax({
				url : window.location.pathname + "/" + id + "/delete",
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