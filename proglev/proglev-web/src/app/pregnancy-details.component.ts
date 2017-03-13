import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Pregnancy, ProgesteroneLevelMeasurement } from "./pregnancy";
import { PregnancyRepository } from "./pregnancy.repository";
import 'rxjs/add/operator/switchMap';
declare var Chart: any;


@Component({
  selector: 'pregnancy-details',
  templateUrl: './pregnancy-details.component.html'
})
export class PregnancyDetailsComponent implements OnInit {
  pregnancy: Pregnancy = new Pregnancy();

  constructor(private pregnancyRepository: PregnancyRepository,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location) {
  }

  ngOnInit(): void {
    this.route.params
      .switchMap((params: Params) => this.pregnancyRepository.getById(+params['id']))
      .subscribe(pregnancy => {
        this.pregnancy = pregnancy;
        this.drawChart(this.pregnancy);
      });
  }

  drawChart(pregnancy: Pregnancy) {
    const EMPTY = {};

    const MILLIS_IN_WEEK = (1000 * 60 * 60 * 24 * 7);

    const BAD_BACKGROUND_COLOR = 'rgba(255, 99, 132, 1)';
    const BAD_BORDER_COLOR = 'rgba(255, 99, 132, 0.2)';
    const GOOD_BACKGROUND_COLOR = 'rgba(54, 162, 235, 1)';
    const GOOD_BORDER_COLOR = 'rgba(54, 162, 235, 0.2)';

    const FIRST_VISIBLE_WEEK = 4;
    const LAST_VISIBLE_WEEK = 40;

    const BUBBLE_RADIUS = 10;

    const labels = createLabels();
    const chartData = loadData();

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
      const measurementDate: Date = toDate(measurement.measurementDate);
      const lastPertiodDate: Date = toDate(pregnancy.lastPeriodDate);
      return Math.round((measurementDate.getTime() - lastPertiodDate.getTime()) / (MILLIS_IN_WEEK));
    }

    function toDate(value: any): Date {
      return (typeof value) == "string"
        ? new Date(value)
        : value;
    }

    function prepareBubbleData(pregnancy: Pregnancy) {
      const result: any = {};
      result.dataForBubble = prepareEmptyDataset();
      result.bubbleBackgroundColor = GOOD_BACKGROUND_COLOR;
      result.bubbleBorderColor = GOOD_BORDER_COLOR;
      let measurementIndex = 0;
      for (let measurement of pregnancy.measurements) {
        const progesteroneLevel = measurement.progesteroneLevel;
        const week = calculateWeekForMeasurement(pregnancy, measurement);
        const currentPoint: BubbleData = { x: week, y: progesteroneLevel, r: BUBBLE_RADIUS, measurementIndex: measurementIndex };
        const dataIndex = Math.floor((week - FIRST_VISIBLE_WEEK) / 2);

        result.dataForBubble[dataIndex] = currentPoint;
        measurementIndex++;
      }

      return result;
    }

    


    console.log("drawing chart");
    const { dataForBubble, bubbleBackgroundColor, bubbleBorderColor } = prepareBubbleData(pregnancy);
    const canvas = document.getElementById("myChart");
    const myChart = new Chart(canvas, {
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

    canvas.onclick = e => this.logClickedElement(myChart, e);


  }

  logClickedElement(chart: any, e: Event) {
      console.log(e);
      const chartElement = chart.getElementAtEvent(e)[0];
      if (chartElement) {
        console.log(chartElement);
        console.log(chartElement._index);
        console.log(chart.config.data.datasets[3].data[chartElement._index]);
        let dataObject = <BubbleData>chart.config.data.datasets[3].data[chartElement._index];
        this.router.navigate(["measurement", dataObject.measurementIndex], { relativeTo: this.route });

      }

    }
}


class BubbleData { 
  x: number; 
  y: number; 
  r: number; 
  measurementIndex: number; }
