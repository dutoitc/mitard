'use strict';

angular.module('mitardApp.violations', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/violations', {
    templateUrl: 'app/views/violations.html',
    controller: 'ViolationsCtrl'
  });
}])

.controller('ViolationsCtrl', ['$http', '$scope', function($http, $scope) {

    $scope.stats={};

    $http.get('data/violations.json')
        .success(function(data) {
            for (var i=0, l=data["fileViolationses"].length; i<l; i++) {
                var fileViolations = data["fileViolationses"][i];

                // General violations
                var generalViolations = fileViolations["generalViolations"];
                var nbViolations = generalViolations.length;
                for (var j=0; j<generalViolations.length; j++) {
                    var generalViolation = generalViolations[j];
                    if ($scope.stats[generalViolation]) {
                        $scope.stats[generalViolation]+=1;
                    } else {
                        $scope.stats[generalViolation]=1;
                    }
                }

                // Component violations
                var componentViolations = fileViolations["componentViolations"];
                for (var cv in componentViolations) {
                    var componentViolation = componentViolations[cv];
                    nbViolations+=componentViolation.length;

                    for (var vi=0; vi<componentViolation.length; vi++) {
                        var violation = componentViolation[vi];
                        if ($scope.stats[violation]) {
                            $scope.stats[violation]+=1;
                        } else {
                            $scope.stats[violation]=1;
                        }
                    }
                }
                fileViolations.nbViolations=nbViolations;
            }
            $scope.violations=data;
            console.log('Stats', $scope.stats);
        });

        $scope.showBubble = function(violation) {

        }


        $(function () {
            $('[data-toggle="tooltip"]').tooltip()
        })


}]);