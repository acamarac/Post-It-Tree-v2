angular.module('notesApp')
.controller('profileImageHandlerCtrl', ['profileImageFactory','usersFactory','$routeParams','$location',
			function(profileImageFactory,usersFactory,$routeParams,$location){
	var PIHandlerViewModel = this;
	PIHandlerViewModel.images=[];
	PIHandlerViewModel.selected="";
    PIHandlerViewModel.functions = {
    	readProfileImages : function() {
    		profileImageFactory.getProfileImages()
    		.then(function(response){
    			PIHandlerViewModel.images=response;
    			console.log("Reading all profile images");
    		}, function(response) {
    			console.log("Error reading all profile images");
    		})
    	},
    	setProfileImage : function() {
    		usersFactory.updateProfileImage(PIHandlerViewModel.selected)
    		.then(function(response){
    			console.log("Profile image updated");
    		}, function(response) {
    			console.log("Error updating profile image");
    		})
    	}
    }
    PIHandlerViewModel.functions.readProfileImages();
}])