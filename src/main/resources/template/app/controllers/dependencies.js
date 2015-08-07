'use strict';

angular.module('mitardApp.dependencies', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/dependencies', {
    templateUrl: 'app/views/dependencies.html',
    controller: 'DependenciesCtrl'
  });
}])

.controller('DependenciesCtrl', ['$http', '$scope', '$sce', function($http, $scope, $sce) {
        var self = this;

        $scope.treeQuery="";
        $scope.treeResult="";

        $http.get('data/dependencies.json')
            .success(function(data) {
                $scope.dependencies=data;
        });

        $scope.computeTree=function() {
            if ($scope.treeQuery.length<3) {
                $scope.treeResult="";
            } else {
                var result = self.findDependencies($scope.treeQuery, true, 0, 10);
                $scope.treeResult = $sce.trustAsHtml(result);
            }
        };

        self.findDependencies = function(query, contains, indent, limit) {
            var result="";
            var lst = $.extend({}, $scope.dependencies.processDependencies, $scope.dependencies.serviceDependencies, $scope.dependencies.routeDependencies);


            for (var key in lst) {
                if ((contains && key.indexOf(query)>=0) || (!contains && key==query)) {
                    for (var i=0; i<indent; i++) result+="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    result+=key+"<br/>";
                    if (limit>0) {
                        var children = {};
                        for (var key2 in lst[key]) {
                            var child = lst[key][key2];
                            children[child]=child; // avoid duplicates
                        }
                        for (var j in children) {
                            var child = children[j];
                            result+=self.findDependencies(child, false, indent+1, limit-1);
                        }
                    }
                }
            }

            return result;

        }

}]);