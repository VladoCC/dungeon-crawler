package ru.myitschool.dcrawler.skills.patterns;

import ru.myitschool.dcrawler.skills.Skill;
import ru.myitschool.dcrawler.skills.Target;

public class FloorSplashTargetPattern extends TargetPattern {
    public FloorSplashTargetPattern(Skill skill) {
        super(skill);
    }

    @Override
    public Target createTarget(Target target) {
        int range = getSkill().getRange();
        int cellX = target.getX();
        int cellY = target.getY();
        for (int i = 0; i < 2 * (range - 1) + 1; i++) {
            for (int j = 0; j < 2 * (range - 1) + 1; j++) {
                if (!(cellX - range + 1 + i == cellX && cellY - range + 1 + j == cellY)){
                    Target linked = new Target(cellX - range + 1 + i,cellY - range + 1 + j);
                    linked.setMain(target);
                    linked.setLinked(true);
                    target.addLinkedTarget(linked);
                }
            }
        }
        return target;
    }
}
