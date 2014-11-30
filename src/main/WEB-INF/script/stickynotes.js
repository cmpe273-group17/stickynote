/**
 * 
 *  Author : Prakruthi Nagaraj
 *  
 *  Ajax functions for Create, Delete, Save in JSON format   
 */

                                     
jQuery(document).ready(function() {
var userdata = { "userid": "admin"};
var docdata = { "name":"new-document",
				"message": "empty data"}

$("#new_note").click(function() {
	var methodURL = "http://localhost:8080/json/ajaxJsonRequest/12345/createnote";
	//alert(JSON.stringify(docdata));
	$.ajax ({
		// change the type to match controller 
		type : "POST",
		contentType: 'application/json',
		dataType : "json",
		url : methodURL,
		data : JSON.stringify(docdata),
		success : function(data) {
		 	// Write code here to perform anything with data.
            alert(data.id)
			alert(data.doc)
		},
		errror: function(e) {
		}
	});

});