package ru.myitschool.dcrawler.skills.patterns;

import com.badlogic.gdx.math.Vector2;
import ru.myitschool.dcrawler.ai.AITweaks;
import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;
import ru.myitschool.dcrawler.utils.AdvancedArray;

public class FloorWaveTargetPattern extends TargetPattern {
    public FloorWaveTargetPattern(Skill skill) {
        super(skill);
    }

    @Override
    public Target createTarget(Target target) {
        int range = getSkill().getRange();
        int cellX = target.getX();
        int cellY = target.getY();
        int charX = getSkill().getDoer().getTileX();
        int charY = getSkill().getDoer().getTileY();
        AdvancedArray<Vector2> array = AITweaks.getCellRaytrace(charX, charY, cellX, cellY, range - 1);
        array.clip(array.size - range + 1, array.size - 1);
        for (Vector2 pos : array){
            Target linked = new Target((int) pos.x, (int) pos.y);
            linked.setMain(target);
            linked.setLinked(true);
            target.addLinkedTarget(linked);
        }
        return target;
    }
}
