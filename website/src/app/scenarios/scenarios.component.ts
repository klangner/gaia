import { Component, OnInit } from '@angular/core';
import { Scenario } from '../scenario'
import { ScenarioService } from '../scenario.service';
import { AmplifyService } from 'aws-amplify-angular';


@Component({
  selector: 'app-scenarios',
  templateUrl: './scenarios.component.html',
  styleUrls: ['./scenarios.component.css']
})
export class ScenariosComponent implements OnInit {

  displayedColumns = ['name', 'state', 'action'];
  dataSource: Scenario[];
  signedIn: boolean;
  user: any;
  greeting: string;

  constructor(
    private scenarioService: ScenarioService, 
    private amplifyService: AmplifyService) 
  {

    this.amplifyService.authStateChange$
        .subscribe(authState => {
            this.signedIn = authState.state === 'signedIn';
            if (!authState.user) {
                this.user = null;
            } else {
                this.user = authState.user;
                this.greeting = "Hello " + this.user.username;
            }
    });
  }

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
