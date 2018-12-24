import { NgModule } from '@angular/core';

import { 
  MdcIconModule,
  MdcTopAppBarModule,
  MdcButtonModule,
  MdcIconButtonModule,
  MdcFormFieldModule,
  MdcTextFieldModule,
  MdcTypographyModule
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
    MdcFormFieldModule,
    MdcTextFieldModule,
    MdcTypographyModule
  ],
  exports: [
    MdcIconModule,
    MdcButtonModule,
    MdcTopAppBarModule,
    MdcIconButtonModule,
    MatTableModule,
    MdcFormFieldModule,
    MdcTextFieldModule,
    MdcTypographyModule
  ]
})
export class MaterialModule {}