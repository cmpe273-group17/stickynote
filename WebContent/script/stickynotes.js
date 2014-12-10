/*
 * 
 *  Author : Prakruthi Nagaraj
 *  
 *  Ajax functions for Register, Log in, Create, Delete, Save in JSON format   
*/

jQuery(document).ready(function() {


	$("#register_user").click(function() {
		var userdata = { "user": "",
				 "password": "",
				 "email":""};
		userdata.user = document.getElementById("new_username").value;
		userdata.password = document.getElementById("new_password").value;
		userdata.email = document.getElementById("new_emailid").value;

		var methodURL = "/SSO/api/stickynotes/register";
		//alert(JSON.stringify(docdata));
		$.ajax ({
			// change the type to match controller 
			type : "POST",
			contentType: 'application/json',
			dataType : "json",
			url : methodURL,
			data : JSON.stringify(userdata),	
			success : function(data) {
			 	// Write code here to perform anything with data.
	            alert(data)
			},
			errror: function(e) {
				alert(e);
			}
		});

	});

	
$("#login_user").click(function() {
	var userdata = { "user": "",
			 "password": ""};
	userdata.user = document.getElementById("emailid").value;
	userdata.password = document.getElementById("password").value;
	var methodURL = "/SSO/api/stickynotes/login";
	//alert(JSON.stringify(userdata));
	$.ajax ({
		// change the type to match controller 
		type : "POST",
		contentType: 'application/json',
 		dataType : "json",
		url : methodURL,
		data : JSON.stringify(userdata),	
		success : function(data) {
		 	// Write code here to perform anything with data.
            alert(JSON.stringify(data))
		},
		errror: function(e) {
			alert(e);
		}
	});

});


$("#view_notes").click(function() {
	var user_id = "test_user";
	var methodURL = "/SSO/api/stickynotes/user/"+ user_id +"/viewnotes";
	//alert(JSON.stringify(docdata));
	$.ajax ({
		// change the type to match controller 
		type : "GET",
		contentType: 'application/json',
		dataType : "json",
		url : methodURL,
		success : function(data) {
		 	// Write code here to perform anything with data.
            alert(data)
		},
		errror: function(e) {
		}
	});

});


$("#save_note").click(function() {
	// hard code docdata
	var docdata = { "name":"",
				"message" : ""}
	docdata.name = "getfrom";
	docdata.message = "messge from text box";
	// hardcoded user id
	var user_id = "test_user";
	var methodURL = "/SSO/api/stickynotes/user/"+ user_id +"/savenote";
	

	//alert(JSON.stringify(docdata));
	$.ajax ({
		// change the type to match controller 
		type : "PUT",
		contentType: 'application/json',
		dataType : "json",
		url : methodURL,
		data : JSON.stringify(docdata),
		success : function(data) {
		 	// Write code here to perform anything with data.
            alert(data)
		},
		errror: function(e) {
		}
	});

});

$("#delete_note").click(function() {
	// hard code docdata
	var docdata = { "name":"new_doc"}
	// hardcoded user id
	var user_id = "test_user";
	var methodURL = "/SSO/api/stickynotes/user/"+ user_id +"/deletenote";
	//alert(JSON.stringify(docdata));
	$.ajax ({
		// change the type to match controller 
		type : "DELETE",
		contentType: 'application/json',
		dataType : "json",
		url : methodURL,
		data : JSON.stringify(docdata),
		success : function(data) {
		 	// Write code here to perform anything with data.
            alert(data)
		}, 
		errror: function(e) {
		}
	});

  });

});

