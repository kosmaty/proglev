'use strict';

(function () {
    "use strict";

    angular.module('progesteroneLevel', ['ngRoute', 'ngSanitize']).config(['$routeProvider', function ($routeProvider) {

        $routeProvider.when('/', {
            controller: 'indexCtrl',
            templateUrl: '/assets/parts/index.html'
        }).when('/add-pregnancy', {
            controller: 'addPregnancyCtrl',
            templateUrl: '/assets/parts/addPregnancy.html'
        }).when('/pregnancy/:id', {
            controller: 'showPregnancyCtrl',
            templateUrl: '/assets/parts/show-pregnancy.html'
        }).otherwise({
            redirectTo: '/'
        });
    }]).controller('appCtl', ['$scope', function appCtrl($scope) {}]).filter('paragraph', function paragraphFilter() {
        return function (input) {
            return input ? input.replace(/\n/g, '<br />') : input;
        };
    }).directive('editable', function editableDirective() {
        return {
            restrict: 'AE',
            templateUrl: 'assets/parts/editable.html',
            scope: {
                value: '=editable',
                field: '@fieldType'
            },
            controller: function editableController($scope, $timeout) {
                console.log('creating controller for editable');
                $scope.editor = {
                    showing: false,
                    value: $scope.value
                };
                $scope.toggleEditor = function () {
                    $scope.editor.showing = !$scope.editor.showing;
                };
                $scope.field = $scope.field ? $scope.field : 'text';

                $scope.save = function () {
                    $scope.value = $scope.editor.value;
                    $scope.toggleEditor();
                    $scope.$emit('saved');
                };
                $scope.$on('loaded', function () {
                    $timeout(function () {
                        return $scope.editor.value = $scope.value;
                    }, 0);
                });
            }
        };
    });
})();
(function () {
    "use strict";

    angular.module('progesteroneLevel').controller('addPregnancyCtrl', ['$scope', 'pregnancyService', function ($scope, pregnancyService) {
        $scope.pregnancy = null;
        $scope.submit = function () {
            console.log(JSON.stringify($scope.pregnancy));
            pregnancyService.create($scope.pregnancy).then(function () {
                $scope.pregnancy = null;
                $scope.added = true;
            });
        };
    }]);
})();
(function () {
    "use strict";

    angular.module('progesteroneLevel').controller('indexCtrl', ['$scope', 'pregnancyService', function ($scope, pregnancyService) {
        pregnancyService.get().then(function (pregnancyList) {
            return $scope.pregnancies = pregnancyList;
        });
    }]);
})();
(function () {
    "use strict";

    angular.module('progesteroneLevel').factory('pregnancyService', function pregnancyService($http) {
        var baseUrl = 'http://localhost:8080/api/pregnancies/';

        return {
            get: function get() {
                return $http.get(baseUrl).then(function (response) {
                    return response.data;
                });
            },
            find: function find(id) {
                return $http.get(baseUrl + id).then(function (response) {
                    return response.data;
                });
            },
            create: function create(pregnancy) {
                return $http.post(baseUrl, pregnancy);
            },
            update: function update(pregnancy) {
                return $http.put(baseUrl + "/" + pregnancy.id, pregnancy);
            },
            destroy: function destroy(id) {
                contacts.splice(id, 1);
            }
        };
    });
})();
(function () {
    "use strict";

    angular.module('progesteroneLevel').controller('showPregnancyCtrl', ['$scope', '$routeParams', '$timeout', 'pregnancyService', function showPregnancyCtrl($scope, $routeParams, $timeout, pregnancyService) {
        resetInputData();
        $scope.$on('saved', savePregnancy);
        $scope.inputData = inputData;
        $scope.initialize = drawChart;
        $scope.redrawChart = redrawChart;
        $scope.editDetails = false;
        $scope.toggleDetails = function toggleDetails() {
            $scope.editDetails = !$scope.editDetails;
        };
        $scope.addingMeasurement = false;
        $scope.toggleMeasurement = function toggleMeasurement() {
            $scope.addingMeasurement = !$scope.addingMeasurement;
            if ($scope.addingMeasurement) {
                $scope.measurement = {};
            }
        };
        $scope.addMeasurement = addMeasurement;

        pregnancyService.find($routeParams.id).then(function (pregnancy) {
            console.log(pregnancy);
            $scope.pregnancy = pregnancy;
            $scope.pregnancy.lastPeriodDate = new Date(pregnancy.lastPeriodDate);
            $scope.inputData.lastPeriodDate = pregnancy.lastPeriodDate;
            $scope.inputData.measurements = pregnancy.progesteroneMeasurements;
            $scope.$broadcast('loaded');
            drawChart();
        });

        function savePregnancy() {
            $timeout(function () {
                pregnancyService.update($scope.pregnancy);
            }, 0);
        }

        function addMeasurement() {
            console.log($scope.measurement);
            $scope.pregnancy.progesteroneMeasurements.push($scope.measurement);
            savePregnancy();
            drawChart();
            $scope.toggleMeasurement();
        }
    }]);

    var EMPTY = {};

    var MILLIS_IN_WEEK = 1000 * 60 * 60 * 24 * 7;

    var BAD_BACKGROUND_COLOR = 'rgba(255, 99, 132, 1)';
    var BAD_BORDER_COLOR = 'rgba(255, 99, 132, 0.2)';
    var GOOD_BACKGROUND_COLOR = 'rgba(54, 162, 235, 1)';
    var GOOD_BORDER_COLOR = 'rgba(54, 162, 235, 0.2)';

    var FIRST_VISIBLE_WEEK = 4;
    var LAST_VISIBLE_WEEK = 40;

    var BUBBLE_RADIUS = 3;

    var labels = createLabels();
    var chartData = loadData();

    var inputData = {
        lastPeriodDate: null,
        progesteroneLevel: null,
        measurements: [],
        isValid: function isValid() {
            return !!(this.lastPeriodDate && this.progesteroneLevel);
        }
    };
    function resetInputData() {
        inputData.lastPeriodDate = null;
        inputData.progesteroneLevel = null;
        inputData.measurements = [];
    }

    function redrawChart() {
        if (inputData.isValid()) {
            drawChart();
        }
    }

    function createLabels() {
        var labels = [];
        for (var i = FIRST_VISIBLE_WEEK; i <= LAST_VISIBLE_WEEK; i++) {
            labels.push(i);
        }
        return labels;
    }

    function loadData() {
        var avgData = [];
        var maxData = [];
        var minData = [];
        for (var i = FIRST_VISIBLE_WEEK; i <= LAST_VISIBLE_WEEK; i++) {
            var stdDev = 5 + Math.random() * 5;
            var mean = 20 + i * i / 10 + Math.random() * 5;
            avgData.push(mean);
            maxData.push(mean + 2 * stdDev);
            minData.push(mean - 2 * stdDev);
        }
        return {
            avgData: avgData,
            maxData: maxData,
            minData: minData
        };
    }

    function prepareEmptyDataset() {
        var dataForBubble = new Array(LAST_VISIBLE_WEEK - FIRST_VISIBLE_WEEK + 1);
        dataForBubble.fill(EMPTY);
        return dataForBubble;
    }

    function calculateWeek() {
        return Math.round((new Date() - inputData.lastPeriodDate) / MILLIS_IN_WEEK);
    }

    function calculateWeekForMeasurement(measurement) {
        var measurementDate = typeof measurement.measurementDate == "string" ? new Date(measurement.measurementDate) : measurement.measurementDate;
        return Math.round((measurementDate - inputData.lastPeriodDate) / MILLIS_IN_WEEK);
    }

    function prepareBubbleData() {
        var result = {};
        result.dataForBubble = prepareEmptyDataset();
        result.bubbleBackgroundColor = GOOD_BACKGROUND_COLOR;
        result.bubbleBorderColor = GOOD_BORDER_COLOR;
        var _iteratorNormalCompletion = true;
        var _didIteratorError = false;
        var _iteratorError = undefined;

        try {
            for (var _iterator = inputData.measurements[Symbol.iterator](), _step; !(_iteratorNormalCompletion = (_step = _iterator.next()).done); _iteratorNormalCompletion = true) {
                var measurement = _step.value;

                var progesteroneLevel = measurement.progesteroneLevel;
                var week = calculateWeekForMeasurement(measurement);
                var currentPoint = { x: week, y: progesteroneLevel, r: BUBBLE_RADIUS };
                var dataIndex = week - FIRST_VISIBLE_WEEK;

                result.dataForBubble[dataIndex] = currentPoint;
            }
            // if (inputData.isValid()) {
            //     const progesteroneLevel = inputData.progesteroneLevel;
            //     const week = calculateWeek();
            //     const currentPoint = {x: week, y: progesteroneLevel, r: BUBBLE_RADIUS};
            //     const dataIndex = week - FIRST_VISIBLE_WEEK;
            //
            //     result.dataForBubble[dataIndex] = currentPoint;
            //     const isGoodLevel =
            //         (chartData.minData[dataIndex] < progesteroneLevel
            //         && progesteroneLevel < chartData.maxData[dataIndex]);
            //     if (!isGoodLevel) {
            //         result.bubbleBackgroundColor = BAD_BACKGROUND_COLOR;
            //         result.bubbleBorderColor = BAD_BORDER_COLOR;
            //     }else {
            //         result.bubbleBackgroundColor = GOOD_BACKGROUND_COLOR;
            //         result.bubbleBorderColor = GOOD_BORDER_COLOR;
            //     }
            //
            // }
        } catch (err) {
            _didIteratorError = true;
            _iteratorError = err;
        } finally {
            try {
                if (!_iteratorNormalCompletion && _iterator.return) {
                    _iterator.return();
                }
            } finally {
                if (_didIteratorError) {
                    throw _iteratorError;
                }
            }
        }

        return result;
    }

    function drawChart() {
        console.log("drawing chart");

        var _prepareBubbleData = prepareBubbleData(),
            dataForBubble = _prepareBubbleData.dataForBubble,
            bubbleBackgroundColor = _prepareBubbleData.bubbleBackgroundColor,
            bubbleBorderColor = _prepareBubbleData.bubbleBorderColor;

        var myChart = new Chart($("#myChart"), {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    type: 'line',
                    label: '+2SD',
                    data: chartData.maxData,
                    fill: false,
                    borderDash: [5],
                    pointRadius: 0
                }, {
                    type: 'line',
                    label: 'Mean',
                    data: chartData.avgData,
                    fill: false,
                    pointRadius: 0,
                    borderColor: 'rgba(0, 0, 0, 0.5)'
                }, {
                    type: 'line',
                    label: '-2SD',
                    data: chartData.minData,
                    fill: false,
                    borderDash: [5],
                    pointRadius: 0
                }, {
                    type: 'bubble',
                    label: 'Current value',
                    data: dataForBubble,
                    backgroundColor: bubbleBackgroundColor,
                    borderColor: bubbleBorderColor,
                    borderWidth: 20
                }]
            },
            options: {
                showLines: true,
                legend: {
                    display: false
                },
                tooltips: {
                    enabled: false
                },
                scales: {
                    yAxes: [{
                        ticks: {
                            beginAtZero: true
                        },
                        scaleLabel: {
                            display: true,
                            labelString: 'Poziom progesteronu'
                        }
                    }],
                    xAxes: [{
                        scaleLabel: {
                            display: true,
                            labelString: 'Tydzień ciąży'
                        }
                    }]
                },
                animation: {
                    duration: 0
                }
            }
        });
    }
})();