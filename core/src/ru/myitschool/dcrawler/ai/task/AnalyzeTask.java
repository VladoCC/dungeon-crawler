package ru.myitschool.dcrawler.ai.task;

import ru.myitschool.dcrawler.ai.AI;
import ru.myitschool.dcrawler.entities.Enemy;
import ru.myitschool.dcrawler.entities.Entity;

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
