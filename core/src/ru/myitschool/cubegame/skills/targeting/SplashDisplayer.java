package ru.myitschool.cubegame.skills.targeting;

import com.badlogic.gdx.utils.Array;
import ru.myitschool.cubegame.skills.Skill;

/**
 * Created by Voyager on 08.03.2018.
 */
public class SplashDisplayer implements TargetDisplayer {

    private boolean center = true;

    public SplashDisplayer(boolean center) {
        this.center = center;
    }

    @Override
    public boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill) {
        int range = skill.getRange();
        for (int i = -range + 1; i < range; i++) {
            for (int j = -range + 1; j < range; j++) {
                if (!(i == 0 && j == 0 && !center)) {
                    array.add(new TilePos(x + i, y + j, TargetRenderer.getDefaultTargetTile()));
                }
            }
        }
        return true;
    }
}
