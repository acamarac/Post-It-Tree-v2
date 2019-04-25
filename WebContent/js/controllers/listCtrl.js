angular.module('notesApp')
.controller('listCtrl', ['notesFactory','usersNotesFactory','versionsFactory','$routeParams','$location',
			function(notesFactory,usersNotesFactory,versionsFactory,$routeParams,$location){
    var listViewModel = this;
    listViewModel.notes=[];
    listViewModel.note={};
    listViewModel.text=undefined;
    listViewModel.option="";
    listViewModel.colors=[];
    listViewModel.friends=[];
    listViewModel.sharedBy=""; //0 -> sharedByFriend, 1 -> sharingWithFriend
    listViewModel.functions = {
    	where : function(route){
    		return $location.path() == route;
    	},
    	readNoteIdn : function(idn) {
    		notesFactory.getNote(idn)
			.then(function(response){
				console.log("Reading note with idn aaa: ", idn," Response: ", listViewModel.note);
				listViewModel.note  = response;
			}, function(response){
				console.log("Error reading note");
				//$location.path('/');
			})
    	},
    	readNotes : function() {
    		notesFactory.getNotes(0)
    		.then(function(response){
    			listViewModel.notes = response
    			console.log("Reading all the notes ", listViewModel.notes);
    		}, function(response){
    			console.log("Error reading notes");
    		})
    	},
    	readNotesArchive : function() {
    		notesFactory.getNotes(1)
				.then(function(response){
					listViewModel.notes = response
	    			console.log("Reading all the notes ", listViewModel.notes);
    			}, function(response){
    				console.log("Error reading archived notes");
    			})
		},	
		readNotesTrash : function() {
			notesFactory.getNotes(2)
				.then(function(response){
					listViewModel.notes = response
					console.log("Reading all the notes in trash", response);
    			}, function(response){
    				console.log("Error reading notes in trash");
    			})
		},
		readVersionsNote : function(idn) {
			versionsFactory.getNoteVersions(idn)
				.then(function(response){
					listViewModel.notes = response
					console.log("Reading all the versions of note", idn, response);
				},function(response) {
					console.log("Error reading version of note", idn);
				})
		},
		basicSearch : function() {
			notesFactory.getNotesBySearch(listViewModel.text,listViewModel.option)
			.then(function(response){
				listViewModel.notes = response
				console.log("Reading all the notes by search",listViewModel.text, listViewModel.option, response);
			}, function(response){
				console.log("Error reading notes by search");
			})
		},
		readAdvancedSearch : function() {
			notesFactory.advSearch(listViewModel.text,listViewModel.option,listViewModel.colors,listViewModel.friends,listViewModel.sharedBy)
			.then(function(response){
				listViewModel.notes = response
				console.log("Reading all the notes by advanced search:", listViewModel.notes);
			}, function(response){
				console.log("Error reading notes by advanced search");
			})
		},
		addColorToArray : function(color) {
			var idx = listViewModel.colors.indexOf(color);

			if (idx > -1) {
		    	listViewModel.colors.splice(idx, 1);
		    }

		    else {
		    	listViewModel.colors.push(color);
		    }
		    console.log("Array colors:", listViewModel.colors);
		},
		addFriendToArray : function(friend) {
			var idx = listViewModel.friends.indexOf(friend);
			
			if (idx > -1) {
		    	listViewModel.friends.splice(idx, 1);
		    }

		    else {
		    	listViewModel.friends.push(friend);
		    }
		    console.log("Array friends:", listViewModel.friends);
		},
		listViewSwitcher : function($routeParams){
			if (listViewModel.functions.where('/')){
				console.log($location.path());
				listViewModel.functions.readNotes();
			}
			else if (listViewModel.functions.where('/archive')){
				console.log($location.path());
				listViewModel.functions.readNotesArchive();
			}
			else if (listViewModel.functions.where('/trash')){
				console.log($location.path());
				listViewModel.functions.readNotesTrash();
			}
			else if (listViewModel.functions.where('/versions/'+$routeParams.IDN)){
				console.log($location.path());
				listViewModel.functions.readVersionsNote($routeParams.IDN);
			}
			else if(listViewModel.functions.where('/advancedSearch')) {
				console.log($location.path());
				//listViewModel.functions.readAdvancedSearch();
			}
			else {
				console.log($location.path());
			}
		}
    }
    //if ($routeParams.IDN!=undefined) listViewModel.functions.readNoteIdn($routeParams.IDN);
    console.log("Entering listCtrl with $routeParams=",$routeParams);
    listViewModel.functions.listViewSwitcher($routeParams);
    
}])