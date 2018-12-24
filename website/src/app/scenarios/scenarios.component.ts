import { Component, OnInit } from '@angular/core';
import { Scenario } from '../scenario'
import { ScenarioService } from '../scenario.service';


@Component({
  selector: 'app-scenarios',
  templateUrl: './scenarios.component.html',
  styleUrls: ['./scenarios.component.css']
})
export class ScenariosComponent implements OnInit {

  displayedColumns = ['name', 'state', 'action'];
  dataSource: Scenario[];

  constructor(private scenarioService: ScenarioService) { }

  ngOnInit() {
    this.loadScenarios();
  }

  loadScenarios(): void {
    this.scenarioService
      .fetchScenarios()
      .subscribe(scenarios => this.dataSource = scenarios);
  }

  start(scenario: Scenario) {
    console.log("Start: " + scenario.name);
  }

  stop(scenario: Scenario) {
    console.log("Stop: " + scenario.id);
  }

}
