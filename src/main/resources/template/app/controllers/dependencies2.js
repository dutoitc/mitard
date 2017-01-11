'use strict';

angular.module('mitardApp.dependencies2', ['ngRoute'])
    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/dependencies2', {
            templateUrl: 'app/views/dependencies2.html',
            controller: 'DependenciesCtrl2'
        });
    }])

    .controller('DependenciesCtrl2', ['$http', '$scope', '$sce', function ($http, $scope, $sce) {
        var self = this;

        $scope.treeQuery = "";

        $http.get('data/dependencies.json')
            .success(function (data) {
                $scope.dependencies = data;
            });

        $scope.computeTree = function () {

        };

        $scope.layout = {name: 'cose'};
        $scope.elements = {
            n1: {data: {name: '111'}},
            n2: {data: {name: '222', href: 'http://www.vd.ch'}},
            n3: {data: {}},
            n4: {data: {}},
            n5: {data: {}},
            e1: {
                data: {
                    source: 'n1',
                    target: 'n3'
                }
            },
            e2: {
                data: {
                    source: 'n2',
                    target: 'n4'
                }
            },
            e3: {
                data: {
                    source: 'n4',
                    target: 'n5'
                }
            },
            e4: {
                data: {
                    source: 'n4',
                    target: 'n3'
                }
            },
        }

        $scope.style = [
            {
                selector: 'node',
                style: {
                    'content': 'data(name)',
                    'text-valign': 'center',
                    'color': 'white',
                    'text-outline-width': 2,
                    'text-outline-color': '#888',
                    'font-size':'8px'
                }
            }];


    }]);