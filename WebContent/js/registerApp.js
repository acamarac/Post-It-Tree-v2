angular.module('RegisterApp', ['ngRoute'])
.config(function($routeProvider){
	$routeProvider
		.when("/register245", {
			controller: "userHandlerCtrl",
			controllerAs: "userHandlerVM",
			templateUrl: "register.html"
		});
})