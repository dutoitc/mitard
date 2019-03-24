'use strict';

// Declare app level module which depends on views, and components
var mitardApp = angular.module('mitardApp', [
  'ngRoute',
  'mitardApp.overview',
  'mitardApp.components',
  'mitardApp.dependencies',
  'mitardApp.dependencies2',
  'mitardApp.diagrams',
  'mitardApp.datasources',
  'mitardApp.violations',
  'mitardApp.versions',
  'mitardApp.search',
  'ui.bootstrap',
  'ngCytoscape'
])
.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
    $routeProvider
    .when('/help', {
        templateUrl: 'app/views/help.html'
    })
    .otherwise({redirectTo: '/overview'});
}])
    .filter('trusted', ['$sce', function($sce){
        return function(text) {
            return $sce.trustAsHtml(text);
        };
}]);
