import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Location } from '@angular/common';
import { Pregnancy, ProgesteroneLevelMeasurement } from "./pregnancy";
import { PregnancyRepository } from "./pregnancy.repository";



@Component({
  selector: 'delete-pregnancy',
  template: '<div></div>'
})
export class DeletePregnancyComponent implements OnInit {  p

  constructor(private pregnancyRepository: PregnancyRepository,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location) {
  }

  ngOnInit() {

    this.route.params.subscribe(params => {
      this.pregnancyRepository.deletePregnancyById(+params['id'])
        .then(x => this.router.navigate([""]) )
         .catch(error => console.log(error));
    });
  }




}
