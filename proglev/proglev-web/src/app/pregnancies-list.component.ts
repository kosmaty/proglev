import { Component, OnInit, Input } from '@angular/core';
import {Pregnancy} from "./pregnancy";
import {PregnancyRepository} from "./pregnancy.repository";


@Component({
  selector: 'pregnancies-list',
  templateUrl: './pregnancies-list.component.html'
})


export class PregnanciesListComponent implements OnInit {
  pregnancies: Pregnancy[];
  @Input() toFind: string = '';

  constructor(private pregnancyRepository: PregnancyRepository) {}

  ngOnInit(): void {
    console.log("initialization");
    this.pregnancyRepository.getAll()
      .then(pregnancies => this.pregnancies = pregnancies);
  }

  // get toFind(){
  //   return this._toFind;
  // }

  // set toFind (value: string){
  //   this._toFind = value;
  // }
}
