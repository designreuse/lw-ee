/**
 * Submit form by POST with its original action address.
 * Reloads page on success.
 * If failed will show error message from recieved json 'msg'.
 * @param formId selector in jquery format ('#formId')
 */
function postFormByAjax(formId) {
	$.ajax({
		type : "POST",
		url : $(formId).attr('action'),
		data : $(formId).serialize(),
		success : function(response) {
			location.reload(true);
		},
		error : function(response) {
			bootbox.alert("Error. Check input data.");
		}
	});
}
