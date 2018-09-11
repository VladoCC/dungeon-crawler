package ru.myitschool.dcrawler.skills.targeting;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.dungeon.DungeonMap;
import ru.myitschool.dcrawler.skills.Skill;

/**
 * Created by Voyager on 28.02.2018.
 */
public class EntityDisplayer implements TargetDisplayer {

    @Override
    public boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill) {
        if (DungeonMap.getCell(x, y) != null && DungeonMap.getCell(x, y).getEntity() != null){
            array.add(new TilePos(x, y, TargetRenderer.getDefaultTargetTile()));
            return true;
        }
        return false;
    }
}
