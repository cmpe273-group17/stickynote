//author -- prakruthi
                                     
jQuery(document).ready(function() {
var userdata = { "userid": "admin"};


$("#new_note").click(function() {
	var methodURL = "http://localhost:8080/json/ajaxJsonRequest/12345/createnote";
	$.ajax ({
		// change the type to match controller 
		type : "POST",
		url : methodURL,
		data : JSON.stringify(userdata),
		success : function(data) {
			// Write code here to perform anything with data.
            alert(data)
		},
		errror: function(e) {
		}
	});

});
