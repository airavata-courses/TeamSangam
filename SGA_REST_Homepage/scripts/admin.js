// JavaScript Document

var home = angular.module("sga_admin",[]);

home.controller("admin_controller", function ($scope, $http) {
	
	$http({
				method : 'GET',
				url : 'http://54.71.90.155:8080/SGA_REST_WeatherForecastClient/sga/weatherclient',
				params: {year: $scope.year, month: $scope.month, day: $scope.day, nexrad: $scope.location, filename: $scope.time}
			})
			.then(function(response){
				// This is a success callback and will be called for status 200-299
				
				
				},
				function(response){
				
				
				});
				
	});