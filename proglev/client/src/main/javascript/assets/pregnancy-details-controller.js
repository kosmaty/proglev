(function () {
    "use strict";

    angular.module('progesteroneLevel')
        .controller('showPregnancyCtrl', ['$scope', '$routeParams', '$timeout', 'pregnancyService',
            function showPregnancyCtrl($scope, $routeParams, $timeout, pregnancyService) {
            resetInputData();
            $scope.$on('saved', savePregnancy);
            $scope.inputData = inputData;
            $scope.initialize = drawChart;
            $scope.redrawChart = redrawChart;
            $scope.editDetails = false;
            $scope.toggleDetails = function toggleDetails () {
                $scope.editDetails =  !$scope.editDetails;
            };
            $scope.addingMeasurement = false;
            $scope.toggleMeasurement = function toggleMeasurement() {
                $scope.addingMeasurement = !$scope.addingMeasurement;
                if ($scope.addingMeasurement){
                    $scope.measurement = {};
                }
            }
            $scope.addMeasurement = addMeasurement;

            pregnancyService.find($routeParams.id)
                .then(function(pregnancy){
                    console.log(pregnancy);
                    $scope.pregnancy = pregnancy;
                    $scope.pregnancy.lastPeriodDate = new Date(pregnancy.lastPeriodDate);
                    $scope.inputData.lastPeriodDate = pregnancy.lastPeriodDate;
                    $scope.inputData.measurements = pregnancy.progesteroneMeasurements;
                    $scope.$broadcast('loaded');
                    drawChart();
                });

            function savePregnancy(){
                $timeout(function(){
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


    const EMPTY = {};

    const MILLIS_IN_WEEK = (1000 * 60 * 60 * 24 * 7);

    const BAD_BACKGROUND_COLOR = 'rgba(255, 99, 132, 1)';
    const BAD_BORDER_COLOR = 'rgba(255, 99, 132, 0.2)';
    const GOOD_BACKGROUND_COLOR = 'rgba(54, 162, 235, 1)';
    const GOOD_BORDER_COLOR = 'rgba(54, 162, 235, 0.2)';

    const FIRST_VISIBLE_WEEK = 4;
    const LAST_VISIBLE_WEEK = 40;

    const BUBBLE_RADIUS = 3;

    const labels = createLabels();
    const chartData = loadData();

    const inputData = {
        lastPeriodDate: null,
        progesteroneLevel: null,
        measurements: [],
        isValid: function () {
            return !!(this.lastPeriodDate && this.progesteroneLevel);
        }
    };
    function resetInputData(){
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
        const labels = [];
        for (let i = FIRST_VISIBLE_WEEK; i <= LAST_VISIBLE_WEEK; i++) {
            labels.push(i);
        }
        return labels;
    }

    function loadData() {
        const avgData = [];
        const maxData = [];
        const minData = [];
        for (let i = FIRST_VISIBLE_WEEK; i <= LAST_VISIBLE_WEEK; i++) {
            const stdDev = 5 + Math.random() * 5;
            const mean = 20 + i * i / 10 + Math.random() * 5;
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
        const dataForBubble = new Array(LAST_VISIBLE_WEEK - FIRST_VISIBLE_WEEK + 1);
        dataForBubble.fill(EMPTY);
        return dataForBubble;
    }

    function calculateWeek() {
        return Math.round((new Date() - inputData.lastPeriodDate) / (MILLIS_IN_WEEK));
    }

    function calculateWeekForMeasurement(measurement) {
        const measurementDate =
            (typeof measurement.measurementDate) == "string"
                ? new Date(measurement.measurementDate)
                : measurement.measurementDate;
        return Math.round((measurementDate - inputData.lastPeriodDate) / (MILLIS_IN_WEEK));
    }

    function prepareBubbleData() {
        const result = {};
        result.dataForBubble = prepareEmptyDataset();
        result.bubbleBackgroundColor = GOOD_BACKGROUND_COLOR;
        result.bubbleBorderColor = GOOD_BORDER_COLOR;
        for (let measurement of inputData.measurements){
            const progesteroneLevel = measurement.progesteroneLevel;
            const week = calculateWeekForMeasurement(measurement);
            const currentPoint = {x: week, y: progesteroneLevel, r: BUBBLE_RADIUS};
            const dataIndex = week - FIRST_VISIBLE_WEEK;

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
        return result;
    }

    function drawChart() {
        console.log("drawing chart");
        const {dataForBubble, bubbleBackgroundColor, bubbleBorderColor} = prepareBubbleData();
        const myChart = new Chart($("#myChart"), {
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
                },
                    {
                        type: 'line',
                        label: 'Mean',
                        data: chartData.avgData,
                        fill: false,
                        pointRadius: 0,
                        borderColor: 'rgba(0, 0, 0, 0.5)'
                    },
                    {
                        type: 'line',
                        label: '-2SD',
                        data: chartData.minData,
                        fill: false,
                        borderDash: [5],
                        pointRadius: 0
                    },
                    {
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