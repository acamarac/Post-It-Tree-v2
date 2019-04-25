angular.module('notesApp')
.factory('usersNotesFactory',['$http', function($http){
	var url = 'https://localhost:8443/notesv2/rest/UsersNotes/';
	var usersNotesInterface = {
			getUsersNotes : function(idn){
				var urlidn = url + idn;
				return $http.get(urlidn)
				.then(function(response){
					return response.data;
				});
			},
			putUsersNote : function(userNote){
				var urlidn = url+userNote.idn;
				return $http.put(urlidn, userNote)
				.then(function(response){
					return response.status;
				});                   
			},
			postUsersNote : function(idn,selectedFriends){
				var i=0;
				var urlUser = 'https://localhost:8443/notesv2/rest/Users/';
				angular.forEach(selectedFriends, function(selectedFriends){
					$http.get(urlUser+selectedFriends).then(function(response){ 
						var friend = response.data;
						var userNotePost = {
								idu : friend.idu,
								idn : idn,
								owner : 0,
								archived : 0,
								pinned : 0,
								trash :  0
						};
						$http.post(url, userNotePost)
					});
				});
			}
	}
	return usersNotesInterface;
}])