package ru.myitschool.cubegame.story.quest;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.dungeon.DungeonCell;
import ru.myitschool.cubegame.dungeon.Room;
import ru.myitschool.cubegame.effects.CellEffect;
import ru.myitschool.cubegame.effects.FloorEffect;
import ru.myitschool.cubegame.entities.Character;
import ru.myitschool.cubegame.entities.Entity;
import ru.myitschool.cubegame.skills.Target;
import ru.myitschool.cubegame.tiles.DungeonTile;

/**
 * Created by Voyager on 09.04.2018.
 */
public class StayHereQuest extends Quest {
    public StayHereQuest(int roomPos, int deltaPos, int roomsMax) {
        super(roomPos, deltaPos, roomsMax);
    }

    @Override
    public Room modifyRoom(Room room) {
        Array<Target> targets = room.getCells(i -> DungeonTile.getTile(i).isReachable());

        FloorEffect effect = null;

        CellEffect cellEffect = new CellEffect(effect){
            @Override
            protected void onStepTo(Entity entity) {
                super.onStepTo(entity);
                int chars = Character.getChars().size;
                int inZone = 0;
                for (DungeonCell cell : this.getFloorEffect().getCells()){
                    if (cell.hasEntity() && cell.getEntity().isPlayer()){
                        inZone++;
                    }
                }
                if (inZone >= chars){
                    complete();
                }
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

    }
}
