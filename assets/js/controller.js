"use strict";
angular.module('progesteroneLevel', [])
	.controller('appCtl', function($scope){
	    $scope.inputData = {
	        lastPeriodDate: null,
	        progesteroneLevel: null,
	        isValid: function() {
	            return !!(this.lastPeriodDate && this.progesteroneLevel);
	        }
	    }

        function calculateWeek() {
            return Math.round((new Date() - $scope.inputData.lastPeriodDate)/(1000*60*60*24*7));
        }



	    var avgData = [];
	    var maxData = [];
	    var minData = [];
	    var labels = [];


	    for (let i = 4; i <= 40; i++) {
	        let stdDev = 5 + Math.random() * 5;
	        let mean = 20 + i * i / 10 + Math.random() * 5;
	        avgData.push(mean);
	        maxData.push(mean + 2 * stdDev);
	        minData.push(mean - 2 * stdDev);
	        labels.push(i);
	    }

        let badBackgroundColor = 'rgba(255, 99, 132, 1)';
        let badBorderColor = 'rgba(255, 99, 132, 0.2)';
        let goodBackgroundColor = 'rgba(54, 162, 235, 1)';
        let goodBorderColor = 'rgba(54, 162, 235, 0.2)';

        $scope.drawChart = function drawChart() {
            var dataForBubble = [];

            var bubbleBackgroundColor = goodBackgroundColor;
            var bubbleBorderColor = goodBorderColor;

            if ($scope.inputData.isValid()){
                let progesteroneLevel = $scope.inputData.progesteroneLevel;
                let week = calculateWeek();
                let currentPoint = {x: week, y: progesteroneLevel, r: 3};
                let empty = {};
                for (let i = 4; i <= 40; i++){
                    if (i == currentPoint.x){
                        dataForBubble.push(currentPoint);
                    }else {
                        dataForBubble.push(empty);
                    }
                }

                let isGoodLevel = (minData[week - 4] < progesteroneLevel && progesteroneLevel < maxData[week - 4]);
                if (!isGoodLevel){
                    bubbleBackgroundColor = badBackgroundColor;
                    bubbleBorderColor = badBorderColor;
                }

            }

            let ctx = $("#myChart");
            let myChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: labels,
                     datasets: [{
                        type: 'line',
                        label: '+2SD',
                        data: maxData,
                        fill: false,
                        borderDash: [5],
                        pointRadius: 0
                    },
                    {
                        type: 'line',
                        label: 'Mean',
                        data: avgData,
                        fill: false,
                        pointRadius: 0,
                        borderColor: 'rgba(0, 0, 0, 0.5)'
                    },
                    {
                        type: 'line',
                        label: '-2SD',
                        data: minData,
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
                                beginAtZero:true
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

            var originalGetElementAtEvent = myChart.getElementAtEvent;
            myChart.getElementAtEvent = function () {
                return originalGetElementAtEvent.apply(this, arguments).filter(function (e) {
                    return e._datasetIndex == 3;
                });
            }

        }
        $scope.initialize = $scope.drawChart;

        $scope.redrawChart = function(){
            if ($scope.inputData.isValid()){
                $scope.drawChart();
            }
        }
	})
	;