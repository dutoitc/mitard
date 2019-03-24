'use strict';

angular.module('mitardApp.diagrams', ['ngRoute'])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/diagrams', {
            templateUrl: 'app/views/diagrams.html',
            controller: 'DiagramsCtrl'
        });
    }])

    .controller('DiagramsCtrl', ['$http', '$scope', '$sce', function ($http, $scope, $sce) {
        var self = this;

        $scope.diagrams = [];

        $http.get('data/diagrams.json')
            .success(function (data) {
                $scope.diagrams = data;
                console.log(data);
            });


    }]);