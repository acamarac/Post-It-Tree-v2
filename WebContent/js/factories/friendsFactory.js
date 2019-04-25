angular.module('notesApp')
.factory('friendsFactory',['$http', function($http){
	var url = 'https://localhost:8443/notesv2/rest/friends/';
	var urlSharedByFriend = 'https://localhost:8443/notesv2/rest/friends/sharedByFriend/';
	var urlSharedByMe = 'https://localhost:8443/notesv2/rest/friends/sharedByMe/';
    var friendsInterface = {
    	getFriendsSimple : function(){
				return $http.get(url)
				.then(function(response){
					return response.data;
				});
			},
    	getFriends : function(){
    		url = url ;
            return $http.get(url)
              	.then(function(response){
        			 var friends = response.data;
        			 var friendsComplete = [];
        			 var i = 0;
        			 
        			 angular.forEach(friends, function(friends) {
        				 var urlSF = urlSharedByFriend+friends.username;
        				 var urlSM = urlSharedByMe+friends.username;
        				 var notesSByFriend = [];
        				 var notesSByMe = [];
        				 $http.get(urlSF).then(function(response){ 
        					 notesSByFriend = response.data;
        					 $http.get(urlSM).then(function(response){ 
            					 notesSByMe = response.data;
            					 var friendComplete = {
            							 friend : friends,
            							 notesSharedByFriend : notesSByFriend,
            							 notesSharedByMe : notesSByMe
            					 };
            					 friendsComplete[i] = friendComplete;
            					 i++;
        					 });
        				 });
        				 
        				
        			 });
        			 return friendsComplete;
               	});
    	},
    	addFriends : function(friendship){
    		url = url;
    		return $http.post(url,friendship)
    		.then(function(response){
    			return response.status;
    		})
    	},
    	deleteFriend : function(username) {
    		var urlusername = url+username;
			return $http.delete(urlusername)
			.then(function(response){
				return response.status;
			});
    	}
    }
    return friendsInterface;
}])