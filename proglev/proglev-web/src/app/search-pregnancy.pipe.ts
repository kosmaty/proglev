import { Pipe } from '@angular/core';
import { Pregnancy } from "./pregnancy";

@Pipe({
    name: "search"
})
export class SearchPregnancyPipe {
    transform(value: Pregnancy[], toFind: string) {
        if (value && toFind) {
            return value.filter(item => {
            return item.patientFirstName.toLocaleLowerCase().includes(toFind.toLocaleLowerCase())
            || item.patientLastName.toLocaleLowerCase().includes(toFind.toLocaleLowerCase())
            || item.email.toLocaleLowerCase().includes(toFind.toLocaleLowerCase())
        });
        } else {
            return value;
        }
    }
}