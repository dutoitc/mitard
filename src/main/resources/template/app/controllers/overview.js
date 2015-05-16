'use strict';

angular.module('mitardApp.overview', ['ngRoute', 'jqwidgets'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/overview', {
    templateUrl: 'app/views/overview.html',
    controller: 'OverviewCtrl'
  });
}])

.controller('OverviewCtrl', ['$http', '$scope', function($http, $scope) {
    var scope = $scope;
    var self=this;

    // https://www.jqwidgets.com/jquery-widgets-demo/demos/jqxangular/treemap.htm
    $scope.treeMapSettings =
    {
        width: 800,
        showLegend: false,
        height: 400,
        colorRange: 100,
        colorMode: 'autoColors',
        baseColor: '#52CBFF'
    };
    $scope.data = [ ];




    $http.get('data/statistics.json')
        .success(function(data) {
            $scope.stats=data;
            //console.log(data.componentCounts);

            var nbComponents=0;
            jQuery.each( data.componentCounts, function( key, value ) {
                            nbComponents+=parseInt(value);
            });

            var componentCountsList=[];
            var limit=nbComponents/100;
            var nbOthers=0;
            jQuery.each( data.componentCounts, function( key, value ) {
                if (value>limit) {
                    componentCountsList.push({"label":key, "value":value});
                } else {
                    nbOthers+=value;
                }
            });
            componentCountsList.sort(function(a, b){return b["value"]-a["value"]});
            componentCountsList.push({"label":"...", "value":nbOthers});

            scope.stats.nbComponents=nbComponents;
            $scope.data=componentCountsList;





    });


    $http.get('data/violations.json')
        .success(function(data) {
            $scope.violations=data;
            });



}]);