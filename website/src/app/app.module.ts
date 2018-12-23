import { BrowserModule }            from '@angular/platform-browser';
import { NgModule }                 from '@angular/core';
import { FormsModule }              from '@angular/forms';
import { LayoutModule }             from '@angular/cdk/layout';
import { MaterialModule }           from './material.module';
import { BrowserAnimationsModule }  from '@angular/platform-browser/animations';
import { HttpClientModule }         from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MainNavComponent } from './main-nav/main-nav.component';
import { ScenarioTableComponent } from './scenario-table/scenario-table.component';
import { ScenariosComponent } from './scenarios/scenarios.component';


@NgModule({
  declarations: [
    AppComponent,
    MainNavComponent,
    ScenarioTableComponent,
    ScenariosComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    LayoutModule,
    MaterialModule,
    BrowserAnimationsModule,
    HttpClientModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
