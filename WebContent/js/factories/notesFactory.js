angular.module('notesApp')
.factory('notesFactory',['$http', function($http){
	var url = 'https://localhost:8443/notesv2/rest/notes/';
	
	var urlUsersNotes = 'https://localhost:8443/notesv2/rest/UsersNotes/';
	var urlVersions = 'https://localhost:8443/notesv2/rest/versions/'
	var notesInterface = {
			compareNotes : function(a, b){
				var comparison = 0;

				if (a.idn > b.idn) {
					comparison = 1;
				} else if (b.idn > a.idn) {
					comparison = -1;
				}

				return comparison;
			},
			myIndexOf : function(array2, note) {    
			    for (var i = 0; i < array2.length; i++) {
			        if (array2[i].idn == note.idn) {
			            return i;
			        }
			    }
			    return -1;
			},
			getMatchingElements : function(array1, array2){
				var ret = [];
			    for(var i in array1) {   
			        if(notesInterface.myIndexOf(array2,array1[i]) > -1){
			            ret.push(array1[i]);
			        }
			    }
			    return ret;
			},
			getNotes: function(condition){
				return $http.get(urlUsersNotes)
				.then(function(response){
					var usersNotesAll= response.data;
					var usersNotes = [];
					switch(condition) {
				    case 0: //Then we want all the notes except those archived and in trash
				    	usersNotes = usersNotesAll.filter(userNote => userNote.trash==0 && userNote.archived==0);
				        break;
				    case 1: //Then we want archived notes
				    	usersNotes = usersNotesAll.filter(userNote => userNote.archived==1);
				        break;
				    case 2: //Then we want notes in trash
				    	usersNotes = usersNotesAll.filter(userNote => userNote.trash==1);
				    default:
				        
					}
					var notesComplete=[];
					var i=0;
					
					angular.forEach(usersNotes, function(usersNotes){
						$http.get(url+usersNotes.idn).then(function(response){ 
							var note = response.data;
							var noteComplete = {
									idn : note.idn,
									title : note.title,
									content : note.content,
									color : note.color,
									idu : usersNotes.idu,
									owner :  usersNotes.owner,
									archived : usersNotes.archived,
									pinned :  usersNotes.pinned,
									trash :  usersNotes.trash
							};
							notesComplete[i]=noteComplete;
							i++
						});
					});
					return notesComplete;
				});
			},
			getNotesBySearch : function (searchInput, searchOption) {
				urlSearch=url+'search/text?t='+searchInput+'&context='+searchOption;
				return $http.get(urlSearch)
				.then(function(response){
					var notes= response.data;
					var notesComplete=[];
					var i=0;
					
					angular.forEach(notes, function(notes){
						$http.get(urlUsersNotes+notes.idn).then(function(response){ 
							var userNote = response.data;
							var noteComplete = {
									idn : notes.idn,
									title : notes.title,
									content : notes.content,
									color : notes.color,
									idu : userNote.idu,
									owner :  userNote.owner,
									archived : userNote.archived,
									pinned :  userNote.pinned,
									trash :  userNote.trash
							};
							notesComplete[i]=noteComplete;
							i++
						});
					});
					return notesComplete;
				});
			},
			createUrlColor : function(url, color, last) {
				for (var i = 0; i < color.length; i+=1) {
					if(!last) url=url+'colors='+color[i]+'&';
					else if(i < color.length-1) url=url+'colors='+color[i]+'&';
					else url=url+'colors='+color[i];
				}
				return url;
			},
			createUrlFriends : function(urlFriends,amigos,sharedBy) {
				for (var i = 0; i < amigos.length; i+=1) {
					urlFriends=urlFriends+'friends='+amigos[i]+'&';
				}
				urlFriends=urlFriends+'sharedBy='+sharedBy;
				return urlFriends;
			},
			advSearch : function(texto,option,color,amigos,sharedBy) {
				var urlS = 'https://localhost:8443/notesv2/rest/notes/search?';
				if(texto!=undefined && color.length>0 && amigos.length>0) {
					urlS = urlS+'text='+texto+'&context='+option+'&';
					urlS = notesInterface.createUrlColor(urlS, color, false);
					urlS = notesInterface.createUrlFriends(urlS,amigos,sharedBy);
				}
				else if(texto!=undefined && color.length>0) {
					urlS = urlS+'text='+texto+'&context='+option+'&';
					urlS = notesInterface.createUrlColor(urlS, color, true);
				}
				else if(texto!=undefined && amigos.length>0) {
					urlS = urlS+'text='+texto+'&context='+option+'&';
					urlS = notesInterface.createUrlFriends(urlS,amigos,sharedBy);
				}
				else if(color.length>0 && amigos.length>0) {
					urlS = notesInterface.createUrlColor(urlS, color, false);
					urlS = notesInterface.createUrlFriends(urlS,amigos,sharedBy);
				}
				else if(color.length>0) {
					urlS = notesInterface.createUrlColor(urlS, color, true);
				}
				else if(amigos.length>0) {
					urlS = notesInterface.createUrlFriends(urlS,amigos,sharedBy);
				}
				else if(texto!=undefined) {
					urlS = urlS+'text='+texto+'&context='+option;
				}
				console.log(urlS);
				return $http.get(urlS)
				.then(function(response){
					var notes= response.data;
					var notesComplete=[];
					var i=0;
					
					angular.forEach(notes, function(notes){
						$http.get(urlUsersNotes+notes.idn).then(function(response){ 
							var userNote = response.data;
							var noteComplete = {
									idn : notes.idn,
									title : notes.title,
									content : notes.content,
									color : notes.color,
									idu : userNote.idu,
									owner :  userNote.owner,
									archived : userNote.archived,
									pinned :  userNote.pinned,
									trash :  userNote.trash
							};
							notesComplete[i]=noteComplete;
							i++
						});
					});
					return notesComplete;
				});
			},
			getVersions : function(idn) {
				var urlversionsIdn = urlVersions+idn;
				return $http.get(urlversionsIdn)
				.then(function(response){
					return response.data;
				})
			},
			getNote : function(idn){
				var urlidn = url + idn;
				return $http.get(urlidn)
				.then(function(response){
					return response.data;
				});
			},
			putNote : function(note){
				var urlidn = url+note.idn;
				return $http.put(urlidn, note)
				.then(function(response){
					return response.status;
				});                   
			},
			postNote:  function(note){
				return $http.post(url,note)
				.then(function(response){
					return response.status;
				});
			}, 
			deleteNote : function(idn){
				var urlidn = url+idn;
				return $http.delete(urlidn)
				.then(function(response){
					return response.status;
				});
			},	
			deleteNoteTrash : function(){
				var urlTrash = url+ 'trash/';
				return $http.delete(urlTrash)
				.then(function(response){
					return response.status;
				});
			},
			deleteOneNoteTrash : function(idn){
				var urlTrashIdn = url+ 'trash/' + idn;
				return $http.delete(urlTrashIdn)
				.then(function(response){
					return response.status;
				});
			}
	}
	return notesInterface;
}])