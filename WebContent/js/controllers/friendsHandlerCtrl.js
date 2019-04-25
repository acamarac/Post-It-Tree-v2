angular.module('notesApp')
.controller('friendsHandlerCtrl', ['friendsFactory','usersFactory','$routeParams','$location',
			function(friendsFactory,usersFactory,$routeParams,$location){
    var friendsHandlerViewModel = this;
    friendsHandlerViewModel.friends=[];
    friendsHandlerViewModel.messages=[];
    friendsHandlerViewModel.username="";
    friendsHandlerViewModel.functions = {
    	where : function(route){
    		return $location.path() == route;
    	},
    	readFriendsSimple : function() {
    		friendsFactory.getFriendsSimple()
    		.then(function(response){
    			friendsHandlerViewModel.friends = response
    			console.log("Reading all the friends simple", response);
    		}, function(response){
    			console.log("Error reading friends");
    		})
    	},
    	readFriends : function() {
    		friendsFactory.getFriends()
    		.then(function(response){
    			friendsHandlerViewModel.friends = response
    			console.log("Reading all the friends complex", response);
    		}, function(response){
    			console.log("Error reading friends");
    		})
    	},
    	addFriend : function(username) {
    		usersFactory.getUserByUsername(username)
    		.then(function(response){
    			var friend = response
    			usersFactory.getUser()
    			.then(function(response){
    				var user = response
    				var idu1Friendship, idu2Friendship;
    				if(friend.idu < user.idu) {
    					idu1Friendship = friend.idu;
    					idu2Friendship = user.idu;
    				} else {
    					idu1Friendship = user.idu;
    					idu2Friendship = friend.idu;
    				}
    				var friendship = {
    						idu1 : idu1Friendship,
    						idu2 : idu2Friendship
    				};
    				friendsFactory.addFriends(friendship)
    				.then(function(response){
    					console.log("Friendship between ", username, "and user created");
    					history.back();
    				},function(response){
    					console.log("Error adding friendship");
    					friendsHandlerViewModel.messages.pop();
    					friendsHandlerViewModel.messages.push("That username does not exist or you are already friends");
    				})
    			},function(response) {
    				console.log("Error reading user");
    			})
    		},function(response) {
    			
    			console.log("Error reading friend");
    		})
    	},
    	deleteFriend: function(friendData) {
    		var index = friendsHandlerViewModel.friends.indexOf(friendData);
    		var username = friendData.friend.username;
    		friendsFactory.deleteFriend(username)
    		.then(function(response){
    			if(index !== -1)
    				friendsHandlerViewModel.friends.splice(index, 1);
    			console.log("Deleting friend with username:",username," Response:", response);
    		},function(response) {
    			console.log("Error deleting friend with username:",username," Response:", response);
    		})
    		
    		
    	}
    }
    if(friendsHandlerViewModel.functions.where('/friends'))  friendsHandlerViewModel.functions.readFriends();
    else friendsHandlerViewModel.functions.readFriendsSimple();
}])