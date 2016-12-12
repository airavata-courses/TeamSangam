// JavaScript Document

// create the module
var home = angular.module("sga_home",[]);
var myurl = "http://ec2-54-183-132-116.us-west-1.compute.amazonaws.com:";
//create the controller and register it with the module
home.controller("sga_controller", function ($scope, $http, $window) {

	// This is the initial page to load.
	$scope.showTemplate = "homeTemplates/createNew.html";
	
	$http({
			method : "GET",
			url : myurl+"5001/isActive"
	})
	.then(function(response){
		// This is a success callback and will be called for status 200-299
		if(response.data === "-1"){
			alert("You are being redirected to login page");
			$window.location.href = "login.html";
		}
		else{
			$scope.sessionId = response.data[0];
			$scope.emailId = response.data[1];
		}
		},
		//based on the response, we will either show an error message or redirect the user to the login page
		function(response){
			$scope.errorMessage = response.data;
		});
		
		$scope.requestId = 0;
		
	// Function for getting the values for years dropdown
	function getYears(){
		$http({
				method : "GET",
				url : myurl + "5001/getyears"
		})
		.then(function(response){
			// This is a success callback and will be called for status 200-299
			$scope.years = response.data;
		},
			//based on the response, we will either show an error message or fill the dropdown
			function(response){
			$scope.data = response;
		});
	}

	// Getting the values for years dropdown
	getYears();

	// Load the template when create new button is clicked.
	$scope.createNew = function(){
		// load the create new template using ng-include
		console.log("in create new");
		$scope.showTemplate = "homeTemplates/createNew.html";
		$scope.message = "";
	};

	$scope.checkPrevious = function(){
		console.log("in check previous");
		$scope.showTemplate = "homeTemplates/previousJobs.html";
		$scope.message = "Please wait while we fetch your previous jobs.";
		// First get all the jobs the user has ever submitted.
		// Create them all into a table format with details button.
		// An http request to get an array of all sessions of a user.
		// From this, all the jobs need to be extracted.
		// From each job, get the "dataingestor" field. It has a NEXRAD url for the file.
		// The path to the file can be used to extract the year and stuff.
		// From this info, an array has to be built like $scope.jobs.
		// $http({
		// 		method : 'GET',
		// 		url : myurl + "8085/SGA_REST_Registry/sga/registry/getuserstats",
		// 		data : {"email" : email}
		// })
		// .then(function(response){
		// 	var data = response.data;
		// },
		// // if the request was not successful
		// function(response){
		// 	$scope.message = "Something went wrong. We could not fetch the jobs. Please try again later.";
		// });
		// For now, hardcoding the values
		$scope.jobs = [
			{"id":"1", "year":"2003", "month":"11", "day":"03", "location":"KMPH", "timestamp":"KMPH20031103jaskfs"},
			{"id":"2", "year":"1990", "month":"01", "day":"09", "location":"CFDH", "timestamp":"KMPH20031103jaskfs"},
			{"id":"3", "year":"2004", "month":"10", "day":"23", "location":"KGGG", "timestamp":"KMPH20031103jaskfs"},
			{"id":"4", "year":"2006", "month":"03", "day":"23", "location":"KSHF", "timestamp":"KMPH20031103jaskfs"},
			{"id":"5", "year":"1993", "month":"12", "day":"14", "location":"OEFJ", "timestamp":"KMPH20031103jaskfs"},
			{"id":"6", "year":"1999", "month":"08", "day":"10", "location":"AAFL", "timestamp":"KMPH20031103jaskfs"}
		];
		if($scope.jobs){
			$scope.message = "Below are all the jobs you have ever submitted. Click on the resubmit to get a job submission form with the corresponding parameters. You can edit the parameters as desired.";
		} else {
			$scope.message = "You haven't submitted any jobs yet. Please create a new job.";
		}
	};

	$scope.resubmit = function(year, month, day, location, timestamp){
		// Load the createNew template with all the fields filled in.
		console.log("in resubmit"+year);
		$scope.showTemplate = "homeTemplates/createNew.html";
		$scope.message = "";
		// Need to fill the values for dropdown here
		getYears();
		//$scope.years = [year];
		$scope.year = year;
		$scope.months = [month];
		$scope.month = month;
		$scope.days = [day];
		$scope.day = day;
		$scope.locations = [location];
		$scope.location = location;
		$scope.files = [timestamp];
		$scope.time = timestamp;
	};

	$scope.fetchMonths = function(){
		if($scope.year !== "None"){
			$http({
				method : "POST",
				url : myurl + "5001/getmonths",
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
				url : myurl + "5001/getdays",
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
				url : myurl + "5001/getlocations",
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
				url : myurl + "5001/getfiles",
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
		$scope.requestId += 1; 
		$scope.showmap = false;
		$http({
				method : 'GET',
				url : myurl + "8080/SGA_REST_WeatherForecastClient/sga/weatherclient",
				params: {year: $scope.year, month: $scope.month, day: $scope.day, nexrad: $scope.location, filename: $scope.time, userid: $scope.emailId, sessionid: $scope.sessionId, requestid: $scope.requestId}
			})
			.then(function(response){
				// This is a success callback and will be called for status 200-299
				var keyID  = response.data;
				$http({
					method : 'GET',
					url : myurl + "8080/SGA_REST_WeatherForecastClient/sga/weatherclient/result",
					params: {"key": keyID}
				})
				.then(function(response){
					// This is a success callback and will be called for status 200-299
					if(response.data !== "no"){		
						$scope.showmap = true;
						$scope.output = response.data;
						$scope.message = "Storm has been forecasted and the impacted areas are shown in the below map";
					
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
						// Need to check if there is an image in the received response. If there is one, then render it.
						// Else, the showImage will be undefined and the image will not be shown.
						if(response.data.image){
							$scope.precip = response.data.image;
							$scope.showImage = true;
						}
					}
					else{
						$scope.message = "No storm has been forecasted for the selected location";
					}
					
				},
				function(response){	
					// this is a failure check
					$scope.errorMessage = response.data;
					$scope.message = "error in processing request";
				});	
				
					
			},
			function(response){	
				// this is a failure check
				$scope.errorMessage = response.data;
				$scope.message = "error in processing request";
			});	
	};
	$scope.logout = function(){
		$scope.message = "Logging you out...";
		$http({
			method : "POST",
			url : myurl + "5001/logout"
		})
		.then(function(response){
			// This is a success callback and will be called for status 200-299
				$scope.message = "Logged out.";
				$window.location.href = "login.html";
			},
			function(response){
				$scope.message = "Error while logging out.";
			});	
	};
})
.config(function ($httpProvider) {
						$httpProvider.defaults.withCredentials = true;
});