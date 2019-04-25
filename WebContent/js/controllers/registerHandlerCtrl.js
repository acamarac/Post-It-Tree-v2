angular.module('notesApp')
.controller('registerHandlerCtrl', ['usersFactory','$location','$route',
	function(usersFactory,$location,$route){
	var registerHandlerViewModel = this;
	registerHandlerViewModel.user={};
	registerHandlerViewModel.messages=[];
	registerHandlerViewModel.functions = {
			validateUser : function() {
				registerHandlerViewModel.messages.length=0;
				if (registerHandlerViewModel.user.username == undefined || registerHandlerViewModel.user.username.trim().length==0) {
					registerHandlerViewModel.messages.push("Fill in the Username field.");
				} else if (registerHandlerViewModel.user.username.length > 16) {
					registerHandlerViewModel.messages.push("Username length cannot be higher than 16 characters.");
				} else if (registerHandlerViewModel.user.username.length < 3) {
					registerHandlerViewModel.messages.push("Username length must be higher than 3 characters.");
    			} else if (registerHandlerViewModel.user.username.includes(" ")) {
    				registerHandlerViewModel.messages.push("Username cannot have blank spaces.");
    			} else if (!registerHandlerViewModel.user.username.match("[a-zA-Z][a-zA-Z0-9_-]*")) {
    				registerHandlerViewModel.messages.push("Invalid Username (Pattern allowed:[a-zA-Z][a-zA-Z0-9_-]*).");
    			}

    			if (registerHandlerViewModel.user.password == undefined || registerHandlerViewModel.user.password.trim().lenght==0) {
    				registerHandlerViewModel.messages.push("Fill in the Password field.");
    			} else if (registerHandlerViewModel.user.password.length > 40) {
    				registerHandlerViewModel.messages.push("Password length cannot be higher than 40 characters.");
    			} else if (registerHandlerViewModel.user.password.length < 6) {
    				registerHandlerViewModel.messages.push("Password length must be higher than 6 characters.");
    			} else if (registerHandlerViewModel.user.password.includes(" ")) {
    				registerHandlerViewModel.messages.push("Password cannot have blank spaces.");
    			} else if (!registerHandlerViewModel.user.password.match("(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]*")) {
    				registerHandlerViewModel.messages.push("Invalid password. It must contain at least 1 upper case and 1 number.");
    			}

    			if (registerHandlerViewModel.user.email == undefined || registerHandlerViewModel.user.email.trim().length == 0) {
    				registerHandlerViewModel.messages.push("Fill in the email field.");
    			} else if (registerHandlerViewModel.user.email.length > 70) {
    				registerHandlerViewModel.messages.push("Email length cannot be higher than 70 characters.");
    			} else if (registerHandlerViewModel.user.email.includes(" ")) {
    				registerHandlerViewModel.messages.push("Email cannot have blank spaces.");
    			} else if (!registerHandlerViewModel.user.email.match("[a-zA-Z0-9_\\.\\+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-\\.]+")) {
    				registerHandlerViewModel.messages.push("Invalid Email.");
    			}

    			if (registerHandlerViewModel.messages.length==0)
    				return true;
    			else
    				return false;
    		},
    		registerUser : function() {
    			if(registerHandlerViewModel.functions.validateUser()) {
    				usersFactory.createUser(registerHandlerViewModel.user)
    				.then(function(response){
    					console.log("User created correctly");
    					window.location.replace('https://localhost:8443/notesv2/pages/index.html#!/');
    				}, function(response){
    					console.log("Error creating user");
    					registerHandlerViewModel.messages.push("Error creating user. Username already in use")
    				})
    			}
    		}
    }
}])