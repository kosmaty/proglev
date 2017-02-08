(function () {
    "use strict";
    angular.module('progesteroneLevel')
        .controller('addPregnancyCtrl', ['$scope', 'pregnancyService', function($scope, pregnancyService) {
            $scope.pregnancy = null;
            $scope.submit = function() {
                console.log(JSON.stringify($scope.pregnancy));
                pregnancyService.create($scope.pregnancy)
                    .then(function(){
                        $scope.pregnancy = null;
                        $scope.added = true;
                    });
            };
        }]);
})();