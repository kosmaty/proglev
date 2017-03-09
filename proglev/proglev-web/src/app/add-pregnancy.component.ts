import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params, Router} from '@angular/router';
import {Location}                 from '@angular/common';
import {Pregnancy, ProgesteroneLevelMeasurement} from "./pregnancy";
import {PregnancyRepository} from "./pregnancy.repository";
import 'rxjs/add/operator/switchMap';


@Component({
  selector: 'add-pregnancy',
  templateUrl: './add-pregnancy.component.html'
})
export class AddPregnancyComponent {
  pregnancy: Pregnancy = new Pregnancy();

  constructor(private pregnancyRepository: PregnancyRepository,
              private route: ActivatedRoute,
              private router: Router,
              private location: Location) {
  }

  onSubmit() {
    this.pregnancyRepository.addPregnancy(this.pregnancy)
      .then(pregnancy => {
        return this.router.navigate(["pregnancy", +pregnancy.id]);
      })
      .catch(error => console.log(error));

  }


}
