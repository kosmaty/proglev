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

import {PregnancyRepository} from "./pregnancy.repository";

@NgModule({
  declarations: [
    AppComponent,
    PregnanciesListComponent,
    PregnancyDetailsComponent,
    AddPregnancyComponent,
    AddMeasurementComponent
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
      {path: 'pregnancy/:pregnancyId/measurement/add', component: AddMeasurementComponent}
    ])
  ],
  providers: [PregnancyRepository],
  bootstrap: [AppComponent]
})
export class AppModule {
}
