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
            for (var i=0, l=data["fileViolationses"].length; i<l; i++) {
                var fileViolations = data["fileViolationses"][i];
                var nbViolations = fileViolations["generalViolations"].length;
                var componentViolations = fileViolations["componentViolations"];
                for (var cv in componentViolations) {
                    nbViolations+=componentViolations[cv].length;
                }
                fileViolations.nbViolations=nbViolations;
            }
            $scope.violations=data;
        });


}]);