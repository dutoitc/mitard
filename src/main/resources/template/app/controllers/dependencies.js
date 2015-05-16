'use strict';

angular.module('mitardApp.dependencies', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/dependencies', {
    templateUrl: 'app/views/dependencies.html',
    controller: 'DependenciesCtrl'
  });
}])

.controller('DependenciesCtrl', ['$http', '$scope', function($http, $scope) {


}]);