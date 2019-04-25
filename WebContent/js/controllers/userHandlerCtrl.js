angular.module('notesApp')
.controller('userHandlerCtrl', ['usersFactory','$location','$route',
	function(usersFactory,$location,$route){
    var userHandlerViewModel = this;
    userHandlerViewModel.user={};
    userHandlerViewModel.password2="";
    userHandlerViewModel.messages=[];
    userHandlerViewModel.functions = {
    	where : function(route){
       		return $location.path() == route;
       	},
		readUser : function() {
			usersFactory.getUser()
				.then(function(response){
					userHandlerViewModel.user = response
					console.log("Getting user with idu: ", userHandlerViewModel.user.idu," Response: ", response);
    			}, function(response){
    				console.log("Error reading user data");
    			})
		},
		validateUser : function() {
			userHandlerViewModel.messages.length=0;
			if (userHandlerViewModel.user.username == undefined || userHandlerViewModel.user.username.trim().length==0) {
				userHandlerViewModel.messages.push("Fill in the Username field.");
			} else if (userHandlerViewModel.user.username.length > 16) {
				userHandlerViewModel.messages.push("Username length cannot be higher than 16 characters.");
			} else if (userHandlerViewModel.user.username.length < 3) {
				userHandlerViewModel.messages.push("Username length must be higher than 3 characters.");
			} else if (userHandlerViewModel.user.username.includes(" ")) {
				userHandlerViewModel.messages.push("Username cannot have blank spaces.");
			} else if (!userHandlerViewModel.user.username.match("[a-zA-Z][a-zA-Z0-9_-]*")) {
				userHandlerViewModel.messages.push("Invalid Username (Pattern allowed:[a-zA-Z][a-zA-Z0-9_-]*).");
			}

			if (userHandlerViewModel.user.password == undefined || userHandlerViewModel.user.password.trim().lenght==0) {
				userHandlerViewModel.messages.push("Fill in the Password field.");
			} else if (userHandlerViewModel.user.password.length > 40) {
				userHandlerViewModel.messages.push("Password length cannot be higher than 40 characters.");
			} else if (userHandlerViewModel.user.password.length < 6) {
				userHandlerViewModel.messages.push("Password length must be higher than 6 characters.");
			} else if (userHandlerViewModel.user.password.includes(" ")) {
				userHandlerViewModel.messages.push("Password cannot have blank spaces.");
			} else if (!userHandlerViewModel.user.password.match("(?=.*[A-Z])(?=.*[0-9])[a-zA-Z0-9]*")) {
				userHandlerViewModel.messages.push("Invalid password. It must contain at least 1 upper case and 1 number.");
			} else if(userHandlerViewModel.user.password !== userHandlerViewModel.password2) {
				userHandlerViewModel.messages.push("Passwords do not match");
			}
			
			if (userHandlerViewModel.user.email == undefined || userHandlerViewModel.user.email.trim().length == 0) {
				userHandlerViewModel.messages.push("Fill in the email field.");
			} else if (userHandlerViewModel.user.email.length > 70) {
				userHandlerViewModel.messages.push("Email length cannot be higher than 70 characters.");
			} else if (userHandlerViewModel.user.email.includes(" ")) {
				userHandlerViewModel.messages.push("Email cannot have blank spaces.");
			} else if (!userHandlerViewModel.user.email.match("[a-zA-Z0-9_\\.\\+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-\\.]+")) {
				userHandlerViewModel.messages.push("Invalid Email.");
			}

			if (userHandlerViewModel.messages.length==0)
				return true;
			else
				return false;
		},
		updateUser : function() {
			var validated = undefined;
			if(userHandlerViewModel.user.password!=="******") {
				validated = userHandlerViewModel.functions.validateUser();
			}
			if(validated==undefined || validated==true) {
				usersFactory.updateUser(userHandlerViewModel.user)
				.then(function(response){
					console.log("Updating user with idu:",userHandlerViewModel.user.idu," Response:", response);
					userHandlerViewModel.messages.pop();
					userHandlerViewModel.messages.push("User updated correctly");
				}, function(response){
					console.log("Error updating user",userHandlerViewModel.user.idu);
					//userHandlerViewModel.messages.push("Error updating user");
				})
				$location.path('/profile');
			}
		},
		deleteAccount : function() {
			usersFactory.deleteUser(userHandlerViewModel.user.idu)
			.then(function(response){
				window.location.replace('https://localhost:8443/notesv2/LoginServlet');
				console.log("User deleted correctly");
			}, function(response){
				userHandlerViewModel.messages.push("Error deleting user");
				console.log("Error deleting user");
			})
		},
		userHandlerSwitcher : function() {
			if(userHandlerViewModel.functions.where('/profile')) {
				console.log($location.path());
				userHandlerViewModel.functions.updateUser();
			}
			else if(userHandlerViewModel.functions.where('/editProfile')) {
				console.log($location.path());
				userHandlerViewModel.functions.updateUser();
				$location.path('/profile');
			}
		}
		
    }
	userHandlerViewModel.functions.readUser();
}])