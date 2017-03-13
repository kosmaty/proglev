import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {RouterModule}   from '@angular/router';

import {AppComponent} from './app.component';
import {PregnanciesListComponent} from './pregnancies-list.component';
import {PregnancyDetailsComponent} from './pregnancy-details.component';
import {AddPregnancyComponent} from './add-pregnancy.component';
import {AddMeasurementComponent} from './add-measurement.component';
import {SearchBox} from "./search-box.component"
import {SearchPregnancyPipe} from "./search-pregnancy.pipe"


import { PregnancyRepository } from "./pregnancy.repository";
import { DeletePregnancyComponent } from "./delete-pregnancy.component";

@NgModule({
  declarations: [
    AppComponent,
    PregnanciesListComponent,
    PregnancyDetailsComponent,
    AddPregnancyComponent,
    AddMeasurementComponent,
    SearchBox,
    SearchPregnancyPipe,
    DeletePregnancyComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    RouterModule.forRoot([
      {path: '', component: PregnanciesListComponent},
      {path: 'pregnancy/:id', component: PregnancyDetailsComponent},
      {path: 'pregnancy/:id/edit', component: AddPregnancyComponent},
      {path: 'add-pregnancy', component: AddPregnancyComponent},
      {path: 'pregnancy/:pregnancyId/measurement/add', component: AddMeasurementComponent},
      {path: 'pregnancy/:pregnancyId/measurement/:measurementId', component: AddMeasurementComponent},
      {path: 'pregnancy/:id/delete', component: DeletePregnancyComponent}
    ],{ useHash: true })
  ],
  providers: [PregnancyRepository],
  bootstrap: [AppComponent]
})
export class AppModule {
}
