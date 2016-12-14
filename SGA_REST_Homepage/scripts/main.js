// JavaScript Document

// create the module
var home = angular.module("sga_home",[]);
var myurl = "http://ec2-54-193-116-150.us-west-1.compute.amazonaws.com:";
var regUrl = "http://ec2-54-67-29-184.us-west-1.compute.amazonaws.com:";
//create the controller and register it with the module
home.controller("sga_controller", function ($scope, $http, $window, $interval) {

	// This is the initial page to load.
	$scope.showCreateNew = true;
	$scope.showPrevious = false;
	$scope.showOutput = false;
	
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
		$scope.errorMessage = response.data; // not displayed as of now
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
		$scope.createMessage = "Please wait as we process your request";
		$scope.requestId += 1; 
		$scope.showmap = false;

		$http({
				method : 'GET',
				url : myurl + "8080/SGA_REST_WeatherForecastClient/sga/weatherclient",
				params: {year: $scope.year, month: $scope.month, day: $scope.day, nexrad: $scope.location, filename: $scope.time, userid: $scope.emailId, sessionid: $scope.sessionId, requestid: $scope.requestId}
			})
			.then(function(response){
				// This is a success callback and will be called for status 200-299
				$scope.showCreateNew = false;
				$scope.showPrevious = false;
				$scope.showOutput = true;
				$scope.outputMessage = "Your job has been successfully submitted. Please cick refresh button to refresh the status of the job.";
				$scope.outputStatus = "SUBMITTING";
				$scope.keyid  = response.data;
				$scope.refreshJobStatus($scope.keyid);
			},
			function(response){	
				// this is a failure check
				$scope.errorMessage = response.data;
				$scope.createMessage = "Error processing request";
			});	
	};


	// Load the template when create new button is clicked.
	$scope.createNew = function(){
		$scope.showCreateNew = true;
		$scope.showPrevious = false;
		$scope.showOutput = false;
		// load the create new template using ng-include
		console.log("in create new");
		$scope.createMessage = "";
	};

	$scope.checkPrevious = function(){
		console.log("in check previous");
		$scope.showCreateNew = false;
		$scope.showPrevious = true;
		$scope.showOutput = false;
		$scope.tableMessage = "Please wait while we fetch your previous jobs.";
		$http({
				method : 'GET',
				url : regUrl + "8085/SGA_REST_Registry/sga/registry/getuserstats",
				params : {"email" : $scope.emailId}
		})
		.then(function(response){
			var userJobs = response.data.users; // Will be the jobs of the email ID provided in request.
			console.log(response.data);
			$scope.jobs = [];
			// loop on the userJobs and build an array of jobs
			for(var i=0; i < userJobs.length; i++) {
				var job = {};
				job["id"] = i+1;
				job["year"] = userJobs[i]["year"];
				job["month"] = userJobs[i]["month"];
				job["day"] = userJobs[i]["day"];
				job["location"] = userJobs[i]["location"];
				job["timestamp"] = userJobs[i]["timestamp"];
				job["keyid"] = userJobs[i]["keyid"];
				$scope.jobs.push(job);
			}
			$scope.jobs.reverse();

			if($scope.jobs){
				$scope.tableMessage = "Below are all the jobs you have ever submitted. Click on the resubmit to get a job submission form with the corresponding parameters. You can edit the parameters as desired.";
			} else {
				$scope.tableMessage = "You haven't submitted any jobs yet. Please create a new job.";
			}
		},
		// if the request was not successful
		function(response){
			$scope.tableMessage = "Something went wrong. We could not fetch the jobs. Please try again later.";
		});
		console.log($scope.jobs);
	};

	var getJobid = function(keyid){
		$http({
			method : 'GET',
			url : myurl + "8080/SGA_REST_WeatherForecastClient/sga/weatherclient/result",
			params: {"key": keyid}
		})
		.then(function(response){
			// This is a success callback and will be called for status 200-299
			$scope.jobid = response.data.jobid;
			$scope.mesos = response.data.mesos;
			$scope.result = response.data.result;

			// Render the map.
			if(result !== "no"){		
				$scope.showmap = true;
				$scope.output = result;
				$scope.mapMessage = "Storm has been forecasted and the impacted areas are shown in the below map";
			
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
				$scope.mapMessage = "No storm has been forecasted for the selected location";
			}
		},
		function(response){	
			// this is a failure check
			// $scope.errorMessage = response.data;
			$scope.outputMessage = "Error. Could not fetch the jobId.";
		});
	};

	$scope.refreshJobStatus = function(keyid) {
		getJobid(keyid);
		if (!$scope.mesos){
			$http({
				method : "GET",
				url : myurl + "8080/SGA_REST_WeatherForecastClient/sga/weatherclient/jobstatus",
				params: {"jobid":$scope.jobid}
			})
			.then(function(response){
				// This is a success callback and will be called for status 200-299
				console.log(response.data);
				$scope.outputStatus = response.data;
			},
			function(response){
				$scope.outputStatus = "?!?!?!?!";
			});
		} else {
			$scope.outputStatus = $scope.mesos;
		}
	};
	
	$scope.resubmit = function(year, month, day, location, timestamp){
		// Load the createNew template with all the fields filled in.
		console.log("in resubmit"+year);
		// $scope.showTemplate = "homeTemplates/createNew.html";
		$scope.createMessage = "";
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
		$scope.showOutput = false;
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