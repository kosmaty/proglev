import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {Location}                 from '@angular/common';
import {Pregnancy, ProgesteroneLevelMeasurement} from "./pregnancy";
import {PregnancyRepository} from "./pregnancy.repository";
import 'rxjs/add/operator/switchMap';


@Component({
  selector: 'add-measurement',
  templateUrl: './add-measurement.component.html'
})
export class AddMeasurementComponent implements OnInit {
  pregnancy: Pregnancy = new Pregnancy();
  measurement: ProgesteroneLevelMeasurement = new ProgesteroneLevelMeasurement();

  constructor(private pregnancyRepository: PregnancyRepository,
              private route: ActivatedRoute,
              private router: Router,
              private location: Location) {
  }

  ngOnInit(){
    this.route.params.subscribe(params => {
      this.pregnancyRepository.getById(+params['pregnancyId'])
        .then(pregnancy => {
          this.pregnancy = pregnancy;
        })
    });


  }

  addMeasurement() {
    this.pregnancy.measurements.push(this.measurement);
    this.pregnancyRepository.savePregnancy(this.pregnancy)
      .then(pregnancy => {
        return this.router.navigate(["pregnancy", +pregnancy.id]);
      })
      .catch(error => console.log(error));

  }


}
