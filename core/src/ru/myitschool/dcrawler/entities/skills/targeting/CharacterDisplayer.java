package ru.myitschool.dcrawler.entities.skills.targeting;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.dungeon.DungeonMap;
import ru.myitschool.dcrawler.entities.skills.Skill;

/**
 * Created by Voyager on 08.03.2018.
 */
public class CharacterDisplayer implements TargetDisplayer {

    @Override
    public boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill) {
        if (DungeonMap.getCell(x, y) != null && DungeonMap.getCell(x, y).getEntity() != null && DungeonMap.getCell(x, y).getEntity().isCharacter()){
            array.add(new TilePos(x, y, TargetRenderer.getDefaultTargetTile()));
            return true;
        }
        return false;
    }
}
