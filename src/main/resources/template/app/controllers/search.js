'use strict';

angular.module('mitardApp.search', ['ngRoute'])
.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/search', {
    templateUrl: 'app/views/search.html',
    controller: 'SearchCtrl'
  });
}])

.controller('SearchCtrl', ['$http', '$scope', '$sce', function($http, $scope, $sce) {

    $http.get('data/textFiles.json')
        .success(function(data) {
            $scope.searchData = data;
        });

    $scope.search = function() {
        var res=[];
        var term = $scope.searchText;
        for (var i=0; i<$scope.searchData.textFiles.length; i++) {
            var textFile = $scope.searchData.textFiles[i];
            for (var j=0; j<textFile.textList.length; j++) {
                var entry = textFile.textList[j];
                if (entry.text.indexOf(term)>-1) {
                    //res+="Found " + term + " in " + textFile.filename + ":" + entry.nodeName + "<br/>\r\n\t&nbsp;&nbsp;" + entry.text + "<br/><br/>\r\n";
                    res.push({"filename":textFile.filename, "nodeName":entry.nodeName, "text":entry.text});
                }
            }
        }
        $scope.searchResult=res;
    };

    $scope.highlight = function(text, search) {
        if (!search) {
            return $sce.trustAsHtml(text);
        }
        return $sce.trustAsHtml(text.replace(new RegExp(search, 'gi'), '<span class="highlightedText">$&</span>'));
    };


}]);