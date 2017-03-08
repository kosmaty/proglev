import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Params} from '@angular/router';
import {Location}                 from '@angular/common';
import {Pregnancy} from "./pregnancy";
import {PregnancyRepository} from "./pregnancy.repository";
import 'rxjs/add/operator/switchMap';

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
      .subscribe(pregnancy => this.pregnancy = pregnancy);
  }
}
