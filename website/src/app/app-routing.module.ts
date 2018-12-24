import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ScenariosComponent } from './scenarios/scenarios.component';
import { ScenarioEditorComponent } from './scenario-editor/scenario-editor.component';


const routes: Routes = [
  { path: '', redirectTo: '/scenarios', pathMatch: 'full' },
  { path: 'scenarios', component: ScenariosComponent },
  { path: 'scenarios/:id', component: ScenarioEditorComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
