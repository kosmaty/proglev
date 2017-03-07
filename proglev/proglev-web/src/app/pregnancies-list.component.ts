import { Component } from '@angular/core';

export class Pregnancy {
  id: number;
  patientFirstName: string;
  patientLastName: string;
  email: string;
  lastPeriodDate: Date;
}

const PREGNANCIES: Pregnancy[] = [
  {id: 1, patientFirstName: 'Jane', patientLastName: 'Doe', email: 'jane.doe@example.com', lastPeriodDate: new Date('2016-10-21')},
  {id: 2, patientFirstName: 'Emma', patientLastName: 'Smith', email: 'emma.smith@example.com', lastPeriodDate: new Date('2016-11-21')}
];

@Component({
  selector: 'pregnancies-list',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})


export class AppComponent {
  pregnancies = PREGNANCIES;
}
