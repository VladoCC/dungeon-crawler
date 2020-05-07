package ru.myitschool.dcrawler.entities.ai.task;

import ru.myitschool.dcrawler.entities.ai.AI;

public abstract class AnalyzeTask extends Task {

    private AI ai;

    public AnalyzeTask(AI ai) {
        this.ai = ai;
    }

    @Override
    protected void startTask() {
        analyze();
        complete();
    }

    protected abstract void analyze();
}
