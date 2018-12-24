import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-scenario-editor',
  templateUrl: './scenario-editor.component.html',
  styleUrls: ['./scenario-editor.component.css']
})
export class ScenarioEditorComponent implements OnInit {

  constructor(private location: Location) { }

  ngOnInit() {
  }

  goScenarios() {
    this.location.go("/subscriptions");
  }
}
