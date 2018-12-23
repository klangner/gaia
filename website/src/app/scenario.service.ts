import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';

import { Scenario, ScenarioState } from './scenario';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const MOCKUP_SCENARIOS: Scenario[] = [
    {"id": "1", "name": "Image resize test", "config":"", "state": ScenarioState.Stopped},
    {"id": "2", "name": "Archiver create vod", "config":"", "state": ScenarioState.Running},
    {"id": "3", "name": "Provision video stream", "config":"", "state": ScenarioState.Failed}
];

@Injectable({ providedIn: 'root' })
export class ScenarioService {

  private heroesUrl = 'api/scenarios';  

  constructor(private http: HttpClient) { }

  /** GET heroes from the server */
  fetchScenarios (): Observable<Scenario[]> {
    return of(MOCKUP_SCENARIOS);
  }
}