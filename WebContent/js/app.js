angular.module('notesApp', ['ngRoute'])
.config(function($routeProvider){
	$routeProvider
		.when("/versions/:IDN", {
			controller: "listCtrl",
			controllerAs: "listVM",
			templateUrl: "listNotesTemplate.html"
		})
		.when("/versions/:IDN/:TS", {
			controller: "noteHandlerCtrl",
			controllerAs: "noteHandlerVM",
			templateUrl: "noteHandlerTemplate.html"
		})
    	.when("/", {
    		controller: "listCtrl",
    		controllerAs: "listVM",
    		templateUrl: "listNotesTemplate.html",
    		resolve: {
    			// produce 500 miliseconds (0,5 seconds) of delay that should be enough to allow the server
    			//does any requested update before reading the orders.
    			// Extracted from script.js used as example on https://docs.angularjs.org/api/ngRoute/service/$route
    			delay: function($q, $timeout) {
    			var delay = $q.defer();
    			$timeout(delay.resolve, 500);
    			return delay.promise;
    			}
    		}
    	})
    	.when("/archive", {
    		controller: "listCtrl",
    		controllerAs: "listVM",
    		templateUrl: "listNotesTemplate.html"
    	})
    	.when("/trash", {
    		controller: "listCtrl",
    		controllerAs: "listVM",
    		templateUrl: "listNotesTemplate.html"
    	})
    	.when("/insertNote", {
    		controller: "noteHandlerCtrl",
    		controllerAs: "noteHandlerVM",
    		templateUrl: "noteHandlerTemplate.html"
        })
        .when("/editNote/:IDN", {
        	controller: "noteHandlerCtrl",
        	controllerAs: "noteHandlerVM",
        	templateUrl: "noteHandlerTemplate.html"
        })
        .when("/deleteNote/:IDN", {
        	controller: "noteHandlerCtrl",
        	controllerAs: "noteHandlerVM",
        	templateUrl: "noteHandlerTemplate.html"
        })
        .when("/shareNote/:IDN", {
        	controller: "usersNotesHandlerCtrl",
        	controllerAs: "usersNotesHandlerVM",
        	templateUrl: "shareTemplate.html"
        })
        .when("/profile", {
        	controller: "userHandlerCtrl",
        	controllerAs: "userHandlerVM",
        	templateUrl: "userHandlerTemplate.html"
        })
        .when("/profileImage", {
        	controller: "profileImageHandlerCtrl",
        	controllerAs: "profileImageVM",
        	templateUrl: "selectProfileImage.html"
        })
        .when("/deleteAccount", {
        	controller: "userHandlerCtrl",
        	controllerAs: "userHandlerVM",
        	templateUrl: "deleteAccount.html"
        })
        .when("/friends", {
        	controller: "friendsHandlerCtrl",
        	controllerAs: "friendsHandlerVM",
        	templateUrl: "friendsMainTemplate.html"
        })
        .when("/addFriend", {
        	controller: "friendsHandlerCtrl",
        	controllerAs: "friendsHandlerVM",
        	templateUrl: "addFriendHandlerTemplate.html"
        })
        .when("/advancedSearch", {
        	controller: "listCtrl",
    		controllerAs: "listVM",
    		templateUrl: "listNotesTemplate.html"
		});
})