import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Pregnancy, ProgesteroneLevelMeasurement } from "./pregnancy";
import { PregnancyRepository } from "./pregnancy.repository";
import 'rxjs/add/operator/switchMap';


@Component({
  selector: 'add-pregnancy',
  templateUrl: './add-pregnancy.component.html'
})
export class AddPregnancyComponent implements OnInit {
  pregnancy: Pregnancy = new Pregnancy();
  title: string = "";
  isEdit: boolean = false;

  constructor(private pregnancyRepository: PregnancyRepository,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location) {
  }

  ngOnInit() {

    this.route.params.subscribe(params => {
      if (params['id']) {
        this.pregnancyRepository.getById(+params['id'])
          .then(pregnancy => {
            this.pregnancy = pregnancy;
            this.title = pregnancy.patientFirstName + " " + pregnancy.patientLastName;
            this.isEdit = true;
          })
      } else {
        this.title = "Nowa pacjentka";
      }
    });
  }

  onSubmit() {
    this.save();

  }

  save() {
    this.pregnancyRepository.savePregnancy(this.pregnancy)
      .then(pregnancy => {
        return this.router.navigate(["pregnancy", +pregnancy.id]);
      })
      .catch(error => console.log(error));
  }

  cancel() {
    this.location.back();
  }

  delete() {
    this.pregnancyRepository.deletePregnancy(this.pregnancy)
      .then(result => this.router.navigate([""]))
      .catch(error => console.log(error));
  }


}
