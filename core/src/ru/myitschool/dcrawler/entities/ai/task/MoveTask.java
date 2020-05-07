package ru.myitschool.dcrawler.entities.ai.task;

import ru.myitschool.dcrawler.entities.ai.pathfinding.NodePath;
import ru.myitschool.dcrawler.dungeon.DungeonMap;

/**
 * Created by Voyager on 12.02.2018.
 */
public class MoveTask extends Task {

    private NodePath path;

    public MoveTask(NodePath path) {
        this(path, false, true);
    }

    public MoveTask(NodePath path, boolean cut, boolean withLast) {
        if (cut && path.getCost() > entity.getSpeed() * 2 + 1) {
            path.cut(entity.getSpeed() * 2 + 1);
        } else if (!withLast){
            path.cutLast();
        }
        this.path = path;
    }

    @Override
    protected void startTask() {
        entity.setPath(path);
        entity.setMovement();
        DungeonMap.updateEntityPos(entity);
    }

    @Override
    public void endMove() {
        super.endMove();
        complete();
    }
}
