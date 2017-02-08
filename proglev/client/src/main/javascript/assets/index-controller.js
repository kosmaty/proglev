(function () {
    "use strict";
    angular.module('progesteroneLevel')
        .controller('indexCtrl', ['$scope', 'pregnancyService', function ($scope, pregnancyService) {
            pregnancyService.get()
                .then(pregnancyList => $scope.pregnancies = pregnancyList);

        }]);
})();