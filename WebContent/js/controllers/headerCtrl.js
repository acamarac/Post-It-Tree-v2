angular.module('notesApp')
.controller('headerCtrl', ['usersFactory',function(usersFactory,sharedUser){
    var headerViewModel = this;
    headerViewModel.user={};
    headerViewModel.functions = {
		readUser : function() {
			usersFactory.getUser()
				.then(function(response){
					headerViewModel.user = response
					console.log("Getting user with idu: ", headerViewModel.user.idu," Response: ", response);
    			}, function(response){
    				console.log("Error reading user data");
    			})
		}
    }
	headerViewModel.functions.readUser();
}])