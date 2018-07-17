'use strict';

angular.module('mitardApp.datasources', ['ngRoute'])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/datasources', {
            templateUrl: 'app/views/datasources.html',
            controller: 'DatasourcesCtrl'
        });
    }])

    .controller('DatasourcesCtrl', ['$http', '$scope', '$sce', function ($http, $scope, $sce) {
        var self = this;


        $http.get('data/datasources.json')
            .success(function (data) {
                $scope.datasources = data;
            });


    }]);