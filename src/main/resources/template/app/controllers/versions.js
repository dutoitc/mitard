'use strict';

function sizeOf(obj) {
    var size = 0, key;
    for (key in obj) {
        if (obj.hasOwnProperty(key)) size++;
    }
    return size;
};


angular.module('mitardApp.versions', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/versions', {
    templateUrl: 'app/views/versions.html',
    controller: 'VersionsCtrl'
  });
}])

.controller('VersionsCtrl', ['$http', '$scope', '$sce', function($http, $scope, $sce) {

    $scope.errors="";
    $scope.minorsProcesses="";
    $scope.minorsServices="";
    $scope.minorsRoutes="";

    $http.get('data/dependencies.json')
        .success(function(data) {
            $scope.dependencies=data;
            console.log($scope.dependencies);

            console.log($scope.dependencies.processDependencies);
            var localErrors="";
            for (var parent in $scope.dependencies.processDependencies) {
                if (!$scope.dependencies.processDependencies.hasOwnProperty(parent)) continue;
                var values = $scope.dependencies.processDependencies[parent];
                //console.log(parent + "  " + (parent.substring(3).indexOf("_")+4));
                var parentVersion = parent.substring(parent.substring(3).indexOf("_")+4);
                if (!parentVersion.endsWith("_0")) continue; // only compute major->minor/latest
                for (var i= 0, l=values.length; i<l; i++) {
                    var child = values[i];
                    if (child.endsWith("latest") || !child.endsWith("0")) {
                        localErrors += parent + "-&gt;" + child + "<br/>";
                    }
                }
            }
            $scope.errors=$sce.trustAsHtml(localErrors);
        });

    $http.get('data/processes.json')
        .success(function(data) {
            var out = [];
            for (var i=0; i<data.processes.length; i++) {
                if (data.processes[i].version.endsWith("0")) continue;
                out.push(data.processes[i].path + "." + data.processes[i].name +  " " + data.processes[i].version + "<br/>");
            }
            out.sort();
            var s="";
            for (var i=0; i<out.length; i++) {
                s+=out[i];
            }
            $scope.minorsProcesses = $sce.trustAsHtml(s);
        });

    $http.get('data/routes.json')
        .success(function(data) {
            var out = [];
            for (var i=0; i<data.routes.length; i++) {
                if (data.routes[i].version.endsWith("0")) continue;
                out.push(data.routes[i].path +  "." + data.routes[i].name +  " " + data.routes[i].version + "<br/>");
            }
            out.sort();
            var s="";
            for (var i=0; i<out.length; i++) {
                s+=out[i];
            }
            $scope.minorsRoutes = $sce.trustAsHtml(s);
        });


    $http.get('data/services.json')
        .success(function(data) {
            var out = [];
            for (var i=0; i<data.services.length; i++) {
                if (data.services[i].version.endsWith("0")) continue;
                out.push(data.services[i].path +  "." + data.services[i].name +  " " + data.services[i].version + "<br/>");
            }
            out.sort();
            var s="";
            for (var i=0; i<out.length; i++) {
                s+=out[i];
            }
            $scope.minorsServices = $sce.trustAsHtml(s);
        });



}]);