'use strict';

angular.module('mitardApp.violations', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/violations', {
    templateUrl: 'app/views/violations.html',
    controller: 'ViolationsCtrl'
  });
}])

.controller('ViolationsCtrl', ['$http', '$scope', function($http, $scope) {

    $http.get('data/violations.json')
        .success(function(data) {
            $scope.violations=data;
            });


}]);