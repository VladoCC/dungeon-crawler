package ru.myitschool.dcrawler.entities.skills.targeting;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.entities.skills.Skill;

/**
 * Created by Voyager on 13.03.2018.
 */
public class SwingDisplayer implements TargetDisplayer {

    @Override
    public boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill) {
        array.add(new TilePos(x, y, TargetRenderer.getDefaultTargetTile()));
        int range = skill.getRange();
        if (x == skill.getDoer().getTileX() && y < skill.getDoer().getTileY()){
            for (int i = 0; i < range; i++) {
                array.add(new TilePos(x - 1 - i, y, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x - 1 - i, y + 1, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x + 1 + i, y, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x + 1 + i, y + 1, TargetRenderer.getDefaultTargetTile()));
            }
            for (int i = 1; i < range; i++) {
                for (int j = -range; j < range + 1; j++) {
                    array.add(new TilePos(x + j, y - i, TargetRenderer.getDefaultTargetTile()));
                }
            }
        } else if (x == skill.getDoer().getTileX() && y > skill.getDoer().getTileY()){
            for (int i = 0; i < range; i++) {
                array.add(new TilePos(x - 1 - i, y, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x - 1 - i, y - 1, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x + 1 + i, y, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x + 1 + i, y - 1, TargetRenderer.getDefaultTargetTile()));
            }
            for (int i = 1; i < range; i++) {
                for (int j = -range; j < range + 1; j++) {
                    array.add(new TilePos(x + j, y + i, TargetRenderer.getDefaultTargetTile()));
                }
            }
        } else if (x > skill.getDoer().getTileX() && y == skill.getDoer().getTileY()){
            for (int i = 0; i < range; i++) {
                array.add(new TilePos(x, y - 1 - i, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x - 1, y - 1 - i, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x, y + 1 + i, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x - 1, y + 1 + i, TargetRenderer.getDefaultTargetTile()));
            }
            for (int i = 1; i < range; i++) {
                for (int j = -range; j < range + 1; j++) {
                    array.add(new TilePos(x + i, y + j, TargetRenderer.getDefaultTargetTile()));
                }
            }
        } else if (x < skill.getDoer().getTileX() && y == skill.getDoer().getTileY()){
            for (int i = 0; i < range; i++) {
                array.add(new TilePos(x, y - 1 - i, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x + 1, y - 1 - i, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x, y + 1 + i, TargetRenderer.getDefaultTargetTile()));
                array.add(new TilePos(x + 1, y + 1 + i, TargetRenderer.getDefaultTargetTile()));
            }
            for (int i = 1; i < range; i++) {
                for (int j = -range; j < range + 1; j++) {
                    array.add(new TilePos(x - i, y + j, TargetRenderer.getDefaultTargetTile()));
                }
            }
        }
        return true;
    }
}
