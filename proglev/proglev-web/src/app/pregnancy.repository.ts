import {Injectable} from '@angular/core';
import {Pregnancy} from "./pregnancy";

const PREGNANCIES: Pregnancy[] = [
  {
    id: 0,
    patientFirstName: 'Jane',
    patientLastName: 'Doe',
    email: 'jane.doe@example.com',
    lastPeriodDate: new Date('2016-10-21'),
    measurements: [{
      measurementDate: new Date('2016-12-21'),
      progesteroneLevel: 53.2,
      notes: 'test'
    }]
  },
  {
    id: 1,
    patientFirstName: 'Emma',
    patientLastName: 'Smith',
    email: 'emma.smith@example.com',
    lastPeriodDate: new Date('2016-11-21'),
    measurements: []
  }
];

@Injectable()
export class PregnancyRepository {
  getAll(): Promise<Pregnancy[]> {
    return Promise.resolve(PREGNANCIES);
  }

  getById(id: number): Promise<Pregnancy> {
    let pregnancy = PREGNANCIES[id];
    console.log("repository.getById: " + pregnancy);
    return Promise.resolve(pregnancy);
  }
}
