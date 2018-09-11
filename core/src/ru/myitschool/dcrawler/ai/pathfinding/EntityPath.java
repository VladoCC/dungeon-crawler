package ru.myitschool.dcrawler.ai.pathfinding;

import ru.myitschool.dcrawler.entities.Entity;

/**
 * Created by Voyager on 13.04.2018.
 */
public class EntityPath {

    private Entity entity;
    private NodePath path;
    private boolean bottom;

    public EntityPath(Entity entity, NodePath path, boolean bottom) {
        this.entity = entity;
        this.path = path;
        this.bottom = bottom;
    }

    public Entity getEntity() {
        return entity;
    }

    public NodePath getPath() {
        return path;
    }

    public boolean isBottom() {
        return bottom;
    }
}
