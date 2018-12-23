export enum ScenarioState {
    Stopped = "stopped",
    Starting = "starting",
    Running = "running",
    Stopping = "stopping",
    Failed = "failed"
}

export interface Scenario {
    id: string;
    name: string;
    config: string;
    state: ScenarioState;
}