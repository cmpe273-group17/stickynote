/*
 * 
 *  Author : Prakruthi Nagaraj
 *  
 *  Ajax functions for Register, Log in, Create, Delete, Save in JSON format   
 */

jQuery(document)
		.ready(
				function() {

					$("#register_user")
							.click(
									function() {

										var userdata = {
											"user" : "",
											"password" : "",
											"email" : "",
											"name" : "",
											"checkemail" : false
										};
										userdata.user = document
												.getElementById("new_username").value;
										userdata.name = document
												.getElementById("username").value;
										userdata.password = document
												.getElementById("new_password").value;
										userdata.email = document
												.getElementById("new_emailid").value;
										if (document
												.getElementById("checkemail").checked) {
											userdata.checkemail = true;
											
										} else {
											userdata.checkemail = false;
											
										}

										document.cookie = "user="
												+ JSON.stringify(userdata);

										if (userdata.user == ''
												|| userdata.password == ''
												|| userdata.email == '') {
											alert("Enter all fields");
											e.preventDefault();
										} else {

											var methodURL = "/SSO/api/stickynotes/register";
											$
													.ajax({
														type : "POST",
														contentType : 'application/json',
														dataType : "json",
														url : methodURL,
														data : JSON
																.stringify(userdata),
														success : function(data) {
															window.location = "https://www.dropbox.com/1/oauth2/authorize?state="
																	+ data.user
																	+ "&locale=en_US&client_id=jpukeu8ca9bxvfv&response_type=code&redirect_uri=http://localhost:8080/SSO/api/login";
														},
														error : function(e) {
															$("#errmsg")
																	.text(
																			"Oops. Registration failed. Please try again after sometime.");
														}
													});
										}
									});

					$("#login_user")
							.click(
									function() {

										var userdata = {
											"user" : "",
											"password" : ""
										};
										userdata.user = document
												.getElementById("emailid").value;
										userdata.password = document
												.getElementById("password").value;

										if (userdata.user == ''
												|| userdata.password == '') {
											alert("Enter all fields");
											e.preventDefault();
										} else {

											var methodURL = "/SSO/api/stickynotes/login";
											// alert(JSON.stringify(userdata));
											document.cookie = "user="
													+ JSON.stringify(userdata);
											// alert("logging in");

											$
													.ajax({
														// change the type to
														// match controller
														type : "POST",
														contentType : 'application/json',
														dataType : "json",
														url : methodURL,
														data : JSON
																.stringify(userdata),
														success : function(data) {
															// Write code here
															// to perform
															// anything with
															// data.
															// alert(JSON.stringify(data))
															// options =
															// data.documentList;
															location.href = "http://localhost:8080/SSO/index.html";

														},
														error : function(e) {
															// alert("Some new
															// err");
															// alert("Got error
															// "+e);
															// $("#errmsg").val()
															// = "Invalid User
															// Id or Password.
															// Please Login
															// again";
															$("#errmsg")
																	.text(
																			"Invalid User Id or Password. Please Login again.");
														}
													});
										}
									});

					$("#view_notes").click(
							function() {
								var user_id = "test_user";
								var methodURL = "/SSO/api/stickynotes/user/"
										+ user_id + "/viewnotes";
								//alert("Fetching notes");
								$.ajax({
									// change the type to match controller
									type : "GET",
									contentType : 'application/json',
									dataType : "json",
									url : methodURL,
									success : function(data) {
										// Write code here to perform anything
										// with data.
										//alert(data)
									},
									error : function(e) {
										alert("error in fetching notes");
									}
								});

							});

					$("#save_note").click(
							function() {
								//alert("Saving Note");
								// hard code docdata
								var docdata = {
									"name" : "",
									"message" : ""
								}
								docdata.name = "New Note";
								docdata.message = $("#textarea-note").val();
								//alert(docdata.message);
								// hardcoded user id
								// var user_id = "test_user";
								var methodURL = "/SSO/api/stickynotes/user/"
										+ cookieUser.user + "/savenote";
//								alert("Saving note for user: "
//										+ cookieUser.user);

								// alert(JSON.stringify(docdata));
								$.ajax({
									// change the type to match controller
									type : "PUT",
									contentType : 'application/json',
									dataType : "json",
									url : methodURL,
									data : JSON.stringify(docdata),
									success : function(data) {
										// Write code here to perform anything
										// with data.
										//alert(data)
									},
									errror : function(e) {
									}
								});

							});

					$("#delete_note").click(
							function() {
								alert("Warning: The note will be deleted permanently !");
								// hard code docdata
								var docdata = {
									"name" : "New Note"
								}
								// hardcoded user id
								// var user_id = "test_user";
								var methodURL = "/SSO/api/stickynotes/user/"
										+ cookieUser.user + "/deletenote";
								// alert(JSON.stringify(docdata));
								$.ajax({
									// change the type to match controller
									type : "DELETE",
									contentType : 'application/json',
									dataType : "json",
									url : methodURL,
									data : JSON.stringify(docdata),
									success : function(data) {
										//alert("Delete refresh");
										
										//$("#p-note-1").text("");
										
										// Write code here to perform anything
										// with data.
										//alert(data)
									},
									errror : function(e) {
									}
								});

							});

					$("#unlink").click(
							function() {
								alert("Please note: It will delete all your files.");

								var methodURL = "/SSO/api/stickynotes/user/"+ cookieUser.user + "/unlink";

								$.ajax({
									// change the type to match controller
									type : "DELETE",
									contentType : 'application/json',
									dataType : "json",
									url : methodURL,
									success : function(data) {
										window.location = "http://localhost:8080/SSO/index.html";
										// Write code here to perform anything
										// with data.
										//alert(data)
									},
									error : function(e) {
									}
								});

							});
					

					$("#logout").click(
							function() {
								alert("Please Note: You will be logged out of the application");
								window.location = "http://localhost:8080/SSO/homepage.html";
								
							});

				});
