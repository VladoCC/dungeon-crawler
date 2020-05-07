package ru.myitschool.dcrawler.entities.skills.patterns;

import ru.myitschool.dcrawler.entities.skills.Skill;
import ru.myitschool.dcrawler.entities.skills.Target;

public class FloorSingleTargetPattern extends TargetPattern {
    public FloorSingleTargetPattern(Skill skill) {
        super(skill);
    }

    @Override
    public Target createTarget(Target target) {
        return target;
    }
}
