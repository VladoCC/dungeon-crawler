package ru.myitschool.dcrawler.skills.targeting;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import ru.myitschool.dcrawler.ai.AIUtils;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.utils.AdvancedArray;

/**
 * Created by Voyager on 14.03.2018.
 */
public class RaytraceDisplayer implements TargetDisplayer {

    @Override
    public boolean targetCreation(int x, int y, Array<TilePos> array, Skill skill) {
        if (Math.abs(skill.getDoer().getTileX() - x) + Math.abs(skill.getDoer().getTileY() - y) > 1) {
            boolean obstruct = skill.isObstruct();
            AdvancedArray<Vector2> cells = AIUtils.getCellRaytrace(skill.getDoer().getTileX(), skill.getDoer().getTileY(), x, y, 0);
            cells.clip(1, cells.size - 2);
            int max = cells.size;
            Array<Integer> poses = AIUtils.getObstructorIndexes(cells);
            System.out.println("X: " + x + " Y: " + y);
            if (obstruct) {
                if (poses.size > 0) {
                    max = poses.get(0);
                }
            }
            for (int i = 0; i < max; i++) {
                Vector2 cell = cells.get(i);
                array.add(new TilePos((int) cell.x, (int) cell.y, TargetRenderer.getDefaultLineTile()));
            }
            if (poses.size > 0 && obstruct) {
                Vector2 cell = cells.get(poses.get(0));
                array.add(new TilePos((int) cell.x, (int) cell.y, TargetRenderer.getDefaultObstructionTile()));
                return false;
            }
        }
        return true;
    }
}
