/**
 * Submit form by POST with its original action address.
 * Reloads page on success.
 * If failed will show error message from recieved json 'msg'.
 * @param formId selector in jquery format ('#formId')
 */
function postFormByAjax(formId) {
	$.ajax({
		type : "POST",
		url : window.location.pathname + "/" + $(formId).attr('action'),
		dataType : "text",
		data : $(formId).serialize(),
		success : function(data) {
			location.reload(true);
		},
		error : function(result) {
			bootbox.alert(result.responseText);
		}
	});
}
