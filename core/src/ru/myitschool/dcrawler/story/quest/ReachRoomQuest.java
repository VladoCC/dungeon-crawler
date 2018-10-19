package ru.myitschool.dcrawler.story.quest;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.dungeon.Room;
import ru.myitschool.dcrawler.effects.CellEffect;
import ru.myitschool.dcrawler.effects.FloorEffect;
import ru.myitschool.dcrawler.entities.Entity;
import ru.myitschool.dcrawler.skills.Target;
import ru.myitschool.dcrawler.tiles.DungeonTile;

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
            public Texture getIcon() {
                return null;
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public boolean isSkillUse() {
                return false;
            }

            @Override
            public boolean isPositive() {
                return false;
            }

            @Override
            public boolean isStackable() {
                return false;
            }

            @Override
            public int getStackSize() {
                return 0;
            }

            @Override
            public boolean isExpiring() {
                return false;
            }

            @Override
            public int getExpireTurns() {
                return 0;
            }

            @Override
            public String getType() {
                return null;
            }

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