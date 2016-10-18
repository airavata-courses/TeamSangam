// JavaScript Document

// create the module
var home = angular.module("sga_home",[]);

//create the controller and register it with the module
home.controller("sga_controller", function ($scope, $http) {
	$http({
			method : "GET",
			url : "http://54.71.95.40:5001/getyears"
	})
	.then(function(response){
		// This is a success callback and will be called for status 200-299
		$scope.years = response.data;
		},
		//based on the response, we will either show an error message or redirect the user to the homepage
		function(response){
		$scope.data = response;
		});
		
	$scope.fetchMonths = function(){
		if($scope.year !== "None"){
			$http({
				method : "POST",
				url : "http://54.71.95.40:5001/getmonths",
				data: {"year":$scope.year}
			})
			.then(function(response){
				// This is a success callback and will be called for status 200-299
				$scope.months = response.data;
				},
				function(response){
				$scope.data = response;
				});	
		} else{
			$scope.months = [];
			$scope.days = [];
			$scope.locations = [];
		}
	};
	
	$scope.fetchDays = function(){
		if($scope.month !== "None"){
			$http({
				method : "POST",
				url : "http://54.71.95.40:5001/getdays",
				data: {"year":$scope.year, "month":$scope.month}
			})
			.then(function(response){
				// This is a success callback and will be called for status 200-299
				$scope.days = response.data;
				},
				function(response){
				$scope.data = response;
				});	
		}
	};
	
	
	$scope.fetchLocations = function(){
		if($scope.day !== "None"){
			$http({
				method : "POST",
				url : "http://54.71.95.40:5001/getlocations",
				data: {"year":$scope.year, "month":$scope.month, "day":$scope.day}
			})
			.then(function(response){
				// This is a success callback and will be called for status 200-299
				$scope.locations = response.data;
				},
				function(response){
				$scope.data = response;
				});	
		}
	};
	
	$scope.fetchFiles = function(){
		if($scope.location !== "None"){
			$http({
				method : "POST",
				url : "http://54.71.95.40:5001/getfiles",
				data: {"year":$scope.year, "month":$scope.month, "day":$scope.day, "location":$scope.location}
			})
			.then(function(response){
				// This is a success callback and will be called for status 200-299
				$scope.files = response.data;
				
				},
				function(response){
				$scope.data = response;
				});	
		}
	};
	
	$scope.submit = function(){
		$scope.message = "Please wait as we process your request";
		$http({
				method : 'GET',
				url : 'http://54.71.95.40:8080/SGA_REST_WeatherForecastClient/sga/weatherclient',
				params: {year: $scope.year, month: $scope.month, day: $scope.day, nexrad: $scope.location, filename: $scope.time}
			})
			.then(function(response){
				// This is a success callback and will be called for status 200-299
				$scope.output = response.data;
				$scope.message = "Storm has been forcasted and the impacted areas are shown in the below map";
				
				var btown = {lat: 39.167107,lng: -86.534359};
				$scope.map = new google.maps.Map(document.getElementById('map'), {
					zoom: 4,
					center: btown,
					mapTypeId: 'terrain'
				});
				
				var places = $scope.output.kml.Document.Placemark;
				for(var i=0; i<places.length; i++){
					var latlong = places[i].Point.coordinates;
					var l = latlong.split(",");
					$scope.mark = new google.maps.LatLng(l[0],l[1]);
					
					var marker = new google.maps.Marker({
					position: $scope.mark,
					map: $scope.map
			  		});
				}
				
			},
			function(response){	
				$scope.message = response.data;
			});	
	   };	 
});
