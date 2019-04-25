angular.module('notesApp')
.controller('noteHandlerCtrl', ['notesFactory','versionsFactory','$routeParams','$location','$route','$window',
			function(notesFactory,versionsFactory,$routeParams,$location,$route,$window){
	var noteHandlerViewModel = this;
    noteHandlerViewModel.note={};
    noteHandlerViewModel.messages=[];
   	noteHandlerViewModel.functions = {
   		where : function(route){
   			return $location.path() == route;
   		},
		readNote : function(idn) {
			notesFactory.getNote(idn)
				.then(function(response){
					console.log("Reading note with idn: ", idn," Response: ", response);
					noteHandlerViewModel.note  = response;
				}, function(response){
					console.log("Error reading note");
					$location.path('/');
				})
		},
		validateNote: function() {
			noteHandlerViewModel.messages.length=0;
			if (noteHandlerViewModel.note.title == undefined || noteHandlerViewModel.note.title.trim().length==0
					|| noteHandlerViewModel.note.title.length < 4) {
				noteHandlerViewModel.messages.push("The title must be higher than 3 characters.");
			} else if (noteHandlerViewModel.note.title.length > 50) {
				noteHandlerViewModel.messages.push("The title cannot be higher than 50 characters.");
			}

			
			if (noteHandlerViewModel.note.content == undefined || noteHandlerViewModel.note.content.trim().length==0
					|| noteHandlerViewModel.note.content.length < 11) {
				noteHandlerViewModel.messages.push("The content must be higher than 10 characters.");
			} else if (noteHandlerViewModel.note.content.length > 1000) {
				noteHandlerViewModel.messages.push("The content cannot be higher than 1000 characters.");
			}

			if (noteHandlerViewModel.messages.length==0)
				return true;
			else
				return false;
		},
		updateNote : function() {
			var validated = noteHandlerViewModel.functions.validateNote();
			if(validated) {
				notesFactory.putNote(noteHandlerViewModel.note)
					.then(function(response){
						console.log("Updating note with idn:",noteHandlerViewModel.note.idn," Response:", response);
					}, function(response){
						console.log("Error updating note");
					})
					window.history.back();
			}
		},	
		readNoteVersion : function(idn, timestamp) {
			versionsFactory.getNoteByTimestamp(idn, timestamp)
			.then(function(response){
				console.log("Reading version of note: ",idn, " with timestamp: ", timestamp);
				noteHandlerViewModel.note = response;
			}, function(response) {
				console.log("Error reading version of note: ",idn, " with timestamp: ", timestamp);
				$location.path('/');
			})
		},
		restoreVersionNote : function() {
			versionsFactory.restoreVersion(noteHandlerViewModel.note)
			.then(function(response){
				console.log("Restoring versions of note:",noteHandlerViewModel.note.idn," Response:", response);
			}, function(response) {
				console.log("Error recovering version");
			})
			$window.history.go(-2);
		},
		createNote : function() {
			var validated = noteHandlerViewModel.functions.validateNote();
			if(validated) {
	        notesFactory.postNote(noteHandlerViewModel.note)
				.then(function(response){
					console.log("Creating note. Response:", response);
    			}, function(response){
    				console.log("Error creating the note");
    			})
    			window.history.back();
			}
		},
		deleteNote : function(idn) {
			notesFactory.deleteNote(idn)
				.then(function(response){
					console.log("Deleting note with idn:",idn," Response:", response);
				}, function(response){
					console.log("Error deleting note");
				})
				window.history.back();
		},
		deleteAllNotesTrash : function(notes) {
			notes.splice(0, notes.length);
			notesFactory.deleteNoteTrash()
				.then(function(response){
					console.log("Deleting all notes in trash, Response:", response);
				}, function(response){
					console.log("Error deleting notes in trash");
				})
		},
		deleteNoteTrash : function(idn) {
			console.log("Llego aqui");
			notesFactory.deleteOneNoteTrash()
				.then(function(response){
					console.log("Deleting note ", idn, " in trash, Response:", response);
					$route.reload();
				}, function(response){
					console.log("Error deleting note in trash");
				})
				window.history.back();
		},
		noteHandlerSwitcher : function(){
			if (noteHandlerViewModel.functions.where('/insertNote')){
				console.log($location.path());
				noteHandlerViewModel.functions.createNote();
			}
			else if (noteHandlerViewModel.functions.where('/editNote/'+noteHandlerViewModel.note.idn)){
				console.log($location.path());
				noteHandlerViewModel.functions.updateNote();
			}
			else if (noteHandlerViewModel.functions.where('/deleteNote/'+noteHandlerViewModel.note.idn)){
				console.log($location.path());
				noteHandlerViewModel.functions.deleteNote(noteHandlerViewModel.note.idn);
			}
			else if (noteHandlerViewModel.functions.where('/versions/'+noteHandlerViewModel.note.idn+'/'+noteHandlerViewModel.note.timestamp)){
				console.log($location.path());
				noteHandlerViewModel.functions.restoreVersionNote();
			}
			else {
			console.log($location.path());
			}
		}
	}
   	console.log("Entering noteHandlerCtrl with $routeParams.IDN=",$routeParams.IDN);
   	if ($routeParams.IDN!=undefined && $routeParams.TS==undefined) noteHandlerViewModel.functions.readNote($routeParams.IDN);
   	else if($routeParams.IDN!=undefined && $routeParams.TS!=undefined) noteHandlerViewModel.functions.readNoteVersion($routeParams.IDN,$routeParams.TS);
}]);