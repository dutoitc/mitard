'use strict';

angular.module('mitardApp.dependencies', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/dependencies', {
    templateUrl: 'app/views/dependencies.html',
    controller: 'DependenciesCtrl'
  });
}])

.controller('DependenciesCtrl', ['$http', '$scope', function($http, $scope) {
        var self = this;

    $http.get('data/dependencies.json')
        .success(function(data) {
            $scope.dependencies=data;
    });

        this.computeTree=function() {
            self.treeResult = "Query was " + self.treeQuery;
        };

}]);