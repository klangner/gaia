import { NgModule } from '@angular/core';

import { 
  MdcIconModule,
  MdcTopAppBarModule,
  MdcButtonModule,
  MdcIconButtonModule
} from '@angular-mdc/web';

import {
  MatTableModule,
} from '@angular/material';

@NgModule({
  imports: [
    MdcIconModule,
    MdcTopAppBarModule,
    MdcButtonModule,
    MdcIconButtonModule,
    MatTableModule,
  ],
  exports: [
    MdcIconModule,
    MdcButtonModule,
    MdcTopAppBarModule,
    MdcIconButtonModule,
    MatTableModule,
  ]
})
export class MaterialModule {}