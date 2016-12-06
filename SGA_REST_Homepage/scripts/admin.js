var myurl = "http://ec2-54-193-9-114.us-west-1.compute.amazonaws.com:";

var admin = angular .module("sga_admin",[])
					.controller("sga_admin_controller", function($scope, $http) {
						// There is no message to display at the beginning.
						$scope.message = "";
						// We show the table using the ng-show directive only when the button is clicked.
						$scope.showTable = false;
						$scope.pullAllUserStats = function() {
							// An http request to fetch the json object with all user details. For now, hard coding the json data as below.
							$http({
									method : 'GET',
									url : myurl + "8085/SGA_REST_Registry/sga/registry/getstats"
							})
							// var data = {
							// 		"users":[
							// 					{"id": "user1@yahoo.co.in", "count": 66},
							// 					{"id": "user2@gmail.com", "count": 5}
							// 				]
							// 			};
							// if the server was reached and valid response was received, then below.
							.then(function(response){
								var data = response.data;
								$scope.stats = data.users;
								$scope.message = "Below are the counts of all the requests submitted by all the users.";
								$scope.showTable = true;
							},
							// if the request was not successful
							function(response){
								$scope.message = "There was an error processing your request. Please try again after sometime.";
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
					});
