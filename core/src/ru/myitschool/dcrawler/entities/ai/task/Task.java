package ru.myitschool.dcrawler.entities.ai.task;

import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.event.EntityEventAdapter;

/**
 * Created by Voyager on 12.02.2018.
 */
public abstract class Task extends EntityEventAdapter {

    protected Entity entity;
    private boolean started = false;
    private boolean complete = false;

    protected void complete(){
        complete = true;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isStarted() {
        return started;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public void start(){
        started = true;
        startTask();
    }

    protected abstract void startTask();
}
