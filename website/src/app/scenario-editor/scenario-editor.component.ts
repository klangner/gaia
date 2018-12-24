import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-scenario-editor',
  templateUrl: './scenario-editor.component.html',
  styleUrls: ['./scenario-editor.component.css']
})
export class ScenarioEditorComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit() {
  }

  save() {
    this.router.navigate(["scenarios"]);
  }
}
