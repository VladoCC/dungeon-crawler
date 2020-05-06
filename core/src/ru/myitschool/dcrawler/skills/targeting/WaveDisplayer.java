package ru.myitschool.dcrawler.skills.targeting;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.AIUtils;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.utils.AdvancedArray;

/**
 * Created by Voyager on 12.03.2018.
 */
public class WaveDisplayer implements TargetDisplayer {

    @Override
    public boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill) {
        int range = skill.getRange();
        AdvancedArray<Vector2> poses = AIUtils.getCellRaytrace(skill.getDoer().getTileX(), skill.getDoer().getTileY(), x, y, range - 1);
        poses.clip(poses.size - range, poses.size - 1);
        for (Vector2 pos : poses){
            array.add(new TilePos((int) pos.x, (int) pos.y, TargetRenderer.getDefaultTargetTile()));
        }
        return true;
    }
}
