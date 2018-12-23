import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ScenariosComponent } from './scenarios/scenarios.component'

const routes: Routes = [
  { path: '', redirectTo: '/scenarios', pathMatch: 'full' },
  { path: 'scenarios', component: ScenariosComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
