angular.module('notesApp')
.controller('usersNotesHandlerCtrl', ['usersNotesFactory','notesFactory','$routeParams','$location','$route',
			function(usersNotesFactory,notesFactory,$routeParams,$location,$route){
	var usersNotesHandlerViewModel = this;
    usersNotesHandlerViewModel.userNote={};
    usersNotesHandlerViewModel.friendsSelected=[];
    usersNotesHandlerViewModel.functions = {
   		where : function(route){
   			return $location.path() == route;
		},
		readUserNote : function(idn) {
			usersNotesFactory.getUsersNotes(idn)
				.then(function(response){
					console.log("Reading note with idn: ", idn," Response: ", response);
					usersNotesHandlerViewModel.userNote  = response;
				}, function(response){
					console.log("Error reading note");
				//	$location.path('/');
				})
		},
		unpin_pinNote : function(notes, note) {
			var index = notes.indexOf(note);
			(note.pinned == 1) ? note.pinned=0 : note.pinned=1; 
			if(index !== -1)
				notes[index] = note;
			var userNote = {
					idu : note.idu,
					idn : note.idn,
					owner :  note.owner,
					archived : note.archived,
					pinned :  note.pinned,
					trash :  note.trash
			}
			usersNotesFactory.putUsersNote(userNote)
			.then(function(response){
				console.log("Pin/unpin note with idn:",userNote.idn," Response:", response);
			}, function(response){
				console.log("Error pinning/unpinning note",userNote.idn);
			})
		},
		unarchive_archiveNote : function(notes, note) {
			var index = notes.indexOf(note);
			(note.archived == 1) ? note.archived=0 : note.archived=1; 
			if(index !== -1)
				notes.splice(index, 1);
			var userNote = {
					idu : note.idu,
					idn : note.idn,
					owner :  note.owner,
					archived : note.archived,
					pinned :  note.pinned,
					trash :  note.trash
			}
			usersNotesFactory.putUsersNote(userNote)
			.then(function(response){
				console.log("Archive/unarchive note with idn:",userNote.idn," Response:", response);
			}, function(response){
				console.log("Error Archive/unarchive note",userNote.idn);
			})
		},
		recoverNote : function(notes, note) {
			var index = notes.indexOf(note);
			note.trash=0;
			if(index !== -1)
				notes.splice(index, 1);
			var userNote = {
					idu : note.idu,
					idn : note.idn,
					owner :  note.owner,
					archived : note.archived,
					pinned :  note.pinned,
					trash :  note.trash
			}
			usersNotesFactory.putUsersNote(userNote)
			.then(function(response){
				console.log("Sending to trash/recovering note with idn:",userNote.idn," Response:", response);
			}, function(response){
				console.log("Error Sending to trash/recovering note",userNote.idn);
			})
		},
		deleteNoteTrash : function(notes, note) {
			var index = notes.indexOf(note);
			if(index !== -1)
				notes.splice(index, 1);
			notesFactory.deleteOneNoteTrash(note.idn)
				.then(function(response){
					console.log("Deleting note ", note.idn, " in trash, Response:", response);
				}, function(response){
					console.log("Error deleting note in trash");
				})
		},
		addFriendToArray : function(username) {
			var idx = usersNotesHandlerViewModel.friendsSelected.indexOf(username);

			if (idx > -1) {
		    	usersNotesHandlerViewModel.friendsSelected.splice(idx, 1);
		    }

		    else {
		    	usersNotesHandlerViewModel.friendsSelected.push(username);
		    }
		    console.log("Array:", usersNotesHandlerViewModel.friendsSelected);
		},
		shareNote : function(idn) {
			usersNotesFactory.postUsersNote(idn,usersNotesHandlerViewModel.friendsSelected)
			$location.path('/');
			/*.then(function(response){
				console.log("Sharing note ", idn, " with friends, Response:", response);
				$location.path('/');
			}, function(response){
				console.log("Error sharing note ",idn, " with friends");
			})*/
		},
		
    }
    console.log("Entering usersNotesHandlerCtrl with $routeParams.IDN=",$routeParams.IDN);
   	if ($routeParams.IDN!=undefined) usersNotesHandlerViewModel.functions.readUserNote($routeParams.IDN);
}]);