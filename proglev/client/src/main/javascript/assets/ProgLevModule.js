(function () {
    "use strict";
    angular.module('progesteroneLevel', ['ngRoute', 'ngSanitize'])
        .config(['$routeProvider', function($routeProvider){

            $routeProvider
                .when('/', {
                    controller: 'indexCtrl',
                    templateUrl: '/assets/parts/index.html'
                })

                .when('/add-pregnancy', {
                    controller: 'addPregnancyCtrl',
                    templateUrl: '/assets/parts/addPregnancy.html'
                })

                .when('/pregnancy/:id', {
                    controller: 'showPregnancyCtrl',
                    templateUrl: '/assets/parts/show-pregnancy.html'
                })

                .otherwise({
                    redirectTo: '/'
                });
        }])
        .controller('appCtl', ['$scope', function appCtrl($scope) {
        }])
        .filter('paragraph', function paragraphFilter(){
            return function(input){
                return (input) ? input.replace(/\n/g, '<br />') : input;
            };
        })
        .directive('editable', function editableDirective(){
            return {
                restrict: 'AE',
                templateUrl: 'assets/parts/editable.html',
                scope: {
                    value: '=editable',
                    field: '@fieldType'
                },
                controller: function editableController($scope, $timeout){
                    console.log('creating controller for editable');
                    $scope.editor = {
                        showing: false,
                        value: $scope.value
                    };
                    $scope.toggleEditor = function(){
                        $scope.editor.showing = !$scope.editor.showing;
                    };
                    $scope.field = ($scope.field) ? $scope.field : 'text';

                    $scope.save = function(){
                        $scope.value = $scope.editor.value;
                        $scope.toggleEditor();
                        $scope.$emit('saved');
                    };
                    $scope.$on('loaded', function(){
                        $timeout(() => $scope.editor.value = $scope.value, 0);
                    });
                }
            };
        });
})();