angular.module('notesApp')
.factory('versionsFactory',['$http', function($http){
	var urlVersions = 'https://localhost:8443/notesv2/rest/versions/';
	var versionsInterface = {
		getNoteVersions: function(idn) {
			var urlversionsIdn = urlVersions+idn;
			return $http.get(urlversionsIdn)
			.then(function(response){
				return response.data;
			})
		},
		getNoteByTimestamp : function(idn,timestamp) {
			var urlGet = urlVersions+idn+'/'+timestamp;
			return $http.get(urlGet)
			.then(function(response){
				return response.data;
			})
		},
		restoreVersion : function(note){
			var urlidn = urlVersions+note.idn+'/'+note.timestamp;
			return $http.put(urlidn, note)
			.then(function(response){
				return response.status;
			});                   
		}
	}
	return versionsInterface;
}])