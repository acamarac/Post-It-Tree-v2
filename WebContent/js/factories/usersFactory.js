angular.module('notesApp')
.factory('usersFactory',['$http', function($http){
	var url = 'https://localhost:8443/notesv2/rest/Users/';
    var usersInterface = {
    	getUser : function(){
    		url = url ;
            return $http.get(url)
              	.then(function(response){
        			 return response.data;
               	});
    	},
    	getUserByUsername : function(username){
    		urlUsername = url + username;
    		return $http.get(urlUsername)
    		.then(function(response){
    			return response.data;
    		})
    	},
    	updateUser : function(user){
    		url = url;
    		return $http.put(url, user)
    			.then(function(response){
    				return response.status;
    			});
    	},
    	deleteUser : function(idu){
    		urlIdu = url+ idu;
			return $http.delete(urlIdu)
			.then(function(response){
				return response.status;
			});
		},
		createUser : function(user) {
			return $http.post(url,user)
			.then(function(response){
				return response.status;
			});
		}
    }
    return usersInterface;
}])