export class Pregnancy {
  id: number;
  patientFirstName: string;
  patientLastName: string;
  email: string;
  lastPeriodDate: Date;
  measurements: ProgesteroneLevelMeasurement[] = [];
}

export class ProgesteroneLevelMeasurement {
  progesteroneLevel: number;
  measurementDate: Date;
  notes: string;
}
