$(document).ready(function() {
$(function() {
$("#dialog").dialog({
	autoOpen: false
});
$("#button").on("click", function() {
	$("#dialog").dialog("open");
	});
});
// Validating Form Fields.....
$("#submit").click(function(e) {
	var email = $("#signup_email").val();
	var password = $("#signup_password").val();
	var emailReg = /^([\w-\.]+@([\w-]+\.)+[\w-]{2,4})?$/;
	if (signup_email === '' || signup_password === '') {
		alert("Please fill all fields...!!!!!!");
		e.preventDefault();
	} else if (!(email).match(emailReg)) {
		alert("Invalid Email...!!!!!!");
		e.preventDefault();
	} else {
		/* get some values from elements on the page: */
		e.preventDefault();
    	var $form = $('#loginForm'),
    	url = "http://localhost:8080/SSO/stickynotes/";

    	/* Send the data using post */
    	var posting = $.post( url, { name: $('#email').val(), name2: $('#password').val() });

    	/* Alerts the results */
    	posting.done(function( data ) {
      		alert("Form Submitted Successfully......");
      		//Need to see if the list of notes is passed to the index.html
      		window.location.href = data.redirect;
    	});
	}
});
});
