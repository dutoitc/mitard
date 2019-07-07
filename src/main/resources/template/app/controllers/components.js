'use strict';

angular.module('mitardApp.components', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/routes', {
    templateUrl: 'app/views/routes.html',
    controller: 'ComponentsCtrl'
  });
  $routeProvider.when('/processes', {
      templateUrl: 'app/views/processes.html',
      controller: 'ComponentsCtrl'
  });
  $routeProvider.when('/services', {
        templateUrl: 'app/views/services.html',
        controller: 'ComponentsCtrl'
   });
  $routeProvider.when('/workflows', {
        templateUrl: 'app/views/workflows.html',
        controller: 'ComponentsCtrl'
   });
}])

.controller('ComponentsCtrl', ['$http', '$scope', function($http, $scope) {
    var scope = $scope;
    var self=this;


    $http.get('data/routes.json')
        .success(function(data) {
            $scope.routes=data["routes"];
    });
    $http.get('data/processes.json')
        .success(function(data) {
            $scope.processes=data["processes"];
    });
    $http.get('data/services.json')
        .success(function(data) {
            $scope.services=data["services"];
    });
    $http.get('data/workflows.json')
        .success(function(data) {
            $scope.workflows=data["workflows"];
    });

}]);