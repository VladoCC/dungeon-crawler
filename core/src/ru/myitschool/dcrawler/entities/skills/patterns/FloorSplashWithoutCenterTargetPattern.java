package ru.myitschool.dcrawler.entities.skills.patterns;

import ru.myitschool.dcrawler.entities.skills.Skill;
import ru.myitschool.dcrawler.entities.skills.Target;

public class FloorSplashWithoutCenterTargetPattern extends TargetPattern {
    public FloorSplashWithoutCenterTargetPattern(Skill skill) {
        super(skill);
    }

    @Override
    public Target createTarget(Target target) {
        int range = getSkill().getRange();
        int cellX = target.getX();
        int cellY = target.getY();
        Target main = new Target(cellX - range + 1, cellY - range + 1);
        main.setCheckCoords(cellX, cellY);
        for (int i = 0; i < 2 * (range - 1) + 1; i++) {
            for (int j = 0; j < 2 * (range - 1) + 1; j++) {
                if (!(cellX - range + 1 + i == cellX && cellY - range + 1 + j == cellY) && !(i == 0 && j == 0)){
                    Target linked = new Target(cellX - range + 1 + i,cellY - range + 1 + j);
                    linked.setMain(main);
                    linked.setLinked(true);
                    main.addLinkedTarget(linked);
                }
            }
        }
        return main;
    }
}
