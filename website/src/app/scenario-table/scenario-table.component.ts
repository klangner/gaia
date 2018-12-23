import { Component, OnInit, ViewChild } from '@angular/core';
import { ScenarioTableDataSource } from './scenario-table-datasource';

@Component({
  selector: 'scenario-table',
  templateUrl: './scenario-table.component.html',
  styleUrls: ['./scenario-table.component.css']
})
export class ScenarioTableComponent implements OnInit {
  dataSource: ScenarioTableDataSource;

  /** Columns displayed in the table. Columns IDs can be added, removed, or reordered. */
  displayedColumns = ['id', 'name'];

  ngOnInit() {
    this.dataSource = new ScenarioTableDataSource();
  }
}
