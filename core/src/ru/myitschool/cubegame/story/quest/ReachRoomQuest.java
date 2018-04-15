package ru.myitschool.cubegame.story.quest;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.Room;
import ru.myitschool.cubegame.effects.CellEffect;
import ru.myitschool.cubegame.effects.FloorEffect;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.skills.Target;
import ru.myitschool.cubegame.tiles.DungeonTile;

/**
 * Created by Voyager on 08.04.2018.
 */
public class ReachRoomQuest extends Quest {

    public ReachRoomQuest(int roomPos, int deltaPos, int roomsMax) {
        super(roomPos, deltaPos, roomsMax);
    }

    @Override
    public Room modifyRoom(Room room) {
        Array<Target> targets = new Array<>();
        for (int i = 0; i < room.getWidth(); i++) {
            for (int j = 0; j < room.getHeight(); j++) {
                if (room.getCell(i, j) != null && DungeonTile.getTile(room.getCell(i, j)).isReachable()){
                    targets.add(new Target(i, j));
                }
            }
        }

        FloorEffect effect = null;
        CellEffect cellEffect = new CellEffect(effect){
            @Override
            protected void onStepTo(Entity entity) {
                super.onStepTo(entity);
                targetReached();
            }
        };
        effect = new FloorEffect(targets, cellEffect, getExitColor(), true, false);

        room.addEffect(effect);
        return room;
    }

    @Override
    public void exitRoomOpened() {

    }

    @Override
    public void targetReached() {
        complete();
    }
}