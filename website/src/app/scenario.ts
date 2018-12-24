export enum ScenarioState {
    Stopped = "stopped",
    Starting = "starting",
    Running = "running",
    Stopping = "stopping",
    Errors = "errors"
}

export interface Scenario {
    id: string;
    name: string;
    config: string;
    state: ScenarioState;
}