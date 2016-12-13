// JavaScript Document

// create the module
var home = angular.module("sga_home",[]);
var myurl = "http://ec2-54-183-132-116.us-west-1.compute.amazonaws.com:";
var regUrl = "http://ec2-54-193-9-114.us-west-1.compute.amazonaws.com:";
//create the controller and register it with the module
home.controller("sga_controller", function ($scope, $http, $window) {

	// This is the initial page to load.
	// $scope.showTemplate = "homeTemplates/createNew.html";
	$scope.showCreateNew = true;
	$scope.showPrevious = false;
	
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
	function fetchYears(){
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
	fetchYears();
	// console.log($scope.year);
	$scope.fetchMonths = function(){
		console.log($scope.year);
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
					var jobid = response.data.jobid;
					var result = response.data.result;

					// Check for the status of the Mesos job by reaching the endpoint.
					$http({
						method : "GET",
						url : myurl + "8080/SGA_REST_WeatherForecastClient/sga/weatherclient/jobstatus",
						params: {"jobid":jobid}
					})
					.then(function(response){
					// This is a success callback and will be called for status 200-299
						$scope.message = "Stage of your Mesos job: "+response.data;
					},
					function(response){
						$scope.message = "Mesos job status could not be retrieved.";
					});

					// Render the map.
					if(result !== "no"){		
						$scope.showmap = true;
						$scope.output = result;
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
						// If we get a finished state, then we can send a request for the gif image.
						// THE BELOW CODE HAS TO BE CHANGED.
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

	// Load the template when create new button is clicked.
	$scope.createNew = function(){
		$scope.showCreateNew = true;
		$scope.showPrevious = false;
		// load the create new template using ng-include
		console.log("in create new");
		// $scope.showTemplate = "homeTemplates/createNew.html";
		$scope.message = "";
	};

	$scope.checkPrevious = function(){
		console.log("in check previous");
		$scope.showCreateNew = false;
		$scope.showPrevious = true;
		// $scope.showTemplate = "homeTemplates/previousJobs.html";
		$scope.message = "Please wait while we fetch your previous jobs.";
		// First get all the jobs the user has ever submitted.
		// Create them all into a table format with details button.
		// An http request to get an array of all sessions of a user.
		// From this, all the jobs need to be extracted.
		// From each job, get the "dataingestor" field. It has a NEXRAD url for the file.
		// The path to the file can be used to extract the year and stuff.
		// From this info, an array has to be built like $scope.jobs.
		// var nexradUrl = "Response returned is http://noaa-nexrad-level2.s3.amazonaws.com/1992/03/13/KMLB/KMLB19920313_005623.gz";
		// console.log(nexradUrl.split('/');
		$http({
				method : 'GET',
				url : regUrl + "8085/SGA_REST_Registry/sga/registry/getuserstats",
				params : {"email" : $scope.emailId}
		})
		.then(function(response){
			var userJobs = response.data.users;
			console.log(response.data);
			$scope.jobs = [];
			// loop on the users and build an
			for(var i=0; i < userJobs.length; i++) {
				var nexradUrl = userJobs[i].dataingestor;
				var urlParts = nexradUrl.split('/');
				var job = {};
				job ["id"] = i+1;
				job ["year"] = urlParts[3];
				job ["month"] = urlParts[4];
				job ["day"] = urlParts[5];
				job ["location"] = urlParts[6];
				job ["timestamp"] = urlParts[7].split(".")[0];
				$scope.jobs.push(job);
			}
		},
		// if the request was not successful
		function(response){
			$scope.message = "Something went wrong. We could not fetch the jobs. Please try again later.";
		});
		console.log($scope.jobs);
		// For now, hardcoding the values
		// $scope.jobs = [
		// 	{"id":"1", "year":"2003", "month":"11", "day":"03", "location":"KMPH", "timestamp":"KMPH20031103jaskfs"},
		// 	{"id":"2", "year":"1990", "month":"01", "day":"09", "location":"CFDH", "timestamp":"KMPH20031103jaskfs"},
		// 	{"id":"3", "year":"2004", "month":"10", "day":"23", "location":"KGGG", "timestamp":"KMPH20031103jaskfs"},
		// 	{"id":"4", "year":"2006", "month":"03", "day":"23", "location":"KSHF", "timestamp":"KMPH20031103jaskfs"},
		// 	{"id":"5", "year":"1993", "month":"12", "day":"14", "location":"OEFJ", "timestamp":"KMPH20031103jaskfs"},
		// 	{"id":"6", "year":"1999", "month":"08", "day":"10", "location":"AAFL", "timestamp":"KMPH20031103jaskfs"}
		// ];
		if($scope.jobs){
			$scope.message = "Below are all the jobs you have ever submitted. Click on the resubmit to get a job submission form with the corresponding parameters. You can edit the parameters as desired.";
		} else {
			$scope.message = "You haven't submitted any jobs yet. Please create a new job.";
		}
	};

	$scope.resubmit = function(year, month, day, location, timestamp){
		// Load the createNew template with all the fields filled in.
		console.log("in resubmit"+year);
		// $scope.showTemplate = "homeTemplates/createNew.html";
		$scope.message = "";
		// Need to fill the values for dropdown here
		fetchYears();
		$scope.year = year;
		// Need to call fetchMonths using the year above.
		$scope.fetchMonths();
		$scope.month = month;
		// Need to call fetchDays using the month above.
		$scope.fetchDays();
		$scope.day = day;
		$scope.fetchLocations();
		$scope.location = location;
		$scope.fetchFiles();
		$scope.time = timestamp;
		$scope.showCreateNew = true;
		$scope.showPrevious = false;
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