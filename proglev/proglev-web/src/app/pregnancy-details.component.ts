import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {Location} from '@angular/common';
import {Pregnancy, ProgesteroneLevelMeasurement} from "./pregnancy";
import {PregnancyRepository} from "./pregnancy.repository";
import 'rxjs/add/operator/switchMap';
// import {Chart, LinearChartData, ChartDataSets, ChartOptions, ChartLegendOptions, ChartScales} from "chart.js";
declare var Chart: any;


@Component({
  selector: 'pregnancy-details',
  templateUrl: './pregnancy-details.component.html'
})
export class PregnancyDetailsComponent implements OnInit {
  pregnancy: Pregnancy = new Pregnancy();

  constructor(private pregnancyRepository: PregnancyRepository,
              private route: ActivatedRoute,
              private location: Location) {
  }

  ngOnInit(): void {
    this.route.params
      .switchMap((params: Params) => this.pregnancyRepository.getById(+params['id']))
      .subscribe(pregnancy => {
        this.pregnancy = pregnancy;
        drawChart(this.pregnancy);
      });
  }


}


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

// const inputData = {
//   lastPeriodDate: null,
//   progesteroneLevel: null,
//   measurements: [],
//   isValid: function () {
//     return !!(this.lastPeriodDate && this.progesteroneLevel);
//   }
// };
// function resetInputData(){
//   inputData.lastPeriodDate = null;
//   inputData.progesteroneLevel = null;
//   inputData.measurements = [];
// }

function createLabels() {
  const labels = [];
  for (let i = FIRST_VISIBLE_WEEK; i <= LAST_VISIBLE_WEEK; i += 2) {
    labels.push(i);
  }
  return labels;
}

function loadData() {
  const avgData = [21.6, 25.9, 26.2, 30.0, 33.4, 39.5, 47.8, 51.8, 55.6, 65.0,
    72.2, 81.6, 90.4, 104.5, 118.2, 134.8, 152.3, 159.4, 166.2];
  const maxData = [36.76, 43.03, 43.24, 45.19, 56.22, 55.57, 64.86, 72.00, 75.03,
    88.22, 96.86, 105.08, 115.46, 129.08, 153.95, 168.86];
  const minData = [6.27, 9.30, 8.43, 14.05, 16.43, 23.14, 30.27, 32.43, 36.32,
    42.16, 45.84, 54.92, 64.86, 72.86, 77.19, 97.73, 111.14, 127.35, 139.03];
  // for (let i = FIRST_VISIBLE_WEEK; i <= LAST_VISIBLE_WEEK; i++) {
  //   const stdDev = 5 + Math.random() * 5;
  //   const mean = 20 + i * i / 10 + Math.random() * 5;
  //   avgData.push(mean);
  //   maxData.push(mean + 2 * stdDev);
  //   minData.push(mean - 2 * stdDev);
  // }
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

function calculateWeekForMeasurement(pregnancy: Pregnancy, measurement: ProgesteroneLevelMeasurement) {
  const measurementDate: Date =
    (typeof measurement.measurementDate) == "string"
      ? new Date(measurement.measurementDate)
      : measurement.measurementDate;
  return Math.round((measurementDate.getTime() - pregnancy.lastPeriodDate.getTime()) / (MILLIS_IN_WEEK));
}

function prepareBubbleData(pregnancy: Pregnancy) {
  const result: any = {};
  result.dataForBubble = prepareEmptyDataset();
  result.bubbleBackgroundColor = GOOD_BACKGROUND_COLOR;
  result.bubbleBorderColor = GOOD_BORDER_COLOR;
  for (let measurement of pregnancy.measurements) {
    const progesteroneLevel = measurement.progesteroneLevel;
    const week = calculateWeekForMeasurement(pregnancy, measurement);
    const currentPoint = {x: week, y: progesteroneLevel, r: BUBBLE_RADIUS};
    const dataIndex = Math.floor((week - FIRST_VISIBLE_WEEK) / 2);

    result.dataForBubble[dataIndex] = currentPoint;
  }

  return result;
}

function drawChart(pregnancy: Pregnancy) {
  console.log("drawing chart");
  const {dataForBubble, bubbleBackgroundColor, bubbleBorderColor} = prepareBubbleData(pregnancy);
  const myChart = new Chart(document.getElementById("myChart"), {
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
