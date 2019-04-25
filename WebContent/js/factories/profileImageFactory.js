angular.module('notesApp')
.factory('profileImageFactory',['$http', function($http){
	var url = 'https://localhost:8443/notesv2/rest/profileImage/';
	
	var profileImagesInterface = {
			getProfileImages : function() {
				return $http.get(url)
				.then(function(response){
					return response.data;
				})
			},
			getProfileImage : function(idi) {
				urlIdi = url + idi;
				return $http.get(urlIdi)
				.then(function(response){
					return response.data;
				})
			}
	}
	return profileImagesInterface;
}])