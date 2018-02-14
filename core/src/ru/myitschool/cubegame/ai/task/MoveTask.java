package ru.myitschool.cubegame.ai.task;

import ru.myitschool.cubegame.ai.pathfinding.GraphStorage;
import ru.myitschool.cubegame.ai.pathfinding.NodePath;
import ru.myitschool.cubegame.ai.pathfinding.Pathfinder;
import ru.myitschool.cubegame.dungeon.DungeonMap;
import ru.myitschool.cubegame.entities.Entity;

/**
 * Created by Voyager on 12.02.2018.
 */
public class MoveTask extends Task {

    private NodePath path;

    public MoveTask(Entity entity, int targetX, int targetY) {
        this(entity, Pathfinder.searchConnectionPath(GraphStorage.getNodeBottom(entity.getTileX(), entity.getTileY()), GraphStorage.getNodeBottom(targetX, targetY), true, 300, 5, false, true));
    }

    public MoveTask(Entity entity, NodePath path) {
        this(entity, path, false, true);
    }

    public MoveTask(Entity entity, NodePath path, boolean cut, boolean withLast) {
        super(entity);
        if (cut && path.getCost() > entity.getSpeed() * 2 + 1) {
            path.cut(entity.getSpeed() * 2 + 1);
        } else if (!withLast){
            path.cutLast();
        }
        this.path = path;
    }

    @Override
    void startTask() {
        entity.setPath(path);
        DungeonMap.moveChar();
    }

    @Override
    public void endMove() {
        super.endMove();
        complete();
    }
}
