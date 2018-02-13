package ru.myitschool.cubegame.ai;

import ru.myitschool.cubegame.entities.Enemy;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.entities.enemies.GoblinWarrior;
import ru.myitschool.cubegame.screens.DungeonScreen;

/**
 * Created by Voyager on 11.12.2017.
 */
public class DummyAI extends AI {

    public DummyAI(Entity controlledEntity) {
        super(controlledEntity);
    }

    @Override
    public void aiAnalyze() {

    }
}
