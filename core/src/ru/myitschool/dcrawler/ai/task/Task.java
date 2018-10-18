package ru.myitschool.dcrawler.ai.task;

import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.event.EntityEventAdapter;

/**
 * Created by Voyager on 12.02.2018.
 */
public abstract class Task extends EntityEventAdapter {

    protected Entity entity;
    private boolean started = false;
    private boolean complete = false;

    public Task(Entity entity) {
        this.entity = entity;
    }

    protected void complete(){
        complete = true;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isStarted() {
        return started;
    }

    public void activate(){
        started = true;
        startTask();
    }

    abstract void startTask();
}