import { Component, OnInit, ViewChild } from '@angular/core';
import { Scenario } from '../scenario'
import { ScenarioService } from '../scenario.service';

@Component({
  selector: 'scenario-table',
  templateUrl: './scenario-table.component.html',
  styleUrls: ['./scenario-table.component.css']
})
export class ScenarioTableComponent implements OnInit {
  
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
}
