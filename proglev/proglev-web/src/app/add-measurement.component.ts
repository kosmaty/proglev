import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Pregnancy, ProgesteroneLevelMeasurement } from "./pregnancy";
import { PregnancyRepository } from "./pregnancy.repository";
import 'rxjs/add/operator/switchMap';


@Component({
  selector: 'add-measurement',
  templateUrl: './add-measurement.component.html'
})
export class AddMeasurementComponent implements OnInit {
  pregnancy: Pregnancy = new Pregnancy();
  measurement: ProgesteroneLevelMeasurement = new ProgesteroneLevelMeasurement();
  measurementIndex: number;

  constructor(private pregnancyRepository: PregnancyRepository,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      const pregnancyId = +params['pregnancyId'];
      const measurementIndex = +params['measurementId'];
      this.pregnancyRepository.getById(pregnancyId)
        .then(pregnancy => {
          this.pregnancy = pregnancy;
          if (measurementIndex >= 0) {
            this.measurement = pregnancy.measurements[measurementIndex];
            this.measurementIndex = measurementIndex;
          }
        })
    });


  }

  addMeasurement() {
    if (this.measurementIndex >= 0) {
      this.pregnancy.measurements[this.measurementIndex] = this.measurement;
    } else {
      this.pregnancy.measurements.push(this.measurement);
    }
    this.pregnancyRepository.savePregnancy(this.pregnancy)
      .then(pregnancy => {
        return this.router.navigate(["pregnancy", +pregnancy.id]);
      })
      .catch(error => console.log(error));

  }

  cancel() {
    this.location.back();
  }


}
